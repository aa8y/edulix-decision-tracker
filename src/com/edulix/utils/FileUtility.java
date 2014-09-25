package com.edulix.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtility {

	public ArrayList<String> readFile(String file) {
		
		ArrayList<String> linesRead = new ArrayList<String>();
		
		try {
			FileReader fReader = new FileReader(file);
		
			BufferedReader reader = new BufferedReader(fReader);
			String line = null;
			
			while (true) {
				line = reader.readLine();
				
				if (line == null) {
					fReader.close();
					reader.close();
					break;
				} else {
					linesRead.add(line.trim());
				}

			}
		} catch (FileNotFoundException e) {						// Handles File not found exception
			System.out.println("Properties file " + file + " not found!");
		} catch (IOException ie) {
			System.out.println("Unable to read properties file " + file); 
		} catch (NullPointerException ne) {
			System.out.println("Properties file not found!");
		}

		return linesRead;
	}
	
	
	/**
	 * Creates a file with the name and extension provided. If the replaceIfExist flag is set true it
	 * deletes and recreates the file if it already exists
	 * 
	 * @param name
	 * @param extension
	 * @param replaceIfExist
	 * @return true is file is created else false
	 */
	public boolean createFile(String name, String extension, boolean replaceIfExist) {
		String fileName = getFileName(name, extension);
		
		File file = new File(fileName);				// Defines a file of the specified file name
		boolean deletionPerformed = true;
		boolean fileCreated = true;
		
		try {
			fileCreated = file.createNewFile();		// Checks if the file already exists
			if (!fileCreated) {
				System.out.println("File " + fileName + " already exists.");
				
				if (replaceIfExist) {
					deletionPerformed = file.delete();
					
					if (deletionPerformed) {
						System.out.println("File " + fileName + " successfully deleted. Trying to recreate file.");
						fileCreated = file.createNewFile();
					} else {
						System.out.println("Unable to delete file " + fileName + ".");
					}
				}
			}	
			
			if (fileCreated) {
				System.out.println("File " + fileName + " created.");
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return fileCreated;
	}
	
	/**
	 * Returns the filename based on the name and extension passed
	 * 
	 * @param name
	 * @param extension
	 * @return
	 */
	public String getFileName(String name, String extension) {
		String sanitizedName = name.replaceAll("\\p{Punct}","").trim();
		sanitizedName = sanitizedName.replaceAll(" ", "_");
		
		if (extension != "" || extension != null) {
			return sanitizedName + "." + extension;
		}
		return sanitizedName;
	}
	
	/**
	 * Writes the provided data to the specified file. Recreates the file if it exists if the replaceIfExist
	 * flag is set true
	 * 
	 * @param name
	 * @param extension
	 * @param data
	 * @param replaceIfExist
	 * @return true if it is successful in writing the data else false
	 */
	public boolean writeToFile(String name, String extension, String data, boolean replaceIfExist) {
		
		String fileName = getFileName(name, extension);
		
		if (!createFile(name, extension, replaceIfExist)) {
			System.out.println("Unable to create file " + fileName);
		} else {
			
			try {
				FileWriter writer = new FileWriter(fileName);
				writer.write(data);
				
				writer.close();
			} catch (IOException ie) {
				System.out.println("Unable to write data to file: " + fileName);
			}
			
			if (new File(fileName).length() == 0) {
				System.out.println("File " + fileName + " is zero byte.");
				return false;
			}
			
			System.out.println("File " + fileName + " downloaded successfully.");
			return true;
			
		}
		return false;
	}
	
	/**
	 * Appends data to the end of file if it exists otherwise creates an entirely new file and writes data
	 * to it
	 * 
	 * @param name
	 * @param extension
	 * @param data
	 * @return
	 */
	public boolean appendToFile(String name, String extension, String data) {
		
		String fileName = getFileName(name, extension);
		
		if (createFile(name, extension, false)) {
			System.out.println(fileName + " does not exist. Creating file for the first time.");
		}
		
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.append(data + "\n");
			
			writer.close();
		} catch (IOException ie) {
			System.out.println("Unable to append data to file: " + fileName);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Appends data to the end of file if it exists otherwise creates an entirely new file and writes data
	 * to it
	 * 
	 * @param name
	 * @param extension
	 * @param data
	 * @param delim
	 * @return
	 */
	/*public boolean appendToFile(String location, String name, String extension, String[] data, String delim) {
		
		String fileName = getFileName(name, extension);
		String separator = System.getProperty("line.separator");
		
		if (createFile(name, extension, false)) {
			System.out.println(fileName + " does not exist. Creating file for the first time.");
		}
		
		try {
			FileWriter writer = new FileWriter(location + fileName, true);
			
			for (int i = 0; i < data.length; i++) {
				if (ForumUtility.isValidData(data[i])) {
					writer.append(data[i] + delim);
				}
			}
			
			writer.append(separator);
			
			writer.close();
		} catch (IOException ie) {
			System.out.println("Unable to append data to file: " + fileName);
			return false;
		}
		
		return true;
	}*/
	
	/**
	 * Deletes the file with the file name and extension provided
	 * 
	 * @param name
	 * @param extension
	 * @return true if it is able to delete, else false
	 */
	public boolean deleteFile(String name, String extension) {
		String fileName = getFileName(name, extension);
		
		File file = new File(fileName);			// Defines a file of the specified file name
		boolean deletionPerformed = true;
		boolean exist = true;

		System.out.print("Attempting to delete. ");
		exist = file.exists();					// Checks if the file already exists
		if (exist) {
			System.out.println("File " + fileName + " found.");
			deletionPerformed = file.delete();
			
			if (deletionPerformed) {
				System.out.println("File " + fileName + " successfully deleted.");
			} else {
				System.out.println("File " + fileName + " could not be deleted.");
			}
		} else {
			System.out.println("File " + fileName + " does not exist.");
		}
		
		return exist;
	}
	
	/**
	 * Deletes the file with the file name provided
	 * 
	 * @param fileName
	 * @return true if it is able to delete, else false
	 */
	public boolean deleteFile(String fileName) {
		
		File file = new File(fileName);			// Defines a file of the specified file name
		boolean deletionPerformed = true;
		boolean exist = true;

		System.out.print("Attempting to delete. ");
		exist = file.exists();					// Checks if the file already exists
		if (exist) {
			System.out.println("File " + fileName + " found.");
			deletionPerformed = file.delete();
			
			if (deletionPerformed) {
				System.out.println("File " + fileName + " successfully deleted.");
			} else {
				System.out.println("File " + fileName + " could not be deleted.");
			}
		} else {
			System.out.println("File " + fileName + " does not exist.");
		}
		
		return exist;
	}
	
	/**
	 * Lists the files present in the directory whose path is provided as a parameter
	 * 
	 * @param path
	 * @return ArrayList of filenames
	 */
	public ArrayList<String> listFiles(String path) {
		
		ArrayList<String> fileList = new ArrayList<String>();
		File file = new File(path);
		File[] files = file.listFiles();
		
		if (files != null) {
	        for (int i = 0; i < files.length; i++) {
	        	fileList.add(files[i].toString());
	        }
	    }
		
		return fileList;
	}
	
	/**
	 * Removes any extension from the file name passed as a parameter
	 * 
	 * @param fileName
	 * @return
	 */
	public String removeExtension(String fileName) {
		
		if (fileName.contains(".")) {
			int dotIndex = fileName.indexOf(".");
			return fileName.substring(0, dotIndex).trim();
		} else {
			System.out.println(fileName + " does not have any extension.");
		}
		
		return fileName;
	}
	
	/**
	 * Removes the extension provided from the file name passed as a parameter
	 * 
	 * @param fileName
	 * @param extension
	 * @return
	 */
	public String removeExtension(String fileName, String extension) {
		
		if (fileName.contains(".")) {
			int dotIndex = fileName.lastIndexOf("." + extension);
			return fileName.substring(0, dotIndex).trim();
		} else {
			System.out.println(fileName + " does not have the extension: " + extension);
		}
		
		return fileName;
	}
	
	/**
	 * Formats the fileList by removing the .done, .undone or any other extensions present in the files of
	 * the list.
	 * 
	 * @param fileList
	 * @return Formatted file list
	 */
	public ArrayList<String> formatFileList(ArrayList<String> fileList) {
		
		for (int i = 0; i < fileList.size(); i++) {
			
			String file = fileList.get(i);
			if (file.contains("\\")) {						// Checks for the end of the directory path
				int index = file.lastIndexOf("\\") + 1;		// Need to escape the backslash
				file = file.substring(index);
			}
			file = removeExtension(file);
			fileList.remove(i);
			fileList.add(i, file);
		}
		
		return fileList;
	}
	
}

package com.edulix.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.edulix.constants.Dates;
import com.edulix.constants.GRE;
import com.edulix.dao.DataCollector;
import com.edulix.model.Decision;

public class DataProcessor {
	
	private static Matcher patternMatcher;
	
	public static String getDataBetweenTags(String data) {
		
		int beginIndex = data.indexOf(">") + 1;
		int endIndex = data.indexOf("<", beginIndex);
		
		return data.substring(beginIndex, endIndex).trim();
		
	}
	
	/**
	 * Extract data from the thread.
	 * @param thread
	 * @param threadTitle
	 */
	public static void extractDataFromThread(int threadID, Document thread, String threadTitle) {
		
		ArrayList<String> data = new ArrayList<String>();
		FileUtility fileUtility = new FileUtility();
		
		/* Check if the data between the <div> tags is valid */
		Elements divisions = thread.getElementsByTag("div");
		for (int i = 0; i < divisions.size(); i++) {
			patternMatcher = Regex.decisionPattern.matcher(divisions.get(i).toString());
			if (patternMatcher.matches()) {
				data.add(divisions.get(i).toString());
			}
		}
		
		int beginIndex = 0;
		int endIndex = 0;
		String threadString = thread.toString();
		String divBegin = "<div id=\"pid_";
		
		while (true) {
			beginIndex = threadString.indexOf(divBegin, endIndex);
			
			if (beginIndex == -1) {
				break;
			}
			
			endIndex = threadString.indexOf("</div>", beginIndex);
			String rawData = threadString.substring(beginIndex, endIndex);
			String[] splitRawData = rawData.split("\n");
			
			Decision decision = processRawData(threadID, splitRawData, extractUnivName(threadTitle));
			if (checkDecisionValidity(decision)) {
				if (DataCollector.insertDecision(decision)) {
					//System.out.println("Decision succssfully collected.");
				} else {
					//System.err.println("Decision could not be collected for PID: " + decision.pid);
					fileUtility.appendToFile("uncollected", "txt", decision.toString());
				}
			} else {
				System.err.println("Decsion is invalid!");
				try {
					fileUtility.appendToFile("invalid", "txt", decision.toString());
				} catch (NullPointerException npe) {
					System.err.println("Couldn't write to file. " + npe.getMessage());
				}
				
			}
			
		}
		
	}
	
	public static ArrayList<String> split(String data, String delim) {
		ArrayList<String> splitData = new ArrayList<String>();
		
		int beginIndex = 0;
		int endIndex = 0;
		
		while (true) {
			endIndex = data.indexOf(delim, beginIndex);
			
			if (endIndex == -1) {
				endIndex = data.length();
			}
			splitData.add(data.substring(beginIndex, endIndex));
			
			if (endIndex == data.length()) {
				break;
			}
			beginIndex = endIndex + 1;
		}
		
		return splitData;
	}
	
	public static String[] split(String data, String[] delims) {
		String[] splitData = null;
		
		for (int i = 0; i < delims.length; i++) {
			if (data.contains(delims[i])) {
				splitData = new String[2];
				int index = data.indexOf(delims[i]);
				
				splitData[0] = data.substring(0, index);
				splitData[1] = data.substring(index + delims[i].length(), data.length());
				break;
			}
		}
		
		return splitData;
	}
	
	/**
	 * Extract relevant data out of raw data
	 * @param splitRawData
	 * @return
	 */
	private static Decision processRawData(int threadID, String[] splitRawData, String univName) {
		
		Decision decision = new Decision();
		decision.threadID = threadID;
		decision.university = univName;
		
		for (int i = 0; i < splitRawData.length; i++) {
			String rawData = splitRawData[i];
			if (Pattern.matches(".*pid_\\d.*", rawData)) {
				patternMatcher = Regex.digitPattern.matcher(rawData);
				
				/* Extract the Post ID from the String */
				if (patternMatcher.find()) {
					decision.pid = Integer.parseInt(patternMatcher.group(0));
				}
				//System.out.println("PID: " + decision.pid);
			} else {
				String data = replaceTags(rawData).toLowerCase();	// Replace all HTML tags with blanks
				
				/* Get the decision */
				if (Regex.decisionPattern.matcher(data).matches()) {
					data = Regex.decisionPatternReplacement.matcher(data).replaceFirst("");
					
					if (Regex.admitPattern.matcher(data).matches()) {
						decision.decision = true;
					} else {
						decision.decision = false;
					}
					//System.out.println(decision.decision);
					
					/* Extract decision date */
					decision.decisionDate = extractDate(data);
					//System.out.println("Decision date:" + decision.decisionDate);
					
				/* Extract application date */	
				} else if (Regex.applicationDatePattern.matcher(data).matches()) {
					decision.applicationDate = extractDate(data);
					//System.out.println("App date: " + decision.applicationDate);
				
				/* Extract GRE score*/
				} else if (Regex.grePattern.matcher(data).matches()) {
					decision.gre = extractGREScore(data);
					//for (int j = 0; j < 4; j++) System.out.print(decision.gre[j] + " ");
					//System.out.println();
				
				/* Extract TOEFL score*/
				} else if (Regex.toeflPattern.matcher(data).matches()) {
					decision.toefl = extractIntegerValue(data);
					//System.out.println("TOEFL: " + decision.toefl);
					
				/* Extract UG CGPA */
				} else if (Regex.ugCGPAPattern.matcher(data).matches()) {
					decision.ugCGPA = extractDecimalValue(data);
					//System.out.println("CGPA: " + decision.ugCGPA);
					
				/* Extract work experience */
				} else if (Regex.workExPattern.matcher(data).matches()) {
					if (Regex.workExMonthPattern.matcher(data).matches()) {
						decision.workEx = (double) extractIntegerValue(data) / 12.0;
					} else if (Regex.workExYearPattern.matcher(data).matches()) {
						decision.workEx = extractDecimalValue(data);
					} else {
						decision.workEx = 0.0;
					}
					//System.out.println("Work ex: " + decision.workEx + " years");
					
				/* Extract publication / project details */
				} else if (Regex.pubProjPattern.matcher(data).matches()) {
					decision.publicationsProjects = Regex.pubProjPatternReplacement.matcher(data).replaceFirst("").trim();
					//System.out.println("Pub proj: " + decision.publicationsProjects);
					
				/* Extract graduate department and/or major */
				} else if (Regex.deptMajorPattern.matcher(data).matches()) {
					decision.gradDeptMajor = sanitizeRawData(Regex.deptMajorPatternReplacement.matcher(data).replaceFirst("").trim());
					//System.out.println("gradDeptMajor: " + decision.gradDeptMajor);
				
				/* Extract UG university and/or major */	
				} else if (Regex.ugUnivMajorPattern.matcher(data).matches()) {
					decision.ugUnivMajor = sanitizeRawData(Regex.ugUnivMajorPatternReplacement.matcher(data).replaceFirst("").trim());
					//System.out.println("ugUnivMajor: " + decision.ugUnivMajor);
					
				}
				
			}
		}
		
		return decision;
	}
	
	/**
	 * Replace the HTML tags with blanks
	 * @param htmlData
	 * @return
	 */
	private static String replaceTags(String htmlData) {
		
		StringBuffer data = new StringBuffer(htmlData);
		int beginIndex = data.indexOf("<");
		int endIndex = data.indexOf(">", beginIndex) + 1;
		
		while (beginIndex != -1 && endIndex != -1) {
			data.replace(beginIndex, endIndex, "");
			beginIndex = data.indexOf("<");
			endIndex = data.indexOf(">", beginIndex) + 1;
		}

		return data.toString().trim();
	}
	
	/**
	 * Extracts the date from a string
	 * @param data
	 * @return
	 */
	private static String extractDate(String data) {

		int[] date = new int[3];
		int dateIndex = 0;
		if (Regex.datePattern1.matcher(data).matches()) {
			while (true) {
				patternMatcher = Regex.digitPattern.matcher(data);
				if (patternMatcher.find()) {
					date[dateIndex++] = Integer.parseInt(patternMatcher.group(0));
					data = patternMatcher.replaceFirst("");
					
					if (dateIndex == 3) break;
				} else {
					break;
				}
			}
			
			/* Check if the date is in dd/MM/yyyy format or MM/dd/yyyy format */
			if (date[Dates.MONTH] > 12) {
				int swap = date[Dates.MONTH];
				date[Dates.MONTH] = date[Dates.DAY];
				date[Dates.DAY] = swap;
			}
			
		} else  if (Regex.datePattern2.matcher(data).matches()) {	// If the month is in words
			patternMatcher = Regex.digitPattern.matcher(data);
			if (patternMatcher.find()) {
				date[dateIndex++] = Integer.parseInt(patternMatcher.group(0));
				data = patternMatcher.replaceFirst("");
			} 

			/* To cater the month in words */
			patternMatcher = Regex.monthPattern.matcher(data);
			if (patternMatcher.find()) {
				date[dateIndex++] = DateUtility.getNumericMonth(patternMatcher.group(0));
			}
			
			patternMatcher = Regex.digitPattern.matcher(data);
			if (patternMatcher.find()) {
				date[dateIndex++] = Integer.parseInt(patternMatcher.group(0));
				data = patternMatcher.replaceFirst("");
			} else {
				date[dateIndex++] = 2012;							// Default year if none is specified
			}
			
		} else {
			return "31-12-2099";									// Default Date if none could be extracted
		}
		
		return DateUtility.getStandardDate(date, "dd-MM-yyyy");
	}
	
	/**
	 * Extract GRE Scores from a String
	 * @param data
	 * @return
	 */
	private static double[] extractGREScore(String data) {
		double[] gre = new double[4];
		
		for (int j = 0; j < 4; j++) {
			patternMatcher = Regex.digitPattern.matcher(data);
			if (j == GRE.AWA) patternMatcher = Regex.decimalPattern.matcher(data);
			
			if (patternMatcher.find()) {
				gre[j] = Double.parseDouble(patternMatcher.group(0));
				data = patternMatcher.replaceFirst("");
				
			} else {
				gre[j] = 0.0;										// Default value if none could be extracted
			}
		}
		
		/* Calculate the GRE total if that was not mentined and hence could not be extracted */
		if (gre[GRE.TOTAL] <= 170 || (gre[GRE.TOTAL] > 340 && gre[GRE.TOTAL] <= 800)) {
			gre[GRE.AWA] = gre[GRE.VERBAL];
			gre[GRE.VERBAL] = gre[GRE.QUANT];
			gre[GRE.QUANT] = gre[GRE.TOTAL];
			gre[GRE.TOTAL] = gre[GRE.QUANT] + gre[GRE.VERBAL]; 
		}
		
		return gre;
	}
	
	/**
	 * Extract the integer values from the string. can be used to extract the TOEFL score or # of months 
	 * in work experience.
	 * @param data
	 * @return
	 */
	private static int extractIntegerValue(String data) {
		patternMatcher = Regex.digitPattern.matcher(data);
		if (patternMatcher.find()) {
			return Integer.parseInt(patternMatcher.group(0));
		} else {
			return 0;
		}
	}
	
	/**
	 * Extract the decimal values from the string. Can be used to extract the CGPA and work experience.
	 * @param data
	 * @return
	 */
	private static double extractDecimalValue(String data) {
		
		/* Check if the CGPA is in decimal format. If not check if it is in integer format */
		if (Regex.decimalExistancePattern.matcher(data).matches()) {
			patternMatcher = Regex.decimalPattern.matcher(data);
		} else if (Regex.digitExistancePattern.matcher(data).matches()) {
			patternMatcher = Regex.digitPattern.matcher(data);
		}
		
		if (patternMatcher.find()) {
			return Double.parseDouble(patternMatcher.group(0));
		} else {
			return 0.0;
		}
	}
	
	public static ArrayList<String> sanitizeRawData(ArrayList<String> data) {
		
		ArrayList<String> sanitizedData = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			String currentData = data.get(i);
			
			while (currentData.contains("<") && currentData.contains(">")) {
				int beginIndex = currentData.indexOf("<");
				int endIndex = currentData.indexOf(">", beginIndex);
				
				currentData = currentData.substring(0, beginIndex) + currentData.substring(endIndex + 1, currentData.length());
			}
			
			while (currentData.contains("[") && currentData.contains("]")) {
				int beginIndex = currentData.indexOf("[");
				int endIndex = currentData.indexOf("]", beginIndex);
				
				currentData = currentData.substring(0, beginIndex) + currentData.substring(endIndex + 1, currentData.length());
			}
			
			sanitizedData.add(currentData);
		}
		
		return sanitizedData;
	}
	
	public static String sanitizeRawData(String rawData) {
		return Regex.symbolPattern.matcher(rawData).replaceAll("").trim();
	}
	
	/**
	 * Removes the extra spaces and the "Fall 2012" text from the thread title to extract the university name.
	 * @param threadTitle
	 * @return univName
	 */
	public static String extractUnivName(String threadTitle) {
		String univName = Regex.whiteSpacePattern.matcher(threadTitle).replaceAll(" ").trim();
		
		return Regex.fallPattern.matcher(univName).replaceAll("").trim();
	}
	
	/**
	 * Checks the validity of the extracted decision.
	 * @param decision
	 * @return true if valid, else false
	 */
	public static boolean checkDecisionValidity(Decision decision) {
		
		try {
			if (decision.pid <= 0) {
				return false;
			}
			
			if (decision.university == null || decision.university == "") {
				return false;
			}
			
			if (decision.gre[GRE.TOTAL] < 100.0 || decision.gre[GRE.QUANT] < 60.0 || 
					decision.gre[GRE.VERBAL] < 60.0 || decision.toefl == 0.0) {
				return false;
			}
		} catch (NullPointerException npe) {
			return false;
		}
		
		return true;
	}
}

package com.edulix.main;

import java.util.ArrayList;

import com.edulix.constants.File;
import com.edulix.utils.DataProcessor;
import com.edulix.utils.FileUtility;

public class DecisionDecoder {

	public static void main(String[] args) {
		
		FileUtility fileUtil = new FileUtility();
		ArrayList<String> rawFileList = fileUtil.listFiles(File.RAW_DATA_LOCATION);
		
		ArrayList<String> linesRead = fileUtil.readFile(rawFileList.get(0));
		ArrayList<String> sanitizedLines = DataProcessor.sanitizeRawData(linesRead);
		for (int i = 1; i < sanitizedLines.size(); i++) {
			ArrayList<String> splitData = DataProcessor.split(sanitizedLines.get(i), "|");
			for (int j = 0; j < splitData.size(); j++) {
				String[] data = DataProcessor.split(splitData.get(j), delims);
				if (data != null) {
					System.out.println(data[0] + " == " + data[1]);
				}
			}
			System.out.println();
 		}
	}
	
	/* Private instance variables */
	private static String[] delims = {":-", ":", "-"};
	
}

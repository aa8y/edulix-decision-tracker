package com.edulix.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.edulix.constants.URL;
import com.edulix.utils.DataProcessor;
import com.edulix.utils.ForumUtility;
import com.edulix.utils.PropertiesUtility;

public class DecisionTracker {

	public static void main(String[] args) {
		
		DecisionTracker tracker = new DecisionTracker();
		String cookieData = "<Put your cookie string here >";
		cookies = PropertiesUtility.addDataToMap(cookieData, ";", "=");
		
		/* Parsing the main page of the Decision Tracker sub-forum */
		Document decisionTrackerMainPage = ForumUtility.connect(cookies, userAgent, URL.EDULIX_FID_URL + URL.DECISION_TRACKER_FID, 1).get(0);
		int decisionTrackerPageCount = ForumUtility.getPageCount(decisionTrackerMainPage);			// Extracting Decision Tracker sub-forum page count
		
		//Map<Integer, Integer> threadProp = new HashMap<Integer, Integer>();
		threadIDs = tracker.getAllThreads(URL.DECISION_TRACKER_FID, decisionTrackerPageCount);
		tracker.processThreads(threadIDs);

	}
	
	/**
	 * Extracts all the thread IDs of the forum whose FID has been passed.
	 * @param mainPageCount
	 * @return List of all thread IDs
	 */
	private ArrayList<Integer> getAllThreads(int mainPageFID, int mainPageCount) {
		ArrayList<Integer> threadIDs = new ArrayList<Integer>();
		
		/* Extracting all the pages of the Decision Tracker sub-forum */
		ArrayList<Document> decisionTrackerPages = ForumUtility.connect(cookies, userAgent, URL.EDULIX_FID_URL + mainPageFID, mainPageCount);
		
		/* Iterating through the pages and extracting all the thread IDs */
		for (int i = 0; i < decisionTrackerPages.size(); i++) {
			Document page = decisionTrackerPages.get(i);
			threadIDs.addAll(ForumUtility.getThreads(page));
		}
		
		return threadIDs;
	}
	
	/**
	 * Processes all the threads whose thread IDs are passed and extracts the data out of them.
	 * 
	 * @param threadIDs
	 */
	private void processThreads(ArrayList<Integer> threadIDs) {
		
		for (int i = 0; i < threadIDs.size(); i++) {
			int threadID = threadIDs.get(i);
			
			if (threadID != 105838) {
				/* Parsing the main page of the thread */
				Document threadPage = ForumUtility.connect(cookies, userAgent, URL.EDULIX_THREAD_URL + threadID, 1).get(0);
				int threadPageCount = ForumUtility.getPageCount(threadPage);						// Extracting the thread page count
				threadTitles.add(threadTitleIndex, ForumUtility.getPageTitle(threadPage));			// Extracting and collecting the thread titles
				
				/* Extracting all the pages of the thread */
				ArrayList<Document> threadPages = ForumUtility.connect(cookies, userAgent, URL.EDULIX_THREAD_URL + threadID, threadPageCount);
				
				for (int j = 0; j < threadPages.size(); j++) {
					DataProcessor.extractDataFromThread(threadID, threadPages.get(j), threadTitles.get(threadTitleIndex));
				}
				threadTitleIndex++;
			}
		}
		
	}
	
	/* Private Instance Variables */
	
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0";
	private static int threadTitleIndex = 0;
	
	private static Map<String, String> cookies = new HashMap<String, String>();
	private static ArrayList<Integer> threadIDs;
	private static ArrayList<String> threadTitles = new ArrayList<String>();
	
}

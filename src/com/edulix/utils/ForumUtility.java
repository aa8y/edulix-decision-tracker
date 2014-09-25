package com.edulix.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.edulix.constants.URL;

public class ForumUtility {

    public static ArrayList<Document> connect(Map<String, String> cookies, String userAgent, String urlPart, int toPage) {
        
        ArrayList<Document> forumPages = new ArrayList<Document>();
        
        for (int index = 1; index <= toPage; index++) {
            tryAgain:
            try {
                String url = URL.EDULIX_URL + urlPart + URL.EDULIX_PAGE_URL + index;
                System.out.println("Attemptting to connect to " + url);
                
                Document forumPage = Jsoup.connect(url)
                        .userAgent(userAgent)
                        .referrer(URL.EDULIX_URL)
                        .cookies(cookies).get();
                forumPages.add(forumPage);
            } catch (IOException e) {
                System.out.println("Connetion timed out! Retrying.");
                index--;
                break tryAgain;
                //forumPages.addAll(connect(index, cookies, userAgent, urlPart, toPage));
            }
        }
        
        return forumPages;
    }
    
    public static int getPageCount(Document page) {
        
        int maxPageNum = 0;
        
        ArrayList<String> pageLinks = getRelevantLinks(page, "page=");
        for (int i = 0; i < pageLinks.size(); i++) {
            String link = pageLinks.get(i);
            String pageNumString = DataProcessor.getDataBetweenTags(link);
            
            try {
                int pageNum = Integer.parseInt(pageNumString);
                if (pageNum > maxPageNum) {
                    maxPageNum = pageNum;
                }
            } catch (NumberFormatException nfe) {
                //System.out.println("No page number in " + pageLinks.get(i));
            }
        }
        
        return maxPageNum;
    }
    
    public static ArrayList<String> getRelevantLinks(Document page, String linkFilter) {
        
        ArrayList<String> relLinks = new ArrayList<String>();
        Elements links = page.select("a[href]"); // a with href
        
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).toString().contains(linkFilter)) {
                relLinks.add(links.get(i).toString());
                //System.out.println(links.get(i).toString());
            }
        }
        
        return relLinks;
    }
    
    public static ArrayList<Integer> getThreads(Document page) {
        
        ArrayList<Integer> threadIDs = new ArrayList<Integer>();
        ArrayList<String> threadLinks = getRelevantLinks(page, URL.EDULIX_THREAD_URL);
        
        for (int i = 0; i < threadLinks.size(); i++) {
            String thread = threadLinks.get(i);
            
            if (thread.contains(">1<")) {
                int beginIndex = thread.indexOf(URL.EDULIX_THREAD_URL) + URL.EDULIX_THREAD_URL.length();
                int endIndex = thread.indexOf("\"", beginIndex);
                
                try {
                    threadIDs.add(Integer.parseInt(thread.substring(beginIndex, endIndex).trim()));
                } catch (NumberFormatException nfe) {
                    System.out.println("Unable to parse!");
                }
            }
        }
        
        return threadIDs;
    }
    
    public static String getPageTitle(Document page) {
        
        Elements titles = page.select("title");
        String title = titles.get(0).toString();
        
        return DataProcessor.getDataBetweenTags(title);
    }
    
    /*public static boolean isValidData(String data) {
        
        String dataLowerCase = data.toLowerCase();
        
        if (dataLowerCase.contains("decision")) {
            return true;
        } else if (dataLowerCase.contains("application")) {
            return true;
        } else if (dataLowerCase.contains("department")) {
            return true;
        } else if (dataLowerCase.contains("major")) {
            return true;
        } else if (dataLowerCase.contains("gre")) {
            return true;
        } else if (dataLowerCase.contains("toefl")) {
            return true;
        } else if (dataLowerCase.contains("ug ")) {
            return true;
        } else if (dataLowerCase.contains("cgpa")) {
            return true;
        } else if (dataLowerCase.contains("work")) {
            return true;
        } else if (dataLowerCase.contains("publications")) {
            return true;
        } else if (dataLowerCase.contains("project")) {
            return true;
        }
        
        return false;
        
    }*/
    
}

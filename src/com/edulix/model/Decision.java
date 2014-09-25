package com.edulix.model;

public class Decision {

	public int threadID;
	public int pid;								// Post ID
	public String university;
	public boolean decision;					// Admit / Reject
	public String decisionDate;
	public String applicationDate;
	public String gradDeptMajor;
	public double[] gre;
	public int toefl;
	public String ugUnivMajor;
	public double ugCGPA;
	public double workEx;						// Work experience in years
	public String publicationsProjects;

	public String toString() {
		String string = threadID + "|" + pid + "|" + university + "|" + decision + "|" + decisionDate + "|" + applicationDate + "|"
				+ gradDeptMajor + "|";
		for (int i = 0; i < gre.length; i++) {
			string += gre[i] + "|";
		}
		
		return string + toefl + "|" + ugUnivMajor + "|" + ugCGPA + "|" + workEx + "|" + publicationsProjects;
	}
	
}

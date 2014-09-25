package com.edulix.utils;

import java.util.regex.Pattern;

public class Regex {

	public static Pattern decisionPattern = Pattern.compile(".*.e.i.ion\\s+[&|a].*\\s+Date.*", Pattern.CASE_INSENSITIVE);
	public static Pattern decisionPatternReplacement = Pattern.compile(".*.e.i.ion\\s+[&|a].*\\s+Date", Pattern.CASE_INSENSITIVE);
	
	public static Pattern admitPattern = Pattern.compile(".*a.*t.*", Pattern.CASE_INSENSITIVE);
	public static Pattern applicationDatePattern = Pattern.compile(".*app.*d.*t.*\\p{Punct}.*", Pattern.CASE_INSENSITIVE);
	
	public static Pattern deptMajorPattern = Pattern.compile(".*dep.*t.*major.*", Pattern.CASE_INSENSITIVE);
	public static Pattern deptMajorPatternReplacement = Pattern.compile(".*dep.*t.*major", Pattern.CASE_INSENSITIVE);
	
	public static Pattern ugUnivMajorPattern = Pattern.compile(".*ug.*univ.*major.*", Pattern.CASE_INSENSITIVE);
	public static Pattern ugUnivMajorPatternReplacement = Pattern.compile("ug.*univ.*major", Pattern.CASE_INSENSITIVE);
	
	public static Pattern datePattern1 = Pattern.compile(".*\\d+\\p{Punct}\\d+\\p{Punct}\\d+.*");
	public static Pattern datePattern2 = Pattern.compile(".*[jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec].*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	public static Pattern grePattern = Pattern.compile(".*gre\\s+score.*", Pattern.CASE_INSENSITIVE);
	public static Pattern toeflPattern = Pattern.compile(".*toefl\\s+score.*", Pattern.CASE_INSENSITIVE);
	public static Pattern ugCGPAPattern = Pattern.compile(".*ug.*cgpa.*", Pattern.CASE_INSENSITIVE);
	
	public static Pattern workExPattern = Pattern.compile(".*work.*ex.*", Pattern.CASE_INSENSITIVE);
	public static Pattern workExMonthPattern = Pattern.compile(".*\\d+.*m.*nt.*", Pattern.CASE_INSENSITIVE);
	public static Pattern workExYearPattern = Pattern.compile(".*\\d+.*y.*r.*", Pattern.CASE_INSENSITIVE);
	
	public static Pattern pubProjPattern = Pattern.compile(".*publication.*project.*", Pattern.CASE_INSENSITIVE);
	public static Pattern pubProjPatternReplacement = Pattern.compile("publications/projects.*[:|-]", Pattern.CASE_INSENSITIVE);
	
	public static Pattern digitExistancePattern = Pattern.compile(".*\\d+.*");
	public static Pattern digitPattern = Pattern.compile("\\d+");
	
	public static Pattern decimalExistancePattern = Pattern.compile(".*\\d+\\.\\d+.*");
	public static Pattern decimalPattern = Pattern.compile("\\d+\\.\\d+");
	
	public static Pattern monthPattern = Pattern.compile("[jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec].*");
	public static Pattern symbolPattern = Pattern.compile("\\p{Punct}");
	public static Pattern whiteSpacePattern = Pattern.compile("\\s+");
	public static Pattern fallPattern = Pattern.compile("\\p{Punct}\\sfall.*\\d+", Pattern.CASE_INSENSITIVE);
	
}

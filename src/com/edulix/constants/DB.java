package com.edulix.constants;

public class DB {
	
	public static final String URL = "jdbc:mysql://localhost:3306/";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root";
	
	public static final String INSERT = "INSERT INTO ";
	public static final String MAIN_TABLE = "EDULIX_TEST.DECISION_TRACKER ";
	public static final String ERROR_TABLE = "EDULIX_TEST.ERR_DECISION_TRACKER ";
	public static final String COLUMNS = "(THREAD_ID, PID, UNIVERSITY, DECISION, DECISION_DATE, APP_DATE, GRAD_DEP_MAJOR, " +
			"GRE_TOTAL, GRE_QUANT, GRE_VERBAL, GRE_AWA, TOEFL, UG_UNIV_MAJOR, UG_CGPA, WORK_EX, PUB_PROJ) ";
	public static final String VALUES = "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	
}

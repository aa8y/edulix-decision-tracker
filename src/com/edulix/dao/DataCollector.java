package com.edulix.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.edulix.constants.DB;
import com.edulix.constants.GRE;
import com.edulix.model.Decision;
import com.edulix.utils.JDBCConnector;

public class DataCollector {

	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet result = null;
	
	
	public static boolean insertDecision(Decision decision) {
		
		int status = 0;
		String appDecision = "reject";
		if (decision.decision) {
			appDecision = "admit";
		}
		double[] gre = decision.gre;
		
		try {
			connection = JDBCConnector.createConnection();
			statement = connection.createStatement();
			
			preparedStatement = connection.prepareStatement(DB.INSERT + DB.MAIN_TABLE + DB.COLUMNS + DB.VALUES);
			preparedStatement.setInt(1, decision.threadID);
			preparedStatement.setInt(2, decision.pid);
			preparedStatement.setString(3, decision.university);
			preparedStatement.setString(4, appDecision);
			preparedStatement.setString(5, decision.decisionDate);
			preparedStatement.setString(6, decision.applicationDate);
			preparedStatement.setString(7, decision.gradDeptMajor);
			preparedStatement.setDouble(8, gre[GRE.TOTAL]);
			preparedStatement.setDouble(9, gre[GRE.QUANT]);
			preparedStatement.setDouble(10, gre[GRE.VERBAL]);
			preparedStatement.setDouble(11, gre[GRE.AWA]);
			preparedStatement.setInt(12, decision.toefl);
			preparedStatement.setString(13, decision.ugUnivMajor);
			preparedStatement.setDouble(14, decision.ugCGPA);
			preparedStatement.setDouble(15, decision.workEx);
			preparedStatement.setString(16, decision.publicationsProjects);
			
			status = preparedStatement.executeUpdate();
			
		} catch (SQLException se) {
			System.err.println(se.getMessage());
			return false;
		} catch (NullPointerException ne) {
			System.err.println(ne.getMessage());
			return false;
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (result != null)
					result.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ((status == 0) ? false : true);
	}
}

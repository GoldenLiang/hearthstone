package com.lc;
import java.sql.*;
import java.util.*;

public class ConnectionManager {
	//static Connection con;
	static String url;

	
	
	public static Connection getConnection() {
		
		Connection con=null;
		
		try {
			
			String dbName="lushi";
			String userName="root";
			String userPasswd="lc19971225";
			
			String url="jdbc:mysql://localhost/"+dbName+"?user="+userName+"&password="+userPasswd
				+"&autoReconnect=true&failOverReadOnly=false&maxReconnects=10"; 
			// assuming "DataSource" is your DataSource name
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con = DriverManager.getConnection(url);
				// assuming your SQL Server's username is "username"
				// and password is "password"
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} 
//		catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		return con;
	}
}

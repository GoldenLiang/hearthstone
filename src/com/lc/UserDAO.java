package com.lc;
import java.sql.*;

public class UserDAO {
	static Connection currentCon = null;
	static ResultSet rs = null;

	public static UserBean login(UserBean bean) {
		// preparing some objects for connection
		Statement stmt = null;
		String username = bean.getUserName();
		String password = bean.getPassword();
		String searchQuery = "select * from users where username='" + username
				+ "' AND password='" + password + "'";
		// "System.out.println" prints in the console; Normally used to trace
		// the process
		System.out.println("Your user name is " + username);
		System.out.println("Your password is " + password);
		System.out.println("Query: " + searchQuery);
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			rs = stmt.executeQuery(searchQuery);
			boolean more = rs.next();
			// if user does not exist set the isValid variable to false
			if (!more) {
				System.out
						.println("Sorry, you are not a registered user! Please sign up first");
				bean.setValid(false);
			}
			// if user exists set the isValid variable to true
			else if (more) {
				
				String userName=rs.getString("username");
				
				System.out.println("Welcome " + userName);
				bean.setUserName(userName);
				bean.setValid(true);
			}
		} catch (Exception ex) {
			System.out.println("Log In failed: An Exception has occurred! "
					+ ex);
		}
		// some exception handling 
		finally
		{
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
				stmt = null;
			}
			if (currentCon != null) {
				try {
					currentCon.close();
				} catch (Exception e) {
				}
				currentCon = null;
			}
		}
		return bean;
	}
	
	
	public static UserBean register(UserBean bean) {
		// preparing some objects for connection
		Statement stmt = null;
		String username = bean.getUserName();
		String password = bean.getPassword();
		String insertQuery = "insert into users (username, password) values ('" + username
				+ "','"+ password + "')";
		// "System.out.println" prints in the console; Normally used to trace
		// the process
		
		System.out.println("Query: " + insertQuery);
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			stmt.executeUpdate(insertQuery);
			
			bean.setValid(true);
			
		} catch (Exception ex) {
			System.out.println("Register failed: An Exception has occurred! "
					+ ex);
		}
		// some exception handling 
		finally
		{
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
				stmt = null;
			}
			if (currentCon != null) {
				try {
					currentCon.close();
				} catch (Exception e) {
				}
				currentCon = null;
			}
		}
		return bean;
	}
	
	public static int getMoney(UserBean user)
	{
		int money=-1;
		
		Statement stmt = null;
		String searchQuery = "select * from users where username='" + user.getUserName()+"'";
		
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			rs = stmt.executeQuery(searchQuery);
			boolean more = rs.next();
			// if user does not exist set the isValid variable to false
			if (!more) {
				System.out.println("Sorry, you are not a registered user! Please sign up first");
				
			}
			// if user exists set the isValid variable to true
			else if (more) {
				
				money=rs.getInt("money");
				System.out.println("your money is " + money);
				
			}
		} catch (Exception ex) {
			System.out.println("Log In failed: An Exception has occurred! "
					+ ex);
		}
		// some exception handling 
		finally
		{
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
				stmt = null;
			}
			if (currentCon != null) {
				try {
					currentCon.close();
				} catch (Exception e) {
				}
				currentCon = null;
			}
		}
		return money;
		
		
	}
	
	public static int getDust(UserBean user)
	{
		int dust=-1;
		
		Statement stmt = null;
		String searchQuery = "select * from users where username='" + user.getUserName()+"'";
		
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			rs = stmt.executeQuery(searchQuery);
			boolean more = rs.next();
			// if user does not exist set the isValid variable to false
			if (!more) {
				System.out.println("Sorry, you are not a registered user! Please sign up first");
				
			}
			// if user exists set the isValid variable to true
			else if (more) {
				
				dust=rs.getInt("dust");
				System.out.println("your dust is " + dust);
				
			}
		} catch (Exception ex) {
			System.out.println("Log In failed: An Exception has occurred! "
					+ ex);
		}
		// some exception handling 
		finally
		{
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
				stmt = null;
			}
			if (currentCon != null) {
				try {
					currentCon.close();
				} catch (Exception e) {
				}
				currentCon = null;
			}
		}
		return dust;
		
		
	}
	
}

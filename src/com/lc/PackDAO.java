package com.lc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class PackDAO {

	static Connection currentCon = null;
	static ResultSet rs = null;
	
	public static List<Pack> getPacks() {
		// preparing some objects for connection
		Statement stmt = null;
		
		String searchQuery = "select * from packs";
		
		List<Pack> packList=new LinkedList<Pack>();
		
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			rs = stmt.executeQuery(searchQuery);
			
						
			while(rs.next())
			{
				Pack pack=new Pack();
				pack.setName(rs.getString(1)); // name
				pack.setCards(rs.getString(2)); // cards
				pack.setJob(rs.getString(3)); // job
								
				packList.add(pack);
			}
			
			
		} catch (Exception ex) {
			System.out.println("getCards failed: An Exception has occurred! "+ ex);
			
			packList.clear();
			
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
		
		return packList;
		
	}
	
	public static boolean create(String name, String job)
	{
		Statement stmt = null;
		
		String insertQuery = "insert into packs values ('"+name+"','','"+job+"')";
		System.out.println(insertQuery);
		
		boolean ret=true;
		
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			stmt.executeUpdate(insertQuery);
		}
		catch (Exception ex) {
			System.out.println("newPack failed: An Exception has occurred! "+ ex);
			
			ret=false;		
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
		
		return ret;
	}
	
	public static boolean storePack(String name, String job, String cards)
	{
		Statement stmt = null;
		
		String updateQuery = "update packs set cards='"+cards+"' where name='"+name+"'";
		System.out.println(updateQuery);
		
		boolean ret=true;
		
		try {
			// connect to DB
			currentCon = ConnectionManager.getConnection();
			stmt = currentCon.createStatement();
			stmt.executeUpdate(updateQuery);
		}
		catch (Exception ex) {
			System.out.println("storePack failed: An Exception has occurred! "+ ex);
			
			ret=false;		
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
		
		return ret;
	}
	
}



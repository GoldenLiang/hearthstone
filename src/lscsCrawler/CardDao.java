package lscsCrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDao {
	String urldb = "jdbc:mysql://localhost:3306/lushi?"
			+ "user=root&password=lc19971225&useUnicode=true&characterEncoding=UTF8";
	public void save(String id, String artist, String attack, String background
			,String cardClass, String cardEffect, String cardRace, String cardRarity,
			String cardSet, String cardType, String code, String consume, String cost
			,String createTime, String description, String durability, String gain, 
			String golden, String health, String imageUrl, String name, String neutralClass, String updateTime) throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("驱动不存在");
		}
		Connection connection = DriverManager.getConnection(urldb);
		PreparedStatement ps = connection.prepareStatement("insert into cards (imageid, name, type, job, effects, race, rate, cost, attack, blood, groupcode, split, effectcode, targetcode) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, cardClass + "/" + replaceBlank(code));
		ps.setString(2, name);
		ps.setString(3, cardType);
		ps.setString(4, filter(cardClass));
		ps.setString(5, description);
		ps.setString(6, cardRace);
		ps.setString(7, cardRarity);
		ps.setString(8, cost);
		ps.setString(9, attack);
		ps.setString(10, health);
		ps.setString(11, "");
		ps.setString(12, "0");
		ps.setString(13, "");
		ps.setString(14, "");
		ps.execute();
		ps.close();
		connection.close();
	}

	private String filter(String cardClass) {
		if(cardClass.equals("neutral")) {
			cardClass = "中立";
		} else if(cardClass.equals("paladin")) {
			cardClass = "骑士";
		} else if(cardClass.equals("mage")) {
			cardClass = "法师";
		} else if(cardClass.equals("druid")) {
			cardClass = "德鲁伊";
		} else if(cardClass.equals("hunter")) {
			cardClass = "猎人";
		} else if(cardClass.equals("priest")) {
			cardClass = "牧师";
		} else if(cardClass.equals("warrior")) {
			cardClass = "战士";
		} else if(cardClass.equals("rogue")) {
			cardClass = "潜行者";
		} else if(cardClass.equals("warlock")) {
			cardClass = "术士";
		} else if(cardClass.equals("shaman")) {
			cardClass = "萨满";
		} 
		return cardClass;
	}

	private String replaceBlank(String code) {
		char[] c = code.toCharArray();
		for(int i = 0; i < c.length; i++) {
			if(c[i] == ' ' ) {
				c[i] = '+';
			}
		}
		String s = String.valueOf(c);
		s += ".png";
		return s;
	}
	
	public List<String> getAllImgUrl() throws SQLException{
		List<String> list = new ArrayList<>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("驱动不存在");
		}
		Connection connection = DriverManager.getConnection(urldb);
		PreparedStatement ps = connection.prepareStatement("select name,imageUrl from card");
		ResultSet resultSet = ps.executeQuery();
		while(resultSet.next()){
			list.add(resultSet.getString(1) + "|" + resultSet.getString(2));
		}
		resultSet.close();
		ps.close();
		connection.close();
		return list;
	}
	
	
}

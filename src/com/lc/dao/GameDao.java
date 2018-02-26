package com.lc.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.lc.Card;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


import utils.SpringFactory;
import com.lc.Game;




public class GameDao {

	private JdbcTemplate jdbcTemplate ;
	
		
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private List<Card> randomize(List<Card> cards)
	{
		List<Card> ret=new ArrayList<Card>();
		
		// shuffle
		Random random = new Random();
		for(int i=0;i<cards.size();i++)
		{
			int rand=random.nextInt(cards.size());
			
			Card tempCard=cards.get(0);
			cards.set(0, cards.get(rand));
			cards.set(rand, tempCard);
		}
		
		for(int i=0;i<30;i++)
		{
			ret.add(cards.get(i));
		}
		
				
		return ret;
	}
	
	
	
	
	
	public boolean begin(String userName, String job, String packName)
	{
		Game emptyGame=getGame("");
		
		if(emptyGame!=null)
		{
			return join(emptyGame.getId(), userName, job, packName);
		}
		else
		{
			return create(userName, job, packName);
		}
	}
	
	public boolean join(String gid, String userName, String job, String packName)
	{
						
		// update an existing game
				
		String gameUpdateQuery = "update games set username2='"+userName + "', job2='"+ job + "' where id='"+gid+"'"; 
		
		System.out.println(gameUpdateQuery);
				
		// insert 30 cards into pack
		
		List<Card> cardList2=null;
		
		if(packName.equals("base_pack"))
		{
			CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
			cardList2=cardDao.getBaseCards(job);
			cardList2=randomize(cardList2);
		}
				
		
		List<String> cardInsertQuerys=new ArrayList<String>();
		for(int i=0;i<cardList2.size();i++)
		{
			String cid="g"+(i+31);
			
			String insertQuery="insert into gamecards (id, gid, uid, cardname, type, state) values ('" +
				cid +"','" +
				gid +"','" +
				"g62" +"','" +
				cardList2.get(i).getName() +
				"','pack','')" ;
				
			cardInsertQuerys.add(insertQuery);	
		}
				
		
		
		for(String cardInsertQuery: cardInsertQuerys)
		{
			System.out.println(cardInsertQuery);
		}
		
		// change  4 cards into hand 
		
		List<String> cardUpdateQuerys=new ArrayList<String>();
		
		for(int i=0;i<4;i++)
		{
			String cid="g"+(i+31);
			String updateQuery="update gamecards set type='hand' where id='"+cid+"' and gid='"+gid+"'";
			cardUpdateQuerys.add(updateQuery);
		}
				
		
		for(String cardUpdateQuery: cardUpdateQuerys)
		{
			System.out.println(cardUpdateQuery);
		}
		
		// insert two hero minions into herominion
		
		String heroMinionInsertQuery1="insert into herominion (id, gid, job, attack, blood, guard, state) values ('" +
			"g62" +"','" +
			gid +"','" +
			job +
			"',0,30,0,'attackcount:1')" ;
		
		
		
		System.out.println(heroMinionInsertQuery1);
		//System.out.println(heroMinionInsertQuery2);
		
		// insert two weapons into weapon
		
		String weaponInsertQuery1="insert into weapon (id,gid,uid,attack,blood,state) values ('g64','" +
			gid +"','" +
			"g62" +"',0,0,'')" ;
			
						
		System.out.println(weaponInsertQuery1);
		//System.out.println(weaponInsertQuery2);
		
		boolean ret=true;
				
		
		// connect to DB
		this.jdbcTemplate.update(gameUpdateQuery);
					
		for(String cardInsertQuery: cardInsertQuerys)
		{
			this.jdbcTemplate.update(cardInsertQuery);
		}
		
		for(String cardUpdateQuery: cardUpdateQuerys)
		{
			this.jdbcTemplate.update(cardUpdateQuery);
		}
		
		this.jdbcTemplate.update(heroMinionInsertQuery1);
					
		this.jdbcTemplate.update(weaponInsertQuery1);
					
		
				
		return ret;
	}
	
	public boolean create(String userName, String job, String packName)
	{
		
		// insert a new game
				
		String gid=String.valueOf(System.currentTimeMillis());
		
		String gameInsertQuery = "insert into games (id, winmoney, turn, username1, uid1, job1, username2, uid2, job2, power1, maxpower1) values ('" +
				gid + "'," +
				20 + "," +
				0 + ",'" +
				userName + "','g61','" +
				job + "','','g62','', 1, 1)"; 
		
		System.out.println(gameInsertQuery);
				
		// insert 30 cards into pack
		
		List<Card> cardList1=null;
		
		if(packName.equals("base_pack"))
		{
			CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
			cardList1=cardDao.getBaseCards(job);
			cardList1=randomize(cardList1);
		}
				
		List<String> cardInsertQuerys=new ArrayList<String>();
		for(int i=0;i<cardList1.size();i++)
		{
			String cid="g"+(i+1);
			
			String insertQuery="insert into gamecards (id, gid, uid, cardname, type, state) values ('" +
				cid +"','" +
				gid +"','" +
				"g61" +"','" +
				cardList1.get(i).getName() +
				"','pack','')" ;
				
			cardInsertQuerys.add(insertQuery);	
		}
				
		
		
		for(String cardInsertQuery: cardInsertQuerys)
		{
			System.out.println(cardInsertQuery);
		}
		
		// change 3 cards  into hand 
		
		List<String> cardUpdateQuerys=new ArrayList<String>();
		
		for(int i=0;i<4;i++)
		{
			String cid="g"+(i+1);
			String updateQuery="update gamecards set type='hand' where id='"+cid+"' and gid='"+gid+"'";
			cardUpdateQuerys.add(updateQuery);
		}
				
		
		for(String cardUpdateQuery: cardUpdateQuerys)
		{
			System.out.println(cardUpdateQuery);
		}
		
		// insert two hero minions into herominion
		
		String heroMinionInsertQuery1="insert into herominion (id, gid, job, attack, blood, guard, state) values ('" +
			"g61" +"','" +
			gid +"','" +
			job +
			"',0,30,0,'attackcount:1')" ;
		
		
		
		System.out.println(heroMinionInsertQuery1);
		//System.out.println(heroMinionInsertQuery2);
		
		// insert two weapons into weapon
		
		String weaponInsertQuery1="insert into weapon (id,gid,uid,attack,blood,state) values ('g63','" +
			gid +"','" +
			"g61" +"',0,0,'')" ;
			
						
		System.out.println(weaponInsertQuery1);
		//System.out.println(weaponInsertQuery2);
		
		boolean ret=true;
		
		
		// connect to DB
		
		this.jdbcTemplate.update(gameInsertQuery);
		
		for(String cardInsertQuery: cardInsertQuerys)
		{
			this.jdbcTemplate.update(cardInsertQuery);
		}
		
		for(String cardUpdateQuery: cardUpdateQuerys)
		{
			this.jdbcTemplate.update(cardUpdateQuery);
		}
		
		this.jdbcTemplate.update(heroMinionInsertQuery1);
					
		this.jdbcTemplate.update(weaponInsertQuery1);
			
						
		return ret;
	}	
	
	public Game getGame(String userName) 
	{
		
		String searchQuery = "select * from games where username1='" + userName
			+ "' or username2='" + userName + "'";
		
		System.out.println("Query: " + searchQuery);
		
		List<Game> list=this.jdbcTemplate.query(searchQuery, new GameRowMapper());
				
		Game game=null;
		if(list.size()==0)
		{
			System.out.println("Sorry, there is no game you are playing");
			game=null;
		}
		else
		{
			game=list.get(0);
		}
		
		return game;
	}
	
	public Game getGameById(String id)
	{
		
		String searchQuery = "select * from games where id='" + id + "'";
		Game game=this.jdbcTemplate.queryForObject(searchQuery, new GameRowMapper());
		
		return game;
	}
	
	public void updateGame(Game game)
	{
				
		String updateQuery = "update games set turn=" +game.getTurn()+", round="+game.getRound()+
				", power1="+game.getPower1()+", maxpower1="+game.getMaxpower1()+
				", power2="+game.getPower2()+", maxpower2="+game.getMaxpower2()+
				" where id='" + game.getId()	+ "'";
		
		System.out.println("Query: " + updateQuery);
				
		this.jdbcTemplate.update(updateQuery);
				
	}
	
	
	public void endGame(String userName)
	{
				
		Game game=getGame(userName);
		
		if(game==null)
			return;
		
		List<String> updateQueries=new LinkedList<String>();
		
		
		if(game.getUserName1().equals(userName))
		{
			if(game.getEnd2()!=0)
			{
				updateQueries.add("delete from games where id='"+game.getId()+"'");
				updateQueries.add("delete from minion where gid='"+game.getId()+"'");
				updateQueries.add("delete from listeners where gid='"+game.getId()+"'");
				updateQueries.add("delete from events where gid='"+game.getId()+"'");
				updateQueries.add("delete from herominion where gid='"+game.getId()+"'");
				updateQueries.add("delete from weapon where gid='"+game.getId()+"'");
				updateQueries.add("delete from gamecards where gid='"+game.getId()+"'");
			}
			else
				updateQueries.add("update games set end1=1 where id='" + game.getId()+"'");
		}
		else
		{
			if(game.getEnd1()!=0)
			{
				updateQueries.add("delete from games where id='"+game.getId()+"'");
				updateQueries.add("delete from minion where gid='"+game.getId()+"'");
				updateQueries.add("delete from listeners where gid='"+game.getId()+"'");
				updateQueries.add("delete from events where gid='"+game.getId()+"'");
				updateQueries.add("delete from herominion where gid='"+game.getId()+"'");
				updateQueries.add("delete from weapon where gid='"+game.getId()+"'");
				updateQueries.add("delete from gamecards where gid='"+game.getId()+"'");
			}
			
			else
				updateQueries.add("update games set end2=1 where id='" + game.getId()+"'");
		}
		
		for(String updateQuery: updateQueries)
			System.out.println("Query: " + updateQuery);
				
						
		for(String updateQuery: updateQueries)
			this.jdbcTemplate.update(updateQuery);
						
			
		
	}
	
	public class GameRowMapper implements RowMapper<Game> {  
		  
        @Override  
        public Game mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	Game game=new Game();
        	String id=rs.getString("id");
			int winMoney=rs.getInt("winmoney");
			int turn=rs.getInt("turn");
			int round=rs.getInt("round");
			String trashCards=rs.getString("trashcards");
			String userName1=rs.getString("username1");
			String uid1=rs.getString("uid1");
			String job1=rs.getString("job1");
			
			int power1=rs.getInt("power1");
			int maxpower1=rs.getInt("maxpower1");
			
			String userName2=rs.getString("username2");
			String uid2=rs.getString("uid2");
			String job2=rs.getString("job2");
			
			int power2=rs.getInt("power2");
			int maxpower2=rs.getInt("maxpower2");
			
			int end1=rs.getInt("end1");
			int end2=rs.getInt("end2");
			
			
			game.setId(id);
			game.setWinMoney(winMoney);
			game.setTurn(turn);
			game.setRound(round);
			game.setTrashCards(trashCards);
			game.setUserName1(userName1);
			game.setUid1(uid1);
			game.setJob1(job1);
			
			game.setPower1(power1);
			game.setMaxpower1(maxpower1);
			
			game.setUserName2(userName2);
			game.setUid2(uid2);
			game.setJob2(job2);
			
			game.setPower2(power2);
			game.setMaxpower2(maxpower2);
			
			game.setEnd1(end1);
			game.setEnd2(end2);
            return game;  
        }  
          
    }  
	

}
















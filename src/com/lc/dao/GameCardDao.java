package com.lc.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lc.GameCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class GameCardDao {

	private JdbcTemplate jdbcTemplate ;
	
		
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<GameCard> getGameCards(String gid, String uid, String type)
	{
		String searchQuery = "select * from gamecards where gid='"+gid+"' and uid='"+uid+"' and type='"+type+"'";
		System.out.println(searchQuery);
		
		List<GameCard> gameCardList=jdbcTemplate.query(searchQuery, new GameCardRowMapper());
		
		return gameCardList;
	}
	
	public int getCount(String gid, String uid, String type)
	{
		String searchQuery = "select count(*) from gamecards where gid='"+gid+"' and uid='"+uid+"' and type='"+type+"'";
		System.out.println(searchQuery);
		
		int count=this.jdbcTemplate.queryForInt(searchQuery);
		
		return count;
	}
	
	public void updateGameCards(List<GameCard> gameCards, String gid, String uid)
	{
		// first, get the previous game card list
		List<GameCard> oldGameCards=getGameCards(gid, uid, "hand");
		
		List<String> updateQueries=new LinkedList<String>();
		
		for(GameCard ogc: oldGameCards)
		{
			boolean found=false;
			
			for(GameCard gc: gameCards)
			{
				if(ogc.getId().equals(gc.getId()))
					found=true;
			}
			
			if(!found)
			{
				String updateQuery = "update gamecards set type='trash' " +
					"where gid='"+ogc.getGid()+"' and id='"+ogc.getId()+"'";
			
				updateQueries.add(updateQuery);
			}
		}
		
		for(String updateQuery: updateQueries)
		{
			System.out.println(updateQuery);
			this.jdbcTemplate.update(updateQuery);
		}
				
	}
	
	public GameCard takeCard(String gid, String uid)
	{
		
		String searchQuery = "select * from gamecards where gid='"+gid+"' and uid='"+uid+"' and type='pack' limit 1";
		System.out.println(searchQuery);
		
		List<GameCard> list=this.jdbcTemplate.query(searchQuery, new GameCardRowMapper());
		
		GameCard gameCard=null;
		if(list.size()>0)
		{
			gameCard=list.get(0);
			String updateQuery="update gamecards set type='hand' where id='"+gameCard.getId()+"' and gid='"+gameCard.getGid()+"'";
			System.out.println(updateQuery);
			
			this.jdbcTemplate.update(updateQuery);
		}
		
		return gameCard;
		
	}
	
	
	
	
	public class GameCardRowMapper implements RowMapper<GameCard> {  
		  
        @Override  
        public GameCard mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	GameCard gameCard=new GameCard();
			gameCard.setId(rs.getString(2)); // id
			gameCard.setGid(rs.getString(3)); // gid
			gameCard.setUid(rs.getString(4)); // uid
			
			gameCard.setCardName(rs.getString(5)); // cardname
			gameCard.setType(rs.getString(6)); // type
			gameCard.setState(rs.getString(7)); // state
			
			Map<String,Map<String,String>> outerState=new HashMap<String, Map<String,String>>();
			//outerState.put(gameCard.getId(), Helper.parseState(gameCard.getState()));
			gameCard.setOuterState(outerState);
            return gameCard;  
        }  
          
    }  
	

}
















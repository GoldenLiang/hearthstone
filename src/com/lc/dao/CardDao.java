package com.lc.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;


import com.lc.Card;
import com.lc.beans.CardCache;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import utils.SpringFactory;

public class CardDao {

	private JdbcTemplate jdbcTemplate ;
	
		
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Card> getAllCards() {
        
        String sql = "select * from cards";
        List<Card> cardList = jdbcTemplate.query(sql, new CardRowMapper()); 
                
        return cardList;
    }
	
	public List<Card> getCards()
	{
		
		String searchQuery = "select * from cards where groupcode!='英雄级' and groupcode!='附属级' order by cost asc";
		List<Card> cardList = jdbcTemplate.query(searchQuery, new CardRowMapper()); 
						
		return cardList;
	}
	
	public List<Card> getBaseCards(String job)
	{
		
		String searchQuery = "select * from cards where (job='"+job+"' or job='中立') and rate='基本' and groupcode!='英雄级' and groupcode!='附属级'";
		List<Card> cardList = jdbcTemplate.query(searchQuery, new CardRowMapper()); 
						
		return cardList;
	}
	
	public Card getCard(String name)
	{
		BeanFactory bf=SpringFactory.getFactory();
		CardCache sc=(CardCache)bf.getBean("springCards");
		
		return sc.getCard(name);
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	public void batchUpdate(String imageid,String name)
	{
		String sql="insert into card(imageid,name) values (?,?) ";
		jdbcTemplate.update(sql,imageid,name);
	}
	
	public class CardRowMapper implements RowMapper<Card> {  
		  
        @Override  
        public Card mapRow(ResultSet rs, int rowNum) throws SQLException {  
            Card card = new Card();  
            card.setImageid(rs.getString(1)); // imageid
			card.setName(rs.getString(2)); // name
			card.setType(rs.getString(3)); // type
			card.setJob(rs.getString(4)); // job
			card.setRate(rs.getString(5)); // rate
			card.setGroup(rs.getString(6)); // group
			card.setRace(rs.getString(7)); // race
			card.setCost(rs.getInt(8)); // cost
			card.setAttack(rs.getInt(9)); // attack
			card.setBlood(rs.getInt(10)); // blood
			card.setEffects(rs.getString(11)); // effects
			card.setSplit(rs.getInt(12)); // split
			card.setJoin(rs.getInt(13)); // join
			card.setEffectCode(rs.getString(14)); // effectcode
			card.setTargetCode(rs.getString(15)); // target
            return card;  
        }  
          
    }  
	

}
















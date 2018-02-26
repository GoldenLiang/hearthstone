package com.lc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.Helper;
import com.lc.Minion;

public class MinionDao {
	
	private JdbcTemplate jdbcTemplate ;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Minion> getMinions(String gid, String uid)
	{
		
		String searchQuery = "select * from minion where gid='"+gid+"' and uid='"+uid+"'";
		System.out.println(searchQuery);
		
		List<Minion> minionList=this.jdbcTemplate.query(searchQuery, new MinionRowMapper());
				
		return minionList;
	}	
	
	public void updateMinions( List<Minion> minions, String gid, String uid)
	{
		
		// delete the minions 
		String deleteQuery="delete from minion where gid='"+gid+"' and uid='"+uid+"'";
		System.out.println(deleteQuery);
				
		List<String> updateQueries=new LinkedList<String>();
		
		for(Minion minion: minions)
		{
			String updateQuery="insert into minion (id,gid,uid,cardname,imageid,race,attack,blood,maxblood,state,outstate,indexcount) " +
				"values('"+minion.getId()+"','"+minion.getGid()+"','"+
				minion.getUid()+"','"+minion.getCardName()+"','"+minion.getImageid()+"','"+
				((minion.getRace()!=null)?minion.getRace():"")+"',"+
				minion.getAttack()+","+minion.getBlood()+","+minion.getMaxBlood()+",'"+
				Helper.makeState(minion.getInnerState())+"','',"+minion.getIndex()+")";
			
			updateQueries.add(updateQuery);
			
			System.out.println(updateQuery);
		}
		
		this.jdbcTemplate.update(deleteQuery);
					
		for(String updateQuery: updateQueries)
		{
			this.jdbcTemplate.update(updateQuery);
		}
	}		
	
	
	public class MinionRowMapper implements RowMapper<Minion> {  
		  
        @Override  
        public Minion mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	Minion minion=new Minion();
        	
        	minion.setId(rs.getString(2)); // id
			minion.setGid(rs.getString(3)); // gid
			minion.setUid(rs.getString(4)); // uid
			minion.setCardName(rs.getString(5)); // cardname
			minion.setImageid(rs.getString(6)); // imageid
			minion.setRace(rs.getString(7)); // race
			minion.setAttack(rs.getInt(8)); // attack
			minion.setBlood(rs.getInt(9)); // blood
			minion.setMaxBlood(rs.getInt(10)); // maxBlood
			minion.setState(rs.getString(11)); // state
			minion.setOutState(rs.getString(12)); // outstate
			minion.setIndex(rs.getInt(13)); // index
			
			minion.setInnerState(Helper.parseState(minion.getState()));
			minion.setOuterState(new HashMap<String,Map<String,String>>());
			
            return minion;  
        }  
          
    }  
	
}
















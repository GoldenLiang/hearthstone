package com.lc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.Helper;

import com.lc.HeroMinion;

public class HeroMinionDao {

	private JdbcTemplate jdbcTemplate ;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public HeroMinion getHeroMinion(String gid, String uid)
	{
		
		String searchQuery = "select * from herominion where gid='" + gid + "' and id='"+uid+"'";
		System.out.println("Query: " + searchQuery);
				
		HeroMinion heroMinion=this.jdbcTemplate.queryForObject(searchQuery, new HeroMinionRowMapper());
		
		return heroMinion;
	}
	
	public void updateHeroMinion(HeroMinion heroMinion)
	{
		String updateQuery = "update herominion set attack="+heroMinion.getAttack()+
			", blood="+heroMinion.getBlood()+", guard="+heroMinion.getGuard()+ ", state='"+
			Helper.makeState(heroMinion.getInnerState())+			
			"' where gid='" + heroMinion.getGid() + "' and id='"+heroMinion.getId()+"'";
		
		
		System.out.println("Query: " + updateQuery);
				
		this.jdbcTemplate.update(updateQuery);
	}
	
	public class HeroMinionRowMapper implements RowMapper<HeroMinion> {  
		  
        @Override  
        public HeroMinion mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	HeroMinion heroMinion=new HeroMinion();
        	
        	heroMinion.setId(rs.getString(2));
			heroMinion.setGid(rs.getString(3));
			heroMinion.setJob(rs.getString(4));
			
			heroMinion.setAttack(rs.getInt(5));
			heroMinion.setBlood(rs.getInt(6));
			heroMinion.setGuard(rs.getInt(7));
			heroMinion.setState(rs.getString(8));
											
			heroMinion.setInnerState(Helper.parseState(heroMinion.getState()));
			
            return heroMinion;  
        }  
          
    }  
	
}













package com.lc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;


import com.lc.Hero;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class HeroDao {

	private JdbcTemplate jdbcTemplate ;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Hero getHero(String job)
	{
		String searchQuery = "select * from hero where job='" + job + "'";
		System.out.println("Query: " + searchQuery);
		
		Hero hero=this.jdbcTemplate.queryForObject(searchQuery, new HeroRowMapper());
		
		return hero;
		
	}
	
	public class HeroRowMapper implements RowMapper<Hero> {  
		  
        @Override  
        public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	Hero hero=new Hero();
			hero.setJob(rs.getString(1));
			hero.setName(rs.getString(2));
			hero.setBlood(rs.getInt(3));
			hero.setCost(rs.getInt(4));
			hero.setSkillName(rs.getString(5));
			hero.setSkillRemark(rs.getString(6));;
			
            return hero;  
        }  
          
    }  
}

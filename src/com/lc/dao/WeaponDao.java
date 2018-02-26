package com.lc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.Helper;
import com.lc.Weapon;

public class WeaponDao {
	private JdbcTemplate jdbcTemplate ;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Weapon getWeapon(String gid, String uid)
	{
		String searchQuery = "select * from weapon where gid='"+gid+"' and uid='"+uid+"'";
		System.out.println(searchQuery);
		
		Weapon weapon=this.jdbcTemplate.queryForObject(searchQuery, new WeaponRowMapper());
		
		return weapon;
	}
	
	public void updateWeapon(Weapon weapon, String id, String gid)
	{
		
		String udpateQuery = "update weapon set attack="+ weapon.getAttack() +", blood=" +
			weapon.getBlood()+", state='" +Helper.makeState(weapon.getInnerState())+"' " +
			" where gid='"+gid+"' and id='"+id+"'";
		
		System.out.println(udpateQuery);
						
		this.jdbcTemplate.update(udpateQuery);
	}		
	
	public class WeaponRowMapper implements RowMapper<Weapon> {  
		  
        @Override  
        public Weapon mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	Weapon weapon=new Weapon();
			
			weapon.setId(rs.getString(2)); // id
			weapon.setGid(rs.getString(3)); // gid
			weapon.setUid(rs.getString(4)); // uid
			weapon.setAttack(rs.getInt(5)); // attack
			weapon.setBlood(rs.getInt(6)); // blood
			weapon.setState(rs.getString(7)); // state
			
			weapon.setInnerState(Helper.parseState(weapon.getState()));
			weapon.setOuterState(new HashMap<String, Map<String,String>>());
			
            return weapon;  
        }  
          
    }  
	
	
}
















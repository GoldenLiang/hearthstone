package com.lc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import utils.Helper;

import com.lc.Event;

public class EventDao {

	private JdbcTemplate jdbcTemplate ;
	
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int createEvent(final Event event, final String gid, final String uid)
	{
		
		final String thisValueStr;
		final String targetStr;
		
		if(event.getThisValue()!=null)
			thisValueStr=Helper.makeString(event.getThisValue());
		else
			thisValueStr="";
		
		if(event.getTarget()!=null)
			targetStr=Helper.makeString(event.getTarget());
		else
			targetStr="";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();  
	    jdbcTemplate.update(new PreparedStatementCreator() {  
	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {  
	              
	            
	        	String insertQuery="insert into events (gid, uid, name, eventsource, eventtarget, thisvalue, target) " +
					"values(?,?,?,'','',?,?)";
	            PreparedStatement ps = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);  
	            ps.setString(1, gid);  
	            ps.setString(2, uid);  
	            ps.setString(3, event.getName());  
	            ps.setString(4, thisValueStr);  
	            ps.setString(5, targetStr);  
	             
	            return ps;  
	        }  
	    }, keyHolder);  
	      
	    int generatedId = keyHolder.getKey().intValue();   
	    return generatedId;  
		
		
	}
	
	public Event getEvent(int id) 
	{
			
		String query="select * from events where id="+id;
		System.out.println(query);
		
		Event event=jdbcTemplate.queryForObject(query, new EventRowMapper());
				
		return event;
	}
	
	public class EventRowMapper implements RowMapper<Event> {  
		  
        @Override  
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	Event event = new Event();  
        	event.setId(rs.getInt(1)); // id
			event.setGid(rs.getString(2)); // gid
			event.setUid(rs.getString(3)); // uid
			event.setName(rs.getString(4)); // name
			
			String eventSourceStr=rs.getString(5);
			if(eventSourceStr.equals(""))
				event.setEventSource(null);
			else
				event.setEventSource(Helper.makeValue(eventSourceStr)); // eventsource
			
			String eventTargetStr=rs.getString(6);
			if(eventTargetStr.equals(""))
				event.setEventTarget(null);
			else
				event.setEventTarget(Helper.makeValue(eventTargetStr)); // eventtarget
			
			String thisValueStr=rs.getString(7);
			if(thisValueStr.equals(""))
				event.setThisValue(null);
			else
				event.setThisValue(Helper.makeValue(thisValueStr)); // thisvalue
			
			String targetStr=rs.getString(8);
			if(targetStr.equals(""))
				event.setTarget(null);
			else
				event.setTarget(Helper.makeValue(targetStr)); // target
			
            return event;  
        }  
          
    }  
	
}




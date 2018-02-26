package com.lc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.lc.Event;
import com.lc.GameListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.SpringFactory;

public class GameListenerDao {
	
	private EventDao eventDao=(EventDao)SpringFactory.getFactory().getBean("eventDao");
	
	private JdbcTemplate jdbcTemplate ;
		
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<GameListener> getListeners(String gid, String uid)
	{
		
		String searchQuery = "select * from listeners where gid='"+gid+"' and uid='"+uid+"'";
		List<GameListener> listeners=this.jdbcTemplate.query(searchQuery, new GameListenerRowMapper());
				
		return listeners;
	}
	
	public void updateListeners(List<GameListener> listeners, String gid, String uid)
	{
				
		// first delete the listeners and events 
		String deleteListenersQuery = "delete from listeners where gid='"+gid+"' and uid='"+uid+"'";
		String deleteEventsQuery = "delete from events where gid='"+gid+"' and uid='"+uid+"'";
		
		this.jdbcTemplate.update(deleteListenersQuery);
		this.jdbcTemplate.update(deleteEventsQuery);
		
		// insert the events and update the listener event ids
		
		for(GameListener gl: listeners)
		{
			int eid=eventDao.createEvent(gl.getEvent(), gid, uid);
			gl.setEventId(eid);
			
			String eids="";
			for(Event removeEvent: gl.getRemoveEvents())
			{
				eid=eventDao.createEvent(removeEvent, gid, uid);
				eids+=String.valueOf(eid)+",";
			}
			
			eids=eids.substring(0, eids.length()-1);
			
			gl.setRemoveEventIds(eids);
			
		}
		
		// insert the listeners
		
		List<String> insertListenerQueries = new LinkedList<String>();
		
		for(GameListener gl: listeners)
		{
			String insertQuery="insert into listeners (gid, uid, eventid, removeeventids, effectcode) values('"+gid+"','"+uid+"',"+
				gl.getEventId()+",'"+gl.getRemoveEventIds()+"','"+gl.getEffectCode().replaceAll("'", "''")+"')";
			
			insertListenerQueries.add(insertQuery);
			
			System.out.println(insertQuery);
		}
		
		for(String insertQuery: insertListenerQueries)
		{
			this.jdbcTemplate.update(insertQuery);
		}
	}
	
	public class GameListenerRowMapper implements RowMapper<GameListener> {  
		  
        @Override  
        public GameListener mapRow(ResultSet rs, int rowNum) throws SQLException {  
        	GameListener listener=new GameListener();
			listener.setGid(rs.getString(2)); // gid
			listener.setUid(rs.getString(3)); // uid
			listener.setEventId(rs.getInt(4)); // eventid
			listener.setRemoveEventIds(rs.getString(5)); // removeeventids
			listener.setEffectCode(rs.getString(6)); // effect code
			
			
			Event event=eventDao.getEvent(listener.getEventId());
			listener.setEvent(event);
			
			List<Event> removeEvents=new LinkedList<Event>();
			String[] parts=listener.getRemoveEventIds().split(",");
			for(int i=0;i<parts.length;i++)
			{
				Event removeEvent=eventDao.getEvent(Integer.parseInt(parts[i]));
				removeEvents.add(removeEvent);
			}
			
			listener.setRemoveEvents(removeEvents);
			
            return listener;  
        }  
          
    }  
			
}





















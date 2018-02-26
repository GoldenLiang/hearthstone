package com.lc;

import java.util.List;

public class GameListener {
	
	private String gid;
	private String uid;
	
	private int eventId;
	private String removeEventIds;
	private String effectCode;
	
	private Event event;
	private List<Event> removeEvents;
	
		
	
	
	public List<Event> getRemoveEvents() {
		return removeEvents;
	}
	public void setRemoveEvents(List<Event> removeEvents) {
		this.removeEvents = removeEvents;
	}
	
	
	
	public String getRemoveEventIds() {
		return removeEventIds;
	}
	public void setRemoveEventIds(String removeEventIds) {
		this.removeEventIds = removeEventIds;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEffectCode() {
		return effectCode;
	}
	public void setEffectCode(String effectCode) {
		this.effectCode = effectCode;
	}
	
	
	
}

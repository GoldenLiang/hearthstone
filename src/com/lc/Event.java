package com.lc;

import com.lc.playing.Value;

public class Event {
	
	private int id;
	private String gid;
	private String uid;
	private String name;
	private Value eventSource;
	
	private Value thisValue;
	private Value eventTarget;
	private Value target;
	
	
	
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
	public Value getTarget() {
		return target;
	}
	public void setTarget(Value target) {
		this.target = target;
	}
	public Value getEventSource() {
		return eventSource;
	}
	public void setEventSource(Value eventSource) {
		this.eventSource = eventSource;
	}
	public Value getEventTarget() {
		return eventTarget;
	}
	public void setEventTarget(Value eventTarget) {
		this.eventTarget = eventTarget;
	}
	public void setThisValue(Value thisValue) {
		this.thisValue = thisValue;
	}
	public Value getThisValue() {
		return thisValue;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}

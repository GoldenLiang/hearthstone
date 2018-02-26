package com.lc.playing;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	
	private Scope parent;
	private Map<String, Object> keyValues;
	
	public Scope(Scope parent)
	{
		this.parent=parent;
		this.keyValues=new HashMap<String, Object>();
	}
	
	public void put(String key, Object value)
	{
		this.keyValues.put(key, value);
	}
	
}

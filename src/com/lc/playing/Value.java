package com.lc.playing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Value {

	public static  Value TRUE = new Value();
	public static  Value FALSE = new Value();
		
	private String type;
	private String stringValue;
	private int intValue;
	private List<String> set;
	
	public Value()
	{
		
	}
	
	public Value(Value value)
	{
		this.setType(value.type);
		this.setStringValue(value.stringValue);
		this.setIntValue(value.intValue);
				
		if(value.set!=null)
		{
			List<String> nset=new LinkedList<String>();
			nset.addAll(value.set);
			
			this.setSet(nset);
		}
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public List<String> getSet() {
		return set;
	}

	public void setSet(List<String> set) {
		this.set = set;
	}

	public boolean equals(Value value)
	{
		if(!this.type.equals(value.type))
			return false;
		
		if(this.type.equals("string") &&  this.stringValue.equals(value.stringValue))
			return true;
		else if(this.type.equals("int") && this.intValue==value.intValue)
			return true;
		else if(this.type.equals("set")  )
		{
			if(this.set.size()==1 && value.set.size()==1)
			{
				String v1=this.set.get(0);
				String v2=value.set.get(0);
				
				if( v1 .equals(v2) )
				{
					return true;
				}
			}
			else if(this.set.size()==0 && value.set.size()==0)
				return true;
			
		}
			
				
		return false;
	}
	
	public boolean biggerThan(Value value)
	{
		if(!this.type.equals(value.type))
			return false;
		
		if(this.type.equals("int") &&  this.intValue>value.intValue)
			return true;
				
		return false;
	}
	
	public static Value union(Value v1, Value v2)
	{
				
		if(v1==null || v2==null || !v1.type.equals("set") ||  !v2.type.equals("set") )
			return null;
		
		Value nValue=new Value();
		
		nValue.type="set";	
		nValue.set=new LinkedList<String>();
		
		nValue.set.addAll(v1.set);
		nValue.set.addAll(v2.set);
			
		return nValue;	
	}
	
	public String toString()
	{
		
		String ret=this.type+": ";
		
		if(this.type.equals("set") && this.set!=null)
		{
			ret+="[";
			for(String s: set)
			{
				ret+=s+",";
			}
			ret+="]";
		}
		else if(this.type.equals("string"))
			ret+=this.stringValue;
		else if(this.type.equals("int"))
			ret+=this.intValue;
		
		return ret;
	}
	
	public boolean generalEquals(Value value)
	{
		if(this.type.equals("set") && value.type.equals("set") && this.set.size()==value.set.size() )
		{
			
			
			for(String id: this.set)
			{
				boolean found=false;
				
				for(String id2: value.set)
				{
					if(id.equals(id2))
					{
						found=true;
						break;
					}
				}
				
				if(!found)
					return false;
				
			}
			
			return true;
			
		}
		
		return false;
	}
	
	public static void main(String args[])
	{
		System.out.println(Value.TRUE==Value.FALSE);
		//System.out.println(Value.FALSE);
	}
	
}










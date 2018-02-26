package com.lc;

import java.util.List;

public class Target {
	
	private String type;
	private List<String> names; 
	private String name;
	
	public Target(String targetStr)
	{
		parse(targetStr);
	}
	
	private void parse(String code)
	{
		code=Utils.clearBlanks(code);
		
		// choice
		if(code.startsWith("choice"))
		{
			this.type="choice";
			
			List<String> params=Utils.extractParams(code.substring(7,code.length()-1));
			this.names=params;
		}
		else if(code.startsWith("random"))
		{
			this.type="random";
			
			List<String> params=Utils.extractParams(code.substring(7,code.length()-1));
			this.names=params;
		}
		else
		{
			this.type="single";
			this.name=code;
		}
	}
	
	public void print()
	{
		
		
		System.out.println("type: "+this.type);
		
		if(this.type.equals("choice"))
		{
			for(String name: this.names)
			{
				System.out.println(name);
			}
		}
		else if(this.type.equals("random"))
		{
			for(String name: this.names)
			{
				System.out.println(name);
			}
		}
		else if(this.type.equals("single"))
		{
			System.out.println(this.name);
			
		}
		
		
	}
}


















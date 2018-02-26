package com.lc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lc.playing.EvaluateException;

public class Variable {
	
	private String name;
	
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Variable(String code)
	{
		parse(code);
	}
	
	public void parse(String code) 
	{
		this.name=code;
	}
	
	public static boolean tryParse(String code) 
	{
		Pattern pattern=Pattern.compile("^\\$(\\$|\\w+)$");
		Matcher m=pattern.matcher(code);
		
		if(!m.find())
			return false;
		
		return true;
	}
	
	public static void main(String args[]) throws EvaluateException
	{
		Variable.tryParse("$$");
		Variable.tryParse("$aaaaa");
		Variable.tryParse("abc");
		Variable.tryParse("$$a");
	}
	
	public String toString()
	{
		return name;
	}
}






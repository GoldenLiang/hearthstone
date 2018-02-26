package com.lc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lc.playing.EvaluateException;

public class Constant {
	
	private String type;
	private String stringValue;
	private int intValue;
	
	
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	public Constant(String code)
	{
		parse(code);
	}
	
	public void parse(String code) 
	{
		if(code.matches("(-|)\\d+"))
		{
			this.type="int";
			this.intValue=Integer.parseInt(code);
		}
		else
		{
			this.type="string";
			this.stringValue=code;
			
		}
		
	}
	
	public static boolean tryParse(String code) 
	{
		Pattern pattern=Pattern.compile("^((-|)\\w+|\\'.*\\')$");
		Matcher m=pattern.matcher(code);
		
		if(!m.find())
			return false;
		
		return true;
	}
	
	public static void main(String args[]) throws EvaluateException
	{
		Constant.tryParse("adf");
		Constant.tryParse("aaaaa");
		Constant.tryParse("'abc'");
		Constant.tryParse("-2");
		Constant.tryParse("'野兽'aa");
	}
	
	public String toString()
	{
		String ret=this.type;
		if(this.type.equals("int"))
			ret+=": "+this.intValue;
		else
			ret+=": "+this.stringValue;
		return ret;
	}
}





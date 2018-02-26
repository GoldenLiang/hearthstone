package com.lc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lc.playing.EvaluateException;

public class VariableDotConstant {
	private Variable v;
	private Constant c;
	
	public Variable getV() {
		return v;
	}

	public void setV(Variable v) {
		this.v = v;
	}

	public Constant getC() {
		return c;
	}

	public void setC(Constant c) {
		this.c = c;
	}

	public VariableDotConstant(String code)
	{
		parse(code);
	}
	
	public void parse(String code) 
	{
		Pattern pattern=Pattern.compile("^(\\$(\\$|\\w+))\\.(\\w+)$");
		Matcher m=pattern.matcher(code);
		
		m.find();
		this.v=new Variable(m.group(1));
		this.c=new Constant(m.group(3));
	}
	
	public static boolean tryParse(String code) 
	{
		Pattern pattern=Pattern.compile("^(\\$(\\$|\\w+))\\.(\\w+)$");
		Matcher m=pattern.matcher(code);
		
		if(!m.find())
			return false;
		else
		{
			return true;
		}
	}
	
	public static void main(String args[]) throws EvaluateException
	{
		VariableDotConstant.tryParse("$aaa.sources");
		
	}
	
	public String toString()
	{
		return ""+v+"."+c;
	}
}

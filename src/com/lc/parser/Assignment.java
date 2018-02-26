package com.lc.parser;

public class Assignment {
	
	private Variable v;
	private Expression e;
	
	
	
	public Variable getV() {
		return v;
	}

	public void setV(Variable v) {
		this.v = v;
	}

	public Expression getE() {
		return e;
	}

	public void setE(Expression e) {
		this.e = e;
	}

	public Assignment(String code)
	{
		this.v=null;
		this.e=null;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("Assignment: "+code);;
		
		int equalIndex=code.indexOf("=");
		
		this.v=new Variable(code.substring(0,equalIndex));
		this.e=new Expression(code.substring(equalIndex+1));
	}
	
	public String toString()
	{
		return ""+this.v+"="+this.e;
	}
	
}












package com.lc.parser;

public class Statement {
	
	private Assignment a;
	
	private FunctionCall fc;
	private IfStatement is;
	
	private String type;
	
	
	public Assignment getA() {
		return a;
	}

	public void setA(Assignment a) {
		this.a = a;
	}

	public FunctionCall getFc() {
		return fc;
	}

	public void setFc(FunctionCall fc) {
		this.fc = fc;
	}

	public IfStatement getIs() {
		return is;
	}

	public void setIs(IfStatement is) {
		this.is = is;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Statement(String code)
	{
		this.a=null;
		
		this.fc=null;
		this.is=null;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("Statement: "+code);
		
		if(code.startsWith("if("))
		{
			this.type="ifstatement";
			this.is=new IfStatement(code);
		}
		
		else if(isAssignment(code))
		{
			this.type="assignment";
			this.a=new Assignment(code);
		}
		else
		{
			this.type="functioncall";
			this.fc=new FunctionCall(code);
		}
	}
	
	private boolean isAssignment(String code)
	{
		int i=0;
		
		int bracketCount1=0;
		int bracketCount2=0;
		while(i<code.length() && ( code.charAt(i)!='=' || bracketCount1 > 0 || bracketCount2>0) )
		{
			
			if(code.charAt(i)=='{')
				bracketCount1++;
			else if(code.charAt(i)=='}')
				bracketCount1--;
			if(code.charAt(i)=='(')
				bracketCount2++;
			else if(code.charAt(i)==')')
				bracketCount2--;
			
			i++;
		}
		
		if(i<code.length())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public String toString()
	{
		if(this.type.equals("ifstatement"))
		{
			return this.is.toString();
		}
		
		else if(this.type.equals("assignment"))
		{
			return this.a.toString();
		}
		else
		{
			return this.fc.toString();
		}
			
	}
	
}


















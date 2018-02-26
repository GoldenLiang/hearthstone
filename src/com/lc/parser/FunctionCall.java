package com.lc.parser;

public class FunctionCall {
	
	private String name;
	private ExpressionList expressionList;
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExpressionList getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(ExpressionList expressionList) {
		this.expressionList = expressionList;
	}

	public FunctionCall(String code)
	{
		this.name=null;
		this.expressionList=null;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("FunctionCall: " +code);
		
		int bracketIndex=code.indexOf("(");
		
		this.name=code.substring(0,bracketIndex);
		this.expressionList=new ExpressionList(code.substring(bracketIndex+1,code.length()-1));
	}
	
	public String toString()
	{
		return ""+this.name+"("+ this.expressionList +")";
	}
}











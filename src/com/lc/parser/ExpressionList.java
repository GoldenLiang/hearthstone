package com.lc.parser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionList {
	
	private Expression expression;
	private ExpressionList remainingList;
	
	
	
	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public ExpressionList getRemainingList() {
		return remainingList;
	}

	public void setRemainingList(ExpressionList remainingList) {
		this.remainingList = remainingList;
	}

	public ExpressionList(String code)
	{
		expression=null;
		remainingList=null;
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("ExpressionList: "+code);
		
		int i=0;
		
		int bracketCount=0;
		while(i<code.length() && ( code.charAt(i)!=',' || bracketCount > 0) )
		{
			
			if(code.charAt(i)=='(')
				bracketCount++;
			else if(code.charAt(i)==')')
				bracketCount--;
			
			i++;
		}
		
		if(i<code.length())
		{
			String code1=code.substring(0,i);
			String code2=code.substring(i+1);
			
			expression=new Expression(code1);
			remainingList=new ExpressionList(code2);
		}
		else
		{
			expression=new Expression(code);
		}
		
		
	}
	
	public String toString()
	{
		String ret="[";
		ret+=this.expression;
		if(this.remainingList!=null)
			ret+=this.remainingList;
		ret+="]";
		
		return ret;
	}
}


















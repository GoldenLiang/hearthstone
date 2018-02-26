package com.lc.parser;

public class Block {
	
	private StatementList statementList;
	
	
	
	public StatementList getStatementList() {
		return statementList;
	}

	public void setStatementList(StatementList statementList) {
		this.statementList = statementList;
	}

	public Block(String code)
	{
		this.statementList=null;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("Block: "+code);
		this.statementList=new StatementList(code.substring(1,code.length()-1));
	}
	
	public String toString()
	{
		return "{"+this.statementList+"}";
	}
}

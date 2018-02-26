package com.lc.parser;

public class IfStatement {

	private Expression expression;
	private Block block;
	
	
	
	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public IfStatement(String code)
	{
		this.expression=null;
		this.block=null;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		int blockIndex=code.indexOf("{");
		
		this.expression=new Expression(code.substring(3,blockIndex-1));
		this.block=new Block(code.substring(blockIndex));
	}
	
	public String toString()
	{
		return "if("+this.expression+")"+this.block;
	}
	
	
	
	
}

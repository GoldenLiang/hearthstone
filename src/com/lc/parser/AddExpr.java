package com.lc.parser;

public class AddExpr {
	
	private Expression left;
	private Expression right;
	
	
	
	
	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}

	public AddExpr(String code)
	{
		this.left=null;
		this.right=null;
		parse(code);
	}
	
	public void parse(String code)
	{
		int opIndex=code.indexOf("+");
		this.left=new Expression(code.substring(0,opIndex));
		this.right=new Expression(code.substring(opIndex+1));
		
	}
	
	public static boolean  tryParse(String code) 
	{
		int opIndex=code.indexOf("+");
		if(opIndex<0)
			return false;
		
		
		String leftCode=code.substring(0, opIndex);
		String rightCode=code.substring(opIndex+1);
		
		if( (Variable.tryParse(leftCode) || Constant.tryParse(leftCode) || VariableDotConstant.tryParse(leftCode)) &&
				(Variable.tryParse(rightCode) || Constant.tryParse(rightCode) || VariableDotConstant.tryParse(rightCode)))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
}





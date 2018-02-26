package com.lc.parser;



public class ConditionalExpr {

	private Expression left;
	private Expression right;
	private String comparator;
	
	
	
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

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public ConditionalExpr(String code)
	{
		this.left=null;
		this.right=null;
		this.comparator=null;
		
		parse(code);
		
	}
	
	public static boolean  tryParse(String code) 
	{
		// first find the !=,==,<=,>=,>,<
		
		int opIndex=-1;
		int opLength=0;
		
		if(code.indexOf("!=")>=0)
		{
			opIndex=code.indexOf("!=");
			opLength=2;
		}
		else if(code.indexOf("==")>=0)
		{
			opIndex=code.indexOf("==");
			opLength=2;
		}
		else if(code.indexOf("<=")>=0)
		{
			opIndex=code.indexOf("<=");
			opLength=2;
		}
		else if(code.indexOf(">=")>=0)
		{
			opIndex=code.indexOf(">=");
			opLength=2;
		}
		else if(code.indexOf("<")>=0)
		{
			opIndex=code.indexOf("<");
			opLength=1;
		}
		else if(code.indexOf(">")>=0)
		{
			opIndex=code.indexOf(">");
			opLength=1;
		}
		
		System.out.println("opIndex:"+opIndex);
				
		if(opIndex<0)
			return false;
		
		String leftCode=code.substring(0,opIndex);
		String rightCode=code.substring(opIndex+opLength);
		
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
	
	public void parse(String code)
	{
		int opIndex=-1;
		int opLength=0;
		
		if(code.indexOf("!=")>=0)
		{
			opIndex=code.indexOf("!=");
			opLength=2;
		}
		else if(code.indexOf("==")>=0)
		{
			opIndex=code.indexOf("==");
			opLength=2;
		}
		else if(code.indexOf("<=")>=0)
		{
			opIndex=code.indexOf("<=");
			opLength=2;
		}
		else if(code.indexOf(">=")>=0)
		{
			opIndex=code.indexOf(">=");
			opLength=2;
		}
		else if(code.indexOf("<")>=0)
		{
			opIndex=code.indexOf("<");
			opLength=1;
		}
		else if(code.indexOf(">")>=0)
		{
			opIndex=code.indexOf(">");
			opLength=1;
		}
		
		String leftCode=code.substring(0,opIndex);
		String rightCode=code.substring(opIndex+opLength);
		
		this.left=new Expression(leftCode);
		this.right=new Expression(rightCode);
		
		this.comparator=code.substring(opIndex,opIndex+opLength);
		
	}
	
	public String toString()
	{
		return this.left+this.comparator+this.right;
	}
	
}










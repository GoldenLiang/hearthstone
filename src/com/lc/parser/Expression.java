package com.lc.parser;


public class Expression {
	
	private String type;
	private FunctionCall fc;
	private Variable v;
	private Constant c;
	private Block b;
	private VariableDotConstant vdc;
	private ConditionalExpr ce;
	private AddExpr ae;
	
	
	
	public AddExpr getAe() {
		return ae;
	}

	public void setAe(AddExpr ae) {
		this.ae = ae;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FunctionCall getFc() {
		return fc;
	}

	public void setFc(FunctionCall fc) {
		this.fc = fc;
	}

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

	public Block getB() {
		return b;
	}

	public void setB(Block b) {
		this.b = b;
	}

	public VariableDotConstant getVdc() {
		return vdc;
	}

	public void setVdc(VariableDotConstant vdc) {
		this.vdc = vdc;
	}

	public ConditionalExpr getCe() {
		return ce;
	}

	public void setCe(ConditionalExpr ce) {
		this.ce = ce;
	}

	public Expression(String code)
	{
		this.fc=null;
		this.v=null;
		this.c=null;
		this.vdc=null;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("Expression: "+code);
		
		
		if(Variable.tryParse(code))
		{
			this.type="variable";
			this.v=new Variable(code);
			return;
		}	
		else if(Constant.tryParse(code))
		{
			this.type="constant";
			this.c=new Constant(code);
			return;
		}
		else if(VariableDotConstant.tryParse(code))
		{
			this.type="variabledotconstant";
			this.vdc=new VariableDotConstant(code);
			return;
			
		} 
		
		else if(ConditionalExpr.tryParse(code))
		{
			this.type="conditionalexpr";
			this.ce=new ConditionalExpr(code);
			return;
			
		} 
		else if(AddExpr.tryParse(code))
		{
			this.type="addexpr";
			this.ae=new AddExpr(code);
			return;
			
		} 
		
		else if(code.startsWith("{"))
		{
			this.type="block";
			this.b=new Block(code);
		}
		else
		{
			this.type="functioncall";
			this.fc=new FunctionCall(code);
		}
	}
	
	public String toString()
	{
		String ret="";
		
		if(this.type.equals("variable"))
			ret+=this.v;
		else if(this.type.equals("constant"))
			ret+=this.c;
		else if(this.type.equals("variabledotconstant"))
			ret+=this.vdc;
		else if(this.type.equals("block"))
			ret+=this.b;
		else if(this.type.equals("conditionalexpr"))
			ret+=this.ce;
		else
			ret+=this.fc;
		
		return ret;
	}
	
	
	
	
	
}
















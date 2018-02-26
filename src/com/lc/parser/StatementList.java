package com.lc.parser;

public class StatementList {

	private Statement statement;
	private StatementList remainingList;
	private String code;
	
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public StatementList getRemainingList() {
		return remainingList;
	}

	public void setRemainingList(StatementList remainingList) {
		this.remainingList = remainingList;
	}

	public StatementList(String code)
	{
		this.statement=null;
		this.remainingList=null;
		this.code=code;
		
		parse(code);
	}
	
	public void parse(String code)
	{
		//System.out.println("StatementList: "+code);
		
		int i=0;
		
		int bracketCount=0;
		while(i<code.length() && ( code.charAt(i)!=';' || bracketCount > 0) )
		{
			
			if(code.charAt(i)=='{')
				bracketCount++;
			else if(code.charAt(i)=='}')
				bracketCount--;
			
			i++;
		}
		
		if(i<code.length())
		{
			String code1=code.substring(0,i);
			String code2=code.substring(i+1);
			
			this.statement=new Statement(code1);
			this.remainingList=new StatementList(code2);
		}
		else
		{
			this.statement=new Statement(code);
		}
	}
	
	public String toString()
	{
		String ret="";
		
		ret+=this.statement;
		
		if(this.remainingList!=null)
		{
			ret+=";"+this.remainingList;
		}
		
		return ret;
	}
	
	public static void main(String args[])
	{
		String code="call('召唤镜像')";
		String code2="damage(choice(selfminions,enemyminions,enmeyhero),6)";
		String code3="$aa.source";
		String code4="'鱼人'";
		String code5="choice(selfhero,selfminions,enemyminions,enmeyhero)";
		
		//Expression expr=new Expression(code2);
		
		String code6="minioncreateevent(if($$.source.race='野兽'){takecard(1)})";
		
		StatementList sl=new StatementList(code6);
		
		
		System.out.println(sl);
	}

}












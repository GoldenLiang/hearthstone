package com.lc;

import java.util.ArrayList;
import java.util.List;


public class Effect 
{
	
	private String type;
	private List<Effect> effectList;
	private int count;
	private Target target;
	private String holdTime;
	private List<String> cardNames;	
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Effect> getEffectList() {
		return effectList;
	}

	public void setEffectList(List<Effect> effectList) {
		this.effectList = effectList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public List<String> getCardNames() {
		return cardNames;
	}

	public void setCardNames(List<String> cardNames) {
		this.cardNames = cardNames;
	}

	public Effect(String effectCode)
	{
		
		parse(effectCode);
	}
	
	private void parse(String code)
	{
		code=Utils.clearBlanks(code);
		
		// union
		if(code.startsWith("union"))
		{
			this.type="union";
			
			List<String> params=Utils.extractParams(code.substring(6,code.length()-1));
			
			this.effectList=new ArrayList<Effect>();
			for(String param: params)
			{
				this.effectList.add(new Effect(param));
			}
		}
		
		// guardadd
		if(code.startsWith("guardadd"))
		{
			this.type="guardadd";
			List<String> params=Utils.extractParams(code.substring(9,code.length()-1));
			
			this.target=new Target(params.get(0));
			this.count=Integer.parseInt(params.get(1));
			this.holdTime=params.get(2);
		}
		
		// attackadd
		if(code.startsWith("attackadd"))
		{
			this.type="attackadd";
			List<String> params=Utils.extractParams(code.substring(10,code.length()-1));
			
			this.target=new Target(params.get(0));
			this.count=Integer.parseInt(params.get(1));
			this.holdTime=params.get(2);
		}
		
		// takecard
		if(code.startsWith("takecard"))
		{
			this.type="takecard";
			List<String> params=Utils.extractParams(code.substring(9,code.length()-1));
			
			this.count=Integer.parseInt(params.get(0));
			
		}
		// damage
		if(code.startsWith("damage"))
		{
			this.type="damage";
			List<String> params=Utils.extractParams(code.substring(7,code.length()-1));
			
			this.target=new Target(params.get(0));
			this.count=Integer.parseInt(params.get(1));
			
		}
		
		// cure
		if(code.startsWith("cure"))
		{
			this.type="cure";
			List<String> params=Utils.extractParams(code.substring(5,code.length()-1));
			
			this.target=new Target(params.get(0));
			this.count=Integer.parseInt(params.get(1));
		}
		
		// randomcall
		if(code.startsWith("randomcall"))
		{
			this.type="randomcall";
			List<String> params=Utils.extractParams(code.substring(11,code.length()-1));
			
			this.cardNames=params;
			
		}
		
		// taunt
		if(code.startsWith("taunt"))
		{
			this.type="taunt";
			List<String> params=Utils.extractParams(code.substring(6,code.length()-1));
			
			this.target=new Target(params.get(0));
			
		}
		// castdamageadd
		if(code.startsWith("castdamageadd"))
		{
			this.type="castdamageadd";
			List<String> params=Utils.extractParams(code.substring(14,code.length()-1));
			
			this.target=new Target(params.get(0));
			this.count=Integer.parseInt(params.get(1));
			
		}
	}
	
	
	
	public void print()
	{
		
		
		System.out.println("type: "+this.type);
		
		if(this.type.equals("union"))
		{
			for(Effect effect: this.effectList)
			{
				effect.print();
			}
		}
		else if(this.type.equals("guardadd"))
		{
			this.target.print();
			System.out.println(this.count);
			System.out.println(this.holdTime);
		}
		else if(this.type.equals("attackadd"))
		{
			this.target.print();
			System.out.println(this.count);
			System.out.println(this.holdTime);
		}
		else if(this.type.equals("takecard"))
		{
			System.out.println(this.count);
		}
		else if(this.type.equals("damage"))
		{
			this.target.print();
			System.out.println(this.count);
			
		}
		else if(this.type.equals("cure"))
		{
			this.target.print();
			System.out.println(this.count);
			
		}
		else if(this.type.equals("randomcall"))
		{
			for(String name: this.cardNames)
			{
				System.out.println(name);
			}
						
		}
		else if(this.type.equals("taunt"))
		{
			this.target.print();		
		}
		else if(this.type.equals("castdamageadd"))
		{
			this.target.print();
			System.out.println(this.count);
		}
		
	}
	
	public static void main(String args[])
	{
		String effectcode="castdamageadd(self,1)";
		
		Effect effect=new Effect(effectcode);
		
		effect.print();
	}
}






















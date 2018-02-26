package com.lc.playing;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.lc.Card;
import com.lc.Event;
import com.lc.GameListener;
import utils.SpringFactory;

import com.lc.dao.CardDao;
import com.lc.parser.AddExpr;
import com.lc.parser.Block;
import com.lc.parser.ConditionalExpr;
import com.lc.parser.Constant;
import com.lc.parser.Expression;
import com.lc.parser.ExpressionList;
import com.lc.parser.FunctionCall;
import com.lc.parser.IfStatement;
import com.lc.parser.Statement;
import com.lc.parser.StatementList;
import com.lc.parser.Variable;
import com.lc.parser.VariableDotConstant;

public class Evaluator {

		
	public static void evaluateCard(Card card, int index, Player player, Player enemy)
	{
		String effectCode=card.getEffectCode().replaceAll("\\s+", "");
		
		if(effectCode.equals(""))
			return;
		
		StatementList statementList=new StatementList(effectCode);
				
		evaluateStatementList(statementList, player, enemy, null, index);
	}
	
	public static void evaluateListener(GameListener gl, Player player, Player enemy)
	{
		String effectCode=gl.getEffectCode().replaceAll("\\s+", "");
		
		if(effectCode.equals(""))
			return;
		
		StatementList statementList=new StatementList(effectCode);
				
		evaluateStatementList(statementList, player, enemy, gl.getEvent(), 0);
	}
	
	public static Value evaluateTarget(Card card, Player player, Player enemy)
	{
		if(card.getTargetCode().equals(""))
		{
			Value emptyValue=new Value();
			emptyValue.setType("set");
			emptyValue.setSet(new LinkedList<String>());
			return emptyValue;
		}
		
		String targetCode=card.getTargetCode().replaceAll("\\s+", "");
		Expression expression=new Expression(targetCode);
		
		return evaluateExpression(expression,player,enemy,null, 0);
	}
	
	private static void evaluateStatementList(StatementList statementList, Player player, Player enemy, Event event, int index)
	{
		
		evaluateStatement(statementList.getStatement(), player, enemy, event, index);
		
		if(statementList.getRemainingList()!=null)
			evaluateStatementList(statementList.getRemainingList(), player, enemy, event, index);
		
	}
	
	private static void evaluateStatement(Statement statement, Player player, Player enemy, Event event, int index)
	{
		
		if(statement.getType().equals("ifstatement"))
		{
			evaluateIfStatement(statement.getIs(),player, enemy, event, index);
		}
		
		else if(statement.getType().equals("functioncall"))
		{
			evaluateFunctionCall(statement.getFc(),player, enemy, event, index);
		}
		
			
	}
	
	private static void evaluateIfStatement(IfStatement ifStatement, Player player, Player enemy, Event event, int index)
	{
		Value conditionResult=evaluateExpression(ifStatement.getExpression(),player,enemy, event, index);				
		
		if(conditionResult==Value.TRUE)
		{
			evaluateBlock(ifStatement.getBlock(),player,enemy, event, index);
		}
		
		
	}
	
	private static void evaluateBlock(Block block, Player player, Player enemy, Event event, int index)
	{
		evaluateStatementList(block.getStatementList(),player,enemy, event, index);
	}
	
	private static Value evaluateExpression(Expression expression, Player player, Player enemy, Event event, int index)
	{
		if(expression.getType().equals("conditionalexpr"))
		{
			return evaluateConditionalExpr(expression.getCe(),player,enemy, event, index);
		}
		else if(expression.getType().equals("functioncall"))
		{
			return evaluateFunctionCall(expression.getFc(),player,enemy, event, index);
		}
		else if(expression.getType().equals("variable"))
		{
			return evaluateVariable(expression.getV(),player,enemy, event, index);
		}
		else if(expression.getType().equals("constant"))
		{
			return evaluateConstant(expression.getC(),player,enemy, event, index);
		}
		else if(expression.getType().equals("variabledotconstant"))
		{
			return evaluateVariableDotConstant(expression.getVdc(), player, enemy,event, index);
		}
		else if(expression.getType().equals("addexpr"))
		{
			return evaluateAddExpr(expression.getAe(), player, enemy,event, index);
		}
		
		return null;
	}
	
	public static Value evaluateAddExpr(AddExpr ae, Player player, Player enemy, Event event, int index)
	{
		Value leftValue=evaluateExpression(ae.getLeft(),player, enemy, event, index);
		Value rightValue=evaluateExpression(ae.getRight(),player, enemy, event, index);
		
		Value ret=new Value();
		ret.setType("int");
		ret.setIntValue(leftValue.getIntValue()+rightValue.getIntValue());
		return ret;
	}
	
	public static Value evaluateConditionalExpr(ConditionalExpr ce, Player player, Player enemy, Event event, int index)
	{
		Value leftValue=evaluateExpression(ce.getLeft(),player, enemy, event, index);
		Value rightValue=evaluateExpression(ce.getRight(),player, enemy, event, index);
		
//		System.out.println("left:"+leftValue);
//		System.out.println("right:"+rightValue);
//		System.out.println(ce.getComparator());
		
		if(leftValue==null || rightValue==null)
			return Value.FALSE;
		
		if(ce.getComparator().equals("=="))
		{
			if(leftValue.equals(rightValue))
			{
				return Value.TRUE;
			}
		}
		else if(ce.getComparator().equals("!="))
		{
						
			if(!leftValue.equals(rightValue))
			{
				return Value.TRUE;
			}
		}
		else if(ce.getComparator().equals(">="))
		{
			if(leftValue.biggerThan(rightValue) || leftValue.equals(rightValue))
			{
				return Value.TRUE;
			}
		}
		else if(ce.getComparator().equals("<="))
		{
			if(!leftValue.biggerThan(rightValue))
			{
				return Value.TRUE;
			}
		}
		else if(ce.getComparator().equals(">"))
		{
			if(leftValue.biggerThan(rightValue))
			{
				return Value.TRUE;
			}
		}
		else if(ce.getComparator().equals("<"))
		{
			if(rightValue.biggerThan(leftValue))
			{
				return Value.TRUE;
			}
		}
		
		return Value.FALSE;
	}
	
	
	
	private static Value evaluateFunctionCall(FunctionCall functionCall, Player player, Player enemy, Event event, int index) 
	{
		if(functionCall.getName().equals("choice"))
		{
			Value targetSet=unionValues(functionCall.getExpressionList(),player,enemy, event, index);
			
			if(targetSet==null || targetSet.getSet().size()==0)
				return null;
			else
				return targetSet;
		}
		else if(functionCall.getName().equals("choiceornone"))
		{
			Value targetSet=unionValues(functionCall.getExpressionList(),player,enemy, event, index);
			
			return targetSet;
		}
		else if(functionCall.getName().equals("doublechoice"))
		{
			Value targetSet=unionValues(functionCall.getExpressionList(),player,enemy, event, index);
			
			if(targetSet==null || targetSet.getSet().size()<2)
				return null;
			else 
				return targetSet;
		}
		
		else if(functionCall.getName().equals("union"))
		{
			Value targetSet=unionValues(functionCall.getExpressionList(),player,enemy, event, index);
			
			return targetSet;
		}
		else if(functionCall.getName().equals("notempty"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			
			if(target.getSet().size()>0)
				return Value.TRUE;
			else
				return Value.FALSE;
		}
		else if(functionCall.getName().equals("filter"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			ConditionalExpr condition=functionCall.getExpressionList().getRemainingList().getExpression().getCe();
			
			Value ret=new Value();
			ret.setType("set");
			ret.setSet(new LinkedList<String>());
			
			for(String id: target.getSet())
			{
				Value oldPlayerValue=player.getCurrentSource();
				Value oldEnemyValue=enemy.getCurrentSource();
				
				if(player.setContext(id))
				{
					if(evaluateConditionalExpr(condition,player, enemy, event, index)==Value.TRUE)
					{
						ret.getSet().add(id);
					}
				}
				
				if(enemy.setContext(id))
				{
					if(evaluateConditionalExpr(condition,enemy, player, event, index)==Value.TRUE)
					{
						ret.getSet().add(id);
					}
				}
				
				player.setCurrentSource(oldPlayerValue);
				player.setCurrentSource(oldEnemyValue);
			}
			
			//System.out.println("filter:"+ret);
			
			return ret;
		}
		else if(functionCall.getName().equals("call"))
		{
			Value cardNameValue=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			String cardName=cardNameValue.getStringValue().substring(1, cardNameValue.getStringValue().length()-1);
			
			CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
			Card card=cardDao.getCard(cardName);
			player.useCard(card, index);
		}
		else if(functionCall.getName().equals("replace"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value cardNameValue=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy, event, index);
			
			index=player.destroy(target);
			
			String cardName=cardNameValue.getStringValue().substring(1, cardNameValue.getStringValue().length()-1);
			
			CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
			Card card=cardDao.getCard(cardName);
			
			if(player.isMyMinion(target))
				player.useCard(card, index);
			else
				enemy.useCard(card, index);
		}
		else if(functionCall.getName().equals("randomcall"))
		{
			List<String> cardNames=new LinkedList<String>();
			Value cardNameValue=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			String cardName=cardNameValue.getStringValue();
			
			cardNames.add(cardName.substring(1, cardName.length()-1));
			
			ExpressionList remainingList=functionCall.getExpressionList().getRemainingList();
			while(remainingList!=null)
			{
				cardNameValue=evaluateExpression(remainingList.getExpression(),player,enemy, event, index);
				cardName=cardNameValue.getStringValue();
				cardNames.add(cardName.substring(1, cardName.length()-1));
				remainingList=remainingList.getRemainingList();
			}
			
			Random random=new Random();
			int r=random.nextInt(cardNames.size());
			
			CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
			Card card=cardDao.getCard(cardNames.get(r));
			
			
			player.useCard(card, index);
		}
		else if(functionCall.getName().equals("taunt"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			player.taunt(target);
			
		}
		else if(functionCall.getName().equals("rush"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			player.rush(target);
			
		}
		else if(functionCall.getName().equals("shield"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy, event, index);
			player.shield(target);
			
		}
		else if(functionCall.getName().equals("freeze"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.freeze(target);
			
		}
		else if(functionCall.getName().equals("windrage"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.windrage(target);
			
		}
		else if(functionCall.getName().equals("random"))
		{
			Value targetSet=unionValues(functionCall.getExpressionList(),player,enemy,event, index);
			List<String> set=targetSet.getSet();
			
			if(set.size()<1)
			{
				Value ret=new Value();
				ret.setType("set");
				ret.setSet(new LinkedList<String>());
				return ret;
			}
			
			Random random = new Random();
			int r=random.nextInt(set.size());
			
			System.out.println("r="+r);
			System.out.println("id="+set.get(r));
			
			Value ret=new Value();
			ret.setType("set");
			ret.setSet(new LinkedList<String>());
			ret.getSet().add(set.get(r));
			return ret;
		}
		
		else if(functionCall.getName().equals("random2"))
		{
			Value targetSet=unionValues(functionCall.getExpressionList(),player,enemy,event, index);
			List<String> set=targetSet.getSet();
			
			Value ret=new Value();
			ret.setType("set");
			ret.setSet(new LinkedList<String>());
			
			if(set.size()==0)
			{
				return ret;
			}
			else if(set.size()==1)
			{
				ret.getSet().add(set.get(0));
				return ret;
			}
			
			Random random = new Random();
			int r=random.nextInt(set.size());
			int r2=random.nextInt(set.size());
			
			while(r2==r)
				r2=random.nextInt(set.size());
			
			System.out.println("r="+r);
			System.out.println("id="+set.get(r));
			System.out.println("r2="+r2);
			System.out.println("id="+set.get(r2));
						
			ret.getSet().add(set.get(r));
			ret.getSet().add(set.get(r2));
			return ret;
		}
		else if(functionCall.getName().equals("damage"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy, event, index);
						
			player.magicDamage(target, count.getIntValue());	
			
		}
		
		else if(functionCall.getName().equals("takecard"))
		{
			Value count=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.takeCard(count.getIntValue());
		}
		else if(functionCall.getName().equals("dropcard"))
		{
			Value count=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.takeCard(count.getIntValue());
		}
		else if(functionCall.getName().equals("copyhandcard"))
		{
			Value count=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.copyHandCard(count.getIntValue());
		}
		else if(functionCall.getName().equals("returnback"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.returnBack(target);
			
		}
		else if(functionCall.getName().equals("destroy"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			player.destroy(target);
			
		}
		else if(functionCall.getName().equals("attackadd"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
						
			player.attackAdd(target, count.getIntValue());
									
		}
		else if(functionCall.getName().equals("guardadd"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
			
			player.guardAdd(target, count.getIntValue());
			
		}
		else if(functionCall.getName().equals("lifeadd"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
			
			player.lifeAdd(target, count.getIntValue());
			
		}
		else if(functionCall.getName().equals("lifedouble"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			
			player.lifeDouble(target);
			
		}
		else if(functionCall.getName().equals("poweradd"))
		{
			Value count=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			
			player.powerAdd(count.getIntValue());
			
		}
		else if(functionCall.getName().equals("maxpoweradd"))
		{
			Value count=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			
			player.maxPowerAdd(count.getIntValue());
			
		}
		else if(functionCall.getName().equals("cure"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
			
			player.cure(target, count.getIntValue());
			
		}
		else if(functionCall.getName().equals("control"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
						
			player.control(target);
			enemy.destroy(target);
		}
		else if(functionCall.getName().endsWith("event"))
		{
			
			generateListeners(functionCall, player, enemy);

		}
		else if(functionCall.getName().equals("addattack"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
			
			
			player.addAttack(event.getThisValue(), target,count.getIntValue());
			
		}
		else if(functionCall.getName().equals("addlife"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
						
			player.addLife(event.getThisValue(), target,count.getIntValue());
			
		}
		else if(functionCall.getName().equals("isminion"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			if(player.isMinion(target))
				return Value.TRUE;
			else
				return Value.FALSE;
		}
		else if(functionCall.getName().equals("repeat"))
		{
			StatementList sl=functionCall.getExpressionList().getExpression().getB().getStatementList();
			Value count=evaluateExpression(functionCall.getExpressionList().getRemainingList().getExpression(),player,enemy,event, index);
			
			for(int i=0;i<count.getIntValue();i++)
			{
				evaluateStatementList(sl, player, enemy, event, index);
			}
		}
		else if(functionCall.getName().equals("count"))
		{
			Value target=evaluateExpression(functionCall.getExpressionList().getExpression(),player,enemy,event, index);
			
			Value count=new Value();
			count.setType("int");
			count.setIntValue(target.getSet().size());
			
			return count;
		}
		
		return null;
	}
	
	private static void generateListeners(FunctionCall functionCall, Player player, Player enemy )
	{
		StatementList sl=functionCall.getExpressionList().getExpression().getB().getStatementList();
		
		int startIndex=0;
		int endIndex=functionCall.getName().indexOf("event", startIndex);
		while(endIndex>=0)
		{
			String eventName=functionCall.getName().substring(startIndex, endIndex+5);
			
			Event event=new Event();
			event.setName(eventName);
			
			event.setEventSource(null);
			event.setEventTarget(null);
			event.setTarget(player.getCurrentTarget());
			event.setThisValue(player.getCurrentSource());
			
			List<Event> removeEvents=new LinkedList<Event>();			
			if(eventName.equals("endevent"))
			{
				Event removeEvent=new Event();
				removeEvent.setName("endevent");
				removeEvents.add(removeEvent);
				
			}
			else if(eventName.equals("beginevent"))
			{
				Event removeEvent=new Event();
				removeEvent.setName("beginevent");
				removeEvents.add(removeEvent);
				
			}
			
			else
			{
				if(player.isMinion(event.getThisValue()))
				{
					Event removeEvent=new Event();
					removeEvent.setName("minionlostevent");	
					removeEvent.setEventSource(null);
					removeEvent.setEventTarget(null);
					removeEvent.setTarget(player.getCurrentTarget());
					removeEvent.setThisValue(player.getCurrentSource());
					
					removeEvents.add(removeEvent);
					
					Event removeEvent2=new Event();
					removeEvent2.setName("minionmuteevent");	
					removeEvent2.setEventSource(null);
					removeEvent2.setEventTarget(null);
					removeEvent2.setTarget(player.getCurrentTarget());
					removeEvent2.setThisValue(player.getCurrentSource());
					
					removeEvents.add(removeEvent2);
				}
				else if(player.isWeapon(event.getThisValue()))
				{
					Event removeEvent=new Event();
					removeEvent.setName("weaponlostevent");	
					removeEvent.setEventSource(null);
					removeEvent.setEventTarget(null);
					removeEvent.setTarget(player.getCurrentTarget());
					removeEvent.setThisValue(player.getCurrentSource());
					
					removeEvents.add(removeEvent);
				}
			}
			
			player.addEventListener(event, removeEvents, sl.getCode());
			
			startIndex=endIndex+5;
			endIndex=functionCall.getName().indexOf("event", startIndex);
		}
	}
	
	private static Value unionValues(ExpressionList expressionList, Player player, Player enemy, Event event, int index)
	{
		//System.out.println("unionValues");
		
		Value value=evaluateExpression(expressionList.getExpression(),player,enemy, event, index);
		//System.out.println(value);
		if(expressionList.getRemainingList()!=null)
		{
			return Value.union(value, unionValues(expressionList.getRemainingList(),player,enemy, event, index));
		}
		else
			return value;
	}
	
	private static Value evaluateVariable(Variable variable, Player player, Player enemy, Event event, int index)
	{
		if(variable.getName().equals("$target"))
		{
			if(event!=null)
				return event.getTarget();
			else
				return player.getCurrentTarget();
		}
		else if(variable.getName().equals("$this"))
		{
			if(event!=null)
				return event.getThisValue();
			else
				return player.getCurrentSource();
		}
		else if(variable.getName().equals("$$"))
		{
			return player.getCurrentSource();
		}
		else if(variable.getName().equals("$eventsource"))
		{
			if(event!=null)
				return event.getEventSource();
			
		}
		else if(variable.getName().equals("$eventtarget"))
		{
			if(event!=null)
				return event.getEventTarget();
		}
		else if(variable.getName().equals("$extra"))
		{
			return player.getExtraValue();
		}
		
		return null;
	}
	
	private static Value evaluateVariableDotConstant(VariableDotConstant vdc, Player player, Player enemy, Event event, int index)
	{
		Value vValue=evaluateVariable(vdc.getV(),player, enemy, event, index);
		Value cValue=evaluateConstant(vdc.getC(), player, enemy, event, index);
				
		return player.getVDCValue(vValue,cValue);
	}
	
	private static Value evaluateConstant(Constant constant, Player player, Player enemy, Event event,int index)
	{
		if(constant.getType().equals("string"))
		{
		
			if(constant.getStringValue().equals("selfhero"))
			{
				return player.getHeroValue();
			}
			else if(constant.getStringValue().equals("selfminions"))
			{
				return player.getMinionsValue();
			}
			else if(constant.getStringValue().equals("enemyminions"))
			{
				return enemy.getMinionsValue();
			}
			else if(constant.getStringValue().equals("enemyhero"))
			{
				return enemy.getHeroValue();
			}
			else if(constant.getStringValue().equals("selfweapon"))
			{
				return player.getWeaponValue();
			}
			else if(constant.getStringValue().equals("enemyweapon"))
			{
				return enemy.getWeaponValue();
			}
			else if(constant.getStringValue().equals("adjcent"))
			{
				if(event!=null)
					return player.getAdjcentValue(event.getThisValue());
				else
					return player.getAdjcentValue(player.getCurrentSource());
			}
			else if(constant.getStringValue().equals("magiccards"))
			{
				return player.getMagicCardsValue();
			}
			else if(constant.getStringValue().equals("none"))
			{
				Value empty=new Value();
				empty.setType("set");
				empty.setSet(new LinkedList<String>());
				
				return empty;
			}
			
			String stringValue=constant.getStringValue();
						
			Value ret=new Value();
			ret.setType("string");
			ret.setStringValue(stringValue);
			return ret;
		}
		else
		{
			Value ret=new Value();
			ret.setType("int");
			ret.setIntValue(constant.getIntValue());
			return ret;
		}
		
		
	}
	
		
	
	
	
}

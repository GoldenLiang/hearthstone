package com.lc.playing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.lc.dao.GameDao;
import com.lc.dao.HeroMinionDao;
import com.lc.dao.MinionDao;
import com.lc.dao.WeaponDao;
import utils.Helper;
import utils.SpringFactory;
import com.lc.Card;

import com.lc.Event;
import com.lc.Game;
import com.lc.GameCard;

import com.lc.GameListener;

import com.lc.Hero;

import com.lc.HeroMinion;

import com.lc.Minion;

import com.lc.Weapon;
import com.lc.dao.CardDao;
import com.lc.dao.GameCardDao;
import com.lc.dao.GameListenerDao;
import com.lc.dao.HeroDao;

public class Player {

	
	private Game game;
	private String userName;
	private String uid;
	
		
	//private List<Card> handCards;
	private List<GameCard> handGameCards;
		
	private int cardCount;
	
	private Hero hero;
	private Card heroCard;
	
	private GameCard heroGameCard;
		
	private List<Minion> minions;
		
	private HeroMinion heroMinion;
	
	//private Minion newMinion;
	
	private List<GameListener> listeners;
		
	private Player enemy;
	
	
	private Value currentTarget;
	
	private Weapon weapon;
	
	//private String filterContext;
	private Value currentSource;
	
	private Player clonePlayer;
	private Player cloneEnemy;
	
	private CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
	private GameCardDao gameCardDao=(GameCardDao)SpringFactory.getFactory().getBean("gameCardDao");
	private GameDao gameDao=(GameDao)SpringFactory.getFactory().getBean("gameDao");
	private GameListenerDao gameListenerDao=(GameListenerDao)SpringFactory.getFactory().getBean("gameListenerDao");
	private HeroDao heroDao=(HeroDao)SpringFactory.getFactory().getBean("heroDao");
	private HeroMinionDao heroMinionDao=(HeroMinionDao)SpringFactory.getFactory().getBean("heroMinionDao");
	private MinionDao minionDao=(MinionDao)SpringFactory.getFactory().getBean("minionDao");
	private WeaponDao weaponDao=(WeaponDao)SpringFactory.getFactory().getBean("weaponDao");
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public Game getGame()
	{
		return this.game;
	}
	
	public Value getCurrentSource() {
		return currentSource;
	}

	public void setCurrentSource(Value value)
	{
		this.currentSource=value;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public GameCard getHeroGameCard() {
		return heroGameCard;
	}

	public void setHeroGameCard(GameCard heroGameCard) {
		this.heroGameCard = heroGameCard;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public List<Minion> getMinions() {
		return minions;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	public List<GameCard> getHandGameCards() {
		return handGameCards;
	}

	public void setHandGameCards(List<GameCard> handGameCards) {
		this.handGameCards = handGameCards;
	}

//	public List<Card> getHandCards() {
//		return handCards;
//	}
//
//	public void setHandCards(List<Card> handCards) {
//		this.handCards = handCards;
//	}

	public Card getHeroCard() {
		return heroCard;
	}

	public void setHeroCard(Card heroCard) {
		this.heroCard = heroCard;
	}

	public HeroMinion getHeroMinion() {
		return heroMinion;
	}

	public void setHeroMinion(HeroMinion heroMinion) {
		this.heroMinion = heroMinion;
	}

	public List<GameListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<GameListener> listeners) {
		this.listeners = listeners;
	}

	public Player getEnemy() {
		return enemy;
	}

	public void setMinions(List<Minion> minions) {
		this.minions = minions;
	}

	public Value getCurrentTarget()
	{
		return this.currentTarget;
	}
	
	public void setCurrentTarget(Value currentTarget)
	{
		this.currentTarget=currentTarget;
	}
	
	public void setEnemy(Player enemy) {
		this.enemy = enemy;
	}
	
	public void initialize()
	{
						
		// generate card event to refresh game card's target
		Event event=new Event();
		event.setName("initevent");
		invokeEvent(event);
	}

	public void makeSnapShot()
	{
		// copy player
		this.clonePlayer=new Player(this);
		
		// copy enemy
		//this.cloneEnemy=new Player(this.enemy);
	}
	
	public Player(Player player)
	{
		this.userName=player.userName;
		
		// copy game
		this.game=new Game(player.getGame());
		
		// copy hand game cards
		this.handGameCards=new LinkedList<GameCard>();
		
		for(GameCard gc: player.handGameCards)
		{
			this.handGameCards.add(new GameCard(gc));
		}
		
		// copy minions
		this.minions=new LinkedList<Minion>();
		
		for(Minion minion: player.minions)
		{
			this.minions.add(new Minion(minion));
		}
		
		// copy hero minion
		this.heroMinion=new HeroMinion(player.heroMinion);
		
		// copy weapon
		this.weapon=new Weapon(player.weapon);
		
	}
	
	public Player(Game game, String userName)
	{
				
		this.game=game;
		this.userName=userName;
		
		
		this.heroGameCard=new GameCard();
		
		if(userName.equals(game.getUserName1()))
		{
			this.uid=game.getUid1();
			
			// read hero card
			this.hero=heroDao.getHero(game.getJob1());
			
			this.heroCard=cardDao.getCard(hero.getSkillName());
								
		}
		else
		{
			this.uid=game.getUid2();
			
			// read hero card
			this.hero=heroDao.getHero(game.getJob2());
			
			this.heroCard=cardDao.getCard(hero.getSkillName());
					
		}
		
		// init an action generator
		
		
		// read hero minion, it must be created before in database 
		this.heroMinion=heroMinionDao.getHeroMinion(game.getId(),this.uid);
		
		// read minions, if there are any in database
		this.minions=minionDao.getMinions(game.getId(), this.uid);
	
		// read listeners, if there are any in database;
		this.listeners=gameListenerDao.getListeners(game.getId(),this.uid);
		
		// read hand cards, it must be created before in database
		this.handGameCards=gameCardDao.getGameCards(game.getId(), this.uid, "hand");
		
		
		// read card count
		this.cardCount=gameCardDao.getCount(game.getId(), this.uid, "pack");
		
		// read weapon
		this.weapon=weaponDao.getWeapon(game.getId(), this.uid);
		
				
	}
	
	// must be called after setEnemy.
	
	public void makeGameCardTarget()
	{
		Value target=Evaluator.evaluateTarget(heroCard, this, enemy);
		
		// a little hack here to set needtarget
		if(heroCard.getTargetCode().equals(""))
			heroGameCard.setNeedTarget(false);
		else
			heroGameCard.setNeedTarget(true);
		
		if(target==null)
		{
			heroGameCard.setUsable(false);
			heroGameCard.setTargets(target);
		}
		else
		{
			heroGameCard.setUsable(true);
			heroGameCard.setTargets(target);
		}
		
		
		
		for(GameCard gc: this.handGameCards)
		{
			Card card=cardDao.getCard(gc.getCardName());
			
			target=Evaluator.evaluateTarget(card, this, enemy);
			//System.out.println(card.getName()+":"+target);
			
			// a little hack here to set needtarget
			if(card.getTargetCode().equals(""))
				gc.setNeedTarget(false);
			else if(card.getTargetCode().startsWith("doublechoice"))
				gc.setNeedTarget(false);
			else if(card.getTargetCode().startsWith("choiceornone") && target.getSet().size()==0)
				gc.setNeedTarget(false);
			else
				gc.setNeedTarget(true);
				
			if(target==null)
			{
				gc.setUsable(false);
				gc.setTargets(target);
			}
			else
			{
				gc.setUsable(true);
				gc.setTargets(target);
			}
						
			
		}
		
	}
	
	public void makeMinionTarget()
	{
		// for minions
		
		Value targets=null;
				
		for(Minion minion: this.minions)
		{
			targets=new Value();
			targets.setType("set");
			targets.setSet(new LinkedList<String>());
			
			if(minion.getInnerState().get("attackable").equals("true")
					&& minion.getInnerState().get("freeze")==null
					&& minion.getInnerState().get("action").equals("ready")
					&& Integer.parseInt(minion.getInnerState().get("attackcount"))>0)
				
			{
				int attack=minion.getAttack();
				
				for(String sid: minion.getOuterState().keySet())
				{
					String extraAttackString=minion.getOuterState().get(sid).get("attack");
					if(extraAttackString!=null)
						attack+=Integer.parseInt(extraAttackString);
				}
				
				if(attack>0)
				{
					// judge whether there are taunts
					boolean taunt=false;
					for(Minion eminion: enemy.minions)
					{
						if(eminion.getInnerState().get("taunt")!=null)
						{
							taunt=true;
						}
					}
					
					if(taunt)
					{
						// only add those taunted minion
						for(Minion eminion: enemy.minions)
						{
							if(eminion.getInnerState().get("taunt")!=null)
							{
								targets.getSet().add(eminion.getId());
							}
						}
					}
					else
					{
						// add all of them
						for(Minion eminion: enemy.minions)
						{
							targets.getSet().add(eminion.getId());
						}
						
						targets.getSet().add(enemy.heroMinion.getId());
					}
				}
			}
			
			minion.setTargets(targets);
		}
		
		// for hero minion
		
		targets=new Value();
		targets.setType("set");
		targets.setSet(new LinkedList<String>());
		
		int attack=heroMinion.getAttack();
		if(this.weapon.getBlood()>0)
			attack+=weapon.getAttack();
		
		if(attack>0 && heroMinion.getInnerState().get("freeze")==null && 
				Integer.parseInt(heroMinion.getInnerState().get("attackcount"))>0)
		{
			
						
			// judge whether there are taunts
			boolean taunt=false;
			for(Minion eminion: enemy.minions)
			{
				if(eminion.getInnerState().get("taunt")!=null)
				{
					taunt=true;
				}
			}
			
			if(taunt)
			{
				// only add those taunted minion
				for(Minion eminion: enemy.minions)
				{
					if(eminion.getInnerState().get("taunt")!=null)
					{
						targets.getSet().add(eminion.getId());
					}
				}
			}
			else
			{
				// add all of them
				for(Minion eminion: enemy.minions)
				{
					targets.getSet().add(eminion.getId());
				}
				
				targets.getSet().add(enemy.heroMinion.getId());
			}
		}
		
		heroMinion.setTargets(targets);
	}
		
	
	public void userHeroSkill(String tid)
	{
		if(tid.equals(" "))
			this.currentTarget=null;
		else
			this.currentTarget=Helper.makeValue(tid);
		
		useCard(heroCard, 0);
		
		// minus 2 from power
		if(this.userName.equals(game.getUserName1()))
		{
			game.setPower1(game.getPower1()-2);
		}
		else
		{
			game.setPower2(game.getPower2()-2);
		}
		
		invokeEvent(new Event());
	}

	public void useGameCard(String cid, String tid, int index)
	{
		if(tid.matches("\\s*"))
		{
			this.currentTarget=new Value();
			this.currentTarget.setType("set");
			this.currentTarget.setSet(new LinkedList<String>());
		}
		else
			this.currentTarget=Helper.makeValue(tid);
		
		
		this.currentSource=Helper.makeValue(cid);
		
		Card card=findCard(cid);
		
		useCard(card, index);
		
		// mark game card(cid) as used
		
		GameCard gc=findGameCardById(cid);
		this.handGameCards.remove(gc);
		
		// minus card's cost from power
		if(this.userName.equals(game.getUserName1()))
		{
			game.setPower1(game.getPower1()-card.getCost());
		}
		else
		{
			game.setPower2(game.getPower2()-card.getCost());
		}
		
		// generate a card event here
		
		Event event=new Event();
		event.setName("cardevent");
		invokeEvent(event);
		
		
	}
	
		
	public void useCard(Card card, int index)
	{
		if(card.getGroup().equals("英雄级") || card.getGroup().equals("附属级"))
		{
			if(card.getType().equals("法术"))
			{
				
				Evaluator.evaluateCard(card, index, this, this.enemy);
			}
			else if(card.getType().equals("随从"))
			{
				this.currentSource=Helper.makeValue(createMinion(card, index).getId());
				Evaluator.evaluateCard(card, index, this, this.enemy);
				
				// generate minion create event
				Event event=new Event();
				event.setName("minioncreateevent");
				event.setEventSource(this.currentSource);
				event.setEventTarget(null);
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
			else if(card.getType().equals("武器"))
			{
				createWeapon(card);
				this.currentSource=Helper.makeValue(weapon.getId());
				Evaluator.evaluateCard(card, index, this, this.enemy);
			}
		}
		else
		{
			if(card.getType().equals("法术"))
			{
				index=this.minions.size();
				Evaluator.evaluateCard(card, index,  this, this.enemy);
				
				// generate magic cast event
				Event event=new Event();
				event.setName("magiccastevent");
				event.setEventSource(this.currentSource);
				event.setEventTarget(this.currentTarget);
				invokeEvent(event);
			}
			else if(card.getType().equals("随从"))
			{
				this.currentSource=Helper.makeValue(createMinion(card, index).getId());
				Evaluator.evaluateCard(card, index, this, this.enemy);
								
				// generate minion create event
				Event event=new Event();
				event.setName("minioncreateevent");
				event.setEventSource(this.currentSource);
				event.setEventTarget(null);
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
			else if(card.getType().equals("武器"))
			{
				createWeapon(card);
				this.currentSource=Helper.makeValue(weapon.getId());
				Evaluator.evaluateCard(card, index, this, this.enemy);
			}						
		}
		
		
		
	}
	
	public void attack(String cid, String tid)
	{
		this.currentSource=Helper.makeValue(cid);
		this.currentTarget=Helper.makeValue(tid);
		
		// generate attack event
		Event event=new Event();
		event.setName("attackevent");
		event.setEventSource(this.currentSource);
		event.setEventTarget(this.currentTarget);
		invokeEvent(event);
		enemy.invokeEvent(event);
		
		
		attackDamage(currentSource, currentTarget, true);
		attackDamage(currentTarget, currentSource, false);
		
		this.updateMinions();
		enemy.updateMinions();
		
		// attackcount--
		minusAttackCount(cid);
		
	}
	
	public void minusAttackCount(String cid)
	{
		System.out.println("minus attack count "+cid);
		
		Minion minion=findMinionById(cid);
		if(minion!=null)
		{
			int attackCount=Integer.parseInt(minion.getInnerState().get("attackcount"));
			minion.getInnerState().put("attackcount", String.valueOf(attackCount-1));
		}
		
		if(heroMinion.getId().equals(cid))
		{
			int attackCount=Integer.parseInt(heroMinion.getInnerState().get("attackcount"));
			System.out.println(attackCount);
			heroMinion.getInnerState().put("attackcount", String.valueOf(attackCount-1));
			
			this.weapon.setBlood(this.weapon.getBlood()-1);
			
			
			if(this.weapon.getBlood()==0)
			{
				// generate weapon lost event
				
				Event event=new Event();
				event.setName("weaponlostevent");
				event.setEventSource(Helper.makeValue(weapon.getId()));
				
				invokeEvent(event);
				
			}
						
		}
	}
	
	public Minion createMinion(Card card, int index)
	{
		Minion minion=new Minion();
		minion.setAttack(card.getAttack());
		minion.setBlood(card.getBlood());
		minion.setCardName(card.getName());
		minion.setGid(game.getId());
		minion.setId("g"+(game.getNextCount()));
		minion.setImageid(card.getImageid());
		minion.setRace(card.getRace());
		minion.setIndex(index);
		minion.setMaxBlood(card.getBlood());
		minion.setState("");
		minion.setOutState("");
		minion.setUid(this.uid);
		
		minion.setInnerState(new HashMap<String,String>());
		minion.getInnerState().put("action", "init");
		minion.getInnerState().put("attackcount","1");
		minion.getInnerState().put("attackable", "true");
		
		minion.setOuterState(new HashMap<String,Map<String,String>>());
		
		for(Minion m: this.minions)
		{
			if(m.getIndex()>=minion.getIndex())
				m.setIndex(m.getIndex()+1);
		}
		
		this.minions.add(minion);
		Collections.sort(this.minions);
		
		return minion;
	}
	
	public void createWeapon(Card card)
	{
		weapon.setGid(this.game.getId());
		weapon.setUid(this.uid);
		weapon.setAttack(card.getAttack());
		weapon.setBlood(card.getBlood());
		weapon.setState("");
		weapon.setInnerState(Helper.parseState(weapon.getState()));
		weapon.setOuterState(new HashMap<String,Map<String,String>>());
		
	}
	
	public Card findCard(String cid)
	{
		GameCard gameCard=findGameCardById(cid);
						
		Card card=cardDao.getCard(gameCard.getCardName());
			
		return card;
	}
	
	public Value getHeroValue()
	{
		Value ret=new Value();
		ret.setType("set");
		ret.setSet(new LinkedList<String>());
		ret.getSet().add(this.heroMinion.getId());
		return ret;
	}
	
	public Value getMinionsValue()
	{
		Value ret=new Value();
		ret.setType("set");
		ret.setSet(new LinkedList<String>());
		
		for(Minion minion: this.minions)
		{
			ret.getSet().add(minion.getId());
		}
		
		return ret;
	}
	
//	public Value getNewMinionValue() {
//		Value ret=new Value();
//		ret.setType("set");
//		ret.setSet(new LinkedList<String>());
//		if(this.newMinion!=null)
//			ret.getSet().add(this.newMinion.getId());
//		return ret;
//	}
	
	public Value getWeaponValue()
	{
		Value ret=new Value();
		ret.setType("set");
		ret.setSet(new LinkedList<String>());
		if(this.weapon!=null)
			ret.getSet().add(this.weapon.getId());
		return ret;
	}
	
		
//	public Value getContextValue()
//	{
//		Value ret=new Value();
//		ret.setType("set");
//		ret.setSet(new LinkedList<String>());
//		if(this.filterContext!=null)
//		{
//			ret.getSet().add(this.filterContext);
//		}
//		return ret;
//	}
	
	public Value getVDCValue(Value vValue, Value cValue)
	{
		String id=vValue.getSet().get(0);
		String attribute=cValue.getStringValue();
		
		Value ret=new Value();
		
		Minion minion=findMinionById(id);
		
		if(minion!=null)
		{
			if(attribute.equals("race"))
			{
				ret.setType("string");
				ret.setStringValue("'"+minion.getRace()+"'");
			}
			else if(attribute.equals("life"))
			{
				ret.setType("int");
				ret.setIntValue(minion.getBlood());
			}
			else if(attribute.equals("maxlife"))
			{
				ret.setType("int");
				ret.setIntValue(minion.getMaxBlood());
			}
			else if(attribute.equals("attack"))
			{
				ret.setType("int");
				ret.setIntValue(minion.getAttack());
			}
		}
				
		return ret;
	}
	
	public Value getAdjcentValue(Value target)
	{
		Minion minion=findMinionById(target.getSet().get(0));
		
		Value ret=new Value();
		ret.setType("set");
		ret.setSet(new LinkedList<String>());
		for(Minion m: this.minions)
		{
			if(m.getIndex()==minion.getIndex()-1)
			{
				ret.getSet().add(m.getId());
			}
			else if(m.getIndex()==minion.getIndex()+1)
			{
				ret.getSet().add(m.getId());
			}
		}
		
		return ret;
	}
	
	public Value getExtraValue()
	{
		GameCard gameCard=findGameCardById(this.currentSource.getSet().get(0));
		
		int count=0;
		for(String id: gameCard.getOuterState().keySet())
		{
			Map<String, String> map=gameCard.getOuterState().get(id);
			for(String key: map.keySet())
			{
				if(key.equals("attack"))
					count+=Integer.parseInt(map.get(key));
			}
		}
		
		Value ret=new Value();
		ret.setType("int");
		ret.setIntValue(count);
		return ret;
	}
	
	public Value getMagicCardsValue()
	{
		Value ret=new Value();
		ret.setType("set");
		ret.setSet(new LinkedList<String>());
		
		for(GameCard gc: this.handGameCards)
		{
			Card card=cardDao.getCard(gc.getCardName());
			if(card.getType().equals("法术"))
			{
				ret.getSet().add(gc.getId());
			}
		}
		
		return ret;
	}
	
//	public Value getCurrentGameCardValue()
//	{
//		Value ret=new Value();
//		ret.setType("set");
//		ret.setSet(new LinkedList<String>());
//		if(this.currentGameCard!=null)
//		{
//			ret.getSet().add(this.currentGameCard.getId());
//		}
//		return ret;
//	}
	
	public void taunt(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
				minion.getInnerState().put("taunt", "true");
		}
	}
	
	public void rush(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				if(minion.getInnerState().get("action").equals("init"))
				{
					minion.getInnerState().put("action", "ready");
				}
			}
		}
	}
	
	public void shield(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
				minion.getInnerState().put("shield", "true");
		}
	}
	
	public void windrage(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				minion.getInnerState().put("windrage", "true");
				minion.getInnerState().put("attackcount", "2");
			}
		}
	}
	
	public void freeze(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
				minion.getInnerState().put("freeze", "true");
			
			if(heroMinion.getId().equals(id))
			{
				heroMinion.getInnerState().put("freeze", "true");
			}
			
			if(enemy.heroMinion.getId().equals(id))
			{
				enemy.heroMinion.getInnerState().put("freeze", "true");
			}
		}
	}
	
	public int destroy(Value target)
	{
		int index=-1;
		
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				minion.setBlood(-100);
				
				updateMinions();
				enemy.updateMinions();
				
				index=minion.getIndex();
			}
			
			if(id.equals(this.weapon.getId()))
			{
				this.weapon.setBlood(0);
				
				if(weapon.getBlood()<=0)
				{
					// generate weapon lost event
					
					Event event=new Event();
					event.setName("weaponlostevent");
					event.setEventSource(Helper.makeValue(weapon.getId()));
					
					invokeEvent(event);
				}
			}
			
			if(id.equals(enemy.weapon.getId()))
			{
				enemy.weapon.setBlood(0);
				
				if(enemy.weapon.getBlood()<=0)
				{
					// generate weapon lost event
					
					Event event=new Event();
					event.setName("weaponlostevent");
					event.setEventSource(Helper.makeValue(enemy.weapon.getId()));
					
					enemy.invokeEvent(event);
				}
			}
			
		}
		
		return index;
	}
	
	public void updateMinions()
	{
		
		List<Minion> copy=new LinkedList<Minion>();
		copy.addAll(minions);
		
		for(Minion minion: copy)
		{
			int blood=minion.getBlood();
			for(String sid: minion.getOuterState().keySet())
			{
				Map<String,String> state=minion.getOuterState().get(sid);
				for(String key: state.keySet())
				{
					if(key.equals("blood"))
					{
						blood+=Integer.parseInt(state.get(key));
					}
				}
			}
			
			// generate minion lost event
			
			if(blood<=0)
			{
				this.minions.remove(minion);
								
				updateMinionsIndex();
				
				Event event=new Event();
				event.setName("minionlostevent");
				event.setEventSource(Helper.makeValue(minion.getId()));
				
				
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
		}
	}
	
	private void updateMinionsIndex()
	{
		Collections.sort(this.minions);
		
		int count=0;
		for(Minion minion: this.minions)
		{
			minion.setIndex(count++);
		}
	}
	
	public void control(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=enemy.findMinionById(id);
			
			if(minion!=null)
			{
				Minion nMinion=new Minion();
								
				nMinion.setAttack(minion.getAttack());
				nMinion.setBlood(minion.getBlood());
				nMinion.setCardName(minion.getCardName());
				nMinion.setGid(minion.getGid());
				nMinion.setId("g"+game.getNextCount());
				nMinion.setImageid(minion.getImageid());
				nMinion.setIndex(0);
				nMinion.setInnerState(minion.getInnerState());
				nMinion.setMaxBlood(minion.getMaxBlood());
				nMinion.setOuterState(minion.getOuterState());
				nMinion.setOutState(minion.getOutState());
				nMinion.setRace(minion.getRace());
				nMinion.setState(minion.getState());
				nMinion.setUid(this.uid);
								
				this.minions.add(nMinion);
			}
						
		}
	}
	
	public void attackAdd(Value target, int count)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				minion.setAttack(minion.getAttack()+count);
			}
			
			if(heroMinion.getId().equals(id))
			{
				heroMinion.setAttack(heroMinion.getAttack()+count);
			}
			
			if(id.equals(this.weapon.getId()))
			{
				weapon.setAttack(weapon.getAttack()+count);
			}
			
			if(id.equals(enemy.weapon.getId()))
			{
				enemy.weapon.setAttack(enemy.weapon.getAttack()+count);
			}
			
		}
	}
	
	public void addAttack(Value source, Value target, int count)
	{
		String sid=source.getSet().get(0);
		
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				Map<String,String> map=minion.getOuterState().get(sid);
				if(map==null)
				{
					map=new HashMap<String,String>();
					map.put("attack", ""+count);
					minion.getOuterState().put(sid, map);
				}
				else if(map.get("attack")==null)
				{
					map.put("attack", ""+count);
				}
			}
			
			GameCard gc=findGameCardById(id);
			if(gc!=null)
			{
				
				Map<String,String> map=gc.getOuterState().get(sid);
				if(map==null)
				{
					map=new HashMap<String,String>();
					map.put("attack", ""+count);
					gc.getOuterState().put(sid, map);
				}
				else if(map.get("attack")==null)
				{
					map.put("attack", ""+count);
				}
				
			}
			
			gc=enemy.findGameCardById(id);
			if(gc!=null)
			{
				
				Map<String,String> map=gc.getOuterState().get(sid);
				if(map==null)
				{
					map=new HashMap<String,String>();
					map.put("attack", ""+count);
					gc.getOuterState().put(sid, map);
				}
				else if(map.get("attack")==null)
				{
					map.put("attack", ""+count);
				}
				
			}
						
		}
	}
	
	public void guardAdd(Value target, int count)
	{
		for(String id: target.getSet())
		{
			if(heroMinion.getId().equals(id))
			{
				heroMinion.setGuard(heroMinion.getGuard()+count);
			}
		}
	}
	
	public void addLife(Value source, Value target, int count)
	{
		String sid=source.getSet().get(0);
		
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				Map<String,String> map=minion.getOuterState().get(sid);
				if(map==null)
				{
					map=new HashMap<String,String>();
					map.put("blood", ""+count);
					minion.getOuterState().put(sid, map);
				}
				else if(map.get("blood")==null)
				{
					map.put("blood", ""+count);
				}
			}
									
		}
	}
	
	public void lifeAdd(Value target, int count)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				minion.setBlood(minion.getBlood()+count);
				minion.setMaxBlood(minion.getMaxBlood()+count);
				
			}
									
			if(id.equals(this.weapon.getId()))
			{
				weapon.setBlood(weapon.getBlood()+count);
				
				
			}
			
			if(id.equals(enemy.weapon.getId()))
			{
				enemy.weapon.setBlood(enemy.weapon.getBlood()+count);
				
				
			}
		}
	}
	
	public void lifeDouble(Value target)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				minion.setBlood(minion.getBlood()*2);
				minion.setMaxBlood(minion.getMaxBlood()*2);
				
				
			}
						
		}
	}
	
	public void powerAdd(int count)
	{
		if(this.userName.equals(this.game.getUserName1()))
			this.game.setPower1(game.getPower1()+count);
		else
			this.game.setPower2(game.getPower2()+count);
	}
	
	public void maxPowerAdd(int count)
	{
		if(this.userName.equals(this.game.getUserName1()))
			this.game.setMaxpower1(game.getMaxpower1()+count);
		else
			this.game.setMaxpower2(game.getMaxpower2()+count);
	}
	
	public void attackDamage(Value source, Value target, boolean aggressive)
	{
				
		String cid=source.getSet().get(0);
				
		Minion sourceMinion=findMinionById(cid);
		
		int attack=0;
		if(sourceMinion!=null )
		{
			attack=sourceMinion.getAttack();
			
			for(String sid: sourceMinion.getOuterState().keySet())
			{
				String extraAttackString=sourceMinion.getOuterState().get(sid).get("attack");
				if(extraAttackString!=null)
					attack+=Integer.parseInt(extraAttackString);
			}
		}
		else if(aggressive)
		{
			// source is hero minion
			
			if(heroMinion.getId().equals(cid))
			{
				attack=heroMinion.getAttack();
				
				if(this.weapon.getBlood()>0)
					attack+=this.weapon.getAttack();
				
			}
			else
			{
				attack=enemy.heroMinion.getAttack();
				
				if(enemy.weapon.getBlood()>0)
					attack+=enemy.weapon.getAttack();
			}
			
			
		}
		else
			return;
		
		String tid=target.getSet().get(0);
		Minion targetMinion=findMinionById(tid);
		
		if(targetMinion!=null && targetMinion.getInnerState().get("shield")!=null)
		{
			targetMinion.getInnerState().remove("shield");
		}
		else
			realDamage(source, target, attack);
		
	}
	
	public void magicDamage(Value target, int count)
	{
		Value empty=new Value();
		empty.setType("set");
		empty.setSet(new LinkedList<String>());
		
		realDamage(empty, target, count);
		
		updateMinions();
		enemy.updateMinions();
	}
	
//	public void cardDamage(Value target, int count)
//	{
//		// calculate the real count
//		if(this.currentSource==null)
//		{
//			realDamage(target, count);
//			return;
//		}
//		
//		String id=this.currentSource.getSet().get(0);
//		
//		GameCard gc=findGameCardById(id);
//		if(gc!=null)
//		{
//			int attack=count;
//			
//			String attackString=gc.getOuterState().get("attack");
//			if(attackString!=null)
//				attack+=Integer.parseInt(attackString);
//			
//			realDamage(target, attack);
//		}
//		
//		
//		
//	}
	
	public void realDamage(Value source, Value target, int count)
	{
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				minion.setBlood(minion.getBlood()-count);
				
				
				
				// generate damage event
				Event event=new Event();
				event.setName("damageevent");
				event.setEventSource(source);
				event.setEventTarget(Helper.makeValue(id));
				
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
			
			if(heroMinion.getId().equals(id))
			{
				if(heroMinion.getGuard()-count>=0)
				{
					heroMinion.setGuard(heroMinion.getGuard()-count);
					
					Event event=new Event();
					event.setName("herominionguardevent");
					event.setEventSource(source);
					event.setEventTarget(Helper.makeValue(id));
					
					invokeEvent(event);
				}
				else
				{
					
					heroMinion.setBlood(heroMinion.getBlood()+heroMinion.getGuard()-count);
					heroMinion.setGuard(0);
				}
				
				// generate damage event
				Event event=new Event();
				event.setName("damageevent");
				event.setEventSource(source);
				event.setEventTarget(Helper.makeValue(id));
				
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
			
			if(enemy.heroMinion.getId().equals(id))
			{
				if(enemy.heroMinion.getGuard()-count>=0)
					enemy.heroMinion.setGuard(enemy.heroMinion.getGuard()-count);
				else
				{
					
					enemy.heroMinion.setBlood(enemy.heroMinion.getBlood()+enemy.heroMinion.getGuard()-count);
					enemy.heroMinion.setGuard(0);
				}
				
				// generate damage event
				Event event=new Event();
				event.setName("damageevent");
				event.setEventSource(source);
				event.setEventTarget(Helper.makeValue(id));
				
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
			
			if(id.equals(this.weapon.getId()))
			{
				weapon.setBlood(weapon.getBlood()-count);
				
				if(weapon.getBlood()<=0)
				{
					// generate weapon lost event
					
					Event event=new Event();
					event.setName("weaponlostevent");
					event.setEventSource(Helper.makeValue(weapon.getId()));
					
					invokeEvent(event);
				}
			}
			
			if(id.equals(enemy.weapon.getId()))
			{
				enemy.weapon.setBlood(enemy.weapon.getBlood()-count);
				
				if(enemy.weapon.getBlood()<=0)
				{
					// generate weapon lost event
					
					Event event=new Event();
					event.setName("weaponlostevent");
					event.setEventSource(Helper.makeValue(enemy.weapon.getId()));
					
					enemy.invokeEvent(event);
				}
			}
		}
	}
	
	public void cure(Value target , int count)
	{
		for(String id: target.getSet())
		{
			boolean cured=false;
			
			Minion minion=findMinionById(id);
			
			if(minion!=null)
			{
				int oldBlood=minion.getBlood();
				minion.setBlood(Math.min(minion.getMaxBlood(),minion.getBlood()+count));
				
				if(oldBlood<minion.getBlood())
					cured=true;
			}
			
			if(heroMinion.getId().equals(id))
			{
				int oldBlood=heroMinion.getBlood();
				heroMinion.setBlood(Math.min(30,heroMinion.getBlood()+count));
				
				if(oldBlood<heroMinion.getBlood())
					cured=true;
			}
			
			if(enemy.heroMinion.getId().equals(id))
			{
				int oldBlood=enemy.heroMinion.getBlood();
				enemy.heroMinion.setBlood(Math.min(30,enemy.heroMinion.getBlood()+count));
				
				if(oldBlood<enemy.heroMinion.getBlood())
					cured=true;
			}
			
			// generate cure event
			
			if(cured)
			{
				Event event=new Event();
				event.setName("cureevent");
				event.setEventSource(this.currentSource);
				event.setEventTarget(Helper.makeValue(id));
				
				invokeEvent(event);
				enemy.invokeEvent(event);
			}
		}
	}
	
	public void takeCard(int count)
	{
		for(int i=0;i<count;i++)
		{
			GameCard gc=gameCardDao.takeCard(game.getId(), uid);
			if(gc!=null)
			{
				handGameCards.add(gc);
			
				// generate card event
				Event event=new Event();
				event.setName("cardevent");
				invokeEvent(event);
			}
				
		}
	}
	
	public void dropCard(int count)
	{
		for(int i=0;i<count;i++)
		{
						
						
			Random random=new Random();
			int r=random.nextInt(this.handGameCards.size());
			
			this.handGameCards.remove(r);
			
			// generate card event
			Event event=new Event();
			event.setName("cardevent");
			invokeEvent(event);
		}
	}
	
	public void copyHandCard(int count)
	{
		
						
		List<GameCard> enemyHandGameCards=enemy.getHandGameCards();
				
		Random random=new Random();
		int r=random.nextInt(enemyHandGameCards.size());
		
		GameCard gc=enemyHandGameCards.get(r);
		
		GameCard gameCard=new GameCard();
		
		gameCard.setCardName(gc.getCardName());
		gameCard.setGid(game.getId());
		gameCard.setId("g"+game.getNextCount());
		
		gameCard.setState("");
		gameCard.setType(gc.getType());
		gameCard.setUid(uid);
		
		this.handGameCards.add(gameCard);
		
		// generate card event
		Event event=new Event();
		event.setName("cardevent");
		invokeEvent(event);
		
	}
	
	public void returnBack(Value target)
	{
		
		for(String id: target.getSet())
		{
			Minion minion=findMinionById(id);
			
			GameCard gameCard=new GameCard();
			
			gameCard.setCardName(minion.getCardName());
			gameCard.setGid(game.getId());
			gameCard.setId("g"+game.getNextCount());
			
			gameCard.setState("");
			gameCard.setType(minion.getRace());
			gameCard.setUid(uid);
			
			Map<String,Map<String,String>> outerState=new HashMap<String, Map<String,String>>();
			gameCard.setOuterState(outerState);
			
			this.handGameCards.add(gameCard);
			
			// generate card event
			Event event=new Event();
			event.setName("cardevent");
			invokeEvent(event);
		}
		
	}
	
	
	public void addEventListener(Event event, List<Event> removeEvents, String code)
	{
		GameListener gl=new GameListener();
		gl.setGid(this.game.getId());
		gl.setUid(this.uid);
		gl.setEffectCode(code);
		gl.setEvent(event);
		gl.setRemoveEvents(removeEvents);
		
		this.listeners.add(gl);
	}
	
	public boolean setContext(String id)
	{
		this.currentSource=null;
		
		Value value=Helper.makeValue(id);
		
		if(isMyMinion(value))
		{
			this.currentSource=value;
		}
		
		if(heroMinion.getId().equals(id))
		{
			this.currentSource=value;
		}
		
		if(this.currentSource!=null)
			return true;
		else
			return false;
	}
	
	private Minion findMinionById(String id)
	{
				
		for(Minion minion: this.minions)
		{
			if(minion.getId().equals(id))
			{
				return minion;
			}
		}
		
		
		for(Minion minion: enemy.minions)
		{
			if(minion.getId().equals(id))
			{
				return minion;
			}
		}
		
		
		return null;
	}
	
	private GameCard findGameCardById(String id)
	{
				
		for(GameCard gc: this.handGameCards)
		{
			if(gc.getId().equals(id))
			{
				return gc;
			}
		}
		
		return null;
	}
	
	public void save()
	{
		// update the game
		gameDao.updateGame(game);
		
		// update the minion database
		minionDao.updateMinions(this.minions, this.getGame().getId(), uid);
				
		// update hero minion database
		heroMinionDao.updateHeroMinion(heroMinion);
	
		// update the game cards
		gameCardDao.updateGameCards(this.handGameCards, this.game.getId(), this.uid);
		
		// update listeners
		gameListenerDao.updateListeners(this.listeners, this.game.getId(), this.uid );
		
		// update weapon
		weaponDao.updateWeapon(this.weapon, this.weapon.getId(), this.game.getId());
		
		// make final diff operation
		if(this.clonePlayer==null )
		{
			makeSnapShot();
		}
		else
		{
			generateDiffActions();
			makeSnapShot();
		}
	}
	
	public void eliminateMinionState(String state)
	{
		for(Minion minion: this.minions)
		{
			if(minion.getInnerState().get(state)!=null)
			{
				minion.getInnerState().remove(state);
			}
		}
	}
	
	public void endRound()
	{
		
		
		// eliminate the freeze state from minions
		eliminateMinionState("freeze");
		
		// change game turn
		this.game.setTurn(game.getTurn()+1);
		
		// call end event
		Event event=new Event();
		event.setName("endevent");
		
		invokeEvent(event);
		
		// enemy begin
		enemy.beginRound();
	}
	
	public void beginRound()
	{
		// initialize the power 
		int maxpower1=Math.min(10,this.game.getMaxpower1()+1);
		int maxpower2=Math.min(10,this.game.getMaxpower2()+1);
		
		if(this.game.getTurn()%2==0)
		{
			this.game.setMaxpower1(maxpower1);
			this.game.setPower1(maxpower1);
		}
		else
		{
			this.game.setMaxpower2(maxpower2);
			this.game.setPower2(maxpower2);
		}
		
		// restore minion inner state
		restoreMinionState();
		
		// take a card
		takeCard(1);
		
//		// call begin event
		Event event=new Event();
		event.setName("beginevent");
		
		invokeEvent(event);
		
	}
	
	private void restoreMinionState()
	{
		for(Minion minion: this.minions)
		{
			// attack count
			
			if(minion.getInnerState().get("windrage")!=null)
				minion.getInnerState().put("attackcount", "2");
			else
				minion.getInnerState().put("attackcount", "1");
			
			// action
			
			minion.getInnerState().put("action", "ready");
		}
		
		heroMinion.getInnerState().put("attackcount", "1");
	}
	
	public void invokeEvent(Event event)
	{
		System.out.println("invoking event:" + event.getName());
				
		// evaluate the event code
		for(GameListener gl: this.listeners)
		{
			if(gl.getEvent().getName().equals(event.getName()))
			{
				System.out.println( this.userName+ " catch event: " +event.getName());
				
				gl.getEvent().setEventSource(event.getEventSource());
				gl.getEvent().setEventTarget(event.getEventTarget());
				Evaluator.evaluateListener(gl, this, enemy);
			}
		}
		
		// remove those listeners
		
		List<GameListener> copy=new LinkedList<GameListener>();
		copy.addAll(this.listeners);
		
		for(GameListener gl: copy)
		{
			
			for(Event removeEvent: gl.getRemoveEvents())
			{
			
				if(removeEvent.getName().equals(event.getName()))
				{
					if(removeEvent.getName().equals("endevent") || removeEvent.getName().equals("beginevent"))
					{
						this.listeners.remove(gl);
					}
					else if( removeEvent.getThisValue().equals(event.getEventSource()))
					{
						this.listeners.remove(gl);
						
						// eliminate the effect caused by this gl
						eliminateEffect(removeEvent);
					}
				}
			}
			
		}
		
		// special event handling with game card's target
		makeGameCardTarget();
				
		// special event handling with minion's target
		makeMinionTarget();
		
		// do a diff operation to generate actions
		if(this.clonePlayer==null )
		{
			makeSnapShot();
		}
		else
		{
			generateDiffActions();
			makeSnapShot();
		}
		
	}
	
	public void generateDiffActions()
	{
		// game diffs
		this.game.generateDiffActions(this.clonePlayer.game);
		
		// minion change or add diffs
		for(Minion minion: this.minions)
		{
			// find the minion in clone player
			Minion foundMinion=null;
			
			for(Minion minion2: this.clonePlayer.minions)
			{
				if(minion.getId().equals(minion2.getId()))
				{
					foundMinion=minion2;
					break;
				}
			}
			
			if(foundMinion!=null)
			{
				minion.generateDiffActions(foundMinion);
			}
			else
			{
				minion.generateBornActions();
			}
		}
		
		// minion lost diffs
		for(Minion minion: this.clonePlayer.minions)
		{
			// find the minion in this player
			Minion foundMinion=null;
			
			for(Minion minion2: this.minions)
			{
				if(minion.getId().equals(minion2.getId()))
				{
					foundMinion=minion2;
					break;
				}
			}
			
			if(foundMinion==null)
			{
				minion.generateLostActions();
			}
		}
		
		// hero minion diffs
		
		this.heroMinion.generateDiffActions(this.clonePlayer.heroMinion);
		
		// game card change or add diffs
		for(GameCard gc: this.handGameCards)
		{
			// find the minion in clone player
			GameCard foundGameCard=null;
			
			for(GameCard gc2: this.clonePlayer.handGameCards)
			{
				if(gc.getId().equals(gc2.getId()))
				{
					foundGameCard=gc2;
					break;
				}
			}
			
			if(foundGameCard!=null)
			{
				gc.generateDiffActions(foundGameCard);
			}
			else
			{
				gc.generateNewActions();
			}
		}
		
		// game card lost diffs
		for(GameCard gc: this.clonePlayer.handGameCards)
		{
			// find the minion in this player
			GameCard foundGameCard=null;
			
			for(GameCard gc2: this.handGameCards)
			{
				if(gc.getId().equals(gc2.getId()))
				{
					foundGameCard=gc2;
					break;
				}
			}
			
			if(foundGameCard==null)
			{
				gc.generateLostActions();
			}
		}
		
		// weapon diff
		
		this.weapon.generateDiffActions(this.clonePlayer.weapon);
		
	}
	
	private void eliminateEffect(Event removeEvent)
	{
		for(Minion minion: this.minions)
		{
			for(String sid: minion.getOuterState().keySet())
			{
				if(sid.equals(removeEvent.getThisValue().getSet().get(0)))
				{
					// deal with blood effect elimination
					Map<String, String> state=minion.getOuterState().get(sid);
					for(String key: state.keySet())
					{
						if(key.equals("blood"))
						{
							if(minion.getBlood()+Integer.parseInt(state.get(key))<=minion.getMaxBlood())
								minion.setBlood(minion.getBlood()+Integer.parseInt(state.get(key)));
							else
								minion.setBlood(minion.getMaxBlood());
						}
					}
					
					minion.getOuterState().remove(sid);
					break;
				}
			}
		}
		
		for(GameCard gc: this.handGameCards)
		{
			for(String sid: gc.getOuterState().keySet())
			{
				if(sid.equals(removeEvent.getThisValue().getSet().get(0)))
				{
					gc.getOuterState().remove(sid);
					break;
				}
			}
		}
	}
	
	public boolean isMinion(Value source)
	{
		String id=source.getSet().get(0);
		
		Minion minion=findMinionById(id);
		
		if(minion!=null)
			return true;
		else
			return false;
	}
	
	public boolean isMyMinion(Value source)
	{
		String id=source.getSet().get(0);
		
		for(Minion minion: this.minions)
		{
			if(id.equals(minion.getId()))
				return true;
		}
		
		return false;
	}
	
	public boolean isWeapon(Value source)
	{
		String id=source.getSet().get(0);
		
		if(id.equals(weapon.getId()))
		{
			return true;
		}
						
		return false;
	}
	
	public void print()
	{
		System.out.print("Game: "+game.getId()+",");
		System.out.print("userName: "+this.userName+",");
		System.out.print("uid: "+this.uid+",");
		
		if(this.userName.equals(game.getUserName1()))
		{
			System.out.println("power: "+game.getPower1());
			System.out.println("maxpower: "+game.getMaxpower1());
		}
		else
		{
			System.out.println("power: "+game.getPower2());
			System.out.println("maxpower: "+game.getMaxpower2());
		}
		
		
		System.out.println("hero cards");
		System.out.print(heroCard.getName()+",targets:");
		
		if(heroGameCard.getTargets()==null)
			System.out.print("null");
		else
		{
			System.out.print("[");
			for(String id: heroGameCard.getTargets().getSet())
				System.out.print(id+",");
		}
		
		System.out.println("],usable:"+heroGameCard.isUsable()+",needTarget:"+heroGameCard.isNeedTarget());
		
		System.out.println("game cards:");
		for(GameCard gc: this.handGameCards)
		{
			gc.print();
		}
		
		
		System.out.println("minions:");
		for(Minion minion: this.minions)
		{
			minion.print();
		}
		
		System.out.println("hero minion:");
		heroMinion.print();
		
		System.out.println("weapon:");
		weapon.print();
		
		
		System.out.println("listeners:");
		
		for(GameListener gl: this.listeners)
		{
			System.out.print(gl.getEvent().getName()+",[");
			for(Event removeEvent: gl.getRemoveEvents())
				System.out.print(removeEvent.getName()+",");
						
			System.out.println("],"+gl.getEffectCode());
		}
		
	}
	
	
	
}




















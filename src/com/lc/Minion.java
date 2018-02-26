package com.lc;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import utils.Helper;

import com.lc.playing.GameAction;
import com.lc.playing.Value;

public class Minion implements Comparable 
{
	
	private String id;
	private String gid;
	private String uid;
	private String cardName;
	private String imageid;
	private String race;
	private int attack;
	private int blood;
	private int maxBlood;
	private String state;
	private String outState;
	private int index;
	
	private Map<String, String> innerState;
	private Map<String,Map<String, String>> outerState;
	
	private Value targets;
	
	public Minion()
	{
		
	}
	
	public Minion(Minion minion)
	{
		this.setId(minion.id); // id
		this.setGid(minion.gid); // gid
		this.setUid(minion.uid); // uid
		this.setCardName(minion.cardName); // cardname
		this.setImageid(minion.imageid); // imageid
		this.setRace(minion.race); // race
		this.setAttack(minion.attack); // attack
		this.setBlood(minion.blood); // blood
		this.setMaxBlood(minion.maxBlood); // maxBlood
		this.setState(minion.state); // state
		this.setOutState(minion.outState); // outstate
		this.setIndex(minion.index); // index
		
		//this.setInnerState(Helper.parseState(minion.getState()));
		
		Map<String, String> innerState=new HashMap<String, String>();
		
		for(String key: minion.innerState.keySet())
		{
			innerState.put(key, minion.innerState.get(key));
		}
		
		this.setInnerState(innerState);
		
		Map<String, Map<String,String>> outerState=new HashMap<String,Map<String,String>>();
		for(String sid: minion.outerState.keySet())
		{
			Map<String, String> nmap=new HashMap<String,String>();
			Map<String,String> map=minion.outerState.get(sid);
			
			for(String key: map.keySet())
			{
				nmap.put(key, map.get(key));
			}
			
			outerState.put(sid, nmap);
			
		}
		
		this.setOuterState(outerState);
		
		this.setTargets(new Value(minion.getTargets()));
	}
	
	public Value getTargets() {
		return targets;
	}
	public void setTargets(Value targets) {
		this.targets = targets;
	}
	public Map<String, Map<String, String>> getOuterState() {
		return outerState;
	}
	public void setOuterState(Map<String, Map<String, String>> outerState) {
		this.outerState = outerState;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getOutState() {
		return outState;
	}
	public void setOutState(String outState) {
		this.outState = outState;
	}
	public Map<String, String> getInnerState() {
		return innerState;
	}
	public void setInnerState(Map<String, String> innerState) {
		this.innerState = innerState;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		
		//int oldAttack=this.attack;
		this.attack = attack;
		
		//ActionGenerator.minionAttack(this, oldAttack, this.attack);
	}
	public int getBlood() {
		return blood;
	}
	public void setBlood(int blood) {
		
		//int oldBlood=this.blood;
		this.blood = blood;
		
		//ActionGenerator.minionBlood(this, oldBlood, this.blood);
			
	}
	public int getMaxBlood() {
		return maxBlood;
	}
	public void setMaxBlood(int maxBlood) {
		//int oldMaxBlood=this.maxBlood;
		this.maxBlood = maxBlood;
		
		//ActionGenerator.minionMaxBlood(this, oldMaxBlood, this.maxBlood);
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void print()
	{
		System.out.print(this.id+","+this.cardName+","+this.index+"," +
				this.attack+","+this.blood+",innerState[");
		
		for(String key: innerState.keySet())
		{
			System.out.print(key+":"+innerState.get(key)+",");
		}
		
		System.out.print("],outerstate[");
		
		if(outerState!=null)
		{
			for(String key: outerState.keySet())
			{
				System.out.print(key+":"+outerState.get(key)+",");
			}
		}
		
		System.out.print("],targets[");
		
		if(targets!=null)
		{
			for(String id: targets.getSet())
				System.out.print(id+",");
		}
		
		System.out.println("]");
	}
	@Override
	public int compareTo(Object other) {
		// TODO Auto-generated method stub
		Minion minion=(Minion)other;
		
		return this.index-minion.index;
	}
	
	public void generateDiffActions(Minion minion)
	{
		// attack diff
		if(this.attack!=minion.attack)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("minionattack");
			gameAction.setOldMinion(minion);
			gameAction.setNewMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// blood diff
		if(this.blood!=minion.blood)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("minionblood");
			gameAction.setOldMinion(minion);
			gameAction.setNewMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
				
		
		// maxblood diff
		if(this.maxBlood!=minion.maxBlood)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("minionmaxblood");
			gameAction.setOldMinion(minion);
			gameAction.setNewMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// index diff
		if(this.index!=minion.index)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("minionindex");
			gameAction.setOldMinion(minion);
			gameAction.setNewMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// inner state diff
		
		
			// change or add state
		for(String key: this.innerState.keySet())
		{
			if(minion.innerState.get(key)==null || !this.innerState.get(key).equals(minion.innerState.get(key)))
			{
				
				GameAction gameAction=new GameAction();
				gameAction.setName("minioninnerstateadd");
				gameAction.setStateName(key);
				gameAction.setOldMinion(minion);
				gameAction.setNewMinion(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
			}
		}
		
			// state lost
		for(String key: minion.innerState.keySet())
		{
			if(this.innerState.get(key)==null)
			{
				GameAction gameAction=new GameAction();
				gameAction.setName("minioninnerstatelost");
				gameAction.setStateName(key);
				gameAction.setOldMinion(minion);
				gameAction.setNewMinion(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
			}
		}
		
		// outer state diff
		
			// change or add state
		for(String sid: this.outerState.keySet())
		{
			
			if(minion.outerState.get(sid)==null )
			{
				Map<String, String> map=this.outerState.get(sid);				
				for(String key: map.keySet())
				{
					GameAction gameAction=new GameAction();
					gameAction.setName("minionouterstateadd");
					gameAction.setSid(sid);
					gameAction.setStateName(key);
					gameAction.setOldMinion(minion);
					gameAction.setNewMinion(this);
					
					System.out.println(gameAction);
					gameAction.writeDatabase();
				}
			}
			else
			{
				Map<String, String> map=this.outerState.get(sid);	
				Map<String, String> map2=minion.outerState.get(sid);
				for(String key: map.keySet())
				{
					if(!map.get(key).equals(map2.get(key)))
					{
						GameAction gameAction=new GameAction();
						gameAction.setName("minionouterstateadd");
						gameAction.setSid(sid);
						gameAction.setStateName(key);
						gameAction.setOldMinion(minion);
						gameAction.setNewMinion(this);
						
						System.out.println(gameAction);
						gameAction.writeDatabase();
					}
				}
			}
		}
		
			// state lost
		
		for(String sid: minion.outerState.keySet())
		{
			
			if(this.outerState.get(sid)==null )
			{
				
				GameAction gameAction=new GameAction();
				gameAction.setName("minionouterstatelost");
				gameAction.setSid(sid);
				
				gameAction.setOldMinion(minion);
				gameAction.setNewMinion(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
				
			}
			
		}
		
		// targets diff
		
		if((this.targets==null && minion.targets!=null) || ( this.targets!=null && 
				( minion.targets==null || !this.targets.generalEquals(minion.targets) ) ) )
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("miniontargets");
			
			gameAction.setOldMinion(minion);
			gameAction.setNewMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		
		}
	}
	
	public void generateBornActions()
	{
		
		GameAction gameAction=new GameAction();
		gameAction.setName("minionborn");
		gameAction.setNewMinion(this);
		
		System.out.println(gameAction);
		gameAction.writeDatabase();
		
	}
	
	public void generateLostActions()
	{
		
		GameAction gameAction=new GameAction();
		gameAction.setName("minionlost");
		gameAction.setOldMinion(this);
		
		System.out.println(gameAction);
		gameAction.writeDatabase();
	}
	
	public JSONObject getAttackJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONATTACK);
		
		jso.put("cid", this.id);
		jso.put("attack", this.attack);
		
		return jso;
	}
	
	public JSONObject getBloodJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONBLOOD);
		
		jso.put("cid", this.id);
		jso.put("blood", this.blood);
		
		return jso;
	}
	
	public JSONObject getMaxBloodJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONMAXBLOOD);
		
		jso.put("cid", this.id);
		jso.put("maxblood", this.maxBlood);
		
		return jso;
	}
	
	public JSONObject getIndexJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONINDEX);
		
		jso.put("cid", this.id);
		jso.put("index", this.index);
		
		return jso;
	}
	
	public JSONObject getInnerStateAddJSON(String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONINNERSTATEADD);
		
		jso.put("cid", this.id);
		jso.put("key", stateName);
		jso.put("value", this.innerState.get(stateName));
		
		return jso;
	}
	
	public JSONObject getInnerStateLostJSON(String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONINNERSTATELOST);
		
		jso.put("cid", this.id);
		jso.put("key", stateName);
		
		return jso;
	}
	
	public JSONObject getOuterStateAddJSON(String sid, String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONOUTERSTATEADD);
		
		jso.put("cid", this.id);
		jso.put("sid", sid);
		jso.put("key", stateName);
		jso.put("value", this.outerState.get(sid).get(stateName));
		
		return jso;
	}
	
	public JSONObject getOuterStateLostJSON(String sid)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONOUTERSTATELOST);
		
		jso.put("cid", this.id);
		jso.put("sid", sid);
		
		
		return jso;
	}
	
	public JSONObject getTargetsJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONTARGETS);
		jso.put("cid", this.id);
		
		JSONArray targetsArray=new JSONArray();
		for(String id: this.targets.getSet())
		{
			targetsArray.add(id);
		}
		
		jso.put("targets", targetsArray);
		
		return jso;
	}
	
	public JSONObject getBornJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONBORN);
		jso.put("pid", this.uid);
		
		JSONObject minionObject=Helper.minion2JSO(this);
		
		jso.put("minion", minionObject);
		
		return jso;
	}
	
	public JSONObject getLostJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.MINIONLOST);
		jso.put("cid", this.id);
				
		return jso;
	}
	
}












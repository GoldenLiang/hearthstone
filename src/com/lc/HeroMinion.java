package com.lc;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lc.playing.GameAction;
import com.lc.playing.Value;

public class HeroMinion {
	
	private String id;
	private String gid;
	private String job;
	private int attack;
	private int blood;
	private int guard;
	private String state;
	
	private Map<String, String> innerState;
	
	private Value targets;
	
	public HeroMinion()
	{
		
	}
	
	public HeroMinion(HeroMinion heroMinion)
	{
		this.setId(heroMinion.id);
		this.setGid(heroMinion.gid);
		this.setJob(heroMinion.job);
		
		this.setAttack(heroMinion.attack);
		this.setBlood(heroMinion.blood);
		this.setGuard(heroMinion.guard);
		this.setState(heroMinion.state);
										
		Map<String, String> innerState=new HashMap<String, String>();
		
		for(String key: heroMinion.innerState.keySet())
		{
			innerState.put(key, heroMinion.innerState.get(key));
		}
		
		this.setInnerState(innerState);
		
		this.setTargets(new Value(heroMinion.getTargets()));
	}
	
		
	public Value getTargets() {
		return targets;
	}
	public void setTargets(Value targets) {
		this.targets = targets;
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
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getBlood() {
		return blood;
	}
	public void setBlood(int blood) {
		this.blood = blood;
	}
	public int getGuard() {
		return guard;
	}
	public void setGuard(int guard) {
		this.guard = guard;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public void print()
	{
		System.out.print(this.getId()+","+this.getAttack()+","+
				this.getBlood()+","+this.getGuard()+",innerstate:["
			);
		
		for(String key: innerState.keySet())
		{
			System.out.print(key+":"+innerState.get(key)+",");
		}
		
		System.out.print("],targets[");
		for(String id: targets.getSet())
		{
			System.out.print(id+",");
		}
		
		System.out.println("]");
	}
	
	public void generateDiffActions(HeroMinion heroMinion)
	{
		// attack diff
		if(this.attack!=heroMinion.attack)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("herominionattack");
			gameAction.setOldHeroMinion(heroMinion);
			gameAction.setNewHeroMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// blood diff
		if(this.blood!=heroMinion.blood)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("herominionblood");
			gameAction.setOldHeroMinion(heroMinion);
			gameAction.setNewHeroMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// hero die
		if(this.blood<=0)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("herominiondie");
			gameAction.setOldHeroMinion(heroMinion);
			gameAction.setNewHeroMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
			
			gameAction=new GameAction();
			gameAction.setName("win");
			gameAction.setOldHeroMinion(heroMinion);
			gameAction.setNewHeroMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// guard diff
		if(this.guard!=heroMinion.guard)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("herominionguard");
			gameAction.setOldHeroMinion(heroMinion);
			gameAction.setNewHeroMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
				
		// inner state diff
		
		
			// change or add state
		for(String key: this.innerState.keySet())
		{
			if(heroMinion.innerState.get(key)==null || !this.innerState.get(key).equals(heroMinion.innerState.get(key)))
			{
				
				GameAction gameAction=new GameAction();
				gameAction.setName("herominioninnerstateadd");
				gameAction.setStateName(key);
				gameAction.setOldHeroMinion(heroMinion);
				gameAction.setNewHeroMinion(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
			}
		}
		
			// state lost
		for(String key: heroMinion.innerState.keySet())
		{
			if(this.innerState.get(key)==null)
			{
				GameAction gameAction=new GameAction();
				gameAction.setName("herominioninnerstatelost");
				gameAction.setStateName(key);
				gameAction.setOldHeroMinion(heroMinion);
				gameAction.setNewHeroMinion(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
			}
		}
		
		
		
		// targets diff
		
		if((this.targets==null && heroMinion.targets!=null) || ( this.targets!=null && 
				( heroMinion.targets==null || !this.targets.generalEquals(heroMinion.targets) ) ) )
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("herominiontargets");
			
			gameAction.setOldHeroMinion(heroMinion);
			gameAction.setNewHeroMinion(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		
		}
	}
	
	public JSONObject getAttackJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONATTACK);
		
		jso.put("cid", this.id);
		jso.put("attack", this.attack);
		
		return jso;
	}
	
	public JSONObject getBloodJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONBLOOD);
		
		jso.put("cid", this.id);
		jso.put("blood", this.blood);
		
		return jso;
	}
	
	public JSONObject getDieJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONDIE);
		
		jso.put("cid", this.id);
				
		return jso;
	}
	
	public JSONObject getWinJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WIN);
		
		String id="";
		if(this.id.equals("g61"))
			id="g62";
		else
			id="g61";
		
		jso.put("cid", id);
				
		return jso;
	}
	
	public JSONObject getGuardJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONGUARD);
		
		jso.put("cid", this.id);
		jso.put("guard", this.guard);
		
		return jso;
	}
	
	public JSONObject getInnerStateAddJSON(String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONINNERSTATEADD);
		
		jso.put("cid", this.id);
		jso.put("key", stateName);
		jso.put("value", this.innerState.get(stateName));
		
		return jso;
	}
	
	public JSONObject getInnerStateLostJSON(String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONINNERSTATELOST);
		
		jso.put("cid", this.id);
		jso.put("key", stateName);
		
		return jso;
	}
	
	public JSONObject getTargetsJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.HEROMINIONTARGETS);
		jso.put("cid", this.id);
		
		JSONArray targetsArray=new JSONArray();
		for(String id: this.targets.getSet())
		{
			targetsArray.add(id);
		}
		
		jso.put("targets", targetsArray);
		
		return jso;
	}
	
}

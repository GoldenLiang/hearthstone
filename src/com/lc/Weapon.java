package com.lc;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import utils.Helper;
import com.lc.playing.GameAction;

public class Weapon {
	
	private String id;
	private String gid;
	private String uid;
	private int attack;
	private int blood;
	private String state;
	
	private Map<String, String> innerState;
	private Map<String, Map<String,String>> outerState;
	
	public Weapon()
	{
		
	}
	
	public Weapon(Weapon weapon)
	{
		this.setId(weapon.id); // id
		this.setGid(weapon.gid); // gid
		this.setUid(weapon.uid); // uid
		this.setAttack(weapon.attack); // attack
		this.setBlood(weapon.blood); // blood
		this.setState(weapon.state); // state
				
		this.setInnerState(Helper.parseState(weapon.getState()));
		
		Map<String, Map<String,String>> outerState=new HashMap<String,Map<String,String>>();
		for(String sid: weapon.outerState.keySet())
		{
			Map<String, String> nmap=new HashMap<String,String>();
			Map<String,String> map=weapon.outerState.get(sid);
			
			for(String key: map.keySet())
			{
				nmap.put(key, map.get(key));
			}
			
			outerState.put(sid, nmap);
			
		}
		
		this.setOuterState(outerState);
		
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Map<String, String> getInnerState() {
		return innerState;
	}
	public void setInnerState(Map<String, String> innerState) {
		this.innerState = innerState;
	}
	public Map<String, Map<String, String>> getOuterState() {
		return outerState;
	}
	public void setOuterState(Map<String, Map<String, String>> outerState) {
		this.outerState = outerState;
	}
	
	public void print()
	{
		System.out.print(this.getId()+","+this.getAttack()+","+this.getBlood()+",innerstate[");
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
		
		System.out.println("]");
	}
	
	public void generateDiffActions(Weapon weapon)
	{
		// attack diff
		if(this.attack!=weapon.attack)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("weaponattack");
			gameAction.setOldWeapon(weapon);
			gameAction.setNewWeapon(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		// blood diff
		if(this.blood!=weapon.blood)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("weaponblood");
			gameAction.setOldWeapon(weapon);
			gameAction.setNewWeapon(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		
		// inner state diff
		
		
			// change or add state
		for(String key: this.innerState.keySet())
		{
			if(weapon.innerState.get(key)==null || !this.innerState.get(key).equals(weapon.innerState.get(key)))
			{
				
				GameAction gameAction=new GameAction();
				gameAction.setName("weaponinnerstateadd");
				gameAction.setStateName(key);
				gameAction.setOldWeapon(weapon);
				gameAction.setNewWeapon(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
			}
		}
		
			// state lost
		for(String key: weapon.innerState.keySet())
		{
			if(this.innerState.get(key)==null)
			{
				GameAction gameAction=new GameAction();
				gameAction.setName("weaponinnerstatelost");
				gameAction.setStateName(key);
				gameAction.setOldWeapon(weapon);
				gameAction.setNewWeapon(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
			}
		}
		
		// outer state diff
		
			// change or add state
		for(String sid: this.outerState.keySet())
		{
			
			if(weapon.outerState.get(sid)==null )
			{
				Map<String, String> map=this.outerState.get(sid);				
				for(String key: map.keySet())
				{
					GameAction gameAction=new GameAction();
					gameAction.setName("minionouterstateadd");
					gameAction.setSid(sid);
					gameAction.setStateName(key);
					gameAction.setOldWeapon(weapon);
					gameAction.setNewWeapon(this);
					
					System.out.println(gameAction);
					gameAction.writeDatabase();
				}
			}
			else
			{
				Map<String, String> map=this.outerState.get(sid);	
				Map<String, String> map2=weapon.outerState.get(sid);
				for(String key: map.keySet())
				{
					if(!map.get(key).equals(map2.get(key)))
					{
						GameAction gameAction=new GameAction();
						gameAction.setName("minionouterstateadd");
						gameAction.setSid(sid);
						gameAction.setStateName(key);
						gameAction.setOldWeapon(weapon);
						gameAction.setNewWeapon(this);
						
						System.out.println(gameAction);
						gameAction.writeDatabase();
					}
				}
			}
		}
		
			// state lost
		
		for(String sid: weapon.outerState.keySet())
		{
			
			if(this.outerState.get(sid)==null )
			{
				
				GameAction gameAction=new GameAction();
				gameAction.setName("minionouterstatelost");
				gameAction.setSid(sid);
				
				gameAction.setOldWeapon(weapon);
				gameAction.setNewWeapon(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
				
			}
			
		}
	}
	
	public JSONObject getAttackJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WEAPONATTACK);
		
		jso.put("cid", this.uid);
		jso.put("attack", this.attack);
		
		return jso;
	}
	
	public JSONObject getBloodJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WEAPONBLOOD);
		
		jso.put("cid", this.uid);
		jso.put("blood", this.blood);
		
		return jso;
	}
	
	public JSONObject getInnerStateAddJSON(String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WEAPONINNERSTATEADD);
		
		jso.put("cid", this.uid);
		jso.put("key", stateName);
		jso.put("value", this.innerState.get(stateName));
		
		return jso;
	}
	
	public JSONObject getInnerStateLostJSON(String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WEAPONINNERSTATELOST);
		
		jso.put("cid", this.uid);
		jso.put("key", stateName);
		
		return jso;
	}
	
	public JSONObject getOuterStateAddJSON(String sid, String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WEAPONOUTERSTATEADD);
		
		jso.put("cid", this.uid);
		jso.put("sid", sid);
		jso.put("key", stateName);
		jso.put("value", this.outerState.get(sid).get(stateName));
		
		return jso;
	}
	
	public JSONObject getOuterStateLostJSON(String sid)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.WEAPONOUTERSTATELOST);
		
		jso.put("cid", this.uid);
		jso.put("sid", sid);
		
		
		return jso;
	}
	
}











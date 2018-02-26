package com.lc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import utils.Helper;
import com.lc.playing.GameAction;
import com.lc.playing.Value;

public class GameCard {

	private String id;
	private String gid;
	private String uid;
	
	private String cardName;
	private String type;
	private String state;
	
	private Map<String,Map<String, String>> outerState;
	
	private Value targets;
	private boolean usable;
	private boolean needTarget;
	
	public GameCard()
	{
		
	}
	
	public GameCard(GameCard gameCard)
	{
		this.setId(gameCard.id); // id
		this.setGid(gameCard.gid); // gid
		
		this.setUid(gameCard.uid); // uid
		this.setCardName(gameCard.cardName); // cardname
		this.setType(gameCard.type); // type
		this.setState(gameCard.state); // state
		
		Map<String,Map<String,String>> outerState=new HashMap<String, Map<String,String>>();
		
		for(String sid: gameCard.outerState.keySet())
		{
			Map<String, String> nmap=new HashMap<String,String>();
			Map<String,String> map=gameCard.outerState.get(sid);
			
			for(String key: map.keySet())
			{
				nmap.put(key, map.get(key));
			}
			
			outerState.put(sid, nmap);
			
		}
		
		this.setOuterState(outerState);
		
		if(gameCard.targets==null)
			this.setTargets(null);
		else
		{
			Value targets=new Value(gameCard.targets);
			
			this.setTargets(targets);
		}
		
		this.setUsable(gameCard.usable);
		this.setNeedTarget(gameCard.needTarget);
		
	}
	
	
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Map<String, Map<String, String>> getOuterState() {
		return outerState;
	}
	public void setOuterState(Map<String, Map<String, String>> outerState) {
		this.outerState = outerState;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Value getTargets() {
		return targets;
	}
	public void setTargets(Value targets) {
		this.targets = targets;
	}
	public boolean isUsable() {
		return usable;
	}
	public void setUsable(boolean usable) {
		this.usable = usable;
	}
		
	public boolean isNeedTarget() {
		return needTarget;
	}
	public void setNeedTarget(boolean needTarget) {
		this.needTarget = needTarget;
	}
	
	
	public void print()
	{
		System.out.print(this.getId()+","+this.getCardName()+", outerstate:[");
		
		for(String sid: this.getOuterState().keySet())
		{
			System.out.print(sid+":{");
			Map<String,String> map=this.getOuterState().get(sid);
			if(map!=null)
			{
				for(String key: map.keySet())
				{
					System.out.print(key+":"+map.get(key)+",");
				}
			}
			
			System.out.print("},");
		}
				
		System.out.print("],targets:");
		
		if(getTargets()==null)
			System.out.print("null");
		else
		{
			System.out.print("[");
			for(String id: getTargets().getSet())
				System.out.print(id+",");
			
			System.out.print("]");
		}
		
		System.out.println(",usable:"+usable+",needTarget:"+needTarget);
	}
	
	public void generateDiffActions(GameCard gameCard)
	{
		// outer state diff
		
			// change or add state
		for(String sid: this.outerState.keySet())
		{
			
			if(gameCard.outerState.get(sid)==null )
			{
				Map<String, String> map=this.outerState.get(sid);				
				for(String key: map.keySet())
				{
					GameAction gameAction=new GameAction();
					gameAction.setName("gamecardouterstateadd");
					gameAction.setSid(sid);
					gameAction.setStateName(key);
					gameAction.setOldGameCard(gameCard);
					gameAction.setNewGameCard(this);
					
					System.out.println(gameAction);
					gameAction.writeDatabase();
				}
			}
			else
			{
				Map<String, String> map=this.outerState.get(sid);	
				Map<String, String> map2=gameCard.outerState.get(sid);
				for(String key: map.keySet())
				{
					if(!map.get(key).equals(map2.get(key)))
					{
						GameAction gameAction=new GameAction();
						gameAction.setName("gamecardouterstateadd");
						gameAction.setSid(sid);
						gameAction.setStateName(key);
						gameAction.setOldGameCard(gameCard);
						gameAction.setNewGameCard(this);
						
						System.out.println(gameAction);
						gameAction.writeDatabase();
					}
				}
			}
		}
		
			// state lost
		
		for(String sid: gameCard.outerState.keySet())
		{
			
			if(this.outerState.get(sid)==null )
			{
				
				GameAction gameAction=new GameAction();
				gameAction.setName("gamecardouterstatelost");
				gameAction.setSid(sid);
				
				gameAction.setOldGameCard(gameCard);
				gameAction.setNewGameCard(this);
				
				System.out.println(gameAction);
				gameAction.writeDatabase();
				
			}
			
		}
		
		// targets diff
		
		if((this.targets==null && gameCard.targets!=null) || ( this.targets!=null && 
				( gameCard.targets==null || !this.targets.generalEquals(gameCard.targets) ) ) )
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("gamecardtargets");
			
			gameAction.setOldGameCard(gameCard);
			gameAction.setNewGameCard(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		
		}
		
		// usable diff
		
		if(this.usable!=gameCard.usable)
		{
			GameAction gameAction=new GameAction();
			gameAction.setName("gamecardusable");
			
			gameAction.setOldGameCard(gameCard);
			gameAction.setNewGameCard(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
	}
	
	public void generateNewActions()
	{
		GameAction gameAction=new GameAction();
		gameAction.setName("gamecardnew");
		gameAction.setNewGameCard(this);
		
		System.out.println(gameAction);
		gameAction.writeDatabase();
	}
	
	public void generateLostActions()
	{
		GameAction gameAction=new GameAction();
		gameAction.setName("gamecardlost");
		gameAction.setOldGameCard(this);
		
		System.out.println(gameAction);
		gameAction.writeDatabase();
	}
	
	
	public JSONObject getOuterStateAddJSON(String sid, String stateName)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECARDOUTERSTATEADD);
		jso.put("pid", this.uid);
		jso.put("cid", this.id);
		jso.put("sid", sid);
		jso.put("key", stateName);
		jso.put("value", this.outerState.get(sid).get(stateName));
		
		return jso;
	}
	
	public JSONObject getOuterStateLostJSON(String sid)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECARDOUTERSTATELOST);
		jso.put("pid", this.uid);
		jso.put("cid", this.id);
		jso.put("sid", sid);
				
		return jso;
	}
	
	public JSONObject getTargetsJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECARDTARGETS);
		jso.put("pid", this.uid);
		jso.put("cid", this.id);
		
		JSONArray targetsArray=new JSONArray();
		
		if(this.targets!=null)
		{
			for(String id: this.targets.getSet())
			{
				targetsArray.add(id);
			}
		}
		
		jso.put("targets", targetsArray);
		
		return jso;
	}
	
	public JSONObject getNewJSON(String uid)
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECARDNEW);
		jso.put("pid", this.uid);
		
		JSONObject gameCardObject=null;
		
		if(this.uid.equals(uid))
			gameCardObject=Helper.gameCard2JSO(this);
		else
		{
			gameCardObject=new JSONObject();
			gameCardObject.put("id", this.getId());
		}
		
		jso.put("gamecard", gameCardObject);
		
		return jso;
	}
	
	public JSONObject getLostJSON()
	{
		System.out.println("get lost json");
		
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECARDLOST); 
		jso.put("pid", this.uid);
		jso.put("cid", this.id);
		
		System.out.println(jso);
				
		return jso;
	}
	
	public JSONObject getUsableJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECARDUSABLE);
		jso.put("pid", this.uid);
		jso.put("cid", this.id);
		jso.put("usable", this.usable);
		
		return jso;
	}
}












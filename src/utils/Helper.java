package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.lc.Action;
import com.lc.Card;
import com.lc.GameCard;
import com.lc.Minion;
import com.lc.Weapon;
import com.lc.playing.Player;
import com.lc.playing.Value;

public class Helper {
	
	public  static JSONObject card2JSO(Card card)
	{
		JSONObject cardObject=new JSONObject();
		
		cardObject.put("imageid", card.getImageid());
		cardObject.put("name", card.getName());
		cardObject.put("type", card.getType());
		cardObject.put("job", card.getJob());
		cardObject.put("rate", card.getRate());
		cardObject.put("group", card.getGroup());
		cardObject.put("cost", card.getCost());
		cardObject.put("attack", card.getAttack());
		cardObject.put("blood", card.getBlood());
		
		cardObject.put("effects", card.getEffects());
		
		return cardObject;
	}
	
	public  static JSONObject action2JSO(Action action)
	{
		JSONObject actionObject=JSONObject.fromObject(action.getActionJSON());
		
		return actionObject;
	}
	
	public  static JSONObject map2JSO(Map<String, String> map)
	{
				
		JSONObject retObject=new JSONObject();
		for(String key: map.keySet())
		{
			retObject.put(key, map.get(key));
		}
		
		return retObject;
	}
	
	public  static JSONObject mapmap2JSO(Map<String, Map<String,String>> mapmap)
	{
				
		JSONObject retObject=new JSONObject();
		for(String sid: mapmap.keySet())
		{
			JSONObject innerObject=map2JSO(mapmap.get(sid));
			retObject.put(sid, innerObject);
		}
		
		return retObject;
	}
	
	public static JSONObject minion2JSO(Minion minion)
	{
		JSONObject minionObject=new JSONObject();
		
		minionObject.put("imageid", minion.getImageid());
		minionObject.put("id", minion.getId());
		minionObject.put("index", minion.getIndex());
		minionObject.put("name", minion.getCardName());
		minionObject.put("attack", minion.getAttack());
		minionObject.put("blood", minion.getBlood());
		minionObject.put("maxblood", minion.getMaxBlood());
		
		JSONObject innerStateObject=map2JSO(minion.getInnerState());
		minionObject.put("innerstate", innerStateObject);
		
		JSONObject outerStateObject=mapmap2JSO(minion.getOuterState());
		minionObject.put("outerstate", outerStateObject);
		
		JSONArray targetsArray=new JSONArray();
		for(String id: minion.getTargets().getSet())
		{
			targetsArray.add(id);
		}
		
		minionObject.put("targets", targetsArray);
		
		return minionObject;
	}
	
	public static JSONObject gameCard2JSO(GameCard gameCard)
	{
		JSONObject gameCardObject=new JSONObject();
		
		
		gameCardObject.put("id", gameCard.getId());
		
		
		gameCardObject.put("cardname", gameCard.getCardName());
				
		JSONObject outerStateObject=mapmap2JSO(gameCard.getOuterState());
		gameCardObject.put("outerstate", outerStateObject);
		
		JSONArray targetsArray=new JSONArray();
		
		if(gameCard.getTargets()!=null)
			for(String id: gameCard.getTargets().getSet())
			{
				targetsArray.add(id);
			}
		
		gameCardObject.put("targets", targetsArray);
		gameCardObject.put("usable", gameCard.isUsable());
		gameCardObject.put("needTarget", gameCard.isNeedTarget());
		
		return gameCardObject;
	}
	
	public static JSONObject weapon2JSO(Weapon weapon)
	{
		JSONObject weaponObject=new JSONObject();
		
		
		weaponObject.put("attack", weapon.getAttack());
		weaponObject.put("blood", weapon.getBlood());
		
		JSONObject innerStateObject=map2JSO(weapon.getInnerState());
		weaponObject.put("innerstate", innerStateObject);
		
		JSONObject outerStateObject=mapmap2JSO(weapon.getOuterState());
		weaponObject.put("outerstate", outerStateObject);
		
		return weaponObject;
	}
	
	public static JSONObject player2JSO(Player player, boolean isPlayer)
	{
		JSONObject playerObject=new JSONObject();
				
		playerObject.put("id", player.getHeroMinion().getId());
		playerObject.put("name", player.getHeroMinion().getJob());
		playerObject.put("blood", player.getHeroMinion().getBlood());
		playerObject.put("guard", player.getHeroMinion().getGuard());
		playerObject.put("attack", player.getHeroMinion().getAttack());
		
		if(player.getUserName().equals(player.getGame().getUserName1()))
		{
			playerObject.put("power",player.getGame().getPower1());
			playerObject.put("maxpower", player.getGame().getMaxpower1());
		}
		else
		{
			playerObject.put("power",player.getGame().getPower2());
			playerObject.put("maxpower", player.getGame().getMaxpower2());
		}
		
		
		JSONArray handArray=new JSONArray();
		for(GameCard gc: player.getHandGameCards())
		{
			JSONObject gcObject;
			if(isPlayer)
				gcObject=Helper.gameCard2JSO(gc);
			else
			{
				gcObject=new JSONObject();
				gcObject.put("id", gc.getId());
			}
						
			handArray.add(gcObject);
		}
		
		playerObject.put("handCards", handArray);
		
		playerObject.put("cardsCount", player.getCardCount());
		
		JSONArray minionArray=new JSONArray();
		
		for(Minion minion: player.getMinions())
		{
			JSONObject minionObject=Helper.minion2JSO(minion);
						
			minionArray.add(minionObject);
		}
		
		playerObject.put("minions", minionArray);
		
				
		JSONObject skillObject=new JSONObject();
		skillObject.put("name", player.getHero().getSkillName());
		skillObject.put("remark", player.getHero().getSkillRemark());
		skillObject.put("cost", player.getHero().getCost());
		
				
		if( player.getHeroGameCard().isNeedTarget())
		{
			skillObject.put("target", true);
			
			JSONArray targetsArray=new JSONArray();
			if(player.getHeroGameCard().getTargets()!=null)
				for(String id: player.getHeroGameCard().getTargets().getSet())
				{
					targetsArray.add(id);
				}
			
			skillObject.put("targets", targetsArray);
		}
		else
		{
			skillObject.put("target", false);
		}
		
		playerObject.put("skill", skillObject);
		
		JSONArray targetsArray=new JSONArray();
		if(player.getHeroMinion().getTargets()!=null)
			for(String id: player.getHeroMinion().getTargets().getSet())
			{
				targetsArray.add(id);
			}
		
		playerObject.put("targets", targetsArray);
		
		JSONObject weaponObject=weapon2JSO(player.getWeapon());
		playerObject.put("weapon", weaponObject);
		
		playerObject.put("innerstate", map2JSO(player.getHeroMinion().getInnerState()));
		
		return playerObject;
	}
	
//	public static  String makeId(String cid)
//	{
//		int uIndex=cid.indexOf("_");
//		return cid.substring(uIndex+1);
//	}
	
	public static Value makeValue(String tid)
	{
		Value ret=new Value();
		ret.setType("set");
		ret.setSet(new LinkedList<String>());
		ret.getSet().add(tid);
		return ret;
	}
	
	public static String makeString(Value value)
	{
		if(value.getSet().size()==0)
			return "";
		else
			return value.getSet().get(0);
	}
	
	public static Map<String, String> parseState(String state)
	{
		Map<String, String> ret=new HashMap<String,String>();
		
		if(state.equals(""))
			return ret;
		
		String[] parts=state.split(",");
		for(int i=0;i<parts.length;i++)
		{
			int colonIndex=parts[i].indexOf(":");
			String key=parts[i].substring(0,colonIndex);
			String value=parts[i].substring(colonIndex+1);
			
			ret.put(key,value);
		}
		
		return ret;
	}
	
//	public static Map<String, String> parseMinionState(Minion minion)
//	{
//		Map<String, String> ret=new MinionMap<String,String>(minion);
//		
//		if(minion.getState().equals(""))
//			return ret;
//		
//		String[] parts=minion.getState().split(",");
//		for(int i=0;i<parts.length;i++)
//		{
//			int colonIndex=parts[i].indexOf(":");
//			String key=parts[i].substring(0,colonIndex);
//			String value=parts[i].substring(colonIndex+1);
//			
//			ret.put(key,value);
//		}
//		
//		return ret;
//	}
	
	public static String makeState(Map<String,String> stateMap)
	{
		String ret="";
		
		for(String key: stateMap.keySet())
		{
			ret+=key+":"+stateMap.get(key)+",";
		}
		
		if(!ret.equals(""))
			ret=ret.substring(0, ret.length()-1);
		
		return ret;
	}
	
}


















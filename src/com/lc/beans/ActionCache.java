package com.lc.beans;

import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lc.dao.GameDao;
import utils.SpringFactory;
import com.lc.Action;

import com.lc.Game;
import com.lc.GameCard;
import com.lc.HeroMinion;
import com.lc.Minion;
import com.lc.Weapon;

public class ActionCache {
	
	private Map<String, List<Action>> actionMap=new HashMap<String, List<Action>>();
	private GameDao gameDao=(GameDao)SpringFactory.getFactory().getBean("gameDao");
	
	public synchronized List<Action> getActions(String gid, String uid)
	{
		List<Action> list= actionMap.get(gid+"_"+uid);
		
		if(list!=null)
		{
			if(list.size()>0)
			{
				List<Action> ret=new LinkedList<Action>(list);
				list.clear();
				return ret;
			}
			else
				return list;
		}
		
		else
			return null;
	}
	
	public synchronized void updateCache(Action action)
	{
				
		String key=action.getGid()+"_"+action.getPid();
		
		
		if(actionMap.get(key)==null)
		{
			List<Action> list=new LinkedList<Action>();
			list.add(action);
			
			actionMap.put(key, list);
		}
		else
		{
			List<Action> list=actionMap.get(key);
			list.add(action);
		}
		
	}
	
	public void changeTurn(Game oldGame, Game newGame)
	{
				
		Action action1=new Action(newGame.getId(), newGame.getUid1(), "changeturn", newGame.getTurnJSON().toString());
		Action action2=new Action(newGame.getId(), newGame.getUid2(), "changeturn", newGame.getTurnJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
	}
	
	public void changePower1(Game oldGame, Game newGame)
	{
				
		Action action1=new Action(newGame.getId(), newGame.getUid1(), "changepower", newGame.getPower1JSON().toString());
		Action action2=new Action(newGame.getId(), newGame.getUid2(), "changepower", newGame.getPower1JSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void changePower2(Game oldGame, Game newGame)
	{
				
		Action action1=new Action(newGame.getId(), newGame.getUid1(), "changepower", newGame.getPower2JSON().toString());
		Action action2=new Action(newGame.getId(), newGame.getUid2(), "changepower", newGame.getPower2JSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionAttack(Minion oldMinion, Minion newMinion)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
				
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionattack", newMinion.getAttackJSON().toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionattack", newMinion.getAttackJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
	}
	
	public void minionBlood(Minion oldMinion, Minion newMinion)
	{
				
		Game game=gameDao.getGameById(newMinion.getGid());
		
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionblood", newMinion.getBloodJSON().toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionblood", newMinion.getBloodJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionMaxBlood(Minion oldMinion, Minion newMinion)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
				
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionmaxblood", newMinion.getMaxBloodJSON().toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionmaxblood", newMinion.getMaxBloodJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionIndex(Minion oldMinion, Minion newMinion)
	{
				
		Game game=gameDao.getGameById(newMinion.getGid());
				
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionindex", newMinion.getIndexJSON().toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionindex", newMinion.getIndexJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionInnerStateAdd(Minion oldMinion, Minion newMinion, String stateName)
	{
		
		Game game=gameDao.getGameById(newMinion.getGid());
		
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minioninnerstateadd", newMinion.getInnerStateAddJSON(stateName).toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minioninnerstateadd", newMinion.getInnerStateAddJSON(stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionInnerStateLost(Minion oldMinion, Minion newMinion, String stateName)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
				
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minioninnerstatelost", newMinion.getInnerStateLostJSON(stateName).toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minioninnerstatelost", newMinion.getInnerStateLostJSON(stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionOuterStateAdd(Minion oldMinion, Minion newMinion, String sid, String stateName)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
				
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionouterstateadd", newMinion.getOuterStateAddJSON(sid, stateName).toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionouterstateadd", newMinion.getOuterStateAddJSON(sid, stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionOuterStateLost(Minion oldMinion, Minion newMinion, String sid)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
		
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionouterstatelost", newMinion.getOuterStateLostJSON(sid).toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionouterstatelost", newMinion.getOuterStateLostJSON(sid).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionTargets(Minion oldMinion, Minion newMinion)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
		
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "miniontargets", newMinion.getTargetsJSON().toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "miniontargets", newMinion.getTargetsJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionBorn( Minion newMinion)
	{
		Game game=gameDao.getGameById(newMinion.getGid());
					
		Action action1=new Action(newMinion.getGid(), game.getUid1(), "minionborn", newMinion.getBornJSON().toString());
		Action action2=new Action(newMinion.getGid(), game.getUid2(), "minionborn", newMinion.getBornJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void minionLost( Minion oldMinion)
	{
		Game game=gameDao.getGameById(oldMinion.getGid());
				
		Action action1=new Action(oldMinion.getGid(), game.getUid1(), "minionlost", oldMinion.getLostJSON().toString());
		Action action2=new Action(oldMinion.getGid(), game.getUid2(), "minionlost", oldMinion.getLostJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionAttack(HeroMinion oldHeroMinion, HeroMinion newHeroMinion)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
		
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominionattack", newHeroMinion.getAttackJSON().toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominionattack", newHeroMinion.getAttackJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionBlood(HeroMinion oldHeroMinion, HeroMinion newHeroMinion)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
				
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominionblood", newHeroMinion.getBloodJSON().toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominionblood", newHeroMinion.getBloodJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionDie(HeroMinion oldHeroMinion, HeroMinion newHeroMinion)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
				
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominiondie", newHeroMinion.getDieJSON().toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominiondie", newHeroMinion.getDieJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void win(HeroMinion oldHeroMinion, HeroMinion newHeroMinion)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
		
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "win", newHeroMinion.getWinJSON().toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "win", newHeroMinion.getWinJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionGuard(HeroMinion oldHeroMinion, HeroMinion newHeroMinion)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
				
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominionguard", newHeroMinion.getGuardJSON().toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominionguard", newHeroMinion.getGuardJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionInnerStateAdd(HeroMinion oldHeroMinion, HeroMinion newHeroMinion, String stateName)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
		
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominioninnerstateadd", newHeroMinion.getInnerStateAddJSON(stateName).toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominioninnerstateadd", newHeroMinion.getInnerStateAddJSON(stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionInnerStateLost(HeroMinion oldHeroMinion, HeroMinion newHeroMinion, String stateName)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
		
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominioninnerstatelost", newHeroMinion.getInnerStateLostJSON(stateName).toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominioninnerstatelost", newHeroMinion.getInnerStateLostJSON(stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void heroMinionTargets(HeroMinion oldHeroMinion, HeroMinion newHeroMinion)
	{
		Game game=gameDao.getGameById(newHeroMinion.getGid());
		
		Action action1=new Action(newHeroMinion.getGid(), game.getUid1(), "herominiontargets", newHeroMinion.getTargetsJSON().toString());
		Action action2=new Action(newHeroMinion.getGid(), game.getUid2(), "herominiontargets", newHeroMinion.getTargetsJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void gameCardOuterStateAdd(GameCard oldGameCard, GameCard newGameCard, String sid, String stateName)
	{
		Game game=gameDao.getGameById(newGameCard.getGid());
		
		Action action1=new Action(newGameCard.getGid(), game.getUid1(), "gamecardouterstateadd", newGameCard.getOuterStateAddJSON(sid, stateName).toString());
		Action action2=new Action(newGameCard.getGid(), game.getUid2(), "gamecardouterstateadd", newGameCard.getOuterStateAddJSON(sid, stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void gameCardOuterStateLost(GameCard oldGameCard, GameCard newGameCard, String sid)
	{
		Game game=gameDao.getGameById(newGameCard.getGid());
		
		Action action1=new Action(newGameCard.getGid(), game.getUid1(), "gamecardouterstatelost", newGameCard.getOuterStateLostJSON(sid).toString());
		Action action2=new Action(newGameCard.getGid(), game.getUid2(), "gamecardouterstatelost", newGameCard.getOuterStateLostJSON(sid).toString());
		
		updateCache(action1);
		updateCache(action2);
	}
	
	public void gameCardTargets(GameCard oldGameCard, GameCard newGameCard)
	{
		Game game=gameDao.getGameById(newGameCard.getGid());
				
		Action action1=new Action(newGameCard.getGid(), game.getUid1(), "gamecardtargets", newGameCard.getTargetsJSON().toString());
		Action action2=new Action(newGameCard.getGid(), game.getUid2(), "gamecardtargets", newGameCard.getTargetsJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void gameCardNew(GameCard newGameCard)
	{
		Game game=gameDao.getGameById(newGameCard.getGid());
				
		Action action1=new Action(newGameCard.getGid(), game.getUid1(), "gamecardnew", newGameCard.getNewJSON(game.getUid1()).toString());
		Action action2=new Action(newGameCard.getGid(), game.getUid2(), "gamecardnew", newGameCard.getNewJSON(game.getUid2()).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void gameCardLost(GameCard oldGameCard)
	{
		Game game=gameDao.getGameById(oldGameCard.getGid());
		
		Action action1=new Action(oldGameCard.getGid(), game.getUid1(), "gamecardlost", oldGameCard.getLostJSON().toString());
		Action action2=new Action(oldGameCard.getGid(), game.getUid2(), "gamecardlost", oldGameCard.getLostJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void gameCardUsable(GameCard oldGameCard, GameCard newGameCard)
	{
		Game game=gameDao.getGameById(newGameCard.getGid());
				
		Action action1=new Action(newGameCard.getGid(), game.getUid1(), "gamecardusable", newGameCard.getUsableJSON().toString());
		Action action2=new Action(newGameCard.getGid(), game.getUid2(), "gamecardusable", newGameCard.getUsableJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void weaponAttack(Weapon oldWeapon, Weapon newWeapon)
	{
		Game game=gameDao.getGameById(newWeapon.getGid());
		
		Action action1=new Action(newWeapon.getGid(), game.getUid1(), "weaponattack", newWeapon.getAttackJSON().toString());
		Action action2=new Action(newWeapon.getGid(), game.getUid2(), "weaponattack", newWeapon.getAttackJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void weaponBlood(Weapon oldWeapon, Weapon newWeapon)
	{
		Game game=gameDao.getGameById(newWeapon.getGid());
				
		Action action1=new Action(newWeapon.getGid(), game.getUid1(), "weaponblood", newWeapon.getBloodJSON().toString());
		Action action2=new Action(newWeapon.getGid(), game.getUid2(), "weaponblood", newWeapon.getBloodJSON().toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void weaponInnerStateAdd(Weapon oldWeapon, Weapon newWeapon, String stateName)
	{
		Game game=gameDao.getGameById(newWeapon.getGid());
		
		Action action1=new Action(newWeapon.getGid(), game.getUid1(), "weaponinnerstateadd", newWeapon.getInnerStateAddJSON(stateName).toString());
		Action action2=new Action(newWeapon.getGid(), game.getUid2(), "weaponinnerstateadd", newWeapon.getInnerStateAddJSON(stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void weaponInnerStateLost(Weapon oldWeapon, Weapon newWeapon, String stateName)
	{
		Game game=gameDao.getGameById(newWeapon.getGid());
		
		Action action1=new Action(newWeapon.getGid(), game.getUid1(), "weaponinnerstatelost", newWeapon.getInnerStateLostJSON(stateName).toString());
		Action action2=new Action(newWeapon.getGid(), game.getUid2(), "weaponinnerstatelost", newWeapon.getInnerStateLostJSON(stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void weaponOuterStateAdd(Weapon oldWeapon, Weapon newWeapon, String sid, String stateName)
	{
		Game game=gameDao.getGameById(newWeapon.getGid());
		
		Action action1=new Action(newWeapon.getGid(), game.getUid1(), "weaponouterstateadd", newWeapon.getOuterStateAddJSON(sid,stateName).toString());
		Action action2=new Action(newWeapon.getGid(), game.getUid2(), "weaponouterstateadd", newWeapon.getOuterStateAddJSON(sid,stateName).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
	public void weaponOuterStateLost(Weapon oldWeapon, Weapon newWeapon, String sid)
	{
		Game game=gameDao.getGameById(newWeapon.getGid());
		
		Action action1=new Action(newWeapon.getGid(), game.getUid1(), "weaponouterstatelost", newWeapon.getOuterStateLostJSON(sid).toString());
		Action action2=new Action(newWeapon.getGid(), game.getUid2(), "weaponouterstatelost", newWeapon.getOuterStateLostJSON(sid).toString());
		
		updateCache(action1);
		updateCache(action2);
		
	}
	
}



















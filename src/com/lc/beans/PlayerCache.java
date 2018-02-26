package com.lc.beans;

import java.util.HashMap;
import java.util.Map;

import com.lc.Game;
import com.lc.dao.GameDao;
import com.lc.playing.Player;

public class PlayerCache {

	private Map<String, Player> playerMap=new HashMap<String, Player>();
	
	
	private GameDao gameDao;
		
	public void setGameDao(GameDao gameDao) {
		this.gameDao = gameDao;
	}

	public Player getPlayer(String userName)
	{
		Player player=playerMap.get(userName);
		
		if(player==null)
		{
			// init one player with the userName
			Game game=gameDao.getGame(userName);
			
			player=new Player(game, userName);
			
			String enemyName;
			if(userName.equals(game.getUserName1()))
				enemyName=game.getUserName2();
			else
				enemyName=game.getUserName1();
			
			Player enemy=new Player(game, enemyName);
			
			player.setEnemy(enemy);
			enemy.setEnemy(player);
			
			player.initialize();
			enemy.initialize();
			
			playerMap.put(userName, player);
			playerMap.put(enemyName, enemy);
		}
		
		return player;
	}
	
	
	
	public void save(String userName, Player player)
	{
		playerMap.put(userName, player);
		
	}
	
	public void clear()
	{
		playerMap.clear();
	}
}





















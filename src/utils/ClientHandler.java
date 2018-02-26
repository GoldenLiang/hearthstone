package utils;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.lc.Action;

import com.lc.Card;

import com.lc.Game;


import com.lc.UserBean;

import com.lc.beans.ActionCache;
import com.lc.beans.PlayerCache;
import com.lc.dao.CardDao;
import com.lc.dao.GameDao;
import com.lc.playing.Player;

public class ClientHandler {
	
	static CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
	static GameDao gameDao=(GameDao)SpringFactory.getFactory().getBean("gameDao");
	static ActionCache actionCache=(ActionCache)SpringFactory.getFactory().getBean("actionCache");
	static PlayerCache playerCache=(PlayerCache)SpringFactory.getFactory().getBean("playerCache");
	
	public static void field(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException 
	{
		
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("0");
		}
		else
		{
		
			// create player and enemy
			
//			Player player=new Player(game, user.getUserName());
//			
			String enemyName;
			if(user.getUserName().equals(game.getUserName1()))
				enemyName=game.getUserName2();
			else
				enemyName=game.getUserName1();
//			
//			Player enemy=new Player(game, enemyName);
//			
//			player.setEnemy(enemy);
//			enemy.setEnemy(player);
//			
//			player.initialize();
//			enemy.initialize();
			
			Player player=playerCache.getPlayer(user.getUserName());
			Player enemy=playerCache.getPlayer(enemyName);
			
			// save player and enemy into session
			

			try
			{
			
				JSONObject jso=new JSONObject();
				
				
				JSONObject dObject=new JSONObject();
				dObject.put("winMoney", game.getWinMoney());
				
				if( (game.getTurn()%2==0 && game.getUserName1().equals(user.getUserName())) || 
						(game.getTurn()%2==1 && game.getUserName2().equals(user.getUserName())) )
					dObject.put("turn", true);
				else
					dObject.put("turn", false);
				
				JSONObject playerObject=Helper.player2JSO(player, true);
				JSONObject enemyObject=Helper.player2JSO(enemy, false);
								
				dObject.put("player", playerObject);
				dObject.put("enemy", enemyObject);
				
				dObject.put("trashCards", new JSONArray());
				
				jso.put("err", null);
				jso.put("d", dObject);
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(jso.toString());
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("0");
			}
			
		}
	}
	
	public static void getCard(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException
	{
		String cardName=parts[1];
		
		
		Card card=cardDao.getCard(cardName);
		
		if(card!=null)
		{
			JSONObject jso=new JSONObject();
			
			jso.put("err", null);
			
			JSONObject cardObject=Helper.card2JSO(card);
							
			jso.put("d", cardObject);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "获取卡牌失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
	}
	
	public static void connect(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException
	{
		// wait until there are something to return back
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "获取游戏失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			String uid=null;
			if(user.getUserName().equals(game.getUserName1()))
				uid=game.getUid1();
			else
				uid=game.getUid2();
			
			//List<Action> actions= ActionDAO.getActions(game.getId(), uid);
			List<Action> actions= actionCache.getActions(game.getId(), uid);
			
			
			int count=0;
			while((actions==null || actions.size()==0) && count<10)
			{
				try {
					Thread.sleep(2000);
					count++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				actions=actionCache.getActions(game.getId(), uid);
			}
			
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			
			
			if(actions==null || actions.size()==0)
			{
				jso.put("d", null);
			}
			else
			{
				JSONArray actionArray=new JSONArray();
				
				for(Action action: actions)
				{
					JSONObject actionObject=Helper.action2JSO(action);
					
					actionArray.add(actionObject);
				}
				
				
				jso.put("d", actionArray);
			}
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
			
		}
		
	}
	
	public static void useCard(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException
	{
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("0");
		}
		else
		{
			
			// create player and enemy
//			Player player=new Player(game, user.getUserName());
//			
			String enemyName;
			if(user.getUserName().equals(game.getUserName1()))
				enemyName=game.getUserName2();
			else
				enemyName=game.getUserName1();
//			
//			Player enemy=new Player(game, enemyName);
//			
//			player.setEnemy(enemy);
//			enemy.setEnemy(player);
//			
//			player.initialize();
//			enemy.initialize();
			
			Player player=playerCache.getPlayer(user.getUserName());
			Player enemy=playerCache.getPlayer(enemyName);
			
			// extract params
			
			String cid=parts[1];
			String tid=parts[2];
			int index=Integer.parseInt(parts[3]);
			
			player.useGameCard(cid,tid,index);
			
			player.save();
			enemy.save();
			
			playerCache.save(user.getUserName(),player);
			playerCache.save(enemyName,enemy);
			
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			jso.put("d", true);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
			
		}
				
	}
	
	public static void next(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException
	{
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("0");
		}
		else
		{
						
			// create player and enemy
//			Player player=new Player(game, user.getUserName());
//			
			String enemyName;
			if(user.getUserName().equals(game.getUserName1()))
				enemyName=game.getUserName2();
			else
				enemyName=game.getUserName1();
//			
//			Player enemy=new Player(game, enemyName);
//			
//			player.setEnemy(enemy);
//			enemy.setEnemy(player);
//			
//			player.initialize();
//			enemy.initialize();
			
			Player player=playerCache.getPlayer(user.getUserName());
			Player enemy=playerCache.getPlayer(enemyName);
			
			// extract params
			
			player.endRound();
						
			player.save();
			enemy.save();
			
			playerCache.save(user.getUserName(),player);
			playerCache.save(enemyName,enemy);
			
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			jso.put("d", true);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
				
	}
	
	public static void attack(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException
	{
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("0");
		}
		else
		{
						
			// create player and enemy
//			Player player=new Player(game, user.getUserName());
//			
			String enemyName;
			if(user.getUserName().equals(game.getUserName1()))
				enemyName=game.getUserName2();
			else
				enemyName=game.getUserName1();
//			
//			Player enemy=new Player(game, enemyName);
//			
//			player.setEnemy(enemy);
//			enemy.setEnemy(player);
//			
//			player.initialize();
//			enemy.initialize();
			
			Player player=playerCache.getPlayer(user.getUserName());
			Player enemy=playerCache.getPlayer(enemyName);
			
			// extract params
			
			String cid=parts[1];
			String tid=parts[2];
						
			player.attack(cid,tid);
			
			player.save();
			enemy.save();
			
			playerCache.save(user.getUserName(),player);
			playerCache.save(enemyName,enemy);
			
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			jso.put("d", true);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
			
		}
				
	}
	
	
	public static void useSkill(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException
	{
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("0");
		}
		else
		{
			
			// create player and enemy
//			Player player=new Player(game, user.getUserName());
//			
			String enemyName;
			if(user.getUserName().equals(game.getUserName1()))
				enemyName=game.getUserName2();
			else
				enemyName=game.getUserName1();
//			
//			Player enemy=new Player(game, enemyName);
//			
//			player.setEnemy(enemy);
//			enemy.setEnemy(player);
//			
//			player.initialize();
//			enemy.initialize();
			
			Player player=playerCache.getPlayer(user.getUserName());
			Player enemy=playerCache.getPlayer(enemyName);
			
			// extract params
			
			
			String tid=parts[1];
						
			player.userHeroSkill(tid);
			
			player.save();
			enemy.save();
			
			playerCache.save(user.getUserName(),player);
			playerCache.save(enemyName,enemy);
			
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			jso.put("d", true);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
			
		}
				
	}
	
}


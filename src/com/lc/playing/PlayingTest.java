package com.lc.playing;

import java.util.Scanner;

import com.lc.Game;
import com.lc.dao.GameDao;

public class PlayingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameDao gameDao=new GameDao();
		Game game=gameDao.getGame("com/lc");
		
		// create player and enemy
		Player player=new Player(game, "com/lc");
		
		String enemyName;
		if("com/lc".equals(game.getUserName1()))
			enemyName=game.getUserName2();
		else
			enemyName=game.getUserName1();
		
		Player enemy=new Player(game, enemyName);
		
		player.setEnemy(enemy);
		enemy.setEnemy(player);
		
		player.initialize();
		enemy.initialize();
		
		
		
		player.print();
		enemy.print();
		
		
		Scanner cin=new Scanner(System.in);
		
		while(true)
		{
			int turn=game.getTurn()%2;
			
			if(turn==0)
			{
				System.out.println(game.getUserName1()+"'s turn, power:"+game.getPower1()+", maxpower:"+game.getMaxpower1());
			}
			else
			{
				System.out.println(game.getUserName2()+"'s turn, power:"+game.getPower2()+", maxpower:"+game.getMaxpower2());
			}
			
			// receiving the command to test the game playing
			String command=cin.nextLine();
			
			String[] parts=command.split(",");
			
			if(parts[0].equals("usegamecard"))
			{
								
				String cid=parts[1];
				String tid=parts[2];
				int index=Integer.parseInt(parts[3]);
				
				if(turn==0)
					player.useGameCard(cid, tid, index);
				else
					enemy.useGameCard(cid, tid, index);
							
			}
			else if(parts[0].startsWith("takecard"))
			{
				int count=Integer.parseInt(parts[1]);
				
				if(turn==0)
					player.takeCard(count);
				else
					enemy.takeCard(count);
			}
			else if(parts[0].startsWith("useheroskill"))
			{
				String tid=parts[1];
				if(turn==0)
					player.userHeroSkill(tid);
				else
					enemy.userHeroSkill(tid);
			}
			else if(parts[0].startsWith("next"))
			{
				if(turn==0)
				{
					player.endRound();
				}
				else
				{
					enemy.endRound();
				}
								
			}
			else if(parts[0].startsWith("attack"))
			{
				String cid=parts[1];
				String tid=parts[2];
				
				if(turn==0)
					player.attack(cid, tid);
				else
					enemy.attack(cid, tid);			
			}
			
			
			player.print();
			System.out.println();
			enemy.print();
			
			player.save();
			enemy.save();
		}
		
		
		
	}

}













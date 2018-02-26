package com.lc;

import net.sf.json.JSONObject;
import com.lc.playing.GameAction;

public class Game {
	
	private String id;
	private int winMoney;
	private int turn;
	private int round;
	private String trashCards;
	
	private String userName1;
	private String uid1;
	private String job1;
	
	private int power1;
	private int maxpower1;
	
	
	private String userName2;
	private String uid2;
	private String job2;
	
	private int power2;
	private int maxpower2;
	
	
	private int end1;
	private int end2;
	
	public Game()
	{
		
	}
	
	public Game(Game game)
	{
		this.setId(game.id);
		this.setWinMoney(game.winMoney);
		this.setTurn(game.turn);
		this.setRound(game.round);
		this.setTrashCards(game.trashCards);
		this.setUserName1(game.userName1);
		this.setUid1(game.uid1);
		this.setJob1(game.job1);
		
		this.setPower1(game.power1);
		this.setMaxpower1(game.maxpower1);
		
		this.setUserName2(game.userName2);
		this.setUid2(game.uid2);
		this.setJob2(game.job2);
		
		this.setPower2(game.power2);
		this.setMaxpower2(game.maxpower2);
	}
	
	
	
	

	public int getEnd1() {
		return end1;
	}

	public void setEnd1(int end1) {
		this.end1 = end1;
	}

	public int getEnd2() {
		return end2;
	}

	public void setEnd2(int end2) {
		this.end2 = end2;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(int winMoney) {
		this.winMoney = winMoney;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public String getTrashCards() {
		return trashCards;
	}
	public void setTrashCards(String trashCards) {
		this.trashCards = trashCards;
	}
	public String getUserName1() {
		return userName1;
	}
	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}
	public String getUid1() {
		return uid1;
	}
	public void setUid1(String uid1) {
		this.uid1 = uid1;
	}
	public String getJob1() {
		return job1;
	}
	public void setJob1(String job1) {
		this.job1 = job1;
	}
	
	public int getPower1() {
		return power1;
	}
	public void setPower1(int power1) {
		this.power1 = power1;
	}
	public int getMaxpower1() {
		return maxpower1;
	}
	public void setMaxpower1(int maxpower1) {
		this.maxpower1 = maxpower1;
	}
	
	public String getUserName2() {
		return userName2;
	}
	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}
	public String getUid2() {
		return uid2;
	}
	public void setUid2(String uid2) {
		this.uid2 = uid2;
	}
	public String getJob2() {
		return job2;
	}
	public void setJob2(String job2) {
		this.job2 = job2;
	}
	
	public int getPower2() {
		return power2;
	}
	public void setPower2(int power2) {
		this.power2 = power2;
	}
	public int getMaxpower2() {
		return maxpower2;
	}
	public void setMaxpower2(int maxpower2) {
		this.maxpower2 = maxpower2;
	}
	
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	
	public int getNextCount()
	{
		return (this.round++)+70;
	}
	
	public void generateDiffActions(Game game)
	{
		// turn change
		if(this.turn!=game.turn)
		{
			GameAction gameAction=new GameAction();
			
			gameAction.setName("changeturn");
			gameAction.setOldGame(game);
			gameAction.setNewGame(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		if(this.power1!=game.power1)
		{
			GameAction gameAction=new GameAction();
			
			gameAction.setName("powerchange1");
			gameAction.setOldGame(game);
			gameAction.setNewGame(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
		
		if(this.power2!=game.power2)
		{
			GameAction gameAction=new GameAction();
			
			gameAction.setName("powerchange2");
			gameAction.setOldGame(game);
			gameAction.setNewGame(this);
			
			System.out.println(gameAction);
			gameAction.writeDatabase();
		}
	}
	
	public JSONObject getTurnJSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECHANGTURN);
		
		if(this.turn%2==0)
			jso.put("pid", this.uid1);
		else
			jso.put("pid", this.uid2);
		
		return jso;
	}
	
	public JSONObject getPower1JSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECHANGEPOWER);
		
		jso.put("pid", this.uid1);
		jso.put("power", this.power1);
		jso.put("maxpower", this.maxpower1);
		
		
		return jso;
	
	}
	
	public JSONObject getPower2JSON()
	{
		JSONObject jso=new JSONObject();
		jso.put("type", Action.GAMECHANGEPOWER);
		
		jso.put("pid", this.uid2);
		jso.put("power", this.power2);
		jso.put("maxpower", this.maxpower2);
		
		
		return jso;
	}
	
}













package com.lc.playing;


import utils.SpringFactory;
import com.lc.Game;
import com.lc.GameCard;
import com.lc.HeroMinion;
import com.lc.Minion;
import com.lc.Weapon;
import com.lc.beans.ActionCache;

public class GameAction {

	private String name;
	private Game oldGame;
	private Game newGame;
	
	private Minion oldMinion;
	private Minion newMinion;
	
	private HeroMinion oldHeroMinion;
	private HeroMinion newHeroMinion;
	
	private Weapon oldWeapon;
	private Weapon newWeapon;
	
	private GameCard oldGameCard;
	private GameCard newGameCard;
	
	private String sid;
	private String stateName;
	
	private ActionCache actionCache=(ActionCache)SpringFactory.getFactory().getBean("actionCache");
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Game getOldGame() {
		return oldGame;
	}
	public void setOldGame(Game oldGame) {
		this.oldGame = oldGame;
	}
	public Game getNewGame() {
		return newGame;
	}
	public void setNewGame(Game newGame) {
		this.newGame = newGame;
	}
	public Minion getOldMinion() {
		return oldMinion;
	}
	public void setOldMinion(Minion oldMinion) {
		this.oldMinion = oldMinion;
	}
	public Minion getNewMinion() {
		return newMinion;
	}
	public void setNewMinion(Minion newMinion) {
		this.newMinion = newMinion;
	}
	public HeroMinion getOldHeroMinion() {
		return oldHeroMinion;
	}
	public void setOldHeroMinion(HeroMinion oldHeroMinion) {
		this.oldHeroMinion = oldHeroMinion;
	}
	public HeroMinion getNewHeroMinion() {
		return newHeroMinion;
	}
	public void setNewHeroMinion(HeroMinion newHeroMinion) {
		this.newHeroMinion = newHeroMinion;
	}
	public Weapon getOldWeapon() {
		return oldWeapon;
	}
	public void setOldWeapon(Weapon oldWeapon) {
		this.oldWeapon = oldWeapon;
	}
	public Weapon getNewWeapon() {
		return newWeapon;
	}
	public void setNewWeapon(Weapon newWeapon) {
		this.newWeapon = newWeapon;
	}
	public GameCard getOldGameCard() {
		return oldGameCard;
	}
	public void setOldGameCard(GameCard oldGameCard) {
		this.oldGameCard = oldGameCard;
	}
	public GameCard getNewGameCard() {
		return newGameCard;
	}
	public void setNewGameCard(GameCard newGameCard) {
		this.newGameCard = newGameCard;
	}
	
	
	public String toString()
	{
		return this.name;
	}
	
	public void writeDatabase()
	{
		// action of game
		
		if(this.name.equals("changeturn"))
		{
			actionCache.changeTurn(this.oldGame, this.newGame);
		}
		else if(this.name.equals("powerchange1"))
		{
			actionCache.changePower1(this.oldGame, this.newGame);
		}
		else if(this.name.equals("powerchange2"))
		{
			actionCache.changePower2(this.oldGame, this.newGame);
		}
		
		
		// action of minion
		else if(this.name.equals("minionattack"))
		{
			actionCache.minionAttack(this.oldMinion, this.newMinion);
		}
		else if(this.name.equals("minionblood"))
		{
			actionCache.minionBlood(this.oldMinion, this.newMinion);
		}
		else if(this.name.equals("minionmaxblood"))
		{
			actionCache.minionMaxBlood(this.oldMinion, this.newMinion);
		}
		else if(this.name.equals("minionindex"))
		{
			actionCache.minionIndex(this.oldMinion, this.newMinion);
		}
		else if(this.name.equals("minioninnerstateadd"))
		{
			actionCache.minionInnerStateAdd(this.oldMinion, this.newMinion, this.stateName);
		}
		else if(this.name.equals("minioninnerstatelost"))
		{
			actionCache.minionInnerStateLost(this.oldMinion, this.newMinion, this.stateName);
		}
		else if(this.name.equals("minionouterstateadd"))
		{
			actionCache.minionOuterStateAdd(this.oldMinion, this.newMinion, this.sid, this.stateName);
		}
		else if(this.name.equals("minionouterstatelost"))
		{
			actionCache.minionOuterStateLost(this.oldMinion, this.newMinion, this.sid);
		}
		else if(this.name.equals("miniontargets"))
		{
			actionCache.minionTargets(this.oldMinion, this.newMinion);
		}
		else if(this.name.equals("minionborn"))
		{
			actionCache.minionBorn(this.newMinion);
		}
		else if(this.name.equals("minionlost"))
		{
			actionCache.minionLost(this.oldMinion);
		}
		
		// action of hero minion
		else if(this.name.equals("herominionattack"))
		{
			actionCache.heroMinionAttack(this.oldHeroMinion, this.newHeroMinion);
		}
		else if(this.name.equals("herominionblood"))
		{
			actionCache.heroMinionBlood(this.oldHeroMinion, this.newHeroMinion);
		}
		else if(this.name.equals("herominiondie"))
		{
			actionCache.heroMinionDie(this.oldHeroMinion, this.newHeroMinion);
		}
		else if(this.name.equals("win"))
		{
			actionCache.win(this.oldHeroMinion, this.newHeroMinion);
		}
		else if(this.name.equals("herominionguard"))
		{
			actionCache.heroMinionGuard(this.oldHeroMinion, this.newHeroMinion);
		}
		else if(this.name.equals("herominioninnerstateadd"))
		{
			actionCache.heroMinionInnerStateAdd(this.oldHeroMinion, this.newHeroMinion, this.stateName);
		}
		else if(this.name.equals("herominioninnerstatelost"))
		{
			actionCache.heroMinionInnerStateLost(this.oldHeroMinion, this.newHeroMinion, this.stateName);
		}
		else if(this.name.equals("herominiontargets"))
		{
			actionCache.heroMinionTargets(this.oldHeroMinion, this.newHeroMinion);
		}
		
		// action of game card
		else if(this.name.equals("gamecardouterstateadd"))
		{
			actionCache.gameCardOuterStateAdd(this.oldGameCard, this.newGameCard, this.sid, this.stateName);
		}
		else if(this.name.equals("gamecardouterstatelost"))
		{
			actionCache.gameCardOuterStateLost(this.oldGameCard, this.newGameCard, this.sid);
		}
		else if(this.name.equals("gamecardtargets"))
		{
			actionCache.gameCardTargets(this.oldGameCard, this.newGameCard);
		}
		else if(this.name.equals("gamecardnew"))
		{
			actionCache.gameCardNew(this.newGameCard);
		}
		else if(this.name.equals("gamecardlost"))
		{
			actionCache.gameCardLost(this.oldGameCard);
		}
		else if(this.name.equals("gamecardusable"))
		{
			actionCache.gameCardUsable(this.oldGameCard, this.newGameCard);
		}
		
		// action of weapon
		else if(this.name.equals("weaponattack"))
		{
			actionCache.weaponAttack(this.oldWeapon, this.newWeapon);
		}
		else if(this.name.equals("weaponblood"))
		{
			actionCache.weaponBlood(this.oldWeapon, this.newWeapon);
		}
		else if(this.name.equals("weaponinnerstateadd"))
		{
			actionCache.weaponInnerStateAdd(this.oldWeapon, this.newWeapon, this.stateName);
		}
		else if(this.name.equals("weaponinnerstatelost"))
		{
			actionCache.weaponInnerStateLost(this.oldWeapon, this.newWeapon, this.stateName);
		}
		else if(this.name.equals("weaponouterstateadd"))
		{
			actionCache.weaponOuterStateAdd(this.oldWeapon, this.newWeapon, this.sid, this.stateName);
		}
		else if(this.name.equals("weaponouterstatelost"))
		{
			actionCache.weaponOuterStateLost(this.oldWeapon, this.newWeapon, this.sid);
		}
		
		
	}
	
	
}















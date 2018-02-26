package com.lc;

public class Action {
	
	public static final int GAMECHANGTURN=1;
	public static final int GAMECHANGEPOWER=2;
	public static final int MINIONATTACK=3;
	public static final int MINIONBLOOD=4;
	public static final int MINIONMAXBLOOD=5;
	public static final int MINIONINDEX=6;
	public static final int MINIONINNERSTATEADD=7;
	public static final int MINIONINNERSTATELOST=8;
	public static final int MINIONOUTERSTATEADD=9;
	public static final int MINIONOUTERSTATELOST=10;
	public static final int MINIONTARGETS=11;
	public static final int MINIONBORN=12;
	public static final int MINIONLOST=13;
	public static final int HEROMINIONATTACK=14;
	public static final int HEROMINIONBLOOD=15;
	public static final int HEROMINIONDIE=32;
	public static final int HEROMINIONGUARD=31;
	public static final int HEROMINIONINNERSTATEADD=16;
	public static final int HEROMINIONINNERSTATELOST=17;
	public static final int HEROMINIONTARGETS=18;
	public static final int GAMECARDOUTERSTATEADD=19;
	public static final int GAMECARDOUTERSTATELOST=20;
	public static final int GAMECARDTARGETS=21;
	public static final int GAMECARDNEW=22;
	public static final int GAMECARDLOST=23;
	public static final int GAMECARDUSABLE=30;
	public static final int WEAPONATTACK=24;
	public static final int WEAPONBLOOD=25;
	public static final int WEAPONINNERSTATEADD=26;
	public static final int WEAPONINNERSTATELOST=27;
	public static final int WEAPONOUTERSTATEADD=28;
	public static final int WEAPONOUTERSTATELOST=29;
	public static final int WIN=33;
	
	
	
	
	private int id;
	private String gid;
	private String pid;
	private String name;
	private String actionJSON;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getActionJSON() {
		return actionJSON;
	}
	public void setActionJSON(String actionJSON) {
		this.actionJSON = actionJSON;
	}
	
	public Action()
	{
		
	}
	
	public Action(String gid, String pid, String name, String actionJSON)
	{
		this.gid=gid;
		this.pid=pid;
		this.name=name;
		this.actionJSON=actionJSON;
	}
	
		
}

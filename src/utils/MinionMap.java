package utils;

import java.util.HashMap;

import com.lc.Minion;


public class MinionMap<T1,T2> extends HashMap<T1, T2>
{
	private Minion minion;
	
	public MinionMap(Minion minion)
	{
		this.minion=minion;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public T2 put(T1 key, T2 value)
	{
		
		return super.put(key, value);
	}
	
	public T2 remove(Object key)
	{
		
		return super.remove(key);
	}
}

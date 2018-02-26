package com.lc;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static  String clearBlanks(String code)
	{
		return code.replaceAll("\\s+", "");
	}
	
	public static  List<String> extractParams(String paramsStr)
	{
		List<String> ret=new ArrayList<String>();
		
		if(paramsStr.equals(""))
			return ret;
		
		int i=0;
		
		while(i<paramsStr.length())
		{
			int start=i;
			int bracketCount=0;
			while(i<paramsStr.length() && ( paramsStr.charAt(i)!=',' || bracketCount > 0) )
			{
				if(paramsStr.charAt(i)=='(')
					bracketCount++;
				else if(paramsStr.charAt(i)==')')
					bracketCount--;
				
				i++;
										
			}
			
			
			String param=paramsStr.substring(start,i);
			ret.add(param);
			
			i++;
			
		}
		
		
		return ret;
	}
}

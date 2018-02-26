package com.lc.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestSuite {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		FileInputStream fileInputStream = new FileInputStream("e:/papers/lushiworkspace/effectsu8.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
		
		String line;
		
		while((line=reader.readLine())!=null)
		{
			
			
			if(line.equals("") || line.startsWith("//"))
				continue;
			
			System.out.println(line);
			
			int sep=line.indexOf(",");
			
			String name=line.substring(0,sep);
			String effectcode=line.substring(sep+1);
			
			StatementList sl=new StatementList(effectcode);
									
			
			System.out.println(sl);
			
			//break;
		}
		
		reader.close();
		
	}

}

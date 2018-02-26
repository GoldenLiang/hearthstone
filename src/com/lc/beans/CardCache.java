package com.lc.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lc.Card;

import com.lc.dao.CardDao;

public class CardCache {
	
	private CardDao cardDao;
	private Map<String, Card> cardMap;
	
		
	public void setCardDao(CardDao cardDao) {
		this.cardDao = cardDao;
	}

		
	public void init()
	{
		
		List<Card> cards=cardDao.getAllCards();
		
		cardMap=new HashMap<String, Card>();
		for(Card card: cards)
		{
			cardMap.put(card.getName(), card);
			
			System.out.println(card.getName());
		}
	}
	
	public Card getCard(String name)
	{
		
		//System.out.println(name);
		Card card=cardMap.get(name);
		
//		System.out.println(card.getImageid());
//		System.out.println(card.getEffectCode());
//		System.out.println(card.getTargetCode());
		
		return card;
	}
	
	
}

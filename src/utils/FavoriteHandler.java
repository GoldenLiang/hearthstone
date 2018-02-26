package utils;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.lc.Card;

import com.lc.Pack;
import com.lc.PackDAO;
import com.lc.UserBean;
import com.lc.UserDAO;
import com.lc.dao.CardDao;

public class FavoriteHandler {
	
	public static void getCards(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		
		CardDao cardDao=(CardDao)SpringFactory.getFactory().getBean("cardDao");
		List<Card> cardList=cardDao.getCards();
		
		System.out.println(cardList.size());
		
		if(cardList.size()==0)
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "获取卡牌失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			
			JSONArray cardArray=new JSONArray();
			
			for(Card card: cardList)
			{
				JSONObject cardObject=Helper.card2JSO(card);
									
				cardArray.add(cardObject);
				
			}
			
			JSONObject dObject=new JSONObject();
			dObject.put("cards", cardArray);
			
			jso.put("d", dObject);
			
			System.out.println(jso);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		
	}
	
	public static void getPacks(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
	
		List<Pack> packList=PackDAO.getPacks();
		System.out.println("packs size: "+packList.size());
		
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		int dust=UserDAO.getDust(user);
		
		if(dust<0)
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "获取魔尘失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			
			JSONArray packArray=new JSONArray();
			
			for(Pack pack: packList)
			{
				JSONObject packObject=new JSONObject();
				packObject.put("name", pack.getName());
				packObject.put("job", pack.getJob());
				
				JSONArray cardArray=new JSONArray();
				
				if(!pack.getCards().equals(""))
				{
					String[] cards=pack.getCards().split("__");
					
					for(int i=0;i<cards.length;i++)
					{
						cardArray.add(cards[i]);
					}
				}
				
				packObject.put("cards", cardArray);
													
				packArray.add(packObject);
				
			}
			
			JSONObject dObject=new JSONObject();
			dObject.put("packs", packArray);
			
			jso.put("d", dObject);
			jso.put("dust", dust);
			
			System.out.println(jso);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		
		}
		
	}
	
	public static void newPack(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		
		String name=parts[1];
		String job=parts[2];
		
		boolean ret=PackDAO.create(name, job);
		
		if(!ret)
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "创建套牌失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			jso.put("d", true);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
	}
	
	public static void storePack(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		
		String name=parts[1];
		String job=parts[2];
		
		String cards=parts[3];
		
		boolean ret=PackDAO.storePack(name,job,cards);
		
		if(!ret)
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "保存套牌失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			JSONObject jso=new JSONObject();
			jso.put("err", null);
			jso.put("d", true);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
	}
	
	
	
	
}

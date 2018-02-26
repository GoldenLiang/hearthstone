package utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;

import net.sf.json.JSONObject;
import com.lc.Game;

import com.lc.UserBean;
import com.lc.UserDAO;
import com.lc.dao.GameDao;

public class PracticeHandler {
	
	static GameDao gameDao=(GameDao)SpringFactory.getFactory().getBean("gameDao");
	
	public static void getMoney(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		
		int money=UserDAO.getMoney(user);
		
		if(money<0)
		{
			JSONObject jso=new JSONObject();
			jso.put("err", "获取金钱失败");
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}
		else
		{
			JSONObject jso=new JSONObject();
			jso.put("d", money);
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(jso.toString());
		}	
		
	}
	
	public static void begin(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
	
		String selfJob=parts[1];
		String packName=parts[2];
		String enemyJob=parts[3];
		
		
		
		UserBean user= (UserBean)request.getSession().getAttribute("currentSessionUser");
		
		Game game=gameDao.getGame(user.getUserName());
		
		if(game==null)
		{
			try
			{
				gameDao.begin(user.getUserName(), selfJob, packName);
				
				JSONObject jso=new JSONObject();
				jso.put("err", null);
				jso.put("d", true);
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(jso.toString());
			}
			catch(DataAccessException e)
			{
				JSONObject jso=new JSONObject();
				jso.put("err", "开始游戏失败");
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(jso.toString());
			}
			
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

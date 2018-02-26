package utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import com.lc.UserBean;
import com.lc.UserDAO;

public class LoginHandler {
	
	public static void login(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		
		String username=parts[1];
		String password=parts[2];
		
		try {
			UserBean user = new UserBean();
			user.setUserName(username);
			user.setPassword(password);
			user = UserDAO.login(user);
			if (user.isValid()) {
				HttpSession session = request.getSession(true);
				session.setAttribute("currentSessionUser", user);
				
				JSONObject jso=new JSONObject();
				jso.put("d", "登录成功");
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(jso.toString());
				
				//response.sendRedirect("userLogged.jsp");
				// logged-in page
			} else
			{
				//response.sendRedirect("invalidLogin.jsp");
				
				JSONObject jso=new JSONObject();
				jso.put("err", "登录失败");
				
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(jso.toString());
				
			}
			// error page
		} catch (Throwable theException) {
			System.out.println(theException);
		}	
		
	}
	public static void logout(String[] parts, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException {
		
		request.getSession().invalidate();
		
		JSONObject jso=new JSONObject();
		jso.put("d", "登出成功");
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(jso.toString());
		
	}
	
}

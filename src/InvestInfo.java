import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;


public class InvestInfo extends HttpServlet

{
	private static final String content_type="text/html;charset=GBK";
	public void init(ServletConfig config ) throws ServletException
	{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType(content_type);
		PrintWriter out=response.getWriter();
		
		out.println("<html><body>");
		String name=request.getParameter("name");
		out.println(name);
		out.println("</body></html>");
		
		out.flush();
	}
}

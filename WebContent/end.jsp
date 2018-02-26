<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.lc.UserBean"%>

<%@ page import="com.lc.Game"%>
<%@ page import="com.lc.dao.GameDao"%>
<%@ page import="com.lc.beans.PlayerCache"%>
<%@ page import="utils.SpringFactory"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>百度</title>
	<link rel="stylesheet" href="css/base.css" media="all" />
</head>
<body style="height:100%; margin: 0;padding: 0;" scroll="no">

<% 
		UserBean currentUser = (UserBean) (session.getAttribute("currentSessionUser"));
	
		if(currentUser==null)
		{ 
			response.sendRedirect("index.jsp");
		}
		else 
		{
			GameDao gameDao=(GameDao)SpringFactory.getFactory().getBean("gameDao");
			gameDao.endGame(currentUser.getUserName());
			
			PlayerCache playerCache=(PlayerCache)SpringFactory.getFactory().getBean("playerCache");
			playerCache.clear();
			
			response.sendRedirect("index.jsp");
		}
	%>
</body>
</html>
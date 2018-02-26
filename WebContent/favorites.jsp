<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.lc.UserBean"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>山寨炉石</title>
<link rel="stylesheet" href="css/base.css" media="all">
<script src="js/jquery-1.js" type="text/javascript"></script>
<script src="js/core.js" type="text/javascript"></script>
<script src="js/fav.js" type="text/javascript"></script>
</head>
<body>
	<% 
		UserBean currentUser = (UserBean) (session.getAttribute("currentSessionUser"));
	
		if(currentUser==null)
		{
	%>
	<script>location.href="index.jsp";</script>
	<% 
		}
		else
		{
	%>
	
	<div class="page_head">
		<h1>山寨炉石</h1>
		<h6>
			一个卡牌爱好者独立制作的<b>炉石传说</b>山寨网页版
		</h6>
		<div class="in" onclick="logout()">登出</div>
		<div><%=currentUser.getUserName() %></div>
		<div title="金币" class="money" id="head_money">0</div>
		<div title="奥术之尘" class="dust" id="head_dust">0</div>
		<script type="text/javascript">
			function logout() {
				r.cuser.logout(function() {
					location = "index.jsp";
				});
			};
			$(function() {
				server("legend.practice.getMoney", function(d) {
					$("#head_money").text(d);
				});
			})
		</script>
	</div>
	<div class="page_prompt">
		<h1>我的卡牌</h1>
		<a href="input.jsp">Edit</a>
		<a href="index.jsp" class="in">返回</a>
	</div>
	<div class="page_fav" id="favpanel">
		
	</div>
	<script type="text/javascript"> 
        $(function () { 
            var f = new favEngine($("#favpanel")); 
 
        }) 
    </script>
    
    <% 
		}
		
	%>

</body>
</html>
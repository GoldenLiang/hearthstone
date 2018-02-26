<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.lc.UserBean" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
   <title>山寨炉石</title>
<link rel="stylesheet" href="css/base.css" media="all">
<script src="js/jquery-1.js" type="text/javascript"></script>
<script src="js/core.js" type="text/javascript"></script>
</head>
<body>
	
	<% 
		UserBean currentUser = (UserBean) (session.getAttribute("currentSessionUser"));
	
		if(currentUser==null)
		{	
	%>
	
	<div class="page_index">
        <h6>一个卡牌爱好者独立制作的<b>炉石传说</b>山寨网页版</h6>
        <h1>山寨炉石</h1>
        <h5>也许你可以试试这里不一样的味道</h5>
        <div class="login">
            <label>用户名：</label>
            <input class="text" id="txt_mail" > 
            <label>密码：</label>
            <input id="txt_psw" class="text" type="password">
            <div class="btn" onclick="login()">登录</div>
            <a href="reg.html">注册</a>
        </div>
        
    </div>

    <script type="text/javascript"> 
        function login() { 
            var n = $("#txt_mail").val(); 
            var p = $("#txt_psw").val(); 
            if (!r.validate("用户名", n, ["notempty"])) 
                return; 
            if (!r.validate("密码", p, ["notempty"])) 
                return; 
 
            r.cuser.login(n, p, function () { 
                location.reload(); 
            }); 
        }; 
    </script>

	<% 
		}
		else
		{
	%>
	<div class="page_head">
    <h1>山寨炉石</h1>
    <h6>一个卡牌爱好者独立制作的<b>炉石传说</b>山寨网页版</h6>
    <div class="in" onclick="logout()">登出</div>
    <div><%=currentUser.getUserName()  %></div>
    <div title="金币" class="money" id="head_money">0</div>
    <script type="text/javascript"> 
        function logout() { 
            r.cuser.logout(function () { 
                location = "index.jsp"; 
            }); 
        }; 
        $(function () { 
            server("legend.practice.getMoney", function (d) { 
                $("#head_money").text(d); 
            }); 
        });
    </script>
	</div>
    <div class="page_menu">
        <div class="menu">
            <img alt="1" src="imgs/artwork3-l.jpg">
            <a class="btn" href="pready.jsp">练习模式</a>
        </div>
        <div class="menu">
            <img alt="1" src="imgs/artwork5-l.jpg">    
            <a class="btn">对战模式</a>
        </div>
        <div class="menu">
            <img alt="1" src="imgs/artwork6-l.jpg">    
            <a class="btn">竞技模式</a>
        </div>
        <div class="smenu">
            <a href="favorites.jsp">我的卡牌</a>
            <a href="pack.jsp">卡包</a>
            <a href="shop.jsp">商店</a>
        </div>
        
    </div>
	<%
		}
	%>
 
</body></html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.lc.UserBean"%>

<%@ page import="com.lc.Game"%>
<%@ page import="com.lc.dao.GameDao"%>
<%@ page import="utils.SpringFactory"%>
	
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
	<script>location.href="index.jsp";</script>
	<% 
		}
		
		else
		{
			GameDao gameDao=(GameDao)SpringFactory.getFactory().getBean("gameDao");
			Game game=gameDao.getGame(currentUser.getUserName());
			if(game==null)
			{
	%>

	<div class="page_head">
    <h1>山寨炉石</h1>
    <h6>一个卡牌爱好者独立制作的<b>炉石传说</b>山寨网页版</h6>
    <div class="in" onclick="logout()">登出</div>
    <div><%=currentUser.getUserName() %></div>
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
        }) 
    </script>
</div>
    <div class="page_prompt">
        <h1>练习模式</h1>
        <a href="index.jsp" class="in">返回</a>
    </div>
    <div class="page_ready">
       <div class="card_group">
    <h1>选择套牌</h1>
    <div style="display: none;" id="self" class="cardlist">
    
        <div class="card" job="圣骑士" name="圣骑"><div class="job job1"></div><span>圣骑</span></div>
        
        <div class="card" job="萨满" name="aaa"><div class="job job3"></div><span>aaa</span></div>
        
    </div>
    <div style="display: block;" id="default" class="cardlist">
        <div class="card selected" job="圣骑士" name="base_pack"><div class="job job1"></div><span>基本套牌</span></div>
        <div class="card" job="潜行者" name="base_pack"><div class="job job2"></div><span>基本套牌</span></div>
        <div class="card" job="萨满" name="base_pack"><div class="job job3"></div><span>基本套牌</span></div>
        <div class="card" job="猎人" name="base_pack"><div class="job job4"></div><span>基本套牌</span></div>
        <div class="card" job="德鲁伊" name="base_pack"><div class="job job5"></div><span>基本套牌</span></div>
        <div class="card" job="法师" name="base_pack"><div class="job job6"></div><span>基本套牌</span></div>
        <div class="card" job="术士" name="base_pack"><div class="job job7"></div><span>基本套牌</span></div>
        <div class="card" job="战士" name="base_pack"><div class="job job8"></div><span>基本套牌</span></div>
        <div class="card" job="牧师" name="base_pack"><div class="job job9"></div><span>基本套牌</span></div>
    </div>
    <div class="bar">
        <span class="c" f="default" id="btn_base">基本套牌</span>
        <span class="" f="self" id="btn_self">我的套牌</span>
    </div>
</div>
<script type="text/javascript"> 
    $(".bar span").click(function () { 
        $(this).parent().find('span').each(function () { 
            $(this).removeClass("c"); 
            var f = $(this).attr("f"); 
            $("#" + f + " div").removeClass("selected"); 
            $("#" + f).hide(); 
        }); 
        $(this).addClass('c'); 
        var f = $(this).attr("f"); 
        $("#" + f).show(); 
        $("#" + f + " div:first").addClass("selected"); 
    }); 
    $(function () { 
        if ($("#self div").length <= 0) { 
            $("#btn_base").click(); 
        } 
        else { 
            //$("#btn_self").click(); 
            $("#btn_base").click(); 
        } 
    }); 
</script>
       <div class="enemy">
            <h1>选择对手</h1>
            <div class="enemyjob job job1 selected" job="圣骑士"></div>
           <div class="enemyjob job job2" job="潜行者"></div>
           <div class="enemyjob job job3" job="萨满"></div>
           <div class="enemyjob job job4" job="猎人"></div>
           <div class="enemyjob job job5" job="德鲁伊"></div>
           <div class="enemyjob job job6" job="法师"></div>
           <div class="enemyjob job job7" job="术士"></div>
           <div class="enemyjob job job8" job="战士"></div>
           <div class="enemyjob job job9" job="牧师"></div>
       </div>
        <div class="start">
            
            <div class="btn" onclick="begin()">开始</div>
            
        </div>
    </div>
    <script type="text/javascript"> 
        function begin() { 
            var n1 = $(".card.selected").attr("job"); 
            var n2 = $(".enemyjob.selected").attr("job"); 
            var pack = $(".card.selected").attr("name"); 
            if (!n1) { 
                r.error("未选套牌！") 
                return; 
            } 
            server("legend.practice.begin", n1,pack, n2, function (d) { 
                location = "play.jsp"; 
            }); 
        }; 
 
        $(function () { 
            //server("legend.practice.checkcards", function (d) { 
            //    alert('all card is ready!'); 
            //}); 
            $(".enemyjob").click(function () { 
                $(".enemyjob").removeClass("selected"); 
                $(this).addClass("selected"); 
            }); 
            $(".card").click(function () { 
                $(".card").removeClass("selected"); 
                $(this).addClass("selected"); 
            }); 
        }); 
    </script>

	<% 
			}
			else
			{
				response.sendRedirect("play.jsp");
			}
		}
	%>
</body></html>











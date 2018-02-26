<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<form:form action="/emp" method="POST" modelAttribute="employee">

		imageid: <form:input path="imageid" id="imageid"/>
		<br>
		name:<form:input path="name"/>
		<br>
		<input type="submit" value="Submit"/>
		
	</form:form>
</body>
</html>
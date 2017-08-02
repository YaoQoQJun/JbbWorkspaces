<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="css/main.css" rel="stylesheet" type="text/css" />
    <link href="css/login.css" rel="stylesheet" type="text/css" />
    <script src="js/jquery.min.js" type="text/javascript"></script>
    <script type="text/javascript">
	    if(top!=window){
			top.location=window.location;
		 }
    
	    function checkUsername(){
			$("#error").html("&nbsp;");
		 	var username=$('#username').val().trim();
		 	if(username==""){
		 		$("#error").text("用户名不能为空！");
		 		return false;
		 	}else{
		 		return true;
		 	}	
		 }
		 function checkPassword(){
			$("#error").html("&nbsp;");
		 	var password=$('#password').val().trim();
		 	if(password==""){
		 		$("#error").text("密码不能为空！");
		 		return false;
		 	}else{
		 		return true;
		 	}	
		 }
	 
		function submit_btn(){
			if(checkUsername()&&checkPassword()){
					$("form:first").submit();		//手动提交表单 
			}
		}
		
		
		$(function(){
	    	$('#password').keydown(function (e) {
		            if (e.keyCode == 13&&checkUsername()&&checkPassword()) {
		                $("form:first").submit();		//enter键提交表单
		             }
		         });
		});
    </script>
    <title>登陆</title>
  </head>
  <body>
    <div class="bg-login">
        <div class="form-signin">
            <div class="form-signin-heading text-center">
                <span class="sign-title">用户登录</span>
                <img src="images/login-logo.png" />
            </div>
            <form action="login/login.do" method="post">
            <div class="login-wrap">
            	<p id="error" style="color: red;text-align: center;font-size: 12px;">&nbsp;${error}</p>
                <input id="username" name="username" onblur="checkUsername()" type="text" class="form-control" placeholder="username" autofocus="">
                <input id="password" name="password" onblur="checkPassword()" type="password" class="form-control" placeholder="password">
                <button id="btn" onclick="submit_btn()" class="btn btn-lg btn-login" type="button">
                   	 登 录
                </button>
            </div>
            </form>
        </div>
    </div>
  </body>
</html>


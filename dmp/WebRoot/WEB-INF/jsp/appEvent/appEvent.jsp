<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="cc" uri="/mytaglib" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>APP事件</title>

<link href="css/icon.css" rel="stylesheet" type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">

<script type="text/javascript" src="js/jquery.min.js"></script>
<script src="js/load.js" type="text/javascript"></script>

<style type="text/css">
#page_div {
	padding-top: 14px;
	margin: 0px auto;
	text-align: center;
	position: relative;
	top: 30px;
	left:1px;
}

#page_div a {
	text-decoration: none;
	display: block;
	float: left;
	border: 1px solid #EBEBEB;
	height: 34px;
	width: 34px;
	line-height: 34px;
	margin-right: 10px;
	font-size: 14px;
	color: #799FD9;
	text-align: center;
}

#page_div a:hover {
	background-color: #F0F6FD;
	border: 1px solid #3388FF;
}
</style>

<script type="text/javascript" language="javascript">

	$(function(){
		
		$(".page_a").click(function(){
			var style=$(this).attr("style");
			if(style!=null){
				return;
			}
			var text=$(this).text();
			var client_model=$("#selectModel").val();
			var channel_name=$("#selectChannel").val();
			var app_version=$("#selectVersion").val();
			
			window.location.href=base+"appEvent/toAppEvent.do?"+"&page="+text+"&client_model="+client_model+"&channel_name="+channel_name+"&app_version="+app_version;
		});
		
		$("#down_page").click(function(){
			var totalPage=$("#totalPage").val();
			var page=$("#page").val();
			var client_model=$("#selectModel").val();
			var channel_name=$("#selectChannel").val();
			var app_version=$("#selectVersion").val();
			
			
			if(eval(page)<eval(totalPage)){
				page=eval(page)+1;
				window.location.href=base+"appEvent/toAppEvent.do?"+"&page="+page+"&client_model="+client_model+"&channel_name="+channel_name+"&app_version="+app_version;
			}
		});
		
		$("#up_page").click(function(){
			var page=$("#page").val();
			
			var client_model=$("#selectModel").val();
			var channel_name=$("#selectChannel").val();
			var app_version=$("#selectVersion").val();
			
			if(page>1){
				page=eval(page)-1;
				window.location.href=base+"appEvent/toAppEvent.do?"+"&page="+page+"&client_model="+client_model+"&channel_name="+channel_name+"&app_version="+app_version;
			}
		});
		
		$("#start_page").click(function(){
			var client_model=$("#selectModel").val();
			var channel_name=$("#selectChannel").val();
			var app_version=$("#selectVersion").val();
			window.location.href=base+"appEvent/toAppEvent.do?"+"&page="+1+"&client_model="+client_model+"&channel_name="+channel_name+"&app_version="+app_version;
			
		});
		
		$("#end_page").click(function(){
			
			var totalPage=$("#totalPage").val();
			var client_model=$("#selectModel").val();
			var channel_name=$("#selectChannel").val();
			var app_version=$("#selectVersion").val();
			
			window.location.href=base+"appEvent/toAppEvent.do?"+"&page="+totalPage+"&client_model="+client_model+"&channel_name="+channel_name+"&app_version="+app_version;
			
		});
		
		
		$("#selectModel").val('${client_model}');
		$("#selectChannel").val('${channel_name}');
		$("#selectVersion").val('${app_version}');
		
		
	});

	var base = document.getElementsByTagName("base")[0].getAttribute("href");

	function changeSelect(){
		var client_model=$("#selectModel").val();
		var channel_name=$("#selectChannel").val();
		var app_version=$("#selectVersion").val();
		var page=$("#page").val();
		window.location.href=base+"appEvent/toAppEvent.do?"+"&page="+page+"&client_model="+client_model+"&channel_name="+channel_name+"&app_version="+app_version;
	}

	function toDetailAppEvent(event_name,event_remark){
		
		window.location.href=base+"appEvent/toAppEventDetail.do?event_name="+event_name;
	}
	
</script>
</head>

<body style="padding: 0px; margin: 0px;" class="pl15">

	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">APP事件分析 </span>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-right: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<select id="selectVersion" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeSelect();">
				<option value="0">全部版本</option>
				<c:forEach items="${appVersions}" var="appVersion">
					<option value="${appVersion.app_version_name}">${appVersion.app_version_name}</option>
				</c:forEach>
			</select>
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-right: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<select id="selectChannel" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeSelect();">
				<option value="0">全部渠道</option>
				<c:forEach items="${appChannels}" var="appChannel">
					<option value="${appChannel.app_channel_name}">${appChannel.app_channel_name}</option>
				</c:forEach>
			</select>
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-right: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<select id="selectModel" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeSelect();">
				<option value="0">全部终端</option>
				<option value="android">android</option>
				<option value="ios">ios</option>
			</select>
		</div>
		
    </div>
    
    	<!-- 列表操作 -->
	<div class="centMain">
		<!-- 列表 -->
		<table class="table mt15">
			<thead>
				<tr>
					<th style="width: 15%">事件名称</th>
					<th style="width: 15%">备注</th>
					<th style="width: 15%">昨日消息数</th>
					<th style="width: 15%">昨日用户数</th>
					<th style="width: 15%">今日消息数</th>
					<th style="width: 15%">今日用户数</th>
					<th style="width: 10%">详情</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${lists}" var="list">
					<tr>
						<td>${list.event_name}</td>
						<td>${list.event_remark}</td>
						<td>${list.yesterdayCount}</td>
						<td>${list.yesterdayUserCount}</td>
						<td>${list.todayCount}</td>
						<td>${list.todayUserCount}</td>
						<td>
							<a class="sckre" onclick="toDetailAppEvent('${list.event_name}','${list.event_remark}');">查看详情</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div id="page_div"
		style="width:590px;height:50px;margin: 0px auto;text-align: center;margin-top: 20px;clear: both;">
		<input id="page" type="hidden" value="${page}"> <input
			id="totalPage" type="hidden" value="${totalPage}"> <a
			id="start_page" href="javascript:void(0);" style="width: 78px;">首
			页</a> <a id="up_page" href="javascript:void(0);" style="width: 78px;">〈
			上一页</a>
		<c:choose>
			<c:when test="${(page-1)%5==0}">
				<fmt:formatNumber type="number" value="${(page-1)/5}"
					maxFractionDigits="0" var="fmtNumber" />
			</c:when>
			<c:otherwise>
				<fmt:formatNumber type="number" value="${(page-1)/5-0.5}"
				maxFractionDigits="0" var="fmtNumber" />
			</c:otherwise>
		</c:choose>
		<c:forEach begin="1" end="5" varStatus="status">
			<c:choose>
				<c:when test="${(status.count+fmtNumber*5)>totalPage}">
					<a class="page_a" href="javascript:void(0);"
						style="visibility: hidden;"></a>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${(status.count+fmtNumber*5)==page}">
							<a class="page_a" href="javascript:void(0);"
								style="border:1px solid white;color: #000;">
								${status.count+fmtNumber*5} </a>
						</c:when>
						<c:otherwise>
							<a class="page_a" href="javascript:void(0);">
								${status.count+fmtNumber*5} </a>
						</c:otherwise>
					</c:choose>

				</c:otherwise>
			</c:choose>

		</c:forEach>

		<a id="down_page" href="javascript:void(0);" style="width: 78px;">下一页
			〉</a> <a id="end_page" href="javascript:void(0);" style="width: 78px;">尾
			页</a>
	</div>
		
</body>
</html>


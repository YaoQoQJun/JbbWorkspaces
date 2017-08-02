<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="cc" uri="/mytaglib" %>
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
<link href="css/icon.css" rel="stylesheet" type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/jquery.zclip.min.js"></script>
<script type="text/javascript" src="js/laydate.dev.js"></script>

<script src="js/load.js" type="text/javascript"></script>

<title>网站管理</title>

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
.pt15{
	padding-top: 5px;
}
</style>

<script type="text/javascript">
	$(function(){
		
		var base = document.getElementsByTagName("base")[0].getAttribute("href");
		var date=$("#date").val();
	 	date=$.trim(date);
	 	
		$(".page_a").click(function(){
			var style=$(this).attr("style");
			if(style!=null){
				return;
			}
			var text=$(this).text();
			window.location.href=base+"monitor/toMonitor.do?"
			+"&page="+text
			+"&date="+date;
		});
		
		$("#down_page").click(function(){
			var totalPage=$("#totalPage").val();
			var page=$("#page").val();
			
			if(eval(page)<eval(totalPage)){
				page=eval(page)+1;
				window.location.href=base+"monitor/toMonitor.do?"
				+"&page="+page
				+"&date="+date;
			}
		});
		
		$("#up_page").click(function(){
			var page=$("#page").val();
			
			
			if(page>1){
				page=eval(page)-1;
				window.location.href=base+"monitor/toMonitor.do?"
				+"&page="+page
				+"&date="+date;
			}
		});
		
		$("#start_page").click(function(){
			
			
			window.location.href=base+"monitor/toMonitor.do?"
			+"&page="+1
			+"&date="+date;
		});
		
		$("#end_page").click(function(){
			
			var totalPage=$("#totalPage").val();
			
			window.location.href=base+"monitor/toMonitor.do?"
			+"&page="+totalPage
			+"&date="+date;
		});
		
		
		laydate({
            elem: '#date',
            max: laydate.now()
        });
		
		var date=$("#date").val();
		if(date==null||date==""){
			var myDate = new Date();
			var year=myDate.getFullYear();
			var month=myDate.getMonth()+1;
			if(month<10){
				month="0"+month;
			}
			var day=myDate.getDate();   
			$("#date").val(year+"-"+month+"-"+day);
		}
	});

	
	function seeSoftware(extension_id){
		var date=$("#date").val();
	 	date=$.trim(date);
		window.location.href="<%=basePath%>monitor/seeSoftWare.do?extension_id="+extension_id+"&state=1"+"&date="+date;
	}
	function toDetailMonitor(extension_link,extension_id){
		var date=$("#date").val();
	 	date=$.trim(date);
		window.location.href="<%=basePath%>monitor/toDetailMonitor.do?extension_id="+extension_id+"&extension_link="+extension_link+"&date="+date;
	}
	
	function searchDate(){
		var page=$("#page").val();
	 	var date=$("#date").val();
	 	date=$.trim(date);
		window.location.href="<%=basePath%>monitor/toMonitor.do?page="+page+"&date="+date;
	}
</script>

</head>
<body class="pl15">
		<div class="centMain bs-bmd" style="margin-top: 20px;">
            <span class="nasrnt">综合数据</span>
            <div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px; ">
            	<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
            	<input type="text" placeholder="日期搜索" id="date" value="${date}" style="width:200px; height:30px; padding:0 6px;border:0" />
            	<a class="fr" id="search_date" onclick="searchDate();" href="javascript:void(0);" style=" display:inline-block; padding:0 14px; line-height:30px; border-left:1px solid rgba(0,0,0,.2);"> 查找</a>
            </div>
        </div>

		<!-- 列表操作 -->
		<div class="centMain">
			<!-- 列表 -->
			<table class="table mt15">
				<thead>
					<tr>
						<th style="width: 5%">编号</th>
						<th style="width: 35%">推广网站</th>
						<th style="width: 10%">渠道名称</th>
						<th style="width: 8%">渠道编号</th>
						<th style="width: 8%">推广天数</th>
						<th style="width: 8%">PV(当日)</th>
						<th style="width: 8%">UV(当日)</th>
						<th style="width: 8%">IP(当日)</th>
						<th style="width: 10%">详情</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${extensions}" var="extension" varStatus="s">
						<tr>
	                        <td>${extension.id}</td>
	                        <td><a href="javascript:toDetailMonitor('${extension.extension_link}',${extension.id});" style="color: #799FD9;">${extension.website.name}</a></td>
	                        <td>${extension.channel_name}</td>
	                        <td>${extension.channel_id}</td>
	                        <td>
	                     		 <cc:pd date="${extension.enable_time}"></cc:pd>
	                        </td>
	                        <td>
	                        	${pvs[s.index]}
	                        </td>
	                        <td>
	                        	${uvs[s.index]}
							</td>
							 <td>
	                        	${ips[s.index]}
							</td>
	                        <td>
	                        	<a class="sckre" onclick="seeSoftware(${extension.id});">参数详情</a>
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


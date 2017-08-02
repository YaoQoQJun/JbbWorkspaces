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

<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-jqLoding.js"></script>
<script type="text/javascript" src="js/jquery.zclip.min.js"></script>

<script src="js/load.js" type="text/javascript"></script>

<title>推广链接详细数据</title>

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
	
	$(".page_a").click(function(){
		var style=$(this).attr("style");
		if(style!=null){
			return;
		}
		var text=$(this).text();
		
		window.location.href=base+"monitor/toDetailMonitor.do?"
		+"&extension_id=${extension_id}"
		+"&extension_link=${extension_link}"
		+"&date="+date
		+"&page="+text;
	});
	
	$("#down_page").click(function(){
		var totalPage=$("#totalPage").val();
		var page=$("#page").val();
		
		if(eval(page)<eval(totalPage)){
			page=eval(page)+1;
			window.location.href=base+"monitor/toDetailMonitor.do?"
			+"&extension_id=${extension_id}"
			+"&extension_link=${extension_link}"
			+"&date="+date
			+"&page="+page;
		}
	});
	
	$("#up_page").click(function(){
		var page=$("#page").val();
		
		
		if(page>1){
			page=eval(page)-1;
			window.location.href=base+"monitor/toDetailMonitor.do?"
			+"&extension_id=${extension_id}"
			+"&extension_link=${extension_link}"
			+"&date="+date
			+"&page="+page;
		}
	});
	
	$("#start_page").click(function(){
		window.location.href=base+"monitor/toDetailMonitor.do?"
		+"&extension_id=${extension_id}"
		+"&extension_link=${extension_link}"
		+"&date="+date
		+"&page="+1;
	});
	
	$("#end_page").click(function(){
		
		var totalPage=$("#totalPage").val();
		
		window.location.href=base+"monitor/toDetailMonitor.do?"
		+"&extension_id=${extension_id}"
		+"&extension_link=${extension_link}"
		+"&date="+date
		+"&page="+totalPage;
	});
});

	
	function seeLinkSoftware(extension_id,link){
		link=link.replace(/&/g,"%26");
		var date=$("#date").val();
		window.location.href="<%=basePath%>monitor/seeLinkSoftware.do?extension_id="+extension_id+"&link="+link+"&date="+date;
	}
	
	function seeHeatmap(url,uv){
		var date=$("#date").val();
		if(url.indexOf("?")!=-1){
			window.open(url+"&state=abc&url="+url.replace(/&/g,"%26")+"&uv="+uv+"&date="+date);
			
		} else{
			window.open(url+"?&state=abc&url="+url.replace(/&/g,"%26")+"&uv="+uv+"&date="+date);
		}
	}
	
	function toSearch(extension_id){
		$.fn.jqLoading({ height: 100, width: 240, text: "正在加载中，请耐心等待...." });
		var link=$("#link").val();
		
		link=link.replace(/&/g,"%26");
		
		var date=$("#date").val();
		
		window.location.href="<%=basePath%>monitor/toDetailMonitor.do?search_link="+link+"&extension_id="+extension_id+"&date="+date;
	}
	
</script>

</head>
<body class="pl15">
		<input type="hidden" id="date" value="${date}">
		<div class="centMain bs-bmd" style="margin-top: 20px;">
			<a class="fz15 fl fxback" href="javascript:window.history.go(-1);"><i class="icon icon-60b"></i>返回</a>
            <span class="nasrnt">推广链接详细数据</span>
           <!--
            <div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px; ">
            	<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
            	<input type="text" id="referer_link" placeholder="上级链接搜索" value="${referer_link}" style="width:300px; height:30px; padding:0 6px;border:0" />
            	<a class="fr" id="search_a" onclick="toSearchReferer(${extension.id});" href="javascript:void(0);" style=" display:inline-block; padding:0 14px; line-height:30px; border-left:1px solid rgba(0,0,0,.2);"> 查找</a>
           </div>
             -->
           <div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px; ">
           	<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
           	<input type="text" id="link" placeholder="当前链接搜索" value="${search_link}" style="width:300px; height:30px; padding:0 6px;border:0" />
           	<a class="fr" id="search_a" onclick="toSearch(${extension_id});" href="javascript:void(0);" style=" display:inline-block; padding:0 14px; line-height:30px; border-left:1px solid rgba(0,0,0,.2);"> 查找</a>
           </div>
        </div>
		<!-- 列表操作 -->
		<div class="centMain">
			<!-- 列表 -->
			<table class="table mt15">
				<thead>
					<tr>
						<th style="width: 4%">序号</th>
						<th style="width: 35%">推广链接</th>
						<th style="width: 6%">PV(当日)</th>
						<th style="width: 6%">UV(当日)</th>
						<th style="width: 6%">ip(当日)</th>
						<th style="width: 6%">人均浏览页面</th>
						<th style="width: 6%">跳出率</th>
						<th style="width: 6%">老访客占比</th>
						<th style="width: 6%">平均访问时间</th>
						<th style="width: 6%">输出pv量</th>
						<th style="width: 13%">详情</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${hbaseDetails}" var="hbaseDetail" varStatus="s">
					<tr>
					    <td>${s.count}</td>
                        <td>${hbaseDetail.url}</td>
                        <td>
                       		${hbaseDetail.count_pv}
                        </td>
                        <td>
                        	${hbaseDetail.count_uv}
						</td>
						<td>
                        	${hbaseDetail.count_ip}
						</td>
                        <td>
                        	<fmt:formatNumber value="${hbaseDetail.count_pv/hbaseDetail.count_uv}" pattern="#.00"/>
                        </td>
                        <td>
                        	<fmt:formatNumber value="${((hbaseDetail.count_pv-hbaseDetail.count_op)/hbaseDetail.count_pv)*100}" pattern="#.00"/>%
                        </td>
                        <td>
                        	<fmt:formatNumber value="${((hbaseDetail.count_pv-hbaseDetail.count_ip)/hbaseDetail.count_pv)*100}" pattern="#.00"/>%
                        </td>
                        <td>${hbaseDetail.count_at}s</td>
                        <td>${hbaseDetail.count_op}</td>
                        <td>
                        	<a class="sckre" onclick="seeHeatmap('${hbaseDetail.url}','${hbaseDetail.count_uv}');">热图详情</a>
                        	&nbsp;&nbsp;
                        	<a class="sckre" onclick="seeLinkSoftware(${extension_id},'${hbaseDetail.url}');">参数详情</a>
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


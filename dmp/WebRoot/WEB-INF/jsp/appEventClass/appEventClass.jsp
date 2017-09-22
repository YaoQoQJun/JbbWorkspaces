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
<title>事件分类管理</title>

<link href="css/icon.css" rel="stylesheet" type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.zclip.min.js"></script>
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

var base = document.getElementsByTagName("base")[0].getAttribute("href");

var save_app_event_class_name_flag=false;

var update_app_event_class_name_flag=false;


$(function(){
	
	$(".page_a").click(function(){
		var style=$(this).attr("style");
		if(style!=null){
			return;
		}
		var text=$(this).text();
		
		window.location.href=base+"appEventClass/toAppEventClass.do?"
		+"&page="+text;
	});
	
	$("#down_page").click(function(){
		var totalPage=$("#totalPage").val();
		var page=$("#page").val();
		
		if(eval(page)<eval(totalPage)){
			page=eval(page)+1;
			window.location.href=base+"appEventClass/toAppEventClass.do?"
			+"&page="+page;
		}
	});
	
	$("#up_page").click(function(){
		var page=$("#page").val();
		
		
		if(page>1){
			page=eval(page)-1;
			window.location.href=base+"appEventClass/toAppEventClass.do?"
			+"&page="+page;
		}
	});
	
	$("#start_page").click(function(){
		window.location.href=base+"appEventClass/toAppEventClass.do?"
		+"&page="+1;
	});
	
	$("#end_page").click(function(){
		var totalPage=$("#totalPage").val();
		window.location.href=base+"appEventClass/toAppEventClass.do?"
		+"&page="+totalPage;
	});
	
	
	$("#save_app_event_class_name").blur(function(){
		
		$("#save_app_event_class_error_msg").html("&nbsp;");
		var app_event_class_name=$("#save_app_event_class_name").val();
		app_event_class_name=$.trim(app_event_class_name);
		if(app_event_class_name==null||app_event_class_name==""){
			$("#save_app_channel_error_msg").text("事件名称不能为空！");
			save_app_event_class_name_flag=false;
			return;
		}
		
		$.ajax({
			type:"post",
			url:"appEventClass/checkAppEventClassName.do",
			data:{"app_event_class_name":app_event_class_name},
			async:false,
			success:function(msg){
				if(msg=="no"){
					$("#save_app_event_class_error_msg").text("APP事件分类名称已经存在！");
					save_app_event_class_name_flag=false;
				}else{
					save_app_event_class_name_flag=true;
				}
			},
			error:function(msg){
				$("#save_app_event_class_error_msg").text("检查APP事件分类名称错误！");
				save_app_event_class_name_flag=false;
			}
		});
		
	});
	
	$("#update_app_event_class_name").blur(function(){
		$("#update_app_event_class_error_msg").html("&nbsp;");
		var app_event_class_name=$("#update_app_event_class_name").val();
		app_event_class_name=$.trim(app_event_class_name);
		if(app_event_class_name==null||app_event_class_name==""){
			$("#update_app_event_class_error_msg").text("事件分类名称不能为空！");
			update_app_event_class_name_flag=false;
			return;
		}
		
		$.ajax({
			type:"post",
			url:"appEventClass/checkAppEventClassName.do",
			data:{"app_event_class_name":app_event_class_name,"id":updateId},
			async:false,
			success:function(msg){
				if(msg=="no"){
					$("#update_app_event_class_error_msg").text("事件分类名称已经存在！");
					update_app_event_class_name_flag=false;
				}else{
					update_app_event_class_name_flag=true;
				}
			},
			error:function(msg){
				$("#update_app_event_class_error_msg").text("检查事件分类名称错误！");
				update_app_event_class_name_flag=false;
			}
		});
	});

});	
		
	
	
	function newAppEventClass(){
		$("#save_app_event_class_div").removeClass("display_none");
		$("#save_app_event_class_error_msg").html("&nbsp;");
		$("#save_app_event_class_name").val("");
	}
	
	function saveAppEventClass(){
		$("#save_app_event_class_name").blur();
		if(!save_app_event_class_name_flag){
			$("#save_app_event_class_name").blur();
			return false;
		}
		
		var app_event_class_name=$("#save_app_event_class_name").val();
		
		$("#saveAppEventClass_button").attr("disabled",true);
		$.ajax({
			type:"post",
			url:"appEventClass/addAppEventClass.do",
			traditional: true,
			data:{"app_event_class_name":app_event_class_name},
			success:function(msg){
				if (msg){
					$("#save_app_event_class_error_msg").text("添加成功！");
					$("#saveAppEventClass_button").attr("disabled",false);
				} else {
					$("#save_app_event_class_error_msg").text("添加失败！");
					$("#saveAppEventClass_button").attr("disabled",false);
				}
			},
			error:function(msg){
				$("#save_app_event_class_error_msg").text("添加失败！");
				$("#saveAppEventClass_button").attr("disabled",false);
			}
		});
	}
	
	var updateId=null;
	function editAppEventClass(id,app_event_class_name){
		updateId=id;
		$("#update_app_event_class_div").removeClass("display_none");
		$("#update_app_event_class_error_msg").html("&nbsp;");
		$("#update_app_event_class_name").val(app_event_class_name);
	}
	
	function updateAppEventClass(){
		$("#update_app_event_class_name").blur();
		if(!update_app_event_class_name_flag){
			return false;
		}
		
		var app_event_class_name=$("#update_app_event_class_name").val();
		$.ajax({
			type:"post",
			url:"appEventClass/updateAppEventClass.do",
			traditional: true,
			data:{"id":updateId,"app_event_class_name":app_event_class_name},
			success:function(msg){
				if (msg){
					$("#update_app_event_class_error_msg").text("修改成功！");
				} else {
					$("#update_app_event_class_error_msg").text("修改失败！");
				}
			},
			error:function(msg){
				$("#update_event_class_error_msg").text("修改失败，请联系管理员！");
			}
		});
		
	}
	
	
	function removeAppEventClasss(id){
		if(confirm("确认删除该APP事件分类?")){
			var page=$("#page").val();
			$.ajax({
				type:"post",
				url:"appEventClass/deleteAppEventClass.do",
				data:{"id":id},
				dataType:"json",
				success:function(msg){
					if (msg){
						window.location.href=base+"appEventClass/toAppEventClass.do?page="+page;
					} else {
						$.messager.show({	
							title: "信息",
							msg: "删除失败！"
						});
					}
				},
				error:function(mgs){
					$.messager.show({	
						title: "信息",
						msg: "删除失败，请联系管理员！"
					});
				}
			});
		}
	}
	
	function Btn_Close(obj) {
        $(obj).parents(".newmoent").addClass("display_none");
    }
	function refresh(){
		var page=$("#page").val();
		window.location.href="<%=basePath%>appEventClass/toAppEventClass.do?page="+page;
	}
</script>
</head>

<body style="padding: 0px; margin: 0px;" class="pl15">

	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">事件分类管理</span>
    </div>
    <!-- 按钮 -->
    <div class="centMain mt15">
    	<cc:ps privileges="${privileges}" privilege="增加,事件分类">
        <a class="itms" onclick="newAppEventClass();"><i class="icon icon-614 fz12"></i>添加事件分类</a>
        <a class="itms" onclick="refresh()">刷 &nbsp; 新</a>
        </cc:ps>
    </div>
    
	<!-- 列表操作 -->
	<div class="centMain">
		<!-- 列表 -->
		<table class="table mt15">
			<thead>
				<tr>
					<th style="width: 20%">序号</th>
					<th style="width: 60%">事件分类名称</th>
					<th style="width: 20%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${appEventClasss}" var="appEventClass" varStatus="s">
					<tr>
						<td>${appEventClass.id}</td>
						<td>${appEventClass.app_event_class_name}</td>
						<td>
							<cc:ps privileges="${privileges}" privilege="修改,事件分类">
							<a class="sckre" onclick="editAppEventClass(${appEventClass.id},'${appEventClass.app_event_class_name}');">
								<i class="icon icon-60a"></i>修改
							</a>
							</cc:ps>
							<cc:ps privileges="${privileges}" privilege="删除,事件分类">
							<a class="sckre" onclick="removeAppEventClasss(${appEventClass.id});">
								<i class="icon icon-604"></i>删除
							</a>
							</cc:ps>
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
	
	
	
	
	<!-- 保存渠道 -->
    <div id="save_app_event_class_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>添加APP渠道</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="save_app_event_class_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">APP渠道名称</div>
                <div class="centMain pt5">
                    <input id="save_app_event_class_name" name="app_event_class_name" type="text" class="txtput fz15" placeholder="APP事件分类名称" />
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="saveAppEventClass_button" class="btn" onclick="saveAppEventClass();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="save_event_class_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 修改渠道 -->
    <div id="update_app_event_class_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>修改APP事件分类</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="update_app_event_class_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">APP事件分类名称</div>
                <div class="centMain pt5">
                    <input id="update_app_event_class_name" name="app_event_class_name" type="text" class="txtput fz15" placeholder="APP事件分类名称" />
                </div>
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="updateAppEventClass_button" class="btn" onclick="updateAppEventClass();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="update_app_event_class_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
		
</body>
</html>


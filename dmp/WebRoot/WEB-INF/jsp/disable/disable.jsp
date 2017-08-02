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

var save_channel_name_flag=false;
var save_channel_id_flag=false;
var save_disable_link_flag=false;

var update_channel_name_flag=false;
var update_channel_id_flag=false;
var update_disable_link_flag=false;


$(function(){
	
	var base = document.getElementsByTagName("base")[0].getAttribute("href");
	
	$(".page_a").click(function(){
		var style=$(this).attr("style");
		if(style!=null){
			return;
		}
		var text=$(this).text();
		window.location.href=base+"disable/toDisable.do?"
		+"&page="+text;
	});
	
	$("#down_page").click(function(){
		var totalPage=$("#totalPage").val();
		var page=$("#page").val();
		
		if(eval(page)<eval(totalPage)){
			page=eval(page)+1;
			window.location.href=base+"disable/toDisable.do?"
			+"&page="+page;
		}
	});
	
	$("#up_page").click(function(){
		var page=$("#page").val();
		
		
		if(page>1){
			page=eval(page)-1;
			window.location.href=base+"disable/toDisable.do?"
			+"&page="+page;
		}
	});
	
	$("#start_page").click(function(){
		
		
		window.location.href=base+"disable/toDisable.do?"
		+"&page="+1;
	});
	
	$("#end_page").click(function(){
		
		var totalPage=$("#totalPage").val();
		
		window.location.href=base+"disable/toDisable.do?"
		+"&page="+totalPage;
	});
	
	
	$("#save_channel_name").blur(function(){
		$("#save_extension_error_msg").html("&nbsp;");
 		var channel_name=$(this).val();
 		if(channel_name==null||channel_name==""){
 			$("#save_extension_error_msg").text("渠道名称不能为空！");
 			save_channel_name_flag=false;
 		}else{
 			save_channel_name_flag=true;
 		}
	});

	$("#save_channel_id").blur(function(){
		$("#save_extension_error_msg").html("&nbsp;");
 		var channel_id=$(this).val();
 		if(channel_id==null||channel_id==""){
 			$("#save_extension_error_msg").text("渠道编号不能为空！");
 			save_channel_id_flag=false;
 		}else{
 			save_channel_id_flag=true;
 		}
	});
	
	$("#save_disable_link").blur(function(){
		$("#save_disable_error_msg").html("&nbsp;");
 		var disable_link=$(this).val();
 		if(disable_link==null||disable_link==""){
 			$("#save_disable_error_msg").text("推广链接不能为空！");
 			save_disable_link_flag=false;
 		}else{
 			save_disable_link_flag=true;
 		}
	});
	
	
	$("#update_channel_name").blur(function(){
		$("#update_extension_error_msg").html("&nbsp;");
 		var channel_name=$(this).val();
 		if(channel_name==null||channel_name==""){
 			$("#update_extension_error_msg").text("渠道名称不能为空！");
 			update_channel_name_flag=false;
 		}else{
 			update_channel_name_flag=true;
 		}
	});

	$("#update_channel_id").blur(function(){
		$("#update_extension_error_msg").html("&nbsp;");
 		var channel_id=$(this).val();
 		if(channel_id==null||channel_id==""){
 			$("#update_extension_error_msg").text("渠道编号不能为空！");
 			update_channel_id_flag=false;
 		}else{
 			update_channel_id_flag=true;
 		}
	});
	
	$("#update_disable_link").blur(function(){
		$("#update_disable_error_msg").html("&nbsp;");
 		var disable_link=$(this).val();
 		if(disable_link==null||disable_link==""){
 			$("#update_disable_error_msg").text("推广链接不能为空！");
 			update_disable_link_flag=false;
 		}else{
 			update_disable_link_flag=true;
 		}
	});
	
});

var update_disable_id=null;
function editDisable(id,website_id,name,domain_name,channel_name,channel_id,disable_link){
	$("#update_disable_div").removeClass("display_none");
	$("#update_website_name").empty();
	update_disable_id=id;
	$.ajax({
		type:"post",
		url:"extension/getWebsites.do",
		success:function(msg){
			$("#update_website_name").append("<option value='0'>请选择</option>");
			for (var int = 0; int < msg.length; int++) {
				$("#update_website_name").append("<option value='"+msg[int].id+"'>"+msg[int].name+"</option>");
			}
		},
		async:false
	});
	$("#update_website_name").val(website_id);
	$("#update_domain_name").val(domain_name);
	$("#update_channel_name").val(channel_name);
	$("#update_channel_id").val(channel_id);
	$("#update_disable_link").val(disable_link);
}

function changeUpdateWebsite(e){
	$("#update_disable_error_msg").html("&nbsp;");
	var website_id=$(e).val();
	if(website_id!=0){
		$.ajax({
			type:"post",
			url:"extension/getWebsite.do",
			data:{"website_id":website_id},
			success:function(msg){
				$("#update_domain_name").val(msg.domain_name);
			}
		});
	}else{
		$("#update_domain_name").val("");
	}
}

function updateDisable(){
	var website_id=$("#update_website_name").val();
	if(website_id=="0"){
		$("#update_disable_error_msg").html("请选择网站！");
		return false;
	}
	$("#update_channel_name").blur();
	if(!update_channel_name_flag){
		return false;
	}
	$("#update_channel_id").blur();
	if(!update_channel_id_flag){
		return false;
	}
	$("#update_disable_link").blur();
	if(!update_disable_link_flag){
		return false;
	}
	
	var channel_name=$("#update_channel_name").val();
	var channel_id=$("#update_channel_id").val();
	var disable_link=$("#update_disable_link").val();
	
	$.ajax({
		type:"post",
		url:"disable/updateDisable.do",
		data:{
			"id":update_disable_id,
			"website_id":website_id,
			"channel_name":channel_name,
			"channel_id":channel_id,
			"disable_link":disable_link
			},
		success:function(msg){
			if(msg=="ok"){
				$.messager.show({
					title: "信息",
					msg: "修改成功！"
				});
				$("#update_disable_close_button").parents(".newmoent").addClass("display_none");
			}else{
				$.messager.show({
					title: "信息",
					msg: "修改失败！"
				});
			}
			update_channel_name_flag=false;
			update_channel_id_flag=false;
			update_disable_link_flag=false;
		},
		error:function(msg){
			$.messager.show({
				title: "信息",
				msg: "修改失败，请联系管理员！"
			});
			update_channel_name_flag=false;
			update_channel_id_flag=false;
			update_disable_link_flag=false;
		}
	});
}


function newDisable(){
	
	$("#save_disable_div").removeClass("display_none");
	
	$("#save_website_name").empty();
	$("#save_domain_name").val("");
	$("#save_channel_name").val("");
	$("#save_channel_id").val("");
	$("#save_disable_link").val("");
	
	$.ajax({
		type:"post",
		url:"extension/getWebsites.do",
		success:function(msg){
			$("#save_website_name").append("<option value='0'>请选择</option>");
			for (var int = 0; int < msg.length; int++) {
				$("#save_website_name").append("<option value='"+msg[int].id+"'>"+msg[int].name+"</option>");
			}
		}
	});
	
	$("#save_website_name").val("0");
	
}

function saveDisable(){
	var website_id=$("#save_website_name").val();
	if(website_id=="0"){
		$("#save_disable_error_msg").html("请选择网站！");
		return false;
	}
	
	if(!save_channel_name_flag){
		$("#save_channel_name").blur();
		return false;
	}
	if(!save_channel_id_flag){
		$("#save_channel_id").blur();
		return false;
	}
	
	if(!save_disable_link_flag){
		$("#save_disable_link").blur();
		return false;
	}
	
	var channel_name=$("#save_channel_name").val();
	var channel_id=$("#save_channel_id").val();
	var disable_link=$("#save_disable_link").val();
	
	$.ajax({
		type:"post",
		url:"disable/addDisable.do",
		data:{
			"website_id":website_id,
			"channel_name":channel_name,
			"channel_id":channel_id,
			"disable_link":disable_link
			},
		success:function(msg){
			if(msg=="ok"){
				$.messager.show({
					title: "信息",
					msg: "添加成功！"
				});
				$("#save_disable_close_button").parents(".newmoent").addClass("display_none");
			}else{
				$.messager.show({
					title: "信息",
					msg: "添加失败！"
				});
			}
			save_channel_name_flag=false;
			save_channel_id_flag=false;
			save_disable_link_flag=false;
		},
		error:function(msg){
			$.messager.show({
				title: "信息",
				msg: "添加失败，请联系管理员！"
			});
			save_channel_name_flag=false;
			save_channel_id_flag=false;
			save_disable_link_flag=false;
		}
	});
}

function changeState(e,id){
	var state=$(e).text();
	state=$.trim(state);
	var str1=null;
	var str2=null;
	if(state=="启用"){
		state=1;
		str1="禁用";
		str2="启用";
	}else{
		state=0;
		str1="启用";
		str2="禁用";
	}
	
	if(confirm("确认"+str2)){
		$.ajax({
			type:"post",
			url:"disable/updateDisableState.do",
			data:{
				"id":id,
				"state":state
				},
			success:function(msg){
				if(msg=="ok"){
					$(e).html("<i class='icon icon-608'></i>&nbsp;"+str1);
					$(e).parent().prev().prev().text(str2);
				}else if(msg=="no_ok"){
					alert("请先开启网站！");
				}else{
					$.messager.show({
						title: "信息",
						msg: "修改失败！"
					});
				}
				
			},
			error:function(msg){
				$.messager.show({
					title: "信息",
					msg: "修改失败，请联系管理员！"
				});
			}
		});
	}
	
}

function changeSaveWebsite(e){
	$("#save_disable_error_msg").html("&nbsp;");
	var website_id=$(e).val();
	if(website_id!=0){
		$.ajax({
			type:"post",
			url:"extension/getWebsite.do",
			data:{"website_id":website_id},
			success:function(msg){
				$("#save_domain_name").val(msg.domain_name);
			}
		});
	}else{
		$("#save_domain_name").val("");
	}
}

function deleteDisable(id){
	if(confirm("确认删除")){
		$.ajax({
			type:"post",
			url:"disable/deleteDisable.do",
			data:{"id":id},
			success:function(msg){
				if(msg=="ok"){
					$.messager.show({
						title: "信息",
						msg: "删除成功！"
					});
					$("#refresh_a").click();
				}else{
					$.messager.show({
						title: "信息",
						msg: "删除失败！"
					});
				}
				
			},
			error:function(msg){
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
    
	function refreshWebsite(){
		var page=$("#page").val();
		window.location.href="<%=basePath%>disable/toDisable.do?page="+page;
	}
	
</script>

</head>
<body class="pl15">
		
		<div class="centMain bs-bmd" style="margin-top: 20px;">
            <span class="nasrnt">禁用链接</span>
        </div>
        <!-- 按钮 -->
        <div class="centMain mt15">
        	<cc:ps privileges="${privileges}" privilege="增加,禁用链接">
            <a class="itms" onclick="newDisable();"><i class="icon icon-614 fz12"></i>添加链接</a>
            </cc:ps>
            <a class="itms" onclick="refreshWebsite()">刷 &nbsp; 新</a>
        </div>

		<!-- 列表操作 -->
		<div class="centMain">
			<!-- 列表 -->
			<table class="table mt15">
				<thead>
					<tr>
						<th style="width: 5%">编号</th>
						<th style="width: 20%">禁用链接</th>
						<th style="width: 20%">渠道名称</th>
						<th style="width: 10%">渠道编号</th>
						<th style="width: 5%">状态</th>
						<th style="width: 15%">创建时间</th>
						<th style="width: 25%"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${disables}" var="disable">
						<tr>
	                        <td>${disable.id}</td>
	                        <td>${disable.disable_link}</td>
	                        <td>${disable.channel_name}</td>
	                        <td>${disable.channel_id}</td>
	                        <td>
	                        	<c:choose>
									<c:when test="${disable.state == 0}">
										禁用
									</c:when>
									<c:otherwise>
										启用
									</c:otherwise>
								</c:choose>
	                        </td>
	                        <td>
	                        <fmt:formatDate value="${disable.add_time}" pattern="yyyy-MM-dd hh:mm"/>
							</td>
	                        <td>
	                            <cc:ps privileges="${privileges}" privilege="修改,禁用链接">
								<a class="sckre" onclick="changeState(this,${disable.id});">
									<i class="icon icon-608"></i>
									<c:choose>
										<c:when test="${disable.state == 0}">
											启用
										</c:when>
										<c:otherwise>
											禁用
										</c:otherwise>
									</c:choose>
								</a> 
	                            <a class="sckre" onclick="editDisable(${disable.id},${disable.website_id},'${disable.website.name}','${disable.website.domain_name}','${disable.channel_name}','${disable.channel_id}','${disable.disable_link}');"><i class="icon icon-60a"></i>修改</a>
	                            </cc:ps>
	                            <cc:ps privileges="${privileges}" privilege="删除,禁用链接">
	                            <a class="sckre" onclick="deleteDisable(${disable.id})"><i class="icon icon-604"></i>删除</a>
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
   
    	<!-- 添加推广 -->
	    <div id="save_disable_div" class="newmoent display_none">
	        <div class="ment_ucount">
	            <div class="centMain metilte">
	                <span>添加链接</span>
	            </div>
	            <div class="centMain pl15 pr15">
	            	<div id="save_disable_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
	                
	                <div class="centMain pt15">推广网站</div>
	                <div class="centMain pt5">
	                	<select id="save_website_name" name="website_name" class="txtput fz15" onchange="changeSaveWebsite(this);">
	                	</select>
	                </div>
	                <div class="centMain pt15">域名</div>
	                <div class="centMain pt5">
	                    <input id="save_domain_name" name="domain_name" type="text" class="txtput fz15" value="www.jybb.com" disabled="disabled"/>
	                </div>
	                <div class="centMain pt15">渠道名称</div>
	                <div class="centMain pt5">
	                    <input id="save_channel_name" name="channel_name" type="text" class="txtput fz15" placeholder="渠道名称" />
	                </div>
	                
                 	<div class="centMain pt15">渠道编号</div>
	                <div class="centMain pt5">
	                    <input id="save_channel_id" name="channel_id" type="text" class="txtput fz15" placeholder="渠道编号" />
	                </div>
	                
	                <div class="centMain pt15">禁用链接</div>
	                <div class="centMain pt5">
	                    <input id="save_disable_link" name="disable_link" type="text" class="txtput fz15" placeholder="禁用链接" />
	                </div>
	                
	                <div class="centMain pt20 mt10">
	                    <div class="w85 fl">
	                        <button class="btn" onclick="saveDisable();">确认</button>
	                    </div>
	                    <div class="w65 fr"><button id="save_disable_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
	                </div>
	            </div>
	        </div>
	    </div>
	    
	    <!-- 修改推广 -->
	    <div id="update_disable_div" class="newmoent display_none">
	        <div class="ment_ucount">
	            <div class="centMain metilte">
	                <span>修改推广</span>
	            </div>
	            <div class="centMain pl15 pr15">
	            	<div id="update_disable_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
	                
	                <div class="centMain pt15">推广网站</div>
	                <div class="centMain pt5">
	                	<select id="update_website_name" name="website_name" class="txtput fz15" onchange="changeUpdateWebsite(this);">
	                		
	                	</select>
	                </div>
	                <div class="centMain pt15">域名</div>
	                <div class="centMain pt5">
	                    <input id="update_domain_name" name="domain_name" type="text" class="txtput fz15" value="www.jybb.com" disabled="disabled"/>
	                </div>
	                <div class="centMain pt15">渠道名称</div>
	                <div class="centMain pt5">
	                    <input id="update_channel_name" name="channel_name" type="text" class="txtput fz15" placeholder="渠道名称" />
	                </div>
	                
                 	<div class="centMain pt15">渠道编号</div>
	                <div class="centMain pt5">
	                    <input id="update_channel_id" name="channel_id" type="text" class="txtput fz15" placeholder="渠道编号" />
	                </div>
	                
	                <div class="centMain pt15">禁用链接</div>
	                <div class="centMain pt5">
	                    <input id="update_disable_link" name="disable_link" type="text" class="txtput fz15" placeholder="禁用链接" />
	                </div>
	                
	                <div class="centMain pt20 mt10">
	                    <div class="w85 fl">
	                        <button class="btn" onclick="updateDisable();">确认</button>
	                    </div>
	                    <div class="w65 fr"><button id="update_disable_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
	                </div>
	            </div>
	        </div>
	    </div>
</body>
</html>


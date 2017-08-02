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
<title>用户管理</title>

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
.role_table{
	width:100%;
}
.role_table td{
	width:50%;
}
.role_table label{
	margin-right: 20px;
}
</style>

<script type="text/javascript" language="javascript">

var base = document.getElementsByTagName("base")[0].getAttribute("href");

var save_username_flag=false;	
var save_password_flag=false;
var save_channel_id_flag=false;

var update_username_flag=false;	
var update_password_flag=false;
var update_channel_id_flag=false;

$(function(){
	
	$.ajax({
		type:"post",
		url:"admin/getRoles.do",
		success:function(msg){
			var content="";
			for (var int = 0; int < msg.roles.length; int+=2) {
				content+="<tr>";
				for (var int2 = int; int2 < int+2; int2++) {
					if(msg.roles.length==int2){
						break;
					}
					content+="<td><label for='"+msg.roles[int2].role_name+"'>"+msg.roles[int2].role_name+"</label><input id='"+msg.roles[int2].role_name+"' type='checkbox' value='"+msg.roles[int2].id+"' ></td>";
					
				}
				content+="</tr>";
			}
			
			$("#save_role_table").append(content);
			$("#update_role_table").append(content);
		},
		dataType:"json"
	});
	
	$(".page_a").click(function(){
		var style=$(this).attr("style");
		if(style!=null){
			return;
		}
		var text=$(this).text();
		window.location.href=base+"admin/toAdmin.do?"
		+"&page="+text;
	});
	
	$("#down_page").click(function(){
		var totalPage=$("#totalPage").val();
		var page=$("#page").val();
		
		if(eval(page)<eval(totalPage)){
			page=eval(page)+1;
			window.location.href=base+"admin/toAdmin.do?"
			+"&page="+page;
		}
	});
	
	$("#up_page").click(function(){
		var page=$("#page").val();
		if(page>1){
			page=eval(page)-1;
			window.location.href=base+"admin/toAdmin.do?"
			+"&page="+page;
		}
	});
	
	$("#start_page").click(function(){
		window.location.href=base+"admin/toAdmin.do?"
		+"&page="+1;
	});
	
	$("#end_page").click(function(){
		var totalPage=$("#totalPage").val();
		window.location.href=base+"admin/toAdmin.do?"
		+"&page="+totalPage;
	});

		
		
	$("#save_username").blur(function(){
		$("#save_admin_error_msg").html("&nbsp;");
		var save_username=$("#save_username").val();
		save_username=$.trim(save_username);
		if(save_username==null||save_username==""){
			$("#save_admin_error_msg").text("用户名不能为空！");
			save_username_flag=false;
			return;
		}
		
		$.ajax({
			type:"post",
			url:"admin/checkAdminByUsername.do",
			data:{"username":save_username},
			async:false,
			success:function(msg){
				if(msg=="no"){
					$("#save_admin_error_msg").text("用户名称已经存在！");
					save_username_flag=false;
				}else{
					save_username_flag=true;
				}
			},
			error:function(msg){
				$("#save_admin_error_msg").text("检查用户名称错误！");
				save_username_flag=false;
			}
		});
	});
		
	$("#save_password").blur(function(){
		$("#save_admin_error_msg").html("&nbsp;");
		var save_password=$("#save_password").val();
		save_password=$.trim(save_password);
		if(save_password==null||save_password==""){
			$("#save_admin_error_msg").text("密码不能为空！");
			save_password_flag=false;
			return;
		}else{
			save_password_flag=true;
		}
	});
	
	$("#save_channel_id").blur(function(){
		$("#save_admin_error_msg").html("&nbsp;");
		var channel_id=$("#save_channel_id").val();
		channel_id=$.trim(channel_id);
		if(channel_id==null||channel_id==""){
			save_channel_id_flag=true;
			return;
		}else{
			$.ajax({
				type:"post",
				url:"channel/checkChannelId.do",
				data:{"channel_id":channel_id},
				async:false,
				success:function(msg){
					if(msg=="no"){
						save_channel_id_flag=true;
					}else{
						$("#save_admin_error_msg").text("渠道编号不存在！");
						save_channel_id_flag=false;
					}
				},
				error:function(msg){
					$("#save_admin_error_msg").text("检查渠道编号错误！");
					save_channel_id_flag=false;
				}
			});
		}
	});
	
	$("#update_username").blur(function(){
		
		$("#update_admin_error_msg").html("&nbsp;");
		
		var update_username=$("#update_username").val();
		update_username=$.trim(update_username);
		if(update_username==null||update_username==""){
			$("#update_admin_error_msg").text("用户名不能为空！");
			update_username_flag=false;
			return;
		}
		
		
		$.ajax({
			type:"post",
			url:"admin/checkAdminByUsername.do",
			data:{"username":update_username,"admin_id":updateAdmin_id},
			async:false,
			success:function(msg){
				if(msg=="no"){
					$("#update_admin_error_msg").text("用户名称已经存在！");
					update_username_flag=false;
				}else{
					update_username_flag=true;
				}
			},
			error:function(msg){
				$("#update_admin_error_msg").text("检查用户名称错误！");
				update_username_flag=false;
			}
		});
		
	});
	
	$("#update_password").blur(function(){
		$("#update_admin_error_msg").html("&nbsp");
		var update_password=$("#update_password").val();
		update_password=$.trim(update_password);
		if(update_password==null||update_password==""){
			$("#update_admin_error_msg").text("密码不能为空！");
			update_password_flag=false;
			return;
		}else{
			update_password_flag=true;
		}
	});
	
	$("#update_channel_id").blur(function(){
		$("#update_admin_error_msg").html("&nbsp;");
		var channel_id=$("#update_channel_id").val();
		channel_id=$.trim(channel_id);
		if(channel_id==null||channel_id==""){
			update_channel_id_flag=true;
			return;
		}else{
			$.ajax({
				type:"post",
				url:"channel/checkChannelId.do",
				data:{"channel_id":channel_id},
				async:false,
				success:function(msg){
					if(msg=="no"){
						update_channel_id_flag=true;
					}else{
						$("#update_admin_error_msg").text("渠道编号不存在！");
						update_channel_id_flag=false;
					}
				},
				error:function(msg){
					$("#update_admin_error_msg").text("检查渠道编号错误！");
					update_channel_id_flag=false;
				}
			});
		}
	});
});
	
	function newAdmin(){
		
		$("#save_admin_div").removeClass("display_none");
		$("#save_admin_error_msg").html("&nbsp;");
		
		$("#save_username").val("");
		$("#save_password").val("");
		$("#save_channel_id").val("");
		
		$("#save_role_table tr td").find(":input").each(function(index,e){
			$(this).prop("checked",false);
		});
	}
	
	
	function saveAdmin(){
		$("#save_username").blur();
		if(!save_username_flag){
			$("#save_username").blur();
			return false;
		}
		
		if(!save_password_flag){
			$("#save_password").blur();
			return false;
		}
		
		if(!save_channel_id_flag){
			$("#save_channel_id").blur();
			return false;
		}
		
		var arr1 = new Array();
		var a=0;
		$("#save_role_table tr td").find(":input").each(function(index,e){
			if($(this).is(":checked")){
				arr1[a]=$(this).val();
				a++;
			}
		});
		
		var save_username=$("#save_username").val();
		var save_password=$("#save_password").val();
		var channel_id=$("#save_channel_id").val();
		$("#saveAdmin_button").attr("disabled",true);
		$.ajax({
			type:"post",
			url:"admin/addAdmin.do",
			traditional: true,
			data:{"username":save_username,"password":save_password,"channel_id":channel_id,"roles":arr1},
			success:function(msg){
				if (msg){
					$("#save_admin_error_msg").text("添加成功！");
					$("#saveAdmin_button").attr("disabled",false);
				} else {
					$("#save_admin_error_msg").text("添加失败！");
					$("#saveAdmin_button").attr("disabled",false);
				}
			},
			error:function(msg){
				$("#save_admin_error_msg").text("添加失败！");
				$("#saveAdmin_button").attr("disabled",false);
			}
		});
	}
	
	var updateAdmin_id=null;
	function editAdmin(id,username,password,channel_id){
		updateAdmin_id=id;
		$("#update_admin_div").removeClass("display_none");
		$("#update_admin_error_msg").html("&nbsp;");
		$("#update_username").val(username);
		$("#update_password").val(password);
		$("#update_channel_id").val(channel_id);
		$("#update_role_table tr td").find(":input").each(function(index,e){
			$(e).prop("checked", false);
			
		});
		$.ajax({
			type:"post",
			url:"admin/getRolesForUpdate.do?admin_id="+id,
			success:function(msg){
				$("#update_role_table tr td").find(":input").each(function(index,e){
					for (var int3 = 0; int3 < msg.map2.roles2.length; int3++) {
						if(msg.map2.roles2[int3]==null){
							continue;
						}
						if(msg.map2.roles2[int3].id==$(this).val()){
							$(e).prop("checked", true);
							break;
						}
					}
				});
				
			},
			error:function(msg){
				$("#update_admin_error_msg").text("用户数据加载失败！");
			},
			dataType:"json"
		});
		
	}
	
	function updateAdmin(){
		$("#update_username").blur();
		if(!update_username_flag){
			return false;
		}
		$("#update_password").blur();
		if(!update_password_flag){
			return false;
		}
		
		$("#update_channel_id").blur();
		if(!update_channel_id_flag){
			return false;
		}
		
		var arr1 = new Array();
		var a=0;
		$("#update_role_table tr td").find(":input").each(function(index,e){
			if($(this).is(":checked")){
				arr1[a]=$(this).val();
				a++;
			}
		});
		var update_username=$("#update_username").val();
		var update_password=$("#update_password").val();
		var update_channel_id=$("#update_channel_id").val();
		$.ajax({
			type:"post",
			url:"admin/updateAdmin.do",
			traditional: true,
			data:{"id":updateAdmin_id,"username":update_username,"password":update_password,"channel_id":update_channel_id,"roles":arr1},
			success:function(msg){
				if (msg){
					$("#update_admin_error_msg").text("修改成功！");
				} else {
					$("#update_admin_error_msg").text("修改失败！");
				}
			},
			error:function(msg){
				$("#update_admin_error_msg").text("修改失败，请联系管理员！");
			}
		});
		
	}
	
	
	function removeAdmin(id){
		if(confirm("确认删除该用户?")){
			var page=$("#page").val();
			$.ajax({
				type:"post",
				url:"admin/deleteAdmin.do",
				data:{"id":id},
				dataType:"json",
				success:function(msg){
					if (msg){
						window.location.href=base+"admin/toAdmin.do?page="+page;
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
	function refreshWebsite(){
		var page=$("#page").val();
		window.location.href="<%=basePath%>admin/toAdmin.do?page="+page;
	}
</script>
</head>

<body style="padding: 0px; margin: 0px;" class="pl15">

	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">用户管理 </span>
    </div>
    <!-- 按钮 -->
    <div class="centMain mt15">
    	<cc:ps privileges="${privileges}" privilege="增加,用户管理">
        <a class="itms" onclick="newAdmin();"><i class="icon icon-614 fz12"></i>添加用户</a>
        <a class="itms" onclick="refreshWebsite()">刷 &nbsp; 新</a>
        </cc:ps>
    </div>
    
	<!-- 列表操作 -->
	<div class="centMain">
		<!-- 列表 -->
		<table class="table mt15">
			<thead>
				<tr>
					<th style="width: 10%">编号</th>
					<th style="width: 25%">用户名</th>
					<th style="width: 25%">密码</th>
					<th style="width: 20%">渠道编号</th>
					<th style="width: 20%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${admins}" var="admin">
					<tr>
						<td style="width: 10%">${admin.id}</td>
						<td style="width: 25%">${admin.username}</td>
						<td style="width: 25%">${admin.password}</td>
						<td style="width: 25%">${admin.channel_id}</td>
						<td style="width: 20%">
							<cc:ps privileges="${privileges}" privilege="修改,用户管理">
							<a class="sckre" onclick="editAdmin(${admin.id},'${admin.username}','${admin.password}','${admin.channel_id}');">
								<i class="icon icon-60a"></i>修改
							</a>
							</cc:ps>
							<cc:ps privileges="${privileges}" privilege="删除,用户管理">
							<a class="sckre" onclick="removeAdmin(${admin.id});">
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
	
	<!-- 保存用户 -->
    <div id="save_admin_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存用户</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="save_admin_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">用户名</div>
                <div class="centMain pt5">
                    <input id="save_username" name="save_username" value="" type="text" class="txtput fz15" placeholder="用户名" />
                </div>
                <div class="centMain pt15">密码</div>
                <div class="centMain pt5">
                    <input id="save_password" name="save_password" value="" type="password" class="txtput fz15" placeholder="密码" />
                </div>
                <div class="centMain pt15">渠道编号</div>
                <div class="centMain pt5">
                	<input id="save_channel_id" name="save_channel_id" value="" type="text" class="txtput fz15" placeholder="渠道编号" />
                </div>
                
                <div class="centMain pt15">角色</div>
                <div class="centMain pt5">
                	<table id="save_role_table" class="role_table">
                	</table>
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="saveAdmin_button" class="btn" onclick="saveAdmin();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="save_admin_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 修改用户 -->
    <div id="update_admin_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>修改用户</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="update_admin_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">用户名</div>
                <div class="centMain pt5">
                    <input id="update_username" name="update_username" type="text" class="txtput fz15" placeholder="用户名" />
                </div>
                <div class="centMain pt15">密码</div>
                <div class="centMain pt5">
                    <input id="update_password" name="update_password" type="password" class="txtput fz15" placeholder="密码" />
                </div>
                <div class="centMain pt15">渠道编号</div>
                <div class="centMain pt5">
                	<input id="update_channel_id" name="update_channel_id" type="text" class="txtput fz15" placeholder="渠道编号" />
                </div>
                <div class="centMain pt15">角色</div>
                <div class="centMain pt5">
                	<table id="update_role_table" class="role_table">
                		
                	</table>
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="updateAdmin_button" class="btn" onclick="updateAdmin();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="update_admin_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
		
</body>
</html>


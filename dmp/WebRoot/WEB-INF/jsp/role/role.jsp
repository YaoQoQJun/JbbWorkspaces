<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="cc" uri="/mytaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<title>角色管理</title>
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
<script src="js/easyui-lang-zh_CN.js"></script>

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
.model_table{
	width:100%;
}
.model_table label{
	margin-right: 20px;
}
</style>


<script type="text/javascript" language="javascript">
var base = document.getElementsByTagName("base")[0].getAttribute("href");

var save_role_name_flag=false;
var update_role_name_flag=false;

	$(function(){
		$.ajax({
			type:"post",
			url:"role/getModelPrivilege.do",
			success:function(msg){
				var content="";
				$.each(msg,function(idx,item){
				   content+="<tr><td style='width: 25%;' >"+idx+"</td>";
				   $.each(item,function(i,t){
					   content+="<td style='width: 15%;'>"+t+"<input type='checkbox' value='"+t+"' ></td>";
						
				   });
				   content+="</tr>";
				});
				$("#save_model_table").append(content);
				$("#update_model_table").append(content);
			},
			dataType:"json"
		});
		
		
		$(".page_a").click(function(){
			var style=$(this).attr("style");
			if(style!=null){
				return;
			}
			var text=$(this).text();
			
			window.location.href=base+"role/toRole.do?"
			+"&page="+text;
		});
		
		$("#down_page").click(function(){
			var totalPage=$("#totalPage").val();
			var page=$("#page").val();
			
			if(eval(page)<eval(totalPage)){
				page=eval(page)+1;
				window.location.href=base+"role/toRole.do?"
				+"&page="+page;
			}
		});
		
		$("#up_page").click(function(){
			var page=$("#page").val();
			
			
			if(page>1){
				page=eval(page)-1;
				window.location.href=base+"role/toRole.do?"
				+"&page="+page;
			}
		});
		
		$("#start_page").click(function(){
			
			
			window.location.href=base+"role/toRole.do?"
			+"&page="+1;
		});
		
		$("#end_page").click(function(){
			
			var totalPage=$("#totalPage").val();
			
			window.location.href=base+"role/toRole.do?"
			+"&page="+totalPage;
		});
		
		
		
		$("#save_role_name").blur(function(){
			$("#save_role_error_msg").html("&nbsp;");
			var role_name=$("#save_role_name").val();
			role_name=$.trim(role_name);
			if(role_name==null||role_name==""){
				$("#save_role_error_msg").text("角色名称不能为空！");
				save_role_name_flag=false;
				return;
			}
			
			$.ajax({
				type:"post",
				url:"role/checkRoleByRoleName.do",
				data:{"role_name":role_name},
				async:false,
				success:function(msg){
					if(msg=="no"){
						$("#save_role_error_msg").text("角色名称已经存在！");
						save_role_name_flag=false;
					}else{
						save_role_name_flag=true;
					}
				},
				error:function(){
					$("#save_role_error_msg").text("检查角色名称错误！");
					save_role_name_flag=false;
				}
			});
		});
		
		
		$("#update_role_name").blur(function(){
			
			var role_name=$("#update_role_name").val();
			
			role_name=$.trim(role_name);
			if(role_name==null||role_name==""){
				$("#update_role_error_msg").text("角色名称不能为空！");
				update_role_name_flag=false;
				return;
			}
			
			$.ajax({
				type:"post",
				url:"role/checkRoleByRoleName.do",
				data:{"role_name":role_name,"role_id":updateRole_id},
				async:false,
				success:function(msg){
					if(msg=="no"){
						$("#update_role_error_msg").text("角色名称已经存在！");
						update_role_name_flag=false;
					}else{
						update_role_name_flag=true;
					}
				},
				error:function(msg){
					$("#update_role_error_msg").text("检查角色名称错误，请联系管理员！");
					update_role_name_flag=false;
				}
			});
		});
		
		
	});
	
	function newRole(){
		$("#save_role_div").removeClass("display_none");
		$("#save_role_error_msg").html("&nbsp;");
		$("#save_role_name").val("");
		$("#save_model_table tr td").find(":input").each(function(index,e){
			$(this).prop("checked",false);
		});
	}
	
	function saveRole(){
		$("#save_role_name").blur();
		if(!save_role_name_flag){
			$("#save_role_name").blur();
			return false;
		}
		$("#saveRole_button").attr("disabled",true);
		var role_name=$("#save_role_name").val();
		
		var arr1 = new Array();
		var model="";
		$("#save_model_table tr").each(function(index,e){
			
			$(this).find("td").each(function(j,p){
				if(j==0){
					model=$(this).text();
					return true;
				}
				if($(this).find(":input").is(":checked")){
					model+="-"+$(this).find(":input").val();
				}
			});
			arr1[index]=model;
		});
		
		$.ajax({
			type:"post",
			url:"role/addRole.do",
			traditional: true,
			data:{"role_name":role_name,"model":arr1},
			success:function(msg){
				if (msg){
					$("#save_role_error_msg").html("添加成功！");
					$("#saveRole_button").attr("disabled",false);
				} else {
					$("#save_role_error_msg").html("添加失败！");
					$("#saveRole_button").attr("disabled",false);
				}
			},
			error:function(msg){
				$("#save_role_error_msg").html("添加失败，请联系管理员！");
				$("#saveRole_button").attr("disabled",false);
			}
		});
	}
	
	
	var updateRole_id=null;
	function editRole(id,role_name){
		updateRole_id=id;
		$("#update_role_div").removeClass("display_none");
		$("#update_role_error_msg").html("&nbsp;");
		$("#update_role_name").val(role_name);
		$("#update_model_table tr td").find(":input").each(function(index,e){
			$(e).prop("checked", false);
		});
		
		$.ajax({
			type:"post",
			url:"role/getModelPrivilegeByRole_id.do?role_id="+id,
			success:function(msg){
				$.each(msg.m2,function(idx,item){
				 	
				 	$("#update_model_table tr").each(function(tr_index,tr_e){
				 		var a=null;
						$(this).find("td").each(function(td_index,td_e){
							if(td_index==0&&idx==$(this).text()){
								a=$(this).text();					
							}
							for (var int = 0; int < item.length; int++) {
								if(item[int]==$(this).text()&&idx==a){
									$(this).find("input").prop("checked",true);
								}
							}
						});
					});
				 	
				});
				
			},
			error:function(msg){
				$("#update_role_error_msg").html("获取信息失败，请联系管理员！");
			},
			dataType:"json"
		});
		
	}
	
	
	function updateRole(){
		
		$("#update_role_name").blur();
		if(!update_role_name_flag){
			return false;
		}
		
		var arr1 = new Array();
		var model="";
		$("#update_model_table tr").each(function(index,e){
			
			$(this).find("td").each(function(j,p){
				if(j==0){
					model=$(this).text();
					return true;
				}
				if($(this).find(":input").is(":checked")){
					model+="-"+$(this).find(":input").val();
				}
			});
			arr1[index]=model;
		});
		
		var role_name=$("#update_role_name").val();
		
		$.ajax({
			type:"post",
			url:"role/updateRole.do",
			traditional: true,
			data:{"role_name":role_name,"role_id":updateRole_id,"model":arr1},
			success:function(msg){
				if (msg){
					$("#update_role_error_msg").html("修改成功！");
				} else {
					$("#update_role_error_msg").html("修改失败！");
				}
			},
			error:function(msg){
				$("#update_role_error_msg").html("修改失败，请联系管理员！");
			}
		});
	}
	
	
	function removeRole(id){
		if (confirm("确认删除该角色?")){
			$.ajax({
				type:"post",
				url:"role/deleteRole.do",
				data:{"role_id":id},
				success:function(msg){
					if (msg){
						$.messager.show({	
							title: "信息",
							msg: "删除成功！"
						});
						var page=$("#page").val();
						window.location.href="<%=basePath%>role/toRole.do?page="+page;
					} else {
						
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
				},
				dataType:"json"
			});
		}
	}
	
	function Btn_Close(obj) {
        $(obj).parents(".newmoent").addClass("display_none");
    }
	function refreshWebsite(){
		var page=$("#page").val();
		window.location.href="<%=basePath%>role/toRole.do?page="+page;
	}
	
</script>
</head>

<body style="padding: 0px; margin: 0px;" class="pl15">

	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">角色管理 </span>
    </div>
    <!-- 按钮 -->
    <div class="centMain mt15">
    	<cc:ps privileges="${privileges}" privilege="增加,角色管理">
        <a class="itms" onclick="newRole();"><i class="icon icon-614 fz12"></i>添加角色</a>
        <a class="itms" onclick="refreshWebsite()">刷 &nbsp; 新</a>
        </cc:ps>
    </div>
    
    <div class="centMain">
		<!-- 列表 -->
		<table class="table mt15">
			<thead>
				<tr>
					<th style="width: 10%">编号</th>
					<th style="width: 70%">角色名称</th>
					<th style="width: 20%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${roles}" var="role">
					<tr>
						<td style="width: 10%">${role.id}</td>
						<td style="width: 70%">${role.role_name}</td>
						<td style="width: 20%">
							<cc:ps privileges="${privileges}" privilege="修改,角色管理">
							<a class="sckre" onclick="editRole(${role.id},'${role.role_name}');">
								<i class="icon icon-60a"></i>修改
							</a>
							</cc:ps>
							<cc:ps privileges="${privileges}" privilege="删除,角色管理">
							<a class="sckre" onclick="removeRole(${role.id});">
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
		
	<!-- 保存角色 -->
    <div id="save_role_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存角色</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="save_role_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">用户名</div>
                <div class="centMain pt5">
                    <input id="save_role_name" name="role_name" type="text" class="txtput fz15" placeholder="角色名称" />
                </div>
                <div class="centMain pt15">模块权限</div>
                <div class="centMain pt5">
                	<table id="save_model_table" class="model_table">
                	</table>
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="saveRole_button" class="btn" onclick="saveRole();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="save_role_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 修改角色 -->
    <div id="update_role_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>修改角色</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="update_role_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">用户名</div>
                <div class="centMain pt5">
                    <input id="update_role_name" name="role_name" type="text" class="txtput fz15" placeholder="角色名称" />
                </div>
                <div class="centMain pt15">模块权限</div>
                <div class="centMain pt5">
                	<table id="update_model_table" class="model_table">
                	</table>
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="updateRole_button" class="btn" onclick="updateRole();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="update_role_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
	
</body>
</html>


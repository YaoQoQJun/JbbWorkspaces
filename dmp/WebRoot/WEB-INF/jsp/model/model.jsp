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
<title>模块管理</title>

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

.privilege_table{
	width:100%;
}
.privilege_table tr td{
	width:50%;
}
.privilege_table label{
	margin-right: 20px;
}
</style>

<script type="text/javascript" language="javascript">
	var base = document.getElementsByTagName("base")[0].getAttribute("href");
	
	var save_model_name_flag=false;	
	var update_model_name_flag=false;	
	
	
	$(function(){
		
		$(".page_a").click(function(){
			var style=$(this).attr("style");
			if(style!=null){
				return;
			}
			var text=$(this).text();
			
			window.location.href=base+"model/toModel.do?"
			+"&page="+text;
		});
		
		$("#down_page").click(function(){
			var totalPage=$("#totalPage").val();
			var page=$("#page").val();
			
			if(eval(page)<eval(totalPage)){
				page=eval(page)+1;
				window.location.href=base+"model/toModel.do?"
				+"&page="+page;
			}
		});
		
		$("#up_page").click(function(){
			var page=$("#page").val();
			
			
			if(page>1){
				page=eval(page)-1;
				window.location.href=base+"model/toModel.do?"
				+"&page="+page;
			}
		});
		
		$("#start_page").click(function(){
			
			
			window.location.href=base+"model/toModel.do?"
			+"&page="+1;
		});
		
		$("#end_page").click(function(){
			
			var totalPage=$("#totalPage").val();
			
			window.location.href=base+"model/toModel.do?"
			+"&page="+totalPage;
		});
		
		
		$("#save_model_name").blur(function(){
			var save_model_name=$("#save_model_name").val();
			save_model_name=$.trim(save_model_name);
			if(save_model_name==null||save_model_name==""){
				save_model_name_flag=false;
				return;
			}else{
				save_model_name_flag=true;
			}
		});
		
		$("#update_model_name").blur(function(){
			var update_model_name=$("#update_model_name").val();
			update_model_name=$.trim(update_model_name);
			if(update_model_name==null||update_model_name==""){
				update_model_name_flag=false;
				return;
			}else{
				update_model_name_flag=true;
			}
		});
		
	});
	
	function newModel(){
		$("#save_model_div").removeClass("display_none");
		$("#save_model_error_msg").html("&nbsp;");
		$("#save_model_name").val("");
		$("#save_model_url").val("");
		$("#save_privilege_table tr td").find(":input").each(function(index,e){
			$(this).prop("checked",false);
		});
	}
	
	function saveModel(){
		
		$("#save_model_name").blur();
		if(!save_model_name_flag){
			$("#save_model_name").blur();
			return false;
		}
		url = "model/addModel.do";
		$("#save_fm").form("submit",{
			url: url,
			onSubmit: function(){
				return $(this).form("validate");
			},
			success: function(msg){
				if (msg){
					$("#save_model_error_msg").text("添加成功！");
				} else {
					$("#save_model_error_msg").text("添加失败！");
				}
			},
			error:function(msg){
				$("#save_model_error_msg").text("添加失败！");
			}
		});
	}
	
	var updateModelId="";
	function editModel(model_id,model_name,model_url){
		
		$("#update_model_div").removeClass("display_none");
		$("#update_model_error_msg").html("&nbsp;");
		$("#update_model_name").val(model_name);
		$("#update_model_url").val(model_url);
		
		updateModelId=model_id;
		$.ajax({
			type:"post",
			url:"model/getPrivileges.do",
			data:{"model_id":model_id},
			success:function(msg){
				$("#access_checkbox").prop("checked",false);
				$("#add_checkbox").prop("checked",false);
				$("#delete_checkbox").prop("checked",false);
				$("#update_checkbox").prop("checked",false);
				for (var int = 0; int < msg.length; int++) {
					if("访问"==msg[int]){
						$("#access_checkbox").prop("checked",true);
					}else if("增加"==msg[int]){
						$("#add_checkbox").prop("checked",true);
					}else if("删除"==msg[int]){
						$("#delete_checkbox").prop("checked",true);
					}else if("修改"==msg[int]){
						$("#update_checkbox").prop("checked",true);
					}
				}
			},
			error:function(msg){
				$.messager.show({
					title: "信息",
					msg: "加载失败，请联系管理员！"
				});
			}
		});
		
	}
	function updateModel(){
		
		$("#update_model_name").blur();
		if(!update_model_name_flag){
			return false;
		}
		
		var arr1=new Array();
		var index=0;
		$("#update_model_div input[type='checkbox']").each(function(i,e){ 
			if($(this).is(":checked")){
				arr1[index]=$(this).val();
				index++;
			}
        });
		
		var update_model_name=$("#update_model_name").val();
		var update_model_url=$("#update_model_url").val();
		
		$.ajax({
			type:"post",
			url:"model/updateModel.do",
			traditional: true,
			data:{
				"id":updateModelId,
				"model_name":update_model_name,
				"model_url":update_model_url,
				"privilege_name":arr1},
			async:false,
			success:function(msg){
				if (msg=="true"){
					$("#update_model_error_msg").text("修改成功！");
				} else {
					$("#update_model_error_msg").text("修改失败！");
				}
			},
			error:function(msg){
				$("#update_model_error_msg").text("修改失败！");
			}
			
		});
	}
	
	
	function removeModel(model_id){
		if (confirm("确认删除该模块?")){
			var page=$("#page").val();
			$.ajax({
				type:"post",
				data:{"model_id":model_id},
				url:"model/deleteModel.do",
				dataType:"json",
				success:function(msg){
					if (msg){
						window.location.href=base+"model/toModel.do?page="+page;
					} else {
						$.messager.show({	// show error message
							title: "信息",
							msg: "删除失败！"
						});
					}
				},
				error:function(msg){
					$.messager.show({	// show error message
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
		window.location.href="<%=basePath%>model/toModel.do?page="+page;
	}
	
</script>
</head>

<body style="padding: 0px; margin: 0px;" class="pl15">

	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">模块管理 </span>
    </div>
    <!-- 按钮 -->
    <div class="centMain mt15">
    	<cc:ps privileges="${privileges}" privilege="增加,模块管理">
        <a class="itms" onclick="newModel();"><i class="icon icon-614 fz12"></i>添加模块</a>
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
					<th style="width: 35%">模块名称</th>
					<th style="width: 35%">模块地址</th>
					<th style="width: 20%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${models}" var="model">
					<tr>
						<td style="width: 10%">${model.id}</td>
						<td style="width: 35%">${model.model_name}</td>
						<td style="width: 35%">${model.model_url}</td>
						<td style="width: 20%">
							<cc:ps privileges="${privileges}" privilege="修改,模块管理">
							<a class="sckre" onclick="editModel(${model.id},'${model.model_name}','${model.model_url}');">
								<i class="icon icon-60a"></i>修改
							</a>
							</cc:ps>
							<cc:ps privileges="${privileges}" privilege="删除,模块管理">
							<a class="sckre" onclick="removeModel(${model.id});">
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
    <div id="save_model_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存用户</span>
            </div>
            <div class="centMain pl15 pr15">
            	<form id="save_fm" method="post">
            	<div id="save_model_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">模块名称</div>
                <div class="centMain pt5">
                    <input id="save_model_name" name="model_name" type="text" class="txtput fz15" placeholder="模块名称" />
                </div>
                <div class="centMain pt15">模块地址</div>
                <div class="centMain pt5">
                    <input id="save_model_url" name="model_url" type="text" class="txtput fz15" placeholder="模块地址" />
                </div>
                
                <div class="centMain pt15">权限</div>
                <div class="centMain pt5">
	                	<table id="save_privilege_table" class="privilege_table">
		               		<tr>
								<td>
									<label for="privilege_1">访问</label>&nbsp;<input type="checkbox" id="privilege_1" name="privilege_name" value="访问">
								</td>
								<td>
									<label for="privilege_2">删除</label>&nbsp;<input type="checkbox" id="privilege_2" name="privilege_name" value="删除">
								</td>
							</tr>
							<tr>
								<td>
									<label for="privilege_3">增加</label>&nbsp;<input type="checkbox" id="privilege_3" name="privilege_name" value="增加">
								</td>
								<td>
									<label for="privilege_4">修改</label>&nbsp;<input type="checkbox" id="privilege_4" name="privilege_name" value="修改">
								</td>
							</tr>
	                	</table>
                </div>
                </form>
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="saveModel_button" class="btn" onclick="saveModel();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="save_model_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
	
	<!-- 修改用户 -->
    <div id="update_model_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存用户</span>
            </div>
            <div class="centMain pl15 pr15">
            	<form id="update_fm" method="post">
            	<div id="update_model_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">模块名称</div>
                <div class="centMain pt5">
                    <input id="update_model_name" name="model_name" type="text" class="txtput fz15" placeholder="模块名称" />
                </div>
                <div class="centMain pt15">模块地址</div>
                <div class="centMain pt5">
                    <input id="update_model_url" name="model_url" type="text" class="txtput fz15" placeholder="模块地址" />
                </div>
                
                <div class="centMain pt15">权限</div>
                <div class="centMain pt5">
	                	<table id="update_privilege_table" class="privilege_table">
		               		<tr>
								<td>
									<label for="privilege_1">访问</label>&nbsp;<input type="checkbox" id="access_checkbox" name="privilege_name" value="访问">
								</td>
								<td>
									<label for="privilege_2">删除</label>&nbsp;<input type="checkbox" id="delete_checkbox" name="privilege_name" value="删除">
								</td>
							</tr>
							<tr>
								<td>
									<label for="privilege_3">增加</label>&nbsp;<input type="checkbox" id="add_checkbox" name="privilege_name" value="增加">
								</td>
								<td>
									<label for="privilege_4">修改</label>&nbsp;<input type="checkbox" id="update_checkbox" name="privilege_name" value="修改">
								</td>
							</tr>
	                	</table>
                </div>
                </form>
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button id="updateModel_button" class="btn" onclick="updateModel();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="update_model_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>


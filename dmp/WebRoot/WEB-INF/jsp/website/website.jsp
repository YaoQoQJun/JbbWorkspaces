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
</style>

<script type="text/javascript">

	var save_name_flag=false;	
	var save_domain_name_flag=false;
	var save_code_flag=false;
	
	var update_name_flag=false;
	var update_domain_name_flag=false;
	var update_code_flag=false;
	
	
	$(function(){
		
		
		var base = document.getElementsByTagName("base")[0].getAttribute("href");
    	
		$(".page_a").click(function(){
			var style=$(this).attr("style");
			if(style!=null){
				return;
			}
			var text=$(this).text();
			
			window.location.href=base+"website/toWebsite.do?"
			+"&page="+text;
		});
		
		$("#down_page").click(function(){
			var totalPage=$("#totalPage").val();
			var page=$("#page").val();
			
			if(eval(page)<eval(totalPage)){
				page=eval(page)+1;
				window.location.href=base+"website/toWebsite.do?"
				+"&page="+page;
			}
		});
		
		$("#up_page").click(function(){
			var page=$("#page").val();
			
			
			if(page>1){
				page=eval(page)-1;
				window.location.href=base+"website/toWebsite.do?"
				+"&page="+page;
			}
		});
		
		$("#start_page").click(function(){
			
			
			window.location.href=base+"website/toWebsite.do?"
			+"&page="+1;
		});
		
		$("#end_page").click(function(){
			
			var totalPage=$("#totalPage").val();
			
			window.location.href=base+"website/toWebsite.do?"
			+"&page="+totalPage;
		});
		
		
		
		$("#save_name").blur(function(){
     		$("#save_website_error_msg").html("&nbsp;");
     		var name=$(this).val();
     		if(name==null||name==""){
     			$("#save_website_error_msg").text("网站名称不能为空！");
     			save_name_flag=false;
     		}else{
     			save_name_flag=true;
     		}
     	});
		
		$("#save_domain_name").blur(function(){
     		$("#save_website_error_msg").html("&nbsp;");
     		var domain_name=$(this).val();
     		if(domain_name==null||domain_name==""){
     			$("#save_website_error_msg").text("域名不能为空！");
     			save_domain_name_flag=false;
     		}else{
     			save_domain_name_flag=true;
     		}
     	});
		
		$("#save_code").blur(function(){
     		$("#save_website_error_msg").html("&nbsp;");
     		var code=$(this).val();
     		if(code==null||code==""){
     			$("#save_website_error_msg").text("统计代码不能为空！");
     			save_code_flag=false;
     		}else{
     			save_code_flag=true;
     		}
     	});
		
		
		
		
		$("#update_name").blur(function(){
     		$("#update_website_error_msg").html("&nbsp;");
     		var name=$(this).val();
     		if(name==null||name==""){
     			$("#update_website_error_msg").text("网站名称不能为空！");
     			update_name_flag=false;
     		}else{
     			update_name_flag=true;
     		}
     	});
		
		
		$("#update_domain_name").blur(function(){
     		$("#update_website_error_msg").html("&nbsp;");
     		var domain_name=$(this).val();
     		if(domain_name==null||domain_name==""){
     			$("#update_website_error_msg").text("域名不能为空！");
     			update_domain_name_flag=false;
     		}else{
     			update_domain_name_flag=true;
     		}
     	});
		
		$("#update_code").blur(function(){
     		$("#update_website_error_msg").html("&nbsp;");
     		var code=$(this).val();
     		if(code==null||code==""){
     			$("#update_website_error_msg").text("统计代码不能为空！");
     			update_code_flag=false;
     		}else{
     			update_code_flag=true;
     		}
     	});
		
	});
	
	function newWebsite(){
		$("#save_website_div").removeClass("display_none");
		$("#save_name").val("");
		$("#save_domain_name").val("");
		$("#save_code").val("");
	}
	
	function saveWebsite(){
		if(!save_name_flag){
			$("#save_name").blur();
			return false;
		}
		if(!save_domain_name_flag){
			$("#save_domain_name").blur();
			return false;
		}
		if(!save_code_flag){
			$("#save_code").blur();
			return false;
		}
		
		var name=$("#save_name").val();
		var domain_name=$("#save_domain_name").val();
		var code=$("#save_code").val();
		
		$.ajax({
			type:"post",
			url:"website/addWebsite.do",
			data:{
				"name":name,
				"domain_name":domain_name,
				"code":code
				},
			success:function(msg){
				if(msg=="ok"){
					$.messager.show({
						title: "信息",
						msg: "添加成功！"
					});
					$("#save_website_close_button").parents(".newmoent").addClass("display_none");
				}else{
					$.messager.show({
						title: "信息",
						msg: "添加失败！"
					});
				}
				save_name_flag=false;
				save_domain_name_flag=false;
				save_code_flag=false;
			},
			error:function(msg){
				$.messager.show({
					title: "信息",
					msg: "添加失败，请联系管理员！"
				});
				save_name_flag=false;
				save_domain_name_flag=false;
				save_code_flag=false;
			}
		});
	}
	
	var update_website_id=null;
	var code_div_id=null;
	function editWebsite(id,name,domain_name,div_id){
		code_div_id=div_id;
		update_website_id=id;
		$("#update_website_div").removeClass("display_none");
		
		$("#update_name").val(name);
		$("#update_domain_name").val(domain_name);
		$("#update_code").val($("#"+div_id).val());
	}
	
	function updateWebsite(){
		$("#update_name").blur();
		if(!update_name_flag){
			return false;
		}
		$("#update_domain_name").blur();
		if(!update_domain_name_flag){
			return false;
		}
		$("#update_code").blur();
		if(!update_code_flag){
			return false;
		}
		
		var name=$("#update_name").val();
		var domain_name=$("#update_domain_name").val();
		var code=$("#update_code").val();
		
		$.ajax({
			type:"post",
			url:"website/updateWebsite.do",
			data:{
				"id":update_website_id,
				"name":name,
				"domain_name":domain_name,
				"code":code
				},
			success:function(msg){
				if(msg=="ok"){
					$.messager.show({
						title: "信息",
						msg: "修改成功！"
					});
					$("#"+code_div_id).html(code);
					$("#update_website_close_button").parents(".newmoent").addClass("display_none");
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
		var flag=false;
		
		if(state==0){
			flag=confirm("确认"+str2+"? 将同时禁用推广链接！");
		}else{
			flag=confirm("确认"+str2+"?");
		}
		
		if(flag){
			$.ajax({
				type:"post",
				url:"website/updateWebsiteState.do",
				data:{
					"id":id,
					"state":state
					},
				success:function(msg){
					if(msg=="ok"){
						$(e).html("<i class='icon icon-608'></i>&nbsp;"+str1);
						$(e).parent().prev().text(str2)
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
	
	function deleteWebsite(id){
		if(confirm("确认删除？同时会删除推广链接！")){
			$.ajax({
				type:"post",
				url:"website/deleteWebsite.do",
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
	
	function seeWebsiteCode(a){
		$("#website_code_div").removeClass("display_none");
		var str=$(a).next().val();
		$("#code_content").text(str);
	}
	
	
    function Btn_Close(obj) {
        $(obj).parents(".newmoent").addClass("display_none");
    }
	function refreshWebsite(){
		var page=$("#page").val();
		window.location.href="<%=basePath%>website/toWebsite.do?page="+page;
	}
	
	
</script>

</head>
<body class="pl15">
		<div class="centMain bs-bmd" style="margin-top: 20px;">
            <span class="nasrnt">网站管理</span>
        </div>
        <!-- 按钮 -->
        <div class="centMain mt15">
        	<cc:ps privileges="${privileges}" privilege="增加,网站管理">
            <a class="itms" onclick="newWebsite();"><i class="icon icon-614 fz12"></i>添加网站</a>
            <a class="itms" onclick="refreshWebsite()">刷 &nbsp; 新</a>
            </cc:ps>
        </div>

		<!-- 列表操作 -->
		<div class="centMain">
			<!-- 列表 -->
			<table class="table mt15">
				<thead>
					<tr>
						<th style="width: 5%">编号</th>
						<th style="width: 25%">网站名称</th>
						<th style="width: 25%">域名</th>
						<th style="width: 15%">创建时间</th>
						<th style="width: 5%">状态</th>
						<th style="width: 25%"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${websites}" var="website">
						<tr>
							<td>${website.id}</td>
							<td>${website.name}</td>
							<td>
							<a href="${website.domain_name}" style="color:#3071A9;" target="_blank">
							${website.domain_name}
							</a>
							</td>
							<td>
							<fmt:formatDate value="${website.add_time}" pattern="yyyy-MM-dd hh:mm"/>
							</td>
							<td>
								<c:choose>
									<c:when test="${website.state == 0}">
										禁用
									</c:when>
									<c:otherwise>
										启用
									</c:otherwise>
								</c:choose>
								
							</td>
							<td>
								<a class="sckre" onclick="seeWebsiteCode(this);">查看代码</a>
								<input value='${website.code}' style="display:none;"></input>
								<cc:ps privileges="${privileges}" privilege="修改,网站管理">
								<a class="sckre" onclick="changeState(this,${website.id});">
									<i class="icon icon-608"></i>
									<c:choose>
										<c:when test="${website.state == 0}">
											启用
										</c:when>
										<c:otherwise>
											禁用
										</c:otherwise>
									</c:choose>
								</a> 
								
								<a class="sckre" onclick="editWebsite(${website.id},'${website.name}','${website.domain_name}','div_${website.id}');">
									<i class="icon icon-60a"></i>修改
								</a>
								</cc:ps> 
								<cc:ps privileges="${privileges}" privilege="删除,网站管理">
								<a class="sckre" onclick="deleteWebsite(${website.id});">
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
		
	<!-- 保存网站 -->
    <div id="save_website_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存数据项</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="save_website_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">网站名称</div>
                <div class="centMain pt5">
                    <input id="save_name" name="name" type="text" class="txtput fz15" placeholder="网站名称" />
                </div>
                <div class="centMain pt15">域名</div>
                <div class="centMain pt5">
                    <input id="save_domain_name" name="domain_name" type="text" class="txtput fz15" placeholder="域名" />
                </div>
                <div class="centMain pt15">统计代码</div>
                <div class="centMain pt5">
                	<textarea  id="save_code" name="code" class="txtput fz15" style="resize: none;height: 100px;"   placeholder="统计代码"></textarea>
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button class="btn" onclick="saveWebsite();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="save_website_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 修改网站 -->
    <div id="update_website_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存数据项</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="update_website_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">网站名称</div>
                <div class="centMain pt5">
                    <input id="update_name" name="name" type="text" class="txtput fz15" placeholder="网站名称" />
                </div>
                <div class="centMain pt15">域名</div>
                <div class="centMain pt5">
                    <input id="update_domain_name" name="domain_name" type="text" class="txtput fz15" placeholder="域名" />
                </div>
                <div class="centMain pt15">统计代码</div>
                <div class="centMain pt5">
                	<textarea  id="update_code" name="code" class="txtput fz15" style="resize: none;height: 100px;"   placeholder="统计代码"></textarea>
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button class="btn" onclick="updateWebsite();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="update_website_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
    
        <!-- 代码查看 -->
    <div id="website_code_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>代码查看</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="code_content" style="height: 200px;">
            	
            	</div>
                <div class="centMain pt20 mt10">
                	<div class="w85 fl">
                        <input type="button" id="copyButton" class="btn" value="复制"/>
                    </div>
                    <div class="w65 fr"><button class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
        $("#copyButton").zclip({
			path: "js/ZeroClipboard.swf",
			copy: function(){
				return $("#code_content").text();
			},
			afterCopy: function(){
				alert("复制成功!");
			}
		});
        </script>
    </div>
    
    
</body>
</html>


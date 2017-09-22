<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    
    <link href="themes/default/easyui.css" rel="stylesheet" type="text/css" >
	<link href="themes/icon.css" rel="stylesheet" type="text/css" >
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	<link href="css/icon.css" rel="stylesheet" type="text/css" />
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script src="js/load.js" type="text/javascript"></script>
    <script type="text/javascript">
   		var update_password_flag=false;
    	$(function(){
    		
    		$("ul").each(function(index,e){
    			if($(e).has("li").length == 0){
    				$(e).parent().css("display", "none");
    			}
    		});
    		
    		
    		$("#channel_manage_a").click(function(){
    			$("#myFrame").attr("src","channel/toChannel.do");
    		});
    		
    		$("#admin_manage_a").click(function(){
    			$("#myFrame").attr("src","admin/toAdmin.do");
    		});
    		
    		$("#model_manage_a").click(function(){
    			$("#myFrame").attr("src","model/toModel.do");
    		});
    		
    		$("#role_manage_a").click(function(){
    			$("#myFrame").attr("src","role/toRole.do");
    		});
    		
    		$("#software_standard_data").click(function(){
    			$("#myFrame").attr("src","softwareStandard/toSoftwareStandard.do");
    		});
    		
    		$("#hardware_standard_data").click(function(){
    			$("#myFrame").attr("src","hardwareStandard/toHardwareStandard.do");
    		});
    		
    		$("#memberAttribute_standard_data").click(function(){
    			$("#myFrame").attr("src","memberAttributeStandard/toMemberAttributeStandard.do");
    		});
    		
    		
    		$("#behavior_standard_data").click(function(){
    			$("#myFrame").attr("src","behaviorStandard/toBehaviorStandard.do");
    		});
    		
    		$("#website_a").click(function(){
    			$("#myFrame").attr("src","website/toWebsite.do");
    		});
    		
    		$("#extension_a").click(function(){
    			$("#myFrame").attr("src","extension/toExtension.do");
    		});
    		
    		$("#disable_a").click(function(){
    			$("#myFrame").attr("src","disable/toDisable.do");
    		});
    		
    		$("#monitor_a1").click(function(){
    			$("#myFrame").attr("src","monitor/toMonitor.do?state=1");
    		});
    		
    		$("#monitor_a2").click(function(){
    			$("#myFrame").attr("src","monitor/toMonitor.do?state=2");
    		});
    		
    		
    		$("#regStatistics_a").click(function(){
    			$("#myFrame").attr("src","regStatistics/toRegStatistics.do");
    		});
    		
    		
    		$("#index_a").click(function(){
    			$("#myFrame").attr("src","index/toIndex.do");
    		});
    		
    		$("#appIndex_a").click(function(){
    			$("#myFrame").attr("src","appIndex/toAppIndex.do");
    		});
    		
    		
    		$("#appEvent_a").click(function(){
    			$("#myFrame").attr("src","appEvent/toAppEvent.do");
    		});
    		
    		$("#appEventClass_a").click(function(){
    			$("#myFrame").attr("src","appEventClass/toAppEventClass.do");
    		});
    		
    		$("#appEventSet_a").click(function(){
    			$("#myFrame").attr("src","appEventSet/toAppEventSet.do");
    		});
    		
    		$("#appChannel_a").click(function(){
    			$("#myFrame").attr("src","appChannel/toAppChannel.do");
    		});
    		
    		$("#appVersion_a").click(function(){
    			$("#myFrame").attr("src","appVersion/toAppVersion.do");
    		});
    		
    		$("#memberAttribute_a1").click(function(){
    			$("#myFrame").attr("src","memberAttribute/toSexAttribute.do");
    		});
    		
    		$("#memberAttribute_a2").click(function(){
    			$("#myFrame").attr("src","memberAttribute/toAgeAttribute.do");
    		});
    		
    		$("#memberAttribute_a3").click(function(){
    			$("#myFrame").attr("src","memberAttribute/toStateAttribute.do");
    		});
    		
    		$("#memberAttribute_a4").click(function(){
    			$("#myFrame").attr("src","memberAttribute/toLocalAttribute.do");
    		});
    		
    		$("#out_system_a").click(function(){
    			window.location.href="login/out.do";
    		});
    		
    		
    		$("#update_password").blur(function(){
    			$("#update_password_error_msg").html("&nbsp;");
         		var password=$(this).val();
         		if(password==null||password==""){
         			$("#update_password_error_msg").text("密码不能为空！");
         			update_password_flag=false;
         		}else{
         			update_password_flag=true;
         		}
    		});
    		
    	});
    	
    	function editPassword(){
    		$("#update_password_div").removeClass("display_none");
    		$("#update_password").val("");
    	}
    	
    	function Btn_Close(obj) {
   	        $(obj).parents(".newmoent").addClass("display_none");
   	    }
    	
    	function updatePassword(){
    		
    		var password=$("#update_password").val();
    		
    		if(!update_password_flag){
    			$("#update_password").blur();
    			return false;
    		}
    		
    		$.ajax({
    			type:"post",
    			url:"home/updatePassword.do",
    			data:{
    				"password":password
    				},
    			success:function(msg){
    				if(msg=="ok"){
    					$.messager.show({
    						title: "信息",
    						msg: "修改成功！"
    					});
    					$("#update_password_close_button").parents(".newmoent").addClass("display_none");
    				}else{
    					$.messager.show({
    						title: "信息",
    						msg: "修改失败！"
    					});
    				}
    				update_password_flag=false;
    			},
    			error:function(msg){
    				$.messager.show({
    					title: "信息",
    					msg: "修改失败，请联系管理员！"
    				});
    				update_password_flag=false;
    			}
    		});
    	}
    </script>
    <title>后台首页</title>
  </head>
  <body>
    <div id="cc" class="easyui-layout" data-options="fit:true">
    
    	<div class="nav_top">
	        <a id="media_first_001" class="media_first_001"><i class="icon icon-61b"></i></a>
	        <div class="navlogo"></div>
	        <div class="nav_item">
	            <div class="fr nav_item_user">
	                <a class="btn_a_user">
	                    <img src="images/tx.jpg" /><font>${admin.username}</font>
	                </a>
	                <div class="uptch">
	                    <div class="upsjx"></div>
	                    <ul>
	                        <li><a href="javascript:void(0)" onclick="editPassword();">修改密码</a></li>
	                        <li class="orth"><a id="out_system_a" href="javascript:void(0);">退出</a></li>
	                    </ul>
	                </div>
	            </div>
	        </div>
	    </div>
	    
	    
	    <div id="nav_left" class="nav_left">
	    
	        <ul id="navleftitem" class="items">
	        	<li data-id="1">
	        		<a id="index_a" href="javascript:void(0);"><i class="icon icon icon-60f"></i>网站概况</a>
	        	</li>
	        	
        		<li data-id="1">
	        		<a id="appIndex_a" href="javascript:void(0);"><i class="icon icon icon-60f"></i>APP概况</a>
	        	</li>
	        	
	        	<li data-id="1">
	        		<a id="appEvent_a" href="javascript:void(0);"><i class="icon icon icon-60f"></i>APP事件</a>
	        	</li>
	        	
	        	<li data-id="1">
	        		<a id="regStatistics_a" href="javascript:void(0);"><i class="icon icon icon-60f"></i>注册统计</a>
	        	</li>
	        	
	        	<cc:ps privileges="${privileges}" privilege="访问,用户属性">
	        	<li data-id = "4">
	        		<a class="depth" id="memberAttribute_a" href="javascript:void(0);" onclick="Openthis(this)"><i class="icon icon-612"></i>用户属性</a>
	        		<ul>
	        			<li>
	        				<a id="memberAttribute_a1" href="javascript:void(0);">性别</a>
	        			</li>
	        			<li>
	        				<a id="memberAttribute_a2" href="javascript:void(0);">年龄</a>
	        			</li>
	        			<li>
	        				<a id="memberAttribute_a3" href="javascript:void(0);">状态</a>
	        			</li>
	        			<li>
	        				<a id="memberAttribute_a4" href="javascript:void(0);">地域</a>
	        			</li>
	        		</ul>
	        	</li>
	        	</cc:ps>
	        	
	        	<cc:ps privileges="${privileges}" privilege="访问,数据监控">
	        	<li data-id = "4" class="">
	        		<a class="depth" href="javascript:void(0);" onclick="Openthis(this)"><i class="icon icon-612"></i>数据监控</a>
	        		<ul>
	        			<li>
	        				<a id="monitor_a1" href="javascript:void(0);">综合数据</a>
	        			</li>
	        		</ul>
	        	</li>
	        	</cc:ps>
	        	
	        	<cc:ps privileges="${privileges}" privilege="访问,网站管理">
	        	<li data-id="2">
	        		<a id="website_a" href="javascript:void(0);"><i class="icon icon-60e"></i>网站管理</a>
	        	</li>
	        	</cc:ps>
	        	
	        	<cc:ps privileges="${privileges}" privilege="访问,推广链接">
	        	<li data-id="3">
	        		<a id="extension_a" href="javascript:void(0);"><i class="icon icon-60e"></i>推广链接</a>
	        	</li>
	        	</cc:ps>
	        	
	        	<cc:ps privileges="${privileges}" privilege="访问,禁用链接">
	        	<li data-id="3">
	        		<a id="disable_a" href="javascript:void(0);"><i class="icon icon-60e"></i>禁用链接</a>
	        	</li>
	        	</cc:ps>
	        	
	        	<li data-id="5" class="">
	        		<a class="depth" href="javascript:" onclick="Openthis(this)"><i class="icon icon-60d"></i>标准数据</a>
	        		<ul>
	        			<cc:ps privileges="${privileges}" privilege="访问,软件标准数据">
	        			<li>
	        				<a id="software_standard_data" href="javascript:void(0);">软件标准数据</a>
	        			</li>
	        			</cc:ps>
	        			<cc:ps privileges="${privileges}" privilege="访问,硬件标准数据">
	        			<li>
	        				<a id="hardware_standard_data" href="javascript:void(0);">硬件标准数据</a>
	        			</li>
	        			</cc:ps>
	        			
	        			<cc:ps privileges="${privileges}" privilege="访问,属性标准数据">
	        			<li>
	        				<a id="memberAttribute_standard_data" href="javascript:void(0);">属性标准数据</a>
	        			</li>
	        			</cc:ps>
	        			
	        		</ul>
	        	</li>
	        	
	        	<li data-id="5" class="">
	        		<a class="depth" href="javascript:void(0);" onclick="Openthis(this)"><i class="icon icon-60d"></i>功能设置</a>
	        		<ul>
	        		
	        			<cc:ps privileges="${privileges}" privilege="访问,事件分类">
	        			<li>
	        				<a id="appEventClass_a" href="javascript:void(0);">事件分类</a>
	        			</li>
	        			</cc:ps>
	        		
	        			<cc:ps privileges="${privileges}" privilege="访问,事件设置">
	        			<li>
	        				<a id="appEventSet_a" href="javascript:void(0);">事件设置</a>
	        			</li>
	        			</cc:ps>
	        		
	        			<cc:ps privileges="${privileges}" privilege="访问,APP渠道">
	        			<li>
	        				<a id="appChannel_a" href="javascript:void(0);">APP渠道</a>
	        			</li>
	        			</cc:ps>
	        			
	        			<cc:ps privileges="${privileges}" privilege="访问,APP版本">
	        			<li>
	        				<a id="appVersion_a" href="javascript:void(0);">APP版本</a>
	        			</li>
	        			</cc:ps>
	        			
	        		</ul>
	        	</li>
	        	
	        	<li data-id="5" class="">
	        		<a class="depth" href="javascript:void(0);" onclick="Openthis(this)"><i class="icon icon-60d"></i>用户设置</a>
	        		<ul>
	        			<cc:ps privileges="${privileges}" privilege="访问,用户管理">
	        			<li>
	        				<a id="admin_manage_a" href="javascript:void(0);">用户管理</a>
	        			</li>
	        			</cc:ps>
	        			<cc:ps privileges="${privileges}" privilege="访问,角色管理">
	        			<li>
	        				<a id="role_manage_a" href="javascript:void(0);">角色管理</a>
	        			</li>
	        			</cc:ps>
	        			<cc:ps privileges="${privileges}" privilege="访问,模块管理">
	        			<li>
	        				<a id="model_manage_a" href="javascript:void(0);">模块管理</a>
	        			</li>
	        			</cc:ps>
	        			<cc:ps privileges="${privileges}" privilege="访问,渠道管理">
	        			<li>
	        				<a id="channel_manage_a" href="javascript:void(0);">渠道管理</a>
	        			</li>
	        			</cc:ps>
	        		</ul>
	        	</li>
	        </ul>
	    </div>
	    
	    <div class="nav_body">
	        <iframe id="myFrame" src="index/toIndex.do" width="100%"  height="100%" frameborder="0"></iframe>
	    </div>
	    
	    <div id="update_password_div" class="newmoent display_none">
	        <div class="ment_ucount">
	            <div class="centMain metilte">
	                <span>修改密码</span>
	            </div>
	            <div class="centMain pl15 pr15">
	            	<div id="update_password_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
	               
	               	<div class="centMain pt15">用户名</div>
	                <div class="centMain pt5">
	                    <input id="update_username" name="username" type="text" class="txtput fz15" value="${admin.username}" disabled="disabled"/>
	                </div>
	               
	                <div class="centMain pt15">密码</div>
	                <div class="centMain pt5">
	                    <input id="update_password" name="password" type="password" class="txtput fz15"/>
	                </div>
	                
	                <div class="centMain pt20 mt10">
	                    <div class="w85 fl">
	                        <button class="btn" onclick="updatePassword();">确认</button>
	                    </div>
	                    <div class="w65 fr"><button id="update_password_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>	
  </body>
</html>


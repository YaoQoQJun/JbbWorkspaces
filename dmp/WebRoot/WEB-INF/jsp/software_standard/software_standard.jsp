<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="cc" uri="/mytaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>软件标准数据</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
    <link href="css/icon.css" rel="stylesheet" type="text/css" />
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <link href="css/main.css" rel="stylesheet" type="text/css" />
    
	<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="themes/icon.css">
	
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/load.js" ></script>
    <script type="text/javascript" src="js/highcharts.js"></script>
	
	<script type="text/javascript">
	
		var save_data_item_name_flag=false;
		var save_hbase_name_flag=false;
		var save_exception_range_flag=false;
		var save_data_source_flag=false;
		
     	var update_data_item_name_flag=false;
     	var update_exception_range_flag=false;
     	var update_data_source_flag=false;
     	var update_hbase_name_flag=false;
     	
     	var save_data_name_flag=false;
     	var save_proportion_flag=false;
    	var update_data_name_flag=false;
		var update_proportion_flag=false;
		
		 $(function(){
			 
			 
		 	$("#hblveitem li").each(function(index,e){
   				var dataItem=$.parseJSON('${dataItem}');
   				if($(this).text()==dataItem.data_item_name){
   					$(this).addClass("active");
   				}
   			});
	   			
			 
			 
	     	$("#save_data_item_name").blur(function(){
	     		$("#save_data_item_error_msg").html("&nbsp;");
	     		var data_item_name=$(this).val();
	     		if(data_item_name==null||data_item_name==""){
	     			$("#save_data_item_error_msg").text("数据项名称不能为空！");
	     			save_data_item_name_flag=false;
	     		}else{
	     			save_data_item_name_flag=true;
	     		}
	     	});
	     	
	     	$("#save_hbase_name").blur(function(){
	     		$("#save_data_item_error_msg").html("&nbsp;");
	     		var hbase_name=$(this).val();
	     		if(hbase_name==null||hbase_name==""){
	     			$("#save_data_item_error_msg").text("hbase数据列名不能为空！");
	     			save_hbase_name_flag=false;
	     		}else{
	     			save_hbase_name_flag=true;
	     		}
	     	});
	     	
	     	
	     	
	     	$("#save_exception_range").blur(function(){
	     		$("#save_data_item_error_msg").html("&nbsp;");
	     		var exception_range=$(this).val();

	     		var reg = /^(\d|[1-9]\d|100)(\.\d{1,2})?$/;// 这里是 正则表达式，大小写英文字母都可以
	     	    if (!reg.test(exception_range)) {
	     	    	$("#save_data_item_error_msg").text("异常范围为3位有效数字，最多保留2位！");
	     	    	save_exception_range_flag=false;
	     		}else{
	     			save_exception_range_flag=true;
	     		}
	     	});
	     	
	     	$("#save_data_source").blur(function(){
	     		$("#save_data_item_error_msg").html("&nbsp;");
	     		var data_source=$(this).val();
	     		if(data_source==null||data_source==""){
	     			$("#save_data_item_error_msg").text("数据来源不能为空！");
	     			save_data_source_flag=false;
	     		}else{
	     			save_data_source_flag=true;
	     		}
	     	});
	     	
	     	
	     	
	     	$("#update_data_item_name").blur(function(){
	     		$("#update_data_item_error_msg").html("&nbsp;");
	     		var data_item_name=$(this).val();
	     		if(data_item_name==null||data_item_name==""){
	     			$("#update_data_item_error_msg").text("数据项名称不能为空！");
	     			update_data_item_name_flag=false;
	     		}else{
	     			update_data_item_name_flag=true;
	     		}
	     	});
	     	
	     	$("#update_hbase_name").blur(function(){
	     		$("#update_data_item_error_msg").html("&nbsp;");
	     		var hbase_name=$(this).val();
	     		if(hbase_name==null||hbase_name==""){
	     			$("#update_data_item_error_msg").text("hbase数据列名不能为空！");
	     			update_hbase_name_flag=false;
	     		}else{
	     			update_hbase_name_flag=true;
	     		}
	     	});
	     	
	     	
	     	$("#update_exception_range").blur(function(){
	     		$("#update_data_item_error_msg").html("&nbsp;");
	     		var exception_range=$(this).val();
	     		var reg = /^(\d|[1-9]\d|100)(\.\d{1,2})?$/;// 这里是 正则表达式，大小写英文字母都可以
	     	    if (!reg.test(exception_range)) {
	     	    	$("#update_data_item_error_msg").text("异常范围为3位有效数字，最多保留2位！");
	     	    	update_exception_range_flag=false;
	     		}else{
	     			update_exception_range_flag=true;
	     		}
	     			
     	  	 	
	     	});
	     	
	     	$("#update_data_source").blur(function(){
	     		$("#update_data_item_error_msg").html("&nbsp;");
	     		var data_source=$(this).val();
	     		if(data_source==null||data_source==""){
	     			$("#update_data_item_error_msg").text("数据来源不能为空！");
	     			update_data_source_flag=false;
	     		}else{
	     			update_data_source_flag=true;
	     		}
	     	});
	     	
	     	
	     	$("#save_data_name").blur(function(){
	     		$("#save_data_error_msg").html("&nbsp;");
	     		var data_name=$(this).val();
	     		if(data_name==null||data_name==""){
	     			$("#save_data_error_msg").text("数据名称不能为空！");
	     			save_data_name_flag=false;
	     		}else{
	     			save_data_name_flag=true;
	     		}
	     	});
	     	
	     	$("#save_proportion").blur(function(){
	     		$("#save_data_error_msg").html("&nbsp;");
	     		var proportion=$(this).val();

	     		var reg = /^(\d|[1-9]\d|100)(\.\d{1,2})?$/;
	     	    if (!reg.test(proportion)) {
	     	    	$("#save_data_error_msg").text("占比为3位有效数字，最多保留2位！");
	     	    	save_proportion_flag=false;
	     		}else{
	     			save_proportion_flag=true;
	     		}
	     	});
	     	
	     	$("#update_data_name").blur(function(){
	     		$("#update_data_error_msg").html("&nbsp;");
	     		var data_name=$(this).val();
	     		if(data_name==null||data_name==""){
	     			$("#update_data_error_msg").text("数据名称不能为空！");
	     			update_data_name_flag=false;
	     		}else{
	     			update_data_name_flag=true;
	     		}
	     	});
	     	
	     	$("#update_proportion").blur(function(){
	     		$("#update_data_error_msg").html("&nbsp;");
	     		var proportion=$(this).val();

	     		var reg = /^(\d|[1-9]\d|100)(\.\d{1,2})?$/;
	     	    if (!reg.test(proportion)) {
	     	    	$("#update_data_error_msg").text("占比为3位有效数字，最多保留2位！");
	     	    	update_proportion_flag=false;
	     		}else{
	     			update_proportion_flag=true;
	     		}
	     	});
	     	
	     	
	     	
	     });
	
		
		function removeDataItem(id,e){
			if(confirm("确认删除数据项！")){
				$.ajax({
					type:"post",
					url:"softwareStandard/deleteDataItem.do",
					async:false,
					data:{
						"data_item_id":id,
						},
					success:function(msg){
						if(msg=="ok"){
							$.messager.show({
								title: "信息",
								msg: "删除数据项成功！"
							});
							window.location.href="softwareStandard/toSoftwareStandard.do";
						}else{
							$.messager.show({
								title: "信息",
								msg: "删除数据项失败！"
							});
						}
					},
					error:function(msg){
						$.messager.show({
							title: "信息",
							msg: "删除数据项失败，请联系管理员！"
						});
					}
				});
			}
		}
		
		
		function newDataItem(){
			$("#save_data_item_div").removeClass("display_none");
			$("#save_data_item_error_msg").html("&nbsp;");
			$("#save_data_item_name").val("");
			$("#save_hbase_name").val("");
			$("#save_exception_range").val("");
			$("#save_data_source").val("");
			
		}
		
		function saveDataItem(){
			
			
			if(!save_data_item_name_flag){
				$("#save_data_item_name").blur();
				return false;
			}
			
			if(!save_hbase_name_flag){
				$("#save_hbase_name").blur();
				return false;
			}
			
			if(!save_exception_range_flag){
				$("#save_exception_range").blur();
				return false;
			}
			
			if(!save_data_source_flag){
				$("#save_data_source").blur();
				return false;
			}
			
			
			
			var data_item_name=$("#save_data_item_name").val();
			var hbase_name=$("#save_hbase_name").val();
			var exception_range=$("#save_exception_range").val();
			var data_source=$("#save_data_source").val();
			
			$.ajax({
				type:"post",
				url:"softwareStandard/addDataItem.do",
				data:{
					"data_item_name":data_item_name,
					"hbase_name":hbase_name,
					"exception_range":exception_range,
					"data_source":data_source
					},
				success:function(msg){
					if(msg.result=="ok"){
						$("#save_data_item_error_msg").text("添加成功！");
						
						var str="<li data-id='"+msg.dataItem_id+"'><a href='softwareStandard/toSoftwareStandard.do?dataItem_id="+msg.dataItem_id+"'>"+data_item_name+"</a></li>";
						$("#hblveitem").append(str);
						
					}else{
						$("#save_data_item_error_msg").text("添加失败！");
					}
					save_data_item_name_flag=false;
					save_exception_range_flag=false;
					save_data_source_flag=false;
					save_hbase_name_flag=false;
				},
				error:function(msg){
					$("#save_data_item_error_msg").text("添加失败，请联系管理员！");
					save_data_item_name_flag=false;
					save_exception_range_flag=false;
					save_data_source_flag=false;
					save_hbase_name_flag=false;
				}
			});
		}
		
		var data_item_id=null;
		function editDataItem(id,data_item_name,hbase_name,exception_range,data_source){
			data_item_id=id;
			$("#update_data_item_error_msg").html("&nbsp;");
			$("#update_data_item_name").val(data_item_name);
			$("#update_hbase_name").val(hbase_name);
			$("#update_exception_range").val(exception_range);
			$("#update_data_source").val(data_source);
			$("#update_data_item_div").removeClass("display_none");
		}
		
		function updateDataItem(){
			$("#update_data_item_name").blur();
			if(!update_data_item_name_flag){
				return false;
			}
			
			$("#update_hbase_name").blur();
			if(!update_hbase_name_flag){
				return false;
			}
			
			$("#update_exception_range").blur();
			if(!update_exception_range_flag){
				return false;
			}
			$("#update_data_source").blur();
			if(!update_data_source_flag){
				return false;
			}
			var data_item_name=$("#update_data_item_name").val();
			var hbase_name=$("#update_hbase_name").val();
			var exception_range=$("#update_exception_range").val();
			var data_source=$("#update_data_source").val();
			
			$.ajax({
				type:"post",
				url:"softwareStandard/updateDataItem.do",
				data:{
					"data_item_name":data_item_name,
					"hbase_name":hbase_name,
					"exception_range":exception_range,
					"data_source":data_source,
					"data_item_id":data_item_id
					},
				success:function(msg){
					if(msg=="ok"){
						$("#update_data_item_error_msg").html("修改成功！");
						
					}else{
						$("#update_data_item_error_msg").html("修改失败！");
					}
				},
				error:function(msg){
					$("#update_data_item_error_msg").html("修改失败，请联系管理员！");
				}
			});
			
		}
		var data_item_id=null;
		function newData(id){
			data_item_id=id;
			$("#save_data_error_msg").html("&nbsp;");
			$("#save_data_div").removeClass("display_none");
			$("#save_data_name").val("");
			$("#save_proportion").val("");
			$("#save_data_name_flag").html("&nbsp;");
		}
		
		function saveData(){
			
			
	     	if(!save_data_name_flag){
				$("#save_data_name").blur();
				return false;
			}
			
			if(!save_proportion_flag){
				$("#save_proportion").blur();
				return false;
			}
			
			
			var data_name=$("#save_data_name").val();
			var proportion=$("#save_proportion").val();
			
			$.ajax({
				type:"post",
				url:"softwareStandard/addData.do",
				data:{
					"data_name":data_name,
					"proportion":proportion,
					"data_item_id":data_item_id
					},
				success:function(msg){
					if(msg.result=="ok"){
						
						var shtml = "<li>";
						shtml+="<span class=\"ittme\">"+data_name+"<font>"+proportion+"%</font></span>";
						shtml+="<span class='icon icon-60a' onclick=editData("+data_item_id+",'"+data_name+"',"+proportion+") ></span>";
						shtml+="<span class=\"icon icon-604\" onclick='deleteData("+msg.data_id+",this);'></span>";
						shtml+="</li>";
						$("#ciomes").append(shtml);
						$("#save_data_error_msg").html("添加成功！");
						
					}else{
						$("#save_data_error_msg").html("添加失败！");
					}
					save_data_name_flag=false;
			     	save_proportion_flag=false;
				},
				error:function(msg){
					$("#save_data_error_msg").html("添加失败！，请联系管理员！");
					save_data_name_flag=false;
			     	save_proportion_flag=false;
				}
			});
		}
		
		var data_id=null;
		function editData(id,data_name,proportion){
			$("#update_data_div").removeClass("display_none");
			$("#update_data_error_msg").html("&nbsp;");
			
			data_id=id;
			$("#update_data_name").val(data_name);
			$("#update_proportion").val(proportion);
			
		}
		
		function updateData(id){
			
			$("#update_data_name").blur();
			if(!update_data_name_flag){
				return false;
			}
			$("#update_proportion").blur();
			if(!update_proportion_flag){
				return false;
			}
		
			
			var data_name=$("#update_data_name").val();
			var proportion=$("#update_proportion").val();
			
			$.ajax({
				type:"post",
				url:"softwareStandard/updateData.do",
				data:{
					"data_name":data_name,
					"proportion":proportion,
					"id":data_id
					},
				success:function(msg){
					if(msg=="ok"){
						$("#update_data_error_msg").html("修改成功！");
						
					}else{
						$("#update_data_error_msg").html("修改失败！");
					}
				},
				error:function(msg){
					$("#update_data_error_msg").html("修改失败，请联系管理员！");
				}
			});
			
		}
		
		function deleteData(data_id,e){
			
			if(confirm("确认删除数据！")){
				var data_a=$(e);
				$.ajax({
					type:"post",
					url:"softwareStandard/deleteData.do",
					async:false,
					data:{
						"data_id":data_id,
						},
					success:function(msg){
						if(msg=="ok"){
							$.messager.show({
								title: "信息",
								msg: "删除数据成功！"
							});
							$(data_a).parent().remove();
						}else{
							$.messager.show({
								title: "信息",
								msg: "删除数据失败！"
							});
						}
					},
					error:function(msg){
						$.messager.show({
							title: "信息",
							msg: "删除数据失败，请联系管理员！"
						});
					}
				});
				
			}
			
		}

		//关闭弹出层
        function Btn_Close(obj) {
            $(obj).parents(".newmoent").addClass("display_none");
        }
        
        
        function refreshDataItem(){
        	window.location.href="<%=basePath%>softwareStandard/toSoftwareStandard.do";
        }
        
    </script>
		
	
	<style type="text/css">
		.easyui-panel table tr td{
			font-size: 12px;
			font-family: "微软雅黑";
		}
		.data_div{
			cursor: pointer;
		}
		a{
			text-decoration: none;
		}
		
	</style>
  </head>
  
  <body class="pl15">
  
  	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">软件标准数据设置</span>
    </div>
    <!-- 列表操作 -->
    <div class="centMain">
    	
        <ul id="hblveitem" class="fl hblve mt15">
        	<c:forEach items="${dataItems}" var="dataItem">
      			<li data-id="${dataItem.id}"><a href="softwareStandard/toSoftwareStandard.do?dataItem_id=${dataItem.id}">${dataItem.data_item_name}</a></li>
      		</c:forEach>
        </ul>
        
        <div class="fl mt15 ml15">
       		<cc:ps privileges="${privileges}" privilege="增加,软件标准数据">
            <a class="itms" onclick="newDataItem()"><i class="icon icon-607 fz12"></i>添加数据项</a>
        	<a class="itms" onclick="refreshDataItem()"><i class="icon icon-614 fz12"></i>刷 &nbsp; 新</a>
        	</cc:ps>
        </div>
        
        <div id="phmax" class="centMain mt30 phmax">
        	<div id="container" class="container">
        	</div>
        </div>
        <div class="centMain fz15 mt20 pr15 pt15 brodc">
			<div class="fl pr15 w150">数据项：<span id="data-name" class="susp">${dataItem.data_item_name}</span></div>
			<div class="fl pr15 w150">异常范围：<span id="data-scope" class="susp">${dataItem.exception_range}</span></div>
			<div class="fl pr15 w150">数据来源：<span id="data-from" class="susp">${dataItem.data_source}</span></div>
			
			<cc:ps privileges="${privileges}" privilege="删除,软件标准数据">
			<a class="sckre fr" onclick="removeDataItem(${dataItem.id},this);"><i class="icon icon-604"></i>删除</a>
			</cc:ps>
			<cc:ps privileges="${privileges}" privilege="修改,软件标准数据">
			<a class="sckre fr" onclick="editDataItem(${dataItem.id},'${dataItem.data_item_name}','${dataItem.hbase_name}','${dataItem.exception_range}','${dataItem.data_source}');"><i class="icon icon-60a"></i>编辑</a>
			</cc:ps>
			<cc:ps privileges="${privileges}" privilege="增加,软件标准数据">
			<a class="sckre fr" onclick="newData(${dataItem.id});"><i class="icon icon-60a"></i>增加数据</a>
			</cc:ps>
			
			<ul id="ciomes" class="centMain ciomes mt10 pb30">
			</ul>
			
		</div>
    </div>
  
	<!-- 保存数据项 -->
    <div id="save_data_item_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存数据项</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="save_data_item_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">数据项名称</div>
                <div class="centMain pt5">
                    <input id="save_data_item_name" name="data_item_name" type="text" class="txtput fz15" placeholder="数据项名称" />
                </div>
                <div class="centMain pt15">hbase数据列名</div>
                <div class="centMain pt5">
                    <input id="save_hbase_name" name="hbase_name" type="text" class="txtput fz15" placeholder="hbase数据列名" />
                </div>
                <div class="centMain pt15">异常范围</div>
                <div class="centMain pt5">
                    <input id="save_exception_range" name="exception_range" type="text" class="txtput fz15" placeholder="异常范围" />
                </div>
                <div class="centMain pt15">数据来源</div>
                <div class="centMain pt5">
                    <input id="save_data_source" name="data_source" type="text" class="txtput fz15" placeholder="数据来源" />
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button class="btn" onclick="saveDataItem();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="save_data_item_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
	
	
	<!-- 修改数据项 -->
    <div id="update_data_item_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>修改数据项</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="update_data_item_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">数据项名称</div>
                <div class="centMain pt5">
                    <input id="update_data_item_name" name="data_item_name" type="text" class="txtput fz15" placeholder="数据项名称" />
                </div>
                <div class="centMain pt15">Hbase数据列名</div>
                <div class="centMain pt5">
                    <input id="update_hbase_name" name="hbase_name" type="text" class="txtput fz15" placeholder="Hbase数据列名" />
                </div>
                <div class="centMain pt15">异常范围</div>
                <div class="centMain pt5">
                    <input id="update_exception_range" name="exception_range" type="text" class="txtput fz15" placeholder="异常范围" />
                </div>
                <div class="centMain pt15">数据来源</div>
                <div class="centMain pt5">
                    <input id="update_data_source" name="data_source" type="text" class="txtput fz15" placeholder="数据来源" />
                </div>
                
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button class="btn" onclick="updateDataItem();">确认</button>
                    </div>
                    <div class="w65 fr"><button id="update_data_item_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button></div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 保存数据 -->
    <div id="save_data_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>保存数据</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="save_data_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">数据名称</div>
                <div class="centMain pt5">
                    <input id="save_data_name" name="data_name" type="text" class="txtput fz15" placeholder="数据名称" />
                </div>
                <div class="centMain pt15">占比</div>
                <div class="centMain pt5">
                    <input id="save_proportion" name="proportion" type="text" class="txtput fz15" placeholder="占比" />
                </div>
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button class="btn" onclick="saveData();">确认</button>
                    </div>
                    <div class="w65 fr">
                    	<button id="save_data_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 修改数据 -->
    <div id="update_data_div" class="newmoent display_none">
        <div class="ment_ucount">
            <div class="centMain metilte">
                <span>修改数据</span>
            </div>
            <div class="centMain pl15 pr15">
            	<div id="update_data_error_msg" class="centMain" style="color:red;text-align: center;padding-top: 5px;">&nbsp;</div>
                <div class="centMain pt15">数据名称</div>
                <div class="centMain pt5">
                    <input id="update_data_name" name="data_name" type="text" class="txtput fz15" placeholder="数据名称" />
                </div>
                <div class="centMain pt15">占比</div>
                <div class="centMain pt5">
                    <input id="update_proportion" name="proportion" type="text" class="txtput fz15" placeholder="占比" />
                </div>
                <div class="centMain pt20 mt10">
                    <div class="w85 fl">
                        <button class="btn" onclick="updateData();">确认</button>
                    </div>
                    <div class="w65 fr">
                    	<button id="update_data_close_button" class="btn btn_colose" onclick="Btn_Close(this)">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
	
	<script type="text/javascript">
		var dataItem=$.parseJSON('${dataItem}');
		var datas=$.parseJSON('${datas}');
		var content=new Array();
		for (var int = 0; int < datas.length; int++) {
			var data=datas[int];
			var a=new Object();
			a.name=data.data_name;
			a.y=data.proportion;
			a.id=data.id;
			content[int]=a;
		}
		
    	var datalist = 
    	[{	
    		"name":dataItem.data_item_name,
    		
    		"data":content,
    	}];
    	
    	
    	$(function () {
    		ConsogleAvgimg(0);
    	});
    	
    	function ConsogleAvgimg(num){
    		var Mdata = datalist[num];
    		$("#ciomes").html("");
    		if(Mdata==undefined||Mdata.data.length==0){
    			$("#container").html("");
    			$("#phmax").append("<span class=\"null\">暂无数据，请先添加。</span>");
    			return;
    		}
    		if($("#phmax span.null").length>0){
    			$("#phmax span.null").remove();
    		}
    		
    		var privileges='${privileges}';
    		var str1="{privilege_name=修改, model_name=软件标准数据}";
    		var str2="{privilege_name=删除, model_name=软件标准数据}";
    		
    		$.each(Mdata.data, function(i,item) {
				var shtml = "<li>";
				shtml+="<span class=\"ittme\">"+item.name+"<font>"+item.y+"%</font></span>";
				
				if(privileges.indexOf(str1)>0){
					shtml+="<span class='icon icon-60a' onclick=editData("+item.id+",'"+item.name+"',"+item.y+") ></span>";
				}
				
				if(privileges.indexOf(str2)>0){
					shtml+="<span class=\"icon icon-604\" onclick='deleteData("+item.id+",this);'></span>";
				}
				shtml+="</li>"; 
				$("#ciomes").append(shtml);
			});

    		
    		$("#container").highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false,
		            type: 'pie'
		        },
		        title: {
		            text: Mdata.name
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    }
		                }
		            }
		        },
		        series: [{
		            name: '市场份额',
		            colorByPoint: true,
		            data: Mdata.data
		        }]
		    });
    	}
    </script>
  </body>
</html>

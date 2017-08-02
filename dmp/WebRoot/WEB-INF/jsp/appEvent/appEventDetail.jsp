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
<title>APP事件详情</title>

<link href="css/icon.css" rel="stylesheet" type="text/css" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />

<style type="text/css"></style>


<script type="text/javascript" src="js/jquery.min.js"></script>
<script src="js/load.js" type="text/javascript"></script>
<script type="text/javascript" src="js/laydate.dev.js"></script>
<script type="text/javascript" src="js/highcharts.js" ></script>


<script type="text/javascript" language="javascript">
	var base = document.getElementsByTagName("base")[0].getAttribute("href");
	var sData1=null;
	var sData2=null;
	var sData3=null;
	var sData4=null;

	$(function(){
		
		$("#selectModel").val('${client_model}');
		$("#selectChannel").val('${channel_name}');
		$("#selectVersion").val('${app_version}');
		
		var startDate=$("#startDate").val();
	 	var endDate=$("#endDate").val();
	 	startDate=$.trim(startDate);
	 	endDate=$.trim(endDate);
		
		laydate({
 	        elem: '#startDate',
 	        max: laydate.now(),
 	        format: 'YYYY-MM-DD'
 	    });
 		
 		laydate({
 	        elem: '#endDate',
 	        max: laydate.now(),
 	        format: 'YYYY-MM-DD'
 	    });
 		
 		
 		getDatas("1",$("#selectModel").val(),$("#selectChannel").val(),$("#selectVersion").val(),startDate,endDate);
 		
		
		$("input:radio[name=rad-hm]").click(function(){
			
			var a=$(this).val();
			var date=new Date();
			
			var startDate="";
			var endDate="";
			
			if(a==1){
				startDate=getStrDate(date);
				endDate=startDate;
			}else if(a==2){
			 	var d1=date.getTime()-24*60*60*1000;
				 	
				startDate=getStrDate(new Date(d1));
				endDate=getStrDate(date);
				
			}else if(a==3){
				var d1=date.getTime()-24*60*60*1000*7;
				 	
				startDate=getStrDate(new Date(d1));
				endDate=getStrDate(date);
				
			}else if(a==4){
				var d1=date.getTime()-24*60*60*1000*30;
				 	
				startDate=getStrDate(new Date(d1));
				endDate=getStrDate(date);
			}else{
				startDate=$.trim($("#startDate").val());
			 	endDate=$.trim($("#endDate").val());
			}
			
			$("#startDate").val(startDate);
			$("#endDate").val(endDate);
			
			getDatas(a,$("#selectModel").val(),$("#selectChannel").val(),$("#selectVersion").val(),startDate,endDate);
		});
 		
	});
	
	function Post_Ajax(state,client_model,channel_name,app_version,startDate,endDate){
		var shtml =null;
		$("#container").html("");
		
		$.ajax({
    		type:"post",
    		url:"appEvent/getDatas.do",
    		data:{
    			"state":state,"client_model":client_model,"channel_name":channel_name,
    			"app_version":app_version,"startDate":startDate,"endDate":endDate,"event_name":'${event_name}'
    			},
    		beforeSend:function(data){
    			$("#phmax").append("<span class=\"load\">正在加载，请稍后 ······</span>");
    		},
    		async:false,
    		success:function(msg){
    			shtml = msg;
    			$("#phmax .load").remove();
    		},
    		dataType:"json"
    	});
		return shtml;
	}
	
	
	function getDatas(state,client_model,channel_name,app_version,startDate,endDate){
		if(state==1){
			sData1=Post_Ajax(state,client_model,channel_name,app_version,startDate,endDate);
   			charts(sData1.countEvents,sData1.countUsers,sData1.arr,'今日数据');
		}else if(state==2){
			sData2=Post_Ajax(state,client_model,channel_name,app_version,startDate,endDate);
			charts(sData2.countEvents,sData2.countUsers,sData2.arr,'昨日数据');
		}else if(state==3){
			sData3=Post_Ajax(state,client_model,channel_name,app_version,startDate,endDate);
			charts(sData3.countEvents,sData3.countUsers,sData3.arr,'近7天数据');
		}else if(state==4){
			sData4=Post_Ajax(state,client_model,channel_name,app_version,startDate,endDate);
			charts(sData4.countEvents,sData4.countUsers,sData4.arr,'近30天数据');
		}
		
	}
	
	function charts(countEvents,countUsers,arr,str){
		$('#container').highcharts({
            title: {
                text: str,
                x: -20 
            },
           	xAxis: {
                categories: arr
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '次'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name:'${event_name} 消息量',
                data: countEvents
            },{
                name:'${event_name} 用户量',
                data: countUsers
            }]
        });
	}
	
	
	function changeSelect(){
		
		var client_model=$("#selectModel").val();
		var channel_name=$("#selectChannel").val();
		var app_version=$("#selectVersion").val();
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
		
		startDate=$.trim(startDate);
	 	endDate=$.trim(endDate);
		
		window.location.href=base+"appEvent/toAppEventDetail.do?client_model="+client_model
				+"&channel_name="+channel_name+"&app_version="+app_version
				+"&startDate="+startDate+"&endDate="+endDate+"&event_name="+'${event_name}';
	}

	
	function searchDate(){
	 	var startDate=$("#startDate").val();
	 	var endDate=$("#endDate").val();
	 	var client_model=$("#selectModel").val();
		var channel_name=$("#selectChannel").val();
		var app_version=$("#selectVersion").val();
		
		startDate=$.trim(startDate);
	 	endDate=$.trim(endDate);
		
		window.location.href=base+"appEvent/toAppEventDetail.do?client_model="+client_model
				+"&channel_name="+channel_name+"&app_version="+app_version
				+"&startDate="+startDate+"&endDate="+endDate+"&event_name="+'${event_name}';
	 	
	}
	
	
	function getStrDate(date){
    	
	    var seperator1 = "-";
	    var month = date.getMonth() + 1;
	    var day = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (day >= 0 && day <= 9) {
	    	day = "0" + day;
	    }
	    var strDate = date.getFullYear() + seperator1 + month + seperator1 + day;
	    
	    return strDate;
    }
	
</script>
</head>

<body style="padding: 0px; margin: 0px;" class="pl15">

	<div class="centMain bs-bmd" style="margin-top: 20px;">
		<a class="fz15 fl fxback" href="javascript:window.history.go(-1);"><i class="icon icon-60b"></i>返回</a>
        <span class="nasrnt">${event_remark}-详情 </span>
        
        <div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px; ">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<input type="text" placeholder="结束日期" id="endDate" value="${endDate}" style="width:200px; height:30px; padding:0 6px;border:0" />
			<a class="fr" id="search_date" onclick="searchDate();" href="javascript:void(0);" style=" display:inline-block; padding:0 14px; line-height:30px; border-left:1px solid rgba(0,0,0,.2);"> 查找</a>
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-left: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<input type="text" placeholder="起始日期" id="startDate" value="${startDate}" style="width:200px; height:30px; padding:0 6px;border:0" />
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-right: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<select id="selectVersion" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeSelect();">
				<option value="0">全部版本</option>
				<c:forEach items="${appVersions}" var="appVersion">
					<option value="${appVersion.app_version_name}">${appVersion.app_version_name}</option>
				</c:forEach>
			</select>
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-right: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<select id="selectChannel" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeSelect();">
				<option value="0">全部渠道</option>
				<c:forEach items="${appChannels}" var="appChannel">
					<option value="${appChannel.app_channel_name}">${appChannel.app_channel_name}</option>
				</c:forEach>
			</select>
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-right: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<select id="selectModel" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeSelect();">
				<option value="0">全部终端</option>
				<option value="android">android</option>
				<option value="ios">ios</option>
			</select>
		</div>
		
    </div>
    
    <!-- 列表操作 -->
    <div id="phmax" class="centMain mt30 phmax" style="margin-top: 50px;">
    	<div id="container" class="container">
    	</div>
    </div>
    <div class="centMain pb30 mt20 text-center">
     	<label class="labradio" style="margin-right: 20px;"><input value="1" type="radio" name="rad-hm" data-name="today" checked="checked" />今日</label>
     	<label class="labradio" style="margin-right: 20px;"><input value="2" type="radio" name="rad-hm" data-name="yesterday" />昨天</label>
     	<label class="labradio" style="margin-right: 20px;"><input value="3" type="radio" name="rad-hm" data-name="sevendays" />近七天</label>
     	<label class="labradio" style="margin-right: 20px;"><input value="4" type="radio" name="rad-hm" data-name="month" />近30天</label>
    </div>
		
</body>
</html>


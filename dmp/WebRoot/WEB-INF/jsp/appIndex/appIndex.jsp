<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
     <title>app概况</title>
    <link href="css/icon.css" rel="stylesheet" type="text/css" />
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <link href="css/main.css" rel="stylesheet" type="text/css" />
    
    <script src="js/jquery.min.js" type="text/javascript"></script>
    <script src="js/load.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/highcharts.js" ></script>
    <script type="text/javascript" src="js/laydate.dev.js"></script>
    
</head>
<body class="pl15">
    <div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">app统计数据分析</span>
         
		
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
			<select id="selectState" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeState();">
				<option value="">全部</option>
				<option value="android">android</option>
				<option value="ios">ios</option>
			</select>
		</div>
		
    </div>
    <div style="margin-top: 20px;float: left;margin-right: 60px;margin-left:20px;font-size: 18px;line-height: 24px;text-align: center;">
        <p>启动次数</p>
        <p style="margin-top: 20px;" id="AllstartCount"></p>
    </div>
    <div style="margin-top: 20px;float: left;margin-right: 60px;margin-left:20px;font-size: 18px;line-height: 24px;text-align: center;">
        <p>活跃用户</p>
        <p style="margin-top: 20px;" id="AllactiveUser"></p>
    </div>
   	<div  style="margin-top: 20px;float: left;margin-right: 60px;margin-left:20px;font-size: 18px;line-height: 24px;text-align: center;">
        <p>新增用户</p>
        <p style="margin-top: 20px;" id="AllnewUser"></p>
    </div>
   	<div style="margin-top: 20px;float: left;margin-right: 60px;margin-left:20px;font-size: 18px;line-height: 24px;text-align: center;">
        <p>新增注册用户</p>
        <p style="margin-top: 20px;" id="AllnewRegistUser"></p>
    </div>
   	<div style="margin-top: 20px;float: left;margin-right: 60px;margin-left:20px;font-size: 18px;line-height: 24px;text-align: center;">
        <p>注册转化率</p>
        <p style="margin-top: 20px;" id="AllregistRate"></p>
    </div>
    <!-- 列表操作 -->
    <div id="phmax" class="centMain mt30 phmax">
    	<div id="container" class="container">
    	</div>
    </div>
    <div class="centMain pb30 mt20 text-center">
     	<label class="labradio" style="margin-right: 20px;"><input value="1" type="radio" name="rad-hm" data-name="today" checked="checked" />今日</label>
     	<label class="labradio" style="margin-right: 20px;"><input value="2" type="radio" name="rad-hm" data-name="yesterday" />昨天</label>
     	<label class="labradio" style="margin-right: 20px;"><input value="3" type="radio" name="rad-hm" data-name="sevendays" />近七天</label>
     	<label class="labradio" style="margin-right: 20px;"><input value="4" type="radio" name="rad-hm" data-name="month" />近30天</label>
    </div>
    
    
    <script type="text/javascript">
    
	    var sData1=null;	
		var sData2=null;
		var sData3=null;
		var sData4=null;
    	
   	 	laydate({
 	        elem: '#startDate',
 	        max: laydate.now()
 	    });
 		
 		laydate({
 	        elem: '#endDate',
 	        max: laydate.now()
 	    });
 		
		function searchDate(){
		 	var startDate=$("#startDate").val();
		 	var endDate=$("#endDate").val();
		 	var state=$("#selectState").val();
		 	
		 	startDate=$.trim(startDate);
		 	endDate=$.trim(endDate);
		 	
			window.location.href="<%=basePath%>appIndex/toAppIndex.do?startDate="+startDate+"&endDate="+endDate+"&state="+state;
			
		}
    	
		function changeState(){
    		var state=$("#selectState").val();
    		var startDate=$("#startDate").val();
		 	var endDate=$("#endDate").val();
    		
		 	sData1 = Post_Ajax("1",state,startDate,endDate);
			charts(sData1.startCounts,sData1.activeUsers,sData1.newUsers,sData1.newRegistUsers,sData1.arr);
    	}
    	
    	$(function () {
    		
    		var startDate=$("#startDate").val();
		 	var endDate=$("#endDate").val();
		 	startDate=$.trim(startDate);
		 	endDate=$.trim(endDate);
		 	
		 	$("#selectState").val('${state}');
		 	var state=$("#selectState").val();
    		
		 	sData1 = Post_Ajax("1",state,startDate,endDate);
    		charts(sData1.startCounts,sData1.activeUsers,sData1.newUsers,sData1.newRegistUsers,sData1.arr);
    		
    		
    		$("input:radio[name=rad-hm]").click(function(){
    			var state=$("#selectState").val();
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
				
    			getDatas(a,state,startDate,endDate);
    		});
    		
        });
    	
    	
		function Post_Ajax(day,state,startDate,endDate){
			var shtml =null;
			$("#container").html("");
			$.ajax({
        		type:"post",
        		url:"appIndex/getDatas.do",
        		data:{"day":day,"state":state,"startDate":startDate,"endDate":endDate},
        		beforeSend:function(data){
        			$("#phmax").append("<span class=\"load\">正在加载，请稍后 ······</span>");
        		},
        		async:false,
        		success:function(msg){
        			shtml = msg;
        			
        			$("#AllstartCount").html(msg.AllstartCount);
        			$("#AllactiveUser").html(msg.AllactiveUser);
        			$("#AllnewUser").html(msg.AllnewUser);
        			$("#AllnewRegistUser").html(msg.AllnewRegistUser);
        			$("#AllnewRegistUser").html(msg.AllnewRegistUser);
        			$("#AllregistRate").html(Math.floor(msg.AllnewRegistUser/msg.AllactiveUser* 100) / 100 +" %");
        			
        			$("#phmax .load").remove();
        		},
        		dataType:"json"
        	});
			return shtml;
		}
		
		
		function getDatas(day,state,startDate,endDate){
			if(day==1){
				sData1=Post_Ajax(day,state,startDate,endDate);
	   			charts(sData1.startCounts,sData1.activeUsers,sData1.newUsers,sData1.newRegistUsers,sData1.arr,'今日访问量');
			}else if(day==2){
				sData2=Post_Ajax(day,state,startDate,endDate);
				charts(sData2.startCounts,sData2.activeUsers,sData2.newUsers,sData2.newRegistUsers,sData2.arr,'昨日访问量');
			}else if(day==3){
				sData3=Post_Ajax(day,state,startDate,endDate);
				charts(sData3.startCounts,sData3.activeUsers,sData3.newUsers,sData3.newRegistUsers,sData3.arr,'近7天访问量');
			}else if(day==4){
				sData4=Post_Ajax(day,state,startDate,endDate);
				charts(sData4.startCounts,sData4.activeUsers,sData4.newUsers,sData4.newRegistUsers,sData4.arr,'近30天访问量');
			}
			
    	}
		
		function charts(startCounts,activeUsers,newUsers,newRegistUsers,arr,str){
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
                    name: '启动次数',
                    data: startCounts
                },
                {
                    name: '活跃用户',
                    data: activeUsers
                },
                {
                    name: '新增用户',
                    data: newUsers
                },{
                	name: '新增注册用户',
                    data: newRegistUsers
                }]
            });
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
</body>
</html>



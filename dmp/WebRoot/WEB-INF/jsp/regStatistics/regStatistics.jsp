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
     <title>主页</title>
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
        <span class="nasrnt">注册数据分析</span>
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px; ">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<input type="text" placeholder="结束日期" id="endDate" value="${endDate}" style="width:200px; height:30px; padding:0 6px;border:0" />
			<a class="fr" id="search_date" onclick="searchDate();" href="javascript:void(0);" style=" display:inline-block; padding:0 14px; line-height:30px; border-left:1px solid rgba(0,0,0,.2);"> 查找</a>
		</div>
		
		<div class="fr" style=" border:1px solid rgba(0,0,0,.2); padding-left:6px; border-radius:3px; -webkit-border-radius:3px;margin-left: 30px;">
			<i class="icon icon-614" style="margin-top:6px; float:left;"></i>
			<input type="text" placeholder="起始日期" id="startDate" value="${startDate}" style="width:200px; height:30px; padding:0 6px;border:0" />
		</div>
    </div>
    
    <div class="centMain" style="margin-top: 20px;">
        <span class="nasrnt" id="all">总注册量：</span>
        <p class="centMain mt10" id="pc_wap_app"></p>
    </div>
    
    <ul class="centMain reportuls drompp mt30">
        <li>
            <p class="centMain time">今天<span>注册量</span></p>
            <p class="centMain mt10" id="today_pc_wap_app"></p>
        </li>
        <li>
            <p class="centMain time">昨天<span>注册量</span></p>
            <p class="centMain mt10" id="yesterday_pc_wap_app"></p>
        </li>
    </ul>
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
    
	    laydate({
	        elem: '#startDate',
	        max: laydate.now()
	    });
		
		laydate({
	        elem: '#endDate',
	        max: laydate.now()
	    });
    
    
    	var sData1=null;	
    	var sData2=null;
    	var sData3=null;
    	var sData4=null;
    	
    	
    	function searchDate(){
    		
		 	var startDate=$("#startDate").val();
		 	var endDate=$("#endDate").val();
		 	startDate=$.trim(startDate);
		 	endDate=$.trim(endDate);
		 	
		 	$("input:radio[name=rad-hm]:checked").attr("checked",false);
		 	
			window.location.href="<%=basePath%>regStatistics/toRegStatistics.do?startDate="+startDate+"&endDate="+endDate;
			
		}
    	
    	$(function () {
    		
    		var startDate=$("#startDate").val();
		 	var endDate=$("#endDate").val();
		 	startDate=$.trim(startDate);
		 	endDate=$.trim(endDate);
    		
        	sData1 = Post_Ajax(startDate,endDate);
    		charts(sData1.pcs,sData1.waps,sData1.apps,sData1.androids,sData1.ioss,sData1.arr);
    		
    		$("#all").html("总注册量："+sData1.all);
    		$("#pc_wap_app").html("pc总注册量： <font>"+sData1.allPcReg+"</font>，wap总注册量： <font>"+sData1.allWapReg+"</font>，app总注册量： <font>"+sData1.allAppReg+"</font>，android总注册量： <font>"+sData1.allAndroidReg+"</font>，ios总注册量： <font>"+sData1.allIosReg+"</font>");
    		
    		$("#today_pc_wap_app").html("pc： <font>"+sData1.today[0]+"</font>，wap： <font>"+sData1.today[1]+"</font>，app： <font>"+sData1.today[2]+"</font>，android： <font>"+sData1.today[3]+"</font>，ios： <font>"+sData1.today[4]+"</font>");
    		
    		$("#yesterday_pc_wap_app").html("pc： <font>"+sData1.yesterday[0]+"</font>，wap： <font>"+sData1.yesterday[1]+"</font>，app： <font>"+sData1.yesterday[2]+"</font>，android： <font>"+sData1.yesterday[3]+"</font>，ios： <font>"+sData1.yesterday[4]+"</font>");
    		
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
				
    			getDatas($(this).val(),startDate,endDate);
    			
    		});
    		
        });
    	
		function Post_Ajax(startDate,endDate){
			var shtml =null;
			$("#container").html("");
			$.ajax({
        		type:"post",
        		url:"regStatistics/getDatas.do",
        		data:{"startDate":startDate,"endDate":endDate},
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
		
		
		function getDatas(state,startDate,endDate){
			if(state==1){
				sData1=Post_Ajax(startDate,endDate);
	   			charts(sData1.pcs,sData1.waps,sData1.apps,sData1.androids,sData1.ioss,sData1.arr,'今日注册量');
			}else if(state==2){
				sData2=Post_Ajax(startDate,endDate);
				charts(sData2.pcs,sData2.waps,sData2.apps,sData2.androids,sData2.ioss,sData2.arr,'昨日注册量');
			}else if(state==3){
				sData3=Post_Ajax(startDate,endDate);
				charts(sData3.pcs,sData3.waps,sData3.apps,sData3.androids,sData3.ioss,sData3.arr,'近7天注册量');
			}else if(state==4){
				sData4=Post_Ajax(startDate,endDate);
				charts(sData4.pcs,sData4.waps,sData4.apps,sData4.androids,sData4.ioss,sData4.arr,'近30天注册量');
			}
			
    	}
		
		function charts(pcs,waps,apps,androids,ioss,arr,str){
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
                    name: 'pc',
                    data: pcs
                },
                {
                    name: 'wap',
                    data: waps
                },{
                	name: 'app',
                    data: apps
                },{
                	name: 'android',
                    data: androids
                },{
                	name: 'ios',
                    data: ioss
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



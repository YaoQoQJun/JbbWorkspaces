<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title></title>
    <link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="themes/icon.css">
	
	<link href="css/icon.css" rel="stylesheet" type="text/css" />
	<link href="css/style.css" rel="stylesheet" type="text/css" />
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	
    <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-jqLoding.js"></script>
    <script type="text/javascript" src="js/load.js" ></script>
    <script type="text/javascript" src="js/highcharts.js"></script>
	<script type="text/javascript" src="js/laydate.dev.js"></script>
	
  </head>
  <body class="pl15">
	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">状态数据</span>
        
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
			<select id="selectUrl" style="width:200px; height:30px; padding:0 6px;border:0" onchange="changeRegfrom();">
				<option value="0">全部</option>
				<option value="pc">pc</option>
				<option value="wap">wap</option>
				<option value="app">app</option>
				<option value="android">android</option>
				<option value="ios">ios</option>
			</select>
		  </div>
    </div>
    
	<div class="centMain">
        
        <div id="soft_demo" class="centMain mt15">
           	<ul id="datas_ul" class="softdemo centMain mt15">
           	
           		<li class="before">
           			<div id="dataItem_name_div" class="centMain bftme">${dataItem.data_item_name}</div>
           			<span class="keyi"></span>
           		</li>
           		<li>
           			<div class="default">${dataItem.exception_range}%<font>${dataItem.data_source}</font></div>
           			<div class="newdef">实际数据</div>
           		</li>
           		
           	</ul>
           </div>
           
		<div id="phmax" class="centMain mt30 phmax">
			<div id="container" class="container">
			</div>
		</div>
		
		<div class="mt30 phmax" style="width: 50%;float: left;">
			<div  id="pregnancy" class="container">
			</div>
		</div>
		
		<div class="mt30 phmax" style="width: 50%;float: left;">
			<div  id="babyAge" class="container">
			</div>
		</div>
		
		<script type="text/javascript">
		
			laydate({
	            elem: '#startDate',
	            istoday: true, 
	            max: laydate.now()
	        });
			
			laydate({
	            elem: '#endDate',
	            istoday: true, 
	            max: laydate.now()
	        });
			
			$("#selectUrl").val('${regFrom}');
			
			function searchDate(){
			 	var startDate=$("#startDate").val();
			 	var endDate=$("#endDate").val();
			 	var regFrom=$("#selectUrl").val();
			 	startDate=$.trim(startDate);
			 	endDate=$.trim(endDate);
			 	regFrom=$.trim(regFrom);
				window.location.href="<%=basePath%>memberAttribute/toStateAttribute.do?startDate="+startDate+"&endDate="+endDate+"&regFrom="+regFrom;
			}
			
			function changeRegfrom(){
	    		var regFrom=$("#selectUrl").val();
	    		var startDate=$("#startDate").val();
			 	var endDate=$("#endDate").val();
			 	startDate=$.trim(startDate);
			 	endDate=$.trim(endDate);
			 	regFrom=$.trim(regFrom);
				window.location.href="<%=basePath%>memberAttribute/toStateAttribute.do?startDate="+startDate+"&endDate="+endDate+"&regFrom="+regFrom;
	    	}
		
			var datas1=null;
			var datas2=null;
			
			var data_a=new Array();
			var data_b=new Array();
			var data_c=new Array();
			var regFrom=$.trim($("#selectUrl").val());
			
			$.ajax({
				type:"post",
				url:"memberAttribute/getStateDatas.do",
				data:{"dataItem_id":${dataItem.id},"startDate":'${startDate}',"endDate":'${endDate}',"regFrom":regFrom},
				success:function(msg){
					datas1=msg.datas1;
					datas2=msg.datas2;
					$(".keyi").text(msg.counts[0]);
					
					for (var int = 0; int <datas2.length; int++) {
						var data2=datas2[int];
						var data1=datas1[int];
						var str="<li><div class='default'>"+
							data1.data_name+"<font>"+(data1.proportion).toFixed(2)+"%</font>"+
							"</div>";
						if(data2.data_item_id==1){
							str+="<div class='newdef abnormal'>"
								+data2.data_name+"<font>"+(data2.proportion).toFixed(2)+"%</font></div>";
						}else{
							str+="<div class='newdef'>"
								+data2.data_name+"<font>"+(data2.proportion).toFixed(2)+"%</font></div>";
						}
						str+="</li>";
						$("#datas_ul").append(str);
					}
					
					
					for (var int = 0; int <datas1.length; int++) {
						data_a[int]=datas1[int].data_name;
						data_b[int]=datas1[int].proportion;
					}
		        	
					for (var int = 0; int <datas2.length; int++) {
						data_c[int]=datas2[int].proportion;
					}
					
					ConsogleAvgimg();
					
					$("#hblveitem li a").each(function(index,e){
						if(index==0){
							$(this).addClass("active");
							return false;
						}
		   			});
					
					$.fn.jqLoading("destroy");
				},
				beforeSend:function(){
					$.fn.jqLoading({ height: 100, width: 240, text: "正在加载中，请耐心等待...." });
				}
			});
			
			
			$.ajax({
				type:"post",
				url:"memberAttribute/getPregnancyDatas.do",
				data:{"dataItem_id":${dataItem.id},"startDate":'${startDate}',"endDate":'${endDate}',"regFrom":regFrom},
				success:function(msg){
					datas2=msg.datas2;
					
					data_c=new Array();
					
					for (var int = 0; int <datas2.length; int++) {
						var a=new Array();
						a[0]=datas2[int].data_name;
						a[1]=datas2[int].proportion;
						data_c[int]=a;
					}
					
					ConsoglePregnancyImg(data_c);
					
					$("#hblveitem li a").each(function(index,e){
						if(index==0){
							$(this).addClass("active");
							return false;
						}
		   			});
					
					$.fn.jqLoading("destroy");
				},
				beforeSend:function(){
					$.fn.jqLoading({ height: 100, width: 240, text: "正在加载中，请耐心等待...." });
				}
			});
			
			$.ajax({
				type:"post",
				url:"memberAttribute/getBabyAgeDatas.do",
				data:{"dataItem_id":${dataItem.id},"startDate":'${startDate}',"endDate":'${endDate}',"regFrom":regFrom},
				success:function(msg){
					datas2=msg.datas2;
					data_c=new Array();
					
					for (var int = 0; int <datas2.length; int++) {
						var a=new Array();
						a[0]=datas2[int].data_name;
						a[1]=datas2[int].proportion;
						data_c[int]=a;
					}
					
					ConsogleBabyAgeImg(data_c);
					
					$("#hblveitem li a").each(function(index,e){
						if(index==0){
							$(this).addClass("active");
							return false;
						}
		   			});
					
					$.fn.jqLoading("destroy");
				},
				beforeSend:function(){
					$.fn.jqLoading({ height: 100, width: 240, text: "正在加载中，请耐心等待...." });
				}
			});
			
			
			
			function ConsogleAvgimg(){
				$('#container').highcharts({
			        chart: {
			            type: 'column'
			        },
			        title: {
			            text: $("#hblveitem li.active").text()+'份额数据对比'
			        },
			        subtitle: {
			            text: null
			        },
			        xAxis: {
			            categories:data_a, //参数集合(底部分类)
			            crosshair: true
			        },
			        yAxis: {
			            min: 0,
			            title: {
			                text: 'Rainfall (%)'
			            }
			        },
			        tooltip: {
			            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
			            footerFormat: '</table>',
			            shared: true,
			            useHTML: true
			        },
			        plotOptions: {
			            column: {
			                pointPadding: 0.2,
			                borderWidth: 0
			            }
			        },
			        series: [{
			            name: '标准数据',
			            data: data_b
			
			        }, {
			            name: '实际数据',
			            data: data_c
			
			        }]
			    });
			}
			
			function ConsoglePregnancyImg(data){
				
				 $('#pregnancy').highcharts({
			            chart: {
			                plotBackgroundColor: null,
			                plotBorderWidth: null,
			                plotShadow: false
			            },
			            title: {
			                text: '孕期占比'
			            },
			            tooltip: {
			                headerFormat: '{series.name}<br>',
			                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
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
			                type: 'pie',
			                name: '孕期占比',
			                data: data
			            }]
			        });
			}
	       
			function ConsogleBabyAgeImg(data){
				
				 $('#babyAge').highcharts({
			            chart: {
			                plotBackgroundColor: null,
			                plotBorderWidth: null,
			                plotShadow: false
			            },
			            title: {
			                text: '宝宝年龄段占比'
			            },
			            tooltip: {
			                headerFormat: '{series.name}<br>',
			                pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
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
			                type: 'pie',
			                name: '宝宝年龄段占比',
			                data: data
			            }]
			        });
			}
	        
	        
		</script>
	</div>
  </body>
</html>


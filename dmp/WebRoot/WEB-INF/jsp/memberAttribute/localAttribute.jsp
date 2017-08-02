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
	
    <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-jqLoding.js"></script>
    <script src="js/load.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/laydate.dev.js"></script>
	<script type="text/javascript" src="js/highmaps.js"></script>
	
  </head>
  <body class="pl15">
	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <span class="nasrnt">地域数据</span>
        
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
		
		
		<script type="text/javascript" src="js/maps.js"></script>
		<div id="map_container" style="margin-top: 30px;">加载地图数据中.......</div>
		
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
				window.location.href="<%=basePath%>memberAttribute/toLocalAttribute.do?startDate="+startDate+"&endDate="+endDate+"&regFrom="+regFrom;
			}
			
			function changeRegfrom(){
	    		var regFrom=$("#selectUrl").val();
	    		var startDate=$("#startDate").val();
			 	var endDate=$("#endDate").val();
			 	startDate=$.trim(startDate);
			 	endDate=$.trim(endDate);
			 	regFrom=$.trim(regFrom);
				window.location.href="<%=basePath%>memberAttribute/toLocalAttribute.do?startDate="+startDate+"&endDate="+endDate+"&regFrom="+regFrom;
	    	}
		
			var datas1=null;
			var datas2=null;
			var cRegist=null;
			
			var data_a=new Array();
			var data_b=new Array();
			var data_c=new Array();
			var regFrom=$.trim($("#selectUrl").val());
			
			$.ajax({
				type:"post",
				url:"memberAttribute/getLocalDatas.do",
				async: false,
				data:{"dataItem_id":${dataItem.id},"startDate":'${startDate}',"endDate":'${endDate}',"regFrom":regFrom},
				success:function(msg){
					datas1=msg.datas1;
					datas2=msg.datas2;
					cRegist=msg.cRegist;
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
			
			
			// 动态加载 Script
			function loadScript(url, callback){
				var script = document.createElement("script");
				script.type = "text/javascript";
				if (script.readyState){  //IE
					script.onreadystatechange = function(){
						if (script.readyState == "loaded" ||
							script.readyState == "complete"){
							script.onreadystatechange = null;
							callback();
						}
					};
				} else {  //Others
					script.onload = function(){
						callback();
					};
				}
				script.src = url;
				document.getElementsByTagName("head")[0].appendChild(script);
			}
			
			/*
			 * 获取链接的 map 参数
			 * map 参数值为地图的路径，所有文件路径参考 https://img.hcharts.cn/mapdata/index.html
			 */
			function getMapName(name) {
				return {
							path: "js/cn-all",
							name: "中国"
						};
			}
			// 地图路径，参考 https://img.hcharts.cn/mapdata/index.html
			var map = getMapName('map'),
				url =  map.path + '.js';
			
			// 动态加载地图数据文件并生成图表。
			loadScript(url, function(){
				// 生成数据
				var mapdata = Highcharts.maps["js/cn-all"];
				
				var data = [];
				
				for (var int = 0; int <datas2.length; int++) {
						var data2=datas2[int];
						var a=Math.round((data2.proportion/100)*cRegist);
						if(data2.data_name=="天津"){
							data.push({
								'hc-key':"cn-tj",
								 value:a
							});
						}else if(data2.data_name=="上海"){
							data.push({
								'hc-key':"cn-sh",
								 value:a
							});
						}else if(data2.data_name=="重庆"){
							data.push({
								'hc-key':"cn-cq",
								 value:a
							});
						}else if(data2.data_name=="河北"){
							data.push({
								'hc-key':"cn-hb",
								 value:a
							});
						}else if(data2.data_name=="山西"){
							data.push({
								'hc-key':"cn-sx",
								 value:a
							});
						}else if(data2.data_name=="辽宁"){
							data.push({
								'hc-key':"cn-ln",
								 value:a
							});
						}else if(data2.data_name=="吉林"){
							data.push({
								'hc-key':"cn-jl",
								 value:a
							});
						}else if(data2.data_name=="黑龙江"){
							data.push({
								'hc-key':"cn-hl",
								 value:a
							});
						}else if(data2.data_name=="江苏"){
							data.push({
								'hc-key':"cn-js",
								 value:a
							});
						}else if(data2.data_name=="浙江"){
							data.push({
								'hc-key':"cn-zj",
								 value:a
							});
						}else if(data2.data_name=="安徽"){
							data.push({
								'hc-key':"cn-ah",
								 value:a
							});
						}else if(data2.data_name=="福建"){
							data.push({
								'hc-key':"cn-fj",
								 value:a
							});
						}else if(data2.data_name=="江西"){
							data.push({
								'hc-key':"cn-jx",
								 value:a
							});
						}else if(data2.data_name=="山东"){
							data.push({
								'hc-key':"cn-sd",
								 value:a
							});
						}else if(data2.data_name=="河南"){
							data.push({
								'hc-key':"cn-he",
								 value:a
							});
						}else if(data2.data_name=="湖北"){
							data.push({
								'hc-key':"cn-hu",
								 value:a
							});
						}else if(data2.data_name=="湖南"){
							data.push({
								'hc-key':"cn-hn",
								 value:a
							});
						}else if(data2.data_name=="广东"){
							data.push({
								'hc-key':"cn-gd",
								 value:a
							});
						}else if(data2.data_name=="海南"){
							data.push({
								'hc-key':"cn-ha",
								 value:a
							});
						}else if(data2.data_name=="四川"){
							data.push({
								'hc-key':"cn-sc",
								 value:a
							});
						}else if(data2.data_name=="贵州"){
							data.push({
								'hc-key':"cn-gz",
								 value:a
							});
						}else if(data2.data_name=="云南"){
							data.push({
								'hc-key':"cn-yn",
								 value:a
							});
						}else if(data2.data_name=="陕西"){
							data.push({
								'hc-key':"cn-sa",
								 value:a
							});
						}else if(data2.data_name=="甘肃"){
							data.push({
								'hc-key':"cn-gs",
								 value:a
							});
						}else if(data2.data_name=="青海"){
							data.push({
								'hc-key':"cn-qh",
								 value:a
							});
						}else if(data2.data_name=="内蒙古"){
							data.push({
								'hc-key':"cn-nm",
								 value:a
							});
						}else if(data2.data_name=="广西"){
							data.push({
								'hc-key':"cn-gx",
								 value:a
							});
						}else if(data2.data_name=="西藏"){
							data.push({
								'hc-key':"cn-xz",
								 value:a
							});
						}else if(data2.data_name=="新疆"){
							data.push({
								'hc-key':"cn-xj",
								 value:a
							});
						}else if(data2.data_name=="宁夏"){
							data.push({
								'hc-key':"cn-nx",
								 value:a
							});
						}else if(data2.data_name=="北京"){
							data.push({
								'hc-key':"cn-bj",
								 value:a
							});
						}
				
				}
				
				
				// 初始化图表
				$('#map_container').highcharts('Map', {
					title : {
						text : '地域热图'
					},
					subtitle : {
						text : '地图数据：'+map.name+'</a>'
					},
					mapNavigation: {
						enabled: true,
						buttonOptions: {
							verticalAlign: 'bottom'
						}
					},
					colorAxis: {
						min: 0,
						stops: [
							[0, '#EFEFFF'],
							[0.5, Highcharts.getOptions().colors[0]],
							[1, Highcharts.Color(Highcharts.getOptions().colors[0]).brighten(-0.5).get()]
						]
					},
					series : [{
						data : data,
						mapData: mapdata,
						joinBy: 'hc-key',
						name: '注册用户',
						states: {
							hover: {
								color: '#a4edba'
							},
						},
						dataLabels: {
							enabled: false,
							format: '{point.name}'
						}
					}]
				});
			});
		</script>
	</div>
  </body>
</html>


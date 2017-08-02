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
    <script src="js/load.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/highmaps.js"></script>
    <script type="text/javascript" src="js/maps.js"></script>
    
    <script type="text/javascript">
    	function seeLinkSoftware(extension_id,link){
    		link=link.replace(/&/g,"%26");
    		var date=$("#date").val();
    		window.location.href="<%=basePath%>monitor/seeLinkSoftware.do?extension_id="+extension_id+"&link="+link+"&date="+date;
    	}
    	function seeLinkHardware(extension_id,link){
    		link=link.replace(/&/g,"%26");
    		var date=$("#date").val();
    		window.location.href="<%=basePath%>monitor/seeLinkHardware.do?extension_id="+extension_id+"&link="+link+"&date="+date;
    	}
    	
    </script>
  </head>
  <body class="pl15">
  	<input type="hidden" id="date" value="${date}">
	<div class="centMain bs-bmd" style="margin-top: 20px;">
        <a class="fz15 fl fxback" href="monitor/toMonitor.do"><i class="icon icon-60b"></i>返回</a>
        <span class="nasrnt">软件数据</span>
    </div>
	<div class="centMain">
		<ul id="radchls" class="centMain radchls mt15">
            <li class="on">
                <a onclick="seeLinkSoftware(${extension.id},'${link}');" class="achlr" href="javascript:void(0);" title="软件参数">软件参数</a>
            </li>
            <li>
                <a onclick="seeLinkHardware(${extension.id},'${link}');" class="achlr" href="javascript:void(0);" title="硬件参数">硬件参数</a>
            </li>
        </ul>
      
      	<ul id="hblveitem" class="centMain hblve mt20">
      		<c:forEach items="${dataItems}" var="dataItem">
      			<li data-id="${dataItem.id}"><a href="javascript:void(0);" onclick="changeDatas(${dataItem.id},this);">${dataItem.data_item_name}</a></li>
      		</c:forEach>
        </ul>
        
        <div id="soft_demo" class="centMain mt15">
           	<ul id="datas_ul" class="softdemo centMain mt15">
           	
           		<li class="before">
           			<div id="dataItem_name_div" class="centMain bftme">${dataItem.data_item_name}</div>
           			<span class="keyi"></span>
           		</li>
           		<li>
           			<div class="default">${dataItem.exception_range}<font>${dataItem.data_source}</font></div>
           			<div class="newdef">${extension.channel_id } | ${extension.channel_name }</div>
           		</li>
           	</ul>
           </div>
           <!-- 数据报表 -->
          	<div id="phmax" class="centMain mt30 phmax">
		    	<div id="container" class="container">
		    	</div>
    		</div>
    		
			
		<script type="text/javascript">
			var datas1=null;
			var datas2=null;
			var cVisitor=null;
			var hbaseName=null;
			
			var data_a=new Array();
			var data_b=new Array();
			var data_c=new Array();
			
			var link='${link}';
			link=link.replace(/&/g,"%26");
    		
			$.ajax({
				type:"post",
				url:"monitor/getLinkSoftWareDatas.do",
				data:{"link":link,"channel_id":'${extension.channel_id}',"dataItem_id":${dataItem.id},"date":'${date}'},
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
		
		
			function changeDatas(dataItem_id,e){
				$("#map_container").remove();
				
	    		$("#hblveitem li a").each(function(index,e){
					$(this).removeClass("active");
	   			});
	    		$(e).addClass("active");
	    		$("#dataItem_name_div").text($(e).text());
	    		
	    		$("#datas_ul li").each(function(index,e){
	    			if(index==0||index==1){
	    				return true;
	    			}
	    			$(e).remove();
	    		});
	    		
	    		var link='${link}';
				link=link.replace(/&/g,"%26");
				
				$.ajax({
					type:"post",
					url:"monitor/getLinkSoftWareDatas.do",
					data:{"link":link,"channel_id":'${extension.channel_id}',"dataItem_id":dataItem_id,"date":'${date}'},
					async:false,
					success:function(msg){
						
						datas1=msg.datas1;
						datas2=msg.datas2;
						cVisitor=msg.cVisitor;
						hbaseName=msg.hbaseName;
						
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
						
						data_a=new Array();
						data_b=new Array();
						data_c=new Array();
						
						for (var int = 0; int <datas1.length; int++) {
							data_a[int]=datas1[int].data_name;
							data_b[int]=datas1[int].proportion;
						}
			        	
						for (var int = 0; int <datas2.length; int++) {
							data_c[int]=datas2[int].proportion;
						}
						
						ConsogleAvgimg();
						
						$.fn.jqLoading("destroy");
						
					},
					beforeSend:function(){
						$.fn.jqLoading({ height: 100, width: 240, text: "正在加载中，请耐心等待...." });
					}
				});
				
				if(hbaseName=="province"){
					$("#phmax").append("<div id='map_container' style='margin-top: 30px;'>加载地图数据中.......</div>");
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
								var a=Math.round((data2.proportion)/100*cVisitor);
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
				}
				
	    	}
		
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
			            name: '家有宝宝',
			            data: data_c
			
			        }]
			    });
			}
			
			
		</script>
       </div>
  </body>
</html>


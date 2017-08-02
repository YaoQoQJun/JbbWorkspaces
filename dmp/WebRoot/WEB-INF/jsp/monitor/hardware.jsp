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
    <script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/modules/data.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
    <script type="text/javascript">
	    function seeSoftware(extension_id){
	    	var date=$("#date").val();
			window.location.href="<%=basePath%>monitor/seeSoftWare.do?extension_id="+extension_id+"&state=1"+"&date="+date;
		}
		function seeHardware(extension_id){
			var date=$("#date").val();
			window.location.href="<%=basePath%>monitor/seeHardware.do?extension_id="+extension_id+"&state=2"+"&date="+date;
		}
    </script>
  </head>
  <body class="pl15">
  	<input type="hidden" id="date" value="${date}">
	<div class="centMain bs-bmd" style="margin-top: 20px;">
		<a class="fz15 fl fxback" href="monitor/toMonitor.do"><i class="icon icon-60b"></i>返回</a>
        <span class="nasrnt">硬件数据</span>
    </div>
    
	<div class="centMain">
		<ul id="radchls" class="centMain radchls mt15">
            <li>
                <a onclick="seeSoftware(${extension.id});" class="achlr" href="javascript:void(0);" title="软件参数">软件参数</a>
            </li>
            <li  class="on">
                <a onclick="seeHardware(${extension.id});" class="achlr" href="javascript:void(0);" title="硬件参数">硬件参数</a>
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
           
           <div id="phmax" class="centMain mt30 phmax">
	    	<div id="container" class="container">
	    	</div>
	       </div>
		<script type="text/javascript">
		
			var datas1=null;
			var datas2=null;
			
			var data_a=new Array();
			var data_b=new Array();
			var data_c=new Array();
				
			var extension_link='${extension.extension_link}';
    		extension_link=extension_link.replace(/&/g,"%26");
    		
			$.ajax({
				type:"post",
				url:"monitor/getSoftWareDatas.do",
				data:{"extension_link":extension_link,"channel_id":'${extension.channel_id}',"dataItem_id":${dataItem.id},"date":'${date}'},
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
	    		
	    		var extension_link='${extension.extension_link}';
	    		extension_link=extension_link.replace(/&/g,"%26");
	    		
				$.ajax({
					type:"post",
					url:"monitor/getSoftWareDatas.do",
					data:{"extension_link":extension_link,"channel_id":'${extension.channel_id}',"dataItem_id":dataItem_id,"date":'${date}'},
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


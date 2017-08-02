/* 下拉框 */
$(function () {	
    $(".menudown span").click(function () {
        if ($(this).parent().attr("class").indexOf("open") == -1) {
            $(this).parent().addClass("open");
        } else {
            $(this).parent().removeClass("open");
        }
        $(".menudown li").click(function () {
            var parent = $(this).parents(".menudown").removeClass("open").find("span").attr({ "data-value": $(this).attr("data-value"), "data-name": $(this).attr("data-name") }).text($(this).attr("data-name"));
        });
        $(document).click(function (event) {
            if ($(event.target).parents(".menudown").length == 0) {
                $(".menudown").removeClass("open");
            }
        });
    });
    //响应式点击
    $("#media_first_001").click(function () {
        if ($("#nav_left").css("left") == "0px") {
            $("#nav_left").stop().animate({ "left": "-90px" });
        } else {
            $("#nav_left").stop().animate({ "left": "0px" });
        }
    });
});

var reg_phone = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
function Getnav(url, item) {
    var shtml;
    $.ajax({
        url: url,
        type: "get",
        dataType: "html",
        cache: false,
        async: false,
        success: function (data) {
            shtml = data;
        }
    });
    $("body").append(shtml);
    if (item.length > 0) {
        $("#navleftitem a:eq(" + item + ")").addClass("on");
    }
}

//清空所有空格
function Trim(str) {
    return str.replace(/\s+/g, "");
}
//关闭
function Close_Member(obj) {
    $(obj).parents(".moent").css({ "display": "none" });
    $("table.navtable tr.on").removeClass("on");
}
//加载
function loads() {
    $("body").append('<div id="loads" class="loads">正在加载，请稍后 <b>···</b></div>');
}
//错误提示
function load_error(str) {
    if ($("#msgerr").length == 0) {
        var shrm = '<div id="msgerr" class="msgerr"><div><p>' + str + '</p><span class="icon icon-605 fr"></span></div></div>';
        $("body").append(shrm);
    } else {
        clearTimeout(i);
    }
    $("#msgerr p").text(str);
    $("#msgerr").stop().animate({ "top": "50px" });
    var i = setTimeout(function () { $("#msgerr").stop().animate({ "top": "0" }); }, 3200);
    $("#msgerr .icon").click(function () {
        clearTimeout(i);
        $("#msgerr").stop().animate({ "top": "0" });
    });
}
//提示成功
function load_success(str) {
    if ($("#load_suc").length == 0) {
        var shrm = '<div id="load_suc" class="load_suc"></div>';
        $("body").append(shrm);
    }
    $("#load_suc").text(str);
    $("#load_suc").stop().animate({ "top": "60px" });
    var i = setTimeout(function () { $("#load_suc").stop().animate({ "top": "0" }); }, 3000);
}
//删除提示
function load_remove() {
    $("body").append('<div id="remove_load" class="loads">正在删除数据中 <b>···</b></div>');
}
//价格转换
function Replace_dec(str) {
    if (parseInt(str) == str) {
        str = str + ".00";
    }
    return str;
}
//时间转换
function ChangeDateFormat(jsondate) {
    jsondate = jsondate.replace("/Date(", "").replace(")/", "");
    if (jsondate.indexOf("+") > 0) {
        jsondate = jsondate.substring(0, jsondate.indexOf("+"));
    }
    else if (jsondate.indexOf("-") > 0) {
        jsondate = jsondate.substring(0, jsondate.indexOf("-"));
    }
    var date = new Date(parseInt(jsondate, 10));
    var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
    var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

    return date.getFullYear() + "-" + month + "-" + currentDate + " " + date.getHours() + ":" + date.getMinutes();
}
//开关按钮
function Anmourty(obj) {
    var dato = $(obj).attr("dato");
    if (dato == "0") {
        $(obj).attr({ "dato": "1" })
        $(obj).find('span').stop().animate({ "left": "50%" });
        $(obj).css({ "background-color": "#4cd764", "border": "1px solid #4cd764" });
        //更多
        $(obj).parent().next("div.centMain").show();
    } else {
        $(obj).attr({ "dato": "0" })
        $(obj).find('span').stop().animate({ "left": "0" });
        $(obj).css({ "background-color": "#fff", "border": "1px solid #e5e5e5" });
        $(obj).parent().next("div.centMain").hide();
    }
}

//---- Post_Ajax
function Post_Ajax(uData) {
    var scdate = "";
    $.ajax({
        url: "../tool/system.ashx",
        type: "post",
        data: uData,
        async: false,
        cache: false,
        datatype: "json",
        success: function (data) {
            scdate = eval("(" + data + ")");
        }
    });
    return scdate;
}

/*  */
function Writeleft(url,nom){
	var shtml="";
	$.ajax({
		type:"get",
		url:url,
		async:false,
		cache:false,
		datatype:"text/html",
		success:function(data){
			shtml=data;
		}
	});
	document.write(shtml);
	$("#navleftitem>li").each(function(){
		if($(this).attr("data-id")==nom){
			$(this).addClass("on");
		}
	});	
}
function Openthis(obj){
	if($(obj).parent().attr("class")=="on"){
		$(obj).parent().removeClass("on");
	}else{
		$(obj).parent().addClass("on");
	}
}
var state='<%=state%>';
var _jbb = {};
_jbb.doc = document;
var jbb_p = (("https:" == document.location.protocol) ? "https://" : "http://");
document.write("<script src='"+jbb_p+"dmp.jbaobao.com/javascripts/heatmap.js'></script>");
document.write("<script src='"+jbb_p+"dmp.jbaobao.com/javascripts/json2.js'></script>");

window._jbb.Evt = (function () {
    var on = function (target, type, listener) {
//debugger;
        if (!listener) {
            return;
        }
// 处理stopImmediatePropagation，通过包装listener来支持stopImmediatePropagation
        if (!(window.Event && window.Event.prototype && window.Event.prototype.stopImmediatePropagation)) {
            listener = _addStopImmediate(listener);
        }

        if (target.addEventListener) {
            target.addEventListener(type, listener, false);

            return {
                remove: function () {
                    target.removeEventListener(type, listener);
                }
            }
        } else {
            return fixAttach(target, type, listener);
        }
    };
    var lastEvent; // 使用全局变量来保证一个元素的多个listenser中事件对象的一致性，冒泡过程中事件对象的一致性；在chrome这些过程中使用的是同一个event
//1、统一事件触发顺序
    function fixAttach(target, type, listener) {
        debugger;
        var listener = fixListener(listener);
        var method = 'on' + type;
        return _jbb.after(target, method, listener, true);
    };

    function fixListener(listener) {
        return function (evt) {
//每次调用listenser之前都会调用fixEvent
            debugger;
            var e = _fixEvent(evt, this);//this作为currentTarget
            if (e && e.cancelBubble && (e.currentTarget !== e.target)) {
                return;
            }
            var results = listener.call(this, e);

            if (e && e.modified) {
// 在整个函数链执行完成后将lastEvent回归到原始状态，
//利用异步队列，在主程序执行完后再执行事件队列中的程序代码
//常规的做法是在emit中判断lastEvent并设为null
//这充分体现了js异步编程的优势，把变量赋值跟清除代码放在一起，避免逻辑分散，缺点是不符合程序员正常思维方式
                if (!lastEvent) {
                    setTimeout(function () {
                        lastEvent = null;
                    });
                }
                lastEvent = e;
            }
            return results;
        }
    }

    function _fixEvent(evt, sender) {
        if (!evt) {
            evt = window.event;
        }
        if (!evt) { // emit没有传递事件参数，或者通过input.onclick方式调用
            return evt;
        }
        if (lastEvent && lastEvent.type && evt.type == lastEvent.type) {
//使用一个全局对象来保证在冒泡过程中访问的是同一个event对象
//chrome中整个事件处理过程event是唯一的
            evt = lastEvent;
        }
        var fixEvent = evt;
// bubbles 和cancelable根据每次emit时手动传入参数设置
        fixEvent.bubbles = typeof evt.bubbles !== 'undefined' ? evt.bubbles : false;
        fixEvent.cancelable = typeof evt.cancelable !== 'undefined' ? evt.cancelable : true;
        fixEvent.currentTarget = sender;
        if (!fixEvent.target) { // 多次绑定统一事件，只fix一次
            fixEvent.target = fixEvent.srcElement || sender;

            fixEvent.eventPhase = fixEvent.target === sender ? 2 : 3;
            if (!fixEvent.preventDefault) {
                fixEvent.preventDefault = _preventDefault;
                fixEvent.stopPropagation = _stopPropagation;
                fixEvent.stopImmediatePropagation = _stopImmediatePropagation;
            }
//参考：http://www.nowamagic.net/javascript/js_EventMechanismInDetail.php
            if (fixEvent.pageX == null && fixEvent.clientX != null) {
                var doc = document.documentElement, body = document.body;
                fixEvent.pageX = fixEvent.clientX + (doc && doc.scrollLeft || body && body.scrollLeft || 0) - (doc && doc.clientLeft || body && body.clientLeft || 0);
                fixEvent.pageY = fixEvent.clientY + (doc && doc.scrollTop || body && body.scrollTop || 0) - (doc && doc.clientTop || body && body.clientTop || 0);
            }
            if (!fixEvent.relatedTarget && fixEvent.fromEvent) {
                fixEvent.relatedTarget = fixEvent.fromEvent === fixEvent.target ? fixEvent.toElement : fixEvent.fromElement;
            }
// 参考: http://www.cnblogs.com/hsapphire/archive/2009/12/18/1627047.html
            if (!fixEvent.which && fixEvent.keyCode) {
                fixEvent.which = fixEvent.keyCode;
            }
        }

        return fixEvent;
    }

    function _preventDefault() {
        this.defaultPrevented = true;
        this.returnValue = false;

        this.modified = true;
    }

    function _stopPropagation() {
        this.cancelBubble = true;

        this.modified = true;
    }

    function _stopImmediatePropagation() {
        this.isStopImmediatePropagation = true;
        this.modified = true;
    }

    function _addStopImmediate(listener) {
        return function (evt) { // 除了包装listener外，还要保证所有的事件函数共用一个evt对象
            if (!evt.isStopImmediatePropagation) {
//evt.stopImmediatePropagation = _stopImmediateProgation;
                return listener.apply(this, arguments);
            }
        }
    }

// 模拟冒泡事件
    var sythenticBubble = function (target, type, evt) {
        var method = 'on' + type;
        var args = Array.prototype.slice.call(arguments, 2);
// 保证使用emit触发dom事件时，event的有效性
        if ('parentNode' in target) {
            var newEvent = args[0] = {};
            for (var p in evt) {
                newEvent[p] = evt[p];
            }

            newEvent.preventDefault = _preventDefault;
            newEvent.stopPropagation = _stopPropagation;
            newEvent.stopImmediatePropagation = _stopImmediatePropagation;
            newEvent.target = target;
            newEvent.type = type;
        }

        do {
            if (target && target[method]) {
                target[method].apply(target, args);
            }
        } while (target && (target = target.parentNode) && target[method] && newEvent && newEvent.bubbles);
    }

    var emit = function (target, type, evt) {
        if (target.dispatchEvent && document.createEvent) {
            var newEvent = document.createEvent('HTMLEvents');
            newEvent.initEvent(type, evt && !!evt.bubbles, evt && !!evt.cancelable);
            if (evt) {
                for (var p in evt) {
                    if (!(p in newEvent)) {
                        newEvent[p] = evt[p];
                    }
                }
            }

            target.dispatchEvent(newEvent);
        } /*else if (target.fireEvent) {
         target.fireEvent('on' + type);// 使用fireEvent在evt参数中设置bubbles:false无效，所以弃用
         } */ else {
            return sythenticBubble.apply(on, arguments);
        }
    }
    return {
        on: on,
        emit: emit
    };
})();

_jbb.http_request=function createXMLHTTPRequest(){
    //1.创建XMLHttpRequest对象
    //这是XMLHttpReuquest对象无部使用中最复杂的一步
    //需要针对IE和其他类型的浏览器建立这个对象的不同方式写不同的代码
    var xmlHttpRequest;
    if (window.XMLHttpRequest) {
        //针对FireFox，Mozillar，Opera，Safari，IE7，IE8
        xmlHttpRequest = new XMLHttpRequest();
        //针对某些特定版本的mozillar浏览器的BUG进行修正
        if (xmlHttpRequest.overrideMimeType) {
            xmlHttpRequest.overrideMimeType("text/xml");
        }
    } else if (window.ActiveXObject) {
        //针对IE6，IE5.5，IE5
        //两个可以用于创建XMLHTTPRequest对象的控件名称，保存在一个js的数组中
        //排在前面的版本较新
        var activexName = [ "MSXML2.XMLHTTP", "Microsoft.XMLHTTP" ];
        for ( var i = 0; i < activexName.length; i++) {
            try {
                //取出一个控件名进行创建，如果创建成功就终止循环
                //如果创建失败，回抛出异常，然后可以继续循环，继续尝试创建
                xmlHttpRequest = new ActiveXObject(activexName[i]);
                if(xmlHttpRequest){
                    break;
                }
            } catch (e) {
            }
        }
    }
    return xmlHttpRequest;
}


if(state==2){

    _jbb.a = {},
    _jbb.expose = +new Date(),
    _jbb.rExtractUri = /((?:http|https|file):\/\/.*?\/[^:]+)(?::\d+)?:\d+/,
    _jbb.isLtIE8 = ('' + _jbb.doc.querySelector).indexOf('[native code]') === -1;
    _jbb.referrer = document.referrer;
    _jbb.dmp_url = jbb_p+"dmp.jbaobao.com/dmp/ajdmp.js?";
    document.write("<script src='"+jbb_p+"dmp.jbaobao.com/javascripts/fingerprintjs2/dist/fingerprint2.min.js'></script>");


    /***
     * 获取操作系统,浏览器信息
     */
    _jbb.getSystemInfo = function () {
        var ua = navigator.userAgent.toLowerCase();
        isOpera = ua.indexOf("opera") > -1;
        isChrome = ua.indexOf("chrome") > -1;
        isSafari = !isChrome && (/webkit|khtml/).test(ua);
        isSafari3 = isSafari && ua.indexOf('webkit/5') != -1;
        isIE = !isOpera && ua.indexOf("msie") > -1;
        isIE7 = !isOpera && ua.indexOf("msie 7") > -1;
        isIE8 = !isOpera && ua.indexOf("msie 8") > -1;
        isFirefox = ua.indexOf("firefox") > -1;

        isWin7 = ua.indexOf("nt 6.1") > -1
        isVista = ua.indexOf("nt 6.0") > -1
        isWin2003 = ua.indexOf("nt 5.2") > -1
        isWinXp = ua.indexOf("nt 5.1") > -1
        isWin2000 = ua.indexOf("nt 5.0") > -1
        isWindows = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1)
        isMac = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1)
        isAir = (ua.indexOf("adobeair") != -1)
        isLinux = (ua.indexOf("linux") != -1)
        var sysinfo = {};
        if (isIE) {
            sysinfo.broser = "IE 6";
        } else if (isIE7) {
            sysinfo.broser = "IE 7";
        } else if (isIE8) {
            sysinfo.broser = "IE 8";
        } else if (isOpera) {
            sysinfo.broser = "Opera";
        } else if (isChrome) {
            sysinfo.broser = "Chrome";
        } else if (isSafari) {
            sysinfo.broser = "Safari";
        } else if (isSafari3) {
            sysinfo.broser = "Safari3";
        } else if (isFirefox) {
            sysinfo.broser = "Firefox";
        } else {
            sysinfo.broser = "Unknow";
        }

        if (isWin7) {
            sysinfo.sys = "Windows 7";
        } else if (isVista) {
            sysinfo.sys = "Vista";
        } else if (isWinXp) {
            sysinfo.sys = "Windows xp";
        } else if (isWin2003) {
            sysinfo.sys = "Windows 2003";
        } else if (isWin2000) {
            sysinfo.sys = "Windows 2000";
        } else if (isWindows) {
            sysinfo.sys = "Windows";
        } else if (isMac) {
            sysinfo.sys = "Macintosh";
        } else if (isAir) {
            sysinfo.sys = "Adobeair";
        } else if (isLinux) {
            sysinfo.sys = "Linux";
        } else {
            sysinfo.sys = "Unknow";
        }
        return sysinfo;
    };

    /**
     * 组装数据  16个数据
     操作系统                           j_sys
     浏览器                            j_browser
     语言                              j_language=zh-CN
     颜色深度                           j_color_depth
     屏幕分辨率                         j_resolution
     时区                              j_timezone_offset
     是否具有会话存储                    j_session_storage
     是否具有本地存储                    j_local_storage
     是否具有索引DB                     j_indexed_db
     CPU类                             j_cpu_class
     是否DoNotTrack                    j_do_not_track
     用户是否篡改了语言                  j_has_lied_languages
     用户是否篡改了屏幕分辨率            j_has_lied_resolution
     用户是否篡改了操作系统              j_has_lied_os
     用户是否篡改了浏览器               j_has_lied_browser
     cookies中的sessionID              j_fingerprint
     */
    _jbb.i = 0;
    _jbb.components = new Array();
    _jbb.result_fp = "";
    _jbb.senddata = function (urlpra) {
        while (true) {
            if (_jbb.i >= _jbb.components.length) {
                break;
            }
            if ((_jbb.i > 0 && _jbb.i < 11) || _jbb.i == 12 || (_jbb.i > 16 && _jbb.i < 22)) {
                urlpra += "&j_" + _jbb.components[_jbb.i].key + "=" + _jbb.components[_jbb.i].value;
                _jbb.i++;
            }
            else {
                _jbb.i++;
            }
        }

        if (urlpra == "") {
            return;
        }
        _jbb.structure(_jbb.dmp_url + urlpra);
    };


    /**
     * 构建jsonp提交
     */
    _jbb.structure = function (url) {
        var script = document.createElement('script');
        script.setAttribute('src', url);
        document.getElementsByTagName('head')[0].appendChild(script);
    }

    /**
     * 加载完后 统计数据
     */
    _jbb.Evt.on(window, 'load', function (evt) {

        _jbb.getPoint();//热图

        var sysinfo = _jbb.getSystemInfo();
        var urlpra = "";
        new Fingerprint2().get(function (result, components) {
            _jbb.components = components;
            _jbb.result_fp = result;

            urlpra += "&j_sys=" + sysinfo.sys;
            urlpra += "&j_broser=" + sysinfo.broser;
            urlpra += "&j_referrer=" +document.referrer.replace(/&/g, "%26");

            _jbb.senddata(urlpra);//组装数据
        });
    });

}else{
    /**
     * 加载完后 统计数据
     */
    _jbb.Evt.on(window, 'load', function (evt) {
        _jbb.getPoint();//热图

    });
}


_jbb.getPoint=function(){
    var points = [];//存储鼠标轨迹
    var times1=0;//鼠标未移动后记录当前毫秒数，
    /*
     根据参数名，获取url参数值
     */
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

    /*
     当鼠标移动时，调用此函数
     */
    function drawdot_web(event){
        event = event || window.event;
        target = event.target || event.srcElement,
            doc = document.documentElement || document.body;
        var point ={};

        // 获取窗口宽度
        var winWidth=0;

        if (window.innerWidth){
            winWidth = window.innerWidth;
        }else{
            ((document.body) && (document.body.clientWidth));
            winWidth = document.body.clientWidth;
        }

        if(winWidth<1024){
            winWidth=1024;
        }

        //获取鼠标轨迹
        if (event.pageX && event.layerX) {
            point = {
                x: event.pageX,
                y:event.pageY,
                value: 1,
                resolution_w:winWidth
            };

        } else {
            point = {
                x:  event.clientX + doc.scrollLeft - doc.clientLeft,
                y: event.clientY + doc.scrollTop - doc.clientTop,
                value: 1,
                resolution_w:winWidth
            };

        };



    //存储鼠标轨迹
        points.push(point);

        //当鼠标轨迹有100个时，上传
        if(points.length>=100){
            var a=JSON.stringify(points);

            var req = _jbb.http_request();
            if(req){
                req.open("POST", jbb_p+"dmp.jbaobao.com/dmp/point.js", false);
                req.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                req.onreadystatechange = function(){
                    if(req.readyState == 4){
                        if(req.status == 200){
                            points=[];
                        }else{
                        }
                    }
                }
                req.send("points="+a);
            }
        }

        times1 = new Date().getTime();//当前时间的毫秒数

    }


    function drawdot_wap(event){
        event = event || window.event;
        target = event.target || event.srcElement,
            doc = document.documentElement || document.body;
        var point ={};

        // 获取窗口宽度
        var winWidth=0;

        if (window.innerWidth){
            winWidth = window.innerWidth;
        }else{
            ((document.body) && (document.body.clientWidth));
            winWidth = document.body.clientWidth;
        }

        if(winWidth<1024){
            winWidth=1024;
        }

        //获取鼠标轨迹
        if (event.pageX && event.layerX) {
            point = {
                x: event.pageX,
                y:event.pageY,
                value: 1,
                resolution_w:winWidth
            };

        } else {
            point = {
                x:  event.clientX + doc.scrollLeft - doc.clientLeft,
                y: event.clientY + doc.scrollTop - doc.clientTop,
                value: 1,
                resolution_w:winWidth
            };

        };


        //存储鼠标轨迹
        points.push(point);

        var a=JSON.stringify(points);

        var req = _jbb.http_request();
        if(req){
            req.open("POST", jbb_p+"dmp.jbaobao.com/dmp/point.js", true);
            req.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            req.onreadystatechange = function(){
                if(req.readyState == 4){
                    if(req.status == 200){
                        points=[];
                    }else{
                    }
                }
            }
            req.send("points="+a);
        }

    }

    var state=getQueryString("state");

    /*
     如果参数值不等于abc，则记录鼠标轨迹
     否则获取鼠标轨迹，并画热力图
     */
    if("abc"!=state){
        var url1=window.location.href.substring(0,21);

        if(url1=="https://m.jbaobao.com"){

            document.onmousemove=drawdot_wap;

        }else{
            //监听鼠标移动，调用dradot函数
            document.onmousemove=drawdot_web;

            //每隔2秒上传一次
            setInterval(function(){
                var times2 = new Date().getTime();//当前时间的毫秒数
                var url1=window.location.href.substring(0,21);

                if(points.length<=5){
                    points=[];
                }
                if((times2-times1)>=2000&&points.length!=0){
                    var a=JSON.stringify(points);
                    var req = _jbb.http_request();
                    if(req){
                        req.open("POST", jbb_p+"dmp.jbaobao.com/dmp/point.js", false);
                        req.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                        req.onreadystatechange = function(){
                            if(req.readyState == 4){
                                if(req.status == 200){
                                    points=[];
                                }else{
                                }
                            }
                        }
                        req.send("points="+a);
                    }
                }

            },2000);
        }


    }else{

        var date=getQueryString("date");
        var url=getQueryString("url").replace(/&/g, "%26");

        var winWidth=0;
        // 获取窗口宽度
        if (window.innerWidth){
            winWidth = window.innerWidth;
        }else{
            ((document.body) && (document.body.clientWidth));
            winWidth = document.body.clientWidth;
        }

        var req = _jbb.http_request();

        if(req){
            //内网10.0.0.61 112.74.191.151
            if(jbb_p=="https://"){
                req.open("POST", jbb_p+"112.74.191.151:9443/statistics/points/getPoints.do", true);
            }else{
                req.open("POST", jbb_p+"112.74.191.151:9090/statistics/points/getPoints.do", true);

            }

            req.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            req.onreadystatechange = function(){
                if(req.readyState == 4){
                    if(req.status == 200){
                        var data= eval('(' + req.responseText + ')');
                        var arr=[];
                        var json =null;

                        //遍历x坐标，根据屏幕大小，生成相应坐标
                        for (var int = 0; int < data.length; int++) {

                            var msg=data[int].points;
                            json = eval('(' + msg + ')');

                            for (var int2 = 0; int2 < json.length; int2++) {

                                if(msg[int]==null||msg[int]=='undefined'||msg[int]==''){
                                    continue;
                                }

                                var resolution_w=json[int2].resolution_w;
                                if(winWidth>1024&&resolution_w<winWidth){
                                    json[int2].x=json[int2].x+(winWidth-resolution_w)/2;
                                }

                                if(winWidth<resolution_w&&resolution_w>1024){
                                    json[int2].x=json[int2].x-(resolution_w-winWidth)/2;
                                }

                                if(winWidth<1024&&resolution_w>1024&&msg[int].x>1024){
                                    json[int2].x=json[int2].x-(resolution_w-1024)/2;
                                }
                                arr.push(json[int2]);
                            }
                        }

                        //uv=getQueryString("uv");
                        //if(uv>100){
                        //    uv=100;
                        //}

                        var uv=Math.round(data.length*0.12);
                        if(uv<0){
                            uv=1;
                        }

                        //给body生成画板
                        var heatmapInstance = h337.create({
                            container:document.body,
                        });
                        //生成数据
                        var arr2 = {
                            max:uv,//当前URL的pv量
                            data: arr,
                        };
                        //生成热力图
                        heatmapInstance.setData(arr2);
                    }else{
                        console.log("error-----------");
                    }
                }
            }
            req.send("date="+date+"&url="+url);
        }

    }
}






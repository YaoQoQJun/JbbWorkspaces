var express = require('express');
var URL = require('url');
var cookieParser = require('cookie-parser');
var config = require('../config/config.js');
var bf = require('browser_fingerprint');
var help = require('../lib/tool.help.js');
var hbase = require('../lib/tool.hbase_rest.js');
var database =  new hbase(
    config.hbase.ip,
    config.hbase.port
);

var router = express.Router();

/**
 * 创建PV数据data
 * @param req
 */
function build_data(req,fingerprint) {

  var data = {};//获取来路请求
  var pv_rows= new Array();
  var referrer = req.headers['referer'];
  referrer = referrer==undefined?"":referrer;
  referrer = URL.parse(referrer);


  data.channel = (typeof(req.query.c)=='undefined'||req.query.c=="null")? 0:req.query.c;//渠道编码
  data.time = new Date().getTime().toString();//当前时间
  data.url = referrer.href;//当前url
  data.hostname = referrer.hostname ==null?"no":referrer.hostname;//当前主机名
  data.pv_rowid = fingerprint;//pv rowid
  data.ip=help.getClientIp(req);//获取ip地址

  var _host = "none";
  if(typeof(data.url) != 'undefined'||data.url != null||data.url != ""){
    _host = URL.parse(data.url).host;
  }

  pv_rows.push({key: data.pv_rowid, column: 'user:host', timestamp: Date.now(), $: _host});//当前host
  pv_rows.push({key: data.pv_rowid, column: 'user:url', timestamp: Date.now(), $: data.url});//当前url
  pv_rows.push({key: data.pv_rowid, column: 'user:c', timestamp: Date.now(), $: data.channel});//渠道编码
  pv_rows.push({key: data.pv_rowid, column: 'user:ctime', timestamp: Date.now(), $: data.time});//时间戳
  pv_rows.push({key: data.pv_rowid, column: 'user:hostname', timestamp: Date.now(), $: data.hostname});//本机服务器的主机名
  pv_rows.push({key: data.pv_rowid, column: 'user:ip', timestamp: Date.now(), $: data.ip});//ip
  pv_rows.push({key: data.pv_rowid, column: 'user:sessionID', timestamp: Date.now(), $: req.sessionID});
  //pv_rows.push({key: data.pv_rowid, column: 'user:sessionID', timestamp: Date.now(), $: data.pv_rowid.substring(0,32)});//sessionID
  var isVisit=req.cookies.isVisit;
  if(isVisit>=1){
    var a=eval(parseInt(isVisit)+1);
    pv_rows.push({key: data.pv_rowid, column: 'user:isVisit', timestamp: Date.now(), $:a+""});//第几次访问
  }else{
    pv_rows.push({key: data.pv_rowid, column: 'user:isVisit', timestamp: Date.now(), $:"1"});//
  }



  return pv_rows;
}

/**
 * 添加pv
 * @param fingerprint 指纹编码
 * @param data 数据实体
 * @param req 当前请求上下文
 */

function pv_add(tablename,rowid,pv_rows) {
  database.add_pv_detail(tablename,rowid,pv_rows);
}

/**
 * 记录uv信息
 */
function uv_add(tablename,rowid,uv_rows) {
  database.add_uv_detail(tablename,rowid,uv_rows);
}

/*
 记录鼠标移动轨迹
 */
function points_add(tablename,rowid,uv_rows) {
  database.add_points(tablename,rowid,uv_rows);
}


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render(
      'index'
  );
});

/* GET home page. */
router.get('/dmp.php', function(req, res, next) {

  var state=URL.parse(req.headers['referer'],true).query.state;

  if(state=="abc"){
    res.render(
        'dmp',
        {
          state: 1
        }
    );
    return;
  }

  //获取当前的访问的url
  var referrer = req.headers['referer'];
  referrer = referrer==undefined?"":referrer;
  referrer = URL.parse(referrer);


  // 今日最后时刻
  var today = new Date();
  today.setHours(23);
  today.setMinutes(59);
  today.setSeconds(59);

  //现在时间
  var now=new Date();

  //cookie过期时间
  var ma=today.getTime()-now.getTime();

  var fingerprint='';

  //先从cookie中获取sessionID
  var dmp_sessionID=req.cookies.dmp_sessionID;
  if(dmp_sessionID!=null&&dmp_sessionID!=''){
    fingerprint=dmp_sessionID.substring(0,32)+"|"+new Date().getTime().toString();
  }else{
    fingerprint=req.sessionID+"|"+new Date().getTime().toString();
  }


  //生成pv数据并记录pv
  var pv_rows=build_data(req,fingerprint);
  //保存pv数据
  pv_add('dmp_pv',fingerprint,pv_rows);


  if (req.cookies.isVisit>=1&&contains(req.cookies.urls,referrer.href)) {
    //console.log('再次欢迎访问');
    res.cookie('isVisit',eval(parseInt(req.cookies.isVisit)+1), {maxAge:ma});
    res.render(
        'dmp',
        {
          state: 1
        }
    );
  }else{
    //console.log('欢迎第一次访问,生成cookies,记录uv');
    if(req.cookies.isVisit>=1){
      res.cookie('isVisit',eval(parseInt(req.cookies.isVisit)+1), {maxAge:ma});
    }else{
      res.cookie('isVisit', 1, {maxAge:ma});
    }

    res.cookie('dmp_sessionID',fingerprint, {maxAge:ma});

    if(req.cookies.urls==null){
      var urls=[];
      urls.push(referrer.href);
      res.cookie('urls',urls, {maxAge:ma});

    }else{
      var urls=req.cookies.urls;
      urls.push(referrer.href);
      res.cookie('urls',urls, {maxAge:ma});
    }

    res.render(
        'dmp',
        {
          state: 2
        }
    );

  }

});


/**
 * 收集uv数据，21
 * 1.前端指纹UV信息     数据-16
 * 2.系统信息           数据-2
 * 3.地区信息           数据-3
 * 4.IP地址             数据-1
 * 5.时间戳            数据-1
 * 6.当前url           数据-1
 *
   5.服务器指纹UV信息    数据-16
 */
router.get('/ajdmp.js',function(req, res, next) {

  var ajfingerprint =req.cookies.dmp_sessionID;//获取sessionID

  if(ajfingerprint==null||ajfingerprint==''){
    res.end();
    return;
  }
  //获取当前的访问的url
  var referrer = req.headers['referer'];
  referrer = referrer==undefined?"":referrer;
  referrer = URL.parse(referrer);




  //利用客户端cookie中的sessionID与服务端的sessionID对比
  //var sid=req.sessionID;
  //if(sid!=ajfingerprint){
  //  res.end();
  //  return;
  //}


  var uv_rows = new Array();
  var t=Date.now();

  //前端指纹信息 通过url参数过来的UV信息
  for (var i in req.query) {
    if(i=="j_referrer"){
      uv_rows.push({key: ajfingerprint, column: 'info:' + i, timestamp:t, $: req.query[i].replace(/%26/g, "&")});
    }else{
      uv_rows.push({key: ajfingerprint, column: 'info:' + i, timestamp:t, $: req.query[i]});
    }
  }

  uv_rows.push({key: ajfingerprint, column: 'info:j_url', timestamp:t, $: referrer.href+''});


  var ipStr = help.getClientIp(req);//获取ip地址

  //异步记录ip城市
  help.getIpInfo(ipStr, function(err, msg) {
    var uv_rows2=new Array();
    if(msg instanceof Object) {
      uv_rows2.push({key: ajfingerprint, column: 'info:country', timestamp:t, $: msg.country+''});
      uv_rows2.push({key: ajfingerprint, column: 'info:province', timestamp:t, $: msg.province+''});
      uv_rows2.push({key: ajfingerprint, column: 'info:city', timestamp: t, $: msg.city+''});
      uv_add("dmp_uv",ajfingerprint,uv_rows2);//记录uv
    }
  });

  uv_rows.push({key: ajfingerprint, column: 'info:ip', timestamp:t, $:ipStr+''});//记录当前ip
  uv_rows.push({key: ajfingerprint, column: 'info:ctime', timestamp:t, $: t+''});//时间戳



  ////服务器指纹信息
  //bf.fingerprint(req, config.fingerprint.options, function (fingerprint, elementHash, cookieHash) {
  //  for (var i in elementHash) {
  //    uv_rows.push({key: ajfingerprint, column: 'info:' + i, timestamp: Date.now(), $: elementHash[i]});
  //  }
  //
  //});

  uv_add("dmp_uv",ajfingerprint,uv_rows);//记录uv

  res.end();

});


/**
 * 收集鼠标移动坐标
 */
router.post('/point.js',function(req, res, next) {
  //获取当前的访问的url
  var referrer = req.headers['referer'];
  referrer = referrer==undefined?"":referrer;
  referrer = URL.parse(referrer);

  var ajfingerprint =req.sessionID+"|"+Date.now();

  var p_rows = new Array();

  p_rows.push({key: ajfingerprint, column: 'info:points' , timestamp: Date.now(), $: req.body.points});
  p_rows.push({key: ajfingerprint, column: 'info:url', timestamp: Date.now(), $: referrer.href});
  p_rows.push({key: ajfingerprint, column: 'info:ctime', timestamp: Date.now(), $: Date.now()+""});

  res.header("Access-Control-Allow-Origin", "*");


  points_add("points",ajfingerprint,p_rows);
  res.end();
});


//判断元素是否存在在数组中，存在返回true
function contains(arr, obj) {
  var i = arr.length;
  while (i--) {
    if (arr[i] === obj) {
      return true;
    }
  }
  return false;
}

module.exports = router;

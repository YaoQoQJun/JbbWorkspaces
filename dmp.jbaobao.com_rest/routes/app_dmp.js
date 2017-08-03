var express = require('express');
var URL = require('url');
var cookieParser = require('cookie-parser');
var config = require('../config/config.js');
var bf = require('browser_fingerprint');

var appEventDao = require('../lib/tool.mysql.js');

var help = require('../lib/tool.help.js');
var hbase = require('../lib/tool.hbase_rest.js');
var database =  new hbase(
    config.hbase.ip,
    config.hbase.port
);



var router = express.Router();



router.post('/event.do', function(req, res, next) {

    //var rows = new Array();
    //var t=Date.now();
    //var ajfingerprint=req.body.client_id+'|'+t;
    //
    //rows.push({key: ajfingerprint, column: 'info:ctime', timestamp:t, $: t+''});//时间戳
    //rows.push({key: ajfingerprint, column: 'info:client_id', timestamp:t, $:req.body.client_id});//客户端编号
    //rows.push({key: ajfingerprint, column: 'info:client_model', timestamp:t, $:req.body.client_model});//客户端型号
    //rows.push({key: ajfingerprint, column: 'info:channel_name', timestamp:t, $:req.body.channel_name});//渠道名称
    //rows.push({key: ajfingerprint, column: 'info:app_version', timestamp:t, $:req.body.app_version});//APP版本
    //rows.push({key: ajfingerprint, column: 'info:notes_id', timestamp:t, $:req.body.notes_id});//手记ID
    //rows.push({key: ajfingerprint, column: 'info:event_name', timestamp:t, $:req.body.event_name});//事件名称
    //
    //database.add_app_event("app_event",ajfingerprint,rows);//记录事件信息

    appEventDao.add(req, res, next);

    res.end();

});

/**
 *APP启动 记录APP访问数据

router.post('/start.do',function(req, res, next) {

    var p_rows = new Array();
    var ajfingerprint=req.body.client_id+'|'+req.body.time;

    p_rows.push({key: ajfingerprint, column: 'info:client_id' , timestamp: Date.now(), $: req.body.client_id});
    p_rows.push({key: ajfingerprint, column: 'info:client_model' , timestamp: Date.now(), $: req.body.client_model});
    p_rows.push({key: ajfingerprint, column: 'info:client_system' , timestamp: Date.now(), $: req.body.client_system});
    p_rows.push({key: ajfingerprint, column: 'info:client_local' , timestamp: Date.now(), $: req.body.client_local});
    p_rows.push({key: ajfingerprint, column: 'info:client_net_mode' , timestamp: Date.now(), $: req.body.client_net_mode});
    p_rows.push({key: ajfingerprint, column: 'info:client_operator' , timestamp: Date.now(), $: req.body.client_operator});
    p_rows.push({key: ajfingerprint, column: 'info:app_version' , timestamp: Date.now(), $: req.body.app_version});
    p_rows.push({key: ajfingerprint, column: 'info:channel_name' , timestamp: Date.now(), $: req.body.channel_name});
    p_rows.push({key: ajfingerprint, column: 'info:time' , timestamp: Date.now(), $: req.body.time});


    //保存数据
    database.add_app_start('app_start',ajfingerprint,p_rows);

    res.header("Access-Control-Allow-Origin", "*");

    res.end();
});

 */


/**
 *记录APP的访问轨迹

router.post('/accessPath.do',function(req, res, next) {

    var p_rows = new Array();

    var ajfingerprint=req.body.client_id+'|'+req.body.time;

    p_rows.push({key: ajfingerprint, column: 'info:id' , timestamp: Date.now(), $: req.body.id});
    p_rows.push({key: ajfingerprint, column: 'info:client_id' , timestamp: Date.now(), $: req.body.client_id});
    p_rows.push({key: ajfingerprint, column: 'info:page_name' , timestamp: Date.now(), $: req.body.page_name});
    p_rows.push({key: ajfingerprint, column: 'info:remark_name' , timestamp: Date.now(), $: req.body.remark_name});
    p_rows.push({key: ajfingerprint, column: 'info:time' , timestamp: Date.now(), $: req.body.time});

    //保存数据
    database.add_app_accessPath('app_accesspath',ajfingerprint,p_rows);

    res.header("Access-Control-Allow-Origin", "*");

    res.end();
});

 */

module.exports = router;

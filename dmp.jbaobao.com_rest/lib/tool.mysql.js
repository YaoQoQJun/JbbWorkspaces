var mysql = require('mysql');
var config = require('../config/config.js');
var $util = require('../util/util');

// 使用连接池，提升性能
var pool  = mysql.createPool($util.extend({}, config.mysql));


module.exports = {
    add: function (req, res, next) {
        pool.getConnection(function(err, connection) {

            // 获取前台页面传过来的参数
            var param = req.body;
            var t=Date.now();

            connection.query(
                'insert into app_event(client_id,client_model,channel_name,app_version,event_name,time) values(?,?,?,?,?,?) ',
                [param.client_id, param.client_model,param.channel_name,param.app_version,param.event_name,t],
                function(err, result) {

            });

            // 释放连接
            connection.release();
        });
    }

};

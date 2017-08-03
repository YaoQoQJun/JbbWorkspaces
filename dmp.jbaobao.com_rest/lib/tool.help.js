/**
 * Created by chenxingliang on 16/8/25.
 */
var http = require('http');
var help={};

/**
 * 根据 ip 获取获取地址信息
 */
help.getIpInfo = function(ip, cb) {
    var sina_server = 'http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=';
    var url = sina_server + ip;
    http.get(url, function(res) {
        var code = res.statusCode;
        if (code == 200) {
            res.on('data', function(data) {
                try {
                    cb(null, JSON.parse(data));
                } catch (err) {
                    cb(err);
                }
            });
        } else {
            cb({ code: code });
        }
    }).on('error', function(e) { cb(e); });
};
/**
 * 获取客户端ip
 */
help.getClientIp =  function(req) {
    return req.headers['x-forwarded-for'] ||
        req.connection.remoteAddress ||
        req.socket.remoteAddress ||
        req.connection.socket.remoteAddress;
};
module.exports = help;

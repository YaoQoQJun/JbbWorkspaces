/**
 *  ps aux | grep 'node www' 需要写个启动,关闭脚本
 *  kill -9 18691
 *  cd /www/node.js.com/bin
 *  node jybb.js
 * 配置文件
 * @type {{hbase: {ip: string, port: number}, fingerprint: {options: {cookieKey: string, toSetCookie: boolean, onlyStaticElements: boolean, settings: {path: string, expires: number}}}}}
 */
module.exports  = {
    hbase: {
        ip: "10.0.0.61",
        port: 8888
    },
    fingerprint: {
        options: {
            cookieKey: "__browserjbb",
            toSetCookie: true,
            onlyStaticElements: false,
            settings: {
                path: '/',
                expires: 3600000,
            }
        }
    },
    base: {
        debug: true,//开发调试模式
        hostname: "127.0.0.1",//主机名
        port: 4000,//主机端口
        domainname: "dmp.jbaobao.com"//主机端口
    },
    mysql: {
        host: '10.0.0.51',
        user: 'jbbdu_dmp',
        password: 'c&QZIyPMbvo$lu2*',
        database:'dmp',
        port: 3306
    }
};

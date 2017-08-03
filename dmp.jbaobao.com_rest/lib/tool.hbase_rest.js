var hbase = require('hbase');
/**
 * hbase rest接口
 */
hbase_rest = function (ip, port) {
    this.client = new hbase({
        host: ip,
        port: port
    });
    this.ip = ip;
    this.port = port;
};
/**
 * 添加pv数据
 * @param rowid 行id
 * @param rows hbase数据格式的数组
 */
hbase_rest.prototype.add_pv_detail = function (tablename, rowid, rows) {
    this.client.table(tablename).row(rowid).put(rows,
        function (error, success) {
            if (!success) {
                console.log("add_pv_detail 插入数据失败" + error);
            }
        }
    );
};
/**
 * 添加服务器端uv
 * @param rowid 行id
 * @param rows hbase数据格式的数组
 */
hbase_rest.prototype.add_uv_detail = function (tablename, rowid, rows) {
    this.client.table(tablename).row(rowid).put(rows,
        function (error, success) {
            if (!success) {
                console.log("add_uv_detail 插入数据失败" + error);
            }
        }
    );
};
/**
 * 添加服务器端鼠标轨迹保存
 * @param rowid 行id
 * @param rows hbase数据格式的数组
 */
hbase_rest.prototype.add_points = function (tablename, rowid, rows) {
    this.client.table(tablename).row(rowid).put(rows,
        function (error, success) {
            if (!success) {
                console.log("add_points 插入数据失败" + error);
            }
        }
    );
};



/**
 * 保存APP启动数据
 * @param rowid 行id
 * @param rows hbase数据格式的数组
 */
hbase_rest.prototype.add_app_start = function (tablename, rowid, rows) {
    this.client.table(tablename).row(rowid).put(rows,
        function (error, success) {
            if (!success) {
                console.log("add_app_start 插入数据失败" + error);
            }
        }
    );
};

/**
 * 保存APP访问轨迹
 * @param rowid 行id
 * @param rows hbase数据格式的数组
 */
hbase_rest.prototype.add_app_accessPath = function (tablename, rowid, rows) {
    this.client.table(tablename).row(rowid).put(rows,
        function (error, success) {
            if (!success) {
                console.log("add_app_accesspath 插入数据失败" + error);
            }
        }
    );
};

/**
 * 保存APP事件记录
 * @param rowid 行id
 * @param rows hbase数据格式的数组
 */
hbase_rest.prototype.add_app_event = function (tablename, rowid, rows) {
    this.client.table(tablename).row(rowid).put(rows,
        function (error, success) {
            if (!success) {
                console.log("add_app_accesspath 插入数据失败" + error);
            }
        }
    );
};



module.exports = hbase_rest;

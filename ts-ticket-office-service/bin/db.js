/**
 * Created by dingding on 2017/10/13.
 */
var HOST=process.env.TICKET_OFFICE_MYSQL_HOST
var PORT=process.env.TICKET_OFFICE_MYSQL_PORT
var USER=process.env.TICKET_OFFICE_MYSQL_USER
var PASSWORD=process.env.TICKET_OFFICE_MYSQL_PASSWORD
var DATABASE=process.env.TICKET_OFFICE_MYSQL_DATABASE
var DB_CONN_STR = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

var mysql = require('mysql2');
var fs = require('fs');
var path = require('path');

var pool = mysql.createPool({
    host: HOST,
    port: PORT,
    user: USER,
    password: PASSWORD,
    database: DATABASE,
    connectionLimit: 10,
    ssl: {
        rejectUnauthorized: false
    },
    authPlugins: {
        mysql_clear_password: () => () => Buffer.from(PASSWORD + '\0')
    }
});

var initData = function(callback) {
    pool.getConnection(function(err, connection) {
        if (err) {
            console.error('Error connecting to the database:', err);
            callback(err);
            return;
        }

        var sql = "CREATE TABLE IF NOT EXISTS office (name VARCHAR(255), city VARCHAR(255), province VARCHAR(255), region VARCHAR(255), address VARCHAR(255), workTime VARCHAR(32), windowNum INT(10))";
        connection.query(sql, function (err, result) {
            if (err) {
                connection.release();
                callback(err);
                return;
            }
            console.log("Table created");
            
            // init data
            var values = "('Jinqiao Road ticket sales outlets', 'Shanghai', 'Shanghai', 'Pudong New Area', 'Jinqiao Road 1320, Shanghai, Pudong New Area', '08:00-18:00', 1)";
            var insertSql = "INSERT INTO office (name, city, province, region, address, workTime, windowNum) VALUES " + values;
            
            connection.query(insertSql, function (err, result) {
                connection.release();
                if (err) {
                    callback(err);
                    return;
                }
                console.log("Initial data inserted");
                callback(null, result);
            });
        });
    });
};

var getAllOffices = function(callback) {
    pool.getConnection(function(err, connection) {
        if (err) {
            callback(err);
            return;
        }
        
        connection.query("SELECT * FROM office", function (err, result) {
            connection.release();
            if (err) {
                callback(err);
                return;
            }
            callback(null, result);
        });
    });
};

var getSpecificOffices = function(province, city, region, callback) {
    pool.getConnection(function(err, connection) {
        if (err) {
            callback(err);
            return;
        }

        var where_sql = "WHERE province = ? AND city = ? AND region = ?";
        var sql = "SELECT * FROM office " + where_sql;
        
        connection.query(sql, [province, city, region], function (err, result) {
            connection.release();
            if (err) {
                callback(err);
                return;
            }
            callback(null, result);
        });
    });
};

var addOffice = function(province, city, region, office, callback) {
    pool.getConnection(function(err, connection) {
        if (err) {
            callback(err);
            return;
        }

        var sql = "INSERT INTO office (name, city, province, region, address, workTime, windowNum) VALUES (?, ?, ?, ?, ?, ?, ?)";
        var values = [office.name, city, province, region, office.address, office.workTime, office.windowNum];
        
        connection.query(sql, values, function (err, result) {
            connection.release();
            if (err) {
                callback(err);
                return;
            }
            callback(null, "insert succeed.");
        });
    });
};

var deleteOffice = function(province, city, region, officeName, callback) {
    pool.getConnection(function(err, connection) {
        if (err) {
            callback(err);
            return;
        }

        var sql = "DELETE FROM office WHERE name = ? AND province = ? AND city = ? AND region = ?";
        var values = [officeName, province, city, region];
        
        connection.query(sql, values, function (err, result) {
            connection.release();
            if (err) {
                callback(err);
                return;
            }
            callback(null, result);
        });
    });
};

var updateOffice = function(province, city, region, oldOfficeName, newOffice, callback) {
    pool.getConnection(function(err, connection) {
        if (err) {
            callback(err);
            return;
        }

        var sql = "UPDATE office SET name = ?, address = ?, workTime = ?, windowNum = ? WHERE name = ? AND province = ? AND city = ? AND region = ?";
        var values = [newOffice.name, newOffice.address, newOffice.workTime, newOffice.windowNum, oldOfficeName, province, city, region];
        
        connection.query(sql, values, function (err, result) {
            connection.release();
            if (err) {
                callback(err);
                return;
            }
            callback(null, result);
        });
    });
};

exports.initMysql = function(callback) {
    initData(function(err, result) {
        if (err) {
            console.error('Database initialization failed:', err);
            callback({ ok: false, error: err });
            return;
        }
        callback({ ok: true, result: result });
    });
};

exports.getAll = function(callback) {
    getAllOffices(function(err, result) {
        if (err) {
            callback([]);
            return;
        }
        callback(result);
    });
};

exports.getSpecificOffices = function(province, city, region, callback) {
    getSpecificOffices(province, city, region, function(err, result) {
        if (err) {
            callback([]);
            return;
        }
        callback(result);
    });
};

exports.addOffice = function(province, city, region, office, callback) {
    addOffice(province, city, region, office, function(err, result) {
        if (err) {
            callback({ error: err.message });
            return;
        }
        callback(result);
    });
};

exports.deleteOffice = function(province, city, region, officeName, callback) {
    deleteOffice(province, city, region, officeName, function(err, result) {
        if (err) {
            callback({ error: err.message });
            return;
        }
        callback(result);
    });
};

exports.updateOffice = function(province, city, region, oldOfficeName, newOffice, callback) {
    updateOffice(province, city, region, oldOfficeName, newOffice, function(err, result) {
        if (err) {
            callback({ error: err.message });
            return;
        }
        callback(result);
    });
};




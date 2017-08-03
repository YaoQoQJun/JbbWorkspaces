var spawn = require('child_process').spawn;

function startServer(){
    var server;
    console.log('start server');
    server = spawn('node',['www'],{
        detached : true,
        stdio: ['ignore', 'ignore', 'ignore']
    });
    server.unref();
    console.log('node js pid is '+server.pid);
    //console.log('Current gid: ' + server.getgid());

    server.on('error',function(code,signal){
        server.kill(signal);
        server = startServer();
    });

    server.on('exit',function(code,signal){

    });

    return server;
};

startServer();

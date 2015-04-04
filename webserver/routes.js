var request = require('request');

function pickIdleWorker() {
    for (var i = 0; i < global.workers.length; i++) {
        if (global.workers.idle[i].status == 'idle') {
            return global.workers.idle[i];
        }
    }
    return null;
}

function assignThread(threadJsonStr, worker) {
    worker.status = 'busy';
    request({
        //TODO
        url: worker.ip + ':' + worker.port,
        method: 'POST',
        json: true,
        body: threadJsonStr
    }, function(err, res, body) {
        console.log(res);
    });
}

function handle_create_thread(req, res) {
    res.json({ result: 'ok'});
    var worker = pickIdleWorker();
    assignThread(req.body, worker);
}

module.exports = function(app) {
    app.get('/', function(req, res) {
        res.json({ message: 'lalala'});
        console.log(global.workers);
    });
    app.post('/create_thread', handle_create_thread); 
};

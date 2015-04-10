var request = require('request');

function pickIdleWorker() {
    for (var i = 0; i < global.workers.length; i++) {
        if (global.workers[i].status == 'idle') {
            return global.workers[i];
        }
    }
    return null;
}

function assignThread(threadJsonStr, worker) {
    worker.status = 'waiting';
    var url = 'http://' + worker.ip + ':' + worker.port + '/assign_thread';
    console.log(url);
    request({
        url: url,
        method: 'PUT',
        json: true,
        body: threadJsonStr
    }, function(err, res, body) {
        if (err) {
            worker.status = 'idle';
            console.error(err);
        }
    });
}

function handleCreateThread(req, res) {
    console.log(req.body);
    res.json({ result: 'ok'});
    var worker = pickIdleWorker();
    assignThread(req.body, worker);
}

function handleGet(req, res) {
    var key = req.query.key;
    if (!global.variables.hasOwnProperty(key)) {
        res.status(404).send('Not found');
    } else {
        var val = global.variables[key];
        console.log('key: ' + key + ' val: ' + val);
        return res.json({ key: key, value: val});
    }
}

function handleSet(req, res) {
    console.log('req body : ', req.body);
    global.variables[req.body['key']] = req.body['value'];
    console.log(global.variables);
    return res.json({ message: 'ok'});
}

function handleLock(req, res) {
    //console.log(req);
    var name = req.query.name;
    if (!global.locks.hasOwnProperty(name)) {
        global.locks[name] = {
            status: 'locked',
            waiting: []
        };
        res.status(200).send();
    } else {
        var lock = global.locks[name];
        if (lock.status != 'locked') {
            res.status(200).send();
        } else {
            var ip = req.ip;
            if (ip.substring(0, 7) == '::ffff:') {
                ip = ip.substring(7);
            }
            lock.waiting.push(
                String(req.ip) + 
                ':' +
                String(req.query.port)
            );
            res.status(404).send();
        }
    }
    console.log(global.locks);
}

function handleUnlock(req, res) {
    res.status(200).send();
    var name = req.query.name;
    var lock = global.locks[name];
    if (lock.waiting.length == 0) {
        lock.status = 'unlocked';
    } else {
        var client = lock.waiting[0];
        lock.waiting.shift();
        var url = 'http://' + client + '/get_lock?name=' + name;
        console.log(url);
        request(url, function(err, res, body) {
            console.log("get_lock returned");
            if (err) {
                console.error(err);
            } else {
                console.log(body);
            }
        });
    }
    console.log(global.locks);
}

module.exports = function(app) {
    app.get('/', function(req, res) {
        res.json({ message: 'lalala'});
        console.log(global.workers);
    });
    app.put('/create_thread', handleCreateThread);
    app.put('/variable', handleSet);
    app.get('/variable', handleGet);
    app.get('/lock', handleLock);
    app.get('/unlock', handleUnlock);
};

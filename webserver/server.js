var express     = require('express');
var app         = express();
var bp          = require('body-parser');
var config      = require('config');

function initWorkers() {
    global.workers = config.get('workers');
    for (var i = 0; i < global.workers.length; i++) {
        global.workers[i].status = 'idle';
    }
    console.log(workers);
    global.variables = {};
    global.locks = {};
}

app.use(bp.json());
app.use(bp.urlencoded({extended: true}));

var port = process.env.PORT || 55555;

require('./routes')(app);

initWorkers();

app.listen(port);


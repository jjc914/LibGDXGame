var IP_ADDRESS = "10.0.18.61";
var PORT = 8000;

var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
players = []

// TODO: https://www.sisik.eu/blog/android/other/embedding-node-into-android-app
server.listen(PORT, IP_ADDRESS, function() {
    console.log("Server running");
})

io.on('connection', function(socket) {
    console.log("Player with id " + socket.id + " connected");
    socket.emit('socketID', { id: socket.id });
    socket.emit('getPlayers', players);
    socket.broadcast.emit("newPlayer", { id: socket.id });

    socket.on('playerMove', function(data) {
        data.id = socket.id;
        socket.broadcast.emit('playerMoved', data);

        for (var i = 0; i < players.length; i++) {
            if (players[i].id == data.id) {
                players[i].x == data.x;
                players[i].y == data.y;
            }
        }
    })

    socket.on('disconnect', function() {
        console.log("Player with id " + socket.id + " disconnected");
        socket.broadcast.emit('playerDisconnected', { id: socket.id });
        for (var i = 0; i < players.length; i++) {
            if (players[i].id == socket.id) {
                players.splice(i, 1);
            }
        }
    })
    players.push(new player(socket.id, 0, 0))
})

function player (id, x, y) {
    this.id = id;
    this.x = x;
    this.y = y;
}
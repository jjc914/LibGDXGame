var IP_ADDRESS = "192.168.1.6";
var PORT = 8000;

var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

// TODO: https://stackoverflow.com/questions/49377519/how-do-i-make-socket-io-listen-in-the-internet-with-a-static-ip
server.listen(PORT, IP_ADDRESS, function() {
    console.log("Server running");
})

io.on('connection', function(socket) {
    console.log("Player with id " + socket.id + " connected");
    socket.emit('socketID', { id: socket.id })
    socket.broadcast.emit("newPlayer", { id: socket.id })
    socket.on('disconnect', function() {
        console.log("Player with id " + socket.id + " disconnected");
    })
})
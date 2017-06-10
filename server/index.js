'use strict'

import express from 'express'
import socketio from 'socket.io'
import cors from'cors'
import Board from './board'

let app = express()
let server = app.listen(3100)
let io = socketio.listen(server)

app.use(cors())

let userQueue = []
let boardList = []

io.on('connection', function (socket) {
    let username
    let board

    // connection tests
    console.log('connection: ' + socket.id)

    // info to client -> new connection, reset game
    //socket.emit('new_connection')

    function addToBoardList(board) {
        boardList.unshift(board)
        console.log('boardList: ' + boardList.map((item)=>item.socketPlayer1.username + '/' + item.socketPlayer2.username))
        // clean up board list
        if (boardList.length > 20){
            boardList.forEach((item,pos)=>{
                if(item.done && pos > 20) {
                    boardList.splice(pos,1)
                }
            })
        }
        send_stats()
    }

    function connectUsers() {
        console.log('userQueue: ' + userQueue.map((item)=>item.username))
        if (userQueue.length > 1) {
            socket.board = new Board(userQueue.shift(), userQueue.shift())
            socket.board.socketPlayer2.board = socket.board

            addToBoardList(socket.board)

            socket.emit('start_game', {
                'player1':socket.board.socketPlayer1.username,
                'player2':socket.board.socketPlayer2.username
            })
            socket.to(socket.board.socketPlayer1.id).emit('start_game', {
                'player1':socket.board.socketPlayer1.username,
                'player2':socket.board.socketPlayer2.username
            })
            
            socket.emit('your_turn', {
                'player':'x',
                'username':socket.username
            })
            socket.to(socket.board.socketPlayer1.id).emit('other_turn', {
                'player': 'x',
                'username': socket.board.socketPlayer2.username
            })
            console.log(`player1 '${socket.board.socketPlayer1.username}' plays versus player2 '${socket.board.socketPlayer2.username}'`)            
        }
    }

    // receive new user
    socket.on('add_user', (data)=>{
        console.log(`username '${data.username}'`)
        socket.username = data.username
        userQueue.push(socket)
        socket.emit('user_added', {'username': data.username})
        connectUsers();
    })

    // disconnect: remove user from queue
    // -> send info to other player, that game is aborted and other player wins!
    socket.on('disconnect', function() {
      console.log('disconnect, socket: ' + socket.id);
      // remove user from waiting queue
      let i = userQueue.indexOf(socket);
      if (i >= 0){
          userQueue.splice(i, 1);
          console.log('userQueue: ' + userQueue.map((item)=>item.username))
      }
      // finish a running game


    });

    // player moves
    socket.on('player_action', (data)=>{
        console.log(`player '${data.player}' set field '${data.field}'`)
        if (socket.board) {
            socket.board.setField(data)
            let gameResult = socket.board.checkBoard()
            if (socket.id === socket.board.socketPlayer2.id) {
                // send player action to the other user
                socket.to(socket.board.socketPlayer1.id).emit('new_move', {
                    'player': 'x',
                    'field': data.field
                })
                if (gameResult) {
                    // send finish message
                    socket.to(socket.board.socketPlayer1.id).emit('game_finished', {
                        'winner': gameResult,
                        'fields': socket.board.fieldsWon
                    })
                    socket.emit('game_finished', {
                        'winner': gameResult,
                        'fields': socket.board.fieldsWon
                    })
                } else {
                    // start next move
                    socket.to(socket.board.socketPlayer1.id).emit('your_turn', {
                        'player': 'o',
                        'username': socket.board.socketPlayer1.username
                    })
                    socket.emit('other_turn', {
                        'player': 'o',
                        'username': socket.board.socketPlayer1.username
                    })
                }
            } else {
                // send player action to the other user
                socket.to(socket.board.socketPlayer2.id).emit('new_move', {
                    'player': 'o',
                    'field': data.field
                })
                if (gameResult) {
                    // send finish message
                    socket.to(socket.board.socketPlayer2.id).emit('game_finished', {
                        'winner': gameResult,
                        'fields': socket.board.fieldsWon
                    })
                    socket.emit('game_finished', {
                        'winner': gameResult,
                        'fields': socket.board.fieldsWon
                    })
                } else {
                    // start next move
                    socket.to(socket.board.socketPlayer2.id).emit('your_turn', {
                        'player': 'x',
                        'username': socket.board.socketPlayer2.username
                    })
                    socket.emit('other_turn', {
                        'player': 'x',
                        'username': socket.board.socketPlayer2.username
                    })
                }
            }
            send_stats()
        }
    })

    socket.on('new_game', (data)=>{
//        boardList = boardList.filter((item)=>item != socket.board)
//        socket.board = null
        userQueue.push(socket)
        connectUsers();
    })

    // send statistic
    function send_stats() {
        io.sockets.emit("stats_update", 
            {
                "boardList": boardList.map((item)=>{return {
                    "timestamp": item.timestamp,
                    "player1": item.player1,
                    "player2": item.player2,
                    "status": item.winner || 'playing'
            }}),
                "userQueue": userQueue.map((item)=>item.username)
            }
        )
    }
})

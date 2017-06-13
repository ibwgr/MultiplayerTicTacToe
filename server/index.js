'use strict'

import express from 'express'
import socketio from 'socket.io'
import cors from'cors'
import Board from './board'
import SocketUtil from './socketUtil'

let app = express()
let server = app.listen(3100)
let io = socketio.listen(server)

app.use(cors())

let userQueue = []
let boardList = []

io.on('connection', (socket) => {
    let username
    let board

    let socketUtil = new SocketUtil(socket)

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
        sendStats()
    }

    function connectUsers() {
        console.log('userQueue: ' + userQueue.map((item)=>item.username))
        if (userQueue.length > 1) {
            // first player is player2 !
            socket.board = new Board(userQueue.shift(), userQueue.shift())
            socket.board.socketPlayer2.board = socket.board

            addToBoardList(socket.board)
            socketUtil.startNewGame(socket.board.socketPlayer1, socket.board.socketPlayer2)
            socketUtil.changePlayer(socket.board.socketPlayer1, socket.board.socketPlayer2)

            console.log(`player1 '${socket.board.player1}' plays versus player2 '${socket.board.player2}'`)            
        }
    }

    // receive new user
    socket.on('add_user', (data)=>{
        console.log(`username '${data.username}'`)
        socket.username = data.username
        userQueue.push(socket)
        socketUtil.userAdded(data.username)
        connectUsers()
        sendStats()
    })

    // disconnect: remove user from queue
    // -> send info to other player, that game is aborted and other player wins!
    socket.on('disconnect', _=> {
      console.log('disconnect, socket: ' + socket.id)
      // remove user from waiting queue
      let i = userQueue.indexOf(socket)
      if (i >= 0){
          userQueue.splice(i, 1)
          console.log('userQueue: ' + userQueue.map((item)=>item.username))
      }
      // finish a running game
      if (socket.board){
        console.log('stop game')
        if (socket.id === socket.board.socketPlayer2.id) {
            socket.board.stopGame(socket.board.player1)
            socketUtil.gameFinished(socket.board.player1, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
        } else {
            socket.board.stopGame(socket.board.player2)
            socketUtil.gameFinished(socket.board.player2, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
        }
        socket.board = null
      }
      sendStats()
    })

    // player moves
    socket.on('player_action', (data)=>{
        console.log(`player '${data.player}' set field '${data.field}'`)
        if (socket.board) {
            socket.board.setField(data)
            let gameResult = socket.board.checkBoard()
            // send player action to the other user
            socketUtil.newMove(data.field, socket.board.socketPlayer1, socket.board.socketPlayer2)
            if (gameResult) {
                // send finish message
                socketUtil.gameFinished(gameResult, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
            } else {
                // start next move
                socketUtil.changePlayer(socket.board.socketPlayer1, socket.board.socketPlayer2)
            }
            sendStats()
        }
    })

    socket.on('new_game', (data)=>{
//        boardList = boardList.filter((item)=>item != socket.board)
        socket.board = null
        userQueue.push(socket)
        connectUsers()
    })

    // send statistic
    function sendStats() {
        io.sockets.emit("stats_update", 
            {
                "boardList": boardList.map((item)=>{return {
                    "timestamp": item.timestamp,
                    "player1": item.player1,
                    "player2": item.player2,
                    "status": item.winner || "playing"
            }}),
                "userQueue": userQueue.map((item)=>item.username)
            }
        )
    }
})

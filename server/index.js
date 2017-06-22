'use strict'

import express from 'express'
import socketio from 'socket.io'
import cors from'cors'
import Board from './board'
import SocketUtil from './socketUtil'

String.prototype.isEmpty = function() {
    return (this.length === 0 || !this.trim());
}

let app = express()
let server = app.listen(process.env.PORT || 3100)
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
    }

    function connectUsers() {
        console.log('userQueue: ' + userQueue.map((item)=>item.username))
        if (userQueue.length > 1){
            // first player is player2 !
            socket.board = new Board(userQueue.shift(), userQueue.shift())
            socket.board.socketPlayer2.board = socket.board

            addToBoardList(socket.board)
            socketUtil.startNewGame(socket.board.socketPlayer1, socket.board.socketPlayer2)
            socketUtil.changePlayer(socket.board.socketPlayer1, socket.board.socketPlayer2)

            console.log(`player1 '${socket.board.player1}' plays versus player2 '${socket.board.player2}'`)            
            sendStats({'newBoard': socket.board})
        }
    }

    function validateUser(name){
        if (name.isEmpty()){
            socket.emit('username_validation', {'msg': 'Name cannot be empty.'})
            return false
        } else {
            if (userQueue && userQueue.filter(item=>item.username===name).length > 0){
                socket.emit('username_validation', {'msg': 'Name is already used, please try another one.'})
                return false
            }
            if (name.length > 12){
                socket.emit('username_validation', {'msg': 'Name is too long. Please try another one.'})
                return false
            }
        }
        return true
    }

    function addUserToQueue(name){
        if (validateUser(name)){
            socket.username = name
            userQueue.push(socket)
            socketUtil.userAdded(name)
            connectUsers()
        }
    }

    // receive new user
    socket.on('add_user', (data)=>{
        console.log(`username '${data.username}'`)
        addUserToQueue(data.username)
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
          sendStats()
      }
      // finish a running game
      if (socket.board && socket.board.done === false){
        console.log('stop game')
        if (socket.id === socket.board.socketPlayer2.id) {
            socket.board.stopGame(socket.board.player1)
            socketUtil.gameFinished(socket.board.player1, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
        } else {
            socket.board.stopGame(socket.board.player2)
            socketUtil.gameFinished(socket.board.player2, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
        }
        socket.board = null
        sendStats({'updateBoard': socket.board})
      }
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
            sendStats({'updateBoard': socket.board})
        }
    })

    socket.on('new_game', (data)=>{
//        boardList = boardList.filter((item)=>item != socket.board)
        socket.board = null
        addUserToQueue(socket.username)
    })

    // send statistic
    function sendStats({newBoard, updateBoard}={}) {
        // stats JSON
        console.log('newBoard:'+newBoard)
        console.log('updateBoard:'+updateBoard)
        let stats = {
            'boardList': boardList.map(item=>{return {
                'timestamp': item.timestamp,
                'player1': item.player1,
                'player2': item.player2,
                'status': item.winner || 'playing',
                'change': newBoard === item ? 'new' : updateBoard === item ? 'update' : ''
            }}),
            'userQueue': userQueue.map((item)=>item.username)
        }
        io.sockets.emit('stats_update', stats)
    }
})

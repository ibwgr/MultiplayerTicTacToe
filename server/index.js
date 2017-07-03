/**
 * Tic Tac Toe Server
 * 
 * Semesterarbeit NDK HF Web und Mobile Frontend Entwicklung
 * Reto Kaufmann / Dieter Biedermann
 */

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
const PLAY_TIMER = 30

//
// websocket connection
//
io.on('connection', (socket) => {
    // new connection
    console.log('connection: ' + socket.id)

    // socket variables
    socket.socketUtil = new SocketUtil(socket)

    // socket functions
    socket.addToBoardList = (board)=>{
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
    socket.stopCurrentGame = _=>{
        // clear all timeouts
        if (socket.board.socketPlayer1.timer){
            clearTimeout(socket.board.socketPlayer1.timer)
        }
        if (socket.board.socketPlayer2.timer){
            clearTimeout(socket.board.socketPlayer2.timer)
        }
        if (socket.id === socket.board.socketPlayer2.id) {
            socket.board.stopGame(socket.board.player1)
            socket.socketUtil.gameFinished(socket.board.player1, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
        } else {
            socket.board.stopGame(socket.board.player2)
            socket.socketUtil.gameFinished(socket.board.player2, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
        }
        if (socket.timer){
            clearTimeout(socket.timer)
        }
        sendStats({'updateBoard': socket.board})
    }
    socket.changePlayer = _=>{
        let otherSocket = socket.socketUtil.changePlayer(socket.board.socketPlayer1, socket.board.socketPlayer2, PLAY_TIMER)
        if (socket.timer){
            clearTimeout(socket.timer)
        }
        otherSocket.timer = setTimeout(_=>{
          if (otherSocket.board && otherSocket.board.done === false){
              otherSocket.stopCurrentGame()
          }
        }, PLAY_TIMER*1000)
        //
    }
    socket.connectUsers = _=>{
        console.log('user added to queue, userQueue: ' + userQueue.map((item)=>item.username))
        if (userQueue.length > 1){
            // first player is player2 !
            socket.board = new Board(userQueue.shift(), userQueue.shift())
            socket.board.socketPlayer2.board = socket.board
            socket.addToBoardList(socket.board)
            socket.socketUtil.startNewGame(socket.board.socketPlayer1, socket.board.socketPlayer2)
            socket.changePlayer()
            console.log(`player1 '${socket.board.player1}' plays versus player2 '${socket.board.player2}'`)            
            sendStats({'newBoard': socket.board})
        }
    }
    socket.addUserToQueue = (name)=>{
        if (socket.validateUser(name)){
            socket.username = name
            userQueue.push(socket)
            socket.socketUtil.userAdded(name)
            socket.connectUsers()
        }
    }
    socket.validateUser = (name)=>{
        if (name.isEmpty()){
            socket.socketUtil.usernameValidation('Name cannot be empty.')
            return false
        } else {
            if (userQueue && userQueue.filter(item=>item.username===name).length > 0){
                socket.socketUtil.usernameValidation('Name is already used, please try another one.')
                return false
            }
            if (name.length > 12){
                socket.socketUtil.usernameValidation('Name is too long. Please try another one.')
                return false
            }
        }
        return true
    }

    //
    // Messages from the client
    //

    // receive new user
    socket.on('add_user', (data)=>{
        //message validation
        if (data && data.username){
            console.log(`new user, username '${data.username}'`)
            socket.addUserToQueue(data.username)
        }
    })

    // disconnect: remove user from queue
    // -> send info to other player, that game is aborted and other player wins!
    socket.on('disconnect', _=> {
      console.log('disconnect: ' + socket.id)
      // remove user from waiting queue
      let i = userQueue.indexOf(socket)
      if (i >= 0){
          userQueue.splice(i, 1)
          console.log('user removed from queue, userQueue: ' + userQueue.map((item)=>item.username))
          sendStats()
      }
      // finish a running game
      if (socket.board && socket.board.done === false){
        console.log('stop game')
        socket.stopCurrentGame(socket)
      }
    })

    // player moves
    socket.on('player_action', (data)=>{
        //message validation
        if (socket.board && data && data.player && data.field && data.field.match('^field[0-8]$')) {
            console.log(`player '${data.player}' set field '${data.field}'`)
            // clear all timeouts
            if (socket.board.socketPlayer1.timer){
                clearTimeout(socket.board.socketPlayer1.timer)
            }
            if (socket.board.socketPlayer2.timer){
                clearTimeout(socket.board.socketPlayer2.timer)
            }
            socket.board.setField(data)
            let gameResult = socket.board.checkBoard()
            // send player action to the other user
            socket.socketUtil.newMove(data.field, socket.board.socketPlayer1, socket.board.socketPlayer2)
            if (gameResult) {
                // send finish message
                console.log(`game finished, winner: '${gameResult}'`)
                socket.socketUtil.gameFinished(gameResult, socket.board.fieldsWon, socket.board.socketPlayer1, socket.board.socketPlayer2)
            } else {
                // start next move
                socket.changePlayer()
            }
            sendStats({'updateBoard': socket.board})
        }
    })

    socket.on('new_game', _=>{
        console.log('new game for user: '+socket.username)
        if (socket.board){
            socket.board = null
            socket.addUserToQueue(socket.username)
        }
    })

})

//
// general functions
//

// send statistic
function sendStats({newBoard, updateBoard}={}) {
    // stats JSON
    let stats = {
        'boardList': boardList.map((item)=>{return {
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

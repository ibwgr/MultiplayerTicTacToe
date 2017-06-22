'use strict'

import io from 'socket.io-client'

let socket = io.connect('http://localhost:3100', {reconnect: true})
//let socket = io.connect('http://warm-shelf-33316.herokuapp.com:80', {reconnect: true})

String.prototype.isEmpty = function() {
    return (this.length === 0 || !this.trim());
}

export default class{

    constructor(view){
        this.view = view
        this.player1 = 0
        this.player2 = 0
        this.player = 1
        this.running = false
        this.gameEnabled = false
        this.timer = 0

        view.registerFieldEventListener(this.fieldEventListener.bind(this))
        view.registerNameEventListener(this.nameEventListener.bind(this))
        view.registerNewGameEventListener(this.newGameEventListener.bind(this))

        // connection
        socket.on('connect', _=> {
            console.log('connected: ' + socket.id)
        })

        socket.on('disconnect', _=> {
            console.log('disconnected')
            this.running = false
            this.gameEnabled = false
            this.view.showNameInput(true)
            this.view.initBoard()
            this.view.showBoard(false)
            this.view.showNewGame(false)
            this.view.showInfo(false)
        })

        // 
        socket.on('user_added', (data)=>{
            console.log('user_added')
            this.view.setInfoText(`Hi ${data.username}, please wait for other user...`)
        })

        // messages from server...
        socket.on('start_game', (data)=>{
            console.log('game started...' + data.player1 + '/' + data.player2)
            this.view.setPlayer1(data.player1)
            this.view.setPlayer2(data.player2)
            this.running = true
            this.view.showBoard(true)
            this.view.setInfoText('New game started...')
        })

        socket.on('your_turn', (data)=>{
            console.log('your turn...' + data.player)
            this.playerToken = data.player
            this.view.setInfoText(`Hi ${data.username}, please make a move ...`)
            this.gameEnabled = true
            this.view.setInfoColor('blue')
            this.view.enableBoard(true)
            // start interval -> setInterval(..., 1000)
            this.time = data.time
            this.view.setTimer(this.time)
            let timer = window.setInterval(_=>{
                this.time--
                this.view.setTimer(this.time)
                if (this.time === 0 || !this.gameEnabled || !this.running){
                    clearInterval(timer)
                    this.view.showTimer(false)
                }
            }, 1000)
        })

        socket.on('other_turn', (data)=>{
            console.log('other turn...')
            this.view.setInfoText(`Waiting for ${data.username} ...`)
            this.gameEnabled = false
            this.view.enableBoard(false)
        })

        socket.on('new_move', (data)=>{
            console.log('new move: ' + data.field + '('+data.player+')')
            this.view.setField(data.field, data.player)
        })

        socket.on('game_finished', (data)=>{
            console.log('game finished, winner: ' + data.winner)
            this.running = false
            this.view.setFieldsWon(data.fields)
            if (data.youWon === 'yes'){
                this.view.setInfoText(`Congratulations ${data.username}, you WON`)
                this.view.setInfoColor('green')
            } else {
                console.log('winner:'+data.winner)
                if (data.winner === 'draw'){
                    this.view.setInfoText(`This game ended in a draw.`)
                    this.view.setInfoColor('orange')
                } else {
                    this.view.setInfoText(`I'm sorry ${data.username}, you LOST`)
                    this.view.setInfoColor('red')
                }
            }
            this.view.showNewGame(true)
        })

        socket.on('stats_update', (data)=>{
            this.view.renderStatistics(data)
        })

        // error handling
        socket.on('connect_failed', (data)=>{
            console.log('connection failed')
        })

        socket.on('error', (data)=>{
            console.log('error')
        })
    }

    fieldEventListener(field){
        if (this.view.isFieldEmpty(field) && this.running && this.gameEnabled){
            this.view.setField(field, this.playerToken)
            // message to server
            socket.emit('player_action', {'player': this.playerToken, 'field': field})
        }
    }

    nameEventListener(username){
        if (!username.isEmpty()) {
            // message to server
            socket.emit('add_user', {'username': username})
            this.view.showNameInput(false)
            this.view.setInfoText('..........')
        }
    }

    newGameEventListener(){
        // message to server
        socket.emit('new_game')
        this.view.initBoard()
        this.running = true
        this.view.showNewGame(false)
        this.view.setInfoText('Waiting for other user...')
        this.view.setPlayer1('')
        this.view.setPlayer2('')
        this.view.showBoard(false)
    }

}
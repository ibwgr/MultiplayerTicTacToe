'use strict'

import io from 'socket.io-client'

let socket = io.connect('http://localhost:3100', {reconnect: true})
//let socket = io.connect('http://warm-shelf-33316.herokuapp.com:80', {reconnect: true})

export default class{

    constructor(view){
        this.view = view
        this.player1 = 0
        this.player2 = 0
        this.player = 1
        this.running = false
        this.gameEnabled = false

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

            // this context is wrong !!! because of a normal FUNCTION not a ARROW FUNCTION !!!
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
            
            //this.view.setPlayerInfoText(data.player1 + ' is playing with ' + data.player2)
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
            this.view.setInfoColor('yellow')
            this.view.enableBoard(true)
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
/*
            this.view.setInfoText(`Game has been finished, winner: '${data.winner}'`)
            this.view.setInfoColor(data.youWon === 'yes' ? 'green' : 'red')
*/
            if (data.youWon === 'yes'){
                this.view.setInfoText(`Congratulations ${data.username}, you WON`)
                this.view.setInfoColor('green')
            } else {
                this.view.setInfoText(`I'm sorry ${data.username}, you LOST`)
                this.view.setInfoColor('red')
            }
            this.view.showNewGame(true)
        })

        socket.on('stats_update', (data)=>{
            console.log(data)
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
        // message to server
        socket.emit('add_user', {'username': username})
        this.view.showNameInput(false)
        this.view.setInfoText('..........')
/*        this.view.showInfo(true)
*/    }

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
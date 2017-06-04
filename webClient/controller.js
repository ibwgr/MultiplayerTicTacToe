'use strict'

import io from 'socket.io-client'

let socket = io.connect('http://localhost:3100', {reconnect: true})

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
        socket.on('connect', function() {
            console.log(socket.id)
        })

        // messages from server...
        socket.on('start_game', (data)=>{
/*            console.log('game started...' + data.player1 + '/' + data.player2)
            this.view.setInfoText(data.player1 + ' is playing with ' + data.player2)
*/            this.running = true
            this.view.showBoard(true)
        })

        socket.on('your_turn', (data)=>{
            console.log('your turn...' + data.player)
            this.playerToken = data.player
            this.view.setInfoText('it is your turn...')
            this.gameEnabled = true
        })

        socket.on('other_turn', (data)=>{
            console.log('other turn...')
            this.view.setInfoText(`Waiting for user '${data.username}' ...`)
            this.gameEnabled = false
        })

        socket.on('new_move', (data)=>{
            console.log('new move: ' + data.field + '('+data.player+')')
            this.view.setField(data.field, data.player)
        })

        socket.on('game_finished', (data)=>{
            console.log('game finished, winner: ' + data.winner)
            this.view.setInfoText(`Game has been finished, winner: '${data.winner}'`)
            this.running = false
            this.view.showNewGame(true)

/*            for (field of data.fields){
                $doc.querySelectorAll("#field"+field).classList.add('fieldWon')
            }
            if (this.$newGame === undefined){
                this.$newGame = this.$doc.createElement("a")
                this.$newGame.innerText = "<start new game>"
                this.$newGame.addEventListener("click", this.onClickNewGame.bind(this))
                this.$newGame.setAttribute("id", "newGame")
                this.$newGame.setAttribute("href", "#")
            }
            this.$resultDiv.appendChild(this.$newGame)
*/        })

        socket.on('stats_update', (data)=>{
            console.log(data)
            this.view.renderStatistics(data)
        })

        // error handling
        socket.on('connect_failed', (data)=>{
            console.log('connection failed')
        })
    }

    fieldEventListener(field){
        if (this.view.isFieldFull(field) && this.running && this.gameEnabled){
            this.view.setField(field, this.playerToken)
            socket.emit('player_action', {'player': this.playerToken, 'field': field})
        }
    }

    nameEventListener(username){
        socket.emit('add_user', {'username': username})
        this.view.showNameInput(false)
        this.view.showInfo(true)
    }

    newGameEventListener(){
        socket.emit('new_game')
        this.view.initBoard()
        this.running = true
        this.showNewGame(false)
        this.setInfoText()
        this.showBoard(false)
    }

/*    onClickNewGame(ev){
        socket.emit('new_game')

        this.view.initBoard()

        this.$fields.forEach(function(element) {
            element.innerText = ""
            element.classList.remove("fieldWon")
            element.querySelector(".token").classList.remove("setX")
            element.querySelector(".token").classList.remove("setO")
            element.querySelector(".token").classList.remove("setXbig")
            element.querySelector(".token").classList.remove("setObig")
        }, this);

        this.running = true
        this.view.showNewGame(false)
        this.view.showBoard(false)
        this.view.setInfoText('Please wait for other player...')

        this.$resultDiv.innerText = ""
        this.$waiting.innerText = 'Please wait for other player...'
        this.$board.classList.add('hidden')
        
    }

    onClickField(ev){
        if (ev.target.innerText === "" && this.running && this.gameEnabled){
//            ev.target.innerText = this.playerToken
            ev.target.querySelector(".token").classList.add(this.playerToken === 'x' ? 'setX' : 'setO')
            ev.target.querySelector(".token").classList.add(this.playerToken === 'x' ? 'setXbig' : 'setObig')
            this.send_action(this.player, ev.target.id)
        }
    }

    onChangeNameInput(ev){
        this.add_user(ev.target.value.trim())
        this.$nameInput.classList.add("hidden")
        this.$waiting.classList.remove("hidden")
    }

    add_user(username) {
        socket.emit('add_user', {'username': username})
    }

    send_action(player, field) {
        socket.emit('player_action', {'player': this.playerToken, 'field': field})
    }
*/
}
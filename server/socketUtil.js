'use strict'

export default class {
    constructor(socket) {
        this.socket = socket
    }

    /**
     * event: start_game
     * json: {'player1': 'user1', 'player2': 'user2'}
     */
    startNewGame(player1, player2){
        let data = {
            'player1':player1.username,
            'player2':player2.username
        }
        this.socket.emit('start_game', data)
        if (this.socket === player1) {
            this.socket.to(player2.id).emit('start_game', data)
        } else {
            this.socket.to(player1.id).emit('start_game', data)
        }
    }

    /**
     * event: your_turn, other_turn
     * json: {'player': 'x', 'username': 'user2', 'time': '30'}
     * returns the socket of the new player
     */
    changePlayer(player1, player2, time){
        let data = {
            'time': time
        }
        if (this.socket === player1) {
            data.player = 'x'
            data.username = player2.username
            this.socket.emit('other_turn', data)
            this.socket.to(player2.id).emit('your_turn', data)
            return player2
        } else {
            data.player = 'o'
            data.username = player1.username
            this.socket.emit('other_turn', data)
            this.socket.to(player1.id).emit('your_turn', data)
            return player1
        }
    }

    /**
     * event: user_added
     * json: {'username': 'user1'}
     * 
     */
    userAdded(username){
       let data = {
            'username': username
       }
       this.socket.emit('user_added', data)
    }

    /**
     * event: username_validation
     * json: {'msg': 'error message'}
     * 
     */
    usernameValidation(msg){
        let data = {
            'msg': msg
        }
        this.socket.emit('username_validation', data)
     }

    /**
     * event: new_move
     * json: {'field': '0', 'player': 'x'}
     * 
     */
    newMove(field, player1, player2){
        let data = {
            'field': field
        }
        if (this.socket === player1){
            data.player = 'o'
            this.socket.to(player2.id).emit('new_move', data)
        } else {
            data.player = 'x'
            this.socket.to(player1.id).emit('new_move', data)
        }
    }

    /**
     * event: game_finished
     * json: {'winner': 'user1', 'fields': '[0,1,2]', 'username': 'user1', 'youWon': 'yes'}
     * 
     */
    gameFinished(result, fields, player1, player2){
        let data = {
            'winner': result,
            'fields': fields,
            'username': '',
            'youWon': ''
        }
        if (this.socket === player1){
            // player1
            data.username = player1.username
            data.youWon = result === player1.username ? 'yes' : 'no'
            this.socket.emit('game_finished', data)
            // player2
            data.username = player2.username
            data.youWon = result === player2.username ? 'yes' : 'no'
            this.socket.to(player2.id).emit('game_finished', data)
        } else {
            // player1
            data.username = player1.username
            data.youWon = result === player1.username ? 'yes' : 'no'
            this.socket.to(player1.id).emit('game_finished', data)
            // player2
            data.username = player2.username
            data.youWon = result === player2.username ? 'yes' : 'no'
            this.socket.emit('game_finished', data)
        }
    }

}
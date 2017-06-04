'use strict'

const fieldEventListener = Symbol()
const nameEventListener = Symbol()
const newGameEventListener = Symbol()
const renderStatsItem = Symbol()

export default class{

    constructor($doc){
        this.$doc = $doc

        this.$nameInput = this.$doc.querySelector('#name_input')
        this.$board = this.$doc.querySelector('#board')
        this.$info = this.$doc.querySelector("#info")
        this.$statsDiv = this.$doc.querySelector("#stats")
        this.$newGame = this.$doc.querySelector("#newGame")
        this.$fields = $doc.querySelectorAll("div.field")
        
        this.$nameInput.addEventListener("change", this[nameEventListener].bind(this))
        this.$newGame.addEventListener("click", this[newGameEventListener].bind(this))
        this.$fields.forEach(function(element) {
            element.addEventListener("click", this[fieldEventListener].bind(this))
        }, this);
    }

    [fieldEventListener]({target}){
        // fire event, invoke subscriber
        this.fieldEventListener &&
        this.fieldEventListener(target.id)
    }

    registerFieldEventListener(handler){
        this.fieldEventListener = handler
    }

    [nameEventListener]({target}){
        // fire event, invoke subscriber
        this.nameEventListener &&
        this.nameEventListener(target.value.trim())
    }

    registerNameEventListener(handler){
        this.nameEventListener = handler
    }

    [newGameEventListener]({target}){
        // fire event, invoke subscriber
        this.newGameEventListener &&
        this.newGameEventListener()
    }

    registerNewGameEventListener(handler){
        this.newGameEventListener = handler
    }

    setField(field, playerToken){
        this.$doc.querySelector('#'+field).classList.add(playerToken === 'x' ? 'setX' : 'setO')
    }

    setInfoText(text){
        this.$info.innerText = text
    }

    showNameInput(show){
        if (show){
            this.$nameInput.classList.remove('hidden')
        } else {
            this.$nameInput.classList.add('hidden')
        }
    }

    showInfo(show){
        if (show){
            this.$info.classList.remove('hidden')
        } else {
            this.$info.classList.add('hidden')
        }
    }

    showNewGame(show){
        if (show){
            this.$newGame.classList.remove('hidden')
        } else {
            this.$newGame.classList.add('hidden')
        }
    }

    showBoard(show){
        if (show){
            this.$board.classList.remove('hidden')
        } else {
            this.$board.classList.add('hidden')
        }
    }

    initBoard(){
        this.$fields.forEach(function(element){
            element.classList.remove("fieldWon")
            element.classList.remove("setX")
            element.classList.remove("setO")
        })
    }

    isFieldFull(field){
        console.log(field)
        let element = this.$doc.querySelector('#'+field)
        return !(element.classList.contains('setX') ||
                element.classList.contains('setO'))
    }

    //
    renderStatistics(data){
        this.$statsDiv.innerHTML = data.boardList.map(this[renderStatsItem]).join('')
    }

    [renderStatsItem](item) {
        return `<li ${item.hasChanged?'class="changed"':''}>
        <div>${item.timestamp}</div>
        <div>${item.player1}</div>
        <div>${item.player2}</div>
        <div>${item.status}</div>
        </li>`
    }

}
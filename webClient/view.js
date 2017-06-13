'use strict'

const fieldEventListener = Symbol()
const nameEventListener = Symbol()
const newGameEventListener = Symbol()
const renderStatsItem = Symbol()

export default class{

    constructor($doc){
        this.$doc = $doc

        this.$nameInput = this.$doc.querySelector('#nameInput')
        this.$board = this.$doc.querySelector('#board')
        this.$info = this.$doc.querySelector("#info")
        this.$playerInfo = this.$doc.querySelector("#playerInfo")
        this.$player1 = this.$doc.querySelector("#player1")
        this.$player2 = this.$doc.querySelector("#player2")
        this.$statsDiv = this.$doc.querySelector("#stats")
        this.$newGame = this.$doc.querySelector("#newGame")
        this.$fields = $doc.querySelectorAll("div.field")
        this.$infoContainer = this.$doc.querySelector('#infoContainer')
        
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
        if (field){
            this.$doc.querySelector('#'+field).querySelector((playerToken === 'x' ? '.setX' : '.setO')).classList.remove('hidden')
        }
    }

    showInfo(show){
        if (show){
            if (this.$infoContainer.classList.contains('hidden')){
                this.$infoContainer.classList.remove('hidden')
            }
        } else {
            this.$infoContainer.classList.add('hidden')
        }
    }

    setInfoText(text){
        this.showInfo(true)
        this.$info.innerText = text
    }

    setInfoColor(text){
        this.$infoContainer.classList.add(text)
        window.setTimeout(_=>this.$infoContainer.classList.remove(text), 1500)
    }

    setPlayer1(text){
        this.$player1.innerText = text
    }

    setPlayer2(text){
        this.$player2.innerText = text
    }

    setPlayerInfoText(text){
        this.showInfo(true)
        this.$playerInfo.innerText = text
    }

    showNameInput(show){
        if (show){
            this.$nameInput.classList.remove('hidden')
        } else {
            this.$nameInput.classList.add('hidden')
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

    enableBoard(show){
        if (show){
            this.$board.classList.remove('inactivate')
        } else {
            this.$board.classList.add('inactivate')
        }
    }

    initBoard(){
        this.$fields.forEach(function(element){
            element.classList.remove('fieldWon')
            if (!element.querySelector('.setX').classList.contains('hidden')){
                element.querySelector('.setX').classList.add('hidden')
            }
            if (!element.querySelector('.setO').classList.contains('hidden')){
                element.querySelector('.setO').classList.add('hidden')
            }
        })
    }

    isFieldEmpty(field){
        if (field){
            let element = this.$doc.querySelector('#'+field)
            return (element.querySelector('.setX').classList.contains('hidden') &&
                    element.querySelector('.setO').classList.contains('hidden'))
        }
        return false
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
'use strict'

import 'es6-symbol'

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
        this.$timer = this.$doc.querySelector('#timer')
        
        this.$nameInput.addEventListener("change", this[nameEventListener].bind(this))
        this.$newGame.addEventListener("click", this[newGameEventListener].bind(this))

        // for .. of loop
        for (let field of this.$fields) {
            field.addEventListener("click", this[fieldEventListener].bind(this))
        }
/*
        // with SPREAD parameter
        [...this.$fields].forEach(item=>{
            item.addEventListener("click", this[fieldEventListener].bind(this))
        })

        // Array.from function
        Array.from(this.$fields).forEach(item=>{
            item.addEventListener("click", this[fieldEventListener].bind(this))
        })
*/
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

    setFieldsWon(fields){
        if (fields){
            fields.forEach(item=>{
                this.$doc.querySelector('#field'+item).classList.add('fieldWon')
            })
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
        window.setTimeout(_=>this.$infoContainer.classList.remove(text), 2000)
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

    setTimer(time){
        this.showTimer(true)
        this.$timer.innerHTML = `you have <span class="timerSeconds">${time}</span> seconds left`
    }

    showTimer(show){
        if (show){
            this.$timer.classList.remove('hidden')
        } else {
            this.$timer.classList.add('hidden')
        }
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
        for (let field of this.$fields) {
            field.classList.remove('fieldWon')
            if (!field.querySelector('.setX').classList.contains('hidden')){
                field.querySelector('.setX').classList.add('hidden')
            }
            if (!field.querySelector('.setO').classList.contains('hidden')){
                field.querySelector('.setO').classList.add('hidden')
            }
        }
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
        this.$statsDiv.innerHTML = data.boardList.map(this[renderStatsItem].bind(this)).join('')
    }

    [renderStatsItem](item) {
        // animation
        if (item.change) {
            window.setTimeout(_=>{
                for (let element of this.$doc.querySelectorAll('.changeNew')){
                    element.classList.remove('changeNew')
                }
                for (let element of this.$doc.querySelectorAll('.changeUpdate')){
                    element.classList.remove('changeUpdate')
                }
            }, 800)
        }

        // return html
        return `<li ${item.change === 'new' ? 'class="changeNew"' : item.change === 'update' ? 'class="changeUpdate"' : ''}>
        <div class="col1">${item.timestamp}</div>
        <div class="col2 ${item.status === item.player1 ? 'winner' : ''}">${item.player1}</div>
        <div class="col3 ${item.status === item.player2 ? 'winner' : item.status === 'playing' ? 'playing' : ''}">${item.player2}</div>
        </li>`
    }

}
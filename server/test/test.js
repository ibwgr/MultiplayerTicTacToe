'use strict'

import assert from 'assert'
import Board from '../board'

describe('board', function(){

    beforeEach(function(){
        this.board = new Board({username: 'user1'},{username: 'user2'})
    })

    describe('test setField', function(){
        it(`should return [' ','x',' ',' ',' ',' ',' ',' ',' '] for x on field 1`, function(){
            let data = {'field': 'field1', 'player': 'x'}
            this.board.setField(data)
            let result = this.board.fields
            assert.deepEqual([' ','x',' ',' ',' ',' ',' ',' ',' '], result)
        })
        it(`should return [' ',' ',' ',' ',' ',' ','o',' ',' '] for o on field 6`, function(){
            let data = {'field': 'field6', 'player': 'o'}
            this.board.setField(data)
            let result = this.board.fields
            assert.deepEqual([' ',' ',' ',' ',' ',' ','o',' ',' '], result)
        })
    })

    describe('test checkFields', function(){
        it(`should return 'user1' for fields ooo`, function(){
            this.board.fields = ['o','o','o',' ',' ',' ',' ',' ',' ']
            let data = [0,1,2]
            let result = this.board.checkFields(data)
            assert.equal('user1', result)
        })
        it(`should return 'user2' for fields xxx`, function(){
            this.board.fields = [' ','x',' ',' ','x',' ',' ','x',' ']
            let data = [1,4,7]
            let result = this.board.checkFields(data)
            assert.equal('user2', result)
        })
    })

    describe('test checkBoard', function(){
        it(`should return [' ','x',' ',' ',' ',' ',' ',' ',' '] for x on field 1`, function(){
            this.board.fields = ['x',' ',' ',' ','x',' ',' ',' ','x']
            let result = this.board.checkBoard()
            assert.equal('user2', result)
        })
    })

})
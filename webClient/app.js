'use strict'

import View from './view'
import Controller from './controller'
import 'babel-polyfill'
//import Store from './store'

/* MVC refactoring...
let view = new View(window.document)
let store = new Store()
let ctrl = new Controller(view, store)
*/

//let store = new Store()
let view = new View(window.document)
let ctrl = new Controller(view)

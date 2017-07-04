/*
 * Tic Tac Toe webClient
 * 
 * Semesterarbeit NDK HF Web und Mobile Frontend Entwicklung
 * Reto Kaufmann / Dieter Biedermann
 */
'use strict'

import View from './view'
import Controller from './controller'

let view = new View(window.document)
let ctrl = new Controller(view)

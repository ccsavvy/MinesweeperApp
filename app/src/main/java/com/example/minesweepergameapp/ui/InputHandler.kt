package com.example.minesweepergameapp.ui

interface InputHandler {
    fun getGridSize(): Int
    fun getMineCount(maxMines: Int): Int
    fun getCellToUncover(): Pair<Int, Int>
    fun getPlayAgain(): Boolean
}
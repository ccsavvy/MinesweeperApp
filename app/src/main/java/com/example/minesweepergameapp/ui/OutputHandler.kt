package com.example.minesweepergameapp.ui

import com.example.minesweepergameapp.game.Grid

interface OutputHandler {
    fun displayGrid(grid: Grid)
    fun displayMessage(message: String)
}
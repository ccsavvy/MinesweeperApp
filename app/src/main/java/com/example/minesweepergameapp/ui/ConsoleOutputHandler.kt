package com.example.minesweepergameapp.ui

import com.example.minesweepergameapp.game.Grid

class ConsoleOutputHandler : OutputHandler{
    override fun displayGrid(grid: Grid) {
        print("  ")
        for (i in 1 .. grid.size) {
            print("$i ")
        }
        println()

        for (row in 0 until grid.size) {
            print("${('A' + row)} ")
            for (col in 0 until grid.size) {
                val cell = grid.cells[row][col]
                val display = when {
                    !cell.isRevealed -> "_ "
                    cell.isMine -> "X "
                    cell.adjacentMines > 0 -> cell.adjacentMines.toString().plus(" ")
                    else -> "0 "
                }
                print(display)
            }
            println()
        }
        println()
    }

    override fun displayMessage(message: String) {
        println(message)
    }
}
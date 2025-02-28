package com.example.minesweepergameapp

import com.example.minesweepergameapp.game.Game
import com.example.minesweepergameapp.game.Grid
import com.example.minesweepergameapp.game.RandomMinePlacer
import com.example.minesweepergameapp.ui.ConsoleInputHandler
import com.example.minesweepergameapp.ui.ConsoleOutputHandler
import kotlin.math.roundToInt

//class MineSweeperApp {

    fun main() {
        val inputHandler = ConsoleInputHandler()
        val outputHandler = ConsoleOutputHandler()
        var playAgain = true

        outputHandler.displayMessage("Welcome to Minesweeper!")

        while (playAgain) {
            val gridSize = inputHandler.getGridSize()
            val maxMines = (gridSize * gridSize * 0.35).roundToInt()
            val mineCount = inputHandler.getMineCount(maxMines)

            val minePlacer = RandomMinePlacer(mineCount)
            val grid = Grid(gridSize, minePlacer)
            val game = Game(grid, outputHandler)

            // Initial grid display
            println("\nHere is your minefield:")
            outputHandler.displayGrid(grid)

            while (!game.isGameOver() && !game.isGameWon()) {
                val (row, col) = inputHandler.getCellToUncover()
                game.uncoverCell(row, col, true)

                if (!game.isGameOver() && !game.isGameWon()) {
                    // Updated grid display after each input
                    println("Here is your updated minefield:")
                    outputHandler.displayGrid(grid)
                }
            }

            playAgain = inputHandler.getPlayAgain()
        }
    }
//}
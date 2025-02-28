package com.example.minesweepergameapp.game

import com.example.minesweepergameapp.ui.OutputHandler

class Game(private val grid: Grid, private val outputHandler: OutputHandler) {

    private var gameOver = false
    private var gameWon = false
    private val revealedCells = mutableSetOf<Pair<Int, Int>>()

    fun uncoverCell(row: Int, col: Int, isUserInput: Boolean = false) {
        if (!grid.isValid(row, col) || gameOver || gameWon) return

        val cell = grid.cells[row][col]
        if (cell.isRevealed) return

        if (isUserInput && !cell.isMine) {
            outputHandler.displayMessage("This square contains ${cell.adjacentMines} adjacent mines. ")
        }

        cell.isRevealed = true
        revealedCells.add(Pair(row, col))

        if (cell.isMine) {
            gameOver = true
            outputHandler.displayMessage("Oh no, you detonated a mine! Game over.")
            return
        }

        if (cell.adjacentMines == 0) {
            uncoverAdjacentCells(row, col)
        }

        if (isUserInput && checkWinCondition()) {
            gameWon = true
            outputHandler.displayMessage("Congratulations, you have won the game!")
            return

        }
    }

    private fun uncoverAdjacentCells(row: Int, col: Int) {
        for (i in maxOf(0, row - 1)..minOf(grid.size - 1, row + 1)) {
            for (j in maxOf(0, col - 1)..minOf(grid.size - 1, col + 1)) {
                if ((i != row || j != col) && !grid.cells[i][j].isRevealed && !grid.cells[i][j].isMine) {
                    if (grid.cells[i][j].adjacentMines == 0) {
                        uncoverCell(i, j)
                    } else {
                        val cellPair = Pair(i, j)
                        grid.cells[i][j].isRevealed = true
                        revealedCells.add(cellPair)
                    }
                }
            }
        }
    }

    private fun checkWinCondition(): Boolean {
        for (row in 0 until grid.size) {
            for (col in 0 until grid.size) {
                if (!grid.cells[row][col].isMine && !grid.cells[row][col].isRevealed) {
                    return false
                }
            }
        }
        return true
    }

    fun isGameOver(): Boolean = gameOver
    fun isGameWon(): Boolean = gameWon
}
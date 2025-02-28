package com.example.minesweepergameapp.ui

class ConsoleInputHandler : InputHandler {
    override fun getGridSize(): Int {
        print("Enter the size of the grid (e.g. 4 for a 4x4 grid): ")
        return readln().toIntOrNull() ?: getGridSize()
    }

    override fun getMineCount(maxMines: Int): Int {
        print("Enter the number of mines to place on the grid (maximum is $maxMines): ")
        val input = readln().toIntOrNull() ?: getMineCount(maxMines)
        return if (input > maxMines) getMineCount(maxMines) else input
    }

    override fun getCellToUncover(): Pair<Int, Int> {
        print("Select a square to reveal (e.g. A1): ")
        val input = readln().uppercase()
        if (input.length < 2)  return getCellToUncover()
        val row = input[0] - 'A'
        val col = input.substring(1).toIntOrNull()?.minus(1) ?: return getCellToUncover()
        return Pair(row, col)
    }

    override fun getPlayAgain(): Boolean {
        print("Press any key to play again...")
        readln()
        return true
    }
}
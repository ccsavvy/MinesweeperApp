package com.example.minesweepergameapp.game

class Grid( val size: Int, minePlacer: MinePlacer) {

    val cells: Array<Array<Cell>> = Array(size) { Array(size) { Cell() } }

    init {
        minePlacer.placeMines(this)
        calculateAdjacentMines()
    }

    private fun calculateAdjacentMines() {
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (!cells[row][col].isMine) {
                    cells[row][col].adjacentMines = countAdjacentMines(row, col)
                }
            }
        }
    }

    private fun countAdjacentMines(row: Int, col: Int): Int {
        var count = 0
        for (i in maxOf(0, row - 1)..minOf(size - 1, row + 1)) {
            for (j in maxOf(0, col - 1)..minOf(size - 1, col + 1)) {
                if ((i != row || j != col) && cells[i][j].isMine) {
                    count++
                }
            }
        }
        return count
    }

    fun isValid(row: Int, col: Int): Boolean {
        return row in 0 until size && col in 0 until size
    }
}
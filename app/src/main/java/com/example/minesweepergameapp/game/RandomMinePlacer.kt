package com.example.minesweepergameapp.game

import kotlin.random.Random

class RandomMinePlacer(private val mineCount: Int): MinePlacer {
    override fun placeMines(grid: Grid) {
        var minesPlaced = 0
        while (minesPlaced < mineCount) {
            val row = Random.nextInt(grid.size)
            val col = Random.nextInt(grid.size)
            if (!grid.cells[row][col].isMine) {
                grid.cells[row][col].isMine = true
                minesPlaced++
            }
        }
    }
}
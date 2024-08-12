package com.example.minesweepergameapp

import junit.framework.Assert.assertEquals
import org.junit.Test

class MinesweeperGameTest {

    @Test
    fun testGameInitialization() {
        val game = MinesweeperGame()
        assertEquals(0, game.gridSize)
        assertEquals(0, game.mineCount)
    }

    @Test
    fun testGridInitialization() {
        val game = MinesweeperGame()
        game.setupGame(4, 2)
        assertEquals(4, game.gridSize)
        assertEquals(2, game.mineCount)
    }

    @Test
    fun testMinePlacement() {
        val game = MinesweeperGame()
        game.setupGame(4, 2)
        var mineCount = 0
        for (row in game.grid) {
            for (cell in row) {
                if (cell.isMine) mineCount++
            }
        }
        assertEquals(2, mineCount)
    }
}
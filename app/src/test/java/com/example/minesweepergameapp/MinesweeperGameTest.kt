package com.example.minesweepergameapp

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

class MinesweeperGameTest {

    @Test
    fun testGameInitialization() {
        val game = MinesweeperGame()
        assertEquals(4, game.gridSize) // Default size should be 4
        assertEquals(0, game.mineCount) // Default mine count should be 0
    }

    @Test
    fun testGridInitialization() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 2)
        assertEquals(4, game.grid.size)
        assertEquals(2, game.grid.flatten().count { it.isMine })
    }

    @Test
    fun testMinePlacement() {
        val game = MinesweeperGame()
        game.setupGame(4, 2)
        val mineCount = game.grid.flatten().count { it.isMine }
        assertEquals(2, mineCount)
    }

    @Test
    fun testUncoveringASafeSquare() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 2)
        val result = game.uncoverSquare(0, 0)
        assertTrue(result)
        assertTrue(game.grid[0][0].isRevealed)
    }

    @Test
    fun testUncoveringAMine() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 1)
        // Force placing a mine at (0, 0)
        game.grid[0][0].isMine = true
        val result = game.uncoverSquare(0, 0)
        assertFalse(result)
        assertTrue(game.grid[0][0].isRevealed)
    }

    @Test
    fun testAdjacentMineCount() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 3, mineCount = 0)
        game.grid[0][1].isMine = true
        game.grid[1][0].isMine = true
        game.grid[1][1].isMine = true
        val adjacentMines = game.countAdjacentMines(0, 0)
        assertEquals(3, adjacentMines)
    }

    @Test
    fun testGameWin() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 2, mineCount = 1)
        // Force placing a mine at (0, 0)
        game.grid[0][0].isMine = true
        game.uncoverSquare(0, 1)
        game.uncoverSquare(1, 0)
        game.uncoverSquare(1, 1)
        assertTrue(game.checkWinCondition())
    }

    @Test
    fun testInvalidGridSizeInput() {
        val game = MinesweeperGame()
        assertFailsWith<IllegalArgumentException> {
            game.setupGame(gridSize = -1, mineCount = 0)
        }
    }

    @Test
    fun testInvalidMineCountInput() {
        val game = MinesweeperGame()
        assertFailsWith<IllegalArgumentException> {
            game.setupGame(gridSize = 4, mineCount = 20)
        }
    }
}

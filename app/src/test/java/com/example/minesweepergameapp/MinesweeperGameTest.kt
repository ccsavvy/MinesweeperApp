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
        assertEquals(0, game.gridSize)
        assertEquals(0, game.mineCount)
    }

    @Test
    fun testGameDefaultGridSizeAndMineCountInitialization() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 5)
        assertEquals(4, game.gridSize) // Default size should be 4
        assertEquals(5, game.mineCount) // Default mine count should be 5
    }

    @Test
    fun testGridInitialization() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 5)
        assertEquals(4, game.grid.size)
        assertEquals(5, game.grid.flatten().count { it.isMine })
    }

    @Test
    fun testMinePlacement() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 5)
        val mineCount = game.grid.flatten().count { it.isMine }
        assertEquals(5, mineCount)
    }

    @Test
    fun testUncoveringASafeSquare() {
        val game = MinesweeperGame()
        game.initializeCustomGrid(Array(4) { Array(4) { Cell() } })
        game.placeCustomMines(listOf(1 to 1))  // Place a mine at (1, 1)

        // Now (0, 0) should be a safe square
        val result = game.uncoverSquare(0, 0)
        assertTrue(result)
        assertTrue(game.grid[0][0].isRevealed)
    }

    @Test
    fun testUncoveringAMine() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 5)
        // Force placing a mine at (0, 0)
        game.grid[0][0].isMine = true
        val result = game.uncoverSquare(0, 0)
        assertFalse(result)
        assertTrue(game.grid[0][0].isRevealed)
    }

    @Test
    fun testAdjacentMineCount() {
        val game = MinesweeperGame()
        game.setupGame(gridSize = 4, mineCount = 5)
        game.grid[0][1].isMine = true
        game.grid[1][0].isMine = true
        game.grid[1][1].isMine = true
        val adjacentMines = game.countAdjacentMines(0, 0)
        assertEquals(3, adjacentMines)
    }

    @Test
    fun testGameWin() {
        val game = MinesweeperGame()
        game.initializeCustomGrid(Array(4) { Array(4) { Cell() } })

        // Manually place mines at specific locations
        game.placeCustomMines(listOf(
            0 to 0, 0 to 1, 1 to 0, 2 to 2, 3 to 3
        ))

        // Uncover squares that are not mines
        assertTrue(game.uncoverSquare(0, 2)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(0, 3)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(1, 1)) // 3 adjacent mines
        assertTrue(game.uncoverSquare(1, 2)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(1, 3)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(2, 0)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(2, 1)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(2, 3)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(3, 0)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(3, 1)) // 1 adjacent mine
        assertTrue(game.uncoverSquare(3, 2)) // 1 adjacent mine

        // Check if the game recognizes a win condition
        assertTrue(game.checkWinCondition())
    }

    @Test
    fun testUncoverSpecificSquare() {
        val game = MinesweeperGame()
        game.initializeCustomGrid(Array(4) { Array(4) { Cell() } })
        game.placeCustomMines(listOf(1 to 1))

        // Uncover a specific square and check adjacent mines
        assertTrue(game.uncoverSquare(0, 0)) // Should reveal square with 1 adjacent mine
        assertEquals(1, game.grid[0][0].adjacentMines)

        // Ensure the win condition is not yet met
        assertFalse(game.checkWinCondition())
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

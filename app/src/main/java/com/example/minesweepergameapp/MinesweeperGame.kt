package com.example.minesweepergameapp

import kotlin.random.Random


class MinesweeperGame {
    lateinit var grid: Array<Array<Cell>>
    var gridSize: Int = 0
    var mineCount: Int = 0

    fun start() {
        while (true) {
            setupGame()
            playGame()
            println("Press any key to play again...")
            readlnOrNull() // Wait for any key to be pressed
        }
    }

    fun setupGame(gridSize: Int = 0, mineCount: Int = 0) {
        println("Welcome to Minesweeper!")
        println()

        this.gridSize = if (gridSize == 0) getGridSize() else gridSize
        this.mineCount = if (mineCount == 0) getMineCount() else mineCount

        initializeGrid()
        placeMines()

        // Initial grid display
        println()
        println("Here is your minefield:")
        displayGrid()
    }

    private fun playGame() {
        while (true) {
            val (x, y) = getUserInput()
            if (!uncoverSquare(x, y)) {
                println("Oh no, you detonated a mine! Game over.")
                //displayGrid(true)
                break
            } else {
                val adjacentMines = grid[x][y].adjacentMines
                println("This square contains $adjacentMines adjacent mine${if (adjacentMines != 1) "s" else ""}.")
                println()
            }

            // Updated grid display after each input
            println("Here is your updated minefield:")
            displayGrid()

            if (checkWinCondition()) {
                println()
                println("Congratulations, you have won the game!")
                break
            }
        }
    }

    @JvmName("getGridSizeMethod")
    private fun getGridSize(): Int {
        print("Enter the size of the grid (e.g., 4 for a 4x4 grid): ")
        return readlnOrNull()?.toIntOrNull()?.coerceAtLeast(1) ?: 4
    }

    @JvmName("getMineCountMethod")
    private fun getMineCount(): Int {
        val maxMines = (gridSize * gridSize * 0.35).toInt()
        print("Enter the number of mines to place on the grid (maximum is $maxMines): ")
        return if (maxMines > 0 ) {
            // Fix java.lang.IllegalArgumentException: Cannot coerce value to an empty range:
            // maximum 0 is less than minimum 1.
            //at kotlin.ranges.RangesKt___RangesKt.coerceIn(_Ranges.kt:1413)
            readlnOrNull()?.toIntOrNull()?.coerceIn(1, maxMines) ?: maxMines
        } else {
            0
        }
    }

    private fun initializeGrid() {
        grid = Array(gridSize) { Array(gridSize) { Cell() } }
    }

    private fun placeMines() {
        var minesPlaced = 0
        while (minesPlaced < mineCount) {
            val x = Random.nextInt(gridSize)
            val y = Random.nextInt(gridSize)
            if (!grid[x][y].isMine) {
                grid[x][y].isMine = true
                minesPlaced++
            }
        }
    }

    private fun uncoverSquare(x: Int, y: Int): Boolean {
        if (grid[x][y].isRevealed) return true

        grid[x][y].isRevealed = true
        if (grid[x][y].isMine) return false

        grid[x][y].adjacentMines = countAdjacentMines(x, y)
        if (grid[x][y].adjacentMines == 0) {
            uncoverAdjacentSquares(x, y)
        }

        return true
    }

    private fun countAdjacentMines(x: Int, y: Int): Int {
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val nx = x + i
                val ny = y + j
                if (nx in 0 until gridSize && ny in 0 until gridSize && grid[nx][ny].isMine) {
                    count++
                }
            }
        }
        return count
    }

    private fun uncoverAdjacentSquares(x: Int, y: Int) {
        for (i in -1..1) {
            for (j in -1..1) {
                val nx = x + i
                val ny = y + j
                if (nx in 0 until gridSize && ny in 0 until gridSize && !grid[nx][ny].isRevealed) {
                    uncoverSquare(nx, ny)
                }
            }
        }
    }

    private fun getUserInput(): Pair<Int, Int> {
        while (true) {
            println()
            print("Select a square to reveal (e.g., A1): ")
            val input = readlnOrNull()?.uppercase() ?: ""
            if (input.matches(Regex("[A-Z][0-9]+"))) {
                val x = input[0] - 'A'
                val y = input.substring(1).toInt() - 1
                if (x in 0 until gridSize && y in 0 until gridSize) {
                    return Pair(x, y)
                }
            }
            println("Invalid input. Please enter a valid square.")
        }
    }

    private fun checkWinCondition(): Boolean {
        for (row in grid) {
            for (cell in row) {
                if (!cell.isMine && !cell.isRevealed) {
                    return false
                }
            }
        }
        return true
    }

    private fun displayGrid(revealMines: Boolean = false) {
        print("  ")
        for (i in 1..gridSize) print("$i ")
        println()

        for (i in 0 until gridSize) {
            print("${'A' + i} ")
            for (j in 0 until gridSize) {
                when {
                    !grid[i][j].isRevealed && !revealMines -> print("_ ")
                    grid[i][j].isMine -> print("X ")
                    else -> print("${grid[i][j].adjacentMines} ")
                }
            }
            println()
        }
    }
}

class Cell {
    var isMine: Boolean = false
    var isRevealed: Boolean = false
    var adjacentMines: Int = 0
}
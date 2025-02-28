package com.example.minesweepergameapp.mvc

import kotlin.random.Random


/**
 * MinesweeperGame is a class that represents a simple text-based Minesweeper game.
 * It allows users to uncover squares on a grid while avoiding mines.
 */
class MinesweeperGame {

    /**
     * @property grid The 2D array representing the game board, where each Cell holds information about mines and state.
     * @property gridSize The size of the grid (e.g., a gridSize of 4 represents a 4x4 grid).
     * @property mineCount The total number of mines placed on the grid.(maximum is 35% of the total squares)
     */

    lateinit var grid: Array<Array<Cell>>
    var gridSize: Int = 0
    var mineCount: Int = 0

    /**
     * Starts the Minesweeper game. Continuously loops, allowing users to play multiple rounds.
     */
    fun start() {
        while (true) {
            setupGame(gridSize = 4, mineCount = 5)
            playGame()
            println("Press any key to play again...")
            readlnOrNull() // Wait for any key to be pressed
        }
    }

    /**
     * Sets up the game by initializing the grid and placing the mines.
     *
     * @param gridSize Optional parameter to set the grid size. If not provided, the user is prompted.
     * @param mineCount Optional parameter to set the mine count. If not provided, the user is prompted.
     */
    fun setupGame(gridSize: Int, mineCount: Int) {
        if (gridSize <= 0) throw IllegalArgumentException("Grid size must be a positive integer.")

        val maxMines = (gridSize * gridSize * 0.35).toInt()
        if (mineCount < 0 || mineCount > maxMines) throw IllegalArgumentException("Mine count must be between 0 and $maxMines (35% of the total squares).")

        println("Welcome to Minesweeper!\n")

        this.gridSize = getGridSize()
        this.mineCount = getMineCount()

        initializeGrid()
        placeMines()

        // Initial grid display
        println("\nHere is your minefield:")
        displayGrid()
    }

    /**
     * Initializes the grid with a custom configuration.
     *
     * @param customGrid A 2D array representing the custom grid to be used for the game.
     *                   Each element in the array should be a Cell object.
     *                   The size of the grid is determined by the dimensions of the array.
     * @throws IllegalArgumentException if the provided grid is empty or non-square.
     */
    fun initializeCustomGrid(customGrid: Array<Array<Cell>>) {
        require(customGrid.isNotEmpty() && customGrid.all { it.size == customGrid.size }) {
            "The custom grid must be non-empty and square-shaped."
        }
        this.gridSize = customGrid.size
        this.grid = customGrid
    }

    /**
     * Places mines on the grid at the specified locations.
     *
     * @param mineLocations A list of pairs representing the coordinates (x, y) where mines should be placed.
     *                      The coordinates must be within the bounds of the grid.
     * @throws IllegalArgumentException if any coordinates are out of bounds.
     */
    fun placeCustomMines(mineLocations: List<Pair<Int, Int>>) {
        mineLocations.forEach { (x, y) ->
            require(x in 0 until gridSize && y in 0 until gridSize) {
                "Mine location ($x, $y) is out of bounds for the grid size $gridSize."
            }

            grid[x][y].isMine = true
        }
    }

    /**
     * Main game loop where the user uncovers squares until they either win or lose.
     */
    private fun playGame() {
        while (true) {
            val (x, y) = getUserInput()
            if (!uncoverSquare(x, y)) {
                println("Oh no, you detonated a mine! Game over.")
                // Reveal all mines on game over for debugging purposes only
                // displayGrid(true)
                break
            } else {
                val adjacentMines = grid[x][y].adjacentMines
                println("This square contains $adjacentMines adjacent mine${if (adjacentMines != 1) "s" else ""}.\n")
            }

            // Updated grid display after each input
            println("Here is your updated minefield:")
            displayGrid()

            if (checkWinCondition()) {
                println("\nCongratulations, you have won the game!")
                break
            }
        }
    }

    /**
     * Prompts the user to enter the grid size, ensuring it's a valid integer.
     *
     * @return The grid size entered by the user or a default value of 4 if input is invalid.
     */
    @JvmName("getGridSizeMethod")
    private fun getGridSize(): Int {
        print("Enter the size of the grid (e.g., 4 for a 4x4 grid): ")
        return readlnOrNull()?.toIntOrNull()?.coerceAtLeast(1) ?: 4
    }

    /**
     * Prompts the user to enter the mine count, ensuring it's within valid limits.
     *
     * @return The mine count entered by the user or the maximum allowable mines if input is invalid.
     */
    @JvmName("getMineCountMethod")
    private fun getMineCount(): Int {
        val maxMines = (gridSize * gridSize * 0.35).toInt() // (maximum is 35% of the total squares)
        print("Enter the number of mines to place on the grid (maximum is $maxMines): ")
        return readlnOrNull()?.toIntOrNull()?.coerceIn(1, maxMines) ?: maxMines
    }

    /**
     * Initializes the grid by creating a 2D array of Cells.
     */
    private fun initializeGrid() {
        grid = Array(gridSize) { Array(gridSize) { Cell() } }
    }

    /**
     * Places mines randomly on the grid.
     */
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

    /**
     * Uncovers a square at the given coordinates and checks if it's a mine.
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @return True if the square was successfully uncovered and not a mine, false if it's a mine.
     */
    fun uncoverSquare(x: Int, y: Int): Boolean {
        // Return if the coordinates are invalid or the square is already revealed
        if (!isValidCoordinate(x, y) || grid[x][y].isRevealed) return true

        // Reveal the square
        grid[x][y].isRevealed = true

        // If it's a mine, game over
        if (grid[x][y].isMine) return false

        // Count adjacent mines
        val adjacentMines = countAdjacentMines(x, y)
        grid[x][y].adjacentMines = adjacentMines

        // If there are no adjacent mines, uncover adjacent squares recursively
        if (adjacentMines == 0) {
            uncoverAdjacentSquares(x, y)
        }

        return true
    }


    /**
     * Counts the number of adjacent mines around a given square.
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @return The number of adjacent mines.
     */
    fun countAdjacentMines(x: Int, y: Int): Int {
        return getAdjacentCoordinates(x, y).count { (nx, ny) ->
            grid[nx][ny].isMine
        }
    }

    /**
     * Uncovers adjacent squares if the selected square has no adjacent mines.
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     */
    private fun uncoverAdjacentSquares(x: Int, y: Int) {
        getAdjacentCoordinates(x, y).forEach { (nx, ny) ->
            if (!grid[nx][ny].isRevealed && !grid[nx][ny].isMine) {
                uncoverSquare(nx, ny)
            }
        }
    }

    /**
     * Returns a list of valid adjacent coordinates.
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @return A list of valid adjacent coordinates.
     */
    private fun getAdjacentCoordinates(x: Int, y: Int): List<Pair<Int, Int>> {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        for (i in -1..1) {
            for (j in -1..1) {
                val nx = x + i
                val ny = y + j
                if (isValidCoordinate(nx, ny) && (nx != x || ny != y)) {
                    coordinates.add(Pair(nx, ny))
                }
            }
        }
        return coordinates
    }

    /**
     * Checks if the given coordinates are within the grid bounds.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if the coordinates are valid, false otherwise.
     */
    private fun isValidCoordinate(x: Int, y: Int): Boolean {
        return x in 0 until gridSize && y in 0 until gridSize
    }

    /**
     * Prompts the user to select a square to reveal and validates the input.
     *
     * @return A pair of integers representing the selected square's coordinates.
     */
    private fun getUserInput(): Pair<Int, Int> {
        while (true) {
            print("\nSelect a square to reveal (e.g., A1): ")
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

    /**
     * Checks if the player has won the game by uncovering all non-mine squares.
     *
     * @return True if the player has won, false otherwise.
     */
    fun checkWinCondition(): Boolean {
        for (row in grid) {
            for (cell in row) {
                // If a non-mine cell is not revealed, the game is not yet won
                if (!cell.isMine && !cell.isRevealed) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Displays the grid, optionally revealing all mines.
     *
     * @param revealMines If true, all mines are revealed; otherwise, only uncovered squares are shown.
     */
    private fun displayGrid(revealMines: Boolean = false) {
        print("  ")
        for (i in 1..gridSize) print("$i ")
        println()

        for (i in 0 until gridSize) {
            print("${'A' + i} ")
            for (j in 0 until gridSize) {
                when {
                    !grid[i][j].isRevealed && !revealMines -> print("_ ")
                    grid[i][j].isMine -> print("X ") // reveal the mines for logging purposes only
                    else -> print("${grid[i][j].adjacentMines} ")
                }
            }
            println()
        }
    }
}

/**
 * Cell is a data class representing a single square on the Minesweeper grid.
 *
 * @property isMine Indicates if the cell contains a mine.
 * @property isRevealed Indicates if the cell has been revealed.
 * @property adjacentMines The number of mines adjacent to this cell.
 */
class Cell {
    var isMine: Boolean = false
    var isRevealed: Boolean = false
    var adjacentMines: Int = 0
}
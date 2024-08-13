# MinesweeperApp
A simple Minesweeper game implemented in Kotlin. Features include a grid setup, random mine placement, cell revealing, and flagging. Ideal for learning basic game mechanics and Kotlin programming.

# Minesweeper Game Application - Design, Assumptions, and Instructions

## Design Overview
The Minesweeper game application is designed as a console-based game where users can interactively play Minesweeper by uncovering squares on a grid. The grid contains a mix of safe squares and mines. The user wins by uncovering all safe squares without detonating any mines.

## Key Components:

### MinesweeperGame Class:

Manages the overall game logic, including initializing the grid, placing mines, handling user input, and determining the game outcome.
Contains methods for setting up the game (setupGame), playing the game (playGame), uncovering squares (uncoverSquare), and displaying the grid (displayGrid).

### Cell Class:

Represents an individual square on the grid.
Tracks whether the square contains a mine (isMine), whether it has been revealed (isRevealed), and the number of adjacent mines (adjacentMines).

### Unit Tests:

MinesweeperGameTest Class: Contains test cases that verify the correctness of the game logic, including grid initialization, mine placement, uncovering squares, and win conditions.

### Assumptions
Grid Size and Mine Count:

The default grid size is set to 4x4.
The default mine count is set to 5. (maximum is 35% of the total squares)
The grid size and mine count can be customized by the user within the constraints provided (e.g., a valid grid size and a mine count that does not exceed the grid's capacity).

### User Input:

The application assumes that the user will provide valid input (e.g., a grid size greater than 0 and a mine count within the allowed range).
The user selects squares using a combination of letters (for rows) and numbers (for columns).

### Platform:

The application is designed to be platform-agnostic and should run on any operating system with Kotlin installed (e.g., Windows, Linux, macOS).

### Environment:

The application is written in Kotlin, and the tests are written using the Kotlin Test framework.
The application is intended to be run in a console environment.

### Instructions to Run the Application
### Environment Setup:

Ensure you have the following installed:
Kotlin: Version 1.5 or later. You can install Kotlin using the Kotlin command-line tools or via an integrated development environment (IDE) like IntelliJ IDEA.
JDK: Java Development Kit version 8 or later.
JUnit: Ensure that your environment or IDE is set up to run JUnit tests.

### Running the Application:

Clone the repository or download the source files.
Open the project in your preferred IDE (e.g., IntelliJ IDEA).

> [!TIP]
> Helpful advice for doing things better or more easily. This app was created on Android Studio Android Studio Koala | 2024.1.1 Patch 2 <br>
> Build #AI-241.18034.62.2411.12169540, built on August 1, 2024 <br>
> Runtime version: 17.0.11+0-17.0.11b1207.24-11852314 aarch64 <br>
> VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o. <br>
> macOS 14.5 <br>
> GC: G1 Young Generation, G1 Old Generation <br>
> Memory: 2048M <br>
> Cores: 10 <br>
> Metal Rendering is ON <br>
> Registry: <br>
> debugger.new.tool.window.layout=true <br>
> ide.experimental.ui=true <br>
> Non-Bundled Plugins: <br>
> wu.seal.tool.jsontokotlin (3.7.4) <br>


To play the game, run the MinesweeperGame class. The game will start in the console, and you can follow the on-screen instructions to play.

### Running the Tests:

The unit tests are located in the MinesweeperGameTest class.
You can run the tests using your IDE’s test runner (e.g., by right-clicking the test class and selecting “Run”).
Alternatively, you can use the Kotlin command-line tools to run the tests using the command:

```kotlinc -classpath <path-to-junit> MinesweeperGameTest.kt -include-runtime -d MinesweeperGameTest.jar java -jar MinesweeperGameTest.jar```

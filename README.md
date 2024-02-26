# Puzzle Solver Java Project

Welcome to the Puzzle Solver Java Project! This project contains a comprehensive solution for solving NxM puzzle games using various search algorithms. The main goal of this project is to provide a robust framework for implementing and comparing different algorithms' effectiveness and efficiency in solving puzzle games.

## Project Structure

This repository contains the following key Java files:

- **Ex1.java**: The main driver class that reads puzzle configurations from an input file, sets up the game environment, and initiates the puzzle-solving process using the specified algorithm.
- **Algorithm.java**: Encapsulates the logic for different search algorithms, including Depth-First Iterative Deepening (DFID), A*, Iterative Deepening A* (IDA*), and Depth-First Branch-and-Bound (DFBnB).
- **Node.java**: Represents a state in the puzzle game, encapsulating the board configuration and other relevant state information necessary for search algorithms.
- **Block.java**: Represents a block within the puzzle board, characterized by its value, color, and other properties relevant to the puzzle-solving process.

## Features

- **Multiple Algorithms**: Support for various algorithms to solve the puzzle, allowing for a comparative analysis of their performance.
- **Customizable Input**: The program reads puzzle configurations from an input file (`input.txt`), including the algorithm to use, flags for timing and open list details, and the puzzle's initial state.
- **Output Generation**: Generates an output file (`output.txt`) containing the solution path, the total cost of the solution, the number of nodes created, and the elapsed time if the timing flag is set.
- **Modular Design**: The project is structured modularly, making it easy to extend with additional algorithms or modify existing ones.

## Getting Started

To run the project, you will need to have Java installed on your system. Follow these steps:

1. **Compile the Java Files**: Navigate to the project directory and compile the Java files using the javac compiler.
    ```
    javac Ex1.java Algorithm.java Node.java Block.java  
    ```
2. **Prepare the Input File**: Create an `input.txt` file in the project directory with the puzzle configuration, algorithm choice, and flags as described in the Ex1.java file.
3. **Run the Solver**: Execute the main class to start solving the puzzle.
   ```
   java Ex1
   ```
4. **Check the Output**: Upon completion, the solution will be written to `output.txt`, including the solution path, total cost, nodes created, and elapsed time (if applicable).




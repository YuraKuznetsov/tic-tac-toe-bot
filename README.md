# Tic Tac Toe Bot

This is a Tic Tac Toe bot written in Java, capable of playing with a user on boards of various sizes including 3x3, 4x4, 5x5, and 6x6. Additionally, it supports two board formats: classic board and thor board.

## Algorithm
Implemented with the Minimax algorithm. Optimized with:
- Transposition table. A transposition table stores previously calculated positions and their evaluations, reducing redundant calculations.
- Multithreading. The AI bot utilizes multithreading to explore multiple branches of the game tree simultaneously, improving performance.
- Alpha-beta pruning. Alpha-beta pruning is applied to minimize the number of nodes evaluated in the minimax algorithm, reducing computation time.
- Move sorting. Moves are sorted based on their evaluation score, allowing more promising moves to be explored first, potentially improving the algorithm's efficiency.


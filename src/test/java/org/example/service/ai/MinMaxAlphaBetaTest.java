package org.example.service.ai;

import org.example.model.*;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class MinMaxAlphaBetaTest {

    MoveFinder underTest = new MinMaxAlphaBeta();


    /* Test Board Format 3x3 And Modification Classic */
    @Test
    void canMakeForkOnBoard3x3() {
        Symbol[][] matrix = {
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.X, Symbol.O, Symbol.EMPTY}
        };

        Board board = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrix);
        BoardCell expectedCell = new BoardCell(1,1);
        Move findedMove = underTest.find(board);

        assertEquals(expectedCell, findedMove.getCell());
    }

    @Test
    void canPredictForkOnBoard3x3() {
        Symbol[][] matrix = {
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY}
        };

        Board board = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrix);
        Move bestMove = underTest.find(board);

        assertEquals(1, bestMove.getCell().getRow());
        assertEquals(1, bestMove.getCell().getCol());
    }

    @Test
    void dontLetFinishOnBoard3x3() {
        Symbol[][] matrix = {
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.O, Symbol.O, Symbol.EMPTY},
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY}
        };

        Board board = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrix);
        Move bestMove = underTest.find(board);

        assertEquals(1, bestMove.getCell().getRow());
        assertEquals(2, bestMove.getCell().getCol());
    }

    @Test
    void canFinishOnBoard3x3() {
        Symbol[][] matrix = {
                {Symbol.X, Symbol.EMPTY, Symbol.X},
                {Symbol.O, Symbol.O, Symbol.EMPTY},
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY}
        };

        Board board = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrix);
        Move bestMove = underTest.find(board);

        assertEquals(1, bestMove.getCell().getRow());
        assertEquals(2, bestMove.getCell().getCol());
    }

    /* Test Board 6x6 */
    @Test
    void canMakeLineForkOnBoard6x6() {
        Symbol[][] matrix = {
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.O, Symbol.X, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.X, Symbol.EMPTY, Symbol.O, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY}
        };

        Board board = getBoard(BoardFormat.BOARD_6X6, BoardModification.CLASSIC, matrix);
        Move bestMove = underTest.find(board);

        assertEquals(1, bestMove.getCell().getRow());
        assertEquals(3, bestMove.getCell().getCol());
    }

    @Test
    void testGameProcess3x3() {
        Board board = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        while (!board.isFilled()) {
            Move bestMove = getMoveAndPrintTime(board);
            board.fillCell(bestMove.getCell(), board.getNextSymbolToPlay());
            System.out.println(bestMove);
            System.out.println(board);
        }
    }

    @Test
    void testGameProcess6x6() {
        Board board = getBoard(BoardFormat.BOARD_6X6, BoardModification.CLASSIC);

        while (!board.isFilled()) {
            Move bestMove = getMoveAndPrintTime(board);
            board.fillCell(bestMove.getCell(), board.getNextSymbolToPlay());
            System.out.println(bestMove);
            System.out.println(board);
        }
    }

    private Move getMoveAndPrintTime(Board board) {
        long startTime = System.nanoTime();
        Move move = underTest.find(board);
        long endTime = System.nanoTime();

        // Calculate and print the elapsed time in milliseconds
        long elapsedTimeMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Execution Time: " + elapsedTimeMillis + " ms");

        return move;
    }

    private Board getBoard(BoardFormat format, BoardModification modification) {
        int size = format.getDimension();
        Symbol[][] matrix = new Symbol[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = Symbol.EMPTY;
            }
        }

        return getBoard(format, modification, matrix);
    }

    private Board getBoard(BoardFormat format, BoardModification modification, Symbol[][] matrix) {
        Board board = new Board();
        board.setFormat(format);
        board.setModification(modification);
        board.setMatrix(matrix);

        return board;
    }
}
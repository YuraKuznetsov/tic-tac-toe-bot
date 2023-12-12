package org.example.service.evaluator;

import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassicEvaluatorTest {

    BoardEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new ClassicEvaluator();
    }

    @Test
    void testHasSymbolWonOnBoard3x3() {
        Symbol[][] matrixWithDiagonal = {
                {Symbol.X, Symbol.O, Symbol.O},
                {Symbol.O, Symbol.X, Symbol.X},
                {Symbol.X, Symbol.O, Symbol.X}
        };

        Symbol[][] matrixWithRow = {
                {Symbol.X, Symbol.O, Symbol.O},
                {Symbol.X, Symbol.X, Symbol.X},
                {Symbol.O, Symbol.O, Symbol.X}
        };

        Symbol[][] matrixWithCol = {
                {Symbol.X, Symbol.O, Symbol.O},
                {Symbol.X, Symbol.X, Symbol.O},
                {Symbol.X, Symbol.O, Symbol.X}
        };

        Board boardWithDiagonal = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrixWithDiagonal);
        Board boardWithRow = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrixWithRow);
        Board boardWithCol = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrixWithCol);

        assertTrue(evaluator.hasSymbolWon(boardWithDiagonal, Symbol.X));
        assertTrue(evaluator.hasSymbolWon(boardWithRow, Symbol.X));
        assertTrue(evaluator.hasSymbolWon(boardWithCol, Symbol.X));
    }

    private void measureTime(Runnable function) {
        long startTime = System.nanoTime();
        function.run();
        long endTime = System.nanoTime();

        // Calculate and print the elapsed time in milliseconds
        double elapsedTimeMillis = (endTime - startTime) / 1_000_000.0;
        System.out.println("Execution Time: " + elapsedTimeMillis + " ms");
    }

    @Test
    void testHasSymbolWonOnBoard6x6() {
        Symbol[][] matrixWithDiagonal = {
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.X, Symbol.EMPTY, Symbol.O, Symbol.EMPTY, Symbol.X},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.X, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.X, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.O, Symbol.EMPTY, Symbol.X, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
        };

        Symbol[][] matrixWithRow = {
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.X, Symbol.X, Symbol.X, Symbol.X, Symbol.O, Symbol.X},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.X, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.O, Symbol.EMPTY, Symbol.X, Symbol.EMPTY},
                {Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
        };

        Symbol[][] matrixWithCol = {
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY, Symbol.O, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.X, Symbol.O, Symbol.EMPTY, Symbol.EMPTY, Symbol.O, Symbol.EMPTY},
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
                {Symbol.X, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY, Symbol.EMPTY},
        };

        Board boardWithDiagonal = getBoard(BoardFormat.BOARD_6X6, BoardModification.CLASSIC, matrixWithDiagonal);
        Board boardWithRow = getBoard(BoardFormat.BOARD_6X6, BoardModification.CLASSIC, matrixWithRow);
        Board boardWithCol = getBoard(BoardFormat.BOARD_6X6, BoardModification.CLASSIC, matrixWithCol);

        measureTime(() -> evaluator.evaluate(boardWithDiagonal));

        assertTrue(evaluator.hasSymbolWon(boardWithDiagonal, Symbol.X));
        assertTrue(evaluator.hasSymbolWon(boardWithRow, Symbol.X));
        assertTrue(evaluator.hasSymbolWon(boardWithCol, Symbol.X));
    }

    @Test
    void whenTieGame_thenShouldReturnZero() {
        Symbol[][] matrix = {
                {Symbol.X, Symbol.X, Symbol.O},
                {Symbol.O, Symbol.O, Symbol.X},
                {Symbol.X, Symbol.O, Symbol.X}
        };

        Board board = getBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC, matrix);

        assertEquals(0, evaluator.evaluate(board));
    }

    @Test
    void testCornerCell_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(0, 0), Symbol.X);
        assertEquals(3, evaluator.evaluate(board));

        board.fillCell(new BoardCell(0, 0), Symbol.O);
        assertEquals(-3, evaluator.evaluate(board));
    }

    @Test
    void testEdgeCell_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(0, 1), Symbol.X);
        assertEquals(2, evaluator.evaluate(board));

        board.fillCell(new BoardCell(0, 1), Symbol.O);
        assertEquals(-2, evaluator.evaluate(board));
    }

    @Test
    void testCenterCell_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(1, 1), Symbol.X);
        assertEquals(4, evaluator.evaluate(board));

        board.fillCell(new BoardCell(1, 1), Symbol.O);
        assertEquals(-4, evaluator.evaluate(board));
    }

    @Test
    void testCornerAndEdgeCells_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(0, 0), Symbol.X);
        board.fillCell(new BoardCell(1, 0), Symbol.O);
        board.fillCell(new BoardCell(0, 1), Symbol.X);

        // rows: 10; -1;
        // cols: 0; 1
        // diagonals: 1
        assertEquals(11, evaluator.evaluate(board));
    }

    @Test
    void testCornerAndEdgeCells_WhenLineIsBusy_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(0, 0), Symbol.X);
        board.fillCell(new BoardCell(0, 2), Symbol.O);
        board.fillCell(new BoardCell(0, 1), Symbol.X);

        assertEquals(1, evaluator.evaluate(board));
    }

    @Test
    void testTwoCornerCells_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(0, 0), Symbol.X);
        board.fillCell(new BoardCell(1, 0), Symbol.O);
        board.fillCell(new BoardCell(0, 2), Symbol.X);

        assertEquals(12, evaluator.evaluate(board));
    }

    @Test
    void testTwoCornerCells_WhenLineIsBusy_WhenBoard3x3() {
        Board board = getEmptyBoard(BoardFormat.BOARD_3X3, BoardModification.CLASSIC);

        board.fillCell(new BoardCell(0, 0), Symbol.X);
        board.fillCell(new BoardCell(0, 1), Symbol.O);
        board.fillCell(new BoardCell(0, 2), Symbol.X);

        assertEquals(3, evaluator.evaluate(board));
    }

    private Board getEmptyBoard(BoardFormat format, BoardModification modification) {
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
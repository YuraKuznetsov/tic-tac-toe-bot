package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void test1() {
        Board immutableBoard = getBoard(BoardFormat.BOARD_6X6, BoardModification.CLASSIC);
        immutableBoard.fillCell(new BoardCell(1, 2), Symbol.X);
        immutableBoard.fillCell(new BoardCell(1, 3), Symbol.O);
        immutableBoard.fillCell(new BoardCell(2, 0), Symbol.X);

        Board mutableBoard = immutableBoard.clone();
        assertEquals(immutableBoard, mutableBoard);

        mutableBoard.rotateClockwise();
        mutableBoard.rotateClockwise();
        mutableBoard.rotateClockwise();
        mutableBoard.rotateClockwise();
        assertEquals(immutableBoard, mutableBoard);

        mutableBoard.turnOverMainDiagonal();
        mutableBoard.turnOverMainDiagonal();
        assertEquals(immutableBoard, mutableBoard);

        mutableBoard.turnOverSecondaryDiagonal();
        mutableBoard.turnOverSecondaryDiagonal();
        assertEquals(immutableBoard, mutableBoard);

        mutableBoard.turnOverHorizontalAxis();
        mutableBoard.turnOverHorizontalAxis();
        assertEquals(immutableBoard, mutableBoard);

        mutableBoard.turnOverVerticalAxis();
        mutableBoard.turnOverVerticalAxis();
        assertEquals(immutableBoard, mutableBoard);
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
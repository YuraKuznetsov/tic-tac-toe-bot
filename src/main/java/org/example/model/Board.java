package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Board {

    BoardFormat format;
    BoardModification modification;
    Symbol[][] matrix;

    public void rotateClockwise() {
        int x = (int) Math.floor(format.getDimension() / 2.0);
        int y = format.getDimension() - 1;

        for (int i = 0; i < x; i++) {
            for (int j = i; j < y - i; j++) {
                Symbol bufSymbol = matrix[i][j];
                matrix[i][j] = matrix[y - j][i];
                matrix[y - j][i] = matrix[y - i][y - j];
                matrix[y - i][y - j] = matrix[j][y - i];
                matrix[j][y - i] = bufSymbol;
            }
        }
    }

    public void turnOverMainDiagonal() {
        for (int i = 0; i < format.getDimension(); i++) {
            for (int j = i + 1; j < format.getDimension(); j++) {
                Symbol bufSymbol = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = bufSymbol;
            }
        }
    }

    public void turnOverSecondaryDiagonal() {
        int dim = format.getDimension();

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim - i - 1; j++) {
                Symbol bufSymbol = matrix[i][j];
                matrix[i][j] = matrix[dim - j - 1][dim - i - 1];
                matrix[dim - j - 1][dim - i - 1] = bufSymbol;
            }
        }
    }

    public void turnOverHorizontalAxis() {
        int dim = format.getDimension();

        for (int i = 0; i < dim / 2; i++) {
            for (int j = 0; j < dim; j++) {
                Symbol bufSymbol = matrix[i][j];
                matrix[i][j] = matrix[dim - i - 1][j];
                matrix[dim - i - 1][j] = bufSymbol;
            }
        }
    }

    public void turnOverVerticalAxis() {
        int dim = format.getDimension();

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim / 2; j++) {
                Symbol bufSymbol = matrix[i][j];
                matrix[i][j] = matrix[i][dim - j - 1];
                matrix[i][dim - j - 1] = bufSymbol;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (format != board.format) return false;
        if (modification != board.modification) return false;

        return Arrays.deepEquals(matrix, board.matrix);
    }

    @Override
    public int hashCode() {
        int result = format.hashCode();
        result = 31 * result + modification.hashCode();
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    public Symbol[][] getMatrix() {
        return matrix;
    }

    public boolean isFilled() {
        for (int i = 0; i < format.getDimension(); i++)
            for (int j = 0; j < format.getDimension(); j++)
                if (matrix[i][j] == Symbol.EMPTY) return false;

        return true;
    }

    public int getFilledCellsCount() {
        int count = 0;

        for (int i = 0; i < format.getDimension(); i++)
            for (int j = 0; j < format.getDimension(); j++)
                if (matrix[i][j] != Symbol.EMPTY) count++;

        return count;
    }

    public void fillCell(BoardCell cell, Symbol symbol) {
        matrix[cell.getRow()][cell.getCol()] = symbol;
    }

    public void eraseCell(BoardCell cell) {
        matrix[cell.getRow()][cell.getCol()] = Symbol.EMPTY;
    }

    public Symbol getNextSymbol() {
        if (isFilled()) throw new IllegalStateException("The board is already filled. No more moves allowed.");

        return getFilledCellsCount() % 2 == 0 ? Symbol.X : Symbol.O;
    }

    public List<BoardCell> getEmptyCells() {
        List<BoardCell> emptyCells = new ArrayList<>();

        for (int i = 0; i < format.getDimension(); i++) {
            for (int j = 0; j < format.getDimension(); j++) {
                if (matrix[i][j] == Symbol.EMPTY) {
                    emptyCells.add(new BoardCell(i, j));
                }
            }
        }

        return emptyCells;
    }

    @Override
    public String toString() {
        int cellWidth = 5; // Adjust the cell width as needed
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < format.getDimension(); i++) {
            for (int j = 0; j < format.getDimension(); j++) {
                String cellValue = matrix[i][j].toString();

                // Append the cell value with padding to ensure fixed width
                sb.append(String.format("%-" + cellWidth + "s", cellValue));

                if (j < format.getDimension() - 1) {
                    sb.append(" | ");
                }
            }

            sb.append("\n");

            // Add horizontal separator between rows
            if (i < format.getDimension() - 1) {
                for (int k = 0; k < format.getDimension() * (cellWidth + 3) - 3; k++) {
                    sb.append("-");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    @Override
    public Board clone() {
        Board clonedBoard = new Board();
        clonedBoard.format = this.format;
        clonedBoard.modification = this.modification;

        // Deep copy of the matrix
        clonedBoard.matrix = new Symbol[format.getDimension()][format.getDimension()];
        for (int i = 0; i < format.getDimension(); i++) {
            clonedBoard.matrix[i] = this.matrix[i].clone();
        }

        return clonedBoard;
    }
}

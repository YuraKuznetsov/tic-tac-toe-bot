package org.example.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board {

    private BoardFormat format;
    private BoardModification modification;
    @Getter(AccessLevel.NONE)
    private Symbol[][] matrix;

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

    public Symbol getNextSymbolToPlay() {
        if (isFilled()) throw new IllegalStateException("The board is already filled. No more moves allowed.");

        return getFilledCellsCount() % 2 == 0 ? Symbol.X : Symbol.O;
    }

    public Symbol[][] getRows() {
        Symbol[][] rows = new Symbol[format.getDimension()][format.getDimension()];

        for (int i = 0; i < format.getDimension(); i++) {
            rows[i] = matrix[i].clone();
        }

        return rows;
    }

    public Symbol[][] getCols() {
        Symbol[][] cols = new Symbol[format.getDimension()][format.getDimension()];

        for (int i = 0; i < format.getDimension(); i++) {
            for (int j = 0; j < format.getDimension(); j++) {
                cols[i][j] = matrix[j][i];
            }
        }

        return cols;
    }

    public Symbol[][] getMainDiagonals() {
        int diagonalsCount = 2 * format.getDimension() - 1;
        Symbol[][] diagonals = new Symbol[diagonalsCount][];

        for (int i = 0; i < diagonalsCount; i++) {
            int startRow, startCol;
            if (i < format.getDimension()) {
                startRow = format.getDimension() - i - 1;
                startCol = 0;
            } else {
                startRow = 0;
                startCol = i - format.getDimension() + 1;
            }

            int length = Math.min(i + 1, diagonalsCount - i);
            diagonals[i] = new Symbol[length];
            for (int j = 0; j < length; j++) {
                diagonals[i][j] = matrix[startRow + j][startCol + j];
            }
        }

        return diagonals;
    }

    public Symbol[][] getSecondaryDiagonals() {
        int diagonalsCount = 2 * format.getDimension() - 1;
        Symbol[][] diagonals = new Symbol[diagonalsCount][];

        for (int i = 0; i < diagonalsCount; i++) {
            int startRow, startCol;
            if (i < format.getDimension()) {
                startRow = i;
                startCol = 0;
            } else {
                startRow = format.getDimension() - 1;
                startCol = i - format.getDimension() + 1;
            }

            int length = Math.min(i + 1, diagonalsCount - i);
            diagonals[i] = new Symbol[length];
            for (int j = 0; j < length; j++) {
                diagonals[i][j] = matrix[startRow - j][startCol + j];
            }
        }

        return diagonals;
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
}

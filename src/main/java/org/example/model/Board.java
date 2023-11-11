package org.example.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Iterator;
import java.util.stream.StreamSupport;

@Getter
@Setter
@ToString
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

    public Iterable<Symbol[]> getRows() {
        return () -> new Iterator<>() {
            private int rowIndex = 0;

            @Override
            public boolean hasNext() {
                return rowIndex < format.getDimension();
            }

            @Override
            public Symbol[] next() {
                return matrix[rowIndex++].clone();
            }
        };
    }

    public Iterable<Symbol[]> getCols() {
        return () -> new Iterator<>() {
            private int colIndex = 0;

            @Override
            public boolean hasNext() {
                return colIndex < format.getDimension();
            }

            @Override
            public Symbol[] next() {
                Symbol[] column = new Symbol[format.getDimension()];
                for (int i = 0; i < format.getDimension(); i++) {
                    column[i] = matrix[i][colIndex];
                }
                colIndex++;
                return column;
            }
        };
    }

    public Iterable<Symbol[]> getMainDiagonals() {
        return () -> new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < (2 * format.getDimension() - 1);
            }

            @Override
            public Symbol[] next() {
                int startRow, startCol;

                if (currentIndex < format.getDimension()) {
                    startRow = format.getDimension() - currentIndex - 1;
                    startCol = 0;
                } else {
                    startRow = 0;
                    startCol = currentIndex - format.getDimension() + 1;
                }

                int length = Math.min(currentIndex + 1, 2 * format.getDimension() - 1 - currentIndex);
                Symbol[] diagonal = new Symbol[length];

                for (int i = 0; i < length; i++) {
                    diagonal[i] = matrix[startRow + i][startCol + i];
                }

                currentIndex++;
                return diagonal;
            }
        };
    }

    public Iterable<Symbol[]> getSecondaryDiagonals() {
        return () -> new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < (2 * format.getDimension() - 1);
            }

            @Override
            public Symbol[] next() {
                int startRow, startCol;

                if (currentIndex < format.getDimension()) {
                    startRow = currentIndex;
                    startCol = 0;
                } else {
                    startRow = format.getDimension() - 1;
                    startCol = currentIndex - format.getDimension() + 1;
                }

                int length = Math.min(currentIndex + 1, 2 * format.getDimension() - 1 - currentIndex);
                Symbol[] diagonal = new Symbol[length];

                for (int i = 0; i < length; i++) {
                    diagonal[i] = matrix[startRow - i][startCol + i];
                }

                currentIndex++;
                return diagonal;
            }
        };
    }

    public Iterable<BoardCell> getAllCells() {
        return () -> new Iterator<>() {
            private int row = 0;
            private int col = 0;

            @Override
            public boolean hasNext() {
                return row < format.getDimension() && col < format.getDimension();
            }

            @Override
            public BoardCell next() {
                BoardCell cell = new BoardCell(row, col);
                if (++col >= format.getDimension()) {
                    col = 0;
                    row++;
                }
                return cell;
            }
        };
    }

    public Iterable<BoardCell> getEmptyCells() {
        return StreamSupport.stream(getAllCells().spliterator(), false)
                .filter(cell -> matrix[cell.getRow()][cell.getCol()] == Symbol.EMPTY)::iterator;
    }
}

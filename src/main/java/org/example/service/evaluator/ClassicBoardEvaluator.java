package org.example.service.evaluator;

import org.example.model.Board;
import org.example.model.BoardModification;
import org.example.model.Symbol;


public class ClassicBoardEvaluator extends BoardEvaluator {

    @Override
    public BoardModification getBoardModification() {
        return BoardModification.CLASSIC;
    }

    @Override
    public int evaluate(Board board) {
        int scoreX = calculateScore(board, Symbol.X);
        int scoreO = calculateScore(board, Symbol.O);

        return scoreX - scoreO;
    }

    @Override
    public boolean hasSymbolWon(Board board, Symbol symbol) {
        Symbol[][] matrix = board.getMatrix();
        int winLineLength = board.getFormat().getWinLineLength();

        return checkRows(matrix, symbol, winLineLength) ||
                checkCols(matrix, symbol, winLineLength) ||
                checkMainDiagonals(matrix, symbol, winLineLength) ||
                checkSecondaryDiagonals(matrix, symbol, winLineLength);
    }

    private boolean checkRows(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        for (int i = 0; i < matrix.length; i++) {
            int count = 0;

            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != symbol) {
                    count = 0;
                    continue;
                }
                if (++count == winLineLength) return true;
            }
        }

        return false;
    }

    private boolean checkCols(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        for (int i = 0; i < matrix.length; i++) {
            int count = 0;

            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] != symbol) {
                    count = 0;
                    continue;
                }
                if (++count == winLineLength) return true;
            }
        }

        return false;
    }

    private boolean checkMainDiagonals(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        int diagonalsCount = 2 * matrix.length - 1;

        for (int i = 0; i < diagonalsCount; i++) {
            int startRow, startCol;
            if (i < matrix.length) {
                startRow = matrix.length - i - 1;
                startCol = 0;
            } else {
                startRow = 0;
                startCol = i - matrix.length + 1;
            }

            int count = 0;

            int length = Math.min(i + 1, diagonalsCount - i);
            if (length < winLineLength) continue;

            for (int j = 0; j < length; j++) {
                if (matrix[startRow + j][startCol + j] != symbol) {
                    count = 0;
                    continue;
                }
                if (++count == winLineLength) return true;
            }
        }

        return false;
    }

    private boolean checkSecondaryDiagonals(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        int diagonalsCount = 2 * matrix.length - 1;

        for (int i = 0; i < diagonalsCount; i++) {
            int startRow, startCol;
            if (i < matrix.length) {
                startRow = i;
                startCol = 0;
            } else {
                startRow = matrix.length - 1;
                startCol = i - matrix.length + 1;
            }

            int count = 0;

            int length = Math.min(i + 1, diagonalsCount - i);
            if (length < winLineLength) continue;

            for (int j = 0; j < length; j++) {
                if (matrix[startRow - j][startCol + j] != symbol) {
                    count = 0;
                    continue;
                }
                if (++count == winLineLength) return true;
            }
        }

        return false;
    }

    private int calculateScore(Board board, Symbol symbol) {
        Symbol[][] matrix = board.getMatrix();
        int winLineLength = board.getFormat().getWinLineLength();

        return calculateRowsScore(matrix, symbol, winLineLength) +
                calculateColsScore(matrix, symbol, winLineLength) +
                calculateMainDiagonalScore(matrix, symbol, winLineLength) +
                calculateSecondaryDiagonalScore(matrix, symbol, winLineLength);
    }

    private int calculateRowsScore(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        int rowsScore = 0;

        for (int i = 0; i < matrix.length; i++) {
            int rowScore = 0;
            int symbolCount = 0;
            int emptyCount = 0;

            for (int j = 0; j < matrix.length; j++) {

                if (matrix[i][j] == symbol) {
                    symbolCount++;
                    if (j != matrix.length - 1) continue;
                }
                if (matrix[i][j] == Symbol.EMPTY) {
                    emptyCount++;
                    if (j != matrix.length - 1) continue;
                }
                if (symbolCount + emptyCount >= winLineLength) {
                    rowScore += getScoreForSymbolsCount(symbolCount);
                }

                symbolCount = 0;
                emptyCount = 0;
            }

            rowsScore += rowScore;
        }

        return rowsScore;
    }

    private int calculateColsScore(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        int colsScore = 0;

        for (int i = 0; i < matrix.length; i++) {
            int colScore = 0;
            int symbolCount = 0;
            int emptyCount = 0;

            for (int j = 0; j < matrix.length; j++) {

                if (matrix[j][i] == symbol) {
                    symbolCount++;
                    if (j != matrix.length - 1) continue;
                }
                if (matrix[j][i] == Symbol.EMPTY) {
                    emptyCount++;
                    if (j != matrix.length - 1) continue;
                }
                if (symbolCount + emptyCount >= winLineLength) {
                    colScore += getScoreForSymbolsCount(symbolCount);
                }

                symbolCount = 0;
                emptyCount = 0;
            }

            colsScore += colScore;
        }

        return colsScore;
    }

    private int calculateMainDiagonalScore(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        int diagonalsCount = 2 * matrix.length - 1;
        int mainDiagonalsScore = 0;

        for (int i = 0; i < diagonalsCount; i++) {
            int startRow, startCol;
            if (i < matrix.length) {
                startRow = matrix.length - i - 1;
                startCol = 0;
            } else {
                startRow = 0;
                startCol = i - matrix.length + 1;
            }

            int diagonalScore = 0;
            int symbolCount = 0;
            int emptyCount = 0;

            int length = Math.min(i + 1, diagonalsCount - i);
            if (length < winLineLength) continue;

            for (int j = 0; j < length; j++) {
                if (matrix[startRow + j][startCol + j] == symbol) {
                    symbolCount++;
                    if (j != length - 1) continue;
                }
                if (matrix[startRow + j][startCol + j] == Symbol.EMPTY) {
                    emptyCount++;
                    if (j != length - 1) continue;
                }
                if (symbolCount + emptyCount >= winLineLength) {
                    diagonalScore += getScoreForSymbolsCount(symbolCount);
                }

                symbolCount = 0;
                emptyCount = 0;
            }

            mainDiagonalsScore += diagonalScore;
        }

        return mainDiagonalsScore;
    }

    private int calculateSecondaryDiagonalScore(Symbol[][] matrix, Symbol symbol, int winLineLength) {
        int diagonalsCount = 2 * matrix.length - 1;
        int secondaryDiagonalsScore = 0;

        for (int i = 0; i < diagonalsCount; i++) {
            int startRow, startCol;
            if (i < matrix.length) {
                startRow = i;
                startCol = 0;
            } else {
                startRow = matrix.length - 1;
                startCol = i - matrix.length + 1;
            }

            int diagonalScore = 0;
            int symbolCount = 0;
            int emptyCount = 0;

            int length = Math.min(i + 1, diagonalsCount - i);
            if (length < winLineLength) continue;

            for (int j = 0; j < length; j++) {
                if (matrix[startRow - j][startCol + j] == symbol) {
                    symbolCount++;
                    if (j != length - 1) continue;
                }
                if (matrix[startRow - j][startCol + j] == Symbol.EMPTY) {
                    emptyCount++;
                    if (j != length - 1) continue;
                }
                if (symbolCount + emptyCount >= winLineLength) {
                    diagonalScore += getScoreForSymbolsCount(symbolCount);
                }

                symbolCount = 0;
                emptyCount = 0;
            }

            secondaryDiagonalsScore += diagonalScore;
        }

        return secondaryDiagonalsScore;
    }

    private int getScoreForSymbolsCount(int symbolsCount) {
        return (int) Math.pow(10, symbolsCount - 1);
    }
}

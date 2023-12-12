package org.example.service.evaluator;

import org.example.model.Board;
import org.example.model.BoardModification;
import org.example.model.Symbol;

public class ThorEvaluator extends BoardEvaluator {

    @Override
    public BoardModification getModification() {
        return BoardModification.THOR;
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

    private boolean checkRows(Symbol[][] matrix, Symbol playerSymbol, int winLineLength) {
        for (int i = 0; i < matrix.length; i++) {
            int startSymbolCount = 0;

            for (int j = 0; j < matrix.length; j++) {
                Symbol symbol = matrix[i][j];

                if (symbol != playerSymbol) break;
                startSymbolCount++;
            }

            if (startSymbolCount == winLineLength) return true;

            int count = 0;

            for (int j = startSymbolCount; j < matrix.length; j++) {
                Symbol symbol = matrix[i][j];

                if (symbol != playerSymbol) {
                    count = 0;
                    continue;
                }

                count++;
                if (j == matrix.length - 1) count += startSymbolCount;

                if (count == winLineLength) return true;
            }
        }

        return false;
    }

    private boolean checkCols(Symbol[][] matrix, Symbol playerSymbol, int winLineLength) {
        for (int i = 0; i < matrix.length; i++) {
            int startSymbolCount = 0;

            for (int j = 0; j < matrix.length; j++) {
                Symbol symbol = matrix[j][i];

                if (symbol != playerSymbol) break;
                startSymbolCount++;
            }

            if (startSymbolCount == winLineLength) return true;

            int count = 0;

            for (int j = startSymbolCount; j < matrix.length; j++) {
                Symbol symbol = matrix[j][i];

                if (symbol != playerSymbol) {
                    count = 0;
                    continue;
                }

                count++;
                if (j == matrix.length - 1) count += startSymbolCount;

                if (count == winLineLength) return true;
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

    private int calculateRowsScore(Symbol[][] matrix, Symbol playerSymbol, int winLineLength) {
        Symbol opponentSymbol = Symbol.opponentOf(playerSymbol);
        int rowsScore = 0;

        for (int i = 0; i < matrix.length; i++) {
            int rowScore = 0;

            int startSymbolCount = 0;
            int startEmptyCount = 0;

            for (int j = 0; j < matrix.length; j++) {
                Symbol symbol = matrix[i][j];

                if (symbol == opponentSymbol) break;
                if (symbol == playerSymbol) startSymbolCount++;
                else startEmptyCount++;
            }

            if (startSymbolCount + startEmptyCount >= winLineLength) {
                rowScore += getScoreForSymbolsCount(startSymbolCount);
            }

            int symbolCount = 0;
            int emptyCount = 0;

            for (int j = startSymbolCount + startEmptyCount; j < matrix.length; j++) {
                Symbol symbol = matrix[i][j];

                if (symbol == playerSymbol) {
                    symbolCount++;
                    if (j != matrix.length - 1) continue;
                }
                if (symbol == Symbol.EMPTY) {
                    emptyCount++;
                    if (j != matrix.length - 1) continue;
                }

                // Якщо останній елемент і не символ противника, додаємо пусті клітинки і символи гравця з початку
                if (j == matrix.length - 1 && symbol != opponentSymbol) {
                    symbolCount += startSymbolCount;
                    emptyCount += startEmptyCount;

                    if (startSymbolCount + startEmptyCount >= winLineLength) {
                        rowScore += getScoreForSymbolsCount(symbolCount) - getScoreForSymbolsCount(startSymbolCount);
                    }
                    else if (symbolCount + emptyCount >= winLineLength) {
                        rowScore += getScoreForSymbolsCount(symbolCount);
                    }
                    break;
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

    private int calculateColsScore(Symbol[][] matrix, Symbol playerSymbol, int winLineLength) {
        Symbol opponentSymbol = Symbol.opponentOf(playerSymbol);
        int colsScore = 0;

        for (int i = 0; i < matrix.length; i++) {
            int colScore = 0;

            int startSymbolCount = 0;
            int startEmptyCount = 0;

            for (int j = 0; j < matrix.length; j++) {
                Symbol symbol = matrix[j][i];

                if (symbol == opponentSymbol) break;
                if (symbol == playerSymbol) startSymbolCount++;
                else startEmptyCount++;
            }

            if (startSymbolCount + startEmptyCount >= winLineLength) {
                colScore += getScoreForSymbolsCount(startSymbolCount);
            }

            int symbolCount = 0;
            int emptyCount = 0;

            for (int j = startSymbolCount + startEmptyCount; j < matrix.length; j++) {
                Symbol symbol = matrix[j][i];

                if (symbol == playerSymbol) {
                    symbolCount++;
                    if (j != matrix.length - 1) continue;
                }
                if (symbol == Symbol.EMPTY) {
                    emptyCount++;
                    if (j != matrix.length - 1) continue;
                }

                // Якщо останній елемент і не символ противника, додаємо пусті клітинки і символи гравця з початку
                if (j == matrix.length - 1 && symbol != opponentSymbol) {
                    symbolCount += startSymbolCount;
                    emptyCount += startEmptyCount;

                    if (symbolCount + emptyCount >= winLineLength) {
                        colScore += getScoreForSymbolsCount(symbolCount) - getScoreForSymbolsCount(startSymbolCount);
                    }
                    break;
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

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
        for (Symbol[] line : getAllLines(board)) {
            if (containsWinLine(line, symbol, board.getFormat().getWinLineLength()))
                return true;
        }
        return false;
    }

    private int calculateScore(Board board, Symbol symbol) {
        int lineLength = board.getFormat().getWinLineLength();
        int score = 0;

        for (Symbol[] line : getAllLines(board))
            score += calculateLineScore(line, symbol, lineLength);

        return score;
    }

    private Symbol[][] getAllLines(Board board) {
        int dimension = board.getFormat().getDimension();
        int numberOfLines = dimension * 2 + dimension * 4 - 2;
        Symbol[][] lines = new Symbol[numberOfLines][dimension];

        int lineIndex = 0;

        for (Symbol[] row : board.getRows()) {
            lines[lineIndex] = row;
            lineIndex++;
        }

        for (Symbol[] col : board.getCols()) {
            lines[lineIndex] = col;
            lineIndex++;
        }

        for (Symbol[] mainDiagonal : board.getMainDiagonals()) {
            lines[lineIndex] = mainDiagonal;
            lineIndex++;
        }

        for (Symbol[] secondaryDiagonal : board.getSecondaryDiagonals()) {
            lines[lineIndex] = secondaryDiagonal;
            lineIndex++;
        }

        return lines;
    }

    private int calculateLineScore(Symbol[] line, Symbol symbol, int winLineLength) {
        if (line.length < winLineLength) return 0;

        int score = 0;
        int symbolCount = 0;
        int emptyCount = 0;

        for (int i = 0; i < line.length; i++) {
            if (line[i] == symbol) {
                symbolCount++;
                if (i != line.length - 1) continue;
            }
            if (line[i] == Symbol.EMPTY) {
                emptyCount++;
                if (i != line.length - 1) continue;
            }

            if (symbolCount + emptyCount >= winLineLength) {
                score += getScoreForSymbolsCount(symbolCount);
            }

            symbolCount = 0;
            emptyCount = 0;
        }

        return score;
    }

    private int getScoreForSymbolsCount(int symbolsCount) {
        return (int) Math.pow(10, symbolsCount - 1);
    }

    private boolean containsWinLine(Symbol[] line, Symbol symbol, int winLineLength) {
        if (line.length < winLineLength) return false;

        int count = 0;
        for (Symbol curSymbol : line) {
            if (curSymbol == symbol) {
                count++;
                if (count == winLineLength)
                    return true;
            } else count = 0;
        }

        return false;
    }
}

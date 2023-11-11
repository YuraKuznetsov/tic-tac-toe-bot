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
        int maxEvaluatingScore = getMaxEvaluatingScore(board);
        int mixEvaluatingScore = getMinEvaluatingScore(board);
        int lineLength = board.getFormat().getWinLineLength();

        for (Symbol[] row : board.getRows()) {
            if (containsWinLine(row, Symbol.X, lineLength))
                return maxEvaluatingScore;
            if (containsWinLine(row, Symbol.O, lineLength))
                return mixEvaluatingScore;
        }

        for (Symbol[] col : board.getCols()) {
            if (containsWinLine(col, Symbol.X, lineLength))
                return maxEvaluatingScore;
            if (containsWinLine(col, Symbol.O, lineLength))
                return mixEvaluatingScore;
        }

        for (Symbol[] mainDiagonal : board.getMainDiagonals()) {
            if (containsWinLine(mainDiagonal, Symbol.X, lineLength))
                return maxEvaluatingScore;
            if (containsWinLine(mainDiagonal, Symbol.O, lineLength))
                return mixEvaluatingScore;
        }

        for (Symbol[] secondaryDiagonal : board.getSecondaryDiagonals()) {
            if (containsWinLine(secondaryDiagonal, Symbol.X, lineLength))
                return maxEvaluatingScore;
            if (containsWinLine(secondaryDiagonal, Symbol.O, lineLength))
                return mixEvaluatingScore;
        }

        return 0;
    }

    private boolean containsWinLine(Symbol[] arr, Symbol checkedSymbol, int lineLength) {
        if (arr.length < lineLength) return false;

        int count = 0;
        for (Symbol symbol : arr) {
            if (symbol == checkedSymbol) {
                count++;
                if (count == lineLength)
                    return true;
            } else count = 0;
        }

        return false;
    }
}

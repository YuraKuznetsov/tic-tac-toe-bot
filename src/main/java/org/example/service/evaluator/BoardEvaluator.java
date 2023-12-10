package org.example.service.evaluator;

import org.example.model.Board;
import org.example.model.BoardModification;
import org.example.model.Symbol;

public abstract class BoardEvaluator {

    public abstract BoardModification getBoardModification();

    public abstract int evaluate(Board board);

    public abstract boolean hasSymbolWon(Board board, Symbol symbol);

    public int getScoreForLineLength(int length) {
        return (int) Math.pow(10, length - 1);
    }
}

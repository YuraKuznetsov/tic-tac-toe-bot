package org.example.service.evaluator;

import org.example.model.Board;
import org.example.model.BoardModification;
import org.example.model.Symbol;

public class ThorBoardEvaluator extends BoardEvaluator {

    @Override
    public BoardModification getBoardModification() {
        return BoardModification.THOR;
    }

    @Override
    public int evaluate(Board board) {
        return 0;
    }

    @Override
    public boolean hasSymbolWon(Board board, Symbol symbol) {
        return false;
    }
}

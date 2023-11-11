package org.example.service.evaluator;

import org.example.model.Board;
import org.example.model.BoardModification;

public abstract class BoardEvaluator {

    public abstract BoardModification getBoardModification();

    public abstract int evaluate(Board board);

    public int getMaxEvaluatingScore(Board board) {
        return board.getFormat().getDimension() * board.getFormat().getDimension() + 1;
    }

    public int getMinEvaluatingScore(Board board) {
        return -1 * getMaxEvaluatingScore(board);
    }
}

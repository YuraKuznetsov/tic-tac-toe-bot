package org.example.service.evaluator;

import org.example.model.Board;
import org.example.model.BoardModification;

public class CircleBoardEvaluator extends BoardEvaluator {

    @Override
    public BoardModification getBoardModification() {
        return BoardModification.CIRCLE;
    }

    @Override
    public int evaluate(Board board) {
        return 0;
    }
}

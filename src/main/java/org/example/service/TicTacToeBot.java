package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.ai.MiniMax;
import org.example.service.evaluator.BoardEvaluator;
import org.example.service.evaluator.ClassicBoardEvaluator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicTacToeBot {

    private static final int DEFAULT_TIME_LIMIT_MILLISECONDS = 1000;

    public Move findOptimalMove(Board board) {
        return findOptimalMove(board, DEFAULT_TIME_LIMIT_MILLISECONDS);
    }

    public Move findOptimalMove(Board board, int limitMilliseconds) {
        MiniMax minimax = new MiniMax(new ClassicBoardEvaluator());

        return minimax.find(board);
    }
}


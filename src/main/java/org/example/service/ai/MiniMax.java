package org.example.service.ai;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.MoveSorter;
import org.example.service.evaluator.BoardEvaluator;

import java.util.concurrent.*;

@RequiredArgsConstructor
public class MiniMax implements Callable<Move> {

    private final BoardEvaluator boardEvaluator;
    private final Board board;
    private final BoardCell cell;
    private static final int MAX_DEPTH = 6;

    @Override
    public Move call() {
        Symbol symbolToPlay = board.getNextSymbol();
        board.fillCell(cell, symbolToPlay);
        int score = minimax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        board.eraseCell(cell);

        return new Move(cell, score);
    }

    private int minimax(Board board, int depth, int alpha, int beta) {
        if (hasMaximizerWon(board)) return boardEvaluator.evaluate(board) - board.getFilledCellsCount();
        if (hasMinimizerWon(board)) return boardEvaluator.evaluate(board) + board.getFilledCellsCount();
        if (board.isFilled()) return 0;
        if (depth >= MAX_DEPTH) return boardEvaluator.evaluate(board);

        Symbol symbol = board.getNextSymbol();
        Player player = Player.of(symbol);

        int bestScore = player.getInitialScore();

        MoveSorter moveSorter = new MoveSorter(boardEvaluator, player);

        for (BoardCell cell : moveSorter.sortCells(board)) {
            board.fillCell(cell, symbol);
            int moveScore = minimax(board, depth + 1, alpha, beta);
            board.eraseCell(cell);
            bestScore = player.chooseBetterScore(bestScore, moveScore);

            if (player == Player.MAXIMIZER) {
                alpha = Math.max(alpha, bestScore);
            }
            if (player == Player.MINIMIZER) {
                beta = Math.min(beta, bestScore);
            }
            if (beta <= alpha) {
                break;
            }
        }

        return bestScore;
    }

    private boolean hasMaximizerWon(Board board) {
        return boardEvaluator.hasSymbolWon(board, Symbol.X);
    }

    private boolean hasMinimizerWon(Board board) {
        return boardEvaluator.hasSymbolWon(board, Symbol.O);
    }
}

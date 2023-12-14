package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.evaluator.BoardEvaluator;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class MiniMax implements Callable<Move> {

    private static final int MAX_DEPTH = 6;

    private final BoardEvaluator boardEvaluator;
    private final TranspositionTable transpositionTable = new TranspositionTable();
    private final Board board;
    private final BoardCell cell;

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

        if (depth <= 4 && transpositionTable.containsKey(board))
            return transpositionTable.get(board);

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

        if (depth <= 4) {
            transpositionTable.put(board.clone(), bestScore);
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

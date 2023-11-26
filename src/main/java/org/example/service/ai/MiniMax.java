package org.example.service.ai;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.evaluator.BoardEvaluator;

import java.util.Map;

@RequiredArgsConstructor
public class MiniMax {

    private final BoardEvaluator boardEvaluator;
    private static final Map<Symbol, Player> PLAYER_FOR_SYMBOL = Map.of(
            Symbol.X, Player.MAXIMIZER,
            Symbol.O, Player.MINIMIZER
    );
    private static final int MAX_DEPTH = 4, alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;

    public Move find(Board board) {
        if (board.isFilled() || hasMaximizerWon(board) || hasMinimizerWon(board))
            throw new IllegalStateException("The board is already filled. No more moves allowed.");

        Player player = getPlayer(board);
        BoardCell bestCell = null;
        int bestScore = player.getInitialScore();

        for (BoardCell cell : board.getEmptyCells()) {
            int moveScore = evaluateMove(board, cell, 1, alpha, beta);
            if (player.chooseBetterScore(bestScore, moveScore) == moveScore) {
                bestScore = moveScore;
                bestCell = cell;
            }
        }

        return new Move(bestCell, bestScore);
    }

    private int minimax(Board board, int depth, int alpha, int beta) {
        int score = boardEvaluator.evaluate(board);
        int filledCellsCount = board.getFilledCellsCount();

        if (hasMaximizerWon(board)) return score - filledCellsCount;
        if (hasMinimizerWon(board)) return score + filledCellsCount;
        if (board.isFilled() || depth >= MAX_DEPTH) return score;

        Player player = getPlayer(board);
        int bestScore = player.getInitialScore();

        for (BoardCell cell : board.getEmptyCells()) {
            int moveScore = evaluateMove(board, cell, depth+1, alpha, beta);
            bestScore = player.chooseBetterScore(bestScore, moveScore);

            // new logic
            if (player == Player.MAXIMIZER) {
                alpha = Math.max(alpha, bestScore);
            }
            if (player == Player.MINIMIZER) {
                beta = Math.min(beta, bestScore);
            }
            if (beta <= alpha) {
                break; // Beta cut-off
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

    private Player getPlayer(Board board) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        return PLAYER_FOR_SYMBOL.get(symbolToPlay);
    }

    private int evaluateMove(Board board, BoardCell cell, int depth, int alfa, int beta) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        board.fillCell(cell, symbolToPlay);
        int score = minimax(board, depth, alfa, beta);
        board.eraseCell(cell);

        return score;
    }
}

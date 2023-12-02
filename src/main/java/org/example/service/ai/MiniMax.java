package org.example.service.ai;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.MoveSorter;
import org.example.service.evaluator.BoardEvaluator;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MiniMax {

    private final BoardEvaluator boardEvaluator;
    private static final Map<Symbol, Player> PLAYER_FOR_SYMBOL = Map.of(
            Symbol.X, Player.MAXIMIZER,
            Symbol.O, Player.MINIMIZER
    );
    private static final int MAX_DEPTH = 6;

    public Move find(Board board) {
        if (board.isFilled() || hasMaximizerWon(board) || hasMinimizerWon(board))
            throw new IllegalStateException("The board is already filled. No more moves allowed.");

        Player player = getPlayer(board);
        BoardCell bestCell = null;
        int bestScore = player.getInitialScore();

        for (int iterativeDepth = 1; iterativeDepth <= MAX_DEPTH; iterativeDepth++) {
            Move depthMove = null;
            int depthScore = player.getInitialScore();

            for (BoardCell cell : board.getEmptyCells()) {
                Symbol symbolToPlay = board.getNextSymbolToPlay();
                board.fillCell(cell, symbolToPlay);
                int moveScore = minimax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, iterativeDepth);
                board.eraseCell(cell);

                if (player.chooseBetterScore(depthScore, moveScore) == moveScore) {
                    depthScore = moveScore;
                    depthMove = new Move(cell, moveScore);
                }
            }

            if (depthMove != null) {
                bestCell = depthMove.getCell();
                bestScore = depthMove.getScore();
            }
        }

        return new Move(bestCell, bestScore);
    }

    private int minimax(Board board, int depth, int alpha, int beta, int maxDepth) {
        if (depth >= maxDepth || board.isFilled() || hasMaximizerWon(board) || hasMinimizerWon(board)) {
            return boardEvaluator.evaluate(board);
        }

        Player player = getPlayer(board);
        int bestScore = player.getInitialScore();

        MoveSorter moveSorter = new MoveSorter(boardEvaluator, player);
        List<BoardCell> cells = moveSorter.sortCells(board);

        for (BoardCell cell : cells) {
            Symbol symbolToPlay = board.getNextSymbolToPlay();
            board.fillCell(cell, symbolToPlay);
            int moveScore = minimax(board, depth + 1, alpha, beta, maxDepth);
            board.eraseCell(cell);
            bestScore = player.chooseBetterScore(bestScore, moveScore);

            // new logic
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

    private Player getPlayer(Board board) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        return PLAYER_FOR_SYMBOL.get(symbolToPlay);
    }

    private int evaluateMove(Board board, BoardCell cell, int depth, int alfa, int beta, int maxDepth) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        board.fillCell(cell, symbolToPlay);
        int score = minimax(board, depth, alfa, beta, maxDepth);
        board.eraseCell(cell);

        return score;
    }
}

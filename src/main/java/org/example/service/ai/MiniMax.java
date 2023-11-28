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
    private static final int MAX_DEPTH = 4;
//    private static final int TIME_LIMIT_MILLISECONDS = 100000;
    private long startTime;

    public Move find(Board board) {
        if (board.isFilled() || hasMaximizerWon(board) || hasMinimizerWon(board))
            throw new IllegalStateException("The board is already filled. No more moves allowed.");

        Player player = getPlayer(board);
        BoardCell bestCell = null;
        int bestScore = player.getInitialScore();

        startTime = System.currentTimeMillis();

        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            Move currentMove = null;
            int currentScore = 0;

            for (BoardCell cell : board.getEmptyCells()) {

                Symbol symbolToPlay = board.getNextSymbolToPlay();
                board.fillCell(cell, symbolToPlay);
                int moveScore = minimax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
                board.eraseCell(cell);

                if (player.chooseBetterScore(currentScore, moveScore) == moveScore) {
                    currentScore = moveScore;
                    currentMove = new Move(cell, moveScore);
                }
            }

            if (currentMove != null) {
                bestCell = currentMove.getCell();
                bestScore = currentMove.getScore();
            }
        }



        return new Move(bestCell, bestScore);
    }

    private int minimax(Board board, int depth, int alpha, int beta, int maxDepth) {
//        if (depth >= maxDepth || board.isFilled() || hasMaximizerWon(board) || hasMinimizerWon(board)) {
//            return boardEvaluator.evaluate(board);
//        }

        int score = boardEvaluator.evaluate(board);
        int filledCellsCount = board.getFilledCellsCount();

        if (hasMaximizerWon(board)) return score - filledCellsCount;
        if (hasMinimizerWon(board)) return score + filledCellsCount;
        if (board.isFilled() || depth >= maxDepth) return score;


        Player player = getPlayer(board);
        int bestScore = player.getInitialScore();

        for (BoardCell cell : board.getEmptyCells()) {
            int moveScore = evaluateMove(board, cell, depth+1, alpha, beta, maxDepth);
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

    private int evaluateMove(Board board, BoardCell cell, int depth, int alfa, int beta, int maxDepth) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        board.fillCell(cell, symbolToPlay);
        int score = minimax(board, depth, alfa, beta, maxDepth);
        board.eraseCell(cell);

        return score;
    }

//    private boolean outOfTime() {
//        return System.currentTimeMillis() - startTime >= TIME_LIMIT_MILLISECONDS;
//    }
}

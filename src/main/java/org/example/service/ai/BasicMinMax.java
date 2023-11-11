package org.example.service.ai;

import org.example.model.Board;
import org.example.model.BoardCell;
import org.example.model.Player;
import org.example.model.Symbol;
import org.example.service.evaluator.BoardEvaluator;
import org.example.service.evaluator.ClassicBoardEvaluator;

import java.util.Map;

/*
DO NOT RUN ON A BIG BOARD!!! YOUR MACHINE WILL EXPLODE =)
 */
public class BasicMinMax implements BestMoveFinder {

    private final BoardEvaluator boardEvaluator = new ClassicBoardEvaluator();
    private static final Map<Symbol, Player> PLAYER_FOR_SYMBOL = Map.of(
            Symbol.X, Player.MAXIMIZER,
            Symbol.O, Player.MINIMIZER
    );

    @Override
    public BoardCell find(Board board) {
        if (board.isFilled())
            throw new IllegalStateException("The board is already filled. No more moves allowed.");

        BoardCell bestCell = null;
        int bestScore = 0;

        Player player = getPlayer(board);

        for (BoardCell cell : board.getEmptyCells()) {
            int moveScore = evaluateMove(board, cell);
            if (player.chooseBetterScore(bestScore, moveScore) == moveScore) {
                bestScore = moveScore;
                bestCell = cell;
            }
        }

        return bestCell;
    }

    private int minimax(Board board) {
        int score = boardEvaluator.evaluate(board);
        int depth = board.getFilledCellsCount();

        if (hasMaximizerWon(score, board)) return score - depth;
        if (hasMinimizerWon(score, board)) return score + depth;
        if (board.isFilled()) return 0;

        Player player = getPlayer(board);

        int bestScore = player.getInitialScore();
        for (BoardCell cell : board.getEmptyCells()) {
            int moveScore = evaluateMove(board, cell);
            bestScore = player.chooseBetterScore(bestScore, moveScore);
        }

        return bestScore;
    }

    private boolean hasMaximizerWon(int score, Board board) {
        return score == boardEvaluator.getMaxEvaluatingScore(board);
    }

    private boolean hasMinimizerWon(int score, Board board) {
        return score == boardEvaluator.getMinEvaluatingScore(board);
    }

    private Player getPlayer(Board board) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        return PLAYER_FOR_SYMBOL.get(symbolToPlay);
    }

    private int evaluateMove(Board board, BoardCell cell) {
        Symbol symbolToPlay = board.getNextSymbolToPlay();
        board.fillCell(cell, symbolToPlay);
        int score = minimax(board);
        board.eraseCell(cell);

        return score;
    }
}

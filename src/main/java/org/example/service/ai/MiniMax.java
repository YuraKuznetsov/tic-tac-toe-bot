package org.example.service.ai;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.MoveSorter;
import org.example.service.evaluator.BoardEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MiniMax {

    private final BoardEvaluator boardEvaluator;
    private static final Map<Symbol, Player> PLAYER_FOR_SYMBOL = Map.of(
            Symbol.X, Player.MAXIMIZER,
            Symbol.O, Player.MINIMIZER
    );
    private static final int MAX_DEPTH = 6;
    private ExecutorService executorService;

    public Move find(Board board) {
        executorService = Executors.newFixedThreadPool(4);
        if (board.isFilled() || hasMaximizerWon(board) || hasMinimizerWon(board))
            throw new IllegalStateException("The board is already filled. No more moves allowed.");

        Player player = getPlayer(board);
        BoardCell bestCell = null;
        int bestScore = player.getInitialScore();

        List<Callable<Move>> tasks = new ArrayList<>();

        for (BoardCell cell : board.getEmptyCells()) {
            tasks.add(new MiniMaxTask(board.clone(), cell));
        }

        List<Move> results;
        try {
            results = executorService.invokeAll(tasks).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Move move : results) {
            int moveScore = move.getScore();
            if (player.chooseBetterScore(bestScore, moveScore) == moveScore) {
                bestScore = moveScore;
                bestCell = move.getCell();
            }
        }

        shutDown();

        return new Move(bestCell, bestScore);
    }

    private int minimax(Board board, int depth, int alpha, int beta) {
        if (hasMaximizerWon(board)) return boardEvaluator.evaluate(board) - board.getFilledCellsCount();
        if (hasMinimizerWon(board)) return boardEvaluator.evaluate(board) + board.getFilledCellsCount();
        if (board.isFilled()) return 0;
        if (depth >= MAX_DEPTH) return boardEvaluator.evaluate(board);

        Player player = getPlayer(board);
        int bestScore = player.getInitialScore();

        MoveSorter moveSorter = new MoveSorter(boardEvaluator, player);
        List<BoardCell> cells = moveSorter.sortCells(board);

        for (BoardCell cell : cells) {
            Symbol symbolToPlay = board.getNextSymbolToPlay();
            board.fillCell(cell, symbolToPlay);
            int moveScore = minimax(board, depth + 1, alpha, beta);
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

    private void shutDown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    private class MiniMaxTask implements Callable<Move> {
        private final Board board;
        private final BoardCell cell;

        public MiniMaxTask(Board board, BoardCell cell) {
            this.board = board;
            this.cell = cell;
        }

        @Override
        public Move call() {
            Symbol symbolToPlay = board.getNextSymbolToPlay();
            board.fillCell(cell, symbolToPlay);
            int score = minimax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            board.eraseCell(cell);

            return new Move(cell, score);
        }
    }
}

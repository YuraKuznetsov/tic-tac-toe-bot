package org.example.service;

import org.example.model.*;
import org.example.service.evaluator.BoardEvaluator;
import org.example.service.evaluator.ClassicEvaluator;
import org.example.service.evaluator.ThorEvaluator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class TicTacToeBot {

    private final List<BoardEvaluator> boardEvaluators;

    public TicTacToeBot() {
        boardEvaluators = List.of(new ClassicEvaluator(), new ThorEvaluator());
    }

    public Move findOptimalMove(Board board) {
        BoardEvaluator boardEvaluator = getEvaluator(board);

        if (board.isFilled() || isGameWon(board, boardEvaluator))
            throw new IllegalStateException("No more moves allowed.");

        Player player = Player.of(board);

        List<Move> moves = evaluateMoves(board, boardEvaluator);
        sortMoves(moves, player);

        return moves.get(0);
    }

    private BoardEvaluator getEvaluator(Board board) {
        return boardEvaluators.stream()
                .filter(evaluator -> evaluator.getModification() == board.getModification())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private boolean isGameWon(Board board, BoardEvaluator boardEvaluator) {
        return boardEvaluator.hasSymbolWon(board, Symbol.X) || boardEvaluator.hasSymbolWon(board, Symbol.O);
    }

    public void sortMoves(List<Move> moves, Player player) {
        moves.sort((m1, m2) -> player == Player.MINIMIZER ?
                Integer.compare(m1.getScore(), m2.getScore()) :
                Integer.compare(m2.getScore(), m1.getScore()));
    }

    private List<Move> evaluateMoves(Board board, BoardEvaluator boardEvaluator) {
        List<Callable<Move>> tasks = prepareMiniMaxTasks(board, boardEvaluator);

        MiniMaxExecutor miniMaxExecutor = new MiniMaxExecutor();
        List<Move> moves = miniMaxExecutor.invokeAll(tasks);
        miniMaxExecutor.shutDown();

        return moves;
    }

    private List<Callable<Move>> prepareMiniMaxTasks(Board board, BoardEvaluator boardEvaluator) {
        List<Callable<Move>> tasks = new ArrayList<>();
        for (BoardCell cell : getCells(board)) {
            tasks.add(new MiniMax(boardEvaluator, board.clone(), cell));
        }

        return tasks;
    }

    private List<BoardCell> getCells(Board board) {
        List<BoardCell> cells = new ArrayList<>();

        TranspositionTable table = new TranspositionTable();
        for (BoardCell cell : board.getEmptyCells()) {
            Symbol symbolToPlay = board.getNextSymbol();
            board.fillCell(cell, symbolToPlay);
            if (!table.containsKey(board)) {
                cells.add(cell);
                table.put(board.clone(), null);
            }
            board.eraseCell(cell);
        }

        return cells;
    }

    public String getGameStatus(Board board) {
        BoardEvaluator boardEvaluator = getEvaluator(board);

        if (board.isFilled()) return "Tie game";
        if (boardEvaluator.hasSymbolWon(board, Symbol.X)) return "X won";
        if (boardEvaluator.hasSymbolWon(board, Symbol.O)) return "O won";

        return "Not finished";
    }
}


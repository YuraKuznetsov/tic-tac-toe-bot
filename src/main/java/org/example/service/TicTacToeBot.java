package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.service.ai.MiniMax;
import org.example.service.ai.MiniMaxExecutor;
import org.example.service.evaluator.BoardEvaluator;
import org.example.service.evaluator.ClassicBoardEvaluator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Service
@RequiredArgsConstructor
public class TicTacToeBot {

    private final BoardEvaluator boardEvaluator = new ClassicBoardEvaluator();

    public List<Move> compereMoves(Board board) {
        List<Move> moves = evaluateMoves(board);

        Symbol symbol = board.getNextSymbol();
        Player player = Player.of(symbol);

        moves.sort((m1, m2) -> player == Player.MINIMIZER ?
                Integer.compare(m1.getScore(), m2.getScore()) :
                Integer.compare(m2.getScore(), m1.getScore()));

        return moves;
    }

    private List<Move> evaluateMoves(Board board) {
        if (board.isFilled() || isGameWon(board))
            throw new IllegalStateException("No more moves allowed.");

        MiniMaxExecutor miniMaxExecutor = new MiniMaxExecutor();
        List<Callable<Move>> tasks = prepareMoveTasks(board);
        List<Move> moves = miniMaxExecutor.calculateMoves(tasks);
        miniMaxExecutor.shutDown();

        return moves;
    }

    private boolean isGameWon(Board board) {
        return boardEvaluator.hasSymbolWon(board, Symbol.X) || boardEvaluator.hasSymbolWon(board, Symbol.O);
    }

    private List<Callable<Move>> prepareMoveTasks(Board board) {
        List<Callable<Move>> tasks = new ArrayList<>();
        for (BoardCell cell : board.getEmptyCells()) {
            tasks.add(new MiniMax(boardEvaluator, board.clone(), cell));
        }

        return tasks;
    }

    public Move makeMove(Board board) {
        return compereMoves(board).get(0);
    }
}


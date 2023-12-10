package org.example.service;

import org.example.model.*;
import org.example.service.evaluator.BoardEvaluator;

import java.util.*;
import java.util.stream.Collectors;

public class MoveSorter {

    private final BoardEvaluator boardEvaluator;
    private final Player player;

    public MoveSorter(BoardEvaluator boardEvaluator, Player player) {
        this.boardEvaluator = boardEvaluator;
        this.player = player;
    }

    private List<CellOrderingNode> initializeCellNodes(Board board) {
        List<CellOrderingNode> cellNodes = new ArrayList<>();

        for (BoardCell cell : board.getEmptyCells()) {
            Symbol symbolToPlay = board.getNextSymbol();
            board.fillCell(cell, symbolToPlay);
            int score = boardEvaluator.evaluate(board);
            board.eraseCell(cell);

            CellOrderingNode node = new CellOrderingNode(cell, score);
            cellNodes.add(node);
        }

        return cellNodes;
    }

    public List<BoardCell> sortCells(Board board) {
        List<CellOrderingNode> cellNodes = initializeCellNodes(board);

        int sortOrder = (player == Player.MINIMIZER) ? 1 : -1;

        return cellNodes.stream()
                .sorted(Comparator.comparingInt(node -> sortOrder * node.getScore()))
                .map(CellOrderingNode::getCell)
                .collect(Collectors.toList());
    }
}

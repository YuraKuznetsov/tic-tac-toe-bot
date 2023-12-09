package org.example.service;

import org.example.model.Board;

import java.util.HashMap;
import java.util.Map;

public class TranspositionTable {

    private final Map<Board, Integer> table = new HashMap<>();

    public boolean containsKey(Board board) {
        Board clone = board.clone();

        // Rotating
        clone.rotateClockwise();
        if (table.containsKey(clone)) return true;
        clone.rotateClockwise();
        if (table.containsKey(clone)) return true;
        clone.rotateClockwise();
        if (table.containsKey(clone)) return true;
        clone.rotateClockwise();


        // Turning over diagonals
        clone.turnOverMainDiagonal();
        if (table.containsKey(clone)) return true;
        clone.turnOverMainDiagonal();

        clone.turnOverSecondaryDiagonal();
        if (table.containsKey(clone)) return true;
        clone.turnOverSecondaryDiagonal();


        // Turning over axis
        clone.turnOverHorizontalAxis();
        if (table.containsKey(clone)) return true;
        clone.turnOverHorizontalAxis();

        clone.turnOverVerticalAxis();
        if (table.containsKey(clone)) return true;
        clone.turnOverVerticalAxis();


        return table.containsKey(clone);
    }

    public Integer get(Board board) {
        Board clone = board.clone();

        // Rotating
        clone.rotateClockwise();
        if (table.containsKey(clone)) return table.get(clone);
        clone.rotateClockwise();
        if (table.containsKey(clone)) return table.get(clone);
        clone.rotateClockwise();
        if (table.containsKey(clone)) return table.get(clone);
        clone.rotateClockwise();


        // Turning over diagonals
        clone.turnOverMainDiagonal();
        if (table.containsKey(clone)) return table.get(clone);
        clone.turnOverMainDiagonal();

        clone.turnOverSecondaryDiagonal();
        if (table.containsKey(clone)) return table.get(clone);
        clone.turnOverSecondaryDiagonal();


        // Turning over axis
        clone.turnOverHorizontalAxis();
        if (table.containsKey(clone)) return table.get(clone);
        clone.turnOverHorizontalAxis();

        clone.turnOverVerticalAxis();
        if (table.containsKey(clone)) return table.get(clone);
        clone.turnOverVerticalAxis();

        return table.get(clone);
    }

    public void put(Board board, Integer score) {
        table.put(board, score);
    }
}

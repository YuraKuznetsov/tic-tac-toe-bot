package org.example.model;

public class CellOrderingNode {

    private final BoardCell cell;
    private int score;

    public CellOrderingNode(BoardCell cell, int score) {
        this.cell = cell;
        this.score = score;
    }

    public BoardCell getCell() {
        return cell;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

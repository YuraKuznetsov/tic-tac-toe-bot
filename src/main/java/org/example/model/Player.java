package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public enum Player {

    MAXIMIZER(Integer.MIN_VALUE, Math::max),
    MINIMIZER(Integer.MAX_VALUE, Math::min);

    @Getter
    private final int initialScore;
    private final BiFunction<Integer, Integer, Integer> scoreComparisonFunction;

    public int chooseBetterScore(int score1, int score2) {
        return scoreComparisonFunction.apply(score1, score2);
    }
}

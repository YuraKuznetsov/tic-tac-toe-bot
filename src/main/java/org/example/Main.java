package org.example;

import org.example.model.*;
import org.example.service.ai.MoveFinder;
import org.example.service.ai.MinMaxAlphaBeta;

public class Main {
    public static void main(String[] args) {
    }

    private static void measureTimeAnd(Runnable function) {
        long startTime = System.nanoTime();

        // Call the function you want to measure
        function.run();

        long endTime = System.nanoTime();

        // Calculate and print the elapsed time in milliseconds
        long elapsedTimeMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Execution Time: " + elapsedTimeMillis + " ms");
    }
}
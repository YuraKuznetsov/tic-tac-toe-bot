package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Board;
import org.example.model.Move;
import org.example.service.TicTacToeBot;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class TicTacToeController {

    private final TicTacToeBot bot;

    @PostMapping("/optimal-move")
    public Move getOptimalMove(@RequestBody Board board) {
        return bot.findOptimalMove(board);
    }

    @PostMapping("/status")
    public String getGameStatus(@RequestBody Board board) {
        return bot.getGameStatus(board);
    }
}

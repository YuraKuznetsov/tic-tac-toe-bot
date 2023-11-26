package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Pages {

    @GetMapping("")
    public String getConfigurationPage() {
        return "configuration";
    }

    @GetMapping("/game")
    public String getGamePage() {
        return "game";
    }
}

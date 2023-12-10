package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardFormat {

    BOARD_3X3(3, 3),
    BOARD_5X5(5, 4),
    BOARD_6X6(6, 4);

    private final int dimension;
    private final int winLineLength;
}

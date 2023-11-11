package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardFormat {

    BOARD_3X3(3, 3),
    BOARD_6X6(6, 4),
    BOARD_9X9(9, 5),
    BOARD_11X11(11, 6);

    private final int dimension;
    private final int winLineLength;
}

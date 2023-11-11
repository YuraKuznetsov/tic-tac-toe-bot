package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Symbol {

    X("X"),
    O("O"),
    EMPTY(null);

    private final String value;
}

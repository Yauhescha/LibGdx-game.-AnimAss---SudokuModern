package com.hescha.game.sudokufulll.model;

import java.io.Serializable;

public enum SudokuDifficulty  implements Serializable {
    FOR_KIDS(1),
    EASY(30),
    NORMAL(40),
    HARDER(50),
    HARD(60);

    final int numberOfRemovedCells;

    SudokuDifficulty(int numberOfRemovedCells) {
        this.numberOfRemovedCells = numberOfRemovedCells;
    }

    public int getNumberOfRemovedCells() {
        return numberOfRemovedCells;
    }
}

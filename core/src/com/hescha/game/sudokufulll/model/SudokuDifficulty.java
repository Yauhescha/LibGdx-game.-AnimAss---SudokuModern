package com.hescha.game.sudokufulll.model;

import java.io.Serializable;

public enum SudokuDifficulty  implements Serializable {
    FOR_KIDS(10),
    EASY(20),
    NORMAL(30),
    HARDER(40);

    final int numberOfRemovedCells;

    SudokuDifficulty(int numberOfRemovedCells) {
        this.numberOfRemovedCells = numberOfRemovedCells;
    }

    public int getNumberOfRemovedCells() {
        return numberOfRemovedCells;
    }
}

package com.hescha.game.sudokufulll.model;


import java.io.Serializable;


public class SudokuCell implements Serializable {
    private SudokuCellType cellType;
    private int number;

    public SudokuCell() {
    }

    public SudokuCell(int number, SudokuCellType cellType) {
        this.number = number;
        this.cellType = cellType;
    }

    public void setNumber(int number) {
        if (cellType != SudokuCellType.DISABLED) {
            this.number = number;
        }
    }

    public SudokuCellType getCellType() {
        return cellType;
    }

    public void setCellType(SudokuCellType cellType) {
        this.cellType = cellType;
    }

    public int getNumber() {
        return number;
    }
}

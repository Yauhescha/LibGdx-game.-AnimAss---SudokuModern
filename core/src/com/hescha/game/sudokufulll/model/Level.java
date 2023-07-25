package com.hescha.game.sudokufulll.model;

import java.io.Serializable;

public class Level implements Serializable {
    private String imagePath;
    private Sudoku sudoku;
    private String name;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void clearBoard() {
        for(SudokuCell[] cells: getSudoku().getBoard()){
            for (SudokuCell cell:cells){
                if(cell.getCellType()!= SudokuCellType.DISABLED)
                    cell.setNumber(0);
            }
        }
    }
}

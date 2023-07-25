package com.hescha.game.sudokufulll.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hescha.game.sudokufulll.model.Sudoku;
import com.hescha.game.sudokufulll.model.SudokuCell;
import com.hescha.game.sudokufulll.model.SudokuCellType;
import com.hescha.game.sudokufulll.screen.GalleryScreen;
import com.hescha.game.sudokufulll.screen.GameScreen;

import java.io.Serializable;

public class Level implements Serializable {
    private String imagePath;
    private Sudoku sudoku;
    private String category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

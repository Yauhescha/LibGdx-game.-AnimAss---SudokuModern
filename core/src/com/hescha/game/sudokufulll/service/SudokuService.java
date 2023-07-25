package com.hescha.game.sudokufulll.service;

import com.hescha.game.sudokufulll.model.Sudoku;
import com.hescha.game.sudokufulll.model.SudokuCell;
import com.hescha.game.sudokufulll.model.SudokuDifficulty;

public class SudokuService {

    public static Sudoku generateGame(SudokuDifficulty sudokuDifficulty) {
        return SudokuGenerator.generateGame(sudokuDifficulty);
    }

    public static boolean isRowSolvedCorrect(Sudoku sudoku) {
        SudokuCell[][] sudokuBoard = sudoku.getBoard();
        int[][] board = new int[sudokuBoard.length][sudokuBoard.length];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = sudokuBoard[i][j].getNumber();
            }
        }
        return isRowSolvedCorrect(board);
    }


    private static boolean isRowSolvedCorrect(int[][] board) {
        int size = board.length;
        for (int i = 0; i < size; i++) {

            boolean[] rowCheck = new boolean[size];
            boolean[] colCheck = new boolean[size];
            boolean[] boxCheck = new boolean[size];

            for (int j = 0; j < size; j++) {

                if (board[i][j] < 1 || board[i][j] > 9 || board[j][i] < 1 || board[j][i] > 9) {
                    return false;
                }

                rowCheck[board[i][j] - 1] = true;
                colCheck[board[j][i] - 1] = true;

                int boxRowOffset = (i / 3) * 3;
                int boxColOffset = (i % 3) * 3;

                if (boxCheck[board[boxRowOffset + j / 3][boxColOffset + j % 3] - 1]) {
                    return false;
                }

                boxCheck[board[boxRowOffset + j / 3][boxColOffset + j % 3] - 1] = true;
            }

            for (int j = 0; j < size; j++) {
                if (!rowCheck[j] || !colCheck[j] || !boxCheck[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

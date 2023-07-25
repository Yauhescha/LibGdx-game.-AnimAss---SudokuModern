package com.hescha.game.sudokufulll.service;


import com.hescha.game.sudokufulll.model.Sudoku;
import com.hescha.game.sudokufulll.model.SudokuCell;
import com.hescha.game.sudokufulll.model.SudokuCellType;
import com.hescha.game.sudokufulll.model.SudokuDifficulty;

import java.lang.Math;


public class SudokuGenerator {
    public static final int SIZE = 9;
    private static final int SRN = 3;
    private static final int[][] arr = new int[SIZE][SIZE];
    private static int numberOfMissingDigit;

    public static Sudoku generateGame(SudokuDifficulty sudokuDifficulty) {
        numberOfMissingDigit = sudokuDifficulty.getNumberOfRemovedCells();
        fillValues();

        Sudoku sudoku = new Sudoku();
        sudoku.setSudokuDifficulty(sudokuDifficulty);
        SudokuCell[][] board = sudoku.getBoard();

        // заполнить доску значениями от 1 до 9 в случайном порядке
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (arr[i][j] == 0)
                    board[i][j] = new SudokuCell(0, SudokuCellType.ENABLED);
                else
                    board[i][j] = new SudokuCell(arr[i][j], SudokuCellType.DISABLED);
            }
        }
        return sudoku;
    }

    // Sudoku Generator
    private static void fillValues() {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal();
        // Fill remaining blocks
        fillRemaining(0, SRN);
        // Remove Randomly K digits to make game
        removeKDigits();
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    private static void fillDiagonal() {
        for (int i = 0; i < SIZE; i = i + SRN)
            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }

    // Returns false if given 3 x 3 block contains num.
    private static boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < SRN; i++)
            for (int j = 0; j < SRN; j++)
                if (arr[rowStart + i][colStart + j] == num)
                    return false;
        return true;
    }

    // Fill a 3 x 3 matrix.
    private static void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < SRN; i++) {
            for (int j = 0; j < SRN; j++) {
                do {
                    num = randomGenerator(SIZE);
                }
                while (!unUsedInBox(row, col, num));

                arr[row + i][col + j] = num;
            }
        }
    }

    // Random generator
    private static int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    // Check if safe to put in cell
    private static boolean CheckIfSafe(int i, int j, int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % SRN, j - j % SRN, num));
    }

    // check in the row for existence
    private static boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < SIZE; j++)
            if (arr[i][j] == num)
                return false;
        return true;
    }

    // check in the row for existence
    private static boolean unUsedInCol(int j, int num) {
        for (int i = 0; i < SIZE; i++)
            if (arr[i][j] == num)
                return false;
        return true;
    }

    // A recursive function to fill remaining
    // matrix
    private static boolean fillRemaining(int i, int j) {
        // System.out.println(i+" "+j);
        if (j >= SIZE && i < SIZE - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= SIZE && j >= SIZE)
            return true;

        if (i < SRN) {
            if (j < SRN)
                j = SRN;
        } else if (i < SIZE - SRN) {
            if (j == (int) (i / SRN) * SRN)
                j = j + SRN;
        } else {
            if (j == SIZE - SRN) {
                i = i + 1;
                j = 0;
                if (i >= SIZE)
                    return true;
            }
        }

        for (int num = 1; num <= SIZE; num++) {
            if (CheckIfSafe(i, j, num)) {
                arr[i][j] = num;
                if (fillRemaining(i, j + 1))
                    return true;

                arr[i][j] = 0;
            }
        }
        return false;
    }

    // Remove the K no. of digits to
    // complete game
    private static void removeKDigits() {
        int count = numberOfMissingDigit;
        while (count != 0) {
            int cellId = randomGenerator(SIZE * SIZE) - 1;

            // System.out.println(cellId);
            // extract coordinates i and j
            int i = (cellId / SIZE);
            int j = cellId % 9;
            if (j != 0)
                j = j - 1;

            // System.out.println(i+" "+j);
            if (arr[i][j] != 0) {
                count--;
                arr[i][j] = 0;
            }
        }
    }
}


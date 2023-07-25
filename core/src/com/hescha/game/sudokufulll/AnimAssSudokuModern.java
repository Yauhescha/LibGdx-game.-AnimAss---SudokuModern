package com.hescha.game.sudokufulll;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.hescha.game.sudokufulll.screen.LoadingScreen;

public class AnimAssSudokuModern extends Game {
    public static final String PREFERENCE_SAVING_PATH = "AnimAss_Sudoku";

    public static final AssetManager assetManager = new AssetManager();

    public static float WORLD_WIDTH = 720;
    public static float WORLD_HEIGHT = 1280;
    public static AnimAssSudokuModern launcher;
    public static Color BACKGROUND_COLOR = new Color(242f / 255, 231f / 255, 216f / 255, 1);

    @Override
    public void create() {
        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();
        launcher = this;
        setScreen(new LoadingScreen());
    }
}

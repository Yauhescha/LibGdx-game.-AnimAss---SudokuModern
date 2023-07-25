package com.hescha.game.sudokufulll.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.sudokufulll.AnimAssSudokuModern;

public class LoadingScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 960;
    private static final float WORLD_HEIGHT = 544;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;

    private float progress = 0;


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

//        AnimAssSudokuModern.assetManager.get("ui/ClosedButton.png", Texture.class);

        AnimAssSudokuModern.assetManager.load("ui/button.png", Texture.class);
        AnimAssSudokuModern.assetManager.load("ui/fieldSelectionButton.png", Texture.class);
        AnimAssSudokuModern.assetManager.load("ui/cleanIcon.png", Texture.class);
        AnimAssSudokuModern.assetManager.load("ui/header.png", Texture.class);
        AnimAssSudokuModern.assetManager.load("ui/ClosedButton.png", Texture.class);
        AnimAssSudokuModern.assetManager.load("ui/buttonGreen.png", Texture.class);
        AnimAssSudokuModern.assetManager.load("ui/mainheader.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void update() {
        if (AnimAssSudokuModern.assetManager.update()) {
            AnimAssSudokuModern.launcher.setScreen(new MainMenuScreen());
        } else {
            progress = AnimAssSudokuModern.assetManager.getProgress();
        }
    }

    private void clearScreen() {
        ScreenUtils.clear(Color.BLACK);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2, WORLD_HEIGHT / 2 -
                        PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }
}

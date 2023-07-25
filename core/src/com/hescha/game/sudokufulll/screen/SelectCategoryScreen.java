package com.hescha.game.sudokufulll.screen;


import static com.hescha.game.sudokufulll.AnimAssSudokuModern.BACKGROUND_COLOR;
import static com.hescha.game.sudokufulll.util.LevelUtil.loadLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.sudokufulll.AnimAssSudokuModern;
import com.hescha.game.sudokufulll.MyFunctionalInterface;
import com.hescha.game.sudokufulll.model.SudokuDifficulty;
import com.hescha.game.sudokufulll.util.FontUtil;
import com.hescha.game.sudokufulll.util.Level;

import java.util.List;
import java.util.stream.Collectors;

public class SelectCategoryScreen extends ScreenAdapter {
    public static SelectCategoryScreen screen;
    private final SudokuDifficulty sudokuDifficulty;
    private Stage stage;
    private BitmapFont font;
    private Table innerTable;
    private Viewport viewport;
    private final boolean isGalleryMode;

    public SelectCategoryScreen(SudokuDifficulty sudokuDifficulty, boolean isGalleryMode) {
        this.sudokuDifficulty = sudokuDifficulty;
        this.isGalleryMode = isGalleryMode;
    }

    @Override
    public void show() {
        screen = this;
        float worldWidth = Gdx.graphics.getWidth();
        float worldHeight = Gdx.graphics.getHeight();
        OrthographicCamera camera = new OrthographicCamera(worldWidth, worldHeight);
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply(true);

        Texture buttonTexture = AnimAssSudokuModern.assetManager.get("ui/button.png", Texture.class);
        Texture headerTexture = AnimAssSudokuModern.assetManager.get("ui/header.png", Texture.class);

        Table table = new Table();
        table.setFillParent(true);
        font = FontUtil.generateFont(Color.BLACK);
        innerTable = new Table();

        createButton(headerTexture, sudokuDifficulty.name().replace("_", " "), 50, null);
        createButton(buttonTexture, "BACK", 100, addAction(() -> {screen=null;AnimAssSudokuModern.launcher.setScreen(SelectDifficultyScreen.screen);}));

        List<Level> levels = loadLevels().stream()
                .filter(level -> sudokuDifficulty == level.getSudoku().getSudokuDifficulty())
                .collect(Collectors.toList());
        List<String> categories = levels
                .stream()
                .map(Level::getCategory)
                .distinct()
                .collect(Collectors.toList());

        for (String category : categories) {
            createButton(buttonTexture, category, 0, addAction(() -> AnimAssSudokuModern.launcher.setScreen(new SelectLevelScreen(sudokuDifficulty, category, levels, isGalleryMode))));
        }


        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label emptyLabel1 = new Label("", labelStyle);
        innerTable.add(emptyLabel1).pad(200).row();

        ScrollPane scrollPane = new ScrollPane(innerTable);
        table.add(scrollPane);
        stage = new Stage(viewport);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void createButton(Texture headerTexture, String CATEGORIES, int padBottom, EventListener listener) {
        TextureRegion headerCover = new TextureRegion(headerTexture);
        TextureRegionDrawable buttonDrawable0 = new TextureRegionDrawable(headerCover);
        ImageTextButton imageTextButton0 = new ImageTextButton(CATEGORIES, new ImageTextButton.ImageTextButtonStyle(buttonDrawable0, null, null, font));
        innerTable.add(imageTextButton0).center().padTop(10).padBottom(padBottom).row();
        if (listener != null) {
            imageTextButton0.addListener(listener);
        }
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND_COLOR);
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private ClickListener addAction(MyFunctionalInterface lambda) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lambda.perform();
            }
        };
    }
}

package com.hescha.game.sudokufulll.screen;


import static com.hescha.game.sudokufulll.AnimAssSudokuModern.BACKGROUND_COLOR;
import static com.hescha.game.sudokufulll.AnimAssSudokuModern.PREFERENCE_SAVING_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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

public class SelectLevelScreen extends ScreenAdapter {
    public static SelectLevelScreen screen;
    private SudokuDifficulty levelType;
    private String category;
    private List<Level> levels;
    private Stage stage;
    private BitmapFont font;
    private Table innerTable;
    private Viewport viewport;
    private boolean isGalleryMode;

    public SelectLevelScreen(SudokuDifficulty levelType, String category, List<Level> levels, boolean isGalleryMode) {
        this.levelType = levelType;
        this.category = category;
        this.levels = levels.stream().filter(level -> category.equals(level.getCategory())).collect(Collectors.toList());
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
        Texture closedButtonTexture = AnimAssSudokuModern.assetManager.get("ui/ClosedButton.png", Texture.class);
        Texture buttonGreenTexture = AnimAssSudokuModern.assetManager.get("ui/buttonGreen.png", Texture.class);
        Texture headerTexture = AnimAssSudokuModern.assetManager.get("ui/header.png", Texture.class);

        Table table = new Table();
        table.setFillParent(true);
        font = FontUtil.generateFont(Color.WHITE);
        innerTable = new Table();

        createButton(headerTexture, levelType.name().replace("_", " ") + "\n" + category, 50, null);
        createButton(buttonTexture, "BACK", 100, addAction(() -> {screen=null;AnimAssSudokuModern.launcher.setScreen(SelectCategoryScreen.screen);}));


        Preferences prefs = Gdx.app.getPreferences(PREFERENCE_SAVING_PATH);
        for (Level level : levels) {

            String levelScoreSavingPath = level.getSudoku().getSudokuDifficulty().name() + "-" + level.getCategory() + "-" + level.getName();
            int moves = prefs.getInteger(levelScoreSavingPath, -1);
            boolean isPassed = moves != -1;

            if (isPassed && isGalleryMode) {
                createButton(buttonGreenTexture, level.getName(), 10, addAction(() -> AnimAssSudokuModern.launcher.setScreen(new GalleryScreen(level))));
            } else if (isPassed && !isGalleryMode) {
                createButton(buttonGreenTexture, level.getName(), 10, addAction(() -> AnimAssSudokuModern.launcher.setScreen(new GameScreen(level))));
            } else if (!isPassed && isGalleryMode) {
                createButton(closedButtonTexture, level.getName(), 10, null);
            } else if (!isPassed && !isGalleryMode) {
                createButton(buttonTexture, level.getName(), 10, addAction(() -> AnimAssSudokuModern.launcher.setScreen(new GameScreen(level))));
            }
        }


        ScrollPane scrollPane = new ScrollPane(innerTable);
        table.add(scrollPane);
        stage = new Stage(viewport);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label emptyLabel1 = new Label("", labelStyle);
        innerTable.add(emptyLabel1).pad(200).row();
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
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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

package com.hescha.game.sudokufulll.screen;


import static com.hescha.game.sudokufulll.AnimAssSudokuModern.BACKGROUND_COLOR;
import static com.hescha.game.sudokufulll.AnimAssSudokuModern.WORLD_HEIGHT;
import static com.hescha.game.sudokufulll.AnimAssSudokuModern.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.hescha.game.sudokufulll.util.FontUtil;
import com.hescha.game.sudokufulll.model.Level;


public class GalleryScreen extends ScreenAdapter {
    private final Level level;
    private Viewport viewport;
    private Stage stageInfo;
    private BitmapFont bitmapFont;
    OrthographicCamera camera;
    SpriteBatch batch;

    public GalleryScreen(Level level) {
        this.level = level;
    }

    @Override
    public void show() {
        float worldWidth = 720;
        float worldHeight = 1280;
        camera = new OrthographicCamera(worldWidth, worldHeight);
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply(true);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        bitmapFont = FontUtil.generateFont(Color.BLACK);

        stageInfo = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stageInfo);

        Texture buttonTexture = AnimAssSudokuModern.assetManager.get("ui/button.png", Texture.class);


        Table table = new Table();
        stageInfo.addActor(table);
        Table innerTable = new Table();
        table.setFillParent(true);


        TextureRegion mainBoard2 = new TextureRegion(buttonTexture);
        TextureRegionDrawable buttonDrawable2 = new TextureRegionDrawable(mainBoard2);
        String text = level.getSudoku().getSudokuDifficulty().name() + "\n" + level.getName();
        ImageTextButton imageTextButton2 = new ImageTextButton(text.replace("_", " "),
                new ImageTextButton.ImageTextButtonStyle(buttonDrawable2, null, null, bitmapFont));
        innerTable.add(imageTextButton2).top().row();

        for (int i = 1; i <= 9; i++) {
            Texture mainImage = new Texture(Gdx.files.internal(level.getImagePath() + "/" + i + ".jpg"));
            TextureRegion mainBoard = new TextureRegion(mainImage);
            TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(mainBoard);
            ImageTextButton imageTextButton = new ImageTextButton("", new ImageTextButton.ImageTextButtonStyle(buttonDrawable, null, null, bitmapFont));
            innerTable.add(imageTextButton).top().row();
        }


        TextureRegion btnBack = new TextureRegion(buttonTexture);
        TextureRegionDrawable buttonDrawable1 = new TextureRegionDrawable(btnBack);
        ImageTextButton imageTextButton1 = new ImageTextButton("Back", new ImageTextButton.ImageTextButtonStyle(buttonDrawable1, null, null, bitmapFont));
        int imageHeight = 512;
        innerTable.add(imageTextButton1).center().padTop(10).padBottom(imageHeight).row();
        imageTextButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AnimAssSudokuModern.launcher.setScreen(SelectLevelScreen.screen);
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Label emptyLabel1 = new Label(" ", labelStyle);
        innerTable.add(emptyLabel1);

        ScrollPane scrollPane = new ScrollPane(innerTable);
        table.add(scrollPane);
        level.clearBoard();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND_COLOR);
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        stageInfo.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stageInfo.draw();
    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
        stageInfo.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}

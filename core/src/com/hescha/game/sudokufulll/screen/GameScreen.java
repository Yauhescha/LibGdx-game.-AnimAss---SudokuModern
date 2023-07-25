package com.hescha.game.sudokufulll.screen;


import static com.hescha.game.sudokufulll.AnimAssSudokuModern.BACKGROUND_COLOR;
import static com.hescha.game.sudokufulll.AnimAssSudokuModern.PREFERENCE_SAVING_PATH;
import static com.hescha.game.sudokufulll.AnimAssSudokuModern.WORLD_HEIGHT;
import static com.hescha.game.sudokufulll.AnimAssSudokuModern.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.sudokufulll.AnimAssSudokuModern;
import com.hescha.game.sudokufulll.model.Sudoku;
import com.hescha.game.sudokufulll.model.SudokuCell;
import com.hescha.game.sudokufulll.model.SudokuCellType;
import com.hescha.game.sudokufulll.service.SudokuService;
import com.hescha.game.sudokufulll.util.FontUtil;
import com.hescha.game.sudokufulll.model.Level;

import java.util.HashMap;
import java.util.Map;


public class GameScreen extends ScreenAdapter {
    public final Level level;
    public static final String RESULT_SAVED = "Result saved ";
    public static final String CHECK_IF_GAME_EDNDED = "CHECK if GAME Ended: ";
    public static final String CLICKED_BY_BUMBR = "CLICKED BY Number: ";
    public static Sudoku sudoku;
    private Viewport viewport;
    private Stage stageInfo;
    private String levelScoreSavingPath;
    public static GlyphLayout glyphLayout;
    Table tableContainer;
    Table tableCells;
    Table tableNumbers;
    SpriteBatch batch;
    TextureRegionDrawable[][] boardTextures = new TextureRegionDrawable[9][9];
    ImageTextButton[][] boardTexturesButtons = new ImageTextButton[9][9];

    public Texture textureSelectedCell;
    public Texture textureEmptyCell;
    public Texture texturePermanentCell;

    public static BitmapFont fontBlack;
    public static BitmapFont fontRed;

    private float elapsedTime;
    private float minTime;
    ImageTextButton infoLabel;
    boolean isSolved = false;


    TextureRegion textureRegion1;
    TextureRegion textureRegionSelectedCell;
    TextureRegion textureRegionEmptyCell;
    Map<Integer, TextureRegion> map = new HashMap<>();
    OrthographicCamera camera;

    public GameScreen(Level level) {
        this.level = level;
    }

    @Override
    public void show() {
        glyphLayout = new GlyphLayout();


        fontBlack = FontUtil.generateFont(Color.BLACK);
        fontRed = FontUtil.generateFont(Color.RED);

        textureEmptyCell = new Texture(Gdx.files.internal("ui/textureEmptyCell.png"));
        textureSelectedCell = new Texture(Gdx.files.internal("ui/textureSelectedCell.png"));
        texturePermanentCell = new Texture(Gdx.files.internal("ui/texturePermanentCell.png"));


        textureRegion1 = new TextureRegion(texturePermanentCell);
        textureRegionSelectedCell = new TextureRegion(textureSelectedCell);
        textureRegionEmptyCell = new TextureRegion(textureEmptyCell);

        map.put(0, textureRegionEmptyCell);
        for (int i = 1; i <= 9; i++) {
            TextureRegion numberTexture = new TextureRegion(new Texture(Gdx.files.internal(level.getImagePath() + i + ".jpg")));
            map.put(i, numberTexture);
        }

        float worldWidth = WORLD_WIDTH;
        float worldHeight = WORLD_HEIGHT;
        camera = new OrthographicCamera(worldWidth, worldHeight);
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply(true);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        sudoku = level.getSudoku();
        stageInfo = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stageInfo);

        tableContainer = new Table();
        tableContainer.setFillParent(true);
        stageInfo.addActor(tableContainer);


        Texture texture = AnimAssSudokuModern.assetManager.get("ui/button.png", Texture.class);
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(textureRegion);
        infoLabel = new ImageTextButton("INFO", new ImageTextButton.ImageTextButtonStyle(buttonDrawable, null, null, fontBlack));
        tableContainer.add(infoLabel).expandX().fillX().padTop(10).padBottom(20).row();

        tableContainer.add(loadSudokuBoard()).center().row();
        tableContainer.add(loadNumbersForFilling()).center().row();

        Texture buttonTexture = AnimAssSudokuModern.assetManager.get("ui/button.png", Texture.class);
        TextureRegion btnBack = new TextureRegion(buttonTexture);
        TextureRegionDrawable buttonDrawable1 = new TextureRegionDrawable(btnBack);
        ImageTextButton imageTextButton1 = new ImageTextButton("Back", new ImageTextButton.ImageTextButtonStyle(buttonDrawable1, null, null, fontBlack));
        tableContainer.add(imageTextButton1).center().padTop(10).row();
        imageTextButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AnimAssSudokuModern.launcher.setScreen(SelectLevelScreen.screen);
            }
        });


        levelScoreSavingPath = level.getSudoku().getSudokuDifficulty().name() + "-" + level.getName();
        Preferences prefs = Gdx.app.getPreferences(PREFERENCE_SAVING_PATH);
        minTime = prefs.getInteger(levelScoreSavingPath, 9999);
    }

    private Table loadSudokuBoard() {
        tableCells = new Table();
        SudokuCell[][] tiles = sudoku.getBoard();
        int pad = 5;
        int padBottom = 20;
        float size = WORLD_WIDTH / 11;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuCell sudokuCell = tiles[i][j];
                TextureRegionDrawable regionDrawable = new TextureRegionDrawable(textureRegionEmptyCell);
                ImageTextButton imageTextButton = new ImageTextButton("",
                        new ImageTextButton.ImageTextButtonStyle(regionDrawable, regionDrawable, regionDrawable, fontBlack));
                imageTextButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println(CLICKED_BY_BUMBR + sudokuCell.getNumber());
                        GameScreen.sudoku.setSelectedSell(sudokuCell);
                    }
                });

                boardTextures[i][j] = regionDrawable;
                boardTexturesButtons[i][j] = imageTextButton;

                i++;
                j++;
                if (i % 3 == 0 && j % 3 == 0) {
                    tableCells.add(imageTextButton).size(size).pad(pad).padBottom(padBottom).padRight(padBottom);
                }
                if (i % 3 == 0 && j % 3 != 0) {
                    tableCells.add(imageTextButton).size(size).pad(pad).padBottom(padBottom).padRight(0);
                }
                if (i % 3 != 0 && j % 3 == 0) {
                    tableCells.add(imageTextButton).size(size).pad(pad).padBottom(0).padRight(padBottom);
                }
                if (i % 3 != 0 && j % 3 != 0) {
                    tableCells.add(imageTextButton).size(size).pad(pad).padBottom(0).padRight(0);
                }
                i--;
                j--;
            }
            tableCells.row();
        }

        // Верните таблицу для размещения в основной таблице
        return tableCells;
    }

    private Table loadNumbersForFilling() {
        tableNumbers = new Table();
        //print numbers
        float size = WORLD_WIDTH / 8;
        for (int i = 1; i <= 9; i++) {
            TextureRegion numberTexture = map.get(i);
            TextureRegionDrawable buttonDrawable1 = new TextureRegionDrawable(numberTexture);
            ImageTextButton imageTextButton1 = new ImageTextButton("", new ImageTextButton.ImageTextButtonStyle(buttonDrawable1, null, null, fontBlack));
            tableNumbers.add(imageTextButton1).size(size).pad(10);
            int finalI = i;
            imageTextButton1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!isSolved && sudoku.getSelectedSell() != null) {
                        sudoku.getSelectedSell().setNumber(finalI);
                    }
                }
            });
            if (i == 5) {
                tableNumbers.row();
            }
        }

        //print clear button
        TextureRegion btnClean = new TextureRegion(AnimAssSudokuModern.assetManager.get("ui/cleanIcon.png", Texture.class));
        TextureRegionDrawable buttonDrawable1 = new TextureRegionDrawable(btnClean);
        ImageTextButton imageTextButton1 = new ImageTextButton(" ", new ImageTextButton.ImageTextButtonStyle(buttonDrawable1, null, null, fontBlack));
        tableNumbers.add(imageTextButton1).size(size).pad(10);
        imageTextButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isSolved && sudoku.getSelectedSell() != null) {
                    sudoku.getSelectedSell().setNumber(0);
                }
            }
        });
        tableNumbers.row();

        return tableNumbers;
    }


    @Override
    public void render(float delta) {
        if (!isSolved) {
            updatePuzzleStatus();
            elapsedTime += Gdx.graphics.getDeltaTime();
        }
        infoLabel.setText("Difficulty: " + level.getSudoku().getSudokuDifficulty().name().replace("_", " ") + "\n" +
                "Seconds: " + (int) elapsedTime + "\n" +
                "Seconds min: " + (int) minTime);

        ScreenUtils.clear(BACKGROUND_COLOR);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuCell tile = sudoku.getBoard()[i][j];
                boardTextures[i][j].setRegion(map.get(tile.getNumber()));
            }
        }

        stageInfo.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stageInfo.draw();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuCell tile = sudoku.getBoard()[i][j];
                if (sudoku.getSelectedSell() == tile) {
                    ImageTextButton button = boardTexturesButtons[i][j];
                    batch.draw(textureSelectedCell, tableCells.getX()+button.getX(), tableCells.getY()+button.getY(), button.getWidth(), button.getHeight());
                }
                if (tile.getCellType() == SudokuCellType.DISABLED) {
                    ImageTextButton button = boardTexturesButtons[i][j];
                    batch.draw(texturePermanentCell, tableCells.getX()+button.getX(), tableCells.getY()+button.getY(), button.getWidth(), button.getHeight());
                }

            }
        }

        batch.end();
    }


    private void updatePuzzleStatus() {
        isSolved = SudokuService.isRowSolvedCorrect(sudoku);
        System.out.println(CHECK_IF_GAME_EDNDED + isSolved);
        if (isSolved) {
            tableNumbers.clear();

            Texture buttonTexture = AnimAssSudokuModern.assetManager.get("ui/button.png", Texture.class);
            TextureRegion btnBack = new TextureRegion(buttonTexture);
            TextureRegionDrawable buttonDrawable1 = new TextureRegionDrawable(btnBack);
            ImageTextButton imageTextButton1 = new ImageTextButton("See image", new ImageTextButton.ImageTextButtonStyle(buttonDrawable1, null, null, fontBlack));
            tableContainer.add(imageTextButton1).center().padTop(10).row();
            imageTextButton1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    isSolved = false;
                    AnimAssSudokuModern.launcher.setScreen(new GalleryScreen(level));

                }
            });
            if (elapsedTime < minTime) {
                saveBestResult();
            }
        }
    }

    private void saveBestResult() {
        minTime = elapsedTime;
        Preferences prefs = Gdx.app.getPreferences(PREFERENCE_SAVING_PATH);
        prefs.putInteger(levelScoreSavingPath, (int) minTime);
        prefs.flush();
        System.out.println(RESULT_SAVED + minTime);
    }

    @Override
    public void dispose() {
        stageInfo.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}

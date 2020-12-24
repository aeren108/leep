package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Set;

import aeren.leep.Assets;
import aeren.leep.Settings;
import aeren.leep.input.GestureHandler;
import aeren.leep.level.Level;
import aeren.leep.level.LevelData;

public class GameState extends State {
    private Level level;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GestureHandler gestureHandler;

    private Stage ui;
    private Skin skin;

    private Table table;
    private Label scoreLabel;
    private Label highscoreLabel;

    //TODO: Constructor with the Level parameter
    public GameState() {
        super(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
    }

    @Override
    void init() {
        ui = new Stage(new ExtendViewport(Settings.WIDTH * 4, Settings.HEIGHT * 4));
        skin = Assets.getInstance().get("ui/ui-skin.json", Skin.class);

        table = new Table();
        table.setPosition(Settings.WIDTH * 2, Settings.HEIGHT * 4, Align.center);

        scoreLabel = new Label("SCORE: [RED]00", skin);
        highscoreLabel = new Label("HIGHSCORE: 00", skin);

        float padAmount = Settings.WIDTH * 4 - (highscoreLabel.getPrefWidth() + scoreLabel.getPrefWidth());
        table.add(scoreLabel).padTop(scoreLabel.getPrefHeight());
        table.add(highscoreLabel).padTop(highscoreLabel.getPrefHeight()).padLeft(padAmount);
        ui.addActor(table);
    }

    @Override
    public void show() {
        level = new Level(LevelData.getLevelDataFromJson("levels/forestlevel.json"));
        mapRenderer = new OrthogonalTiledMapRenderer(level.getData().map);
        gestureHandler = new GestureHandler(level.getPlayer());

        getCamera().position.set(Settings.WIDTH / 2, Settings.HEIGHT / 2, 0);
        Gdx.input.setInputProcessor(gestureHandler);

        addActor(level);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(100f / 255f, 173f / 255f, 234f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        getViewport().apply();
        mapRenderer.setView((OrthographicCamera) this.getCamera());
        mapRenderer.render();

        if (level.isNewRecord())
            highscoreLabel.setText("[MAROON]HIGHSCORE: [GOLD]" + level.getScore());
        else
            highscoreLabel.setText("[BLACK]HIGHSCORE: [GOLD]" + level.getHighscore());

        scoreLabel.setText("[BLACK]SCORE: [GOLD]" + level.getScore());

        super.render(delta);

        ui.getViewport().apply();
        ui.act(delta);
        ui.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        ui.getViewport().update(width, height);
        table.setPosition(ui.getWidth() / 2, ui.getHeight(), Align.center);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        level.dispose();
        super.dispose();
    }

    @Override
    public void popFragment() {
        super.popFragment();
        Gdx.input.setInputProcessor(fragments.isEmpty() ? gestureHandler : fragments.peek());
    }

    public void reset() {
        level.reset();
    }

    @Override
    public String getGroupName() {
        return "game";
    }
}

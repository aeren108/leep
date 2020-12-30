package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.Settings;
import aeren.leep.input.GestureHandler;
import aeren.leep.level.Level;
import aeren.leep.level.LevelDataFactory;
import aeren.leep.level.LevelDataParser;
import aeren.leep.level.ScoreListener;
import aeren.leep.states.fragments.PauseFragment;

public class GameState extends State implements ScoreListener {
    private Level level;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GestureHandler gestureHandler;

    private Stage ui;
    private InputMultiplexer multiplexer;

    private Table table;
    private Label scoreLabel;
    private ImageButton pause;

    //TODO: Constructor with the Level parameter
    public GameState() {
        super(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
    }

    @Override
    public void show() {
        Assets.getInstance().finishLoading();
        Skin skin = Assets.getInstance().get("ui/ui-skin.json", Skin.class);

        ui = new Stage(new ExtendViewport(Settings.WIDTH * 4, Settings.HEIGHT * 4));
        level = new Level(new LevelDataFactory().getLevelData("levels/forestlevel.json"));
        mapRenderer = new OrthogonalTiledMapRenderer(level.getData().map);
        gestureHandler = new GestureHandler(level.getPlayer());

        table = new Table();
        scoreLabel = new Label("SCORE: [GOLD]00", skin);
        pause = new ImageButton(skin.getDrawable("pause-up"));

        float padAmount = Settings.WIDTH * 4 - (pause.getPrefWidth() + scoreLabel.getPrefWidth());
        table.add(scoreLabel).padTop(scoreLabel.getPrefHeight());
        table.add(pause).padTop(pause.getPrefHeight()).padLeft(padAmount);
        table.setPosition(Settings.WIDTH * 2, Settings.HEIGHT * 4, Align.center);

        pause.addListener((Event event) -> {
            if (!level.isGameOver() && event instanceof ChangeListener.ChangeEvent) {
                pauseGame();
                pushFragment(new PauseFragment(this));
            }
            return true;
        });

        ui.addActor(table);
        addActor(level);

        getCamera().position.set(Settings.WIDTH / 2, Settings.HEIGHT / 2, 0);

        multiplexer = new InputMultiplexer(gestureHandler, ui);
        Gdx.input.setInputProcessor(multiplexer);
        level.setScoreListener(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(100f / 255f, 173f / 255f, 234f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        super.render(delta);

        ui.act(delta);
        ui.draw();
    }

    @Override
    public void draw() {
        mapRenderer.setView((OrthographicCamera) getCamera());
        mapRenderer.render();

        super.draw();
    }

    public void pauseGame() {
        level.pause();
    }

    public void resumeGame() {
        level.resume();
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        ui.dispose();
        mapRenderer.dispose();
        level.dispose();

        super.dispose();
    }

    @Override
    public void popFragment() {
        super.popFragment();
        Gdx.input.setInputProcessor(fragments.isEmpty() ? multiplexer : fragments.peek());
    }

    public void reset() {
        level.reset();
    }

    @Override
    public void onScoreChanged(int score, boolean isRecord) {
        String text = isRecord ? "[PURPLE]HIGHSCORE: [GOLD]" + score : "[BLACK]SCORE: [GOLD]" + score;
        scoreLabel.setText(text);
    }
}

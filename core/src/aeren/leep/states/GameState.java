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
  
  private Stage ui;
  private Skin skin;
  
  private Table table;
  private Label scoreLabel;
  private Label highscoreLabel;
  
  //TODO: Constructor with the Level parameter
  public GameState() {
    super(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
    ui = new Stage(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
  }
  
  private void initUi() {
    skin = Assets.manager.get(Assets.skin);
    
    table = new Table();
    table.setPosition(Settings.WIDTH / 2, Settings.HEIGHT - 8, Align.center);
    
    scoreLabel = new Label("SKOR: ", skin);
    
    table.add(scoreLabel);
    ui.addActor(table);
  }

  @Override
  public void show() {
    level = new Level(LevelData.getLevelDataFromJson("levels/forestlevel.json"));
    mapRenderer = new OrthogonalTiledMapRenderer(level.getData().map);
    
    getCamera().position.set(Settings.WIDTH / 2, Settings.HEIGHT / 2, 0);
    Gdx.input.setInputProcessor(new GestureHandler(level.getPlayer()));
    
    addActor(level);
    initUi();
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(100f / 255f, 173f / 255f, 234f / 255f, 1f);
    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
  
    getViewport().apply();
    mapRenderer.setView((OrthographicCamera) this.getCamera());
    mapRenderer.render();
    
    if (level.isNewRecord()) {
      scoreLabel.getStyle().fontColor = Color.SCARLET;
      scoreLabel.setText("HIGHSCORE: " + level.getScore());
    } else {
      scoreLabel.getStyle().fontColor = Color.BLACK;
      scoreLabel.setText("SCORE: " + level.getScore());
    }
    
    super.render(delta);
    
    ui.getViewport().apply();
    ui.act();
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
    
    super.resize(width, height);
  }
}

package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aeren.leep.Settings;
import aeren.leep.input.GestureHandler;
import aeren.leep.level.Level;
import aeren.leep.level.LevelData;

public class GameState extends State {
  private Level level;
  private OrthogonalTiledMapRenderer mapRenderer;
  
  private Stage ui;
  private Label scoreLabel;
  private Label highscoreLabel;
  
  //TODO: Constructor with the Level parameter
  public GameState() {
    super(new FitViewport(Settings.WIDTH, Settings.HEIGHT));
    ui = new Stage(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
    
    initUi();
  }
  
  private void initUi() {
    Label.LabelStyle style = new Label.LabelStyle();
    style.font = new BitmapFont(Gdx.files.internal("fonts/retro_gaming.fnt"));
    style.fontColor = Color.DARK_GRAY;
    
    scoreLabel = new Label("SKOR: ", style);
    highscoreLabel = new Label("HS: ", style);
    
    ui.addActor(scoreLabel);
    ui.addActor(highscoreLabel);
  }

  @Override
  public void show() {
    level = new Level(LevelData.getLevelDataFromJson("levels/forestlevel.json"));
    mapRenderer = new OrthogonalTiledMapRenderer(level.getData().map);
    
    getCamera().position.set(Settings.WIDTH / 2, Settings.HEIGHT / 2, 0);
    Gdx.input.setInputProcessor(new GestureHandler(level.getPlayer()));
    
    addActor(level);
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(100f / 255f, 173f / 255f, 234f / 255f, 1f);
    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
  
    getViewport().apply();
    mapRenderer.setView((OrthographicCamera) this.getCamera());
    mapRenderer.render();
    
    scoreLabel.setText("SKOR: " + level.getScore());
    highscoreLabel.setText("HS: " + level.getHighscore());
  
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
    scoreLabel.setPosition(2f, ui.getHeight() - 12);
    highscoreLabel.setPosition(ui.getWidth() - highscoreLabel.getWidth() - 16, ui.getHeight() - 12);
    super.resize(width, height);
  }
}

package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aeren.leep.Settings;
import aeren.leep.input.GestureHandler;
import aeren.leep.level.Level;
import aeren.leep.level.LevelData;

public class GameState extends State {
  private Level level;
  private LevelData data;
  
  private OrthogonalTiledMapRenderer mapRenderer;
  
  public GameState() {
    super(new FitViewport(Settings.WIDTH, Settings.HEIGHT));
  }

  @Override
  public void show() {
    data = new LevelData("levels/forestlevel.json");
    level = new Level(data);
    mapRenderer = new OrthogonalTiledMapRenderer(data.getMap());
    
    getCamera().position.set(Settings.WIDTH / 2, Settings.HEIGHT / 2, 0);
    Gdx.input.setInputProcessor(new GestureHandler(level.getPlayer()));
    
    addActor(level);
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(100f/255f, 173f/255f, 234f/255f, 1f);
    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
    
    mapRenderer.setView((OrthographicCamera) this.getCamera());
    mapRenderer.render();
    
    super.render(delta);
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
}

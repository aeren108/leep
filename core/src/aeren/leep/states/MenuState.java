package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.Settings;

public class MenuState extends State {
  private Skin skin;
  
  private Table table;
  private Label title;
  private TextButton play;
  private TextButton stats;
  private TextButton settings;
  
  public MenuState() {
    super(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
  }

  @Override
  public void show() {
    skin = Assets.manager.get(Assets.skin);
    
    table = new Table();
    table.align(Align.center);
    
    title = new Label("LEEP", skin, "title");
    
    play = new TextButton("PLAY", skin);
    play.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        StateManager.getInstance().setState(new GameState());
      }
    });
    
    stats = new TextButton("STATS", skin);
    settings = new TextButton("SETTINGS", skin);
    
    table.padTop(-48);
    table.add(title).row();
    table.add(play).minWidth(54).spaceTop(8).row();
    table.add(stats).minWidth(54).spaceTop(4).row();
    table.add(settings).minWidth(54).spaceTop(4);
    
    this.addActor(table);
    Gdx.input.setInputProcessor(this);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(255f / 255f, 245f / 255f, 214f / 255f, 1f);
    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
    
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

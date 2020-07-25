package aeren.leep.states;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

abstract class State extends Stage implements Screen {

  public State(Viewport viewport) {
    this.setViewport(viewport);
  }

  @Override
  public void render(float delta) {
    this.act(delta);
    this.draw();
  }

  @Override
  public void resize(int width, int height) {
    this.getViewport().update(width, height);
  }
}

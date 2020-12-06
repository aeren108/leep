package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

abstract class State extends Stage implements Screen {

  private boolean backReleased = true;

  public State(Viewport viewport) {
    this.setViewport(viewport);
    init();
  }

  abstract void init();

  @Override
  public void render(float delta) {
    if (backReleased && Gdx.input.isKeyPressed(Input.Keys.BACK)) {
      StateManager.getInstance().popState();
      backReleased = false;
    } else backReleased = true;

    this.act(delta);
    this.draw();
  }

  @Override
  public void resize(int width, int height) {
    this.getViewport().update(width, height);
  }
}

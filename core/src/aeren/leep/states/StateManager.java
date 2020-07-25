package aeren.leep.states;

import com.badlogic.gdx.Game;

public class StateManager {

  private static StateManager instance = null;

  private Game game;
  private State state;

  private StateManager() {

  }

  public void setGame(Game game) {
    this.game = game;
  }

  public void setState(State state) {
    this.state = state;
    
    if (game == null)
      throw new NullPointerException("StateManager: game is null");
    
    game.setScreen(state);
  }

  public State getState() {
    return state;
  }

  public static StateManager getInstance() {
    if (instance == null)
      instance = new StateManager();
    return instance;
  }
}

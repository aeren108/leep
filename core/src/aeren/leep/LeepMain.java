package aeren.leep;

import com.badlogic.gdx.Game;

import aeren.leep.states.GameState;
import aeren.leep.states.MenuState;
import aeren.leep.states.StateManager;

public class LeepMain extends Game {

	@Override
	public void create () {
		Assets.loadAssets();
		Assets.manager.finishLoading();
	
		StateManager stateManager = StateManager.getInstance();
		stateManager.setGame(this);
		stateManager.setState(new MenuState());
	}

	@Override
	public void dispose() {
		Assets.dispose();
		
		super.dispose();
	}
}

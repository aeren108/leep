package aeren.leep;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aeren.leep.states.GameState;
import aeren.leep.states.StateManager;

public class LeepMain extends Game {
	private StateManager stateManager;
	
	@Override
	public void create () {
		Assets.loadAssets();
		Assets.manager.finishLoading();
		
		stateManager = StateManager.getInstance();
		stateManager.setGame(this);
		stateManager.setState(new GameState());
	}

	@Override
	public void dispose() {
		Assets.dispose();
		
		super.dispose();
	}
}

package aeren.leep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import aeren.leep.states.MenuState;
import aeren.leep.states.StateManager;

public class LeepMain extends Game {

    @Override
    public void create() {
        Assets.loadAssets();
        Assets.manager.finishLoading();

        Assets.manager.get(Assets.skin).getFont("orange-kid").getData().markupEnabled = true;
        Assets.manager.get(Assets.skin).getFont("orange-kid-title").getData().markupEnabled = true;

        StateManager stateManager = StateManager.getInstance();
        stateManager.setGame(this);
        stateManager.pushState(new MenuState());

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void dispose() {
        Assets.dispose();
        super.dispose();
    }
}

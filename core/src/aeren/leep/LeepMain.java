package aeren.leep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import aeren.leep.states.MenuState;
import aeren.leep.states.StateManager;

public class LeepMain extends Game {
    private Assets assets;

    public LeepMain() {

    }

    @Override
    public void create() {
        assets = Assets.getInstance();

        assets.get("ui/ui-skin.json", Skin.class).getFont("orange-kid").getData().markupEnabled = true;
        assets.get("ui/ui-skin.json", Skin.class).getFont("orange-kid-title").getData().markupEnabled = true;

        StateManager stateManager = StateManager.getInstance();
        stateManager.setGame(this);
        stateManager.pushState(new MenuState());

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void dispose() {
        assets.dispose();
        super.dispose();
    }
}

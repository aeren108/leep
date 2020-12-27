package aeren.leep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import aeren.leep.character.CharacterManager;
import aeren.leep.states.StateEnum;
import aeren.leep.states.StateManager;

public class LeepMain extends Game {
    private Assets assets;
    private CharacterManager cm;
    public AndroidCallback callback;

    public LeepMain(AndroidCallback callback) {
        this.callback = callback;
    }

    @Override
    public void create() {
        assets = Assets.getInstance();
        cm = CharacterManager.getInstance();

        assets.get("ui/ui-skin.json", Skin.class).getFont("orange-kid").getData().markupEnabled = true;
        assets.get("ui/ui-skin.json", Skin.class).getFont("orange-kid-title").getData().markupEnabled = true;
        cm.loadCharacters();

        StateManager stateManager = StateManager.getInstance();
        stateManager.initialize(this);
        stateManager.pushState(StateEnum.MAIN_MENU);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void dispose() {
        assets.dispose();
        super.dispose();
    }
}

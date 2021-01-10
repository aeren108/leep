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
    private DataManager dm;
    private CharacterManager cm;
    public AndroidCallback callback;

    public LeepMain(AndroidCallback callback) {
        this.callback = callback;
    }

    @Override
    public void create() {
        copyFiles();

        assets = Assets.getInstance();
        dm = DataManager.getInstance();
        cm = CharacterManager.getInstance();

        dm.loadData();
        cm.loadCharacters();
        assets.loadGroup("splash");
        assets.finishLoading();

        StateManager stateManager = StateManager.getInstance();
        stateManager.initialize(this);
        stateManager.pushState(StateEnum.SPLASH);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void dispose() {
        assets.dispose();
        super.dispose();
    }

    private void copyFiles() {
        if (!Gdx.files.local("data.json").exists())
            Gdx.files.internal("data.json").copyTo(Gdx.files.local(""));
    }
}

package aeren.leep.states.fragments;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.states.State;
import aeren.leep.Settings;

public abstract class Fragment extends Stage {

    protected State state;

    public Fragment(State state) {
        this.state = state;
        this.setViewport(new ExtendViewport(Settings.WIDTH * 4, Settings.HEIGHT * 4));
        init();
    }

    public abstract void init();
}

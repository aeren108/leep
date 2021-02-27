package aeren.leep.states.fragments;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.states.State;
import aeren.leep.Constants;

public abstract class Fragment extends Stage implements Disposable {

    protected State state;

    public Fragment(State state) {
        this.state = state;
        this.setViewport(new ExtendViewport(Constants.UI_WIDTH, Constants.UI_HEIGHT));
    }

    public abstract void init();
}

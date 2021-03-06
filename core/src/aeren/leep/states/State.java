package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Stack;

import aeren.leep.Assets;
import aeren.leep.states.fragments.Fragment;

public abstract class State extends Stage implements Screen {
    protected Stack<Fragment> fragments;

    public State(Viewport viewport) {
        this.setViewport(viewport);
        this.fragments = new Stack<>();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            StateManager.getInstance().popState();
        }

        this.act(delta);
        this.draw();

        if (!fragments.isEmpty()) {
            fragments.peek().getViewport().apply();
            fragments.peek().draw();
            fragments.peek().act(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.getViewport().update(width, height);
    }

    public void pushFragment(Fragment f) {
        fragments.push(f);
        f.init();
        f.getViewport().update(getViewport().getScreenWidth(), getViewport().getScreenHeight());

        Gdx.input.setInputProcessor(fragments.peek());
    }

    public void popFragment() {
        fragments.peek().dispose();
        fragments.pop();

        Gdx.input.setInputProcessor(fragments.isEmpty() ? this : fragments.peek());
    }
}

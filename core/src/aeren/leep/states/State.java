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

    private boolean backReleased = true;
    protected Stack<Fragment> fragments;

    public State(Viewport viewport) {
        this.setViewport(viewport);
        this.fragments = new Stack<>();
    }

    abstract String getGroupName();

    @Override
    public void render(float delta) {
        if (backReleased && Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            StateManager.getInstance().popState();
            backReleased = false;
        } else backReleased = true;

        this.act(delta);
        this.draw();

        if (!fragments.isEmpty()) {
            fragments.peek().getViewport().apply();
            fragments.peek().act(delta);
            fragments.peek().draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        this.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.getInstance().unloadGroup(getGroupName());
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

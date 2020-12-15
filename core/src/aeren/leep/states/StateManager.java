package aeren.leep.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.lang.reflect.Constructor;
import java.util.Stack;

public class StateManager {

    private static StateManager instance = null;

    private Game game;
    private State state;
    private Stack<State> stateStack;

    private StateManager() {
        stateStack = new Stack();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setState(State state) {
        stateStack.clear();
        this.state.dispose();
        this.state = state;

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        game.setScreen(state);
        stateStack.push(state);
    }

    public State getState(Class<? extends State> cls) {
        for (State s : stateStack) {
            if (s.getClass().equals(cls))
                return s;
        }

        return null;
    }

    public void pushState(State state) {
        //if (this.state != null) this.state.dispose();
        this.state = state;

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        stateStack.push(state);
        game.setScreen(state);
    }

    public void popState() {
        if (stateStack.size() == 1)
            return;

        state.dispose();
        stateStack.pop();

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        state = stateStack.peek();
        game.setScreen(state);
    }

    public State getState() {
        return state;
    }

    public static StateManager getInstance() {
        if (instance == null)
            instance = new StateManager();
        return instance;
    }
}

package aeren.leep.states;

import com.badlogic.gdx.Game;
import java.util.Stack;

import aeren.leep.Assets;
import aeren.leep.LeepMain;

public class StateManager {

    private static StateManager instance = null;

    private Game game;
    private State state;
    private Assets assets;
    private Stack<State> stateStack;

    private StateManager() {
        stateStack = new Stack<>();
        assets = Assets.getInstance();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setState(State state) {
        for (State s : stateStack) {
            assets.unloadGroup(s.getGroupName());
            s.dispose();
        }

        stateStack.clear();
        this.state = state;

        assets.loadGroup(state.getGroupName());
        assets.finishLoading();

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
        if (this.state != null)
            assets.unloadGroup(this.state.getGroupName());

        this.state = state;
        assets.loadGroup(state.getGroupName());
        assets.finishLoading();

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        stateStack.push(state);
        game.setScreen(state);
    }

    public void popState() {
        if (stateStack.size() == 1) {
            ((LeepMain) game).callback.shutDown();
            return;
        }

        assets.unloadGroup(state.getGroupName());
        state.dispose();
        stateStack.pop();

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        assets.loadGroup(stateStack.peek().getGroupName());
        assets.finishLoading();

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

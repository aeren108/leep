package aeren.leep.states;

import com.badlogic.gdx.Game;
import java.util.Stack;

import aeren.leep.Assets;
import aeren.leep.LeepMain;

public class StateManager {

    private static StateManager instance = null;

    private Game game;
    private State curState;
    private Assets assets;
    private Stack<StateEnum> stateStack;

    private StateManager() {
        stateStack = new Stack<>();
        assets = Assets.getInstance();
    }

    public void initialize(Game game) {
        this.game = game;
    }

    public void setState(StateEnum stateEnum) {
        stateStack.clear();
        curState.dispose();

        curState = stateEnum.getState();

        assets.loadGroup(curState.getGroupName());
        assets.finishLoading();

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        game.setScreen(curState);
        stateStack.push(stateEnum);
    }

    public void pushState(StateEnum stateEnum) {
        if (curState != null)
            curState.dispose();

        curState = stateEnum.getState();
        assets.loadGroup(curState.getGroupName());
        assets.finishLoading();

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        stateStack.push(stateEnum);
        game.setScreen(curState);
    }

    public void popState() {
        if (stateStack.size() == 1) {
            ((LeepMain) game).callback.shutDown();
            return;
        }

        curState.dispose();
        stateStack.pop();

        if (game == null)
            throw new NullPointerException("StateManager: game is null");

        curState = stateStack.peek().getState();

        assets.loadGroup(curState.getGroupName());
        assets.finishLoading();
        game.setScreen(curState);
    }

    public State getState() {
        return curState;
    }

    public static StateManager getInstance() {
        if (instance == null)
            instance = new StateManager();
        return instance;
    }
}

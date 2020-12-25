package aeren.leep.states;

public enum StateEnum {
    MAIN_MENU {
        public State getState(Object... params) {
            return new MenuState();
        }
    },
    GAME_STATE {
        public State getState(Object... params) {
            return new GameState();
        }
    };

    public abstract State getState(Object... params);
}

package aeren.leep.states;

public enum StateEnum {
    MAIN_MENU("menu") {
        public State getState(Object... params) {
            return new MenuState();
        }
    },
    GAME_STATE("game") {
        public State getState(Object... params) {
            return new GameState();
        }
    };

    StateEnum(String groupName) {
        this.groupName = groupName;
    }

    public abstract State getState(Object... params);
    public final String groupName;
}

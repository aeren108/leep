package aeren.leep.states;

public enum StateEnum {
    SPLASH("splash") {
        public State getState(Object... params) {
            return new SplashState();
        }
    },
    MAIN_MENU("menu") {
        public State getState(Object... params) {
            return new MenuState();
        }
    },
    CHARACTER_STATE("character") {
        public State getState(Object... params) {
            return new CharacterState();
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

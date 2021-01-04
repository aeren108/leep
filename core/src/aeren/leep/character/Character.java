package aeren.leep.character;

public class Character {
    public final String name, status, condition;
    public final int x, y, conditionValue;
    public boolean unlocked;

    public Character(String name, String status, String condition, int conditionValue, int x, int y, boolean unlocked) {
        this.name = name;
        this.status = status;
        this.condition = condition;
        this.conditionValue = conditionValue;
        this.x = x;
        this.y = y;
        this.unlocked = unlocked;
    }
}

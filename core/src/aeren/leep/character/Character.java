package aeren.leep.character;

public class Character {
    public final String name;
    public final int x, y;
    public boolean unlocked;

    public Character(String name, boolean unlocked, int x, int y) {
        this.name = name;
        this.unlocked = unlocked;
        this.x = x;
        this.y = y;
    }
}

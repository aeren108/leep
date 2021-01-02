package aeren.leep.character;

public class Character {
    public final String name, status;
    public final int x, y;
    public boolean unlocked;

    public Character(String name, String status, boolean unlocked, int x, int y) {
        this.name = name;
        this.status = status;
        this.unlocked = unlocked;
        this.x = x;
        this.y = y;
    }
}

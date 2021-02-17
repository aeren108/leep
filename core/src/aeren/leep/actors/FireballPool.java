package aeren.leep.actors;

import java.util.Stack;

public class FireballPool {
    private Stack<Fireball> freeV;
    private Stack<Fireball> freeH;

    public FireballPool() {
        freeV = new Stack<>();
        freeH = new Stack<>();
    }

    public Fireball obtain(FireballType type) {
        if (type == FireballType.VERTICAL) {
            return (freeV.isEmpty()) ? new Fireball(type) : freeV.pop();
        } else {
            return (freeH.isEmpty()) ? new Fireball(type) : freeH.pop();
        }
    }

    public void free(Fireball f) {
        if (f.getType() == FireballType.VERTICAL) freeV.push(f);
        else freeH.push(f);

        f.reset();
    }

}

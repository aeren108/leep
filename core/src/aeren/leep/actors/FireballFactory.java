package aeren.leep.actors;

import com.badlogic.gdx.utils.Pool;

public class FireballFactory {

    private final Pool<VerticalFireball> vfp = new Pool<VerticalFireball>() {
        @Override
        protected VerticalFireball newObject() {
            return new VerticalFireball();
        }
    };

    private final Pool<HorizontalFireball> hfp = new Pool<HorizontalFireball>() {
        @Override
        protected HorizontalFireball newObject() {
            return new HorizontalFireball();
        }
    };

    public Fireball createFireball(Fireball.FireballType type) {
        switch (type) {
            case VERTICAL:
                return vfp.obtain();
            case HORIZONTAL:
                return hfp.obtain();
            default:
                return null;
        }
    }

    public void destroyFireball(Fireball fireball) {
        if (fireball instanceof VerticalFireball) {
            vfp.free((VerticalFireball) fireball);
        } else if (fireball instanceof HorizontalFireball) {
            hfp.free((HorizontalFireball) fireball);
        }
    }

}

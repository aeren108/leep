package aeren.leep.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import aeren.leep.Constants;

public enum LaserType {
    VERTICAL(1, (int) Constants.HEIGHT / 16) {
        @Override
        public Vector2 setLinage(int linage) {
            return new Vector2(linage * 16, 0);
        }

        @Override
        public Rectangle getBounds(float x, float y) {
            return new Rectangle(x + 4, y, 12, Constants.HEIGHT);
        }

        @Override
        public int getXOffset(int i) {
            return 0;
        }

        @Override
        public int getYOffset(int i) {
            return i * 16;
        }
    },
    HORIZONTAL(0, (int) Constants.WIDTH / 16) {
        @Override
        public Vector2 setLinage(int linage) {
            return new Vector2(0, linage * 16);
        }

        @Override
        public Rectangle getBounds(float x, float y) {
            return new Rectangle(x, y + 4, Constants.WIDTH, 12);
        }

        @Override
        public int getXOffset(int i) {
            return i * 16;
        }

        @Override
        public int getYOffset(int i) {
            return 0;
        }
    };

    private int animIndex;
    private int tileRepeat;

    LaserType(int animIndex, int tileRepeat) {
        this.animIndex = animIndex;
        this.tileRepeat = tileRepeat;
    }

    public abstract Vector2 setLinage(int linage);
    public abstract Rectangle getBounds(float x, float y);
    public abstract int getXOffset(int i);
    public abstract int getYOffset(int i);

    public int getAnimIndex() {
        return animIndex;
    }

    public int getTileRepeat() {
        return tileRepeat;
    }
}

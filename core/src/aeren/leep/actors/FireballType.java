package aeren.leep.actors;

import com.badlogic.gdx.math.Vector2;

import aeren.leep.Constants;

public enum FireballType {
    VERTICAL(Fireball.VEL_DOWN, 0, 4, 4, 0, -16) {
        @Override
        public Vector2 setLinage(int linage) {
            return new Vector2(linage * 16, Constants.HEIGHT);
        }
    },
    HORIZONTAL(Fireball.VEL_RIGHT,1, 3, 4, 16, 0) {
        @Override
        public Vector2 setLinage(int linage) {
            return new Vector2(-16, linage * 16);
        }
    };

    private final Vector2 velocity;
    private final int animIndex;
    private final int xOffset, yOffset;
    private final int alertXOffset, alertYOffset;

    FireballType(Vector2 velocity, int animIndex, int xOffset, int yOffset, int alertXOffset, int alertYOffset) {
        this.velocity = velocity;
        this.animIndex = animIndex;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.alertXOffset = alertXOffset;
        this.alertYOffset = alertYOffset;
    }

    public abstract Vector2 setLinage(int linage);

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getAnimIndex() {
        return animIndex;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int getAlertXOffset() {
        return alertXOffset;
    }

    public int getAlertYOffset() {
        return alertYOffset;
    }
}
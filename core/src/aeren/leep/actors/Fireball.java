package aeren.leep.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

import aeren.leep.Assets;
import aeren.leep.Settings;

public abstract class Fireball extends Actor implements Pool.Poolable {
    protected FireballType type;
    protected Texture alert;
    protected TextureRegion[][] frames;
    protected Animation<TextureRegion> anim;

    protected Vector2 velocity;
    protected Rectangle bounds;
    protected int xOffset = 0, yOffset = 0;

    protected float elapsed = 0;
    protected float alertTimer = 0;
    protected float alertThreshold = 0.5f;
    protected boolean alerted = false;

    public boolean alive = true;
    protected boolean flipped = false;

    public static final Vector2 VEL_UP = new Vector2(0f, 1f);
    public static final Vector2 VEL_DOWN = new Vector2(0f, -1f);
    public static final Vector2 VEL_RIGHT = new Vector2(1f, 0f);
    public static final Vector2 VEL_LEFT = new Vector2(-1f, 0f);

    public Fireball() {
        bounds = new Rectangle(0, 0, 16, 16);
        frames = TextureRegion.split(Assets.getInstance().get("sprites/fireball.png", Texture.class), 16, 16);
        alert = Assets.getInstance().get("sprites/alert.png", Texture.class);
    }

    @Override
    public void act(float delta) {
        elapsed += delta;

        if (!alerted) {
            alertTimer += delta;

            if (alertTimer >= alertThreshold) {
                alerted = true;
                alertTimer = 0;
            }
        } else {
            setPosition(getX() + velocity.x, getY() + velocity.y);
        }

        bounds.setPosition(getX() + xOffset, getY() + yOffset);

        if (getX() > Settings.WIDTH || getY() + 24 < 0)
            alive = false;

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public abstract void setLinage(int linage);

    public abstract void flip();

    @Override
    public void reset() {
        alive = true;
        alerted = false;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public FireballType getType() {
        return type;
    }

    public void setAlertThreshold(float alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public enum FireballType {
        VERTICAL, HORIZONTAL
    }
}

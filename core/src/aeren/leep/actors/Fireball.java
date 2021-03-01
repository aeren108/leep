package aeren.leep.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aeren.leep.Assets;
import aeren.leep.Constants;

public class Fireball extends Actor {
    private FireballType type;
    private TextureRegion alert;
    private TextureRegion[][] frames;
    private Animation<TextureRegion> anim;

    private Vector2 velocity;
    private Rectangle bounds;

    private float elapsed = 0;
    private float alertTimer = 0;
    private float alertThreshold = 0.5f;
    private boolean isAlerted = false;
    public boolean isAlive = true;

    public static final Vector2 VEL_UP = new Vector2(0f, 1f);
    public static final Vector2 VEL_DOWN = new Vector2(0f, -1f);
    public static final Vector2 VEL_RIGHT = new Vector2(1f, 0f);
    public static final Vector2 VEL_LEFT = new Vector2(-1f, 0f);

    public Fireball(FireballType type) {
        frames = TextureRegion.split(Assets.getInstance().get("sprites/fireball.png", Texture.class), 16, 16);
        alert = TextureRegion.split(Assets.getInstance().get("sprites/alert.png", Texture.class), 16, 16)[0][type.getAlertIndex()];
        bounds = new Rectangle(0, 0, 9, 9);

        setType(type);
    }

    @Override
    public void act(float delta) {
        elapsed += delta;

        if (!isAlerted) {
            alertTimer += delta;

            if (alertTimer >= alertThreshold) {
                isAlerted = true;
                alertTimer = 0;
            }
        } else {
            setPosition(getX() + velocity.x, getY() + velocity.y);
        }

        bounds.setPosition(getX() + type.getXOffset(), getY() + type.getYOffset());

        if (getX() > Constants.WIDTH || getY() + 24 < 0)
            isAlive = false;

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (!isAlerted) {
            batch.draw(alert, getX() + type.getAlertXOffset(), getY() + type.getAlertYOffset());
        } else {
            batch.draw(anim.getKeyFrame(elapsed, true), getX(), getY());
        }
    }

    public void setLinage(int linage) {
        Vector2 pos = type.setLinage(linage);
        setPosition(pos.x, pos.y);
    }

    public void reset() {
        isAlive = true;
        isAlerted = false;
        velocity = type.getVelocity();
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

    public void setType(FireballType type) {
        this.type = type;
        this.velocity = type.getVelocity();
        anim = new Animation<>(.1f, frames[type.getAnimIndex()]);
    }

    public FireballType getType() {
        return type;
    }

    public void setAlertThreshold(float alertThreshold) {
        this.alertThreshold = alertThreshold;
    }
}

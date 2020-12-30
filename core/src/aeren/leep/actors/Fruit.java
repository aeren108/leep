package aeren.leep.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import aeren.leep.Assets;

public class Fruit extends Actor {
    private Texture sprite;
    private Rectangle bounds;
    private Animation<TextureRegion> idleAnim;

    private float elapsed = 0;

    public Fruit() {
        sprite = Assets.getInstance().get("sprites/fruit.png", Texture.class);
        bounds = new Rectangle(0, 0, 10, 11);

        TextureRegion[][] frames = TextureRegion.split(sprite, 16, 16);
        idleAnim = new Animation<>(.15f, frames[0]);
    }

    @Override
    public void act(float delta) {
        elapsed += delta;

        bounds.setPosition(this.getX() + 3, this.getY() + 3);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(idleAnim.getKeyFrame(elapsed, true), getX(), getY());

        super.draw(batch, parentAlpha);
    }

    public void respawn() {
        clearActions();

        getColor().a = 0f;
        addAction(Actions.fadeIn(.5f));
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

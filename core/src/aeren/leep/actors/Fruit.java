package aeren.leep.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;

import aeren.leep.Assets;

public class Fruit extends Actor implements Disposable {
    private Texture sprite;
    private Rectangle bounds;
    private Animation<TextureRegion> idleAnim;

    private float elapsed = 0;

    public Fruit() {
        sprite = Assets.getInstance().get("sprites/fruit.png");
        bounds = new Rectangle();

        TextureRegion[][] frames = TextureRegion.split(sprite, 16, 16);
        idleAnim = new Animation<TextureRegion>(.15f, frames[0]);
    }

    @Override
    public void act(float delta) {
        elapsed += delta;

        this.bounds.set(this.getX() + 3, this.getY() + 3, 10, 11);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(idleAnim.getKeyFrame(elapsed, true), getX(), getY());

        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        sprite.dispose();
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

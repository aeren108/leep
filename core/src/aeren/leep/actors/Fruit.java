package aeren.leep.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;

import aeren.leep.Assets;

public class Fruit extends Actor implements Disposable {
    private Sprite sprite;
    private Rectangle bounds;

    public Fruit() {
        sprite = new Sprite(Assets.getInstance().get("sprites/fruit.png", Texture.class));
        bounds = new Rectangle();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        sprite.setColor(getColor());
        sprite.setPosition(this.getX(), this.getY());
        this.bounds.set(this.getX() + 3, this.getY() + 3, 10, 11);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch, parentAlpha);

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

    @Override
    public void dispose() {

    }
}

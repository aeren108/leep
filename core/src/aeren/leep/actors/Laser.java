package aeren.leep.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aeren.leep.Assets;

public class Laser extends Actor {
    private LaserType type;

    private TextureRegion activeTile;
    private TextureRegion deactiveTile;
    private Rectangle bounds;

    private float deactiveDuration;
    private float activeDuration;
    private float activationTimer;

    private float elapsed;
    public boolean isActive;
    public boolean isAlive = true;

    public Laser(LaserType type, float activeDuration, float deactiveDuration) {
        this.type = type;
        this.activeDuration = activeDuration;
        this.deactiveDuration = deactiveDuration;

        bounds = type.getBounds(getX(), getY());

        TextureRegion[][] textures = TextureRegion.split(Assets.getInstance().get("sprites/laser.png", Texture.class), 16, 16);
        activeTile = textures[0][type.getAnimIndex()];
        deactiveTile = textures[1][type.getAnimIndex()];
    }

    @Override
    public void act(float delta) {
        elapsed += delta;
        activationTimer += delta;

        if (!isActive && activationTimer >= deactiveDuration) {
            isActive = true;
            activationTimer = 0;
        } else if (isActive && activationTimer >= activeDuration){
            isActive = false;
            isAlive = false;
            activationTimer = 0;
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isActive) {
            for (int i = -1; i < type.getTileRepeat() + 1; i++) {
                batch.draw(activeTile, getX() + type.getXOffset(i), getY() + type.getYOffset(i));
            }
        } else {
            for (int i = -1; i < type.getTileRepeat() + 1; i++) {
                batch.draw(deactiveTile, getX() + type.getXOffset(i), getY() + type.getYOffset(i));
            }
        }

        super.draw(batch, parentAlpha);
    }

    public void setLinage(int linage) {
        Vector2 pos = type.setLinage(linage);
        setPosition(pos.x, pos.y);
        bounds = type.getBounds(getX(), getY());
    }

    public void reset() {
        isActive = false;
        isAlive = true;
        activationTimer = 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public LaserType getType() {
        return type;
    }
}

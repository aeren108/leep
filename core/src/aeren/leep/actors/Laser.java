package aeren.leep.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aeren.leep.Assets;
import aeren.leep.Settings;

public class Laser extends Actor {
    private LaserType type;
    private Settings settings;
    private TextureRegion activeTile;
    private TextureRegion deactiveTile;

    private Rectangle bounds;

    private Sound buzz;
    private long id;

    private float deactiveDuration;
    private float activeDuration;
    private float activationTimer;

    private float elapsed;
    private boolean isActive;
    private boolean isAlive = true;

    public Laser(LaserType type, float activeDuration, float deactiveDuration) {
        this.activeDuration = activeDuration;
        this.deactiveDuration = deactiveDuration;

        settings = Settings.getInstance();
        buzz = Assets.getInstance().get("sfx/buzz.wav", Sound.class);

        setType(type);
    }

    @Override
    public void act(float delta) {
        elapsed += delta;
        activationTimer += delta;

        if (!isActive && activationTimer >= deactiveDuration) {
            isActive = true;
            activationTimer = 0;

            id = buzz.loop(settings.getVolume());
        } else if (isActive && activationTimer >= activeDuration){
            isActive = false;
            isAlive = false;
            activationTimer = 0;

            buzz.stop(id);
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

        elapsed = 0;
        activationTimer = 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public LaserType getType() {
        return type;
    }

    public void setType(LaserType type) {
        this.type = type;

        bounds = type.getBounds(getX(), getY());

        TextureRegion[][] textures = TextureRegion.split(Assets.getInstance().get("sprites/laser.png", Texture.class), 16, 16);
        activeTile = textures[0][type.getAnimIndex()];
        deactiveTile = textures[1][type.getAnimIndex()];
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
        buzz.stop(id);
    }
}

package aeren.leep.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import aeren.leep.Assets;
import aeren.leep.Utils;
import aeren.leep.character.Character;
import aeren.leep.character.CharacterManager;
import aeren.leep.input.SwipeListener;

public class Player extends Actor implements SwipeListener {
    private Rectangle bounds;

    private Texture spriteSheet;
    private Character character;
    private Animation<TextureRegion> idleLeft;
    private Animation<TextureRegion> idleRight;
    private Animation<TextureRegion> curAnim;
    private float elapsed = 0;

    private Sound swipe;

    public Player() {
        bounds = new Rectangle();

        swipe = Assets.getInstance().get("sfx/swipe.wav", Sound.class);
        spriteSheet = Assets.getInstance().get("sprites/characters.png", Texture.class);
        character = CharacterManager.getInstance().getCurrentCharacter();

        updateCharacter();
        setPosition(96, 48);

        bounds.width = idleLeft.getKeyFrames()[0].getRegionWidth();
        bounds.height = idleLeft.getKeyFrames()[0].getRegionHeight() - 4;
    }

    @Override
    public void act(float delta) {
        elapsed += delta;
        bounds.setPosition(this.getX(), this.getY());

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        batch.draw(curAnim.getKeyFrame(elapsed, true), getX(), getY());

        super.draw(batch, parentAlpha);
    }

    @Override
    public void onSwipe(Vector2 dir) {
        if (hasActions())
            return;

        dir.y = -dir.y;
        swipe.play();

        if (dir.y + dir.x < 0 && dir.y - dir.x < 0) {
            addAction(Actions.moveBy(0, -16, .03f));
        } else if (dir.y + dir.x > 0 && dir.y - dir.x > 0) {
            addAction(Actions.moveBy(0, 16, .03f));
        } else if (dir.y + dir.x < 0 && dir.y - dir.x > 0) {
            curAnim = idleLeft;
            addAction(Actions.moveBy(-16, 0, .03f));
        } else if (dir.y + dir.x > 0 && dir.y - dir.x < 0) {
            curAnim = idleRight;
            addAction(Actions.moveBy(16, 0, .03f));
        }
    }

    public void updateCharacter() {
        setCharacter(CharacterManager.getInstance().getCurrentCharacter());
    }

    public void setCharacter(Character c) {
        TextureRegion[][] frames = Utils.getFrames(spriteSheet, c.x, c.y, 1, 4, 16, 20);
        idleRight = new Animation<>(.15f, frames[0]);
        idleLeft = new Animation<>(.15f, Utils.flipFrames(frames[0], true, false));
        curAnim = idleLeft;
    }

    public Character getCharacter() {
        return character;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

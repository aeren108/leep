package aeren.leep.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import aeren.leep.Settings;

class VerticalFireball extends Fireball {

    public VerticalFireball() {
        type = FireballType.VERTICAL;

        anim = new Animation<>(.1f, frames[0]);

        xOffset = 4;
        yOffset = 4;
        bounds.setSize(8f, 9f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!alerted) {
            if (velocity.y < 0)
                batch.draw(alert, getX(), getY() - 16);
            else
                batch.draw(alert, getX(), getY() + 16);
        }

        if (alerted)
            batch.draw(anim.getKeyFrame(elapsed, true), getX(), getY());
    }

    @Override
    public void setLinage(int linage) {
        if (linage < 0 || linage > Settings.WIDTH / 16)
            throw new IndexOutOfBoundsException("Linage should be between 0-" + Settings.WIDTH / 16 + ".");

        if (velocity.y < 0)
            setPosition(linage * 16, Settings.HEIGHT);
        else
            setPosition(linage * 16, -16f);
    }

    @Override
    public void flip() {
        if (velocity.y < 0 || flipped)
            return;

        flipped = true;
        for (TextureRegion tr : anim.getKeyFrames())
            tr.flip(false, true);
    }
}

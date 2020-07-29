package aeren.leep.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import aeren.leep.Settings;

class HorizontalFireball extends Fireball {
  
  public HorizontalFireball() {
    type = FireballType.HORIZONTAL;
    anim = new Animation<>(.1f, frames[1]);
    
    xOffset = 3; yOffset = 4;
    bounds.setSize(9f, 8f);
  }
  
  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (!alerted) {
      if (velocity.x < 0)
        batch.draw(alert, getX() - 16, getY(), 8, 8, 16, 16, 1, 1, 90, 0, 0, 16, 16, false, false);
      else
        batch.draw(alert, getX() + 18, getY(), 8, 8, 16, 16, 1, 1, 270, 0, 0, 16, 16, false, false);
    }
    
    batch.draw(anim.getKeyFrame(elapsed, true), getX(), getY());
  }

  @Override
  public void setLinage(int linage) {
    if (linage < 0 || linage > Settings.HEIGHT / 16)
      throw new IndexOutOfBoundsException("Linage should be between 0-" + Settings.HEIGHT / 16 + ".");
    
    if (velocity.x < 0)
      setPosition(Settings.WIDTH, linage * 16);
    else
      setPosition(-16, linage * 16);
  }
  
  @Override
  public void flip() {
    if (velocity.x > 0 || flipped)
      return;
    
    flipped = true;
    for (TextureRegion tr : anim.getKeyFrames())
      tr.flip(true, false);
  }
}

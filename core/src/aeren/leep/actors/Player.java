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
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import aeren.leep.Assets;
import aeren.leep.input.SwipeListener;

public class Player extends Actor implements SwipeListener {
  private Rectangle bounds;
  
  private Animation<TextureRegion> idleLeft;
  private Animation<TextureRegion> idleRight;
  private Animation<TextureRegion> curAnim;
  private float elapsed = 0;
  
  private Sound swipe;
  
  public SequenceAction flicker;
  public MoveToAction respawn;
  
  public Player() {
    bounds = new Rectangle();
    swipe = Assets.manager.get(Assets.swipe);
    
    init();
  }
  
  private void init() {
    flicker = new SequenceAction(Actions.fadeOut(0.15f), Actions.fadeIn(0.15f));
    respawn = Actions.moveTo(16 * 5, 16 * 3, .2f);
  
    Texture sheet = Assets.manager.get(Assets.dinoIdle);
    TextureRegion[][] frames = TextureRegion.split(sheet, 16, 19);
    
    idleRight = new Animation<>(.15f, frames[0]);
    idleLeft = new Animation<>(.15f, frames[1]);
    
    curAnim = idleLeft;
    setPosition(16 * 5, 16 * 3);
  }

  @Override
  public void act(float delta) {
    elapsed += delta;

    bounds.set(this.getX(), this.getY(), idleLeft.getKeyFrames()[0].getRegionWidth(), idleLeft.getKeyFrames()[0].getRegionHeight() - 4);
    
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
    dir.y = -dir.y;
    
    if (hasActions())
      return;
    
    if (dir.y + dir.x < 0 && dir.y - dir.x < 0) {
      addAction(Actions.moveBy(0, -16, .025f));
    } else if (dir.y + dir.x > 0 && dir.y - dir.x > 0) {
      addAction(Actions.moveBy(0, 16, .025f));
    } else if (dir.y + dir.x < 0 && dir.y - dir.x > 0) {
      curAnim = idleLeft;
      addAction(Actions.moveBy(-16, 0, .025f));
    } else if (dir.y + dir.x > 0 && dir.y - dir.x < 0) {
      curAnim = idleRight;
      addAction(Actions.moveBy(16, 0, .025f));
    }
    
    swipe.play();
  }
  
  public Rectangle getBounds() {
    return bounds;
  }
}

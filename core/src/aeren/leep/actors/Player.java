package aeren.leep.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import aeren.leep.Assets;
import aeren.leep.input.SwipeListener;

public class Player extends Actor implements SwipeListener {
  private Sprite sprite;
  private Rectangle bounds;
  
  private Sound swipe;
  
  public SequenceAction flicker;
  public MoveToAction respawn;
  
  public Player() {
    sprite = new Sprite(Assets.manager.get(Assets.player));
    bounds = new Rectangle();
    
    swipe = Assets.manager.get(Assets.swipe);
    
    initActions();
    setPosition(16 * 5, 16 * 3);
  }
  
  private void initActions() {
    flicker = new SequenceAction(Actions.fadeOut(0.20f), Actions.fadeIn(0.20f));
    respawn = Actions.moveTo(16 * 5, 16 * 3, .25f);
  }

  @Override
  public void act(float delta) {
    sprite.setColor(getColor());
    sprite.setPosition(this.getX(), this.getY());
    bounds.set(this.getX(), this.getY(), sprite.getWidth(), sprite.getHeight());
    
    super.act(delta);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    sprite.draw(batch, parentAlpha);
    
    super.draw(batch, parentAlpha);
  }

  @Override
  public void onSwipe(Vector2 dir) {
    dir.y = -dir.y;
    
    if (hasActions())
      return;
    
    if (dir.y + dir.x < 0 && dir.y - dir.x < 0) {
      addAction(Actions.moveBy(0, -16, .05f));
    } else if (dir.y + dir.x > 0 && dir.y - dir.x > 0) {
      addAction(Actions.moveBy(0, 16, .05f));
    } else if (dir.y + dir.x < 0 && dir.y - dir.x > 0) {
      addAction(Actions.moveBy(-16, 0, .05f));
    } else if (dir.y + dir.x > 0 && dir.y - dir.x < 0) {
      addAction(Actions.moveBy(16, 0, .05f));
    }
    
    swipe.play();
  }
  
  public Rectangle getBounds() {
    return bounds;
  }
}

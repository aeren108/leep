package aeren.leep.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Level extends Group {
  private LevelData data;
  private Actor player;
  
  public Level(LevelData data) {
    this.data = data;
  }
  
  @Override
  public void act(float delta) {
    super.act(delta);
  }
  
  public LevelData getData() {
    return data;
  }
  
  /* TODO: generateFireballs()
  *  TODO: generateLasers()
  *  TODO: checkCollisions() */
  
  
}

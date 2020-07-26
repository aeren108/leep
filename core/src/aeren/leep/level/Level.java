package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.Random;

import aeren.leep.Assets;
import aeren.leep.actors.Fruit;
import aeren.leep.actors.Player;

public class Level extends Group {
  private LevelData data;
  
  private Player player;
  private Fruit fruit;
  
  private Sound pickup;
  
  private Random random;
  private boolean respawning = false;
  
  private int score = 0;
  
  public Level(LevelData data) {
    this.data = data;
    random = new Random();
    
    player = new Player();
    fruit = new Fruit();
    
    pickup = Assets.manager.get(Assets.pickup);
    
    addActor(player);
    addActor(fruit);
    
    placeFruit();
  }
  
  @Override
  public void act(float delta) {
    if (!respawning) {
      checkActorCollisions();
      checkTileCollisions();
    }
    
    super.act(delta);
  }
  
  private void checkActorCollisions() {
    if (fruit.getBounds().overlaps(player.getBounds())) {
      pickup.play(.8f);
      
      score++;
      placeFruit();
  
      Gdx.app.log("SCORE", "SCORE: " + score);
    }
  }
  
  private void checkTileCollisions() {
    TiledMapTileLayer layer = (TiledMapTileLayer) data.getMap().getLayers().get("ground");
    TiledMapTileLayer.Cell cell = layer.getCell((int)((player.getX() + 8) / 16), (int)((player.getY() + 8) / 16));
    if (cell == null)
      return;
    
    int tileId = cell.getTile().getId();
    
    for (int id : data.getDeadlyTiles()) {
      if (tileId == id) {
        respawning = true;
        
        player.clearActions();
        player.addAction(Actions.sequence(Actions.repeat(3, player.flicker), player.respawn, Actions.run(() -> {
          respawning = false;
          placeFruit();
        })));
        
        Gdx.app.log("SCORE", "GAME OVER, YOUR SCORE: " + score);
        score = 0;
      }
    }
  }
  
  private void placeFruit() {
    Vector2 pos = data.getAvailableCells().get(random.nextInt(data.getAvailableCells().size() - 1));
    
    fruit.setPosition(pos.x, pos.y);
    fruit.respawn();
  }
  
  public LevelData getData() {
    return data;
  }
  
  public Player getPlayer() {
    return player;
  }
  
  /* TODO: generateFireballs()
  *  TODO: generateLasers() */
  
  
}
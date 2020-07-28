package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aeren.leep.Assets;
import aeren.leep.actors.Fireball;
import aeren.leep.actors.FireballFactory;
import aeren.leep.actors.Fruit;
import aeren.leep.actors.Player;

public class Level extends Group {
  private LevelData data;
  
  private Player player;
  private Fruit fruit;
  
  private Sound pickup;
  
  private Random random;
  private boolean respawning = false;
  
  private List<Fireball> activeFireballs;
  private FireballFactory fireballFactory;
  private float fireballTimer = 0;
  
  private int score = 0;
  private int highscore = 0;
  
  private Preferences prefs;
  
  public Level(LevelData data) {
    this.data = data;
    random = new Random();
    
    player = new Player();
    fruit = new Fruit();
    
    pickup = Assets.manager.get(Assets.pickup);
  
    addActor(fruit);
    addActor(player);
    
    activeFireballs = new ArrayList<>();
    fireballFactory = new FireballFactory();
    
    placeFruit();
  
    prefs = Gdx.app.getPreferences("leep");
    highscore = prefs.getInteger("hs");
  }
  
  @Override
  public void act(float delta) {
    fireballTimer += delta;
    
    if (!respawning) {
      checkActorCollisions();
      checkTileCollisions();
      generateFireballs();
    }
    
    for (int i = 0; i < activeFireballs.size(); i++) {
      Fireball f = activeFireballs.get(i);
      
      if (!f.alive) {
        removeActor(f);
        activeFireballs.remove(f);
        fireballFactory.destroyFireball(f);
        
        i--;
      }
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
    
    for (int i = 0; i < activeFireballs.size(); i++) {
      Fireball f = activeFireballs.get(i);
      
      if (player.getBounds().overlaps(f.getBounds())) {
        activeFireballs.remove(f);
        removeActor(f);
        fireballFactory.destroyFireball(f);
        
        gameOver();
        i--;
      }
    }
  }
  
  private void checkTileCollisions() {
    TiledMapTileLayer layer = (TiledMapTileLayer) data.map.getLayers().get("ground");
    TiledMapTileLayer.Cell cell = layer.getCell((int)((player.getX() + 8) / 16), (int)((player.getY() + 8) / 16));
    if (cell == null)
      return;
    
    int tileId = cell.getTile().getId();
    
    for (int id : data.deadlyTiles) {
      if (tileId == id) {
        gameOver();
      }
    }
  }
  
  private void placeFruit() {
    Vector2 pos = data.availableCells.get(random.nextInt(data.availableCells.size() - 1));
    
    fruit.setPosition(pos.x, pos.y);
    fruit.respawn();
  }
  
  private void generateFireballs() {
    if (fireballTimer >= data.fireballCooldown) {
      
      for (int i = 0; i < 2; i++) {
        Fireball f = fireballFactory.createFireball(Fireball.FireballType.values()[i]);
        f.setVelocity((f.getType() == Fireball.FireballType.VERTICAL) ? Fireball.VEL_UP : Fireball.VEL_RIGHT);
        f.setLinage((f.getType() == Fireball.FireballType.VERTICAL) ? random.nextInt(9) : random.nextInt(16));
        f.setVelocity(f.getVelocity().cpy().scl(data.fireballSpeed));
        f.flip();
    
        activeFireballs.add(f);
        addActor(f);
      }
      
      fireballTimer = 0;
    }
  }
  
  private void gameOver() {
    respawning = true;
  
    player.clearActions();
    activeFireballs.clear();
    
    player.addAction(Actions.sequence(Actions.repeat(3, player.flicker), player.respawn, Actions.run(() -> {
      respawning = false;
      placeFruit();
    })));
  
    if (score > highscore)
      setHighscore(score);
    
    score = 0;
  }
  
  public LevelData getData() {
    return data;
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public int getScore() {
    return score;
  }
  
  public int getHighscore() {
    return highscore;
  }

  public void setHighscore(int highscore) {
    this.highscore = highscore;
    
    prefs.putInteger("hs", highscore);
    prefs.flush();
  }

/* TODO: generateLasers() */
  
  
}
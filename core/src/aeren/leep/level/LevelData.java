package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.InputStream;

public class LevelData {
  private String levelPath;
  
  private TiledMap map;
  private int[] deadlyTiles;
  
  public LevelData(String levelPath) {
    this.levelPath = levelPath;
    
    parseData();
  }
  
  private void parseData() {
    JsonReader reader = new JsonReader();
    JsonValue data = reader.parse(Gdx.files.internal(levelPath));
    
    map = new TmxMapLoader().load("levels/" + data.getString("tmxFile"));
    deadlyTiles = data.get("deadlyTiles").asIntArray();
  }
  
  public TiledMap getMap() {
    return map;
  }
}

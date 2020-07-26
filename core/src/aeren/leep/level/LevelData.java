package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelData {
  private String levelPath;
  
  private TiledMap map;
  private int[] deadlyTiles;
  private int[] availableTiles;
  private List<Vector2> availableCells;
  
  public LevelData(String levelPath) {
    this.levelPath = levelPath;
    availableCells = new ArrayList<>();
    
    parseData();
    findAvailableCells();
  }
  
  private void parseData() {
    JsonReader reader = new JsonReader();
    JsonValue data = reader.parse(Gdx.files.internal(levelPath));
    
    map = new TmxMapLoader().load("levels/" + data.getString("tmxFile"));
    deadlyTiles = data.get("deadlyTiles").asIntArray();
    availableTiles = data.get("availableTiles").asIntArray();
  }
  
  private void findAvailableCells() {
    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");
    
    for (int i = 0; i < layer.getWidth(); i++) {
      for (int j = 0; j < layer.getHeight(); j++) {
        for (int id : availableTiles) {
          TiledMapTileLayer.Cell cell = layer.getCell(i, j);
          if (cell == null)
            continue;
          
          if (cell.getTile().getId() == id)
            availableCells.add(new Vector2(i * 16, j * 16));
        }
      }
    }
  }
  
  public TiledMap getMap() {
    return map;
  }

  public int[] getDeadlyTiles() {
    return deadlyTiles;
  }
  
  public List<Vector2> getAvailableCells() {
    return availableCells;
  }
}

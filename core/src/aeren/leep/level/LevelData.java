package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelData {
  private String levelPath;
  
  public TiledMap map;
  public int[] deadlyTiles;
  private int[] availableTiles;
  public List<Vector2> availableCells;

  public float fireballCooldown;
  public float fireballSpeed;
  
  private LevelData() {
    availableCells = new ArrayList<>();
  }
  
  public static LevelData getLevelDataFromJson(String path) {
    LevelData data = new LevelData();
    
    JsonReader reader = new JsonReader();
    JsonValue json = reader.parse(Gdx.files.internal(path));
    JsonValue fireball = json.get("fireball");
    Gdx.app.log("DBG", fireball.toJson(JsonWriter.OutputType.json));
  
    data.map = new TmxMapLoader().load("levels/" + json.getString("tmxFile"));
    data.deadlyTiles = json.get("deadlyTiles").asIntArray();
    data.availableTiles = json.get("availableTiles").asIntArray();
    data.fireballCooldown = fireball.getFloat("cooldown");
    data.fireballSpeed = fireball.getFloat("speed");
    
    data.findAvailableCells();
    
    return data;
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
}

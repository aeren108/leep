package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.Assets;

public class LevelData {
    public TiledMap map;
    public int[] deadlyTiles;
    private int[] availableTiles;
    public List<Vector2> availableCells;

    public Music music;

    public float fireballCooldown;
    public float fireballSpeed, fireballMaxSpeed, fireballSpeedTemp;
    public float fireballAlert, fireballMinCooldown, fireballCooldownTemp;

    public float difficultyThreshold;

    public float fireballSpeedInc;
    public float fireballCooldownDec;

    private LevelData() {
        availableCells = new ArrayList<>();
    }

    public static LevelData getLevelDataFromJson(String path) {
        LevelData data = new LevelData();

        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(Gdx.files.internal(path));
        JsonValue fireball = json.get("fireball");

        data.map = new TmxMapLoader().load(json.getString("tmxFile"));
        data.music = Assets.manager.get(json.getString("music"));
        data.deadlyTiles = json.get("deadlyTiles").asIntArray();
        data.availableTiles = json.get("availableTiles").asIntArray();
        data.fireballCooldown = fireball.getFloat("cooldown");
        data.fireballSpeed = fireball.getFloat("speed");
        data.fireballMaxSpeed = fireball.getFloat("maxSpeed");
        data.fireballMinCooldown = fireball.getFloat("minCooldown");
        data.fireballAlert = fireball.getFloat("alert");
        data.difficultyThreshold = json.getFloat("difficultyThreshold");
        data.fireballSpeedInc = fireball.getFloat("speedInc");
        data.fireballCooldownDec = fireball.getFloat("cooldownDec");

        data.fireballSpeedTemp = data.fireballSpeed;
        data.fireballCooldownTemp = data.fireballCooldown;

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

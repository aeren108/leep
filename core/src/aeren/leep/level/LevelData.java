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
    public int[] availableTiles;
    public List<Vector2> availableCells;

    public Music music;

    public float fireballCooldown;
    public float fireballSpeed, fireballMaxSpeed, fireballSpeedTemp;
    public float fireballAlert, fireballMinCooldown, fireballCooldownTemp;

    public float difficultyThreshold;

    public float fireballSpeedInc;
    public float fireballCooldownDec;

    public float birthRate;
    public int birthThreshold, deathThreshold, maxStep;

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

        data.birthRate = json.getFloat("birthRate");
        data.birthThreshold = json.getInt("birthThreshold");
        data.deathThreshold = json.getInt("deathThreshold");
        data.maxStep = json.getInt("maxStep");

        return data;
    }


}

package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.Assets;
import aeren.leep.level.pattern.Pattern;

public class LevelData {
    public TiledMap map;
    public List<Pattern> patterns;
    public int[] deadlyTiles;
    public int[] availableTiles;
    public int[] autoTiles;

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
        patterns = new ArrayList<>();
    }

    public static LevelData getLevelDataFromJson(String path) {
        LevelData data = new LevelData();

        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(Gdx.files.internal(path));
        JsonValue fireball = json.get("fireball");

        data.map = new TmxMapLoader().load(json.getString("tmxFile"));
        data.music = Assets.getInstance().get(json.getString("music"), Music.class);
        data.deadlyTiles = json.get("deadlyTiles").asIntArray();
        data.availableTiles = json.get("availableTiles").asIntArray();
        data.autoTiles = json.get("autoTiles").asIntArray();
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

        data.patterns = loadPatterns(json.get("patterns"));

        return data;
    }

    private static List<Pattern> loadPatterns(JsonValue patternJson) {
        List<Pattern> patterns = new ArrayList<>();
        for (JsonValue jv : patternJson) {
            Pattern p = new Pattern();
            p.tileThreshold = jv.getInt("tileThreshold");
            p.max = jv.getInt("max");
            p.repeat = jv.getBoolean("repeat");

            JsonValue jControlPattern = jv.get("controlPattern");
            JsonValue jTargetPattern = jv.get("targetPattern");

            p.controlPattern = new int[jControlPattern.size][jControlPattern.get(0).asIntArray().length];
            p.targetPattern = new int[jTargetPattern.size][jTargetPattern.get(0).asIntArray().length];

            for (int i = 0; i < jControlPattern.size; i++)
                p.controlPattern[i] = jControlPattern.get(i).asIntArray();

            for (int i = 0; i < jTargetPattern.size; i++)
                p.targetPattern[i] = jTargetPattern.get(i).asIntArray();

            patterns.add(p);
        }

        return patterns;
    }


}

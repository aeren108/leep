package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public LevelData() {
        patterns = new ArrayList<>();
    }
}

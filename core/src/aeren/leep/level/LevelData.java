package aeren.leep.level;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.level.pattern.Pattern;

public class LevelData implements Disposable {
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


    @Override
    public void dispose() {
        map.dispose();
    }
}

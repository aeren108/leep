package aeren.leep.level;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.level.pattern.Pattern;

public class LevelData implements Disposable {
    public String levelName;
    public TiledMap map;
    public List<Pattern> patterns;
    public int[] deadlyTiles;
    public int[] availableTiles;
    public int[] autoTiles;

    public Music music;

    public float difficultyThreshold;

    public float fireballCooldown;
    public float fireballSpeed, fireballMaxSpeed, fireballSpeedTemp;
    public float fireballAlert, fireballMinCooldown, fireballCooldownTemp;

    public float fireballSpeedInc;
    public float fireballCooldownDec;

    public float laserActiveDuration, laserDeactiveDuration;
    public float laserCooldown, laserMinCooldown, laserCooldownTemp;
    public float laserCooldownDec;

    public float playerMovementDelay, playerMovementDelayDec, playerMinMovement;
    public float playerMovementDelayTemp;

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

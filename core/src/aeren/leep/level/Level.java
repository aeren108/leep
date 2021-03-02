package aeren.leep.level;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.Assets;
import aeren.leep.DataManager;
import aeren.leep.Settings;
import aeren.leep.Utils;
import aeren.leep.actors.Fireball;
import aeren.leep.actors.FireballPool;
import aeren.leep.actors.FireballType;
import aeren.leep.actors.Fruit;
import aeren.leep.actors.Laser;
import aeren.leep.actors.LaserType;
import aeren.leep.actors.Player;
import aeren.leep.character.CharacterManager;
import aeren.leep.states.GameState;
import aeren.leep.states.StateManager;
import aeren.leep.states.fragments.GameOverFragment;

public class Level extends Group implements Disposable {
    private Settings settings;
    private GameState state;

    private Assets assets;
    private CharacterManager charManager;
    private DataManager dm;

    private LevelData data;
    private MapGenerator generator;
    private Rectangle mapBounds;
    private List<Vector2> availableCells;

    private Player player;
    private Fruit fruit;

    private Sound pickup;
    private Sound hurt;
    private Sound shock;
    private Sound fall;

    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean isPlacingPlayer = false;

    private List<Fireball> activeFireballs;
    private FireballPool fireballPool;
    private float fireballTimer = 0;

    private Laser laser;
    private float laserTimer = 0;
    private int laserTypeIndex = 0;

    private float difficultyTimer = 0;

    private int score = 0;
    private int best;
    private boolean isNewBest = false;

    private ScoreListener scoreListener;

    public Level(LevelData data) {
        this.data = data;
        generator = new MapGenerator(data);
        availableCells = new ArrayList<>();

        player = new Player();
        fruit = new Fruit();

        settings = Settings.getInstance();
        assets = Assets.getInstance();
        charManager = CharacterManager.getInstance();
        dm = DataManager.getInstance();
        charManager.setCharacterListener(player);

        pickup = assets.get("sfx/pickup.wav", Sound.class);
        hurt = assets.get("sfx/hurt.wav", Sound.class);
        shock = assets.get("sfx/shock.wav", Sound.class);
        fall = assets.getInstance().get("sfx/fall.wav", Sound.class);
        best = dm.getHighscore(data.levelName);

        addActor(fruit);
        addActor(player);

        activeFireballs = new ArrayList<>();
        fireballPool = new FireballPool();

        laser = new Laser(LaserType.VERTICAL, data.laserActiveDuration, data.laserDeactiveDuration);

        data.music.setLooping(true);
        data.music.setVolume(.25f * settings.getVolume());
        data.music.play();
        player.movementDelay = data.playerMovementDelay;

        state = (GameState) StateManager.getInstance().getState();

        generator.generateTiledMap();
        findAvailableCells();
        computeMapBoundaries();
        placeFruit();
        placePlayer();
    }

    @Override
    public void act(float delta) {
        if (isPaused) return;

        if (!isGameOver && !isPlacingPlayer) {
            fireballTimer += delta;
            laserTimer += delta;
            difficultyTimer += delta;

            checkActorCollisions();
            checkTileCollisions();
            generateFireballs();
            generateLasers();
            handleDifficulty();
        }

        destroyFireballs();
        destroyLasers();

        super.act(delta);
    }

    private void checkActorCollisions() {
        for (Actor a : getChildren()) {
            if (a instanceof Fruit) {
                Fruit f = (Fruit) a;

                if (f.getBounds().overlaps(player.getBounds())) {
                    if (score > best) isNewBest = true;

                    pickup.play(.35f * settings.getVolume());
                    placeFruit();
                    setScore(++score);
                }
            } else if (a instanceof Fireball) {
                Fireball f = (Fireball) a;

                if (player.getBounds().overlaps(f.getBounds())) {
                    f.isAlive = false;

                    hurt.play(.5f * settings.getVolume());
                    gameOver();

                    break;
                }
            } else if (a instanceof Laser) {
                Laser l = (Laser) a;

                if (l.isActive() && player.getBounds().overlaps(l.getBounds())) {
                    l.setAlive(false);

                    shock.play(settings.getVolume());
                    gameOver();

                    break;
                }
            }
        }
    }

    private void checkTileCollisions() {
        TiledMapTileLayer layer = (TiledMapTileLayer) data.map.getLayers().get("ground");
        TiledMapTileLayer.Cell cell = layer.getCell((int) ((player.getX() + 8) / 16), (int) ((player.getY() + 8) / 16));

        if (cell == null) return;
        int tileId = cell.getTile().getId();

        for (int id : data.deadlyTiles) {
            if (tileId == id) {
                fall.play(settings.getVolume());
                gameOver();

                return;
            }
        }
    }

    private void placeFruit() {
        List<Vector2> tiles = Utils.removeAdjacentTiles(availableCells, (int) fruit.getX() / 16, (int) fruit.getY() / 16, 2);
        Vector2 pos = tiles.get((int) (Math.random() * tiles.size()));

        if ((pos.x == player.getX() && pos.y == player.getY())) {
            placeFruit();
            return;
        }

        fruit.setPosition(pos.x * 16, pos.y * 16);
        fruit.respawn();
    }

    private void placePlayer() {
        isPlacingPlayer = true;

        Vector2 playerPos = availableCells.get((int) (Math.random() * availableCells.size()));
        player.addAction(Actions.sequence(Actions.moveTo(playerPos.x * 16, playerPos.y * 16, 0.2f), Actions.run(() -> isPlacingPlayer = false)));
    }

    private void generateFireballs() {
        if (fireballTimer >= data.fireballCooldownTemp) {
            for (int i = 0; i < 2; i++) {
                Fireball f = fireballPool.obtain(FireballType.values()[i]);
                f.setLinage((f.getType() == FireballType.VERTICAL) ? (int)(mapBounds.x + Math.random() * (mapBounds.width - mapBounds.x)) :
                            (int)(mapBounds.y + Math.random() * (mapBounds.height - mapBounds.y)));
                f.setVelocity(f.getVelocity().cpy().scl(data.fireballSpeedTemp));
                f.setAlertThreshold(data.fireballAlert);

                activeFireballs.add(f);
                addActor(f);
            }

            fireballTimer = 0;
        }
    }

    private void destroyFireballs() {
        for (int i = 0; i < activeFireballs.size(); i++) {
            Fireball f = activeFireballs.get(i);

            if (!f.isAlive) {
                removeActor(f);
                activeFireballs.remove(f);
                fireballPool.free(f);

                i--;
            }
        }
    }

    private void generateLasers() {
        if (laserTimer >= data.laserCooldownTemp) {
            laser.setType(LaserType.values()[laserTypeIndex & 1]);
            laser.setLinage(laser.getType() == LaserType.VERTICAL ? (int)(mapBounds.x + Math.random() * (mapBounds.width - mapBounds.x)) :
                    (int)(mapBounds.y + Math.random() * (mapBounds.height - mapBounds.y)));

            addActor(laser);

            laserTimer = 0;
            laserTypeIndex++;
        }
    }

    private void destroyLasers() {
        if (!laser.isAlive()) {
            removeActor(laser);
            laser.reset();
        }
    }

    private void handleDifficulty() {
        if (difficultyTimer >= data.difficultyThreshold) {
            if (data.fireballSpeedTemp < data.fireballMaxSpeed)
                data.fireballSpeedTemp += data.fireballSpeedInc;
            if (data.fireballCooldownTemp > data.fireballMinCooldown)
                data.fireballCooldownTemp -= data.fireballCooldownDec;

            if (data.laserCooldownTemp > data.laserMinCooldown)
                data.laserCooldownTemp -= data.laserCooldownDec;

            if (player.movementDelay > data.playerMinMovement)
                player.movementDelay -= data.playerMovementDelayDec;

            difficultyTimer = 0;
        }
    }

    public void findAvailableCells() {
        int[][] rawMap = generator.getRawMap();
        availableCells.clear();

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 9; x++) {
                if (rawMap[15 - y][x] == 1)
                    availableCells.add(new Vector2(x, y));
            }
        }
    }

    private void gameOver() {
        isGameOver = true;

        dm.increaseData("totalFruitsCollected", score);
        dm.increaseData("totalGamesPlayed", 1);

        if (score > best)
            setBest(score);

        data.music.setVolume(0.15f * settings.getVolume());

        player.addAction(Actions.sequence( //flicker the player and show game over fragment
            Actions.repeat(2, Actions.sequence(Actions.fadeOut(0.15f), Actions.fadeIn(0.15f))),
            Actions.run(() -> state.pushFragment(new GameOverFragment(state, score, best, charManager.checkUnlockConditions()))))
        );
    }

    private void computeMapBoundaries() {
        int[][] rawMap = generator.getRawMap();

        int minY = 15, maxY = 0;
        int minX = 8, maxX = 0;
        for (int y = 0; y < 16; y++) {
            int[] row = rawMap[y];
            boolean containsTile = false;

            for (int value : row) {
                if (value == 1) {
                    containsTile = true;
                    break;
                }
            }

            if (containsTile) {
                int curY = 16 - y;

                if (curY < minY) minY = curY;
                if (curY > maxY) maxY = curY;
            }
        }

        for (int x = 0; x < 9; x++) {
            boolean containsTile = false;
            for (int y = 0; y < 16; y++) {
                if (rawMap[y][x] == 1) {
                    containsTile = true;
                    break;
                }
            }

            if (containsTile) {
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
            }
        }

        mapBounds = new Rectangle(minX, minY, maxX, maxY);
    }

    public void reset() {
        generator.generateTiledMap();
        findAvailableCells();

        player.clearActions();
        activeFireballs.clear();
        data.music.setVolume(.25f * settings.getVolume());

        data.fireballSpeedTemp = data.fireballSpeed;
        data.fireballCooldownTemp = data.fireballCooldown;
        data.laserCooldownTemp = data.laserCooldown;

        fireballTimer = 0;
        laserTimer = 0;
        difficultyTimer = 0;

        placePlayer();
        placeFruit();

        isNewBest = false;
        isGameOver = false;
        isPaused = false;

        setScore(0);
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    @Override
    public void dispose() {
        data.music.stop();
    }

    public void setScoreListener(ScoreListener scoreListener) {
        this.scoreListener = scoreListener;
    }

    public LevelData getData() {
        return data;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isNewBest() {
        return isNewBest;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;

        if (scoreListener != null)
            scoreListener.onScoreChanged(score, isNewBest);
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
        dm.setHighscore(data.levelName, best);
    }
}
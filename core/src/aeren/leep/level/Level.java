package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import aeren.leep.actors.Fireball;
import aeren.leep.actors.FireballFactory;
import aeren.leep.actors.Fruit;
import aeren.leep.actors.Player;
import aeren.leep.character.Character;
import aeren.leep.character.CharacterManager;
import aeren.leep.states.GameState;
import aeren.leep.states.StateManager;
import aeren.leep.states.fragments.GameOverFragment;

public class Level extends Group implements Disposable {
    private GameState state;
    private Assets assets;
    private CharacterManager charManager;

    private LevelData data;
    private MapGenerator generator;
    private Rectangle mapBounds;
    private List<Vector2> availableCells;

    private Player player;
    private Fruit fruit;

    private Sound pickup;
    private Sound hurt;
    private Sound fall;

    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean isPlacingPlayer = false;

    private List<Fireball> activeFireballs;
    private FireballFactory fireballFactory;
    private float fireballTimer = 0;
    private float difficultyTimer = 0;

    private int score = 0;
    private int highscore;
    private boolean isNewRecord = false;

    private Preferences prefs;

    public Level(LevelData data) {
        this.data = data;
        generator = new MapGenerator(data);
        availableCells = new ArrayList<>();

        player = new Player();
        fruit = new Fruit();

        assets = Assets.getInstance();
        charManager = CharacterManager.getInstance();

        pickup = assets.get("sfx/pickup.wav", Sound.class);
        hurt = assets.get("sfx/hurt.wav", Sound.class);
        fall = assets.get("sfx/fall.wav", Sound.class);

        addActor(fruit);
        addActor(player);

        activeFireballs = new ArrayList<>();
        fireballFactory = new FireballFactory();

        prefs = Gdx.app.getPreferences("leep");
        highscore = prefs.getInteger("hs");

        data.music.setLooping(true);
        data.music.setVolume(.3f);
        data.music.play();

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

        fireballTimer += delta;
        difficultyTimer += delta;

        if (!isGameOver && !isPlacingPlayer) {
            checkActorCollisions();
            checkTileCollisions();
            generateFireballs();
            handleDifficulty();
        }

        destroyFireballs();

        super.act(delta);
    }

    private void checkActorCollisions() {
        for (Actor a : getChildren()) {
            if (a instanceof Fruit) {
                Fruit f = (Fruit) a;

                if (f.getBounds().overlaps(player.getBounds())) {
                    score++;
                    pickup.play(.35f);
                    placeFruit();

                    if (score > highscore) isNewRecord = true;
                }
            } else if (a instanceof Fireball) {
                Fireball f = (Fireball) a;

                if (player.getBounds().overlaps(f.getBounds())) {
                    removeActor(f);
                    activeFireballs.remove(f);
                    fireballFactory.destroyFireball(f);

                    hurt.play(.5f);
                    gameOver();

                    return;
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
                fall.play();
                gameOver();

                return;
            }
        }
    }

    private void placeFruit() {
        Vector2 pos = availableCells.get((int) (Math.random() * availableCells.size()));

        if ((pos.x == player.getX() && pos.y == player.getY()) || (pos.x == fruit.getX() && pos.y == fruit.getY())) {
            placeFruit();
            return;
        }

        fruit.setPosition(pos.x, pos.y);
        fruit.respawn();
    }

    private void placePlayer() {
        isPlacingPlayer = true;

        Vector2 playerPos = availableCells.get((int) (Math.random() * availableCells.size()));
        player.addAction(Actions.sequence(Actions.moveTo(playerPos.x, playerPos.y, 0.2f), Actions.run(() -> isPlacingPlayer = false)));
    }

    private void generateFireballs() {
        if (fireballTimer >= data.fireballCooldownTemp) {
            for (int i = 0; i < 2; i++) {
                Fireball f = fireballFactory.createFireball(Fireball.FireballType.values()[i]);
                f.setVelocity((f.getType() == Fireball.FireballType.VERTICAL) ? Fireball.VEL_DOWN : Fireball.VEL_RIGHT);
                f.setLinage((f.getType() == Fireball.FireballType.VERTICAL) ? (int)(mapBounds.x + Math.random() * (mapBounds.width - mapBounds.x)) : (int)(mapBounds.y + Math.random() * (mapBounds.height - mapBounds.y)));
                f.setVelocity(f.getVelocity().cpy().scl(data.fireballSpeedTemp));
                f.setAlertThreshold(data.fireballAlert);
                f.flip();

                activeFireballs.add(f);
                addActor(f);
            }

            fireballTimer = 0;
        }
    }

    private void destroyFireballs() {
        for (int i = 0; i < activeFireballs.size(); i++) {
            Fireball f = activeFireballs.get(i);

            if (!f.alive) {
                removeActor(f);
                activeFireballs.remove(f);
                fireballFactory.destroyFireball(f);

                i--;
            }
        }
    }

    private void handleDifficulty() {
        if (difficultyTimer >= data.difficultyThreshold) {
            if (data.fireballSpeedTemp < data.fireballMaxSpeed)
                data.fireballSpeedTemp += data.fireballSpeedInc;
            if (data.fireballCooldownTemp > data.fireballMinCooldown)
                data.fireballCooldownTemp -= data.fireballCooldownDec;

            difficultyTimer = 0;
        }
    }

    public void findAvailableCells() {
        TiledMapTileLayer layer = (TiledMapTileLayer) data.map.getLayers().get("ground");
        availableCells.clear();

        for (int i = 0; i < layer.getWidth(); i++) {
            for (int j = 0; j < layer.getHeight(); j++) {
                for (int id : data.availableTiles) {
                    TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                    if (cell == null)
                        continue;

                    if (cell.getTile().getId() == id)
                        availableCells.add(new Vector2(i * 16, j * 16));
                }
            }
        }
    }

    private void gameOver() {
        isGameOver = true;
        player.addAction(Actions.sequence( //flicker the player and show game over fragment
            Actions.repeat(2, Actions.sequence(Actions.fadeOut(0.15f), Actions.fadeIn(0.15f))),
            Actions.run(() -> state.pushFragment(new GameOverFragment(state, score))))
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
        data.map = generator.generateTiledMap();
        findAvailableCells();

        player.clearActions();
        activeFireballs.clear();
        data.music.setVolume(.15f);

        data.fireballSpeedTemp = data.fireballSpeed;
        data.fireballCooldownTemp = data.fireballCooldown;

        charManager.setCurrentCharacter(charManager.getCharacter("ottoman-pirate"));
        player.updateCharacter();

        placePlayer();
        placeFruit();

        if (score > highscore)
            setHighscore(score);

        score = 0;
        isNewRecord = false;
        isGameOver = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    @Override
    public void dispose() {
        pickup.dispose();
        fall.dispose();
        hurt.dispose();
        data.music.dispose();

        player.dispose();
        fruit.dispose();
    }

    public LevelData getData() {
        return data;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getScore() {
        return score;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;

        prefs.putInteger("hs", highscore);
        prefs.flush();
    }
}
package aeren.leep.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aeren.leep.Assets;
import aeren.leep.actors.Fireball;
import aeren.leep.actors.FireballFactory;
import aeren.leep.actors.Fruit;
import aeren.leep.actors.Player;
import aeren.leep.states.GameState;
import aeren.leep.states.StateManager;
import aeren.leep.states.fragments.GameOverFragment;

public class Level extends Group {
    private GameState state;
    private LevelData data;
    private MapGenerator generator;
    private List<Vector2> availableCells;

    private Player player;
    private Fruit fruit;

    private Sound pickup;
    private Sound hurt;
    private Sound fall;

    private Random random;
    private boolean respawning = false;

    private List<Fireball> activeFireballs;
    private FireballFactory fireballFactory;
    private float fireballTimer = 0;
    private float difficultyTimer = 0;

    private int score = 0;
    private int highscore = 0;
    private boolean isNewRecord = false;

    private boolean pause = false;

    private Preferences prefs;

    public Level(LevelData data) {
        this.data = data;
        random = new Random();
        generator = new MapGenerator(data);

        player = new Player();
        fruit = new Fruit();

        pickup = Assets.manager.get(Assets.pickup);
        hurt = Assets.manager.get(Assets.hurt);
        fall = Assets.manager.get(Assets.fall);

        addActor(fruit);
        addActor(player);

        activeFireballs = new ArrayList<>();
        fireballFactory = new FireballFactory();

        placeFruit();

        prefs = Gdx.app.getPreferences("leep");
        highscore = prefs.getInteger("hs");

        data.music.setLooping(true);
        data.music.setVolume(.3f);
        data.music.play();

        state = (GameState) StateManager.getInstance().getState(GameState.class);
        generator.generateTiledMap();
        findAvailableCells();
    }

    @Override
    public void act(float delta) {
        fireballTimer += delta;
        difficultyTimer += delta;

        if (!respawning && !pause) {
            checkActorCollisions();
            checkTileCollisions();
            generateFireballs();
            handleDifficulty();
        }

        for (int i = 0; i < activeFireballs.size(); i++) {
            Fireball f = activeFireballs.get(i);

            if (!f.alive) {
                removeActor(f);
                activeFireballs.remove(f);
                fireballFactory.destroyFireball(f);

                i--;
            }
        }

        super.act(delta);
    }

    private void checkActorCollisions() {
        if (fruit.getBounds().overlaps(player.getBounds())) {
            pickup.play(.35f);

            score++;
            placeFruit();

            if (score > highscore)
                isNewRecord = true;
        }

        for (int i = 0; i < activeFireballs.size(); i++) {
            Fireball f = activeFireballs.get(i);

            if (player.getBounds().overlaps(f.getBounds())) {
                activeFireballs.remove(f);
                removeActor(f);
                fireballFactory.destroyFireball(f);

                hurt.play(.5f);

                state.addFragment(new GameOverFragment(state, score));
                pause = true;

                break;
            }
        }
    }

    private void checkTileCollisions() {
        TiledMapTileLayer layer = (TiledMapTileLayer) data.map.getLayers().get("ground");
        TiledMapTileLayer.Cell cell = layer.getCell((int) ((player.getX() + 8) / 16), (int) ((player.getY() + 8) / 16));
        if (cell == null)
            return;

        int tileId = cell.getTile().getId();

        for (int id : data.deadlyTiles) {
            if (tileId == id) {
                fall.play();

                state.addFragment(new GameOverFragment(state, score));
                pause = true;

                break;
            }
        }
    }

    private void placeFruit() {
        Vector2 pos = data.availableCells.get(random.nextInt(data.availableCells.size() - 1));

        if ((pos.x == player.getX() && pos.y == player.getY()) || (pos.x == fruit.getX() && pos.y == fruit.getY())) {
            placeFruit();
            return;
        }

        fruit.setPosition(pos.x, pos.y);
        fruit.respawn();
    }

    private void generateFireballs() {
        if (fireballTimer >= data.fireballCooldownTemp) {

            for (int i = 0; i < 2; i++) {
                Fireball f = fireballFactory.createFireball(Fireball.FireballType.values()[i]);
                f.setVelocity((f.getType() == Fireball.FireballType.VERTICAL) ? Fireball.VEL_DOWN : Fireball.VEL_RIGHT);
                f.setLinage((f.getType() == Fireball.FireballType.VERTICAL) ? random.nextInt(9) : random.nextInt(16));
                f.setVelocity(f.getVelocity().cpy().scl(data.fireballSpeedTemp));
                f.setAlertThreshold(data.fireballAlert);
                f.flip();

                activeFireballs.add(f);
                addActor(f);
            }

            fireballTimer = 0;
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

    public void reset() {
        respawning = true;
        pause = false;

        data.map = generator.generateTiledMap();
        findAvailableCells();

        player.clearActions();
        activeFireballs.clear();
        data.music.setVolume(.15f);

        data.fireballSpeedTemp = data.fireballSpeed;
        data.fireballCooldownTemp = data.fireballCooldown;

        player.addAction(Actions.sequence(Actions.repeat(3, player.flicker), player.respawn, Actions.run(() -> {
            placeFruit();

            respawning = false;
            data.music.setVolume(.3f);
        })));

        if (score > highscore)
            setHighscore(score);

        score = 0;
        isNewRecord = false;
    }

    public void dispose() {
        pickup.stop();
        fall.stop();
        hurt.stop();
        data.music.stop();
    }

    public LevelData getData() {
        return data;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;

        prefs.putInteger("hs", highscore);
        prefs.flush();
    }

    /* TODO: generateLasers() */

}
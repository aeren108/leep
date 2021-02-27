package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.DataManager;
import aeren.leep.Constants;
import aeren.leep.Utils;
import aeren.leep.character.CharacterManager;
import aeren.leep.character.Character;
import aeren.leep.ui.CharacterSelector;

public class CharacterState extends State {
    private Assets assets;
    private CharacterManager cm;

    private Skin skin;
    private Sound blipNext;
    private Sound blipPrev;

    private Stage background;
    private Color batchColor;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int[] layers;

    private CharacterSelector selector;
    private TextButton select;
    private ImageButton next;
    private ImageButton prev;

    private Animation<TextureRegion> anim;
    private float elapsed = 0;

    public CharacterState() {
        super(new ExtendViewport(Constants.UI_WIDTH, Constants.UI_HEIGHT));
        background = new Stage(new ExtendViewport(Constants.WIDTH, Constants.HEIGHT));
    }

    @Override
    public void show() {
        assets = Assets.getInstance();
        cm = CharacterManager.getInstance();

        skin = assets.get("ui/ui-skin.json", Skin.class);
        map = assets.get("ui/menu-maps.tmx", TiledMap.class);
        blipNext = assets.get("sfx/blip_next.wav", Sound.class);
        blipPrev = assets.get("sfx/blip_prev.wav", Sound.class);

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        layers = new int[] {map.getLayers().getIndex("character")};

        selector = new CharacterSelector(skin, cm.getCharacters());
        selector.setPosition(-selector.getPrefWidth() / 2, Constants.UI_HEIGHT / 2 - selector.getHeight() - 64);

        select = new TextButton("SELECT", skin);
        select.setSize(296, 90);
        select.setPosition(-select.getWidth() / 2, -Constants.UI_HEIGHT / 2 + 64);
        select.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                cm.setCurrentCharacter(selector.getCharacter());
                StateManager.getInstance().setState(StateEnum.MAIN_MENU);
            }
            return true;
        });

        next = new ImageButton(skin, "next");
        next.setPosition(select.getX() + select.getWidth() + 32, -Constants.UI_HEIGHT / 2  + 64);
        next.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                selector.next();
                updateCharacter(selector.getCharacter());
                blipNext.play(0.5f);
            }
            return true;
        });

        prev = new ImageButton(skin, "prev");
        prev.setPosition(select.getX() - next.getWidth() - 32, -Constants.UI_HEIGHT / 2 + 64);
        prev.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                selector.prev();
                updateCharacter(selector.getCharacter());
                blipPrev.play(0.5f);
            }
            return true;
        });

        addActor(selector);
        addActor(select);
        addActor(prev);
        addActor(next);

        background.getCamera().position.set(Constants.WIDTH / 2, Constants.HEIGHT / 2, 0);
        updateCharacter(selector.getCharacter());

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(100f / 255f, 173f / 255f, 234f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        elapsed += delta;

        background.getViewport().apply();
        mapRenderer.setView((OrthographicCamera) background.getCamera());
        mapRenderer.render(layers);

        background.getBatch().setProjectionMatrix(background.getCamera().combined);
        background.getBatch().setColor(batchColor);
        background.getBatch().begin();
        background.getBatch().draw(getFrame(), 4 * 16, 8 * 16);
        background.getBatch().end();

        getViewport().apply();
        super.render(delta);
    }

    private void updateCharacter(Character c) {
        TextureRegion[] frames = Utils.getFrames(assets.get("sprites/characters.png", Texture.class), c.x, c.y, 1, 4, 16, 20)[0];
        anim = new Animation<>(.15f, frames);

        select.setDisabled(!selector.getCharacter().unlocked);
        batchColor = selector.getCharacter().unlocked ? Color.WHITE : Color.BLACK;
    }

    private TextureRegion getFrame() {
        return selector.getCharacter().unlocked ? anim.getKeyFrame(elapsed, true) : anim.getKeyFrames()[0];
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        background.getViewport().update(width, height);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        DataManager.getInstance().flush();
        background.dispose();
        super.dispose();
    }
}

package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.Settings;
import aeren.leep.Utils;
import aeren.leep.character.Character;
import aeren.leep.character.CharacterManager;

public class MenuState extends State {
    private CharacterManager charManager;
    private Assets assets;
    private Skin skin;

    private Stage background;

    private Table table;
    private Label title;
    private TextButton play;
    private TextButton stats;
    private TextButton settings;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Animation<TextureRegion> charAnim;
    private Animation<TextureRegion> fruitAnim;

    private float elapsed = 0;

    public MenuState() {
        super(new ExtendViewport(Settings.WIDTH * 4, Settings.HEIGHT * 4));
        background = new Stage(new ExtendViewport(Settings.WIDTH, Settings.HEIGHT));
        assets = Assets.getInstance();
    }

    @Override
    public void show() {
        assets.finishLoading();
        charManager = CharacterManager.getInstance();

        Character curr = charManager.getCurrentCharacter();
        TextureRegion[] charFrames = Utils.getFrames(assets.get("sprites/characters.png", Texture.class), curr.x, curr.y, 1, 4, 16, 20)[0];
        TextureRegion[] fruitFrames = Utils.getFrames(assets.get("sprites/fruit.png", Texture.class), 0, 0, 1, 4, 16, 16)[0];

        skin = assets.get("ui/ui-skin.json", Skin.class);
        map = new TmxMapLoader().load("ui/main-menu.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        charAnim = new Animation<>(.15f, charFrames);
        fruitAnim = new Animation<>(.15f, fruitFrames);

        table = new Table();
        title = new Label("[GOLD]LEEP", skin, "title");
        play = new TextButton("PLAY", skin);
        stats = new TextButton("STATS", skin);
        settings = new TextButton("SETTINGS", skin);

        play.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent)
                StateManager.getInstance().pushState(StateEnum.GAME_STATE);
            return false;
        });

        table.padTop(-288);
        table.add(title).row();
        table.add(play).minWidth(216).spaceTop(32).row();
        table.add(stats).minWidth(216).spaceTop(8).row();
        table.add(settings).minWidth(216).spaceTop(8);
        table.align(Align.center);
        addActor(table);

        background.getCamera().position.set(Settings.WIDTH / 2, Settings.HEIGHT / 2, 0);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(100f / 255f, 173f / 255f, 234f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        elapsed += delta;

        background.getViewport().apply();
        mapRenderer.setView((OrthographicCamera) background.getCamera());
        mapRenderer.render();

        background.getBatch().setProjectionMatrix(background.getCamera().combined);
        background.getBatch().begin();
        background.getBatch().draw(fruitAnim.getKeyFrame(elapsed, true), 32, 48);
        background.getBatch().draw(charAnim.getKeyFrame(elapsed, true), 96, 48);
        background.getBatch().end();

        getViewport().apply();
        super.render(delta);
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
        background.getViewport().update(width, height, false);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        background.dispose();
        mapRenderer.dispose();
        map.dispose();

        super.dispose();
    }
}

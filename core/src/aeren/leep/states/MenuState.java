package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.DataManager;
import aeren.leep.Constants;
import aeren.leep.Settings;
import aeren.leep.Utils;
import aeren.leep.character.Character;
import aeren.leep.character.CharacterManager;

public class MenuState extends State {
    private Settings settings;
    private CharacterManager charManager;
    private DataManager dm;
    private Assets assets;
    private Skin skin;

    private Stage background;

    private Table table;
    private Label title;
    private ImageButton play;
    private ImageButton chars;
    private ImageButton mute;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Animation<TextureRegion> charAnim;
    private Animation<TextureRegion> fruitAnim;

    private float elapsed = 0;
    private int[] layers;

    public MenuState() {
        super(new ExtendViewport(Constants.UI_WIDTH, Constants.UI_HEIGHT));
        background = new Stage(new ExtendViewport(Constants.WIDTH, Constants.HEIGHT));
    }

    @Override
    public void show() {
        settings = Settings.getInstance();
        assets = Assets.getInstance();
        dm = DataManager.getInstance();
        charManager = CharacterManager.getInstance();

        Character curr = charManager.getCurrentCharacter();
        TextureRegion[] charFrames = Utils.getFrames(assets.get("sprites/characters.png", Texture.class), curr.x, curr.y, 1, 4, 16, 20)[0];
        TextureRegion[] fruitFrames = Utils.getFrames(assets.get("sprites/fruit.png", Texture.class), 0, 0, 1, 4, 16, 16)[0];

        skin = assets.get("ui/ui-skin.json", Skin.class);
        map = assets.get("ui/menu-maps.tmx", TiledMap.class);

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        charAnim = new Animation<>(.15f, charFrames);
        fruitAnim = new Animation<>(.15f, fruitFrames);

        table = new Table();
        title = new Label("LEEP", new Label.LabelStyle(assets.get("fonts/inkythin.fnt", BitmapFont.class), Color.WHITE));
        play = new ImageButton(skin, "play");
        chars = new ImageButton(skin, "charselect");
        mute = new ImageButton(skin, (settings.getVolume() == 1) ? "volume-on" : "volume-off");

        layers = new int[] {map.getLayers().getIndex("main-menu")};

        play.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent)
                StateManager.getInstance().pushState(StateEnum.GAME_STATE);
            return false;
        });

        chars.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent)
                StateManager.getInstance().pushState(StateEnum.CHARACTER_STATE);
            return false;
        });

        mute.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                settings.setVolume((settings.getVolume() == 1) ? 0 : 1);
                mute.setStyle(skin.get((settings.getVolume() == 1) ? "volume-on" : "volume-off", ImageButton.ImageButtonStyle.class));
            }
            return false;
        });

        Table col = new Table();
        col.add(chars).minWidth(184).minHeight(104).spaceRight(16);
        col.add(mute).minWidth(104).minHeight(104);

        table.align(Align.center).padTop(-256);
        table.add(title).padBottom(48).padLeft(8).row();
        table.add(play).minWidth(304).minHeight(104).spaceTop(8).row();
        table.add(col).spaceTop(16);

        addActor(table);

        background.getCamera().position.set(Constants.WIDTH / 2, Constants.HEIGHT / 2, 0);
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

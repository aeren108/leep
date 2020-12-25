package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.Settings;

public class MenuState extends State {
    private Skin skin;

    private Table table;
    private Label title;
    private TextButton play;
    private TextButton stats;
    private TextButton settings;

    public MenuState() {
        super(new ExtendViewport(Settings.WIDTH * 4, Settings.HEIGHT * 4));
    }

    @Override
    public void show() {
        skin = Assets.getInstance().get("ui/ui-skin.json", Skin.class);

        table = new Table();
        title = new Label("[#FFA64D]LEEP", skin, "title");
        play = new TextButton("PLAY", skin);
        stats = new TextButton("STATS", skin);
        settings = new TextButton("SETTINGS", skin);

        play.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent)
                StateManager.getInstance().pushState(StateEnum.GAME_STATE);
            return false;
        });

        table.padTop(-192);
        table.add(title).row();
        table.add(play).minWidth(216).spaceTop(32).row();
        table.add(stats).minWidth(216).spaceTop(8).row();
        table.add(settings).minWidth(216).spaceTop(8);
        table.align(Align.center);

        addActor(table);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(181f / 255f, 234f / 255f, 255f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
    public String getGroupName() {
        return "menu";
    }
}

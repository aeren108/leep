package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.Settings;

public class SplashState extends State {
    private Assets assets;
    private Label title;

    private float timer = 0;

    public SplashState() {
        super(new ExtendViewport(Settings.UI_WIDTH, Settings.UI_HEIGHT));
    }

    @Override
    public void show() {
        assets = Assets.getInstance();
        assets.loadAll();

        title = new Label("LEEP", assets.get("ui/ui-skin.json", Skin.class), "title");
        title.setPosition(-title.getWidth() / 2, -title.getHeight() / 2);
        addActor(title);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        timer += delta;

        if (assets.update() && timer >= 1f) {
            StateManager.getInstance().setState(StateEnum.MAIN_MENU);
            timer = 0;
        }

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
}

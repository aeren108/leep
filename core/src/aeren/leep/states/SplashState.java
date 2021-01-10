package aeren.leep.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import aeren.leep.Assets;
import aeren.leep.Settings;

public class SplashState extends State {
    private Assets assets;
    private Texture title;

    private float timer = 0;

    public SplashState() {
        super(new ExtendViewport(Settings.UI_WIDTH, Settings.UI_HEIGHT));
    }

    @Override
    public void show() {
        assets = Assets.getInstance();
        assets.loadAll();

        title = assets.get("ui/title.png", Texture.class);
        /*title = new Label("[RED]LEEP", assets.get("ui/ui-skin.json", Skin.class), "title");
        title.setPosition(-title.getWidth() / 2, -title.getHeight() / 2);
        addActor(title);*/
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        timer += delta;

        if (assets.update() && timer >= 1.5f) {
            assets.get("ui/ui-skin.json", Skin.class).getFont("orange-kid").getData().markupEnabled = true;
            assets.get("ui/ui-skin.json", Skin.class).getFont("orange-kid-title").getData().markupEnabled = true;

            StateManager.getInstance().setState(StateEnum.MAIN_MENU);
            timer = 0;
        }

        getBatch().begin();
        getBatch().draw(title, -title.getWidth() / 2, -title.getHeight() / 2);
        getBatch().end();

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

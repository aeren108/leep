package aeren.leep.states.fragments;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import aeren.leep.Assets;
import aeren.leep.Settings;
import aeren.leep.states.GameState;
import aeren.leep.states.MenuState;
import aeren.leep.states.StateManager;

public class GameOverFragment extends Fragment {
    private Texture background;

    private Label titleLabel;
    private Label scoreLabel;
    private TextButton retryButton;
    private TextButton backButton;

    private int score;

    public GameOverFragment(GameState state, int score) {
        super(state);
        this.score = score;
    }

    @Override
    public void init() {
        Skin skin = Assets.manager.get(Assets.skin);

        Pixmap pixmap = new Pixmap((int) Settings.WIDTH * 4, (int) Settings.HEIGHT * 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, .5f);
        pixmap.fillRectangle(0, 0, (int) Settings.WIDTH * 4, (int) Settings.HEIGHT * 4);
        background = new Texture(pixmap);

        titleLabel = new Label("[GOLD]YOU DIED", skin);
        scoreLabel = new Label("[WHITE]SCORE: [GOLD]" + score, skin);
        retryButton = new TextButton("RETRY", skin);
        backButton = new TextButton("MAIN MENU", skin);

        retryButton.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                state.popFragment();
                ((GameState)state).reset();
            }
            return true;
        });

        backButton.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent)
                StateManager.getInstance().setState(new MenuState());
            return true;
        });

        Table table = new Table();
        table.align(Align.center);

        table.padTop(-132);
        table.add(titleLabel).row();
        table.add(scoreLabel).row();
        table.add(retryButton).minWidth(256).spaceTop(16).row();
        table.add(backButton).minWidth(256).spaceTop(8).row();

        this.addActor(table);
    }

    @Override
    public void draw() {
        getBatch().begin();
        getBatch().draw(background, -getWidth() / 2, -getHeight() / 2, getWidth(), getHeight());
        getBatch().end();

        super.draw();
    }
}

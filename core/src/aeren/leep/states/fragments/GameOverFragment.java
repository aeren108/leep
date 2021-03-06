package aeren.leep.states.fragments;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import aeren.leep.Assets;
import aeren.leep.states.GameState;
import aeren.leep.states.StateEnum;
import aeren.leep.states.StateManager;
import aeren.leep.ui.GameOverTable;

public class GameOverFragment extends Fragment {
    private Texture background;

    private GameOverTable got;
    private ImageButton retryButton;
    private ImageButton backButton;
    private Label notif;

    private int score, best;
    private boolean unlocked;

    public GameOverFragment(GameState state, int score, int best, boolean unlocked) {
        super(state);
        this.score = score;
        this.best = best;
        this.unlocked = unlocked;
    }

    @Override
    public void init() {
        Skin skin = Assets.getInstance().get("ui/ui-skin.json", Skin.class);

        Pixmap pixmap = new Pixmap(9, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, .5f);
        pixmap.fillRectangle(0, 0, 9, 16);
        background = new Texture(pixmap);
        pixmap.dispose();

        got = new GameOverTable(skin, score, best);
        retryButton = new ImageButton(skin, "play");
        backButton = new ImageButton(skin, "menu");
        notif = new Label("NEW CHARACTER UNLOCKED", skin);

        retryButton.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                retryButton.setDisabled(true);
                state.popFragment();
                ((GameState)state).reset();
            }
            return true;
        });

        backButton.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent)
                StateManager.getInstance().setState(StateEnum.MAIN_MENU);
            return true;
        });

        Table col = new Table();
        col.add(retryButton).minWidth(got.getPrefWidth() / 2 - 8).minHeight(96).spaceRight(16);
        col.add(backButton).minWidth(got.getPrefWidth() / 2 - 8).minHeight(96);

        Table table = new Table();
        table.align(Align.center);
        table.add(got).row();
        if (unlocked) table.add(notif).spaceTop(24).row();
        table.add(col).spaceTop(16);

        this.addActor(table);
    }

    @Override
    public void draw() {
        getBatch().begin();
        getBatch().draw(background, -getWidth() / 2, -getHeight() / 2, getWidth(), getHeight());
        getBatch().end();

        super.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        super.dispose();
    }
}

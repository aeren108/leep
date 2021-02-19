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
import aeren.leep.ui.PauseTable;

public class PauseFragment extends Fragment {
    private Texture background;

    private Table col;
    private Label countDown;
    private PauseTable pt;
    private ImageButton resume;
    private ImageButton menu;

    private int countDownDuration = 3;
    private float countDownTimer = 0;
    private boolean countDownStarted = false;

    private final int score;

    public PauseFragment(GameState state, int score) {
        super(state);
        this.score = score;
    }

    @Override
    public void init() {
        Skin skin = Assets.getInstance().get("ui/ui-skin.json", Skin.class);

        Pixmap pixmap = new Pixmap(9, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, .5f);
        pixmap.fillRectangle(0, 0, 9, 16);
        background = new Texture(pixmap);
        pixmap.dispose();

        Table table = new Table();
        pt = new PauseTable(skin, score);
        countDown = new Label("[WHITE]" + countDownDuration, skin, "title");
        resume = new ImageButton(skin, "play");
        menu = new ImageButton(skin, "menu");

        resume.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                table.removeActor(pt);
                table.removeActor(col);
                table.add(countDown);

                countDownStarted = true;
                resume.setDisabled(true);
            }
            return true;
        });

        menu.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                StateManager.getInstance().setState(StateEnum.MAIN_MENU);
            }
            return false;
        });

        col = new Table();
        col.add(resume).minWidth(pt.getMinWidth() / 2 - 8).minHeight(96).spaceTop(16).spaceRight(16);
        col.add(menu).minWidth(pt.getMinWidth() / 2 - 8).minHeight(96);

        table.align(Align.center).padTop(-128);
        table.add(pt).row();
        table.add(col).spaceTop(16);

        addActor(table);
    }

    @Override
    public void draw() {
        getBatch().begin();
        getBatch().draw(background, -getWidth() / 2, -getHeight() / 2, getWidth(), getHeight());
        getBatch().end();

        super.draw();
    }

    @Override
    public void act(float delta) {
        if (countDownStarted) {
            countDownTimer += delta;

            if (countDownTimer >= 1f) {
                countDownDuration--;
                countDownTimer = 0;

                countDown.setText("[WHITE]" + countDownDuration);

                if (countDownDuration <= 0) {

                    ((GameState)state).resumeGame();
                    state.popFragment();
                }
            }

        }

        super.act(delta);
    }

    @Override
    public void dispose() {
        background.dispose();
        super.dispose();
    }
}

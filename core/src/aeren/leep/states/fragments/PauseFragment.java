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
import aeren.leep.states.GameState;

public class PauseFragment extends Fragment {
    private Texture background;

    private Label title;
    private Label countDown;
    private TextButton resume;

    private int countDownDuration = 3;
    private float countDownTimer = 0;
    private boolean countDownStarted = false;

    public PauseFragment(GameState state) {
        super(state);
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
        title = new Label("GAME PAUSED", skin);
        countDown = new Label("[WHITE]" + countDownDuration, skin, "title");
        resume = new TextButton("RESUME", skin);

        resume.addListener((Event event) -> {
            if (event instanceof ChangeListener.ChangeEvent) {
                table.removeActor(title);
                table.removeActor(resume);
                table.add(countDown);

                countDownStarted = true;
                resume.setDisabled(true);
            }
            return true;
        });

        table.align(Align.center);
        table.padTop(-156);
        table.add(title).row();
        table.add(resume).minWidth(256).spaceTop(16).row();

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
                    state.resume();
                    state.popFragment();
                }
            }

        }

        super.act(delta);
    }
}

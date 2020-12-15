package aeren.leep.states.fragments;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import aeren.leep.Assets;
import aeren.leep.states.GameState;
import aeren.leep.states.MenuState;
import aeren.leep.states.StateManager;

public class GameOverFragment extends Fragment {
    private Label title;
    private Label score;
    private TextButton retry;
    private TextButton back;

    public GameOverFragment(GameState state) {
        super(state);
    }

    @Override
    public void init() {
        Skin skin = Assets.manager.get(Assets.skin);

        title = new Label("[RED]GAME OVER", skin);
        score = new Label("[BLACK]SCORE: [MAROON]" + ((GameState)state).getLevel().getScore(), skin);
        retry = new TextButton("RETRY", skin);
        back = new TextButton("MAIN MENU", skin);

        retry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.popFragment();
                ((GameState)state).getLevel().reset();
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StateManager.getInstance().setState(new MenuState());
            }
        });

        Table table = new Table();
        table.align(Align.center);

        table.padTop(-132);
        table.add(title).row();
        table.add(score).row();
        table.add(retry).spaceTop(16).row();
        table.add(back).spaceTop(8).row();

        this.addActor(table);
    }
}

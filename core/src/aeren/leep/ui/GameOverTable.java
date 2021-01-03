package aeren.leep.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

public class GameOverTable extends Table {
    private Skin skin;

    private Label titleLabel;
    private Label scoreLabel;
    private Label bestLabel;

    private NinePatchDrawable background;

    private int score, best;

    public GameOverTable(Skin skin, int score, int best) {
        this.skin = skin;
        this.score = score;
        this.best = best;

        setSkin(skin);
        setSize(getPrefWidth(), getPrefHeight());
        initialize();
    }

    private void initialize() {
        background = new NinePatchDrawable(skin.getPatch("button"));

        titleLabel = new Label("[WHITE]GAME OVER", skin);
        scoreLabel = new Label("[MAROON]SCORE\n[WHITE]" + score, skin);
        bestLabel = new Label("[MAROON]BEST\n[WHITE]" + best, skin);

        scoreLabel.setAlignment(Align.center);
        bestLabel.setAlignment(Align.center);

        add(titleLabel).padTop(16).row();
        add(scoreLabel).padTop(48).row();
        add(bestLabel).padTop(24).padBottom(16);

        setBackground(background);
    }

    @Override
    public float getPrefWidth() {
        return 256;
    }

    @Override
    public float getPrefHeight() {
        return 448;
    }

    @Override
    public float getMinWidth() {
        return getPrefWidth();
    }

    @Override
    public float getMinHeight() {
        return getPrefHeight();
    }
}

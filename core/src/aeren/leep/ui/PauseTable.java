package aeren.leep.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

public class PauseTable extends Table {
    private Skin skin;
    private NinePatchDrawable background;

    private Label titleLabel;
    private Label scoreLabel;

    private int score;

    public PauseTable(Skin skin, int score) {
        this.skin = skin;
        this.score = score;

        initialize();
    }

    private void initialize() {
        background = new NinePatchDrawable(skin.getPatch("button"));

        titleLabel = new Label("[WHITE]PAUSED", skin);
        scoreLabel = new Label("[MAROON]SCORE\n[WHITE]" + score, skin);

        scoreLabel.setAlignment(Align.center);

        add(titleLabel).padTop(16).row();
        add(scoreLabel).padBottom(16).padTop(24).row();

        setBackground(background);
    }

    @Override
    public float getPrefWidth() {
        return 256;
    }

    @Override
    public float getPrefHeight() {
        return 256;
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

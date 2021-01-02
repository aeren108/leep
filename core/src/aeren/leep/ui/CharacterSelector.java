package aeren.leep.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import java.util.List;

import aeren.leep.character.Character;
import aeren.leep.character.CharacterListener;
import aeren.leep.character.CharacterManager;

public class CharacterSelector extends Table {
    private Skin skin;
    private Label status;

    private List<Character> characters;
    private Character character;
    private int characterIndex;

    private Drawable selector;

    public CharacterSelector(Skin skin, List<Character> characters) {
        this.skin = skin;
        this.characters = characters;

        setSkin(skin);
        initialize();
    }

    private void initialize() {
        status = new Label("LOCKED", skin);

        setTouchable(Touchable.enabled);
        setSize(getPrefWidth(), getPrefHeight());

        setCharacter(CharacterManager.getInstance().getCurrentCharacter());
        characterIndex = characters.indexOf(character);

        selector = skin.getDrawable("unlocked");

        bottom().add(status).padBottom(16);
        setBackground(new NinePatchDrawable(skin.getPatch("button")));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawSelector(batch);
    }

    private void drawSelector(Batch batch) {
        float prevEnd = getX() + getPrefWidth() / 2 - getSelectorWidth() / 2 - 16;
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);

            if (c.unlocked) batch.setColor(Color.WHITE);
            else batch.setColor(Color.valueOf("#454545"));

            if (c == character) {
                selector.draw(batch, prevEnd + 16, getY() + (getPrefHeight() - 56) - 14, 28, 28);
                prevEnd += 44;
            } else {
                selector.draw(batch, prevEnd + 16, getY() + (getPrefHeight() - 56) - 8, 16, 16);
                prevEnd += 32;
            }
        }
    }

    private String getCharacterStatus(Character c) {
        if (c.unlocked)
            return "UNLOCKED";

        return c.status;
    }

    private float getSelectorWidth() {
        return (characters.size() - 1) * 32 + 28;
    }

    public void next() {
        if (characterIndex + 1 >= characters.size())
            characterIndex = 0;
        else
            characterIndex++;

        setCharacter(characters.get(characterIndex));
    }

    public void prev() {
        if (characterIndex - 1 < 0)
            characterIndex = characters.size() - 1;
        else
            characterIndex--;

        setCharacter(characters.get(characterIndex));
    }

    public void setCharacter(Character character) {
        this.character = character;
        status.setText(getCharacterStatus(character));
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public float getPrefWidth() {
        return getSelectorWidth() + 96;
    }

    @Override
    public float getPrefHeight() {
        return 192;
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

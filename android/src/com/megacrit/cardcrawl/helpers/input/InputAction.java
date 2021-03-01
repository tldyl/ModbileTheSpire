package com.megacrit.cardcrawl.helpers.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.megacrit.cardcrawl.android.mods.helpers.input.ScrollInputProcessor.TextInput;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import java.util.HashMap;
import java.util.Map;

public class InputAction {
    private static final UIStrings uiStrings;
    public static final Map<String, String> TEXT_CONVERSIONS;
    private int keycode;
    private static final HashMap<Integer, Integer> equivalentKeys;

    public InputAction(int keycode) {
        this.keycode = keycode;
    }

    public int getKey() {
        return this.keycode;
    }

    public String getKeyString() {
        String keycodeStr = Keys.toString(this.keycode);
        return TEXT_CONVERSIONS.getOrDefault(keycodeStr, keycodeStr);
    }

    public boolean isJustPressed() {
        if (TextInput.isTextInputActive()) {
            return false;
        }
        boolean alternatePressed = equivalentKeys.containsKey(this.keycode) && Gdx.input.isKeyJustPressed(equivalentKeys.get(this.keycode));
        return alternatePressed || Gdx.input.isKeyJustPressed(this.keycode);
    }

    public boolean isPressed() {
        if (TextInput.isTextInputActive()) {
            return false;
        }
        boolean alternatePressed = equivalentKeys.containsKey(this.keycode) && Gdx.input.isKeyPressed(equivalentKeys.get(this.keycode));
        return alternatePressed || Gdx.input.isKeyPressed(this.keycode);
    }

    public void remap(int newKeycode) {
        this.keycode = newKeycode;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("InputKeyNames");
        TEXT_CONVERSIONS = uiStrings.TEXT_DICT;
        equivalentKeys = new HashMap<>();
        equivalentKeys.put(7, 144);
        equivalentKeys.put(8, 145);
        equivalentKeys.put(9, 146);
        equivalentKeys.put(10, 147);
        equivalentKeys.put(11, 148);
        equivalentKeys.put(12, 149);
        equivalentKeys.put(13, 150);
        equivalentKeys.put(14, 151);
        equivalentKeys.put(15, 152);
        equivalentKeys.put(16, 153);
        equivalentKeys.put(144, 7);
        equivalentKeys.put(145, 8);
        equivalentKeys.put(146, 9);
        equivalentKeys.put(147, 10);
        equivalentKeys.put(148, 11);
        equivalentKeys.put(149, 12);
        equivalentKeys.put(150, 13);
        equivalentKeys.put(151, 14);
        equivalentKeys.put(152, 15);
        equivalentKeys.put(153, 16);
    }
}

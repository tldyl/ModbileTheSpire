package com.megacrit.cardcrawl.helpers.input;

import com.badlogic.gdx.InputProcessor;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.ui.panels.RenamePopup;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;

public class AndroidTypeHelper implements InputProcessor {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(AndroidTypeHelper.class);
    private boolean seed;

    public AndroidTypeHelper(boolean seed) {
        this.seed = seed;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        String charStr = String.valueOf(character);
        logger.info(charStr);
        if (charStr.length() != 1) {
            return false;
        } else {
            if (this.seed) {
                if (SeedPanel.isFull()) {
                    return false;
                }

                if (InputHelper.isPasteJustPressed()) {
                    return false;
                }

                String converted = SeedHelper.getValidCharacter(charStr, SeedPanel.textField);
                if (converted != null) {
                    SeedPanel.textField = SeedPanel.textField + converted;
                }
                if (character == '\b' && SeedPanel.textField.length() > 0) {
                    SeedPanel.textField = SeedPanel.textField.substring(0, SeedPanel.textField.length() - 1);
                }
            } else {
                if (FontHelper.getSmartWidth(FontHelper.cardTitleFont, RenamePopup.textField, 1.0E7F, 0.0F, 0.82F) >= 240.0F * Settings.scale) {
                    return false;
                }

                if (Character.isDigit(character) || Character.isLetter(character)) {
                    RenamePopup.textField = RenamePopup.textField + charStr;
                }
            }

            return true;
        }
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}

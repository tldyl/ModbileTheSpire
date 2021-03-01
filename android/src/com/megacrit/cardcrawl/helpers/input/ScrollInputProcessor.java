package com.megacrit.cardcrawl.helpers.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.helpers.input.ScrollInputProcessor.TextInput;
import com.megacrit.cardcrawl.android.mods.interfaces.TextReceiver;
import com.megacrit.cardcrawl.core.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScrollInputProcessor implements InputProcessor {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(ScrollInputProcessor.class);
    public static String lastPressed = "";
    public static String lastPressed2 = "";
    public static boolean lastPressedSwitch = true;

    public ScrollInputProcessor() {
    }

    public static void logLastPressed(String msg) {
        lastPressedSwitch = !lastPressedSwitch;
        if (lastPressedSwitch) {
            lastPressed = msg;
        } else {
            lastPressed2 = msg;
        }

        if (Settings.isInfo) {
            logger.info(msg);
        }

    }

    public boolean keyDown(int keycode) {
        return !TextInput.receivers.isEmpty() && TextInput.receivers.get(TextInput.receivers.size() - 1).onKeyDown(keycode);
    }

    public boolean keyUp(int keycode) {
        return !TextInput.receivers.isEmpty() && TextInput.receivers.get(TextInput.receivers.size() - 1).onKeyUp(keycode);
    }

    public boolean keyTyped(char character) {
        if (!TextInput.receivers.isEmpty()) {
            TextReceiver t = TextInput.receivers.get(TextInput.receivers.size() - 1);
            String text = t.getCurrentText();
            switch(character) {
                case '\n':
                case '\r':
                    if (t.onPushEnter()) {
                        return true;
                    }
                case '\t':
                case ' ':
                    if (t.onPushTab()) {
                        return true;
                    }
                case '\b':
                    if (t.onPushBackspace()) {
                        return true;
                    }
                    break;
                case '\u000b':
                case '\f':
                default:
                    if (character < ' ') {
                        return false;
                    }
            }
            if (!(UIUtils.isMac || Gdx.input.isKeyPressed(Input.Keys.SYM))) {
                boolean backspace = character == '\b';
                boolean add = t.acceptCharacter(character);
                if (backspace && text.length() > 1) {
                    t.setText(text.substring(0, text.length() - 1));
                    return true;
                } else if (backspace) {
                    t.setText("");
                    return true;
                } else {
                    if (add && (TextInput.currentCharLimit == -1 || text.length() < TextInput.currentCharLimit)) {
                        String s = t.getAppendedText(character);
                        if (s != null) {
                            t.setText(text.concat(s));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!Gdx.input.isButtonPressed(1)) {
            InputHelper.touchDown = true;
        }

        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        InputHelper.touchUp = true;
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        InputHelper.isMouseDown = true;
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        if (amount == -1) {
            InputHelper.scrolledUp = true;
        } else if (amount == 1) {
            InputHelper.scrolledDown = true;
        }

        return false;
    }
}

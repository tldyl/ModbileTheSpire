package com.megacrit.cardcrawl.android.mods.helpers.input.ScrollInputProcessor;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.interfaces.PreUpdateSubscriber;
import com.megacrit.cardcrawl.android.mods.interfaces.TextReceiver;

import java.util.ArrayList;

public class TextInput implements PreUpdateSubscriber {
    public static String text;
    private static final char BACKSPACE = '\b';
    private static final char ENTER_DESKTOP = '\r';
    private static final char ENTER_ANDROID = '\n';
    private static final char TAB = '\t';
    public static int currentCharLimit;
    public static final ArrayList<TextReceiver> receivers;

    @Override
    public void receivePreUpdate() {
        if (receivers.size() > 0 && (receivers.get(0)).isDone()) {
            stopTextReceiver(receivers.get(0));
        }
    }

    public static boolean isTextInputActive() {
        return receivers.size() > 0;
    }

    public static void startTextReceiver(TextReceiver t) {
        receivers.remove(t);
        receivers.add(t);
        currentCharLimit = t.getCharLimit();
    }

    public static void stopTextReceiver(TextReceiver t) {
        receivers.remove(t);
        if (receivers.size() > 0) {
            TextReceiver next = receivers.get(receivers.size() - 1);
            currentCharLimit = next.getCharLimit();
        }
    }

    static {
        BaseMod.subscribe(new TextInput());
        text = "";
        currentCharLimit = -1;
        receivers = new ArrayList<>();
    }
}

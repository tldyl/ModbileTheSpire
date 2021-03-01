package com.megacrit.cardcrawl.android.mods.interfaces;

public interface TextReceiver {
    String getCurrentText();

    void setText(String var1);

    boolean isDone();

    default boolean onKeyDown(int keycode) {
        return false;
    }

    default boolean onKeyUp(int keycode) {
        return false;
    }

    default boolean onPushEnter() {
        return true;
    }

    default boolean onPushTab() {
        return true;
    }

    default boolean onPushBackspace() {
        return false;
    }

    boolean acceptCharacter(char var1);

    default String getAppendedText(char c) {
        return String.valueOf(c);
    }

    default int getCharLimit() {
        return -1;
    }
}

package com.megacrit.cardcrawl.android.mods.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IUIElement {
    void render(SpriteBatch var1);

    void update();

    int renderLayer();

    int updateOrder();

    default void set(float xPos, float yPos) {
    }

    default void setX(float xPos) {
    }

    default void setY(float yPos) {
    }

    default float getX() {
        return -2.14748365E9F;
    }

    default float getY() {
        return -2.14748365E9F;
    }
}

package com.megacrit.cardcrawl.android.mods.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.ModPanel;
import com.megacrit.cardcrawl.android.mods.interfaces.IUIElement;

import java.util.function.Consumer;

public class ModLabeledToggleButton implements IUIElement {
    private static final float TEXT_X_OFFSET = 40.0F;
    private static final float TEXT_Y_OFFSET = 8.0F;
    public ModToggleButton toggle;
    public ModLabel text;

    public ModLabeledToggleButton(String labelText, float xPos, float yPos, Color color, BitmapFont font, boolean enabled, ModPanel p, Consumer<ModLabel> labelUpdate, Consumer<ModToggleButton> c) {
        this.toggle = new ModToggleButton(xPos, yPos, enabled, false, p, c);
        this.text = new ModLabel(labelText, xPos + 40.0F, yPos + 8.0F, color, font, p, labelUpdate);
        this.toggle.wrapHitboxToText(labelText, 1000.0F, 0.0F, font);
    }

    public void render(SpriteBatch sb) {
        this.toggle.render(sb);
        this.text.render(sb);
    }

    public void update() {
        this.toggle.update();
        this.text.update();
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

    public void set(float xPos, float yPos) {
        this.toggle.set(xPos, yPos);
        this.text.set(xPos + 40.0F, yPos + 8.0F);
    }

    public void setX(float xPos) {
        this.toggle.setX(xPos);
        this.text.setX(xPos + 40.0F);
    }

    public void setY(float yPos) {
        this.toggle.setY(yPos);
        this.text.setY(yPos + 8.0F);
    }

    public float getX() {
        return this.toggle.getX();
    }

    public float getY() {
        return this.toggle.getY();
    }
}

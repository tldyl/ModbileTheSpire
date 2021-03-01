package com.megacrit.cardcrawl.android.mods.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.ModPanel;
import com.megacrit.cardcrawl.android.mods.interfaces.IUIElement;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.util.function.Consumer;

public class ModLabel implements IUIElement {
    private Consumer<ModLabel> update;
    public ModPanel parent;
    public String text;
    public float x;
    public float y;
    public Color color;
    public BitmapFont font;

    public ModLabel(String labelText, float xPos, float yPos, ModPanel p, Consumer<ModLabel> updateFunc) {
        this(labelText, xPos, yPos, Color.WHITE, FontHelper.buttonLabelFont, p, updateFunc);
    }

    public ModLabel(String labelText, float xPos, float yPos, Color color, ModPanel p, Consumer<ModLabel> updateFunc) {
        this(labelText, xPos, yPos, color, FontHelper.buttonLabelFont, p, updateFunc);
    }

    public ModLabel(String labelText, float xPos, float yPos, BitmapFont font, ModPanel p, Consumer<ModLabel> updateFunc) {
        this(labelText, xPos, yPos, Color.WHITE, font, p, updateFunc);
    }

    public ModLabel(String labelText, float xPos, float yPos, Color color, BitmapFont font, ModPanel p, Consumer<ModLabel> updateFunc) {
        this.text = labelText;
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
        this.color = color;
        this.font = font;
        this.parent = p;
        this.update = updateFunc;
    }

    public void render(SpriteBatch sb) {
        FontHelper.renderFontLeftDownAligned(sb, this.font, this.text, this.x, this.y, this.color);
    }

    public void update() {
        this.update.accept(this);
    }

    public int renderLayer() {
        return 2;
    }

    public int updateOrder() {
        return 0;
    }

    public void set(float xPos, float yPos) {
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
    }

    public void setX(float xPos) {
        this.x = xPos * Settings.scale;
    }

    public void setY(float yPos) {
        this.y = yPos * Settings.scale;
    }

    public float getX() {
        return this.x / Settings.scale;
    }

    public float getY() {
        return this.y / Settings.scale;
    }
}

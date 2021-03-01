package com.megacrit.cardcrawl.android.mods.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.interfaces.IUIElement;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class ModImage implements IUIElement {
    private Texture texture;
    private float x;
    private float y;
    private float w;
    private float h;

    public ModImage(float x, float y, String texturePath) {
        this.texture = ImageMaster.loadImage(texturePath);
        this.x = x * Settings.scale;
        this.y = y * Settings.scale;
        this.w = (float)this.texture.getWidth() * Settings.scale;
        this.h = (float)this.texture.getHeight() * Settings.scale;
    }

    public ModImage(float x, float y, Texture tex) {
        this.texture = tex;
        this.x = x * Settings.scale;
        this.y = y * Settings.scale;
        this.w = (float)this.texture.getWidth() * Settings.scale;
        this.h = (float)this.texture.getHeight() * Settings.scale;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(this.texture, this.x, this.y, this.w, this.h);
    }

    public void update() {
    }

    public int renderLayer() {
        return 0;
    }

    public int updateOrder() {
        return 1;
    }
}

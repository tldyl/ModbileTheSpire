package com.megacrit.cardcrawl.android.mods.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.helpers.UIElementModificationHelper;
import com.megacrit.cardcrawl.android.mods.interfaces.IUIElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public class ModColorDisplay implements IUIElement {
    public float hbShrink;
    public float r;
    public float g;
    public float b;
    public float a;
    public float rOutline;
    public float gOutline;
    public float bOutline;
    public float aOutline;
    public float x;
    public float y;
    public float w;
    public float h;
    public Consumer<ModColorDisplay> click;
    public Texture texture;
    public Texture outline;
    private Hitbox hb;

    public ModColorDisplay(float x, float y, Texture texture, Texture outline, Consumer<ModColorDisplay> click) {
        this.hbShrink = 16.0F;
        this.r = 1.0F;
        this.g = 1.0F;
        this.b = 1.0F;
        this.a = 1.0F;
        this.rOutline = 0.0F;
        this.gOutline = 0.0F;
        this.bOutline = 0.0F;
        this.aOutline = 1.0F;
        this.texture = texture;
        this.outline = outline;
        this.x = x;
        this.y = y;
        this.w = (float)texture.getWidth();
        this.h = (float)texture.getHeight();
        this.click = click;
        float hbx = (x + this.hbShrink) * Settings.scale;
        float hby = (y + this.hbShrink) * Settings.scale;
        float hbw = (this.w - 2.0F * this.hbShrink) * Settings.scale;
        float hbh = (this.h - 2.0F * this.hbShrink) * Settings.scale;
        this.hb = new Hitbox(hbx, hby, hbw, hbh);
    }

    public ModColorDisplay(float x, float y, float hbShrink, Texture texture, Texture outline, Consumer<ModColorDisplay> click) {
        this(x, y, texture, outline, click);
        this.hbShrink = hbShrink;
        float hbx = (x + hbShrink) * Settings.scale;
        float hby = (y + hbShrink) * Settings.scale;
        float hbw = (this.w - 2.0F * hbShrink) * Settings.scale;
        float hbh = (this.h - 2.0F * hbShrink) * Settings.scale;
        this.hb = new Hitbox(hbx, hby, hbw, hbh);
    }

    public void render(SpriteBatch sb) {
        if (this.outline != null) {
            sb.setColor(new Color(this.rOutline, this.gOutline, this.bOutline, this.aOutline));
            sb.draw(this.outline, this.x * Settings.scale, this.y * Settings.scale, this.w * Settings.scale, this.h * Settings.scale);
        }

        sb.setColor(new Color(this.r, this.g, this.b, this.a));
        sb.draw(this.texture, this.x * Settings.scale, this.y * Settings.scale, this.w * Settings.scale, this.h * Settings.scale);
        this.hb.render(sb);
    }

    public void update() {
        this.hb.update();
        if (this.hb.justHovered) {
            CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
        }

        if (this.hb.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
            this.hb.clickStarted = true;
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            this.onClick();
        }

    }

    private void onClick() {
        this.click.accept(this);
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

    public void set(float xPos, float yPos) {
        this.x = xPos;
        this.y = yPos;
        UIElementModificationHelper.moveHitboxByOriginalParameters(this.hb, (this.x + this.hbShrink) * Settings.scale, (this.y + this.hbShrink) * Settings.scale);
    }

    public void setX(float xPos) {
        this.set(xPos, this.y);
    }

    public void setY(float yPos) {
        this.set(this.x, yPos);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}

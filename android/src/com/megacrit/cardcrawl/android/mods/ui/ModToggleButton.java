package com.megacrit.cardcrawl.android.mods.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.ModPanel;
import com.megacrit.cardcrawl.android.mods.helpers.UIElementModificationHelper;
import com.megacrit.cardcrawl.android.mods.interfaces.IUIElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public class ModToggleButton implements IUIElement {
    private static final float TOGGLE_Y_DELTA = 0.0F;
    private static final float TOGGLE_X_EXTEND = 12.0F;
    private static final float HB_WIDTH_EXTENDED = 200.0F;
    private Consumer<ModToggleButton> toggle;
    private Hitbox hb;
    private float x;
    private float y;
    private float w;
    private float h;
    private boolean extendedHitbox;
    public boolean enabled;
    public ModPanel parent;

    public ModToggleButton(float xPos, float yPos, ModPanel p, Consumer<ModToggleButton> c) {
        this(xPos, yPos, false, true, p, c);
    }

    public ModToggleButton(float xPos, float yPos, boolean enabled, boolean extendedHitbox, ModPanel p, Consumer<ModToggleButton> c) {
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
        this.w = (float)ImageMaster.OPTION_TOGGLE.getWidth();
        this.h = (float)ImageMaster.OPTION_TOGGLE.getHeight();
        this.extendedHitbox = extendedHitbox;
        if (extendedHitbox) {
            this.hb = new Hitbox(this.x - 12.0F * Settings.scale, this.y - 0.0F * Settings.scale, 200.0F * Settings.scale, this.h * Settings.scale);
        } else {
            this.hb = new Hitbox(this.x, this.y - 0.0F * Settings.scale, this.w * Settings.scale, this.h * Settings.scale);
        }

        this.enabled = enabled;
        this.parent = p;
        this.toggle = c;
    }

    public void wrapHitboxToText(String text, float lineWidth, float lineSpacing, BitmapFont font) {
        float tWidth = FontHelper.getSmartWidth(font, text, lineWidth, lineSpacing);
        this.hb.width = tWidth + this.h * Settings.scale + 12.0F;
    }

    public void render(SpriteBatch sb) {
        if (this.hb.hovered) {
            sb.setColor(Color.CYAN);
        } else if (this.enabled) {
            sb.setColor(Color.LIGHT_GRAY);
        } else {
            sb.setColor(Color.WHITE);
        }

        sb.draw(ImageMaster.OPTION_TOGGLE, this.x, this.y, this.w * Settings.scale, this.h * Settings.scale);
        if (this.enabled) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.OPTION_TOGGLE_ON, this.x, this.y, this.w * Settings.scale, this.h * Settings.scale);
        }

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
            this.onToggle();
        }

    }

    private void onToggle() {
        this.enabled = !this.enabled;
        this.toggle.accept(this);
    }

    public void toggle() {
        this.onToggle();
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

    public void set(float xPos, float yPos) {
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
        if (this.extendedHitbox) {
            UIElementModificationHelper.moveHitboxByOriginalParameters(this.hb, this.x - 12.0F * Settings.scale, this.y - 0.0F * Settings.scale);
        } else {
            UIElementModificationHelper.moveHitboxByOriginalParameters(this.hb, this.x, this.y - 0.0F * Settings.scale);
        }

    }

    public void setX(float xPos) {
        this.set(xPos, this.y / Settings.scale);
    }

    public void setY(float yPos) {
        this.set(this.x / Settings.scale, yPos);
    }

    public float getX() {
        return this.x / Settings.scale;
    }

    public float getY() {
        return this.y / Settings.scale;
    }
}

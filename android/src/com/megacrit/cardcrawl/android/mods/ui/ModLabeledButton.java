package com.megacrit.cardcrawl.android.mods.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

public class ModLabeledButton implements IUIElement {
    private Consumer<ModLabeledButton> click;
    private Hitbox hb;
    private float x;
    private float y;
    private float w;
    private float middle_width;
    private float h;
    public BitmapFont font;
    public String label;
    public ModPanel parent;
    public Color color;
    public Color colorHover;
    private static final float TEXT_OFFSET = 9.0F;
    private Texture textureLeft;
    private Texture textureRight;
    private Texture textureMiddle;

    public ModLabeledButton(String label, float xPos, float yPos, ModPanel p, Consumer<ModLabeledButton> c) {
        this(label, xPos, yPos, Color.WHITE, Color.GREEN, FontHelper.buttonLabelFont, p, c);
    }

    public ModLabeledButton(String label, float xPos, float yPos, Color textColor, Color textColorHover, ModPanel p, Consumer<ModLabeledButton> c) {
        this(label, xPos, yPos, textColor, textColorHover, FontHelper.buttonLabelFont, p, c);
    }

    public ModLabeledButton(String label, float xPos, float yPos, Color textColor, Color textColorHover, BitmapFont font, ModPanel p, Consumer<ModLabeledButton> c) {
        this.label = label;
        this.font = font;
        this.color = textColor;
        this.colorHover = textColorHover;
        this.textureLeft = ImageMaster.loadImage("images/ui/ButtonLeft.png");
        this.textureRight = ImageMaster.loadImage("images/ui/ButtonRight.png");
        this.textureMiddle = ImageMaster.loadImage("images/ui/ButtonMiddle.png");
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
        this.middle_width = Math.max(0.0F, FontHelper.getSmartWidth(font, label, 9999.0F, 0.0F) - 18.0F * Settings.scale);
        this.w = (float)(this.textureLeft.getWidth() + this.textureRight.getWidth()) * Settings.scale + this.middle_width;
        this.h = (float)this.textureLeft.getHeight() * Settings.scale;
        this.hb = new Hitbox(this.x + 1.0F * Settings.scale, this.y + 1.0F * Settings.scale, this.w - 2.0F * Settings.scale, this.h - 2.0F * Settings.scale);
        this.parent = p;
        this.click = c;
    }

    public void render(SpriteBatch sb) {
        sb.draw(this.textureLeft, this.x, this.y, (float)this.textureLeft.getWidth() * Settings.scale, this.h);
        sb.draw(this.textureMiddle, this.x + (float)this.textureLeft.getWidth() * Settings.scale, this.y, this.middle_width, this.h);
        sb.draw(this.textureRight, this.x + (float)this.textureLeft.getWidth() * Settings.scale + this.middle_width, this.y, (float)this.textureRight.getWidth() * Settings.scale, this.h);
        this.hb.render(sb);
        sb.setColor(Color.WHITE);
        if (this.hb.hovered) {
            FontHelper.renderFontCentered(sb, this.font, this.label, this.hb.cX, this.hb.cY, this.colorHover);
        } else {
            FontHelper.renderFontCentered(sb, this.font, this.label, this.hb.cX, this.hb.cY, this.color);
        }

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
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
        UIElementModificationHelper.moveHitboxByOriginalParameters(this.hb, this.x + 1.0F * Settings.scale, this.y + 1.0F * Settings.scale);
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

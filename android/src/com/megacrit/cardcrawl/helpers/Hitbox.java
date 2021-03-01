package com.megacrit.cardcrawl.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class Hitbox {
    public float x;
    public float y;
    public float cX;
    public float cY;
    public float width;
    public float height;
    public boolean hovered;
    public boolean justHovered;
    public boolean clickStarted;
    public boolean clicked;

    public Hitbox(float width, float height) {
        this(-10000.0F, -10000.0F, width, height);
    }

    public Hitbox(float x, float y, float width, float height) {
        this.hovered = false;
        this.justHovered = false;
        this.clickStarted = false;
        this.clicked = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cX = x + width / 2.0F;
        this.cY = y + height / 2.0F;
    }

    public void update() {
        this.update(this.x, this.y);
        if (this.clickStarted && InputHelper.justReleasedClickLeft) {
            if (this.hovered) {
                this.clicked = true;
            }

            this.clickStarted = false;
        }

    }

    public void update(float x, float y) {
        if (!AbstractDungeon.isFadingOut) {
            this.x = x;
            this.y = y;
            if (this.justHovered) {
                this.justHovered = false;
            }

            if (!this.hovered) {
                this.hovered = (float)InputHelper.mX > x && (float)InputHelper.mX < x + this.width && (float)InputHelper.mY > y && (float)InputHelper.mY < y + this.height;
                if (this.hovered) {
                    this.justHovered = true;
                }
            } else {
                this.hovered = (float)InputHelper.mX > x && (float)InputHelper.mX < x + this.width && (float)InputHelper.mY > y && (float)InputHelper.mY < y + this.height;
            }

        }
    }

    public void encapsulatedUpdate(HitboxListener listener) {
        this.update();
        if (this.justHovered) {
            listener.hoverStarted(this);
        }

        if (this.hovered && InputHelper.justClickedLeft) {
            this.clickStarted = true;
            listener.startClicking(this);
        } else if (this.clicked || this.hovered && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.select.unpress();
            this.clicked = false;
            listener.clicked(this);
        }

    }

    public void unhover() {
        this.hovered = false;
        this.justHovered = false;
    }

    public void move(float cX, float cY) {
        this.cX = cX;
        this.cY = cY;
        this.x = cX - this.width / 2.0F;
        this.y = cY - this.height / 2.0F;
    }

    public void moveY(float cY) {
        this.cY = cY;
        this.y = cY - this.height / 2.0F;
    }

    public void moveX(float cX) {
        this.cX = cX;
        this.x = cX - this.width / 2.0F;
    }

    public void translate(float x, float y) {
        this.x = x;
        this.y = y;
        this.cX = x + this.width / 2.0F;
        this.cY = y + this.height / 2.0F;
    }

    public void resize(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public boolean intersects(Hitbox other) {
        return this.x < other.x + other.width && this.x + this.width > other.x && this.y < other.y + other.height && this.y + this.height > other.y;
    }

    public void render(SpriteBatch sb) {
        if (Settings.isDebug || Settings.isInfo) {
            if (this.clickStarted) {
                sb.setColor(Color.CHARTREUSE);
            } else {
                sb.setColor(Color.RED);
            }

            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, this.x, this.y, this.width, this.height);
        }

    }
}

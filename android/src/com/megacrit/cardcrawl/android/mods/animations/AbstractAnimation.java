package com.megacrit.cardcrawl.android.mods.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AbstractAnimation {

    public abstract AbstractAnimation.Type type();

    public void setFlip(boolean horizontal, boolean vertical) {
    }

    /** @deprecated */
    @Deprecated
    public void renderSprite(SpriteBatch batch) {
        AbstractPlayer player = AbstractDungeon.player;
        this.renderSprite(batch, player.drawX + player.animX, player.drawY + player.animY + AbstractDungeon.sceneOffsetY);
    }

    public void renderSprite(SpriteBatch batch, float x, float y) {
    }

    public void renderModel(ModelBatch batch, Environment env) {
    }

    public enum Type {
        NONE,
        SPRITE,
        MODEL;

        Type() {
        }
    }
}

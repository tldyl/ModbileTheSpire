package com.megacrit.cardcrawl.android.mods.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class CustomRelic extends AbstractRelic {
    public CustomRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, "", tier, sfx);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.setTexture(texture);
    }

    public CustomRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, "", tier, sfx);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outline.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.setTextureOutline(texture, outline);
    }

    public CustomRelic(String modId, String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
        this.img = AssetLoader.getTexture(modId, imgName);
        this.outlineImg = this.img;
    }

    public void setTexture(Texture t) {
        this.img = t;
        this.outlineImg = t;
    }

    public void setTextureOutline(Texture t, Texture o) {
        this.img = t;
        this.outlineImg = o;
    }

    public AbstractRelic makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw new RuntimeException("BaseMod failed to auto-generate makeCopy for relic: " + this.relicId);
        }
    }
}

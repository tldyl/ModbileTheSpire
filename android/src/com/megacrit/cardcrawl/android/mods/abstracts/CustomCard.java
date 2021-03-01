package com.megacrit.cardcrawl.android.mods.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class CustomCard extends AbstractCard {
    private String textureImg;
    private String modId;

    public static Texture getPortraitImage(CustomCard card) {
        return card.getPortraitImage();
    }

    public CustomCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, "status/beta", "status/beta", cost, rawDescription, type, color, rarity, target);
        this.textureImg = img;
        this.modId = BaseMod.getColorBundleMap().getOrDefault(this.color, new CardColorBundle()).modId;
        if (img != null) {
            this.loadCardImage(img);
            this.portraitImg = getPortraitImage();
        }
    }

    public void loadCardImage(String img) {
        Texture cardTexture;
        cardTexture = AssetLoader.getTexture(modId, img);
        if (cardTexture != null) {
            cardTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            int tw = cardTexture.getWidth();
            int th = cardTexture.getHeight();
            this.portrait = new TextureAtlas.AtlasRegion(cardTexture, 0, 0, tw, th);
        }
    }

    protected Texture getPortraitImage() {
        if (this.textureImg == null) {
            return null;
        } else {
            int endingIndex = this.textureImg.lastIndexOf(".");
            String newPath = this.textureImg.substring(0, endingIndex) + "_p" + this.textureImg.substring(endingIndex);
            System.out.println("Finding texture: " + newPath);

            Texture portraitTexture;
            try {
                portraitTexture = AssetLoader.getTexture(modId, newPath);
            } catch (Exception e) {
                portraitTexture = null;
            }
            return portraitTexture;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw new RuntimeException("BaseMod failed to auto-generate makeCopy for card: " + this.cardID);
        }
    }

    @Override
    public void unlock() {
        this.isLocked = false;
    }
}

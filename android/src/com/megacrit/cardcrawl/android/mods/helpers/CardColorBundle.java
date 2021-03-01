package com.megacrit.cardcrawl.android.mods.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class CardColorBundle {
    public AbstractCard.CardColor cardColor;
    public String modId;
    public Color bgColor; //背景颜色
    public Color cardBackColor; //卡牌背面颜色
    public Color frameColor;
    public Color frameOutlineColor;
    public Color descBoxColor;
    public Color trailVfxColor;
    public Color glowColor;
    public CardLibrary.LibraryType libraryType;
    public String attackBg; //攻击牌小图
    public String skillBg; //技能牌小图
    public String powerBg; //能力牌小图
    public String energyOrb; //能量图标缩略图
    public String attackBgPortrait; //攻击牌大图
    public String skillBgPortrait; //技能牌大图
    public String powerBgPortrait; //能力牌大图
    public String energyOrbPortrait; //能量图标大图
    public String cardEnergyOrb; //卡牌能量图标
    private int cardWidth = 512;
    private int cardHeight = 512;
    private int cardPortraitWidth = 1024;
    private int cardPortraitHeight = 1024;
    private int energyWidth = 22;
    private int energyHeight = 22;
    private int cardEnergyWidth = 512;
    private int cardEnergyHeight = 512;
    private int energyPortraitWidth = 164;
    private int energyPortraitHeight = 164;
    private TextureAtlas.AtlasRegion attackBgRegion;
    private TextureAtlas.AtlasRegion skillBgRegion;
    private TextureAtlas.AtlasRegion powerBgRegion;
    private TextureAtlas.AtlasRegion energyOrbRegion;
    private TextureAtlas.AtlasRegion attackBgPortraitRegion;
    private TextureAtlas.AtlasRegion skillBgPortraitRegion;
    private TextureAtlas.AtlasRegion powerBgPortraitRegion;
    private TextureAtlas.AtlasRegion energyOrbPortraitRegion;
    private TextureAtlas.AtlasRegion cardEnergyOrbRegion;

    public void loadRegion() {
        if (attackBg != null) this.attackBgRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, attackBg), 0, 0, cardWidth, cardHeight);
        if (skillBg != null) this.skillBgRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, skillBg), 0, 0, cardWidth, cardHeight);
        if (powerBg != null) this.powerBgRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, powerBg), 0, 0, cardWidth, cardHeight);
        if (energyOrb != null) this.energyOrbRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, energyOrb), 0, 0, energyWidth, energyHeight);
        if (attackBgPortrait != null) this.attackBgPortraitRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, attackBgPortrait), 0, 0, cardPortraitWidth, cardPortraitHeight);
        if (skillBgPortrait != null) this.skillBgPortraitRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, skillBgPortrait), 0, 0, cardPortraitWidth, cardPortraitHeight);
        if (powerBgPortrait != null) this.powerBgPortraitRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, powerBgPortrait), 0, 0, cardPortraitWidth, cardPortraitHeight);
        if (energyOrbPortrait != null) this.energyOrbPortraitRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, energyOrbPortrait), 0, 0, energyPortraitWidth, energyPortraitHeight);
        if (cardEnergyOrb != null) this.cardEnergyOrbRegion = new TextureAtlas.AtlasRegion(AssetLoader.getTexture(modId, cardEnergyOrb), 0, 0, cardEnergyWidth, cardEnergyHeight);
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public void setCardWidth(int cardWidth) {
        this.cardWidth = cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public void setCardHeight(int cardHeight) {
        this.cardHeight = cardHeight;
    }

    public int getCardPortraitWidth() {
        return cardPortraitWidth;
    }

    public void setCardPortraitWidth(int cardPortraitWidth) {
        this.cardPortraitWidth = cardPortraitWidth;
    }

    public int getCardPortraitHeight() {
        return cardPortraitHeight;
    }

    public void setCardPortraitHeight(int cardPortraitHeight) {
        this.cardPortraitHeight = cardPortraitHeight;
    }

    public int getEnergyWidth() {
        return energyWidth;
    }

    public void setEnergyWidth(int energyWidth) {
        this.energyWidth = energyWidth;
    }

    public int getEnergyHeight() {
        return energyHeight;
    }

    public void setEnergyHeight(int energyHeight) {
        this.energyHeight = energyHeight;
    }

    public int getCardEnergyWidth() {
        return cardEnergyWidth;
    }

    public void setCardEnergyWidth(int cardEnergyWidth) {
        this.cardEnergyWidth = cardEnergyWidth;
    }

    public int getCardEnergyHeight() {
        return cardEnergyHeight;
    }

    public void setCardEnergyHeight(int cardEnergyHeight) {
        this.cardEnergyHeight = cardEnergyHeight;
    }

    public int getEnergyPortraitWidth() {
        return energyPortraitWidth;
    }

    public void setEnergyPortraitWidth(int energyPortraitWidth) {
        this.energyPortraitWidth = energyPortraitWidth;
    }

    public int getEnergyPortraitHeight() {
        return energyPortraitHeight;
    }

    public void setEnergyPortraitHeight(int energyPortraitHeight) {
        this.energyPortraitHeight = energyPortraitHeight;
    }

    public TextureAtlas.AtlasRegion getAttackBgRegion() {
        return attackBgRegion;
    }

    public TextureAtlas.AtlasRegion getSkillBgRegion() {
        return skillBgRegion;
    }

    public TextureAtlas.AtlasRegion getPowerBgRegion() {
        return powerBgRegion;
    }

    public TextureAtlas.AtlasRegion getEnergyOrbRegion() {
        return energyOrbRegion;
    }

    public TextureAtlas.AtlasRegion getAttackBgPortraitRegion() {
        return attackBgPortraitRegion;
    }

    public TextureAtlas.AtlasRegion getSkillBgPortraitRegion() {
        return skillBgPortraitRegion;
    }

    public TextureAtlas.AtlasRegion getPowerBgPortraitRegion() {
        return powerBgPortraitRegion;
    }

    public TextureAtlas.AtlasRegion getEnergyOrbPortraitRegion() {
        return energyOrbPortraitRegion;
    }

    public TextureAtlas.AtlasRegion getCardEnergyOrbRegion() {
        return cardEnergyOrbRegion;
    }
}

package com.megacrit.cardcrawl.android.mods.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.animations.AbstractAnimation;
import com.megacrit.cardcrawl.android.mods.animations.G3DJAnimation;
import com.megacrit.cardcrawl.android.mods.animations.SpineAnimation;
import com.megacrit.cardcrawl.android.mods.helpers.CountModdedUnlockCards;
import com.megacrit.cardcrawl.android.mods.interfaces.ModelRenderSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomPlayer extends AbstractPlayer implements ModelRenderSubscriber {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(CustomPlayer.class);
    protected AbstractAnimation animation;
    protected EnergyOrbInterface energyOrb;
    protected Prefs prefs;
    protected CharStat charStat;
    private String modId;

    public CustomPlayer(String modId, String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, String model, String animation) {
        this(modId, name, playerClass, orbTextures, orbVfxPath, null, model, animation);
    }

    public CustomPlayer(String modId, String name, PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, String model, String animation) {
        this(modId, name, playerClass, energyOrbInterface, new G3DJAnimation(model, animation));
    }

    public CustomPlayer(String modId, String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, String model, String animation) {
        this(modId, name, playerClass, orbTextures, orbVfxPath, layerSpeeds, new G3DJAnimation(model, animation));
    }

    public CustomPlayer(String modId, String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, AbstractAnimation animation) {
        this(modId, name, playerClass, orbTextures, orbVfxPath, null, animation);
    }

    public CustomPlayer(String modId, String name, PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, AbstractAnimation animation) {
        this(modId, name, playerClass, new CustomEnergyOrb(modId, orbTextures, orbVfxPath, layerSpeeds), animation);
    }

    public CustomPlayer(String modId, String name, PlayerClass playerClass, EnergyOrbInterface energyOrbInterface, AbstractAnimation animation) {
        super(name, playerClass);
        this.modId = modId;
        this.energyOrb = energyOrbInterface;
        this.charStat = new CharStat(this);
        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        this.animation = animation;
        if (animation instanceof SpineAnimation) {
            SpineAnimation spine = (SpineAnimation)animation;
            this.loadAnimation(spine.atlasUrl, spine.skeletonUrl, spine.scale);
        }

        if (animation.type() != AbstractAnimation.Type.NONE) {
            this.atlas = new TextureAtlas();
        }

        if (animation.type() == AbstractAnimation.Type.MODEL) {
            BaseMod.subscribe(this);
        }
    }

    public void receiveModelRender(ModelBatch batch, Environment env) {
        if (this != AbstractDungeon.player) {
            BaseMod.unsubscribeLater(this);
        } else {
            this.animation.renderModel(batch, env);
        }

    }

    public void renderPlayerImage(SpriteBatch sb) {
        switch(this.animation.type()) {
            case NONE:
                super.renderPlayerImage(sb);
                break;
            case MODEL:
                BaseMod.publishAnimationRender(sb);
                break;
            case SPRITE:
                this.animation.setFlip(this.flipHorizontal, this.flipVertical);
                this.animation.renderSprite(sb, this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
        }
    }

    public String getAchievementKey() {
        return "MODDING";
    }

    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> tmpPool) {
        AbstractCard.CardColor color = this.getCardColor();
        CardLibrary.addCardsIntoPool(tmpPool, color);
        return tmpPool;
    }

    public String getLeaderboardCharacterName() {
        return null;
    }

    public Texture getEnergyImage() {
        if (this.energyOrb instanceof CustomEnergyOrb) {
            return ((CustomEnergyOrb)this.energyOrb).getEnergyImage();
        } else {
            throw new RuntimeException();
        }
    }

    public TextureAtlas.AtlasRegion getOrb() {
        return BaseMod.getColorBundleMap().get(this.getCardColor()).getCardEnergyOrbRegion();
    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        this.energyOrb.renderOrb(sb, enabled, current_x, current_y);
    }

    public void updateOrb(int energyCount) {
        this.energyOrb.updateOrb(energyCount);
    }

    public String getSaveFilePath() {
        return SaveAndContinue.getPlayerSavePath(this.chosenClass);
    }

    public Prefs getPrefs() {
        if (this.prefs == null) {
            logger.error("prefs need to be initialized first!");
        }

        return this.prefs;
    }

    public void loadPrefs() {
        if (this.prefs == null) {
            this.prefs = SaveHelper.getPrefs(this.chosenClass.name());
        }
    }

    public CharStat getCharStat() {
        return this.charStat;
    }

    public int getUnlockedCardCount() {
        return CountModdedUnlockCards.getUnlockedCardCount(this.chosenClass);
    }

    public int getSeenCardCount() {
        return BaseMod.getSeenCardCount(this.getCardColor());
    }

    public int getCardCount() {
        return BaseMod.getCardCount(this.getCardColor());
    }

    public boolean saveFileExists() {
        return SaveAndContinue.saveExistsAndNotCorrupted(this);
    }

    public String getWinStreakKey() {
        return "win_streak_" + this.chosenClass.name();
    }

    public String getLeaderboardWinStreakKey() {
        return this.chosenClass.name() + "_CONSECUTIVE_WINS";
    }

    public void renderStatScreen(SpriteBatch sb, float screenX, float screenY) {
        StatsScreen.renderHeader(sb, BaseMod.colorString(this.getLocalizedCharacterName(), "#" + this.getCardRenderColor().toString()), screenX, screenY);
        this.getCharStat().render(sb, screenX, screenY);
    }

    public Texture getCustomModeCharacterButtonImage() {
        if (BaseMod.getCustomModePlayerButton(this.chosenClass) != null) {
            return ImageMaster.loadImage(BaseMod.getCustomModePlayerButton(this.chosenClass));
        } else {
            Pixmap pixmap = new Pixmap(AssetLoader.getFileHandle(this.modId, BaseMod.getPlayerButton(this.chosenClass)));
            Pixmap small = new Pixmap(128, 128, pixmap.getFormat());
            small.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 20, 20, small.getWidth() - 40, small.getHeight() - 40);
            Texture texture = new Texture(small);
            pixmap.dispose();
            small.dispose();
            return texture;
        }
    }

    public CharacterStrings getCharacterString() {
        CharSelectInfo loadout = this.getLoadout();
        CharacterStrings characterStrings = new CharacterStrings();
        characterStrings.NAMES = new String[]{loadout.name};
        characterStrings.TEXT = new String[]{loadout.flavorText};
        return characterStrings;
    }

    public String getSensoryStoneText() {
        return null;
    }

    public void refreshCharStat() {
        this.charStat = new CharStat(this);
    }

    public void movePosition(float x, float y) {
        float dialogOffsetX = this.dialogX - this.drawX;
        float dialogOffsetY = this.dialogY - this.drawY;
        this.drawX = x;
        this.drawY = y;
        this.dialogX = this.drawX + dialogOffsetX;
        this.dialogY = this.drawY + dialogOffsetY;
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.refreshHitboxLocation();
    }

    @Override
    protected void updateEscapeAnimation() {
        if (this.escapeTimer != 0.0F) {
            if (this.flipHorizontal) {
                this.dialogX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
            } else {
                this.dialogX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
            }
        }

        super.updateEscapeAnimation();
    }

    public Texture getCutsceneBg() {
        return AssetLoader.getTexture(this.modId, BaseMod.cutsceneMap.get(chosenClass).getBgImgPath());
    }

    public List<CutscenePanel> getCutscenePanels() {
        return BaseMod.cutsceneMap.get(chosenClass).getPanelList();
    }

    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
    }

    public String getPortraitImageName() {
        return BaseMod.getPlayerPortrait(this.chosenClass);
    }
}

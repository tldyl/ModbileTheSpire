package com.megacrit.cardcrawl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SingleCardViewPopup {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public boolean isOpen = false;
    private CardGroup group;
    private AbstractCard card;
    private AbstractCard prevCard;
    private AbstractCard nextCard;
    private Texture portraitImg = null;
    private Hitbox nextHb;
    private Hitbox prevHb;
    private Hitbox cardHb;
    private float fadeTimer = 0.0F;
    private Color fadeColor;
    private static final float LINE_SPACING = 1.53F;
    private float current_x;
    private float current_y;
    private float drawScale;
    private float card_energy_w;
    private static final float DESC_OFFSET_Y2 = -12.0F;
    private static final Color CARD_TYPE_COLOR;
    private static final GlyphLayout gl;
    public static boolean isViewingUpgrade;
    public static boolean enableUpgradeToggle;
    private Hitbox upgradeHb;
    private Hitbox betaArtHb;
    private boolean viewBetaArt;

    public SingleCardViewPopup() {
        this.fadeColor = Color.BLACK.cpy();
        this.upgradeHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
        this.betaArtHb = null;
        this.viewBetaArt = false;
        this.prevHb = new Hitbox(200.0F * Settings.scale, 70.0F * Settings.scale);
        this.nextHb = new Hitbox(200.0F * Settings.scale, 70.0F * Settings.scale);
    }

    public void open(AbstractCard card, CardGroup group) {
        CardCrawlGame.isPopupOpen = true;
        this.prevCard = null;
        this.nextCard = null;
        this.prevHb = null;
        this.nextHb = null;

        for(int i = 0; i < group.size(); ++i) {
            if (group.group.get(i) == card) {
                if (i != 0) {
                    this.prevCard = (AbstractCard)group.group.get(i - 1);
                }

                if (i != group.size() - 1) {
                    this.nextCard = (AbstractCard)group.group.get(i + 1);
                }
                break;
            }
        }

        this.prevHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
        this.nextHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
        this.prevHb.move((float)Settings.WIDTH / 2.0F - 400.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F);
        this.nextHb.move((float)Settings.WIDTH / 2.0F + 400.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F);
        this.card_energy_w = 24.0F * Settings.scale;
        this.drawScale = 2.0F;
        this.cardHb = new Hitbox(550.0F * Settings.scale, 770.0F * Settings.scale);
        this.cardHb.move((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F);
        this.card = card.makeStatEquivalentCopy();
        this.loadPortraitImg();
        this.group = group;
        this.isOpen = true;
        this.fadeTimer = 0.25F;
        this.fadeColor.a = 0.0F;
        this.current_x = (float)Settings.WIDTH / 2.0F - 10.0F * Settings.scale;
        this.current_y = (float)Settings.HEIGHT / 2.0F - 300.0F * Settings.scale;
        if (this.canToggleBetaArt()) {
            if (this.allowUpgradePreview()) {
                this.betaArtHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
                this.betaArtHb.move((float)Settings.WIDTH / 2.0F + 270.0F * Settings.scale, 70.0F * Settings.scale);
                this.upgradeHb.move((float)Settings.WIDTH / 2.0F - 180.0F * Settings.scale, 70.0F * Settings.scale);
            } else {
                this.betaArtHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
                this.betaArtHb.move((float)Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
            }

            this.viewBetaArt = UnlockTracker.betaCardPref.getBoolean(card.cardID, false);
        } else {
            this.upgradeHb.move((float)Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
            this.betaArtHb = null;
        }

    }

    private boolean canToggleBetaArt() {
        if (UnlockTracker.isAchievementUnlocked("THE_ENDING")) {
            return true;
        } else {
            switch(this.card.color.name()) {
                case "RED":
                    return UnlockTracker.isAchievementUnlocked("RUBY_PLUS");
                case "GREEN":
                    return UnlockTracker.isAchievementUnlocked("EMERALD_PLUS");
                case "BLUE":
                    return UnlockTracker.isAchievementUnlocked("SAPPHIRE_PLUS");
                case "PURPLE":
                    return UnlockTracker.isAchievementUnlocked("AMETHYST_PLUS");
                default:
                    return false;
            }
        }
    }

    private void loadPortraitImg() {
        if (!Settings.PLAYTESTER_ART_MODE && !UnlockTracker.betaCardPref.getBoolean(this.card.cardID, false)) {
            this.portraitImg = ImageMaster.loadImage("images/1024Portraits/" + this.card.assetUrl + ".png");
            if (this.portraitImg == null) {
                this.portraitImg = ImageMaster.loadImage("images/1024PortraitsBeta/" + this.card.assetUrl + ".png");
            }
        } else {
            this.portraitImg = ImageMaster.loadImage("images/1024PortraitsBeta/" + this.card.assetUrl + ".png");
        }

    }

    public void open(AbstractCard card) {
        CardCrawlGame.isPopupOpen = true;
        this.prevCard = null;
        this.nextCard = null;
        this.prevHb = null;
        this.nextHb = null;
        this.card_energy_w = 24.0F * Settings.scale;
        this.drawScale = 2.0F;
        this.cardHb = new Hitbox(550.0F * Settings.scale, 770.0F * Settings.scale);
        this.cardHb.move((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F);
        this.card = card.makeStatEquivalentCopy();
        this.loadPortraitImg();
        this.group = null;
        this.isOpen = true;
        this.fadeTimer = 0.25F;
        this.fadeColor.a = 0.0F;
        this.current_x = (float)Settings.WIDTH / 2.0F - 10.0F * Settings.scale;
        this.current_y = (float)Settings.HEIGHT / 2.0F - 300.0F * Settings.scale;
        this.betaArtHb = null;
        if (this.canToggleBetaArt()) {
            this.betaArtHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
            this.betaArtHb.move((float)Settings.WIDTH / 2.0F + 270.0F * Settings.scale, 70.0F * Settings.scale);
            this.upgradeHb.move((float)Settings.WIDTH / 2.0F - 180.0F * Settings.scale, 70.0F * Settings.scale);
            this.viewBetaArt = UnlockTracker.betaCardPref.getBoolean(card.cardID, false);
        } else {
            this.upgradeHb.move((float)Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
        }

    }

    public void close() {
        isViewingUpgrade = false;
        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        this.isOpen = false;
        if (this.portraitImg != null) {
            this.portraitImg.dispose();
            this.portraitImg = null;
        }

    }

    public void update() {
        this.cardHb.update();
        this.updateArrows();
        this.updateInput();
        this.updateFade();
        if (this.allowUpgradePreview()) {
            this.updateUpgradePreview();
        }

        if (this.betaArtHb != null && this.canToggleBetaArt()) {
            this.updateBetaArtToggler();
        }

    }

    private void updateBetaArtToggler() {
        this.betaArtHb.update();
        if (this.betaArtHb.hovered && InputHelper.justClickedLeft) {
            this.betaArtHb.clickStarted = true;
        }

        if (this.betaArtHb.clicked || CInputActionSet.topPanel.isJustPressed()) {
            CInputActionSet.topPanel.unpress();
            this.betaArtHb.clicked = false;
            this.viewBetaArt = !this.viewBetaArt;
            UnlockTracker.betaCardPref.putBoolean(this.card.cardID, this.viewBetaArt);
            UnlockTracker.betaCardPref.flush();
            if (this.portraitImg != null) {
                this.portraitImg.dispose();
            }

            this.loadPortraitImg();
        }

    }

    private void updateUpgradePreview() {
        this.upgradeHb.update();
        if (this.upgradeHb.hovered && InputHelper.justClickedLeft) {
            this.upgradeHb.clickStarted = true;
        }

        if (this.upgradeHb.clicked || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            this.upgradeHb.clicked = false;
            isViewingUpgrade = !isViewingUpgrade;
        }

    }

    private boolean allowUpgradePreview() {
        return enableUpgradeToggle && this.card.color != CardColor.CURSE && this.card.type != CardType.STATUS;
    }

    private void updateArrows() {
        if (this.prevCard != null) {
            this.prevHb.update();
            if (this.prevHb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.prevHb.clicked || this.prevCard != null && CInputActionSet.pageLeftViewDeck.isJustPressed()) {
                CInputActionSet.pageLeftViewDeck.unpress();
                this.openPrev();
            }
        }

        if (this.nextCard != null) {
            this.nextHb.update();
            if (this.nextHb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.nextHb.clicked || this.nextCard != null && CInputActionSet.pageRightViewExhaust.isJustPressed()) {
                CInputActionSet.pageRightViewExhaust.unpress();
                this.openNext();
            }
        }

    }

    private void updateInput() {
        if (InputHelper.justClickedLeft) {
            if (this.prevCard != null && this.prevHb.hovered) {
                this.prevHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }

            if (this.nextCard != null && this.nextHb.hovered) {
                this.nextHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }
        }

        if (InputHelper.justClickedLeft) {
            if (!this.cardHb.hovered && !this.upgradeHb.hovered && (this.betaArtHb == null || !this.betaArtHb.hovered)) {
                this.close();
                InputHelper.justClickedLeft = false;
                FontHelper.ClearSCPFontTextures();
            }
        } else if (InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed()) {
            CInputActionSet.cancel.unpress();
            InputHelper.pressedEscape = false;
            this.close();
            FontHelper.ClearSCPFontTextures();
        }

        if (this.prevCard != null && InputActionSet.left.isJustPressed()) {
            this.openPrev();
        } else if (this.nextCard != null && InputActionSet.right.isJustPressed()) {
            this.openNext();
        }

    }

    private void openPrev() {
        boolean tmp = isViewingUpgrade;
        this.close();
        this.open(this.prevCard, this.group);
        isViewingUpgrade = tmp;
        this.fadeTimer = 0.0F;
        this.fadeColor.a = 0.9F;
    }

    private void openNext() {
        boolean tmp = isViewingUpgrade;
        this.close();
        this.open(this.nextCard, this.group);
        isViewingUpgrade = tmp;
        this.fadeTimer = 0.0F;
        this.fadeColor.a = 0.9F;
    }

    private void updateFade() {
        this.fadeTimer -= Gdx.graphics.getDeltaTime();
        if (this.fadeTimer < 0.0F) {
            this.fadeTimer = 0.0F;
        }

        this.fadeColor.a = Interpolation.pow2In.apply(0.9F, 0.0F, this.fadeTimer * 4.0F);
    }

    public void render(SpriteBatch sb) {
        AbstractCard copy = null;
        if (isViewingUpgrade) {
            copy = this.card.makeStatEquivalentCopy();
            this.card.upgrade();
            this.card.displayUpgrades();
        }

        sb.setColor(this.fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setColor(Color.WHITE);
        this.renderCardBack(sb);
        this.renderPortrait(sb);
        this.renderFrame(sb);
        this.renderCardBanner(sb);
        this.renderCardTypeText(sb);
        if (Settings.lineBreakViaCharacter) {
            this.renderDescriptionCN(sb);
        } else {
            this.renderDescription(sb);
        }

        this.renderTitle(sb);
        this.renderCost(sb);
        this.renderArrows(sb);
        this.renderTips(sb);
        this.cardHb.render(sb);
        if (this.nextHb != null) {
            this.nextHb.render(sb);
        }

        if (this.prevHb != null) {
            this.prevHb.render(sb);
        }

        FontHelper.cardTitleFont.getData().setScale(1.0F);
        if (this.canToggleBetaArt()) {
            this.renderBetaArtToggle(sb);
        }

        if (this.allowUpgradePreview()) {
            this.renderUpgradeViewToggle(sb);
            if (Settings.isControllerMode) {
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.upgradeHb.cX - 132.0F * Settings.scale - 32.0F, -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }
        }

        if (this.betaArtHb != null && Settings.isControllerMode) {
            sb.draw(CInputActionSet.topPanel.getKeyImg(), this.betaArtHb.cX - 132.0F * Settings.scale - 32.0F, -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        if (copy != null) {
            this.card = copy;
        }

    }

    public void renderCardBack(SpriteBatch sb) {
        AtlasRegion tmpImg = this.getCardBackAtlasRegion();
        if (tmpImg != null) {
            this.renderHelper(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, tmpImg);
        } else {
            Texture img = this.getCardBackImg();
            if (img != null) {
                sb.draw(img, (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
            }
        }

    }

    private Texture getCardBackImg() {
        switch(this.card.type) {
            case ATTACK:
                switch(this.card.color.name()) {
                }
            case POWER:
                switch(this.card.color.name()) {
                }
            default:
                switch(this.card.color.name()) {
                    default:
                        return null;
                }
        }
    }

    private AtlasRegion getCardBackAtlasRegion() {
        switch(this.card.type) {
            case ATTACK:
                switch(this.card.color.name()) {
                    case "RED":
                        return ImageMaster.CARD_ATTACK_BG_RED_L;
                    case "GREEN":
                        return ImageMaster.CARD_ATTACK_BG_GREEN_L;
                    case "BLUE":
                        return ImageMaster.CARD_ATTACK_BG_BLUE_L;
                    case "PURPLE":
                        return ImageMaster.CARD_ATTACK_BG_PURPLE_L;
                    case "COLORLESS":
                        return ImageMaster.CARD_ATTACK_BG_GRAY_L;
                }
                CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                if (bundle != null) {
                    AtlasRegion region = bundle.getAttackBgPortraitRegion();
                    return region == null ? ImageMaster.CARD_ATTACK_BG_GRAY_L : region;
                }
            case POWER:
                switch(this.card.color.name()) {
                    case "RED":
                        return ImageMaster.CARD_POWER_BG_RED_L;
                    case "GREEN":
                        return ImageMaster.CARD_POWER_BG_GREEN_L;
                    case "BLUE":
                        return ImageMaster.CARD_POWER_BG_BLUE_L;
                    case "PURPLE":
                        return ImageMaster.CARD_POWER_BG_PURPLE_L;
                    case "COLORLESS":
                        return ImageMaster.CARD_POWER_BG_GRAY_L;
                }
                bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                if (bundle != null) {
                    AtlasRegion region = bundle.getPowerBgPortraitRegion();
                    return region == null ? ImageMaster.CARD_POWER_BG_GRAY_L : region;
                }
            default:
                switch(this.card.color.name()) {
                    case "RED":
                        return ImageMaster.CARD_SKILL_BG_RED_L;
                    case "GREEN":
                        return ImageMaster.CARD_SKILL_BG_GREEN_L;
                    case "BLUE":
                        return ImageMaster.CARD_SKILL_BG_BLUE_L;
                    case "PURPLE":
                        return ImageMaster.CARD_SKILL_BG_PURPLE_L;
                    case "COLORLESS":
                        return ImageMaster.CARD_SKILL_BG_GRAY_L;
                    case "CURSE":
                        return ImageMaster.CARD_SKILL_BG_BLACK_L;
                }
                bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                if (bundle != null) {
                    AtlasRegion region = bundle.getSkillBgPortraitRegion();
                    return region == null ? ImageMaster.CARD_SKILL_BG_GRAY_L : region;
                }
                return ImageMaster.CARD_SKILL_BG_RED_L;
        }
    }

    private void renderPortrait(SpriteBatch sb) {
        AtlasRegion img;
        if (this.card.isLocked) {
            switch(this.card.type) {
                case ATTACK:
                    img = ImageMaster.CARD_LOCKED_ATTACK_L;
                    break;
                case POWER:
                    img = ImageMaster.CARD_LOCKED_POWER_L;
                    break;
                case SKILL:
                default:
                    img = ImageMaster.CARD_LOCKED_SKILL_L;
            }
            this.renderHelper(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 136.0F * Settings.scale, img);
        } else if (this.card.portraitImg != null) {
            sb.draw(this.card.portraitImg, (float)Settings.WIDTH / 2.0F - 250.0F, (float)Settings.HEIGHT / 2.0F - 190.0F + 136.0F * Settings.scale, 250.0F, 190.0F, 500.0F, 380.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 500, 380, false, false);
        } else if (this.card.jokePortrait != null && this.viewBetaArt) {
            sb.draw(this.card.jokePortrait, (float)Settings.WIDTH / 2.0F - (float)this.card.portrait.packedWidth / 2.0F, (float)Settings.HEIGHT / 2.0F - (float)this.card.portrait.packedHeight / 2.0F + 140.0F * Settings.scale, (float)this.card.portrait.packedWidth / 2.0F, (float)this.card.portrait.packedHeight / 2.0F, (float)this.card.portrait.packedWidth, (float)this.card.portrait.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, 0.0F);
        } else if (this.portraitImg != null) {
            sb.draw(this.portraitImg, (float)Settings.WIDTH / 2.0F - 250.0F, (float)Settings.HEIGHT / 2.0F - 190.0F + 136.0F * Settings.scale, 250.0F, 190.0F, 500.0F, 380.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 500, 380, false, false);
        }
    }

    private void renderFrame(SpriteBatch sb) {
        AtlasRegion tmpImg;
        float tOffset;
        float tWidth;
        tmpImg = null;
        tOffset = 0.0F;
        tWidth = 0.0F;
        label39:
        switch(this.card.type) {
            case ATTACK:
                tWidth = AbstractCard.typeWidthAttack;
                tOffset = AbstractCard.typeOffsetAttack;
                switch(this.card.rarity) {
                    case UNCOMMON:
                        tmpImg = ImageMaster.CARD_FRAME_ATTACK_UNCOMMON_L;
                        break label39;
                    case RARE:
                        tmpImg = ImageMaster.CARD_FRAME_ATTACK_RARE_L;
                        break label39;
                    case COMMON:
                    default:
                        tmpImg = ImageMaster.CARD_FRAME_ATTACK_COMMON_L;
                        break label39;
                }
            case POWER:
                tWidth = AbstractCard.typeWidthPower;
                tOffset = AbstractCard.typeOffsetPower;
                switch(this.card.rarity) {
                    case UNCOMMON:
                        tmpImg = ImageMaster.CARD_FRAME_POWER_UNCOMMON_L;
                        break label39;
                    case RARE:
                        tmpImg = ImageMaster.CARD_FRAME_POWER_RARE_L;
                        break label39;
                    case COMMON:
                    default:
                        tmpImg = ImageMaster.CARD_FRAME_POWER_COMMON_L;
                        break label39;
                }
            case SKILL:
                tWidth = AbstractCard.typeWidthSkill;
                tOffset = AbstractCard.typeOffsetSkill;
                switch(this.card.rarity) {
                    case UNCOMMON:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L;
                        break label39;
                    case RARE:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_RARE_L;
                        break label39;
                    case COMMON:
                    default:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
                        break label39;
                }
            case CURSE:
                tWidth = AbstractCard.typeWidthCurse;
                tOffset = AbstractCard.typeOffsetCurse;
                switch(this.card.rarity) {
                    case UNCOMMON:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L;
                        break label39;
                    case RARE:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_RARE_L;
                        break label39;
                    case COMMON:
                    default:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
                        break label39;
                }
            case STATUS:
                tWidth = AbstractCard.typeWidthStatus;
                tOffset = AbstractCard.typeOffsetStatus;
                switch(this.card.rarity) {
                    case UNCOMMON:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L;
                        break;
                    case RARE:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_RARE_L;
                        break;
                    case COMMON:
                    default:
                        tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
                }
        }

        if (tmpImg != null) {
            this.renderHelper(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, tmpImg);
        } else {
            Texture img = this.getFrameImg();
            tWidth = AbstractCard.typeWidthSkill;
            tOffset = AbstractCard.typeOffsetSkill;
            if (img != null) {
                sb.draw(img, (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
            } else {
                this.renderHelper(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, ImageMaster.CARD_FRAME_SKILL_COMMON_L);
            }
        }

        this.renderDynamicFrame(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, tOffset, tWidth);
    }

    private Texture getFrameImg() {
        switch(this.card.rarity) {
            default:
                return null;
        }
    }

    private void renderDynamicFrame(SpriteBatch sb, float x, float y, float typeOffset, float typeWidth) {
        if (typeWidth > 1.1F) {
            switch(this.card.rarity) {
                case UNCOMMON:
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_MID_L, 0.0F, typeWidth);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_LEFT_L, -typeOffset, 1.0F);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_RIGHT_L, typeOffset, 1.0F);
                    break;
                case RARE:
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_MID_L, 0.0F, typeWidth);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_LEFT_L, -typeOffset, 1.0F);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_RIGHT_L, typeOffset, 1.0F);
                    break;
                case COMMON:
                case BASIC:
                case CURSE:
                case SPECIAL:
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_MID_L, 0.0F, typeWidth);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_LEFT_L, -typeOffset, 1.0F);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_RIGHT_L, typeOffset, 1.0F);
            }

        }
    }

    private void dynamicFrameRenderHelper(SpriteBatch sb, AtlasRegion img, float xOffset, float xScale) {
        sb.draw(img, (float)Settings.WIDTH / 2.0F + img.offsetX - (float)img.originalWidth / 2.0F + xOffset * this.drawScale, (float)Settings.HEIGHT / 2.0F + img.offsetY - (float)img.originalHeight / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalHeight / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, Settings.scale * xScale, Settings.scale, 0.0F);
    }

    private void renderCardBanner(SpriteBatch sb) {
        AtlasRegion tmpImg = null;
        switch(this.card.rarity) {
            case UNCOMMON:
                tmpImg = ImageMaster.CARD_BANNER_UNCOMMON_L;
                break;
            case RARE:
                tmpImg = ImageMaster.CARD_BANNER_RARE_L;
                break;
            case COMMON:
                tmpImg = ImageMaster.CARD_BANNER_COMMON_L;
        }

        if (tmpImg != null) {
            this.renderHelper(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, tmpImg);
        } else {
            Texture img = this.getBannerImg();
            if (img != null) {
                sb.draw(img, (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
            } else {
                tmpImg = ImageMaster.CARD_BANNER_COMMON_L;
                this.renderHelper(sb, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, tmpImg);
            }
        }

    }

    private Texture getBannerImg() {
        switch(this.card.rarity) {
            default:
                return null;
        }
    }

    private String getDynamicValue(char key) { //TODO
        switch(key) {
            case 'B':
                if (this.card.isBlockModified) {
                    if (this.card.block >= this.card.baseBlock) {
                        return "[#7fff00]" + Integer.toString(this.card.block) + "[]";
                    }

                    return "[#ff6563]" + Integer.toString(this.card.block) + "[]";
                }

                return Integer.toString(this.card.baseBlock);
            case 'D':
                if (this.card.isDamageModified) {
                    if (this.card.damage >= this.card.baseDamage) {
                        return "[#7fff00]" + Integer.toString(this.card.damage) + "[]";
                    }

                    return "[#ff6563]" + Integer.toString(this.card.damage) + "[]";
                }

                return Integer.toString(this.card.baseDamage);
            case 'M':
                if (this.card.isMagicNumberModified) {
                    if (this.card.magicNumber >= this.card.baseMagicNumber) {
                        return "[#7fff00]" + Integer.toString(this.card.magicNumber) + "[]";
                    }

                    return "[#ff6563]" + Integer.toString(this.card.magicNumber) + "[]";
                }

                return Integer.toString(this.card.baseMagicNumber);
            default:
                return Integer.toString(-99);
        }
    }

    private void renderDescriptionCN(SpriteBatch sb) {
        if (!this.card.isLocked && this.card.isSeen) {
            BitmapFont font = FontHelper.SCP_cardDescFont;
            float draw_y = this.current_y + 100.0F * Settings.scale;
            draw_y += (float)this.card.description.size() * font.getCapHeight() * 0.775F - font.getCapHeight() * 0.375F;
            float spacing = 1.53F * -font.getCapHeight() / Settings.scale / this.drawScale;
            GlyphLayout gl = new GlyphLayout();

            for(int i = 0; i < this.card.description.size(); ++i) {
                float start_x;
                if (Settings.leftAlignCards) {
                    start_x = this.current_x - 214.0F * Settings.scale;
                } else {
                    start_x = this.current_x - (this.card.description.get(i)).width * this.drawScale / 2.0F - 20.0F * Settings.scale;
                }

                String[] var8 = (this.card.description.get(i)).getCachedTokenizedTextCN();
                int var9 = var8.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    String tmp = var8[var10];
                    String updateTmp = null;

                    int j;
                    for(j = 0; j < tmp.length(); ++j) {
                        if (tmp.charAt(j) == 'D' || tmp.charAt(j) == 'B' && !tmp.contains("[B]") || tmp.charAt(j) == 'M') {
                            updateTmp = tmp.substring(0, j);
                            updateTmp = updateTmp + this.getDynamicValue(tmp.charAt(j));
                            updateTmp = updateTmp + tmp.substring(j + 1);
                            break;
                        }
                    }

                    if (updateTmp != null) {
                        tmp = updateTmp;
                    }

                    for(j = 0; j < tmp.length(); ++j) {
                        if (tmp.charAt(j) == 'D' || tmp.charAt(j) == 'B' && !tmp.contains("[B]") || tmp.charAt(j) == 'M') {
                            updateTmp = tmp.substring(0, j);
                            updateTmp = updateTmp + this.getDynamicValue(tmp.charAt(j));
                            updateTmp = updateTmp + tmp.substring(j + 1);
                            break;
                        }
                    }

                    if (updateTmp != null) {
                        tmp = updateTmp;
                    }

                    if (!tmp.isEmpty() && tmp.charAt(0) == '*') {
                        tmp = tmp.substring(1);
                        String punctuation = "";
                        if (tmp.length() > 1 && tmp.charAt(tmp.length() - 2) != '+' && !Character.isLetter(tmp.charAt(tmp.length() - 2))) {
                            punctuation = punctuation + tmp.charAt(tmp.length() - 2);
                            tmp = tmp.substring(0, tmp.length() - 2);
                            punctuation = punctuation + ' ';
                        }

                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.GOLD_COLOR);
                        start_x = (float)Math.round(start_x + gl.width);
                        gl.setText(font, punctuation);
                        FontHelper.renderRotatedText(sb, font, punctuation, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        gl.setText(font, punctuation);
                        start_x += gl.width;
                    } else if (tmp.equals("[R]")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[G]")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[B]")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[W]")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[E]")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                        AtlasRegion region = null;
                        if (bundle != null) {
                            region = bundle.getEnergyOrbRegion();
                        }
                        this.renderSmallEnergy(sb, region == null ? AbstractCard.orb_red : region, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else {
                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        start_x += gl.width;
                    }
                }
            }

            font.getData().setScale(1.0F);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.largeCardFont, "? ? ?", (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F - 195.0F * Settings.scale, Settings.CREAM_COLOR);
        }
    }

    private void renderDescription(SpriteBatch sb) {
        if (!this.card.isLocked && this.card.isSeen) {
            BitmapFont font = FontHelper.SCP_cardDescFont;
            float draw_y = this.current_y + 100.0F * Settings.scale;
            draw_y += (float)this.card.description.size() * font.getCapHeight() * 0.775F - font.getCapHeight() * 0.375F;
            float spacing = 1.53F * -font.getCapHeight() / Settings.scale / this.drawScale;
            GlyphLayout gl = new GlyphLayout();

            for(int i = 0; i < this.card.description.size(); ++i) {
                float start_x = this.current_x - (this.card.description.get(i)).width * this.drawScale / 2.0F;
                String[] var8 = (this.card.description.get(i)).getCachedTokenizedText();
                int var9 = var8.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    String tmp = var8[var10];
                    if (tmp.charAt(0) == '*') {
                        tmp = tmp.substring(1);
                        String punctuation = "";
                        if (tmp.length() > 1 && tmp.charAt(tmp.length() - 2) != '+' && !Character.isLetter(tmp.charAt(tmp.length() - 2))) {
                            punctuation = punctuation + tmp.charAt(tmp.length() - 2);
                            tmp = tmp.substring(0, tmp.length() - 2);
                            punctuation = punctuation + ' ';
                        }

                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.GOLD_COLOR);
                        start_x = (float)Math.round(start_x + gl.width);
                        gl.setText(font, punctuation);
                        FontHelper.renderRotatedText(sb, font, punctuation, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        gl.setText(font, punctuation);
                        start_x += gl.width;
                    } else if (tmp.charAt(0) == '!') {
                        if (tmp.length() == 4) {
                            start_x += this.renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, (Character)null);
                        } else if (tmp.length() == 5) {
                            start_x += this.renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, tmp.charAt(3));
                        }
                    } else if (tmp.equals("[R] ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[R]. ")) {
                        gl.width = this.card_energy_w * this.drawScale / Settings.scale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + this.card_energy_w * this.drawScale, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        start_x += gl.width;
                        gl.setText(font, LocalizedStrings.PERIOD);
                        start_x += gl.width;
                    } else if (tmp.equals("[G] ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[G]. ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + this.card_energy_w * this.drawScale, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        start_x += gl.width;
                    } else if (tmp.equals("[B] ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[B]. ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + this.card_energy_w * this.drawScale, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        start_x += gl.width;
                    } else if (tmp.equals("[W] ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[W]. ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        this.renderSmallEnergy(sb, AbstractCard.orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + this.card_energy_w * this.drawScale, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        start_x += gl.width;
                    } else if (tmp.equals("[E] ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                        this.renderSmallEnergy(sb, bundle.getEnergyOrbRegion() == null ? AbstractCard.orb_red : bundle.getEnergyOrbRegion(), (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                    } else if (tmp.equals("[E]. ")) {
                        gl.width = this.card_energy_w * this.drawScale;
                        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                        this.renderSmallEnergy(sb, bundle.getEnergyOrbRegion() == null ? AbstractCard.orb_red : bundle.getEnergyOrbRegion(), (start_x - this.current_x) / Settings.scale / this.drawScale, -87.0F - (((float)this.card.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + this.card_energy_w * this.drawScale, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                    } else {
                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
                        start_x += gl.width;
                    }
                }
            }

            font.getData().setScale(1.0F);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.largeCardFont, "? ? ?", (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F - 195.0F * Settings.scale, Settings.CREAM_COLOR);
        }
    }

    private void renderSmallEnergy(SpriteBatch sb, AtlasRegion region, float x, float y) {
        sb.setColor(Color.WHITE);
        sb.draw(region.getTexture(), this.current_x + x * Settings.scale * this.drawScale + region.offsetX * Settings.scale - 4.0F * Settings.scale, this.current_y + y * Settings.scale * this.drawScale + 280.0F * Settings.scale, 0.0F, 0.0F, (float)region.packedWidth, (float)region.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

    private void renderCardTypeText(SpriteBatch sb) {
        String label = "";
        switch(this.card.type) {
            case ATTACK:
                label = TEXT[0];
                break;
            case POWER:
                label = TEXT[2];
                break;
            case SKILL:
                label = TEXT[1];
                break;
            case CURSE:
                label = TEXT[3];
                break;
            case STATUS:
                label = TEXT[7];
        }

        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, label, (float)Settings.WIDTH / 2.0F + 3.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F - 40.0F * Settings.scale, CARD_TYPE_COLOR);
    }

    private float renderDynamicVariable(char key, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character end) {
        //TODO
        StringBuilder stringBuilder = new StringBuilder();
        Color c = null;
        int num = 0;
        switch(key) {
            case 'B':
                num = this.card.baseBlock;
                if (this.card.upgradedBlock) {
                    c = Settings.GREEN_TEXT_COLOR;
                } else {
                    c = Settings.CREAM_COLOR;
                }
                break;
            case 'D':
                num = this.card.baseDamage;
                if (this.card.upgradedDamage) {
                    c = Settings.GREEN_TEXT_COLOR;
                } else {
                    c = Settings.CREAM_COLOR;
                }
                break;
            case 'M':
                num = this.card.baseMagicNumber;
                if (this.card.upgradedMagicNumber) {
                    c = Settings.GREEN_TEXT_COLOR;
                } else {
                    c = Settings.CREAM_COLOR;
                }
        }

        stringBuilder.append(Integer.toString(num));
        gl.setText(font, stringBuilder.toString());
        FontHelper.renderRotatedText(sb, font, stringBuilder.toString(), this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, c);
        if (end != null) {
            FontHelper.renderRotatedText(sb, font, Character.toString(end), this.current_x, this.current_y, start_x - this.current_x + gl.width + 10.0F * Settings.scale, (float)i * 1.53F * -font.getCapHeight() + draw_y - this.current_y + -12.0F, 0.0F, true, Settings.CREAM_COLOR);
        }

        stringBuilder.append(' ');
        gl.setText(font, stringBuilder.toString());
        return gl.width;
    }

    private void renderTitle(SpriteBatch sb) {
        if (this.card.isLocked) {
            FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, TEXT[4], (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, Settings.CREAM_COLOR);
        } else if (this.card.isSeen) {
            if (isViewingUpgrade && !this.allowUpgradePreview()) {
                FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, this.card.name, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, Settings.GREEN_TEXT_COLOR);
            } else {
                FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, this.card.name, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, Settings.CREAM_COLOR);
            }
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, TEXT[5], (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, Settings.CREAM_COLOR);
        }

    }

    private void renderCost(SpriteBatch sb) {
        if (!this.card.isLocked && this.card.isSeen) {
            AtlasRegion tmpImg;
            if (this.card.cost > -2) {
                switch(this.card.color.name()) {
                    case "RED":
                        tmpImg = ImageMaster.CARD_RED_ORB_L;
                        break;
                    case "GREEN":
                        tmpImg = ImageMaster.CARD_GREEN_ORB_L;
                        break;
                    case "BLUE":
                        tmpImg = ImageMaster.CARD_BLUE_ORB_L;
                        break;
                    case "PURPLE":
                        tmpImg = ImageMaster.CARD_PURPLE_ORB_L;
                        break;
                    case "COLORLESS":
                    default:
                        tmpImg = ImageMaster.CARD_GRAY_ORB_L;
                }
                CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.card.color, null);
                if (bundle != null) {
                    AtlasRegion region = bundle.getEnergyOrbPortraitRegion();
                    if (region != null) {
                        tmpImg = region;
                    }
                }
                if (tmpImg != null) {
                    this.renderHelper(sb, (float)Settings.WIDTH / 2.0F - 270.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 380.0F * Settings.scale, tmpImg);
                }
            }

            Color c;
            if (this.card.isCostModified) {
                c = Settings.GREEN_TEXT_COLOR;
            } else {
                c = Settings.CREAM_COLOR;
            }

            switch(this.card.cost) {
                case -2:
                    break;
                case -1:
                    FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, "X", (float)Settings.WIDTH / 2.0F - 292.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                    break;
                case 0:
                default:
                    FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(this.card.cost), (float)Settings.WIDTH / 2.0F - 292.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                    break;
                case 1:
                    FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(this.card.cost), (float)Settings.WIDTH / 2.0F - 284.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
            }

        }
    }

    private void renderHelper(SpriteBatch sb, float x, float y, AtlasRegion img) {
        if (img != null) {
            sb.draw(img, x + img.offsetX - (float)img.originalWidth / 2.0F, y + img.offsetY - (float)img.originalHeight / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalHeight / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, Settings.scale, Settings.scale, 0.0F);
        }
    }

    private void renderArrows(SpriteBatch sb) {
        if (this.prevCard != null) {
            sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            if (Settings.isControllerMode) {
                sb.draw(CInputActionSet.pageLeftViewDeck.getKeyImg(), this.prevHb.cX - 32.0F + 0.0F * Settings.scale, this.prevHb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            if (this.prevHb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                sb.setColor(Color.WHITE);
                sb.setBlendFunction(770, 771);
            }
        }

        if (this.nextCard != null) {
            sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
            if (Settings.isControllerMode) {
                sb.draw(CInputActionSet.pageRightViewExhaust.getKeyImg(), this.nextHb.cX - 32.0F + 0.0F * Settings.scale, this.nextHb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            if (this.nextHb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
                sb.setColor(Color.WHITE);
                sb.setBlendFunction(770, 771);
            }
        }

    }

    private void renderBetaArtToggle(SpriteBatch sb) {
        if (this.betaArtHb != null) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.CHECKBOX, this.betaArtHb.cX - 80.0F * Settings.scale - 32.0F, this.betaArtHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            if (this.betaArtHb.hovered) {
                FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[14], this.betaArtHb.cX - 45.0F * Settings.scale, this.betaArtHb.cY + 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);
            } else {
                FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[14], this.betaArtHb.cX - 45.0F * Settings.scale, this.betaArtHb.cY + 10.0F * Settings.scale, Settings.GOLD_COLOR);
            }

            if (this.viewBetaArt) {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.TICK, this.betaArtHb.cX - 80.0F * Settings.scale - 32.0F, this.betaArtHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            this.betaArtHb.render(sb);
        }
    }

    private void renderUpgradeViewToggle(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.CHECKBOX, this.upgradeHb.cX - 80.0F * Settings.scale - 32.0F, this.upgradeHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        if (this.upgradeHb.hovered) {
            FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[6], this.upgradeHb.cX - 45.0F * Settings.scale, this.upgradeHb.cY + 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);
        } else {
            FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[6], this.upgradeHb.cX - 45.0F * Settings.scale, this.upgradeHb.cY + 10.0F * Settings.scale, Settings.GOLD_COLOR);
        }

        if (isViewingUpgrade) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TICK, this.upgradeHb.cX - 80.0F * Settings.scale - 32.0F, this.upgradeHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        this.upgradeHb.render(sb);
    }

    private void renderTips(SpriteBatch sb) {
        ArrayList<PowerTip> t = new ArrayList<>();
        if (this.card.isLocked) {
            t.add(new PowerTip(TEXT[4], GameDictionary.keywords.get(TEXT[4].toLowerCase())));
        } else if (!this.card.isSeen) {
            t.add(new PowerTip(TEXT[5], GameDictionary.keywords.get(TEXT[5].toLowerCase())));
        } else {

            for (String s : this.card.keywords) {
                if (!s.equals("[R]") && !s.equals("[G]") && !s.equals("[B]") && !s.equals("[W]")) {
                    t.add(new PowerTip(TipHelper.capitalize(s), GameDictionary.keywords.get(s)));
                }
            }
        }

        if (!t.isEmpty()) {
            TipHelper.queuePowerTips((float)Settings.WIDTH / 2.0F + 340.0F * Settings.scale, 420.0F * Settings.scale, t);
        }

        if (this.card.cardsToPreview != null) {
            this.card.renderCardPreviewInSingleView(sb);
        }

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
        TEXT = uiStrings.TEXT;
        CARD_TYPE_COLOR = new Color(0.35F, 0.35F, 0.35F, 1.0F);
        gl = new GlyphLayout();
        isViewingUpgrade = false;
        enableUpgradeToggle = true;
    }
}


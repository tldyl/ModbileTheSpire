package com.megacrit.cardcrawl.screens.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.CurScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen.PanelScreen;

public class MenuButton {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(MenuButton.class);
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public MenuButton.ClickResult result;
    private String label;
    public Hitbox hb;
    private Color tint;
    private Color highlightColor;
    private int index;
    private boolean hidden;
    private float x;
    private float targetX;
    public static final float FONT_X;
    public static final float START_Y;
    public static final float SPACE_Y;
    public static final float FONT_OFFSET_Y;
    private boolean confirmation;
    private static Texture highlightImg;

    public MenuButton(MenuButton.ClickResult r, int index) {
        this.tint = Color.WHITE.cpy();
        this.highlightColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.hidden = false;
        this.x = 0.0F;
        this.targetX = 0.0F;
        this.confirmation = false;
        if (highlightImg == null) {
            highlightImg = ImageMaster.loadImage("images/ui/mainMenu/menu_option_highlight.png");
        }

        this.result = r;
        this.index = index;
        this.setLabel();
        if (!Settings.isTouchScreen && !Settings.isMobile) {
            this.hb = new Hitbox(FontHelper.getSmartWidth(FontHelper.buttonLabelFont, this.label, 9999.0F, 1.0F) + 100.0F * Settings.scale, SPACE_Y);
            this.hb.move(this.hb.width / 2.0F + 75.0F * Settings.scale, START_Y + (float)index * SPACE_Y);
        } else {
            this.hb = new Hitbox(FontHelper.getSmartWidth(FontHelper.losePowerFont, this.label, 9999.0F, 1.0F) * 1.25F + 100.0F * Settings.scale, SPACE_Y * 2.0F);
            this.hb.move(this.hb.width / 2.0F + 75.0F * Settings.scale, START_Y + (float)index * SPACE_Y * 2.0F);
        }

    }

    private void setLabel() {
        switch(this.result) {
            case PLAY:
                this.label = TEXT[1];
                break;
            case RESUME_GAME:
                this.label = TEXT[4];
                break;
            case ABANDON_RUN:
                this.label = TEXT[10];
                break;
            case INFO:
                this.label = TEXT[14];
                break;
            case STAT:
                this.label = TEXT[6];
                break;
            case SETTINGS:
                this.label = TEXT[12];
                break;
            case QUIT:
                this.label = TEXT[8];
                break;
            case PATCH_NOTES:
                this.label = TEXT[9];
                break;
            case MODS:
                this.label = "MODS";
                break;
            case ABOUT:
                this.label = TEXT[15];
                break;
            default:
                this.label = "ERROR";
        }

    }

    public void update() {
        if (CardCrawlGame.mainMenuScreen.screen == CurScreen.MAIN_MENU && CardCrawlGame.mainMenuScreen.bg.slider < 0.5F) {
            this.hb.update();
        }

        this.x = MathHelper.uiLerpSnap(this.x, this.targetX);
        if (this.hb.justHovered && !this.hidden) {
            CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
        }

        if (this.hb.hovered) {
            this.highlightColor.a = 0.9F;
            this.targetX = 25.0F * Settings.scale;
            if (InputHelper.justClickedLeft) {
                CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
                this.hb.clickStarted = true;
            }

            this.tint = Color.WHITE.cpy();
        } else if (CardCrawlGame.mainMenuScreen.screen == CurScreen.MAIN_MENU) {
            this.highlightColor.a = MathHelper.fadeLerpSnap(this.highlightColor.a, 0.0F);
            this.targetX = 0.0F;
            this.hidden = false;
            this.tint.r = MathHelper.fadeLerpSnap(this.tint.r, 0.3F);
            this.tint.g = this.tint.r;
            this.tint.b = this.tint.r;
        }

        if (this.hb.hovered && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.select.unpress();
            this.hb.clicked = true;
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            this.buttonEffect();
            CardCrawlGame.mainMenuScreen.hideMenuButtons();
        }

    }

    public void hide() {
        this.hb.hovered = false;
        this.targetX = -1000.0F * Settings.scale + 30.0F * Settings.scale * (float)this.index;
        this.hidden = true;
    }

    public void buttonEffect() {
        switch(this.result) {
            case PLAY:
                CardCrawlGame.mainMenuScreen.panelScreen.open(PanelScreen.PLAY);
                break;
            case RESUME_GAME:
                CardCrawlGame.mainMenuScreen.screen = CurScreen.NONE;
                CardCrawlGame.mainMenuScreen.hideMenuButtons();
                CardCrawlGame.mainMenuScreen.darken();
                this.resumeGame();
                break;
            case ABANDON_RUN:
                CardCrawlGame.mainMenuScreen.screen = CurScreen.ABANDON_CONFIRM;
                CardCrawlGame.mainMenuScreen.abandonPopup.show();
                break;
            case INFO:
                CardCrawlGame.mainMenuScreen.panelScreen.open(PanelScreen.COMPENDIUM);
                break;
            case STAT:
                CardCrawlGame.mainMenuScreen.panelScreen.open(PanelScreen.STATS);
                break;
            case SETTINGS:
                CardCrawlGame.mainMenuScreen.panelScreen.open(PanelScreen.SETTINGS);
                break;
            case QUIT:
                logger.info("Quit Game button clicked!");
                Gdx.app.exit();
                break;
            case PATCH_NOTES:
                CardCrawlGame.mainMenuScreen.patchNotesScreen.open();
                break;
            case MODS:
                CardCrawlGame.mainMenuScreen.modListScreen.open();
                break;
            case ABOUT:
                EarlyAccessPopup.isUp = true;
                CardCrawlGame.mainMenuScreen.bg.activated = false;
                CardCrawlGame.mainMenuScreen.screen = CurScreen.ABOUT;
                break;
        }

    }

    private void resumeGame() {
        CardCrawlGame.loadingSave = true;
        CardCrawlGame.chosenCharacter = CardCrawlGame.characterManager.loadChosenCharacter().chosenClass;
        CardCrawlGame.mainMenuScreen.isFadingOut = true;
        CardCrawlGame.mainMenuScreen.fadeOutMusic();
        Settings.isDailyRun = false;
        Settings.isTrial = false;
        ModHelper.setModsFalse();
        if (CardCrawlGame.steelSeries.isEnabled) {
            CardCrawlGame.steelSeries.event_character_chosen(CardCrawlGame.chosenCharacter);
        }
    }

    public void render(SpriteBatch sb) {
        float lerper = Interpolation.circleIn.apply(CardCrawlGame.mainMenuScreen.bg.slider);
        float sliderX = -1000.0F * Settings.scale * lerper;
        sliderX -= (float)this.index * 250.0F * Settings.scale * lerper;
        if (this.result == MenuButton.ClickResult.ABANDON_RUN) {
            if (this.confirmation) {
                this.label = TEXT[11];
            } else {
                this.label = TEXT[10];
            }
        }

        sb.setBlendFunction(770, 1);
        sb.setColor(this.highlightColor);
        if (!Settings.isTouchScreen && !Settings.isMobile) {
            sb.draw(highlightImg, this.x + FONT_X + sliderX - 179.0F + 120.0F * Settings.scale, this.hb.cY - 52.0F, 179.0F, 52.0F, 358.0F, 104.0F, Settings.scale, Settings.scale * 0.8F, 0.0F, 0, 0, 358, 104, false, false);
        } else {
            sb.draw(highlightImg, this.x + FONT_X + sliderX - 179.0F + 120.0F * Settings.scale, this.hb.cY - 56.0F, 179.0F, 52.0F, 358.0F, 104.0F, Settings.scale, Settings.scale * 1.2F, 0.0F, 0, 0, 358, 104, false, false);
        }

        sb.setBlendFunction(770, 771);
        if (!Settings.isTouchScreen && !Settings.isMobile) {
            FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, this.label, this.x + FONT_X + sliderX, this.hb.cY + FONT_OFFSET_Y, 9999.0F, 1.0F, Settings.CREAM_COLOR);
        } else {
            FontHelper.renderSmartText(sb, FontHelper.losePowerFont, this.label, this.x + FONT_X + sliderX, this.hb.cY + FONT_OFFSET_Y, 9999.0F, 1.0F, Settings.CREAM_COLOR, 1.25F);
        }

        this.hb.render(sb);
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("MenuButton");
        TEXT = uiStrings.TEXT;
        FONT_X = 120.0F * Settings.scale;
        START_Y = 120.0F * Settings.scale;
        SPACE_Y = 50.0F * Settings.scale;
        FONT_OFFSET_Y = 10.0F * Settings.scale;
        highlightImg = null;
    }

    public enum ClickResult {
        PLAY,
        RESUME_GAME,
        ABANDON_RUN,
        INFO,
        STAT,
        SETTINGS,
        MODS,
        PATCH_NOTES,
        QUIT,
        ABOUT;

        ClickResult() {
        }
    }
}

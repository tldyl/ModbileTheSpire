package com.megacrit.cardcrawl.ui.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.helpers.input.ScrollInputProcessor;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.SaveSlotScreen;

public class RenamePopup {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(RenamePopup.class);
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private int slot = 0;
    private boolean newSave = false;
    private boolean shown = false;
    public static String textField;
    public Hitbox yesHb;
    public Hitbox noHb;
    private static final int CONFIRM_W = 360;
    private static final int CONFIRM_H = 414;
    private static final int YES_W = 173;
    private static final int NO_W = 161;
    private static final int BUTTON_H = 74;
    private Color faderColor;
    private Color uiColor;
    private float waitTimer;

    public RenamePopup() {
        this.yesHb = new Hitbox(160.0F * Settings.scale, 70.0F * Settings.scale);
        this.noHb = new Hitbox(160.0F * Settings.scale, 70.0F * Settings.scale);
        this.faderColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.uiColor = new Color(1.0F, 0.965F, 0.886F, 0.0F);
        this.waitTimer = 0.0F;
        this.yesHb.move(854.0F * Settings.scale, Settings.OPTION_Y - 120.0F * Settings.scale);
        this.noHb.move(1066.0F * Settings.scale, Settings.OPTION_Y - 120.0F * Settings.scale);
    }

    public void update() {
        if (Gdx.input.isKeyPressed(67) && !textField.equals("") && this.waitTimer <= 0.0F) {
            textField = textField.substring(0, textField.length() - 1);
            this.waitTimer = 0.09F;
        }

        if (this.waitTimer > 0.0F) {
            this.waitTimer -= Gdx.graphics.getDeltaTime();
        }

        if (this.shown) {
            this.faderColor.a = MathHelper.fadeLerpSnap(this.faderColor.a, 0.75F);
            this.uiColor.a = MathHelper.fadeLerpSnap(this.uiColor.a, 1.0F);
            this.updateButtons();
            if (Gdx.input.isKeyJustPressed(66)) {
                this.confirm();
            } else if (InputHelper.pressedEscape) {
                InputHelper.pressedEscape = false;
                this.cancel();
            }
        } else {
            this.faderColor.a = MathHelper.fadeLerpSnap(this.faderColor.a, 0.0F);
            this.uiColor.a = MathHelper.fadeLerpSnap(this.uiColor.a, 0.0F);
        }

    }

    private void updateButtons() {
        this.yesHb.update();
        if (this.yesHb.justHovered) {
            CardCrawlGame.sound.play("UI_HOVER");
        }

        if (this.yesHb.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            this.yesHb.clickStarted = true;
        } else if (this.yesHb.clicked) {
            this.confirm();
            this.yesHb.clicked = false;
        }

        this.noHb.update();
        if (this.noHb.justHovered) {
            CardCrawlGame.sound.play("UI_HOVER");
        }

        if (this.noHb.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            this.noHb.clickStarted = true;
        } else if (this.noHb.clicked) {
            this.cancel();
            this.noHb.clicked = false;
        }

        if (CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            this.confirm();
        } else if (CInputActionSet.cancel.isJustPressed() || InputActionSet.cancel.isJustPressed()) {
            CInputActionSet.cancel.unpress();
            this.cancel();
        }

    }

    public void confirm() {
        textField = textField.trim();
        if (!textField.equals("")) {
            CardCrawlGame.mainMenuScreen.saveSlotScreen.curPop = SaveSlotScreen.CurrentPopup.NONE;
            this.shown = false;
            Gdx.input.setInputProcessor(new ScrollInputProcessor());
            if (this.newSave) {
                boolean saveSlotPrefSave = false;
                logger.info("UPDATING DEFAULT SLOT: ", this.slot);
                CardCrawlGame.saveSlotPref.putInteger("DEFAULT_SLOT", this.slot);
                saveSlotPrefSave = true;
                CardCrawlGame.reloadPrefs();
                if (!CardCrawlGame.saveSlotPref.getString(SaveHelper.slotName("PROFILE_NAME", this.slot), "").equals(textField)) {
                    logger.info("NAME CHANGE IN SLOT " + this.slot + ": " + textField);
                    CardCrawlGame.saveSlotPref.putString(SaveHelper.slotName("PROFILE_NAME", this.slot), textField);
                    saveSlotPrefSave = true;
                }

                if (saveSlotPrefSave) {
                    CardCrawlGame.saveSlotPref.flush();
                }

                if (CardCrawlGame.playerPref.getString("alias", "").equals("")) {
                    CardCrawlGame.playerPref.putString("alias", CardCrawlGame.generateRandomAlias());
                }

                CardCrawlGame.alias = CardCrawlGame.playerPref.getString("alias", "");
                CardCrawlGame.playerPref.putString("name", textField);
                CardCrawlGame.playerPref.flush();
                CardCrawlGame.playerName = textField;
            } else if (!CardCrawlGame.saveSlotPref.getString(SaveHelper.slotName("PROFILE_NAME", this.slot), "").equals(textField)) {
                logger.info("RENAMING " + this.slot + ": " + textField);
                CardCrawlGame.saveSlotPref.putString(SaveHelper.slotName("PROFILE_NAME", this.slot), textField);
                CardCrawlGame.saveSlotPref.flush();
                CardCrawlGame.mainMenuScreen.saveSlotScreen.rename(this.slot, textField);
                CardCrawlGame.playerName = textField;
            }

        }
    }

    public void cancel() {
        CardCrawlGame.mainMenuScreen.saveSlotScreen.curPop = SaveSlotScreen.CurrentPopup.NONE;
        this.shown = false;
        Gdx.input.setInputProcessor(new ScrollInputProcessor());
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.faderColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        this.renderPopupBg(sb);
        this.renderTextbox(sb);
        this.renderHeader(sb);
        this.renderButtons(sb);
    }

    private void renderHeader(SpriteBatch sb) {
        Color c = Settings.GOLD_COLOR.cpy();
        c.a = this.uiColor.a;
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[1], (float)Settings.WIDTH / 2.0F, Settings.OPTION_Y + 150.0F * Settings.scale, c);
    }

    private void renderPopupBg(SpriteBatch sb) {
        sb.setColor(this.uiColor);
        sb.draw(ImageMaster.OPTION_CONFIRM, (float)Settings.WIDTH / 2.0F - 180.0F, Settings.OPTION_Y - 207.0F, 180.0F, 207.0F, 360.0F, 414.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 360, 414, false, false);
    }

    private void renderTextbox(SpriteBatch sb) {
        sb.draw(ImageMaster.RENAME_BOX, (float)Settings.WIDTH / 2.0F - 160.0F, Settings.OPTION_Y + 20.0F * Settings.scale - 160.0F, 160.0F, 160.0F, 320.0F, 320.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 320, 320, false, false);
        FontHelper.renderSmartText(sb, FontHelper.cardTitleFont, textField, (float)Settings.WIDTH / 2.0F - 120.0F * Settings.scale, Settings.OPTION_Y + 24.0F * Settings.scale, 100000.0F, 0.0F, this.uiColor, 0.82F);
        float tmpAlpha = (MathUtils.cosDeg((float)(System.currentTimeMillis() / 3L % 360L)) + 1.25F) / 3.0F * this.uiColor.a;
        FontHelper.renderSmartText(sb, FontHelper.cardTitleFont, "_", (float)Settings.WIDTH / 2.0F - 122.0F * Settings.scale + FontHelper.getSmartWidth(FontHelper.cardTitleFont, textField, 1000000.0F, 0.0F, 0.82F), Settings.OPTION_Y + 24.0F * Settings.scale, 100000.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, tmpAlpha));
    }

    private void renderButtons(SpriteBatch sb) {
        sb.setColor(this.uiColor);
        Color c = Settings.GOLD_COLOR.cpy();
        c.a = this.uiColor.a;
        if (this.yesHb.clickStarted) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.uiColor.a * 0.9F));
            sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
            sb.setColor(new Color(this.uiColor));
        } else {
            sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
        }

        if (!this.yesHb.clickStarted && this.yesHb.hovered) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.uiColor.a * 0.25F));
            sb.setBlendFunction(770, 1);
            sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(this.uiColor);
        }

        if (!this.yesHb.clickStarted && !textField.trim().equals("")) {
            if (this.yesHb.hovered) {
                c = Settings.CREAM_COLOR.cpy();
            } else {
                c = Settings.GOLD_COLOR.cpy();
            }
        } else {
            c = Color.LIGHT_GRAY.cpy();
        }

        c.a = this.uiColor.a;
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[2], (float)Settings.WIDTH / 2.0F - 110.0F * Settings.scale, Settings.OPTION_Y - 118.0F * Settings.scale, c, 0.82F);
        sb.draw(ImageMaster.OPTION_NO, (float)Settings.WIDTH / 2.0F - 80.5F + 106.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 80.5F, 37.0F, 161.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 161, 74, false, false);
        if (!this.noHb.clickStarted && this.noHb.hovered) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.uiColor.a * 0.25F));
            sb.setBlendFunction(770, 1);
            sb.draw(ImageMaster.OPTION_NO, (float)Settings.WIDTH / 2.0F - 80.5F + 106.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 80.5F, 37.0F, 161.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 161, 74, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(this.uiColor);
        }

        if (this.noHb.clickStarted) {
            c = Color.LIGHT_GRAY.cpy();
        } else if (this.noHb.hovered) {
            c = Settings.CREAM_COLOR.cpy();
        } else {
            c = Settings.GOLD_COLOR.cpy();
        }

        c.a = this.uiColor.a;
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[3], (float)Settings.WIDTH / 2.0F + 110.0F * Settings.scale, Settings.OPTION_Y - 118.0F * Settings.scale, c, 0.82F);
        if (Settings.isControllerMode) {
            sb.draw(CInputActionSet.proceed.getKeyImg(), 770.0F * Settings.xScale - 32.0F, Settings.OPTION_Y - 32.0F - 140.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            sb.draw(CInputActionSet.cancel.getKeyImg(), 1150.0F * Settings.xScale - 32.0F, Settings.OPTION_Y - 32.0F - 140.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        if (this.shown) {
            this.yesHb.render(sb);
            this.noHb.render(sb);
        }

    }

    public void open(int slot, boolean isNewSave) {
        Gdx.input.setOnscreenKeyboardVisible(true);
        Gdx.input.setInputProcessor(new TypeHelper());
        this.slot = slot;
        this.newSave = isNewSave;
        this.shown = true;
        textField = "";
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RenamePanel");
        TEXT = uiStrings.TEXT;
        textField = "";
    }
}

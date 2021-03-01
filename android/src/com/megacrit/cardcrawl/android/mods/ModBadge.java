package com.megacrit.cardcrawl.android.mods;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.interfaces.PreUpdateSubscriber;
import com.megacrit.cardcrawl.android.mods.interfaces.RenderSubscriber;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.EarlyAccessPopup;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

public class ModBadge implements RenderSubscriber, PreUpdateSubscriber {
    public static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(ModBadge.class);
    private Texture texture;
    private String modName;
    private String tip;
    public float x;
    public float y;
    private float w;
    private float h;
    private Hitbox hb;
    private ModPanel modPanel;

    public ModBadge(Texture t, float xPos, float yPos, String name, String auth, String desc, ModPanel modSettings) {
        this.modName = name;
        this.tip = auth + " NL " + desc;
        this.texture = t;
        this.x = xPos;
        this.y = yPos;
        this.w = (float)t.getWidth();
        this.h = (float)t.getHeight();
        this.hb = new Hitbox(this.x, this.y, this.w * Settings.scale, this.h * Settings.scale);
        this.modPanel = modSettings;
        logger.info("initialized mod badge for: " + this.modName);
        BaseMod.subscribe(this);
        logger.info("setup hooks for " + this.modName + " mod badge");
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (CardCrawlGame.mainMenuScreen != null && CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.MAIN_MENU && !EarlyAccessPopup.isUp && !BaseMod.modSettingsUp) {
            sb.setColor(Color.WHITE);
            sb.draw(this.texture, this.x, this.y, this.w * Settings.scale, this.h * Settings.scale);
            this.hb.render(sb);
        } else if (this.modPanel != null && this.modPanel.isUp) {
            this.modPanel.render(sb);
        }
    }

    @Override
    public void receivePreUpdate() {
        if (CardCrawlGame.mainMenuScreen != null && CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.MAIN_MENU && !EarlyAccessPopup.isUp && !BaseMod.modSettingsUp) {
            this.hb.update();
            if (this.hb.justHovered) {
                logger.info(this.modName + " badge hovered");
                CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
            }

            if (this.hb.hovered) {
                TipHelper.renderGenericTip(this.x + 2.0F * this.w, this.y + this.h, this.modName, this.tip);
                if (InputHelper.justClickedLeft) {
                    CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
                    this.hb.clickStarted = true;
                }
            }

            if (this.hb.clicked) {
                this.hb.clicked = false;
                this.onClick();
            }
        } else if (this.modPanel != null && this.modPanel.isUp) {
            this.modPanel.update();
        }
    }

    public void onClick() {
        logger.info(this.modName + " badge clicked");
        if (this.modPanel != null) {
            this.modPanel.oldInputProcessor = Gdx.input.getInputProcessor();
            BaseMod.modSettingsUp = true;
            this.modPanel.isUp = true;
            this.modPanel.onCreate();
            CardCrawlGame.mainMenuScreen.darken();
            CardCrawlGame.mainMenuScreen.hideMenuButtons();
            CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.SETTINGS;
            CardCrawlGame.cancelButton.show("Close");
        }
    }

    public ModPanel getModPanel() {
        return this.modPanel;
    }
}

package com.megacrit.cardcrawl.android.mods.screens.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModColorTabBar {
    private static final int TAB_W = 222;
    private static final int TAB_H = 68;
    private List<Hitbox> modHbList = new ArrayList<>();
    private List<AbstractCard.CardColor> modColorList = new ArrayList<>();
    private List<Texture> colorTabList = new ArrayList<>();
    public int currentTabIndex = -1;
    private boolean initialized = false;

    public ModColorTabBar() {
    }

    public void initialize(Map<AbstractCard.CardColor, CardGroup> modCards) {
        if (!initialized) {
            for (Map.Entry<AbstractCard.CardColor, CardGroup> e : modCards.entrySet()) {
                modColorList.add(e.getKey());
                modHbList.add(new Hitbox(TAB_W * Settings.xScale, TAB_H * Settings.yScale));
                Color color = BaseMod.getColorBundleMap().get(e.getKey()).bgColor;
                Pixmap pixmap = new Pixmap(TAB_W, TAB_H, Pixmap.Format.RGBA8888);
                pixmap.setColor(color);
                pixmap.fill();
                colorTabList.add(new Texture(pixmap));
            }
            initialized = true;
        }
    }

    public void update() {
        float x = 140.0F * Settings.xScale;
        float y = Settings.HEIGHT * 0.8F;
        int i = 0;
        for (Hitbox hb : modHbList) {
            hb.move(x, y);
            hb.update();
            if (hb.justHovered) {
                CardCrawlGame.sound.playA("UI_HOVER", -0.4F);
            }
            if (InputHelper.justClickedLeft) {
                int prevIndex = this.currentTabIndex;
                if (hb.hovered) {
                    this.currentTabIndex = i;
                }
                if (prevIndex != this.currentTabIndex) {
                    CardCrawlGame.mainMenuScreen.cardLibraryScreen.didChangeModTab(modColorList.get(i));
                }
            }
            y -= 74.0F * Settings.yScale;
            i++;
        }
    }

    public void render(SpriteBatch sb) {
        int i = 0;
        for (Texture tab : colorTabList) {
            sb.setColor(Color.GRAY);
            if (modHbList.get(i).hovered) {
                sb.setColor(Color.WHITE);
            }
            sb.draw(tab, modHbList.get(i).x, modHbList.get(i).y, TAB_W, TAB_H, TAB_W, TAB_H, Settings.xScale, Settings.yScale, 0.0F, 0, 0, TAB_W, TAB_H, false, false);
            Color c = Color.GRAY;
            if (this.currentTabIndex != i) {
                c = Settings.GOLD_COLOR;
            }
            for (AbstractPlayer player : CardCrawlGame.characterManager.getAllCharacters()) {
                if (player.getCardColor() == modColorList.get(i)) {
                    FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, player.name, modHbList.get(i).x + (TAB_W / 2) * Settings.xScale, modHbList.get(i).y + (TAB_H / 2) * Settings.yScale, c, 0.9F);
                    break;
                }
            }
            modHbList.get(i).render(sb);
            i++;
        }
    }
}

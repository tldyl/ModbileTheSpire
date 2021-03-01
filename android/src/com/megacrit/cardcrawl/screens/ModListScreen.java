package com.megacrit.cardcrawl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.megacrit.cardcrawl.android.mods.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.PatchNotesScreen;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings({"SuspiciousNameCombination", "SameParameterValue"})
public class ModListScreen {
    private static final float START_Y = Settings.HEIGHT - 200.0F * Settings.scale;
    private float scrollY = START_Y;
    private float targetY = this.scrollY;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private MenuCancelButton button = new MenuCancelButton();
    private boolean grabbedScreen = false;
    private float grabStartY = 0.0F;

    private ArrayList<Hitbox> hitboxes = new ArrayList<>();
    private int selectedMod = -1;

    private Hitbox configHb;

    public static Map<URL, ModBadge> baseModBadges;
    private static boolean justClosedModPanel = false;

    public ModListScreen() {
        for (ModInfo ignored : Loader.MODINFOS) {
            this.hitboxes.add(new Hitbox(430.0F * Settings.scale, 40.0F * Settings.scale));
        }

        this.configHb = new Hitbox(100.0F * Settings.scale, 40.0F * Settings.scale);
    }

    public void open() {
        this.button.show(PatchNotesScreen.TEXT[0]);
        this.scrollY = this.targetY = Settings.HEIGHT - 150.0F * Settings.scale;
        CardCrawlGame.mainMenuScreen.darken();
        CardCrawlGame.mainMenuScreen.screen =  MainMenuScreen.CurScreen.MOD_LIST;

        this.selectedMod = -1;

        this.scrollUpperBound = this.targetY + Math.max(0, Loader.MODINFOS.length - 15) * 45.0F * Settings.scale;
        this.scrollLowerBound = this.targetY;
    }

    public void update() {
        this.button.update();
        if (this.button.hb.clicked || InputHelper.pressedEscape) {
            this.button.hb.clicked = false;
            InputHelper.pressedEscape = false;
            if (!BaseMod.modSettingsUp) {
                CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
                this.button.hide();
                CardCrawlGame.mainMenuScreen.lighten();
            } else {
                BaseMod.modSettingsUp = false;
                justClosedModPanel = true;
            }
        }

        if (baseModBadges != null) {
            for (Map.Entry<URL, ModBadge> entry : baseModBadges.entrySet()) {
                modPanel_update(entry.getValue());
            }
            if (justClosedModPanel) {
                justClosedModPanel = false;
                CardCrawlGame.mainMenuScreen.darken();
                CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MOD_LIST;
                this.button.show(PatchNotesScreen.TEXT[0]);
            }
        }

        if (!BaseMod.modSettingsUp) {
            updateScrolling();
            float tmpY = 0.0F;
            for (int i = 0; i < this.hitboxes.size(); i++) {
                this.hitboxes.get(i).x = 90.0F * Settings.scale;
                this.hitboxes.get(i).y = tmpY + this.scrollY - 30.0F * Settings.scale;
                this.hitboxes.get(i).update();
                if (this.hitboxes.get(i).hovered && InputHelper.isMouseDown) {
                    this.hitboxes.get(i).clickStarted = true;
                }
                tmpY -= 45.0F * Settings.scale;

                if (this.hitboxes.get(i).clicked) {
                    this.hitboxes.get(i).clicked = false;
                    this.selectedMod = i;
                }
            }

            if (baseModBadges != null && this.selectedMod >= 0 && baseModBadges.get((Loader.MODINFOS[this.selectedMod]).jarURL) != null) {
                this.configHb.update();
                if (this.configHb.hovered && InputHelper.justClickedLeft) {
                    modBadge_onClick(baseModBadges.get((Loader.MODINFOS[this.selectedMod]).jarURL));
                }
            }
        }

        InputHelper.justClickedLeft = false;
    }

    private void modPanel_update(ModBadge badge) {
        ModPanel panel = badge.getModPanel();
        panel.update();
    }

    private void modBadge_onClick(ModBadge badge) {
        this.button.show("Close");
        badge.onClick();
    }

    private void updateScrolling() {
        if (this.hitboxes.size() > 16) {
            int y = InputHelper.mY;
            if (!this.grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    this.targetY += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.targetY -= Settings.SCROLL_SPEED;
                }
                if (InputHelper.justClickedLeft) {
                    this.grabbedScreen = true;
                    this.grabStartY = y - this.targetY;
                }
            } else if (InputHelper.isMouseDown) {
                this.targetY = y - this.grabStartY;
            } else {
                this.grabbedScreen = false;
            }
        }
        this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.targetY);
        resetScrolling();
    }

    private void resetScrolling() {
        if (this.targetY < this.scrollLowerBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollLowerBound);
        } else if (this.targetY > this.scrollUpperBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollUpperBound);
        }
    }

    public void render(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, "Mod List", Settings.WIDTH / 2.0F, Settings.HEIGHT - 70.0F * Settings.scale, Settings.GOLD_COLOR);
        renderModInfo(sb);
        renderModList(sb);

        if (baseModBadges != null && BaseMod.modSettingsUp) {
            for (Map.Entry<URL, ModBadge> entry : baseModBadges.entrySet()) {
                MainMenuScreen.CurScreen tmpScreen = CardCrawlGame.mainMenuScreen.screen;
                CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
                entry.getValue().receiveRender(sb);
                CardCrawlGame.mainMenuScreen.screen = tmpScreen;
            }
        }

        for (Hitbox hitbox : this.hitboxes) {
            hitbox.render(sb);
        }

        this.button.render(sb);
    }

    private void renderModInfo(SpriteBatch sb) {
        sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
        float screenPadding = 50.0F * Settings.scale;
        float x = 600.0F * Settings.scale;
        float y = 110.0F * Settings.scale;
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, screenPadding, Settings.WIDTH - x - screenPadding, Settings.HEIGHT - y - screenPadding);

        sb.setColor(Color.WHITE);

        float padding = 20.0F * Settings.scale;
        if (this.selectedMod >= 0) {
            ModInfo info = Loader.MODINFOS[this.selectedMod];
            String text = info.modName;
            text = text + " NL ModVersion: " + ((info.modVersion != null) ? info.modVersion : "<MISSING>");
            text = text + " NL Mod ID: " + ((info.modId != null) ? info.modId : "<MISSING>");
            text = text + " NL Author" + ((info.authorList.length > 1) ? "s" : "") + ": " + StringUtils.join(info.authorList, ", ");

            text = text + " NL NL " + newlineToNL(info.descriptions.getOrDefault(Settings.language.name().toLowerCase(), info.descriptions.get("eng")));

            FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, text, x + padding, Settings.HEIGHT - y - padding, Settings.WIDTH - x - screenPadding, 26.0F * Settings.scale, Settings.CREAM_COLOR);

            if (baseModBadges != null) {
                this.configHb.move(x - padding - 50.0F * Settings.scale, this.button.hb.y + this.button.hb.height / 2.0F);

                if (baseModBadges.get((Loader.MODINFOS[this.selectedMod]).jarURL) != null) {
                    this.configHb.render(sb);

                    Color c = Settings.CREAM_COLOR;
                    if (this.configHb.hovered) {
                        c = Settings.GOLD_COLOR;
                    }
                    FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, "Config", this.configHb.cX, this.configHb.cY, c);
                }
            }
        }
    }

    private String newlineToNL(String[] descriptions) {
        StringBuilder sb = new StringBuilder();
        for (String s : descriptions) {
            sb.append(s).append(" NL ");
        }
        return sb.toString();
    }

    private void renderModList(SpriteBatch sb) {
        OrthographicCamera camera = null;
        try {
            Field f = CardCrawlGame.class.getDeclaredField("camera");
            f.setAccessible(true);
            camera = (OrthographicCamera)f.get(Gdx.app.getApplicationListener());
        } catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }

        if (camera != null) {
            sb.flush();
            Rectangle scissors = new Rectangle();
            float y = Settings.HEIGHT - 110.0F * Settings.scale;
            Rectangle clipBounds = new Rectangle(50.0F * Settings.scale, y, 500.0F * Settings.scale, this.button.hb.y - y + this.button.hb.height);

            ScissorStack.calculateScissors(camera, sb.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
        }

        sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(Color.WHITE);

        float tmpY = 0.0F;
        for (int i = 0; i < Loader.MODINFOS.length; i++) {
            if (this.hitboxes.get(i).hovered) {
                Color c = sb.getColor();
                sb.setColor(1.0F, 1.0F, 1.0F, this.hitboxes.get(i).clickStarted ? 0.8F : 0.4F);
                sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.hitboxes.get(i).x, this.hitboxes.get(i).y, this.hitboxes.get(i).width, this.hitboxes.get(i).height);
                sb.setColor(c);
            }

            URL modURL = (Loader.MODINFOS[i]).jarURL;
            Color renderColor;
            if (Loader.MODINFOS[i].isLoaded) {
                renderColor = Settings.CREAM_COLOR;
            } else {
                renderColor = Color.RED;
            }
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.buttonLabelFont, (Loader.MODINFOS[i]).modName, 95.0F * Settings.scale, tmpY + this.scrollY, renderColor);

            if (i == this.selectedMod) {
                drawRect(sb, this.hitboxes
                        .get(i).x, this.hitboxes
                        .get(i).y, this.hitboxes
                        .get(i).width, this.hitboxes
                        .get(i).height, 2.0F);
            }

            if (baseModBadges != null) {
                for (Map.Entry<URL, ModBadge> entry : baseModBadges.entrySet()) {
                    if (entry.getKey().equals(modURL)) {
                        ModBadge modBadge = entry.getValue();
                        modBadge.x = 55.0F * Settings.scale;
                        modBadge.y = tmpY + this.scrollY - 27.0F * Settings.scale;
                        boolean tmpModSettingsUp = BaseMod.modSettingsUp;
                        BaseMod.modSettingsUp = false;
                        MainMenuScreen.CurScreen tmpScreen = CardCrawlGame.mainMenuScreen.screen;
                        CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
                        modBadge.receiveRender(sb);
                        CardCrawlGame.mainMenuScreen.screen = tmpScreen;
                        BaseMod.modSettingsUp = tmpModSettingsUp;
                        break;
                    }
                }
            }

            tmpY -= 45.0F * Settings.scale;
        }

        if (camera != null) {
            sb.flush();
            ScissorStack.popScissors();
        }
    }

    private void drawRect(SpriteBatch sb, float x, float y, float width, float height, float thickness) {
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, y, width, thickness);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, y, thickness, height);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, y + height - thickness, width, thickness);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x + width - thickness, y, thickness, height);
    }
}

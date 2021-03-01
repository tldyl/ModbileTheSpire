package com.megacrit.cardcrawl.android.mods;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.interfaces.IUIElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModPanel {
    public static final int BACKGROUND_LAYER = 0;
    public static final int MIDDLE_LAYER = 1;
    public static final int TEXT_LAYER = 2;
    public static final int PRIORITY_UPDATE = 0;
    public static final int DEFAULT_UPDATE = 1;
    public static final int LATE_UPDATE = 2;
    private static Texture background;
    private static Comparator<IUIElement> renderComparator = Comparator.comparingInt(IUIElement::renderLayer);
    private static Comparator<IUIElement> updateComparator = Comparator.comparingInt(IUIElement::updateOrder);
    private ArrayList<IUIElement> uiElementsRender;
    private ArrayList<IUIElement> uiElementsUpdate;
    private Consumer<ModPanel> createFunc;
    public InputProcessor oldInputProcessor;
    public boolean isUp;
    public HashMap<String, Integer> state;
    public boolean waitingOnEvent;
    private boolean created;

    public ModPanel() {
        this((me) -> {});
    }

    public ModPanel(Consumer<ModPanel> createFunc) {
        this.oldInputProcessor = null;
        this.isUp = false;
        this.waitingOnEvent = false;
        this.created = false;
        background = ImageMaster.loadImage("images/ui/ModPanelBg.png");
        this.uiElementsRender = new ArrayList<>();
        this.uiElementsUpdate = new ArrayList<>();
        this.state = new HashMap<>();
        this.createFunc = createFunc;
    }

    public void onCreate() {
        if (!this.created) {
            this.createFunc.accept(this);
            this.created = true;
        }

    }

    public void addUIElement(IUIElement element) {
        this.uiElementsRender.add(element);
        this.uiElementsRender.sort(renderComparator);
        this.uiElementsUpdate.add(element);
        this.uiElementsUpdate.sort(updateComparator);
    }

    public void render(SpriteBatch sb) {
        this.renderBg(sb);

        for (IUIElement elem : this.uiElementsRender) {
            elem.render(sb);
        }
    }

    public void renderBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(background, (float)Settings.WIDTH / 2.0F - 682.0F, Settings.OPTION_Y - 376.0F, 682.0F, 376.0F, 1364.0F, 752.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1364, 752, false, false);
    }

    public void update() {
        for (IUIElement elem : this.uiElementsUpdate) {
            elem.update();
        }

        if (InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            BaseMod.modSettingsUp = false;
        }

        if (!BaseMod.modSettingsUp) {
            this.waitingOnEvent = false;
            Gdx.input.setInputProcessor(this.oldInputProcessor);
            CardCrawlGame.mainMenuScreen.lighten();
            CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
            CardCrawlGame.cancelButton.hideInstantly();
            this.isUp = false;
        }
    }

    public ArrayList<IUIElement> getUIElements() {
        ArrayList<IUIElement> retVal = new ArrayList<>();
        retVal.addAll(this.uiElementsRender);
        retVal.addAll(this.uiElementsUpdate);
        return retVal.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
    }
}

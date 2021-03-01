package com.megacrit.cardcrawl.helpers.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;
import java.util.Iterator;

public class CInputHelper {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(CInputHelper.class);
    public static Array<Controller> controllers = null;
    public static Controller controller = null;
    public static ArrayList<CInputAction> actions = new ArrayList<>();
    public static CInputListener listener = null;
    private static boolean initializedController = false;
    public static CInputHelper.ControllerModel model = null;

    public CInputHelper() {
    }

    public static void loadSettings() {
        CInputActionSet.load();
    }

    public static void initializeIfAble() {
        if (!initializedController) {
            initializedController = true;
            logger.info("[CONTROLLER] Utilizing DirectInput");

            try {
                controllers = Controllers.getControllers();
            } catch (Exception var1) {
                logger.info(var1.getMessage());
            }

            if (controllers == null) {
                logger.info("[ERROR] getControllers() returned null");
                return;
            }

            for(int i = 0; i < controllers.size; ++i) {
                logger.info("CONTROLLER[" + i + "] " + controllers.get(i).getName());
            }

            if (controllers.size != 0) {
                Settings.isControllerMode = true;
                Settings.isTouchScreen = false;
                listener = new CInputListener();
                controller = controllers.first();
                controller.addListener(listener);
                if (controller.getName().contains("360")) {
                    model = CInputHelper.ControllerModel.XBOX_360;
                    ImageMaster.loadControllerImages(CInputHelper.ControllerModel.XBOX_360);
                } else if (controller.getName().contains("Xbox One")) {
                    model = CInputHelper.ControllerModel.XBOX_ONE;
                    ImageMaster.loadControllerImages(CInputHelper.ControllerModel.XBOX_ONE);
                } else {
                    model = CInputHelper.ControllerModel.XBOX_360;
                    ImageMaster.loadControllerImages(CInputHelper.ControllerModel.XBOX_360);
                }
            } else {
                logger.info("[CONTROLLER] No controllers detected");
            }
        }

    }

    public static void updateFirst() {
    }

    public static void setCursor(Hitbox hb) {
        if (hb != null) {
            Gdx.input.setCursorPosition((int)hb.cX, Settings.HEIGHT - (int)hb.cY);
        }

    }

    public static void updateLast() {
        CInputAction a;
        for(Iterator var0 = actions.iterator(); var0.hasNext(); a.justReleased = false) {
            a = (CInputAction)var0.next();
            a.justPressed = false;
        }

    }

    public static boolean listenerPress(int keycode) {
        for (CInputAction a : actions) {
            if (a.keycode == keycode) {
                a.justPressed = true;
                a.pressed = true;
                break;
            }
        }

        return false;
    }

    public static boolean listenerRelease(int keycode) {
        for (CInputAction a : actions) {
            if (a.keycode == keycode) {
                a.justReleased = true;
                a.pressed = false;
                break;
            }
        }

        return false;
    }

    public static boolean isJustPressed(int keycode) {
        Iterator var1 = actions.iterator();

        CInputAction a;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            a = (CInputAction)var1.next();
        } while(a.keycode != keycode);

        return a.justPressed;
    }

    public static boolean isJustReleased(int keycode) {
        Iterator var1 = actions.iterator();

        CInputAction a;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            a = (CInputAction)var1.next();
        } while(a.keycode != keycode);

        return a.justReleased;
    }

    public static void regainInputFocus() {
        CInputListener.remapping = false;
    }

    public static boolean isTopPanelActive() {
        return AbstractDungeon.topPanel.selectPotionMode || AbstractDungeon.player.viewingRelics || !AbstractDungeon.topPanel.potionUi.isHidden;
    }

    public static enum ControllerModel {
        XBOX_360,
        XBOX_ONE,
        PS3,
        PS4,
        STEAM,
        OTHER;

        private ControllerModel() {
        }
    }
}

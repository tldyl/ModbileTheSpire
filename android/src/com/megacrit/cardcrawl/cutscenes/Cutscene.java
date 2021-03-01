package com.megacrit.cardcrawl.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomCutscene;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import java.util.ArrayList;
import java.util.Iterator;

public class Cutscene implements Disposable {
    private float darkenTimer = 1.0F;
    private float fadeTimer = 1.0F;
    private float switchTimer = 1.0F;
    private Color screenColor;
    private Color bgColor;
    public ArrayList<CutscenePanel> panels = new ArrayList<>();
    public Texture bgImg;
    private boolean isDone = false;

    public Cutscene(PlayerClass chosenClass) {
        switch(chosenClass.name()) {
            case "IRONCLAD":
                this.bgImg = ImageMaster.loadImage("images/scenes/redBg.jpg");
                this.panels.add(new CutscenePanel("images/scenes/ironclad1.png", "ATTACK_HEAVY"));
                this.panels.add(new CutscenePanel("images/scenes/ironclad2.png"));
                this.panels.add(new CutscenePanel("images/scenes/ironclad3.png"));
                break;
            case "THE_SILENT":
                this.bgImg = ImageMaster.loadImage("images/scenes/greenBg.jpg");
                this.panels.add(new CutscenePanel("images/scenes/silent1.png", "ATTACK_POISON2"));
                this.panels.add(new CutscenePanel("images/scenes/silent2.png"));
                this.panels.add(new CutscenePanel("images/scenes/silent3.png"));
                break;
            case "DEFECT":
                this.bgImg = ImageMaster.loadImage("images/scenes/blueBg.jpg");
                this.panels.add(new CutscenePanel("images/scenes/defect1.png", "ATTACK_MAGIC_BEAM_SHORT"));
                this.panels.add(new CutscenePanel("images/scenes/defect2.png"));
                this.panels.add(new CutscenePanel("images/scenes/defect3.png"));
                break;
            case "WATCHER":
                this.bgImg = ImageMaster.loadImage("images/scenes/purpleBg.jpg");
                this.panels.add(new CutscenePanel("images/scenes/watcher1.png", "WATCHER_HEART_PUNCH"));
                this.panels.add(new CutscenePanel("images/scenes/watcher2.png"));
                this.panels.add(new CutscenePanel("images/scenes/watcher3.png"));
                break;
            default:
                CustomCutscene customCutscene = BaseMod.cutsceneMap.getOrDefault(chosenClass, null);
                if (customCutscene != null) {
                    customCutscene.setCutscene(this);
                    break;
                }
                this.bgImg = ImageMaster.loadImage("images/scenes/redBg.jpg");
                this.panels.add(new CutscenePanel("images/scenes/ironclad1.png", "ATTACK_HEAVY"));
                this.panels.add(new CutscenePanel("images/scenes/ironclad2.png"));
                this.panels.add(new CutscenePanel("images/scenes/ironclad3.png"));
        }

        this.bgColor = Color.WHITE.cpy();
        this.screenColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void update() {
        this.updateFadeOut();
        this.updateFadeIn();
        for (CutscenePanel p : this.panels) {
            p.update();
        }
        this.updateIfDone();
        this.updateSceneChange();
    }

    private void updateIfDone() {
        if (this.isDone) {
            this.bgColor.a -= Gdx.graphics.getDeltaTime();

            for (CutscenePanel p : this.panels) {
                if (!p.finished) {
                    return;
                }
            }

            this.dispose();
            this.bgColor.a = 0.0F;
            this.openVictoryScreen();
        }

    }

    private void updateSceneChange() {
        this.switchTimer -= Gdx.graphics.getDeltaTime();
        if ((InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) && this.switchTimer > 1.0F) {
            this.switchTimer = 1.0F;
        }

        if (this.switchTimer < 0.0F) {
            Iterator var1 = this.panels.iterator();

            CutscenePanel p;
            while(var1.hasNext()) {
                p = (CutscenePanel)var1.next();
                if (!p.activated) {
                    p.activate();
                    this.switchTimer = 5.0F;
                    return;
                }
            }

            var1 = this.panels.iterator();

            while(var1.hasNext()) {
                p = (CutscenePanel)var1.next();
                p.fadeOut();
            }

            this.isDone = true;
        }

    }

    private void openVictoryScreen() {
        GameCursor.hidden = false;
        AbstractDungeon.victoryScreen = new VictoryScreen((MonsterGroup)null);
    }

    private void updateFadeIn() {
        if (this.darkenTimer == 0.0F) {
            this.fadeTimer -= Gdx.graphics.getDeltaTime();
            if (this.fadeTimer < 0.0F) {
                this.fadeTimer = 0.0F;
            }

            this.screenColor.a = this.fadeTimer;
        }

    }

    private void updateFadeOut() {
        if (this.darkenTimer != 0.0F) {
            this.darkenTimer -= Gdx.graphics.getDeltaTime();
            if (this.darkenTimer < 0.0F) {
                this.darkenTimer = 0.0F;
                this.fadeTimer = 1.0F;
                this.switchTimer = 1.0F;
            }

            this.screenColor.a = 1.0F - this.darkenTimer;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
    }

    public void renderAbove(SpriteBatch sb) {
        if (this.bgImg != null) {
            sb.setColor(this.bgColor);
            this.renderImg(sb, this.bgImg);
        }

        this.renderPanels(sb);
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
    }

    private void renderPanels(SpriteBatch sb) {
        for (CutscenePanel p : this.panels) {
            p.render(sb);
        }
    }

    private void renderImg(SpriteBatch sb, Texture img) {
        if (Settings.isSixteenByTen) {
            sb.draw(img, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        } else {
            sb.draw(img, 0.0F, -50.0F * Settings.scale, (float)Settings.WIDTH, (float)Settings.HEIGHT + 110.0F * Settings.scale);
        }
    }

    public void dispose() {
        if (this.bgImg != null) {
            this.bgImg.dispose();
            this.bgImg = null;
        }
        for (CutscenePanel p : this.panels) {
            p.dispose();
        }
    }
}

package com.megacrit.cardcrawl.android.mods.abstracts;

import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomCutscene {
    public abstract String getBgImgPath();

    public abstract ArrayList<CutscenePanel> getPanelList();

    public abstract AbstractPlayer.PlayerClass getPlayerClass();

    public abstract String getModId();

    public abstract void updateVictoryVfx(List<AbstractGameEffect> effectList);

    public void setCutscene(Cutscene cutscene) {
        cutscene.bgImg = AssetLoader.getTexture(getModId(), getBgImgPath());
        cutscene.panels = getPanelList();
    }
}

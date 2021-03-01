package com.megacrit.cardcrawl.core;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class EnergyManager {
    public int energy;
    public int energyMaster;

    public EnergyManager(int e) {
        this.energyMaster = e;
    }

    public void prep() {
        this.energy = this.energyMaster;
        EnergyPanel.totalCount = 0;
    }

    public void recharge() {
        if (AbstractDungeon.player.hasRelic("Ice Cream")) {
            if (EnergyPanel.totalCount > 0) {
                AbstractDungeon.player.getRelic("Ice Cream").flash();
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, AbstractDungeon.player.getRelic("Ice Cream")));
            }

            EnergyPanel.addEnergy(this.energy);
        } else if (AbstractDungeon.player.hasPower("Conserve")) {
            if (EnergyPanel.totalCount > 0) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, "Conserve", 1));
            }

            EnergyPanel.addEnergy(this.energy);
        } else {
            EnergyPanel.setEnergy(this.energy);
        }

        AbstractDungeon.actionManager.updateEnergyGain(this.energy);
        BaseMod.receivePostEnergyRecharge();
    }

    public void use(int e) {
        EnergyPanel.useEnergy(e);
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.blight;

import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Arrays;

public class BlightRemove extends ConsoleCommand {
    public BlightRemove() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String[] blightNameArray = Arrays.copyOfRange(tokens, 2, tokens.length);
        String blightName = String.join(" ", blightNameArray);
        AbstractDungeon.player.blights.removeIf(b -> b.blightID.equals(blightName));

        for(int i = 0; i < AbstractDungeon.player.blights.size(); ++i) {
            AbstractBlight tmp = AbstractDungeon.player.blights.get(i);
            tmp.currentX = tmp.targetX = 64.0F * Settings.scale + (float)i * AbstractRelic.PAD_X;
            tmp.hb.move(tmp.currentX, tmp.currentY);
        }

    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (AbstractBlight blight : AbstractDungeon.player.blights) {
            result.add(blight.blightID.replace(' ', '_'));
        }

        return result;
    }

    @Override
    public void errorMsg() {
        Blight.cmdBlightHelp();
    }
}

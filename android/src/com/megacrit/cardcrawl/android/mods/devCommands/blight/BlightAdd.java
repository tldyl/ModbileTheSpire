package com.megacrit.cardcrawl.android.mods.devCommands.blight;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.BlightHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class BlightAdd extends ConsoleCommand {
    public BlightAdd() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String[] blightNameArray = Arrays.copyOfRange(tokens, 2, tokens.length);
        String blightName = String.join(" ", blightNameArray);
        AbstractBlight blight = AbstractDungeon.player.getBlight(blightName);
        if (blight != null) {
            blight.incrementUp();
            blight.stack();
        } else if (BlightHelper.getBlight(blightName) != null) {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, BlightHelper.getBlight(blightName));
        } else {
            DevConsole.log("invalid blight ID");
        }

    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (String id : BlightHelper.blights) {
            result.add(id.replace(' ', '_'));
        }

        return result;
    }

    @Override
    public void errorMsg() {
        Blight.cmdBlightHelp();
    }
}

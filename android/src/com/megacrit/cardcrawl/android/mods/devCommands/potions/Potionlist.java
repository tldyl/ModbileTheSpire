package com.megacrit.cardcrawl.android.mods.devCommands.potions;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Potionlist extends ConsoleCommand {
    @Override
    public void execute(String[] tokens, int depth) {
        List<String> allPotions = PotionHelper.getPotions(AbstractPlayer.PlayerClass.IRONCLAD, true);
        Collections.sort(allPotions);
        DevConsole.log(allPotions);
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        complete = true;
        if (tokens.length > depth && tokens[depth].length() > 0) {
            tooManyTokensError();
        }
        return null;
    }
}


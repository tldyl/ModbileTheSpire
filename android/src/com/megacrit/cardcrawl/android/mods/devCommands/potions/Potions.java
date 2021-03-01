package com.megacrit.cardcrawl.android.mods.devCommands.potions;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.ArrayList;
import java.util.List;

public class Potions extends ConsoleCommand {
    public Potions() {
        this.followup.put("list", Potionlist.class);
        this.minExtraTokens = 2;
        this.maxExtraTokens = 2;
        this.requiresPlayer = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        if (PotionHelper.potions != null && !PotionHelper.potions.isEmpty()) {
            int i;
            try {
                i = Integer.parseInt(tokens[1]);
                if (i >= AbstractDungeon.player.potionSlots || i < 0) {
                    throw new Exception();
                }
            } catch (Exception var7) {
                DevConsole.log("Invalid Potionslot");
                return;
            }

            if (AbstractDungeon.player == null) {
                DevConsole.log("cannot obtain potion when player doesn't exist");
            } else {
                String potionID = "";

                for(int k = 2; k < tokens.length; ++k) {
                    potionID = potionID.concat(tokens[k]);
                    if (k != tokens.length - 1) {
                        potionID = potionID.concat(" ");
                    }
                }

                if (BaseMod.underScorePotionIDs.containsKey(potionID)) {
                    potionID = BaseMod.underScorePotionIDs.get(potionID);
                }

                List<String> allPotions = PotionHelper.getPotions(AbstractPlayer.PlayerClass.IRONCLAD, true);
                AbstractPotion p = null;
                if (allPotions.contains(potionID)) {
                    p = PotionHelper.getPotion(potionID);
                }

                if (allPotions.contains(potionID + " Potion")) {
                    p = PotionHelper.getPotion(potionID + " Potion");
                }

                if (p == null) {
                    DevConsole.log("invalid potion id");
                    DevConsole.log("use potion list to see valid ids");
                } else {
                    AbstractDungeon.player.obtainPotion(i, p);
                }
            }
        } else {
            DevConsole.log("cannot use potion command when potions are not initialized");
            DevConsole.log("start a run and try again");
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        int slots = AbstractDungeon.player.potionSlots;

        for(int i = 0; i < slots; ++i) {
            result.add(String.valueOf(i));
        }

        if (result.contains(tokens[depth]) && tokens.length > depth + 1) {
            result.clear();
            List<String> allPotions = PotionHelper.getPotions(AbstractPlayer.PlayerClass.IRONCLAD, true);

            for (String key : allPotions) {
                result.add(key.replace(' ', '_'));
            }

            if (result.contains(tokens[depth + 1])) {
                complete = true;
            }
        }

        return result;
    }

    public void errorMsg() {
        cmdPotionHelp();
    }

    public static void cmdPotionHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* list");
        DevConsole.log("* [slot] [id]");
    }
}

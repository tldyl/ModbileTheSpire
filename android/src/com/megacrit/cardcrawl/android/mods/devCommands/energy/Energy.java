package com.megacrit.cardcrawl.android.mods.devCommands.energy;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.helpers.ConvertHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class Energy extends ConsoleCommand {
    public Energy() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 2;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        if (tokens[1].toLowerCase().equals("add") && tokens.length > 2) {
            AbstractDungeon.player.gainEnergy(ConvertHelper.tryParseInt(tokens[2], 0));
        } else if (tokens[1].toLowerCase().equals("lose") && tokens.length > 2) {
            AbstractDungeon.player.loseEnergy(ConvertHelper.tryParseInt(tokens[2], 0));
        } else if (tokens[1].toLowerCase().equals("inf")) {
            DevConsole.infiniteEnergy = !DevConsole.infiniteEnergy;
            if (DevConsole.infiniteEnergy) {
                AbstractDungeon.player.gainEnergy(9999);
            }
        } else {
            cmdEnergyHelp();
        }

    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("add");
        result.add("lose");
        result.add("inf");
        if (tokens.length == depth + 1) {
            if (tokens[depth].equalsIgnoreCase("inf")) {
                complete = true;
            }

            return result;
        } else {
            if (!tokens[depth].equalsIgnoreCase("add") && !tokens[depth].equalsIgnoreCase("lose")) {
                if (tokens[depth].equalsIgnoreCase("inf")) {
                    complete = true;
                }
            } else {
                if (tokens[depth + 1].matches("\\d+")) {
                    complete = true;
                }

                result = smallNumbers();
            }

            return result;
        }
    }

    @Override
    public void errorMsg() {
        cmdEnergyHelp();
    }

    private static void cmdEnergyHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [amt]");
        DevConsole.log("* lose [amt]");
        DevConsole.log("* inf");
    }
}

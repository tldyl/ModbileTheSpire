package com.megacrit.cardcrawl.android.mods.devCommands.power;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.ConsoleTargetedPower;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;

import java.util.ArrayList;

public class Power extends ConsoleCommand {
    public Power() {
        this.requiresPlayer = false;
        this.minExtraTokens = 2;
        this.maxExtraTokens = 2;
    }

    public void execute(String[] tokens, int depth) {
        String powerID = "";
        int amount = 1;

        for(int i = 1; i < tokens.length - 1; ++i) {
            powerID = powerID.concat(tokens[i]).concat(" ");
        }

        try {
            amount = Integer.parseInt(tokens[tokens.length - 1]);
        } catch (Exception var9) {
            powerID = powerID.concat(tokens[tokens.length - 1]);
        }

        powerID = powerID.trim();
        if (BaseMod.underScorePowerIDs.containsKey(powerID)) {
            powerID = BaseMod.underScorePowerIDs.get(powerID);
        }

        Class power;
        try {
            power = BaseMod.getPowerClass(powerID);
        } catch (Exception var8) {
            BaseMod.logger.info("failed to load power " + powerID);
            DevConsole.log("could not load power");
            cmdPowerHelp();
            return;
        }

        try {
            new ConsoleTargetedPower(power, amount);
        } catch (Exception var7) {
            DevConsole.log("could not make power");
            cmdPowerHelp();
        }

    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (String key : BaseMod.getPowerKeys()) {
            result.add(key.replace(' ', '_'));
        }

        if (result.contains(tokens[depth]) && tokens.length > depth + 1) {
            result.clear();
            result = ConsoleCommand.smallNumbers();
            if (tokens[depth + 1].matches("\\d+")) {
                complete = true;
            }
        }

        return result;
    }

    public void errorMsg() {
        cmdPowerHelp();
    }

    private static void cmdPowerHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* [id] [amt]");
    }
}

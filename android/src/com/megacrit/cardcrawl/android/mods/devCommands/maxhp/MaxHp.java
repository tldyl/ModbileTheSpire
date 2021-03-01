package com.megacrit.cardcrawl.android.mods.devCommands.maxhp;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class MaxHp extends ConsoleCommand {
    public MaxHp() {
        this.requiresPlayer = true;
        this.minExtraTokens = 2;
        this.maxExtraTokens = 2;
    }

    public void execute(String[] tokens, int depth) {
        int i;
        if (!tokens[1].toLowerCase().equals("add") && !tokens[1].toLowerCase().equals("a")) {
            if (!tokens[1].toLowerCase().equals("lose") && !tokens[1].toLowerCase().equals("l")) {
                cmdMaxHPHelp();
            } else {
                try {
                    i = Integer.parseInt(tokens[2]);
                    AbstractDungeon.player.decreaseMaxHealth(i);
                } catch (Exception var5) {
                    cmdMaxHPHelp();
                }
            }
        } else {
            try {
                i = Integer.parseInt(tokens[2]);
                AbstractDungeon.player.increaseMaxHp(i, true);
            } catch (Exception var6) {
                cmdMaxHPHelp();
            }
        }

    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("add");
        result.add("lose");
        if (tokens.length == depth + 1) {
            return result;
        } else {
            if (result.contains(tokens[depth])) {
                if (tokens[depth + 1].matches("\\d+")) {
                    complete = true;
                }

                result = smallNumbers();
            }

            return result;
        }
    }

    public void errorMsg() {
        cmdMaxHPHelp();
    }

    private static void cmdMaxHPHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [amt]");
        DevConsole.log("* lose [amt]");
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.draw;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.helpers.ConvertHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class Draw extends ConsoleCommand {
    public Draw() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        if (tokens.length != 2) {
            cmdDrawHelp();
        } else {
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, ConvertHelper.tryParseInt(tokens[1], 0)));
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (tokens[depth].matches("\\d+")) {
            complete = true;
        }

        return ConsoleCommand.smallNumbers();
    }

    @Override
    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* draw [amt]");
    }
}

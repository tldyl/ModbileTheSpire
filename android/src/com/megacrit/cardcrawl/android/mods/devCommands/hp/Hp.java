package com.megacrit.cardcrawl.android.mods.devCommands.hp;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class Hp extends ConsoleCommand {
    public Hp() {
        this.requiresPlayer = true;
        this.minExtraTokens = 2;
        this.maxExtraTokens = 2;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        int i;
        if (!tokens[1].toLowerCase().equals("add") && !tokens[1].toLowerCase().equals("a")) {
            if (!tokens[1].toLowerCase().equals("lose") && !tokens[1].toLowerCase().equals("l")) {
                cmdHPHelp();
            } else {
                try {
                    i = Integer.parseInt(tokens[2]);
                    AbstractDungeon.actionManager.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, i, AbstractGameAction.AttackEffect.NONE));
                } catch (Exception var5) {
                    cmdHPHelp();
                }
            }
        } else {
            try {
                i = Integer.parseInt(tokens[2]);
                AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, i));
            } catch (Exception var6) {
                cmdHPHelp();
            }
        }

    }

    @Override
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

    @Override
    public void errorMsg() {
        cmdHPHelp();
    }

    private static void cmdHPHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [amt]");
        DevConsole.log("* lose [amt]");
    }
}

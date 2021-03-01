package com.megacrit.cardcrawl.android.mods.devCommands.kill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class Kill extends ConsoleCommand {
    public Kill() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        switch (tokens[1].toLowerCase()) {
            case "all":
                int monsterCount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
                int[] multiDamage = new int[monsterCount];
                for (int i = 0; i < monsterCount; ++i) {
                    multiDamage[i] = 99999;
                }
                AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
                break;
            case "self":
                AbstractDungeon.actionManager.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 99999));
                break;
            default:
                cmdKillHelp();
                break;
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("all");
        result.add("self");
        return result;
    }

    @Override
    public void errorMsg() {
        cmdKillHelp();
    }

    private static void cmdKillHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* all");
        DevConsole.log("* self");
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.relic;

import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Arrays;

public class RelicRemove extends ConsoleCommand {
    public RelicRemove() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String[] relicNameArray = Arrays.copyOfRange(tokens, 2, tokens.length);
        String relicName = Relic.getRelicName(relicNameArray);
        AbstractDungeon.player.loseRelic(relicName);
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            result.add(relic.relicId.replace(' ', '_'));
        }
        return result;
    }

    @Override
    public void errorMsg() {
        Relic.cmdRelicHelp();
    }
}

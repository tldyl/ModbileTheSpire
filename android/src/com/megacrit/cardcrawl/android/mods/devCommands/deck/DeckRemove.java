package com.megacrit.cardcrawl.android.mods.devCommands.deck;

import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class DeckRemove extends DeckManipulator {
    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens[2].equals("all")) {
            for (String str : AbstractDungeon.player.masterDeck.getCardNames()) {
                AbstractDungeon.player.masterDeck.removeCard(str);
            }
        } else {
            AbstractDungeon.player.masterDeck.removeCard(this.getCardID(tokens));
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = ConsoleCommand.getCardOptionsFromCardGroup(AbstractDungeon.player.masterDeck);
        result.add("all");
        return result;
    }
}

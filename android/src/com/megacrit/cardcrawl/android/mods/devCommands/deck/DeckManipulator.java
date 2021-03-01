package com.megacrit.cardcrawl.android.mods.devCommands.deck;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.helpers.ConvertHelper;

import java.util.Arrays;

public abstract class DeckManipulator extends ConsoleCommand {
    public DeckManipulator() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
    }

    protected String getCardID(String[] tokens) {
        return this.getCardID(tokens, this.countIndex(tokens));
    }

    protected String getCardID(String[] tokens, int countIndex) {
        String[] cardNameArray = Arrays.copyOfRange(tokens, 2, countIndex + 1);
        String cardName = String.join(" ", cardNameArray);
        if (BaseMod.underScoreCardIDs.containsKey(cardName)) {
            cardName = BaseMod.underScoreCardIDs.get(cardName);
        }

        return cardName;
    }

    protected int countIndex(String[] tokens) {
        int countIndex;
        for(countIndex = tokens.length - 1; ConvertHelper.tryParseInt(tokens[countIndex]) != null; --countIndex) {
            ;
        }

        return countIndex;
    }

    public void errorMsg() {
        Deck.cmdDeckHelp();
    }
}

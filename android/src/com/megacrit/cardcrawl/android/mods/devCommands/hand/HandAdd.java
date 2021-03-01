package com.megacrit.cardcrawl.android.mods.devCommands.hand;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.helpers.ConvertHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

public class HandAdd extends ConsoleCommand {
    public HandAdd() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 3;
    }

    public void execute(String[] tokens, int depth) {
        int countIndex = Hand.countIndex(tokens);
        String cardName = Hand.cardName(tokens, countIndex);
        AbstractCard c = CardLibrary.getCard(cardName);
        if (c != null) {
            int count = 1;
            if (tokens.length > countIndex + 1 && ConvertHelper.tryParseInt(tokens[countIndex + 1]) != null) {
                count = ConvertHelper.tryParseInt(tokens[countIndex + 1], 0);
            }

            int upgradeCount = 0;
            if (tokens.length > countIndex + 2) {
                upgradeCount = ConvertHelper.tryParseInt(tokens[countIndex + 2], 0);
            }

            DevConsole.log("adding " + count + (count == 1 ? " copy of " : " copies of ") + cardName + " with " + upgradeCount + " upgrade(s)");

            for(int i = 0; i < count; ++i) {
                AbstractCard copy = c.makeCopy();

                for(int j = 0; j < upgradeCount; ++j) {
                    copy.upgrade();
                }

                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(copy, true));
            }
        } else {
            DevConsole.log("could not find card " + cardName);
        }

    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> options = ConsoleCommand.getCardOptions();
        if (options.contains(tokens[depth])) {
            if (tokens.length > depth + 1 && tokens[depth + 1].matches("\\d*")) {
                if (tokens.length > depth + 2) {
                    if (tokens[depth + 2].matches("\\d+")) {
                        ConsoleCommand.complete = true;
                    } else if (tokens[depth + 2].length() > 0) {
                        tooManyTokensError();
                    }
                }

                return ConsoleCommand.smallNumbers();
            }

            if (tokens.length > depth + 1) {
                tooManyTokensError();
            }
        } else if (tokens.length > depth + 1) {
            tooManyTokensError();
        }

        return options;
    }

    public void errorMsg() {
        Hand.cmdHandHelp();
    }
}

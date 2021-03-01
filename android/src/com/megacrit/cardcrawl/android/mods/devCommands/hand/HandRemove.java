package com.megacrit.cardcrawl.android.mods.devCommands.hand;

import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class HandRemove extends ConsoleCommand {
    public HandRemove() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        int countIndex = Hand.countIndex(tokens);
        String cardName = Hand.cardName(tokens, countIndex);
        AbstractCard toRemove;
        if (tokens[2].equals("all")) {

            for (AbstractCard o : new ArrayList<>(AbstractDungeon.player.hand.group)) {
                toRemove = o;
                AbstractDungeon.player.hand.moveToExhaustPile(toRemove);
            }

        } else {
            boolean removed = false;
            toRemove = null;

            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (removed) {
                    break;
                }

                if (c.cardID.equals(cardName)) {
                    toRemove = c;
                    removed = true;
                }
            }

            if (removed) {
                AbstractDungeon.player.hand.moveToExhaustPile(toRemove);
            }

        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = ConsoleCommand.getCardOptionsFromCardGroup(AbstractDungeon.player.hand);
        result.add("all");
        return result;
    }

    public void errorMsg() {
        Hand.cmdHandHelp();
    }
}

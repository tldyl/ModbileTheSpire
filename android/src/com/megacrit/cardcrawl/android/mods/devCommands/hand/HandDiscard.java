package com.megacrit.cardcrawl.android.mods.devCommands.hand;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class HandDiscard extends ConsoleCommand {
    public HandDiscard() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if (tokens[2].equals("all")) {

            for (AbstractCard c : new ArrayList<>(AbstractDungeon.player.hand.group)) {
                AbstractDungeon.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
        } else {
            String cardName = Hand.cardName(tokens);

            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.cardID.equals(cardName)) {
                    AbstractDungeon.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                    return;
                }
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

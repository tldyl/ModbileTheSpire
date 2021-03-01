package com.megacrit.cardcrawl.android.mods.devCommands.hand;

import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class HandSet extends ConsoleCommand {
    public HandSet() {
        this.requiresPlayer = true;
        this.minExtraTokens = 3;
        this.maxExtraTokens = 3;
    }

    public void execute(String[] tokens, int depth) {
        if (!tokens[2].equalsIgnoreCase("damage") && !tokens[2].equalsIgnoreCase("block") && !tokens[2].equalsIgnoreCase("magic") && !tokens[2].equalsIgnoreCase("cost") && !tokens[2].equalsIgnoreCase("d") && !tokens[2].equalsIgnoreCase("b") && !tokens[2].equalsIgnoreCase("m") && !tokens[2].equalsIgnoreCase("c")) {
            Hand.cmdHandHelp();
        } else {
            try {
                String cardName = tokens[3];
                boolean all = tokens[3].equals("all");
                int v = Integer.parseInt(tokens[4]);
                Iterator handIterator = new ArrayList<>(AbstractDungeon.player.hand.group).iterator();

                do {
                    AbstractCard c;
                    do {
                        if (!handIterator.hasNext()) {
                            return;
                        }

                        c = (AbstractCard)handIterator.next();
                    } while(!all && !c.cardID.equals(cardName));

                    if (tokens[2].equalsIgnoreCase("damage") || tokens[2].equalsIgnoreCase("d")) {
                        if (c.baseDamage != v) {
                            c.upgradedDamage = true;
                        }

                        c.baseDamage = v;
                    }

                    if (tokens[2].equalsIgnoreCase("block") || tokens[2].equalsIgnoreCase("b")) {
                        if (c.baseBlock != v) {
                            c.upgradedBlock = true;
                        }

                        c.baseBlock = v;
                    }

                    if (tokens[2].equalsIgnoreCase("magic") || tokens[2].equalsIgnoreCase("m")) {
                        if (c.baseMagicNumber != v) {
                            c.upgradedMagicNumber = true;
                        }

                        c.magicNumber = c.baseMagicNumber = v;
                    }

                    if (tokens[2].equalsIgnoreCase("cost") || tokens[2].equalsIgnoreCase("c")) {
                        if (c.cost != v) {
                            c.upgradedCost = true;
                        }

                        c.cost = v;
                        c.baseBlock = v;
                        c.costForTurn = v;
                    }

                    c.displayUpgrades();
                    c.applyPowers();
                } while(all);
            } catch (NumberFormatException var8) {
                Hand.cmdHandHelp();
            }
        }

    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> options = new ArrayList<>();
        options.add("damage");
        options.add("block");
        options.add("magic");
        options.add("cost");
        if (options.contains(tokens[depth])) {
            options.clear();
            options = ConsoleCommand.getCardOptionsFromCardGroup(AbstractDungeon.player.hand);
            options.add("all");
            if (tokens.length == depth + 2) {
                return options;
            }

            if (tokens.length > depth + 2 && options.contains(tokens[depth + 1])) {
                options = ConsoleCommand.smallNumbers();
                if (tokens[depth + 2].matches("\\d+")) {
                    complete = true;
                }

                return options;
            }

            tooManyTokensError();
        } else if (tokens.length > depth + 1) {
            tooManyTokensError();
        }

        return options;
    }

    public void errorMsg() {
        Hand.cmdHandHelp();
    }
}

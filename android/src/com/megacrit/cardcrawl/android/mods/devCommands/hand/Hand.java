package com.megacrit.cardcrawl.android.mods.devCommands.hand;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.helpers.ConvertHelper;

import java.util.Arrays;

public class Hand extends ConsoleCommand {
    public Hand() {
        this.followup.put("add", HandAdd.class);
        this.followup.put("remove", HandRemove.class);
        this.followup.put("discard", HandDiscard.class);
        this.followup.put("set", HandSet.class);
        this.requiresPlayer = true;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        cmdHandHelp();
    }

    public void errorMsg() {
        cmdHandHelp();
    }

    public static int countIndex(String[] tokens) {
        int countIndex = tokens.length - 1;
        while (ConvertHelper.tryParseInt(tokens[countIndex]) != null && countIndex > 0) {
            --countIndex;
        }
        return countIndex;
    }

    public static String cardName(String[] tokens) {
        return cardName(tokens, countIndex(tokens));
    }

    public static String cardName(String[] tokens, int countIndex) {
        String[] cardNameArray = Arrays.copyOfRange(tokens, 2, countIndex + 1);
        String cardName = String.join(" ", cardNameArray);
        if (BaseMod.underScoreCardIDs.containsKey(cardName)) {
            cardName = BaseMod.underScoreCardIDs.get(cardName);
        }

        return cardName;
    }

    public static void cmdHandHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [id] {count} {upgrade amt}");
        DevConsole.log("* remove [id]");
        DevConsole.log("* remove all");
        DevConsole.log("* discard [id]");
        DevConsole.log("* discard all");
        DevConsole.log("* set damage [id] [amount]");
        DevConsole.log("* set block [id] [amount]");
        DevConsole.log("* set magic [id] [amount]");
        DevConsole.log("* set cost [id] [amount]");
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.deck;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;

public class Deck extends ConsoleCommand {
    public Deck() {
        this.followup.put("add", DeckAdd.class);
        this.followup.put("remove", DeckRemove.class);
        this.requiresPlayer = true;
    }

    @Override
    protected void execute(String[] var1, int var2) {
        cmdDeckHelp();
    }

    public void errorMsg() {
        cmdDeckHelp();
    }

    public static void cmdDeckHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [id] {count} {upgrade amt}");
        DevConsole.log("* remove [id]");
        DevConsole.log("* remove all");
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.blight;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;

public class Blight extends ConsoleCommand {
    public Blight() {
        this.followup.put("add", BlightAdd.class);
        this.followup.put("remove", BlightRemove.class);
        this.requiresPlayer = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        cmdBlightHelp();
    }

    @Override
    public void errorMsg() {
        cmdBlightHelp();
    }

    public static void cmdBlightHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [id]");
        DevConsole.log("* remove [id]");
    }
}

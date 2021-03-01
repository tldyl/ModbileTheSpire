package com.megacrit.cardcrawl.android.mods.devCommands.info;

import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;

public class Info extends ConsoleCommand {
    public Info() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (!tokens[1].equals("true") && !tokens[1].equals("false")) {
            this.errorMsg();
        } else {
            try {
                Settings.isInfo = Boolean.parseBoolean(tokens[1]);
                DevConsole.log("Setting info mode to: " + Settings.isInfo);
            } catch (Exception var4) {
                this.errorMsg();
            }
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("true");
        result.add("false");
        return result;
    }

    @Override
    public void errorMsg() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* true");
        DevConsole.log("* false");
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.relic;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;

public class Relic extends ConsoleCommand {
    public Relic() {
        this.followup.put("add", RelicAdd.class);
        this.followup.put("remove", RelicRemove.class);
        this.followup.put("desc", RelicDescription.class);
        this.followup.put("flavor", RelicFlavor.class);
        this.followup.put("list", RelicList.class);
        this.followup.put("pool", RelicPool.class);
    }

    @Override
    public void execute(String[] tokens, int depth) {
        cmdRelicHelp();
    }

    public static String getRelicName(String[] relicNameArray) {
        String relic = String.join(" ", relicNameArray);
        if (BaseMod.underScoreRelicIDs.containsKey(relic)) {
            relic = BaseMod.underScoreRelicIDs.get(relic);
        }

        return relic;
    }

    @Override
    public void errorMsg() {
        cmdRelicHelp();
    }

    public static void cmdRelicHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* remove [id]");
        DevConsole.log("* add [id]");
        DevConsole.log("* desc [id]");
        DevConsole.log("* flavor [id]");
        DevConsole.log("* pool [id]");
        DevConsole.log("* list [type]");
    }
}

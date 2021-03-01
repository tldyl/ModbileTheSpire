package com.megacrit.cardcrawl.android.mods.devCommands.relic;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RelicList extends ConsoleCommand {
    public RelicList() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String tier = tokens[2];
        List<AbstractRelic> list;
        switch (tier) {
            case "starter":
                list = RelicLibrary.starterList;
                break;
            case "common":
                list = RelicLibrary.commonList;
                break;
            case "uncommon":
                list = RelicLibrary.uncommonList;
                break;
            case "rare":
                list = RelicLibrary.rareList;
                break;
            case "boss":
                list = RelicLibrary.bossList;
                break;
            case "special":
                list = RelicLibrary.specialList;
                break;
            case "shop":
                list = RelicLibrary.shopList;
                break;
            default:
                DevConsole.log("options are: starter common uncommon rare boss special shop");
                return;
        }
        Collections.sort(list);
        BaseMod.logger.info("{}", list.toString());
        DevConsole.log(list);
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("starter");
        result.add("common");
        result.add("uncommon");
        result.add("rare");
        result.add("boss");
        result.add("special");
        result.add("shop");
        return result;
    }

    @Override
    public void errorMsg() {
        Relic.cmdRelicHelp();
    }
}

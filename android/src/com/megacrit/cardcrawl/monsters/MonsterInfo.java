package com.megacrit.cardcrawl.monsters;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MonsterInfo implements Comparable<MonsterInfo> {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(MonsterInfo.class);
    private static int calls = 0;
    public String name;
    public float weight;

    public MonsterInfo(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    public static void normalizeWeights(ArrayList<MonsterInfo> monsters) {
        calls++;
        switch (calls) {
            case 1:
                monsters.addAll(BaseMod.getMonsterEncounters(AbstractDungeon.id));
                break;
            case 2:
                monsters.addAll(BaseMod.getStrongMonsterEncounters(AbstractDungeon.id));
                break;
            case 3:
                monsters.addAll(BaseMod.getEliteEncounters(AbstractDungeon.id));
                calls = 0;
                break;
        }
        Collections.sort(monsters);
        float total = 0.0F;

        Iterator var2;
        MonsterInfo i;
        for(var2 = monsters.iterator(); var2.hasNext(); total += i.weight) {
            i = (MonsterInfo)var2.next();
        }

        var2 = monsters.iterator();

        while(var2.hasNext()) {
            i = (MonsterInfo)var2.next();
            i.weight /= total;
            if (Settings.isInfo) {
                logger.info(i.name + ": " + i.weight + "%");
            }
        }

    }

    public static String roll(ArrayList<MonsterInfo> list, float roll) {
        float currentWeight = 0.0F;
        Iterator var3 = list.iterator();

        MonsterInfo i;
        do {
            if (!var3.hasNext()) {
                return "ERROR";
            }

            i = (MonsterInfo)var3.next();
            currentWeight += i.weight;
        } while(roll >= currentWeight);

        return i.name;
    }

    public int compareTo(MonsterInfo other) {
        return other == null ? 1 : Float.compare(this.weight, other.weight);
    }
}

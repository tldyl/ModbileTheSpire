package com.megacrit.cardcrawl.android.mods.helpers;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.HashMap;

public class CountModdedUnlockCards {
    private static SpireAndroidLogger logger = SpireAndroidLogger.getLogger(CountModdedUnlockCards.class);
    public static HashMap<AbstractPlayer.PlayerClass, Integer> lockedCardCounts = new HashMap<>();
    public static HashMap<AbstractPlayer.PlayerClass, Integer> unlockedCardCounts = new HashMap<>();
    public static boolean enabled = false;

    public static int getUnlockedCardCount(AbstractPlayer.PlayerClass c) {
        return unlockedCardCounts.getOrDefault(c, 0);
    }

    public static int getLockedCardCount(AbstractPlayer.PlayerClass c, int defaultValue) {
        return lockedCardCounts.getOrDefault(c, defaultValue);
    }
}

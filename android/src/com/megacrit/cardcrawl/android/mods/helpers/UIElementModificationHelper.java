package com.megacrit.cardcrawl.android.mods.helpers;

import com.megacrit.cardcrawl.helpers.Hitbox;

public class UIElementModificationHelper {
    public static void moveHitboxByOriginalParameters(Hitbox hb, float x, float y) {
        hb.move(x + hb.width / 2.0F, y + hb.height / 2.0F);
    }
}

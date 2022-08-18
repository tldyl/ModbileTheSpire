package com.megacrit.cardcrawl.android.mods.interfaces;

import com.megacrit.cardcrawl.rooms.AbstractRoom;

public interface PostBattleSubscriber extends ISubscriber {
    void receivePostBattle(AbstractRoom room);
}

package com.megacrit.cardcrawl.android.mods;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class CustomEventRoom extends EventRoom {
    public CustomEventRoom() {
    }

    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        String eventName = AbstractDungeon.eventList.remove(0);
        this.event = EventHelper.getEvent(eventName);
        this.event.onEnterRoom();
    }
}

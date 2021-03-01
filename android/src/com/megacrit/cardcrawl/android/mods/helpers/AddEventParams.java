package com.megacrit.cardcrawl.android.mods.helpers;

import com.megacrit.cardcrawl.android.mods.utils.Condition;
import com.megacrit.cardcrawl.android.mods.utils.EventUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.AbstractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddEventParams {
    public String eventID;
    public Class<? extends AbstractEvent> eventClass;
    public EventUtils.EventType eventType;
    public List<String> dungeonIDs;
    public AbstractPlayer.PlayerClass playerClass;
    public Condition spawnCondition;
    public Condition bonusCondition;
    public String overrideEventID;

    public AddEventParams() {
        this.eventType = EventUtils.EventType.NORMAL;
        this.dungeonIDs = new ArrayList<>();
        this.playerClass = null;
        this.spawnCondition = null;
        this.bonusCondition = null;
        this.overrideEventID = null;
    }

    public static class Builder {
        private AddEventParams params = new AddEventParams();

        public Builder(String eventID, Class<? extends AbstractEvent> eventClass) {
            this.params.eventID = eventID;
            this.params.eventClass = eventClass;
        }

        public AddEventParams create() {
            return this.params;
        }

        public AddEventParams.Builder dungeonID(String dungeonID) {
            this.params.dungeonIDs.add(dungeonID);
            return this;
        }

        public AddEventParams.Builder dungeonIDs(String... dungeonIDs) {
            Collections.addAll(this.params.dungeonIDs, dungeonIDs);
            return this;
        }

        public AddEventParams.Builder playerClass(AbstractPlayer.PlayerClass playerClass) {
            this.params.playerClass = playerClass;
            return this;
        }

        public AddEventParams.Builder spawnCondition(Condition spawnCondition) {
            this.params.spawnCondition = spawnCondition;
            return this;
        }

        public AddEventParams.Builder bonusCondition(Condition bonusCondition) {
            this.params.bonusCondition = bonusCondition;
            return this;
        }

        public AddEventParams.Builder overrideEvent(String overrideEventID) {
            this.params.overrideEventID = overrideEventID;
            return this;
        }

        public AddEventParams.Builder eventType(EventUtils.EventType eventType) {
            this.params.eventType = eventType;
            return this;
        }
    }
}

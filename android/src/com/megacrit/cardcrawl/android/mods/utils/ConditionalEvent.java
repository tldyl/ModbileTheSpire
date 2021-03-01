package com.megacrit.cardcrawl.android.mods.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class ConditionalEvent<T extends AbstractEvent> {
    public Class<T> eventClass;
    public AbstractPlayer.PlayerClass playerClass;
    public Condition spawnCondition;
    public List<String> actIDs;
    public String overrideEvent = "";

    public ConditionalEvent(Class<T> eventClass, AbstractPlayer.PlayerClass playerClass, Condition spawnCondition, String[] actIDs) {
        this.eventClass = eventClass;
        this.playerClass = playerClass;
        this.spawnCondition = spawnCondition;
        if (spawnCondition == null) {
            this.spawnCondition = () -> true;
        }

        this.actIDs = Arrays.asList(actIDs);
    }

    public AbstractEvent getEvent() {
        try {
            return this.eventClass.getConstructor().newInstance();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException var2) {
            EventUtils.eventLogger.info("Failed to instantiate event " + this.eventClass.getName());
            var2.printStackTrace();
            return null;
        }
    }

    public boolean isValid() {
        return (this.actIDs.isEmpty() || this.actIDs.contains(AbstractDungeon.id)) && this.spawnCondition.test() && (this.playerClass == null || AbstractDungeon.player.chosenClass == this.playerClass);
    }

    public String toString() {
        return this.eventClass.getSimpleName();
    }

    public String getConditions() {
        return (this.playerClass != null ? this.playerClass.name().toUpperCase() : "ANY") + " | " + (this.actIDs.isEmpty() ? "ANY" : this.actIDs);
    }
}

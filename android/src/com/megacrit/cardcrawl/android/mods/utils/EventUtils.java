package com.megacrit.cardcrawl.android.mods.utils;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.lang.reflect.Field;
import java.util.*;

public class EventUtils {
    public static SpireAndroidLogger eventLogger = SpireAndroidLogger.getLogger(EventUtils.class);
    public static HashMap<String, Condition> normalEventBonusConditions = new HashMap<>();
    public static HashMap<String, Condition> specialEventBonusConditions = new HashMap<>();
    public static HashMap<ConditionalEvent<? extends AbstractEvent>, Condition> overrideBonusConditions = new HashMap<>();
    public static HashMap<String, ConditionalEvent<? extends AbstractEvent>> normalEvents = new HashMap<>();
    public static HashMap<String, ConditionalEvent<? extends AbstractEvent>> shrineEvents = new HashMap<>();
    public static HashMap<String, ConditionalEvent<? extends AbstractEvent>> oneTimeEvents = new HashMap<>();
    public static HashMap<String, ConditionalEvent<? extends AbstractEvent>> fullReplaceEvents = new HashMap<>();
    public static HashMap<String, ArrayList<ConditionalEvent<? extends AbstractEvent>>> overrideEvents = new HashMap<>();
    public static HashMap<String, ArrayList<ConditionalEvent<? extends AbstractEvent>>> fullReplaceEventList = new HashMap<>();
    public static HashSet<String> eventIDs = new HashSet<>();
    private static int id = 0;

    public EventUtils() {
    }

    public static <T extends AbstractEvent> void registerEvent(String ID, Class<T> eventClass, AbstractPlayer.PlayerClass playerClass, String[] actIDs, Condition spawnCondition, String overrideEvent, Condition bonusCondition, EventUtils.EventType type) {
        ID = ID.replace(' ', '_');
        if (eventIDs.contains(ID)) {
            ID = generateEventKey(ID);
        }

        eventIDs.add(ID);
        ConditionalEvent<T> c = new ConditionalEvent<>(eventClass, playerClass, spawnCondition, actIDs == null ? new String[0] : actIDs);
        if (type == EventUtils.EventType.FULL_REPLACE && overrideEvent != null) {
            c.overrideEvent = ID;
            if (!fullReplaceEventList.containsKey(overrideEvent)) {
                fullReplaceEventList.put(overrideEvent, new ArrayList<>());
            }

            fullReplaceEventList.get(overrideEvent).add(c);
            fullReplaceEvents.put(ID, c);
            eventLogger.info("Full Replacement event " + c + " for event " + overrideEvent + " registered. " + c.getConditions());
            if (bonusCondition != null) {
                normalEventBonusConditions.put(ID, bonusCondition);
                specialEventBonusConditions.put(ID, bonusCondition);
                eventLogger.info("  This event has a bonus condition.");
            }
        } else if (overrideEvent != null) {
            c.overrideEvent = overrideEvent;
            if (!overrideEvents.containsKey(overrideEvent)) {
                overrideEvents.put(overrideEvent, new ArrayList<>());
            }

            overrideEvents.get(overrideEvent).add(c);
            eventLogger.info("Override event " + c + " for event " + overrideEvent + " registered. " + c.getConditions());
            if (bonusCondition != null) {
                overrideBonusConditions.put(c, bonusCondition);
                eventLogger.info("  This event has a bonus condition.");
            }
        } else if (type == EventUtils.EventType.ONE_TIME) {
            oneTimeEvents.put(ID, c);
            eventLogger.info("SpecialOneTimeEvent " + c + " registered. " + c.getConditions());
            if (bonusCondition != null) {
                specialEventBonusConditions.put(ID, bonusCondition);
                eventLogger.info("  This event has a bonus condition.");
            }
        } else if (type == EventUtils.EventType.SHRINE) {
            shrineEvents.put(ID, c);
            eventLogger.info("Shrine " + c + " registered. " + c.getConditions());
            if (bonusCondition != null) {
                specialEventBonusConditions.put(ID, bonusCondition);
                eventLogger.info("  This event has a bonus condition.");
            }
        } else {
            normalEvents.put(ID, c);
            eventLogger.info("Event " + c + " registered. " + c.getConditions());
            if (bonusCondition != null) {
                normalEventBonusConditions.put(ID, bonusCondition);
                eventLogger.info("  This event has a bonus condition.");
            }
        }
    }

    private static String generateEventKey(String ID) {
        return ID + id++;
    }

    public static AbstractEvent getEvent(String eventID) {
        if (normalEvents.containsKey(eventID)) {
            return normalEvents.get(eventID).getEvent();
        } else if (shrineEvents.containsKey(eventID)) {
            return shrineEvents.get(eventID).getEvent();
        } else if (oneTimeEvents.containsKey(eventID)) {
            return oneTimeEvents.get(eventID).getEvent();
        } else {
            return fullReplaceEvents.containsKey(eventID) ? fullReplaceEvents.get(eventID).getEvent() : null;
        }
    }

    public static Class<? extends AbstractEvent> getEventClass(String eventID) {
        if (normalEvents.containsKey(eventID)) {
            return normalEvents.get(eventID).eventClass;
        } else if (shrineEvents.containsKey(eventID)) {
            return shrineEvents.get(eventID).eventClass;
        } else if (oneTimeEvents.containsKey(eventID)) {
            return oneTimeEvents.get(eventID).eventClass;
        } else {
            return fullReplaceEvents.containsKey(eventID) ? fullReplaceEvents.get(eventID).eventClass : null;
        }
    }

    public static HashMap<String, Class<? extends AbstractEvent>> getDungeonEvents(String dungeonID) {
        HashMap<String, Class<? extends AbstractEvent>> events = new HashMap<>();
        Iterator<Map.Entry<String, ConditionalEvent<? extends AbstractEvent>>> entryIterator = normalEvents.entrySet().iterator();

        Map.Entry<String, ConditionalEvent<? extends AbstractEvent>> c;
        while(entryIterator.hasNext()) {
            c = entryIterator.next();
            if (c.getValue().actIDs.contains(dungeonID)) {
                events.put(c.getKey(), c.getValue().eventClass);
            }
        }

        entryIterator = shrineEvents.entrySet().iterator();

        while(entryIterator.hasNext()) {
            c = entryIterator.next();
            if (c.getValue().actIDs.contains(dungeonID)) {
                events.put(c.getKey(), c.getValue().eventClass);
            }
        }

        return events;
    }

    @SuppressWarnings("unchecked")
    public static void loadBaseEvents() {
        try {
            Field eventStrings = LocalizedStrings.class.getDeclaredField("events");
            eventStrings.setAccessible(true);
            Map<String, EventStrings> events = (Map<String, EventStrings>) eventStrings.get(null);
            if (events != null) {

                for (String key : events.keySet()) {
                    eventIDs.add(key.replace(' ', '_'));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException var4) {
            var4.printStackTrace();
        }

    }

    public enum EventType {
        NORMAL,
        SHRINE,
        ONE_TIME,
        OVERRIDE,
        FULL_REPLACE;

        EventType() {
        }
    }
}


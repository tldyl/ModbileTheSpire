package com.megacrit.cardcrawl.android.mods.devCommands.event;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.CustomEventRoom;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.utils.EventUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EventRoom;

import java.util.ArrayList;
import java.util.Arrays;

public class Event extends ConsoleCommand {
    public Event() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if (AbstractDungeon.currMapNode == null) {
            DevConsole.log("cannot execute event when there is no map");
        } else if (tokens.length == 2 && tokens[1].toLowerCase().equals("random")) {
            RoomEventDialog.optionList.clear();
            MapRoomNode cur = AbstractDungeon.currMapNode;
            MapRoomNode node = new MapRoomNode(cur.x, cur.y);
            node.room = new EventRoom();
            ArrayList<MapEdge> curEdges = cur.getEdges();
            for (MapEdge edge : curEdges) {
                node.addEdge(edge);
            }

            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.hideCombatPanels();
            AbstractDungeon.previousScreen = null;
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.dungeonMapScreen.closeInstantly();
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.topPanel.unhoverHitboxes();
            AbstractDungeon.fadeIn();
            AbstractDungeon.effectList.clear();
            AbstractDungeon.topLevelEffects.clear();
            AbstractDungeon.topLevelEffectsQueue.clear();
            AbstractDungeon.effectsQueue.clear();
            AbstractDungeon.dungeonMapScreen.dismissable = true;
            AbstractDungeon.nextRoom = node;
            AbstractDungeon.setCurrMapNode(node);
            AbstractDungeon.getCurrRoom().onPlayerEntry();
            AbstractDungeon.scene.nextRoom(node.room);
            AbstractDungeon.rs = node.room.event instanceof AbstractImageEvent ? AbstractDungeon.RenderScene.EVENT : AbstractDungeon.RenderScene.NORMAL;
        } else {
            String[] eventArray = Arrays.copyOfRange(tokens, 1, tokens.length);
            String eventName = String.join(" ", eventArray);
            if (BaseMod.underScoreEventIDs.containsKey(eventName)) {
                eventName = BaseMod.underScoreEventIDs.get(eventName);
            }

            if (EventHelper.getEvent(eventName) == null) {
                DevConsole.couldNotParse();
                DevConsole.log(eventName + " is not an event ID");
            } else {
                RoomEventDialog.optionList.clear();
                AbstractDungeon.eventList.add(0, eventName);
                MapRoomNode cur = AbstractDungeon.currMapNode;
                MapRoomNode node = new MapRoomNode(cur.x, cur.y);
                node.room = new CustomEventRoom();
                ArrayList<MapEdge> curEdges = cur.getEdges();

                for (MapEdge edge : curEdges) {
                    node.addEdge(edge);
                }

                AbstractDungeon.player.releaseCard();
                AbstractDungeon.overlayMenu.hideCombatPanels();
                AbstractDungeon.previousScreen = null;
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.dungeonMapScreen.closeInstantly();
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.topPanel.unhoverHitboxes();
                AbstractDungeon.fadeIn();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.topLevelEffects.clear();
                AbstractDungeon.topLevelEffectsQueue.clear();
                AbstractDungeon.effectsQueue.clear();
                AbstractDungeon.dungeonMapScreen.dismissable = true;
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.setCurrMapNode(node);
                AbstractDungeon.getCurrRoom().onPlayerEntry();
                AbstractDungeon.scene.nextRoom(node.room);
                AbstractDungeon.rs = node.room.event instanceof AbstractImageEvent ? AbstractDungeon.RenderScene.EVENT : AbstractDungeon.RenderScene.NORMAL;
            }
        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>(EventUtils.eventIDs);
        result.add("random");
        return result;
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.fight;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import java.util.ArrayList;
import java.util.Arrays;

public class Fight extends ConsoleCommand {
    public Fight() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.requiresPlayer = true;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        MapRoomNode cur = AbstractDungeon.currMapNode;
        if (cur == null) {
            DevConsole.log("cannot fight when there is no map");
        } else {
            String[] encounterArray = Arrays.copyOfRange(tokens, 1, tokens.length);
            String encounterName = String.join(" ", encounterArray);
            if (BaseMod.underScoreEncounterIDs.containsKey(encounterName)) {
                encounterName = BaseMod.underScoreEncounterIDs.get(encounterName);
            }

            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                AbstractDungeon.monsterList.add(1, encounterName);
            } else {
                AbstractDungeon.monsterList.add(0, encounterName);
            }

            MapRoomNode node = new MapRoomNode(cur.x, cur.y);
            node.room = new MonsterRoom();
            ArrayList<MapEdge> curEdges = cur.getEdges();
            for (MapEdge edge : curEdges) {
                node.addEdge(edge);
            }

            AbstractDungeon.nextRoom = node;
            AbstractDungeon.nextRoomTransitionStart();
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (String id : BaseMod.encounterList) {
            result.add(id.replace(' ', '_'));
        }

        return result;
    }
}

package com.megacrit.cardcrawl.android.mods.devCommands.act;

import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActCommand extends ConsoleCommand {
    private static Map<String, Integer> acts = new HashMap<>();

    public static void initialize() {
        addAct("Exordium", 1);
        addAct("TheCity", 2);
        addAct("TheBeyond", 3);
        addAct("TheEnding", 4);
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens[depth].equalsIgnoreCase("boss")) {
            DevConsole.log("Skipping to boss room");
            this.prepareTransition();
            AbstractDungeon.currMapNode.room = new MonsterRoomBoss();
            AbstractDungeon.getCurrRoom().onPlayerEntry();
            AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
            AbstractDungeon.combatRewardScreen.clear();
            AbstractDungeon.previousScreen = null;
            AbstractDungeon.closeCurrentScreen();
            CardCrawlGame.music.silenceTempBgmInstantly();
            CardCrawlGame.music.silenceBGMInstantly();
        } else if (tokens[depth].equalsIgnoreCase("num")) {
            DevConsole.log("Current Act Number: " + AbstractDungeon.actNum);
        } else if (acts.containsKey(tokens[depth])) {
            try {
                DevConsole.log("Skipping to act " + tokens[depth]);
                if (AbstractDungeon.floorNum <= 1) {
                    AbstractDungeon.floorNum = 2;
                }

                this.prepareTransition();
                CardCrawlGame.nextDungeon = tokens[depth];
                CardCrawlGame.dungeonTransitionScreen = new DungeonTransitionScreen(tokens[depth]);
                AbstractDungeon.actNum = acts.get(tokens[depth]) - 1;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            } catch (Exception ignored) {

            }
        } else {
            DevConsole.log("Invalid Act ID");
        }
    }

    public static void addAct(String actID, int actNum) {
        if (!acts.containsKey(actID) && !actID.equalsIgnoreCase("boss") && !actID.equalsIgnoreCase("num")) {
            acts.put(actID, actNum);
        } else {
            BaseMod.logger.error("Act " + actID + " is already registered!");
        }

    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add("boss");
        tmp.add("num");

        tmp.addAll(acts.keySet());
        return tmp;
    }

    private void prepareTransition() {
        AbstractDungeon.player.hand.group.clear();
        AbstractDungeon.actionManager.clear();
        AbstractDungeon.effectsQueue.clear();
        AbstractDungeon.effectList.clear();

        for(int i = AbstractDungeon.topLevelEffects.size() - 1; i > 0; --i) {
            if (AbstractDungeon.topLevelEffects.get(i) instanceof BattleStartEffect) {
                AbstractDungeon.topLevelEffects.remove(i);
            }
        }
    }
}

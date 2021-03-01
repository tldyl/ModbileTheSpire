package com.megacrit.cardcrawl.android.mods.devCommands.history;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.android.mods.utils.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class History extends ConsoleCommand {
    public History() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        if (tokens[1].equalsIgnoreCase("random")) {
            getrandomVictorySetup();
        } else if (tokens[1].equalsIgnoreCase("last")) {
            getlastVictorySetup();
        } else {
            DevConsole.couldNotParse();
        }

    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("last");
        result.add("random");
        return result;
    }

    @Override
    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* last");
        DevConsole.log("* random");
    }

    public static void getlastVictorySetup() {
        ArrayList<RunData> rdlist = getVictories(characterIndex(AbstractDungeon.player));
        if (!rdlist.isEmpty()) {
            setLoadout(rdlist.get(rdlist.size() - 1));
        } else {
            DevConsole.log("could not find run data for the current class");
        }

    }

    public static void getrandomVictorySetup() {
        ArrayList<RunData> rdlist = getVictories(characterIndex(AbstractDungeon.player));
        if (!rdlist.isEmpty()) {
            setLoadout(rdlist.get(MathUtils.random(rdlist.size() - 1)));
        } else {
            DevConsole.log("could not find run data for the current class");
        }

    }

    public static void setLoadout(RunData rd) {
        AbstractDungeon.player.relics.clear();
        Iterator var2 = rd.relics.iterator();

        String card;
        while(var2.hasNext()) {
            card = (String)var2.next();
            AbstractRelic ar = RelicLibrary.getRelic(card);
            ar.instantObtain();
        }

        AbstractDungeon.player.masterDeck.group.clear();

        AbstractCard ac;
        for(var2 = rd.master_deck.iterator(); var2.hasNext(); AbstractDungeon.player.masterDeck.group.add(ac)) {
            card = (String)var2.next();
            if (card.matches(".*\\+\\d+")) {
                int index = card.lastIndexOf("+");
                ac = CardLibrary.getCopy(card.substring(0, index), index, 0);
            } else {
                ac = CardLibrary.getCopy(card, 0, 0);
            }
        }

        AbstractDungeon.player.maxHealth = rd.max_hp_per_floor.get(rd.max_hp_per_floor.size() - 1);
        AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
    }

    public static ArrayList<RunData> getVictories(int character) {
        new ArrayList();
        RunHistoryScreen rhs = new RunHistoryScreen();
        rhs.refreshData();
        if (character > 0) {
            ((DropdownMenu)ReflectionHacks.getPrivate(rhs, RunHistoryScreen.class, "characterFilter")).setSelectedIndex(character);
        }

        ((DropdownMenu)ReflectionHacks.getPrivate(rhs, RunHistoryScreen.class, "winLossFilter")).setSelectedIndex(1);

        try {
            Method resetRunsDropdown = RunHistoryScreen.class.getDeclaredMethod("resetRunsDropdown");
            resetRunsDropdown.setAccessible(true);
            resetRunsDropdown.invoke(rhs);
        } catch (Exception ignored) {
        }

        return ReflectionHacks.getPrivate(rhs, RunHistoryScreen.class, "filteredRuns");
    }

    public static int characterIndex(AbstractPlayer p) {
        int index = 1;

        for(Iterator var2 = CardCrawlGame.characterManager.getAllCharacters().iterator(); var2.hasNext(); ++index) {
            AbstractPlayer p2 = (AbstractPlayer)var2.next();
            if (p2.chosenClass == p.chosenClass) {
                break;
            }
        }

        return index;
    }
}

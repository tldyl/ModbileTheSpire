package com.megacrit.cardcrawl.characters;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import java.util.ArrayList;
import java.util.Iterator;

public class CharacterManager {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(CharacterManager.class);
    private static final ArrayList<AbstractPlayer> masterCharacterList = new ArrayList<>();

    public CharacterManager() {
        if (masterCharacterList.isEmpty()) {
            masterCharacterList.add(new Ironclad(CardCrawlGame.playerName));
            masterCharacterList.add(new TheSilent(CardCrawlGame.playerName));
            masterCharacterList.add(new Defect(CardCrawlGame.playerName));
            masterCharacterList.add(new Watcher(CardCrawlGame.playerName));
            BaseMod.receiveEditCharacters();
        } else {
            for (AbstractPlayer c : masterCharacterList) {
                c.loadPrefs();
            }
        }

    }

    public AbstractPlayer setChosenCharacter(PlayerClass c) {
        AbstractPlayer character = getCharacter(c);
        AbstractDungeon.player = character;
        return character;
    }

    public boolean anySaveFileExists() {
        Iterator var1 = masterCharacterList.iterator();

        AbstractPlayer character;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            character = (AbstractPlayer)var1.next();
        } while(!character.saveFileExists());

        return true;
    }

    public AbstractPlayer loadChosenCharacter() {
        Iterator var1 = masterCharacterList.iterator();

        AbstractPlayer character;
        do {
            if (!var1.hasNext()) {
                logger.info("No character save file was found!");
                return null;
            }

            character = (AbstractPlayer)var1.next();
        } while(!character.saveFileExists());

        AbstractDungeon.player = character;
        return character;
    }

    public ArrayList<CharStat> getAllCharacterStats() {
        ArrayList<CharStat> allCharStats = new ArrayList<>();
        Iterator var2 = masterCharacterList.iterator();

        while(var2.hasNext()) {
            AbstractPlayer c = (AbstractPlayer)var2.next();
            allCharStats.add(c.getCharStat());
        }

        return allCharStats;
    }

    public void refreshAllCharStats() {
        Iterator var1 = masterCharacterList.iterator();

        while(var1.hasNext()) {
            AbstractPlayer c = (AbstractPlayer)var1.next();
            c.refreshCharStat();
        }

    }

    public ArrayList<Prefs> getAllPrefs() {
        ArrayList<Prefs> allPrefs = new ArrayList<>();
        Iterator var2 = masterCharacterList.iterator();

        while(var2.hasNext()) {
            AbstractPlayer c = (AbstractPlayer)var2.next();
            allPrefs.add(c.getPrefs());
        }

        return allPrefs;
    }

    public AbstractPlayer getRandomCharacter(Random rng) {
        int index = rng.random(masterCharacterList.size() - 1);
        return masterCharacterList.get(index);
    }

    public AbstractPlayer recreateCharacter(PlayerClass p) {
        Iterator var2 = masterCharacterList.iterator();

        AbstractPlayer old;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            old = (AbstractPlayer)var2.next();
        } while(old.chosenClass != p);

        AbstractPlayer newChar = old.newInstance();
        masterCharacterList.set(masterCharacterList.indexOf(old), newChar);
        old.dispose();
        logger.info("Successfully recreated " + newChar.chosenClass.name());
        return newChar;
    }

    public AbstractPlayer getCharacter(PlayerClass c) {
        Iterator var2 = masterCharacterList.iterator();

        AbstractPlayer character;
        do {
            if (!var2.hasNext()) {
                logger.error("The character " + c.name() + " does not exist in the CharacterManager's master character list");
                return null;
            }

            character = (AbstractPlayer)var2.next();
        } while(character.chosenClass != c);

        return character;
    }

    public ArrayList<AbstractPlayer> getAllCharacters() {
        return masterCharacterList;
    }
}

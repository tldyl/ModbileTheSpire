package com.megacrit.cardcrawl.helpers;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.BotDataUploader;
import com.megacrit.cardcrawl.metrics.BotDataUploader.GameDataType;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import com.megacrit.cardcrawl.random.Random;

import java.util.*;

public class PotionHelper {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(PotionHelper.class);
    public static ArrayList<String> potions = new ArrayList<>();
    public static Map<PlayerClass, List<String>> characterPotions = new HashMap<>();
    public static Map<String, AbstractPotion> potionMap = new HashMap<>();
    public static int POTION_COMMON_CHANCE = 65;
    public static int POTION_UNCOMMON_CHANCE = 25;

    public PotionHelper() {
    }

    public static void initialize(PlayerClass chosenClass) {
        potions.clear();
        potions = getPotions(chosenClass, false);
    }

    public static ArrayList<AbstractPotion> getPotionsByRarity(PotionRarity rarity) {
        ArrayList<AbstractPotion> retVal = new ArrayList<>();

        for (String s : getPotions(null, true)) {
            AbstractPotion p = getPotion(s);
            if (p.rarity == rarity) {
                retVal.add(p);
            }
        }

        return retVal;
    }

    public static ArrayList<String> getPotions(PlayerClass c, boolean getAll) {
        ArrayList<String> retVal = new ArrayList<>();
        if (!getAll) {
            switch(c.name()) {
                case "IRONCLAD":
                    retVal.add("BloodPotion");
                    retVal.add("ElixirPotion");
                    retVal.add("HeartOfIron");
                    break;
                case "THE_SILENT":
                    retVal.add("Poison Potion");
                    retVal.add("CunningPotion");
                    retVal.add("GhostInAJar");
                    break;
                case "DEFECT":
                    retVal.add("FocusPotion");
                    retVal.add("PotionOfCapacity");
                    retVal.add("EssenceOfDarkness");
                    break;
                case "WATCHER":
                    retVal.add("BottledMiracle");
                    retVal.add("StancePotion");
                    retVal.add("Ambrosia");
                    break;
                default:
                    retVal.addAll(characterPotions.getOrDefault(c, new ArrayList<>()));
            }
        } else {
            retVal.add("BloodPotion");
            retVal.add("ElixirPotion");
            retVal.add("HeartOfIron");
            retVal.add("Poison Potion");
            retVal.add("CunningPotion");
            retVal.add("GhostInAJar");
            retVal.add("FocusPotion");
            retVal.add("PotionOfCapacity");
            retVal.add("EssenceOfDarkness");
            retVal.add("BottledMiracle");
            retVal.add("StancePotion");
            retVal.add("Ambrosia");
            for (Map.Entry<PlayerClass, List<String>> e : characterPotions.entrySet()) {
                retVal.addAll(e.getValue());
            }
        }

        retVal.add("Block Potion");
        retVal.add("Dexterity Potion");
        retVal.add("Energy Potion");
        retVal.add("Explosive Potion");
        retVal.add("Fire Potion");
        retVal.add("Strength Potion");
        retVal.add("Swift Potion");
        retVal.add("Weak Potion");
        retVal.add("FearPotion");
        retVal.add("AttackPotion");
        retVal.add("SkillPotion");
        retVal.add("PowerPotion");
        retVal.add("ColorlessPotion");
        retVal.add("SteroidPotion");
        retVal.add("SpeedPotion");
        retVal.add("BlessingOfTheForge");
        retVal.add("Regen Potion");
        retVal.add("Ancient Potion");
        retVal.add("LiquidBronze");
        retVal.add("GamblersBrew");
        retVal.add("EssenceOfSteel");
        retVal.add("DuplicationPotion");
        retVal.add("DistilledChaos");
        retVal.add("LiquidMemories");
        retVal.add("CultistPotion");
        retVal.add("Fruit Juice");
        retVal.add("SneckoOil");
        retVal.add("FairyPotion");
        retVal.add("SmokeBomb");
        retVal.add("EntropicBrew");
        return retVal;
    }

    public static AbstractPotion getRandomPotion(Random rng) {
        String randomKey = potions.get(rng.random(potions.size() - 1));
        return getPotion(randomKey);
    }

    public static AbstractPotion getRandomPotion() {
        String randomKey = potions.get(AbstractDungeon.potionRng.random(potions.size() - 1));
        return getPotion(randomKey);
    }

    public static boolean isAPotion(String key) {
        return getPotions(null, true).contains(key);
    }

    public static AbstractPotion getPotion(String name) {
        if (name != null && !name.equals("")) {
            byte var2 = -1;
            switch(name.hashCode()) {
                case -2006332828:
                    if (name.equals("EssenceOfSteel")) {
                        var2 = 30;
                    }
                    break;
                case -1946326446:
                    if (name.equals("Strength Potion")) {
                        var2 = 8;
                    }
                    break;
                case -1813432936:
                    if (name.equals("Ambrosia")) {
                        var2 = 0;
                    }
                    break;
                case -1573559404:
                    if (name.equals("EssenceOfDarkness")) {
                        var2 = 2;
                    }
                    break;
                case -1521326906:
                    if (name.equals("Block Potion")) {
                        var2 = 3;
                    }
                    break;
                case -1392305052:
                    if (name.equals("EntropicBrew")) {
                        var2 = 40;
                    }
                    break;
                case -1365175786:
                    if (name.equals("LiquidBronze")) {
                        var2 = 27;
                    }
                    break;
                case -1084502213:
                    if (name.equals("Weak Potion")) {
                        var2 = 11;
                    }
                    break;
                case -993832189:
                    if (name.equals("FearPotion")) {
                        var2 = 12;
                    }
                    break;
                case -989581205:
                    if (name.equals("FocusPotion")) {
                        var2 = 26;
                    }
                    break;
                case -976296533:
                    if (name.equals("Poison Potion")) {
                        var2 = 10;
                    }
                    break;
                case -871582954:
                    if (name.equals("Regen Potion")) {
                        var2 = 24;
                    }
                    break;
                case -852144469:
                    if (name.equals("GamblersBrew")) {
                        var2 = 29;
                    }
                    break;
                case -717939453:
                    if (name.equals("StancePotion")) {
                        var2 = 32;
                    }
                    break;
                case -639415142:
                    if (name.equals("SpeedPotion")) {
                        var2 = 18;
                    }
                    break;
                case -563244898:
                    if (name.equals("DistilledChaos")) {
                        var2 = 22;
                    }
                    break;
                case -471865564:
                    if (name.equals("SkillPotion")) {
                        var2 = 13;
                    }
                    break;
                case -396593240:
                    if (name.equals("FairyPotion")) {
                        var2 = 38;
                    }
                    break;
                case -350179221:
                    if (name.equals("Potion Slot")) {
                        var2 = 42;
                    }
                    break;
                case -131581938:
                    if (name.equals("GhostInAJar")) {
                        var2 = 25;
                    }
                    break;
                case -114734959:
                    if (name.equals("SmokeBomb")) {
                        var2 = 39;
                    }
                    break;
                case -16400371:
                    if (name.equals("BloodPotion")) {
                        var2 = 31;
                    }
                    break;
                case 187249195:
                    if (name.equals("Energy Potion")) {
                        var2 = 5;
                    }
                    break;
                case 286712340:
                    if (name.equals("Fruit Juice")) {
                        var2 = 36;
                    }
                    break;
                case 378930877:
                    if (name.equals("Fire Potion")) {
                        var2 = 7;
                    }
                    break;
                case 571908635:
                    if (name.equals("AttackPotion")) {
                        var2 = 15;
                    }
                    break;
                case 741355073:
                    if (name.equals("DuplicationPotion")) {
                        var2 = 33;
                    }
                    break;
                case 748260995:
                    if (name.equals("CunningPotion")) {
                        var2 = 21;
                    }
                    break;
                case 891586619:
                    if (name.equals("Ancient Potion")) {
                        var2 = 23;
                    }
                    break;
                case 912386821:
                    if (name.equals("HeartOfIron")) {
                        var2 = 41;
                    }
                    break;
                case 938559616:
                    if (name.equals("Swift Potion")) {
                        var2 = 9;
                    }
                    break;
                case 1092732943:
                    if (name.equals("ColorlessPotion")) {
                        var2 = 16;
                    }
                    break;
                case 1120790805:
                    if (name.equals("SneckoOil")) {
                        var2 = 37;
                    }
                    break;
                case 1163620974:
                    if (name.equals("Explosive Potion")) {
                        var2 = 6;
                    }
                    break;
                case 1272738036:
                    if (name.equals("BlessingOfTheForge")) {
                        var2 = 19;
                    }
                    break;
                case 1312927451:
                    if (name.equals("LiquidMemories")) {
                        var2 = 28;
                    }
                    break;
                case 1313368658:
                    if (name.equals("ElixirPotion")) {
                        var2 = 34;
                    }
                    break;
                case 1321250223:
                    if (name.equals("Dexterity Potion")) {
                        var2 = 4;
                    }
                    break;
                case 1592755027:
                    if (name.equals("BottledMiracle")) {
                        var2 = 1;
                    }
                    break;
                case 1711181252:
                    if (name.equals("PotionOfCapacity")) {
                        var2 = 20;
                    }
                    break;
                case 1939845731:
                    if (name.equals("CultistPotion")) {
                        var2 = 35;
                    }
                    break;
                case 1974850767:
                    if (name.equals("SteroidPotion")) {
                        var2 = 17;
                    }
                    break;
                case 2018692824:
                    if (name.equals("PowerPotion")) {
                        var2 = 14;
                    }
            }

            switch(var2) {
                case 0:
                    return new Ambrosia();
                case 1:
                    return new BottledMiracle();
                case 2:
                    return new EssenceOfDarkness();
                case 3:
                    return new BlockPotion();
                case 4:
                    return new DexterityPotion();
                case 5:
                    return new EnergyPotion();
                case 6:
                    return new ExplosivePotion();
                case 7:
                    return new FirePotion();
                case 8:
                    return new StrengthPotion();
                case 9:
                    return new SwiftPotion();
                case 10:
                    return new PoisonPotion();
                case 11:
                    return new WeakenPotion();
                case 12:
                    return new FearPotion();
                case 13:
                    return new SkillPotion();
                case 14:
                    return new PowerPotion();
                case 15:
                    return new AttackPotion();
                case 16:
                    return new ColorlessPotion();
                case 17:
                    return new SteroidPotion();
                case 18:
                    return new SpeedPotion();
                case 19:
                    return new BlessingOfTheForge();
                case 20:
                    return new PotionOfCapacity();
                case 21:
                    return new CunningPotion();
                case 22:
                    return new DistilledChaosPotion();
                case 23:
                    return new AncientPotion();
                case 24:
                    return new RegenPotion();
                case 25:
                    return new GhostInAJar();
                case 26:
                    return new FocusPotion();
                case 27:
                    return new LiquidBronze();
                case 28:
                    return new LiquidMemories();
                case 29:
                    return new GamblersBrew();
                case 30:
                    return new EssenceOfSteel();
                case 31:
                    return new BloodPotion();
                case 32:
                    return new StancePotion();
                case 33:
                    return new DuplicationPotion();
                case 34:
                    return new Elixir();
                case 35:
                    return new CultistPotion();
                case 36:
                    return new FruitJuice();
                case 37:
                    return new SneckoOil();
                case 38:
                    return new FairyPotion();
                case 39:
                    return new SmokeBomb();
                case 40:
                    return new EntropicBrew();
                case 41:
                    return new HeartOfIron();
                case 42:
                    return null;
                default:
                    AbstractPotion potion = potionMap.getOrDefault(name, null);
                    if (potion != null) {
                        return potion.makeCopy();
                    }
                    logger.info("MISSING KEY: POTIONHELPER 37: " + name);
                    return new FirePotion();
            }
        } else {
            return null;
        }
    }

    public static void uploadPotionData() {
        initialize(PlayerClass.IRONCLAD);
        HashSet<String> ironcladPotions = new HashSet<>(potions);
        HashSet<String> sharedPotions = new HashSet<>(potions);
        initialize(PlayerClass.THE_SILENT);
        HashSet<String> silentPotions = new HashSet<>(potions);
        sharedPotions.retainAll(potions);
        initialize(PlayerClass.DEFECT);
        HashSet<String> defectPotions = new HashSet<>(potions);
        sharedPotions.retainAll(potions);
        initialize(PlayerClass.WATCHER);
        HashSet<String> watcherPotions = new HashSet<>(potions);
        sharedPotions.retainAll(potions);
        ironcladPotions.removeAll(sharedPotions);
        silentPotions.removeAll(sharedPotions);
        defectPotions.removeAll(sharedPotions);
        watcherPotions.removeAll(sharedPotions);
        potions.clear();
        ArrayList<String> data = new ArrayList<>();
        Iterator var6 = ironcladPotions.iterator();

        String id;
        while(var6.hasNext()) {
            id = (String)var6.next();
            data.add(getPotion(id).getUploadData("RED"));
        }

        var6 = silentPotions.iterator();

        while(var6.hasNext()) {
            id = (String)var6.next();
            data.add(getPotion(id).getUploadData("GREEN"));
        }

        var6 = defectPotions.iterator();

        while(var6.hasNext()) {
            id = (String)var6.next();
            data.add(getPotion(id).getUploadData("BLUE"));
        }

        var6 = watcherPotions.iterator();

        while(var6.hasNext()) {
            id = (String)var6.next();
            data.add(getPotion(id).getUploadData("PURPLE"));
        }

        var6 = sharedPotions.iterator();

        while(var6.hasNext()) {
            id = (String)var6.next();
            data.add(getPotion(id).getUploadData("ALL"));
        }

        BotDataUploader.uploadDataAsync(GameDataType.POTION_DATA, AbstractPotion.gameDataUploadHeader(), data);
    }
}

package com.megacrit.cardcrawl.helpers;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.BotDataUploader;
import com.megacrit.cardcrawl.metrics.BotDataUploader.GameDataType;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.*;
import java.util.Map.Entry;

public class RelicLibrary {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(RelicLibrary.class);
    public static int totalRelicCount = 0;
    public static int seenRelics = 0;
    private static HashMap<String, AbstractRelic> sharedRelics = new HashMap<>();
    private static HashMap<String, AbstractRelic> redRelics = new HashMap<>();
    private static HashMap<String, AbstractRelic> greenRelics = new HashMap<>();
    private static HashMap<String, AbstractRelic> blueRelics = new HashMap<>();
    private static HashMap<String, AbstractRelic> purpleRelics = new HashMap<>();
    private static Map<AbstractCard.CardColor, Map<String, AbstractRelic>> colorRelics = new HashMap<>();
    public static ArrayList<AbstractRelic> starterList = new ArrayList<>();
    public static ArrayList<AbstractRelic> commonList = new ArrayList<>();
    public static ArrayList<AbstractRelic> uncommonList = new ArrayList<>();
    public static ArrayList<AbstractRelic> rareList = new ArrayList<>();
    public static ArrayList<AbstractRelic> bossList = new ArrayList<>();
    public static ArrayList<AbstractRelic> specialList = new ArrayList<>();
    public static ArrayList<AbstractRelic> shopList = new ArrayList<>();
    public static ArrayList<AbstractRelic> redList = new ArrayList<>();
    public static ArrayList<AbstractRelic> greenList = new ArrayList<>();
    public static ArrayList<AbstractRelic> blueList = new ArrayList<>();
    public static ArrayList<AbstractRelic> whiteList = new ArrayList<>();
    public static Map<AbstractCard.CardColor, List<AbstractRelic>> colorListMap = new HashMap<>();

    public RelicLibrary() {
    }

    public static void initialize() {
        long startTime = System.currentTimeMillis();
        add(new Abacus());
        add(new Akabeko());
        add(new Anchor());
        add(new AncientTeaSet());
        add(new ArtOfWar());
        add(new Astrolabe());
        add(new BagOfMarbles());
        add(new BagOfPreparation());
        add(new BirdFacedUrn());
        add(new BlackStar());
        add(new BloodVial());
        add(new BloodyIdol());
        add(new BlueCandle());
        add(new Boot());
        add(new BottledFlame());
        add(new BottledLightning());
        add(new BottledTornado());
        add(new BronzeScales());
        add(new BustedCrown());
        add(new Calipers());
        add(new CallingBell());
        add(new CaptainsWheel());
        add(new Cauldron());
        add(new CentennialPuzzle());
        add(new CeramicFish());
        add(new ChemicalX());
        add(new ClockworkSouvenir());
        add(new CoffeeDripper());
        add(new Courier());
        add(new CultistMask());
        add(new CursedKey());
        add(new DarkstonePeriapt());
        add(new DeadBranch());
        add(new DollysMirror());
        add(new DreamCatcher());
        add(new DuVuDoll());
        add(new Ectoplasm());
        add(new EmptyCage());
        add(new Enchiridion());
        add(new EternalFeather());
        add(new FaceOfCleric());
        add(new FossilizedHelix());
        add(new FrozenEgg2());
        add(new FrozenEye());
        add(new FusionHammer());
        add(new GamblingChip());
        add(new Ginger());
        add(new Girya());
        add(new GoldenIdol());
        add(new GremlinHorn());
        add(new GremlinMask());
        add(new HandDrill());
        add(new HappyFlower());
        add(new HornCleat());
        add(new IceCream());
        add(new IncenseBurner());
        add(new InkBottle());
        add(new JuzuBracelet());
        add(new Kunai());
        add(new Lantern());
        add(new LetterOpener());
        add(new LizardTail());
        add(new Mango());
        add(new MarkOfTheBloom());
        add(new Matryoshka());
        add(new MawBank());
        add(new MealTicket());
        add(new MeatOnTheBone());
        add(new MedicalKit());
        add(new MembershipCard());
        add(new MercuryHourglass());
        add(new MoltenEgg2());
        add(new MummifiedHand());
        add(new MutagenicStrength());
        add(new Necronomicon());
        add(new NeowsLament());
        add(new NilrysCodex());
        add(new NlothsGift());
        add(new NlothsMask());
        add(new Nunchaku());
        add(new OddlySmoothStone());
        add(new OddMushroom());
        add(new OldCoin());
        add(new Omamori());
        add(new OrangePellets());
        add(new Orichalcum());
        add(new OrnamentalFan());
        add(new Orrery());
        add(new PandorasBox());
        add(new Pantograph());
        add(new PeacePipe());
        add(new Pear());
        add(new PenNib());
        add(new PhilosopherStone());
        add(new Pocketwatch());
        add(new PotionBelt());
        add(new PrayerWheel());
        add(new PreservedInsect());
        add(new PrismaticShard());
        add(new QuestionCard());
        add(new RedMask());
        add(new RegalPillow());
        add(new RunicDome());
        add(new RunicPyramid());
        add(new SacredBark());
        add(new Shovel());
        add(new Shuriken());
        add(new SingingBowl());
        add(new SlaversCollar());
        add(new Sling());
        add(new SmilingMask());
        add(new SneckoEye());
        add(new Sozu());
        add(new SpiritPoop());
        add(new SsserpentHead());
        add(new StoneCalendar());
        add(new StrangeSpoon());
        add(new Strawberry());
        add(new StrikeDummy());
        add(new Sundial());
        add(new ThreadAndNeedle());
        add(new TinyChest());
        add(new TinyHouse());
        add(new Toolbox());
        add(new Torii());
        add(new ToxicEgg2());
        add(new ToyOrnithopter());
        add(new TungstenRod());
        add(new Turnip());
        add(new UnceasingTop());
        add(new Vajra());
        add(new VelvetChoker());
        add(new Waffle());
        add(new WarPaint());
        add(new WarpedTongs());
        add(new Whetstone());
        add(new WhiteBeast());
        add(new WingBoots());
        addGreen(new HoveringKite());
        addGreen(new NinjaScroll());
        addGreen(new PaperCrane());
        addGreen(new RingOfTheSerpent());
        addGreen(new SnakeRing());
        addGreen(new SneckoSkull());
        addGreen(new TheSpecimen());
        addGreen(new Tingsha());
        addGreen(new ToughBandages());
        addGreen(new TwistedFunnel());
        addGreen(new WristBlade());
        addRed(new BlackBlood());
        addRed(new Brimstone());
        addRed(new BurningBlood());
        addRed(new ChampionsBelt());
        addRed(new CharonsAshes());
        addRed(new MagicFlower());
        addRed(new MarkOfPain());
        addRed(new PaperFrog());
        addRed(new RedSkull());
        addRed(new RunicCube());
        addRed(new SelfFormingClay());
        addBlue(new CrackedCore());
        addBlue(new DataDisk());
        addBlue(new EmotionChip());
        addBlue(new FrozenCore());
        addBlue(new GoldPlatedCables());
        addBlue(new Inserter());
        addBlue(new NuclearBattery());
        addBlue(new RunicCapacitor());
        addBlue(new SymbioticVirus());
        addPurple(new CloakClasp());
        addPurple(new Damaru());
        addPurple(new GoldenEye());
        addPurple(new HolyWater());
        addPurple(new Melange());
        addPurple(new PureWater());
        addPurple(new VioletLotus());
        addPurple(new TeardropLocket());
        addPurple(new Duality());
        BaseMod.receiveEditRelics();
        logger.info("Relic load time: " + (System.currentTimeMillis() - startTime) + "ms");
        sortLists();
    }

    public static void resetForReload() {
        totalRelicCount = 0;
        seenRelics = 0;
        sharedRelics.clear();
        redRelics.clear();
        greenRelics.clear();
        blueRelics.clear();
        purpleRelics.clear();
        for (Entry<AbstractCard.CardColor, Map<String, AbstractRelic>> e : colorRelics.entrySet()) {
            e.getValue().clear();
        }
        starterList.clear();
        commonList.clear();
        uncommonList.clear();
        rareList.clear();
        bossList.clear();
        specialList.clear();
        shopList.clear();
        redList.clear();
        greenList.clear();
        blueList.clear();
        whiteList.clear();
        for (Entry<AbstractCard.CardColor, List<AbstractRelic>> e : colorListMap.entrySet()) {
            e.getValue().clear();
        }
    }

    private static void sortLists() {
        Collections.sort(starterList);
        Collections.sort(commonList);
        Collections.sort(uncommonList);
        Collections.sort(rareList);
        Collections.sort(bossList);
        Collections.sort(specialList);
        Collections.sort(shopList);
        if (Settings.isDev) {
            logger.info(starterList.toString());
            logger.info(commonList.toString());
            logger.info(uncommonList.toString());
            logger.info(rareList.toString());
            logger.info(bossList.toString());
        }

    }

    private static void printRelicsMissingLargeArt() {
        logger.info("[ART] START DISPLAYING RELICS WITH MISSING HIGH RES ART");

        for (Entry<String, AbstractRelic> o : sharedRelics.entrySet()) {
            AbstractRelic relic = o.getValue();
            if (ImageMaster.loadImage("images/largeRelics/" + relic.imgUrl) == null) {
                logger.info(relic.name);
            }
        }
    }

    private static void printRelicCount() {
        int common = 0;
        int uncommon = 0;
        int rare = 0;
        int boss = 0;
        int shop = 0;
        int other = 0;

        for (Entry<String, AbstractRelic> o : sharedRelics.entrySet()) {
            switch (o.getValue().tier) {
                case COMMON:
                    ++common;
                    break;
                case UNCOMMON:
                    ++uncommon;
                    break;
                case RARE:
                    ++rare;
                    break;
                case BOSS:
                    ++boss;
                    break;
                case SHOP:
                    ++shop;
                    break;
                default:
                    ++other;
            }
        }

        if (Settings.isDev) {
            logger.info("RELIC COUNTS");
            logger.info("Common: " + common);
            logger.info("Uncommon: " + uncommon);
            logger.info("Rare: " + rare);
            logger.info("Boss: " + boss);
            logger.info("Shop: " + shop);
            logger.info("Other: " + other);
            logger.info("Red: " + redRelics.size());
            logger.info("Green: " + greenRelics.size());
            logger.info("Blue: " + blueRelics.size());
            logger.info("Purple: " + purpleRelics.size());
        }

    }

    public static void add(AbstractRelic relic) {
        if (UnlockTracker.isRelicSeen(relic.relicId)) {
            ++seenRelics;
        }

        relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
        sharedRelics.put(relic.relicId, relic);
        addToTierList(relic);
        ++totalRelicCount;
    }

    public static void addModColor(AbstractRelic relic, AbstractCard.CardColor color) {
        if (UnlockTracker.isRelicSeen(relic.relicId)) {
            ++seenRelics;
        }

        relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);

        Map<String, AbstractRelic> relicMap = colorRelics.getOrDefault(color, new HashMap<>());
        relicMap.put(relic.relicId, relic);
        colorRelics.put(color, relicMap);
        addToTierList(relic);
        List<AbstractRelic> colorRelicList = colorListMap.getOrDefault(color, new ArrayList<>());
        colorRelicList.add(relic);
        colorListMap.put(color, colorRelicList);
        ++totalRelicCount;
    }

    public static void addRed(AbstractRelic relic) {
        if (UnlockTracker.isRelicSeen(relic.relicId)) {
            ++seenRelics;
        }

        relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
        redRelics.put(relic.relicId, relic);
        addToTierList(relic);
        redList.add(relic);
        ++totalRelicCount;
    }

    public static void addGreen(AbstractRelic relic) {
        if (UnlockTracker.isRelicSeen(relic.relicId)) {
            ++seenRelics;
        }

        relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
        greenRelics.put(relic.relicId, relic);
        addToTierList(relic);
        greenList.add(relic);
        ++totalRelicCount;
    }

    public static void addBlue(AbstractRelic relic) {
        if (UnlockTracker.isRelicSeen(relic.relicId)) {
            ++seenRelics;
        }

        relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
        blueRelics.put(relic.relicId, relic);
        addToTierList(relic);
        blueList.add(relic);
        ++totalRelicCount;
    }

    public static void addPurple(AbstractRelic relic) {
        if (UnlockTracker.isRelicSeen(relic.relicId)) {
            ++seenRelics;
        }

        relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
        purpleRelics.put(relic.relicId, relic);
        addToTierList(relic);
        whiteList.add(relic);
        ++totalRelicCount;
    }

    public static void addToTierList(AbstractRelic relic) {
        switch(relic.tier) {
            case COMMON:
                commonList.add(relic);
                break;
            case UNCOMMON:
                uncommonList.add(relic);
                break;
            case RARE:
                rareList.add(relic);
                break;
            case BOSS:
                bossList.add(relic);
                break;
            case SHOP:
                shopList.add(relic);
                break;
            case STARTER:
                starterList.add(relic);
                break;
            case SPECIAL:
                specialList.add(relic);
                break;
            case DEPRECATED:
                logger.info(relic.relicId + " is deprecated.");
                break;
            default:
                logger.info(relic.relicId + " is undefined tier.");
        }

    }

    public static AbstractRelic getRelic(String key) {
        if (sharedRelics.containsKey(key)) {
            return sharedRelics.get(key);
        } else if (redRelics.containsKey(key)) {
            return redRelics.get(key);
        } else if (greenRelics.containsKey(key)) {
            return greenRelics.get(key);
        } else if (blueRelics.containsKey(key)) {
            return blueRelics.get(key);
        } else if (purpleRelics.containsKey(key)) {
            return purpleRelics.get(key);
        } else {
            for (Entry<AbstractCard.CardColor, Map<String, AbstractRelic>> e : colorRelics.entrySet()) {
                if (e.getValue().containsKey(key)) {
                    return e.getValue().getOrDefault(key, new Circlet());
                }
            }
            return new Circlet();
        }
    }

    public static boolean isARelic(String key) {
        return sharedRelics.containsKey(key) ||
                redRelics.containsKey(key) ||
                greenRelics.containsKey(key) ||
                blueRelics.containsKey(key) ||
                purpleRelics.containsKey(key) ||
                isAColorRelic(key);
    }

    public static boolean isAColorRelic(String key) {
        for (Entry<AbstractCard.CardColor, Map<String, AbstractRelic>> e : colorRelics.entrySet()) {
            if (e.getValue().containsKey(key)) return true;
        }
        return false;
    }

    public static void populateRelicPool(ArrayList<String> pool, RelicTier tier, PlayerClass c) {
        Iterator<Map.Entry<String, AbstractRelic>> entryIterator = sharedRelics.entrySet().iterator();

        while(true) {
            Entry<String, AbstractRelic> r;
            do {
                do {
                    if (!entryIterator.hasNext()) {
                        switch(c.name()) {
                            case "IRONCLAD":
                                for (Entry<String, AbstractRelic> e : redRelics.entrySet()) {
                                    if (e.getValue().tier == tier && (!UnlockTracker.isRelicLocked(e.getKey()) || Settings.treatEverythingAsUnlocked())) {
                                        pool.add(e.getKey());
                                    }
                                }
                                return;
                            case "THE_SILENT":
                                for (Entry<String, AbstractRelic> e : greenRelics.entrySet()) {
                                    if (e.getValue().tier == tier && (!UnlockTracker.isRelicLocked(e.getKey()) || Settings.treatEverythingAsUnlocked())) {
                                        pool.add(e.getKey());
                                    }
                                }
                                return;
                            case "DEFECT":
                                for (Entry<String, AbstractRelic> e : blueRelics.entrySet()) {
                                    if (e.getValue().tier == tier && (!UnlockTracker.isRelicLocked(e.getKey()) || Settings.treatEverythingAsUnlocked())) {
                                        pool.add(e.getKey());
                                    }
                                }
                                return;
                            case "WATCHER":
                                for (Entry<String, AbstractRelic> e : purpleRelics.entrySet()) {
                                    if (e.getValue().tier == tier && (!UnlockTracker.isRelicLocked(e.getKey()) || Settings.treatEverythingAsUnlocked())) {
                                        pool.add(e.getKey());
                                    }
                                }
                                return;
                            default:
                                for (AbstractPlayer p : CardCrawlGame.characterManager.getAllCharacters()) {
                                    if (p.chosenClass == c) {
                                        AbstractCard.CardColor color = p.getCardColor();
                                        for (Entry<String, AbstractRelic> e : colorRelics.getOrDefault(color, new HashMap<>()).entrySet()) {
                                            if (e.getValue().tier == tier && (!UnlockTracker.isRelicLocked(e.getKey()) || Settings.treatEverythingAsUnlocked())) {
                                                pool.add(e.getKey());
                                            }
                                        }
                                        break;
                                    }
                                }
                                return;
                        }
                    }

                    r = entryIterator.next();
                } while(r.getValue().tier != tier);
            } while(UnlockTracker.isRelicLocked(r.getKey()) && !Settings.treatEverythingAsUnlocked());
            pool.add(r.getKey());
        }
    }

    public static void addSharedRelics(ArrayList<AbstractRelic> relicPool) {
        if (Settings.isDev) {
            logger.info("[RELIC] Adding " + sharedRelics.size() + " shared relics...");
        }

        for (Entry<String, AbstractRelic> o : sharedRelics.entrySet()) {
            relicPool.add(o.getValue());
        }

    }

    public static void addClassSpecificRelics(ArrayList<AbstractRelic> relicPool) {
        Iterator<Map.Entry<String, AbstractRelic>> var1;
        Entry<String, AbstractRelic> r;
        switch(AbstractDungeon.player.chosenClass.name()) {
            case "IRONCLAD":
                if (Settings.isDev) {
                    logger.info("[RELIC] Adding " + redRelics.size() + " red relics...");
                }

                var1 = redRelics.entrySet().iterator();

                while(var1.hasNext()) {
                    r = var1.next();
                    relicPool.add(r.getValue());
                }

                return;
            case "THE_SILENT":
                if (Settings.isDev) {
                    logger.info("[RELIC] Adding " + greenRelics.size() + " green relics...");
                }

                var1 = greenRelics.entrySet().iterator();

                while(var1.hasNext()) {
                    r = var1.next();
                    relicPool.add(r.getValue());
                }

                return;
            case "DEFECT":
                if (Settings.isDev) {
                    logger.info("[RELIC] Adding " + blueRelics.size() + " blue relics...");
                }

                var1 = blueRelics.entrySet().iterator();

                while(var1.hasNext()) {
                    r = var1.next();
                    relicPool.add(r.getValue());
                }

                return;
            case "WATCHER":
                if (Settings.isDev) {
                    logger.info("[RELIC] Adding " + purpleRelics.size() + " purple relics...");
                }

                var1 = purpleRelics.entrySet().iterator();

                while(var1.hasNext()) {
                    r = var1.next();
                    relicPool.add(r.getValue());
                }
                return;
            default:
                AbstractCard.CardColor color = AbstractDungeon.player.getCardColor();
                if (Settings.isDev) {
                    logger.info("[RELIC] Adding " + colorRelics.getOrDefault(color, new HashMap<>()).size() + " {} relics...", color.name());
                }
                for (Entry<String, AbstractRelic> e : colorRelics.getOrDefault(color, new HashMap<>()).entrySet()) {
                    relicPool.add(e.getValue());
                }
        }

    }

    public static void uploadRelicData() {
        ArrayList<String> data = new ArrayList<>();
        Iterator var1 = sharedRelics.entrySet().iterator();

        Entry r;
        while(var1.hasNext()) {
            r = (Entry)var1.next();
            data.add(((AbstractRelic)r.getValue()).gameDataUploadData("All"));
        }

        var1 = redRelics.entrySet().iterator();

        while(var1.hasNext()) {
            r = (Entry)var1.next();
            data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Red"));
        }

        var1 = greenRelics.entrySet().iterator();

        while(var1.hasNext()) {
            r = (Entry)var1.next();
            data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Green"));
        }

        var1 = blueRelics.entrySet().iterator();

        while(var1.hasNext()) {
            r = (Entry)var1.next();
            data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Blue"));
        }

        var1 = purpleRelics.entrySet().iterator();

        while(var1.hasNext()) {
            r = (Entry)var1.next();
            data.add(((AbstractRelic)r.getValue()).gameDataUploadData("Purple"));
        }

        BotDataUploader.uploadDataAsync(GameDataType.RELIC_DATA, AbstractRelic.gameDataUploadHeader(), data);
    }

    public static ArrayList<AbstractRelic> sortByName(ArrayList<AbstractRelic> group, boolean ascending) {
        ArrayList<AbstractRelic> tmp = new ArrayList<>();

        int addIndex;
        AbstractRelic r;
        for(Iterator var4 = group.iterator(); var4.hasNext(); tmp.add(addIndex, r)) {
            r = (AbstractRelic)var4.next();
            addIndex = 0;

            for(Iterator var6 = tmp.iterator(); var6.hasNext(); ++addIndex) {
                AbstractRelic r2 = (AbstractRelic)var6.next();
                if (!ascending) {
                    if (r.name.compareTo(r2.name) < 0) {
                        break;
                    }
                } else if (r.name.compareTo(r2.name) > 0) {
                    break;
                }
            }
        }

        return tmp;
    }

    public static ArrayList<AbstractRelic> sortByStatus(ArrayList<AbstractRelic> group, boolean ascending) {
        ArrayList<AbstractRelic> tmp = new ArrayList<>();

        int addIndex;
        AbstractRelic r;
        for(Iterator var6 = group.iterator(); var6.hasNext(); tmp.add(addIndex, r)) {
            r = (AbstractRelic)var6.next();
            addIndex = 0;

            for(Iterator var8 = tmp.iterator(); var8.hasNext(); ++addIndex) {
                AbstractRelic r2 = (AbstractRelic)var8.next();
                String a;
                String b;
                if (!ascending) {
                    if (UnlockTracker.isRelicLocked(r.relicId)) {
                        a = "LOCKED";
                    } else if (UnlockTracker.isRelicSeen(r.relicId)) {
                        a = "UNSEEN";
                    } else {
                        a = "SEEN";
                    }

                    if (UnlockTracker.isRelicLocked(r2.relicId)) {
                        b = "LOCKED";
                    } else if (UnlockTracker.isRelicSeen(r2.relicId)) {
                        b = "UNSEEN";
                    } else {
                        b = "SEEN";
                    }

                    if (a.compareTo(b) > 0) {
                        break;
                    }
                } else {
                    if (UnlockTracker.isRelicLocked(r.relicId)) {
                        a = "LOCKED";
                    } else if (UnlockTracker.isRelicSeen(r.relicId)) {
                        a = "UNSEEN";
                    } else {
                        a = "SEEN";
                    }

                    if (UnlockTracker.isRelicLocked(r2.relicId)) {
                        b = "LOCKED";
                    } else if (UnlockTracker.isRelicSeen(r2.relicId)) {
                        b = "UNSEEN";
                    } else {
                        b = "SEEN";
                    }

                    if (a.compareTo(b) < 0) {
                        break;
                    }
                }
            }
        }

        return tmp;
    }

    public static void unlockAndSeeAllRelics() {
        Iterator var0 = UnlockTracker.lockedRelics.iterator();

        while(var0.hasNext()) {
            String s = (String)var0.next();
            UnlockTracker.hardUnlockOverride(s);
        }

        var0 = sharedRelics.entrySet().iterator();

        Entry r;
        while(var0.hasNext()) {
            r = (Entry)var0.next();
            UnlockTracker.markRelicAsSeen((String)r.getKey());
        }

        var0 = redRelics.entrySet().iterator();

        while(var0.hasNext()) {
            r = (Entry)var0.next();
            UnlockTracker.markRelicAsSeen((String)r.getKey());
        }

        var0 = greenRelics.entrySet().iterator();

        while(var0.hasNext()) {
            r = (Entry)var0.next();
            UnlockTracker.markRelicAsSeen((String)r.getKey());
        }

        var0 = blueRelics.entrySet().iterator();

        while(var0.hasNext()) {
            r = (Entry)var0.next();
            UnlockTracker.markRelicAsSeen((String)r.getKey());
        }

        var0 = purpleRelics.entrySet().iterator();

        while(var0.hasNext()) {
            r = (Entry)var0.next();
            UnlockTracker.markRelicAsSeen((String)r.getKey());
        }

        for (Entry<AbstractCard.CardColor, Map<String, AbstractRelic>> e1 : colorRelics.entrySet()) {
            for (Entry<String, AbstractRelic> e2 : e1.getValue().entrySet()) {
                UnlockTracker.markRelicAsSeen(e2.getKey());
            }
        }
    }
}

package com.megacrit.cardcrawl.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.utils.ConditionalEvent;
import com.megacrit.cardcrawl.android.mods.utils.EventUtils;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.colorless.SwiftStrike;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.credits.CreditsScreen;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.shrines.NoteForYourself;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.helpers.EventHelper.RoomResult;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.map.RoomTypeAssigner;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.metrics.Metrics.MetricRequestType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rewards.chests.*;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.rooms.VictoryRoom.EventType;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile.SaveType;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.*;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.SettingsScreen;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockCharacterScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;

import java.util.*;
import java.util.Map.Entry;

public abstract class AbstractDungeon {
    protected static final SpireAndroidLogger logger =SpireAndroidLogger.getLogger(AbstractDungeon.class);
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static String name;
    public static String levelNum;
    public static String id;
    public static int floorNum;
    public static int actNum;
    public static AbstractPlayer player;
    public static ArrayList<AbstractUnlock> unlocks;
    protected static float shrineChance;
    protected static float cardUpgradedChance;
    public static AbstractCard transformedCard;
    public static boolean loading_post_combat;
    public static boolean is_victory;
    public static Texture eventBackgroundImg;
    public static Random monsterRng;
    public static Random mapRng;
    public static Random eventRng;
    public static Random merchantRng;
    public static Random cardRng;
    public static Random treasureRng;
    public static Random relicRng;
    public static Random potionRng;
    public static Random monsterHpRng;
    public static Random aiRng;
    public static Random shuffleRng;
    public static Random cardRandomRng;
    public static Random miscRng;
    public static CardGroup srcColorlessCardPool;
    public static CardGroup srcCurseCardPool;
    public static CardGroup srcCommonCardPool;
    public static CardGroup srcUncommonCardPool;
    public static CardGroup srcRareCardPool;
    public static CardGroup colorlessCardPool;
    public static CardGroup curseCardPool;
    public static CardGroup commonCardPool;
    public static CardGroup uncommonCardPool;
    public static CardGroup rareCardPool;
    public static ArrayList<String> commonRelicPool;
    public static ArrayList<String> uncommonRelicPool;
    public static ArrayList<String> rareRelicPool;
    public static ArrayList<String> shopRelicPool;
    public static ArrayList<String> bossRelicPool;
    public static String lastCombatMetricKey;
    public static ArrayList<String> monsterList;
    public static ArrayList<String> eliteMonsterList;
    public static ArrayList<String> bossList;
    public static String bossKey;
    public static ArrayList<String> eventList;
    public static ArrayList<String> shrineList;
    public static ArrayList<String> specialOneTimeEventList;
    public static GameActionManager actionManager;
    public static ArrayList<AbstractGameEffect> topLevelEffects;
    public static ArrayList<AbstractGameEffect> topLevelEffectsQueue;
    public static ArrayList<AbstractGameEffect> effectList;
    public static ArrayList<AbstractGameEffect> effectsQueue;
    public static boolean turnPhaseEffectActive;
    public static float colorlessRareChance;
    protected static float shopRoomChance;
    protected static float restRoomChance;
    protected static float eventRoomChance;
    protected static float eliteRoomChance;
    protected static float treasureRoomChance;
    protected static int smallChestChance;
    protected static int mediumChestChance;
    protected static int largeChestChance;
    protected static int commonRelicChance;
    protected static int uncommonRelicChance;
    protected static int rareRelicChance;
    public static AbstractScene scene;
    public static MapRoomNode currMapNode;
    public static ArrayList<ArrayList<MapRoomNode>> map;
    public static boolean leftRoomAvailable;
    public static boolean centerRoomAvailable;
    public static boolean rightRoomAvailable;
    public static boolean firstRoomChosen;
    public static final int MAP_HEIGHT = 15;
    public static final int MAP_WIDTH = 7;
    public static final int MAP_DENSITY = 6;
    public static final int FINAL_ACT_MAP_HEIGHT = 3;
    public static AbstractDungeon.RenderScene rs;
    public static ArrayList<Integer> pathX;
    public static ArrayList<Integer> pathY;
    public static Color topGradientColor;
    public static Color botGradientColor;
    public static float floorY;
    public static TopPanel topPanel;
    public static CardRewardScreen cardRewardScreen;
    public static CombatRewardScreen combatRewardScreen;
    public static BossRelicSelectScreen bossRelicScreen;
    public static MasterDeckViewScreen deckViewScreen;
    public static DiscardPileViewScreen discardPileViewScreen;
    public static DrawPileViewScreen gameDeckViewScreen;
    public static ExhaustPileViewScreen exhaustPileViewScreen;
    public static SettingsScreen settingsScreen;
    public static InputSettingsScreen inputSettingsScreen;
    public static DungeonMapScreen dungeonMapScreen;
    public static GridCardSelectScreen gridSelectScreen;
    public static HandCardSelectScreen handCardSelectScreen;
    public static ShopScreen shopScreen;
    public static CreditsScreen creditsScreen;
    public static FtueTip ftue;
    public static DeathScreen deathScreen;
    public static VictoryScreen victoryScreen;
    public static UnlockCharacterScreen unlockScreen;
    public static NeowUnlockScreen gUnlockScreen;
    public static boolean isScreenUp;
    public static OverlayMenu overlayMenu;
    public static AbstractDungeon.CurrentScreen screen;
    public static AbstractDungeon.CurrentScreen previousScreen;
    public static DynamicBanner dynamicBanner;
    public static boolean screenSwap;
    public static boolean isDungeonBeaten;
    public static int cardBlizzStartOffset;
    public static int cardBlizzRandomizer;
    public static int cardBlizzGrowth;
    public static int cardBlizzMaxOffset;
    public static boolean isFadingIn;
    public static boolean isFadingOut;
    public static boolean waitingOnFadeOut;
    protected static float fadeTimer;
    public static Color fadeColor;
    public static Color sourceFadeColor;
    public static MapRoomNode nextRoom;
    public static float sceneOffsetY;
    public static ArrayList<String> relicsToRemoveOnStart;
    public static int bossCount;
    public static final float SCENE_OFFSET_TIME = 1.3F;
    public static boolean isAscensionMode;
    public static int ascensionLevel;
    public static ArrayList<AbstractBlight> blightPool;
    public static boolean ascensionCheck;
    private static final SpireAndroidLogger LOGGER;

    public AbstractDungeon(String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {
        ascensionCheck = UnlockTracker.isAscensionUnlocked(p);
        CardCrawlGame.dungeon = this;
        long startTime = System.currentTimeMillis();
        AbstractDungeon.name = name;
        id = levelId;
        player = p;
        topPanel.setPlayerName();
        actionManager = new GameActionManager();
        overlayMenu = new OverlayMenu(p);
        dynamicBanner = new DynamicBanner();
        unlocks.clear();
        specialOneTimeEventList = newSpecialOneTimeEventList;
        isFadingIn = false;
        isFadingOut = false;
        waitingOnFadeOut = false;
        fadeTimer = 1.0F;
        isDungeonBeaten = false;
        isScreenUp = false;
        dungeonTransitionSetup();
        this.generateMonsters();
        this.initializeBoss();
        if (AbstractDungeon.bossList.size() != 1) {
            List<String> customBosses = BaseMod.getBossIDs(AbstractDungeon.id);

            if (!customBosses.isEmpty()) {
                AbstractDungeon.bossList.addAll(customBosses);
                Collections.shuffle(AbstractDungeon.bossList, new java.util.Random(AbstractDungeon.monsterRng.randomLong()));
            }
        }
        this.setBoss(bossList.get(0));
        this.initializeEventList();
        this.initializeEventImg();
        this.initializeShrineList();
        this.initializeCardPools();
        EventUtils.eventLogger.info("Adding normal events and shrines on Dungeon Instantiation");
        for (Map.Entry<String, ConditionalEvent<? extends AbstractEvent>> e : EventUtils.normalEvents.entrySet()) {
            if (e.getValue().isValid()) {
                if (p.seenEvents.contains(e.getKey())) {
                    EventUtils.eventLogger.info("Event " + e.getValue() + " was already seen and will not be added.");
                    continue;
                }
                EventUtils.eventLogger.info("Added " + e.getValue() + " to the event list for " + AbstractDungeon.id + ".");
                AbstractDungeon.eventList.add(e.getKey());
            }
        }
        for (Map.Entry<String, ConditionalEvent<? extends AbstractEvent>> e : EventUtils.shrineEvents.entrySet()) {
            if (e.getValue().isValid()) {
                EventUtils.eventLogger.info("Added " + e.getValue() + " to the shrine list for " + AbstractDungeon.id + ".");
                AbstractDungeon.shrineList.add(e.getKey());
            }
        }
        EventUtils.eventLogger.info("Checking for replacement normal events and shrines...");
        for (Map.Entry<String, ArrayList<ConditionalEvent<? extends AbstractEvent>>> replacements : EventUtils.fullReplaceEventList.entrySet()) {
            if (AbstractDungeon.eventList.contains(replacements.getKey())) {
                for (ConditionalEvent<? extends AbstractEvent> e : replacements.getValue()) {
                    if (e.isValid()) {
                        AbstractDungeon.eventList.add(AbstractDungeon.eventList.indexOf(replacements.getKey()), e.overrideEvent);
                        AbstractDungeon.eventList.remove(replacements.getKey());
                        EventUtils.eventLogger.info("Replaced " + replacements.getKey() + " with " + e.overrideEvent + " in event list for " + AbstractDungeon.id + ".");
                        break;
                    }
                }
            }
            if (AbstractDungeon.shrineList.contains(replacements.getKey())) {
                for (ConditionalEvent<? extends AbstractEvent> e : replacements.getValue()) {
                    if (e.isValid()) {
                        AbstractDungeon.shrineList.add(AbstractDungeon.shrineList.indexOf(replacements.getKey()), e.overrideEvent);
                        AbstractDungeon.shrineList.remove(replacements.getKey());
                        EventUtils.eventLogger.info("Replaced " + replacements.getKey() + " with " + e.overrideEvent + " in shrine list for " + AbstractDungeon.id + ".");
                    }
                }
            }
        }
        if (floorNum == 0) {
            p.initializeStarterDeck();
        }

        this.initializePotions();
        BlightHelper.initialize();
        if (id.equals("Exordium")) {
            screen = AbstractDungeon.CurrentScreen.NONE;
            isScreenUp = false;
        } else {
            screen = AbstractDungeon.CurrentScreen.MAP;
            isScreenUp = true;
        }

        logger.info("Content generation time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public AbstractDungeon(String name, AbstractPlayer p, SaveFile saveFile) {
        ascensionCheck = UnlockTracker.isAscensionUnlocked(p);
        id = saveFile.level_name;
        CardCrawlGame.dungeon = this;
        long startTime = System.currentTimeMillis();
        AbstractDungeon.name = name;
        player = p;
        topPanel.setPlayerName();
        actionManager = new GameActionManager();
        overlayMenu = new OverlayMenu(p);
        dynamicBanner = new DynamicBanner();
        isFadingIn = false;
        isFadingOut = false;
        waitingOnFadeOut = false;
        fadeTimer = 1.0F;
        isDungeonBeaten = false;
        isScreenUp = false;
        firstRoomChosen = true;
        unlocks.clear();

        try {
            this.loadSave(saveFile);
        } catch (Exception e) {
            logger.info("Exception occurred while loading save!");
            logger.info("Deleting save due to crash!");
            SaveAndContinue.deleteSave(player);
            e.printStackTrace();
            CardCrawlGame.writeExceptionToFile(e);
            //ExceptionHandler.handleException(var7, LOGGER);
            Gdx.app.exit();
        }

        this.initializeEventImg();
        this.initializeShrineList();
        this.initializeCardPools();
        EventUtils.eventLogger.info("Initializing Shrine List on load.");
        for (Map.Entry<String, ConditionalEvent<? extends AbstractEvent>> e : EventUtils.shrineEvents.entrySet()) {
            if (e.getValue().isValid()) {
                EventUtils.eventLogger.info("Added " + e.getValue() + " to the shrine list for " + AbstractDungeon.id + ".");
                AbstractDungeon.shrineList.add(e.getKey());
            }
        }

        EventUtils.eventLogger.info("Checking for Shrine replacements...");
        for (Map.Entry<String, ArrayList<ConditionalEvent<? extends AbstractEvent>>> replacements : EventUtils.fullReplaceEventList.entrySet()) {
            if (AbstractDungeon.shrineList.contains(replacements.getKey())) {
                for (ConditionalEvent<? extends AbstractEvent> e : replacements.getValue()) {
                    if (e.isValid()) {
                        AbstractDungeon.shrineList.add(AbstractDungeon.shrineList.indexOf(replacements.getKey()), e.overrideEvent);
                        AbstractDungeon.shrineList.remove(replacements.getKey());
                        EventUtils.eventLogger.info("Replaced " + replacements.getKey() + " with " + e.overrideEvent + " in shrine list for " + AbstractDungeon.id + ".");
                    }
                }
            }
        }
        this.initializePotions();
        BlightHelper.initialize();
        screen = AbstractDungeon.CurrentScreen.NONE;
        isScreenUp = false;
        logger.info("Dungeon load time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void setBoss(String key) {
        BaseMod.BossInfo bossInfo = BaseMod.getBossInfo(key);
        if (bossInfo != null) {

            if (DungeonMap.boss != null) {
                DungeonMap.boss.dispose();
            }
            if (DungeonMap.bossOutline != null) {
                DungeonMap.bossOutline.dispose();
            }

            bossKey = key;
            DungeonMap.boss = bossInfo.loadBossMap();
            DungeonMap.bossOutline = bossInfo.loadBossMapOutline();

            logger.info("[BOSS] " + key);
            return;
        }
        bossKey = key;
        if (DungeonMap.boss != null && DungeonMap.bossOutline != null) {
            DungeonMap.boss.dispose();
            DungeonMap.bossOutline.dispose();
        }

        if (key.equals("The Guardian")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/guardian.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/guardian.png");
        } else if (key.equals("Hexaghost")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/hexaghost.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/hexaghost.png");
        } else if (key.equals("Slime Boss")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/slime.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/slime.png");
        } else if (key.equals("Collector")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/collector.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/collector.png");
        } else if (key.equals("Automaton")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/automaton.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/automaton.png");
        } else if (key.equals("Champ")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/champ.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/champ.png");
        } else if (key.equals("Awakened One")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/awakened.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/awakened.png");
        } else if (key.equals("Time Eater")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/timeeater.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/timeeater.png");
        } else if (key.equals("Donu and Deca")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/donu.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/donu.png");
        } else if (key.equals("The Heart")) {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/heart.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/heart.png");
        } else {
            logger.info("WARNING: UNKNOWN BOSS ICON: " + key);
            DungeonMap.boss = null;
        }

        logger.info("[BOSS] " + key);
    }

    protected abstract void initializeLevelSpecificChances();

    public static boolean isPlayerInDungeon() {
        return CardCrawlGame.dungeon != null;
    }

    public static void generateSeeds() {
        logger.info("Generating seeds: " + Settings.seed);
        monsterRng = new Random(Settings.seed);
        eventRng = new Random(Settings.seed);
        merchantRng = new Random(Settings.seed);
        cardRng = new Random(Settings.seed);
        treasureRng = new Random(Settings.seed);
        relicRng = new Random(Settings.seed);
        monsterHpRng = new Random(Settings.seed);
        potionRng = new Random(Settings.seed);
        aiRng = new Random(Settings.seed);
        shuffleRng = new Random(Settings.seed);
        cardRandomRng = new Random(Settings.seed);
        miscRng = new Random(Settings.seed);
    }

    public static void loadSeeds(SaveFile save) {
        if (save.is_daily || save.is_trial) {
            Settings.isDailyRun = save.is_daily;
            Settings.isTrial = save.is_trial;
            Settings.specialSeed = save.special_seed;
            if (save.is_daily) {
                ModHelper.setTodaysMods(save.special_seed, player.chosenClass);
            } else {
                ModHelper.setTodaysMods(save.seed, player.chosenClass);
            }
        }

        monsterRng = new Random(Settings.seed, save.monster_seed_count);
        eventRng = new Random(Settings.seed, save.event_seed_count);
        merchantRng = new Random(Settings.seed, save.merchant_seed_count);
        cardRng = new Random(Settings.seed, save.card_seed_count);
        cardBlizzRandomizer = save.card_random_seed_randomizer;
        treasureRng = new Random(Settings.seed, save.treasure_seed_count);
        relicRng = new Random(Settings.seed, save.relic_seed_count);
        potionRng = new Random(Settings.seed, save.potion_seed_count);
        logger.info("Loading seeds: " + Settings.seed);
        logger.info("Monster seed:  " + monsterRng.counter);
        logger.info("Event seed:    " + eventRng.counter);
        logger.info("Merchant seed: " + merchantRng.counter);
        logger.info("Card seed:     " + cardRng.counter);
        logger.info("Treasure seed: " + treasureRng.counter);
        logger.info("Relic seed:    " + relicRng.counter);
        logger.info("Potion seed:   " + potionRng.counter);
    }

    public void populatePathTaken(SaveFile saveFile) {
        MapRoomNode node = null;
        if (saveFile.current_room.equals(MonsterRoomBoss.class.getName())) {
            node = new MapRoomNode(-1, 15);
            node.room = new MonsterRoomBoss();
            nextRoom = node;
        } else if (saveFile.current_room.equals(TreasureRoomBoss.class.getName())) {
            node = new MapRoomNode(-1, 15);
            node.room = new TreasureRoomBoss();
            nextRoom = node;
        } else if (saveFile.room_y == 15 && saveFile.room_x == -1) {
            node = new MapRoomNode(-1, 15);
            node.room = new VictoryRoom(EventType.HEART);
            nextRoom = node;
        } else if (saveFile.current_room.equals(NeowRoom.class.getName())) {
            nextRoom = null;
        } else {
            nextRoom = (MapRoomNode)((ArrayList)map.get(saveFile.room_y)).get(saveFile.room_x);
        }

        for(int i = 0; i < pathX.size(); ++i) {
            if (pathY.get(i) == 14) {
                MapRoomNode node2 = (MapRoomNode)((ArrayList)map.get(pathY.get(i))).get(pathX.get(i));

                for (MapEdge e : node2.getEdges()) {
                    if (e != null) {
                        e.markAsTaken();
                    }
                }
            }

            if (pathY.get(i) < 15) {
                ((MapRoomNode)((ArrayList)map.get(pathY.get(i))).get(pathX.get(i))).taken = true;
                if (node != null) {
                    MapEdge connectedEdge = node.getEdgeConnectedTo((MapRoomNode)((ArrayList)map.get(pathY.get(i))).get(pathX.get(i)));
                    if (connectedEdge != null) {
                        connectedEdge.markAsTaken();
                    }
                }

                node = (MapRoomNode)((ArrayList)map.get(pathY.get(i))).get(pathX.get(i));
            }
        }

        if (this.isLoadingIntoNeow(saveFile)) {
            logger.info("Loading into Neow");
            currMapNode = new MapRoomNode(0, -1);
            currMapNode.room = new EmptyRoom();
            nextRoom = null;
        } else {
            logger.info("Loading into: " + saveFile.room_x + "," + saveFile.room_y);
            currMapNode = new MapRoomNode(0, -1);
            currMapNode.room = new EmptyRoom();
        }

        this.nextRoomTransition(saveFile);
        if (this.isLoadingIntoNeow(saveFile)) {
            if (saveFile.chose_neow_reward) {
                currMapNode.room = new NeowRoom(true);
            } else {
                currMapNode.room = new NeowRoom(false);
            }
        }

        if (currMapNode.room instanceof VictoryRoom && (!Settings.isFinalActAvailable || !Settings.hasRubyKey || !Settings.hasEmeraldKey || !Settings.hasSapphireKey)) {
            CardCrawlGame.stopClock = true;
        }

    }

    protected boolean isLoadingIntoNeow(SaveFile saveFile) {
        return floorNum == 0 || saveFile.current_room.equals(NeowRoom.class.getName());
    }

    public static AbstractChest getRandomChest() {
        int roll = treasureRng.random(0, 99);
        if (roll < smallChestChance) {
            return new SmallChest();
        } else {
            return roll < mediumChestChance + smallChestChance ? new MediumChest() : new LargeChest();
        }
    }

    protected static void generateMap() {
        long startTime = System.currentTimeMillis();
        int mapHeight = 15;
        int mapWidth = 7;
        int mapPathDensity = 6;
        ArrayList<AbstractRoom> roomList = new ArrayList<>();
        map = MapGenerator.generateDungeon(mapHeight, mapWidth, mapPathDensity, mapRng);
        int count = 0;

        for (ArrayList<MapRoomNode> aMap : map) {
            for (MapRoomNode n : aMap) {
                if (n.hasEdges() && n.y != map.size() - 2) {
                    ++count;
                }
            }
        }

        generateRoomTypes(roomList, count);
        RoomTypeAssigner.assignRowAsRoomType(map.get(map.size() - 1), RestRoom.class);
        RoomTypeAssigner.assignRowAsRoomType(map.get(0), MonsterRoom.class);
        if (Settings.isEndless && player.hasBlight("MimicInfestation")) {
            RoomTypeAssigner.assignRowAsRoomType(map.get(8), MonsterRoomElite.class);
        } else {
            RoomTypeAssigner.assignRowAsRoomType(map.get(8), TreasureRoom.class);
        }

        map = RoomTypeAssigner.distributeRoomsAcrossMap(mapRng, map, roomList);
        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
        setEmeraldElite();
    }

    protected static void setEmeraldElite() {
        if (Settings.isFinalActAvailable && !Settings.hasEmeraldKey) {
            ArrayList<MapRoomNode> eliteNodes = new ArrayList<>();

            for (ArrayList<MapRoomNode> aMap : map) {
                for (int j = 0; j < aMap.size(); ++j) {
                    if (((MapRoomNode) ((ArrayList) aMap).get(j)).room instanceof MonsterRoomElite) {
                        eliteNodes.add(aMap.get(j));
                    }
                }
            }

            MapRoomNode chosenNode = eliteNodes.get(mapRng.random(0, eliteNodes.size() - 1));
            chosenNode.hasEmeraldKey = true;
            logger.info("[INFO] Elite nodes identified: " + eliteNodes.size());
            logger.info("[INFO] Emerald Key  placed in: [" + chosenNode.x + "," + chosenNode.y + "]");
        }

    }

    private static void generateRoomTypes(ArrayList<AbstractRoom> roomList, int availableRoomCount) {
        logger.info("Generating Room Types! There are " + availableRoomCount + " rooms:");
        int shopCount = Math.round((float)availableRoomCount * shopRoomChance);
        logger.info(" SHOP (" + toPercentage(shopRoomChance) + "): " + shopCount);
        int restCount = Math.round((float)availableRoomCount * restRoomChance);
        logger.info(" REST (" + toPercentage(restRoomChance) + "): " + restCount);
        int treasureCount = Math.round((float)availableRoomCount * treasureRoomChance);
        logger.info(" TRSRE (" + toPercentage(treasureRoomChance) + "): " + treasureCount);
        int eliteCount;
        if (ModHelper.isModEnabled("Elite Swarm")) {
            eliteCount = Math.round((float)availableRoomCount * eliteRoomChance * 2.5F);
            logger.info(" ELITE (" + toPercentage(eliteRoomChance) + "): " + eliteCount);
        } else if (ascensionLevel >= 1) {
            eliteCount = Math.round((float)availableRoomCount * eliteRoomChance * 1.6F);
            logger.info(" ELITE (" + toPercentage(eliteRoomChance) + "): " + eliteCount);
        } else {
            eliteCount = Math.round((float)availableRoomCount * eliteRoomChance);
            logger.info(" ELITE (" + toPercentage(eliteRoomChance) + "): " + eliteCount);
        }

        int eventCount = Math.round((float)availableRoomCount * eventRoomChance);
        logger.info(" EVNT (" + toPercentage(eventRoomChance) + "): " + eventCount);
        int monsterCount = availableRoomCount - shopCount - restCount - treasureCount - eliteCount - eventCount;
        logger.info(" MSTR (" + toPercentage(1.0F - shopRoomChance - restRoomChance - treasureRoomChance - eliteRoomChance - eventRoomChance) + "): " + monsterCount);

        int i;
        for(i = 0; i < shopCount; ++i) {
            roomList.add(new ShopRoom());
        }

        for(i = 0; i < restCount; ++i) {
            roomList.add(new RestRoom());
        }

        for(i = 0; i < eliteCount; ++i) {
            roomList.add(new MonsterRoomElite());
        }

        for(i = 0; i < eventCount; ++i) {
            roomList.add(new EventRoom());
        }

    }

    private static String toPercentage(float n) {
        return String.format(Locale.ENGLISH, "%.0f", n * 100.0F) + "%";
    }

    private static void firstRoomLogic() {
        initializeFirstRoom();
        leftRoomAvailable = currMapNode.leftNodeAvailable();
        centerRoomAvailable = currMapNode.centerNodeAvailable();
        rightRoomAvailable = currMapNode.rightNodeAvailable();
    }

    private boolean passesDonutCheck(ArrayList<ArrayList<MapRoomNode>> map) {
        logger.info("CASEY'S DONUT CHECK: ");
        int width = (map.get(0)).size();
        int height = map.size();
        logger.info(" HEIGHT: " + height);
        logger.info(" WIDTH:  " + width);
        int nodeCount = 0;
        boolean[] roomHasNode = new boolean[width];

        for(int i = 0; i < width; ++i) {
            roomHasNode[i] = false;
        }

        ArrayList<MapRoomNode> secondToLastRow = map.get(map.size() - 2);

        for (MapRoomNode n : secondToLastRow) {
            MapEdge e;
            for (Iterator var9 = n.getEdges().iterator(); var9.hasNext(); roomHasNode[e.dstX] = true) {
                e = (MapEdge) var9.next();
            }
        }

        int roomCount;
        for(roomCount = 0; roomCount < width - 1; ++roomCount) {
            if (roomHasNode[roomCount]) {
                ++nodeCount;
            }
        }

        if (nodeCount != 1) {
            logger.info(" [FAIL] " + nodeCount + " NODES IN LAST ROW");
            return false;
        } else {
            logger.info(" [SUCCESS] " + nodeCount + " NODE IN LAST ROW");
            roomCount = 0;

            for (ArrayList<MapRoomNode> aMap : map) {
                for (MapRoomNode n : aMap) {
                    if (n.room != null) {
                        ++roomCount;
                    }
                }
            }

            logger.info(" ROOM COUNT: " + roomCount);
            return true;
        }
    }

    public static AbstractRoom getCurrRoom() {
        return currMapNode.getRoom();
    }

    public static MapRoomNode getCurrMapNode() {
        return currMapNode;
    }

    public static void setCurrMapNode(MapRoomNode currMapNode) {
        SoulGroup group = currMapNode.room.souls;
        if (AbstractDungeon.currMapNode != null && getCurrRoom() != null) {
            getCurrRoom().dispose();
        }

        AbstractDungeon.currMapNode = currMapNode;
        if (currMapNode.room == null) {
            logger.warn("This player loaded into a room that no longer exists (due to a new map gen?)");

            for(int i = 0; i < 5; ++i) {
                if (((MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i)).room != null) {
                    currMapNode = (MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i);
                    currMapNode.room = ((MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i)).room;
                    nextRoom.room = ((MapRoomNode)((ArrayList)map.get(currMapNode.y)).get(i)).room;
                    break;
                }
            }
        } else {
            currMapNode.room.souls = group;
        }

    }

    public ArrayList<ArrayList<MapRoomNode>> getMap() {
        return map;
    }

    public static AbstractRelic returnRandomRelic(RelicTier tier) {
        logger.info("Returning " + tier.name() + " relic");
        return RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy();
    }

    public static AbstractRelic returnRandomScreenlessRelic(RelicTier tier) {
        logger.info("Returning " + tier.name() + " relic");

        AbstractRelic tmpRelic;
        for(tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy(); Objects.equals(tmpRelic.relicId, "Bottled Flame") || Objects.equals(tmpRelic.relicId, "Bottled Lightning") || Objects.equals(tmpRelic.relicId, "Bottled Tornado") || Objects.equals(tmpRelic.relicId, "Whetstone"); tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy()) {
        }

        return tmpRelic;
    }

    public static AbstractRelic returnRandomNonCampfireRelic(RelicTier tier) {
        logger.info("Returning " + tier.name() + " relic");

        AbstractRelic tmpRelic;
        for(tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy(); Objects.equals(tmpRelic.relicId, "Peace Pipe") || Objects.equals(tmpRelic.relicId, "Shovel") || Objects.equals(tmpRelic.relicId, "Girya"); tmpRelic = RelicLibrary.getRelic(returnRandomRelicKey(tier)).makeCopy()) {
        }

        return tmpRelic;
    }

    public static AbstractRelic returnRandomRelicEnd(RelicTier tier) {
        logger.info("Returning " + tier.name() + " relic");
        return RelicLibrary.getRelic(returnEndRandomRelicKey(tier)).makeCopy();
    }

    public static String returnEndRandomRelicKey(RelicTier tier) {
        String retVal = null;
        switch(tier) {
            case COMMON:
                if (commonRelicPool.isEmpty()) {
                    retVal = returnRandomRelicKey(RelicTier.UNCOMMON);
                } else {
                    retVal = commonRelicPool.remove(commonRelicPool.size() - 1);
                }
                break;
            case UNCOMMON:
                if (uncommonRelicPool.isEmpty()) {
                    retVal = returnRandomRelicKey(RelicTier.RARE);
                } else {
                    retVal = uncommonRelicPool.remove(uncommonRelicPool.size() - 1);
                }
                break;
            case RARE:
                if (rareRelicPool.isEmpty()) {
                    retVal = "Circlet";
                } else {
                    retVal = rareRelicPool.remove(rareRelicPool.size() - 1);
                }
                break;
            case SHOP:
                if (shopRelicPool.isEmpty()) {
                    retVal = returnRandomRelicKey(RelicTier.UNCOMMON);
                } else {
                    retVal = shopRelicPool.remove(shopRelicPool.size() - 1);
                }
                break;
            case BOSS:
                if (bossRelicPool.isEmpty()) {
                    retVal = "Red Circlet";
                } else {
                    retVal = bossRelicPool.remove(0);
                }
                break;
            default:
                logger.info("Incorrect relic tier: " + tier.name() + " was called in returnEndRandomRelicKey()");
        }

        return !RelicLibrary.getRelic(retVal).canSpawn() ? returnEndRandomRelicKey(tier) : retVal;
    }

    public static String returnRandomRelicKey(RelicTier tier) {
        String retVal = null;
        switch(tier) {
            case COMMON:
                if (commonRelicPool.isEmpty()) {
                    retVal = returnRandomRelicKey(RelicTier.UNCOMMON);
                } else {
                    retVal = commonRelicPool.remove(0);
                }
                break;
            case UNCOMMON:
                if (uncommonRelicPool.isEmpty()) {
                    retVal = returnRandomRelicKey(RelicTier.RARE);
                } else {
                    retVal = uncommonRelicPool.remove(0);
                }
                break;
            case RARE:
                if (rareRelicPool.isEmpty()) {
                    retVal = "Circlet";
                } else {
                    retVal = rareRelicPool.remove(0);
                }
                break;
            case SHOP:
                if (shopRelicPool.isEmpty()) {
                    retVal = returnRandomRelicKey(RelicTier.UNCOMMON);
                } else {
                    retVal = shopRelicPool.remove(0);
                }
                break;
            case BOSS:
                if (bossRelicPool.isEmpty()) {
                    retVal = "Red Circlet";
                } else {
                    retVal = bossRelicPool.remove(0);
                }
                break;
            default:
                logger.info("Incorrect relic tier: " + tier.name() + " was called in returnRandomRelicKey()");
        }

        return !RelicLibrary.getRelic(retVal).canSpawn() ? returnEndRandomRelicKey(tier) : retVal;
    }

    public static RelicTier returnRandomRelicTier() {
        int roll = relicRng.random(0, 99);
        if (roll < commonRelicChance) {
            return RelicTier.COMMON;
        } else {
            return roll < commonRelicChance + uncommonRelicChance ? RelicTier.UNCOMMON : RelicTier.RARE;
        }
    }

    public static AbstractPotion returnTotallyRandomPotion() {
        return PotionHelper.getRandomPotion();
    }

    public static AbstractPotion returnRandomPotion() {
        return returnRandomPotion(false);
    }

    public static AbstractPotion returnRandomPotion(boolean limited) {
        int roll = potionRng.random(0, 99);
        if (roll < PotionHelper.POTION_COMMON_CHANCE) {
            return returnRandomPotion(PotionRarity.COMMON, limited);
        } else {
            return roll < PotionHelper.POTION_UNCOMMON_CHANCE + PotionHelper.POTION_COMMON_CHANCE ? returnRandomPotion(PotionRarity.UNCOMMON, limited) : returnRandomPotion(PotionRarity.RARE, limited);
        }
    }

    public static AbstractPotion returnRandomPotion(PotionRarity rarity, boolean limited) {
        AbstractPotion temp = PotionHelper.getRandomPotion();
        boolean spamCheck = limited;

        while(temp.rarity != rarity || spamCheck) {
            spamCheck = limited;
            temp = PotionHelper.getRandomPotion();
            if (!temp.ID.equals("Fruit Juice")) {
                spamCheck = false;
            }
        }

        return temp;
    }

    public static void transformCard(AbstractCard c) {
        transformCard(c, false);
    }

    public static void transformCard(AbstractCard c, boolean autoUpgrade) {
        transformCard(c, autoUpgrade, new Random());
    }

    public static void transformCard(AbstractCard c, boolean autoUpgrade, Random rng) {
        switch(c.color.name()) {
            case "COLORLESS":
                transformedCard = returnTrulyRandomColorlessCardFromAvailable(c, rng).makeCopy();
                break;
            case "CURSE":
                transformedCard = CardLibrary.getCurse(c, rng).makeCopy();
                break;
            default:
                transformedCard = returnTrulyRandomCardFromAvailable(c, rng).makeCopy();
        }

        UnlockTracker.markCardAsSeen(transformedCard.cardID);
        if (autoUpgrade && transformedCard.canUpgrade()) {
            transformedCard.upgrade();
        }

    }

    public static void srcTransformCard(AbstractCard c) {
        logger.info("Transform using SRC pool...");
        switch(c.rarity) {
            case BASIC:
                transformedCard = srcCommonCardPool.getRandomCard(false).makeCopy();
                break;
            case COMMON:
                srcCommonCardPool.removeCard(c.cardID);
                transformedCard = srcCommonCardPool.getRandomCard(false).makeCopy();
                srcCommonCardPool.addToTop(c.makeCopy());
                break;
            case UNCOMMON:
                srcUncommonCardPool.removeCard(c.cardID);
                transformedCard = srcUncommonCardPool.getRandomCard(false).makeCopy();
                srcUncommonCardPool.addToTop(c.makeCopy());
                break;
            case RARE:
                srcRareCardPool.removeCard(c.cardID);
                if (srcRareCardPool.isEmpty()) {
                    transformedCard = srcUncommonCardPool.getRandomCard(false).makeCopy();
                } else {
                    transformedCard = srcRareCardPool.getRandomCard(false).makeCopy();
                }

                srcRareCardPool.addToTop(c.makeCopy());
                break;
            case CURSE:
                if (!srcRareCardPool.isEmpty()) {
                    transformedCard = srcRareCardPool.getRandomCard(false).makeCopy();
                } else {
                    transformedCard = srcUncommonCardPool.getRandomCard(false).makeCopy();
                }
            default:
                logger.info("Transform called on a strange card type: " + c.type.name());
                transformedCard = srcCommonCardPool.getRandomCard(false).makeCopy();
        }

    }

    public static CardGroup getEachRare() {
        CardGroup everyRareCard = new CardGroup(CardGroupType.UNSPECIFIED);

        for (AbstractCard c : rareCardPool.group) {
            everyRareCard.addToBottom(c.makeCopy());
        }

        return everyRareCard;
    }

    public static AbstractCard returnRandomCard() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        CardRarity rarity = rollRarity();
        if (rarity.equals(CardRarity.COMMON)) {
            list.addAll(srcCommonCardPool.group);
        } else if (rarity.equals(CardRarity.UNCOMMON)) {
            list.addAll(srcUncommonCardPool.group);
        } else {
            list.addAll(srcRareCardPool.group);
        }

        return list.get(cardRandomRng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomCard() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.addAll(srcCommonCardPool.group);
        list.addAll(srcUncommonCardPool.group);
        list.addAll(srcRareCardPool.group);
        return list.get(cardRandomRng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomCardInCombat() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        Iterator var1 = srcCommonCardPool.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (!c.hasTag(CardTags.HEALING)) {
                list.add(c);
                UnlockTracker.markCardAsSeen(c.cardID);
            }
        }

        var1 = srcUncommonCardPool.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (!c.hasTag(CardTags.HEALING)) {
                list.add(c);
                UnlockTracker.markCardAsSeen(c.cardID);
            }
        }

        var1 = srcRareCardPool.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (!c.hasTag(CardTags.HEALING)) {
                list.add(c);
                UnlockTracker.markCardAsSeen(c.cardID);
            }
        }

        return list.get(cardRandomRng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomCardInCombat(CardType type) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        Iterator var2 = srcCommonCardPool.group.iterator();

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.type == type && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = srcUncommonCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.type == type && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        var2 = srcRareCardPool.group.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.type == type && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        return list.get(cardRandomRng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomColorlessCardInCombat() {
        return returnTrulyRandomColorlessCardInCombat(cardRandomRng);
    }

    public static AbstractCard returnTrulyRandomColorlessCardInCombat(String prohibitedID) {
        return returnTrulyRandomColorlessCardFromAvailable(prohibitedID, cardRandomRng);
    }

    public static AbstractCard returnTrulyRandomColorlessCardInCombat(Random rng) {
        ArrayList<AbstractCard> list = new ArrayList<>();

        for (AbstractCard c : srcColorlessCardPool.group) {
            if (!c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        return list.get(rng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomColorlessCardFromAvailable(String prohibited, Random rng) {
        ArrayList<AbstractCard> list = new ArrayList<>();

        for (AbstractCard c : srcColorlessCardPool.group) {
            if (!c.cardID.equals(prohibited)) {
                list.add(c);
            }
        }

        return list.get(rng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomColorlessCardFromAvailable(AbstractCard prohibited, Random rng) {
        ArrayList<AbstractCard> list = new ArrayList<>();

        for (AbstractCard c : srcColorlessCardPool.group) {
            if (!Objects.equals(c.cardID, prohibited.cardID)) {
                list.add(c);
            }
        }

        return list.get(rng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomCardFromAvailable(AbstractCard prohibited, Random rng) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        Iterator var3;
        AbstractCard c;
        switch(prohibited.color.name()) {
            case "COLORLESS":
                var3 = colorlessCardPool.group.iterator();

                while(var3.hasNext()) {
                    c = (AbstractCard)var3.next();
                    if (!Objects.equals(c.cardID, prohibited.cardID)) {
                        list.add(c);
                    }
                }

                return (list.get(rng.random(list.size() - 1))).makeCopy();
            case "CURSE":
                return CardLibrary.getCurse();
            default:
                var3 = commonCardPool.group.iterator();

                while(var3.hasNext()) {
                    c = (AbstractCard)var3.next();
                    if (!Objects.equals(c.cardID, prohibited.cardID)) {
                        list.add(c);
                    }
                }

                var3 = srcUncommonCardPool.group.iterator();

                while(var3.hasNext()) {
                    c = (AbstractCard)var3.next();
                    if (!Objects.equals(c.cardID, prohibited.cardID)) {
                        list.add(c);
                    }
                }

                var3 = srcRareCardPool.group.iterator();

                while(var3.hasNext()) {
                    c = (AbstractCard)var3.next();
                    if (!Objects.equals(c.cardID, prohibited.cardID)) {
                        list.add(c);
                    }
                }

                return list.get(rng.random(list.size() - 1)).makeCopy();
        }
    }

    public static AbstractCard returnTrulyRandomCardFromAvailable(AbstractCard prohibited) {
        return returnTrulyRandomCardFromAvailable(prohibited, new Random());
    }

    public static AbstractCard getTransformedCard() {
        AbstractCard retVal = transformedCard;
        transformedCard = null;
        return retVal;
    }

    public void populateFirstStrongEnemy(ArrayList<MonsterInfo> monsters, ArrayList<String> exclusions) {
        String m;
        do {
            m = MonsterInfo.roll(monsters, monsterRng.random());
        } while(exclusions.contains(m));

        monsterList.add(m);
    }

    public void populateMonsterList(ArrayList<MonsterInfo> monsters, int numMonsters, boolean elites) {
        if (elites) {
            for (int i = 0; i < numMonsters; i++) {
                if (eliteMonsterList.isEmpty()) {
                    eliteMonsterList.add(MonsterInfo.roll(monsters, monsterRng.random()));
                } else {
                    String toAdd = MonsterInfo.roll(monsters, monsterRng.random());
                    if (!toAdd.equals(eliteMonsterList.get(eliteMonsterList.size() - 1))) {
                        eliteMonsterList.add(toAdd);
                    } else {
                        i--;
                    }
                }
            }
        } else {
            for (int i = 0; i < numMonsters; i++) {
                if (monsterList.isEmpty()) {
                    monsterList.add(MonsterInfo.roll(monsters, monsterRng.random()));
                } else {
                    String toAdd = MonsterInfo.roll(monsters, monsterRng.random());
                    if (!toAdd.equals(monsterList.get(monsterList.size() - 1))) {
                        if (monsterList.size() > 1 && toAdd.equals(monsterList.get(monsterList.size() - 2))) {
                            i--;
                        } else {
                            monsterList.add(toAdd);
                        }
                    } else {
                        i--;
                    }
                }
            }
        }

    }

    protected abstract ArrayList<String> generateExclusions();

    public static AbstractCard returnColorlessCard(CardRarity rarity) {
        Collections.shuffle(colorlessCardPool.group, new java.util.Random(shuffleRng.randomLong()));
        Iterator var1 = colorlessCardPool.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.rarity == rarity) {
                return c.makeCopy();
            }
        }

        if (rarity == CardRarity.RARE) {
            var1 = colorlessCardPool.group.iterator();

            while(var1.hasNext()) {
                c = (AbstractCard)var1.next();
                if (c.rarity == CardRarity.UNCOMMON) {
                    return c.makeCopy();
                }
            }
        }

        return new SwiftStrike();
    }

    public static AbstractCard returnColorlessCard() {
        Collections.shuffle(colorlessCardPool.group);
        Iterator var0 = colorlessCardPool.group.iterator();
        if (var0.hasNext()) {
            AbstractCard c = (AbstractCard)var0.next();
            return c.makeCopy();
        } else {
            return new SwiftStrike();
        }
    }

    public static AbstractCard returnRandomCurse() {
        AbstractCard c = CardLibrary.getCurse().makeCopy();
        UnlockTracker.markCardAsSeen(c.cardID);
        return c;
    }

    public void initializePotions() {
        PotionHelper.initialize(player.chosenClass);
    }

    public void initializeCardPools() {
        logger.info("INIT CARD POOL");
        long startTime = System.currentTimeMillis();
        commonCardPool.clear();
        uncommonCardPool.clear();
        rareCardPool.clear();
        colorlessCardPool.clear();
        curseCardPool.clear();
        ArrayList<AbstractCard> tmpPool = new ArrayList<>();
        if (ModHelper.isModEnabled("Colorless Cards")) {
            CardLibrary.addColorlessCards(tmpPool);
        }

        if (ModHelper.isModEnabled("Diverse")) {
            CardLibrary.addRedCards(tmpPool);
            CardLibrary.addGreenCards(tmpPool);
            CardLibrary.addBlueCards(tmpPool);
            if (!UnlockTracker.isCharacterLocked("Watcher")) {
                CardLibrary.addPurpleCards(tmpPool);
            }
        } else {
            player.getCardPool(tmpPool);
        }

        this.addColorlessCards();
        this.addCurseCards();
        Iterator var4 = tmpPool.iterator();

        AbstractCard c;
        while(var4.hasNext()) {
            c = (AbstractCard)var4.next();
            switch(c.rarity) {
                case COMMON:
                    commonCardPool.addToTop(c);
                    break;
                case UNCOMMON:
                    uncommonCardPool.addToTop(c);
                    break;
                case RARE:
                    rareCardPool.addToTop(c);
                    break;
                case CURSE:
                    curseCardPool.addToTop(c);
                    break;
                default:
                    logger.info("Unspecified rarity: " + c.rarity.name() + " when creating pools! AbstractDungeon: Line 827");
            }
        }

        srcColorlessCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcCurseCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcRareCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcUncommonCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcCommonCardPool = new CardGroup(CardGroupType.CARD_POOL);
        var4 = colorlessCardPool.group.iterator();

        while(var4.hasNext()) {
            c = (AbstractCard)var4.next();
            srcColorlessCardPool.addToBottom(c);
        }

        var4 = curseCardPool.group.iterator();

        while(var4.hasNext()) {
            c = (AbstractCard)var4.next();
            srcCurseCardPool.addToBottom(c);
        }

        var4 = rareCardPool.group.iterator();

        while(var4.hasNext()) {
            c = (AbstractCard)var4.next();
            srcRareCardPool.addToBottom(c);
        }

        var4 = uncommonCardPool.group.iterator();

        while(var4.hasNext()) {
            c = (AbstractCard)var4.next();
            srcUncommonCardPool.addToBottom(c);
        }

        var4 = commonCardPool.group.iterator();

        while(var4.hasNext()) {
            c = (AbstractCard)var4.next();
            srcCommonCardPool.addToBottom(c);
        }

        logger.info("Cardpool load time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void addColorlessCards() {

        for (Entry<String, AbstractCard> o : CardLibrary.cards.entrySet()) {
            AbstractCard card = o.getValue();
            if (card.color == CardColor.COLORLESS && card.rarity != CardRarity.BASIC && card.rarity != CardRarity.SPECIAL && card.type != CardType.STATUS) {
                colorlessCardPool.addToTop(card);
            }
        }

        logger.info("COLORLESS CARDS: " + colorlessCardPool.size());
    }

    private void addCurseCards() {

        for (Entry<String, AbstractCard> o : CardLibrary.cards.entrySet()) {
            AbstractCard card = o.getValue();
            if (card.type == CardType.CURSE && !Objects.equals(card.cardID, "Necronomicurse") && !Objects.equals(card.cardID, "AscendersBane") && !Objects.equals(card.cardID, "CurseOfTheBell") && !Objects.equals(card.cardID, "Pride")) {
                curseCardPool.addToTop(card);
            }
        }

        logger.info("CURSE CARDS: " + curseCardPool.size());
    }

    protected void initializeRelicList() {
        commonRelicPool.clear();
        uncommonRelicPool.clear();
        rareRelicPool.clear();
        shopRelicPool.clear();
        bossRelicPool.clear();
        RelicLibrary.populateRelicPool(commonRelicPool, RelicTier.COMMON, player.chosenClass);
        RelicLibrary.populateRelicPool(uncommonRelicPool, RelicTier.UNCOMMON, player.chosenClass);
        RelicLibrary.populateRelicPool(rareRelicPool, RelicTier.RARE, player.chosenClass);
        RelicLibrary.populateRelicPool(shopRelicPool, RelicTier.SHOP, player.chosenClass);
        RelicLibrary.populateRelicPool(bossRelicPool, RelicTier.BOSS, player.chosenClass);
        Iterator var1;
        if (floorNum >= 1) {
            var1 = player.relics.iterator();

            while(var1.hasNext()) {
                AbstractRelic r = (AbstractRelic)var1.next();
                relicsToRemoveOnStart.add(r.relicId);
            }
        }

        Collections.shuffle(commonRelicPool, new java.util.Random(relicRng.randomLong()));
        Collections.shuffle(uncommonRelicPool, new java.util.Random(relicRng.randomLong()));
        Collections.shuffle(rareRelicPool, new java.util.Random(relicRng.randomLong()));
        Collections.shuffle(shopRelicPool, new java.util.Random(relicRng.randomLong()));
        Collections.shuffle(bossRelicPool, new java.util.Random(relicRng.randomLong()));
        if (ModHelper.isModEnabled("Flight") || ModHelper.isModEnabled("Uncertain Future")) {
            relicsToRemoveOnStart.add("WingedGreaves");
        }

        if (ModHelper.isModEnabled("Diverse")) {
            relicsToRemoveOnStart.add("PrismaticShard");
        }

        if (ModHelper.isModEnabled("DeadlyEvents")) {
            relicsToRemoveOnStart.add("Juzu Bracelet");
        }

        if (ModHelper.isModEnabled("Hoarder")) {
            relicsToRemoveOnStart.add("Smiling Mask");
        }

        if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("SealedDeck") || ModHelper.isModEnabled("Shiny") || ModHelper.isModEnabled("Insanity")) {
            relicsToRemoveOnStart.add("Pandora's Box");
        }

        var1 = relicsToRemoveOnStart.iterator();

        while(true) {
            String s;
            while(var1.hasNext()) {
                s = (String)var1.next();
                Iterator s1 = commonRelicPool.iterator();

                String derp;
                while(s1.hasNext()) {
                    derp = (String)s1.next();
                    if (derp.equals(s)) {
                        s1.remove();
                        logger.info(derp + " removed.");
                        break;
                    }
                }

                s1 = uncommonRelicPool.iterator();

                while(s1.hasNext()) {
                    derp = (String)s1.next();
                    if (derp.equals(s)) {
                        s1.remove();
                        logger.info(derp + " removed.");
                        break;
                    }
                }

                s1 = rareRelicPool.iterator();

                while(s1.hasNext()) {
                    derp = (String)s1.next();
                    if (derp.equals(s)) {
                        s1.remove();
                        logger.info(derp + " removed.");
                        break;
                    }
                }

                s1 = bossRelicPool.iterator();

                while(s1.hasNext()) {
                    derp = (String)s1.next();
                    if (derp.equals(s)) {
                        s1.remove();
                        logger.info(derp + " removed.");
                        break;
                    }
                }

                s1 = shopRelicPool.iterator();

                while(s1.hasNext()) {
                    derp = (String)s1.next();
                    if (derp.equals(s)) {
                        s1.remove();
                        logger.info(derp + " removed.");
                        break;
                    }
                }
            }

            if (Settings.isDebug) {
                logger.info("Relic (Common):");
                var1 = commonRelicPool.iterator();

                while(var1.hasNext()) {
                    s = (String)var1.next();
                    logger.info(" " + s);
                }

                logger.info("Relic (Uncommon):");
                var1 = uncommonRelicPool.iterator();

                while(var1.hasNext()) {
                    s = (String)var1.next();
                    logger.info(" " + s);
                }

                logger.info("Relic (Rare):");
                var1 = rareRelicPool.iterator();

                while(var1.hasNext()) {
                    s = (String)var1.next();
                    logger.info(" " + s);
                }

                logger.info("Relic (Shop):");
                var1 = shopRelicPool.iterator();

                while(var1.hasNext()) {
                    s = (String)var1.next();
                    logger.info(" " + s);
                }

                logger.info("Relic (Boss):");
                var1 = bossRelicPool.iterator();

                while(var1.hasNext()) {
                    s = (String)var1.next();
                    logger.info(" " + s);
                }
            }

            return;
        }
    }

    protected abstract void generateMonsters();

    protected abstract void generateWeakEnemies(int var1);

    protected abstract void generateStrongEnemies(int var1);

    protected abstract void generateElites(int var1);

    protected abstract void initializeBoss();

    protected abstract void initializeEventList();

    protected abstract void initializeEventImg();

    protected abstract void initializeShrineList();

    public void initializeSpecialOneTimeEventList() {
        specialOneTimeEventList.clear();
        specialOneTimeEventList.add("Accursed Blacksmith");
        specialOneTimeEventList.add("Bonfire Elementals");
        specialOneTimeEventList.add("Designer");
        specialOneTimeEventList.add("Duplicator");
        specialOneTimeEventList.add("FaceTrader");
        specialOneTimeEventList.add("Fountain of Cleansing");
        specialOneTimeEventList.add("Knowing Skull");
        specialOneTimeEventList.add("Lab");
        specialOneTimeEventList.add("N'loth");
        if (this.isNoteForYourselfAvailable()) {
            specialOneTimeEventList.add("NoteForYourself");
        }

        specialOneTimeEventList.add("SecretPortal");
        specialOneTimeEventList.add("The Joust");
        specialOneTimeEventList.add("WeMeetAgain");
        specialOneTimeEventList.add("The Woman in Blue");

        EventUtils.eventLogger.info("Adding conditional SpecialOneTimeEvents.");

        for (Map.Entry<String, ConditionalEvent<? extends AbstractEvent>> e : EventUtils.oneTimeEvents.entrySet()) {
            if (!e.getValue().spawnCondition.test()) {
                if (e.getValue().playerClass == null || AbstractDungeon.player.chosenClass.equals(e.getValue().playerClass)) {
                    EventUtils.eventLogger.info("Added " + e.getValue() + " to the specialOneTimeEventList.");
                    AbstractDungeon.specialOneTimeEventList.add(e.getKey());
                }
            }
        }

        EventUtils.eventLogger.info("Checking for SpecialOneTimeEvent replacements...");
        for (Map.Entry<String, ArrayList<ConditionalEvent<? extends AbstractEvent>>> replacements : EventUtils.fullReplaceEventList.entrySet()) {
            if (AbstractDungeon.specialOneTimeEventList.contains(replacements.getKey())) {
                for (ConditionalEvent<? extends AbstractEvent> e : replacements.getValue()) {
                    if (e.isValid()) {
                        AbstractDungeon.specialOneTimeEventList.add(AbstractDungeon.specialOneTimeEventList.indexOf(replacements.getKey()), e.overrideEvent);
                        AbstractDungeon.specialOneTimeEventList.remove(replacements.getKey());
                        EventUtils.eventLogger.info("Replaced " + replacements.getKey() + " with " + e.overrideEvent + " in special one time event list for " + AbstractDungeon.id + ".");
                    }
                }
            }
        }
    }

    private boolean isNoteForYourselfAvailable() {
        if (Settings.isDailyRun) {
            logger.info("Note For Yourself is disabled due to Daily Run");
            return false;
        } else if (ascensionLevel >= 15) {
            logger.info("Note For Yourself is disabled beyond Ascension 15+");
            return false;
        } else if (ascensionLevel == 0) {
            logger.info("Note For Yourself is enabled due to No Ascension");
            return true;
        } else if (ascensionLevel < player.getPrefs().getInteger("ASCENSION_LEVEL")) {
            logger.info("Note For Yourself is enabled as it's less than Highest Unlocked Ascension");
            return true;
        } else {
            logger.info("Note For Yourself is disabled as requirements aren't met");
            return false;
        }
    }

    public static ArrayList<AbstractCard> getColorlessRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;

        AbstractRelic r;
        for(Iterator var2 = player.relics.iterator(); var2.hasNext(); numCards = r.changeNumberOfCardsInReward(numCards)) {
            r = (AbstractRelic)var2.next();
        }

        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }

        AbstractCard card;
        for(int i = 0; i < numCards; ++i) {
            CardRarity rarity = rollRareOrUncommon(colorlessRareChance);
            card = null;
            switch(rarity) {
                case UNCOMMON:
                    card = getColorlessCardFromPool(rarity);
                    break;
                case RARE:
                    card = getColorlessCardFromPool(rarity);
                    cardBlizzRandomizer = cardBlizzStartOffset;
                    break;
                default:
                    logger.info("WTF?");
            }

            for(; retVal.contains(card); card = getColorlessCardFromPool(rarity)) {
                if (card != null) {
                    logger.info("DUPE: " + card.originalName);
                }
            }

            if (card != null) {
                retVal.add(card);
            }
        }

        ArrayList<AbstractCard> retVal2 = new ArrayList<>();

        for (AbstractCard aRetVal : retVal) {
            card = aRetVal;
            retVal2.add(card.makeCopy());
        }

        return retVal2;
    }

    public static ArrayList<AbstractCard> getRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;

        for (AbstractRelic relic : player.relics) {
            numCards = relic.changeNumberOfCardsInReward(numCards);
        }

        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }

        AbstractCard card;
        for (int i = 0; i < numCards; ++i) {
            CardRarity rarity = rollRarity();
            card = null;
            switch(rarity) {
                case COMMON:
                    cardBlizzRandomizer -= cardBlizzGrowth;
                    if (cardBlizzRandomizer <= cardBlizzMaxOffset) {
                        cardBlizzRandomizer = cardBlizzMaxOffset;
                    }
                case UNCOMMON:
                    break;
                case RARE:
                    cardBlizzRandomizer = cardBlizzStartOffset;
                    break;
                default:
                    logger.info("WTF?");
            }

            boolean containsDupe = true;

            while(containsDupe) {
                containsDupe = false;
                if (player.hasRelic("PrismaticShard")) {
                    card = CardLibrary.getAnyColorCard(rarity);
                } else {
                    card = getCard(rarity);
                }

                for (AbstractCard c : retVal) {
                    if (card != null && c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        break;
                    }
                }
            }

            if (card != null) {
                retVal.add(card);
            }
        }

        ArrayList<AbstractCard> retVal2 = new ArrayList<>();

        for (AbstractCard aRetVal : retVal) {
            retVal2.add(aRetVal.makeCopy());
        }

        for (AbstractCard c : retVal2) {
            if (c.rarity != CardRarity.RARE && cardRng.randomBoolean(cardUpgradedChance) && c.canUpgrade()) {
                c.upgrade();
            } else {
                for (AbstractRelic r1 : player.relics) {
                    r1.onPreviewObtainCard(c);
                }
            }
        }

        return retVal2;

    }

    public static AbstractCard getCard(CardRarity rarity) {
        switch(rarity) {
            case COMMON:
                return commonCardPool.getRandomCard(true);
            case UNCOMMON:
                return uncommonCardPool.getRandomCard(true);
            case RARE:
                return rareCardPool.getRandomCard(true);
            case CURSE:
                return curseCardPool.getRandomCard(true);
            default:
                logger.info("No rarity on getCard in Abstract Dungeon");
                return null;
        }
    }

    public static AbstractCard getCard(CardRarity rarity, Random rng) {
        switch(rarity) {
            case COMMON:
                return commonCardPool.getRandomCard(rng);
            case UNCOMMON:
                return uncommonCardPool.getRandomCard(rng);
            case RARE:
                return rareCardPool.getRandomCard(rng);
            case CURSE:
                return curseCardPool.getRandomCard(rng);
            default:
                logger.info("No rarity on getCard in Abstract Dungeon");
                return null;
        }
    }

    public static AbstractCard getCardWithoutRng(CardRarity rarity) {
        switch(rarity) {
            case COMMON:
                return commonCardPool.getRandomCard(false);
            case UNCOMMON:
                return uncommonCardPool.getRandomCard(false);
            case RARE:
                return rareCardPool.getRandomCard(false);
            case CURSE:
                return returnRandomCurse();
            default:
                logger.info("Check getCardWithoutRng");
                return null;
        }
    }

    public static AbstractCard getCardFromPool(CardRarity rarity, CardType type, boolean useRng) {
        AbstractCard retVal;
        switch(rarity) {
            case RARE:
                retVal = rareCardPool.getRandomCard(type, useRng);
                if (retVal != null) {
                    return retVal;
                } else {
                    logger.info("ERROR: Could not find Rare card of type: " + type.name());
                }
            case UNCOMMON:
                retVal = uncommonCardPool.getRandomCard(type, useRng);
                if (retVal != null) {
                    return retVal;
                } else if (type == CardType.POWER) {
                    return getCardFromPool(CardRarity.RARE, type, useRng);
                } else {
                    logger.info("ERROR: Could not find Uncommon card of type: " + type.name());
                }
            case COMMON:
                retVal = commonCardPool.getRandomCard(type, useRng);
                if (retVal != null) {
                    return retVal;
                } else if (type == CardType.POWER) {
                    return getCardFromPool(CardRarity.UNCOMMON, type, useRng);
                } else {
                    logger.info("ERROR: Could not find Common card of type: " + type.name());
                }
            case CURSE:
                retVal = curseCardPool.getRandomCard(type, useRng);
                if (retVal != null) {
                    return retVal;
                } else {
                    logger.info("ERROR: Could not find Curse card of type: " + type.name());
                }
            default:
                logger.info("ERROR: Default in getCardFromPool");
                return null;
        }
    }

    public static AbstractCard getColorlessCardFromPool(CardRarity rarity) {
        AbstractCard retVal;
        switch(rarity) {
            case RARE:
                retVal = colorlessCardPool.getRandomCard(true, rarity);
                if (retVal != null) {
                    return retVal;
                }
            case UNCOMMON:
                retVal = colorlessCardPool.getRandomCard(true, rarity);
                if (retVal != null) {
                    return retVal;
                }
            default:
                logger.info("ERROR: getColorlessCardFromPool");
                return null;
        }
    }

    public static CardRarity rollRarity(Random rng) {
        int roll = rng.random(99);
        roll += cardBlizzRandomizer;
        return currMapNode == null ? getCardRarityFallback(roll) : getCurrRoom().getCardRarity(roll);
    }

    private static CardRarity getCardRarityFallback(int roll) {
        int rareRate = 3;
        if (roll < rareRate) {
            return CardRarity.RARE;
        } else {
            return roll < 40 ? CardRarity.UNCOMMON : CardRarity.COMMON;
        }
    }

    public static CardRarity rollRarity() {
        return rollRarity(cardRng);
    }

    public static CardRarity rollRareOrUncommon(float rareChance) {
        return cardRng.randomBoolean(rareChance) ? CardRarity.RARE : CardRarity.UNCOMMON;
    }

    public static AbstractMonster getRandomMonster() {
        return currMapNode.room.monsters.getRandomMonster(null, true, cardRandomRng);
    }

    public static AbstractMonster getRandomMonster(AbstractMonster except) {
        return currMapNode.room.monsters.getRandomMonster(except, true, cardRandomRng);
    }

    public static void nextRoomTransitionStart() {
        fadeOut();
        waitingOnFadeOut = true;
        overlayMenu.proceedButton.hide();
        if (ModHelper.isModEnabled("Terminal")) {
            player.decreaseMaxHealth(1);
        }

    }

    public static void initializeFirstRoom() {
        fadeIn();
        ++floorNum;
        if (currMapNode.room instanceof MonsterRoom) {
            if (!CardCrawlGame.loadingSave) {
                if (SaveHelper.shouldSave()) {
                    SaveHelper.saveIfAppropriate(SaveType.ENTER_ROOM);
                } else {
                    Metrics metrics = new Metrics();
                    metrics.setValues(false, false, null, MetricRequestType.NONE);
                    metrics.gatherAllDataAndSave(false, false, null);
                }
            }

            --floorNum;
        }

        scene.nextRoom(currMapNode.room);
    }

    public static void resetPlayer() {
        player.orbs.clear();
        player.animX = 0.0F;
        player.animY = 0.0F;
        player.hideHealthBar();
        player.hand.clear();
        player.powers.clear();
        player.drawPile.clear();
        player.discardPile.clear();
        player.exhaustPile.clear();
        player.limbo.clear();
        player.loseBlock(true);
        player.damagedThisCombat = 0;
        if (!player.stance.ID.equals("Neutral")) {
            player.stance = new NeutralStance();
            player.onStanceChange("Neutral");
        }

        GameActionManager.turn = 1;
    }

    public void nextRoomTransition() {
        this.nextRoomTransition(null);
    }

    public void nextRoomTransition(SaveFile saveFile) {
        overlayMenu.proceedButton.setLabel(TEXT[0]);
        combatRewardScreen.clear();
        if (nextRoom != null && nextRoom.room != null) {
            nextRoom.room.rewards.clear();
        }

        if (getCurrRoom() instanceof MonsterRoomElite) {
            if (!eliteMonsterList.isEmpty()) {
                logger.info("Removing elite: " + eliteMonsterList.get(0) + " from monster list.");
                eliteMonsterList.remove(0);
            } else {
                this.generateElites(10);
            }
        } else if (getCurrRoom() instanceof MonsterRoom) {
            if (!monsterList.isEmpty()) {
                logger.info("Removing monster: " + monsterList.get(0) + " from monster list.");
                monsterList.remove(0);
            } else {
                this.generateStrongEnemies(12);
            }
        } else if (getCurrRoom() instanceof EventRoom && getCurrRoom().event instanceof NoteForYourself) {
            AbstractCard tmpCard = ((NoteForYourself)getCurrRoom().event).saveCard;
            if (tmpCard != null) {
                CardCrawlGame.playerPref.putString("NOTE_CARD", tmpCard.cardID);
                CardCrawlGame.playerPref.putInteger("NOTE_UPGRADE", tmpCard.timesUpgraded);
                CardCrawlGame.playerPref.flush();
            }
        }

        if (RestRoom.lastFireSoundId != 0L) {
            CardCrawlGame.sound.fadeOut("REST_FIRE_WET", RestRoom.lastFireSoundId);
        }

        if (player.stance != null && !player.stance.ID.equals("Neutral")) {
            player.stance.stopIdleSfx();
        }

        gridSelectScreen.upgradePreviewCard = null;
        previousScreen = null;
        dynamicBanner.hide();
        dungeonMapScreen.closeInstantly();
        closeCurrentScreen();
        topPanel.unhoverHitboxes();
        fadeIn();
        player.resetControllerValues();
        effectList.clear();
        Iterator i = topLevelEffects.iterator();

        while(i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)i.next();
            if (!(e instanceof ObtainKeyEffect)) {
                i.remove();
            }
        }

        topLevelEffectsQueue.clear();
        effectsQueue.clear();
        dungeonMapScreen.dismissable = true;
        dungeonMapScreen.map.legend.isLegendHighlighted = false;
        resetPlayer();
        if (!CardCrawlGame.loadingSave) {
            this.incrementFloorBasedMetrics();
            ++floorNum;
            if (!TipTracker.tips.get("INTENT_TIP") && floorNum == 6) {
                TipTracker.neverShowAgain("INTENT_TIP");
            }

            StatsScreen.incrementFloorClimbed();
            SaveHelper.saveIfAppropriate(SaveType.ENTER_ROOM);
        }

        monsterHpRng = new Random(Settings.seed + (long)floorNum);
        aiRng = new Random(Settings.seed + (long)floorNum);
        shuffleRng = new Random(Settings.seed + (long)floorNum);
        cardRandomRng = new Random(Settings.seed + (long)floorNum);
        miscRng = new Random(Settings.seed + (long)floorNum);
        boolean isLoadingPostCombatSave = CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat;
        boolean isLoadingCompletedEvent = false;
        Iterator var4;
        AbstractRelic r;
        if (nextRoom != null && !isLoadingPostCombatSave) {
            var4 = player.relics.iterator();

            while(var4.hasNext()) {
                r = (AbstractRelic)var4.next();
                r.onEnterRoom(nextRoom.room);
            }
        }

        if (!actionManager.actions.isEmpty()) {
            logger.info("[WARNING] Line:1904: Action Manager was NOT clear! Clearing");
            actionManager.clear();
        }

        String eventKey;
        if (nextRoom != null) {
            eventKey = nextRoom.room.getMapSymbol();
            if (nextRoom.room instanceof EventRoom) {
                Random eventRngDuplicate = new Random(Settings.seed, eventRng.counter);
                RoomResult roomResult = EventHelper.roll(eventRngDuplicate);
                isLoadingCompletedEvent = isLoadingPostCombatSave && roomResult == RoomResult.EVENT;
                if (!isLoadingCompletedEvent) {
                    eventRng = eventRngDuplicate;
                    nextRoom.room = this.generateRoom(roomResult);
                }

                eventKey = nextRoom.room.getMapSymbol();
                if (nextRoom.room instanceof MonsterRoom || nextRoom.room instanceof MonsterRoomElite) {
                    nextRoom.room.combatEvent = true;
                }

                nextRoom.room.setMapSymbol("?");
                nextRoom.room.setMapImg(ImageMaster.MAP_NODE_EVENT, ImageMaster.MAP_NODE_EVENT_OUTLINE);
            }

            if (!isLoadingPostCombatSave) {
                CardCrawlGame.metricData.path_per_floor.add(eventKey);
            }

            setCurrMapNode(nextRoom);
        }

        if (getCurrRoom() != null && !isLoadingPostCombatSave) {
            var4 = player.relics.iterator();

            while(var4.hasNext()) {
                r = (AbstractRelic)var4.next();
                r.justEnteredRoom(getCurrRoom());
            }
        }

        if (isLoadingCompletedEvent) {
            getCurrRoom().phase = RoomPhase.COMPLETE;
            eventKey = (String)(saveFile.metric_event_choices.get(saveFile.metric_event_choices.size() - 1)).get("event_name");
            ((EventRoom)getCurrRoom()).event = EventHelper.getEvent(eventKey);
        } else {
            if (isAscensionMode) {
                CardCrawlGame.publisherIntegration.setRichPresenceDisplayPlaying(floorNum, ascensionLevel, player.getLocalizedCharacterName());
            } else {
                CardCrawlGame.publisherIntegration.setRichPresenceDisplayPlaying(floorNum, player.getLocalizedCharacterName());
            }

            getCurrRoom().onPlayerEntry();
        }

        if (getCurrRoom() instanceof MonsterRoom && lastCombatMetricKey.equals("Shield and Spear")) {
            player.movePosition((float)Settings.WIDTH / 2.0F, floorY);
        } else {
            player.movePosition((float)Settings.WIDTH * 0.25F, floorY);
            player.flipHorizontal = false;
        }

        if (currMapNode.room instanceof MonsterRoom && !isLoadingPostCombatSave) {
            player.preBattlePrep();
        }

        scene.nextRoom(currMapNode.room);
        if (currMapNode.room instanceof RestRoom) {
            rs = AbstractDungeon.RenderScene.CAMPFIRE;
        } else if (currMapNode.room.event instanceof AbstractImageEvent) {
            rs = AbstractDungeon.RenderScene.EVENT;
        } else {
            rs = AbstractDungeon.RenderScene.NORMAL;
        }

    }

    private void incrementFloorBasedMetrics() {
        if (floorNum != 0) {
            CardCrawlGame.metricData.current_hp_per_floor.add(player.currentHealth);
            CardCrawlGame.metricData.max_hp_per_floor.add(player.maxHealth);
            CardCrawlGame.metricData.gold_per_floor.add(player.gold);
        }

    }

    private AbstractRoom generateRoom(RoomResult roomType) {
        logger.info("GENERATING ROOM: " + roomType.name());
        switch(roomType) {
            case ELITE:
                return new MonsterRoomElite();
            case MONSTER:
                return new MonsterRoom();
            case SHOP:
                return new ShopRoom();
            case TREASURE:
                return new TreasureRoom();
            default:
                return new EventRoom();
        }
    }

    public static MonsterGroup getMonsters() {
        return getCurrRoom().monsters;
    }

    public MonsterGroup getMonsterForRoomCreation() {
        if (monsterList.isEmpty()) {
            this.generateStrongEnemies(12);
        }

        logger.info("MONSTER: " + monsterList.get(0));
        lastCombatMetricKey = monsterList.get(0);
        return MonsterHelper.getEncounter(monsterList.get(0));
    }

    public MonsterGroup getEliteMonsterForRoomCreation() {
        if (eliteMonsterList.isEmpty()) {
            this.generateElites(10);
        }

        logger.info("ELITE: " + eliteMonsterList.get(0));
        lastCombatMetricKey = eliteMonsterList.get(0);
        return MonsterHelper.getEncounter(eliteMonsterList.get(0));
    }

    public static AbstractEvent generateEvent(Random rng) {
        if (rng.random(1.0F) < shrineChance) {
            if (shrineList.isEmpty() && specialOneTimeEventList.isEmpty()) {
                if (!eventList.isEmpty()) {
                    return getEvent(rng);
                } else {
                    logger.info("No events or shrines left");
                    return null;
                }
            } else {
                return getShrine(rng);
            }
        } else {
            AbstractEvent retVal = getEvent(rng);
            return retVal == null ? getShrine(rng) : retVal;
        }
    }

    public static AbstractEvent getShrine(Random rng) {
        ArrayList<String> tmp = new ArrayList<>(shrineList);
        for (String e : specialOneTimeEventList) {
            byte var5 = -1;
            switch (e.hashCode()) {
                case -2022548400:
                    if (e.equals("N'loth")) {
                        var5 = 5;
                    }
                    break;
                case -1699225493:
                    if (e.equals("FaceTrader")) {
                        var5 = 3;
                    }
                    break;
                case -207216254:
                    if (e.equals("The Joust")) {
                        var5 = 6;
                    }
                    break;
                case 591082013:
                    if (e.equals("Duplicator")) {
                        var5 = 2;
                    }
                    break;
                case 1051874140:
                    if (e.equals("SecretPortal")) {
                        var5 = 8;
                    }
                    break;
                case 1088076555:
                    if (e.equals("Designer")) {
                        var5 = 1;
                    }
                    break;
                case 1121505076:
                    if (e.equals("Knowing Skull")) {
                        var5 = 4;
                    }
                    break;
                case 1167999560:
                    if (e.equals("The Woman in Blue")) {
                        var5 = 7;
                    }
                    break;
                case 1917982011:
                    if (e.equals("Fountain of Cleansing")) {
                        var5 = 0;
                    }
            }

            switch (var5) {
                case 0:
                    if (player.isCursed()) {
                        tmp.add(e);
                    }
                    break;
                case 1:
                    if ((id.equals("TheCity") || id.equals("TheBeyond")) && player.gold >= 75) {
                        tmp.add(e);
                    }
                    break;
                case 2:
                    if (id.equals("TheCity") || id.equals("TheBeyond")) {
                        tmp.add(e);
                    }
                    break;
                case 3:
                    if (id.equals("TheCity") || id.equals("Exordium")) {
                        tmp.add(e);
                    }
                    break;
                case 4:
                    if (id.equals("TheCity") && player.currentHealth > 12) {
                        tmp.add(e);
                    }
                    break;
                case 5:
                    if ((id.equals("TheCity") || id.equals("TheCity")) && player.relics.size() >= 2) {
                        tmp.add(e);
                    }
                    break;
                case 6:
                    if (id.equals("TheCity") && player.gold >= 50) {
                        tmp.add(e);
                    }
                    break;
                case 7:
                    if (player.gold >= 50) {
                        tmp.add(e);
                    }
                    break;
                case 8:
                    if (CardCrawlGame.playtime >= 800.0F && id.equals("TheBeyond")) {
                        tmp.add(e);
                    }
                    break;
                default:
                    tmp.add(e);
            }
        }

        String tmpKey = tmp.get(rng.random(tmp.size() - 1));
        tmp.removeIf(e -> (EventUtils.specialEventBonusConditions.containsKey(e) && !EventUtils.specialEventBonusConditions.get(e).test()));
        shrineList.remove(tmpKey);
        specialOneTimeEventList.remove(tmpKey);
        logger.info("Removed event: " + tmpKey + " from pool.");
        return EventHelper.getEvent(tmpKey);

    }

    public static AbstractEvent getEvent(Random rng) {
        ArrayList<String> tmp = new ArrayList<>();
        for (String e : eventList) {
            byte var5 = -1;
            switch (e.hashCode()) {
                case -1914822917:
                    if (e.equals("Mushrooms")) {
                        var5 = 1;
                    }
                    break;
                case -308228690:
                    if (e.equals("Colosseum")) {
                        var5 = 5;
                    }
                    break;
                case 236474855:
                    if (e.equals("The Moai Head")) {
                        var5 = 2;
                    }
                    break;
                case 1824060574:
                    if (e.equals("Dead Adventurer")) {
                        var5 = 0;
                    }
                    break;
                case 1962578239:
                    if (e.equals("The Cleric")) {
                        var5 = 3;
                    }
                    break;
                case 1985970164:
                    if (e.equals("Beggar")) {
                        var5 = 4;
                    }
            }

            switch (var5) {
                case 0:
                    if (floorNum > 6) {
                        tmp.add(e);
                    }
                    break;
                case 1:
                    if (floorNum > 6) {
                        tmp.add(e);
                    }
                    break;
                case 2:
                    if (player.hasRelic("Golden Idol") || (float) player.currentHealth / (float) player.maxHealth <= 0.5F) {
                        tmp.add(e);
                    }
                    break;
                case 3:
                    if (player.gold >= 35) {
                        tmp.add(e);
                    }
                    break;
                case 4:
                    if (player.gold >= 75) {
                        tmp.add(e);
                    }
                    break;
                case 5:
                    if (currMapNode != null && currMapNode.y > map.size() / 2) {
                        tmp.add(e);
                    }
                    break;
                default:
                    tmp.add(e);
            }
        }

        if (tmp.isEmpty()) {
            tmp.removeIf(e -> (EventUtils.normalEventBonusConditions.containsKey(e) && !EventUtils.normalEventBonusConditions.get(e).test()));
            return getShrine(rng);
        }

        String tmpKey = tmp.get(rng.random(tmp.size() - 1));
        eventList.remove(tmpKey);
        player.seenEvents.add(tmpKey);
        logger.info("Removed event: " + tmpKey + " from pool.");
        return EventHelper.getEvent(tmpKey);

    }

    public MonsterGroup getBoss() {
        lastCombatMetricKey = bossKey;
        dungeonMapScreen.map.atBoss = true;
        return MonsterHelper.getEncounter(bossKey);
    }

    public void update() {
        if (!CardCrawlGame.stopClock) {
            CardCrawlGame.playtime += Gdx.graphics.getDeltaTime();
        }

        if (CardCrawlGame.screenTimer > 0.0F) {
            InputHelper.justClickedLeft = false;
            CInputActionSet.select.unpress();
        }

        topPanel.update();
        dynamicBanner.update();
        this.updateFading();
        currMapNode.room.updateObjects();
        if (isScreenUp) {
            topGradientColor.a = MathHelper.fadeLerpSnap(topGradientColor.a, 0.25F);
            botGradientColor.a = MathHelper.fadeLerpSnap(botGradientColor.a, 0.25F);
        } else {
            topGradientColor.a = MathHelper.fadeLerpSnap(topGradientColor.a, 0.1F);
            botGradientColor.a = MathHelper.fadeLerpSnap(botGradientColor.a, 0.1F);
        }

        switch(screen) {
            case NO_INTERACT:
            case NONE:
                dungeonMapScreen.update();
                currMapNode.room.update();
                scene.update();
                currMapNode.room.eventControllerInput();
                break;
            case FTUE:
                ftue.update();
                InputHelper.justClickedRight = false;
                InputHelper.justClickedLeft = false;
                currMapNode.room.update();
                break;
            case MASTER_DECK_VIEW:
                deckViewScreen.update();
                break;
            case GAME_DECK_VIEW:
                gameDeckViewScreen.update();
                break;
            case DISCARD_VIEW:
                discardPileViewScreen.update();
                break;
            case EXHAUST_VIEW:
                exhaustPileViewScreen.update();
                break;
            case SETTINGS:
                settingsScreen.update();
                break;
            case INPUT_SETTINGS:
                inputSettingsScreen.update();
                break;
            case MAP:
                dungeonMapScreen.update();
                break;
            case GRID:
                gridSelectScreen.update();
                if (PeekButton.isPeeking) {
                    currMapNode.room.update();
                }
                break;
            case CARD_REWARD:
                cardRewardScreen.update();
                if (PeekButton.isPeeking) {
                    currMapNode.room.update();
                }
                break;
            case COMBAT_REWARD:
                combatRewardScreen.update();
                break;
            case BOSS_REWARD:
                bossRelicScreen.update();
                currMapNode.room.update();
                break;
            case HAND_SELECT:
                handCardSelectScreen.update();
                currMapNode.room.update();
                break;
            case SHOP:
                shopScreen.update();
                break;
            case DEATH:
                deathScreen.update();
                break;
            case VICTORY:
                victoryScreen.update();
                break;
            case UNLOCK:
                unlockScreen.update();
                break;
            case NEOW_UNLOCK:
                gUnlockScreen.update();
                break;
            case CREDITS:
                creditsScreen.update();
                break;
            case DOOR_UNLOCK:
                CardCrawlGame.mainMenuScreen.doorUnlockScreen.update();
                break;
            default:
                logger.info("ERROR: UNKNOWN SCREEN TO UPDATE: " + screen.name());
        }

        turnPhaseEffectActive = false;
        Iterator i = topLevelEffects.iterator();

        AbstractGameEffect e;
        while(i.hasNext()) {
            e = (AbstractGameEffect)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

        i = effectList.iterator();

        while(i.hasNext()) {
            e = (AbstractGameEffect)i.next();
            e.update();
            if (e instanceof PlayerTurnEffect) {
                turnPhaseEffectActive = true;
            }

            if (e.isDone) {
                i.remove();
            }
        }

        i = effectsQueue.iterator();

        while(i.hasNext()) {
            e = (AbstractGameEffect)i.next();
            effectList.add(e);
            i.remove();
        }

        i = topLevelEffectsQueue.iterator();

        while(i.hasNext()) {
            e = (AbstractGameEffect)i.next();
            topLevelEffects.add(e);
            i.remove();
        }

        overlayMenu.update();
    }

    public void render(SpriteBatch sb) {
        switch(rs) {
            case NORMAL:
                scene.renderCombatRoomBg(sb);
                break;
            case CAMPFIRE:
                scene.renderCampfireRoom(sb);
                this.renderLetterboxGradient(sb);
                break;
            case EVENT:
                scene.renderEventRoom(sb);
        }

        for (AbstractGameEffect e : effectList) {
            if (e.renderBehind) {
                e.render(sb);
            }
        }

        currMapNode.room.render(sb);
        if (rs == AbstractDungeon.RenderScene.NORMAL) {
            scene.renderCombatRoomFg(sb);
        }

        if (rs != AbstractDungeon.RenderScene.CAMPFIRE) {
            this.renderLetterboxGradient(sb);
        }

        AbstractRoom room = getCurrRoom();
        if (room instanceof EventRoom || room instanceof NeowRoom || room instanceof VictoryRoom) {
            room.renderEventTexts(sb);
        }

        for (AbstractGameEffect e : effectList) {
            if (!e.renderBehind) {
                e.render(sb);
            }
        }

        overlayMenu.render(sb);
        overlayMenu.renderBlackScreen(sb);
        switch(screen) {
            case NO_INTERACT:
            case FTUE:
                break;
            case NONE:
                dungeonMapScreen.render(sb);
                break;
            case MASTER_DECK_VIEW:
                deckViewScreen.render(sb);
                break;
            case GAME_DECK_VIEW:
                gameDeckViewScreen.render(sb);
                break;
            case DISCARD_VIEW:
                discardPileViewScreen.render(sb);
                break;
            case EXHAUST_VIEW:
                exhaustPileViewScreen.render(sb);
                break;
            case SETTINGS:
                settingsScreen.render(sb);
                break;
            case INPUT_SETTINGS:
                inputSettingsScreen.render(sb);
                break;
            case MAP:
                dungeonMapScreen.render(sb);
                break;
            case GRID:
                gridSelectScreen.render(sb);
                break;
            case CARD_REWARD:
                cardRewardScreen.render(sb);
                break;
            case COMBAT_REWARD:
                combatRewardScreen.render(sb);
                break;
            case BOSS_REWARD:
                bossRelicScreen.render(sb);
                break;
            case HAND_SELECT:
                handCardSelectScreen.render(sb);
                break;
            case SHOP:
                shopScreen.render(sb);
                break;
            case DEATH:
                deathScreen.render(sb);
                break;
            case VICTORY:
                victoryScreen.render(sb);
                break;
            case UNLOCK:
                unlockScreen.render(sb);
                break;
            case NEOW_UNLOCK:
                gUnlockScreen.render(sb);
                break;
            case CREDITS:
                creditsScreen.render(sb);
                break;
            case DOOR_UNLOCK:
                CardCrawlGame.mainMenuScreen.doorUnlockScreen.render(sb);
                break;
            default:
                logger.info("ERROR: UNKNOWN SCREEN TO RENDER: " + screen.name());
        }

        if (screen != AbstractDungeon.CurrentScreen.UNLOCK) {
            sb.setColor(topGradientColor);
            if (!Settings.hideTopBar) {
                sb.draw(ImageMaster.SCROLL_GRADIENT, 0.0F, (float)Settings.HEIGHT - 128.0F * Settings.scale, (float)Settings.WIDTH, 64.0F * Settings.scale);
            }

            sb.setColor(botGradientColor);
            if (!Settings.hideTopBar) {
                sb.draw(ImageMaster.SCROLL_GRADIENT, 0.0F, 64.0F * Settings.scale, (float)Settings.WIDTH, -64.0F * Settings.scale);
            }
        }

        if (screen == AbstractDungeon.CurrentScreen.FTUE) {
            ftue.render(sb);
        }

        overlayMenu.cancelButton.render(sb);
        dynamicBanner.render(sb);
        if (screen != AbstractDungeon.CurrentScreen.UNLOCK) {
            topPanel.render(sb);
        }

        currMapNode.room.renderAboveTopPanel(sb);

        for (AbstractGameEffect topLevelEffect : topLevelEffects) {
            if (!topLevelEffect.renderBehind) {
                topLevelEffect.render(sb);
            }
        }

        sb.setColor(fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
    }

    private void renderLetterboxGradient(SpriteBatch sb) {
    }

    public void updateFading() {
        if (isFadingIn) {
            fadeTimer -= Gdx.graphics.getDeltaTime();
            fadeColor.a = Interpolation.fade.apply(0.0F, 1.0F, fadeTimer / 0.8F);
            if (fadeTimer < 0.0F) {
                isFadingIn = false;
                fadeColor.a = 0.0F;
                fadeTimer = 0.0F;
            }
        } else if (isFadingOut) {
            fadeTimer -= Gdx.graphics.getDeltaTime();
            fadeColor.a = Interpolation.fade.apply(1.0F, 0.0F, fadeTimer / 0.8F);
            if (fadeTimer < 0.0F) {
                fadeTimer = 0.0F;
                isFadingOut = false;
                fadeColor.a = 1.0F;
                if (!isDungeonBeaten) {
                    this.nextRoomTransition();
                }
            }
        }

    }

    public static void closeCurrentScreen() {
        PeekButton.isPeeking = false;
        if (previousScreen == screen) {
            previousScreen = null;
        }

        Iterator var0;
        AbstractCard c;
        label64:
        switch(screen) {
            case FTUE:
                genericScreenOverlayReset();
                break;
            case MASTER_DECK_VIEW:
                overlayMenu.cancelButton.hide();
                genericScreenOverlayReset();
                var0 = player.masterDeck.group.iterator();

                while(true) {
                    if (!var0.hasNext()) {
                        break label64;
                    }

                    c = (AbstractCard)var0.next();
                    c.unhover();
                    c.untip();
                }
            case GAME_DECK_VIEW:
                overlayMenu.cancelButton.hide();
                genericScreenOverlayReset();
                break;
            case DISCARD_VIEW:
                overlayMenu.cancelButton.hide();
                genericScreenOverlayReset();
                var0 = player.discardPile.group.iterator();

                while(true) {
                    if (!var0.hasNext()) {
                        break label64;
                    }

                    c = (AbstractCard)var0.next();
                    c.drawScale = 0.12F;
                    c.targetDrawScale = 0.12F;
                    c.teleportToDiscardPile();
                    c.darken(true);
                    c.unhover();
                }
            case EXHAUST_VIEW:
                overlayMenu.cancelButton.hide();
                genericScreenOverlayReset();
                break;
            case SETTINGS:
                overlayMenu.cancelButton.hide();
                genericScreenOverlayReset();
                settingsScreen.abandonPopup.hide();
                settingsScreen.exitPopup.hide();
                break;
            case INPUT_SETTINGS:
                overlayMenu.cancelButton.hide();
                genericScreenOverlayReset();
                settingsScreen.abandonPopup.hide();
                settingsScreen.exitPopup.hide();
                break;
            case MAP:
                genericScreenOverlayReset();
                dungeonMapScreen.close();
                if (!firstRoomChosen && nextRoom != null && !dungeonMapScreen.dismissable) {
                    firstRoomChosen = true;
                    firstRoomLogic();
                }
                break;
            case GRID:
                genericScreenOverlayReset();
                if (!combatRewardScreen.rewards.isEmpty()) {
                    previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
                }
                break;
            case CARD_REWARD:
                overlayMenu.cancelButton.hide();
                dynamicBanner.hide();
                genericScreenOverlayReset();
                if (!screenSwap) {
                    cardRewardScreen.onClose();
                }
                break;
            case COMBAT_REWARD:
                dynamicBanner.hide();
                genericScreenOverlayReset();
                break;
            case BOSS_REWARD:
                genericScreenOverlayReset();
                dynamicBanner.hide();
                break;
            case HAND_SELECT:
                genericScreenOverlayReset();
                overlayMenu.showCombatPanels();
                break;
            case SHOP:
                CardCrawlGame.sound.play("SHOP_CLOSE");
                genericScreenOverlayReset();
                overlayMenu.cancelButton.hide();
                break;
            case DEATH:
            case VICTORY:
            case UNLOCK:
            case CREDITS:
            case DOOR_UNLOCK:
            default:
                logger.info("UNSPECIFIED CASE: " + screen.name());
                break;
            case NEOW_UNLOCK:
                genericScreenOverlayReset();
                CardCrawlGame.sound.stop("UNLOCK_SCREEN", gUnlockScreen.id);
                break;
            case TRANSFORM:
                CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
                genericScreenOverlayReset();
                overlayMenu.cancelButton.hide();
        }

        if (previousScreen == null) {
            screen = AbstractDungeon.CurrentScreen.NONE;
        } else if (screenSwap) {
            screenSwap = false;
        } else {
            screen = previousScreen;
            previousScreen = null;
            if (getCurrRoom().rewardTime) {
                previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
            }

            isScreenUp = true;
            openPreviousScreen(screen);
        }

    }

    private static void openPreviousScreen(AbstractDungeon.CurrentScreen s) {
        switch(s) {
            case MASTER_DECK_VIEW:
                deckViewScreen.open();
                break;
            case GAME_DECK_VIEW:
                gameDeckViewScreen.reopen();
                break;
            case DISCARD_VIEW:
                discardPileViewScreen.reopen();
                break;
            case EXHAUST_VIEW:
                exhaustPileViewScreen.reopen();
            case SETTINGS:
            case INPUT_SETTINGS:
            case UNLOCK:
            default:
                break;
            case MAP:
                if (dungeonMapScreen.dismissable) {
                    overlayMenu.cancelButton.show(DungeonMapScreen.TEXT[1]);
                } else {
                    overlayMenu.cancelButton.hide();
                }
                break;
            case GRID:
                overlayMenu.hideBlackScreen();
                if (gridSelectScreen.isJustForConfirming) {
                    dynamicBanner.appear();
                }

                gridSelectScreen.reopen();
                break;
            case CARD_REWARD:
                cardRewardScreen.reopen();
                if (cardRewardScreen.rItem != null) {
                    previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
                }
                break;
            case COMBAT_REWARD:
                combatRewardScreen.reopen();
                break;
            case BOSS_REWARD:
                bossRelicScreen.reopen();
                break;
            case HAND_SELECT:
                overlayMenu.hideBlackScreen();
                handCardSelectScreen.reopen();
                break;
            case SHOP:
                shopScreen.open();
                break;
            case DEATH:
                deathScreen.reopen();
                break;
            case VICTORY:
                victoryScreen.reopen();
                break;
            case NEOW_UNLOCK:
                gUnlockScreen.reOpen();
        }

    }

    private static void genericScreenOverlayReset() {
        if (previousScreen == null) {
            if (player.isDead) {
                previousScreen = AbstractDungeon.CurrentScreen.DEATH;
            } else {
                isScreenUp = false;
                overlayMenu.hideBlackScreen();
            }
        }

        if (getCurrRoom().phase == RoomPhase.COMBAT && !player.isDead) {
            overlayMenu.showCombatPanels();
        }

    }

    public static void fadeIn() {
        if (fadeColor.a != 1.0F) {
            logger.info("WARNING: Attempting to fade in even though screen is not black");
        }

        isFadingIn = true;
        if (Settings.FAST_MODE) {
            fadeTimer = 0.001F;
        } else {
            fadeTimer = 0.8F;
        }

    }

    public static void fadeOut() {
        if (fadeTimer == 0.0F) {
            if (fadeColor.a != 0.0F) {
                logger.info("WARNING: Attempting to fade out even though screen is not transparent");
            }

            isFadingOut = true;
            if (Settings.FAST_MODE) {
                fadeTimer = 0.001F;
            } else {
                fadeTimer = 0.8F;
            }
        }

    }

    public static void dungeonTransitionSetup() {
        ++actNum;
        if (cardRng.counter > 0 && cardRng.counter < 250) {
            cardRng.setCounter(250);
        } else if (cardRng.counter > 250 && cardRng.counter < 500) {
            cardRng.setCounter(500);
        } else if (cardRng.counter > 500 && cardRng.counter < 750) {
            cardRng.setCounter(750);
        }

        logger.info("CardRng Counter: " + cardRng.counter);
        topPanel.unhoverHitboxes();
        pathX.clear();
        pathY.clear();
        EventHelper.resetProbabilities();
        eventList.clear();
        shrineList.clear();
        monsterList.clear();
        eliteMonsterList.clear();
        bossList.clear();
        AbstractRoom.blizzardPotionMod = 0;
        if (ascensionLevel >= 5) {
            player.heal(MathUtils.round((float)(player.maxHealth - player.currentHealth) * 0.75F), false);
        } else {
            player.heal(player.maxHealth, false);
        }

        if (floorNum > 1) {
            topPanel.panelHealEffect();
        }

        if (floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
            if (ascensionLevel >= 14) {
                player.decreaseMaxHealth(player.getAscensionMaxHPLoss());
            }

            if (ascensionLevel >= 6) {
                player.currentHealth = MathUtils.round((float)player.maxHealth * 0.9F);
            }

            if (ascensionLevel >= 10) {
                player.masterDeck.addToTop(new AscendersBane());
                UnlockTracker.markCardAsSeen("AscendersBane");
            }

            CardCrawlGame.playtime = 0.0F;
        }

        dungeonMapScreen.map.atBoss = false;
    }

    public static void reset() {
        logger.info("Resetting variables...");
        CardCrawlGame.resetScoreVars();
        ModHelper.setModsFalse();
        floorNum = 0;
        actNum = 0;
        if (currMapNode != null && getCurrRoom() != null) {
            getCurrRoom().dispose();
            if (getCurrRoom().monsters != null) {
                Iterator var0 = getCurrRoom().monsters.monsters.iterator();

                while(var0.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var0.next();
                    m.dispose();
                }
            }
        }

        currMapNode = null;
        shrineList.clear();
        relicsToRemoveOnStart.clear();
        previousScreen = null;
        actionManager.clear();
        actionManager.clearNextRoomCombatActions();
        combatRewardScreen.clear();
        cardRewardScreen.reset();
        if (dungeonMapScreen != null) {
            dungeonMapScreen.closeInstantly();
        }

        effectList.clear();
        effectsQueue.clear();
        topLevelEffectsQueue.clear();
        topLevelEffects.clear();
        cardBlizzRandomizer = cardBlizzStartOffset;
        if (player != null) {
            player.relics.clear();
        }

        rs = AbstractDungeon.RenderScene.NORMAL;
        blightPool.clear();
    }

    protected void removeRelicFromPool(ArrayList<String> pool, String name) {
        Iterator i = pool.iterator();

        while(i.hasNext()) {
            String s = (String)i.next();
            if (s.equals(name)) {
                i.remove();
                logger.info("Relic" + s + " removed from relic pool.");
            }
        }

    }

    public static void onModifyPower() {
        Iterator var0;
        if (player != null) {
            player.hand.applyPowers();
            if (player.hasPower("Focus")) {
                var0 = player.orbs.iterator();

                while(var0.hasNext()) {
                    AbstractOrb o = (AbstractOrb)var0.next();
                    o.updateDescription();
                }
            }
        }

        if (getCurrRoom().monsters != null) {
            var0 = getCurrRoom().monsters.monsters.iterator();

            while(var0.hasNext()) {
                AbstractMonster m = (AbstractMonster)var0.next();
                m.applyPowers();
            }
        }

    }

    public void checkForPactAchievement() {
        if (player != null && player.exhaustPile.size() >= 20) {
            UnlockTracker.unlockAchievement("THE_PACT");
        }

    }

    public void loadSave(SaveFile saveFile) {
        floorNum = saveFile.floor_num;
        actNum = saveFile.act_num;
        Settings.seed = saveFile.seed;
        loadSeeds(saveFile);
        monsterList = saveFile.monster_list;
        eliteMonsterList = saveFile.elite_monster_list;
        bossList = saveFile.boss_list;
        this.setBoss(saveFile.boss);
        commonRelicPool = saveFile.common_relics;
        uncommonRelicPool = saveFile.uncommon_relics;
        rareRelicPool = saveFile.rare_relics;
        shopRelicPool = saveFile.shop_relics;
        bossRelicPool = saveFile.boss_relics;
        pathX = saveFile.path_x;
        pathY = saveFile.path_y;
        bossCount = saveFile.spirit_count;
        eventList = saveFile.event_list;
        specialOneTimeEventList = saveFile.one_time_event_list;
        EventHelper.setChances(saveFile.event_chances);
        AbstractRoom.blizzardPotionMod = saveFile.potion_chance;
        ShopScreen.purgeCost = saveFile.purgeCost;
        CardHelper.obtainedCards = saveFile.obtained_cards;
        if (saveFile.daily_mods != null) {
            ModHelper.setMods(saveFile.daily_mods);
        }

    }

    public static AbstractBlight getBlight(String targetID) {
        Iterator var1 = blightPool.iterator();

        AbstractBlight b;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            b = (AbstractBlight)var1.next();
        } while(!b.blightID.equals(targetID));

        return b;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("AbstractDungeon");
        TEXT = uiStrings.TEXT;
        floorNum = 0;
        actNum = 0;
        unlocks = new ArrayList<>();
        shrineChance = 0.25F;
        loading_post_combat = false;
        is_victory = false;
        srcColorlessCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcCurseCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcCommonCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcUncommonCardPool = new CardGroup(CardGroupType.CARD_POOL);
        srcRareCardPool = new CardGroup(CardGroupType.CARD_POOL);
        colorlessCardPool = new CardGroup(CardGroupType.CARD_POOL);
        curseCardPool = new CardGroup(CardGroupType.CARD_POOL);
        commonCardPool = new CardGroup(CardGroupType.CARD_POOL);
        uncommonCardPool = new CardGroup(CardGroupType.CARD_POOL);
        rareCardPool = new CardGroup(CardGroupType.CARD_POOL);
        commonRelicPool = new ArrayList<>();
        uncommonRelicPool = new ArrayList<>();
        rareRelicPool = new ArrayList<>();
        shopRelicPool = new ArrayList<>();
        bossRelicPool = new ArrayList<>();
        lastCombatMetricKey = null;
        monsterList = new ArrayList<>();
        eliteMonsterList = new ArrayList<>();
        bossList = new ArrayList<>();
        eventList = new ArrayList<>();
        shrineList = new ArrayList<>();
        specialOneTimeEventList = new ArrayList<>();
        actionManager = new GameActionManager();
        topLevelEffects = new ArrayList<>();
        topLevelEffectsQueue = new ArrayList<>();
        effectList = new ArrayList<>();
        effectsQueue = new ArrayList<>();
        turnPhaseEffectActive = false;
        firstRoomChosen = false;
        rs = AbstractDungeon.RenderScene.NORMAL;
        pathX = new ArrayList<>();
        pathY = new ArrayList<>();
        topGradientColor = new Color(1.0F, 1.0F, 1.0F, 0.25F);
        botGradientColor = new Color(1.0F, 1.0F, 1.0F, 0.25F);
        floorY = 340.0F * Settings.yScale;
        topPanel = new TopPanel();
        cardRewardScreen = new CardRewardScreen();
        combatRewardScreen = new CombatRewardScreen();
        bossRelicScreen = new BossRelicSelectScreen();
        deckViewScreen = new MasterDeckViewScreen();
        discardPileViewScreen = new DiscardPileViewScreen();
        gameDeckViewScreen = new DrawPileViewScreen();
        exhaustPileViewScreen = new ExhaustPileViewScreen();
        settingsScreen = new SettingsScreen();
        inputSettingsScreen = new InputSettingsScreen();
        dungeonMapScreen = new DungeonMapScreen();
        gridSelectScreen = new GridCardSelectScreen();
        handCardSelectScreen = new HandCardSelectScreen();
        shopScreen = new ShopScreen();
        creditsScreen = null;
        ftue = null;
        unlockScreen = new UnlockCharacterScreen();
        gUnlockScreen = new NeowUnlockScreen();
        isScreenUp = false;
        screenSwap = false;
        cardBlizzStartOffset = 5;
        cardBlizzRandomizer = cardBlizzStartOffset;
        cardBlizzGrowth = 1;
        cardBlizzMaxOffset = -40;
        sceneOffsetY = 0.0F;
        relicsToRemoveOnStart = new ArrayList<>();
        bossCount = 0;
        isAscensionMode = false;
        ascensionLevel = 0;
        blightPool = new ArrayList<>();
        LOGGER = SpireAndroidLogger.getLogger(AbstractDungeon.class);
    }

    public enum RenderScene {
        NORMAL,
        EVENT,
        CAMPFIRE;

        RenderScene() {
        }
    }

    public enum CurrentScreen {
        NONE,
        MASTER_DECK_VIEW,
        SETTINGS,
        INPUT_SETTINGS,
        GRID,
        MAP,
        FTUE,
        CHOOSE_ONE,
        HAND_SELECT,
        SHOP,
        COMBAT_REWARD,
        DISCARD_VIEW,
        EXHAUST_VIEW,
        GAME_DECK_VIEW,
        BOSS_REWARD,
        DEATH,
        CARD_REWARD,
        TRANSFORM,
        VICTORY,
        UNLOCK,
        DOOR_UNLOCK,
        CREDITS,
        NO_INTERACT,
        NEOW_UNLOCK;

        CurrentScreen() {
        }
    }
}

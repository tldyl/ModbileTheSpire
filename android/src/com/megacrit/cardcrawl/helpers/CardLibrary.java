package com.megacrit.cardcrawl.helpers;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.blue.Stack;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.optionCards.BecomeAlmighty;
import com.megacrit.cardcrawl.cards.optionCards.FameAndFortune;
import com.megacrit.cardcrawl.cards.optionCards.LiveForever;
import com.megacrit.cardcrawl.cards.purple.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.cards.tempCards.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.BotDataUploader;
import com.megacrit.cardcrawl.metrics.BotDataUploader.GameDataType;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.*;
import java.util.Map.Entry;

public class CardLibrary {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(CardLibrary.class);
    public static int totalCardCount = 0;
    public static HashMap<String, AbstractCard> cards = new HashMap<>();
    private static HashMap<String, AbstractCard> curses = new HashMap<>();
    public static int redCards = 0;
    public static int greenCards = 0;
    public static int blueCards = 0;
    public static int purpleCards = 0;
    public static int colorlessCards = 0;
    public static int curseCards = 0;
    public static int seenRedCards = 0;
    public static int seenGreenCards = 0;
    public static int seenBlueCards = 0;
    public static int seenPurpleCards = 0;
    public static int seenColorlessCards = 0;
    public static int seenCurseCards = 0;

    public CardLibrary() {
    }

    public static void initialize() {
        long startTime = System.currentTimeMillis();
        addRedCards();
        addGreenCards();
        addBlueCards();
        addPurpleCards();
        addColorlessCards();
        addCurseCards();
        BaseMod.receiveEditCards();

        logger.info("Card load time: " + (System.currentTimeMillis() - startTime) + "ms with " + cards.size() + " cards");
        if (Settings.isDev) {
            logger.info("[INFO] Red Cards: \t" + redCards);
            logger.info("[INFO] Green Cards: \t" + greenCards);
            logger.info("[INFO] Blue Cards: \t" + blueCards);
            logger.info("[INFO] Purple Cards: \t" + purpleCards);
            logger.info("[INFO] Colorless Cards: \t" + colorlessCards);
            logger.info("[INFO] Curse Cards: \t" + curseCards);
            logger.info("[INFO] Total Cards: \t" + (redCards + greenCards + blueCards + purpleCards + colorlessCards + curseCards));
        }

    }

    public static void resetForReload() {
        cards = new HashMap<>();
        curses = new HashMap<>();
        totalCardCount = 0;
        redCards = 0;
        greenCards = 0;
        blueCards = 0;
        purpleCards = 0;
        colorlessCards = 0;
        curseCards = 0;
        seenRedCards = 0;
        seenGreenCards = 0;
        seenBlueCards = 0;
        seenPurpleCards = 0;
        seenColorlessCards = 0;
        seenCurseCards = 0;
    }

    private static void addRedCards() {
        add(new Anger());
        add(new Armaments());
        add(new Barricade());
        add(new Bash());
        add(new BattleTrance());
        add(new Berserk());
        add(new BloodForBlood());
        add(new Bloodletting());
        add(new Bludgeon());
        add(new BodySlam());
        add(new Brutality());
        add(new BurningPact());
        add(new Carnage());
        add(new Clash());
        add(new Cleave());
        add(new Clothesline());
        add(new Combust());
        add(new Corruption());
        add(new DarkEmbrace());
        add(new Defend_Red());
        add(new DemonForm());
        add(new Disarm());
        add(new DoubleTap());
        add(new Dropkick());
        add(new DualWield());
        add(new Entrench());
        add(new Evolve());
        add(new Exhume());
        add(new Feed());
        add(new FeelNoPain());
        add(new FiendFire());
        add(new FireBreathing());
        add(new FlameBarrier());
        add(new Flex());
        add(new GhostlyArmor());
        add(new Havoc());
        add(new Headbutt());
        add(new HeavyBlade());
        add(new Hemokinesis());
        add(new Immolate());
        add(new Impervious());
        add(new InfernalBlade());
        add(new Inflame());
        add(new Intimidate());
        add(new IronWave());
        add(new Juggernaut());
        add(new LimitBreak());
        add(new Metallicize());
        add(new Offering());
        add(new PerfectedStrike());
        add(new PommelStrike());
        add(new PowerThrough());
        add(new Pummel());
        add(new Rage());
        add(new Rampage());
        add(new Reaper());
        add(new RecklessCharge());
        add(new Rupture());
        add(new SearingBlow());
        add(new SecondWind());
        add(new SeeingRed());
        add(new Sentinel());
        add(new SeverSoul());
        add(new Shockwave());
        add(new ShrugItOff());
        add(new SpotWeakness());
        add(new Strike_Red());
        add(new SwordBoomerang());
        add(new ThunderClap());
        add(new TrueGrit());
        add(new TwinStrike());
        add(new Uppercut());
        add(new Warcry());
        add(new Whirlwind());
        add(new WildStrike());
    }

    private static void addGreenCards() {
        add(new Accuracy());
        add(new Acrobatics());
        add(new Adrenaline());
        add(new AfterImage());
        add(new Alchemize());
        add(new AllOutAttack());
        add(new AThousandCuts());
        add(new Backflip());
        add(new Backstab());
        add(new Bane());
        add(new BladeDance());
        add(new Blur());
        add(new BouncingFlask());
        add(new BulletTime());
        add(new Burst());
        add(new CalculatedGamble());
        add(new Caltrops());
        add(new Catalyst());
        add(new Choke());
        add(new CloakAndDagger());
        add(new Concentrate());
        add(new CorpseExplosion());
        add(new CripplingPoison());
        add(new DaggerSpray());
        add(new DaggerThrow());
        add(new Dash());
        add(new DeadlyPoison());
        add(new Defend_Green());
        add(new Deflect());
        add(new DieDieDie());
        add(new Distraction());
        add(new DodgeAndRoll());
        add(new Doppelganger());
        add(new EndlessAgony());
        add(new Envenom());
        add(new EscapePlan());
        add(new Eviscerate());
        add(new Expertise());
        add(new Finisher());
        add(new Flechettes());
        add(new FlyingKnee());
        add(new Footwork());
        add(new GlassKnife());
        add(new GrandFinale());
        add(new HeelHook());
        add(new InfiniteBlades());
        add(new LegSweep());
        add(new Malaise());
        add(new MasterfulStab());
        add(new Neutralize());
        add(new Nightmare());
        add(new NoxiousFumes());
        add(new Outmaneuver());
        add(new PhantasmalKiller());
        add(new PiercingWail());
        add(new PoisonedStab());
        add(new Predator());
        add(new Prepared());
        add(new QuickSlash());
        add(new Reflex());
        add(new RiddleWithHoles());
        add(new Setup());
        add(new Skewer());
        add(new Slice());
        add(new StormOfSteel());
        add(new Strike_Green());
        add(new SuckerPunch());
        add(new Survivor());
        add(new Tactician());
        add(new Terror());
        add(new ToolsOfTheTrade());
        add(new SneakyStrike());
        add(new Unload());
        add(new WellLaidPlans());
        add(new WraithForm());
    }

    private static void addBlueCards() {
        add(new Aggregate());
        add(new AllForOne());
        add(new Amplify());
        add(new AutoShields());
        add(new BallLightning());
        add(new Barrage());
        add(new BeamCell());
        add(new BiasedCognition());
        add(new Blizzard());
        add(new BootSequence());
        add(new Buffer());
        add(new Capacitor());
        add(new Chaos());
        add(new Chill());
        add(new Claw());
        add(new ColdSnap());
        add(new CompileDriver());
        add(new ConserveBattery());
        add(new Consume());
        add(new Coolheaded());
        add(new CoreSurge());
        add(new CreativeAI());
        add(new Darkness());
        add(new Defend_Blue());
        add(new Defragment());
        add(new DoomAndGloom());
        add(new DoubleEnergy());
        add(new Dualcast());
        add(new EchoForm());
        add(new Electrodynamics());
        add(new Fission());
        add(new ForceField());
        add(new FTL());
        add(new Fusion());
        add(new GeneticAlgorithm());
        add(new Glacier());
        add(new GoForTheEyes());
        add(new Heatsinks());
        add(new HelloWorld());
        add(new Hologram());
        add(new Hyperbeam());
        add(new Leap());
        add(new LockOn());
        add(new Loop());
        add(new MachineLearning());
        add(new Melter());
        add(new MeteorStrike());
        add(new MultiCast());
        add(new Overclock());
        add(new Rainbow());
        add(new Reboot());
        add(new Rebound());
        add(new Recursion());
        add(new Recycle());
        add(new ReinforcedBody());
        add(new Reprogram());
        add(new RipAndTear());
        add(new Scrape());
        add(new Seek());
        add(new SelfRepair());
        add(new Skim());
        add(new Stack());
        add(new StaticDischarge());
        add(new SteamBarrier());
        add(new Storm());
        add(new Streamline());
        add(new Strike_Blue());
        add(new Sunder());
        add(new SweepingBeam());
        add(new Tempest());
        add(new ThunderStrike());
        add(new Turbo());
        add(new Equilibrium());
        add(new WhiteNoise());
        add(new Zap());
    }

    private static void addPurpleCards() {
        add(new Alpha());
        add(new BattleHymn());
        add(new Blasphemy());
        add(new BowlingBash());
        add(new Brilliance());
        add(new CarveReality());
        add(new Collect());
        add(new Conclude());
        add(new ConjureBlade());
        add(new Consecrate());
        add(new Crescendo());
        add(new CrushJoints());
        add(new CutThroughFate());
        add(new DeceiveReality());
        add(new Defend_Watcher());
        add(new DeusExMachina());
        add(new DevaForm());
        add(new Devotion());
        add(new EmptyBody());
        add(new EmptyFist());
        add(new EmptyMind());
        add(new Eruption());
        add(new Establishment());
        add(new Evaluate());
        add(new Fasting());
        add(new FearNoEvil());
        add(new FlurryOfBlows());
        add(new FlyingSleeves());
        add(new FollowUp());
        add(new ForeignInfluence());
        add(new Foresight());
        add(new Halt());
        add(new Indignation());
        add(new InnerPeace());
        add(new Judgement());
        add(new JustLucky());
        add(new LessonLearned());
        add(new LikeWater());
        add(new MasterReality());
        add(new Meditate());
        add(new MentalFortress());
        add(new Nirvana());
        add(new Omniscience());
        add(new Perseverance());
        add(new Pray());
        add(new PressurePoints());
        add(new Prostrate());
        add(new Protect());
        add(new Ragnarok());
        add(new ReachHeaven());
        add(new Rushdown());
        add(new Sanctity());
        add(new SandsOfTime());
        add(new SashWhip());
        add(new Scrawl());
        add(new SignatureMove());
        add(new SimmeringFury());
        add(new SpiritShield());
        add(new Strike_Purple());
        add(new Study());
        add(new Swivel());
        add(new TalkToTheHand());
        add(new Tantrum());
        add(new ThirdEye());
        add(new Tranquility());
        add(new Vault());
        add(new Vigilance());
        add(new Wallop());
        add(new WaveOfTheHand());
        add(new Weave());
        add(new WheelKick());
        add(new WindmillStrike());
        add(new Wish());
        add(new Worship());
        add(new WreathOfFlame());
    }

    private static void printMissingPortraitInfo() {
        Iterator var0 = cards.entrySet().iterator();

        Entry c;
        AbstractCard card;
        while(var0.hasNext()) {
            c = (Entry)var0.next();
            card = (AbstractCard)c.getValue();
            if (card.jokePortrait == null) {
                System.out.println(card.name + ";" + card.color.name() + ";" + card.type.name());
            }
        }

        var0 = cards.entrySet().iterator();

        while(var0.hasNext()) {
            c = (Entry)var0.next();
            card = (AbstractCard)c.getValue();
            if (ImageMaster.loadImage("images/1024PortraitsBeta/" + card.assetUrl + ".png") == null) {
                System.out.println("[INFO] " + card.name + " missing LARGE beta portrait.");
            }
        }

    }

    private static void printBlueCards(CardColor color) {
        for (Entry<String, AbstractCard> o : cards.entrySet()) {
            if ((o.getValue()).color == color) {
                AbstractCard card = o.getValue();
                System.out.println(card.originalName + "; " + card.type.toString() + "; " + card.rarity.toString() + "; " + card.cost + "; " + card.rawDescription);
            }
        }
    }

    private static void addColorlessCards() {
        add(new Apotheosis());
        add(new BandageUp());
        add(new Blind());
        add(new Chrysalis());
        add(new DarkShackles());
        add(new DeepBreath());
        add(new Discovery());
        add(new DramaticEntrance());
        add(new Enlightenment());
        add(new Finesse());
        add(new FlashOfSteel());
        add(new Forethought());
        add(new GoodInstincts());
        add(new HandOfGreed());
        add(new Impatience());
        add(new JackOfAllTrades());
        add(new Madness());
        add(new Magnetism());
        add(new MasterOfStrategy());
        add(new Mayhem());
        add(new Metamorphosis());
        add(new MindBlast());
        add(new Panacea());
        add(new Panache());
        add(new PanicButton());
        add(new Purity());
        add(new SadisticNature());
        add(new SecretTechnique());
        add(new SecretWeapon());
        add(new SwiftStrike());
        add(new TheBomb());
        add(new ThinkingAhead());
        add(new Transmutation());
        add(new Trip());
        add(new Violence());
        add(new Burn());
        add(new Dazed());
        add(new Slimed());
        add(new VoidCard());
        add(new Wound());
        add(new Apparition());
        add(new Beta());
        add(new Bite());
        add(new JAX());
        add(new Insight());
        add(new Miracle());
        add(new Omega());
        add(new RitualDagger());
        add(new Safety());
        add(new Shiv());
        add(new Smite());
        add(new ThroughViolence());
        add(new BecomeAlmighty());
        add(new FameAndFortune());
        add(new LiveForever());
        add(new Expunger());
    }

    private static void addCurseCards() {
        add(new AscendersBane());
        add(new CurseOfTheBell());
        add(new Clumsy());
        add(new Decay());
        add(new Doubt());
        add(new Injury());
        add(new Necronomicurse());
        add(new Normality());
        add(new Pain());
        add(new Parasite());
        add(new Pride());
        add(new Regret());
        add(new Shame());
        add(new Writhe());
    }

    private static void removeNonFinalizedCards() {
        ArrayList<String> toRemove = new ArrayList<>();
        Iterator<Map.Entry<String, AbstractCard>> var1 = cards.entrySet().iterator();

        Entry<String, AbstractCard> c;
        while(var1.hasNext()) {
            c = var1.next();
            if ((c.getValue()).assetUrl == null) {
                toRemove.add(c.getKey());
            }
        }

        for (String s : toRemove) {
            logger.info("Removing Card " + s + " for trailer build.");
            cards.remove(s);
        }

        toRemove.clear();
        var1 = curses.entrySet().iterator();

        while(var1.hasNext()) {
            c = var1.next();
            if ((c.getValue()).assetUrl == null) {
                toRemove.add(c.getKey());
            }
        }

        for (String s : toRemove) {
            logger.info("Removing Curse " + s + " for trailer build.");
            curses.remove(s);
        }
    }

    public static void unlockAndSeeAllCards() {
        Iterator var0 = UnlockTracker.lockedCards.iterator();

        while(var0.hasNext()) {
            String s = (String)var0.next();
            UnlockTracker.hardUnlockOverride(s);
        }

        var0 = cards.entrySet().iterator();

        Entry c;
        while(var0.hasNext()) {
            c = (Entry)var0.next();
            if (((AbstractCard)c.getValue()).rarity != CardRarity.BASIC && !UnlockTracker.isCardSeen((String)c.getKey())) {
                UnlockTracker.markCardAsSeen((String)c.getKey());
            }
        }

        var0 = curses.entrySet().iterator();

        while(var0.hasNext()) {
            c = (Entry)var0.next();
            if (!UnlockTracker.isCardSeen((String)c.getKey())) {
                UnlockTracker.markCardAsSeen((String)c.getKey());
            }
        }

    }

    public static void add(AbstractCard card) {
        switch(card.color.name()) {
            case "RED":
                ++redCards;
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    ++seenRedCards;
                }
                break;
            case "GREEN":
                ++greenCards;
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    ++seenGreenCards;
                }
                break;
            case "PURPLE":
                ++purpleCards;
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    ++seenPurpleCards;
                }
                break;
            case "BLUE":
                ++blueCards;
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    ++seenBlueCards;
                }
                break;
            case "COLORLESS":
                ++colorlessCards;
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    ++seenColorlessCards;
                }
                break;
            case "CURSE":
                ++curseCards;
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    ++seenCurseCards;
                }
                curses.put(card.cardID, card);
                break;
            default:
                BaseMod.incrementCardCount(card.color);
                if (UnlockTracker.isCardSeen(card.cardID)) {
                    BaseMod.incrementSeenCardCount(card.color);
                }
        }

        if (!UnlockTracker.isCardSeen(card.cardID)) {
            card.isSeen = false;
        }

        cards.put(card.cardID, card);
        ++totalCardCount;
    }

    public static AbstractCard getCopy(String key, int upgradeTime, int misc) {
        AbstractCard source = getCard(key);
        AbstractCard retVal;
        if (source == null) {
            retVal = getCard("Madness").makeCopy();
        } else {
            retVal = getCard(key).makeCopy();
        }

        for(int i = 0; i < upgradeTime; ++i) {
            retVal.upgrade();
        }

        retVal.misc = misc;
        if (misc != 0) {
            if (retVal.cardID.equals("Genetic Algorithm")) {
                retVal.block = misc;
                retVal.baseBlock = misc;
                retVal.initializeDescription();
            }

            if (retVal.cardID.equals("RitualDagger")) {
                retVal.damage = misc;
                retVal.baseDamage = misc;
                retVal.initializeDescription();
            }
        }

        return retVal;
    }

    public static AbstractCard getCopy(String key) {
        return getCard(key).makeCopy();
    }

    public static AbstractCard getCard(PlayerClass plyrClass, String key) {
        return cards.get(key);
    }

    public static AbstractCard getCard(String key) {
        return cards.get(key);
    }

    public static String getCardNameFromMetricID(String metricID) {
        String[] components = metricID.split("\\+");
        String baseId = components[0];
        AbstractCard card = cards.getOrDefault(baseId, null);
        if (card == null) {
            return metricID;
        } else {
            try {
                if (components.length > 1) {
                    card = card.makeCopy();
                    int upgrades = Integer.parseInt(components[1]);

                    for(int i = 0; i < upgrades; ++i) {
                        card.upgrade();
                    }
                }
            } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                ;
            }

            return card.name;
        }
    }

    public static boolean isACard(String metricID) {
        String[] components = metricID.split("\\+");
        String baseId = components[0];
        AbstractCard card = cards.getOrDefault(baseId, null);
        return card != null;
    }

    public static AbstractCard getCurse() {
        ArrayList<String> tmp = new ArrayList<>();

        for (Entry<String, AbstractCard> o : curses.entrySet()) {
            if (!o.getValue().cardID.equals("AscendersBane") && !o.getValue().cardID.equals("Necronomicurse") && !o.getValue().cardID.equals("CurseOfTheBell") && !o.getValue().cardID.equals("Pride")) {
                tmp.add(o.getKey());
            }
        }

        return cards.get(tmp.get(AbstractDungeon.cardRng.random(0, tmp.size() - 1)));
    }

    public static AbstractCard getCurse(AbstractCard prohibitedCard, Random rng) {
        ArrayList<String> tmp = new ArrayList<>();

        for (Entry<String, AbstractCard> o : curses.entrySet()) {
            if (!Objects.equals(o.getValue().cardID, prohibitedCard.cardID) && !Objects.equals(o.getValue().cardID, "Necronomicurse") && !Objects.equals(o.getValue().cardID, "AscendersBane") && !Objects.equals(o.getValue().cardID, "CurseOfTheBell") && !Objects.equals(o.getValue().cardID, "Pride")) {
                tmp.add(o.getKey());
            }
        }

        return cards.get(tmp.get(rng.random(0, tmp.size() - 1)));
    }

    public static AbstractCard getCurse(AbstractCard prohibitedCard) {
        return getCurse(prohibitedCard, new Random());
    }

    public static void uploadCardData() {
        ArrayList<String> data = new ArrayList<>();

        for (Entry<String, AbstractCard> o : cards.entrySet()) {
            data.add(o.getValue().gameDataUploadData());
            AbstractCard c2 = (o.getValue()).makeCopy();
            if (c2.canUpgrade()) {
                c2.upgrade();
                data.add(c2.gameDataUploadData());
            }
        }

        BotDataUploader.uploadDataAsync(GameDataType.CARD_DATA, AbstractCard.gameDataUploadHeader(), data);
    }

    public static ArrayList<AbstractCard> getAllCards() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();

        for (Entry<String, AbstractCard> o : cards.entrySet()) {
            retVal.add(o.getValue());
        }

        return retVal;
    }

    public static AbstractCard getAnyColorCard(CardType type, CardRarity rarity) {
        CardGroup anyCard = new CardGroup(CardGroupType.UNSPECIFIED);
        Iterator var3 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!var3.hasNext()) {
                                        anyCard.shuffle(AbstractDungeon.cardRandomRng);
                                        return anyCard.getRandomCard(true, rarity);
                                    }

                                    c = (Entry)var3.next();
                                } while(((AbstractCard)c.getValue()).rarity != rarity);
                            } while(((AbstractCard)c.getValue()).hasTag(CardTags.HEALING));
                        } while(((AbstractCard)c.getValue()).type == CardType.CURSE);
                    } while(((AbstractCard)c.getValue()).type == CardType.STATUS);
                } while(((AbstractCard)c.getValue()).type != type);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            anyCard.addToBottom((AbstractCard)c.getValue());
        }
    }

    public static AbstractCard getAnyColorCard(CardRarity rarity) {
        CardGroup anyCard = new CardGroup(CardGroupType.UNSPECIFIED);
        Iterator var2 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        do {
                            if (!var2.hasNext()) {
                                anyCard.shuffle(AbstractDungeon.cardRng);
                                return anyCard.getRandomCard(true, rarity).makeCopy();
                            }

                            c = (Entry)var2.next();
                        } while(((AbstractCard)c.getValue()).rarity != rarity);
                    } while(((AbstractCard)c.getValue()).type == CardType.CURSE);
                } while(((AbstractCard)c.getValue()).type == CardType.STATUS);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            anyCard.addToBottom((AbstractCard)c.getValue());
        }
    }

    public static CardGroup getEachRare(AbstractPlayer p) {
        CardGroup everyRareCard = new CardGroup(CardGroupType.UNSPECIFIED);

        for (Entry<String, AbstractCard> o : cards.entrySet()) {
            if (o.getValue().color == p.getCardColor() && o.getValue().rarity == CardRarity.RARE) {
                everyRareCard.addToBottom(o.getValue().makeCopy());
            }
        }

        return everyRareCard;
    }

    public static ArrayList<AbstractCard> getCardList(CardLibrary.LibraryType type) {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        Iterator<Entry<String, AbstractCard>> var2;
        Entry<String, AbstractCard> c;
        switch(type.name()) {
            case "COLORLESS":
                var2 = cards.entrySet().iterator();

                while(var2.hasNext()) {
                    c = var2.next();
                    if ((c.getValue()).color == CardColor.COLORLESS) {
                        retVal.add(c.getValue());
                    }
                }

                return retVal;
            case "CURSE":
                var2 = cards.entrySet().iterator();

                while(var2.hasNext()) {
                    c = var2.next();
                    if ((c.getValue()).color == CardColor.CURSE) {
                        retVal.add(c.getValue());
                    }
                }

                return retVal;
            case "RED":
                var2 = cards.entrySet().iterator();

                while(var2.hasNext()) {
                    c = var2.next();
                    if ((c.getValue()).color == CardColor.RED) {
                        retVal.add(c.getValue());
                    }
                }

                return retVal;
            case "GREEN":
                var2 = cards.entrySet().iterator();

                while(var2.hasNext()) {
                    c = var2.next();
                    if ((c.getValue()).color == CardColor.GREEN) {
                        retVal.add(c.getValue());
                    }
                }

                return retVal;
            case "BLUE":
                var2 = cards.entrySet().iterator();

                while(var2.hasNext()) {
                    c = var2.next();
                    if ((c.getValue()).color == CardColor.BLUE) {
                        retVal.add(c.getValue());
                    }
                }

                return retVal;
            case "PURPLE":
                var2 = cards.entrySet().iterator();

                while(var2.hasNext()) {
                    c = var2.next();
                    if ((c.getValue()).color == CardColor.PURPLE) {
                        retVal.add(c.getValue());
                    }
                }
                return retVal;
        }

        for (Entry<String, AbstractCard> e : cards.entrySet()) {
            CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(e.getValue().color, null);
            if (bundle != null) {
                if (bundle.libraryType == type) {
                    retVal.add(e.getValue());
                }
            }
        }
        return retVal;
    }

    public static void addCardsIntoPool(ArrayList<AbstractCard> tmpPool, CardColor color) {
        logger.info("[INFO] Adding " + color + " cards into card pool.");
        AbstractCard card;
        Iterator<Entry<String, AbstractCard>> cardsEntries = cards.entrySet().iterator();

        while(true) {
            Entry<String, AbstractCard> cardEntry;
            do {
                do {
                    do {
                        do {
                            if (!cardsEntries.hasNext()) {
                                return;
                            }

                            cardEntry = cardsEntries.next();
                            card = cardEntry.getValue();
                        } while(card.color != color);
                    } while(card.rarity == CardRarity.BASIC);
                } while(card.type == CardType.STATUS);
            } while(UnlockTracker.isCardLocked(cardEntry.getKey()) && !Settings.treatEverythingAsUnlocked());

            tmpPool.add(card);
        }
    }

    public static void addRedCards(ArrayList<AbstractCard> tmpPool) {
        logger.info("[INFO] Adding red cards into card pool.");
        AbstractCard card;
        Iterator var2 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        c = (Entry)var2.next();
                        card = (AbstractCard)c.getValue();
                    } while(card.color != CardColor.RED);
                } while(card.rarity == CardRarity.BASIC);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            tmpPool.add(card);
        }
    }

    public static void addGreenCards(ArrayList<AbstractCard> tmpPool) {
        logger.info("[INFO] Adding green cards into card pool.");
        AbstractCard card;
        Iterator var2 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        c = (Entry)var2.next();
                        card = (AbstractCard)c.getValue();
                    } while(card.color != CardColor.GREEN);
                } while(card.rarity == CardRarity.BASIC);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            tmpPool.add(card);
        }
    }

    public static void addBlueCards(ArrayList<AbstractCard> tmpPool) {
        logger.info("[INFO] Adding blue cards into card pool.");
        AbstractCard card;
        Iterator var2 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        c = (Entry)var2.next();
                        card = (AbstractCard)c.getValue();
                    } while(card.color != CardColor.BLUE);
                } while(card.rarity == CardRarity.BASIC);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            tmpPool.add(card);
        }
    }

    public static void addPurpleCards(ArrayList<AbstractCard> tmpPool) {
        logger.info("[INFO] Adding purple cards into card pool.");
        AbstractCard card;
        Iterator var2 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        c = (Entry)var2.next();
                        card = (AbstractCard)c.getValue();
                    } while(card.color != CardColor.PURPLE);
                } while(card.rarity == CardRarity.BASIC);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            tmpPool.add(card);
        }
    }

    public static void addColorlessCards(ArrayList<AbstractCard> tmpPool) {
        logger.info("[INFO] Adding colorless cards into card pool.");
        AbstractCard card;
        Iterator var2 = cards.entrySet().iterator();

        while(true) {
            Entry c;
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        c = (Entry)var2.next();
                        card = (AbstractCard)c.getValue();
                    } while(card.color != CardColor.COLORLESS);
                } while(card.type == CardType.STATUS);
            } while(UnlockTracker.isCardLocked((String)c.getKey()) && !Settings.treatEverythingAsUnlocked());

            tmpPool.add(card);
        }
    }

    public static class LibraryType {
        public static final LibraryType RED = new LibraryType("RED");
        public static final LibraryType GREEN = new LibraryType("GREEN");
        public static final LibraryType BLUE = new LibraryType("BLUE");
        public static final LibraryType PURPLE = new LibraryType("PURPLE");
        public static final LibraryType CURSE = new LibraryType("CURSE");
        public static final LibraryType COLORLESS = new LibraryType("COLORLESS");

        private String name;

        private LibraryType(String name) {
            this.name = name;
        }

        public String name() {
            return this.name;
        }

        public static LibraryType add(String name) {
            return new LibraryType(name);
        }
    }
}


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.megacrit.cardcrawl.helpers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MysteriousSphere;
import com.megacrit.cardcrawl.helpers.EnemyData.MonsterType;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.metrics.BotDataUploader;
import com.megacrit.cardcrawl.metrics.BotDataUploader.GameDataType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.beyond.Deca;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.beyond.GiantHead;
import com.megacrit.cardcrawl.monsters.beyond.Maw;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import com.megacrit.cardcrawl.monsters.beyond.Spiker;
import com.megacrit.cardcrawl.monsters.beyond.SpireGrowth;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.monsters.beyond.WrithingMass;
import com.megacrit.cardcrawl.monsters.city.BanditBear;
import com.megacrit.cardcrawl.monsters.city.BanditLeader;
import com.megacrit.cardcrawl.monsters.city.BanditPointy;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.BronzeAutomaton;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.city.Champ;
import com.megacrit.cardcrawl.monsters.city.Chosen;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.city.SnakePlant;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import com.megacrit.cardcrawl.monsters.exordium.GremlinFat;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.GremlinThief;
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWizard;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonsterHelper {
    private static final Logger logger = LogManager.getLogger(MonsterHelper.class.getName());
    private static final UIStrings uiStrings;
    public static final String[] MIXED_COMBAT_NAMES;
    public static final String BLUE_SLAVER_ENC = "Blue Slaver";
    public static final String CULTIST_ENC = "Cultist";
    public static final String JAW_WORM_ENC = "Jaw Worm";
    public static final String LOOTER_ENC = "Looter";
    public static final String TWO_LOUSE_ENC = "2 Louse";
    public static final String SMALL_SLIMES_ENC = "Small Slimes";
    public static final String GREMLIN_GANG_ENC = "Gremlin Gang";
    public static final String RED_SLAVER_ENC = "Red Slaver";
    public static final String LARGE_SLIME_ENC = "Large Slime";
    public static final String LVL_1_THUGS_ENC = "Exordium Thugs";
    public static final String LVL_1_WILDLIFE_ENC = "Exordium Wildlife";
    public static final String THREE_LOUSE_ENC = "3 Louse";
    public static final String TWO_FUNGI_ENC = "2 Fungi Beasts";
    public static final String LOTS_OF_SLIMES_ENC = "Lots of Slimes";
    public static final String GREMLIN_NOB_ENC = "Gremlin Nob";
    public static final String LAGAVULIN_ENC = "Lagavulin";
    public static final String THREE_SENTRY_ENC = "3 Sentries";
    public static final String LAGAVULIN_EVENT_ENC = "Lagavulin Event";
    public static final String MUSHROOMS_EVENT_ENC = "The Mushroom Lair";
    public static final String GUARDIAN_ENC = "The Guardian";
    public static final String HEXAGHOST_ENC = "Hexaghost";
    public static final String SLIME_BOSS_ENC = "Slime Boss";
    public static final String TWO_THIEVES_ENC = "2 Thieves";
    public static final String THREE_BYRDS_ENC = "3 Byrds";
    public static final String CHOSEN_ENC = "Chosen";
    public static final String SHELL_PARASITE_ENC = "Shell Parasite";
    public static final String SPHERE_GUARDIAN_ENC = "Spheric Guardian";
    public static final String CULTIST_CHOSEN_ENC = "Cultist and Chosen";
    public static final String THREE_CULTISTS_ENC = "3 Cultists";
    public static final String FOUR_BYRDS_ENC = "4 Byrds";
    public static final String CHOSEN_FLOCK_ENC = "Chosen and Byrds";
    public static final String SENTRY_SPHERE_ENC = "Sentry and Sphere";
    public static final String SNAKE_PLANT_ENC = "Snake Plant";
    public static final String SNECKO_ENC = "Snecko";
    public static final String TANK_HEALER_ENC = "Centurion and Healer";
    public static final String PARASITE_AND_FUNGUS = "Shelled Parasite and Fungi";
    public static final String STAB_BOOK_ENC = "Book of Stabbing";
    public static final String GREMLIN_LEADER_ENC = "Gremlin Leader";
    public static final String SLAVERS_ENC = "Slavers";
    public static final String MASKED_BANDITS_ENC = "Masked Bandits";
    public static final String COLOSSEUM_SLAVER_ENC = "Colosseum Slavers";
    public static final String COLOSSEUM_NOB_ENC = "Colosseum Nobs";
    public static final String AUTOMATON_ENC = "Automaton";
    public static final String CHAMP_ENC = "Champ";
    public static final String COLLECTOR_ENC = "Collector";
    public static final String THREE_DARKLINGS_ENC = "3 Darklings";
    public static final String THREE_SHAPES_ENC = "3 Shapes";
    public static final String ORB_WALKER_ENC = "Orb Walker";
    public static final String TRANSIENT_ENC = "Transient";
    public static final String REPTOMANCER_ENC = "Reptomancer";
    public static final String SPIRE_GROWTH_ENC = "Spire Growth";
    public static final String MAW_ENC = "Maw";
    public static final String FOUR_SHAPES_ENC = "4 Shapes";
    public static final String SPHERE_TWO_SHAPES_ENC = "Sphere and 2 Shapes";
    public static final String JAW_WORMS_HORDE = "Jaw Worm Horde";
    public static final String SNECKO_WITH_MYSTICS = "Snecko and Mystics";
    public static final String WRITHING_MASS_ENC = "Writhing Mass";
    public static final String TWO_ORB_WALKER_ENC = "2 Orb Walkers";
    public static final String NEMESIS_ENC = "Nemesis";
    public static final String GIANT_HEAD_ENC = "Giant Head";
    public static final String MYSTERIOUS_SPHERE_ENC = "Mysterious Sphere";
    public static final String MIND_BLOOM_BOSS = "Mind Bloom Boss Battle";
    public static final String TIME_EATER_ENC = "Time Eater";
    public static final String AWAKENED_ENC = "Awakened One";
    public static final String DONU_DECA_ENC = "Donu and Deca";
    public static final String THE_HEART_ENC = "The Heart";
    public static final String SHIELD_SPEAR_ENC = "Shield and Spear";
    public static final String EYES_ENC = "The Eyes";
    public static final String APOLOGY_SLIME_ENC = "Apologetic Slime";
    public static final String OLD_REPTO_ONE_ENC = "Flame Bruiser 1 Orb";
    public static final String OLD_REPTO_TWO_ENC = "Flame Bruiser 2 Orb";
    public static final String OLD_SLAVER_PARASITE = "Slaver and Parasite";
    public static final String OLD_SNECKO_MYSTICS = "Snecko and Mystics";

    public MonsterHelper() {
    }

    public static String getEncounterName(String key) {
        if (key == null) {
            return "";
        } else {
            byte var2 = -1;
            switch(key.hashCode()) {
                case -222004000:
                    if (key.equals("Snecko and Mystics")) {
                        var2 = 3;
                    }
                    break;
                case -52065857:
                    if (key.equals("Flame Bruiser 1 Orb")) {
                        var2 = 0;
                    }
                    break;
                case -51142336:
                    if (key.equals("Flame Bruiser 2 Orb")) {
                        var2 = 1;
                    }
                    break;
                case 563756501:
                    if (key.equals("Slaver and Parasite")) {
                        var2 = 2;
                    }
            }

            switch(var2) {
                case 0:
                case 1:
                    return MIXED_COMBAT_NAMES[25];
                case 2:
                    return MIXED_COMBAT_NAMES[26];
                case 3:
                    return MIXED_COMBAT_NAMES[27];
                default:
                    var2 = -1;
                    switch(key.hashCode()) {
                        case -2108458454:
                            if (key.equals("3 Sentries")) {
                                var2 = 16;
                            }
                            break;
                        case -2096825302:
                            if (key.equals("Red Slaver")) {
                                var2 = 5;
                            }
                            break;
                        case -2013947971:
                            if (key.equals("Lagavulin Event")) {
                                var2 = 17;
                            }
                            break;
                        case -2013219467:
                            if (key.equals("Looter")) {
                                var2 = 3;
                            }
                            break;
                        case -1879712874:
                            if (key.equals("2 Louse")) {
                                var2 = 10;
                            }
                            break;
                        case -1873987067:
                            if (key.equals("Slime Boss")) {
                                var2 = 21;
                            }
                            break;
                        case -1788706656:
                            if (key.equals("The Guardian")) {
                                var2 = 19;
                            }
                            break;
                        case -1508851536:
                            if (key.equals("Cultist")) {
                                var2 = 1;
                            }
                            break;
                        case -1368941933:
                            if (key.equals("Exordium Wildlife")) {
                                var2 = 8;
                            }
                            break;
                        case -1342165661:
                            if (key.equals("Large Slime")) {
                                var2 = 6;
                            }
                            break;
                        case -992209193:
                            if (key.equals("3 Louse")) {
                                var2 = 9;
                            }
                            break;
                        case -902890624:
                            if (key.equals("Exordium Thugs")) {
                                var2 = 7;
                            }
                            break;
                        case -663909825:
                            if (key.equals("Gremlin Nob")) {
                                var2 = 14;
                            }
                            break;
                        case -548386477:
                            if (key.equals("Jaw Worm")) {
                                var2 = 2;
                            }
                            break;
                        case -380917097:
                            if (key.equals("The Mushroom Lair")) {
                                var2 = 18;
                            }
                            break;
                        case 70731812:
                            if (key.equals("Small Slimes")) {
                                var2 = 13;
                            }
                            break;
                        case 644100489:
                            if (key.equals("Hexaghost")) {
                                var2 = 20;
                            }
                            break;
                        case 893410389:
                            if (key.equals("Gremlin Gang")) {
                                var2 = 4;
                            }
                            break;
                        case 1057095158:
                            if (key.equals("Lots of Slimes")) {
                                var2 = 12;
                            }
                            break;
                        case 1434486691:
                            if (key.equals("Lagavulin")) {
                                var2 = 15;
                            }
                            break;
                        case 1637395457:
                            if (key.equals("Blue Slaver")) {
                                var2 = 0;
                            }
                            break;
                        case 1650599105:
                            if (key.equals("2 Fungi Beasts")) {
                                var2 = 11;
                            }
                    }

                    switch(var2) {
                        case 0:
                            return SlaverBlue.NAME;
                        case 1:
                            return Cultist.NAME;
                        case 2:
                            return JawWorm.NAME;
                        case 3:
                            return Looter.NAME;
                        case 4:
                            return MIXED_COMBAT_NAMES[0];
                        case 5:
                            return SlaverRed.NAME;
                        case 6:
                            return MIXED_COMBAT_NAMES[1];
                        case 7:
                            return MIXED_COMBAT_NAMES[2];
                        case 8:
                            return MIXED_COMBAT_NAMES[3];
                        case 9:
                            return LouseNormal.NAME;
                        case 10:
                            return LouseNormal.NAME;
                        case 11:
                            return FungiBeast.NAME;
                        case 12:
                            return MIXED_COMBAT_NAMES[4];
                        case 13:
                            return MIXED_COMBAT_NAMES[5];
                        case 14:
                            return GremlinNob.NAME;
                        case 15:
                            return Lagavulin.NAME;
                        case 16:
                            return MIXED_COMBAT_NAMES[23];
                        case 17:
                            return Lagavulin.NAME;
                        case 18:
                            return FungiBeast.NAME;
                        case 19:
                            return TheGuardian.NAME;
                        case 20:
                            return Hexaghost.NAME;
                        case 21:
                            return SlimeBoss.NAME;
                        default:
                            var2 = -1;
                            switch(key.hashCode()) {
                                case -1834311388:
                                    if (key.equals("Colosseum Nobs")) {
                                        var2 = 18;
                                    }
                                    break;
                                case -1814052995:
                                    if (key.equals("Snecko")) {
                                        var2 = 11;
                                    }
                                    break;
                                case -1766312002:
                                    if (key.equals("Masked Bandits")) {
                                        var2 = 17;
                                    }
                                    break;
                                case -1706901225:
                                    if (key.equals("Sentry and Sphere")) {
                                        var2 = 9;
                                    }
                                    break;
                                case -1045722682:
                                    if (key.equals("Colosseum Slavers")) {
                                        var2 = 19;
                                    }
                                    break;
                                case -1001149827:
                                    if (key.equals("3 Byrds")) {
                                        var2 = 1;
                                    }
                                    break;
                                case -757040165:
                                    if (key.equals("Centurion and Healer")) {
                                        var2 = 12;
                                    }
                                    break;
                                case -621937833:
                                    if (key.equals("Shell Parasite")) {
                                        var2 = 4;
                                    }
                                    break;
                                case -617327920:
                                    if (key.equals("Automaton")) {
                                        var2 = 20;
                                    }
                                    break;
                                case -537649645:
                                    if (key.equals("Chosen and Byrds")) {
                                        var2 = 8;
                                    }
                                    break;
                                case -461459912:
                                    if (key.equals("Slavers")) {
                                        var2 = 16;
                                    }
                                    break;
                                case -407507859:
                                    if (key.equals("Collector")) {
                                        var2 = 22;
                                    }
                                    break;
                                case -279622453:
                                    if (key.equals("Gremlin Leader")) {
                                        var2 = 15;
                                    }
                                    break;
                                case -255148213:
                                    if (key.equals("Snake Plant")) {
                                        var2 = 10;
                                    }
                                    break;
                                case -113646146:
                                    if (key.equals("4 Byrds")) {
                                        var2 = 2;
                                    }
                                    break;
                                case 15349611:
                                    if (key.equals("Cultist and Chosen")) {
                                        var2 = 6;
                                    }
                                    break;
                                case 65070879:
                                    if (key.equals("Champ")) {
                                        var2 = 21;
                                    }
                                    break;
                                case 75370758:
                                    if (key.equals("2 Thieves")) {
                                        var2 = 0;
                                    }
                                    break;
                                case 813978224:
                                    if (key.equals("Shelled Parasite and Fungi")) {
                                        var2 = 13;
                                    }
                                    break;
                                case 1328987920:
                                    if (key.equals("3 Cultists")) {
                                        var2 = 7;
                                    }
                                    break;
                                case 1917396788:
                                    if (key.equals("Book of Stabbing")) {
                                        var2 = 14;
                                    }
                                    break;
                                case 1989842815:
                                    if (key.equals("Spheric Guardian")) {
                                        var2 = 5;
                                    }
                                    break;
                                case 2017619858:
                                    if (key.equals("Chosen")) {
                                        var2 = 3;
                                    }
                            }

                            switch(var2) {
                                case 0:
                                    return MIXED_COMBAT_NAMES[6];
                                case 1:
                                    return MIXED_COMBAT_NAMES[7];
                                case 2:
                                    return MIXED_COMBAT_NAMES[8];
                                case 3:
                                    return Chosen.NAME;
                                case 4:
                                    return ShelledParasite.NAME;
                                case 5:
                                    return SphericGuardian.NAME;
                                case 6:
                                    return MIXED_COMBAT_NAMES[24];
                                case 7:
                                    return MIXED_COMBAT_NAMES[9];
                                case 8:
                                    return MIXED_COMBAT_NAMES[10];
                                case 9:
                                    return MIXED_COMBAT_NAMES[11];
                                case 10:
                                    return SnakePlant.NAME;
                                case 11:
                                    return Snecko.NAME;
                                case 12:
                                    return MIXED_COMBAT_NAMES[12];
                                case 13:
                                    return MIXED_COMBAT_NAMES[13];
                                case 14:
                                    return BookOfStabbing.NAME;
                                case 15:
                                    return GremlinLeader.NAME;
                                case 16:
                                    return Taskmaster.NAME;
                                case 17:
                                    return MIXED_COMBAT_NAMES[14];
                                case 18:
                                    return MIXED_COMBAT_NAMES[15];
                                case 19:
                                    return MIXED_COMBAT_NAMES[16];
                                case 20:
                                    return BronzeAutomaton.NAME;
                                case 21:
                                    return Champ.NAME;
                                case 22:
                                    return TheCollector.NAME;
                                default:
                                    var2 = -1;
                                    switch(key.hashCode()) {
                                        case -2106067372:
                                            if (key.equals("Writhing Mass")) {
                                                var2 = 12;
                                            }
                                            break;
                                        case -1660991978:
                                            if (key.equals("Donu and Deca")) {
                                                var2 = 17;
                                            }
                                            break;
                                        case -1420565618:
                                            if (key.equals("2 Orb Walkers")) {
                                                var2 = 10;
                                            }
                                            break;
                                        case -1315824018:
                                            if (key.equals("Reptomancer")) {
                                                var2 = 0;
                                            }
                                            break;
                                        case -1238252950:
                                            if (key.equals("Transient")) {
                                                var2 = 1;
                                            }
                                            break;
                                        case -1235989956:
                                            if (key.equals("Sphere and 2 Shapes")) {
                                                var2 = 9;
                                            }
                                            break;
                                        case -1178669457:
                                            if (key.equals("Mysterious Sphere")) {
                                                var2 = 14;
                                            }
                                            break;
                                        case -793826098:
                                            if (key.equals("Nemesis")) {
                                                var2 = 11;
                                            }
                                            break;
                                        case -500373089:
                                            if (key.equals("3 Shapes")) {
                                                var2 = 3;
                                            }
                                            break;
                                        case -209678232:
                                            if (key.equals("Spire Growth")) {
                                                var2 = 6;
                                            }
                                            break;
                                        case 77123:
                                            if (key.equals("Maw")) {
                                                var2 = 7;
                                            }
                                            break;
                                        case 461826094:
                                            if (key.equals("Awakened One")) {
                                                var2 = 16;
                                            }
                                            break;
                                        case 731946207:
                                            if (key.equals("Jaw Worm Horde")) {
                                                var2 = 4;
                                            }
                                            break;
                                        case 1014856122:
                                            if (key.equals("3 Darklings")) {
                                                var2 = 2;
                                            }
                                            break;
                                        case 1242437246:
                                            if (key.equals("4 Shapes")) {
                                                var2 = 8;
                                            }
                                            break;
                                        case 1282761458:
                                            if (key.equals("Time Eater")) {
                                                var2 = 15;
                                            }
                                            break;
                                        case 1679632599:
                                            if (key.equals("Orb Walker")) {
                                                var2 = 5;
                                            }
                                            break;
                                        case 1871099803:
                                            if (key.equals("Giant Head")) {
                                                var2 = 13;
                                            }
                                    }

                                    switch(var2) {
                                        case 0:
                                            return Reptomancer.NAME;
                                        case 1:
                                            return Transient.NAME;
                                        case 2:
                                            return Darkling.NAME;
                                        case 3:
                                            return MIXED_COMBAT_NAMES[17];
                                        case 4:
                                            return MIXED_COMBAT_NAMES[18];
                                        case 5:
                                            return OrbWalker.NAME;
                                        case 6:
                                            return SpireGrowth.NAME;
                                        case 7:
                                            return Maw.NAME;
                                        case 8:
                                            return MIXED_COMBAT_NAMES[19];
                                        case 9:
                                            return MIXED_COMBAT_NAMES[20];
                                        case 10:
                                            return MIXED_COMBAT_NAMES[21];
                                        case 11:
                                            return Nemesis.NAME;
                                        case 12:
                                            return WrithingMass.NAME;
                                        case 13:
                                            return GiantHead.NAME;
                                        case 14:
                                            return MysteriousSphere.NAME;
                                        case 15:
                                            return TimeEater.NAME;
                                        case 16:
                                            return AwakenedOne.NAME;
                                        case 17:
                                            return MIXED_COMBAT_NAMES[22];
                                        default:
                                            var2 = -1;
                                            switch(key.hashCode()) {
                                                case -209380457:
                                                    if (key.equals("The Heart")) {
                                                        var2 = 0;
                                                    }
                                                    break;
                                                case 143439961:
                                                    if (key.equals("Shield and Spear")) {
                                                        var2 = 1;
                                                    }
                                            }

                                            switch(var2) {
                                                case 0:
                                                    return CorruptHeart.NAME;
                                                case 1:
                                                    return MIXED_COMBAT_NAMES[28];
                                            }
                                    }
                            }
                    }
            }
        }
        if (BaseMod.customMonsterExists(key)) {
            return BaseMod.getMonsterName(key);
        }
        return "";
    }

    public static MonsterGroup getEncounter(String key) {
        byte var2 = -1;
        switch(key.hashCode()) {
            case -2108458454:
                if (key.equals("3 Sentries")) {
                    var2 = 16;
                }
                break;
            case -2096825302:
                if (key.equals("Red Slaver")) {
                    var2 = 5;
                }
                break;
            case -2013947971:
                if (key.equals("Lagavulin Event")) {
                    var2 = 17;
                }
                break;
            case -2013219467:
                if (key.equals("Looter")) {
                    var2 = 3;
                }
                break;
            case -1879712874:
                if (key.equals("2 Louse")) {
                    var2 = 10;
                }
                break;
            case -1873987067:
                if (key.equals("Slime Boss")) {
                    var2 = 21;
                }
                break;
            case -1788706656:
                if (key.equals("The Guardian")) {
                    var2 = 19;
                }
                break;
            case -1508851536:
                if (key.equals("Cultist")) {
                    var2 = 1;
                }
                break;
            case -1368941933:
                if (key.equals("Exordium Wildlife")) {
                    var2 = 8;
                }
                break;
            case -1342165661:
                if (key.equals("Large Slime")) {
                    var2 = 6;
                }
                break;
            case -992209193:
                if (key.equals("3 Louse")) {
                    var2 = 9;
                }
                break;
            case -902890624:
                if (key.equals("Exordium Thugs")) {
                    var2 = 7;
                }
                break;
            case -663909825:
                if (key.equals("Gremlin Nob")) {
                    var2 = 14;
                }
                break;
            case -548386477:
                if (key.equals("Jaw Worm")) {
                    var2 = 2;
                }
                break;
            case -380917097:
                if (key.equals("The Mushroom Lair")) {
                    var2 = 18;
                }
                break;
            case 70731812:
                if (key.equals("Small Slimes")) {
                    var2 = 13;
                }
                break;
            case 644100489:
                if (key.equals("Hexaghost")) {
                    var2 = 20;
                }
                break;
            case 893410389:
                if (key.equals("Gremlin Gang")) {
                    var2 = 4;
                }
                break;
            case 1057095158:
                if (key.equals("Lots of Slimes")) {
                    var2 = 12;
                }
                break;
            case 1434486691:
                if (key.equals("Lagavulin")) {
                    var2 = 15;
                }
                break;
            case 1637395457:
                if (key.equals("Blue Slaver")) {
                    var2 = 0;
                }
                break;
            case 1650599105:
                if (key.equals("2 Fungi Beasts")) {
                    var2 = 11;
                }
        }

        switch(var2) {
            case 0:
                return new MonsterGroup(new SlaverBlue(0.0F, 0.0F));
            case 1:
                return new MonsterGroup(new Cultist(0.0F, -10.0F));
            case 2:
                return new MonsterGroup(new JawWorm(0.0F, 25.0F));
            case 3:
                return new MonsterGroup(new Looter(0.0F, 0.0F));
            case 4:
                return spawnGremlins();
            case 5:
                return new MonsterGroup(new SlaverRed(0.0F, 0.0F));
            case 6:
                if (AbstractDungeon.miscRng.randomBoolean()) {
                    return new MonsterGroup(new AcidSlime_L(0.0F, 0.0F));
                }

                return new MonsterGroup(new SpikeSlime_L(0.0F, 0.0F));
            case 7:
                return bottomHumanoid();
            case 8:
                return bottomWildlife();
            case 9:
                return new MonsterGroup(new AbstractMonster[]{getLouse(-350.0F, 25.0F), getLouse(-125.0F, 10.0F), getLouse(80.0F, 30.0F)});
            case 10:
                return new MonsterGroup(new AbstractMonster[]{getLouse(-200.0F, 10.0F), getLouse(80.0F, 30.0F)});
            case 11:
                return new MonsterGroup(new AbstractMonster[]{new FungiBeast(-400.0F, 30.0F), new FungiBeast(-40.0F, 20.0F)});
            case 12:
                return spawnManySmallSlimes();
            case 13:
                return spawnSmallSlimes();
            case 14:
                return new MonsterGroup(new GremlinNob(0.0F, 0.0F));
            case 15:
                return new MonsterGroup(new Lagavulin(true));
            case 16:
                return new MonsterGroup(new AbstractMonster[]{new Sentry(-330.0F, 25.0F), new Sentry(-85.0F, 10.0F), new Sentry(140.0F, 30.0F)});
            case 17:
                return new MonsterGroup(new Lagavulin(false));
            case 18:
                return new MonsterGroup(new AbstractMonster[]{new FungiBeast(-450.0F, 30.0F), new FungiBeast(-145.0F, 20.0F), new FungiBeast(180.0F, 15.0F)});
            case 19:
                return new MonsterGroup(new TheGuardian());
            case 20:
                return new MonsterGroup(new Hexaghost());
            case 21:
                return new MonsterGroup(new SlimeBoss());
            default:
                var2 = -1;
                switch(key.hashCode()) {
                    case -1834311388:
                        if (key.equals("Colosseum Nobs")) {
                            var2 = 18;
                        }
                        break;
                    case -1814052995:
                        if (key.equals("Snecko")) {
                            var2 = 11;
                        }
                        break;
                    case -1766312002:
                        if (key.equals("Masked Bandits")) {
                            var2 = 17;
                        }
                        break;
                    case -1706901225:
                        if (key.equals("Sentry and Sphere")) {
                            var2 = 9;
                        }
                        break;
                    case -1045722682:
                        if (key.equals("Colosseum Slavers")) {
                            var2 = 19;
                        }
                        break;
                    case -1001149827:
                        if (key.equals("3 Byrds")) {
                            var2 = 1;
                        }
                        break;
                    case -757040165:
                        if (key.equals("Centurion and Healer")) {
                            var2 = 12;
                        }
                        break;
                    case -621937833:
                        if (key.equals("Shell Parasite")) {
                            var2 = 4;
                        }
                        break;
                    case -617327920:
                        if (key.equals("Automaton")) {
                            var2 = 20;
                        }
                        break;
                    case -537649645:
                        if (key.equals("Chosen and Byrds")) {
                            var2 = 8;
                        }
                        break;
                    case -461459912:
                        if (key.equals("Slavers")) {
                            var2 = 16;
                        }
                        break;
                    case -407507859:
                        if (key.equals("Collector")) {
                            var2 = 22;
                        }
                        break;
                    case -279622453:
                        if (key.equals("Gremlin Leader")) {
                            var2 = 15;
                        }
                        break;
                    case -255148213:
                        if (key.equals("Snake Plant")) {
                            var2 = 10;
                        }
                        break;
                    case -113646146:
                        if (key.equals("4 Byrds")) {
                            var2 = 2;
                        }
                        break;
                    case 15349611:
                        if (key.equals("Cultist and Chosen")) {
                            var2 = 6;
                        }
                        break;
                    case 65070879:
                        if (key.equals("Champ")) {
                            var2 = 21;
                        }
                        break;
                    case 75370758:
                        if (key.equals("2 Thieves")) {
                            var2 = 0;
                        }
                        break;
                    case 813978224:
                        if (key.equals("Shelled Parasite and Fungi")) {
                            var2 = 13;
                        }
                        break;
                    case 1328987920:
                        if (key.equals("3 Cultists")) {
                            var2 = 7;
                        }
                        break;
                    case 1917396788:
                        if (key.equals("Book of Stabbing")) {
                            var2 = 14;
                        }
                        break;
                    case 1989842815:
                        if (key.equals("Spheric Guardian")) {
                            var2 = 5;
                        }
                        break;
                    case 2017619858:
                        if (key.equals("Chosen")) {
                            var2 = 3;
                        }
                }

                switch(var2) {
                    case 0:
                        return new MonsterGroup(new AbstractMonster[]{new Looter(-200.0F, 15.0F), new Mugger(80.0F, 0.0F)});
                    case 1:
                        return new MonsterGroup(new AbstractMonster[]{new Byrd(-360.0F, MathUtils.random(25.0F, 70.0F)), new Byrd(-80.0F, MathUtils.random(25.0F, 70.0F)), new Byrd(200.0F, MathUtils.random(25.0F, 70.0F))});
                    case 2:
                        return new MonsterGroup(new AbstractMonster[]{new Byrd(-470.0F, MathUtils.random(25.0F, 70.0F)), new Byrd(-210.0F, MathUtils.random(25.0F, 70.0F)), new Byrd(50.0F, MathUtils.random(25.0F, 70.0F)), new Byrd(310.0F, MathUtils.random(25.0F, 70.0F))});
                    case 3:
                        return new MonsterGroup(new Chosen());
                    case 4:
                        return new MonsterGroup(new ShelledParasite());
                    case 5:
                        return new MonsterGroup(new SphericGuardian());
                    case 6:
                        return new MonsterGroup(new AbstractMonster[]{new Cultist(-230.0F, 15.0F, false), new Chosen(100.0F, 25.0F)});
                    case 7:
                        return new MonsterGroup(new AbstractMonster[]{new Cultist(-465.0F, -20.0F, false), new Cultist(-130.0F, 15.0F, false), new Cultist(200.0F, -5.0F)});
                    case 8:
                        return new MonsterGroup(new AbstractMonster[]{new Byrd(-170.0F, MathUtils.random(25.0F, 70.0F)), new Chosen(80.0F, 0.0F)});
                    case 9:
                        return new MonsterGroup(new AbstractMonster[]{new Sentry(-305.0F, 30.0F), new SphericGuardian()});
                    case 10:
                        return new MonsterGroup(new SnakePlant(-30.0F, -30.0F));
                    case 11:
                        return new MonsterGroup(new Snecko());
                    case 12:
                        return new MonsterGroup(new AbstractMonster[]{new Centurion(-200.0F, 15.0F), new Healer(120.0F, 0.0F)});
                    case 13:
                        return new MonsterGroup(new AbstractMonster[]{new ShelledParasite(-260.0F, 15.0F), new FungiBeast(120.0F, 0.0F)});
                    case 14:
                        return new MonsterGroup(new BookOfStabbing());
                    case 15:
                        return new MonsterGroup(new AbstractMonster[]{spawnGremlin(GremlinLeader.POSX[0], GremlinLeader.POSY[0]), spawnGremlin(GremlinLeader.POSX[1], GremlinLeader.POSY[1]), new GremlinLeader()});
                    case 16:
                        return new MonsterGroup(new AbstractMonster[]{new SlaverBlue(-385.0F, -15.0F), new Taskmaster(-133.0F, 0.0F), new SlaverRed(125.0F, -30.0F)});
                    case 17:
                        return new MonsterGroup(new AbstractMonster[]{new BanditPointy(-320.0F, 0.0F), new BanditLeader(-75.0F, -6.0F), new BanditBear(150.0F, -6.0F)});
                    case 18:
                        return new MonsterGroup(new AbstractMonster[]{new Taskmaster(-270.0F, 15.0F), new GremlinNob(130.0F, 0.0F)});
                    case 19:
                        return new MonsterGroup(new AbstractMonster[]{new SlaverBlue(-270.0F, 15.0F), new SlaverRed(130.0F, 0.0F)});
                    case 20:
                        return new MonsterGroup(new BronzeAutomaton());
                    case 21:
                        return new MonsterGroup(new Champ());
                    case 22:
                        return new MonsterGroup(new TheCollector());
                    default:
                        var2 = -1;
                        switch(key.hashCode()) {
                            case -2106067372:
                                if (key.equals("Writhing Mass")) {
                                    var2 = 15;
                                }
                                break;
                            case -1660991978:
                                if (key.equals("Donu and Deca")) {
                                    var2 = 20;
                                }
                                break;
                            case -1420565618:
                                if (key.equals("2 Orb Walkers")) {
                                    var2 = 13;
                                }
                                break;
                            case -1315824018:
                                if (key.equals("Reptomancer")) {
                                    var2 = 2;
                                }
                                break;
                            case -1238252950:
                                if (key.equals("Transient")) {
                                    var2 = 3;
                                }
                                break;
                            case -1235989956:
                                if (key.equals("Sphere and 2 Shapes")) {
                                    var2 = 12;
                                }
                                break;
                            case -1178669457:
                                if (key.equals("Mysterious Sphere")) {
                                    var2 = 17;
                                }
                                break;
                            case -793826098:
                                if (key.equals("Nemesis")) {
                                    var2 = 14;
                                }
                                break;
                            case -500373089:
                                if (key.equals("3 Shapes")) {
                                    var2 = 5;
                                }
                                break;
                            case -222004000:
                                if (key.equals("Snecko and Mystics")) {
                                    var2 = 7;
                                }
                                break;
                            case -209678232:
                                if (key.equals("Spire Growth")) {
                                    var2 = 9;
                                }
                                break;
                            case -52065857:
                                if (key.equals("Flame Bruiser 1 Orb")) {
                                    var2 = 0;
                                }
                                break;
                            case -51142336:
                                if (key.equals("Flame Bruiser 2 Orb")) {
                                    var2 = 1;
                                }
                                break;
                            case 77123:
                                if (key.equals("Maw")) {
                                    var2 = 10;
                                }
                                break;
                            case 461826094:
                                if (key.equals("Awakened One")) {
                                    var2 = 19;
                                }
                                break;
                            case 731946207:
                                if (key.equals("Jaw Worm Horde")) {
                                    var2 = 6;
                                }
                                break;
                            case 1014856122:
                                if (key.equals("3 Darklings")) {
                                    var2 = 4;
                                }
                                break;
                            case 1242437246:
                                if (key.equals("4 Shapes")) {
                                    var2 = 11;
                                }
                                break;
                            case 1282761458:
                                if (key.equals("Time Eater")) {
                                    var2 = 18;
                                }
                                break;
                            case 1679632599:
                                if (key.equals("Orb Walker")) {
                                    var2 = 8;
                                }
                                break;
                            case 1871099803:
                                if (key.equals("Giant Head")) {
                                    var2 = 16;
                                }
                        }

                        switch(var2) {
                            case 0:
                                return new MonsterGroup(new AbstractMonster[]{new Reptomancer(), new SnakeDagger(Reptomancer.POSX[0], Reptomancer.POSY[0])});
                            case 1:
                            case 2:
                                return new MonsterGroup(new AbstractMonster[]{new SnakeDagger(Reptomancer.POSX[1], Reptomancer.POSY[1]), new Reptomancer(), new SnakeDagger(Reptomancer.POSX[0], Reptomancer.POSY[0])});
                            case 3:
                                return new MonsterGroup(new Transient());
                            case 4:
                                return new MonsterGroup(new AbstractMonster[]{new Darkling(-440.0F, 10.0F), new Darkling(-140.0F, 30.0F), new Darkling(180.0F, -5.0F)});
                            case 5:
                                return spawnShapes(true);
                            case 6:
                                return new MonsterGroup(new AbstractMonster[]{new JawWorm(-490.0F, -5.0F, true), new JawWorm(-150.0F, 20.0F, true), new JawWorm(175.0F, 5.0F, true)});
                            case 7:
                                return new MonsterGroup(new AbstractMonster[]{new Healer(-475.0F, -10.0F), new Snecko(-130.0F, -13.0F), new Healer(175.0F, -10.0F)});
                            case 8:
                                return new MonsterGroup(new OrbWalker(-30.0F, 20.0F));
                            case 9:
                                return new MonsterGroup(new SpireGrowth());
                            case 10:
                                return new MonsterGroup(new Maw(-70.0F, 20.0F));
                            case 11:
                                return spawnShapes(false);
                            case 12:
                                return new MonsterGroup(new AbstractMonster[]{getAncientShape(-435.0F, 10.0F), getAncientShape(-210.0F, 0.0F), new SphericGuardian(110.0F, 10.0F)});
                            case 13:
                                return new MonsterGroup(new AbstractMonster[]{new OrbWalker(-250.0F, 32.0F), new OrbWalker(150.0F, 26.0F)});
                            case 14:
                                return new MonsterGroup(new Nemesis());
                            case 15:
                                return new MonsterGroup(new WrithingMass());
                            case 16:
                                return new MonsterGroup(new GiantHead());
                            case 17:
                                return new MonsterGroup(new AbstractMonster[]{getAncientShape(-475.0F, 10.0F), getAncientShape(-250.0F, 0.0F), new OrbWalker(150.0F, 30.0F)});
                            case 18:
                                return new MonsterGroup(new TimeEater());
                            case 19:
                                return new MonsterGroup(new AbstractMonster[]{new Cultist(-590.0F, 10.0F, false), new Cultist(-298.0F, -10.0F, false), new AwakenedOne(100.0F, 15.0F)});
                            case 20:
                                return new MonsterGroup(new AbstractMonster[]{new Deca(), new Donu()});
                            default:
                                var2 = -1;
                                switch(key.hashCode()) {
                                    case -209380457:
                                        if (key.equals("The Heart")) {
                                            var2 = 0;
                                        }
                                        break;
                                    case 143439961:
                                        if (key.equals("Shield and Spear")) {
                                            var2 = 1;
                                        }
                                }

                                switch(var2) {
                                    case 0:
                                        return new MonsterGroup(new CorruptHeart());
                                    case 1:
                                        return new MonsterGroup(new AbstractMonster[]{new SpireShield(), new SpireSpear()});
                                }
                        }
                }
        }
        if (BaseMod.customMonsterExists(key)) {
            return BaseMod.getMonster(key);
        }
        return new MonsterGroup(new ApologySlime());
    }

    private static float randomYOffset(float y) {
        return y + MathUtils.random(-20.0F, 20.0F);
    }

    private static float randomXOffset(float x) {
        return x + MathUtils.random(-20.0F, 20.0F);
    }

    public static AbstractMonster getGremlin(String key, float xPos, float yPos) {
        byte var4 = -1;
        switch(key.hashCode()) {
            case -762313271:
                if (key.equals("GremlinWizard")) {
                    var4 = 4;
                }
                break;
            case 117167995:
                if (key.equals("GremlinFat")) {
                    var4 = 2;
                }
                break;
            case 942423992:
                if (key.equals("GremlinThief")) {
                    var4 = 1;
                }
                break;
            case 1076440234:
                if (key.equals("GremlinTsundere")) {
                    var4 = 3;
                }
                break;
            case 1902169252:
                if (key.equals("GremlinWarrior")) {
                    var4 = 0;
                }
        }

        switch(var4) {
            case 0:
                return new GremlinWarrior(xPos, yPos);
            case 1:
                return new GremlinThief(xPos, yPos);
            case 2:
                return new GremlinFat(xPos, yPos);
            case 3:
                return new GremlinTsundere(xPos, yPos);
            case 4:
                return new GremlinWizard(xPos, yPos);
            default:
                logger.info("UNKNOWN GREMLIN: " + key);
                return null;
        }
    }

    public static AbstractMonster getAncientShape(float x, float y) {
        switch(AbstractDungeon.miscRng.random(2)) {
            case 0:
                return new Spiker(x, y);
            case 1:
                return new Repulsor(x, y);
            default:
                return new Exploder(x, y);
        }
    }

    public static AbstractMonster getShape(String key, float xPos, float yPos) {
        byte var4 = -1;
        switch(key.hashCode()) {
            case -1864267823:
                if (key.equals("Exploder")) {
                    var4 = 2;
                }
                break;
            case -1812079284:
                if (key.equals("Spiker")) {
                    var4 = 1;
                }
                break;
            case -357033662:
                if (key.equals("Repulsor")) {
                    var4 = 0;
                }
        }

        switch(var4) {
            case 0:
                return new Repulsor(xPos, yPos);
            case 1:
                return new Spiker(xPos, yPos);
            case 2:
                return new Exploder(xPos, yPos);
            default:
                logger.info("UNKNOWN SHAPE: " + key);
                return null;
        }
    }

    private static MonsterGroup spawnShapes(boolean weak) {
        ArrayList<String> shapePool = new ArrayList<>();
        shapePool.add("Repulsor");
        shapePool.add("Repulsor");
        shapePool.add("Exploder");
        shapePool.add("Exploder");
        shapePool.add("Spiker");
        shapePool.add("Spiker");
        AbstractMonster[] retVal;
        if (weak) {
            retVal = new AbstractMonster[3];
        } else {
            retVal = new AbstractMonster[4];
        }

        int index = AbstractDungeon.miscRng.random(shapePool.size() - 1);
        String key = shapePool.get(index);
        shapePool.remove(index);
        retVal[0] = getShape(key, -480.0F, 6.0F);
        index = AbstractDungeon.miscRng.random(shapePool.size() - 1);
        key = shapePool.get(index);
        shapePool.remove(index);
        retVal[1] = getShape(key, -240.0F, -6.0F);
        index = AbstractDungeon.miscRng.random(shapePool.size() - 1);
        key = shapePool.get(index);
        shapePool.remove(index);
        retVal[2] = getShape(key, 0.0F, -12.0F);
        if (!weak) {
            index = AbstractDungeon.miscRng.random(shapePool.size() - 1);
            key = shapePool.get(index);
            shapePool.remove(index);
            retVal[3] = getShape(key, 240.0F, 12.0F);
        }

        return new MonsterGroup(retVal);
    }

    private static MonsterGroup spawnSmallSlimes() {
        AbstractMonster[] retVal = new AbstractMonster[2];
        if (AbstractDungeon.miscRng.randomBoolean()) {
            retVal[0] = new SpikeSlime_S(-230.0F, 32.0F, 0);
            retVal[1] = new AcidSlime_M(35.0F, 8.0F);
        } else {
            retVal[0] = new AcidSlime_S(-230.0F, 32.0F, 0);
            retVal[1] = new SpikeSlime_M(35.0F, 8.0F);
        }

        return new MonsterGroup(retVal);
    }

    private static MonsterGroup spawnManySmallSlimes() {
        ArrayList<String> slimePool = new ArrayList<>();
        slimePool.add("SpikeSlime_S");
        slimePool.add("SpikeSlime_S");
        slimePool.add("SpikeSlime_S");
        slimePool.add("AcidSlime_S");
        slimePool.add("AcidSlime_S");
        AbstractMonster[] retVal = new AbstractMonster[5];
        int index = AbstractDungeon.miscRng.random(slimePool.size() - 1);
        String key = slimePool.get(index);
        slimePool.remove(index);
        if (key.equals("SpikeSlime_S")) {
            retVal[0] = new SpikeSlime_S(-480.0F, 30.0F, 0);
        } else {
            retVal[0] = new AcidSlime_S(-480.0F, 30.0F, 0);
        }

        index = AbstractDungeon.miscRng.random(slimePool.size() - 1);
        key = slimePool.get(index);
        slimePool.remove(index);
        if (key.equals("SpikeSlime_S")) {
            retVal[1] = new SpikeSlime_S(-320.0F, 2.0F, 0);
        } else {
            retVal[1] = new AcidSlime_S(-320.0F, 2.0F, 0);
        }

        index = AbstractDungeon.miscRng.random(slimePool.size() - 1);
        key = slimePool.get(index);
        slimePool.remove(index);
        if (key.equals("SpikeSlime_S")) {
            retVal[2] = new SpikeSlime_S(-160.0F, 32.0F, 0);
        } else {
            retVal[2] = new AcidSlime_S(-160.0F, 32.0F, 0);
        }

        index = AbstractDungeon.miscRng.random(slimePool.size() - 1);
        key = slimePool.get(index);
        slimePool.remove(index);
        if (key.equals("SpikeSlime_S")) {
            retVal[3] = new SpikeSlime_S(10.0F, -12.0F, 0);
        } else {
            retVal[3] = new AcidSlime_S(10.0F, -12.0F, 0);
        }

        index = AbstractDungeon.miscRng.random(slimePool.size() - 1);
        key = slimePool.get(index);
        slimePool.remove(index);
        if (key.equals("SpikeSlime_S")) {
            retVal[4] = new SpikeSlime_S(200.0F, 9.0F, 0);
        } else {
            retVal[4] = new AcidSlime_S(200.0F, 9.0F, 0);
        }

        return new MonsterGroup(retVal);
    }

    private static MonsterGroup spawnGremlins() {
        ArrayList<String> gremlinPool = new ArrayList<>();
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinTsundere");
        gremlinPool.add("GremlinWizard");
        AbstractMonster[] retVal = new AbstractMonster[4];
        int index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        String key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[0] = getGremlin(key, -320.0F, 25.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[1] = getGremlin(key, -160.0F, -12.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[2] = getGremlin(key, 25.0F, -35.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[3] = getGremlin(key, 205.0F, 40.0F);
        return new MonsterGroup(retVal);
    }

    private static AbstractMonster spawnGremlin(float x, float y) {
        ArrayList<String> gremlinPool = new ArrayList<>();
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinTsundere");
        gremlinPool.add("GremlinWizard");
        return getGremlin(gremlinPool.get(AbstractDungeon.miscRng.random(0, gremlinPool.size() - 1)), x, y);
    }

    private static MonsterGroup bottomHumanoid() {
        AbstractMonster[] monsters = new AbstractMonster[]{bottomGetWeakWildlife(randomXOffset(-160.0F), randomYOffset(20.0F)), bottomGetStrongHumanoid(randomXOffset(130.0F), randomYOffset(20.0F))};
        return new MonsterGroup(monsters);
    }

    private static MonsterGroup bottomWildlife() {
        int numMonster = 2;
        AbstractMonster[] monsters = new AbstractMonster[numMonster];
        if (numMonster == 2) {
            monsters[0] = bottomGetStrongWildlife(randomXOffset(-150.0F), randomYOffset(20.0F));
            monsters[1] = bottomGetWeakWildlife(randomXOffset(150.0F), randomYOffset(20.0F));
        } else if (numMonster == 3) {
            monsters[0] = bottomGetWeakWildlife(randomXOffset(-200.0F), randomYOffset(20.0F));
            monsters[1] = bottomGetWeakWildlife(randomXOffset(0.0F), randomYOffset(20.0F));
            monsters[2] = bottomGetWeakWildlife(randomXOffset(200.0F), randomYOffset(20.0F));
        }

        return new MonsterGroup(monsters);
    }

    private static AbstractMonster bottomGetStrongHumanoid(float x, float y) {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        monsters.add(new Cultist(x, y));
        monsters.add(getSlaver(x, y));
        monsters.add(new Looter(x, y));
        return monsters.get(AbstractDungeon.miscRng.random(0, monsters.size() - 1));
    }

    private static AbstractMonster bottomGetStrongWildlife(float x, float y) {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        monsters.add(new FungiBeast(x, y));
        monsters.add(new JawWorm(x, y));
        return monsters.get(AbstractDungeon.miscRng.random(0, monsters.size() - 1));
    }

    private static AbstractMonster bottomGetWeakWildlife(float x, float y) {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        monsters.add(getLouse(x, y));
        monsters.add(new SpikeSlime_M(x, y));
        monsters.add(new AcidSlime_M(x, y));
        return monsters.get(AbstractDungeon.miscRng.random(0, monsters.size() - 1));
    }

    private static AbstractMonster getSlaver(float x, float y) {
        return AbstractDungeon.miscRng.randomBoolean() ? new SlaverRed(x, y) : new SlaverBlue(x, y);
    }

    private static AbstractMonster getLouse(float x, float y) {
        return AbstractDungeon.miscRng.randomBoolean() ? new LouseNormal(x, y) : new LouseDefensive(x, y);
    }

    public static void uploadEnemyData() {
        ArrayList<String> derp = new ArrayList<>();
        ArrayList<EnemyData> data = new ArrayList<>();
        data.add(new EnemyData("Blue Slaver", 1, MonsterType.WEAK));
        data.add(new EnemyData("Cultist", 1, MonsterType.WEAK));
        data.add(new EnemyData("Jaw Worm", 1, MonsterType.WEAK));
        data.add(new EnemyData("2 Louse", 1, MonsterType.WEAK));
        data.add(new EnemyData("Small Slimes", 1, MonsterType.WEAK));
        data.add(new EnemyData("Gremlin Gang", 1, MonsterType.STRONG));
        data.add(new EnemyData("Large Slime", 1, MonsterType.STRONG));
        data.add(new EnemyData("Looter", 1, MonsterType.STRONG));
        data.add(new EnemyData("Lots of Slimes", 1, MonsterType.STRONG));
        data.add(new EnemyData("Exordium Thugs", 1, MonsterType.STRONG));
        data.add(new EnemyData("Exordium Wildlife", 1, MonsterType.STRONG));
        data.add(new EnemyData("Red Slaver", 1, MonsterType.STRONG));
        data.add(new EnemyData("3 Louse", 1, MonsterType.STRONG));
        data.add(new EnemyData("2 Fungi Beasts", 1, MonsterType.STRONG));
        data.add(new EnemyData("Gremlin Nob", 1, MonsterType.ELITE));
        data.add(new EnemyData("Lagavulin", 1, MonsterType.ELITE));
        data.add(new EnemyData("3 Sentries", 1, MonsterType.ELITE));
        data.add(new EnemyData("Lagavulin Event", 1, MonsterType.EVENT));
        data.add(new EnemyData("The Mushroom Lair", 1, MonsterType.EVENT));
        data.add(new EnemyData("The Guardian", 1, MonsterType.BOSS));
        data.add(new EnemyData("Hexaghost", 1, MonsterType.BOSS));
        data.add(new EnemyData("Slime Boss", 1, MonsterType.BOSS));
        data.add(new EnemyData("Chosen", 2, MonsterType.WEAK));
        data.add(new EnemyData("Shell Parasite", 2, MonsterType.WEAK));
        data.add(new EnemyData("Spheric Guardian", 2, MonsterType.WEAK));
        data.add(new EnemyData("3 Byrds", 2, MonsterType.WEAK));
        data.add(new EnemyData("2 Thieves", 2, MonsterType.WEAK));
        data.add(new EnemyData("Chosen and Byrds", 2, MonsterType.STRONG));
        data.add(new EnemyData("Sentry and Sphere", 2, MonsterType.STRONG));
        data.add(new EnemyData("Snake Plant", 2, MonsterType.STRONG));
        data.add(new EnemyData("Snecko", 2, MonsterType.STRONG));
        data.add(new EnemyData("Centurion and Healer", 2, MonsterType.STRONG));
        data.add(new EnemyData("Cultist and Chosen", 2, MonsterType.STRONG));
        data.add(new EnemyData("3 Cultists", 2, MonsterType.STRONG));
        data.add(new EnemyData("Shelled Parasite and Fungi", 2, MonsterType.STRONG));
        data.add(new EnemyData("Gremlin Leader", 2, MonsterType.ELITE));
        data.add(new EnemyData("Slavers", 2, MonsterType.ELITE));
        data.add(new EnemyData("Book of Stabbing", 2, MonsterType.ELITE));
        data.add(new EnemyData("Masked Bandits", 2, MonsterType.EVENT));
        data.add(new EnemyData("Colosseum Nobs", 2, MonsterType.EVENT));
        data.add(new EnemyData("Colosseum Slavers", 2, MonsterType.EVENT));
        data.add(new EnemyData("Automaton", 2, MonsterType.BOSS));
        data.add(new EnemyData("Champ", 2, MonsterType.BOSS));
        data.add(new EnemyData("Collector", 2, MonsterType.BOSS));
        data.add(new EnemyData("Orb Walker", 3, MonsterType.WEAK));
        data.add(new EnemyData("3 Darklings", 3, MonsterType.WEAK));
        data.add(new EnemyData("3 Shapes", 3, MonsterType.WEAK));
        data.add(new EnemyData("Transient", 3, MonsterType.STRONG));
        data.add(new EnemyData("4 Shapes", 3, MonsterType.STRONG));
        data.add(new EnemyData("Maw", 3, MonsterType.STRONG));
        data.add(new EnemyData("Jaw Worm Horde", 3, MonsterType.STRONG));
        data.add(new EnemyData("Sphere and 2 Shapes", 3, MonsterType.STRONG));
        data.add(new EnemyData("Spire Growth", 3, MonsterType.STRONG));
        data.add(new EnemyData("Writhing Mass", 3, MonsterType.STRONG));
        data.add(new EnemyData("Giant Head", 3, MonsterType.ELITE));
        data.add(new EnemyData("Nemesis", 3, MonsterType.ELITE));
        data.add(new EnemyData("Reptomancer", 3, MonsterType.ELITE));
        data.add(new EnemyData("Mysterious Sphere", 3, MonsterType.EVENT));
        data.add(new EnemyData("Mind Bloom Boss Battle", 3, MonsterType.EVENT));
        data.add(new EnemyData("2 Orb Walkers", 3, MonsterType.EVENT));
        data.add(new EnemyData("Awakened One", 3, MonsterType.BOSS));
        data.add(new EnemyData("Donu and Deca", 3, MonsterType.BOSS));
        data.add(new EnemyData("Time Eater", 3, MonsterType.BOSS));
        data.add(new EnemyData("Shield and Spear", 4, MonsterType.ELITE));
        data.add(new EnemyData("The Heart", 4, MonsterType.BOSS));

        for (EnemyData d : data) {
            derp.add(d.gameDataUploadData());
        }

        BotDataUploader.uploadDataAsync(GameDataType.ENEMY_DATA, EnemyData.gameDataUploadHeader(), derp);
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RunHistoryMonsterNames");
        MIXED_COMBAT_NAMES = uiStrings.TEXT;
    }
}

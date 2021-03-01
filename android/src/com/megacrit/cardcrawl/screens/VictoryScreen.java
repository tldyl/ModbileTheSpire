package com.megacrit.cardcrawl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomCutscene;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.metrics.Metrics.MetricRequestType;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;
import com.megacrit.cardcrawl.ui.buttons.ReturnToMenuButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.AscensionLevelUpTextEffect;
import com.megacrit.cardcrawl.vfx.AscensionUnlockedTextEffect;
import com.megacrit.cardcrawl.vfx.BetaCardArtUnlockedTextEffect;
import com.megacrit.cardcrawl.vfx.scene.DefectVictoryEyesEffect;
import com.megacrit.cardcrawl.vfx.scene.DefectVictoryNumberEffect;
import com.megacrit.cardcrawl.vfx.scene.IroncladVictoryFlameEffect;
import com.megacrit.cardcrawl.vfx.scene.SilentVictoryStarEffect;
import com.megacrit.cardcrawl.vfx.scene.SlowFireParticleEffect;
import com.megacrit.cardcrawl.vfx.scene.WatcherVictoryEffect;
import java.util.ArrayList;
import java.util.Iterator;

public class VictoryScreen extends GameOverScreen {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(VictoryScreen.class);
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private MonsterGroup monsters;
    private ArrayList<AbstractGameEffect> effect = new ArrayList<>();
    private float effectTimer = 0.0F;
    private float effectTimer2 = 0.0F;
    public static long STINGER_ID;
    public static String STINGER_KEY;
    private int unlockedBetaArt;

    public VictoryScreen(MonsterGroup m) {
        isVictory = true;
        this.playtime = (long)CardCrawlGame.playtime;
        if (this.playtime < 0L) {
            this.playtime = 0L;
        }

        AbstractDungeon.getCurrRoom().clearEvent();
        resetScoreChecks();
        AbstractDungeon.is_victory = true;
        AbstractDungeon.player.drawX = (float)Settings.WIDTH / 2.0F;
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.screen = CurrentScreen.VICTORY;
        AbstractDungeon.overlayMenu.showBlackScreen(1.0F);
        AbstractDungeon.previousScreen = null;
        AbstractDungeon.overlayMenu.cancelButton.hideInstantly();
        AbstractDungeon.isScreenUp = true;
        this.monsters = m;
        logger.info("PLAYTIME: " + this.playtime);
        if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
            ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
        }

        this.showingStats = false;
        this.returnButton = new ReturnToMenuButton();
        this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[0]);
        AbstractDungeon.dynamicBanner.appear(TEXT[12]);
        if (Settings.isStandardRun()) {
            CardCrawlGame.playerPref.putInteger(AbstractDungeon.player.chosenClass.name() + "_SPIRITS", 1);
        }

        this.unlockedBetaArt = -1;
        switch(AbstractDungeon.player.chosenClass.name()) {
            case "IRONCLAD":
                if (!UnlockTracker.isAchievementUnlocked("RUBY_PLUS")) {
                    this.unlockedBetaArt = 0;
                    UnlockTracker.unlockAchievement("RUBY_PLUS");
                }
                break;
            case "THE_SILENT":
                if (!UnlockTracker.isAchievementUnlocked("EMERALD_PLUS")) {
                    this.unlockedBetaArt = 1;
                    UnlockTracker.unlockAchievement("EMERALD_PLUS");
                    CampfireUI.hidden = true;
                }
                break;
            case "DEFECT":
                if (!UnlockTracker.isAchievementUnlocked("SAPPHIRE_PLUS")) {
                    this.unlockedBetaArt = 2;
                    UnlockTracker.unlockAchievement("SAPPHIRE_PLUS");
                    this.effectTimer2 = 5.0F;
                }
                break;
            case "WATCHER":
                if (!UnlockTracker.isAchievementUnlocked("AMETHYST_PLUS")) {
                    this.unlockedBetaArt = 4;
                    UnlockTracker.unlockAchievement("AMETHYST");
                    UnlockTracker.unlockAchievement("AMETHYST_PLUS");
                }
        }

        if (UnlockTracker.isAchievementUnlocked("RUBY_PLUS") && UnlockTracker.isAchievementUnlocked("EMERALD_PLUS") && UnlockTracker.isAchievementUnlocked("SAPPHIRE_PLUS")) {
            UnlockTracker.unlockAchievement("THE_ENDING");
        }

        this.submitVictoryMetrics();
        if (this.playtime != 0L) {
            StatsScreen.updateVictoryTime(this.playtime);
        }

        StatsScreen.incrementVictory(AbstractDungeon.player.getCharStat());
        StatsScreen.incrementAscension(AbstractDungeon.player.getCharStat());
        if (AbstractDungeon.ascensionLevel == 10 && !Settings.isTrial) {
            UnlockTracker.unlockAchievement("ASCEND_10");
        } else if (AbstractDungeon.ascensionLevel == 20 && !Settings.isTrial) {
            UnlockTracker.unlockAchievement("ASCEND_20");
        }

        if (this.playtime != 0L) {
            StatsScreen.incrementPlayTime(this.playtime);
        }

        if (Settings.isStandardRun()) {
            StatsScreen.updateFurthestAscent(AbstractDungeon.floorNum);
        } else if (Settings.isDailyRun) {
            StatsScreen.updateHighestDailyScore(AbstractDungeon.floorNum);
        }

        if (SaveHelper.shouldDeleteSave()) {
            SaveAndContinue.deleteSave(AbstractDungeon.player);
        }

        this.calculateUnlockProgress();
        if (!Settings.isEndless) {
            this.uploadToSteamLeaderboards();
        }

        this.createGameOverStats();
        CardCrawlGame.playerPref.flush();
    }

    private void createGameOverStats() {
        this.stats.clear();
        this.stats.add(new GameOverStat(TEXT[1] + " (" + AbstractDungeon.floorNum + ")", null, Integer.toString(floorPoints)));
        this.stats.add(new GameOverStat(TEXT[8] + " (" + CardCrawlGame.monstersSlain + ")", null, Integer.toString(monsterPoints)));
        this.stats.add(new GameOverStat(EXORDIUM_ELITE.NAME + " (" + CardCrawlGame.elites1Slain + ")", null, Integer.toString(elite1Points)));
        if (Settings.isEndless) {
            if (CardCrawlGame.elites2Slain > 0) {
                this.stats.add(new GameOverStat(CITY_ELITE.NAME + " (" + CardCrawlGame.elites2Slain + ")", null, Integer.toString(elite2Points)));
            }
        } else if (CardCrawlGame.dungeon instanceof TheCity || CardCrawlGame.dungeon instanceof TheBeyond || CardCrawlGame.dungeon instanceof TheEnding) {
            this.stats.add(new GameOverStat(CITY_ELITE.NAME + " (" + CardCrawlGame.elites2Slain + ")", null, Integer.toString(elite2Points)));
        }

        if (Settings.isEndless) {
            if (CardCrawlGame.elites3Slain > 0) {
                this.stats.add(new GameOverStat(BEYOND_ELITE.NAME + " (" + CardCrawlGame.elites3Slain + ")", null, Integer.toString(elite3Points)));
            }
        } else if (CardCrawlGame.dungeon instanceof TheBeyond || CardCrawlGame.dungeon instanceof TheEnding) {
            this.stats.add(new GameOverStat(BEYOND_ELITE.NAME + " (" + CardCrawlGame.elites3Slain + ")", null, Integer.toString(elite3Points)));
        }

        this.stats.add(new GameOverStat(BOSSES_SLAIN.NAME + " (" + AbstractDungeon.bossCount + ")", null, Integer.toString(bossPoints)));
        if (IS_POOPY) {
            this.stats.add(new GameOverStat(POOPY.NAME, POOPY.DESCRIPTIONS[0], Integer.toString(-1)));
        }

        if (IS_SPEEDSTER) {
            this.stats.add(new GameOverStat(SPEEDSTER.NAME, SPEEDSTER.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (IS_LIGHT_SPEED) {
            this.stats.add(new GameOverStat(LIGHT_SPEED.NAME, LIGHT_SPEED.DESCRIPTIONS[0], Integer.toString(50)));
        }

        if (IS_HIGHLANDER) {
            this.stats.add(new GameOverStat(HIGHLANDER.NAME, HIGHLANDER.DESCRIPTIONS[0], Integer.toString(100)));
        }

        if (IS_SHINY) {
            this.stats.add(new GameOverStat(SHINY.NAME, SHINY.DESCRIPTIONS[0], Integer.toString(50)));
        }

        if (IS_I_LIKE_GOLD) {
            this.stats.add(new GameOverStat(I_LIKE_GOLD.NAME + " (" + CardCrawlGame.goldGained + ")", I_LIKE_GOLD.DESCRIPTIONS[0], Integer.toString(75)));
        } else if (IS_RAINING_MONEY) {
            this.stats.add(new GameOverStat(RAINING_MONEY.NAME + " (" + CardCrawlGame.goldGained + ")", RAINING_MONEY.DESCRIPTIONS[0], Integer.toString(50)));
        } else if (IS_MONEY_MONEY) {
            this.stats.add(new GameOverStat(MONEY_MONEY.NAME + " (" + CardCrawlGame.goldGained + ")", MONEY_MONEY.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (IS_MYSTERY_MACHINE) {
            this.stats.add(new GameOverStat(MYSTERY_MACHINE.NAME + " (" + CardCrawlGame.mysteryMachine + ")", MYSTERY_MACHINE.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (IS_FULL_SET > 0) {
            this.stats.add(new GameOverStat(COLLECTOR.NAME + " (" + IS_FULL_SET + ")", COLLECTOR.DESCRIPTIONS[0], Integer.toString(25 * IS_FULL_SET)));
        }

        if (IS_PAUPER) {
            this.stats.add(new GameOverStat(PAUPER.NAME, PAUPER.DESCRIPTIONS[0], Integer.toString(50)));
        }

        if (IS_LIBRARY) {
            this.stats.add(new GameOverStat(LIBRARIAN.NAME, LIBRARIAN.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (IS_ENCYCLOPEDIA) {
            this.stats.add(new GameOverStat(ENCYCLOPEDIAN.NAME, ENCYCLOPEDIAN.DESCRIPTIONS[0], Integer.toString(50)));
        }

        if (IS_STUFFED) {
            this.stats.add(new GameOverStat(STUFFED.NAME, STUFFED.DESCRIPTIONS[0], Integer.toString(50)));
        } else if (IS_WELL_FED) {
            this.stats.add(new GameOverStat(WELL_FED.NAME, WELL_FED.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (IS_CURSES) {
            this.stats.add(new GameOverStat(CURSES.NAME, CURSES.DESCRIPTIONS[0], Integer.toString(100)));
        }

        if (IS_ON_MY_OWN) {
            this.stats.add(new GameOverStat(ON_MY_OWN_TERMS.NAME, ON_MY_OWN_TERMS.DESCRIPTIONS[0], Integer.toString(50)));
        }

        if (CardCrawlGame.champion > 0) {
            this.stats.add(new GameOverStat(CHAMPION.NAME + " (" + CardCrawlGame.champion + ")", CHAMPION.DESCRIPTIONS[0], Integer.toString(25 * CardCrawlGame.champion)));
        }

        if (CardCrawlGame.perfect >= 3) {
            this.stats.add(new GameOverStat(BEYOND_PERFECT.NAME, BEYOND_PERFECT.DESCRIPTIONS[0], Integer.toString(200)));
        } else if (CardCrawlGame.perfect > 0) {
            this.stats.add(new GameOverStat(PERFECT.NAME + " (" + CardCrawlGame.perfect + ")", PERFECT.DESCRIPTIONS[0], Integer.toString(50 * CardCrawlGame.perfect)));
        }

        if (CardCrawlGame.overkill) {
            this.stats.add(new GameOverStat(OVERKILL.NAME, OVERKILL.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (CardCrawlGame.combo) {
            this.stats.add(new GameOverStat(COMBO.NAME, COMBO.DESCRIPTIONS[0], Integer.toString(25)));
        }

        if (AbstractDungeon.isAscensionMode) {
            this.stats.add(new GameOverStat(ASCENSION.NAME + " (" + AbstractDungeon.ascensionLevel + ")", ASCENSION.DESCRIPTIONS[0], Integer.toString(ascensionPoints)));
        }

        if (CardCrawlGame.dungeon instanceof TheEnding) {
            this.stats.add(new GameOverStat(HEARTBREAKER.NAME, HEARTBREAKER.DESCRIPTIONS[0], Integer.toString(250)));
        }

        this.stats.add(new GameOverStat());
        this.stats.add(new GameOverStat(TEXT[4], null, Integer.toString(this.score)));
    }

    protected void submitVictoryMetrics() {
        Metrics metrics = new Metrics();
        metrics.gatherAllDataAndSave(false, true, null);
        if (Settings.UPLOAD_DATA) {
            metrics.setValues(false, true, null, MetricRequestType.UPLOAD_METRICS);
            Thread t = new Thread(metrics);
            t.start();
        }

        if (Settings.isStandardRun()) {
            StatsScreen.updateFurthestAscent(AbstractDungeon.floorNum);
        }

        if (SaveHelper.shouldDeleteSave()) {
            SaveAndContinue.deleteSave(AbstractDungeon.player);
        }

    }

    public void hide() {
        this.returnButton.hide();
        AbstractDungeon.dynamicBanner.hide();
    }

    public void reopen() {
        this.reopen(false);
    }

    public void reopen(boolean fromVictoryUnlock) {
        AbstractDungeon.previousScreen = CurrentScreen.DEATH;
        this.statsTimer = 0.5F;
        AbstractDungeon.dynamicBanner.appearInstantly(TEXT[12]);
        AbstractDungeon.overlayMenu.showBlackScreen(1.0F);
        if (fromVictoryUnlock) {
            this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[0]);
        } else if (!this.showingStats) {
            this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[0]);
        } else if (this.unlockBundle == null) {
            this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[0]);
        } else {
            this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[5]);
        }

    }

    public void update() {
        if (Settings.isDebug && InputHelper.justClickedRight) {
            UnlockTracker.resetUnlockProgress(AbstractDungeon.player.chosenClass);
        }

        this.updateControllerInput();
        this.returnButton.update();
        if (this.returnButton.hb.clicked || this.returnButton.show && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.topPanel.unpress();
            if (Settings.isControllerMode) {
                Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            }

            this.returnButton.hb.clicked = false;
            if (!this.showingStats) {
                this.showingStats = true;
                this.statsTimer = 0.5F;
                logger.info("Clicked");
                this.returnButton = new ReturnToMenuButton();
                this.updateAscensionAndBetaArtProgress();
                if (this.unlockBundle == null) {
                    this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[0]);
                } else {
                    this.returnButton.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[5]);
                }
            } else if (this.unlockBundle != null) {
                AbstractDungeon.gUnlockScreen.open(this.unlockBundle, true);
                AbstractDungeon.previousScreen = CurrentScreen.VICTORY;
                AbstractDungeon.screen = CurrentScreen.NEOW_UNLOCK;
                this.unlockBundle = null;
                this.returnButton.label = TEXT[5];
            } else {
                this.returnButton.hide();
                if (!AbstractDungeon.unlocks.isEmpty() && !Settings.isDemo) {
                    AbstractDungeon.unlocks.clear();
                    Settings.isTrial = false;
                    Settings.isDailyRun = false;
                    Settings.isEndless = false;
                    CardCrawlGame.trial = null;
                    if (Settings.isDailyRun) {
                        CardCrawlGame.startOver();
                    } else {
                        CardCrawlGame.playCreditsBgm = true;
                        CardCrawlGame.startOverButShowCredits();
                    }
                } else {
                    CardCrawlGame.playCreditsBgm = true;
                    CardCrawlGame.startOverButShowCredits();
                }
            }
        }

        this.updateStatsScreen();
        if (this.monsters != null) {
            this.monsters.update();
            this.monsters.updateAnimations();
        }

        this.updateVfx();
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
            boolean anyHovered = false;
            int index = 0;
            if (this.stats != null) {
                for(Iterator var3 = this.stats.iterator(); var3.hasNext(); ++index) {
                    GameOverStat s = (GameOverStat)var3.next();
                    if (s.hb.hovered) {
                        anyHovered = true;
                        break;
                    }
                }
            }

            if (!anyHovered) {
                index = -1;
            }

            int numItemsInLeftColumn;
            if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                    if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                        if (this.stats.size() > 10) {
                            numItemsInLeftColumn = (this.stats.size() - 2) / 2;
                            if (this.stats.size() % 2 != 0) {
                                ++numItemsInLeftColumn;
                            }

                            if (index < numItemsInLeftColumn - 1) {
                                index += numItemsInLeftColumn;
                            } else if (index == numItemsInLeftColumn - 1) {
                                if (this.stats.size() % 2 != 0) {
                                    index += numItemsInLeftColumn - 1;
                                } else {
                                    index += numItemsInLeftColumn;
                                }
                            } else if (index >= numItemsInLeftColumn && index < this.stats.size() - 2) {
                                index -= numItemsInLeftColumn;
                            }
                        }

                        if (index > this.stats.size() - 1) {
                            index = this.stats.size() - 1;
                        }

                        if (index != -1) {
                            CInputHelper.setCursor(this.stats.get(index).hb);
                        }
                    }
                } else {
                    if (index == -1) {
                        index = 0;
                        CInputHelper.setCursor(this.stats.get(index).hb);
                        return;
                    }

                    ++index;
                    if (this.stats.size() > 10) {
                        numItemsInLeftColumn = (this.stats.size() - 2) / 2;
                        if (this.stats.size() % 2 != 0) {
                            ++numItemsInLeftColumn;
                        }

                        if (index == numItemsInLeftColumn) {
                            index = this.stats.size() - 1;
                        }
                    } else {
                        if (index > this.stats.size() - 1) {
                            index = 0;
                        }

                        if (index == this.stats.size() - 2) {
                            ++index;
                        }
                    }

                    if (index > this.stats.size() - 3) {
                        index = this.stats.size() - 1;
                    }

                    CInputHelper.setCursor(this.stats.get(index).hb);
                }
            } else {
                --index;
                if (this.stats.size() > 10) {
                    numItemsInLeftColumn = (this.stats.size() - 2) / 2;
                    if (this.stats.size() % 2 == 0) {
                        --numItemsInLeftColumn;
                    }

                    if (index == numItemsInLeftColumn) {
                        index = this.stats.size() - 1;
                    } else if (index < 0) {
                        index = this.stats.size() - 1;
                    } else if (index == this.stats.size() - 2) {
                        --index;
                    }
                } else if (index < 0) {
                    index = this.stats.size() - 1;
                } else if (index == this.stats.size() - 2) {
                    --index;
                }

                CInputHelper.setCursor(this.stats.get(index).hb);
            }

        }
    }

    private void updateAscensionAndBetaArtProgress() {
        if (AbstractDungeon.isAscensionMode && !Settings.seedSet && !Settings.isTrial && AbstractDungeon.ascensionLevel < 20 && StatsScreen.isPlayingHighestAscension(AbstractDungeon.player.getPrefs())) {
            StatsScreen.incrementAscension(AbstractDungeon.player.getCharStat());
            AbstractDungeon.topLevelEffects.add(new AscensionLevelUpTextEffect());
        } else if (!AbstractDungeon.ascensionCheck && UnlockTracker.isAscensionUnlocked(AbstractDungeon.player)) {
            AbstractDungeon.topLevelEffects.add(new AscensionUnlockedTextEffect());
        } else if (this.unlockedBetaArt != -1) {
            AbstractDungeon.topLevelEffects.add(new BetaCardArtUnlockedTextEffect(this.unlockedBetaArt));
        }

    }

    private void updateVfx() {
        switch(AbstractDungeon.player.chosenClass.name()) {
            case "IRONCLAD":
                this.effectTimer -= Gdx.graphics.getDeltaTime();
                if (this.effectTimer < 0.0F) {
                    this.effect.add(new SlowFireParticleEffect());
                    this.effect.add(new SlowFireParticleEffect());
                    this.effect.add(new IroncladVictoryFlameEffect());
                    this.effect.add(new IroncladVictoryFlameEffect());
                    this.effect.add(new IroncladVictoryFlameEffect());
                    this.effectTimer = 0.1F;
                }
                break;
            case "THE_SILENT":
                this.effectTimer -= Gdx.graphics.getDeltaTime();
                if (this.effectTimer < 0.0F) {
                    if (this.effect.size() < 100) {
                        this.effect.add(new SilentVictoryStarEffect());
                        this.effect.add(new SilentVictoryStarEffect());
                        this.effect.add(new SilentVictoryStarEffect());
                        this.effect.add(new SilentVictoryStarEffect());
                    }

                    this.effectTimer = 0.1F;
                }

                this.effectTimer2 += Gdx.graphics.getDeltaTime();
                if (this.effectTimer2 > 1.0F) {
                    this.effectTimer2 = 1.0F;
                }
                break;
            case "DEFECT":
                boolean foundEyeVfx = false;

                for (AbstractGameEffect e : this.effect) {
                    if (e instanceof DefectVictoryEyesEffect) {
                        foundEyeVfx = true;
                        break;
                    }
                }

                if (!foundEyeVfx) {
                    this.effect.add(new DefectVictoryEyesEffect());
                }

                if (this.effect.size() < 15) {
                    this.effect.add(new DefectVictoryNumberEffect());
                }
                break;
            case "WATCHER":
                boolean createdEffect = false;

                for (AbstractGameEffect e : this.effect) {
                    if (e instanceof WatcherVictoryEffect) {
                        createdEffect = true;
                        break;
                    }
                }

                if (!createdEffect) {
                    this.effect.add(new WatcherVictoryEffect());
                }
                break;
            default:
                CustomCutscene cutscene = BaseMod.cutsceneMap.getOrDefault(AbstractDungeon.player.chosenClass, null);
                if (cutscene != null) {
                    cutscene.updateVictoryVfx(this.effect);
                }
                break;
        }

    }

    private void updateStatsScreen() {
        if (this.showingStats) {
            this.progressBarAlpha = MathHelper.slowColorLerpSnap(this.progressBarAlpha, 1.0F);
            this.statsTimer -= Gdx.graphics.getDeltaTime();
            if (this.statsTimer < 0.0F) {
                this.statsTimer = 0.0F;
            }

            this.returnButton.y = Interpolation.pow3In.apply((float)Settings.HEIGHT * 0.1F, (float)Settings.HEIGHT * 0.15F, this.statsTimer * 1.0F / 0.5F);
            AbstractDungeon.dynamicBanner.y = Interpolation.pow3In.apply((float)Settings.HEIGHT / 2.0F + 320.0F * Settings.scale, DynamicBanner.Y, this.statsTimer * 1.0F / 0.5F);

            for (GameOverStat i : this.stats) {
                i.update();
            }

            if (this.statAnimateTimer < 0.0F) {
                boolean allStatsShown = true;

                for (GameOverStat i : this.stats) {
                    if (i.hidden) {
                        i.hidden = false;
                        this.statAnimateTimer = 0.1F;
                        allStatsShown = false;
                        break;
                    }
                }

                if (allStatsShown) {
                    this.animateProgressBar();
                }
            } else {
                this.statAnimateTimer -= Gdx.graphics.getDeltaTime();
            }
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        Iterator i = this.effect.iterator();

        AbstractGameEffect e;
        while(i.hasNext()) {
            e = (AbstractGameEffect)i.next();
            if (e.renderBehind) {
                e.render(sb);
            }

            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

        sb.setBlendFunction(770, 771);
        if (AbstractDungeon.player.chosenClass == PlayerClass.THE_SILENT) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.effectTimer2));
            AbstractDungeon.player.renderShoulderImg(sb);
        }

        sb.setBlendFunction(770, 1);
        i = this.effect.iterator();

        while(i.hasNext()) {
            e = (AbstractGameEffect)i.next();
            if (!e.renderBehind) {
                e.render(sb);
            }
        }

        sb.setBlendFunction(770, 771);
        this.renderStatsScreen(sb);
        this.returnButton.render(sb);
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("VictoryScreen");
        TEXT = uiStrings.TEXT;
        STINGER_ID = -999L;
        STINGER_KEY = "";
    }
}

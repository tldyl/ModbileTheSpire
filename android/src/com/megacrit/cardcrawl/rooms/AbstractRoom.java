package com.megacrit.cardcrawl.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Careless;
import com.megacrit.cardcrawl.daily.mods.ControlledChaos;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.DevInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.BlessingOfTheForge;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile.SaveType;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.GameSavedEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractRoom implements Disposable {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final SpireAndroidLogger logger;
    public ArrayList<AbstractPotion> potions = new ArrayList<>();
    public ArrayList<AbstractRelic> relics = new ArrayList<>();
    public ArrayList<RewardItem> rewards = new ArrayList<>();
    public SoulGroup souls = new SoulGroup();
    public AbstractRoom.RoomPhase phase;
    public AbstractEvent event = null;
    public MonsterGroup monsters;
    private float endBattleTimer = 0.0F;
    public float rewardPopOutTimer = 1.0F;
    private static final float END_TURN_WAIT_DURATION = 1.2F;
    protected String mapSymbol;
    protected Texture mapImg;
    protected Texture mapImgOutline;
    public boolean isBattleOver = false;
    public boolean cannotLose = false;
    public boolean eliteTrigger = false;
    public static int blizzardPotionMod;
    private static final int BLIZZARD_POTION_MOD_AMT = 10;
    public boolean mugged = false;
    public boolean smoked = false;
    public boolean combatEvent = false;
    public boolean rewardAllowed = true;
    public boolean rewardTime = false;
    public boolean skipMonsterTurn = false;
    public int baseRareCardChance = 3;
    public int baseUncommonCardChance = 37;
    public int rareCardChance;
    public int uncommonCardChance;
    public static float waitTimer;

    public AbstractRoom() {
        this.rareCardChance = this.baseRareCardChance;
        this.uncommonCardChance = this.baseUncommonCardChance;
    }

    public final Texture getMapImg() {
        return this.mapImg;
    }

    public final Texture getMapImgOutline() {
        return this.mapImgOutline;
    }

    public final void setMapImg(Texture img, Texture imgOutline) {
        this.mapImg = img;
        this.mapImgOutline = imgOutline;
    }

    public abstract void onPlayerEntry();

    public void applyEmeraldEliteBuff() {
    }

    public void playBGM(String key) {
        CardCrawlGame.music.playTempBGM(key);
    }

    public void playBgmInstantly(String key) {
        CardCrawlGame.music.playTempBgmInstantly(key);
    }

    public final String getMapSymbol() {
        return this.mapSymbol;
    }

    public final void setMapSymbol(String newSymbol) {
        this.mapSymbol = newSymbol;
    }

    public CardRarity getCardRarity(int roll) {
        return this.getCardRarity(roll, true);
    }

    public CardRarity getCardRarity(int roll, boolean useAlternation) {
        this.rareCardChance = this.baseRareCardChance;
        this.uncommonCardChance = this.baseUncommonCardChance;
        if (useAlternation) {
            this.alterCardRarityProbabilities();
        }

        Iterator var3;
        AbstractRelic r;
        if (roll < this.rareCardChance) {
            if (roll >= this.baseRareCardChance) {
                var3 = AbstractDungeon.player.relics.iterator();

                while(var3.hasNext()) {
                    r = (AbstractRelic)var3.next();
                    if (r.changeRareCardRewardChance(this.baseRareCardChance) > this.baseRareCardChance) {
                        r.flash();
                    }
                }
            }

            return CardRarity.RARE;
        } else if (roll >= this.rareCardChance + this.uncommonCardChance) {
            return CardRarity.COMMON;
        } else {
            if (roll >= this.baseRareCardChance + this.baseUncommonCardChance) {
                var3 = AbstractDungeon.player.relics.iterator();

                while(var3.hasNext()) {
                    r = (AbstractRelic)var3.next();
                    if (r.changeUncommonCardRewardChance(this.baseUncommonCardChance) > this.baseUncommonCardChance) {
                        r.flash();
                    }
                }
            }

            return CardRarity.UNCOMMON;
        }
    }

    public void alterCardRarityProbabilities() {
        Iterator var1;
        AbstractRelic r;
        for(var1 = AbstractDungeon.player.relics.iterator(); var1.hasNext(); this.rareCardChance = r.changeRareCardRewardChance(this.rareCardChance)) {
            r = (AbstractRelic)var1.next();
        }

        for(var1 = AbstractDungeon.player.relics.iterator(); var1.hasNext(); this.uncommonCardChance = r.changeUncommonCardRewardChance(this.uncommonCardChance)) {
            r = (AbstractRelic)var1.next();
        }

    }

    public void updateObjects() {
        this.souls.update();
        Iterator i = this.potions.iterator();

        while(i.hasNext()) {
            AbstractPotion tmpPotion = (AbstractPotion)i.next();
            tmpPotion.update();
            if (tmpPotion.isObtained) {
                i.remove();
            }
        }

        i = this.relics.iterator();

        while(i.hasNext()) {
            AbstractRelic relic = (AbstractRelic)i.next();
            relic.update();
            if (relic.isDone) {
                i.remove();
            }
        }

    }

    public void update() {
        if (!AbstractDungeon.isScreenUp && InputHelper.pressedEscape && AbstractDungeon.overlayMenu.cancelButton.current_x == CancelButton.HIDE_X) {
            AbstractDungeon.settingsScreen.open();
        }

        if (Settings.isDebug) {
            if (InputHelper.justClickedRight) {
                AbstractDungeon.player.obtainPotion(new BlessingOfTheForge());
                AbstractDungeon.scene.randomizeScene();
            }

            if (Gdx.input.isKeyJustPressed(49)) {
                AbstractDungeon.player.increaseMaxOrbSlots(1, true);
            }

            if (DevInputActionSet.gainGold.isJustPressed()) {
                AbstractDungeon.player.gainGold(100);
            }
        }

        switch(this.phase) {
            case EVENT:
                this.event.updateDialog();
                break;
            case COMBAT:
                this.monsters.update();
                if (waitTimer <= 0.0F) {
                    if (Settings.isDebug && DevInputActionSet.drawCard.isJustPressed()) {
                        AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 1));
                    }

                    if (!AbstractDungeon.isScreenUp) {
                        AbstractDungeon.actionManager.update();
                        if (!this.monsters.areMonstersBasicallyDead() && AbstractDungeon.player.currentHealth > 0) {
                            AbstractDungeon.player.updateInput();
                        }
                    }

                    if (!AbstractDungeon.screen.equals(CurrentScreen.HAND_SELECT)) {
                        AbstractDungeon.player.combatUpdate();
                    }

                    if (AbstractDungeon.player.isEndingTurn) {
                        this.endTurn();
                    }
                } else {
                    if (AbstractDungeon.actionManager.currentAction == null && AbstractDungeon.actionManager.isEmpty()) {
                        waitTimer -= Gdx.graphics.getDeltaTime();
                    } else {
                        AbstractDungeon.actionManager.update();
                    }

                    if (waitTimer <= 0.0F) {
                        AbstractDungeon.actionManager.turnHasEnded = true;
                        if (!AbstractDungeon.isScreenUp) {
                            AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
                        }

                        AbstractDungeon.actionManager.addToBottom(new GainEnergyAndEnableControlsAction(AbstractDungeon.player.energy.energyMaster));
                        AbstractDungeon.player.applyStartOfCombatPreDrawLogic();
                        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.gameHandSize));
                        AbstractDungeon.actionManager.addToBottom(new EnableEndTurnButtonAction());
                        AbstractDungeon.overlayMenu.showCombatPanels();
                        AbstractDungeon.player.applyStartOfCombatLogic();
                        if (ModHelper.isModEnabled("Careless")) {
                            Careless.modAction();
                        }

                        if (ModHelper.isModEnabled("ControlledChaos")) {
                            ControlledChaos.modAction();
                        }

                        this.skipMonsterTurn = false;
                        AbstractDungeon.player.applyStartOfTurnRelics();
                        AbstractDungeon.player.applyStartOfTurnPostDrawRelics();
                        AbstractDungeon.player.applyStartOfTurnCards();
                        AbstractDungeon.player.applyStartOfTurnPowers();
                        AbstractDungeon.player.applyStartOfTurnOrbs();
                        AbstractDungeon.actionManager.useNextCombatActions();
                    }
                }

                if (this.isBattleOver && AbstractDungeon.actionManager.actions.isEmpty()) {
                    this.skipMonsterTurn = false;
                    this.endBattleTimer -= Gdx.graphics.getDeltaTime();
                    if (this.endBattleTimer < 0.0F) {
                        this.phase = AbstractRoom.RoomPhase.COMPLETE;
                        if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) || !(CardCrawlGame.dungeon instanceof TheBeyond) || Settings.isEndless) {
                            CardCrawlGame.sound.play("VICTORY");
                        }

                        this.endBattleTimer = 0.0F;
                        int card_seed_before_roll;
                        if (this instanceof MonsterRoomBoss && !AbstractDungeon.loading_post_combat) {
                            if (!CardCrawlGame.loadingSave) {
                                if (Settings.isDailyRun) {
                                    this.addGoldToRewards(100);
                                } else {
                                    card_seed_before_roll = 100 + AbstractDungeon.miscRng.random(-5, 5);
                                    if (AbstractDungeon.ascensionLevel >= 13) {
                                        this.addGoldToRewards(MathUtils.round((float)card_seed_before_roll * 0.75F));
                                    } else {
                                        this.addGoldToRewards(card_seed_before_roll);
                                    }
                                }
                            }

                            if (ModHelper.isModEnabled("Cursed Run")) {
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            }
                        } else if (this instanceof MonsterRoomElite && !AbstractDungeon.loading_post_combat) {
                            if (CardCrawlGame.dungeon instanceof Exordium) {
                                ++CardCrawlGame.elites1Slain;
                                logger.info("ELITES SLAIN " + CardCrawlGame.elites1Slain);
                            } else if (CardCrawlGame.dungeon instanceof TheCity) {
                                ++CardCrawlGame.elites2Slain;
                                logger.info("ELITES SLAIN " + CardCrawlGame.elites2Slain);
                            } else if (CardCrawlGame.dungeon instanceof TheBeyond) {
                                ++CardCrawlGame.elites3Slain;
                                logger.info("ELITES SLAIN " + CardCrawlGame.elites3Slain);
                            } else {
                                ++CardCrawlGame.elitesModdedSlain;
                                logger.info("ELITES SLAIN " + CardCrawlGame.elitesModdedSlain);
                            }

                            if (!CardCrawlGame.loadingSave) {
                                if (Settings.isDailyRun) {
                                    this.addGoldToRewards(30);
                                } else {
                                    this.addGoldToRewards(AbstractDungeon.treasureRng.random(25, 35));
                                }
                            }
                        } else if (this instanceof MonsterRoom && !AbstractDungeon.getMonsters().haveMonstersEscaped()) {
                            ++CardCrawlGame.monstersSlain;
                            logger.info("MONSTERS SLAIN " + CardCrawlGame.monstersSlain);
                            if (Settings.isDailyRun) {
                                this.addGoldToRewards(15);
                            } else {
                                this.addGoldToRewards(AbstractDungeon.treasureRng.random(10, 20));
                            }
                        }

                        if (!(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) || !(CardCrawlGame.dungeon instanceof TheBeyond) && !(CardCrawlGame.dungeon instanceof TheEnding) || Settings.isEndless) {
                            if (!AbstractDungeon.loading_post_combat) {
                                this.dropReward();
                                this.addPotionToRewards();
                            }

                            card_seed_before_roll = AbstractDungeon.cardRng.counter;
                            int card_randomizer_before_roll = AbstractDungeon.cardBlizzRandomizer;
                            if (this.rewardAllowed) {
                                if (this.mugged) {
                                    AbstractDungeon.combatRewardScreen.openCombat(TEXT[0]);
                                } else if (this.smoked) {
                                    AbstractDungeon.combatRewardScreen.openCombat(TEXT[1], true);
                                } else {
                                    AbstractDungeon.combatRewardScreen.open();
                                }

                                if (!CardCrawlGame.loadingSave && !AbstractDungeon.loading_post_combat) {
                                    SaveFile saveFile = new SaveFile(SaveType.POST_COMBAT);
                                    saveFile.card_seed_count = card_seed_before_roll;
                                    saveFile.card_random_seed_randomizer = card_randomizer_before_roll;
                                    if (this.combatEvent) {
                                        --saveFile.event_seed_count;
                                    }

                                    SaveAndContinue.save(saveFile);
                                    AbstractDungeon.effectList.add(new GameSavedEffect());
                                } else {
                                    CardCrawlGame.loadingSave = false;
                                }

                                AbstractDungeon.loading_post_combat = false;
                            }
                        }
                    }
                }

                this.monsters.updateAnimations();
                break;
            case COMPLETE:
                if (!AbstractDungeon.isScreenUp) {
                    AbstractDungeon.actionManager.update();
                    if (this.event != null) {
                        this.event.updateDialog();
                    }

                    if (AbstractDungeon.actionManager.isEmpty() && !AbstractDungeon.isFadingOut) {
                        if (this.rewardPopOutTimer > 1.0F) {
                            this.rewardPopOutTimer = 1.0F;
                        }

                        this.rewardPopOutTimer -= Gdx.graphics.getDeltaTime();
                        if (this.rewardPopOutTimer < 0.0F) {
                            if (this.event == null) {
                                AbstractDungeon.overlayMenu.proceedButton.show();
                            } else if (!(this.event instanceof AbstractImageEvent) && !this.event.hasFocus) {
                                AbstractDungeon.overlayMenu.proceedButton.show();
                            }
                        }
                    }
                }
            case INCOMPLETE:
                break;
            default:
                logger.info("MISSING PHASE, bro");
        }

        AbstractDungeon.player.update();
        AbstractDungeon.player.updateAnimations();
    }

    public void endTurn() {
        AbstractDungeon.player.applyEndOfTurnTriggers();
        AbstractDungeon.actionManager.addToBottom(new ClearCardQueueAction());
        AbstractDungeon.actionManager.addToBottom(new DiscardAtEndOfTurnAction());
        Iterator var1 = AbstractDungeon.player.drawPile.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            c.resetAttributes();
        }

        var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            c.resetAttributes();
        }

        var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            c.resetAttributes();
        }

        if (AbstractDungeon.player.hoveredCard != null) {
            AbstractDungeon.player.hoveredCard.resetAttributes();
        }

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                this.addToBot(new EndTurnAction());
                this.addToBot(new WaitAction(1.2F));
                if (!AbstractRoom.this.skipMonsterTurn) {
                    this.addToBot(new MonsterStartTurnAction());
                }

                AbstractDungeon.actionManager.monsterAttacksQueued = false;
                this.isDone = true;
            }
        });
        AbstractDungeon.player.isEndingTurn = false;
    }

    public void endBattle() {
        this.isBattleOver = true;
        if (AbstractDungeon.player.currentHealth == 1) {
            UnlockTracker.unlockAchievement("SHRUG_IT_OFF");
        }

        if (AbstractDungeon.player.hasRelic("Meat on the Bone")) {
            AbstractDungeon.player.getRelic("Meat on the Bone").onTrigger();
        }

        AbstractDungeon.player.onVictory();
        this.endBattleTimer = 0.25F;
        int attackCount = 0;
        int skillCount = 0;

        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (c.type == CardType.ATTACK) {
                ++attackCount;
                break;
            }

            if (c.type == CardType.SKILL) {
                ++skillCount;
            }
        }

        if (attackCount == 0 && !this.smoked) {
            UnlockTracker.unlockAchievement("COME_AT_ME");
        }

        if (skillCount == 0) {
        }

        if (!this.smoked && GameActionManager.damageReceivedThisCombat - GameActionManager.hpLossThisCombat <= 0 && this instanceof MonsterRoomElite) {
            ++CardCrawlGame.champion;
        }

        CardCrawlGame.metricData.addEncounterData();
        AbstractDungeon.actionManager.clear();
        AbstractDungeon.player.inSingleTargetMode = false;
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.resetControllerValues();
        AbstractDungeon.overlayMenu.hideCombatPanels();
        if (!AbstractDungeon.player.stance.ID.equals("Neutral") && AbstractDungeon.player.stance != null) {
            AbstractDungeon.player.stance.stopIdleSfx();
        }
        BaseMod.publishPostBattle(this);
    }

    public void dropReward() {
    }

    public void render(SpriteBatch sb) {
        if (!(this instanceof EventRoom) && !(this instanceof VictoryRoom)) {
            if (AbstractDungeon.screen != CurrentScreen.BOSS_REWARD) {
                AbstractDungeon.player.render(sb);
            }
        } else if (this.event != null && (!(this.event instanceof AbstractImageEvent) || this.event.combatTime)) {
            this.event.renderRoomEventPanel(sb);
            if (AbstractDungeon.screen != CurrentScreen.VICTORY) {
                AbstractDungeon.player.render(sb);
            }
        }

        Iterator var2;
        if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            if (this.monsters != null && AbstractDungeon.screen != CurrentScreen.DEATH) {
                this.monsters.render(sb);
            }

            if (this.phase == AbstractRoom.RoomPhase.COMBAT) {
                AbstractDungeon.player.renderPlayerBattleUi(sb);
            }

            var2 = this.potions.iterator();

            while(var2.hasNext()) {
                AbstractPotion i = (AbstractPotion)var2.next();
                if (!i.isObtained) {
                    i.render(sb);
                }
            }
        }

        var2 = this.relics.iterator();

        while(var2.hasNext()) {
            AbstractRelic r = (AbstractRelic)var2.next();
            r.render(sb);
        }

        this.renderTips(sb);
    }

    public void renderAboveTopPanel(SpriteBatch sb) {

        for (AbstractPotion p : this.potions) {
            if (p.isObtained) {
                p.render(sb);
            }
        }

        this.souls.render(sb);
        if (Settings.isInfo) {
            String msg = "[GAME MODE DATA]\n isDaily: " + Settings.isDailyRun + "\n isSpecialSeed: " + Settings.isTrial + "\n isAscension: " + AbstractDungeon.isAscensionMode + "\n\n[CARDGROUPS]\n Deck: " + AbstractDungeon.player.masterDeck.size() + "\n Draw Pile: " + AbstractDungeon.player.drawPile.size() + "\n Discard Pile: " + AbstractDungeon.player.discardPile.size() + "\n Exhaust Pile: " + AbstractDungeon.player.exhaustPile.size() + "\n\n[ACTION MANAGER]\n Phase: " + AbstractDungeon.actionManager.phase.name() + "\n turnEnded: " + AbstractDungeon.actionManager.turnHasEnded + "\n numTurns: " + GameActionManager.turn + "\n\n[Misc]\n Publisher Connection: " + CardCrawlGame.publisherIntegration.isInitialized() + "\n CUR_SCREEN: " + AbstractDungeon.screen.name() + "\n Controller Mode: " + Settings.isControllerMode + "\n isFadingOut: " + AbstractDungeon.isFadingOut + "\n isScreenUp: " + AbstractDungeon.isScreenUp + "\n Particle Count: " + AbstractDungeon.effectList.size();
            FontHelper.renderFontCenteredHeight(sb, FontHelper.tipBodyFont, msg, 30.0F, (float)Settings.HEIGHT * 0.5F, Color.WHITE);
        }

    }

    public void renderTips(SpriteBatch sb) {
    }

    public void spawnRelicAndObtain(float x, float y, AbstractRelic relic) {
        if (relic.relicId.equals("Circlet") && AbstractDungeon.player.hasRelic("Circlet")) {
            AbstractRelic circ = AbstractDungeon.player.getRelic("Circlet");
            ++circ.counter;
            circ.flash();
        } else {
            relic.spawn(x, y);
            this.relics.add(relic);
            relic.obtain();
            relic.isObtained = true;
            relic.isAnimating = false;
            relic.isDone = false;
            relic.flash();
        }

    }

    public void spawnBlightAndObtain(float x, float y, AbstractBlight blight) {
        blight.spawn(x, y);
        blight.obtain();
        blight.isObtained = true;
        blight.isAnimating = false;
        blight.isDone = false;
        blight.flash();
    }

    public void applyEndOfTurnRelics() {
        Iterator var1 = AbstractDungeon.player.relics.iterator();

        while(var1.hasNext()) {
            AbstractRelic r = (AbstractRelic)var1.next();
            r.onPlayerEndTurn();
        }

        var1 = AbstractDungeon.player.blights.iterator();

        while(var1.hasNext()) {
            AbstractBlight b = (AbstractBlight)var1.next();
            b.onPlayerEndTurn();
        }

    }

    public void applyEndOfTurnPreCardPowers() {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            p.atEndOfTurnPreEndTurnCards(true);
        }
    }

    public void addRelicToRewards(RelicTier tier) {
        this.rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(tier)));
    }

    public void addSapphireKey(RewardItem item) {
        this.rewards.add(new RewardItem(item, RewardType.SAPPHIRE_KEY));
    }

    public void removeOneRelicFromRewards() {
        Iterator i = this.rewards.iterator();

        while(i.hasNext()) {
            RewardItem rewardItem = (RewardItem)i.next();
            if (rewardItem.type == RewardType.RELIC) {
                i.remove();
                if (i.hasNext() && rewardItem.relicLink == i.next()) {
                    i.remove();
                }
                break;
            }
        }

    }

    public void addNoncampRelicToRewards(RelicTier tier) {
        this.rewards.add(new RewardItem(AbstractDungeon.returnRandomNonCampfireRelic(tier)));
    }

    public void addRelicToRewards(AbstractRelic relic) {
        this.rewards.add(new RewardItem(relic));
    }

    public void addPotionToRewards(AbstractPotion potion) {
        this.rewards.add(new RewardItem(potion));
    }

    public void addCardToRewards() {
        RewardItem cardReward = new RewardItem();
        if (cardReward.cards.size() > 0) {
            this.rewards.add(cardReward);
        }

    }

    public void addPotionToRewards() {
        int chance = 0;
        if (this instanceof MonsterRoomElite) {
            chance = 40;
            chance = chance + blizzardPotionMod;
        } else if (this instanceof MonsterRoom) {
            if (!AbstractDungeon.getMonsters().haveMonstersEscaped()) {
                chance = 40;
                chance = chance + blizzardPotionMod;
            }
        } else if (this instanceof EventRoom) {
            chance = 40;
            chance = chance + blizzardPotionMod;
        }

        if (AbstractDungeon.player.hasRelic("White Beast Statue")) {
            chance = 100;
        }

        if (this.rewards.size() >= 4) {
            chance = 0;
        }

        logger.info("POTION CHANCE: " + chance);
        if (AbstractDungeon.potionRng.random(0, 99) >= chance && !Settings.isDebug) {
            blizzardPotionMod += 10;
        } else {
            CardCrawlGame.metricData.potions_floor_spawned.add(AbstractDungeon.floorNum);
            this.rewards.add(new RewardItem(AbstractDungeon.returnRandomPotion()));
            blizzardPotionMod -= 10;
        }

    }

    public void addGoldToRewards(int gold) {
        Iterator var2 = this.rewards.iterator();

        RewardItem i;
        do {
            if (!var2.hasNext()) {
                this.rewards.add(new RewardItem(gold));
                return;
            }

            i = (RewardItem)var2.next();
        } while(i.type != RewardType.GOLD);

        i.incrementGold(gold);
    }

    public void addStolenGoldToRewards(int gold) {
        Iterator var2 = this.rewards.iterator();

        RewardItem i;
        do {
            if (!var2.hasNext()) {
                this.rewards.add(new RewardItem(gold, true));
                return;
            }

            i = (RewardItem)var2.next();
        } while(i.type != RewardType.STOLEN_GOLD);

        i.incrementGold(gold);
    }

    public boolean isBattleEnding() {
        if (this.isBattleOver) {
            return true;
        } else {
            return this.monsters != null && this.monsters.areMonstersBasicallyDead();
        }
    }

    public void renderEventTexts(SpriteBatch sb) {
        if (this.event != null) {
            this.event.renderText(sb);
        }

    }

    public void clearEvent() {
        if (this.event != null) {
            this.event.imageEventText.clear();
            this.event.roomEventText.clear();
        }

    }

    public void eventControllerInput() {
        if (Settings.isControllerMode) {
            if (AbstractDungeon.getCurrRoom().event != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.potionUi.targetMode && !AbstractDungeon.player.viewingRelics) {
                boolean anyHovered;
                int index;
                Iterator var3;
                LargeDialogOptionButton o;
                if (!RoomEventDialog.optionList.isEmpty()) {
                    anyHovered = false;
                    index = 0;

                    for(var3 = RoomEventDialog.optionList.iterator(); var3.hasNext(); ++index) {
                        o = (LargeDialogOptionButton)var3.next();
                        if (o.hb.hovered) {
                            anyHovered = true;
                            break;
                        }
                    }

                    if (!anyHovered) {
                        Gdx.input.setCursorPosition((int) RoomEventDialog.optionList.get(0).hb.cX, Settings.HEIGHT - (int) RoomEventDialog.optionList.get(0).hb.cY);
                    } else if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                        if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
                            --index;
                            if (index < 0) {
                                index = RoomEventDialog.optionList.size() - 1;
                            }

                            Gdx.input.setCursorPosition((int) RoomEventDialog.optionList.get(index).hb.cX, Settings.HEIGHT - (int) RoomEventDialog.optionList.get(index).hb.cY);
                        }
                    } else {
                        ++index;
                        if (index > RoomEventDialog.optionList.size() - 1) {
                            index = 0;
                        }

                        Gdx.input.setCursorPosition((int) RoomEventDialog.optionList.get(index).hb.cX, Settings.HEIGHT - (int) RoomEventDialog.optionList.get(index).hb.cY);
                    }
                } else if (!this.event.imageEventText.optionList.isEmpty()) {
                    anyHovered = false;
                    index = 0;

                    for(var3 = this.event.imageEventText.optionList.iterator(); var3.hasNext(); ++index) {
                        o = (LargeDialogOptionButton)var3.next();
                        if (o.hb.hovered) {
                            anyHovered = true;
                            break;
                        }
                    }

                    if (!anyHovered) {
                        Gdx.input.setCursorPosition((int) this.event.imageEventText.optionList.get(0).hb.cX, Settings.HEIGHT - (int) this.event.imageEventText.optionList.get(0).hb.cY);
                    } else if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                        if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
                            --index;
                            if (index < 0) {
                                index = this.event.imageEventText.optionList.size() - 1;
                            }

                            Gdx.input.setCursorPosition((int) this.event.imageEventText.optionList.get(index).hb.cX, Settings.HEIGHT - (int) this.event.imageEventText.optionList.get(index).hb.cY);
                        }
                    } else {
                        ++index;
                        if (index > this.event.imageEventText.optionList.size() - 1) {
                            index = 0;
                        }

                        Gdx.input.setCursorPosition((int) this.event.imageEventText.optionList.get(index).hb.cX, Settings.HEIGHT - (int) this.event.imageEventText.optionList.get(index).hb.cY);
                    }
                }
            }

        }
    }

    public void addCardReward(RewardItem rewardItem) {
        if (!rewardItem.cards.isEmpty()) {
            this.rewards.add(rewardItem);
        }

    }

    public void dispose() {
        if (this.event != null) {
            this.event.dispose();
        }

        if (this.monsters != null) {

            for (AbstractMonster m : this.monsters.monsters) {
                m.dispose();
            }
        }

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("AbstractRoom");
        TEXT = uiStrings.TEXT;
        logger = SpireAndroidLogger.getLogger(AbstractRoom.class);
        blizzardPotionMod = 0;
        waitTimer = 0.0F;
    }

    public enum RoomType {
        SHOP,
        MONSTER,
        SHRINE,
        TREASURE,
        EVENT,
        BOSS;

        RoomType() {
        }
    }

    public enum RoomPhase {
        COMBAT,
        EVENT,
        COMPLETE,
        INCOMPLETE;

        RoomPhase() {
        }
    }
}

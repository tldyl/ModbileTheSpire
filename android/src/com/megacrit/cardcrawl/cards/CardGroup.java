//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.megacrit.cardcrawl.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.*;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardGroup {
    private static final Logger logger = LogManager.getLogger(CardGroup.class.getName());
    public ArrayList<AbstractCard> group = new ArrayList<>();
    public float HAND_START_X;
    public float HAND_OFFSET_X;
    private static final float HAND_HOVER_PUSH_AMT = 0.4F;
    private static final float PUSH_TAPER = 0.25F;
    private static final float TWO_CARD_PUSH_AMT = 0.2F;
    private static final float THREE_FOUR_CARD_PUSH_AMT = 0.27F;
    public static final float DRAW_PILE_X;
    public static final float DRAW_PILE_Y;
    public static final int DISCARD_PILE_X;
    public static final int DISCARD_PILE_Y = 0;
    public CardGroup.CardGroupType type;
    public HashMap<Integer, Integer> handPositioningMap;
    private ArrayList<AbstractCard> queued;
    private ArrayList<AbstractCard> inHand;

    public CardGroup(CardGroup.CardGroupType type) {
        this.HAND_START_X = (float)Settings.WIDTH * 0.36F;
        this.HAND_OFFSET_X = AbstractCard.IMG_WIDTH * 0.35F;
        this.handPositioningMap = new HashMap<>();
        this.queued = new ArrayList<>();
        this.inHand = new ArrayList<>();
        this.type = type;
    }

    public CardGroup(CardGroup g, CardGroup.CardGroupType type) {
        this.HAND_START_X = (float)Settings.WIDTH * 0.36F;
        this.HAND_OFFSET_X = AbstractCard.IMG_WIDTH * 0.35F;
        this.handPositioningMap = new HashMap<>();
        this.queued = new ArrayList<>();
        this.inHand = new ArrayList<>();
        this.type = type;
        Iterator var3 = g.group.iterator();

        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            this.group.add(c.makeSameInstanceOf());
        }

    }

    public ArrayList<CardSave> getCardDeck() {
        ArrayList<CardSave> retVal = new ArrayList<>();
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard card = (AbstractCard)var2.next();
            retVal.add(new CardSave(card.cardID, card.timesUpgraded, card.misc));
        }

        return retVal;
    }

    public ArrayList<String> getCardNames() {
        ArrayList<String> retVal = new ArrayList<>();
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard card = (AbstractCard)var2.next();
            retVal.add(card.cardID);
        }

        return retVal;
    }

    public ArrayList<String> getCardIdsForMetrics() {
        ArrayList<String> retVal = new ArrayList<>();
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard card = (AbstractCard)var2.next();
            retVal.add(card.getMetricID());
        }

        return retVal;
    }

    public void clear() {
        this.group.clear();
    }

    public boolean contains(AbstractCard c) {
        return this.group.contains(c);
    }

    public boolean canUseAnyCard() {
        Iterator var1 = this.group.iterator();

        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            c = (AbstractCard)var1.next();
        } while(!c.hasEnoughEnergy());

        return true;
    }

    public int fullSetCheck() {
        int times = 0;
        ArrayList<String> cardIDS = new ArrayList<>();

        for (AbstractCard c : this.group) {
            if (c.rarity != CardRarity.BASIC) {
                cardIDS.add(c.cardID);
            }
        }

        HashMap<String, Integer> cardCount = new HashMap<>();
        Iterator var7 = cardIDS.iterator();

        while(var7.hasNext()) {
            String cardID = (String)var7.next();
            if (cardCount.containsKey(cardID)) {
                cardCount.put(cardID, cardCount.get(cardID) + 1);
            } else {
                cardCount.put(cardID, 1);
            }
        }

        var7 = cardCount.entrySet().iterator();

        while(var7.hasNext()) {
            Entry<String, Integer> card = (Entry<String, Integer>) var7.next();
            if (card.getValue() >= 4) {
                ++times;
            }
        }

        return times;
    }

    public boolean pauperCheck() {
        Iterator var1 = this.group.iterator();

        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            c = (AbstractCard)var1.next();
        } while(c.rarity != CardRarity.RARE);

        return false;
    }

    public boolean cursedCheck() {
        int count = 0;
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.type == CardType.CURSE) {
                ++count;
            }
        }

        return count >= 5;
    }

    public boolean highlanderCheck() {
        ArrayList<String> cardIDS = new ArrayList<>();
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.rarity != CardRarity.BASIC) {
                cardIDS.add(c.cardID);
            }
        }

        Set<String> set = new HashSet<>(cardIDS);
        return set.size() >= cardIDS.size();
    }

    public void applyPowers() {
        Iterator var1 = this.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            c.applyPowers();
        }

    }

    public void removeCard(AbstractCard c) {
        this.group.remove(c);
        if (this.type == CardGroup.CardGroupType.MASTER_DECK) {
            c.onRemoveFromMasterDeck();
            Iterator var2 = AbstractDungeon.player.relics.iterator();

            while(var2.hasNext()) {
                AbstractRelic r = (AbstractRelic)var2.next();
                r.onMasterDeckChange();
            }
        }

    }

    public boolean removeCard(String targetID) {
        Iterator i = this.group.iterator();

        AbstractCard e;
        do {
            if (!i.hasNext()) {
                return false;
            }

            e = (AbstractCard)i.next();
        } while(!e.cardID.equals(targetID));

        i.remove();
        return true;
    }

    public void addToHand(AbstractCard c) {
        c.untip();
        this.group.add(c);
    }

    public void refreshHandLayout() {
        if (AbstractDungeon.getCurrRoom().monsters == null || !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            Iterator var1;
            if (AbstractDungeon.player.hasPower("Surrounded") && AbstractDungeon.getCurrRoom().monsters != null) {
                var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    if (AbstractDungeon.player.flipHorizontal) {
                        if (AbstractDungeon.player.drawX < m.drawX) {
                            m.applyPowers();
                        } else {
                            m.applyPowers();
                            m.removeSurroundedPower();
                        }
                    } else if (!AbstractDungeon.player.flipHorizontal) {
                        if (AbstractDungeon.player.drawX > m.drawX) {
                            m.applyPowers();
                        } else {
                            m.applyPowers();
                            m.removeSurroundedPower();
                        }
                    }
                }
            }

            var1 = AbstractDungeon.player.orbs.iterator();

            while(var1.hasNext()) {
                AbstractOrb o = (AbstractOrb)var1.next();
                o.hideEvokeValues();
            }

            if (AbstractDungeon.player.hand.size() + AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() <= 3 && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && AbstractDungeon.floorNum > 3) {
                UnlockTracker.unlockAchievement("PURITY");
            }

            var1 = AbstractDungeon.player.relics.iterator();

            while(var1.hasNext()) {
                AbstractRelic r = (AbstractRelic)var1.next();
                r.onRefreshHand();
            }

            float angleRange = 50.0F - (float)(10 - this.group.size()) * 5.0F;
            float incrementAngle = angleRange / (float)this.group.size();
            float sinkStart = 80.0F * Settings.scale;
            float sinkRange = 300.0F * Settings.scale;
            float incrementSink = sinkRange / (float)this.group.size() / 2.0F;
            int middle = this.group.size() / 2;

            for(int i = 0; i < this.group.size(); ++i) {
                this.group.get(i).setAngle(angleRange / 2.0F - incrementAngle * (float)i - incrementAngle / 2.0F);
                int t = i - middle;
                if (t >= 0) {
                    if (this.group.size() % 2 == 0) {
                        ++t;
                        t = -t;
                    } else {
                        t = -t;
                    }
                }

                if (this.group.size() % 2 == 0) {
                    ++t;
                }

                t = (int)((float)t * 1.7F);
                this.group.get(i).target_y = sinkStart + incrementSink * (float)t;
            }

            Iterator var14;
            AbstractCard c;
            for(var14 = this.group.iterator(); var14.hasNext(); c.targetDrawScale = 0.75F) {
                c = (AbstractCard)var14.next();
            }

            AbstractCard var10000;
            label113:
            switch(this.group.size()) {
                case 0:
                    return;
                case 1:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F;
                    break;
                case 2:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.47F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.53F;
                    break;
                case 3:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.9F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.9F;
                    var10000 = this.group.get(0);
                    var10000.target_y += 20.0F * Settings.scale;
                    var10000 = this.group.get(2);
                    var10000.target_y += 20.0F * Settings.scale;
                    break;
                case 4:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.36F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.46F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.46F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.36F;
                    var10000 = this.group.get(1);
                    var10000.target_y -= 10.0F * Settings.scale;
                    var10000 = this.group.get(2);
                    var10000.target_y -= 10.0F * Settings.scale;
                    break;
                case 5:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.7F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.9F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.9F;
                    this.group.get(4).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.7F;
                    var10000 = this.group.get(0);
                    var10000.target_y += 25.0F * Settings.scale;
                    var10000 = this.group.get(2);
                    var10000.target_y -= 10.0F * Settings.scale;
                    var10000 = this.group.get(4);
                    var10000.target_y += 25.0F * Settings.scale;
                    break;
                case 6:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.1F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.3F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.43F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.43F;
                    this.group.get(4).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.3F;
                    this.group.get(5).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.1F;
                    var10000 = this.group.get(0);
                    var10000.target_y += 10.0F * Settings.scale;
                    var10000 = this.group.get(5);
                    var10000.target_y += 10.0F * Settings.scale;
                    break;
                case 7:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.4F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.7F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.9F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F;
                    this.group.get(4).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.9F;
                    this.group.get(5).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.7F;
                    this.group.get(6).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.4F;
                    var10000 = this.group.get(0);
                    var10000.target_y += 25.0F * Settings.scale;
                    var10000 = this.group.get(1);
                    var10000.target_y += 18.0F * Settings.scale;
                    var10000 = this.group.get(3);
                    var10000.target_y -= 6.0F * Settings.scale;
                    var10000 = this.group.get(5);
                    var10000.target_y += 18.0F * Settings.scale;
                    var10000 = this.group.get(6);
                    var10000.target_y += 25.0F * Settings.scale;
                    break;
                case 8:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.5F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.82F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.1F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.38F;
                    this.group.get(4).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.38F;
                    this.group.get(5).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.1F;
                    this.group.get(6).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.77F;
                    this.group.get(7).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.5F;
                    var10000 = this.group.get(1);
                    var10000.target_y += 10.0F * Settings.scale;
                    var10000 = this.group.get(6);
                    var10000.target_y += 10.0F * Settings.scale;
                    var14 = this.group.iterator();

                    while(true) {
                        if (!var14.hasNext()) {
                            break label113;
                        }

                        c = (AbstractCard)var14.next();
                        c.targetDrawScale = 0.7125F;
                    }
                case 9:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.8F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.2F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.53F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.8F;
                    this.group.get(4).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.0F;
                    this.group.get(5).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.8F;
                    this.group.get(6).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.53F;
                    this.group.get(7).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.2F;
                    this.group.get(8).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.8F;
                    var10000 = this.group.get(1);
                    var10000.target_y += 22.0F * Settings.scale;
                    var10000 = this.group.get(2);
                    var10000.target_y += 18.0F * Settings.scale;
                    var10000 = this.group.get(3);
                    var10000.target_y += 12.0F * Settings.scale;
                    var10000 = this.group.get(5);
                    var10000.target_y += 12.0F * Settings.scale;
                    var10000 = this.group.get(6);
                    var10000.target_y += 18.0F * Settings.scale;
                    var10000 = this.group.get(7);
                    var10000.target_y += 22.0F * Settings.scale;
                    var14 = this.group.iterator();

                    while(true) {
                        if (!var14.hasNext()) {
                            break label113;
                        }

                        c = (AbstractCard)var14.next();
                        c.targetDrawScale = 0.67499995F;
                    }
                case 10:
                    this.group.get(0).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.9F;
                    this.group.get(1).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 2.4F;
                    this.group.get(2).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.8F;
                    this.group.get(3).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 1.1F;
                    this.group.get(4).target_x = (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH_S * 0.4F;
                    this.group.get(5).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 0.4F;
                    this.group.get(6).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.1F;
                    this.group.get(7).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 1.8F;
                    this.group.get(8).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.4F;
                    this.group.get(9).target_x = (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH_S * 2.9F;
                    var10000 = this.group.get(1);
                    var10000.target_y += 20.0F * Settings.scale;
                    var10000 = this.group.get(2);
                    var10000.target_y += 17.0F * Settings.scale;
                    var10000 = this.group.get(3);
                    var10000.target_y += 12.0F * Settings.scale;
                    var10000 = this.group.get(4);
                    var10000.target_y += 5.0F * Settings.scale;
                    var10000 = this.group.get(5);
                    var10000.target_y += 5.0F * Settings.scale;
                    var10000 = this.group.get(6);
                    var10000.target_y += 12.0F * Settings.scale;
                    var10000 = this.group.get(7);
                    var10000.target_y += 17.0F * Settings.scale;
                    var10000 = this.group.get(8);
                    var10000.target_y += 20.0F * Settings.scale;
                    var14 = this.group.iterator();

                    while(true) {
                        if (!var14.hasNext()) {
                            break label113;
                        }

                        c = (AbstractCard)var14.next();
                        c.targetDrawScale = 0.63750005F;
                    }
                default:
                    logger.info("WTF MATE, why so many cards");
            }

            AbstractCard card = AbstractDungeon.player.hoveredCard;
            if (card != null) {
                card.setAngle(0.0F);
                card.target_x = (card.current_x + card.target_x) / 2.0F;
                card.target_y = card.current_y;
            }

            Iterator var17 = AbstractDungeon.actionManager.cardQueue.iterator();

            while(var17.hasNext()) {
                CardQueueItem q = (CardQueueItem)var17.next();
                if (q.card != null) {
                    q.card.setAngle(0.0F);
                    q.card.target_x = q.card.current_x;
                    q.card.target_y = q.card.current_y;
                }
            }

            this.glowCheck();
        }
    }

    public void glowCheck() {
        AbstractCard c;
        for(Iterator var1 = this.group.iterator(); var1.hasNext(); c.triggerOnGlowCheck()) {
            c = (AbstractCard)var1.next();
            if (c.canUse(AbstractDungeon.player, null) && AbstractDungeon.screen != CurrentScreen.HAND_SELECT) {
                c.beginGlowing();
            } else {
                c.stopGlowing();
            }
        }

    }

    public void stopGlowing() {
        Iterator var1 = this.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            c.stopGlowing();
        }

    }

    public void hoverCardPush(AbstractCard c) {
        if (this.group.size() > 1) {
            int cardNum = -1;

            for(int i = 0; i < this.group.size(); ++i) {
                if (c.equals(this.group.get(i))) {
                    cardNum = i;
                    break;
                }
            }

            float pushAmt = 0.4F;
            if (this.group.size() == 2) {
                pushAmt = 0.2F;
            } else if (this.group.size() == 3 || this.group.size() == 4) {
                pushAmt = 0.27F;
            }

            AbstractCard var10000;
            int currentSlot;
            for(currentSlot = cardNum + 1; currentSlot < this.group.size(); ++currentSlot) {
                var10000 = this.group.get(currentSlot);
                var10000.target_x += AbstractCard.IMG_WIDTH_S * pushAmt;
                pushAmt *= 0.25F;
            }

            pushAmt = 0.4F;
            if (this.group.size() == 2) {
                pushAmt = 0.2F;
            } else if (this.group.size() == 3 || this.group.size() == 4) {
                pushAmt = 0.27F;
            }

            for(currentSlot = cardNum - 1; currentSlot > -1 && currentSlot < this.group.size(); --currentSlot) {
                var10000 = this.group.get(currentSlot);
                var10000.target_x -= AbstractCard.IMG_WIDTH_S * pushAmt;
                pushAmt *= 0.25F;
            }
        }

    }

    public void addToTop(AbstractCard c) {
        this.group.add(c);
    }

    public void addToBottom(AbstractCard c) {
        this.group.add(0, c);
    }

    public void addToRandomSpot(AbstractCard c) {
        if (this.group.size() == 0) {
            this.group.add(c);
        } else {
            this.group.add(AbstractDungeon.cardRandomRng.random(this.group.size() - 1), c);
        }

    }

    public AbstractCard getTopCard() {
        return this.group.get(this.group.size() - 1);
    }

    public AbstractCard getNCardFromTop(int num) {
        return this.group.get(this.group.size() - 1 - num);
    }

    public AbstractCard getBottomCard() {
        return this.group.get(0);
    }

    public AbstractCard getHoveredCard() {
        Iterator var1 = this.group.iterator();

        AbstractCard c;
        boolean success;
        do {
            do {
                if (!var1.hasNext()) {
                    return null;
                }

                c = (AbstractCard)var1.next();
            } while(!c.isHoveredInHand(0.7F));

            success = true;
            Iterator var4 = AbstractDungeon.actionManager.cardQueue.iterator();

            while(var4.hasNext()) {
                CardQueueItem q = (CardQueueItem)var4.next();
                if (q.card == c) {
                    success = false;
                    break;
                }
            }
        } while(!success);

        return c;
    }

    public AbstractCard getRandomCard(Random rng) {
        return this.group.get(rng.random(this.group.size() - 1));
    }

    public AbstractCard getRandomCard(boolean useRng) {
        return useRng ? this.group.get(AbstractDungeon.cardRng.random(this.group.size() - 1)) : this.group.get(MathUtils.random(this.group.size() - 1));
    }

    public AbstractCard getRandomCard(boolean useRng, CardRarity rarity) {
        ArrayList<AbstractCard> tmp = new ArrayList<>();
        Iterator var4 = this.group.iterator();

        while(var4.hasNext()) {
            AbstractCard c = (AbstractCard)var4.next();
            if (c.rarity == rarity) {
                tmp.add(c);
            }
        }

        if (tmp.isEmpty()) {
            logger.info("ERROR: No cards left for type: " + this.type.name());
            return null;
        } else {
            Collections.sort(tmp);
            if (useRng) {
                return tmp.get(AbstractDungeon.cardRng.random(tmp.size() - 1));
            } else {
                return tmp.get(MathUtils.random(tmp.size() - 1));
            }
        }
    }

    public AbstractCard getRandomCard(Random rng, CardRarity rarity) {
        ArrayList<AbstractCard> tmp = new ArrayList<>();
        Iterator var4 = this.group.iterator();

        while(var4.hasNext()) {
            AbstractCard c = (AbstractCard)var4.next();
            if (c.rarity == rarity) {
                tmp.add(c);
            }
        }

        if (tmp.isEmpty()) {
            logger.info("ERROR: No cards left for type: " + this.type.name());
            return null;
        } else {
            Collections.sort(tmp);
            return tmp.get(rng.random(tmp.size() - 1));
        }
    }

    public AbstractCard getRandomCard(CardType type, boolean useRng) {
        ArrayList<AbstractCard> tmp = new ArrayList<>();
        Iterator var4 = this.group.iterator();

        while(var4.hasNext()) {
            AbstractCard c = (AbstractCard)var4.next();
            if (c.type == type) {
                tmp.add(c);
            }
        }

        if (tmp.isEmpty()) {
            logger.info("ERROR: No cards left for type: " + type.name());
            return null;
        } else {
            Collections.sort(tmp);
            if (useRng) {
                return tmp.get(AbstractDungeon.cardRng.random(tmp.size() - 1));
            } else {
                return tmp.get(MathUtils.random(tmp.size() - 1));
            }
        }
    }

    public void removeTopCard() {
        this.group.remove(this.group.size() - 1);
    }

    public void shuffle() {
        Collections.shuffle(this.group, new java.util.Random(AbstractDungeon.shuffleRng.randomLong()));
    }

    public void shuffle(Random rng) {
        Collections.shuffle(this.group, new java.util.Random(rng.randomLong()));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            sb.append(c.cardID);
            sb.append("\n");
        }

        return sb.toString();
    }

    public void update() {
        Iterator var1 = this.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            c.update();
        }

    }

    public void updateHoverLogic() {
        Iterator var1 = this.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            c.updateHoverLogic();
        }

    }

    public void render(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.render(sb);
        }

    }

    public void renderShowBottled(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.render(sb);
            AbstractRelic tmp;
            float prevX;
            float prevY;
            if (c.inBottleFlame) {
                tmp = RelicLibrary.getRelic("Bottled Flame");
                prevX = tmp.currentX;
                prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                tmp = null;
            } else if (c.inBottleLightning) {
                tmp = RelicLibrary.getRelic("Bottled Lightning");
                prevX = tmp.currentX;
                prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                tmp = null;
            } else if (c.inBottleTornado) {
                tmp = RelicLibrary.getRelic("Bottled Tornado");
                prevX = tmp.currentX;
                prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                tmp = null;
            }
        }

    }

    public void renderMasterDeck(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.render(sb);
            AbstractRelic tmp;
            float prevX;
            float prevY;
            if (c.inBottleFlame) {
                tmp = RelicLibrary.getRelic("Bottled Flame");
                prevX = tmp.currentX;
                prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                tmp = null;
            } else if (c.inBottleLightning) {
                tmp = RelicLibrary.getRelic("Bottled Lightning");
                prevX = tmp.currentX;
                prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                tmp = null;
            } else if (c.inBottleTornado) {
                tmp = RelicLibrary.getRelic("Bottled Tornado");
                prevX = tmp.currentX;
                prevY = tmp.currentY;
                tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                tmp.scale = c.drawScale * Settings.scale * 1.5F;
                tmp.render(sb);
                tmp.currentX = prevX;
                tmp.currentY = prevY;
                tmp = null;
            }
        }

    }

    public void renderExceptOneCard(SpriteBatch sb, AbstractCard card) {
        Iterator var3 = this.group.iterator();

        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            if (!c.equals(card)) {
                c.render(sb);
            }
        }

    }

    public void renderExceptOneCardShowBottled(SpriteBatch sb, AbstractCard card) {
        Iterator var3 = this.group.iterator();

        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            if (!c.equals(card)) {
                c.render(sb);
                AbstractRelic tmp;
                float prevX;
                float prevY;
                if (c.inBottleFlame) {
                    tmp = RelicLibrary.getRelic("Bottled Flame");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    tmp = null;
                } else if (c.inBottleLightning) {
                    tmp = RelicLibrary.getRelic("Bottled Lightning");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    tmp = null;
                } else if (c.inBottleTornado) {
                    tmp = RelicLibrary.getRelic("Bottled Tornado");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    tmp = null;
                }
            }
        }

    }

    public void renderMasterDeckExceptOneCard(SpriteBatch sb, AbstractCard card) {
        Iterator var3 = this.group.iterator();

        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            if (!c.equals(card)) {
                c.render(sb);
                AbstractRelic tmp;
                float prevX;
                float prevY;
                if (c.inBottleFlame) {
                    tmp = RelicLibrary.getRelic("Bottled Flame");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    tmp = null;
                } else if (c.inBottleLightning) {
                    tmp = RelicLibrary.getRelic("Bottled Lightning");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    tmp = null;
                } else if (c.inBottleTornado) {
                    tmp = RelicLibrary.getRelic("Bottled Tornado");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = c.current_x + 390.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.currentY = c.current_y + 546.0F * c.drawScale / 3.0F * Settings.scale;
                    tmp.scale = c.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                    tmp = null;
                }
            }
        }

    }

    public void renderHand(SpriteBatch sb, AbstractCard exceptThis) {
        Iterator var3 = this.group.iterator();

        while(true) {
            AbstractCard c;
            do {
                if (!var3.hasNext()) {
                    var3 = this.inHand.iterator();

                    while(var3.hasNext()) {
                        c = (AbstractCard)var3.next();
                        c.render(sb);
                    }

                    var3 = this.queued.iterator();

                    while(var3.hasNext()) {
                        c = (AbstractCard)var3.next();
                        c.render(sb);
                    }

                    this.inHand.clear();
                    this.queued.clear();
                    return;
                }

                c = (AbstractCard)var3.next();
            } while(c == exceptThis);

            boolean inQueue = false;
            Iterator var6 = AbstractDungeon.actionManager.cardQueue.iterator();

            while(var6.hasNext()) {
                CardQueueItem i = (CardQueueItem)var6.next();
                if (i.card != null && i.card.equals(c)) {
                    this.queued.add(c);
                    inQueue = true;
                    break;
                }
            }

            if (!inQueue) {
                this.inHand.add(c);
            }
        }
    }

    public void renderInLibrary(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.renderInLibrary(sb);
        }

    }

    public void renderTip(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.renderCardTip(sb);
        }

    }

    public void renderWithSelections(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.renderWithSelections(sb);
        }

    }

    public void renderDiscardPile(SpriteBatch sb) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.render(sb);
        }

    }

    public void moveToDiscardPile(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        c.darken(false);
        AbstractDungeon.getCurrRoom().souls.discard(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
    }

    public void empower(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        AbstractDungeon.getCurrRoom().souls.empower(c);
    }

    public void moveToExhaustPile(AbstractCard c) {
        Iterator var2 = AbstractDungeon.player.relics.iterator();

        while(var2.hasNext()) {
            AbstractRelic r = (AbstractRelic)var2.next();
            r.onExhaust(c);
        }

        var2 = AbstractDungeon.player.powers.iterator();

        while(var2.hasNext()) {
            AbstractPower p = (AbstractPower)var2.next();
            p.onExhaust(c);
        }

        c.triggerOnExhaust();
        this.resetCardBeforeMoving(c);
        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        AbstractDungeon.player.exhaustPile.addToTop(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
    }

    public void moveToHand(AbstractCard c, CardGroup group) {
        c.unhover();
        c.lighten(true);
        c.setAngle(0.0F);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.current_x = DRAW_PILE_X;
        c.current_y = DRAW_PILE_Y;
        group.removeCard(c);
        AbstractDungeon.player.hand.addToTop(c);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
    }

    public void moveToHand(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.unhover();
        c.lighten(true);
        c.setAngle(0.0F);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.current_x = DRAW_PILE_X;
        c.current_y = DRAW_PILE_Y;
        AbstractDungeon.player.hand.addToTop(c);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
    }

    public void moveToDeck(AbstractCard c, boolean randomSpot) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        AbstractDungeon.getCurrRoom().souls.onToDeck(c, randomSpot);
    }

    public void moveToBottomOfDeck(AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        AbstractDungeon.getCurrRoom().souls.onToBottomOfDeck(c);
    }

    private void resetCardBeforeMoving(AbstractCard c) {
        if (AbstractDungeon.player.hoveredCard == c) {
            AbstractDungeon.player.releaseCard();
        }

        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        this.group.remove(c);
    }

    public boolean isEmpty() {
        return this.group.isEmpty();
    }

    private void discardAll(CardGroup discardPile) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.target_x = (float)DISCARD_PILE_X;
            c.target_y = 0.0F;
            discardPile.addToTop(c);
        }

        this.group.clear();
    }

    public void initializeDeck(CardGroup masterDeck) {
        this.clear();
        CardGroup copy = new CardGroup(masterDeck, CardGroup.CardGroupType.DRAW_PILE);
        copy.shuffle(AbstractDungeon.shuffleRng);
        ArrayList<AbstractCard> placeOnTop = new ArrayList();
        Iterator var4 = copy.group.iterator();

        while(true) {
            AbstractCard c;
            while(var4.hasNext()) {
                c = (AbstractCard)var4.next();
                if (c.isInnate) {
                    placeOnTop.add(c);
                } else if (!c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                    c.target_x = DRAW_PILE_X;
                    c.target_y = DRAW_PILE_Y;
                    c.current_x = DRAW_PILE_X;
                    c.current_y = DRAW_PILE_Y;
                    this.addToTop(c);
                } else {
                    placeOnTop.add(c);
                }
            }

            var4 = placeOnTop.iterator();

            while(var4.hasNext()) {
                c = (AbstractCard)var4.next();
                this.addToTop(c);
            }

            if (placeOnTop.size() > AbstractDungeon.player.masterHandSize) {
                AbstractDungeon.actionManager.addToTurnStart(new DrawCardAction(AbstractDungeon.player, placeOnTop.size() - AbstractDungeon.player.masterHandSize));
            }

            placeOnTop.clear();
            return;
        }
    }

    public int size() {
        return this.group.size();
    }

    public CardGroup getUpgradableCards() {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.canUpgrade()) {
                retVal.group.add(c);
            }
        }

        return retVal;
    }

    public Boolean hasUpgradableCards() {
        Iterator var1 = this.group.iterator();

        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            c = (AbstractCard)var1.next();
        } while(!c.canUpgrade());

        return true;
    }

    public CardGroup getPurgeableCards() {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard c : this.group) {
            if (!c.cardID.equals("Necronomicurse") && !c.cardID.equals("CurseOfTheBell") && !c.cardID.equals("AscendersBane")) {
                retVal.group.add(c);
            }
        }

        return retVal;
    }

    public AbstractCard getSpecificCard(AbstractCard targetCard) {
        return this.group.contains(targetCard) ? targetCard : null;
    }

    public void triggerOnOtherCardPlayed(AbstractCard usedCard) {
        Iterator var2 = this.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c != usedCard) {
                c.triggerOnOtherCardPlayed(usedCard);
            }
        }

        var2 = AbstractDungeon.player.powers.iterator();

        while(var2.hasNext()) {
            AbstractPower p = (AbstractPower)var2.next();
            p.onAfterCardPlayed(usedCard);
        }

    }

    private void sortWithComparator(Comparator<AbstractCard> comp, boolean ascending) {
        if (ascending) {
            this.group.sort(comp);
        } else {
            this.group.sort(Collections.reverseOrder(comp));
        }

    }

    public void sortByRarity(boolean ascending) {
        this.sortWithComparator(new CardGroup.CardRarityComparator(), ascending);
    }

    public void sortByRarityPlusStatusCardType(boolean ascending) {
        this.sortWithComparator(new CardGroup.CardRarityComparator(), ascending);
        this.sortWithComparator(new CardGroup.StatusCardsLastComparator(), true);
    }

    public void sortByType(boolean ascending) {
        this.sortWithComparator(new CardGroup.CardTypeComparator(), ascending);
    }

    public void sortByAcquisition() {
    }

    public void sortByStatus(boolean ascending) {
        this.sortWithComparator(new CardGroup.CardLockednessComparator(), ascending);
    }

    public void sortAlphabetically(boolean ascending) {
        this.sortWithComparator(new CardGroup.CardNameComparator(), ascending);
    }

    public void sortByCost(boolean ascending) {
        this.sortWithComparator(new CardGroup.CardCostComparator(), ascending);
    }

    public CardGroup getSkills() {
        return this.getCardsOfType(CardType.SKILL);
    }

    public CardGroup getAttacks() {
        return this.getCardsOfType(CardType.ATTACK);
    }

    public CardGroup getPowers() {
        return this.getCardsOfType(CardType.POWER);
    }

    public CardGroup getCardsOfType(CardType cardType) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard card : this.group) {
            if (card.type == cardType) {
                retVal.addToBottom(card);
            }
        }

        return retVal;
    }

    public CardGroup getGroupedByColor() {
        Map<String, CardGroup> colorGroupsMap = new LinkedHashMap<>();
        colorGroupsMap.put(CardColor.RED.name(), new CardGroup(CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.GREEN.name(), new CardGroup(CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.BLUE.name(), new CardGroup(CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.PURPLE.name(), new CardGroup(CardGroupType.UNSPECIFIED));

        for (CardColor cardColor : BaseMod.getColorBundleMap().keySet()) {
            colorGroupsMap.put(cardColor.name(), new CardGroup(CardGroupType.UNSPECIFIED));
        }

        colorGroupsMap.put(CardColor.COLORLESS.name(), new CardGroup(CardGroupType.UNSPECIFIED));
        colorGroupsMap.put(CardColor.CURSE.name(), new CardGroup(CardGroupType.UNSPECIFIED));

        for (AbstractCard card : this.group) {
            colorGroupsMap.get(card.color.name()).addToTop(card);
        }

        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (Entry<String, CardGroup> e : colorGroupsMap.entrySet()) {
            CardGroup group = e.getValue();
            retVal.group.addAll(group.group);
        }

        return retVal;
    }

    public AbstractCard findCardById(String id) {
        Iterator var2 = this.group.iterator();

        AbstractCard c;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            c = (AbstractCard)var2.next();
        } while(!c.cardID.equals(id));

        return c;
    }

    public static CardGroup getGroupWithoutBottledCards(CardGroup group) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var2 = group.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (!c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                retVal.addToTop(c);
            }
        }

        return retVal;
    }

    static {
        DRAW_PILE_X = (float)Settings.WIDTH * 0.04F;
        DRAW_PILE_Y = 50.0F * Settings.scale;
        DISCARD_PILE_X = (int)((float)Settings.WIDTH + AbstractCard.IMG_WIDTH_S / 2.0F + 100.0F * Settings.scale);
    }

    private class CardCostComparator implements Comparator<AbstractCard> {
        private CardCostComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.cost - c2.cost;
        }
    }

    private class CardNameComparator implements Comparator<AbstractCard> {
        private CardNameComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.name.compareTo(c2.name);
        }
    }

    private class CardLockednessComparator implements Comparator<AbstractCard> {
        private CardLockednessComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            int c1Rank = 0;
            if (UnlockTracker.isCardLocked(c1.cardID)) {
                c1Rank = 2;
            } else if (!UnlockTracker.isCardSeen(c1.cardID)) {
                c1Rank = 1;
            }

            int c2Rank = 0;
            if (UnlockTracker.isCardLocked(c2.cardID)) {
                c2Rank = 2;
            } else if (!UnlockTracker.isCardSeen(c2.cardID)) {
                c2Rank = 1;
            }

            return c1Rank - c2Rank;
        }
    }

    private class CardTypeComparator implements Comparator<AbstractCard> {
        private CardTypeComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.type.compareTo(c2.type);
        }
    }

    private class StatusCardsLastComparator implements Comparator<AbstractCard> {
        private StatusCardsLastComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            if (c1.type == CardType.STATUS && c2.type != CardType.STATUS) {
                return 1;
            } else {
                return c1.type != CardType.STATUS && c2.type == CardType.STATUS ? -1 : 0;
            }
        }
    }

    private class CardRarityComparator implements Comparator<AbstractCard> {
        private CardRarityComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.rarity.compareTo(c2.rarity);
        }
    }

    public enum CardGroupType {
        DRAW_PILE,
        MASTER_DECK,
        HAND,
        DISCARD_PILE,
        EXHAUST_PILE,
        CARD_POOL,
        UNSPECIFIED;

        CardGroupType() {
        }
    }
}

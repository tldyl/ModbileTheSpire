//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.megacrit.cardcrawl.screens.compendium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.android.mods.screens.mainMenu.ModColorTabBar;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.GameCursor.CursorType;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.CurrentTab;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.CurScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CardLibraryScreen implements TabBarListener, ScrollBarListener {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(CardLibraryScreen.class);
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private static final int CARDS_PER_LINE;
    private boolean grabbedScreen = false;
    private float grabStartY = 0.0F;
    private float currentDiffY = 0.0F;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private AbstractCard hoveredCard;
    private AbstractCard clickStartedCard;
    private ColorTabBar colorBar;
    private ModColorTabBar modColorBar;
    public MenuCancelButton button;
    private CardGroup redCards;
    private CardGroup greenCards;
    private CardGroup blueCards;
    private CardGroup purpleCards;
    private CardGroup colorlessCards;
    private CardGroup curseCards;
    private Map<AbstractCard.CardColor, CardGroup> modCards;
    private CardLibSortHeader sortHeader;
    private CardGroup visibleCards;
    private ScrollBar scrollBar;
    private CardLibraryScreen.CardLibSelectionType type;
    private Texture filterSelectionImg;
    private int selectionIndex;
    private AbstractCard controllerCard;
    private Color highlightBoxColor;

    public CardLibraryScreen() {
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.hoveredCard = null;
        this.clickStartedCard = null;
        this.button = new MenuCancelButton();
        this.redCards = new CardGroup(CardGroupType.UNSPECIFIED);
        this.greenCards = new CardGroup(CardGroupType.UNSPECIFIED);
        this.blueCards = new CardGroup(CardGroupType.UNSPECIFIED);
        this.purpleCards = new CardGroup(CardGroupType.UNSPECIFIED);
        this.colorlessCards = new CardGroup(CardGroupType.UNSPECIFIED);
        this.curseCards = new CardGroup(CardGroupType.UNSPECIFIED);
        this.modCards = new HashMap<>(); //key:CardColor value:CardGroup
        this.type = CardLibraryScreen.CardLibSelectionType.NONE;
        this.filterSelectionImg = null;
        this.selectionIndex = 0;
        this.controllerCard = null;
        this.highlightBoxColor = new Color(1.0F, 0.95F, 0.5F, 0.0F);
        drawStartX = (float)Settings.WIDTH;
        drawStartX -= (float)CARDS_PER_LINE * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartX -= (float)(CARDS_PER_LINE - 1) * Settings.CARD_VIEW_PAD_X;
        drawStartX /= 2.0F;
        drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        this.colorBar = new ColorTabBar(this);
        this.modColorBar = new ModColorTabBar();
        this.sortHeader = new CardLibSortHeader(null);
        this.scrollBar = new ScrollBar(this);
    }

    public void initialize() {
        logger.info("Initializing card library screen.");
        this.redCards.group = CardLibrary.getCardList(LibraryType.RED);
        this.greenCards.group = CardLibrary.getCardList(LibraryType.GREEN);
        this.blueCards.group = CardLibrary.getCardList(LibraryType.BLUE);
        this.purpleCards.group = CardLibrary.getCardList(LibraryType.PURPLE);
        this.colorlessCards.group = CardLibrary.getCardList(LibraryType.COLORLESS);
        this.curseCards.group = CardLibrary.getCardList(LibraryType.CURSE);
        for (Map.Entry<AbstractCard.CardColor, CardColorBundle> e : BaseMod.getColorBundleMap().entrySet()) {
            CardGroup group = new CardGroup(CardGroupType.UNSPECIFIED);
            group.group = CardLibrary.getCardList(e.getValue().libraryType);
            this.modCards.put(e.getKey(), group);
        }
        this.modColorBar.initialize(this.modCards);
        this.visibleCards = this.redCards;
        this.sortHeader.setGroup(this.visibleCards);
        this.calculateScrollBounds();
    }

    private void setLockStatus() {
        this.lockStatusHelper(this.redCards);
        this.lockStatusHelper(this.greenCards);
        this.lockStatusHelper(this.blueCards);
        this.lockStatusHelper(this.purpleCards);
        this.lockStatusHelper(this.colorlessCards);
        this.lockStatusHelper(this.curseCards);
        for (Map.Entry<AbstractCard.CardColor, CardGroup> e : this.modCards.entrySet()) {
            this.lockStatusHelper(e.getValue());
        }
    }

    private void lockStatusHelper(CardGroup group) {
        ArrayList<AbstractCard> toAdd = new ArrayList<>();
        Iterator i = group.group.iterator();

        while(i.hasNext()) {
            AbstractCard c = (AbstractCard)i.next();
            if (UnlockTracker.isCardLocked(c.cardID)) {
                AbstractCard tmp = CardLibrary.getCopy(c.cardID);
                tmp.setLocked();
                toAdd.add(tmp);
                i.remove();
            }
        }

        group.group.addAll(toAdd);
    }

    public void open() {
        this.controllerCard = null;
        if (Settings.isInfo) {
            CardLibrary.unlockAndSeeAllCards();
        }

        if (this.filterSelectionImg == null) {
            this.filterSelectionImg = ImageMaster.loadImage("images/ui/cardlibrary/selectBox.png");
        }

        this.setLockStatus();
        this.sortOnOpen();
        this.button.show(TEXT[0]);
        this.currentDiffY = this.scrollLowerBound;
        SingleCardViewPopup.isViewingUpgrade = false;
        CardCrawlGame.mainMenuScreen.screen = CurScreen.CARD_LIBRARY;
    }

    private void sortOnOpen() {
        this.sortHeader.justSorted = true;
        this.visibleCards.sortAlphabetically(true);
        this.visibleCards.sortByRarity(true);
        this.visibleCards.sortByStatus(true);

        AbstractCard c;
        for(Iterator var1 = this.visibleCards.group.iterator(); var1.hasNext(); c.targetDrawScale = 0.75F) {
            c = (AbstractCard)var1.next();
            c.drawScale = MathUtils.random(0.6F, 0.65F);
        }

    }

    public void update() {
        this.updateControllerInput();
        if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen) {
            if ((float)Gdx.input.getY() > (float)Settings.HEIGHT * 0.75F) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if ((float)Gdx.input.getY() < (float)Settings.HEIGHT * 0.25F) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }
        }

        this.colorBar.update(this.visibleCards.getBottomCard().current_y + 230.0F * Settings.yScale);
        this.modColorBar.update();
        this.sortHeader.update();
        if (this.hoveredCard != null) {
            CardCrawlGame.cursor.changeType(CursorType.INSPECT);
            if (InputHelper.justClickedLeft) {
                this.clickStartedCard = this.hoveredCard;
            }

            if (InputHelper.justReleasedClickLeft && this.clickStartedCard != null && this.hoveredCard != null || this.hoveredCard != null && CInputActionSet.select.isJustPressed()) {
                if (Settings.isControllerMode) {
                    this.clickStartedCard = this.hoveredCard;
                }

                InputHelper.justReleasedClickLeft = false;
                CardCrawlGame.cardPopup.open(this.clickStartedCard, this.visibleCards);
                this.clickStartedCard = null;
            }
        } else {
            this.clickStartedCard = null;
        }

        boolean isScrollBarScrolling = this.scrollBar.update();
        if (!CardCrawlGame.cardPopup.isOpen && !isScrollBarScrolling) {
            this.updateScrolling();
        }

        this.updateCards();
        this.button.update();
        if (this.button.hb.clicked || InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            this.button.hb.clicked = false;
            this.button.hide();
            CardCrawlGame.mainMenuScreen.panelScreen.refresh();
        }

        if (Settings.isControllerMode && this.controllerCard != null) {
            Gdx.input.setCursorPosition((int)this.controllerCard.hb.cX, (int)((float)Settings.HEIGHT - this.controllerCard.hb.cY));
        }

    }

    private void updateControllerInput() {
        if (Settings.isControllerMode) {
            this.selectionIndex = 0;
            boolean anyHovered = false;
            this.type = CardLibraryScreen.CardLibSelectionType.NONE;
            if (this.colorBar.viewUpgradeHb.hovered) {
                anyHovered = true;
                this.type = CardLibraryScreen.CardLibSelectionType.FILTERS;
                this.selectionIndex = 4;
                this.controllerCard = null;
            } else if (this.sortHeader.updateControllerInput() != null) {
                anyHovered = true;
                this.controllerCard = null;
                this.type = CardLibraryScreen.CardLibSelectionType.FILTERS;
                this.selectionIndex = this.sortHeader.getHoveredIndex();
            } else {
                for(Iterator var2 = this.visibleCards.group.iterator(); var2.hasNext(); ++this.selectionIndex) {
                    AbstractCard c = (AbstractCard)var2.next();
                    if (c.hb.hovered) {
                        anyHovered = true;
                        this.type = CardLibraryScreen.CardLibSelectionType.CARDS;
                        break;
                    }
                }
            }

            if (!anyHovered) {
                Gdx.input.setCursorPosition((int)(this.visibleCards.group.get(0)).hb.cX, Settings.HEIGHT - (int)(this.visibleCards.group.get(0)).hb.cY);
            } else {
                switch(this.type) {
                    case CARDS:
                        if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && this.visibleCards.size() > CARDS_PER_LINE) {
                            if (this.selectionIndex < CARDS_PER_LINE) {
                                Gdx.input.setCursorPosition((int)this.sortHeader.buttons[0].hb.cX, Settings.HEIGHT - (int)this.sortHeader.buttons[0].hb.cY);
                                this.controllerCard = null;
                                return;
                            }

                            this.selectionIndex -= 5;
                            Gdx.input.setCursorPosition((int)(this.visibleCards.group.get(this.selectionIndex)).hb.cX, Settings.HEIGHT - (int)(this.visibleCards.group.get(this.selectionIndex)).hb.cY);
                            this.controllerCard = this.visibleCards.group.get(this.selectionIndex);
                        } else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && this.visibleCards.size() > CARDS_PER_LINE) {
                            if (this.selectionIndex < this.visibleCards.size() - CARDS_PER_LINE) {
                                this.selectionIndex += CARDS_PER_LINE;
                            } else {
                                this.selectionIndex %= CARDS_PER_LINE;
                            }

                            Gdx.input.setCursorPosition((int)(this.visibleCards.group.get(this.selectionIndex)).hb.cX, Settings.HEIGHT - (int)(this.visibleCards.group.get(this.selectionIndex)).hb.cY);
                            this.controllerCard = this.visibleCards.group.get(this.selectionIndex);
                        } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                                if (this.selectionIndex % CARDS_PER_LINE < CARDS_PER_LINE - 1) {
                                    ++this.selectionIndex;
                                    if (this.selectionIndex > this.visibleCards.size() - 1) {
                                        this.selectionIndex -= this.visibleCards.size() % CARDS_PER_LINE;
                                    }
                                } else {
                                    this.selectionIndex -= CARDS_PER_LINE - 1;
                                    if (this.selectionIndex < 0) {
                                        this.selectionIndex = 0;
                                    }
                                }

                                Gdx.input.setCursorPosition((int)(this.visibleCards.group.get(this.selectionIndex)).hb.cX, Settings.HEIGHT - (int)(this.visibleCards.group.get(this.selectionIndex)).hb.cY);
                                this.controllerCard = this.visibleCards.group.get(this.selectionIndex);
                            }
                        } else {
                            if (this.selectionIndex % CARDS_PER_LINE > 0) {
                                --this.selectionIndex;
                            } else {
                                this.selectionIndex += CARDS_PER_LINE - 1;
                                if (this.selectionIndex > this.visibleCards.size() - 1) {
                                    this.selectionIndex = this.visibleCards.size() - 1;
                                }
                            }

                            Gdx.input.setCursorPosition((int)(this.visibleCards.group.get(this.selectionIndex)).hb.cX, Settings.HEIGHT - (int)(this.visibleCards.group.get(this.selectionIndex)).hb.cY);
                            this.controllerCard = this.visibleCards.group.get(this.selectionIndex);
                        }
                        break;
                    case FILTERS:
                        if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
                                    --this.selectionIndex;
                                    if (this.selectionIndex == -1) {
                                        Gdx.input.setCursorPosition((int)this.colorBar.viewUpgradeHb.cX, Settings.HEIGHT - (int)this.colorBar.viewUpgradeHb.cY);
                                    } else {
                                        if (this.selectionIndex > this.sortHeader.buttons.length - 1) {
                                            this.selectionIndex = this.sortHeader.buttons.length - 1;
                                        }

                                        Gdx.input.setCursorPosition((int)this.sortHeader.buttons[this.selectionIndex].hb.cX, Settings.HEIGHT - (int)this.sortHeader.buttons[this.selectionIndex].hb.cY);
                                    }
                                }
                            } else {
                                ++this.selectionIndex;
                                if (this.selectionIndex == this.sortHeader.buttons.length) {
                                    Gdx.input.setCursorPosition((int)this.colorBar.viewUpgradeHb.cX, Settings.HEIGHT - (int)this.colorBar.viewUpgradeHb.cY);
                                } else {
                                    if (this.selectionIndex > this.sortHeader.buttons.length) {
                                        this.selectionIndex = 0;
                                    }

                                    Gdx.input.setCursorPosition((int)this.sortHeader.buttons[this.selectionIndex].hb.cX, Settings.HEIGHT - (int)this.sortHeader.buttons[this.selectionIndex].hb.cY);
                                }
                            }
                        } else {
                            Gdx.input.setCursorPosition((int)(this.visibleCards.group.get(0)).hb.cX, Settings.HEIGHT - (int)(this.visibleCards.group.get(0)).hb.cY);
                        }
                    case NONE:
                }

                if (this.type == CardLibraryScreen.CardLibSelectionType.FILTERS) {
                    this.sortHeader.selectionIndex = this.selectionIndex;
                } else {
                    this.sortHeader.selectionIndex = -1;
                }

            }
        }
    }

    private void updateCards() {
        this.hoveredCard = null;
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.visibleCards.group;

        for(int i = 0; i < cards.size(); ++i) {
            int mod = i % CARDS_PER_LINE;
            if (mod == 0 && i != 0) {
                ++lineNum;
            }

            cards.get(i).target_x = drawStartX + (float)mod * padX;
            cards.get(i).target_y = drawStartY + this.currentDiffY - (float)lineNum * padY;
            cards.get(i).update();
            cards.get(i).updateHoverLogic();
            if (cards.get(i).hb.hovered) {
                this.hoveredCard = cards.get(i);
            }
        }

        if (this.sortHeader.justSorted) {
            AbstractCard c;
            for(Iterator var5 = cards.iterator(); var5.hasNext(); c.current_y = c.target_y) {
                c = (AbstractCard)var5.next();
                c.current_x = c.target_x;
            }

            this.sortHeader.justSorted = false;
        }

    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartY = (float)y - this.currentDiffY;
            }
        } else if (InputHelper.isMouseDown) {
            this.currentDiffY = (float)y - this.grabStartY;
        } else {
            this.grabbedScreen = false;
        }

        this.resetScrolling();
        this.updateBarPosition();
    }

    private void calculateScrollBounds() {
        int size = this.visibleCards.size();
        if (size > CARDS_PER_LINE * 2) {
            int scrollTmp = size / CARDS_PER_LINE - 2;
            if (size % CARDS_PER_LINE != 0) {
                ++scrollTmp;
            }

            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float)scrollTmp * padY;
        } else {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

    }

    private void resetScrolling() {
        if (this.currentDiffY < this.scrollLowerBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        } else if (this.currentDiffY > this.scrollUpperBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }

    }

    public void render(SpriteBatch sb) {
        this.scrollBar.render(sb);
        this.colorBar.render(sb, this.visibleCards.getBottomCard().current_y + 230.0F * Settings.yScale);
        this.modColorBar.render(sb);
        this.sortHeader.render(sb);
        this.renderGroup(sb, this.visibleCards);
        if (this.hoveredCard != null) {
            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.renderInLibrary(sb);
        }

        this.button.render(sb);
        if (Settings.isControllerMode) {
            this.renderControllerUi(sb);
        }

    }

    private void renderControllerUi(SpriteBatch sb) {
        sb.draw(CInputActionSet.pageLeftViewDeck.getKeyImg(), 280.0F * Settings.xScale - 32.0F, this.sortHeader.group.getBottomCard().current_y + 280.0F * Settings.yScale - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        sb.draw(CInputActionSet.pageRightViewExhaust.getKeyImg(), 1640.0F * Settings.xScale - 32.0F, this.sortHeader.group.getBottomCard().current_y + 280.0F * Settings.yScale - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        if (this.type == CardLibraryScreen.CardLibSelectionType.FILTERS && (this.selectionIndex == 4 || this.selectionIndex == 3 && Settings.removeAtoZSort)) {
            this.highlightBoxColor.a = 0.7F + MathUtils.cosDeg((float)(System.currentTimeMillis() / 2L % 360L)) / 5.0F;
            sb.setColor(this.highlightBoxColor);
            float doop = 1.0F + (1.0F + MathUtils.cosDeg((float)(System.currentTimeMillis() / 2L % 360L))) / 50.0F;
            sb.draw(this.filterSelectionImg, this.colorBar.viewUpgradeHb.cX - 100.0F, this.colorBar.viewUpgradeHb.cY - 43.0F, 100.0F, 43.0F, 200.0F, 86.0F, Settings.scale * doop * (this.colorBar.viewUpgradeHb.width / 150.0F / Settings.scale), Settings.scale * doop, 0.0F, 0, 0, 200, 86, false, false);
        }
    }

    private void renderGroup(SpriteBatch sb, CardGroup group) {
        group.renderInLibrary(sb);
        group.renderTip(sb);
    }

    public void didChangeModTab(AbstractCard.CardColor newSelection) {
        CardGroup oldSelection = this.visibleCards;
        this.visibleCards = this.modCards.get(newSelection);
        if (oldSelection != this.visibleCards) {
            this.sortHeader.setGroup(this.visibleCards);
            this.calculateScrollBounds();
        }

        this.sortHeader.justSorted = true;

        for (AbstractCard card : this.visibleCards.group) {
            card.drawScale = MathUtils.random(0.6F, 0.65F);
            card.targetDrawScale = 0.75F;
        }
    }

    public void didChangeTab(ColorTabBar tabBar, CurrentTab newSelection) {
        CardGroup oldSelection = this.visibleCards;
        switch(newSelection) {
            case RED:
                this.visibleCards = this.redCards;
                break;
            case GREEN:
                this.visibleCards = this.greenCards;
                break;
            case BLUE:
                this.visibleCards = this.blueCards;
                break;
            case PURPLE:
                this.visibleCards = this.purpleCards;
                break;
            case COLORLESS:
                this.visibleCards = this.colorlessCards;
                break;
            case CURSE:
                this.visibleCards = this.curseCards;
        }

        if (oldSelection != this.visibleCards) {
            this.sortHeader.setGroup(this.visibleCards);
            this.calculateScrollBounds();
            this.modColorBar.currentTabIndex = -1;
        }

        this.sortHeader.justSorted = true;

        for (AbstractCard card : this.visibleCards.group) {
            card.drawScale = MathUtils.random(0.6F, 0.65F);
            card.targetDrawScale = 0.75F;
        }
    }

    public void scrolledUsingBar(float newPercent) {
        this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("CardLibraryScreen");
        TEXT = uiStrings.TEXT;
        drawStartY = (float)Settings.HEIGHT * 0.66F;
        CARDS_PER_LINE = (int)((float)Settings.WIDTH / (AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X * 3.0F));
    }

    private enum CardLibSelectionType {
        NONE,
        FILTERS,
        CARDS;

        CardLibSelectionType() {
        }
    }
}


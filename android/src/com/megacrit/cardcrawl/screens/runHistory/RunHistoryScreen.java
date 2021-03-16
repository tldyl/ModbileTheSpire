package com.megacrit.cardcrawl.screens.runHistory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.GameCursor.CursorType;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.CurScreen;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import com.megacrit.cardcrawl.screens.stats.RunData;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

public class RunHistoryScreen implements DropdownMenuListener {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(RunHistoryScreen.class);
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final CardRarity[] orderedRarity;
    private static final RelicTier[] orderedRelicRarity;
    private static final boolean SHOULD_SHOW_PATH = true;
    private static final String IRONCLAD_NAME;
    private static final String SILENT_NAME;
    private static final String DEFECT_NAME;
    private static final String WATCHER_NAME;
    private static final String ALL_CHARACTERS_TEXT;
    private static final String WINS_AND_LOSSES_TEXT;
    private static final String WINS_TEXT;
    private static final String LOSSES_TEXT;
    private static final String RUN_TYPE_ALL;
    private static final String RUN_TYPE_NORMAL;
    private static final String RUN_TYPE_ASCENSION;
    private static final String RUN_TYPE_DAILY;
    private static final String RARITY_LABEL_STARTER;
    private static final String RARITY_LABEL_COMMON;
    private static final String RARITY_LABEL_UNCOMMON;
    private static final String RARITY_LABEL_RARE;
    private static final String RARITY_LABEL_SPECIAL;
    private static final String RARITY_LABEL_CURSE;
    private static final String RARITY_LABEL_BOSS;
    private static final String RARITY_LABEL_SHOP;
    private static final String RARITY_LABEL_UNKNOWN;
    private static final String COUNT_WITH_LABEL;
    private static final String LABEL_WITH_COUNT_IN_PARENS;
    private static final String SEED_LABEL;
    private static final String CUSTOM_SEED_LABEL;
    public MenuCancelButton button = new MenuCancelButton();
    private static Gson gson;
    private ArrayList<RunData> unfilteredRuns = new ArrayList<>();
    private ArrayList<RunData> filteredRuns = new ArrayList<>();
    private int runIndex = 0;
    private RunData viewedRun = null;
    public boolean screenUp = false;
    public PlayerClass currentChar = null;
    private static final float SHOW_X;
    private static final float HIDE_X;
    private float screenX;
    private float targetX;
    private RunHistoryPath runPath;
    private ModIcons modIcons;
    private CopyableTextElement seedElement;
    private CopyableTextElement secondSeedElement;
    private boolean grabbedScreen;
    private float grabStartY;
    private float scrollTargetY;
    private float scrollY;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private DropdownMenu characterFilter;
    private DropdownMenu winLossFilter;
    private DropdownMenu runTypeFilter;
    private Hitbox prevHb;
    private Hitbox nextHb;
    private ArrayList<AbstractRelic> relics;
    private ArrayList<TinyCard> cards;
    private String cardCountByRarityString;
    private String relicCountByRarityString;
    private int circletCount;
    private DropdownMenu runsDropdown;
    private static final float ARROW_SIDE_PADDING = 180.0F;
    private Hitbox currentHb;
    private static final float RELIC_SPACE;
    private Color controllerUiColor;
    AbstractRelic hoveredRelic;
    AbstractRelic clickStartedRelic;

    public RunHistoryScreen() {
        this.screenX = HIDE_X;
        this.targetX = HIDE_X;
        this.grabbedScreen = false;
        this.grabStartY = 0.0F;
        this.scrollTargetY = 0.0F;
        this.scrollY = 0.0F;
        this.scrollLowerBound = 0.0F;
        this.scrollUpperBound = 0.0F;
        this.relics = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.cardCountByRarityString = "";
        this.relicCountByRarityString = "";
        this.circletCount = 0;
        this.controllerUiColor = new Color(0.7F, 0.9F, 1.0F, 0.25F);
        this.hoveredRelic = null;
        this.clickStartedRelic = null;
        this.runPath = new RunHistoryPath();
        this.modIcons = new ModIcons();
        this.seedElement = new CopyableTextElement(FontHelper.cardDescFont_N);
        this.secondSeedElement = new CopyableTextElement(FontHelper.cardDescFont_N);
        this.prevHb = new Hitbox(110.0F * Settings.scale, 110.0F * Settings.scale);
        this.prevHb.move(180.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F);
        this.nextHb = new Hitbox(110.0F * Settings.scale, 110.0F * Settings.scale);
        this.nextHb.move((float)Settings.WIDTH - 180.0F * Settings.xScale, (float)Settings.HEIGHT / 2.0F);
    }

    public void refreshData() {
        FileHandle[] subfolders = Gdx.files.local("runs" + File.separator).list();
        this.unfilteredRuns.clear();

        for (FileHandle subFolder : subfolders) {
            switch (CardCrawlGame.saveSlot) {
                case 0:
                    if (subFolder.name().contains("0_") || subFolder.name().contains("1_") || subFolder.name().contains("2_")) {
                        continue;
                    }
                    break;
                default:
                    if (!subFolder.name().contains(CardCrawlGame.saveSlot + "_")) {
                        continue;
                    }
            }

            FileHandle[] var6 = subFolder.list();

            for (FileHandle file : var6) {
                try {
                    RunData data = gson.fromJson(file.readString(), RunData.class);
                    if (data != null && data.timestamp == null) {
                        data.timestamp = file.nameWithoutExtension();
                        String exampleDaysSinceUnixStr = "17586";
                        boolean assumeDaysSinceUnix = data.timestamp.length() == exampleDaysSinceUnixStr.length();
                        if (assumeDaysSinceUnix) {
                            try {
                                long days = Long.parseLong(data.timestamp);
                                data.timestamp = Long.toString(days * 86400L);
                            } catch (NumberFormatException var18) {
                                logger.info("Run file " + file.path() + " name is could not be parsed into a Timestamp.");
                                data = null;
                            }
                        }
                    }

                    if (data != null) {
                        try {
                            PlayerClass.valueOf(data.character_chosen);
                            this.unfilteredRuns.add(data);
                        } catch (NullPointerException | IllegalArgumentException var17) {
                            logger.info("Run file " + file.path() + " does not use a real character: " + data.character_chosen);
                        }
                    }
                } catch (JsonSyntaxException var19) {
                    logger.info("Failed to load RunData from JSON file: " + file.path());
                }
            }
        }

        if (this.unfilteredRuns.size() > 0) {
            this.unfilteredRuns.sort(RunData.orderByTimestampDesc);
            this.viewedRun = this.unfilteredRuns.get(0);
        }

        String[] charFilterOptions = new String[]{ALL_CHARACTERS_TEXT, IRONCLAD_NAME, SILENT_NAME, DEFECT_NAME, WATCHER_NAME};
        this.characterFilter = new DropdownMenu(this, charFilterOptions, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        String[] winLossFilterOptions = new String[]{WINS_AND_LOSSES_TEXT, WINS_TEXT, LOSSES_TEXT};
        this.winLossFilter = new DropdownMenu(this, winLossFilterOptions, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        String[] runTypeFilterOptions = new String[]{RUN_TYPE_ALL, RUN_TYPE_NORMAL, RUN_TYPE_ASCENSION, RUN_TYPE_DAILY};
        this.runTypeFilter = new DropdownMenu(this, runTypeFilterOptions, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
        this.resetRunsDropdown();
    }

    private void resetRunsDropdown() {
        this.filteredRuns.clear();
        boolean only_wins = this.winLossFilter.getSelectedIndex() == 1;
        boolean only_losses = this.winLossFilter.getSelectedIndex() == 2;
        boolean only_ironclad = this.characterFilter.getSelectedIndex() == 1;
        boolean only_silent = this.characterFilter.getSelectedIndex() == 2;
        boolean only_defect = this.characterFilter.getSelectedIndex() == 3;
        boolean only_watcher = this.characterFilter.getSelectedIndex() == 4;
        boolean only_normal = this.runTypeFilter.getSelectedIndex() == 1;
        boolean only_ascension = this.runTypeFilter.getSelectedIndex() == 2;
        boolean only_daily = this.runTypeFilter.getSelectedIndex() == 3;

        for (RunData data : this.unfilteredRuns) {
            boolean includeMe = true;
            if (only_wins) {
                includeMe = includeMe && data.victory;
            } else if (only_losses) {
                includeMe = includeMe && !data.victory;
            }

            String runCharacter = data.character_chosen;
            if (only_ironclad) {
                includeMe = includeMe && runCharacter.equals(PlayerClass.IRONCLAD.name());
            } else if (only_silent) {
                includeMe = includeMe && runCharacter.equals(PlayerClass.THE_SILENT.name());
            } else if (only_defect) {
                includeMe = includeMe && runCharacter.equals(PlayerClass.DEFECT.name());
            } else if (only_watcher) {
                includeMe = includeMe && runCharacter.equals(PlayerClass.WATCHER.name());
            }

            if (only_normal) {
                includeMe = includeMe && !data.is_ascension_mode && !data.is_daily;
            } else if (only_ascension) {
                includeMe = includeMe && data.is_ascension_mode;
            } else if (only_daily) {
                includeMe = includeMe && data.is_daily;
            }

            if (includeMe) {
                this.filteredRuns.add(data);
            }
        }

        ArrayList<String> options = new ArrayList<>();
        SimpleDateFormat dateFormat;
        if (Settings.language == GameLanguage.JPN) {
            dateFormat = new SimpleDateFormat(TEXT[34], Locale.JAPAN);
        } else {
            dateFormat = new SimpleDateFormat(TEXT[34]);
        }

        for (RunData run : this.filteredRuns) {
            try {
                String dateTimeStr;
                if (run.local_time != null) {
                    dateTimeStr = dateFormat.format(Metrics.timestampFormatter.parse(run.local_time));
                } else {
                    dateTimeStr = dateFormat.format(Long.valueOf(run.timestamp) * 1000L);
                }

                dateTimeStr = dateTimeStr + " - " + run.score;
                options.add(dateTimeStr);
            } catch (Exception var15) {
                logger.info(var15.getMessage());
            }
        }

        this.runsDropdown = new DropdownMenu(this, options, FontHelper.panelNameFont, Settings.CREAM_COLOR);
        this.runIndex = 0;
        if (this.filteredRuns.size() > 0) {
            this.reloadWithRunData(this.filteredRuns.get(this.runIndex));
        } else {
            this.viewedRun = null;
            this.reloadWithRunData(null);
        }

    }

    public String baseCardSuffixForCharacter(String character) {
        switch(PlayerClass.valueOf(character)) {
            case "IRONCLAD":
                return "_R";
            case "THE_SILENT":
                return "_G";
            case "DEFECT":
                return "_B";
            case "WATCHER":
                return "_W";
            default:
                return "";
        }
    }

    public void reloadWithRunData(RunData runData) {
        if (runData == null) {
            logger.info("Attempted to load Run History with 0 runs.");
        } else {
            this.scrollUpperBound = 0.0F;
            this.viewedRun = runData;
            this.reloadRelics(runData);
            this.reloadCards(runData);
            this.runPath.setRunData(runData);
            this.modIcons.setRunData(runData);

            try {
                String seedFormat;
                String seedText;
                if (this.viewedRun.special_seed != null && this.viewedRun.special_seed != 0L && !this.viewedRun.is_daily) {
                    seedFormat = SeedHelper.getString(runData.special_seed);
                    this.seedElement.setText(String.format(CUSTOM_SEED_LABEL, seedFormat), seedFormat);
                    seedText = SeedHelper.getString(Long.parseLong(runData.seed_played));
                    this.secondSeedElement.setText(String.format(SEED_LABEL, seedText), seedText);
                } else {
                    seedFormat = this.viewedRun.chose_seed ? CUSTOM_SEED_LABEL : SEED_LABEL;
                    seedText = SeedHelper.getString(Long.parseLong(runData.seed_played));
                    this.seedElement.setText(String.format(seedFormat, seedText), seedText);
                    this.secondSeedElement.setText("", "");
                }
            } catch (NumberFormatException var4) {
                this.seedElement.setText("", "");
                this.secondSeedElement.setText("", "");
            }

            this.scrollTargetY = 0.0F;
            this.resetScrolling();
            if (this.runsDropdown != null) {
                this.runsDropdown.setSelectedIndex(this.filteredRuns.indexOf(runData));
            }

        }
    }

    private void reloadRelics(RunData runData) {
        this.relics.clear();
        this.circletCount = runData.circlet_count;
        boolean circletCountSet = this.circletCount > 0;
        Hashtable<RelicTier, Integer> relicRarityCounts = new Hashtable<>();
        AbstractRelic circlet = null;
        Iterator var5 = runData.relics.iterator();

        int newCount;
        while(var5.hasNext()) {
            String relicName = (String)var5.next();

            try {
                AbstractRelic relic = RelicLibrary.getRelic(relicName).makeCopy();
                relic.isSeen = true;
                if (relic instanceof Circlet) {
                    if (relicName.equals("Circlet")) {
                        if (!circletCountSet) {
                            ++this.circletCount;
                        }

                        if (circlet == null) {
                            circlet = relic;
                            this.relics.add(relic);
                        }
                    } else {
                        logger.info("Could not find relic for: " + relicName);
                    }
                } else {
                    this.relics.add(relic);
                }

                newCount = relicRarityCounts.containsKey(relic.tier) ? relicRarityCounts.get(relic.tier) + 1 : 1;
                relicRarityCounts.put(relic.tier, newCount);
            } catch (NullPointerException var10) {
                logger.info("NPE while loading: " + relicName);
            }
        }

        if (circlet != null && this.circletCount > 1) {
            circlet.setCounter(this.circletCount);
        }

        StringBuilder bldr = new StringBuilder();
        RelicTier[] var12 = orderedRelicRarity;
        int var13 = var12.length;

        for(newCount = 0; newCount < var13; ++newCount) {
            RelicTier rarity = var12[newCount];
            if (relicRarityCounts.containsKey(rarity)) {
                if (bldr.length() > 0) {
                    bldr.append(", ");
                }

                bldr.append(String.format(COUNT_WITH_LABEL, relicRarityCounts.get(rarity), this.rarityLabel(rarity)));
            }
        }

        this.relicCountByRarityString = bldr.toString();
    }

    private void reloadCards(RunData runData) {
        Hashtable<String, AbstractCard> rawNameToCards = new Hashtable<>();
        Hashtable<AbstractCard, Integer> cardCounts = new Hashtable<>();
        Hashtable<CardRarity, Integer> cardRarityCounts = new Hashtable<>();
        CardGroup sortedMasterDeck = new CardGroup(CardGroupType.UNSPECIFIED);
        Iterator var6 = runData.master_deck.iterator();

        int value;
        while(var6.hasNext()) {
            String cardID = (String)var6.next();
            AbstractCard card;
            if (rawNameToCards.containsKey(cardID)) {
                card = rawNameToCards.get(cardID);
            } else {
                card = this.cardForName(runData, cardID);
            }

            if (card != null) {
                value = cardCounts.containsKey(card) ? cardCounts.get(card) + 1 : 1;
                cardCounts.put(card, value);
                rawNameToCards.put(cardID, card);
                int rarityCount = cardRarityCounts.containsKey(card.rarity) ? cardRarityCounts.get(card.rarity) + 1 : 1;
                cardRarityCounts.put(card.rarity, rarityCount);
            }
        }

        sortedMasterDeck.clear();
        var6 = rawNameToCards.values().iterator();

        AbstractCard card;
        while(var6.hasNext()) {
            card = (AbstractCard)var6.next();
            sortedMasterDeck.addToTop(card);
        }

        sortedMasterDeck.sortAlphabetically(true);
        sortedMasterDeck.sortByRarityPlusStatusCardType(false);
        sortedMasterDeck = sortedMasterDeck.getGroupedByColor();
        this.cards.clear();
        var6 = sortedMasterDeck.group.iterator();

        while(var6.hasNext()) {
            card = (AbstractCard)var6.next();
            this.cards.add(new TinyCard(card, cardCounts.get(card)));
        }

        StringBuilder bldr = new StringBuilder();
        CardRarity[] var13 = orderedRarity;
        int var14 = var13.length;

        for(value = 0; value < var14; ++value) {
            CardRarity rarity = var13[value];
            if (cardRarityCounts.containsKey(rarity)) {
                if (bldr.length() > 0) {
                    bldr.append(", ");
                }

                bldr.append(String.format(COUNT_WITH_LABEL, cardRarityCounts.get(rarity), this.rarityLabel(rarity)));
            }
        }

        this.cardCountByRarityString = bldr.toString();
    }

    private String rarityLabel(CardRarity rarity) {
        switch(rarity) {
            case BASIC:
                return RARITY_LABEL_STARTER;
            case SPECIAL:
                return RARITY_LABEL_SPECIAL;
            case COMMON:
                return RARITY_LABEL_COMMON;
            case UNCOMMON:
                return RARITY_LABEL_UNCOMMON;
            case RARE:
                return RARITY_LABEL_RARE;
            case CURSE:
                return RARITY_LABEL_CURSE;
            default:
                return RARITY_LABEL_UNKNOWN;
        }
    }

    private String rarityLabel(RelicTier rarity) {
        switch(rarity) {
            case STARTER:
                return RARITY_LABEL_STARTER;
            case COMMON:
                return RARITY_LABEL_COMMON;
            case UNCOMMON:
                return RARITY_LABEL_UNCOMMON;
            case RARE:
                return RARITY_LABEL_RARE;
            case SPECIAL:
                return RARITY_LABEL_SPECIAL;
            case BOSS:
                return RARITY_LABEL_BOSS;
            case SHOP:
                return RARITY_LABEL_SHOP;
            case DEPRECATED:
            default:
                return RARITY_LABEL_UNKNOWN;
        }
    }

    private void layoutTinyCards(ArrayList<TinyCard> cards, float x, float y) {
        float originX = x + this.screenPosX(60.0F);
        float originY = y - this.screenPosY(64.0F);
        float rowHeight = this.screenPosY(48.0F);
        float columnWidth = this.screenPosX(340.0F);
        int row = 0;
        int column = 0;
        TinyCard.desiredColumns = cards.size() <= 36 ? 3 : 4;
        int cardsPerColumn = cards.size() / TinyCard.desiredColumns;
        int remainderCards = cards.size() - cardsPerColumn * TinyCard.desiredColumns;
        int[] columnSizes = new int[TinyCard.desiredColumns];
        Arrays.fill(columnSizes, cardsPerColumn);

        for(int i = 0; i < remainderCards; ++i) {
            ++columnSizes[i % TinyCard.desiredColumns];
        }

        float cardY;
        for(Iterator var16 = cards.iterator(); var16.hasNext(); this.scrollUpperBound = Math.max(this.scrollUpperBound, this.scrollY - cardY + this.screenPosY(50.0F))) {
            TinyCard card = (TinyCard)var16.next();
            if (row >= columnSizes[column]) {
                row = 0;
                ++column;
            }

            cardY = originY - (float)row * rowHeight;
            card.hb.move(originX + (float)column * columnWidth + card.hb.width / 2.0F, cardY);
            if (card.col == -1) {
                card.col = column;
                card.row = row;
            }

            ++row;
        }

    }

    private AbstractCard cardForName(RunData runData, String cardID) {
        String libraryLookupName = cardID;
        if (cardID.endsWith("+")) {
            libraryLookupName = cardID.substring(0, cardID.length() - 1);
        }

        if (libraryLookupName.equals("Defend") || libraryLookupName.equals("Strike")) {
            libraryLookupName = libraryLookupName + this.baseCardSuffixForCharacter(runData.character_chosen);
        }

        AbstractCard card = CardLibrary.getCard(libraryLookupName);
        int upgrades = 0;
        if (card != null) {
            if (cardID.endsWith("+")) {
                upgrades = 1;
            }
        } else if (libraryLookupName.contains("+")) {
            String[] split = libraryLookupName.split("\\+", -1);
            libraryLookupName = split[0];
            upgrades = Integer.parseInt(split[1]);
            card = CardLibrary.getCard(libraryLookupName);
        }

        if (card == null) {
            logger.info("Could not find card named: " + cardID);
            return null;
        } else {
            card = card.makeCopy();

            for(int i = 0; i < upgrades; ++i) {
                card.upgrade();
            }

            return card;
        }
    }

    public void update() {
        this.updateControllerInput();
        if (Settings.isControllerMode && !CardCrawlGame.isPopupOpen && this.currentHb != null) {
            if ((float)Gdx.input.getY() > (float)Settings.HEIGHT * 0.8F) {
                this.scrollTargetY += Settings.SCROLL_SPEED / 2.0F;
                if (this.scrollTargetY > this.scrollUpperBound) {
                    this.scrollTargetY = this.scrollUpperBound;
                }
            } else if ((float)Gdx.input.getY() < (float)Settings.HEIGHT * 0.2F && this.scrollY > 100.0F) {
                this.scrollTargetY -= Settings.SCROLL_SPEED / 2.0F;
                if (this.scrollTargetY < this.scrollLowerBound) {
                    this.scrollTargetY = this.scrollLowerBound;
                }
            }
        }

        if (this.runsDropdown.isOpen) {
            this.runsDropdown.update();
        } else if (this.winLossFilter.isOpen) {
            this.winLossFilter.update();
        } else if (this.characterFilter.isOpen) {
            this.characterFilter.update();
        } else if (this.runTypeFilter.isOpen) {
            this.runTypeFilter.update();
        } else {
            this.runsDropdown.update();
            this.winLossFilter.update();
            this.characterFilter.update();
            this.runTypeFilter.update();
            this.button.update();
            this.updateScrolling();
            this.updateArrows();
            this.modIcons.update();
            this.runPath.update();
            if (!this.seedElement.getText().isEmpty()) {
                this.seedElement.update();
            }

            if (!this.secondSeedElement.getText().isEmpty()) {
                this.secondSeedElement.update();
            }

            if (this.button.hb.clicked || InputHelper.pressedEscape) {
                InputHelper.pressedEscape = false;
                this.hide();
            }

            this.screenX = MathHelper.uiLerpSnap(this.screenX, this.targetX);
            if (this.filteredRuns.size() != 0) {
                boolean isAPopupOpen = CardCrawlGame.cardPopup.isOpen || CardCrawlGame.relicPopup.isOpen;
                if (!isAPopupOpen) {
                    if (InputActionSet.left.isJustPressed()) {
                        this.runIndex = Math.max(0, this.runIndex - 1);
                        this.reloadWithRunData(this.filteredRuns.get(this.runIndex));
                    }

                    if (InputActionSet.right.isJustPressed()) {
                        this.runIndex = Math.min(this.runIndex + 1, this.filteredRuns.size() - 1);
                        this.reloadWithRunData(this.filteredRuns.get(this.runIndex));
                    }
                }

                this.handleRelicInteraction();
                Iterator var2 = this.cards.iterator();

                while(true) {
                    TinyCard card;
                    boolean didClick;
                    do {
                        if (!var2.hasNext()) {
                            if (Settings.isControllerMode && this.currentHb != null) {
                                CInputHelper.setCursor(this.currentHb);
                            }

                            return;
                        }

                        card = (TinyCard)var2.next();
                        didClick = card.updateDidClick();
                    } while(!didClick);

                    CardGroup cardGroup = new CardGroup(CardGroupType.UNSPECIFIED);

                    for (TinyCard addMe : this.cards) {
                        cardGroup.addToTop(addMe.card);
                    }

                    CardCrawlGame.cardPopup.open(card.card, cardGroup);
                }
            }
        }
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !this.runsDropdown.isOpen && !this.winLossFilter.isOpen && !this.characterFilter.isOpen && !this.runTypeFilter.isOpen) {
            RunHistoryScreen.InputSection section = null;
            boolean anyHovered = false;
            int index = 0;
            ArrayList<Hitbox> hbs = new ArrayList<>();
            if (!this.runsDropdown.rows.isEmpty()) {
                hbs.add(this.runsDropdown.getHitbox());
            }

            hbs.add(this.winLossFilter.getHitbox());
            hbs.add(this.characterFilter.getHitbox());
            hbs.add(this.runTypeFilter.getHitbox());

            Iterator var5;
            for(var5 = hbs.iterator(); var5.hasNext(); ++index) {
                Hitbox hb = (Hitbox)var5.next();
                if (hb.hovered) {
                    section = RunHistoryScreen.InputSection.DROPDOWN;
                    anyHovered = true;
                    break;
                }
            }

            if (!anyHovered) {
                index = 0;

                for(var5 = this.runPath.pathElements.iterator(); var5.hasNext(); ++index) {
                    RunPathElement e = (RunPathElement)var5.next();
                    if (e.hb.hovered) {
                        section = RunHistoryScreen.InputSection.ROOM;
                        anyHovered = true;
                        break;
                    }
                }
            }

            if (!anyHovered) {
                index = 0;

                for(var5 = this.relics.iterator(); var5.hasNext(); ++index) {
                    AbstractRelic r = (AbstractRelic)var5.next();
                    if (r.hb.hovered) {
                        section = RunHistoryScreen.InputSection.RELIC;
                        anyHovered = true;
                        break;
                    }
                }
            }

            if (!anyHovered) {
                index = 0;

                for(var5 = this.cards.iterator(); var5.hasNext(); ++index) {
                    TinyCard card = (TinyCard)var5.next();
                    if (card.hb.hovered) {
                        section = RunHistoryScreen.InputSection.CARD;
                        anyHovered = true;
                        break;
                    }
                }
            }

            if (!anyHovered) {
                CInputHelper.setCursor(hbs.get(0));
                this.currentHb = hbs.get(0);
                this.scrollTargetY = 0.0F;
            } else {
                int i;
                int c;
                int r;
                boolean foundNode;
                switch(section) {
                    case DROPDOWN:
                        if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                            if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                                if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                                    if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                                        if (index == 1) {
                                            CInputHelper.setCursor(hbs.get(0));
                                            this.currentHb = hbs.get(0);
                                            this.scrollTargetY = 0.0F;
                                        } else if (index > 1) {
                                            if (!this.runPath.pathElements.isEmpty()) {
                                                CInputHelper.setCursor(this.runPath.pathElements.get(0).hb);
                                                this.currentHb = this.runPath.pathElements.get(0).hb;
                                            } else if (!this.relics.isEmpty()) {
                                                CInputHelper.setCursor(this.relics.get(0).hb);
                                                this.currentHb = this.relics.get(0).hb;
                                            } else if (!this.cards.isEmpty()) {
                                                CInputHelper.setCursor(this.cards.get(0).hb);
                                                this.currentHb = this.cards.get(0).hb;
                                            }
                                        }
                                    }
                                } else if (index == 0) {
                                    CInputHelper.setCursor(hbs.get(1));
                                    this.currentHb = hbs.get(1);
                                }
                            } else {
                                ++index;
                                if (hbs.size() == 4) {
                                    if (index <= hbs.size() - 1 && index != 1) {
                                        CInputHelper.setCursor(hbs.get(index));
                                        this.currentHb = hbs.get(index);
                                    } else if (!this.runPath.pathElements.isEmpty()) {
                                        CInputHelper.setCursor(this.runPath.pathElements.get(0).hb);
                                        this.currentHb = this.runPath.pathElements.get(0).hb;
                                    } else if (!this.relics.isEmpty()) {
                                        CInputHelper.setCursor(this.relics.get(0).hb);
                                        this.currentHb = this.relics.get(0).hb;
                                    } else {
                                        CInputHelper.setCursor(this.cards.get(0).hb);
                                        this.currentHb = this.cards.get(0).hb;
                                    }
                                } else {
                                    if (index > hbs.size() - 1) {
                                        index = 0;
                                    }

                                    CInputHelper.setCursor(hbs.get(index));
                                    this.currentHb = hbs.get(index);
                                }
                            }
                        } else {
                            --index;
                            if (index != -1) {
                                CInputHelper.setCursor(hbs.get(index));
                                this.currentHb = hbs.get(index);
                            }
                        }
                        break;
                    case ROOM:
                        if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                            if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                                if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                                    if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                                        ++index;
                                        if (index > this.runPath.pathElements.size() - 1) {
                                            if (!this.relics.isEmpty()) {
                                                CInputHelper.setCursor(this.relics.get(0).hb);
                                                this.currentHb = this.relics.get(0).hb;
                                            } else {
                                                CInputHelper.setCursor(this.cards.get(0).hb);
                                                this.currentHb = this.cards.get(0).hb;
                                            }
                                        } else {
                                            CInputHelper.setCursor(this.runPath.pathElements.get(index).hb);
                                            this.currentHb = this.runPath.pathElements.get(index).hb;
                                        }
                                    }
                                } else {
                                    --index;
                                    if (index < 0) {
                                        if (hbs.size() > 3) {
                                            CInputHelper.setCursor(hbs.get(3));
                                            this.currentHb = hbs.get(3);
                                        }
                                    } else {
                                        CInputHelper.setCursor(this.runPath.pathElements.get(index).hb);
                                        this.currentHb = this.runPath.pathElements.get(index).hb;
                                    }
                                }
                            } else {
                                c = this.runPath.pathElements.get(index).col;
                                r = this.runPath.pathElements.get(index).row + 1;
                                if (r > this.runPath.pathElements.get(this.runPath.pathElements.size() - 1).row) {
                                    if (!this.relics.isEmpty()) {
                                        CInputHelper.setCursor(this.relics.get(0).hb);
                                        this.currentHb = this.relics.get(0).hb;
                                    } else {
                                        CInputHelper.setCursor(this.cards.get(0).hb);
                                        this.currentHb = this.cards.get(0).hb;
                                    }
                                } else {
                                    foundNode = false;

                                    for(i = this.runPath.pathElements.size() - 1; i > 0; --i) {
                                        if (this.runPath.pathElements.get(i).row == r && this.runPath.pathElements.get(i).col == c) {
                                            CInputHelper.setCursor(this.runPath.pathElements.get(i).hb);
                                            this.currentHb = this.runPath.pathElements.get(i).hb;
                                            foundNode = true;
                                            break;
                                        }
                                    }

                                    if (!foundNode) {
                                        for(i = this.runPath.pathElements.size() - 1; i > 0; --i) {
                                            if (this.runPath.pathElements.get(i).row == r) {
                                                CInputHelper.setCursor(this.runPath.pathElements.get(i).hb);
                                                this.currentHb = this.runPath.pathElements.get(i).hb;
                                                foundNode = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!foundNode) {
                                        if (!this.relics.isEmpty()) {
                                            CInputHelper.setCursor(this.relics.get(0).hb);
                                            this.currentHb = this.relics.get(0).hb;
                                        } else {
                                            CInputHelper.setCursor(this.cards.get(0).hb);
                                            this.currentHb = this.cards.get(0).hb;
                                        }
                                    }
                                }
                            }
                        } else {
                            c = this.runPath.pathElements.get(index).col;
                            r = this.runPath.pathElements.get(index).row - 1;
                            if (r < 0) {
                                CInputHelper.setCursor(hbs.get(0));
                                this.currentHb = hbs.get(0);
                                this.scrollTargetY = 0.0F;
                            } else {
                                foundNode = false;

                                for(i = 0; i < this.runPath.pathElements.size(); ++i) {
                                    if (this.runPath.pathElements.get(i).row == r && this.runPath.pathElements.get(i).col == c) {
                                        CInputHelper.setCursor(this.runPath.pathElements.get(i).hb);
                                        this.currentHb = this.runPath.pathElements.get(i).hb;
                                        foundNode = true;
                                        break;
                                    }
                                }

                                if (!foundNode) {
                                    for(i = this.runPath.pathElements.size() - 1; i > 0; --i) {
                                        if (this.runPath.pathElements.get(i).row == r) {
                                            CInputHelper.setCursor(this.runPath.pathElements.get(i).hb);
                                            this.currentHb = this.runPath.pathElements.get(i).hb;
                                            foundNode = true;
                                            break;
                                        }
                                    }
                                }

                                if (!foundNode) {
                                    CInputHelper.setCursor(hbs.get(0));
                                    this.currentHb = hbs.get(0);
                                    this.scrollTargetY = 0.0F;
                                }
                            }
                        }
                        break;
                    case RELIC:
                        if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                            if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                                if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                                    if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                        if (CInputActionSet.select.isJustPressed()) {
                                            CardCrawlGame.relicPopup.open(this.relics.get(index), this.relics);
                                        }
                                    } else {
                                        ++index;
                                        if (index > this.relics.size() - 1) {
                                            if (!this.cards.isEmpty()) {
                                                CInputHelper.setCursor(this.cards.get(0).hb);
                                                this.currentHb = this.cards.get(0).hb;
                                            }
                                        } else if (!this.relics.isEmpty()) {
                                            CInputHelper.setCursor(this.relics.get(index).hb);
                                            this.currentHb = this.relics.get(index).hb;
                                        }
                                    }
                                } else {
                                    --index;
                                    if (index < 0) {
                                        if (!this.cards.isEmpty()) {
                                            CInputHelper.setCursor(this.cards.get(0).hb);
                                            this.currentHb = this.cards.get(0).hb;
                                        }
                                    } else if (!this.relics.isEmpty()) {
                                        CInputHelper.setCursor(this.relics.get(index).hb);
                                        this.currentHb = this.relics.get(index).hb;
                                    }
                                }
                            } else {
                                index += 15;
                                if (index > this.relics.size() - 1) {
                                    if (!this.cards.isEmpty()) {
                                        CInputHelper.setCursor(this.cards.get(0).hb);
                                        this.currentHb = this.cards.get(0).hb;
                                    }
                                } else {
                                    CInputHelper.setCursor(this.relics.get(index).hb);
                                    this.currentHb = this.relics.get(index).hb;
                                }
                            }
                        } else {
                            index -= 15;
                            if (index < 0) {
                                if (!this.runPath.pathElements.isEmpty()) {
                                    CInputHelper.setCursor(this.runPath.pathElements.get(0).hb);
                                    this.currentHb = this.runPath.pathElements.get(0).hb;
                                } else {
                                    CInputHelper.setCursor(hbs.get(0));
                                    this.currentHb = hbs.get(0);
                                    this.scrollTargetY = 0.0F;
                                }
                            } else {
                                CInputHelper.setCursor(this.relics.get(index).hb);
                                this.currentHb = this.relics.get(index).hb;
                            }
                        }
                        break;
                    case CARD:
                        c = this.cards.get(index).col;
                        r = this.cards.get(index).row;
                        if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                                    if (!CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                                        if (CInputActionSet.select.isJustPressed()) {
                                            CardGroup cardGroup = new CardGroup(CardGroupType.UNSPECIFIED);

                                            for (TinyCard addMe : this.cards) {
                                                cardGroup.addToTop(addMe.card);
                                            }

                                            CardCrawlGame.cardPopup.open(this.cards.get(index).card, cardGroup);
                                        }
                                    } else {
                                        ++index;
                                        if (index <= this.cards.size() - 1) {
                                            CInputHelper.setCursor(this.cards.get(index).hb);
                                            this.currentHb = this.cards.get(index).hb;
                                        }
                                    }
                                } else {
                                    --index;
                                    if (index < 0) {
                                        if (!this.relics.isEmpty()) {
                                            CInputHelper.setCursor(this.relics.get(this.relics.size() - 1).hb);
                                            this.currentHb = this.relics.get(this.relics.size() - 1).hb;
                                        } else if (!this.runPath.pathElements.isEmpty()) {
                                            CInputHelper.setCursor(this.runPath.pathElements.get(0).hb);
                                            this.currentHb = this.runPath.pathElements.get(0).hb;
                                        } else {
                                            CInputHelper.setCursor(this.runsDropdown.getHitbox());
                                            this.currentHb = this.runsDropdown.getHitbox();
                                        }
                                    } else {
                                        CInputHelper.setCursor(this.cards.get(index).hb);
                                        this.currentHb = this.cards.get(index).hb;
                                    }
                                }
                            } else {
                                ++c;
                                if (c <= TinyCard.desiredColumns - 1) {
                                    foundNode = false;

                                    for(i = 0; i < this.cards.size(); ++i) {
                                        if (this.cards.get(i).col == c && this.cards.get(i).row == r) {
                                            CInputHelper.setCursor(this.cards.get(i).hb);
                                            this.currentHb = this.cards.get(i).hb;
                                            foundNode = true;
                                            break;
                                        }
                                    }

                                    if (!foundNode) {
                                        c = 0;

                                        for(i = 0; i < this.cards.size(); ++i) {
                                            if (this.cards.get(i).col == c && this.cards.get(i).row == r) {
                                                CInputHelper.setCursor(this.cards.get(i).hb);
                                                this.currentHb = this.cards.get(i).hb;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            --c;
                            if (c < 0) {
                                for(i = this.cards.size() - 1; i > 0; --i) {
                                    if ((this.cards.get(i).col == TinyCard.desiredColumns - 1 || this.cards.get(i).col == 1) && this.cards.get(i).row == r) {
                                        CInputHelper.setCursor(this.cards.get(i).hb);
                                        this.currentHb = this.cards.get(i).hb;
                                        break;
                                    }
                                }
                            } else {
                                foundNode = false;

                                for(i = 0; i < this.cards.size(); ++i) {
                                    if (this.cards.get(i).col == c && this.cards.get(i).row == r) {
                                        CInputHelper.setCursor(this.cards.get(i).hb);
                                        this.currentHb = this.cards.get(i).hb;
                                        foundNode = true;
                                        break;
                                    }
                                }

                                if (!foundNode) {
                                    if (!this.relics.isEmpty()) {
                                        CInputHelper.setCursor(this.relics.get(0).hb);
                                        this.currentHb = this.relics.get(0).hb;
                                    } else {
                                        CInputHelper.setCursor(this.runsDropdown.getHitbox());
                                        this.currentHb = this.runsDropdown.getHitbox();
                                    }
                                }
                            }
                        }
                }
            }

        }
    }

    public void open() {
        this.currentHb = null;
        CardCrawlGame.mainMenuScreen.screen = CurScreen.RUN_HISTORY;
        SingleCardViewPopup.enableUpgradeToggle = false;
        this.refreshData();
        this.targetX = SHOW_X;
        this.button.show(TEXT[3]);
        this.screenUp = true;
        this.scrollY = this.scrollLowerBound;
        this.scrollTargetY = this.scrollLowerBound;
    }

    public void hide() {
        this.targetX = HIDE_X;
        this.button.hide();
        this.screenUp = false;
        this.currentChar = null;
        CardCrawlGame.mainMenuScreen.panelScreen.refresh();
        SingleCardViewPopup.enableUpgradeToggle = true;
    }

    public void render(SpriteBatch sb) {
        this.renderRunHistoryScreen(sb);
        this.renderArrows(sb);
        this.renderFilters(sb);
        this.button.render(sb);
        this.renderControllerUi(sb, this.currentHb);
    }

    private void renderControllerUi(SpriteBatch sb, Hitbox hb) {
        if (Settings.isControllerMode && hb != null) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.controllerUiColor);
            sb.draw(ImageMaster.CONTROLLER_HB_HIGHLIGHT, hb.cX - hb.width / 2.0F, hb.cY - hb.height / 2.0F, hb.width, hb.height);
            sb.setBlendFunction(770, 771);
        }

    }

    private String characterText(String chosenCharacter) {
        if (chosenCharacter.equals(PlayerClass.IRONCLAD.name())) {
            return IRONCLAD_NAME;
        } else if (chosenCharacter.equals(PlayerClass.THE_SILENT.name())) {
            return SILENT_NAME;
        } else if (chosenCharacter.equals(PlayerClass.DEFECT.name())) {
            return DEFECT_NAME;
        } else {
            return chosenCharacter.equals(PlayerClass.WATCHER.name()) ? WATCHER_NAME : chosenCharacter;
        }
    }

    private void renderArrows(SpriteBatch sb) {
        if (this.runIndex < this.filteredRuns.size() - 1) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, true, false);
            if (this.nextHb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, true, false);
                sb.setBlendFunction(770, 771);
            }

            if (Settings.isControllerMode) {
                sb.draw(CInputActionSet.pageRightViewExhaust.getKeyImg(), this.nextHb.cX - 32.0F, this.nextHb.cY - 32.0F - 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            this.nextHb.render(sb);
        }

        if (this.runIndex > 0) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, false, false);
            if (this.prevHb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }

            if (Settings.isControllerMode) {
                sb.draw(CInputActionSet.pageLeftViewDeck.getKeyImg(), this.prevHb.cX - 32.0F, this.prevHb.cY - 32.0F - 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            this.prevHb.render(sb);
        }

    }

    private void renderRunHistoryScreen(SpriteBatch sb) {
        float TOP_POSITION = 1020.0F;
        if (this.viewedRun == null) {
            FontHelper.renderSmartText(sb, FontHelper.panelNameFont, TEXT[4], (float)Settings.WIDTH * 0.43F, (float)Settings.HEIGHT * 0.53F, Settings.GOLD_COLOR);
        } else {
            float header1x = this.screenX + this.screenPosX(100.0F);
            float header2x = this.screenX + this.screenPosX(120.0F);
            float yOffset = this.scrollY + this.screenPosY(TOP_POSITION);
            String characterName = this.characterText(this.viewedRun.character_chosen);
            this.renderHeader1(sb, characterName, header1x, yOffset);
            float approxCharNameWidth = this.approximateHeader1Width(characterName);
            if (!this.seedElement.getText().isEmpty()) {
                this.seedElement.render(sb, this.screenX + 1200.0F * Settings.scale, yOffset);
                if (!this.secondSeedElement.getText().isEmpty()) {
                    this.secondSeedElement.render(sb, 1200.0F * Settings.scale, yOffset - this.screenPosY(36.0F));
                }
            }

            yOffset -= this.screenPosY(50.0F);
            String specialModeText = "";
            if (this.viewedRun.is_daily) {
                specialModeText = " (" + TEXT[27] + ")";
            } else if (this.viewedRun.is_ascension_mode) {
                specialModeText = " (" + TEXT[5] + this.viewedRun.ascension_level + ")";
            }

            String resultText;
            Color resultTextColor;
            if (this.viewedRun.victory) {
                resultText = TEXT[8] + specialModeText;
                resultTextColor = Settings.GREEN_TEXT_COLOR;
            } else {
                resultTextColor = Settings.RED_TEXT_COLOR;
                if (this.viewedRun.killed_by == null) {
                    resultText = String.format(TEXT[7], this.viewedRun.floor_reached) + specialModeText;
                } else {
                    resultText = String.format(TEXT[6], this.viewedRun.floor_reached, MonsterHelper.getEncounterName(this.viewedRun.killed_by)) + specialModeText;
                }
            }

            this.renderSubHeading(sb, resultText, header1x, yOffset, resultTextColor);
            if (this.viewedRun.victory) {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.TIMER_ICON, header1x + this.approximateSubHeaderWidth(resultText) - 32.0F + 54.0F * Settings.scale, yOffset - 32.0F - 10.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                this.renderSubHeading(sb, CharStat.formatHMSM((float)this.viewedRun.playtime), header1x + this.approximateSubHeaderWidth(resultText) + 80.0F * Settings.scale, yOffset, Settings.CREAM_COLOR);
            }

            yOffset -= this.screenPosY(40.0F);
            String scoreText = String.format(TEXT[22], this.viewedRun.score);
            this.renderSubHeading(sb, scoreText, header1x, yOffset, Settings.GOLD_COLOR);
            float scoreLineXOffset = header1x + this.approximateSubHeaderWidth(scoreText);
            if (this.modIcons.hasMods()) {
                this.modIcons.renderDailyMods(sb, scoreLineXOffset, yOffset);
                //float var10000 = scoreLineXOffset + this.modIcons.approximateWidth();
            }

            yOffset -= this.screenPosY(18.0F);
            this.runPath.render(sb, header2x + 52.0F * Settings.scale, yOffset);
            yOffset -= this.runPath.approximateHeight();
            yOffset -= this.screenPosY(35.0F);
            float relicBottom = this.renderRelics(sb, header2x, yOffset);
            yOffset = relicBottom - this.screenPosY(70.0F);
            this.renderDeck(sb, header2x, yOffset);
            this.runsDropdown.render(sb, header1x + approxCharNameWidth + this.screenPosX(30.0F), this.scrollY + this.screenPosY(TOP_POSITION + 6.0F));
        }
    }

    private void renderHeader1(SpriteBatch sb, String text, float x, float y) {
        FontHelper.renderSmartText(sb, FontHelper.charTitleFont, text, x, y, 9999.0F, 36.0F * Settings.scale, Settings.GOLD_COLOR);
    }

    private float approximateHeader1Width(String text) {
        return FontHelper.getSmartWidth(FontHelper.charTitleFont, text, 9999.0F, 36.0F * Settings.scale);
    }

    private float approximateSubHeaderWidth(String text) {
        return FontHelper.getSmartWidth(FontHelper.buttonLabelFont, text, 9999.0F, 36.0F * Settings.scale);
    }

    private void renderSubHeading(SpriteBatch sb, String text, float x, float y, Color color) {
        FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, text, x, y, 9999.0F, 36.0F * Settings.scale, color);
    }

    private void renderSubHeadingWithMessage(SpriteBatch sb, String main, String description, float x, float y) {
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.buttonLabelFont, main, x, y, Settings.GOLD_COLOR);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.cardDescFont_N, description, x + FontHelper.getSmartWidth(FontHelper.buttonLabelFont, main, 99999.0F, 0.0F), y - 4.0F * Settings.scale, Settings.CREAM_COLOR);
    }

    private void renderDeck(SpriteBatch sb, float x, float y) {
        this.layoutTinyCards(this.cards, this.screenX + this.screenPosX(90.0F), y);
        int cardCount = 0;

        TinyCard card;
        for(Iterator var5 = this.cards.iterator(); var5.hasNext(); cardCount += card.count) {
            card = (TinyCard)var5.next();
            card.render(sb);
        }

        String mainText = String.format(LABEL_WITH_COUNT_IN_PARENS, TEXT[9], cardCount);
        this.renderSubHeadingWithMessage(sb, mainText, this.cardCountByRarityString, x, y);
    }

    private float renderRelics(SpriteBatch sb, float x, float y) {
        String mainText = String.format(LABEL_WITH_COUNT_IN_PARENS, TEXT[10], this.relics.size());
        this.renderSubHeadingWithMessage(sb, mainText, this.relicCountByRarityString, x, y);
        int col = 0;
        int row = 0;
        float relicStartX = x + this.screenPosX(30.0F) + RELIC_SPACE / 2.0F;
        float relicStartY = y - RELIC_SPACE - this.screenPosY(10.0F);

        for(Iterator var9 = this.relics.iterator(); var9.hasNext(); ++col) {
            AbstractRelic r = (AbstractRelic)var9.next();
            if (col == 15) {
                col = 0;
                ++row;
            }

            r.currentX = relicStartX + RELIC_SPACE * (float)col;
            r.currentY = relicStartY - RELIC_SPACE * (float)row;
            r.hb.move(r.currentX, r.currentY);
            r.render(sb, false, Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
        }

        return relicStartY - RELIC_SPACE * (float)row;
    }

    private void handleRelicInteraction() {

        for (AbstractRelic r : this.relics) {
            boolean wasScreenUp = AbstractDungeon.isScreenUp;
            AbstractDungeon.isScreenUp = true;
            r.update();
            AbstractDungeon.isScreenUp = wasScreenUp;
            if (r.hb.hovered) {
                this.hoveredRelic = r;
            }
        }

        if (this.hoveredRelic != null) {
            CardCrawlGame.cursor.changeType(CursorType.INSPECT);
            if (InputHelper.justClickedLeft) {
                this.clickStartedRelic = this.hoveredRelic;
            }

            if (InputHelper.justReleasedClickLeft && this.hoveredRelic == this.clickStartedRelic) {
                CardCrawlGame.relicPopup.open(this.hoveredRelic, this.relics);
                this.clickStartedRelic = null;
            }
        } else {
            this.clickStartedRelic = null;
        }

        this.hoveredRelic = null;
    }

    private float screenPos(float val) {
        return val * Settings.scale;
    }

    private float screenPosX(float val) {
        return val * Settings.xScale;
    }

    private float screenPosY(float val) {
        return val * Settings.yScale;
    }

    private void updateArrows() {
        if (this.runIndex < this.filteredRuns.size() - 1) {
            this.nextHb.update();
            if (this.nextHb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            } else if (this.nextHb.hovered && InputHelper.justClickedLeft) {
                this.nextHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            } else if (this.nextHb.clicked || CInputActionSet.pageRightViewExhaust.isJustPressed() && !CardCrawlGame.isPopupOpen) {
                this.nextHb.clicked = false;
                this.runIndex = Math.min(this.runIndex + 1, this.filteredRuns.size() - 1);
                this.reloadWithRunData(this.filteredRuns.get(this.runIndex));
            }
        }

        if (this.runIndex > 0) {
            this.prevHb.update();
            if (this.prevHb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            } else if (this.prevHb.hovered && InputHelper.justClickedLeft) {
                this.prevHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            } else if (this.prevHb.clicked || CInputActionSet.pageLeftViewDeck.isJustPressed() && !CardCrawlGame.isPopupOpen) {
                this.prevHb.clicked = false;
                this.runIndex = Math.max(0, this.runIndex - 1);
                this.reloadWithRunData(this.filteredRuns.get(this.runIndex));
            }
        }

    }

    private void renderFilters(SpriteBatch sb) {
        float filterX = this.screenX + this.screenPosX(-270.0F);
        float winLossY = this.scrollY + this.screenPosY(1000.0F);
        float charY = winLossY - this.screenPosY(54.0F);
        float runTypeY = charY - this.screenPosY(54.0F);
        this.runTypeFilter.render(sb, filterX, runTypeY);
        this.characterFilter.render(sb, filterX, charY);
        this.winLossFilter.render(sb, filterX, winLossY);
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (this.scrollUpperBound > 0.0F) {
            if (!this.grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    this.scrollTargetY += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.scrollTargetY -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    this.grabbedScreen = true;
                    this.grabStartY = (float)y - this.scrollTargetY;
                }
            } else if (InputHelper.isMouseDown) {
                this.scrollTargetY = (float)y - this.grabStartY;
            } else {
                this.grabbedScreen = false;
            }
        }

        this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.scrollTargetY);
        this.resetScrolling();
    }

    private void resetScrolling() {
        if (this.scrollTargetY < this.scrollLowerBound) {
            this.scrollTargetY = MathHelper.scrollSnapLerpSpeed(this.scrollTargetY, this.scrollLowerBound);
        } else if (this.scrollTargetY > this.scrollUpperBound) {
            this.scrollTargetY = MathHelper.scrollSnapLerpSpeed(this.scrollTargetY, this.scrollUpperBound);
        }

    }

    public void changedSelectionTo(DropdownMenu dropdownMenu, int index, String optionText) {
        if (dropdownMenu == this.runsDropdown) {
            this.runDropdownChangedTo(index);
        } else if (this.isFilterDropdown(dropdownMenu)) {
            this.resetRunsDropdown();
        }

    }

    private boolean isFilterDropdown(DropdownMenu dropdownMenu) {
        return dropdownMenu == this.winLossFilter || dropdownMenu == this.characterFilter || dropdownMenu == this.runTypeFilter;
    }

    private void runDropdownChangedTo(int index) {
        if (this.runIndex != index) {
            this.runIndex = index;
            this.reloadWithRunData(this.filteredRuns.get(this.runIndex));
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RunHistoryScreen");
        TEXT = uiStrings.TEXT;
        orderedRarity = new CardRarity[]{CardRarity.SPECIAL, CardRarity.RARE, CardRarity.UNCOMMON, CardRarity.COMMON, CardRarity.BASIC, CardRarity.CURSE};
        orderedRelicRarity = new RelicTier[]{RelicTier.BOSS, RelicTier.SPECIAL, RelicTier.RARE, RelicTier.SHOP, RelicTier.UNCOMMON, RelicTier.COMMON, RelicTier.STARTER, RelicTier.DEPRECATED};
        IRONCLAD_NAME = TEXT[0];
        SILENT_NAME = TEXT[1];
        DEFECT_NAME = TEXT[2];
        WATCHER_NAME = TEXT[35];
        ALL_CHARACTERS_TEXT = TEXT[23];
        WINS_AND_LOSSES_TEXT = TEXT[24];
        WINS_TEXT = TEXT[25];
        LOSSES_TEXT = TEXT[26];
        RUN_TYPE_ALL = TEXT[28];
        RUN_TYPE_NORMAL = TEXT[29];
        RUN_TYPE_ASCENSION = TEXT[30];
        RUN_TYPE_DAILY = TEXT[31];
        RARITY_LABEL_STARTER = TEXT[11];
        RARITY_LABEL_COMMON = TEXT[12];
        RARITY_LABEL_UNCOMMON = TEXT[13];
        RARITY_LABEL_RARE = TEXT[14];
        RARITY_LABEL_SPECIAL = TEXT[15];
        RARITY_LABEL_CURSE = TEXT[16];
        RARITY_LABEL_BOSS = TEXT[17];
        RARITY_LABEL_SHOP = TEXT[18];
        RARITY_LABEL_UNKNOWN = TEXT[19];
        COUNT_WITH_LABEL = TEXT[20];
        LABEL_WITH_COUNT_IN_PARENS = TEXT[21];
        SEED_LABEL = TEXT[32];
        CUSTOM_SEED_LABEL = TEXT[33];
        gson = new Gson();
        SHOW_X = 300.0F * Settings.xScale;
        HIDE_X = -800.0F * Settings.xScale;
        RELIC_SPACE = 64.0F * Settings.scale;
    }

    private enum InputSection {
        DROPDOWN,
        ROOM,
        RELIC,
        CARD;

        InputSection() {
        }
    }
}

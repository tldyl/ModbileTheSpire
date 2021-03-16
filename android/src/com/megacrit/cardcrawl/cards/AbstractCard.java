package com.megacrit.cardcrawl.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDataStringBuilder;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import java.io.Serializable;
import java.util.*;

public abstract class AbstractCard implements Comparable<AbstractCard> {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(AbstractCard.class);
    public AbstractCard.CardType type;
    public int cost;
    public int costForTurn;
    public int price;
    public int chargeCost;
    public boolean isCostModified;
    public boolean isCostModifiedForTurn;
    public boolean retain;
    public boolean selfRetain;
    public boolean dontTriggerOnUseCard;
    public AbstractCard.CardRarity rarity;
    public AbstractCard.CardColor color;
    public boolean isInnate;
    public boolean isLocked;
    public boolean showEvokeValue;
    public int showEvokeOrbCount;
    public ArrayList<String> keywords;
    private static final int COMMON_CARD_PRICE = 50;
    private static final int UNCOMMON_CARD_PRICE = 75;
    private static final int RARE_CARD_PRICE = 150;
    protected boolean isUsed;
    public boolean upgraded;
    public int timesUpgraded;
    public int misc;
    public int energyOnUse;
    public boolean ignoreEnergyOnUse;
    public boolean isSeen;
    public boolean upgradedCost;
    public boolean upgradedDamage;
    public boolean upgradedBlock;
    public boolean upgradedMagicNumber;
    public UUID uuid;
    public boolean isSelected;
    public boolean exhaust;
    public boolean returnToHand;
    public boolean shuffleBackIntoDrawPile;
    public boolean isEthereal;
    public ArrayList<AbstractCard.CardTags> tags;
    public int[] multiDamage;
    protected boolean isMultiDamage;
    public int baseDamage;
    public int baseBlock;
    public int baseMagicNumber;
    public int baseHeal;
    public int baseDraw;
    public int baseDiscard;
    public int damage;
    public int block;
    public int magicNumber;
    public int heal;
    public int draw;
    public int discard;
    public boolean isDamageModified;
    public boolean isBlockModified;
    public boolean isMagicNumberModified;
    protected DamageType damageType;
    public DamageType damageTypeForTurn;
    public AbstractCard.CardTarget target;
    public boolean purgeOnUse;
    public boolean exhaustOnUseOnce;
    public boolean exhaustOnFire;
    public boolean freeToPlayOnce;
    public boolean isInAutoplay;
    private static TextureAtlas orbAtlas;
    private static TextureAtlas cardAtlas;
    private static TextureAtlas oldCardAtlas;
    public static AtlasRegion orb_red;
    public static AtlasRegion orb_green;
    public static AtlasRegion orb_blue;
    public static AtlasRegion orb_purple;
    public static AtlasRegion orb_card;
    public static AtlasRegion orb_potion;
    public static AtlasRegion orb_relic;
    public static AtlasRegion orb_special;
    public AtlasRegion portrait;
    public AtlasRegion jokePortrait;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static float typeWidthAttack;
    public static float typeWidthSkill;
    public static float typeWidthPower;
    public static float typeWidthCurse;
    public static float typeWidthStatus;
    public static float typeOffsetAttack;
    public static float typeOffsetSkill;
    public static float typeOffsetPower;
    public static float typeOffsetCurse;
    public static float typeOffsetStatus;
    public AbstractGameEffect flashVfx;
    public String assetUrl;
    public boolean fadingOut;
    public float transparency;
    public float targetTransparency;
    private Color goldColor;
    private Color renderColor;
    private Color textColor;
    private Color typeColor;
    public float targetAngle;
    private static final float NAME_OFFSET_Y = 175.0F;
    private static final float ENERGY_TEXT_OFFSET_X = -132.0F;
    private static final float ENERGY_TEXT_OFFSET_Y = 192.0F;
    private static final int W = 512;
    public float angle;
    private ArrayList<CardGlowBorder> glowList;
    private float glowTimer;
    public boolean isGlowing;
    public static final float SMALL_SCALE = 0.7F;
    public static final int RAW_W = 300;
    public static final int RAW_H = 420;
    public static final float IMG_WIDTH;
    public static final float IMG_HEIGHT;
    public static final float IMG_WIDTH_S;
    public static final float IMG_HEIGHT_S;
    private static final float SHADOW_OFFSET_X;
    private static final float SHADOW_OFFSET_Y;
    public float current_x;
    public float current_y;
    public float target_x;
    public float target_y;
    public Texture portraitImg;
    private boolean useSmallTitleFont;
    public float drawScale;
    public float targetDrawScale;
    private static final int PORTRAIT_WIDTH = 250;
    private static final int PORTRAIT_HEIGHT = 190;
    private static final float PORTRAIT_OFFSET_Y = 72.0F;
    private static final float LINE_SPACING = 1.45F;
    public boolean isFlipped;
    private boolean darken;
    private float darkTimer;
    private static final float DARKEN_TIME = 0.3F;
    public Hitbox hb;
    private static final float HB_W;
    private static final float HB_H;
    public float hoverTimer;
    private boolean renderTip;
    private boolean hovered;
    private float hoverDuration;
    private static final float HOVER_TIP_TIME = 0.2F;
    private static final GlyphLayout gl;
    private static final StringBuilder sbuilder;
    private static final StringBuilder sbuilder2;
    public AbstractCard cardsToPreview;
    protected static final float CARD_TIP_PAD = 16.0F;
    public float newGlowTimer;
    public String originalName;
    public String name;
    public String rawDescription;
    public String cardID;
    public ArrayList<DescriptionLine> description;
    public String cantUseMessage;
    private static final float TYPE_OFFSET_Y = -1.0F;
    private static final float DESC_OFFSET_Y;
    private static final float DESC_OFFSET_Y2 = -6.0F;
    private static final float DESC_BOX_WIDTH;
    private static final float CN_DESC_BOX_WIDTH;
    private static final float TITLE_BOX_WIDTH;
    private static final float TITLE_BOX_WIDTH_NO_COST;
    private static final float CARD_ENERGY_IMG_WIDTH;
    private static final float MAGIC_NUM_W;
    private static final UIStrings cardRenderStrings;
    public static final String LOCKED_STRING;
    public static final String UNKNOWN_STRING;
    private Color bgColor;
    private Color backColor;
    private Color frameColor;
    private Color frameOutlineColor;
    private Color frameShadowColor;
    private Color imgFrameColor;
    private Color descBoxColor;
    private Color bannerColor;
    private Color tintColor;
    private static final Color ENERGY_COST_RESTRICTED_COLOR;
    private static final Color ENERGY_COST_MODIFIED_COLOR;
    private static final Color FRAME_SHADOW_COLOR;
    private static final Color IMG_FRAME_COLOR_COMMON;
    private static final Color IMG_FRAME_COLOR_UNCOMMON;
    private static final Color IMG_FRAME_COLOR_RARE;
    private static final Color HOVER_IMG_COLOR;
    private static final Color SELECTED_CARD_COLOR;
    private static final Color BANNER_COLOR_COMMON;
    private static final Color BANNER_COLOR_UNCOMMON;
    private static final Color BANNER_COLOR_RARE;
    private static final Color CURSE_BG_COLOR;
    private static final Color CURSE_TYPE_BACK_COLOR;
    private static final Color CURSE_FRAME_COLOR;
    private static final Color CURSE_DESC_BOX_COLOR;
    private static final Color COLORLESS_BG_COLOR;
    private static final Color COLORLESS_TYPE_BACK_COLOR;
    private static final Color COLORLESS_FRAME_COLOR;
    private static final Color COLORLESS_DESC_BOX_COLOR;
    private static final Color RED_BG_COLOR;
    private static final Color RED_TYPE_BACK_COLOR;
    private static final Color RED_FRAME_COLOR;
    private static final Color RED_RARE_OUTLINE_COLOR;
    private static final Color RED_DESC_BOX_COLOR;
    private static final Color GREEN_BG_COLOR;
    private static final Color GREEN_TYPE_BACK_COLOR;
    private static final Color GREEN_FRAME_COLOR;
    private static final Color GREEN_RARE_OUTLINE_COLOR;
    private static final Color GREEN_DESC_BOX_COLOR;
    private static final Color BLUE_BG_COLOR;
    private static final Color BLUE_TYPE_BACK_COLOR;
    private static final Color BLUE_FRAME_COLOR;
    private static final Color BLUE_RARE_OUTLINE_COLOR;
    private static final Color BLUE_DESC_BOX_COLOR;
    protected static final Color BLUE_BORDER_GLOW_COLOR;
    protected static final Color GREEN_BORDER_GLOW_COLOR;
    protected static final Color GOLD_BORDER_GLOW_COLOR;
    public boolean inBottleFlame;
    public boolean inBottleLightning;
    public boolean inBottleTornado;
    public Color glowColor;

    public boolean isStarterStrike() {
        return this.hasTag(AbstractCard.CardTags.STRIKE) && this.rarity.equals(AbstractCard.CardRarity.BASIC);
    }

    public boolean isStarterDefend() {
        return this.hasTag(AbstractCard.CardTags.STARTER_DEFEND) && this.rarity.equals(AbstractCard.CardRarity.BASIC);
    }

    public AbstractCard(String id, String name, String imgUrl, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        this(id, name, imgUrl, cost, rawDescription, type, color, rarity, target, DamageType.NORMAL);
    }

    public AbstractCard(String id, String name, String deprecatedJokeUrl, String imgUrl, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        this(id, name, imgUrl, cost, rawDescription, type, color, rarity, target, DamageType.NORMAL);
    }

    public AbstractCard(String id, String name, String deprecatedJokeUrl, String imgUrl, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, DamageType dType) {
        this(id, name, imgUrl, cost, rawDescription, type, color, rarity, target, dType);
    }

    public AbstractCard(String id, String name, String imgUrl, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, DamageType dType) {
        this.chargeCost = -1;
        this.isCostModified = false;
        this.isCostModifiedForTurn = false;
        this.retain = false;
        this.selfRetain = false;
        this.dontTriggerOnUseCard = false;
        this.isInnate = false;
        this.isLocked = false;
        this.showEvokeValue = false;
        this.showEvokeOrbCount = 0;
        this.keywords = new ArrayList<>();
        this.isUsed = false;
        this.upgraded = false;
        this.timesUpgraded = 0;
        this.misc = 0;
        this.ignoreEnergyOnUse = false;
        this.isSeen = true;
        this.upgradedCost = false;
        this.upgradedDamage = false;
        this.upgradedBlock = false;
        this.upgradedMagicNumber = false;
        this.isSelected = false;
        this.exhaust = false;
        this.returnToHand = false;
        this.shuffleBackIntoDrawPile = false;
        this.isEthereal = false;
        this.tags = new ArrayList<>();
        this.isMultiDamage = false;
        this.baseDamage = -1;
        this.baseBlock = -1;
        this.baseMagicNumber = -1;
        this.baseHeal = -1;
        this.baseDraw = -1;
        this.baseDiscard = -1;
        this.damage = -1;
        this.block = -1;
        this.magicNumber = -1;
        this.heal = -1;
        this.draw = -1;
        this.discard = -1;
        this.isDamageModified = false;
        this.isBlockModified = false;
        this.isMagicNumberModified = false;
        this.target = AbstractCard.CardTarget.ENEMY;
        this.purgeOnUse = false;
        this.exhaustOnUseOnce = false;
        this.exhaustOnFire = false;
        this.freeToPlayOnce = false;
        this.isInAutoplay = false;
        this.fadingOut = false;
        this.transparency = 1.0F;
        this.targetTransparency = 1.0F;
        this.goldColor = Settings.GOLD_COLOR.cpy();
        this.renderColor = Color.WHITE.cpy();
        this.textColor = Settings.CREAM_COLOR.cpy();
        this.typeColor = new Color(0.35F, 0.35F, 0.35F, 0.0F);
        this.targetAngle = 0.0F;
        this.angle = 0.0F;
        this.glowList = new ArrayList<>();
        this.glowTimer = 0.0F;
        this.isGlowing = false;
        this.portraitImg = null;
        this.useSmallTitleFont = false;
        this.drawScale = 0.7F;
        this.targetDrawScale = 0.7F;
        this.isFlipped = false;
        this.darken = false;
        this.darkTimer = 0.0F;
        this.hb = new Hitbox(IMG_WIDTH_S, IMG_HEIGHT_S);
        this.hoverTimer = 0.0F;
        this.renderTip = false;
        this.hovered = false;
        this.hoverDuration = 0.0F;
        this.cardsToPreview = null;
        this.newGlowTimer = 0.0F;
        this.description = new ArrayList<>();
        this.inBottleFlame = false;
        this.inBottleLightning = false;
        this.inBottleTornado = false;
        this.color = color;
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
        if (bundle != null) {
            this.glowColor = bundle.glowColor.cpy();
        }
        this.originalName = name;
        this.name = name;
        this.cardID = id;
        this.assetUrl = imgUrl;
        this.portrait = cardAtlas.findRegion(imgUrl);
        this.jokePortrait = oldCardAtlas.findRegion(imgUrl);
        if (this.portrait == null) {
            if (this.jokePortrait != null) {
                this.portrait = this.jokePortrait;
            } else {
                this.portrait = cardAtlas.findRegion("status/beta");
            }
        }

        this.cost = cost;
        this.costForTurn = cost;
        this.rawDescription = rawDescription;
        this.type = type;
        this.rarity = rarity;
        this.target = target;
        this.damageType = dType;
        this.damageTypeForTurn = dType;
        this.createCardImage();
        if (name == null || rawDescription == null) {
            logger.info("Card initialized incorrecty");
        }

        this.initializeTitle();
        this.initializeDescription();
        this.updateTransparency();
        this.uuid = UUID.randomUUID();
    }

    public static void initialize() {
        long startTime = System.currentTimeMillis();
        cardAtlas = new TextureAtlas(Gdx.files.internal("cards/cards.atlas"));
        oldCardAtlas = new TextureAtlas(Gdx.files.internal("oldCards/cards.atlas"));
        orbAtlas = new TextureAtlas(Gdx.files.internal("orbs/orb.atlas"));
        orb_red = orbAtlas.findRegion("red");
        orb_green = orbAtlas.findRegion("green");
        orb_blue = orbAtlas.findRegion("blue");
        orb_purple = orbAtlas.findRegion("purple");
        orb_card = orbAtlas.findRegion("card");
        orb_potion = orbAtlas.findRegion("potion");
        orb_relic = orbAtlas.findRegion("relic");
        orb_special = orbAtlas.findRegion("special");
        logger.info("Card Image load time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static void initializeDynamicFrameWidths() {
        float d = 48.0F * Settings.scale;
        gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[0]);
        typeOffsetAttack = (gl.width - 48.0F * Settings.scale) / 2.0F;
        typeWidthAttack = (gl.width / d - 1.0F) * 2.0F + 1.0F;
        gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[1]);
        typeOffsetSkill = (gl.width - 48.0F * Settings.scale) / 2.0F;
        typeWidthSkill = (gl.width / d - 1.0F) * 2.0F + 1.0F;
        gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[2]);
        typeOffsetPower = (gl.width - 48.0F * Settings.scale) / 2.0F;
        typeWidthPower = (gl.width / d - 1.0F) * 2.0F + 1.0F;
        gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[3]);
        typeOffsetCurse = (gl.width - 48.0F * Settings.scale) / 2.0F;
        typeWidthCurse = (gl.width / d - 1.0F) * 2.0F + 1.0F;
        gl.setText(FontHelper.cardTypeFont, uiStrings.TEXT[7]);
        typeOffsetStatus = (gl.width - 48.0F * Settings.scale) / 2.0F;
        typeWidthStatus = (gl.width / d - 1.0F) * 2.0F + 1.0F;
    }

    protected void initializeTitle() {
        FontHelper.cardTitleFont.getData().setScale(1.0F);
        gl.setText(FontHelper.cardTitleFont, this.name, Color.WHITE, 0.0F, 1, false);
        if (this.cost <= 0 && this.cost != -1) {
            if (gl.width > TITLE_BOX_WIDTH_NO_COST) {
                this.useSmallTitleFont = true;
            }
        } else if (gl.width > TITLE_BOX_WIDTH) {
            this.useSmallTitleFont = true;
        }

        gl.reset();
    }

    public void initializeDescription() {
        this.keywords.clear();
        if (Settings.lineBreakViaCharacter) {
            this.initializeDescriptionCN();
        } else {
            this.description.clear();
            int numLines = 1;
            sbuilder.setLength(0);
            float currentWidth = 0.0F;
            String[] var4 = this.rawDescription.split(" ");
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String word = var4[var6];
                boolean isKeyword = false;
                sbuilder2.setLength(0);
                sbuilder2.append(" ");
                if (word.length() > 0 && word.charAt(word.length() - 1) != ']' && !Character.isLetterOrDigit(word.charAt(word.length() - 1))) {
                    sbuilder2.insert(0, word.charAt(word.length() - 1));
                    word = word.substring(0, word.length() - 1);
                }

                String keywordTmp = word.toLowerCase();
                keywordTmp = this.dedupeKeyword(keywordTmp);
                if (GameDictionary.keywords.containsKey(keywordTmp)) {
                    if (!this.keywords.contains(keywordTmp)) {
                        this.keywords.add(keywordTmp);
                    }

                    gl.reset();
                    gl.setText(FontHelper.cardDescFont_N, sbuilder2);
                    float tmp = gl.width;
                    gl.setText(FontHelper.cardDescFont_N, word);
                    gl.width += tmp;
                    isKeyword = true;
                } else if (!word.isEmpty() && word.charAt(0) == '[') {
                    gl.reset();
                    gl.setText(FontHelper.cardDescFont_N, sbuilder2);
                    gl.width += CARD_ENERGY_IMG_WIDTH;
                    switch(this.color.name()) {
                        case "RED":
                            if (!this.keywords.contains("[R]")) {
                                this.keywords.add("[R]");
                            }
                            break;
                        case "GREEN":
                            if (!this.keywords.contains("[G]")) {
                                this.keywords.add("[G]");
                            }
                            break;
                        case "BLUE":
                            if (!this.keywords.contains("[B]")) {
                                this.keywords.add("[B]");
                            }
                            break;
                        case "PURPLE":
                            if (!this.keywords.contains("[W]")) {
                                this.keywords.add("[W]");
                            }
                            break;
                        case "COLORLESS":
                            if (word.equals("[W]") && !this.keywords.contains("[W]")) {
                                this.keywords.add("[W]");
                            }
                            break;
                        default:
                            if (!this.keywords.contains("[E]")) {
                                this.keywords.add("[E]");
                            }
                    }
                } else if (!word.equals("!D") && !word.equals("!B") && !word.equals("!M")) {
                    if (word.equals("NL")) {
                        gl.width = 0.0F;
                        word = "";
                        this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
                        currentWidth = 0.0F;
                        ++numLines;
                        sbuilder.setLength(0);
                    } else {
                        gl.setText(FontHelper.cardDescFont_N, word + sbuilder2);
                    }
                } else {
                    gl.setText(FontHelper.cardDescFont_N, word);
                }

                if (currentWidth + gl.width > DESC_BOX_WIDTH) {
                    this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
                    ++numLines;
                    sbuilder.setLength(0);
                    currentWidth = gl.width;
                } else {
                    currentWidth += gl.width;
                }

                if (isKeyword) {
                    sbuilder.append('*');
                }

                sbuilder.append(word).append(sbuilder2);
            }

            if (!sbuilder.toString().trim().isEmpty()) {
                this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
            }

            if (Settings.isDev && numLines > 5) {
                logger.info("WARNING: Card " + this.name + " has lots of text");
            }

        }
    }

    public void initializeDescriptionCN() {
        this.description.clear();
        int numLines = 1;
        sbuilder.setLength(0);
        float currentWidth = 0.0F;
        String[] var3 = this.rawDescription.split(" ");
        int i = var3.length;

        for(int var5 = 0; var5 < i; ++var5) {
            String word = var3[var5];
            word = word.trim();
            if (Settings.manualLineBreak || Settings.manualAndAutoLineBreak || !word.contains("NL")) {
                if ((!word.equals("NL") || sbuilder.length() != 0) && !word.isEmpty()) {
                    String keywordTmp = word.toLowerCase();
                    keywordTmp = this.dedupeKeyword(keywordTmp);
                    if (GameDictionary.keywords.containsKey(keywordTmp)) {
                        if (!this.keywords.contains(keywordTmp)) {
                            this.keywords.add(keywordTmp);
                        }

                        gl.setText(FontHelper.cardDescFont_N, word);
                        if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
                            ++numLines;
                            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                            sbuilder.setLength(0);
                            currentWidth = gl.width;
                            sbuilder.append(" *").append(word).append(" ");
                        } else {
                            sbuilder.append(" *").append(word).append(" ");
                            currentWidth += gl.width;
                        }
                    } else if (!word.isEmpty() && word.charAt(0) == '[') {
                        switch(this.color.name()) {
                            case "RED":
                                if (!this.keywords.contains("[R]")) {
                                    this.keywords.add("[R]");
                                }
                                break;
                            case "GREEN":
                                if (!this.keywords.contains("[G]")) {
                                    this.keywords.add("[G]");
                                }
                                break;
                            case "BLUE":
                                if (!this.keywords.contains("[B]")) {
                                    this.keywords.add("[B]");
                                }
                                break;
                            case "PURPLE":
                                if (!this.keywords.contains("[W]")) {
                                    this.keywords.add("[W]");
                                }
                                break;
                            case "COLORLESS":
                                if (!this.keywords.contains("[W]")) {
                                    this.keywords.add("[W]");
                                }
                                break;
                            default:
                                if (!this.keywords.contains("[E]")) {
                                    this.keywords.add("[E]");
                                }
                        }

                        if (currentWidth + CARD_ENERGY_IMG_WIDTH > CN_DESC_BOX_WIDTH) {
                            ++numLines;
                            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                            sbuilder.setLength(0);
                            currentWidth = CARD_ENERGY_IMG_WIDTH;
                            sbuilder.append(" ").append(word).append(" ");
                        } else {
                            sbuilder.append(" ").append(word).append(" ");
                            currentWidth += CARD_ENERGY_IMG_WIDTH;
                        }
                    } else if (word.equals("!D!")) {
                        if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
                            ++numLines;
                            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                            sbuilder.setLength(0);
                            currentWidth = MAGIC_NUM_W;
                            sbuilder.append(" D ");
                        } else {
                            sbuilder.append(" D ");
                            currentWidth += MAGIC_NUM_W;
                        }
                    } else if (word.equals("!B!")) {
                        if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
                            ++numLines;
                            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                            sbuilder.setLength(0);
                            currentWidth = MAGIC_NUM_W;
                            sbuilder.append(" ").append(word).append("! ");
                        } else {
                            sbuilder.append(" ").append(word).append("! ");
                            currentWidth += MAGIC_NUM_W;
                        }
                    } else if (word.equals("!M!")) {
                        if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
                            ++numLines;
                            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                            sbuilder.setLength(0);
                            currentWidth = MAGIC_NUM_W;
                            sbuilder.append(" ").append(word).append("! ");
                        } else {
                            sbuilder.append(" ").append(word).append("! ");
                            currentWidth += MAGIC_NUM_W;
                        }
                    } else if ((Settings.manualLineBreak || Settings.manualAndAutoLineBreak) && word.equals("NL") && sbuilder.length() != 0) {
                        gl.width = 0.0F;
                        word = "";
                        this.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
                        currentWidth = 0.0F;
                        ++numLines;
                        sbuilder.setLength(0);
                    } else if (word.charAt(0) == '*') {
                        gl.setText(FontHelper.cardDescFont_N, word.substring(1));
                        if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
                            ++numLines;
                            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                            sbuilder.setLength(0);
                            currentWidth = gl.width;
                            sbuilder.append(" ").append(word).append(" ");
                        } else {
                            sbuilder.append(" ").append(word).append(" ");
                            currentWidth += gl.width;
                        }
                    } else {
                        word = word.trim();
                        char[] var8 = word.toCharArray();
                        int var9 = var8.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            char c = var8[var10];
                            gl.setText(FontHelper.cardDescFont_N, String.valueOf(c));
                            sbuilder.append(c);
                            if (!Settings.manualLineBreak) {
                                if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
                                    ++numLines;
                                    this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
                                    sbuilder.setLength(0);
                                    currentWidth = gl.width;
                                } else {
                                    currentWidth += gl.width;
                                }
                            }
                        }
                    }
                } else {
                    currentWidth = 0.0F;
                }
            }
        }

        if (sbuilder.length() != 0) {
            this.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
        }

        int removeLine = -1;

        for(i = 0; i < this.description.size(); ++i) {
            if ((this.description.get(i)).text.equals(LocalizedStrings.PERIOD)) {
                StringBuilder var10000 = new StringBuilder();
                DescriptionLine var10002 = this.description.get(i - 1);
                var10002.text = var10000.append(var10002.text).append(LocalizedStrings.PERIOD).toString();
                removeLine = i;
            }
        }

        if (removeLine != -1) {
            this.description.remove(removeLine);
        }

        if (Settings.isDev && numLines > 5) {
            logger.info("WARNING: Card " + this.name + " has lots of text");
        }

    }

    public boolean hasTag(AbstractCard.CardTags tagToCheck) {
        return this.tags.contains(tagToCheck);
    }

    public boolean canUpgrade() {
        if (this.type == AbstractCard.CardType.CURSE) {
            return false;
        } else if (this.type == AbstractCard.CardType.STATUS) {
            return false;
        } else {
            return !this.upgraded;
        }
    }

    public abstract void upgrade();

    public void displayUpgrades() {
        if (this.upgradedCost) {
            this.isCostModified = true;
        }

        if (this.upgradedDamage) {
            this.damage = this.baseDamage;
            this.isDamageModified = true;
        }

        if (this.upgradedBlock) {
            this.block = this.baseBlock;
            this.isBlockModified = true;
        }

        if (this.upgradedMagicNumber) {
            this.magicNumber = this.baseMagicNumber;
            this.isMagicNumberModified = true;
        }

    }

    protected void upgradeDamage(int amount) {
        this.baseDamage += amount;
        this.upgradedDamage = true;
    }

    protected void upgradeBlock(int amount) {
        this.baseBlock += amount;
        this.upgradedBlock = true;
    }

    protected void upgradeMagicNumber(int amount) {
        this.baseMagicNumber += amount;
        this.magicNumber = this.baseMagicNumber;
        this.upgradedMagicNumber = true;
    }

    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = this.name + "+";
        this.initializeTitle();
    }

    protected void upgradeBaseCost(int newBaseCost) {
        int diff = this.costForTurn - this.cost;
        this.cost = newBaseCost;
        if (this.costForTurn > 0) {
            this.costForTurn = this.cost + diff;
        }

        if (this.costForTurn < 0) {
            this.costForTurn = 0;
        }

        this.upgradedCost = true;
    }

    private String dedupeKeyword(String keyword) {
        String retVal = GameDictionary.parentWord.get(keyword);
        return retVal != null ? retVal : keyword;
    }

    public AbstractCard(AbstractCard c) {
        this.chargeCost = -1;
        this.isCostModified = false;
        this.isCostModifiedForTurn = false;
        this.retain = false;
        this.selfRetain = false;
        this.dontTriggerOnUseCard = false;
        this.isInnate = false;
        this.isLocked = false;
        this.showEvokeValue = false;
        this.showEvokeOrbCount = 0;
        this.keywords = new ArrayList<>();
        this.isUsed = false;
        this.upgraded = false;
        this.timesUpgraded = 0;
        this.misc = 0;
        this.ignoreEnergyOnUse = false;
        this.isSeen = true;
        this.upgradedCost = false;
        this.upgradedDamage = false;
        this.upgradedBlock = false;
        this.upgradedMagicNumber = false;
        this.isSelected = false;
        this.exhaust = false;
        this.returnToHand = false;
        this.shuffleBackIntoDrawPile = false;
        this.isEthereal = false;
        this.tags = new ArrayList<>();
        this.isMultiDamage = false;
        this.baseDamage = -1;
        this.baseBlock = -1;
        this.baseMagicNumber = -1;
        this.baseHeal = -1;
        this.baseDraw = -1;
        this.baseDiscard = -1;
        this.damage = -1;
        this.block = -1;
        this.magicNumber = -1;
        this.heal = -1;
        this.draw = -1;
        this.discard = -1;
        this.isDamageModified = false;
        this.isBlockModified = false;
        this.isMagicNumberModified = false;
        this.target = AbstractCard.CardTarget.ENEMY;
        this.purgeOnUse = false;
        this.exhaustOnUseOnce = false;
        this.exhaustOnFire = false;
        this.freeToPlayOnce = false;
        this.isInAutoplay = false;
        this.fadingOut = false;
        this.transparency = 1.0F;
        this.targetTransparency = 1.0F;
        this.goldColor = Settings.GOLD_COLOR.cpy();
        this.renderColor = Color.WHITE.cpy();
        this.textColor = Settings.CREAM_COLOR.cpy();
        this.typeColor = new Color(0.35F, 0.35F, 0.35F, 0.0F);
        this.targetAngle = 0.0F;
        this.angle = 0.0F;
        this.glowList = new ArrayList<>();
        this.glowTimer = 0.0F;
        this.isGlowing = false;
        this.portraitImg = null;
        this.useSmallTitleFont = false;
        this.drawScale = 0.7F;
        this.targetDrawScale = 0.7F;
        this.isFlipped = false;
        this.darken = false;
        this.darkTimer = 0.0F;
        this.hb = new Hitbox(IMG_WIDTH_S, IMG_HEIGHT_S);
        this.hoverTimer = 0.0F;
        this.renderTip = false;
        this.hovered = false;
        this.hoverDuration = 0.0F;
        this.cardsToPreview = null;
        this.newGlowTimer = 0.0F;
        this.description = new ArrayList<>();
        this.inBottleFlame = false;
        this.inBottleLightning = false;
        this.inBottleTornado = false;
        this.color = c.color;
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
        if (bundle != null) {
            this.glowColor = bundle.glowColor.cpy();
        }
        this.name = c.name;
        this.rawDescription = c.rawDescription;
        this.cost = c.cost;
        this.type = c.type;
    }

    private void createCardImage() {
        switch(this.color.name()) {
            case "RED":
                this.bgColor = RED_BG_COLOR.cpy();
                this.backColor = RED_TYPE_BACK_COLOR.cpy();
                this.frameColor = RED_FRAME_COLOR.cpy();
                this.frameOutlineColor = RED_RARE_OUTLINE_COLOR.cpy();
                this.descBoxColor = RED_DESC_BOX_COLOR.cpy();
                break;
            case "GREEN":
                this.bgColor = GREEN_BG_COLOR.cpy();
                this.backColor = GREEN_TYPE_BACK_COLOR.cpy();
                this.frameColor = GREEN_FRAME_COLOR.cpy();
                this.frameOutlineColor = GREEN_RARE_OUTLINE_COLOR.cpy();
                this.descBoxColor = GREEN_DESC_BOX_COLOR.cpy();
                break;
            case "BLUE":
                this.bgColor = BLUE_BG_COLOR.cpy();
                this.backColor = BLUE_TYPE_BACK_COLOR.cpy();
                this.frameColor = BLUE_FRAME_COLOR.cpy();
                this.frameOutlineColor = BLUE_RARE_OUTLINE_COLOR.cpy();
                this.descBoxColor = BLUE_DESC_BOX_COLOR.cpy();
            case "PURPLE":
                this.bgColor = BLUE_BG_COLOR.cpy();
                this.backColor = BLUE_TYPE_BACK_COLOR.cpy();
                this.frameColor = BLUE_FRAME_COLOR.cpy();
                this.frameOutlineColor = BLUE_RARE_OUTLINE_COLOR.cpy();
                this.descBoxColor = BLUE_DESC_BOX_COLOR.cpy();
                break;
            case "COLORLESS":
                this.bgColor = COLORLESS_BG_COLOR.cpy();
                this.backColor = COLORLESS_TYPE_BACK_COLOR.cpy();
                this.frameColor = COLORLESS_FRAME_COLOR.cpy();
                this.frameOutlineColor = Color.WHITE.cpy();
                this.descBoxColor = COLORLESS_DESC_BOX_COLOR.cpy();
                break;
            case "CURSE":
                this.bgColor = CURSE_BG_COLOR.cpy();
                this.backColor = CURSE_TYPE_BACK_COLOR.cpy();
                this.frameColor = CURSE_FRAME_COLOR.cpy();
                this.descBoxColor = CURSE_DESC_BOX_COLOR.cpy();
                break;
            default:
                CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
                if (bundle != null) {
                    this.bgColor = bundle.bgColor.cpy();
                    this.backColor = bundle.cardBackColor.cpy();
                    this.frameColor = bundle.frameColor.cpy();
                    this.frameOutlineColor = bundle.frameOutlineColor.cpy();
                    this.descBoxColor = bundle.descBoxColor.cpy();
                } else {
                    logger.info("ERROR: Card color was NOT set for " + this.name);
                }
        }

        if (this.rarity != AbstractCard.CardRarity.COMMON && this.rarity != AbstractCard.CardRarity.BASIC && this.rarity != AbstractCard.CardRarity.CURSE) {
            if (this.rarity == AbstractCard.CardRarity.UNCOMMON) {
                this.bannerColor = BANNER_COLOR_UNCOMMON.cpy();
                this.imgFrameColor = IMG_FRAME_COLOR_UNCOMMON.cpy();
            } else {
                this.bannerColor = BANNER_COLOR_RARE.cpy();
                this.imgFrameColor = IMG_FRAME_COLOR_RARE.cpy();
            }
        } else {
            this.bannerColor = BANNER_COLOR_COMMON.cpy();
            this.imgFrameColor = IMG_FRAME_COLOR_COMMON.cpy();
        }

        this.tintColor = CardHelper.getColor(43, 37, 65);
        this.tintColor.a = 0.0F;
        this.frameShadowColor = FRAME_SHADOW_COLOR.cpy();
    }

    public AbstractCard makeSameInstanceOf() {
        AbstractCard card = this.makeStatEquivalentCopy();
        card.uuid = this.uuid;
        return card;
    }

    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = this.makeCopy();

        for(int i = 0; i < this.timesUpgraded; ++i) {
            card.upgrade();
        }

        card.name = this.name;
        card.target = this.target;
        card.upgraded = this.upgraded;
        card.timesUpgraded = this.timesUpgraded;
        card.baseDamage = this.baseDamage;
        card.baseBlock = this.baseBlock;
        card.baseMagicNumber = this.baseMagicNumber;
        card.cost = this.cost;
        card.costForTurn = this.costForTurn;
        card.isCostModified = this.isCostModified;
        card.isCostModifiedForTurn = this.isCostModifiedForTurn;
        card.inBottleLightning = this.inBottleLightning;
        card.inBottleFlame = this.inBottleFlame;
        card.inBottleTornado = this.inBottleTornado;
        card.isSeen = this.isSeen;
        card.isLocked = this.isLocked;
        card.misc = this.misc;
        card.freeToPlayOnce = this.freeToPlayOnce;
        return card;
    }

    public void onRemoveFromMasterDeck() {
    }

    public boolean cardPlayable(AbstractMonster m) {
        MonsterGroup group = AbstractDungeon.getMonsters();
        if ((this.target != AbstractCard.CardTarget.ENEMY && this.target != AbstractCard.CardTarget.SELF_AND_ENEMY || m == null || !m.isDying) && group != null && !group.areMonstersBasicallyDead()) {
            return true;
        } else {
            this.cantUseMessage = null;
            return false;
        }
    }

    public boolean hasEnoughEnergy() {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.cantUseMessage = TEXT[9];
            return false;
        } else {
            Iterator var1 = AbstractDungeon.player.powers.iterator();

            AbstractPower p;
            do {
                if (!var1.hasNext()) {
                    if (AbstractDungeon.player.hasPower("Entangled") && this.type == AbstractCard.CardType.ATTACK) {
                        this.cantUseMessage = TEXT[10];
                        return false;
                    }

                    var1 = AbstractDungeon.player.relics.iterator();

                    AbstractRelic r;
                    do {
                        if (!var1.hasNext()) {
                            var1 = AbstractDungeon.player.blights.iterator();

                            AbstractBlight b;
                            do {
                                if (!var1.hasNext()) {
                                    var1 = AbstractDungeon.player.hand.group.iterator();

                                    AbstractCard c;
                                    do {
                                        if (!var1.hasNext()) {
                                            if (EnergyPanel.totalCount < this.costForTurn && !this.freeToPlay() && !this.isInAutoplay) {
                                                this.cantUseMessage = TEXT[11];
                                                return false;
                                            }

                                            return true;
                                        }

                                        c = (AbstractCard)var1.next();
                                    } while(c.canPlay(this));

                                    return false;
                                }

                                b = (AbstractBlight)var1.next();
                            } while(b.canPlay(this));

                            return false;
                        }

                        r = (AbstractRelic)var1.next();
                    } while(r.canPlay(this));

                    return false;
                }

                p = (AbstractPower)var1.next();
            } while(p.canPlayCard(this));

            this.cantUseMessage = TEXT[13];
            return false;
        }
    }

    public void tookDamage() {
    }

    public void didDiscard() {
    }

    public void switchedStance() {
    }

    /** @deprecated */
    @Deprecated
    protected void useBlueCandle(AbstractPlayer p) {
    }

    /** @deprecated */
    @Deprecated
    protected void useMedicalKit(AbstractPlayer p) {
    }

    public boolean canPlay(AbstractCard card) {
        return true;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.type == AbstractCard.CardType.STATUS && this.costForTurn < -1 && !AbstractDungeon.player.hasRelic("Medical Kit")) {
            return false;
        } else if (this.type == AbstractCard.CardType.CURSE && this.costForTurn < -1 && !AbstractDungeon.player.hasRelic("Blue Candle")) {
            return false;
        } else {
            return this.cardPlayable(m) && this.hasEnoughEnergy();
        }
    }

    public abstract void use(AbstractPlayer var1, AbstractMonster var2);

    public void update() {
        this.updateFlashVfx();
        if (this.hoverTimer != 0.0F) {
            this.hoverTimer -= Gdx.graphics.getDeltaTime();
            if (this.hoverTimer < 0.0F) {
                this.hoverTimer = 0.0F;
            }
        }

        if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && this == AbstractDungeon.player.hoveredCard) {
            this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
            this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
            if (AbstractDungeon.player.hasRelic("Necronomicon")) {
                if (this.cost >= 2 && this.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.getRelic("Necronomicon").checkTrigger()) {
                    AbstractDungeon.player.getRelic("Necronomicon").beginLongPulse();
                } else {
                    AbstractDungeon.player.getRelic("Necronomicon").stopPulse();
                }
            }
        }

        if (Settings.FAST_MODE) {
            this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
            this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
        }

        this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
        this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
        this.hb.move(this.current_x, this.current_y);
        this.hb.resize(HB_W * this.drawScale, HB_H * this.drawScale);
        if (this.hb.clickStarted && this.hb.hovered) {
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
        } else {
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);
        }

        if (this.angle != this.targetAngle) {
            this.angle = MathHelper.angleLerpSnap(this.angle, this.targetAngle);
        }

        this.updateTransparency();
        this.updateColor();
    }

    private void updateFlashVfx() {
        if (this.flashVfx != null) {
            this.flashVfx.update();
            if (this.flashVfx.isDone) {
                this.flashVfx = null;
            }
        }

    }

    private void updateGlow() {
        if (this.isGlowing) {
            this.glowTimer -= Gdx.graphics.getDeltaTime();
            if (this.glowTimer < 0.0F) {
                this.glowList.add(new CardGlowBorder(this, this.glowColor));
                this.glowTimer = 0.3F;
            }
        }

        Iterator i = this.glowList.iterator();

        while(i.hasNext()) {
            CardGlowBorder e = (CardGlowBorder)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

    }

    public boolean isHoveredInHand(float scale) {
        if (this.hoverTimer > 0.0F) {
            return false;
        } else {
            int x = InputHelper.mX;
            int y = InputHelper.mY;
            return (float)x > this.current_x - IMG_WIDTH * scale / 2.0F && (float)x < this.current_x + IMG_WIDTH * scale / 2.0F && (float)y > this.current_y - IMG_HEIGHT * scale / 2.0F && (float)y < this.current_y + IMG_HEIGHT * scale / 2.0F;
        }
    }

    private boolean isOnScreen() {
        return this.current_y >= -200.0F * Settings.scale && this.current_y <= (float)Settings.HEIGHT + 200.0F * Settings.scale;
    }

    public void render(SpriteBatch sb) {
        if (!Settings.hideCards) {
            this.render(sb, false);
        }

    }

    public void renderHoverShadow(SpriteBatch sb) {
        if (!Settings.hideCards) {
            this.renderHelper(sb, Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR, ImageMaster.CARD_SUPER_SHADOW, this.current_x, this.current_y, 1.15F);
        }

    }

    public void renderInLibrary(SpriteBatch sb) {
        if (this.isOnScreen()) {
            if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked) {
                AbstractCard copy = this.makeCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                copy.upgrade();
                copy.displayUpgrades();
                copy.render(sb);
            } else {
                this.updateGlow();
                this.renderGlow(sb);
                this.renderImage(sb, this.hovered, false);
                this.renderType(sb);
                this.renderTitle(sb);
                if (Settings.lineBreakViaCharacter) {
                    this.renderDescriptionCN(sb);
                } else {
                    this.renderDescription(sb);
                }

                this.renderTint(sb);
                this.renderEnergy(sb);
                this.hb.render(sb);
            }

        }
    }

    public void render(SpriteBatch sb, boolean selected) {
        if (!Settings.hideCards) {
            if (this.flashVfx != null) {
                this.flashVfx.render(sb);
            }

            this.renderCard(sb, this.hovered, selected);
            this.hb.render(sb);
        }

    }

    public void renderUpgradePreview(SpriteBatch sb) {
        this.upgraded = true;
        this.name = this.originalName + "+";
        this.initializeTitle();
        this.renderCard(sb, this.hovered, false);
        this.name = this.originalName;
        this.initializeTitle();
        this.upgraded = false;
        this.resetAttributes();
    }

    public void renderWithSelections(SpriteBatch sb) {
        this.renderCard(sb, false, true);
    }

    private void renderCard(SpriteBatch sb, boolean hovered, boolean selected) {
        if (!Settings.hideCards) {
            if (!this.isOnScreen()) {
                return;
            }

            if (!this.isFlipped) {
                this.updateGlow();
                this.renderGlow(sb);
                this.renderImage(sb, hovered, selected);
                this.renderTitle(sb);
                this.renderType(sb);
                if (Settings.lineBreakViaCharacter) {
                    this.renderDescriptionCN(sb);
                } else {
                    this.renderDescription(sb);
                }

                this.renderTint(sb);
                this.renderEnergy(sb);
            } else {
                this.renderBack(sb, hovered, selected);
                this.hb.render(sb);
            }
        }

    }

    private void renderTint(SpriteBatch sb) {
        if (!Settings.hideCards) {
            AtlasRegion cardBgImg = this.getCardBgAtlas();
            if (cardBgImg != null) {
                this.renderHelper(sb, this.tintColor, cardBgImg, this.current_x, this.current_y);
            } else {
                this.renderHelper(sb, this.tintColor, this.getCardBg(), this.current_x - 256.0F, this.current_y - 256.0F);
            }
        }

    }

    public void renderOuterGlow(SpriteBatch sb) {
        if (AbstractDungeon.player != null && Settings.hideCards) {
            this.renderHelper(sb, AbstractDungeon.player.getCardRenderColor(), this.getCardBgAtlas(), this.current_x, this.current_y, 1.0F + this.tintColor.a / 5.0F);
        }
    }

    public Texture getCardBg() {
        int i = 10;
        if (i < 5) {
            System.out.println("Add special logic here");
        }

        return null;
    }

    public AtlasRegion getCardBgAtlas() {
        switch(this.type) {
            case ATTACK:
                return ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
            case CURSE:
            case STATUS:
            case SKILL:
                return ImageMaster.CARD_SKILL_BG_SILHOUETTE;
            case POWER:
                return ImageMaster.CARD_POWER_BG_SILHOUETTE;
            default:
                return null;
        }
    }

    private void renderGlow(SpriteBatch sb) {
        if (!Settings.hideCards) {
            this.renderMainBorder(sb);
            Iterator var2 = this.glowList.iterator();

            while(var2.hasNext()) {
                AbstractGameEffect e = (AbstractGameEffect)var2.next();
                e.render(sb);
            }

            sb.setBlendFunction(770, 771);
        }

    }

    public void beginGlowing() {
        this.isGlowing = true;
    }

    public void stopGlowing() {
        this.isGlowing = false;

        CardGlowBorder e;
        for(Iterator var1 = this.glowList.iterator(); var1.hasNext(); e.duration /= 5.0F) {
            e = (CardGlowBorder)var1.next();
        }

    }

    private void renderMainBorder(SpriteBatch sb) {
        if (this.isGlowing) {
            sb.setBlendFunction(770, 1);
            AtlasRegion img;
            switch(this.type) {
                case ATTACK:
                    img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
                    break;
                case POWER:
                    img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
                    break;
                default:
                    img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
            }

            if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
                sb.setColor(this.glowColor);
            } else {
                sb.setColor(GREEN_BORDER_GLOW_COLOR);
            }

            sb.draw(img, this.current_x + img.offsetX - (float)img.originalWidth / 2.0F, this.current_y + img.offsetY - (float)img.originalWidth / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalWidth / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, this.drawScale * Settings.scale * 1.04F, this.drawScale * Settings.scale * 1.03F, this.angle);
        }

    }

    private void renderHelper(SpriteBatch sb, Color color, AtlasRegion img, float drawX, float drawY) {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float)img.originalWidth / 2.0F, drawY + img.offsetY - (float)img.originalHeight / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalHeight / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
    }

    private void renderHelper(SpriteBatch sb, Color color, AtlasRegion img, float drawX, float drawY, float scale) {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float)img.originalWidth / 2.0F, drawY + img.offsetY - (float)img.originalHeight / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalHeight / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle);
    }

    private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY) {
        sb.setColor(color);
        sb.draw(img, drawX + 256.0F, drawY + 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
    }

    private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float scale) {
        sb.setColor(color);
        sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle, 0, 0, 512, 512, false, false);
    }

    public void renderSmallEnergy(SpriteBatch sb, AtlasRegion region, float x, float y) {
        sb.setColor(this.renderColor);
        sb.draw(region.getTexture(), this.current_x + x * Settings.scale * this.drawScale + region.offsetX * Settings.scale, this.current_y + y * Settings.scale * this.drawScale + region.offsetY * Settings.scale, 0.0F, 0.0F, (float)region.packedWidth, (float)region.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

    private void renderImage(SpriteBatch sb, boolean hovered, boolean selected) {
        if (AbstractDungeon.player != null) {
            if (selected) {
                this.renderHelper(sb, Color.SKY, this.getCardBgAtlas(), this.current_x, this.current_y, 1.03F);
            }

            this.renderHelper(sb, this.frameShadowColor, this.getCardBgAtlas(), this.current_x + SHADOW_OFFSET_X * this.drawScale, this.current_y - SHADOW_OFFSET_Y * this.drawScale);
            if (AbstractDungeon.player.hoveredCard == this && (AbstractDungeon.player.isDraggingCard && AbstractDungeon.player.isHoveringDropZone || AbstractDungeon.player.inSingleTargetMode)) {
                this.renderHelper(sb, HOVER_IMG_COLOR, this.getCardBgAtlas(), this.current_x, this.current_y);
            } else if (selected) {
                this.renderHelper(sb, SELECTED_CARD_COLOR, this.getCardBgAtlas(), this.current_x, this.current_y);
            }
        }

        this.renderCardBg(sb, this.current_x, this.current_y);
        if (!UnlockTracker.betaCardPref.getBoolean(this.cardID, false) && !Settings.PLAYTESTER_ART_MODE) {
            this.renderPortrait(sb);
        } else {
            this.renderJokePortrait(sb);
        }

        this.renderPortraitFrame(sb, this.current_x, this.current_y);
        this.renderBannerImage(sb, this.current_x, this.current_y);
    }

    private void renderCardBg(SpriteBatch sb, float x, float y) {
        switch(this.type) {
            case ATTACK:
                this.renderAttackBg(sb, x, y);
                break;
            case CURSE:
                this.renderSkillBg(sb, x, y);
                break;
            case STATUS:
                this.renderSkillBg(sb, x, y);
                break;
            case SKILL:
                this.renderSkillBg(sb, x, y);
                break;
            case POWER:
                this.renderPowerBg(sb, x, y);
        }

    }

    private void renderAttackBg(SpriteBatch sb, float x, float y) {
        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
        if (bundle != null) {
            AtlasRegion region = bundle.getAttackBgRegion();
            if (region != null) {
                this.renderHelper(sb, this.renderColor, region, x, y);
                return;
            }
        }
        switch(this.color.name()) {
            case "RED":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_RED, x, y);
                break;
            case "GREEN":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_GREEN, x, y);
                break;
            case "BLUE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_BLUE, x, y);
                break;
            case "PURPLE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_PURPLE, x, y);
                break;
            case "COLORLESS":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_ATTACK_BG_GRAY, x, y);
                break;
            case "CURSE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
                break;
            default:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
        }

    }

    private void renderSkillBg(SpriteBatch sb, float x, float y) {
        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
        if (bundle != null) {
            AtlasRegion region = bundle.getSkillBgRegion();
            if (region != null) {
                this.renderHelper(sb, this.renderColor, region, x, y);
                return;
            }
        }
        switch(this.color.name()) {
            case "RED":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_RED, x, y);
                break;
            case "GREEN":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_GREEN, x, y);
                break;
            case "BLUE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLUE, x, y);
                break;
            case "PURPLE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_PURPLE, x, y);
                break;
            case "COLORLESS":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_GRAY, x, y);
                break;
            case "CURSE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
                break;
            default:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
        }

    }

    private void renderPowerBg(SpriteBatch sb, float x, float y) {
        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
        if (bundle != null) {
            AtlasRegion region = bundle.getPowerBgRegion();
            if (region != null) {
                this.renderHelper(sb, this.renderColor, region, x, y);
                return;
            }
        }
        switch(this.color.name()) {
            case "RED":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_RED, x, y);
                break;
            case "GREEN":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_GREEN, x, y);
                break;
            case "BLUE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_BLUE, x, y);
                break;
            case "PURPLE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_PURPLE, x, y);
                break;
            case "COLORLESS":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_POWER_BG_GRAY, x, y);
                break;
            case "CURSE":
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
                break;
            default:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_SKILL_BG_BLACK, x, y);
        }

    }

    private void renderPortraitFrame(SpriteBatch sb, float x, float y) {
        float tWidth = 0.0F;
        float tOffset = 0.0F;
        switch(this.type) {
            case ATTACK:
                this.renderAttackPortrait(sb, x, y);
                tWidth = typeWidthAttack;
                tOffset = typeOffsetAttack;
                break;
            case CURSE:
                this.renderSkillPortrait(sb, x, y);
                tWidth = typeWidthCurse;
                tOffset = typeOffsetCurse;
                break;
            case STATUS:
                this.renderSkillPortrait(sb, x, y);
                tWidth = typeWidthStatus;
                tOffset = typeOffsetStatus;
                break;
            case SKILL:
                this.renderSkillPortrait(sb, x, y);
                tWidth = typeWidthSkill;
                tOffset = typeOffsetSkill;
                break;
            case POWER:
                this.renderPowerPortrait(sb, x, y);
                tWidth = typeWidthPower;
                tOffset = typeOffsetPower;
        }

        this.renderDynamicFrame(sb, x, y, tOffset, tWidth);
    }

    private void renderAttackPortrait(SpriteBatch sb, float x, float y) {
        switch(this.rarity) {
            case BASIC:
            case CURSE:
            case SPECIAL:
            case COMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
                return;
            case UNCOMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
                return;
            case RARE:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
                return;
            default:
        }
    }

    private void renderDynamicFrame(SpriteBatch sb, float x, float y, float typeOffset, float typeWidth) {
        if (typeWidth > 1.1F) {
            switch(this.rarity) {
                case BASIC:
                case CURSE:
                case SPECIAL:
                case COMMON:
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_MID, x, y, 0.0F, typeWidth);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_LEFT, x, y, -typeOffset, 1.0F);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_COMMON_FRAME_RIGHT, x, y, typeOffset, 1.0F);
                    break;
                case UNCOMMON:
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_MID, x, y, 0.0F, typeWidth);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_LEFT, x, y, -typeOffset, 1.0F);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_UNCOMMON_FRAME_RIGHT, x, y, typeOffset, 1.0F);
                    break;
                case RARE:
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_MID, x, y, 0.0F, typeWidth);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_LEFT, x, y, -typeOffset, 1.0F);
                    this.dynamicFrameRenderHelper(sb, ImageMaster.CARD_RARE_FRAME_RIGHT, x, y, typeOffset, 1.0F);
            }

        }
    }

    private void dynamicFrameRenderHelper(SpriteBatch sb, AtlasRegion img, float x, float y, float xOffset, float xScale) {
        sb.draw(img, x + img.offsetX - (float)img.originalWidth / 2.0F + xOffset * this.drawScale, y + img.offsetY - (float)img.originalHeight / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalHeight / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, this.drawScale * Settings.scale * xScale, this.drawScale * Settings.scale, this.angle);
    }

    private void dynamicFrameRenderHelper(SpriteBatch sb, Texture img, float x, float y, float xOffset, float xScale) {
        sb.draw(img, x + xOffset * this.drawScale, y, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale * xScale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
    }

    private void renderSkillPortrait(SpriteBatch sb, float x, float y) {
        switch(this.rarity) {
            case BASIC:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;
            case CURSE:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;
            case SPECIAL:
            default:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;
            case COMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;
            case UNCOMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_UNCOMMON, x, y);
                return;
            case RARE:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_SKILL_RARE, x, y);
        }
    }

    private void renderPowerPortrait(SpriteBatch sb, float x, float y) {
        switch(this.rarity) {
            case BASIC:
            case CURSE:
            case SPECIAL:
            case COMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_POWER_COMMON, x, y);
                break;
            case UNCOMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
                break;
            case RARE:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
        }

    }

    private void renderBannerImage(SpriteBatch sb, float drawX, float drawY) {
        switch(this.rarity) {
            case BASIC:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
                return;
            case CURSE:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
                return;
            case SPECIAL:
            default:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
                return;
            case COMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_COMMON, drawX, drawY);
                return;
            case UNCOMMON:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_UNCOMMON, drawX, drawY);
                return;
            case RARE:
                this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BANNER_RARE, drawX, drawY);
        }
    }

    private void renderBack(SpriteBatch sb, boolean hovered, boolean selected) {
        this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BACK, this.current_x, this.current_y);
    }

    private void renderPortrait(SpriteBatch sb) {
        float drawX = this.current_x - 125.0F;
        float drawY = this.current_y - 95.0F;
        Texture img = null;
        if (this.portraitImg != null) {
            img = this.portraitImg;
        }

        if (!this.isLocked) {
            if (this.portrait != null) {
                drawX = this.current_x - (float)this.portrait.packedWidth / 2.0F;
                drawY = this.current_y - (float)this.portrait.packedHeight / 2.0F;
                sb.setColor(this.renderColor);
                sb.draw(this.portrait, drawX, drawY + 72.0F, (float)this.portrait.packedWidth / 2.0F, (float)this.portrait.packedHeight / 2.0F - 72.0F, (float)this.portrait.packedWidth, (float)this.portrait.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
            } else if (img != null) {
                sb.setColor(this.renderColor);
                sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
            }
        } else {
            sb.draw(this.portraitImg, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
        }

    }

    private void renderJokePortrait(SpriteBatch sb) {
        float drawX = this.current_x - 125.0F;
        float drawY = this.current_y - 95.0F;
        Texture img = null;
        if (this.portraitImg != null) {
            img = this.portraitImg;
        }

        if (!this.isLocked) {
            if (this.jokePortrait != null) {
                drawX = this.current_x - (float)this.portrait.packedWidth / 2.0F;
                drawY = this.current_y - (float)this.portrait.packedHeight / 2.0F;
                sb.setColor(this.renderColor);
                sb.draw(this.jokePortrait, drawX, drawY + 72.0F, (float)this.jokePortrait.packedWidth / 2.0F, (float)this.jokePortrait.packedHeight / 2.0F - 72.0F, (float)this.jokePortrait.packedWidth, (float)this.jokePortrait.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
            } else if (img != null) {
                sb.setColor(this.renderColor);
                sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
            }
        } else {
            sb.draw(this.portraitImg, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250, 190, false, false);
        }

    }

    private void renderDescription(SpriteBatch sb) {
        if (this.isSeen && !this.isLocked) {
            BitmapFont font = this.getDescFont();
            float draw_y = this.current_y - IMG_HEIGHT * this.drawScale / 2.0F + DESC_OFFSET_Y * this.drawScale;
            draw_y += (float)this.description.size() * font.getCapHeight() * 0.775F - font.getCapHeight() * 0.375F;
            float spacing = 1.45F * -font.getCapHeight() / Settings.scale / this.drawScale;

            for(int i = 0; i < this.description.size(); ++i) {
                float start_x = this.current_x - ((DescriptionLine)this.description.get(i)).width * this.drawScale / 2.0F;
                String[] var7 = ((DescriptionLine)this.description.get(i)).getCachedTokenizedText();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    String tmp = var7[var9];
                    if (tmp.length() > 0 && tmp.charAt(0) == '*') {
                        tmp = tmp.substring(1);
                        String punctuation = "";
                        if (tmp.length() > 1 && tmp.charAt(tmp.length() - 2) != '+' && !Character.isLetter(tmp.charAt(tmp.length() - 2))) {
                            punctuation = punctuation + tmp.charAt(tmp.length() - 2);
                            tmp = tmp.substring(0, tmp.length() - 2);
                            punctuation = punctuation + ' ';
                        }

                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.goldColor);
                        start_x = (float)Math.round(start_x + gl.width);
                        gl.setText(font, punctuation);
                        FontHelper.renderRotatedText(sb, font, punctuation, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        gl.setText(font, punctuation);
                        start_x += gl.width;
                    } else if (tmp.length() > 0 && tmp.charAt(0) == '!') {
                        if (tmp.length() == 4) {
                            start_x += this.renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, (Character)null);
                        } else if (tmp.length() == 5) {
                            start_x += this.renderDynamicVariable(tmp.charAt(1), start_x, draw_y, i, font, sb, tmp.charAt(3));
                        }
                    } else if (tmp.equals("[R] ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[R]. ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale / Settings.scale;
                        this.renderSmallEnergy(sb, orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                        gl.setText(font, LocalizedStrings.PERIOD);
                        start_x += gl.width;
                    } else if (tmp.equals("[G] ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[G]. ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                    } else if (tmp.equals("[B] ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[B]. ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                    } else if (tmp.equals("[W] ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[W]. ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float) this.description.size() - 4.0F) / 2.0F - (float) i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, (float) i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                    } else if (tmp.equals("[E] ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
                        AtlasRegion region = null;
                        if (bundle != null) {
                            region = bundle.getEnergyOrbRegion();
                        }
                        this.renderSmallEnergy(sb, region == null ? orb_red : region, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[E]. ")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
                        AtlasRegion region = null;
                        if (bundle != null) {
                            region = bundle.getEnergyOrbRegion();
                        }
                        this.renderSmallEnergy(sb, region == null ? orb_red : region, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        FontHelper.renderRotatedText(sb, font, LocalizedStrings.PERIOD, this.current_x, this.current_y, start_x - this.current_x + CARD_ENERGY_IMG_WIDTH * this.drawScale, (float) i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                    } else {
                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                    }
                }
            }

            font.getData().setScale(1.0F);
        } else {
            FontHelper.menuBannerFont.getData().setScale(this.drawScale * 1.25F);
            FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", this.current_x, this.current_y, 0.0F, -200.0F * Settings.scale * this.drawScale / 2.0F, this.angle, true, this.textColor);
            FontHelper.menuBannerFont.getData().setScale(1.0F);
        }
    }

    private String getDynamicValue(char key) {
        //TODO
        switch(key) {
            case 'B':
                if (this.isBlockModified) {
                    if (this.block >= this.baseBlock) {
                        return "[#7fff00]" + Integer.toString(this.block) + "[]";
                    }

                    return "[#ff6563]" + Integer.toString(this.block) + "[]";
                }

                return Integer.toString(this.baseBlock);
            case 'D':
                if (this.isDamageModified) {
                    if (this.damage >= this.baseDamage) {
                        return "[#7fff00]" + Integer.toString(this.damage) + "[]";
                    }

                    return "[#ff6563]" + Integer.toString(this.damage) + "[]";
                }

                return Integer.toString(this.baseDamage);
            case 'M':
                if (this.isMagicNumberModified) {
                    if (this.magicNumber >= this.baseMagicNumber) {
                        return "[#7fff00]" + Integer.toString(this.magicNumber) + "[]";
                    }

                    return "[#ff6563]" + Integer.toString(this.magicNumber) + "[]";
                }

                return Integer.toString(this.baseMagicNumber);
            default:
                logger.info("KEY: " + key);
                return Integer.toString(-99);
        }
    }

    private void renderDescriptionCN(SpriteBatch sb) {
        if (this.isSeen && !this.isLocked) {
            BitmapFont font = this.getDescFont();
            float draw_y = this.current_y - IMG_HEIGHT * this.drawScale / 2.0F + DESC_OFFSET_Y * this.drawScale;
            draw_y += (float)this.description.size() * font.getCapHeight() * 0.775F - font.getCapHeight() * 0.375F;
            float spacing = 1.45F * -font.getCapHeight() / Settings.scale / this.drawScale;

            for(int i = 0; i < this.description.size(); ++i) {
                float start_x = 0.0F;
                if (Settings.leftAlignCards) {
                    start_x = this.current_x - DESC_BOX_WIDTH * this.drawScale / 2.0F + 2.0F * Settings.scale;
                } else {
                    start_x = this.current_x - ((DescriptionLine)this.description.get(i)).width * this.drawScale / 2.0F - 14.0F * Settings.scale;
                }

                String[] var7 = ((DescriptionLine)this.description.get(i)).getCachedTokenizedTextCN();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    String tmp = var7[var9];
                    String updateTmp = null;

                    int j;
                    for(j = 0; j < tmp.length(); ++j) {
                        if (tmp.charAt(j) == 'D' || tmp.charAt(j) == 'B' && !tmp.contains("[B]") || tmp.charAt(j) == 'M') {
                            updateTmp = tmp.substring(0, j);
                            updateTmp = updateTmp + this.getDynamicValue(tmp.charAt(j));
                            updateTmp = updateTmp + tmp.substring(j + 1);
                            break;
                        }
                    }

                    if (updateTmp != null) {
                        tmp = updateTmp;
                    }

                    for(j = 0; j < tmp.length(); ++j) {
                        if (tmp.charAt(j) == 'D' || tmp.charAt(j) == 'B' && !tmp.contains("[B]") || tmp.charAt(j) == 'M') {
                            updateTmp = tmp.substring(0, j);
                            updateTmp = updateTmp + this.getDynamicValue(tmp.charAt(j));
                            updateTmp = updateTmp + tmp.substring(j + 1);
                            break;
                        }
                    }

                    if (updateTmp != null) {
                        tmp = updateTmp;
                    }

                    if (tmp.length() > 0 && tmp.charAt(0) == '*') {
                        tmp = tmp.substring(1);
                        String punctuation = "";
                        if (tmp.length() > 1 && tmp.charAt(tmp.length() - 2) != '+' && !Character.isLetter(tmp.charAt(tmp.length() - 2))) {
                            punctuation = punctuation + tmp.charAt(tmp.length() - 2);
                            tmp = tmp.substring(0, tmp.length() - 2);
                            punctuation = punctuation + ' ';
                        }

                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.goldColor);
                        start_x = (float)Math.round(start_x + gl.width);
                        gl.setText(font, punctuation);
                        FontHelper.renderRotatedText(sb, font, punctuation, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        gl.setText(font, punctuation);
                        start_x += gl.width;
                    } else if (tmp.equals("[R]")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_red, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[G]")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_green, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[B]")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_blue, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[W]")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        this.renderSmallEnergy(sb, orb_purple, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else if (tmp.equals("[E]")) {
                        gl.width = CARD_ENERGY_IMG_WIDTH * this.drawScale;
                        CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
                        AtlasRegion region = null;
                        if (bundle != null) {
                            region = bundle.getEnergyOrbRegion();
                        }
                        this.renderSmallEnergy(sb, region == null ? orb_red : region, (start_x - this.current_x) / Settings.scale / this.drawScale, -100.0F - (((float)this.description.size() - 4.0F) / 2.0F - (float)i + 1.0F) * spacing);
                        start_x += gl.width;
                    } else {
                        gl.setText(font, tmp);
                        FontHelper.renderRotatedText(sb, font, tmp, this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, this.textColor);
                        start_x += gl.width;
                    }
                }
            }

            font.getData().setScale(1.0F);
        } else {
            FontHelper.menuBannerFont.getData().setScale(this.drawScale * 1.25F);
            FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", this.current_x, this.current_y, 0.0F, -200.0F * Settings.scale * this.drawScale / 2.0F, this.angle, true, this.textColor);
            FontHelper.menuBannerFont.getData().setScale(1.0F);
        }
    }

    private float renderDynamicVariable(char key, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character end) {
        //TODO
        sbuilder.setLength(0);
        Color c = null;
        int num = 0;
        switch(key) {
            case 'B':
                if (this.isBlockModified) {
                    num = this.block;
                    if (this.block >= this.baseBlock) {
                        c = Settings.GREEN_TEXT_COLOR;
                    } else {
                        c = Settings.RED_TEXT_COLOR;
                    }
                } else {
                    c = this.textColor;
                    num = this.baseBlock;
                }
                break;
            case 'D':
                if (this.isDamageModified) {
                    num = this.damage;
                    if (this.damage >= this.baseDamage) {
                        c = Settings.GREEN_TEXT_COLOR;
                    } else {
                        c = Settings.RED_TEXT_COLOR;
                    }
                } else {
                    c = this.textColor;
                    num = this.baseDamage;
                }
                break;
            case 'M':
                if (this.isMagicNumberModified) {
                    num = this.magicNumber;
                    if (this.magicNumber >= this.baseMagicNumber) {
                        c = Settings.GREEN_TEXT_COLOR;
                    } else {
                        c = Settings.RED_TEXT_COLOR;
                    }
                } else {
                    c = this.textColor;
                    num = this.baseMagicNumber;
                }
        }

        sbuilder.append(Integer.toString(num));
        gl.setText(font, sbuilder);
        FontHelper.renderRotatedText(sb, font, sbuilder.toString(), this.current_x, this.current_y, start_x - this.current_x + gl.width / 2.0F, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, this.angle, true, c);
        if (end != null) {
            FontHelper.renderRotatedText(sb, font, Character.toString(end), this.current_x, this.current_y, start_x - this.current_x + gl.width + 4.0F * Settings.scale, (float)i * 1.45F * -font.getCapHeight() + draw_y - this.current_y + -6.0F, 0.0F, true, Settings.CREAM_COLOR);
            sbuilder.append(end);
        }

        sbuilder.append(' ');
        gl.setText(font, sbuilder);
        return gl.width;
    }

    private BitmapFont getDescFont() {
        BitmapFont font = null;
        if (this.angle == 0.0F && this.drawScale == 1.0F) {
            font = FontHelper.cardDescFont_N;
        } else {
            font = FontHelper.cardDescFont_L;
        }

        font.getData().setScale(this.drawScale);
        return font;
    }

    private void renderTitle(SpriteBatch sb) {
        if (this.isLocked) {
            FontHelper.cardTitleFont.getData().setScale(this.drawScale);
            FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, LOCKED_STRING, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, this.renderColor);
        } else if (!this.isSeen) {
            FontHelper.cardTitleFont.getData().setScale(this.drawScale);
            FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, UNKNOWN_STRING, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, this.renderColor);
        } else {
            if (!this.useSmallTitleFont) {
                FontHelper.cardTitleFont.getData().setScale(this.drawScale);
            } else {
                FontHelper.cardTitleFont.getData().setScale(this.drawScale * 0.85F);
            }

            if (this.upgraded) {
                Color color = Settings.GREEN_TEXT_COLOR.cpy();
                color.a = this.renderColor.a;
                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, this.name, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, color);
            } else {
                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, this.name, this.current_x, this.current_y, 0.0F, 175.0F * this.drawScale * Settings.scale, this.angle, false, this.renderColor);
            }

        }
    }

    private void renderType(SpriteBatch sb) {
        String text;
        switch(this.type) {
            case ATTACK:
                text = TEXT[0];
                break;
            case CURSE:
                text = TEXT[3];
                break;
            case STATUS:
                text = TEXT[7];
                break;
            case SKILL:
                text = TEXT[1];
                break;
            case POWER:
                text = TEXT[2];
                break;
            default:
                text = TEXT[5];
        }

        BitmapFont font = FontHelper.cardTypeFont;
        font.getData().setScale(this.drawScale);
        this.typeColor.a = this.renderColor.a;
        FontHelper.renderRotatedText(sb, font, text, this.current_x, this.current_y - 22.0F * this.drawScale * Settings.scale, 0.0F, -1.0F * this.drawScale * Settings.scale, this.angle, false, this.typeColor);
    }

    public static int getPrice(AbstractCard.CardRarity rarity) {
        switch(rarity) {
            case BASIC:
                logger.info("ERROR: WHY WE SELLIN' BASIC");
                return 9999;
            case CURSE:
            default:
                logger.info("No rarity on this card?");
                return 0;
            case SPECIAL:
                logger.info("ERROR: WHY WE SELLIN' SPECIAL");
                return 9999;
            case COMMON:
                return 50;
            case UNCOMMON:
                return 75;
            case RARE:
                return 150;
        }
    }

    private void renderEnergy(SpriteBatch sb) {
        if (this.cost > -2 && !this.darken && !this.isLocked && this.isSeen) {
            switch(this.color.name()) {
                case "RED":
                    this.renderHelper(sb, this.renderColor, ImageMaster.CARD_RED_ORB, this.current_x, this.current_y);
                    break;
                case "GREEN":
                    this.renderHelper(sb, this.renderColor, ImageMaster.CARD_GREEN_ORB, this.current_x, this.current_y);
                    break;
                case "BLUE":
                    this.renderHelper(sb, this.renderColor, ImageMaster.CARD_BLUE_ORB, this.current_x, this.current_y);
                    break;
                case "PURPLE":
                    this.renderHelper(sb, this.renderColor, ImageMaster.CARD_PURPLE_ORB, this.current_x, this.current_y);
                    break;
                case "COLORLESS":
                    this.renderHelper(sb, this.renderColor, ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);
                    break;
                default:
                    CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(this.color, null);
                    if (bundle != null) {
                        AtlasRegion region = bundle.getCardEnergyOrbRegion();
                        if (region != null) {
                            this.renderHelper(sb, this.renderColor, region, this.current_x, this.current_y);
                            break;
                        }
                    }
                    this.renderHelper(sb, this.renderColor, ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);
            }

            Color costColor = Color.WHITE.cpy();
            if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this) && !this.hasEnoughEnergy()) {
                costColor = ENERGY_COST_RESTRICTED_COLOR;
            } else if (this.isCostModified || this.isCostModifiedForTurn || this.freeToPlay()) {
                costColor = ENERGY_COST_MODIFIED_COLOR;
            }

            costColor.a = this.transparency;
            String text = this.getCost();
            BitmapFont font = this.getEnergyFont();
            if (this.cost > -2) {
                FontHelper.renderRotatedText(sb, font, text, this.current_x, this.current_y, -132.0F * this.drawScale * Settings.scale, 192.0F * this.drawScale * Settings.scale, this.angle, false, costColor);
            }
        }
    }

    public void updateCost(int amt) {
        if (this.cost < -1) {
            logger.info("Unplayable cards cannot have their costs modified");
        } else {
            int tmpCost = this.cost;
            int diff = this.cost - this.costForTurn;
            tmpCost += amt;
            if (tmpCost < 0) {
                tmpCost = 0;
            }

            if (tmpCost != this.cost) {
                this.isCostModified = true;
                this.cost = tmpCost;
                this.costForTurn = this.cost - diff;
                if (this.costForTurn < 0) {
                    this.costForTurn = 0;
                }
            }
        }

    }

    public void setCostForTurn(int amt) {
        if (this.costForTurn >= 0) {
            this.costForTurn = amt;
            if (this.costForTurn < 0) {
                this.costForTurn = 0;
            }

            if (this.costForTurn != this.cost) {
                this.isCostModifiedForTurn = true;
            }
        }

    }

    public void modifyCostForCombat(int amt) {
        if (this.costForTurn > 0) {
            this.costForTurn += amt;
            if (this.costForTurn < 0) {
                this.costForTurn = 0;
            }

            if (this.cost != this.costForTurn) {
                this.isCostModified = true;
            }

            this.cost = this.costForTurn;
        } else if (this.cost >= 0) {
            this.cost += amt;
            if (this.cost < 0) {
                this.cost = 0;
            }

            this.costForTurn = 0;
            if (this.cost != this.costForTurn) {
                this.isCostModified = true;
            }
        }

    }

    public void resetAttributes() {
        this.block = this.baseBlock;
        this.isBlockModified = false;
        this.damage = this.baseDamage;
        this.isDamageModified = false;
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
        this.damageTypeForTurn = this.damageType;
        this.costForTurn = this.cost;
        this.isCostModifiedForTurn = false;
    }

    private String getCost() {
        if (this.cost == -1) {
            return "X";
        } else {
            return this.freeToPlay() ? "0" : Integer.toString(this.costForTurn);
        }
    }

    public boolean freeToPlay() {
        if (this.freeToPlayOnce) {
            return true;
        } else {
            return AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && AbstractDungeon.player.hasPower("FreeAttackPower") && this.type == AbstractCard.CardType.ATTACK;
        }
    }

    protected BitmapFont getEnergyFont() {
        FontHelper.cardEnergyFont_L.getData().setScale(this.drawScale);
        return FontHelper.cardEnergyFont_L;
    }

    public void hover() {
        if (!this.hovered) {
            this.hovered = true;
            this.drawScale = 1.0F;
            this.targetDrawScale = 1.0F;
        }

    }

    public void unhover() {
        if (this.hovered) {
            this.hovered = false;
            this.hoverDuration = 0.0F;
            this.renderTip = false;
            this.targetDrawScale = 0.75F;
        }

    }

    public void updateHoverLogic() {
        this.hb.update();
        if (this.hb.hovered) {
            this.hover();
            this.hoverDuration += Gdx.graphics.getDeltaTime();
            if (this.hoverDuration > 0.2F && !Settings.hideCards) {
                this.renderTip = true;
            }
        } else {
            this.unhover();
        }

    }

    public void untip() {
        this.hoverDuration = 0.0F;
        this.renderTip = false;
    }

    public void moveToDiscardPile() {
        this.target_x = (float)CardGroup.DISCARD_PILE_X;
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            this.target_y = 0.0F;
        } else {
            this.target_y = 0.0F - OverlayMenu.HAND_HIDE_Y;
        }

    }

    public void teleportToDiscardPile() {
        this.current_x = (float)CardGroup.DISCARD_PILE_X;
        this.target_x = this.current_x;
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            this.current_y = 0.0F;
        } else {
            this.current_y = 0.0F - OverlayMenu.HAND_HIDE_Y;
        }

        this.target_y = this.current_y;
        this.onMoveToDiscard();
    }

    public void onMoveToDiscard() {
    }

    public void renderCardTip(SpriteBatch sb) {
        if (!Settings.hideCards && this.renderTip) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen) {
                return;
            }

            ArrayList unseen;
            if (this.isLocked) {
                unseen = new ArrayList();
                unseen.add(0, "locked");
                TipHelper.renderTipForCard(this, sb, unseen);
                return;
            }

            if (!this.isSeen) {
                unseen = new ArrayList();
                unseen.add(0, "unseen");
                TipHelper.renderTipForCard(this, sb, unseen);
                return;
            }

            if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked) {
                AbstractCard copy = this.makeCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                copy.upgrade();
                TipHelper.renderTipForCard(copy, sb, copy.keywords);
            } else {
                TipHelper.renderTipForCard(this, sb, this.keywords);
            }

            if (this.cardsToPreview != null) {
                this.renderCardPreview(sb);
            }
        }

    }

    public void renderCardPreviewInSingleView(SpriteBatch sb) {
        this.cardsToPreview.current_x = 1435.0F * Settings.scale;
        this.cardsToPreview.current_y = 795.0F * Settings.scale;
        this.cardsToPreview.drawScale = 0.8F;
        this.cardsToPreview.render(sb);
    }

    public void renderCardPreview(SpriteBatch sb) {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard) {
            float tmpScale = this.drawScale * 0.8F;
            if (this.current_x > (float)Settings.WIDTH * 0.75F) {
                this.cardsToPreview.current_x = this.current_x + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
            } else {
                this.cardsToPreview.current_x = this.current_x - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
            }

            this.cardsToPreview.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
            this.cardsToPreview.drawScale = tmpScale;
            this.cardsToPreview.render(sb);
        }
    }

    public void triggerWhenDrawn() {
    }

    public void triggerWhenCopied() {
    }

    public void triggerOnEndOfPlayerTurn() {
        if (this.isEthereal) {
            this.addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        }

    }

    public void triggerOnEndOfTurnForPlayingCard() {
    }

    public void triggerOnOtherCardPlayed(AbstractCard c) {
    }

    public void triggerOnGainEnergy(int e, boolean dueToCard) {
    }

    public void triggerOnManualDiscard() {
    }

    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
    }

    public void triggerOnScry() {
    }

    public void triggerExhaustedCardsOnStanceChange(AbstractStance newStance) {
    }

    public void triggerAtStartOfTurn() {
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
    }

    public void atTurnStart() {
    }

    public void atTurnStartPreDraw() {
    }

    public void onChoseThisOption() {
    }

    public void onRetained() {
    }

    public void triggerOnExhaust() {
    }

    public void applyPowers() {
        this.applyPowersToBlock();
        AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (!this.isMultiDamage) {
            float tmp = (float)this.baseDamage;
            Iterator var3 = player.relics.iterator();

            while(var3.hasNext()) {
                AbstractRelic r = (AbstractRelic)var3.next();
                tmp = r.atDamageModify(tmp, this);
                if (this.baseDamage != (int)tmp) {
                    this.isDamageModified = true;
                }
            }

            AbstractPower p;
            for(var3 = player.powers.iterator(); var3.hasNext(); tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower)var3.next();
            }

            tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
            if (this.baseDamage != (int)tmp) {
                this.isDamageModified = true;
            }

            for(var3 = player.powers.iterator(); var3.hasNext(); tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower)var3.next();
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            if (this.baseDamage != MathUtils.floor(tmp)) {
                this.isDamageModified = true;
            }

            this.damage = MathUtils.floor(tmp);
        } else {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[m.size()];

            int i;
            for(i = 0; i < tmp.length; ++i) {
                tmp[i] = (float)this.baseDamage;
            }

            Iterator var5;
            AbstractPower p;
            for(i = 0; i < tmp.length; ++i) {
                var5 = player.relics.iterator();

                while(var5.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var5.next();
                    tmp[i] = r.atDamageModify(tmp[i], this);
                    if (this.baseDamage != (int)tmp[i]) {
                        this.isDamageModified = true;
                    }
                }

                for(var5 = player.powers.iterator(); var5.hasNext(); tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn, this)) {
                    p = (AbstractPower)var5.next();
                }

                tmp[i] = player.stance.atDamageGive(tmp[i], this.damageTypeForTurn, this);
                if (this.baseDamage != (int)tmp[i]) {
                    this.isDamageModified = true;
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                for(var5 = player.powers.iterator(); var5.hasNext(); tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn, this)) {
                    p = (AbstractPower)var5.next();
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                if (tmp[i] < 0.0F) {
                    tmp[i] = 0.0F;
                }
            }

            this.multiDamage = new int[tmp.length];

            for(i = 0; i < tmp.length; ++i) {
                if (this.baseDamage != (int)tmp[i]) {
                    this.isDamageModified = true;
                }

                this.multiDamage[i] = MathUtils.floor(tmp[i]);
            }

            this.damage = this.multiDamage[0];
        }

    }

    protected void applyPowersToBlock() {
        this.isBlockModified = false;
        float tmp = (float)this.baseBlock;

        Iterator var2;
        AbstractPower p;
        for(var2 = AbstractDungeon.player.powers.iterator(); var2.hasNext(); tmp = p.modifyBlock(tmp, this)) {
            p = (AbstractPower)var2.next();
        }

        for(var2 = AbstractDungeon.player.powers.iterator(); var2.hasNext(); tmp = p.modifyBlockLast(tmp)) {
            p = (AbstractPower)var2.next();
        }

        if (this.baseBlock != MathUtils.floor(tmp)) {
            this.isBlockModified = true;
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }

        this.block = MathUtils.floor(tmp);
    }

    public void calculateDamageDisplay(AbstractMonster mo) {
        this.calculateCardDamage(mo);
    }

    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowersToBlock();
        AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (!this.isMultiDamage && mo != null) {
            float tmp = (float)this.baseDamage;
            Iterator var9 = player.relics.iterator();

            while(var9.hasNext()) {
                AbstractRelic r = (AbstractRelic)var9.next();
                tmp = r.atDamageModify(tmp, this);
                if (this.baseDamage != (int)tmp) {
                    this.isDamageModified = true;
                }
            }

            AbstractPower p;
            for(var9 = player.powers.iterator(); var9.hasNext(); tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower)var9.next();
            }

            tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
            if (this.baseDamage != (int)tmp) {
                this.isDamageModified = true;
            }

            for(var9 = mo.powers.iterator(); var9.hasNext(); tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower)var9.next();
            }

            for(var9 = player.powers.iterator(); var9.hasNext(); tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower)var9.next();
            }

            for(var9 = mo.powers.iterator(); var9.hasNext(); tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower)var9.next();
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            if (this.baseDamage != MathUtils.floor(tmp)) {
                this.isDamageModified = true;
            }

            this.damage = MathUtils.floor(tmp);
        } else {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[m.size()];

            int i;
            for(i = 0; i < tmp.length; ++i) {
                tmp[i] = (float)this.baseDamage;
            }

            Iterator var6;
            AbstractPower p;
            for(i = 0; i < tmp.length; ++i) {
                var6 = player.relics.iterator();

                while(var6.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var6.next();
                    tmp[i] = r.atDamageModify(tmp[i], this);
                    if (this.baseDamage != (int)tmp[i]) {
                        this.isDamageModified = true;
                    }
                }

                for(var6 = player.powers.iterator(); var6.hasNext(); tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn, this)) {
                    p = (AbstractPower)var6.next();
                }

                tmp[i] = player.stance.atDamageGive(tmp[i], this.damageTypeForTurn, this);
                if (this.baseDamage != (int)tmp[i]) {
                    this.isDamageModified = true;
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                var6 = ((AbstractMonster)m.get(i)).powers.iterator();

                while(var6.hasNext()) {
                    p = (AbstractPower)var6.next();
                    if (!((AbstractMonster)m.get(i)).isDying && !((AbstractMonster)m.get(i)).isEscaping) {
                        tmp[i] = p.atDamageReceive(tmp[i], this.damageTypeForTurn, this);
                    }
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                for(var6 = player.powers.iterator(); var6.hasNext(); tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn, this)) {
                    p = (AbstractPower)var6.next();
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                var6 = ((AbstractMonster)m.get(i)).powers.iterator();

                while(var6.hasNext()) {
                    p = (AbstractPower)var6.next();
                    if (!((AbstractMonster)m.get(i)).isDying && !((AbstractMonster)m.get(i)).isEscaping) {
                        tmp[i] = p.atDamageFinalReceive(tmp[i], this.damageTypeForTurn, this);
                    }
                }
            }

            for(i = 0; i < tmp.length; ++i) {
                if (tmp[i] < 0.0F) {
                    tmp[i] = 0.0F;
                }
            }

            this.multiDamage = new int[tmp.length];

            for(i = 0; i < tmp.length; ++i) {
                if (this.baseDamage != MathUtils.floor(tmp[i])) {
                    this.isDamageModified = true;
                }

                this.multiDamage[i] = MathUtils.floor(tmp[i]);
            }

            this.damage = this.multiDamage[0];
        }

    }

    public void setAngle(float degrees, boolean immediate) {
        this.targetAngle = degrees;
        if (immediate) {
            this.angle = this.targetAngle;
        }

    }

    public void shrink() {
        this.targetDrawScale = 0.12F;
    }

    public void shrink(boolean immediate) {
        this.targetDrawScale = 0.12F;
        this.drawScale = 0.12F;
    }

    public void darken(boolean immediate) {
        this.darken = true;
        this.darkTimer = 0.3F;
        if (immediate) {
            this.tintColor.a = 1.0F;
            this.darkTimer = 0.0F;
        }

    }

    public void lighten(boolean immediate) {
        this.darken = false;
        this.darkTimer = 0.3F;
        if (immediate) {
            this.tintColor.a = 0.0F;
            this.darkTimer = 0.0F;
        }

    }

    private void updateColor() {
        if (this.darkTimer != 0.0F) {
            this.darkTimer -= Gdx.graphics.getDeltaTime();
            if (this.darkTimer < 0.0F) {
                this.darkTimer = 0.0F;
            }

            if (this.darken) {
                this.tintColor.a = 1.0F - this.darkTimer * 1.0F / 0.3F;
            } else {
                this.tintColor.a = this.darkTimer * 1.0F / 0.3F;
            }
        }

    }

    public void superFlash(Color c) {
        this.flashVfx = new CardFlashVfx(this, c, true);
    }

    public void superFlash() {
        this.flashVfx = new CardFlashVfx(this, true);
    }

    public void flash() {
        this.flashVfx = new CardFlashVfx(this);
    }

    public void flash(Color c) {
        this.flashVfx = new CardFlashVfx(this, c);
    }

    public void unfadeOut() {
        this.fadingOut = false;
        this.transparency = 1.0F;
        this.targetTransparency = 1.0F;
        this.bannerColor.a = this.transparency;
        this.backColor.a = this.transparency;
        this.frameColor.a = this.transparency;
        this.bgColor.a = this.transparency;
        this.descBoxColor.a = this.transparency;
        this.imgFrameColor.a = this.transparency;
        this.frameShadowColor.a = this.transparency / 4.0F;
        this.renderColor.a = this.transparency;
        this.goldColor.a = this.transparency;
        if (this.frameOutlineColor != null) {
            this.frameOutlineColor.a = this.transparency;
        }

    }

    private void updateTransparency() {
        if (this.fadingOut && this.transparency != 0.0F) {
            this.transparency -= Gdx.graphics.getDeltaTime() * 2.0F;
            if (this.transparency < 0.0F) {
                this.transparency = 0.0F;
            }
        } else if (this.transparency != this.targetTransparency) {
            this.transparency += Gdx.graphics.getDeltaTime() * 2.0F;
            if (this.transparency > this.targetTransparency) {
                this.transparency = this.targetTransparency;
            }
        }

        this.bannerColor.a = this.transparency;
        this.backColor.a = this.transparency;
        this.frameColor.a = this.transparency;
        this.bgColor.a = this.transparency;
        this.descBoxColor.a = this.transparency;
        this.imgFrameColor.a = this.transparency;
        this.frameShadowColor.a = this.transparency / 4.0F;
        this.renderColor.a = this.transparency;
        this.textColor.a = this.transparency;
        this.goldColor.a = this.transparency;
        if (this.frameOutlineColor != null) {
            this.frameOutlineColor.a = this.transparency;
        }

    }

    public void setAngle(float degrees) {
        this.setAngle(degrees, false);
    }

    protected String getCantPlayMessage() {
        return TEXT[13];
    }

    public void clearPowers() {
        this.resetAttributes();
        this.isDamageModified = false;
    }

    public static void debugPrintDetailedCardDataHeader() {
        logger.info(gameDataUploadHeader());
    }

    public static String gameDataUploadHeader() {
        GameDataStringBuilder builder = new GameDataStringBuilder();
        builder.addFieldData("name");
        builder.addFieldData("cardID");
        builder.addFieldData("rawDescription");
        builder.addFieldData("assetURL");
        builder.addFieldData("keywords");
        builder.addFieldData("color");
        builder.addFieldData("type");
        builder.addFieldData("rarity");
        builder.addFieldData("cost");
        builder.addFieldData("target");
        builder.addFieldData("damageType");
        builder.addFieldData("baseDamage");
        builder.addFieldData("baseBlock");
        builder.addFieldData("baseHeal");
        builder.addFieldData("baseDraw");
        builder.addFieldData("baseDiscard");
        builder.addFieldData("baseMagicNumber");
        builder.addFieldData("isMultiDamage");
        return builder.toString();
    }

    public void debugPrintDetailedCardData() {
        logger.info(this.gameDataUploadData());
    }

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public String gameDataUploadData() {
        GameDataStringBuilder builder = new GameDataStringBuilder();
        builder.addFieldData(this.name);
        builder.addFieldData(this.cardID);
        builder.addFieldData(this.rawDescription);
        builder.addFieldData(this.assetUrl);
        builder.addFieldData(Arrays.toString(this.keywords.toArray()));
        builder.addFieldData(this.color.name());
        builder.addFieldData(this.type.name());
        builder.addFieldData(this.rarity.name());
        builder.addFieldData(this.cost);
        builder.addFieldData(this.target.name());
        builder.addFieldData(this.damageType.name());
        builder.addFieldData(this.baseDamage);
        builder.addFieldData(this.baseBlock);
        builder.addFieldData(this.baseHeal);
        builder.addFieldData(this.baseDraw);
        builder.addFieldData(this.baseDiscard);
        builder.addFieldData(this.baseMagicNumber);
        builder.addFieldData(this.isMultiDamage);
        return builder.toString();
    }

    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(AbstractCard other) {
        return other == null ? 1 : this.cardID.compareTo(other.cardID);
    }

    public void setLocked() {
        this.isLocked = true;
        switch(this.type) {
            case ATTACK:
                this.portraitImg = ImageMaster.CARD_LOCKED_ATTACK;
                break;
            case POWER:
                this.portraitImg = ImageMaster.CARD_LOCKED_POWER;
                break;
            default:
                this.portraitImg = ImageMaster.CARD_LOCKED_SKILL;
        }

        this.initializeDescription();
    }

    public void unlock() {
        this.isLocked = false;
        this.portrait = cardAtlas.findRegion(this.assetUrl);
        if (this.portrait == null) {
            this.portrait = oldCardAtlas.findRegion(this.assetUrl);
        }

    }

    public HashMap<String, Serializable> getLocStrings() {
        HashMap<String, Serializable> cardData = new HashMap();
        this.initializeDescription();
        cardData.put("name", this.name);
        cardData.put("description", this.rawDescription);
        return cardData;
    }

    public String getMetricID() {
        String id = this.cardID;
        if (this.upgraded) {
            id = id + "+";
            if (this.timesUpgraded > 0) {
                id = id + this.timesUpgraded;
            }
        }

        return id;
    }

    public void triggerOnGlowCheck() {
    }

    public abstract AbstractCard makeCopy();

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
        TEXT = uiStrings.TEXT;
        IMG_WIDTH = 300.0F * Settings.scale;
        IMG_HEIGHT = 420.0F * Settings.scale;
        IMG_WIDTH_S = 300.0F * Settings.scale * 0.7F;
        IMG_HEIGHT_S = 420.0F * Settings.scale * 0.7F;
        SHADOW_OFFSET_X = 18.0F * Settings.scale;
        SHADOW_OFFSET_Y = 14.0F * Settings.scale;
        HB_W = 300.0F * Settings.scale;
        HB_H = 420.0F * Settings.scale;
        gl = new GlyphLayout();
        sbuilder = new StringBuilder();
        sbuilder2 = new StringBuilder();
        DESC_OFFSET_Y = Settings.BIG_TEXT_MODE ? IMG_HEIGHT * 0.24F : IMG_HEIGHT * 0.255F;
        DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.95F : IMG_WIDTH * 0.79F;
        CN_DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.87F : IMG_WIDTH * 0.72F;
        TITLE_BOX_WIDTH = IMG_WIDTH * 0.6F;
        TITLE_BOX_WIDTH_NO_COST = IMG_WIDTH * 0.7F;
        CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;
        MAGIC_NUM_W = 20.0F * Settings.scale;
        cardRenderStrings = CardCrawlGame.languagePack.getUIString("AbstractCard");
        LOCKED_STRING = cardRenderStrings.TEXT[0];
        UNKNOWN_STRING = cardRenderStrings.TEXT[1];
        ENERGY_COST_RESTRICTED_COLOR = new Color(1.0F, 0.3F, 0.3F, 1.0F);
        ENERGY_COST_MODIFIED_COLOR = new Color(0.4F, 1.0F, 0.4F, 1.0F);
        FRAME_SHADOW_COLOR = new Color(0.0F, 0.0F, 0.0F, 0.25F);
        IMG_FRAME_COLOR_COMMON = CardHelper.getColor(53, 58, 64);
        IMG_FRAME_COLOR_UNCOMMON = CardHelper.getColor(119, 152, 161);
        IMG_FRAME_COLOR_RARE = new Color(0.855F, 0.557F, 0.32F, 1.0F);
        HOVER_IMG_COLOR = new Color(1.0F, 0.815F, 0.314F, 0.8F);
        SELECTED_CARD_COLOR = new Color(0.5F, 0.9F, 0.9F, 1.0F);
        BANNER_COLOR_COMMON = CardHelper.getColor(131, 129, 121);
        BANNER_COLOR_UNCOMMON = CardHelper.getColor(142, 196, 213);
        BANNER_COLOR_RARE = new Color(1.0F, 0.796F, 0.251F, 1.0F);
        CURSE_BG_COLOR = CardHelper.getColor(29, 29, 29);
        CURSE_TYPE_BACK_COLOR = new Color(0.23F, 0.23F, 0.23F, 1.0F);
        CURSE_FRAME_COLOR = CardHelper.getColor(21, 2, 21);
        CURSE_DESC_BOX_COLOR = CardHelper.getColor(52, 58, 64);
        COLORLESS_BG_COLOR = new Color(0.15F, 0.15F, 0.15F, 1.0F);
        COLORLESS_TYPE_BACK_COLOR = new Color(0.23F, 0.23F, 0.23F, 1.0F);
        COLORLESS_FRAME_COLOR = new Color(0.48F, 0.48F, 0.48F, 1.0F);
        COLORLESS_DESC_BOX_COLOR = new Color(0.351F, 0.363F, 0.3745F, 1.0F);
        RED_BG_COLOR = CardHelper.getColor(50, 26, 26);
        RED_TYPE_BACK_COLOR = CardHelper.getColor(91, 43, 32);
        RED_FRAME_COLOR = CardHelper.getColor(121, 12, 28);
        RED_RARE_OUTLINE_COLOR = new Color(1.0F, 0.75F, 0.43F, 1.0F);
        RED_DESC_BOX_COLOR = CardHelper.getColor(53, 58, 64);
        GREEN_BG_COLOR = CardHelper.getColor(19, 45, 40);
        GREEN_TYPE_BACK_COLOR = CardHelper.getColor(32, 91, 43);
        GREEN_FRAME_COLOR = CardHelper.getColor(52, 123, 8);
        GREEN_RARE_OUTLINE_COLOR = new Color(1.0F, 0.75F, 0.43F, 1.0F);
        GREEN_DESC_BOX_COLOR = CardHelper.getColor(53, 58, 64);
        BLUE_BG_COLOR = CardHelper.getColor(19, 45, 40);
        BLUE_TYPE_BACK_COLOR = CardHelper.getColor(32, 91, 43);
        BLUE_FRAME_COLOR = CardHelper.getColor(52, 123, 8);
        BLUE_RARE_OUTLINE_COLOR = new Color(1.0F, 0.75F, 0.43F, 1.0F);
        BLUE_DESC_BOX_COLOR = CardHelper.getColor(53, 58, 64);
        BLUE_BORDER_GLOW_COLOR = new Color(0.2F, 0.9F, 1.0F, 0.25F);
        GREEN_BORDER_GLOW_COLOR = new Color(0.0F, 1.0F, 0.0F, 0.25F);
        GOLD_BORDER_GLOW_COLOR = Color.GOLD.cpy();
    }

    public static enum CardTags {
        HEALING,
        STRIKE,
        EMPTY,
        STARTER_DEFEND,
        STARTER_STRIKE;

        private CardTags() {
        }
    }

    public static enum CardType {
        ATTACK,
        SKILL,
        POWER,
        STATUS,
        CURSE;

        private CardType() {
        }
    }

    public static enum CardRarity {
        BASIC,
        SPECIAL,
        COMMON,
        UNCOMMON,
        RARE,
        CURSE;

        private CardRarity() {
        }
    }

    public static class CardColor {
        public static final CardColor RED = new CardColor("RED");
        public static final CardColor GREEN = new CardColor("GREEN");
        public static final CardColor BLUE = new CardColor("BLUE");
        public static final CardColor PURPLE = new CardColor("PURPLE");
        public static final CardColor COLORLESS = new CardColor("COLORLESS");
        public static final CardColor CURSE = new CardColor("CURSE");

        private String name;

        private CardColor(String name) {
            this.name = name;
        }

        public String name() {
            return this.name;
        }

        public static CardColor add(String name) {
            return new CardColor(name);
        }

        public static int totalCount() {
            return 6;
        }
    }

    public static enum CardTarget {
        ENEMY,
        ALL_ENEMY,
        SELF,
        NONE,
        SELF_AND_ENEMY,
        ALL;

        private CardTarget() {
        }
    }
}


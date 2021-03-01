package com.megacrit.cardcrawl.screens.runHistory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.GameCursor.CursorType;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class TinyCard {
    private static final int MAX_CARD_TEXT_LENGTH = 18;
    private static final Color RED_BACKGROUND_COLOR = new Color(-719117313);
    private static final Color RED_DESCRIPTION_COLOR = new Color(1613902591);
    private static final Color GREEN_BACKGROUND_COLOR = new Color(1792302079);
    private static final Color GREEN_DESCRIPTION_COLOR = new Color(894908927);
    private static final Color BLUE_BACKGROUND_COLOR = new Color(1774256127);
    private static final Color BLUE_DESCRIPTION_COLOR = new Color(1417522687);
    private static final Color PURPLE_BACKGROUND_COLOR = new Color(-1657150465);
    private static final Color PURPLE_DESCRIPTION_COLOR = new Color(1611837695);
    private static final Color COLORLESS_BACKGROUND_COLOR = new Color(2054847231);
    private static final Color COLORLESS_DESCRIPTION_COLOR = new Color(1077952767);
    private static final Color CURSE_BACKGROUND_COLOR = new Color(993541375);
    private static final Color CURSE_DESCRIPTION_COLOR = new Color(454761471);
    private static final Color COMMON_BANNER_COLOR = new Color(-1364283649);
    private static final Color UNCOMMON_BANNER_COLOR = new Color(-1930365185);
    private static final Color RARE_BANNER_COLOR = new Color(-103454721);
    public static final float LINE_SPACING;
    public static final float LINE_WIDTH = 9999.0F;
    public static final float TEXT_LEADING_SPACE;
    public static int desiredColumns;
    public AbstractCard card;
    public int count;
    public Hitbox hb;
    public int col = -1;
    public int row = -1;

    public TinyCard(AbstractCard card, int count) {
        this.card = card;
        this.count = count;
        this.hb = new Hitbox(this.approximateWidth(), (float)ImageMaster.TINY_CARD_ATTACK.getHeight());
    }

    public float approximateWidth() {
        String text = this.getText();
        return TEXT_LEADING_SPACE + FontHelper.getSmartWidth(FontHelper.charDescFont, text, 9999.0F, LINE_SPACING);
    }

    private String getText() {
        String text = this.count == 1 ? this.card.name : this.count + " x " + this.card.name;
        if (text.length() > 18) {
            text = text.substring(0, 15) + "...";
        }

        return text;
    }

    public void render(SpriteBatch sb) {
        float x = this.hb.x;
        float y = this.hb.y;
        float width = scaled(46.0F);
        float height = scaled(46.0F);
        this.renderTinyCardIcon(sb, this.card, x, y, width, height);
        String text = this.getText();
        float textOffset = -(height / 2.0F) + scaled(7.0F);
        Color basicColor = this.card.upgraded ? Settings.GREEN_TEXT_COLOR : Settings.CREAM_COLOR;
        Color textColor = this.hb.hovered ? Settings.GOLD_COLOR : basicColor;
        if (this.hb.hovered) {
            FontHelper.renderSmartText(sb, FontHelper.charDescFont, text, x + TEXT_LEADING_SPACE + 3.0F * Settings.scale, y + height + textOffset, 9999.0F, LINE_SPACING, textColor);
        } else {
            FontHelper.renderSmartText(sb, FontHelper.charDescFont, text, x + TEXT_LEADING_SPACE, y + height + textOffset, 9999.0F, LINE_SPACING, textColor);
        }

        this.hb.render(sb);
    }

    public boolean updateDidClick() {
        this.hb.update();
        if (this.hb.justHovered) {
            CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
        }

        if (this.hb.hovered) {
            CardCrawlGame.cursor.changeType(CursorType.INSPECT);
            if (InputHelper.justClickedLeft) {
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.hb.clickStarted = true;
            }
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            return true;
        } else {
            return false;
        }
    }

    private Color getIconBackgroundColor(AbstractCard card) {
        switch(card.color.name()) {
            case "RED":
                return RED_BACKGROUND_COLOR;
            case "GREEN":
                return GREEN_BACKGROUND_COLOR;
            case "BLUE":
                return BLUE_BACKGROUND_COLOR;
            case "PURPLE":
                return PURPLE_BACKGROUND_COLOR;
            case "COLORLESS":
                return COLORLESS_BACKGROUND_COLOR;
            case "CURSE":
                return CURSE_BACKGROUND_COLOR;
            default:
                return new Color(-9849601);
        }
    }

    private Color getIconDescriptionColor(AbstractCard card) {
        switch(card.color.name()) {
            case "RED":
                return RED_DESCRIPTION_COLOR;
            case "GREEN":
                return GREEN_DESCRIPTION_COLOR;
            case "BLUE":
                return BLUE_DESCRIPTION_COLOR;
            case "PURPLE":
                return PURPLE_DESCRIPTION_COLOR;
            case "COLORLESS":
                return COLORLESS_DESCRIPTION_COLOR;
            case "CURSE":
                return CURSE_DESCRIPTION_COLOR;
            default:
                return new Color(-1303806465);
        }
    }

    private Color getIconBannerColor(AbstractCard card) {
        switch(card.rarity) {
            case BASIC:
            case SPECIAL:
            case COMMON:
            case CURSE:
                return COMMON_BANNER_COLOR;
            case UNCOMMON:
                return UNCOMMON_BANNER_COLOR;
            case RARE:
                return RARE_BANNER_COLOR;
            default:
                return COMMON_BANNER_COLOR;
        }
    }

    private Texture getIconPortrait(AbstractCard card) {
        switch(card.type) {
            case ATTACK:
                return ImageMaster.TINY_CARD_ATTACK;
            case POWER:
                return ImageMaster.TINY_CARD_POWER;
            case SKILL:
            case STATUS:
            case CURSE:
            default:
                return ImageMaster.TINY_CARD_SKILL;
        }
    }

    private void renderTinyCardIcon(SpriteBatch sb, AbstractCard card, float x, float y, float width, float height) {
        sb.setColor(this.getIconBackgroundColor(card));
        sb.draw(ImageMaster.TINY_CARD_BACKGROUND, x, y, width, height);
        sb.setColor(this.getIconDescriptionColor(card));
        sb.draw(ImageMaster.TINY_CARD_DESCRIPTION, x, y, width, height);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.TINY_CARD_PORTRAIT_SHADOW, x, y, width, height);
        sb.draw(this.getIconPortrait(card), x, y, width, height);
        sb.draw(ImageMaster.TINY_CARD_BANNER_SHADOW, x, y, width, height);
        sb.setColor(this.getIconBannerColor(card));
        sb.draw(ImageMaster.TINY_CARD_BANNER, x, y, width, height);
    }

    private static float scaled(float val) {
        return Settings.scale * val;
    }

    static {
        LINE_SPACING = 36.0F * Settings.scale;
        TEXT_LEADING_SPACE = scaled(60.0F);
    }
}


package com.megacrit.cardcrawl.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.helpers.CardColorBundle;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.mainMenu.SortHeaderButton;

import java.util.Comparator;

public class MasterDeckSortHeader extends CardLibSortHeader {
    private static final int BAR_W = 1334;
    private static final int BAR_H = 102;
    private static final Color BAR_COLOR = new Color(0.4F, 0.4F, 0.4F, 1.0F);
    private static final Color IRONCLAD_COLOR = new Color(0.5F, 0.1F, 0.1F, 1.0F);
    private static final Color SILENT_COLOR = new Color(0.25F, 0.55F, 0.0F, 1.0F);
    private static final Color DEFECT_COLOR = new Color(0.01F, 0.34F, 0.52F, 1.0F);
    private static final Comparator<AbstractCard> BY_TYPE = Comparator.comparing(a -> (a.type.name() + a.name));
    private static final Comparator<AbstractCard> ALPHA = Comparator.comparing(a -> a.name);
    private static final Comparator<AbstractCard> BY_COST = Comparator.comparing(a -> ("" + a.cost + a.name));
    private static final Comparator<AbstractCard> PURE_REVERSE = (a, b) -> a.cardID.equals(b.cardID) ? 0 : -1;
    private MasterDeckViewScreen masterDeckView;
    private float scrollY;

    public MasterDeckSortHeader(MasterDeckViewScreen masterDeckView) {
        super(null);
        this.masterDeckView = masterDeckView;
        this.buttons[0] = new SortHeaderButton(TEXT[5], START_X, 0.0F, this);
        this.buttons[0].setActive(true);
        float HB_W = this.buttons[0].hb.width;
        float leftSideOffset = (float)Settings.WIDTH / 2.0F - HB_W * (float)this.buttons.length / 2.0F;

        for(int i = 0; i < this.buttons.length; ++i) {
            SortHeaderButton button = this.buttons[i];
            button.hb.move(leftSideOffset + HB_W * (float)i + HB_W / 2.0F, button.hb.cY);
        }
    }

    public void didChangeOrder(SortHeaderButton button, boolean isAscending) {
        button.setActive(true);
        if (button == this.buttons[0]) {
            if (isAscending) {
                this.masterDeckView.setSortOrder(null);
            } else {
                this.masterDeckView.setSortOrder(PURE_REVERSE);
            }

        } else {
            Comparator<AbstractCard> order;
            if (button == this.buttons[1]) {
                order = BY_TYPE;
            } else if (button == this.buttons[2]) {
                order = BY_COST;
            } else {
                if (button != this.buttons[3]) {
                    return;
                }

                order = ALPHA;
            }

            if (!isAscending) {
                order = order.reversed();
            }

            this.masterDeckView.setSortOrder(order);
        }
    }

    protected void updateScrollPositions() {
    }

    public void render(SpriteBatch sb) {
        switch(AbstractDungeon.player.chosenClass.name()) {
            case "IRONCLAD":
                sb.setColor(IRONCLAD_COLOR);
                break;
            case "THE_SILENT":
                sb.setColor(SILENT_COLOR);
                break;
            case "DEFECT":
                sb.setColor(DEFECT_COLOR);
                break;
            default:
                CardColorBundle bundle = BaseMod.getColorBundleMap().getOrDefault(AbstractDungeon.player.getCardColor(), null);
                if (bundle != null) {
                    sb.setColor(bundle.bgColor.cpy());
                    break;
                }
                sb.setColor(BAR_COLOR);
        }

        sb.draw(ImageMaster.COLOR_TAB_BAR, (float)Settings.WIDTH / 2.0F - 667.0F, this.scrollY - 51.0F, 667.0F, 51.0F, 1334.0F, 102.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1334, 102, false, false);
        super.render(sb);
    }

    public void updateScrollPosition(float y) {
        this.scrollY = y + 240.0F * Settings.scale;
        SortHeaderButton[] var2 = this.buttons;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SortHeaderButton button = var2[var4];
            button.updateScrollPosition(this.scrollY);
        }

    }
}

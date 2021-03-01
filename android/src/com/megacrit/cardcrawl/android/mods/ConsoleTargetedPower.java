package com.megacrit.cardcrawl.android.mods;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.android.mods.interfaces.RenderSubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ConsoleTargetedPower implements RenderSubscriber, PostUpdateSubscriber {
    public static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(ConsoleTargetedPower.class);
    private Class<?> powerToApply;
    private AbstractCreature hoveredCreature;
    private Vector2 controlPoint;
    private float arrowScaleTimer;
    private Vector2[] points = new Vector2[20];
    private boolean isHidden;
    private int amount;

    public ConsoleTargetedPower(Class<?> power, int amount) {
        this.powerToApply = power;
        this.amount = amount;
        BaseMod.subscribe(this);
        this.isHidden = false;
        GameCursor.hidden = true;

        for(int i = 0; i < this.points.length; ++i) {
            this.points[i] = new Vector2();
        }

    }

    private void close() {
        this.isHidden = true;
    }

    @SuppressWarnings("ConstantConditions")
    private AbstractPower instantiatePower() {
        try {
            return (AbstractPower)this.powerToApply.getConstructor(AbstractCreature.class, Integer.TYPE, Boolean.TYPE).newInstance(this.hoveredCreature, this.amount, false);
        } catch (Exception var5) {
            try {
                return (AbstractPower)this.powerToApply.getConstructor(AbstractCreature.class, AbstractCreature.class, Integer.TYPE).newInstance(this.hoveredCreature, AbstractDungeon.player, this.amount);
            } catch (Exception var4) {
                try {
                    return (AbstractPower)this.powerToApply.getConstructor(AbstractCreature.class, Integer.TYPE).newInstance(this.hoveredCreature, this.amount);
                } catch (Exception var3) {
                    try {
                        return (AbstractPower)this.powerToApply.getConstructor(AbstractCreature.class).newInstance(this.hoveredCreature);
                    } catch (Exception var2) {
                        logger.info("Failed to instantiate " + this.powerToApply);
                        return null;
                    }
                }
            }
        }
    }

    private void updateTargetMode() {
        if (InputHelper.justClickedRight || AbstractDungeon.isScreenUp || (float)InputHelper.mY > (float)Settings.HEIGHT - 80.0F * Settings.scale || AbstractDungeon.player.hoveredCard != null || (float)InputHelper.mY < 140.0F * Settings.scale) {
            GameCursor.hidden = false;
            this.close();
        }

        this.hoveredCreature = null;

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.hb.hovered && !monster.isDying) {
                this.hoveredCreature = monster;
                break;
            }
        }

        AbstractCreature m = AbstractDungeon.player;
        if (m.hb.hovered && !m.isDying) {
            this.hoveredCreature = m;
        }

        if (InputHelper.justClickedLeft) {
            InputHelper.justClickedLeft = false;
            if (this.hoveredCreature != null) {
                AbstractPower power = this.instantiatePower();
                if (power != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.hoveredCreature, AbstractDungeon.player, power, this.amount));
                }
            }

            GameCursor.hidden = false;
            this.close();
        }

    }

    public void receiveRender(SpriteBatch sb) {
        this.render(sb);
    }

    public void render(SpriteBatch sb) {
        if (!this.isHidden) {
            this.renderTargetingUi(sb);
            if (this.hoveredCreature != null) {
                this.hoveredCreature.renderReticle(sb);
            }
        }

    }

    public void renderTargetingUi(SpriteBatch sb) {
        float x = (float)InputHelper.mX;
        float y = (float)InputHelper.mY;
        this.controlPoint = new Vector2(AbstractDungeon.player.animX - (x - AbstractDungeon.player.animX) / 4.0F, AbstractDungeon.player.animY + (y - AbstractDungeon.player.animY - 40.0F * Settings.scale) / 2.0F);
        float arrowScale;
        if (this.hoveredCreature == null) {
            arrowScale = Settings.scale;
            this.arrowScaleTimer = 0.0F;
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        } else {
            this.arrowScaleTimer += Gdx.graphics.getDeltaTime();
            if (this.arrowScaleTimer > 1.0F) {
                this.arrowScaleTimer = 1.0F;
            }

            arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, this.arrowScaleTimer);
            sb.setColor(new Color(1.0F, 0.2F, 0.3F, 1.0F));
        }

        Vector2 tmp = new Vector2(this.controlPoint.x - x, this.controlPoint.y - y);
        tmp.nor();
        this.drawCurvedLine(sb, new Vector2(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY - 40.0F * Settings.scale), new Vector2(x, y), this.controlPoint);
        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128.0F, y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, arrowScale, arrowScale, tmp.angle() + 90.0F, 0, 0, 256, 256, false, false);
    }

    private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {
        float radius = 7.0F * Settings.scale;

        for(int i = 0; i < this.points.length - 1; ++i) {
            this.points[i] = Bezier.quadratic(this.points[i], (float)i / 20.0F, start, control, end, new Vector2());
            radius += 0.4F * Settings.scale;
            float angle;
            Vector2 tmp;
            if (i != 0) {
                tmp = new Vector2(this.points[i - 1].x - this.points[i].x, this.points[i - 1].y - this.points[i].y);
                angle = tmp.nor().angle() + 90.0F;
            } else {
                tmp = new Vector2(this.controlPoint.x - this.points[i].x, this.controlPoint.y - this.points[i].y);
                angle = tmp.nor().angle() + 270.0F;
            }

            sb.draw(ImageMaster.TARGET_UI_CIRCLE, this.points[i].x - 64.0F, this.points[i].y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, angle, 0, 0, 128, 128, false, false);
        }

    }

    public void receivePostUpdate() {
        if (!this.isHidden) {
            this.updateTargetMode();
        }
    }
}

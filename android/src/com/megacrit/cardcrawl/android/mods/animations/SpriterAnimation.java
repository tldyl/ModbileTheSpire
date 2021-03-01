package com.megacrit.cardcrawl.android.mods.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.Point;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.LibGdx.LibGdxDrawer;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;

public class SpriterAnimation extends AbstractAnimation {
    public static boolean drawBones = false;
    private static final float animFps = 0.016666668F;
    private float frameRegulator = 0.0F;
    LibGdxLoader loader;
    LibGdxDrawer drawer;
    ShapeRenderer renderer = new ShapeRenderer();
    public PlayerTweener myPlayer;

    public SpriterAnimation(String filepath) {
        FileHandle handle = Gdx.files.internal(filepath);
        Data data = (new SCMLReader(handle.read())).getData();
        this.loader = new LibGdxLoader(data);
        this.loader.load(handle.file());
        this.drawer = new LibGdxDrawer(this.loader, this.renderer);
        this.myPlayer = new PlayerTweener(data.getEntity(0));
        this.myPlayer.setScale(Settings.scale);
    }

    public Type type() {
        return Type.SPRITE;
    }

    public void setFlip(boolean horizontal, boolean vertical) {
        if (horizontal && this.myPlayer.flippedX() > 0 || !horizontal && this.myPlayer.flippedX() < 0) {
            this.myPlayer.flipX();
        }

        if (vertical && this.myPlayer.flippedY() > 0 || !vertical && this.myPlayer.flippedY() < 0) {
            this.myPlayer.flipY();
        }

    }

    public void renderSprite(SpriteBatch batch, float x, float y) {
        this.drawer.batch = batch;

        for(this.frameRegulator += Gdx.graphics.getDeltaTime(); this.frameRegulator - 0.016666668F >= 0.0F; this.frameRegulator -= 0.016666668F) {
            this.myPlayer.update();
        }

        AbstractPlayer player = AbstractDungeon.player;
        if (player != null) {
            this.myPlayer.setPosition(new Point(x, y));
            this.drawer.draw(this.myPlayer);
            if (drawBones) {
                batch.end();
                this.renderer.setAutoShapeType(true);
                this.renderer.begin();
                this.drawer.drawBoxes(this.myPlayer);
                this.drawer.drawBones(this.myPlayer);
                this.renderer.end();
                batch.begin();
            }
        }
    }
}

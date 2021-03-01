package com.megacrit.cardcrawl.android.mods.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class G3DJAnimation extends AbstractAnimation {
    private String modelString;
    private String animationString;
    private ModelInstance myInstance;
    private AnimationController controller;
    private boolean rescaled = false;

    public G3DJAnimation(String model, String animation) {
        this.modelString = model;
        this.animationString = animation;
        if (this.modelString != null) {
            try {
                JsonReader jsonReader = new JsonReader();
                G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
                Model myModel = modelLoader.loadModel(Gdx.files.internal(this.modelString));

                for (Material mat : myModel.materials) {
                    mat.set(new BlendingAttribute(770, 771));
                }

                this.myInstance = new ModelInstance(myModel, 0.0F, 0.0F, 10.0F);
                this.myInstance.transform.rotate(1.0F, 0.0F, 0.0F, -90.0F);
                if (this.animationString != null) {
                    this.controller = new AnimationController(this.myInstance);
                    this.controller.setAnimation(this.animationString, 1, new AnimationController.AnimationListener() {
                        public void onEnd(AnimationController.AnimationDesc animation) {
                            G3DJAnimation.this.controller.queue(G3DJAnimation.this.animationString, -1, 1.0F, (AnimationController.AnimationListener)null, 0.0F);
                        }

                        public void onLoop(AnimationController.AnimationDesc animation) {
                        }
                    });
                }
            } catch (Exception var7) {
                var7.printStackTrace();
                Gdx.app.exit();
            }
        }
    }

    public Type type() {
        return this.modelString != null ? Type.MODEL : Type.NONE;
    }

    public void renderModel(ModelBatch batch, Environment env) {
        if (this.animationString != null) {
            this.controller.update(Gdx.graphics.getDeltaTime());
        }

        Vector3 loc = this.myInstance.transform.getTranslation(new Vector3());
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null) {
            loc.x = player.drawX + player.animX - (float)(Gdx.graphics.getWidth() / 2);
            loc.y = player.drawY + player.animY + AbstractDungeon.sceneOffsetY - (float)(Gdx.graphics.getHeight() / 2);
            this.myInstance.transform.setTranslation(loc);
            if (!this.rescaled) {
                this.myInstance.transform.scale(Settings.scale, 1.0F, Settings.scale);
                this.rescaled = true;
            }

            batch.render(this.myInstance, env);
        }
    }
}

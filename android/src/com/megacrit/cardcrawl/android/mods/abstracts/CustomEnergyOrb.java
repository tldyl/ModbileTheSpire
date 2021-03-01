package com.megacrit.cardcrawl.android.mods.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.BuildConfig;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;

public class CustomEnergyOrb implements EnergyOrbInterface {
    private static final int ORB_W = 128;
    private static final float ORB_IMG_SCALE;
    protected Texture baseLayer;
    protected Texture[] energyLayers;
    protected Texture[] noEnergyLayers;
    protected Texture orbVfx;
    protected float[] layerSpeeds;
    protected float[] angles;

    public CustomEnergyOrb(String modId, String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds) {
        if (orbTexturePaths != null && orbVfxPath != null) {
            if (BuildConfig.DEBUG && orbTexturePaths.length < 3) {
                throw new AssertionError();
            }

            if (BuildConfig.DEBUG && orbTexturePaths.length % 2 != 1) {
                throw new AssertionError();
            }

            int middleIdx = orbTexturePaths.length / 2;
            System.out.println(middleIdx);
            this.energyLayers = new Texture[middleIdx];
            this.noEnergyLayers = new Texture[middleIdx];
            this.baseLayer = AssetLoader.getTexture(modId, orbTexturePaths[middleIdx]);

            for(int i = 0; i < middleIdx; ++i) {
                this.energyLayers[i] = AssetLoader.getTexture(modId, orbTexturePaths[i]);
                this.noEnergyLayers[i] = AssetLoader.getTexture(modId, orbTexturePaths[i + middleIdx + 1]);
            }

            this.orbVfx = AssetLoader.getTexture(modId, orbVfxPath);
        } else {
            this.energyLayers = new Texture[5];
            this.noEnergyLayers = new Texture[5];
            this.baseLayer = ImageMaster.ENERGY_RED_LAYER6;
            this.energyLayers[0] = ImageMaster.ENERGY_RED_LAYER1;
            this.energyLayers[1] = ImageMaster.ENERGY_RED_LAYER2;
            this.energyLayers[2] = ImageMaster.ENERGY_RED_LAYER3;
            this.energyLayers[3] = ImageMaster.ENERGY_RED_LAYER4;
            this.energyLayers[4] = ImageMaster.ENERGY_RED_LAYER5;
            this.noEnergyLayers[0] = ImageMaster.ENERGY_RED_LAYER1D;
            this.noEnergyLayers[1] = ImageMaster.ENERGY_RED_LAYER2D;
            this.noEnergyLayers[2] = ImageMaster.ENERGY_RED_LAYER3D;
            this.noEnergyLayers[3] = ImageMaster.ENERGY_RED_LAYER4D;
            this.noEnergyLayers[4] = ImageMaster.ENERGY_RED_LAYER5D;
            this.orbVfx = ImageMaster.RED_ORB_FLASH_VFX;
        }

        if (layerSpeeds == null) {
            layerSpeeds = new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
        }

        this.layerSpeeds = layerSpeeds;
        this.angles = new float[this.layerSpeeds.length];

        if (BuildConfig.DEBUG && this.energyLayers.length != this.layerSpeeds.length) {
            throw new AssertionError();
        }
    }

    public Texture getEnergyImage() {
        return this.orbVfx;
    }

    public void updateOrb(int energyCount) {
        if (energyCount == 0) {
            this.angles[4] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[0] / 4.0F;
            this.angles[3] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[1] / 4.0F;
            this.angles[2] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[2] / 4.0F;
            this.angles[1] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[3] / 4.0F;
            this.angles[0] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[4] / 4.0F;
        } else {
            this.angles[4] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[0];
            this.angles[3] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[1];
            this.angles[2] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[2];
            this.angles[1] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[3];
            this.angles[0] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[4];
        }

    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.setColor(Color.WHITE);
        int i;
        if (enabled) {
            for(i = 0; i < this.energyLayers.length; ++i) {
                sb.draw(this.energyLayers[i], current_x - ORB_W / 2, current_y - ORB_W / 2, ORB_W / 2, ORB_W / 2, ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, ORB_W, ORB_W, false, false);
            }
        } else {
            for(i = 0; i < this.noEnergyLayers.length; ++i) {
                sb.draw(this.noEnergyLayers[i], current_x - ORB_W / 2, current_y - ORB_W / 2, ORB_W / 2, ORB_W / 2, ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, ORB_W, ORB_W, false, false);
            }
        }

        sb.draw(this.baseLayer, current_x - ORB_W / 2, current_y - ORB_W / 2, ORB_W / 2, ORB_W / 2, ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, ORB_W, ORB_W, false, false);
    }

    static {
        ORB_IMG_SCALE = 1.15F * Settings.scale;
    }
}

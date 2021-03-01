package com.megacrit.cardcrawl.android.mods.animations;

public class SpineAnimation extends AbstractAnimation {
    public String atlasUrl;
    public String skeletonUrl;
    public float scale;

    public SpineAnimation(String atlasUrl, String skeletonUrl, float scale) {
        this.atlasUrl = atlasUrl;
        this.skeletonUrl = skeletonUrl;
        this.scale = scale;
    }

    public Type type() {
        return Type.NONE;
    }
}

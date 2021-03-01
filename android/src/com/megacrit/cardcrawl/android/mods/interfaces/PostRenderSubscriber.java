package com.megacrit.cardcrawl.android.mods.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface PostRenderSubscriber extends ISubscriber {
    void receivePostRender(SpriteBatch sb);
}

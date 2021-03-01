package com.megacrit.cardcrawl.android.mods.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface PreRenderSubscriber extends ISubscriber {
    void receiveCameraRender(OrthographicCamera camera);
}

package com.megacrit.cardcrawl.android.mods.interfaces;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface ModelRenderSubscriber extends ISubscriber {
    void receiveModelRender(ModelBatch mb, Environment env);
}
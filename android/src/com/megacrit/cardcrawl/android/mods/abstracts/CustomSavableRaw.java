package com.megacrit.cardcrawl.android.mods.abstracts;

import com.google.gson.JsonElement;

public interface CustomSavableRaw {
    JsonElement onSaveRaw();

    void onLoadRaw(JsonElement paramJsonElement);
}

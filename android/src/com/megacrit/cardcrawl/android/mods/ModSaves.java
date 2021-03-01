package com.megacrit.cardcrawl.android.mods;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

public class ModSaves {
    public static class ArrayListOfJsonElement extends ArrayList<JsonElement> {}

    public static class ArrayListOfString extends ArrayList<String> {}

    public static class HashMapOfJsonElement extends HashMap<String, JsonElement> {}
}

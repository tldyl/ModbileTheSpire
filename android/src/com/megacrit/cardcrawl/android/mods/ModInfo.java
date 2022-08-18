package com.megacrit.cardcrawl.android.mods;

import java.net.URL;
import java.util.Map;

public class ModInfo {
    public String modId;
    public String modName;
    public String modVersion;
    public Map<String, String[]> descriptions;
    public String[] authorList;
    public String[] dependencies;
    public String mainClassPath;

    public transient boolean isLoaded;
    public transient URL jarURL;
}

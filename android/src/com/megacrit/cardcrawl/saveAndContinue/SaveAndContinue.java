package com.megacrit.cardcrawl.saveAndContinue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.STSSentry;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.exceptions.SaveFileLoadError;
import com.megacrit.cardcrawl.helpers.AsyncSaver;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import java.io.File;
import java.util.HashMap;

public class SaveAndContinue {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(SaveAndContinue.class);
    public static final String SAVE_PATH;

    public SaveAndContinue() {
    }

    public static String getPlayerSavePath(PlayerClass c) {
        StringBuilder sb = new StringBuilder();
        sb.append(SAVE_PATH);
        if (CardCrawlGame.saveSlot != 0) {
            sb.append(CardCrawlGame.saveSlot).append("_");
        }

        sb.append(c.name()).append(".autosave");
        return sb.toString();
    }

    public static boolean saveExistsAndNotCorrupted(AbstractPlayer p) {
        String filepath = getPlayerSavePath(p.chosenClass);
        boolean fileExists = Gdx.files.external(filepath).exists();
        if (fileExists) {
            try {
                loadSaveFile(filepath);
            } catch (SaveFileLoadError var4) {
                deleteSave(p);
                logger.info(p.chosenClass.name() + " save INVALID!");
                return false;
            }

            logger.info(p.chosenClass.name() + " save exists and is valid.");
            return true;
        } else {
            logger.info(p.chosenClass.name() + " save does NOT exist!");
            return false;
        }
    }

    public static String loadSaveString(PlayerClass c) {
        return loadSaveString(getPlayerSavePath(c));
    }

    private static String loadSaveString(String filePath) {
        FileHandle file = Gdx.files.external(filePath);
        String data = file.readString();
        return SaveFileObfuscator.isObfuscated(data) ? SaveFileObfuscator.decode(data, "key") : data;
    }

    public static SaveFile loadSaveFile(PlayerClass c) {
        String fileName = getPlayerSavePath(c);

        try {
            return loadSaveFile(fileName);
        } catch (SaveFileLoadError e) {
            logger.info("Exception occurred while loading save!");
            //ExceptionHandler.handleException(e, logger);
            e.printStackTrace();
            CardCrawlGame.writeExceptionToFile(e);
            Gdx.app.exit();
            return null;
        }
    }

    private static SaveFile loadSaveFile(String filePath) throws SaveFileLoadError {
        SaveFile saveFile = null;
        Gson gson = new Gson();
        String savestr = null;
        Exception err = null;

        try {
            savestr = loadSaveString(filePath);
            saveFile = gson.fromJson(savestr, SaveFile.class);
        } catch (Exception var6) {
            STSSentry.attachToEvent("savefile", saveFile);
            STSSentry.attachToEvent("savestr", savestr);
            if (Gdx.files.external(filePath).exists()) {
                SaveHelper.preserveCorruptFile(filePath);
            }

            err = var6;
            if (!filePath.endsWith(".backUp")) {
                logger.info(filePath + " was corrupt, loading backup...");
                return loadSaveFile(filePath + ".backUp");
            }
        }

        if (saveFile == null) {
            throw new SaveFileLoadError("Unable to load save file: " + filePath, err);
        } else {
            logger.info(filePath + " save file was successfully loaded.");
            return saveFile;
        }
    }

    public static void save(SaveFile save) {
        CardCrawlGame.loadingSave = false;
        HashMap<Object, Object> params = new HashMap<>();
        params.put("name", save.name);
        params.put("loadout", save.loadout);
        params.put("current_health", save.current_health);
        params.put("max_health", save.max_health);
        params.put("max_orbs", save.max_orbs);
        params.put("gold", save.gold);
        params.put("hand_size", save.hand_size);
        params.put("red", save.red);
        params.put("green", save.green);
        params.put("blue", save.blue);
        params.put("monsters_killed", save.monsters_killed);
        params.put("elites1_killed", save.elites1_killed);
        params.put("elites2_killed", save.elites2_killed);
        params.put("elites3_killed", save.elites3_killed);
        params.put("gold_gained", save.gold_gained);
        params.put("mystery_machine", save.mystery_machine);
        params.put("champions", save.champions);
        params.put("perfect", save.perfect);
        params.put("overkill", save.overkill);
        params.put("combo", save.combo);
        params.put("cards", save.cards);
        params.put("obtained_cards", save.obtained_cards);
        params.put("relics", save.relics);
        params.put("relic_counters", save.relic_counters);
        params.put("potions", save.potions);
        params.put("potion_slots", save.potion_slots);
        params.put("is_endless_mode", save.is_endless_mode);
        params.put("blights", save.blights);
        params.put("blight_counters", save.blight_counters);
        params.put("endless_increments", save.endless_increments);
        params.put("chose_neow_reward", save.chose_neow_reward);
        params.put("neow_bonus", save.neow_bonus);
        params.put("neow_cost", save.neow_cost);
        params.put("is_ascension_mode", save.is_ascension_mode);
        params.put("ascension_level", save.ascension_level);
        params.put("level_name", save.level_name);
        params.put("floor_num", save.floor_num);
        params.put("act_num", save.act_num);
        params.put("event_list", save.event_list);
        params.put("one_time_event_list", save.one_time_event_list);
        params.put("potion_chance", save.potion_chance);
        params.put("event_chances", save.event_chances);
        params.put("monster_list", save.monster_list);
        params.put("elite_monster_list", save.elite_monster_list);
        params.put("boss_list", save.boss_list);
        params.put("play_time", save.play_time);
        params.put("save_date", save.save_date);
        params.put("seed", save.seed);
        params.put("special_seed", save.special_seed);
        params.put("seed_set", save.seed_set);
        params.put("is_daily", save.is_daily);
        params.put("is_final_act_on", save.is_final_act_on);
        params.put("has_ruby_key", save.has_ruby_key);
        params.put("has_emerald_key", save.has_emerald_key);
        params.put("has_sapphire_key", save.has_sapphire_key);
        params.put("daily_date", save.daily_date);
        params.put("is_trial", save.is_trial);
        params.put("daily_mods", save.daily_mods);
        params.put("custom_mods", save.custom_mods);
        params.put("boss", save.boss);
        params.put("purgeCost", save.purgeCost);
        params.put("monster_seed_count", save.monster_seed_count);
        params.put("event_seed_count", save.event_seed_count);
        params.put("merchant_seed_count", save.merchant_seed_count);
        params.put("card_seed_count", save.card_seed_count);
        params.put("treasure_seed_count", save.treasure_seed_count);
        params.put("relic_seed_count", save.relic_seed_count);
        params.put("potion_seed_count", save.potion_seed_count);
        params.put("ai_seed_count", save.ai_seed_count);
        params.put("shuffle_seed_count", save.shuffle_seed_count);
        params.put("card_random_seed_count", save.card_random_seed_count);
        params.put("card_random_seed_randomizer", save.card_random_seed_randomizer);
        params.put("path_x", save.path_x);
        params.put("path_y", save.path_y);
        params.put("room_x", save.room_x);
        params.put("room_y", save.room_y);
        params.put("spirit_count", save.spirit_count);
        params.put("current_room", save.current_room);
        params.put("common_relics", save.common_relics);
        params.put("uncommon_relics", save.uncommon_relics);
        params.put("rare_relics", save.rare_relics);
        params.put("shop_relics", save.shop_relics);
        params.put("boss_relics", save.boss_relics);
        params.put("post_combat", save.post_combat);
        params.put("mugged", save.mugged);
        params.put("smoked", save.smoked);
        params.put("combat_rewards", save.combat_rewards);
        if (AbstractDungeon.player.hasRelic("Bottled Flame")) {
            saveBottle(params, "Bottled Flame", "bottled_flame", ((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card);
        } else {
            params.put("bottled_flame", null);
        }

        if (AbstractDungeon.player.hasRelic("Bottled Lightning")) {
            saveBottle(params, "Bottled Lightning", "bottled_lightning", ((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card);
        } else {
            params.put("bottled_lightning", null);
        }

        if (AbstractDungeon.player.hasRelic("Bottled Tornado")) {
            saveBottle(params, "Bottled Tornado", "bottled_tornado", ((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card);
        } else {
            params.put("bottled_tornado", null);
        }

        params.put("metric_campfire_rested", save.metric_campfire_rested);
        params.put("metric_campfire_upgraded", save.metric_campfire_upgraded);
        params.put("metric_campfire_rituals", save.metric_campfire_rituals);
        params.put("metric_campfire_meditates", save.metric_campfire_meditates);
        params.put("metric_purchased_purges", save.metric_purchased_purges);
        params.put("metric_potions_floor_spawned", save.metric_potions_floor_spawned);
        params.put("metric_potions_floor_usage", save.metric_potions_floor_usage);
        params.put("metric_current_hp_per_floor", save.metric_current_hp_per_floor);
        params.put("metric_max_hp_per_floor", save.metric_max_hp_per_floor);
        params.put("metric_gold_per_floor", save.metric_gold_per_floor);
        params.put("metric_path_per_floor", save.metric_path_per_floor);
        params.put("metric_path_taken", save.metric_path_taken);
        params.put("metric_items_purchased", save.metric_items_purchased);
        params.put("metric_item_purchase_floors", save.metric_item_purchase_floors);
        params.put("metric_items_purged", save.metric_items_purged);
        params.put("metric_items_purged_floors", save.metric_items_purged_floors);
        params.put("metric_card_choices", save.metric_card_choices);
        params.put("metric_event_choices", save.metric_event_choices);
        params.put("metric_boss_relics", save.metric_boss_relics);
        params.put("metric_damage_taken", save.metric_damage_taken);
        params.put("metric_potions_obtained", save.metric_potions_obtained);
        params.put("metric_relics_obtained", save.metric_relics_obtained);
        params.put("metric_campfire_choices", save.metric_campfire_choices);
        params.put("metric_build_version", save.metric_build_version);
        params.put("metric_seed_played", save.metric_seed_played);
        params.put("metric_floor_reached", save.metric_floor_reached);
        params.put("metric_playtime", save.metric_playtime);
        params.put("basemod:mod_saves", save.modSaves);
        params.put("basemod:mod_card_saves", save.modCardSaves);
        params.put("basemod:mod_relic_saves", save.modRelicSaves);
        params.put("basemod:mod_potion_saves", save.modPotionSaves);
        params.put("basemod:abstract_card_modifiers_save", save.cardModifierSaves);
        params.put("basemod:event_saves", save.eventSaves);
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        String data = gson.toJson(params);
        String filepath = getPlayerSavePath(AbstractDungeon.player.chosenClass);
        if (Settings.isBeta) {
            AsyncSaver.save(filepath + "BETA", data);
        }

        AsyncSaver.save(filepath, SaveFileObfuscator.encode(data, "key"));
    }

    private static void saveBottle(HashMap<Object, Object> params, String bottleId, String save_name, AbstractCard card) {
        if (AbstractDungeon.player.hasRelic(bottleId)) {
            if (card != null) {
                params.put(save_name, card.cardID);
                params.put(save_name + "_upgrade", card.timesUpgraded);
                params.put(save_name + "_misc", card.misc);
            } else {
                params.put(save_name, null);
            }
        } else {
            params.put(save_name, null);
        }

    }

    public static void deleteSave(AbstractPlayer p) {
        String savePath = p.getSaveFilePath();
        logger.info("DELETING " + savePath + " SAVE");
        Gdx.files.external(savePath).delete();
        Gdx.files.external(savePath + ".backUp").delete();
    }

    static {
        SAVE_PATH = "saves" + File.separator;
    }
}

package com.megacrit.cardcrawl.saveAndContinue;

import com.google.gson.annotations.SerializedName;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.ModSaves;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomSavableRaw;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.*;

public class SaveFile {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(SaveFile.class);
    public String name;
    public String loadout;
    public int current_health;
    public int max_health;
    public int max_orbs;
    public int gold;
    public int hand_size;
    public int potion_slots;
    public int red;
    public int green;
    public int blue;
    public ArrayList<CardSave> cards;
    public HashMap<String, Integer> obtained_cards;
    public ArrayList<String> relics;
    public ArrayList<Integer> relic_counters;
    public ArrayList<String> blights;
    public ArrayList<Integer> blight_counters;
    public ArrayList<String> potions;
    public boolean is_ascension_mode;
    public int ascension_level;
    public boolean chose_neow_reward;
    public String level_name;
    public long play_time;
    public long save_date;
    public long daily_date;
    public int floor_num;
    public int act_num;
    public long seed;
    public long special_seed;
    public boolean seed_set;
    public boolean is_trial;
    public boolean is_daily;
    public boolean is_final_act_on;
    public boolean has_ruby_key;
    public boolean has_emerald_key;
    public boolean has_sapphire_key;
    public ArrayList<String> custom_mods;
    public ArrayList<String> daily_mods;
    public int monster_seed_count;
    public int event_seed_count;
    public int merchant_seed_count;
    public int card_seed_count;
    public int treasure_seed_count;
    public int relic_seed_count;
    public int potion_seed_count;
    public int monster_hp_seed_count;
    public int ai_seed_count;
    public int shuffle_seed_count;
    public int card_random_seed_count;
    public int card_random_seed_randomizer;
    public int potion_chance;
    public int purgeCost;
    public ArrayList<String> monster_list;
    public ArrayList<String> elite_monster_list;
    public ArrayList<String> boss_list;
    public ArrayList<String> event_list;
    public ArrayList<String> one_time_event_list;
    public ArrayList<Float> event_chances;
    public ArrayList<Integer> path_x;
    public ArrayList<Integer> path_y;
    public int room_x;
    public int room_y;
    public int spirit_count;
    public String boss;
    public String current_room;
    public ArrayList<String> common_relics;
    public ArrayList<String> uncommon_relics;
    public ArrayList<String> rare_relics;
    public ArrayList<String> shop_relics;
    public ArrayList<String> boss_relics;
    public String bottled_flame;
    public String bottled_lightning;
    public String bottled_tornado;
    public int bottled_flame_upgrade;
    public int bottled_lightning_upgrade;
    public int bottled_tornado_upgrade;
    public int bottled_flame_misc;
    public int bottled_lightning_misc;
    public int bottled_tornado_misc;
    public boolean is_endless_mode;
    public ArrayList<Integer> endless_increments;
    public boolean post_combat;
    public boolean mugged;
    public boolean smoked;
    public ArrayList<RewardSave> combat_rewards;
    public int monsters_killed;
    public int elites1_killed;
    public int elites2_killed;
    public int elites3_killed;
    public int champions;
    public int perfect;
    public boolean overkill;
    public boolean combo;
    public boolean cheater;
    public int gold_gained;
    public int mystery_machine;
    public int metric_campfire_rested;
    public int metric_campfire_upgraded;
    public int metric_campfire_rituals;
    public int metric_campfire_meditates;
    public int metric_purchased_purges;
    public ArrayList<Integer> metric_potions_floor_spawned;
    public ArrayList<Integer> metric_potions_floor_usage;
    public ArrayList<Integer> metric_current_hp_per_floor;
    public ArrayList<Integer> metric_max_hp_per_floor;
    public ArrayList<Integer> metric_gold_per_floor;
    public ArrayList<String> metric_path_per_floor;
    public ArrayList<String> metric_path_taken;
    public ArrayList<String> metric_items_purchased;
    public ArrayList<Integer> metric_item_purchase_floors;
    public ArrayList<String> metric_items_purged;
    public ArrayList<Integer> metric_items_purged_floors;
    public ArrayList<HashMap> metric_card_choices;
    public ArrayList<HashMap> metric_event_choices;
    public ArrayList<HashMap> metric_boss_relics;
    public ArrayList<HashMap> metric_damage_taken;
    public ArrayList<HashMap> metric_potions_obtained;
    public ArrayList<HashMap> metric_relics_obtained;
    public ArrayList<HashMap> metric_campfire_choices;
    public String metric_build_version;
    public String metric_seed_played;
    public int metric_floor_reached;
    public long metric_playtime;
    public String neow_bonus;
    public String neow_cost;

    @SerializedName("basemod:mod_saves")
    public ModSaves.HashMapOfJsonElement modSaves;
    @SerializedName("basemod:mod_card_saves")
    public ModSaves.ArrayListOfJsonElement modCardSaves;
    @SerializedName("basemod:mod_relic_saves")
    public ModSaves.ArrayListOfJsonElement modRelicSaves;
    @SerializedName("basemod:mod_potion_saves")
    public ModSaves.ArrayListOfJsonElement modPotionSaves;
    @SerializedName("basemod:abstract_card_modifiers_save")
    public ModSaves.ArrayListOfJsonElement cardModifierSaves;
    @SerializedName("basemod:event_saves")
    public ModSaves.ArrayListOfString eventSaves;

    public SaveFile() {
    }

    public SaveFile(SaveFile.SaveType type) {
        ModSaves.ArrayListOfJsonElement modCardSaves = new ModSaves.ArrayListOfJsonElement();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof CustomSavableRaw) {
                modCardSaves.add(((CustomSavableRaw)card).onSaveRaw());
                continue;
            }
            modCardSaves.add(null);
        }
        this.modCardSaves = modCardSaves;
        ModSaves.ArrayListOfJsonElement cardModifierSaves = new ModSaves.ArrayListOfJsonElement();
        //TODO
        this.cardModifierSaves = cardModifierSaves;
        ModSaves.ArrayListOfJsonElement modRelicSaves = new ModSaves.ArrayListOfJsonElement();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof CustomSavableRaw) {
                modRelicSaves.add(((CustomSavableRaw)relic).onSaveRaw());
                continue;
            }
            modRelicSaves.add(null);
        }
        this.modRelicSaves = modRelicSaves;
        ModSaves.ArrayListOfJsonElement modPotionSaves = new ModSaves.ArrayListOfJsonElement();
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion instanceof CustomSavableRaw) {
                modPotionSaves.add(((CustomSavableRaw)potion).onSaveRaw());
                continue;
            }
            modPotionSaves.add(null);
        }
        this.modPotionSaves = modPotionSaves;
        ModSaves.HashMapOfJsonElement modSaves = new ModSaves.HashMapOfJsonElement();
        for (Map.Entry<String, CustomSavableRaw> field : BaseMod.getSaveFields().entrySet()) {
            modSaves.put(field.getKey(), field.getValue().onSaveRaw());
        }
        this.modSaves = modSaves;
        ModSaves.ArrayListOfString eventSaves = new ModSaves.ArrayListOfString();
        eventSaves.addAll(AbstractDungeon.player.seenEvents);
        this.eventSaves = eventSaves;

        AbstractPlayer p = AbstractDungeon.player;
        this.name = p.name;
        this.current_health = p.currentHealth;
        this.max_health = p.maxHealth;
        this.max_orbs = p.masterMaxOrbs;
        this.gold = p.gold;
        this.hand_size = p.masterHandSize;
        this.red = p.energy.energyMaster;
        this.green = 0;
        this.blue = 0;
        this.monsters_killed = CardCrawlGame.monstersSlain;
        this.elites1_killed = CardCrawlGame.elites1Slain;
        this.elites2_killed = CardCrawlGame.elites2Slain;
        this.elites3_killed = CardCrawlGame.elites3Slain;
        this.champions = CardCrawlGame.champion;
        this.perfect = CardCrawlGame.perfect;
        this.overkill = CardCrawlGame.overkill;
        this.combo = CardCrawlGame.combo;
        this.cheater = CardCrawlGame.cheater;
        this.gold_gained = CardCrawlGame.goldGained;
        this.mystery_machine = CardCrawlGame.mysteryMachine;
        this.play_time = (long)CardCrawlGame.playtime;
        this.cards = p.masterDeck.getCardDeck();
        this.obtained_cards = CardHelper.obtainedCards;
        this.relics = new ArrayList<>();
        this.relic_counters = new ArrayList<>();

        for (AbstractRelic r : p.relics) {
            this.relics.add(r.relicId);
            this.relic_counters.add(r.counter);
        }

        this.is_endless_mode = Settings.isEndless;
        this.blights = new ArrayList<>();
        this.blight_counters = new ArrayList<>();

        for (AbstractBlight b : p.blights) {
            this.blights.add(b.blightID);
            this.blight_counters.add(b.counter);
        }

        this.endless_increments = new ArrayList<>();

        for (AbstractBlight b : p.blights) {
            this.endless_increments.add(b.increment);
        }

        this.potion_slots = AbstractDungeon.player.potionSlots;
        this.potions = new ArrayList<>();

        for (AbstractPotion pot : p.potions) {
            this.potions.add(pot.ID);
        }

        this.is_ascension_mode = AbstractDungeon.isAscensionMode;
        this.ascension_level = AbstractDungeon.ascensionLevel;
        this.chose_neow_reward = false;
        this.level_name = AbstractDungeon.id;
        this.floor_num = AbstractDungeon.floorNum;
        this.act_num = AbstractDungeon.actNum;
        this.monster_list = AbstractDungeon.monsterList;
        this.elite_monster_list = AbstractDungeon.eliteMonsterList;
        this.boss_list = AbstractDungeon.bossList;
        this.event_list = AbstractDungeon.eventList;
        this.one_time_event_list = AbstractDungeon.specialOneTimeEventList;
        this.potion_chance = AbstractRoom.blizzardPotionMod;
        this.event_chances = type == SaveFile.SaveType.POST_COMBAT ? EventHelper.getChancesPreRoll() : EventHelper.getChances();
        this.save_date = Calendar.getInstance().getTimeInMillis();
        this.seed = Settings.seed;
        if (Settings.specialSeed != null) {
            this.special_seed = Settings.specialSeed;
        }

        this.seed_set = Settings.seedSet;
        this.is_daily = Settings.isDailyRun;
        this.is_final_act_on = Settings.isFinalActAvailable;
        this.has_ruby_key = Settings.hasRubyKey;
        this.has_emerald_key = Settings.hasEmeraldKey;
        this.has_sapphire_key = Settings.hasSapphireKey;
        this.daily_date = Settings.dailyDate;
        this.is_trial = Settings.isTrial;
        this.daily_mods = ModHelper.getEnabledModIDs();
        if (AbstractPlayer.customMods == null) {
            if (CardCrawlGame.trial != null) {
                AbstractPlayer.customMods = CardCrawlGame.trial.dailyModIDs();
            } else {
                AbstractPlayer.customMods = new ArrayList<>();
            }
        }

        this.custom_mods = AbstractPlayer.customMods;
        this.boss = AbstractDungeon.bossKey;
        this.purgeCost = ShopScreen.purgeCost;
        this.monster_seed_count = AbstractDungeon.monsterRng.counter;
        this.event_seed_count = AbstractDungeon.eventRng.counter;
        this.merchant_seed_count = AbstractDungeon.merchantRng.counter;
        this.card_seed_count = AbstractDungeon.cardRng.counter;
        this.card_random_seed_randomizer = AbstractDungeon.cardBlizzRandomizer;
        this.treasure_seed_count = AbstractDungeon.treasureRng.counter;
        this.relic_seed_count = AbstractDungeon.relicRng.counter;
        this.potion_seed_count = AbstractDungeon.potionRng.counter;
        this.path_x = AbstractDungeon.pathX;
        this.path_y = AbstractDungeon.pathY;
        if (AbstractDungeon.nextRoom != null && type != SaveFile.SaveType.ENDLESS_NEOW) {
            this.room_x = AbstractDungeon.nextRoom.x;
            this.room_y = AbstractDungeon.nextRoom.y;
            this.current_room = AbstractDungeon.nextRoom.room.getClass().getName();
        } else {
            this.room_x = AbstractDungeon.getCurrMapNode().x;
            this.room_y = AbstractDungeon.getCurrMapNode().y;
            this.current_room = AbstractDungeon.getCurrRoom().getClass().getName();
        }

        this.spirit_count = AbstractDungeon.bossCount;
        logger.info("Next Room: " + this.current_room);
        this.common_relics = AbstractDungeon.commonRelicPool;
        this.uncommon_relics = AbstractDungeon.uncommonRelicPool;
        this.rare_relics = AbstractDungeon.rareRelicPool;
        this.shop_relics = AbstractDungeon.shopRelicPool;
        this.boss_relics = AbstractDungeon.bossRelicPool;
        this.post_combat = false;
        this.mugged = false;
        this.smoked = false;
        switch(type) {
            case AFTER_BOSS_RELIC:
            case ENTER_ROOM:
            default:
                break;
            case POST_COMBAT:
                this.post_combat = true;
                this.mugged = AbstractDungeon.getCurrRoom().mugged;
                this.smoked = AbstractDungeon.getCurrRoom().smoked;
                this.combat_rewards = new ArrayList<>();
                for (RewardItem i : AbstractDungeon.getCurrRoom().rewards) {
                    switch(i.type) {
                        case SAPPHIRE_KEY:
                        case EMERALD_KEY:
                        case CARD:
                            this.combat_rewards.add(new RewardSave(i.type.toString(), null));
                            break;
                        case GOLD:
                            this.combat_rewards.add(new RewardSave(i.type.toString(), null, i.goldAmt, i.bonusGold));
                            break;
                        case POTION:
                            this.combat_rewards.add(new RewardSave(i.type.toString(), i.potion.ID));
                            break;
                        case RELIC:
                            this.combat_rewards.add(new RewardSave(i.type.toString(), i.relic.relicId));
                            break;
                        case STOLEN_GOLD:
                            this.combat_rewards.add(new RewardSave(i.type.toString(), null, i.goldAmt, 0));
                    }
                }
            case POST_NEOW:
                this.chose_neow_reward = true;
        }

        if (AbstractDungeon.player.hasRelic("Bottled Flame")) {
            if (((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card != null) {
                this.bottled_flame = ((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).card.cardID;
            } else {
                this.bottled_flame = null;
            }
        } else {
            this.bottled_flame = null;
        }

        if (AbstractDungeon.player.hasRelic("Bottled Lightning")) {
            if (((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card != null) {
                this.bottled_lightning = ((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).card.cardID;
            } else {
                this.bottled_lightning = null;
            }
        } else {
            this.bottled_lightning = null;
        }

        if (AbstractDungeon.player.hasRelic("Bottled Tornado")) {
            if (((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card != null) {
                this.bottled_tornado = ((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).card.cardID;
            } else {
                this.bottled_tornado = null;
            }
        } else {
            this.bottled_tornado = null;
        }

        this.metric_campfire_rested = CardCrawlGame.metricData.campfire_rested;
        this.metric_campfire_upgraded = CardCrawlGame.metricData.campfire_upgraded;
        this.metric_purchased_purges = CardCrawlGame.metricData.purchased_purges;
        this.metric_potions_floor_spawned = CardCrawlGame.metricData.potions_floor_spawned;
        this.metric_potions_floor_usage = CardCrawlGame.metricData.potions_floor_usage;
        this.metric_current_hp_per_floor = CardCrawlGame.metricData.current_hp_per_floor;
        this.metric_max_hp_per_floor = CardCrawlGame.metricData.max_hp_per_floor;
        this.metric_gold_per_floor = CardCrawlGame.metricData.gold_per_floor;
        this.metric_path_per_floor = CardCrawlGame.metricData.path_per_floor;
        this.metric_path_taken = CardCrawlGame.metricData.path_taken;
        this.metric_items_purchased = CardCrawlGame.metricData.items_purchased;
        this.metric_item_purchase_floors = CardCrawlGame.metricData.item_purchase_floors;
        this.metric_items_purged = CardCrawlGame.metricData.items_purged;
        this.metric_items_purged_floors = CardCrawlGame.metricData.items_purged_floors;
        this.metric_card_choices = CardCrawlGame.metricData.card_choices;
        this.metric_event_choices = CardCrawlGame.metricData.event_choices;
        this.metric_boss_relics = CardCrawlGame.metricData.boss_relics;
        this.metric_potions_obtained = CardCrawlGame.metricData.potions_obtained;
        this.metric_relics_obtained = CardCrawlGame.metricData.relics_obtained;
        this.metric_campfire_choices = CardCrawlGame.metricData.campfire_choices;
        this.metric_damage_taken = CardCrawlGame.metricData.damage_taken;
        this.metric_build_version = CardCrawlGame.TRUE_VERSION_NUM;
        this.metric_seed_played = Settings.seed.toString();
        this.metric_floor_reached = AbstractDungeon.floorNum;
        this.metric_playtime = (long)CardCrawlGame.playtime;
        this.neow_bonus = CardCrawlGame.metricData.neowBonus;
        this.neow_cost = CardCrawlGame.metricData.neowCost;
    }

    public enum SaveType {
        ENTER_ROOM,
        POST_NEOW,
        POST_COMBAT,
        AFTER_BOSS_RELIC,
        ENDLESS_NEOW;

        SaveType() {
        }
    }
}

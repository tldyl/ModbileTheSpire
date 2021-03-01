package com.megacrit.cardcrawl.android.mods.devCommands;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.BaseMod;
import com.megacrit.cardcrawl.android.mods.DevConsole;
import com.megacrit.cardcrawl.android.mods.devCommands.act.ActCommand;
import com.megacrit.cardcrawl.android.mods.devCommands.blight.Blight;
import com.megacrit.cardcrawl.android.mods.devCommands.clear.Clear;
import com.megacrit.cardcrawl.android.mods.devCommands.debug.Debug;
import com.megacrit.cardcrawl.android.mods.devCommands.deck.Deck;
import com.megacrit.cardcrawl.android.mods.devCommands.draw.Draw;
import com.megacrit.cardcrawl.android.mods.devCommands.energy.Energy;
import com.megacrit.cardcrawl.android.mods.devCommands.event.Event;
import com.megacrit.cardcrawl.android.mods.devCommands.fight.Fight;
import com.megacrit.cardcrawl.android.mods.devCommands.gold.Gold;
import com.megacrit.cardcrawl.android.mods.devCommands.hand.Hand;
import com.megacrit.cardcrawl.android.mods.devCommands.history.History;
import com.megacrit.cardcrawl.android.mods.devCommands.hp.Hp;
import com.megacrit.cardcrawl.android.mods.devCommands.info.Info;
import com.megacrit.cardcrawl.android.mods.devCommands.kill.Kill;
import com.megacrit.cardcrawl.android.mods.devCommands.maxhp.MaxHp;
import com.megacrit.cardcrawl.android.mods.devCommands.potions.Potions;
import com.megacrit.cardcrawl.android.mods.devCommands.power.Power;
import com.megacrit.cardcrawl.android.mods.devCommands.relic.Relic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.*;

public abstract class ConsoleCommand {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(ConsoleCommand.class);
    protected Map<String, Class<? extends ConsoleCommand>> followup = new HashMap<>();
    protected boolean simpleCheck = false;
    protected boolean requiresPlayer = false;
    protected int minExtraTokens = 0;
    protected int maxExtraTokens = 1;
    private static Map<String, Class<? extends ConsoleCommand>> root = new HashMap<>();
    public static boolean complete;
    public static boolean isNumber;
    public static boolean duringRun;
    public static String errormsg;

    public ConsoleCommand() {
    }

    protected abstract void execute(String[] tokens, int depth);

    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (this.followup.isEmpty()) {
            if (tokens.length > depth && tokens[depth].length() > 0) {
                tooManyTokensError();
            } else {
                complete = true;
            }
        }

        return new ArrayList<>();
    }

    protected void errorMsg(String[] tokens) {
        this.errorMsg();
    }

    protected void errorMsg() {
    }

    private ConsoleCommand last(String[] tokens, int[] depth, boolean forExecution) throws IllegalAccessException, InstantiationException, InvalidCommandException {
        if (depth[0] < tokens.length - (forExecution ? 0 : 1) && this.followup.containsKey(tokens[depth[0]].toLowerCase())) {
            ConsoleCommand cc = (ConsoleCommand)((Class)this.followup.get(tokens[depth[0]].toLowerCase())).newInstance();
            ++depth[0];
            return cc.last(tokens, depth, forExecution);
        } else {
            if (this.requiresPlayer && AbstractDungeon.player == null) {
                if (forExecution) {
                    DevConsole.log("This action can only be executed during a run.");
                } else {
                    duringRun = true;
                }
            } else if (forExecution && tokens.length < depth[0] + this.minExtraTokens) {
                this.errorMsg();
            } else {
                if (forExecution || this.maxExtraTokens <= 0 || tokens.length <= this.maxExtraTokens + depth[0] || tokens[depth[0] + this.maxExtraTokens].length() <= 0) {
                    return this;
                }

                tooManyTokensError();
            }

            throw new InvalidCommandException();
        }
    }

    private ArrayList<String> autocomplete(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        for (String key : this.followup.keySet()) {
            if (key.toLowerCase().startsWith(tokens[depth].toLowerCase())) {
                result.add(key);
            }
        }

        ArrayList<String> extras = this.extraOptions(tokens, depth);
        if (extras != null) {
            for (String key : extras) {
                if (key.toLowerCase().startsWith(tokens[tokens.length - 1].toLowerCase())) {
                    result.add(key);
                }
            }
        }

        Collections.sort(result);
        return result;
    }

    private static ConsoleCommand getLastCommand(String[] tokens, int[] depth, boolean forExecution) {
        try {
            ConsoleCommand cc = new ConsoleCommand() {
                protected void execute(String[] tokens, int depth) {
                }
            };
            cc.followup = root;
            cc = cc.last(tokens, depth, forExecution);
            return cc;
        } catch (InvalidCommandException ignored) {

        } catch (Exception e) {
            if (forExecution) {
                DevConsole.log("An error occurred.");
            }
            e.printStackTrace();
        }

        return null;
    }

    public static void initialize() {
        addCommand("deck", Deck.class);
        addCommand("potion", Potions.class);
        addCommand("blight", Blight.class);
        addCommand("clear", Clear.class);
        addCommand("debug", Debug.class);
        addCommand("draw", Draw.class);
        addCommand("energy", Energy.class);
        addCommand("event", Event.class);
        addCommand("fight", Fight.class);
        addCommand("gold", Gold.class);
        addCommand("hand", Hand.class);
        addCommand("hp", Hp.class);
        /*
        addCommand("help", Help.class);
        */
        addCommand("info", Info.class);
        addCommand("kill", Kill.class);
        addCommand("maxhp", MaxHp.class);
        addCommand("power", Power.class);
        addCommand("relic", Relic.class);
        /*
        addCommand("unlock", Unlock.class);
        */
        addCommand("history", History.class);
        addCommand("act", ActCommand.class);
        /*
        addCommand("key", KeyCommand.class);
        */
        ActCommand.initialize();
    }

    public static Iterator<String> getKeys() {
        return root.keySet().iterator();
    }

    public static void addCommand(String key, Class<? extends ConsoleCommand> val) {
        if (root.containsKey(key)) {
            logger.error("Command \"" + key + "\" already exists.");
        } else if (!key.matches("[a-zA-Z:]+")) {
            logger.error("Commands cannot contain whitespaces.");
        } else {
            root.put(key, val);
        }

    }

    public static void execute(String[] tokens) {
        int[] depth = new int[]{0};
        ConsoleCommand cc;
        if ((cc = getLastCommand(tokens, depth, true)) != null) {
            cc.execute(tokens, depth[0]);
        }

    }

    public static ArrayList<String> suggestions(String[] tokens) {
        complete = false;
        isNumber = false;
        duringRun = false;
        errormsg = null;
        int[] depth = new int[]{0};
        ConsoleCommand cc;
        if ((cc = getLastCommand(tokens, depth, false)) != null) {
            ArrayList<String> result = cc.autocomplete(tokens, depth[0]);
            if (cc.simpleCheck && result.contains(tokens[depth[0]])) {
                result.clear();
                complete = true;
            }

            if (!complete && !duringRun && errormsg == null) {
                return result;
            }
        }

        return new ArrayList<>();
    }

    public static ArrayList<String> smallNumbers() {
        ArrayList<String> result = new ArrayList<>();

        for(int i = 1; i <= 9; ++i) {
            result.add(String.valueOf(i));
        }

        isNumber = true;
        return result;
    }

    public static ArrayList<String> mediumNumbers() {
        ArrayList<String> result = new ArrayList<>();

        for(int i = 10; i <= 90; i += 10) {
            result.add(String.valueOf(i));
            result.add(String.valueOf(i * 10));
        }

        isNumber = true;
        return result;
    }

    public static ArrayList<String> bigNumbers() {
        ArrayList<String> result = new ArrayList<>();

        for(int i = 100; i <= 900; i += 100) {
            result.add(String.valueOf(i));
            result.add(String.valueOf(i * 10));
        }

        isNumber = true;
        return result;
    }

    public static ArrayList<String> getCardOptions() {
        ArrayList<String> result = new ArrayList<>();
        for (String key : CardLibrary.cards.keySet()) {
            result.add(key.replace(' ', '_'));
        }

        return result;
    }

    public static ArrayList<String> getCardOptionsFromCardGroup(CardGroup cg) {
        ArrayList<String> result = new ArrayList<>();
        for (AbstractCard card : cg.group) {
            String cardid = card.cardID.replace(' ', '_');
            if (!result.contains(cardid)) {
                result.add(cardid);
            }
        }

        return result;
    }

    public static ArrayList<String> getRelicOptions() {
        ArrayList<String> result = new ArrayList<>();
        for (String id : BaseMod.listAllRelicIDs()) {
            result.add(id.replace(' ', '_'));
        }

        return result;
    }

    public static void tooManyTokensError() {
        errormsg = "Too many tokens";
    }
}


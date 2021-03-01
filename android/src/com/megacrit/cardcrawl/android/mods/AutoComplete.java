package com.megacrit.cardcrawl.android.mods;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.devCommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoComplete {
    private static int ID_CREATOR = -1;
    public static final int RESET;
    private static final int MAX_SUGGESTIONS = 5;
    private static final Color TEXT_COLOR;
    private static final Color HIGHLIGHT_COLOR;
    public static boolean enabled;
    public static int selectKey;
    public static int deleteTokenKey;
    public static int fillKey1;
    public static int fillKey2;
    public static int selected;
    private static ArrayList<String> suggestions = new ArrayList<>();
    private static Stack<AutoComplete.Pair> suggestionPairs = new Stack<>();
    private static String[] tokens;
    private static boolean foundStart;
    private static boolean foundEnd;
    private static boolean noMatch;
    private static int currentID;
    private static int lastLength;
    private static int whiteSpaces;
    private static int lastWhiteSpaces;
    private static Pattern spacePattern;
    private static boolean implementedYet;
    private static float drawX;
    private static float promptWidth;
    private static GlyphLayout glyphs;
    private static final char ID_DELIMITER = ':';
    private static final String SPACE_AND_ID_DELIMITER = "[ :]";

    public static void init() {
        reset();
    }

    public static void postInit() {
        glyphs = new GlyphLayout(DevConsole.consoleFont, "$> ");
        promptWidth = glyphs.width;
        calculateDrawX();
        suggest(false);
    }

    public static void reset() {
        calculateDrawX();
        currentID = RESET;
        suggestions = new ArrayList<>();
        suggestionPairs = new Stack<>();
        tokens = new String[0];
        selected = 0;
        noMatch = false;
        foundEnd = false;
        foundStart = false;
        whiteSpaces = 0;
        lastWhiteSpaces = 0;
        lastLength = 0;
    }

    public static void resetAndSuggest() {
        if (enabled) {
            reset();
            lastWhiteSpaces = countSpaces();
            suggest(false);
        }

    }

    public static void selectUp() {
        if (selected > 0 && !noMatch && !suggestions.isEmpty() && !suggestionPairs.isEmpty()) {
            --selected;
        }

    }

    public static void selectDown() {
        if (!noMatch && !suggestions.isEmpty() && !suggestionPairs.isEmpty()) {
            AutoComplete.Pair pair = suggestionPairs.peek();
            if (selected < pair.end - pair.start && selected < suggestions.size() - 1) {
                ++selected;
            }
        }

    }

    public static void fillInSuggestion() {
        if (!noMatch && !suggestions.isEmpty() && !suggestionPairs.isEmpty()) {
            String textToInsert = suggestions.get(selected + (suggestionPairs.peek()).start);
            if (textToInsert.lastIndexOf(58) > DevConsole.currentText.lastIndexOf(58)) {
                DevConsole.currentText = getTextWithoutRightmostSpaceToken() + textToInsert.substring(0, textToInsert.lastIndexOf(58)) + ':';
            } else {
                DevConsole.currentText = getTextWithoutRightmostSpaceToken() + textToInsert + " ";
                reset();
            }

            suggest(false);
        }

    }

    private static String getTextWithoutRightmostSpaceToken() {
        int lastSpace = DevConsole.currentText.lastIndexOf(32);
        String text = "";
        if (lastSpace != -1) {
            text = DevConsole.currentText.substring(0, lastSpace + 1);
        }

        return text;
    }

    private static int lastIndexOfRegex(String currentText, String tokenDelimiter) {
        int index = -1;

        for(Matcher matcher = Pattern.compile(tokenDelimiter).matcher(currentText); matcher.find(); index = matcher.start()) {
            ;
        }

        return index;
    }

    public static void removeOneTokenUsingSpaceAndIdDelimiter() {
        String text = "";
        int lastChar = lastIndexOfRegex(DevConsole.currentText, "[ :]");
        int curTextLength = DevConsole.currentText.length();
        if (lastChar != -1 && !DevConsole.currentText.isEmpty()) {
            if (DevConsole.currentText.charAt(curTextLength - 1) == ' ') {
                text = DevConsole.currentText.substring(0, curTextLength - 1);
            } else if (DevConsole.currentText.charAt(curTextLength - 1) == ':') {
                text = getTextWithoutRightmostSpaceToken();
            } else {
                text = DevConsole.currentText.substring(0, lastChar + 1);
            }
        }

        DevConsole.currentText = text;
    }

    private static int countSpaces() {
        int spaces = 0;
        Matcher spaceMatcher = spacePattern.matcher(DevConsole.currentText);

        while(spaceMatcher.find()) {
            if (spaceMatcher.start() != 0) {
                ++spaces;
            }
        }

        return spaces;
    }

    public static void suggest(boolean isCharacterRemoved) {
        if (DevConsole.currentText.matches(".*\\s+")) {
            tokens = (DevConsole.currentText + "d").trim().split("[\\s]+");
            tokens[tokens.length - 1] = "";
        } else {
            tokens = DevConsole.currentText.trim().split("[\\s]+");
        }

        whiteSpaces = countSpaces();
        createCMDSuggestions();
        if (tokenLengthChanged() || DevConsole.currentText.isEmpty() || currentID == RESET) {
            suggestionPairs.clear();
        }

        if (currentID == RESET) {
            suggestions.clear();
        }

        if (isCharacterRemoved && !tokenLengthChanged() && suggestionPairs.size() >= 2) {
            suggestionPairs.pop();
            selected = 0;
            if (suggestionPairs.peek().end <= -1) {
                noMatch = true;
            }
        } else {
            createPair();
        }

        calculateDrawX();
        lastWhiteSpaces = whiteSpaces;
    }

    private static boolean tokenLengthChanged() {
        return lastLength != tokens.length;
    }

    private static boolean whiteSpacesIncreased() {
        return whiteSpaces > lastWhiteSpaces;
    }

    private static void createPair() {
        if (whiteSpacesIncreased()) {
            createPair(" ");
        } else {
            createPair(tokens[tokens.length - 1]);
        }
    }

    private static void createPair(String prefix) {
        selected = 0;
        AutoComplete.Pair pair;
        if (!suggestionPairs.isEmpty()) {
            pair = suggestionPairs.peek().cpy();
        } else {
            pair = new AutoComplete.Pair(0, suggestions.size() - 1);
        }

        if (shouldShowAll(prefix)) {
            foundEnd = true;
            foundStart = true;
            noMatch = false;
            suggestionPairs.push(pair.set(0, suggestions.size() - 1));
        } else {
            linearSearch(pair, prefix);
        }
    }

    private static boolean shouldShowAll(String prefix) {
        if (DevConsole.currentText.isEmpty()) {
            return true;
        } else {
            return prefix == null || prefix.isEmpty() || prefix.equals(" ") || DevConsole.currentText.lastIndexOf(32) == DevConsole.currentText.length() - 1;
        }
    }

    private static void linearSearch(AutoComplete.Pair pair, String prefix) {
        String lowerCasePrefix = prefix.toLowerCase();
        noMatch = false;
        foundEnd = false;
        foundStart = false;
        int size = suggestions.size();

        while(!foundStart && !noMatch) {
            if (pair.start >= size) {
                noMatch = true;
            } else if ((suggestions.get(pair.start)).toLowerCase().startsWith(lowerCasePrefix)) {
                foundStart = true;
            } else {
                ++pair.start;
            }
        }

        if (foundStart) {
            pair.end = pair.start + 1;

            label35:
            while(true) {
                while(true) {
                    if (foundEnd) {
                        break label35;
                    }

                    if (pair.end < size && (suggestions.get(pair.end)).toLowerCase().startsWith(lowerCasePrefix)) {
                        ++pair.end;
                    } else {
                        foundEnd = true;
                        --pair.end;
                    }
                }
            }
        }

        if (noMatch) {
            pair.set(2147483647, -2147483648);
        }

        suggestionPairs.push(pair);
    }

    private static void createCMDSuggestions() {
        currentID = RESET + 1;
        suggestions = ConsoleCommand.suggestions(tokens);
    }

    private static void calculateDrawX() {
        drawX = 200.0F * Settings.scale + promptWidth + 15.0F * Settings.scale + textLength();
    }

    private static float textLength() {
        if (glyphs != null && !DevConsole.currentText.isEmpty()) {
            glyphs.setText(DevConsole.consoleFont, getTextWithoutRightmostSpaceToken());
            return glyphs.width;
        } else {
            return 0.0F;
        }
    }

    private static boolean shouldRenderInfo() {
        return suggestionPairs.isEmpty() || suggestions.isEmpty() || ConsoleCommand.errormsg != null || ConsoleCommand.complete;
    }

    public static void render(SpriteBatch sb) {
        DevConsole.consoleFont.setColor(HIGHLIGHT_COLOR);
        if (shouldRenderInfo()) {
            sb.draw(DevConsole.consoleBackground, getBGX(), 800.0F * Settings.scale, getWidth(), -getHeight());
            String text = "[No Match found]";
            if (!implementedYet) {
                text = "[Not implemented yet]";
            }

            if (suggestions.isEmpty() && ConsoleCommand.isNumber) {
                text = "[Number]";
            }

            if (suggestions.isEmpty() && ConsoleCommand.duringRun) {
                text = "[Only available during a run]";
            }

            if (ConsoleCommand.errormsg != null) {
                text = "[" + ConsoleCommand.errormsg + "]";
            }

            if (ConsoleCommand.complete) {
                text = "[Command is complete]";
            }

            DevConsole.consoleFont.draw(sb, text, drawX, 800.0F * Settings.scale);
        } else {
            AutoComplete.Pair pair = suggestionPairs.peek();
            int amount = pair.end - selected;
            if (amount > 5) {
                amount = 5;
            }

            if (amount > pair.end - pair.start) {
                amount = pair.end - pair.start;
            }

            float y = 800.0F * Settings.scale + (float)Math.floor((double)(30.0F * Settings.scale));

            int factor;
            int i;
            for(factor = 1; factor <= amount; ++factor) {
                i = selected + pair.start + factor;
                if (i > pair.end || i >= suggestions.size()) {
                    break;
                }
            }

            sb.draw(DevConsole.consoleBackground, getBGX(), 800.0F * Settings.scale, getWidth(), -getHeight() * (float)factor);

            for(i = 0; i <= amount; ++i) {
                int item = selected + pair.start + i;
                if (item > pair.end || item >= suggestions.size()) {
                    break;
                }

                y -= (float)Math.floor((double)(30.0F * Settings.scale));
                DevConsole.consoleFont.draw(sb, suggestions.get(item), drawX, y);
                DevConsole.consoleFont.setColor(TEXT_COLOR);
            }
        }

        DevConsole.consoleFont.setColor(Color.WHITE);
    }

    private static float getBGX() {
        return drawX - 15.0F * Settings.scale;
    }

    private static float getWidth() {
        return 800.0F * Settings.scale - drawX + 200.0F * Settings.scale + 15.0F * Settings.scale;
    }

    private static float getHeight() {
        return 30.0F * Settings.scale;
    }

    static {
        RESET = ID_CREATOR++;
        TEXT_COLOR = Color.GRAY.cpy();
        HIGHLIGHT_COLOR = Color.WHITE.cpy();
        enabled = true;
        selectKey = Input.Keys.AT;
        deleteTokenKey = Input.Keys.SLASH;
        fillKey1 = Input.Keys.EQUALS;
        fillKey2 = Input.Keys.SPACE;
        selected = 0;
        lastLength = 0;
        whiteSpaces = 0;
        lastWhiteSpaces = 0;
        spacePattern = Pattern.compile("[\\s]+");
        implementedYet = true;
        promptWidth = 0.0F;
    }

    public static class Pair {
        public int start;
        public int end;

        public Pair(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public AutoComplete.Pair cpy() {
            return new AutoComplete.Pair(this.start, this.end);
        }

        public AutoComplete.Pair set(int start, int end) {
            this.start = start;
            this.end = end;
            return this;
        }
    }
}

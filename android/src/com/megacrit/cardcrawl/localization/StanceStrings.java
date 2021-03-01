package com.megacrit.cardcrawl.localization;

public class StanceStrings {
    public String NAME;
    public String[] DESCRIPTION;

    public StanceStrings() {
    }

    public static StanceStrings getMockStanceString() {
        StanceStrings retVal = new StanceStrings();
        retVal.NAME = "[MISSING_NAME]";
        retVal.DESCRIPTION = new String[1];
        retVal.DESCRIPTION[0] = "[MISSING_DESCRIPTION]";
        return retVal;
    }
}

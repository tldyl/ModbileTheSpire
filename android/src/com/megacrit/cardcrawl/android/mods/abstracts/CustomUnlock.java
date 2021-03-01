package com.megacrit.cardcrawl.android.mods.abstracts;

import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;

import java.util.ArrayList;

public abstract class CustomUnlock {
    protected ArrayList<UnlockBundle> bundleList;

    public ArrayList<AbstractUnlock> getUnlockBundle(int unlockLevel) {
        UnlockBundle bundle = bundleList.get(unlockLevel);
        return bundle.getUnlockList();
    }

    public static class UnlockBundle {
        public AbstractUnlock.UnlockType type;
        private ArrayList<AbstractUnlock> unlockList = new ArrayList<>();

        public UnlockBundle(AbstractUnlock.UnlockType type, String unlock1, String unlock2, String unlock3) {
            this.type = type;
            unlockList.add(getUnlock(unlock1));
            unlockList.add(getUnlock(unlock2));
            unlockList.add(getUnlock(unlock3));
        }

        private AbstractUnlock getUnlock(String id) {
            AbstractUnlock unlock = new AbstractUnlock();
            unlock.type = this.type;
            if (this.type == AbstractUnlock.UnlockType.CARD) {
                unlock.card = CardLibrary.getCard(id);
                unlock.key = id;
                unlock.title = unlock.card.name;
            } else if (this.type == AbstractUnlock.UnlockType.RELIC) {
                unlock.relic = RelicLibrary.getRelic(id);
                unlock.key = id;
                unlock.title = unlock.relic.name;
            }
            return unlock;
        }

        public ArrayList<AbstractUnlock> getUnlockList() {
            return this.unlockList;
        }
    }
}

package com.megacrit.cardcrawl.android.mods.interfaces;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface PostCreateStartingDeckSubscriber extends ISubscriber {
    void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass paramPlayerClass, CardGroup paramCardGroup);
}

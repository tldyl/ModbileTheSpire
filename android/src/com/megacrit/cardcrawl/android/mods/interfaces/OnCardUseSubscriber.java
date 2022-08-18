package com.megacrit.cardcrawl.android.mods.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCardUseSubscriber extends ISubscriber {
    void receiveCardUsed(AbstractCard paramAbstractCard);
}

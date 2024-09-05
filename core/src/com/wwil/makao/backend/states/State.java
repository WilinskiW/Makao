package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.player.Player;

public interface State {
    State saveState();

    void setDefaultValueOfActivations();

    boolean isValid(Card chosenCard, CardValidator validator);
    default boolean isChooserActive(){return false;}

    Card findValidCard(CardFinder cardFinder, Player player, Card stackCard);

    default boolean isFocusDrawnCard() {
        return false;
    }
    boolean isPutActive();

    void setPutActive(boolean putActive);

    boolean isPullActive();

    void setPullActive(boolean pullActive);

    boolean isEndActive();

    void setEndActive(boolean endActive);
}

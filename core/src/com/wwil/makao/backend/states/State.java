package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public interface State {
    void setDefaultValueOfActivations();

    boolean isValid(Card chosenCard, CardValidator validator);

    List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard);

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

package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.management.StateChanger;

public interface State {
    State saveState();

    void setDefaultValueOfActivations();

    boolean isValid(Card chosenCard, CardValidator validator);

    Card findValidCard(CardFinder cardFinder, Player player, Card stackCard);

    default void handlePut(Player player, Card card, StateChanger changer) {
        changer.applyNormalState(player);
        changer.setActions(player, true, false, true);
    }

    default void handlePull(Player player, StateChanger changer) {
        changer.applyNormalState(player);
        changer.setActions(player, false, false, true);
    }

    default void handleEnd(Player player, StateChanger changer) {
        changer.setActions(player, false, false, false);
    }

    default boolean isFocusDrawnCard() {
        return false;
    }

    default boolean isChooserActive() {
        return false;
    }

    boolean isPutActive();

    void setPutActive(boolean putActive);

    boolean isPullActive();

    void setPullActive(boolean pullActive);

    boolean isEndActive();

    void setEndActive(boolean endActive);
}

package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public abstract class PlayerState {


    public abstract boolean isValid(Card chosenCard,CardValidator validator);

    abstract List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard);
    abstract PlayReport setActionActivations(PlayReport playReport);
}

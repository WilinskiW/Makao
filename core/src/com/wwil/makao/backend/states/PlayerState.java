package com.wwil.makao.backend.states;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.Action;
import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerState {
    Player player;
    RoundManager roundManager;
    CardFinder cardFinder;

    public List<Play> generatePlays(Player player, RoundManager roundManager, CardFinder cardFinder) {
        initialize(player, roundManager, cardFinder);
        List<Play> plays = new ArrayList<>();
        List<Card> validCards = findCards(cardFinder, roundManager.getDeckManager().peekStackCard());

        if (!validCards.isEmpty()) {
            plays.addAll(createPutPlays(validCards));
        } else {
            plays.addAll(createRescuePlays());
        }
        return plays;
    }

    private void initialize(Player player, RoundManager roundManager, CardFinder cardFinder) {
        this.player = player;
        this.roundManager = roundManager;
        this.cardFinder = cardFinder;
    }

    protected abstract List<Card> findCards(CardFinder cardFinder, Card stackCard);

    private List<Play> createPutPlays(List<Card> cards) {
        List<Play> plays = new ArrayList<>();
        for (Card card : cards) {
            plays.add(createPutPlay(card));
        }
        plays.add(createEndPlay());
        return plays;
    }

    protected Play createPutPlay(Card card) {
        return new Play().setCardPlayed(card).setAction(Action.PUT);
    }

    protected Play createEndPlay() {
        return new Play().setAction(Action.END);
    }

    protected List<Play> createRescuePlays() {
        List<Play> plays = new ArrayList<>();
        Card rescueCard = pullCard(roundManager.getDeckManager());
        plays.add(createPullPlay(rescueCard));

        if (this.isValid(rescueCard, roundManager.getValidator())) {
            plays.add(createPutPlay(rescueCard));
        } else {
            plays.addAll(failRescue(plays));
        }

        plays.add(createEndPlay());

        return plays;
    }

    protected Card pullCard(DeckManager deckManager) {
        return deckManager.takeCardFromGameDeck();
    }

    protected Play createPullPlay(Card card) {
        return new Play().setDrawnCard(card).setAction(Action.PULL);
    }

    protected abstract List<Play> failRescue(List<Play> plays);

    public abstract boolean isValid(Card chosenCard,CardValidator validator);

}

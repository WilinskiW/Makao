package com.wwil.makao.backend.gameplay.utils;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

//Szuka kart dla komputera
public class CardFinder {
    private final CardValidator validator;
    private final CardChooser cardChooser;

    public CardFinder(CardValidator validator, DeckManager deckManager) {
        this.validator = validator;
        this.cardChooser = new CardChooser(deckManager);
    }

    public Card findCardForDefenceState(Player player) {
        List<Card> defensiveCards = findDefensiveCards(player.getCards());

        if (defensiveCards.isEmpty()) {
            return null;
        }

        List<Card> cardsWithSameRank = findCardsWithSameRank(defensiveCards);
        return cardsWithSameRank.isEmpty() ? getRandomElement(defensiveCards) : cardsWithSameRank.get(0);
    }

    private List<Card> findDefensiveCards(List<Card> playerCards) {
        return playerCards.stream()
                .filter(validator::isValidForDefenceState)
                .collect(Collectors.toList());
    }

    private List<Card> findCardsWithSameRank(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRank))
                .values().stream()
                .filter(list -> list.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private <T> T getRandomElement(List<T> elements) {
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty!");
        }
        return elements.get(new Random().nextInt(elements.size()));
    }

    public Card findForNormalState(Player player, Card stackCard) {
        List<Card> playableCards = findPlayableCards(player);

        if (playableCards.isEmpty()) {
            return null;
        }

        List<Card> cardsWithSameRank = getPlayableWithSameRank(playableCards);
        if (cardsWithSameRank.isEmpty()) {
            return getRandomElement(playableCards);
        }

        segregate(cardsWithSameRank, stackCard);
        return cardsWithSameRank.get(0);
    }

    private List<Card> findPlayableCards(Player player) {
        return player.getCards().stream()
                .filter(card -> player.getState().isValid(card, validator))
                .collect(Collectors.toList());
    }

    private List<Card> getPlayableWithSameRank(List<Card> cards) {
        List<Card> playableWithSameRank = findCardsWithSameRank(cards);
        return playableWithSameRank.size() >= 4 ? chooseRank(playableWithSameRank) : playableWithSameRank;
    }

    private List<Card> chooseRank(List<Card> playable) {
        Map<Rank, Long> rankCount = playable.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        long maxCount = Collections.max(rankCount.values());

        List<Rank> maxRanks = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Rank chosenRank = maxRanks.size() > 1 ? getRandomElement(maxRanks) : maxRanks.get(0);

        return playable.stream()
                .filter(card -> card.getRank() == chosenRank)
                .collect(Collectors.toList());
    }

    private void segregate(List<Card> cards, Card stackCard) {
        // Sprawdź, czy pierwsza karta na liście pasuje do karty ze stosu
        if (!isFirstCardLikeStackCard(cards.get(0), stackCard)) {
            for (int i = 1; i < cards.size(); i++) {
                Card card = cards.get(i);
                // Jeśli znajdziesz kartę, która pasuje do karty ze stosu
                if (isFirstCardLikeStackCard(card, stackCard)) {
                    // Przenieś ją na początek listy
                    Collections.swap(cards, 0, i);
                    break;
                }
            }
        }
    }

    private boolean isFirstCardLikeStackCard(Card card, Card stackCard) {
        return card.matchesSuit(stackCard) || card.matchesSuit(stackCard);
    }

    public CardChooser getCardChooser() {
        return cardChooser;
    }
}


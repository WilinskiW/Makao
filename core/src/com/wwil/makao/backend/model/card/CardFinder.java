package com.wwil.makao.backend.model.card;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.player.Player;

import java.util.*;

//Szuka kart dla gracza
public class CardFinder {
    private final CardValidator validator;

    public CardFinder(CardValidator validator) {
        this.validator = validator;
    }

    public List<Card> findCardsForDefenceState(Player player,Card attackingCard) {
        List<Card> playerCards = player.getCards();
        List<Card> defensiveCards = new ArrayList<>();

        for (Card playerCard : playerCards) {
            if (playerCard.getRank() == attackingCard.getRank()) {
                defensiveCards.add(playerCard);
            }  //fixme: Karty już sprawdzone są pomijane np. Stack: 3 PIK| Pierwsza karta sprawdzona: 2 TREFL, Druga: 2 PIK. Druga trafia, pierwsza nie
            else if (playerCard.getSuit() == attackingCard.getSuit() && playerCard.isBattleCard() && attackingCard.getRank().equals(Rank.K)) {
                defensiveCards.add(playerCard);
            }
        }

        if (defensiveCards.isEmpty()) {
            return defensiveCards;
        }

        List<Card> cardsWithSameRank = findCardsWithSameRank(defensiveCards);
        if (cardsWithSameRank.isEmpty()) {
            return Collections.singletonList(getRandomElement(defensiveCards));
        }

        return cardsWithSameRank;
    }

    private List<Card> findCardsWithSameRank(List<Card> cards) {
        Map<Rank, List<Card>> rankToCards = new HashMap<>();
        for (Card card : cards) {
            rankToCards.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }

        List<Card> sameRankCards = new ArrayList<>();
        for (List<Card> cardList : rankToCards.values()) {
            if (cardList.size() > 1) {
                sameRankCards.addAll(cardList);
            }
        }
        return sameRankCards;
    }

    private <T> T getRandomElement(List<T> elements) {
        return elements.get(new Random().nextInt(elements.size()));
    }

    public List<Card> findCardAgainstBlockAttack(Player player){
        List<Card> playerCards = player.getCards();
        List<Card> cards = new ArrayList<>();

        for(Card card : playerCards){
            if(card.getRank() == Rank.FOUR){
                cards.add(card);
            }
        }
        return cards;
    }

    public List<Card> findCardForDefaultState(Player player,Card stackCard) {
        List<Card> playerCards = player.getCards();
        List<Card> playableCards = new ArrayList<>();

        //Dodajemy karty, które mogą być zagrane
        for (Card card : playerCards) {
            if (player.getState().isValid(card,validator)) {
                playableCards.add(card);
            }
        }

        //Jeśli nie ma kart możliwych do zagrania, zwracamy pustą listę
        if (playableCards.isEmpty()) {
            return playableCards;
        }

        //Szukamy kart o tej samej randze
        List<Card> cardsWithSameRank = getPlayableWithSameRank(playerCards);

        //Jeśli nie ma kart o tej samej randze, zwracamy losową kartę z całej puli możliwych kart do zagrania
        if (cardsWithSameRank.isEmpty()) {
            return Collections.singletonList(getRandomElement(playableCards));
        }

        //Sprawdzamy, które karty mogą zostać zagrane mają pary
        for (Card playableCard : playableCards) {
            if (playableCard.getRank() == cardsWithSameRank.get(0).getRank()) {
                //Musimy ustawić kolejność kart tak aby ranga karty ze stosu była taka sama jak pierwszej rzuconej karty
                segregate(cardsWithSameRank, stackCard);
                return cardsWithSameRank;
            }
        }
        //Jeżeli takich nie znajdziemy to zwracamy losową możliwą kartę
        return Collections.singletonList(getRandomElement(playableCards));
    }

    private List<Card> getPlayableWithSameRank(List<Card> cards) {
        List<Card> playable = findCardsWithSameRank(cards);
        if (playable.size() >= 4) {
            return chooseRank(playable);
        }
        return playable;
    }

    private List<Card> chooseRank(List<Card> playable) {
        // Tworzymy mapę do zliczania wystąpień każdej rangi
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card card : playable) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        // Znajdujemy maksymalną liczbę wystąpień rangi
        int maxCount = Collections.max(rankCount.values());

        // Zbieramy wszystkie rangi, które mają maksymalną liczbę wystąpień
        List<Rank> maxRanks = new ArrayList<>();
        for (Map.Entry<Rank, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                maxRanks.add(entry.getKey());
            }
        }

        // Jeżeli mamy więcej niż jedną rangę o takiej samej liczbie wystąpień, wybieramy losowo jedną z nich
        Rank chosenRank;
        if (maxRanks.size() > 1) {
            chosenRank = getRandomElement(maxRanks);
        } else {
            chosenRank = maxRanks.get(0);
        }

        // Zbieramy karty z najczęściej występującymi rangami
        List<Card> maxPlayable = new ArrayList<>();
        for (Card card : playable) {
            if (card.getRank() == chosenRank) {
                maxPlayable.add(card);
            }
        }

        return maxPlayable;
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
        return card.getRank() == stackCard.getRank() || card.getSuit() == stackCard.getSuit();
    }

//    public Card findCardToDemand(Player player) {
//        List<Card> cards = player.getCards();
//        Collections.shuffle(cards);
//        for (Card card : cards) {
//            if (card.getRank().getAbility() == Ability.NONE) {
//                return card;
//            }
//        }
//        return null;
//    }

//    public Card findDemandedCard(Card demanded, boolean lookForJ) {
//        List<Card> cards = playerCards;
//        Collections.shuffle(cards);
//        Card cardToPlay = null;
//        for (Card card : cards) {
//            if (lookForJ && card.getRank().equals(Rank.J)) {
//                return card;
//            }
//
//            if (card.getRank() == demanded.getRank()) {
//                cardToPlay = card;
//            }
//        }
//        return cardToPlay;
//    }
//
//    public Suit giveMostDominantSuit() {
//        int[] counts = new int[4]; // Tablica przechowująca liczbę wystąpień dla każdego koloru
//
//        for (Card card : playerCards) {
//            Suit cardSuit = card.getSuit();
//            if (cardSuit != Suit.BLACK && cardSuit != Suit.RED) { // Pomijanie BLACK i RED
//                counts[cardSuit.ordinal()]++; // Inkrementacja odpowiedniego licznika
//            }
//        }
//
//        int maxIndex = 0;
//        for (int i = 1; i < counts.length; i++) {
//            if (counts[i] > counts[maxIndex]) {
//                maxIndex = i;
//            }
//        }
//
//        return Suit.values()[maxIndex]; // Zwracanie koloru z największą liczbą wystąpień
//    }
}

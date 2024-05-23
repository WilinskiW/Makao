package com.wwil.makao.backend;

import java.util.*;

public class Player {
    private final List<Card> cards;
    private CardBattle cardBattle;

    public Player(List<Card> cards) {
        this.cards = cards;
    }

    public void addCardToHand(Card card) {
        cards.add(card);
    }

    public void addCardsToHand(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public void removeCardFromHand(Card card) {
        cards.remove(card);
    }

    public boolean checkIfPlayerHaveNoCards() {
        return cards.isEmpty();
    }

    public List<Card> getPlayableWithSameRank(List<Card> cards) {
        List<Card> playable = findCardsWithSameRank(cards);
        if (playable.size() > 4) {
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

            // Zbieramy karty z najczęściej występującymi rangami
            List<Card> maxPlayable = new ArrayList<>();
            for (Card card : playable) {
                if (maxRanks.contains(card.getRank())) {
                    maxPlayable.add(card);
                }
            }

            return maxPlayable;
        }
        return playable;
    }

    public List<Card> findCardsWithSameRank(List<Card> cards) {
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
    public List<Card> findDefensiveCards(CardBattle cardBattle){
        List<Card> defensiveCards = new ArrayList<>();
        Card attackingCard = cardBattle.getAttackingCard();

        for(Card playerCard : cards){
            if(playerCard.getRank() == attackingCard.getRank()){
                defensiveCards.add(playerCard);
            }  //fixme: Karty już sprawdzone są pomijane np. Stack: 3 PIK| Pierwsza karta sprawdzona: 2 TREFL, Druga: 2 PIK. Druga trafia, pierwsza nie
            else if(playerCard.getSuit() == attackingCard.getSuit() && playerCard.isBattleCard() && !cardBattle.getAttackingCard().getRank().equals(Rank.K)){
                defensiveCards.add(playerCard);
            }
        }

        if(defensiveCards.isEmpty()){
            return defensiveCards;
        }

        List<Card> cardsWithSameRank = findCardsWithSameRank(defensiveCards);
        if(cardsWithSameRank.isEmpty()){
            return Collections.singletonList(defensiveCards.get(new Random().nextInt(defensiveCards.size())));
        }


        return cardsWithSameRank;
    }

    public CardBattle moveCardBattle(){
        CardBattle battle = cardBattle;
        cardBattle = null;
        return battle;
    }

    public Card findCardToDemand() {
        List<Card> playerCards = cards;
        Collections.shuffle(playerCards);
        for (Card card : playerCards) {
            if (card.getRank().getAbility() == Ability.NONE) {
                return card;
            }
        }
        return null;
    }

    public Card findDemandedCard(Card demanded, boolean lookForJ) {
        List<Card> playerCards = cards;
        Collections.shuffle(playerCards);
        Card cardToPlay = null;
        for (Card card : playerCards) {
            if (lookForJ && card.getRank().equals(Rank.J)) {
                return card;
            }

            if (card.getRank() == demanded.getRank()) {
                cardToPlay = card;
            }
        }
        return cardToPlay;
    }

    public Suit giveMostDominantSuit() {
        int[] counts = new int[4]; // Tablica przechowująca liczbę wystąpień dla każdego koloru

        for (Card card : cards) {
            Suit cardSuit = card.getSuit();
            if (cardSuit != Suit.BLACK && cardSuit != Suit.RED) { // Pomijanie BLACK i RED
                counts[cardSuit.ordinal()]++; // Inkrementacja odpowiedniego licznika
            }
        }

        int maxIndex = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[maxIndex]) {
                maxIndex = i;
            }
        }

        return Suit.values()[maxIndex]; // Zwracanie koloru z największą liczbą wystąpień
    }

    public boolean isAttack(){
        return cardBattle != null;
    }

    public List<Card> getCards() {
        return cards;
    }


    public CardBattle getAttacker() {
        return cardBattle;
    }

    public Player setAttacker(CardBattle cardBattle) {
        this.cardBattle = cardBattle;
        return this;
    }
}

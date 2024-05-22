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

    public List<Card> findCardsWithSameRank(List<Card> cards) {
        List<Card> sameCards = new ArrayList<>();
        boolean isCurrentHasMultiple = false;
        boolean[] checkList = new boolean[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            for (int j = 0; j < cards.size(); j++) {
                if(checkList[i]){
                    break;
                }

                if (checkList[j]) {
                    continue;
                }

                if (cards.get(i).getRank() == cards.get(j).getRank() && i != j) {
                    sameCards.add(cards.get(j));
                    checkList[j] = true;
                    isCurrentHasMultiple = true;
                }
            }

            if(isCurrentHasMultiple){
                sameCards.add(cards.get(i));
            }

            checkList[i] = true;
            isCurrentHasMultiple = false;

            if(endSearching(checkList)){
                break;
            }
        }

        return sameCards;
    }


    //Obrona: Dowolna 2, 3 PIK, K PIK
    //Atakujacy: 2 np. PIK

    public List<Card> findDefensiveCards(CardBattle cardBattle){
        List<Card> defensiveCards = new ArrayList<>();
        Card attackingCard = cardBattle.getAttackingCard();

        for(Card playerCard : cards){
            //1. Szukamy karty o tej samej randze.
            if(playerCard.getRank() == attackingCard.getRank()){
                defensiveCards.add(playerCard);
            }  //fixme: Karty już sprawdzone są pomijane np. Stack: 3 PIK| Pierwsza karta sprawdzona: 2 TREFL, Druga: 2 PIK. Druga trafia, pierwsza nie
            else if(playerCard.getSuit() == attackingCard.getSuit() && playerCard.isBattleCard()){
                defensiveCards.add(playerCard);
            }
        }

        if(defensiveCards.isEmpty()){
            return defensiveCards;
        }

        //Mamy wszystkie karty do obrony
        //np. 2 PIK, 2 TREFL, K PIK
        List<Card> cardsWithSameRank = findCardsWithSameRank(defensiveCards);
        if(cardsWithSameRank.isEmpty()){
            return Collections.singletonList(defensiveCards.get(new Random().nextInt(defensiveCards.size())));
        }


        return cardsWithSameRank;
    }


    //Atakujacy: 3 np. KIER
    //Obrona: Dowolna 3, 2 KIER, K KIER

    //Atakujący: K np. KIER
    //Obrona: Dowolny K, 2 KIER, 3 KIER



    private boolean endSearching(boolean[]checkList){
        int trueValueCounter = 0;
        for (boolean checked : checkList) {
            if (checked) {
                trueValueCounter++;
            }
        }
        return (checkList.length-trueValueCounter) <= 1;
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

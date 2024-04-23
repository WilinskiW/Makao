package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<PlayerHand> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;
    private DemandManager demand = new DemandManager(0, false, null);
    private final List<Card> humanPlayedCards = new ArrayList<>();

    public MakaoBackend() {
        createCardsToGameDeck();
        stack.addCardToStack(getStartStackCard());
        createPlayers();
    }

    private void createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
    }

    private Card getStartStackCard() {
        for (Card card : gameDeck) {
            if (card.getRank().getAbility().equals(Ability.NONE)) {
                return card;
            }
        }
        return null;
    }

    private Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    private void createPlayers() {
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            PlayerHand playerHand = new PlayerHand(giveCards(STARTING_CARDS));
            players.add(playerHand);
        }
    }

    private List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    //////////// Logika:
    //Jedyna publiczna metoda (odbiera informacje, wykonuje działanie i wysyła)
    public RoundReport executeAction(Play play) {
        roundReport = new RoundReport();
//
////        if(humanPlay.isDragging()){
////            return roundReport;
////        }
//
//        //Zapytania od frontendu:
//        //1. Czy gracz jest zablokowany? TAK -> Zakończ turę gracza
////        if (humanPlay.isBlock()) {
////            playRound(humanPlay);
////            return roundReport;
////        }
//
//        //3. Czy gracz żąda? TAK -> Zakończ turę gracza
//        if (humanPlay.isDemanding()) {
//            demand = new DemandManager(0, true, humanPlay.getCardPlayed());
//        }
//
//        if (humanPlay.wantToEndTurn()) {
//            if (demand.isActive()) {
//                playRound(humanPlay);
//            } else {
//                playRound(humanPlay);
//            }
//            humanPlayedCards.clear();
//            return roundReport;
//        }
//
//
//        //4. Czy karta jest nieprawidłowa? TAK -> Zwróć report incorrect
//        if (isCardIncorrect(humanPlay)) {
//            roundReport.setIncorrect();
//            return roundReport;
//        }
//        //5. Czy karta aktywuje chooser? TAK -> Aktywuj chooser i zwróć report incorrect
////        if (shouldActiveChooser(play)) {
////            return createActivateChooserReport(play);
////        }
//
//        if (humanPlay.wantToEndTurn() || humanPlay.wantsToDraw()) {
//            if (humanPlayedCards.size() >= 2) {
//                playRound(humanPlay);
//                return roundReport;
//            }
//            //6. Czy gracz jest osobą żądającą obecnego demand? TAK -> Wyłącz demand i kontynuuj.
//            if (demand.getPerformerIndex() == 0 && demand.isActive()) {
//                demand.setActive(false);
//            }
//            playRound(humanPlay);
//            return roundReport;
//        }
//
//        roundReport.addPlay(executePlay(humanPlay));

//        if (play.isDragging()) {
//            return createDragReport(play);
//        }
        if (play.isDropped() && isCardCorrect(play)) {
            putCard(play.getCardPlayed());
            roundReport.addPlay(
                    new PlayReport(play)
                            .setCardCorrect(true)
            );
        } else if (play.wantToEndTurn()) {
            playRound(play);
        }

        return roundReport;
    }

    private RoundReport createDragReport(Play play) {
        roundReport.addPlay(
                new PlayReport(play)
                .setCardCorrect(true)
        );
        return roundReport;
    }

    private boolean isCardCorrect(Play humanPlay) {
        if (demand.isActive()) {
            return isCorrectCardForDemand(humanPlay);
        } else {
            return isCorrectCard(humanPlay.getCardPlayed());
        }
    }

//    private boolean isCardIncorrect(Play humanPlay) {
//        if (demand.isActive()) {
//            return !humanPlay.wantsToDraw() && !isCorrectCardForDemand(humanPlay);
//        }
//        return !humanPlay.wantsToDraw() && !isCorrectCard(humanPlay.getCardPlayed());
//    }

    private boolean isCorrectCardForDemand(Play humanPlay) {
        Card chosenCard = humanPlay.getCardPlayed();
        if (humanPlayedCards.size() == 1) {
            return chosenCard.getRank() == demand.getCard().getRank() ||
                    chosenCard.getRank().equals(Rank.JOKER) ||
                    (chosenCard.getRank().equals(Rank.J) && stack.isJackOnTop()) ||
                    stack.isJackBeforeJoker() && chosenCard.getRank().equals(Rank.J);
        }
        return chosenCard.getRank() == humanPlay.getCardsPlayed().get(0).getRank();
    }

//    private boolean shouldActiveChooser(HumanPlay humanPlay) {
//        return ((!humanPlay.wantsToDraw()
//                && humanPlay.getCardPlayed().getRank().isRankActivateChooser())
//                && !humanPlay
//                || stack.peekCard().getRank().equals(Rank.JOKER);
//    }

//    private RoundReport createActivateChooserReport(Play humanPlay) {
//        roundReport.addPlay(new PlayReport(currentPlayer(), null, humanPlay, null,
//                isCorrectCard(humanPlay.getCardPlayed()), false));
//        executePlay(humanPlay);
//        roundReport.setIncorrect();
//        //roundReport.setChooserActivation(true);
//        return roundReport;
//    }

    private boolean isCorrectCard(Card chosenCard) {
        Card stackCard = stack.peekCard();
        if (!humanPlayedCards.isEmpty()) {
            return chosenCard.getRank() == humanPlayedCards.get(0).getRank();
        } else if (stackCard.getRank().equals(Rank.Q)
                || chosenCard.getRank().equals(Rank.Q)
                || chosenCard.getRank().equals(Rank.JOKER)
                || stackCard.getRank().equals(Rank.JOKER)) {
            return true;
        }
        return stackCard.getSuit() == chosenCard.getSuit() || stackCard.getRank() == chosenCard.getRank();
    }

    private void playRound(Play humanPlay) {
        roundReport.addPlay(executePlay(humanPlay));
        nextPlayer();
        for (int i = 1; i < players.size(); i++) {
            roundReport.addPlay(executePlay(executeComputerPlay()));
            nextPlayer();
        }
    }

    private PlayReport executePlay(Play play) {
        if (play.endTurn()) {
            return new PlayReport(play).setCardCorrect(true);
        }

        //Skip turn
        if (play.isBlock()) {
            return new PlayReport(play).setBlocked(true);
        }

        if (play.isDemanding()) {
            return new PlayReport(play).setCardCorrect(true);
        }

        //Is demanded
        if (demand.isActive()) {
            if (play.wantsToDraw()) {
                return drawCard(play);
            }
            putCard(play.getCardPlayed());
            return new PlayReport(play).setCardCorrect(true);
        }

        //Dobierz kartę
        if (play.wantsToDraw()) {
            return drawCard(play);
        }

        //Połóż kartę
        AbilityReport abilityReport = putCard(play.getCardPlayed());
        roundReport.setBlockPullButton(true);
        return new PlayReport(play)
                .setAbilityReport(abilityReport)
                .setCardCorrect(true);
    }

    private PlayReport drawCard(Play play) {
        Card drawn = takeCardFromGameDeck();
        players.get(currentPlayerIndex).addCardToHand(drawn);
        return new PlayReport(play).setDrawn(drawn);
    }

    private AbilityReport putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayerIndex == 0) {
            humanPlayedCards.add(cardPlayed);
        }
        return useCardAbility(cardPlayed, null);
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    //Generator playa dla komputerów
    private Play executeComputerPlay() {
        PlayerHand playerHand = currentPlayer();
        PlayReport lastReport = roundReport.getLastPlay();

        //Czy poprzedni żąda czegoś?
        if (demand.isActive()) {
            return getDemandPlay();
        }
        //Czy poprzedni zablokował obecnego?
        if (lastReport.getAbilityReport() != null && lastReport.getAbilityReport().isBlockNext()) {
            return new Play().setBlocked(true);
        }
        //Znajdź poprawną kartę i ją zapisz
        //todo komputer w trakcie swojej tury może położyć parę pasujących kart
        for (Card card : playerHand.getCards()) {
            if (isCorrectCard(card)) {
                return new Play()
                        .setCardsPlayed(Collections.singletonList(card));
            }
        }
        //Przypadek dobrania
        return getPullCardPlay();
    }

    private Play getDemandPlay() {
        Card card;
        if (currentPlayerIndex == demand.getPerformerIndex()) {
            demand.setActive(false);
            card = currentPlayer().findDemandedCard(demand.getCard(), false);
        } else {
            card = currentPlayer().findDemandedCard(demand.getCard(), stack.isJackOnTop());
        }
        //todo komputer w trakcie żądania może położyć parę pasujących kart
        if (card != null) {
            //return new Play(Arrays.asList(card), true, false, false, false);
        }
        return getPullCardPlay();
    }

    private Play getPullCardPlay() {
        return new Play().setAction(Action.PULL);
    }

    private AbilityReport useCardAbility(Card card, AbilityReport wildCardReport) {
        AbilityReport abilityReport = null;
        Card wildCard = null;
        if (wildCardReport != null) {
            wildCard = wildCardReport.getChoosenCard();
        }
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                abilityReport = useChangeSuitAbility();
                break;
            case PLUS_2:
                abilityReport = usePlusAbility(2, wildCard);
                break;
            case PLUS_3:
                abilityReport = usePlusAbility(3, wildCard);
                break;
            case WAIT:
                abilityReport = useWaitAbility(wildCard);
                break;
            case DEMAND:
                abilityReport = useDemandAbility(wildCard);
                break;
            case KING:
                abilityReport = chooseAbilityForKing(card, abilityReport, wildCard);
                break;
            case WILD_CARD:
                abilityReport = useWildCard();
        }
        return abilityReport;
    }

    private AbilityReport useChangeSuitAbility() {
        if (currentPlayerIndex != 0) {
            Card choosenCard = new Card(Rank.AS, currentPlayer().giveMostDominantSuit());
            stack.addCardToStack(choosenCard);
            return new AbilityReport(currentPlayerIndex, null, choosenCard, false, false);
        }
        return new AbilityReport(0, null, stack.peekCard(), false, false);
    }

    private AbilityReport usePlusAbility(int amountOfCards, Card wildCard) {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(amountOfCards);
        if (currentPlayerIndex != lastIndex) {
            players.get(currentPlayerIndex + 1).addCardsToHand(pulledCards);
            return new AbilityReport(currentPlayerIndex + 1, pulledCards, wildCard, false, false);
        } else {
            players.get(0).addCardsToHand(pulledCards);
            return new AbilityReport(0, pulledCards, wildCard, false, false);
        }
    }

    private AbilityReport useWaitAbility(Card wildCard) {
        int lastIndex = players.size() - 1;
        if (currentPlayerIndex != lastIndex) {
            return new AbilityReport(currentPlayerIndex + 1, null, wildCard, true, false);
        } else {
            return new AbilityReport(0, null, wildCard, true, false);
        }
    }

    private AbilityReport useDemandAbility(Card wildCard) {
        if (currentPlayerIndex != 0) {
            demand = new DemandManager(currentPlayerIndex, true, currentPlayer().findCardToDemand());
            if (demand.getCard() == null) {
                demand.setActive(false);
                System.out.println("Gracz " + (currentPlayerIndex + 1) + " nie zada");
            } else {
                System.out.println("Gracz " + (currentPlayerIndex + 1) + " zada " + demand.getCard().getRank());
            }
            if (wildCard != null) {
                return new AbilityReport(currentPlayerIndex, null, wildCard, false, demand.isActive());
            }
            return new AbilityReport(currentPlayerIndex, null, demand.getCard(), false, demand.isActive());
        }
        return new AbilityReport(0, null, demand.getCard(), false, true);
    }

    private AbilityReport chooseAbilityForKing(Card card, AbilityReport abilityReport, Card wildCard) {
        switch (card.getSuit()) {
            case HEART:
                abilityReport = usePlusAbility(5, wildCard);
                break;
            case SPADE:
                abilityReport = usePlusFiveAbilityToPreviousPlayer(wildCard);
        }
        return abilityReport;
    }

    private AbilityReport usePlusFiveAbilityToPreviousPlayer(Card wilCard) {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(5);
        if (currentPlayerIndex != 0) {
            players.get(currentPlayerIndex - 1).addCardsToHand(pulledCards);
            return new AbilityReport(currentPlayerIndex - 1, pulledCards, wilCard, false, false);
        } else {
            players.get(lastIndex).addCardsToHand(pulledCards);
            return new AbilityReport(3, pulledCards, null, false, false);
        }
    }

    private AbilityReport useWildCard() {
        if (currentPlayerIndex != 0) {
            Card choosenCard = new Card(Rank.getRandomWithAbility(), currentPlayer().giveMostDominantSuit());
            stack.addCardToStack(choosenCard);
            return useCardAbility(choosenCard, new AbilityReport(currentPlayerIndex, null, choosenCard, false, false));
        }
        return new AbilityReport(0, null, stack.peekCard(), false, false);
    }

    private PlayerHand currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Stack getStack() {
        return stack;
    }

    public List<PlayerHand> getPlayers() {
        return players;
    }
}

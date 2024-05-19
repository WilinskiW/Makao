package com.wwil.makao.backend;

import java.util.*;

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
        startRound();
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
    private void startRound() {
        roundReport = new RoundReport();
    }

    public RoundReport executeAction(Play play) {

        //PUT + PULL(Pierwsza karta ratuje)
        //PUT - nie można kłaść tych samych kart

        if (play.getAction() == Action.PULL) {
            roundReport.addPlayRaport(executePlay(play));
            return roundReport;
        }

        if (play.getAction() == Action.END) {
            playRound();
            RoundReport report = roundReport;
            startRound();
            return report;
        }

        if (play.getAction() != Action.PUT) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        //scenario for put:
        roundReport.addPlayRaport(
                new PlayReport(currentPlayer(), play)
                        .setCardCorrect(isCardCorrect(play)));


        if (isCardCorrect(play)) {
            putCard(play.getCardPlayed());
        }
        return roundReport;
    }

    private PlayReport drawCard(Play play) {
        if (play.getAction() != Action.PULL) {
            throw new IllegalStateException("whe we are here?");
        }
        Card drawn = takeCardFromGameDeck();
        players.get(currentPlayerIndex).addCardToHand(drawn);
        return new PlayReport(currentPlayer(), play).setDrawn(drawn);
    }

    private RoundReport createDragReport(Play play) {
        roundReport.addPlayRaport(
                new PlayReport(currentPlayer(), play)
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
        Rank chosenCardRank = humanPlay.getCardPlayed().getRank();
        if (humanPlayedCards.size() == 1) {
            return chosenCardRank == demand.getCard().getRank() ||
                    chosenCardRank.equals(Rank.JOKER) ||
                    (chosenCardRank.equals(Rank.J) && stack.isJackOnTop()) ||
                    stack.isJackBeforeJoker() && chosenCardRank.equals(Rank.J);
        }
        return chosenCardRank == humanPlay.getCardsPlayed().get(0).getRank();
    }


    private boolean isCorrectCard(Card chosenCard) {
        Card stackCard = stack.peekCard();
        if (!humanPlayedCards.isEmpty()) {
            return chosenCard.getRank() == humanPlayedCards.get(0).getRank();
        }

        if (stackCard.getRank().equals(Rank.Q) || chosenCard.getRank().equals(Rank.Q)
                || chosenCard.getRank().equals(Rank.JOKER) || stackCard.getRank().equals(Rank.JOKER)) {
            return true;
        }

        return stackCard.getSuit() == chosenCard.getSuit() || stackCard.getRank() == chosenCard.getRank();
    }

    private void playRound() {
        humanPlayedCards.clear();
        nextPlayer();
        for (int i = 1; i < players.size(); i++) {
            roundReport.addPlayRaport(executePlay(executeComputerPlay()));
            nextPlayer();
        }
    }

    private PlayReport executePlay(Play play) {

        //Skip turn
        if (play.isBlock()) {
            return new PlayReport(currentPlayer(), play).setBlocked(true);
        }

        if (play.isDemanding()) {
            return new PlayReport(currentPlayer(), play).setCardCorrect(true);
        }

        //Is demanded
        if (demand.isActive()) {
            if (play.getAction() == Action.PULL) {
                return drawCard(play);
            }
            putCard(play.getCardPlayed());
            return new PlayReport(currentPlayer(), play).setCardCorrect(true);
        }

        //Dobierz kartę
        if (play.getAction() == Action.PULL) {
            return drawCard(play);
        }

        //Połóż kartę/karty
        AbilityReport abilityReport = null;
        if (play.getCardsPlayed().size() > 1) {
            for (Card card : play.getCardsPlayed()) {
                abilityReport = putCard(card);
            }
        } else {
            abilityReport = putCard(play.getCardPlayed());
        }
        roundReport.setBlockPullButton(true);
        return new PlayReport(currentPlayer(), play)
                .setAbilityReport(abilityReport)
                .setCardCorrect(true);
    }


    private AbilityReport putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayerIndex == 0) {
            humanPlayedCards.add(cardPlayed);
        }
        //return useCardAbility(cardPlayed, null);
        return null;
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    //Generator playa dla komputerów
    private Play executeComputerPlay() {
        PlayReport lastReport = roundReport.getLastPlay();
        //Czy poprzedni żąda czegoś?
        if (demand.isActive()) {
            return getDemandPlay();
        }
        //Czy poprzedni zablokował obecnego?
        if (lastReport.getAbilityReport() != null && lastReport.getAbilityReport().isBlockNext()) {
            return new Play().setBlocked(true);
        }
        //Znajdź karty do zagrania
        List<Card> playableCards = getPlayableCards();
        if (!playableCards.isEmpty()) {
            return new Play().
                    setCardsPlayed(playableCards)
                    .setAction(Action.PUT);
        }
        //Dobierz kartę
        return new Play().setAction(Action.PULL);
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
        return new Play().setAction(Action.PULL);
    }

    private List<Card> getPlayableCards() {
        List<Card> playableCards = new ArrayList<>();

        //Dodajemy karty, które mogą być zagrane
        for (Card card : currentPlayer().getCards()) {
            if (isCorrectCard(card)) {
                playableCards.add(card);
            }
        }

        //Jeśli nie ma kart możliwych do zagrania, zwracamy pustą listę
        if (playableCards.isEmpty()) {
            return playableCards;
        }

        //Szukamy kart o tej samej randze
        List<Card> cardsWithSameRank = currentPlayer().findCardsWithSameRank();

        //Jeśli nie ma kart o tej samej randze, zwracamy listę kart możliwych do zagrania
        if (cardsWithSameRank.isEmpty()) {
            return playableCards;
        }

        //Sprawdzamy, czy są karty o tej samej randze wśród kart możliwych do zagrania
        Rank chosenRank = null;
        for (Card playableCard : playableCards) {
            for (Card cardWithMultiple : cardsWithSameRank) {
                if (playableCard.getRank() == cardWithMultiple.getRank()) {
                    chosenRank = playableCard.getRank();
                    break;
                }
            }
            if (chosenRank != null) {
                break;
            }
        }

        //Jeśli znaleźliśmy powtórzenie, wybieramy karty z tą samą rangą
        if (chosenRank != null) {
            playableCards.clear();
            for (Card cardToPlay : cardsWithSameRank) {
                if (cardToPlay.getRank() == chosenRank) {
                    playableCards.add(cardToPlay);
                }
            }
        }

        return playableCards;
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
            return new AbilityReport(currentPlayer(), null, choosenCard, false, false);
        }
        return new AbilityReport(humanPlayer(), null, stack.peekCard(), false, false);
    }

    private AbilityReport usePlusAbility(int amountOfCards, Card wildCard) {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(amountOfCards);
        if (currentPlayerIndex != lastIndex) {
            players.get(currentPlayerIndex + 1).addCardsToHand(pulledCards);
            return new AbilityReport(getNextPlayer(), pulledCards, wildCard, false, false);
        } else {
            players.get(0).addCardsToHand(pulledCards);
            return new AbilityReport(humanPlayer(), pulledCards, wildCard, false, false);
        }
    }

    private AbilityReport useWaitAbility(Card wildCard) {
        int lastIndex = players.size() - 1;
        if (currentPlayerIndex != lastIndex) {
            return new AbilityReport(getNextPlayer(), null, wildCard, true, false);
        } else {
            return new AbilityReport(humanPlayer(), null, wildCard, true, false);
        }
    }

    private AbilityReport useDemandAbility(Card wildCard) {
        if (notHumanPlaying()) {
            demand = new DemandManager(currentPlayerIndex, true, currentPlayer().findCardToDemand());
            if (demand.getCard() == null) {
                demand.setActive(false);
                System.out.println("Gracz " + (currentPlayerIndex + 1) + " nie zada");
            } else {
                System.out.println("Gracz " + (currentPlayerIndex + 1) + " zada " + demand.getCard().getRank());
            }
            if (wildCard != null) {
                return new AbilityReport(currentPlayer(), null, wildCard, false, demand.isActive());
            }
            return new AbilityReport(currentPlayer(), null, demand.getCard(), false, demand.isActive());
        }
        return new AbilityReport(humanPlayer(), null, demand.getCard(), false, true);
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
       /* List<Card> pulledCards = giveCards(5);
        if (currentPlayerIndex != 0) {
            players.get(currentPlayerIndex - 1).addCardsToHand(pulledCards);
            return new AbilityReport(currentPlayerIndex - 1, pulledCards, wilCard, false, false);
        } else {
            playerBefore().addCardsToHand(pulledCards);
            return new AbilityReport(3, pulledCards, null, false, false);
        }*/
        throw new IllegalStateException("Special events not supported in new version");
    }

    private AbilityReport useWildCard() {
        if (notHumanPlaying()) {
            Card choosenCard = new Card(Rank.getRandomWithAbility(), currentPlayer().giveMostDominantSuit());
            stack.addCardToStack(choosenCard);
            return useCardAbility(choosenCard, new AbilityReport(currentPlayer(), null, choosenCard, false, false));
        }
        return new AbilityReport(humanPlayer(), null, stack.peekCard(), false, false);
    }

    private boolean notHumanPlaying() {
        return currentPlayer() != humanPlayer();
    }

    private PlayerHand currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private PlayerHand playerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    private PlayerHand getNextPlayer() {
        int playerBeforeIndex = currentPlayerIndex + 1;
        if (playerBeforeIndex >= players.size()) {
            playerBeforeIndex = 0;
        }
        return players.get(playerBeforeIndex);
    }

    private PlayerHand humanPlayer() {
        return players.get(0);
    }

    public Stack getStack() {
        return stack;
    }

    public List<PlayerHand> getPlayers() {
        return players;
    }


}

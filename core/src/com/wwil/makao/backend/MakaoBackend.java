package com.wwil.makao.backend;

import java.util.*;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;
    private DemandManager demand = new DemandManager(0, false, null);
    private final List<Card> humanPlayedCards = new ArrayList<>();
    private final List<Card> pullDeck = new ArrayList<>();

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
            Player player = new Player(giveCards(STARTING_CARDS));
            players.add(player);
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
            roundReport.addPlayRaport(executePlay(createComputerPlay()));
            nextPlayer();
        }
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    private Play createComputerPlay() {
        Play play = new Play();
        if (currentPlayer().isAttack()) {
            List<Card> defensiveCards = currentPlayer().findDefensiveCards(currentPlayer().getAttacker());
            if (!defensiveCards.isEmpty()) {
                getNextPlayer().setAttacker(currentPlayer().moveCardBattle());
                System.out.println("Gracz " + currentPlayerIndex + " broni sie :" + defensiveCards);
                return play.setCardsPlayed(defensiveCards).setAction(Action.PUT);
            }
            play.setAction(Action.PULL);
        }

        //Znajdź karty do zagrania
        List<Card> playableCards = getPlayableCards();
        if (!playableCards.isEmpty()) {
            return play
                    .setCardsPlayed(playableCards)
                    .setAction(Action.PUT);
        }
        //Dobierz kartę
        return play.setAction(Action.PULL);
    }

    private PlayReport executePlay(Play play) {
        PlayReport playReport = new PlayReport(currentPlayer(), play);

        //Dobierz kartę
        if (currentPlayer().isAttack()) {
            takeFromPullDeck(playReport);
            System.out.println("Gracz ciagnie karty");
        } else if (play.getAction() == Action.PULL) {
            Card drawn = takeCardFromGameDeck();
            players.get(currentPlayerIndex).addCardToHand(drawn);
            if (!isCorrectCard(drawn) || currentPlayer() == humanPlayer()) {
                return new PlayReport(currentPlayer(), play).setDrawn(drawn);
            }
            playReport.setDrawn(drawn);
            play.setCardsPlayed(Collections.singletonList(drawn));
            System.out.println("Pierwsza karta ratuje!!!");
            return playReport;
        }


        //Połóż kartę/karty
        if (play.getCardsPlayed() != null) {
            putCards(play);
        }


        roundReport.setBlockPullButton(true);
        return playReport.setCardCorrect(true);
    }

    private void takeFromPullDeck(PlayReport playReport) {
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        playReport.setCardsToPull(cardsToPull);
        currentPlayer().addCardsToHand(cardsToPull);
        pullDeck.clear();
        currentPlayer().setAttacker(null);
    }

    private void putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayer() == humanPlayer()) {
            humanPlayedCards.add(cardPlayed);
        }
        useCardAbility(cardPlayed);
    }

    private void putCards(Play play){
        if (play.getCardsPlayed().size() > 1) {
            for (Card card : play.getCardsPlayed()){
                putCard(card);
            }
        } else {
            putCard(play.getCardPlayed());
        }
    }
    private Play defend() {
        List<Card> defensiveCards = currentPlayer().findDefensiveCards(currentPlayer().getAttacker());
        if (!defensiveCards.isEmpty()) {
            getNextPlayer().setAttacker(currentPlayer().moveCardBattle());
            System.out.println("Gracz " + currentPlayerIndex + " broni sie :" + defensiveCards);
            return new Play()
                    .setCardsPlayed(defensiveCards)
                    .setAction(Action.PUT);
        }
        System.out.println("Gracz " + currentPlayerIndex + " dobiera :" + currentPlayer().getAttacker().getCardToDraw());
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
        List<Card> cardsWithSameRank = currentPlayer().findCardsWithSameRank(playableCards);

        //Jeśli nie ma kart o tej samej randze, zwracamy listę kart możliwych do zagrania
        if (cardsWithSameRank.isEmpty()) {
            return Collections.singletonList(playableCards.get(new Random().nextInt(playableCards.size())));
        }

        return playableCards;
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


    private void useCardAbility(Card card) {
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                //abilityReport = useChangeSuitAbility();
                break;
            case PLUS_2:
                attack(getNextPlayer(), 2, card);
                break;
            case PLUS_3:
                attack(getNextPlayer(), 3, card); //ATAK
                break;
            case WAIT:
                //useWaitAbility(wildCard);
                break;
            case DEMAND:
                //useDemandAbility(wildCard); //ATAK
                break;
            case KING:
                //abilityReport = chooseAbilityForKing(card, abilityReport, wildCard); //ATAK
                break;
            case WILD_CARD:
                //useWildCard(); //PRAWDOPODOBNY ATAK
        }
    }

    private void useChangeSuitAbility() {
        if (currentPlayerIndex != 0) {
            Card choosenCard = new Card(Rank.AS, currentPlayer().giveMostDominantSuit());
            stack.addCardToStack(choosenCard);
        }
    }

    private void attack(Player player, int amountOfCards, Card attackingCard) {
        pullDeck.addAll(giveCards(amountOfCards));
        player.setAttacker(new CardBattle(pullDeck, attackingCard));
        System.out.println("Gracz " + currentPlayerIndex + " atakuje Gracza: " + (currentPlayerIndex + 1));
    }

    private void useWaitAbility(Card wildCard) {

    }

    private void useDemandAbility(Card wildCard) {
        if (notHumanPlaying()) {
            demand = new DemandManager(currentPlayerIndex, true, currentPlayer().findCardToDemand());
            if (demand.getCard() == null) {
                demand.setActive(false);
                System.out.println("Gracz " + (currentPlayerIndex + 1) + " nie zada");
            } else {
                System.out.println("Gracz " + (currentPlayerIndex + 1) + " zada " + demand.getCard().getRank());
            }
            if (wildCard != null) {
                //return new AbilityReport(currentPlayer(), null, wildCard, false, demand.isActive());
            }
            // new AbilityReport(currentPlayer(), null, demand.getCard(), false, demand.isActive());
        }
        //return new AbilityReport(humanPlayer(), null, demand.getCard(), false, true);
    }

    private void chooseAbilityForKing(Card card, Card wildCard) {
        switch (card.getSuit()) {
            case HEART:
                attack(getNextPlayer(), 5, card);
                break;
            case SPADE:
                usePlusFiveAbilityToPreviousPlayer(wildCard);
        }
    }

    private void usePlusFiveAbilityToPreviousPlayer(Card wilCard) {
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

    private void useWildCard() {
        if (notHumanPlaying()) {
            Card choosenCard = new Card(Rank.getRandomWithAbility(), currentPlayer().giveMostDominantSuit());
            stack.addCardToStack(choosenCard);
            //  return useCardAbility(choosenCard, new AbilityReport(currentPlayer(), null, choosenCard, false, false));
        }
        //return new AbilityReport(humanPlayer(), null, stack.peekCard(), false, false);
    }

    private boolean notHumanPlaying() {
        return currentPlayer() != humanPlayer();
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private Player playerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    private Player getNextPlayer() {
        int playerBeforeIndex = currentPlayerIndex + 1;
        if (playerBeforeIndex >= players.size()) {
            playerBeforeIndex = 0;
        }
        return players.get(playerBeforeIndex);
    }

    private Player humanPlayer() {
        return players.get(0);
    }

    public Stack getStack() {
        return stack;
    }

    public List<Player> getPlayers() {
        return players;
    }


}

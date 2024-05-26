package com.wwil.makao.backend;

import java.util.*;

public class MakaoBackend {
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;
    private final CardValidator validator = new CardValidator(this);
    private DemandManager demand = new DemandManager(0, false, null);
    private final List<Card> humanPlayedCards = new ArrayList<>();
    private final List<Card> pullDeck = new ArrayList<>();
    final PlayMaker playMaker = new PlayMaker(this);
    private Event event;

    public MakaoBackend() {
        createCardsToGameDeck();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
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
        for (int i = 0; i < RuleParams.AMOUNT_OF_PLAYERS; i++) {
            Player player = new Player(giveCards(RuleParams.STARTING_CARDS));
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

    //todo: Pierwsza karta ratuje przy ciągniętych kart
    public RoundReport executeAction(Play play) {
        if (play.getAction() == Action.PULL) {
            PlayReport humanReport = executePlay(play);
            roundReport.addPlayRaport(humanReport);
            if(doesEventExist()){
                humanReport.getPlay().setAction(Action.PULL_END);
                event.setActive(false);
                return endTurn();
            }
            return roundReport;
        }

        if (play.getAction() == Action.END) {
            if(doesEventExist()){
                event.setActive(false);
            }
            return endTurn();
        }

        //scenario for put:
        boolean isValid = validator.isValidCardForCurrentState(play.getCardPlayed());
        roundReport.addPlayRaport(
                new PlayReport(getCurrentPlayer(), play)
                        .setCardCorrect(isValid));


        if (isValid) {
            putCard(play.getCardPlayed());
        }
        return roundReport;
    }

    private RoundReport endTurn(){
        playRound();
        RoundReport report = roundReport;
        startRound();
        return report;
    }

    //todo: Kiedy gracz dobierze karty po K PIK to ture wykonuje komputer, po rzucającym K PIK
    private void playRound() {
        humanPlayedCards.clear();
        checkEvent();
        while (getCurrentPlayer() != getHumanPlayer()) {
            checkEvent();
            roundReport.addPlayRaport(executePlay(playMaker.generate()));
            checkEvent();
            if (!getCurrentPlayer().isAttack()) {
                nextPlayer();
            }

            if (getCurrentPlayer().checkIfPlayerHaveNoCards()) {
                break;
            }
        }
    }

    private void checkEvent() {
        if (doesEventExist()) {
            if(event.isActive()) {
                event.startEvent();
            }
            else{
                event.endEvent();
                event = null;
            }
        }
        else if (getCurrentPlayer() == getHumanPlayer()) {
            nextPlayer();
        }
    }

    void playerBefore() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = RuleParams.AMOUNT_OF_PLAYERS - 1;
        }
    }

    void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= RuleParams.AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    PlayReport executePlay(Play play) {
        PlayReport playReport = new PlayReport(getCurrentPlayer(), play);
        //Dobierz kartę
        if (getCurrentPlayer().isAttack()) {
            drainPullDeck(playReport);
        } else if (play.getAction() == Action.PULL) {
            pull(playReport);
        }

        //Połóż kartę/karty
        if (play.getCardsPlayed() != null) {
            putCards(play);
        }
        return playReport.setCardCorrect(true);
    }

    private void drainPullDeck(PlayReport playReport) {
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        playReport.setCardsToPull(cardsToPull);
        getCurrentPlayer().addCardsToHand(cardsToPull);
        pullDeck.clear();
        getCurrentPlayer().setAttacker(null);
    }

    private void pull(PlayReport playReport) {
        Card drawn = takeCardFromGameDeck();
        getCurrentPlayer().addCardToHand(drawn);
        if (validator.isValidCardForCurrentState(drawn)) {
            if(getCurrentPlayer() != getHumanPlayer()) {
                playReport.getPlay().setCardsPlayed(Collections.singletonList(drawn));
                System.out.println("Pierwsza karta ratuje!");
            }
        }
        playReport.setDrawn(drawn);
    }


    private void putCards(Play play) {
        for (Card card : play.getCardsPlayed()) {
            putCard(card);
        }
    }

    private void putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        getCurrentPlayer().removeCardFromHand(cardPlayed);
        if (getCurrentPlayer() == getHumanPlayer()) {
            humanPlayedCards.add(cardPlayed);
        }
        useCardAbility(cardPlayed);
    }

    private void useCardAbility(Card card) {
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                //useChangeSuitAbility();
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
                chooseAbilityForKing(card); //ATAK
                break;
            case WILD_CARD:
                //useWildCard(); //PRAWDOPODOBNY ATAK
        }
    }


    private void attack(Player player, int amountOfCards, Card attackingCard) {
        pullDeck.addAll(giveCards(amountOfCards));
        player.setAttacker(new CardBattle(pullDeck, attackingCard));
        if (attackingCard.doesStartEvent()) {
            event = new AttackPreviousPlayerEvent(this);
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(getNextPlayer(), 5, card);
                break;
            case SPADE:
                attack(getPlayerBefore(), 5, card);
                break;
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setCurrentPlayer(Player player){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i) == player){
                currentPlayerIndex = i;
                return;
            }
        }
    }

    public Player getPlayerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    public Player getNextPlayer() {
        int playerBeforeIndex = currentPlayerIndex + 1;
        if (playerBeforeIndex >= players.size()) {
            playerBeforeIndex = 0;
        }
        return players.get(playerBeforeIndex);
    }

    boolean doesEventExist() {
        return event != null;
    }

    CardValidator getValidator() {
        return validator;
    }

    public RoundReport getRoundReport() {
        return roundReport;
    }

    public Player getHumanPlayer() {
        return players.get(0);
    }

    public Stack getStack() {
        return stack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public DemandManager getDemand() {
        return demand;
    }

    public List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }
}

package com.wwil.makao.backend;

import java.util.ArrayList;
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
    private DemandManager demand;

    public MakaoBackend() {
        createCardsToGameDeck();
        stack.addCardToStack(takeCardFromGameDeck());
        createPlayers();
    }

    private void createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
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
    public RoundReport executeAction(Play humanPlay) {
        roundReport = new RoundReport();

        if(demand != null && demand.getPerformerIndex() == currentPlayerIndex){
            roundReport.addPlay(new PlayReport(currentPlayer(), null, humanPlay, null,
                    isCorrectCardForDemand(humanPlay.getCardPlayed()), true));
            if(roundReport.getLastPlay().isCardCorrect()){
                demand = null;
            }
            roundReport.setIncorrect();
            return roundReport;
        }


        if(humanPlay.isDemanding()){
            demand = new DemandManager(0,true,humanPlay.getCardPlayed());
            playRound(humanPlay);
            return roundReport;
        }

        //Wait turn
        if(humanPlay.isBlock()){
            playRound(humanPlay);
            return roundReport;
        }

        //Nie jest położona i gracz nie chce dobrać (Przypadek drag)
        if (isDrag(humanPlay)) {
            roundReport.addPlay(new PlayReport(currentPlayer(), null, humanPlay, null,
                    isCorrectCard(humanPlay.getCardPlayed()), true));
            roundReport.setIncorrect();
            return roundReport;
        }

        //Nie chce ciągnąć i jest nieprawidłowa
        if (isPlayUnfinished(humanPlay)) {
            roundReport.setIncorrect();
            return roundReport;
        }
        //Czy aktywować okno
        if (!humanPlay.wantsToDraw() && isCardActivateChooser(humanPlay)) {
            roundReport.addPlay(new PlayReport(currentPlayer(), null, humanPlay, null,
                    isCorrectCard(humanPlay.getCardPlayed()), false));
            executePlay(humanPlay);
            roundReport.setIncorrect();
            roundReport.setChooserActivation(true);
            return roundReport;
        }

        playRound(humanPlay);

        return roundReport;
    }

    private boolean isCorrectCardForDemand(Card chosenCard){
        return chosenCard.getRank() == demand.getDemandedCard().getRank();
    }

    private boolean isCardActivateChooser(Play humanPlay) {
        return !humanPlay.isChooserActive() &&
                (humanPlay.getCardPlayed().getRank().equals(Rank.J) ||
                        humanPlay.getCardPlayed().getRank().equals(Rank.AS));
    }

    private boolean isCorrectCard(Card chosenCard) {
        Card stackCard = stack.peekCard();
        if (stackCard.getRank().equals(Rank.Q) || chosenCard.getRank().equals(Rank.Q)
            /*|| chosenCard.getRank().name().equals("JOKER")*/) {
            return true;
        }

        return compareCards(chosenCard, stackCard);
    }

    private boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    private boolean isDrag(Play humanPlay) {
        return !humanPlay.isDropped() && !humanPlay.wantsToDraw() && !humanPlay.isChooserActive();
    }

    private boolean isPlayUnfinished(Play humanPlay) {
        return !humanPlay.wantsToDraw() && !isCorrectCard(humanPlay.getCardPlayed());
    }
//Tutaj nic do zmiany
    private void playRound(Play humanPlay) {
        roundReport.addPlay(executePlay(humanPlay));
        nextPlayer();
        for (int i = 1; i < players.size(); i++) {
            roundReport.addPlay(executePlay(executeComputerPlay()));
            nextPlayer();
        }
    }
//Tutaj nic do zmiany
    private PlayReport executePlay(Play play) {
        //Is demanding
        if(demand != null && demand.isActive()){
            if(play.wantsToDraw()){
                return drawCard(play);
            }
            putCard(play.getCardPlayed());
            return new PlayReport(currentPlayer(),null,play,null,true,false);
        }

        //Skip turn
        if(play.isBlock()){
            return new PlayReport(currentPlayer(),null,play,null,false, true);
        }
        //Dobierz kartę
        if (play.wantsToDraw()) {
            return drawCard(play);
        }

        //Skip turn
        if(play.isBlock()){
            return new PlayReport(currentPlayer(),null,play,null,false, true);
        }

        //Połóż kartę
        AbilityReport abilityReport = putCard(play.getCardPlayed());
        return new PlayReport(currentPlayer(), abilityReport, play, null, true, false);
    }

    private PlayReport drawCard(Play play){
        Card drawn = takeCardFromGameDeck();
        players.get(currentPlayerIndex).addCardToHand(drawn);
        return new PlayReport(currentPlayer(), null, play, drawn, false, false);
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }
    private AbilityReport putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        return useCardAbility(cardPlayed);
    }
//Tutaj pokombinować: Generator playa dla komputerów
    //
    private Play executeComputerPlay() {
        PlayerHand playerHand = currentPlayer();
        PlayReport lastReport = roundReport.getLastPlay();
        //Czy poprzedni żąda czegoś?
        if(demand != null && demand.isActive()){
            return getDemandPlay();
        }
        //Czy poprzedni zablokował obecnego?
        if(lastReport.getAbilityReport() != null && lastReport.getAbilityReport().isBlockNext()){
            return new Play(null,false,false,false, true);
        }
        //Znajdź poprawną kartę i ją zapisz
        for (Card card : playerHand.getCards()) {
            if (isCorrectCard(card)) {
                return new Play(card, false, true, false, false);
            }
        }
        //Przypadek dobrania
        return getPullCardPlay(false);
    }

    // TODO: 12.03.2024 Działa na jednego bota, ale nie na wszystkich.
    private Play getDemandPlay(){
        Card card = currentPlayer().findDemandedCard(demand.getDemandedCard());
        if(card != null){
            return new Play(card,false,true,true,false);
        }
        return getPullCardPlay(true);
    }

    private Play getPullCardPlay(boolean chooserActive){
        return new Play(null, true, false, chooserActive, false);
    }

    private AbilityReport useCardAbility(Card card) {
        AbilityReport abilityReport = null;
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                abilityReport = useChangeSuitAbility();
                break;
            case PLUS_2:
                abilityReport = usePlusAbility(2);
                break;
            case PLUS_3:
                abilityReport = usePlusAbility(3);
                break;
            case WAIT:
                abilityReport = useFourAbility();
                break;
            case DEMAND:
                System.out.println("J");
                break;
            case KING:
                abilityReport = useKingAbility(card, abilityReport);
                break;
        }
        return abilityReport;
    }

    private AbilityReport useChangeSuitAbility() {
        if(currentPlayerIndex != 0){
            Card choosenCard = new Card(Rank.AS,currentPlayer().giveMostDominantSuit());
            stack.addCardToStack(choosenCard);
            return new AbilityReport(currentPlayerIndex, null, choosenCard, false, false);
        }
        return new AbilityReport(0,null,stack.peekCard(), false, false);
    }

    private AbilityReport usePlusAbility(int amountOfCards) {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(amountOfCards);
        if (currentPlayerIndex != lastIndex) {
            players.get(currentPlayerIndex + 1).addCardsToHand(pulledCards);
            return new AbilityReport(currentPlayerIndex + 1, pulledCards, null, false, false);
        } else {
            players.get(0).addCardsToHand(pulledCards);
            return new AbilityReport(0, pulledCards, null, false, false);
        }
    }

    private AbilityReport useFourAbility() {
        int lastIndex = players.size() - 1;
        if (currentPlayerIndex != lastIndex) {
            return new AbilityReport(currentPlayerIndex + 1, null, null, true, false);
        } else {
            return new AbilityReport(0, null, null, true, false);
        }
    }

    private AbilityReport useKingAbility(Card card, AbilityReport abilityReport) {
        switch (card.getSuit()) {
            case HEART:
                abilityReport = usePlusAbility(5);
                break;
            case SPADE:
                abilityReport = usePlusFiveAbilityToPreviousPlayer();
        }
        return abilityReport;
    }

    private AbilityReport usePlusFiveAbilityToPreviousPlayer() {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(5);
        if (currentPlayerIndex != 0) {
            players.get(currentPlayerIndex - 1).addCardsToHand(pulledCards);
            return new AbilityReport(currentPlayerIndex - 1, pulledCards, null, false, false);
        } else {
            players.get(lastIndex).addCardsToHand(pulledCards);
            return new AbilityReport(3, pulledCards, null, false, false);
        }
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

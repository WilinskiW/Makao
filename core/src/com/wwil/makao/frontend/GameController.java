package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.*;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.groups.CardChooserGroup;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.groups.PlayerHandGroup;
import com.wwil.makao.frontend.entities.groups.StackCardsGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Komunikacja miedzy back endem a front endem

public class GameController {
    private final GameplayScreen gameplayScreen;
    private final Stage stage;
    private final MakaoBackend backend = new MakaoBackend();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final StackCardsGroup stackCardsGroup = new StackCardsGroup(backend.getStack());
    private CardChooserGroup cardChooser;
    private GameButton pullButton;
    private GameButton endTurnButton;
    private final DragAndDropManager dragAndDropManager = new DragAndDropManager(this);
    private CardActor choosenCardActor;
    private boolean inputBlockActive = false;

    //Frontend może jedynie odczytywać.
    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.stage = gameplayScreen.getStage();
    }

    public void executePut(Play play, CardActor cardActor) {
        if (play.getAction() == Action.PUT && play.getCardPlayed().getRank().isRankActivateChooser()) {
            showCardChooser(cardActor);
            return;
        }
        choosenCardActor = cardActor;
        executeAction(play);
    }

    public void executeAction(Play play) {
        if (play.getAction() == Action.DRAG) {
            return;
        }
        //jeśli isDropped = false -> jest to próba

        //jeśli jest to karta wymagająca choosera to pokaż chooser i zatrzymaj grę

        RoundReport report;
        report = backend.executeAction(play);


        switch (play.getAction()) {
            case DRAG:
                changeCardColor(report.getLastPlay().isCardCorrect(), choosenCardActor);
                break;
            case PUT:
                useCard(report.getLastPlay());
                break;
            case PULL:
                pullCard(report.getLastPlay().getDrawn(), getHumanHand());
                break;
            case END:
                endTurn(report);
                break;
        }
    }

    private void useCard(PlayReport playReport) {
        if (playReport.isCardCorrect()) {
            putCard(choosenCardActor, getHumanHand(), true);
            pullButton.setActive(false);
        } else {
            positionCardInGroup(getHumanHand(), choosenCardActor);
        }
    }

    private void putCard(CardActor playedCard, PlayerHandGroup player, boolean alignCards) {
        if (player == getHumanHand()) {
            playedCard.clearListeners();
            endTurnButton.setActive(true);
            pullButton.setActive(false);
        }
        addCardActorToStackGroup(playedCard);
        endIfPlayerWon(player);
        if (alignCards) {
            player.moveCloserToStartingPosition();
        }
    }

    private RoundReport createWaitReport() {
        return backend.executeAction(
                new Play()
                        .setBlocked(true)
                        .setAction(Action.END)
        );
    }

    private void endTurn(RoundReport report) {
        cardChooser.setVisibility(false);
        dragAndDropManager.startListening();
        turnOffHumanInput();
        executeComputersTurn(report);
    }

    private void pullCard(Card card, PlayerHandGroup player) {
        CardActor drawnCard = cardActorFactory.createCardActor(card);
        if (player == getHumanHand()) {
            drawnCard.setUpSideDown(false);
            dragAndDropManager.focusOneCard(drawnCard);
            pullButton.setActive(false);
            endTurnButton.setActive(true);
        }
        player.addActor(drawnCard);
    }

    private RoundReport createDemandReport(Play humanPlay) {
        getHumanHand().moveCloserToStartingPosition();
        cardChooser.setVisibility(false);
        endTurnButton.setActive(true);
        return backend.executeAction(humanPlay);
    }


    public void changeCardColor(boolean isCardCorrect, CardActor chosenCardActor) {
        if (isCardCorrect) {
            chosenCardActor.setColor(Color.LIME);
        } else {
            chosenCardActor.setColor(Color.SCARLET);
        }
    }

    private void showCardChooser(CardActor cardPlayed) {
        CardActor stackCard = stackCardsGroup.peekCardActor();
        putCard(cardPlayed, getHumanHand(), false);
        cardChooser.setVisibility(true);
        cardChooser.getManager().setDisplayCard(stackCard, cardPlayed);
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    private void endIfPlayerWon(PlayerHandGroup playerHandGroup) {
        //Do czasu wprowadzenia menu
        if (playerHandGroup.getPlayerHand().checkIfPlayerHaveNoCards()) {
            System.out.println(playerHandGroup + " won");
            Gdx.app.exit();
        }
    }

    private void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
        if (!human.getChildren().isEmpty()) {
            chosenCard.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(human, chosenCard);
        }
    }

    private void moveCardBackToHumanGroup(PlayerHandGroup humanGroup, CardActor card) {
        humanGroup.addActor(card);
        card.setX(card.getLastPositionBeforeRemove().x);
        card.setY(card.getLastPositionBeforeRemove().y);
        card.setZIndex((int) card.getLastPositionBeforeRemove().z);
    }

    public void executeDragStop(CardActor card) {
        if (!getHumanHand().getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(getHumanHand(), card);
        }
    }

    public void turnOffHumanInput() {
        pullButton.setActive(false);
        endTurnButton.setActive(false);
        Gdx.input.setInputProcessor(null);
        setInputBlockActive(true);
    }

    ///////////////////////
//     Komputer
//////////////////////
    private void executeComputersTurn(final RoundReport roundReport) {
        float delta = 1.5f;
        final int numberOfComputers = handGroups.size() - 1;
        final AtomicInteger completedComputers = new AtomicInteger(0);
        final List<PlayReport> computerPlayReports = roundReport.getComputerPlayReport();

        for (int i = 1; i < handGroups.size(); i++) {
            final PlayerHandGroup currentHandGroup = handGroups.get(i);
            final PlayReport currentPlayReport = computerPlayReports.get(i - 1);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (!currentPlayReport.isBlocked()) {
                        processComputerTurn(currentPlayReport, currentHandGroup);
                    }
                    // Sprawdź, czy to był ostatni ruch komputera
                    if (completedComputers.incrementAndGet() == numberOfComputers) {
                        turnOnHumanInput();
                        //todo: Na razie czlowiek nie moze sie obronic
                        if(getHumanHand().getPlayerHand().isAttack()){
                            System.out.println("Czlowiek jest atakowany!");
                            getHumanHand().getPlayerHand().setAttacker(null);
                        }
                        System.out.println("-----------------------");
                    }
                }
            }, i * delta); // Opóźnienie względem indeksu
        }
    }

    private void processComputerTurn(PlayReport playReport, PlayerHandGroup playerGroup) {
        List<Card> cardsToPlay = playReport.getPlay().getCardsPlayed();

        if (playReport.getCardsToPull() != null && !playReport.getCardsToPull().isEmpty()) {
            pullCards(playReport, playerGroup);
        }
        else if (playReport.getPlay().getAction() == Action.PULL) {
            pullCard(playReport.getDrawn(), playerGroup);
        }

        if (cardsToPlay != null) {
            List<CardActor> cardActorsToPlay = playerGroup.getCardActors(playReport.getPlay().getCardsPlayed());
            putCards(playerGroup, cardActorsToPlay, true);
        }
    }


    private void putCards(PlayerHandGroup player, List<CardActor> cardsToPlay, boolean alignCards) {
        for (CardActor cardActor : cardsToPlay) {
            addCardActorToStackGroup(cardActor);
        }
        endIfPlayerWon(player);
        if (alignCards) {
            player.moveCloserToStartingPosition();
        }
    }

    private void pullCards(PlayReport playReport, PlayerHandGroup playerGroup) {
        for (Card card : playReport.getCardsToPull()) {
            pullCard(card, playerGroup);
        }
    }

    private void turnOnHumanInput() {
        setInputBlockActive(false);
        endTurnButton.setActive(false);
        pullButton.setActive(true);
        Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }

    public CardActor peekStackCardActor() {
        return stackCardsGroup.peekCardActor();
    }

    public PlayerHandGroup getHumanHand() {
        return handGroups.get(0);
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public void setPullButton(GameButton pullButton) {
        this.pullButton = pullButton;
    }

    public void setEndTurnButton(GameButton endTurnButton) {
        this.endTurnButton = endTurnButton;
    }

    public void setInputBlockActive(boolean inputBlockActive) {
        this.inputBlockActive = inputBlockActive;
    }

    public void setCardChooser(CardChooserGroup cardChooser) {
        this.cardChooser = cardChooser;
    }

    public void setChosenCardActor(CardActor choosenCardActor) {
        this.choosenCardActor = choosenCardActor;
    }

    public boolean isInputBlockActive() {
        return inputBlockActive;
    }

    public CardActorFactory getCardActorFactory() {
        return cardActorFactory;
    }

    public List<PlayerHandGroup> getHandGroups() {
        return handGroups;
    }

    public MakaoBackend getBackend() {
        return backend;
    }

    public DragAndDropManager getDragAndDropManager() {
        return dragAndDropManager;
    }

    public GameplayScreen getGameplayScreen() {
        return gameplayScreen;
    }
}
/*
 * todo To zrobić:
 *  Bez podświetlania (na razie)
 * Obiekt Play:
 * Okno może się uruchamiać ale może nie przepuścić
 * zagranie próba
 * zagranie normalne
 * zagranie normalne + wybór
 * dobranie karty (nie pozwoli Ci jeśli już zagrałeś) (Pierwsza karta ratuje)
 * kończę turę -> zapytanie na backendzie
 *
 *  Akcje:
 *  zagralem karte (próba lub prawdziwe zagranie)
 *    -> wybór
 *  kończe turę
 *  dobieram karte
 *
 * Umięjętności:
 * zadam figur (jopek)
 * zmiana koloru (as)
 * przeciwnik dobiera x (2,3, krol pik/kier)
 * przeciwnik czeka x tur (4)
 * joker ( zmienia sie w dowolna wybrana karte)
 *
 * //Abstrakcyjna klasa Event
 * //event dobierania -> aktywny, jakie karty
 * //event czekania
 * //event żądania -> aktywny, jaka karta (ranga)
 *
 *
 * */
package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.*;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.cardChooser.CardChooserGroup;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardsGroup.PlayerHandGroup;
import com.wwil.makao.frontend.entities.cardsGroup.StackCardsGroup;

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

    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.stage = gameplayScreen.getStage();
    }

    public void executePut(Play play, CardActor cardActor) {
        if (play.getAction() == Action.PUT && play.getCardPlayed().getRank().isRankActivateChooser()) {
            throw new UnsupportedOperationException("Card chooser jest nieaktywny");
//            showCardChooser(cardActor);
//            return;
        }
        choosenCardActor = cardActor;
        executePlay(play);
    }

    public void executePlay(Play play) {
        RoundReport report;
        report = backend.processHumanPlay(play);
        switch (play.getAction()) {
            case DRAG:
                changeCardColor(report.getLastPlayReport().isCardCorrect(), choosenCardActor);
                break;
            case END:
                endTurn(report);
                break;
            case PUT:
                useCard(report.getLastPlayReport());
                break;
            case PULL:
                pullCards(report.getLastPlayReport(), humanHand());
                break;
        }
    }

    private void changeCardColor(boolean isValid, CardActor cardActor) {
        if (isValid) {
            cardActor.setColor(Color.LIME);
        } else {
            cardActor.setColor(Color.SCARLET);
        }
    }

    private void endTurn(RoundReport report) {
        cardChooser.setVisibility(false);
        dragAndDropManager.startListening();
        turnOffHumanInput();
        executeComputersPlayReport(report);
    }

    private void useCard(PlayReport playReport) {
        if (playReport.isCardCorrect()) {
            putCard(choosenCardActor, humanHand(), true);
            pullButton.setActive(false);
        } else {
            positionCardInGroup(humanHand(), choosenCardActor);
        }
    }

    private void putCard(CardActor playedCard, PlayerHandGroup player, boolean alignCards) {
        if (player == humanHand()) {
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

    private void pullCards(PlayReport playReport, PlayerHandGroup playerGroup) {
        Card singleDrawn = playReport.getSingleDrawn();
        if (singleDrawn != null) {
            pullCard(singleDrawn, playerGroup);
            if (playerGroup == humanHand()) {
                activeRescueCard(singleDrawn);
            }
        } else {
            for (Card card : playReport.getCardsToPull()) {
                pullCard(card, playerGroup);
            }

            if (playerGroup == humanHand()) {
                pullButton.setActive(true);
                endTurnButton.setActive(false);
            }
        }
    }

    private void pullCard(Card card, PlayerHandGroup player) {
        CardActor drawnCard = cardActorFactory.createCardActor(card);
        if (player == humanHand()) {
            drawnCard.setUpSideDown(false);
            dragAndDropManager.prepareDragAndDrop(drawnCard);
            pullButton.setActive(false);
            endTurnButton.setActive(true);
        }
        player.addActor(drawnCard);
    }

    private void activeRescueCard(Card singleDraw) {
        pullButton.setActive(false);
        endTurnButton.setActive(true);
        dragAndDropManager.focusOneCard(humanHand().getCardActor(singleDraw));
    }

    private void showCardChooser(CardActor cardPlayed) {
        CardActor stackCard = stackCardsGroup.peekCardActor();
        putCard(cardPlayed, humanHand(), false);
        cardChooser.setVisibility(true);
        cardChooser.getManager().setDisplayCard(stackCard, cardPlayed);
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        try {
            stage.addActor(cardActor);
        } catch (NullPointerException e) {
            throw new CardNotFoundException();
        }
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    private void endIfPlayerWon(PlayerHandGroup handGroup) {
        //Do czasu wprowadzenia menu
        if (handGroup.getPlayerHand().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
            System.out.println(handGroup.getCardsAlignment() + " won");
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
        if (!humanHand().getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(humanHand(), card);
        }
    }

    public void turnOffHumanInput() {
        pullButton.setActive(false);
        endTurnButton.setActive(false);
        Gdx.input.setInputProcessor(null);
        humanHand().changeTransparencyOfGroup(0.25f);
        setInputBlockActive(true);
    }
    ///////////////////////
//     Komputer
//////////////////////

    private void executeComputersPlayReport(final RoundReport roundReport) {
        float delta = 1.65f;
        final List<PlayReport> computerPlayReports = roundReport.getComputerPlayReports();
        final int numberOfComputers = computerPlayReports.size();
        final AtomicInteger completedComputers = new AtomicInteger(0);

        for (int i = 0; i < computerPlayReports.size(); i++) {
            final PlayReport playReport = computerPlayReports.get(i);
            final PlayerHandGroup handGroup = getHandGroup(playReport.getPlayer());

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    processComputerTurn(playReport, handGroup);
                    // Sprawdź, czy to był ostatni ruch komputera
                    if (completedComputers.incrementAndGet() == numberOfComputers) {
                        turnOnHumanInput();
                    }
                }
            }, (i + 1) * delta); // Opóźnienie względem indeksu
        }
    }

    private void processComputerTurn(PlayReport playReport, PlayerHandGroup playerHand) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        if (cardPlayed != null) {
            CardActor cardActor = playerHand.getCardActor(cardPlayed);
            putCard(cardActor, playerHand, true);
        } else {
            pullCards(playReport, playerHand);
        }
    }

    private void turnOnHumanInput() {
        setInputBlockActive(false);
        endTurnButton.setActive(false);
        pullButton.setActive(true);
        humanHand().changeTransparencyOfGroup(1f);
        Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }

    private PlayerHandGroup getHandGroup(Player player) {
        for (PlayerHandGroup handGroup : handGroups) {
            if (handGroup.getPlayerHand() == player) {
                return handGroup;
            }
        }
        return null;
    }

    public CardActor peekStackCardActor() {
        return stackCardsGroup.peekCardActor();
    }

    public PlayerHandGroup humanHand() {
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
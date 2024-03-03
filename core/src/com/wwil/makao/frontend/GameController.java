package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.*;
import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.frontend.gameComponents.CardActor;
import com.wwil.makao.frontend.gameComponents.PlayerHandGroup;
import com.wwil.makao.frontend.gameComponents.PullButtonActor;
import com.wwil.makao.frontend.gameComponents.StackCardsGroup;
import java.util.ArrayList;
import java.util.List;

//Komunikacja miedzy backendem a frontendem
public class GameController {
    private final GameplayScreen gameplayScreen;
    private final MakaoBackend backend = new MakaoBackend();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final StackCardsGroup stackCardsGroup = new StackCardsGroup(backend.getStack());
    private PullButtonActor pullButtonActor;
    private final DragAndDropManager dragAndDropManager = new DragAndDropManager(this);
    private final Stage stage;
    private boolean inputBlockActive = false;

    //Frontend może jedynie odczytywać. Jedynie może wykonywać w executeDropAction
    //Zrobic zeby dzialalo
    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.stage = gameplayScreen.getStage();
    }

    //Skupic się na jednej metodzie. (Rozbudowac inne)
    public void executeHumanAction(CardActor cardPlayed, boolean isDropped) {
        RoundReport report;
        PlayerHandGroup human = handGroups.get(0);

        if (pullButtonActor.isClick()) {
            report = handlePullButtonAction(isDropped, human);
        } else {
            if (isDropped) {
                report = handleCardDropAction(cardPlayed, human);
            } else {
                report = handleDragAction(cardPlayed);
            }
        }

        if (report.isCorrect()) {
            pullDemandedCards(report.getPlayReports().get(0));
            executeComputersTurn(report);
        }
    }

    private RoundReport handlePullButtonAction(boolean isDropped, PlayerHandGroup human) {
        RoundReport report = backend.executeAction(new Play(null, true, isDropped));
        pullCard(report.getPlayReports().get(0).getDrawn(), human);
        return report;
    }

    private void pullCard(Card card, PlayerHandGroup player) {
        CardActor drawnCard = cardActorFactory.createCardActor(card);
        if (player == getHumanHand()) {
            drawnCard.setUpSideDown(false);
            dragAndDropManager.prepareDragAndDrop(drawnCard);
        }
        player.addActor(drawnCard);
    }

    private RoundReport handleDragAction(CardActor cardPlayed) {
        RoundReport report = backend.executeAction(new Play(cardPlayed.getCard(), false, false));
        changeCardColor(report.getPlayReports().get(0).isCardCorrect(), cardPlayed);
        return report;
    }

    public void changeCardColor(boolean isCardCorrect, CardActor chosenCardActor) {
        if (isCardCorrect) {
            chosenCardActor.setColor(Color.LIME);
        } else {
            chosenCardActor.setColor(Color.SCARLET);
        }
    }

    private RoundReport handleCardDropAction(CardActor cardPlayed, PlayerHandGroup human) {
        RoundReport report = backend.executeAction(new Play(cardPlayed.getCard(), false, true));
        if (report.isCorrect()) {
            putCard(cardPlayed, human);
        } else {
            positionCardInGroup(human, cardPlayed);
        }
        return report;
    }


    private void putCard(CardActor cardToPlay, PlayerHandGroup player) {
        addCardActorToStackGroup(cardToPlay); //polozyc aktora
        endIfPlayerWon(player);
        player.moveCloserToStartingPosition(); //autowyrównanie
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    private void endIfPlayerWon(PlayerHandGroup playerHandGroup) {
        if (playerHandGroup.getPlayerHand().checkIfPlayerHaveNoCards()) {
            System.out.println("Ktos wygral");
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

    private void pullDemandedCards(PlayReport playReport) {
        PullDemander pullDemander = playReport.getPullRequest();
        if (pullDemander != null) {
            pullCards(pullDemander.getCards(), handGroups.get(pullDemander.getPerformerIndex()));
        }
    }

    private void pullCards(List<Card> cards, PlayerHandGroup player) {
        for (Card card : cards) {
            pullCard(card, player);
        }
    }

    public void executeDragStop(CardActor card) {
        PlayerHandGroup humanGroup = handGroups.get(0);
        if (!humanGroup.getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(humanGroup, card);
        }
    }

    public void turnOffHumanInput() { //todo na razie nie używane - przyda się na czas animowania
        Gdx.input.setInputProcessor(null);
        setInputBlockActive(true);
    }

    private void executeComputersTurn(final RoundReport roundReport) {
        float delta = 1.5f;
        for (int i = 1; i < handGroups.size(); i++) {
            final PlayerHandGroup currentHandGroup = handGroups.get(i);
            final PlayReport currentPlay = roundReport.getPlayReports().get(i);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    //Stwórz kart aktorów z pull request
                    pullDemandedCards(currentPlay);

                    if (currentPlay.getPlay().getCardPlayed() != null) {
                        CardActor cardToPlay = currentHandGroup.findCardActor(currentPlay.getPlay().getCardPlayed());
                        putCard(cardToPlay, currentHandGroup);
                    } else {
                        CardActor drawnCard = cardActorFactory.createCardActor(currentPlay.getDrawn());
                        currentHandGroup.addActor(drawnCard);
                    }

                }
            }, i * delta); // Opóźnienie względem indeksu
        }
    }

    private void turnOnHumanInput() {
        setInputBlockActive(false);
        pullButtonActor.changeTransparency(1);
        Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }

    public PlayerHandGroup getHumanHand() {
        return handGroups.get(0);
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public void setPullButtonActor(PullButtonActor pullButtonActor) {
        this.pullButtonActor = pullButtonActor;
    }

    public void setInputBlockActive(boolean inputBlockActive) {
        this.inputBlockActive = inputBlockActive;
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

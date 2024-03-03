package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.Play;
import com.wwil.makao.backend.PlayReport;
import com.wwil.makao.backend.RoundReport;
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

    public void executeHumanAction(CardActor cardPlayed) {
        RoundReport report;
        PlayerHandGroup human = handGroups.get(0);

        if (pullButtonActor.isClick()) {
            report = backend.executeAction(new Play(null, true));
            pullCard(report.getPlays().get(0).getDrawn(), human);
        } else {
            report = backend.executeAction(new Play(cardPlayed.getCard(), false));
            putCard(cardPlayed, human);
        }

        if (report.isCorrect()) { //sprawdza czy wykonano poprawny ruch
            executeComputersTurn(report);
        } else {
            positionCardInGroup(human, cardPlayed);
        }
    }


    private void putCard(CardActor cardToPlay, PlayerHandGroup player) {
        addCardActorToStackGroup(cardToPlay); //polozyc aktora
        endIfPlayerWon(player);
        player.moveCloserToStartingPosition(); //autowyrównanie
    }

    private void endIfPlayerWon(PlayerHandGroup playerHandGroup) {
        if (playerHandGroup.getPlayerHand().getCards().isEmpty()) {
            System.out.println("Ktos wygral");
            Gdx.app.exit();
        }
    }

    private void pullCard(Card card, PlayerHandGroup player) {
        CardActor drawnCard = cardActorFactory.createCardActor(card);
        if (player == getHumanHand()) {
            drawnCard.setUpSideDown(false);
            dragAndDropManager.prepareDragAndDrop(drawnCard);
        }
        player.addActor(drawnCard);
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

//todo Zmiana koloru przy Drag

//    public void executeDragAction(CardActor chosenCardActor) {
//        if (isCardActorCorrect(chosenCardActor)) {
//            chosenCardActor.setColor(Color.LIME);
//        } else {
//            chosenCardActor.setColor(Color.SCARLET);
//        }
//    }

    private void moveCardBackToHumanGroup(PlayerHandGroup humanGroup, CardActor card) {
        humanGroup.addActor(card);
        card.setX(card.getLastPositionBeforeRemove().x);
        card.setY(card.getLastPositionBeforeRemove().y);
        card.setZIndex((int) card.getLastPositionBeforeRemove().z);
    }

    private void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
        if (!human.getChildren().isEmpty()) {
            chosenCard.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(human, chosenCard);
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
            final PlayReport currentPlay = roundReport.getPlays().get(i);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (!currentPlay.isWaiting()) {
                        if (currentPlay.getPlayed() != null) {
                            CardActor cardToPlay = currentHandGroup.findCardActor(currentPlay.getPlayed());
                            putCard(cardToPlay, currentHandGroup);
                        } else {
                            CardActor drawnCard = cardActorFactory.createCardActor(currentPlay.getDrawn());
                            currentHandGroup.addActor(drawnCard);
                        }
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

    public void executeDragStop(CardActor card) {
        PlayerHandGroup humanGroup = handGroups.get(0);
        if (!humanGroup.getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(humanGroup, card);
        }
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

package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.*;
import com.wwil.makao.backend.Card;
import com.wwil.makao.frontend.cardChooserWindow.CardChooserGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Komunikacja miedzy back endem a front endem

//todo Zrobić refactor
public class GameController {
    private final GameplayScreen gameplayScreen;
    private final MakaoBackend backend = new MakaoBackend();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final StackCardsGroup stackCardsGroup = new StackCardsGroup(backend.getStack());
    private CardChooserGroup cardChooser;
    private PullButtonActor pullButtonActor;
    private final DragAndDropManager dragAndDropManager = new DragAndDropManager(this);
    private final Stage stage;
    private boolean inputBlockActive = false;

    //Frontend może jedynie odczytywać.
    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.stage = gameplayScreen.getStage();
    }

    public void startTurn(CardActor cardPlayed, boolean isDropped,
                          boolean isCardChooserActive, boolean humanBlock, boolean isDemanding) {
        RoundReport report;
        PlayerHandGroup human = handGroups.get(0);

        if (humanBlock) {
            report = createWaitReport();
        } else if (pullButtonActor.isClick()) {
            report = createPullReport(isDropped, human);
        } else {
            if (isDemanding) {
                report = createDemandReport(cardPlayed);
            } else if (isDropped) {
                report = createDropReport(cardPlayed, isCardChooserActive);
            } else {
                report = createDragReport(cardPlayed);
            }
        }

        if (report.isCorrect()) {
            cardChooser.setVisibility(false);
            pullCards(report.getPlayReports().get(0));
            turnOffHumanInput();
            executeComputersTurn(report);
        }
    }

    private RoundReport createWaitReport() {
        return backend.executeAction
                (new Play(null, false, false, false, true));
    }

    private RoundReport createPullReport(boolean isDropped, PlayerHandGroup human) {
        RoundReport report = backend.executeAction
                (new Play(null, true, isDropped, false, false));
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

    private RoundReport createDemandReport(CardActor cardPlayed) {
        return backend.executeAction
                (new Play(cardPlayed.getCard(), false, false, true, false));
    }

    private RoundReport createDragReport(CardActor cardPlayed) {
        RoundReport report = backend.executeAction
                (new Play(cardPlayed.getCard(), false, false, false, false));
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

    private RoundReport createDropReport(CardActor cardPlayed, boolean isCardChooserActive) {
        RoundReport report = backend.executeAction
                (new Play(cardPlayed.getCard(), false, true, isCardChooserActive, false));

        if (report.isCorrect()) {
            putCard(cardPlayed, getHumanHand(), isCardChooserActive);
        } else if (report.isChooserActive()) {
            showCardChooser(cardPlayed, getHumanHand(), isCardChooserActive);
        } else {
            positionCardInGroup(getHumanHand(), cardPlayed);
        }
        return report;
    }

    private void putCard(CardActor cardToPlay, PlayerHandGroup player, boolean isChooserActive) {
        addCardActorToStackGroup(cardToPlay); //położyć aktora
        endIfPlayerWon(player);
        if (!isChooserActive) {
            player.moveCloserToStartingPosition(); //autowyrównanie
        }
    }

    private void showCardChooser(CardActor cardPlayed, PlayerHandGroup human, boolean isChooserActive) {
        CardActor stackCard = stackCardsGroup.peekCardActor();
        putCard(cardPlayed, human, isChooserActive);
        cardChooser.setVisibility(true);
        cardChooser.getManager().setAttributesFromStackCard(stackCard, cardPlayed);
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

    private void pullCards(PlayReport playReport) {
        AbilityReport abilityReport = playReport.getAbilityReport();
        if (abilityReport != null && abilityReport.getToPull() != null) {
            pullCards(abilityReport.getToPull(), handGroups.get(abilityReport.getPerformerIndex()));
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

    public void turnOffHumanInput() {
        pullButtonActor.changeTransparency(0.5f);
        Gdx.input.setInputProcessor(null);
        setInputBlockActive(true);
    }

    private void executeComputersTurn(final RoundReport roundReport) {
        float delta = 1.5f;
        final int numberOfComputers = handGroups.size() - 1;
        final AtomicInteger completedComputers = new AtomicInteger(0);

        for (int i = 1; i < handGroups.size(); i++) {
            final PlayerHandGroup currentHandGroup = handGroups.get(i);
            final PlayReport currentPlayReport = roundReport.getPlayReports().get(i);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (!currentPlayReport.isBlocked()) {
                        processNormalComputerTurn(currentPlayReport, currentHandGroup);
                    }
                    // Sprawdź, czy to był ostatni ruch komputera
                    if (completedComputers.incrementAndGet() == numberOfComputers) {
                        if (currentPlayReport.getAbilityReport() != null && currentPlayReport.getAbilityReport().isBlockNext()) {
                            startTurn(null, false, false, true, false);
                        } else {
                            turnOnHumanInput();
                        }
                    }
                }
            }, i * delta); // Opóźnienie względem indeksu
        }
    }

    private void processNormalComputerTurn(PlayReport currentPlayReport, PlayerHandGroup currentHandGroup) {
        pullCards(currentPlayReport);
        Card cardToPlay = currentPlayReport.getPlay().getCardPlayed();
        if (cardToPlay != null) {
            putCard(currentHandGroup.findCardActor(cardToPlay), currentHandGroup, false);
            putChosenCardIfNecessary(currentPlayReport.getAbilityReport(), currentHandGroup, currentPlayReport.getPlay());
        } else {
            CardActor drawnCard = cardActorFactory.createCardActor(currentPlayReport.getDrawn());
            currentHandGroup.addActor(drawnCard);
        }
    }

    private void putChosenCardIfNecessary(AbilityReport abilityReport, PlayerHandGroup currentHandGroup, Play play) {
        if (abilityReport != null &&
                (abilityReport.getChoosenCard() != null && !abilityReport.isDemanded()
                        || play.getCardPlayed().getRank().equals(Rank.JOKER))) {
            putCard(cardActorFactory.createCardActor(abilityReport.getChoosenCard()),
                    currentHandGroup, false);
        }
    }

    private void turnOnHumanInput() {
        setInputBlockActive(false);
        pullButtonActor.changeTransparency(1);
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

    public void setPullButtonActor(PullButtonActor pullButtonActor) {
        this.pullButtonActor = pullButtonActor;
    }

    public void setInputBlockActive(boolean inputBlockActive) {
        this.inputBlockActive = inputBlockActive;
    }

    public void setCardChooser(CardChooserGroup cardChooser) {
        this.cardChooser = cardChooser;
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

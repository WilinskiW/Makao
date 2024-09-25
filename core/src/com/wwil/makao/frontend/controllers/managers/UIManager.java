package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.frontend.controllers.gameplay.GameController;
import com.wwil.makao.frontend.controllers.gameplay.GameStagePreparer;
import com.wwil.makao.frontend.controllers.gameplay.GameplayScreen;
import com.wwil.makao.frontend.entities.cardChooser.actors.CardChooserGroup;
import com.wwil.makao.frontend.entities.cards.*;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.utils.params.GUIparams;
import com.wwil.makao.frontend.utils.text.ReportToTextConverter;
import com.wwil.makao.frontend.utils.text.TextContainer;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private final GameController controller;
    private final GameplayScreen gameplayScreen;
    private GameDeckGroup gameDeckGroup;
    private final StackCardsGroup stackCardsGroup;
    private final CardActorFactory cardActorFactory;
    private final List<PlayerHandGroup> handGroups;
    private TextContainer textContainer;
    private CardChooserGroup cardChooser;
    private GameButton pullButton;
    private GameButton endTurnButton;
    private GameButton makaoButton;


    public UIManager(GameController controller, GameplayScreen gameplayScreen) {
        this.controller = controller;
        this.gameplayScreen = gameplayScreen;
        this.stackCardsGroup = new StackCardsGroup();
        this.cardActorFactory = new CardActorFactory();
        this.handGroups = new ArrayList<>();
    }

    public void prepareStage() {
        new GameStagePreparer(this, controller.getBackend()).execute();
    }

    public void changeToEndingScreen(String whoWon) {
        controller.changeScreen(whoWon);
    }

    public void updateButtonStates(boolean isPullActive, boolean isEndActive, boolean isMakaoActive) {
        pullButton.setActive(isPullActive);
        endTurnButton.setActive(isEndActive);
        makaoButton.setActive(isMakaoActive);
    }

    public void changeCardColor(boolean isValid, CardActor cardActor) {
        if (isValid) {
            cardActor.setColor(Color.LIME);
        } else {
            cardActor.setColor(Color.SCARLET);
        }
    }

    public void changeText(PlayReport playReport) {
        String newText = ReportToTextConverter.convert(playReport);
        textContainer.setText(newText);
    }

    //todo przenieść do player hand group
    public void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
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

    public void deployCardToStage(CardActor card) {
        card.saveGroup();
        card.setLastPositionBeforeRemove(new Vector3(card.getX(), card.getY(), card.getZIndex()));
        gameplayScreen.getStage().addActor(card);
    }

    public GameController getController() {
        return controller;
    }

    public void positionCardInHumanHandGroup(CardActor card) {
        if (!getHumanHandGroup().getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(getHumanHandGroup(), card);
        }
    }

    public void addCardToStack(CardActor cardActor) {
        if (cardActor.getCard().isShadow()) {
            cardActor.setColor(Color.GRAY);
        }
        cardActor.setUpsideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    public Action putCardWithAnimation(PlayReport playReport, CardActor cardActor, PlayerHandGroup handGroup) {
        Action moveCard = getMovementAction(new Vector3(0, 0, 0), 2f);

        Action finishAnimation = new Action() {
            @Override
            public boolean act(float delta) {
                cardActor.setUpsideDown(false);
                changeText(playReport);
                cardActor.setRotation(handGroup.getRotation());
                controller.getSoundManager().playPut();
                endGameIfPlayerWon(handGroup);
                return true;
            }
        };

        Action sequence = Actions.sequence(moveCard, finishAnimation);
        sequence.setTarget(cardActor);
        return sequence;
    }

    private Action getMovementAction(Vector3 groupPos, float duration) {
        MoveToAction moveCard = new MoveToAction();
        moveCard.setPosition(groupPos.x, groupPos.y);  // Używamy obliczonych współrzędnych
        moveCard.setDuration(duration);
        moveCard.setInterpolation(Interpolation.exp10);

        RotateToAction rotateCard = new RotateToAction();
        rotateCard.setRotation(0);
        rotateCard.setDuration(duration);
        rotateCard.setInterpolation(Interpolation.exp10);

        // Zwróć równoczesną akcję (animacja ruchu + rotacja)
        return Actions.parallel(moveCard, rotateCard);
    }

    public void endGameIfPlayerWon(PlayerHandGroup handGroup) {
        if (handGroup.getPlayer().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
            changeToEndingScreen(handGroup.getPlayer().toString());
        }
        //todo boolean w TurnManager hasPlayerWon
        //If yes -> UIManager.changeToEndingScreen
    }

    public Action pullCardWithAnimation(PlayReport playReport, PlayerHandGroup handGroup) {
        // Pobierz kartę, która ma zostać przeniesiona
        CardActor pulledCard = gameDeckGroup.peekCardActor();

        // Oblicz ilość dzieci rodzica, tak aby odpowiednio wyrównać kartę na scenie
        int groupSize = pulledCard.getParent().getChildren().size;

        // Przelicz lokalne współrzędne karty w starej grupie na globalne (sceniczne) współrzędne
        Vector2 stagePosition = pulledCard.localToStageCoordinates(new Vector2(pulledCard.getX(), pulledCard.getY()));

        // Przelicz globalną rotacje
        float globalRotation = pulledCard.getRotation() + pulledCard.getParent().getRotation();

        // Usuń kartę z grupy
        pulledCard.remove();

        // Dodaj kartę do talii gracza
        handGroup.addActor(pulledCard);

        // Ustaw rotację globalną karty
        pulledCard.setRotation(globalRotation - handGroup.getRotation());

        // Zapisz pozycje jaka została przydzielona przy dołączaniu do grupy
        Vector3 groupPosition = new Vector3(pulledCard.getX(), pulledCard.getY(), pulledCard.getZIndex());

        // Przelicz współrzędne globalne (sceniczne) na lokalne współrzędne nowej grupy
        Vector2 newLocalPosition = handGroup.stageToLocalCoordinates(stagePosition);

        // Ustaw kartę w nowej grupie na odpowiedniej pozycji (tej samej co miała na scenie)
        pulledCard.setPosition(newLocalPosition.x - groupSize * GUIparams.GAP_BETWEEN_DECK_CARDS, newLocalPosition.y);

        // Animacja ruchu karty do docelowej pozycji
        Action moveCard = getMovementAction(groupPosition, 1f);

        // Końcowe akcje po animacji
        Action finishAnimation = new Action() {
            @Override
            public boolean act(float delta) {
                pulledCard.setUpsideDown(false);
                pulledCard.setRotation(0); // Wyrównaj rotację karty z grupą
                changeText(playReport);
                controller.getSoundManager().playPull();
                return true;
            }
        };

        Action sequence = Actions.sequence(moveCard, finishAnimation);
        sequence.setTarget(pulledCard);  // Ustaw cel sekwencji na kartę

        return sequence;
    }

    public void changeTransparencyOfPlayerGroup(PlayerHandGroup playerHandGroup, float alpha) {
        for (CardActor cardActor : playerHandGroup.getCardActors()) {
            cardActor.changeTransparency(alpha);
        }
    }

    public GameplayScreen getGameplayScreen() {
        return gameplayScreen;
    }

    public GameDeckGroup getGameDeckGroup() {
        return gameDeckGroup;
    }

    public void setGameDeckGroup(GameDeckGroup gameDeckGroup) {
        this.gameDeckGroup = gameDeckGroup;
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public CardActor peekStackCardActor() {
        return stackCardsGroup.peekCardActor();
    }

    public CardActorFactory getCardActorFactory() {
        return cardActorFactory;
    }

    public List<PlayerHandGroup> getHandGroups() {
        return handGroups;
    }

    public PlayerHandGroup getHumanHandGroup() {
        return handGroups.get(0);
    }

    public CardChooserGroup getCardChooser() {
        return cardChooser;
    }

    public void setCardChooser(CardChooserGroup cardChooser) {
        this.cardChooser = cardChooser;
    }

    public void setPullButton(GameButton pullButton) {
        this.pullButton = pullButton;
    }

    public void setEndTurnButton(GameButton endTurnButton) {
        this.endTurnButton = endTurnButton;
    }

    public GameButton getMakaoButton() {
        return makaoButton;
    }

    public void setMakaoButton(GameButton makaoButton) {
        this.makaoButton = makaoButton;
    }

    public void setTextContainer(TextContainer textContainer) {
        this.textContainer = textContainer;
    }
}

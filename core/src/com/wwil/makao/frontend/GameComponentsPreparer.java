package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.Card;
import com.wwil.makao.backend.Rank;
import com.wwil.makao.backend.Suit;
import com.wwil.makao.frontend.entities.gameButtons.EndTurnButton;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.cardChooser.CardChooserGroup;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardsGroup.PlayerHandGroup;
import com.wwil.makao.frontend.entities.gameButtons.PullButton;

import java.util.Collections;
import java.util.List;

//Przygotowanie elementów graficznych ekranu
public class GameComponentsPreparer {
    private final GameController controller;
    private final MakaoBackend backend;
    private final List<PlayerHandGroup> handGroups;
    private final CardActorFactory cardActorFactory;
    private final Stage stage;

    public GameComponentsPreparer(GameController controller, Stage stage) {
        this.controller = controller;
        this.backend = controller.getBackend();
        this.handGroups = controller.getHandGroups();
        this.cardActorFactory = controller.getCardActorFactory();
        this.stage = stage;
    }

    public void execute() {
        prepareStackCardsGroup();
        preparePullButton();
        prepareEndTurnButton();
        prepareHandGroups();
        createCardChooser();
    }

    private void prepareStackCardsGroup() {
        controller.addCardActorToStackGroup(cardActorFactory.createCardActor(backend.getStack().peekCard()));
        stage.addActor(controller.getStackCardsGroup());
        controller.getStackCardsGroup().setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private void preparePullButton() {
        GameButton pullButton = new PullButton(controller);
        stage.addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        controller.setPullButton(pullButton);
    }

    private void prepareEndTurnButton() {
        GameButton endTurnButton = new EndTurnButton(controller);
        stage.addActor(endTurnButton);
        endTurnButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 25);
        controller.setEndTurnButton(endTurnButton);
    }

    private void prepareHandGroups() {
        createHandGroups();
        adjustHumanCards();
        positionHandGroupsOnStage();
    }

    public void createHandGroups() {
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup(backend.getPlayers().get(i)));
        }
        setPlayersCardActorsAlignmentParams();

        //todo metoda testowa:
        test(2);

        for (PlayerHandGroup handGroup : handGroups) {
            for (Card card : handGroup.getPlayerHand().getCards()) {
                CardActor cardActor = controller.getCardActorFactory().createCardActor(card);
                handGroup.addActor(cardActor);
            }
        }
    }

    private void test(int subject) {
        handGroups.get(1).getPlayerHand().getCards().clear();
        handGroups.get(1).getPlayerHand().addCardsToHand(Collections.singletonList(new Card(Rank.SEVEN,Suit.HEART)));
        handGroups.get(1).getPlayerHand().addCardsToHand(Collections.singletonList(new Card(Rank.FIVE, Suit.SPADE)));
        handGroups.get(subject).getPlayerHand().getCards().clear();
        handGroups.get(subject).getPlayerHand().addCardsToHand(Collections.singletonList(new Card(Rank.K,Suit.SPADE)));
        handGroups.get(subject).getPlayerHand().addCardsToHand(Collections.singletonList(new Card(Rank.SIX,Suit.CLUB)));
    }


    private void setPlayersCardActorsAlignmentParams() {
        handGroups.get(0).setCardsAlignment(CardsAlignmentParams.SOUTH);
        handGroups.get(1).setCardsAlignment(CardsAlignmentParams.EAST);
        handGroups.get(2).setCardsAlignment(CardsAlignmentParams.NORTH);
        handGroups.get(3).setCardsAlignment(CardsAlignmentParams.WEST);
    }


    private void adjustHumanCards() {
        for (CardActor card : controller.getHumanHand().getCardActors()) {
            card.setUpSideDown(false);
            controller.getDragAndDropManager().prepareDragAndDrop(card);
        }
    }

    private void positionHandGroupsOnStage() {
        for (PlayerHandGroup handGroup : controller.getHandGroups()) {
            stage.addActor(handGroup);
        }
        setRotationOfHandGroups();
        setPositionOfHandGroups();
    }

    private void setRotationOfHandGroups() {
        controller.getHandGroups().get(1).setRotation(90);
        controller.getHandGroups().get(2).setRotation(180);
        controller.getHandGroups().get(3).setRotation(-90);
    }

    private void setPositionOfHandGroups() {
        //South
        controller.getHandGroups().get(0).setPosition
                (GUIparams.WIDTH / 2f,
                        0);
        //East
        controller.getHandGroups().get(1).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 10,
                GUIparams.HEIGHT / 2.0f);
        //North
        controller.getHandGroups().get(2).setPosition(GUIparams.WIDTH / 2f + GUIparams.CARD_WIDTH,
                GUIparams.HEIGHT + GUIparams.CARD_HEIGHT - 5);
        //West
        controller.getHandGroups().get(3).setPosition(GUIparams.CARD_WIDTH / 5f - 32,
                GUIparams.HEIGHT / 2f + GUIparams.CARD_HEIGHT / 2f + 25);
    }

    private void createCardChooser() {
        CardChooserGroup cardChooser = new CardChooserGroup(controller);
        stage.addActor(cardChooser);
        cardChooser.setPosition(0, 0);
        controller.setCardChooser(cardChooser);
    }
}

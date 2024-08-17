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
public class GameStagePreparer {
    private final GameController controller;
    private final MakaoBackend backend;
    private final List<PlayerHandGroup> handGroups;
    private final CardActorFactory cardActorFactory;
    private final Stage stage;

    public GameStagePreparer(GameController controller, Stage stage) {
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
        controller.addCardActorToStackGroup(cardActorFactory.createCardActor(backend.getDeckManager().peekStackCard()));
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
        for (int i = 0; i < backend.getPlayerManager().getPlayers().size(); i++) {
            handGroups.add(new PlayerHandGroup(backend.getPlayerManager().getPlayers().get(i)));
            preparePlayer(i);
        }
    }

    private void preparePlayer(int index) {
        PlayerHandGroup handGroup = handGroups.get(index);
        handGroup.setCardsAlignment(CardsAlignmentParams.getParamFromOrdinal(index));

        //todo  Metoda tylko wyłącznie do testów! Usuń po testach
        test(index);

        for (Card card : handGroup.getPlayer().getCards()) {
            CardActor cardActor = controller.getCardActorFactory().createCardActor(card);
            handGroup.addActor(cardActor);
        }
    }

    private void test(int index) {
        switch (index) {
            case 0:
                handGroups.get(index).getPlayer().getCards().clear();
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.TWO, Suit.CLUB)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.TWO, Suit.SPADE)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.TWO, Suit.DIAMOND)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.TWO, Suit.HEART)));
                break;
            case 1:
                handGroups.get(index).getPlayer().getCards().clear();
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.SEVEN, Suit.CLUB)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.SPADE)));
                break;
            case 2:
//                handGroups.get(index).getPlayer().getCards().clear();
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.SEVEN, Suit.CLUB)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.THREE, Suit.SPADE)));
                break;
            case 3:
                handGroups.get(index).getPlayer().getCards().clear();
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.CLUB)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.HEART)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.DIAMOND)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.HEART)));
                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.FIVE, Suit.HEART)));
                break;
        }
    }


    private void adjustHumanCards() {
        for (CardActor card : controller.humanHand().getCardActors()) {
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
                GUIparams.HEIGHT / 2f);
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

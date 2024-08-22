package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.card.Suit;
import com.wwil.makao.frontend.entities.gameButtons.EndTurnButton;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.cardChooser.CardChooserGroup;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardsGroup.PlayerHandGroup;
import com.wwil.makao.frontend.entities.gameButtons.PullButton;

//Przygotowanie elementów graficznych ekranu
public class GameStagePreparer {
    private final UIManager uiManager;
    private final BackendFacade backend;
    private final CardActorFactory cardActorFactory;
    private final Stage stage;

    public GameStagePreparer(UIManager uiManager, BackendFacade backend) {
        this.uiManager = uiManager;
        this.backend = backend;
        this.cardActorFactory = uiManager.getCardActorFactory();
        this.stage = uiManager.getGameplayScreen().getStage();
    }

    public void execute() {
        prepareStackCardsGroup();
        preparePullButton();
        prepareEndTurnButton();
        prepareHandGroups();
        createCardChooser();
    }

    private void prepareStackCardsGroup() {
        uiManager.addCardActorToStackGroup(cardActorFactory.createCardActor(backend.getDeckManager().peekStackCard()));
        stage.addActor(uiManager.getStackCardsGroup());
        uiManager.getStackCardsGroup().setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private void preparePullButton() {
        GameButton pullButton = new PullButton(uiManager.getController());
        stage.addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        uiManager.setPullButton(pullButton);
    }

    private void prepareEndTurnButton() {
        GameButton endTurnButton = new EndTurnButton(uiManager.getController());
        stage.addActor(endTurnButton);
        endTurnButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 25);
        uiManager.setEndTurnButton(endTurnButton);
    }

    private void prepareHandGroups() {
        createHandGroups();
        adjustHumanCards();
        positionHandGroupsOnStage();
    }

    public void createHandGroups() {
        for (int i = 0; i < backend.getPlayers().size(); i++) {
            uiManager.getHandGroups().add(new PlayerHandGroup(backend.getPlayers().get(i)));
            preparePlayer(i);
        }
    }

    private void preparePlayer(int index) {
        PlayerHandGroup handGroup = uiManager.getHandGroups().get(index);
        handGroup.setCardsAlignment(CardsAlignmentParams.getParamFromOrdinal(index));

        //todo  Metoda tylko wyłącznie do testów! Usuń po testach
        test(index);

        for (Card card : handGroup.getPlayer().getCards()) {
            CardActor cardActor = cardActorFactory.createCardActor(card);
            handGroup.addActor(cardActor);
        }
    }

    private void test(int index) {
        switch (index) {
            case 0:
                backend.getPlayers().get(index).getCards().clear();
                backend.getPlayers().get(index).getCards().add(new Card(Rank.FOUR, Suit.CLUB));
                backend.getPlayers().get(index).getCards().add(new Card(Rank.FOUR, Suit.SPADE));
                backend.getPlayers().get(index).getCards().add(new Card(Rank.FOUR, Suit.HEART));
                backend.getPlayers().get(index).getCards().add(new Card(Rank.FOUR, Suit.DIAMOND));

                break;
            case 1:
//                handGroups.get(index).getPlayer().getCards().clear();
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.SEVEN, Suit.CLUB)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.SPADE)));
                break;
            case 2:
//                handGroups.get(index).getPlayer().getCards().clear();
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.SEVEN, Suit.CLUB)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.THREE, Suit.SPADE)));
                break;
            case 3:
//                handGroups.get(index).getPlayer().getCards().clear();
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.CLUB)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.HEART)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.DIAMOND)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.K, Suit.HEART)));
//                handGroups.get(index).getPlayer().addCardsToHand(Collections.singletonList(new Card(Rank.FIVE, Suit.HEART)));
                break;
        }
    }


    private void adjustHumanCards() {
        for (CardActor card : uiManager.getHumanHandGroup().getCardActors()) {
            card.setUpSideDown(false);
            uiManager.getController().getInputManager().getDragAndDropManager().prepareDragAndDrop(card);
        }
    }

    private void positionHandGroupsOnStage() {
        for (PlayerHandGroup handGroup : uiManager.getHandGroups()) {
            stage.addActor(handGroup);
        }
        setRotationOfHandGroups();
        setPositionOfHandGroups();
    }

    private void setRotationOfHandGroups() {
        uiManager.getHandGroups().get(1).setRotation(90);
        uiManager.getHandGroups().get(2).setRotation(180);
        uiManager.getHandGroups().get(3).setRotation(-90);
    }

    private void setPositionOfHandGroups() {
        //South
        uiManager.getHandGroups().get(0).setPosition
                (GUIparams.WIDTH / 2f,
                        0);
        //East
        uiManager.getHandGroups().get(1).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 10,
                GUIparams.HEIGHT / 2f);
        //North
        uiManager.getHandGroups().get(2).setPosition(GUIparams.WIDTH / 2f + GUIparams.CARD_WIDTH,
                GUIparams.HEIGHT + GUIparams.CARD_HEIGHT - 5);
        //West
        uiManager.getHandGroups().get(3).setPosition(GUIparams.CARD_WIDTH / 5f - 32,
                GUIparams.HEIGHT / 2f + GUIparams.CARD_HEIGHT / 2f + 25);
    }

    private void createCardChooser() {
        CardChooserGroup cardChooser = new CardChooserGroup(uiManager.getController());
        stage.addActor(cardChooser);
        cardChooser.setPosition(0, 0);
        uiManager.setCardChooser(cardChooser);
    }
}

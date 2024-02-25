package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.frontend.gameComponents.CardActor;
import com.wwil.makao.frontend.gameComponents.PlayerHandGroup;
import com.wwil.makao.frontend.gameComponents.PullButtonActor;
import com.wwil.makao.frontend.gameComponents.StackCardsGroup;
import com.wwil.makao.frontend.parameters.CardsAlignmentParams;

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

    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        gameplayScreen.getStage().addActor(cardActor);
        cardActor.setUpSideDown(false);
        getStackCardsGroup().addActor(cardActor);
    }


    public void createHandGroups() {
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup(getBackend().getPlayers().get(i)));
        }
        setPlayersCardActorsAlignmentParams();
        for(PlayerHandGroup handGroup : handGroups){
            for(Card card : handGroup.getPlayerHand().getCards()){
                CardActor cardActor = cardActorFactory.createCardActor(card);
                handGroup.addActor(cardActor);
            }
        }
    }

    private void setPlayersCardActorsAlignmentParams() {
        getHandGroups().get(0).setCardsAlignment(CardsAlignmentParams.SOUTH);
        getHandGroups().get(1).setCardsAlignment(CardsAlignmentParams.EAST);
        getHandGroups().get(2).setCardsAlignment(CardsAlignmentParams.NORTH);
        getHandGroups().get(3).setCardsAlignment(CardsAlignmentParams.WEST);
    }

    public CardActor createStartingCardActorForStackGroup(){
        return cardActorFactory.createCardActor(backend.getStack().peekCard());
    }


    public void executeDragAction(CardActor chosenCardActor) {
        if (isCardActorCorrect(chosenCardActor)) {
            chosenCardActor.setColor(Color.LIME);
        } else {
            chosenCardActor.setColor(Color.SCARLET);
        }
    }

    public void executeDropAction(CardActor chosenCardActor) {
        PlayerHandGroup human = handGroups.get(0);
        if (isCardActorCorrect(chosenCardActor)) {
            putPlayerCardOnStack(chosenCardActor,0);
            computerTurns();
        } else {
            positionCardInGroup(human, chosenCardActor);
        }
    }

    private void putPlayerCardOnStack(CardActor cardActor, int currentPlayerIndex){
        updateBackendAfterPuttingCardOnStack(cardActor,currentPlayerIndex);
        addCardActorToStackGroup(cardActor);
        backend.endIfPlayerWon(currentPlayerIndex);
        handGroups.get(currentPlayerIndex).moveCloserToStartingPosition();
    }

    private void updateBackendAfterPuttingCardOnStack(CardActor chosenCardActor, int playerIndex) {
        backend.useCardAbility(chosenCardActor.getCard(), playerIndex);
        backend.getStack().addCardToStack(chosenCardActor.getCard());
        backend.getPlayers().get(playerIndex).removeCardFromHand(chosenCardActor.getCard());
    }

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

    private boolean isCardActorCorrect(CardActor chosenCard) {
        return backend.isCorrectCard(chosenCard.getCard());
    }

    private void computerTurns() {
        turnOffHumanInput();
        executeComputersTurn();
    }

    private void turnOffHumanInput() {
        backend.setInputBlock(true);
        pullButtonActor.changeTransparency(0.5f);
        Gdx.input.setInputProcessor(null);
    }

    //todo do refactora
    private void executeComputersTurn() {
        for (int i = 1; i < handGroups.size(); i++) {
            final int playerIndex = i;
            final float delta = 1.5f;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (!handGroups.get(playerIndex).getPlayerHand().isWaiting()) {
                        CardActor cardActorToPlay = getPlayableCard(playerIndex);
                        if (cardActorToPlay != null) {
                            putPlayerCardOnStack(cardActorToPlay,playerIndex);
                        } else {
                            backend.playerPullCard(playerIndex);
                            playerPullCardActor(playerIndex);
                        }
                    }
                    handGroups.get(playerIndex).getPlayerHand().setWaiting(false);
                    checkAndHandleHumanTurn(playerIndex);
                }
            }, i * delta); // Opóźnienie względem indeksu
        }
    }

    private void playerPullCardActor(int playerIndex){
        CardActor cardActor = cardActorFactory.createCardActor(backend.playerPullCard(playerIndex));
        gameplayScreen.getStage().addActor(cardActor);
        handGroups.get(playerIndex).addActor(cardActor);
        if(playerIndex == 0){
            cardActor.setUpSideDown(false);
            dragAndDropManager.prepareDragAndDrop(cardActor, dragAndDropManager.getTarget());
        }
    }


    private void checkAndHandleHumanTurn(int playerIndex) {
        int lastIndex = handGroups.size() - 1;
        if (lastIndex == playerIndex) {
            if (!getHumanHand().getPlayerHand().isWaiting()) {
                turnOnHumanInput();
            } else {
                getHumanHand().getPlayerHand().setWaiting(false);
                executeComputersTurn();
            }
        }
    }

    private void turnOnHumanInput() {
        pullButtonActor.changeTransparency(1);
        Gdx.input.setInputProcessor(gameplayScreen.getStage());
        backend.setInputBlock(false);
    }

    private CardActor getPlayableCard(int index) {
        SnapshotArray<Actor> playerCards = handGroups.get(index).getChildren();
        for (int i = 0; i < playerCards.size; i++) {
            CardActor currentCardActor = (CardActor) playerCards.get(i);
            if (isCardActorCorrect(currentCardActor)) {
                return currentCardActor;
            }
        }
        return null;
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

    public void handlePullButtonInput() {
        int graphicsY = gameplayScreen.getViewport().getScreenHeight() - Gdx.input.getY();
        if (pullButtonActor.checkIfButtonIsClick(graphicsY) && !backend.isInputBlock()) {
            performPullButtonClick();
        }
    }

    private void performPullButtonClick() {
        playerPullCardActor(0);
        pullButtonActor.setClick(true);
        performButtonAnimation();
        computerTurns();
    }


    private void performButtonAnimation() {
        Timer.Task undoClick = new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                pullButtonActor.setClick(false);
            }
        };
        Timer.schedule(undoClick, 0.5f);
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public void setPullButtonActor(PullButtonActor pullButtonActor) {
        this.pullButtonActor = pullButtonActor;
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

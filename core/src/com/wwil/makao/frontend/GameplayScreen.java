package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.frontend.gameComponents.CardActor;
import com.wwil.makao.frontend.gameComponents.PlayerHandGroup;
import com.wwil.makao.frontend.gameComponents.PullButtonActor;
import com.wwil.makao.frontend.gameComponents.StackCardsGroup;
import com.wwil.makao.frontend.parameters.CardsAligmentParams;
import com.wwil.makao.frontend.parameters.GUIparams;

import java.util.*;
import java.util.List;

public class GameplayScreen implements Screen {
    private final Makao makao;
    private OrthographicCamera camera;
    private Stage stage;
    private final MakaoBackend backend = new MakaoBackend();
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private PullButtonActor pullButtonActor;
    private FitViewport viewport;
    private DragAndDrop.Target dragTarget;
    private final StackCardsGroup stackCardsGroup = new StackCardsGroup(backend.getStack());

    // TODO: Animacje rzucania kart przez boty
    // TODO: Aktywacja specjalnych zdolności kart ~ Specjalne zdolności
    // TODO: Obrona
    // TODO: Komunikaty kcji graczy
    // TODO: Główne menu
    // TODO: Dźwięk i muzyk
    // TODO: Możliwość zmiany skinów do kart
    //tworzy główny ekran gry
    public GameplayScreen(Makao makao) {
        this.makao = makao;
        prepareGraphicComponents();
        prepareGameComponents();
    }
    /////////////////////////////////////////////////////////////////////////////////

    private void prepareGraphicComponents() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);

        this.viewport = new FitViewport(0, 0, camera);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);
    }

    private void prepareGameComponents() {
        prepareStackCardsGroup();
        preparePullButton();
        this.dragTarget = prepareTarget(stackCardsGroup);
        prepareHandGroups();
    }

    private void prepareStackCardsGroup() {
        CardActor stackCard = cardActorFactory.createCardActor(backend.getStack().peekCard());
        addCardActorToStack(stackCard);
        stage.addActor(stackCardsGroup);
        stackCardsGroup.setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }


    private void addCardActorToStack(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    private void preparePullButton() {
        PullButtonActor pullButton = new PullButtonActor();
        stage.addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        this.pullButtonActor = pullButton;
    }

    private void prepareHandGroups() {
        createHandGroups();
        createAllPlayersCardsActorsThatWereNotDrew();
        positionHandGroupsOnStage();
    }


    private void createAllPlayersCardsActorsThatWereNotDrew(){
        for (int i = 0; i < handGroups.size(); i++) {
            createAllPlayerCardsActorsThatWereNotDrew(i);
        }
    }

    private void createHandGroups(){
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup(backend.getPlayers().get(i)));
        }
        setPlayersCardActorsAlignmentParams();
    }

    private void setPlayersCardActorsAlignmentParams() {
        // TODO: 18.02.2024 Poprawić for loopem przy pomocy value
        handGroups.get(0).setCardsAlignment(CardsAligmentParams.SOUTH);
        handGroups.get(1).setCardsAlignment(CardsAligmentParams.EAST);
        handGroups.get(2).setCardsAlignment(CardsAligmentParams.NORTH);
        handGroups.get(3).setCardsAlignment(CardsAligmentParams.WEST);
    }

    private void createAllPlayerCardsActorsThatWereNotDrew(int playerIndex) {
        for (Card card : backend.getPlayers().get(playerIndex).getCards()) {
            if (card.isDrawn()) {
                handGroups.get(playerIndex).addActor(cardActorFactory.createCardActor(card));
            }
        }

        if(playerIndex == 0){
            adjustHumanCards(dragTarget);
        }
    }

    private void adjustHumanCards(DragAndDrop.Target target){
        PlayerHandGroup group = getHumanHand();
        for (CardActor card : group.getCardActors()) {
            card.setUpSideDown(false);
            prepareDragAndDrop(card, target);
        }
    }

    private void positionHandGroupsOnStage() {
        for (PlayerHandGroup handGroup : handGroups) {
            stage.addActor(handGroup);
        }
        setRotationOfHandGroups();
        setPositionOfHandGroups();
    }

    private void setRotationOfHandGroups() {
        handGroups.get(1).setRotation(90);
        handGroups.get(2).setRotation(180);
        handGroups.get(3).setRotation(-90);
    }

    private void setPositionOfHandGroups() {
        // TODO: 05.02.2024 Poprawić ustawianie grupy
        //South
        handGroups.get(0).setPosition
                (GUIparams.WIDTH / 2f,
                        0);
        //East
        handGroups.get(1).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 10,
                GUIparams.HEIGHT / 2.0f);
        //North
        handGroups.get(2).setPosition(GUIparams.WIDTH / 2f + GUIparams.CARD_WIDTH,
                GUIparams.HEIGHT + GUIparams.CARD_HEIGHT - 25);
        //West
        handGroups.get(3).setPosition(GUIparams.CARD_WIDTH / 5f - 32,
                GUIparams.HEIGHT / 2f + GUIparams.CARD_HEIGHT / 2f + 25);
    }

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) { // stack - target
        //Target
        return new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) { // SOURCE - CARD
                CardActor chosenCardActor = (CardActor) source.getActor();
                executeDragAction(chosenCardActor);
                return true;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                source.getActor().setColor(Color.WHITE);
                super.reset(source, payload);
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                CardActor chosenCardActor = (CardActor) source.getActor();
                executeDropAction(chosenCardActor);
            }

        };
    }

    private void executeDragAction(CardActor chosenCardActor) {
        if (isCardActorCorrect(chosenCardActor)) {
            chosenCardActor.setColor(Color.LIME);
        } else {
            chosenCardActor.setColor(Color.SCARLET);
        }
    }

    private void executeDropAction(CardActor chosenCardActor) {
        PlayerHandGroup human = handGroups.get(0);
        if (isCardActorCorrect(chosenCardActor)) {
            updateBackendAfterPuttingCardOnStack(chosenCardActor, 0);
            stackCardsGroup.addActor(chosenCardActor);
            createAllPlayersCardsActorsThatWereNotDrew();
            endIfHumanWin();
            human.moveCloserToStartingPosition();
            computerTurns();
        } else {
            positionCardInGroup(human, chosenCardActor);
        }
    }
//todo Do refactora
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

    //TODO To powinnien robić BACKEND
    private void endIfHumanWin() {
        if (handGroups.get(0).checkIsPlayerHasNoCards()) {
            System.out.println("You win!");
            Gdx.app.exit();
        }
    }

    private void turnOffHumanInput() {
        backend.setInputBlock(true);
        Gdx.input.setInputProcessor(null);
    }

    private void executeComputersTurn() {
        for (int i = 1; i < handGroups.size(); i++) {
            final int playerIndex = i;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if(!handGroups.get(playerIndex).getPlayerHand().isWaiting()) {
                        CardActor cardActorToPlay = preparePlayableCard(playerIndex);
                        if (cardActorToPlay != null) {
                            addCardActorToStack(cardActorToPlay);
                            updateBackendAfterPuttingCardOnStack(cardActorToPlay, playerIndex);
                            createAllPlayersCardsActorsThatWereNotDrew();
                            endIfComputerWin(playerIndex);
                            handGroups.get(playerIndex).moveCloserToStartingPosition();
                        } else {
                            computerPullCard(playerIndex);
                        }
                    }
                    handGroups.get(playerIndex).getPlayerHand().setWaiting(false);
                }
            }, i * 1f); // Opóźnienie względem indeksu

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if(!getHumanHand().getPlayerHand().isWaiting()) {
                        turnOnHumanInput();
                    }
                    else {
                        getHumanHand().getPlayerHand().setWaiting(false);
                    }
                }
            }, (handGroups.size() - 1) * 1f);
        }
    }

    private void endIfComputerWin(int playerIndex) {
        PlayerHandGroup currentHandGroup = handGroups.get(playerIndex);
        if (currentHandGroup.checkIsPlayerHasNoCards()) {
            System.out.println("Player " + (playerIndex + 1) + " won!");
            Gdx.app.exit();
        }
    }

    private void turnOnHumanInput() {
        Gdx.input.setInputProcessor(stage);
        backend.setInputBlock(false);
    }

    private CardActor preparePlayableCard(int index) {
        SnapshotArray<Actor> playerCards = handGroups.get(index).getChildren();
        for (int i = 0; i < playerCards.size; i++) {
            CardActor card = (CardActor) playerCards.get(i);
            if (isCardActorCorrect(card)) {
                return card;
            }
        }
        return null;
    }

    private void computerPullCard(int playerIndex) {
        CardActor cardActor = cardActorFactory.createCardActor(backend.takeCardFromGameDeck());
        cardActor.setUpSideDown(GUIparams.HIDE_COMPUTER_CARD);
        backend.getPlayers().get(playerIndex).addCardToHand(cardActor.getCard());
        handGroups.get(playerIndex).addActor(cardActor);
    }

    private void prepareDragAndDrop(final CardActor card, DragAndDrop.Target target) {
        final DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(GUIparams.CARD_WIDTH / 2f, -GUIparams.CARD_HEIGHT / 2f);
        DragAndDrop.Source dropSource = prepareSource(card);
        dragAndDrop.addSource(dropSource);
        dragAndDrop.addTarget(target);
    }

    private DragAndDrop.Source prepareSource(final CardActor card) {

        return new DragAndDrop.Source(card) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                payload.setObject(card);
                prepareCardToStage();
                return payload;
            }

            private void prepareCardToStage() {
                card.saveGroup();
                card.setLastPositionBeforeRemove(new Vector3(card.getX(), card.getY(), card.getZIndex()));
                stage.addActor(card);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    executeDragStop(card);
                }
                super.dragStop(event, x, y, pointer, payload, target);
            }
        };
    }

    private void executeDragStop(CardActor card) {
        PlayerHandGroup humanGroup = handGroups.get(0);
        if (!humanGroup.getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(humanGroup, card);
        }
    }

    private PlayerHandGroup getHumanHand() {
        return handGroups.get(0);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.FOREST);
        makao.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();

        int graphicsY = viewport.getScreenHeight() - Gdx.input.getY();
        if (pullButtonActor.checkIfButtonIsClick(graphicsY) && !backend.isInputBlock()) {
            performPullButtonClick();
        }

    }

    private void performPullButtonClick() {
        humanPullCard();
        pullButtonActor.setClick(true);
        performButtonAnimation();
        computerTurns();
    }

    private void humanPullCard() {
        backend.playerPullCard(0);
        CardActor cardActor = cardActorFactory.createCardActor(backend.takeCardFromGameDeck());
        prepareDragAndDrop(cardActor, dragTarget);
        cardActor.setUpSideDown(false);
        handGroups.get(0).addActor(cardActor);
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

    @Override
    public void resize(int width, int height) {
        viewport.setWorldSize(width, height);
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}


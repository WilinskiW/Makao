package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wwil.makao.backend.Card;
import com.wwil.makao.backend.MakaoBackend;

import java.util.*;
import java.util.List;

public class GameplayScreen implements Screen {
    private final Makao makao;
    private OrthographicCamera camera;
    private Stage stage;
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final MakaoBackend backend = new MakaoBackend();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private PullButtonActor pullButtonActor;
    private FitViewport viewport;
    private DragAndDrop.Target dragTarget;
    private StackCardsGroup stackCardsGroup;
    private int currentPlayerIndex = 0;
    // TODO: 20.12.2023 Komputer może kłaść karty - główna pętla gry ~ TERAZ ROZWIJANE
    // TODO: 20.12.2023 Aktywacja specjalnych zdolności kart
    // TODO: 20.12.2023 Restart użytych kart 

    //tworzy główny ekran gry pod względem grafiki
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
        final StackCardsGroup stackCardsGroup = new StackCardsGroup();
        prepareStack(stackCardsGroup, backend.getCard());

        preparePullButton();

        this.dragTarget = prepareTarget(stackCardsGroup);
        preparePlayers(dragTarget);
    }


    private void prepareStack(StackCardsGroup stackCardsGroup, Card card) {
        this.stackCardsGroup = stackCardsGroup;
        CardActor stackCard = cardActorFactory.createCardActor(card);
        addStackGroupToStage(stackCard);
        stackCard.setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }


    private void addStackGroupToStage(CardActor cardActor) {
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
        stage.addActor(stackCardsGroup);
    }

    private void preparePullButton() {
        PullButtonActor pullButton = new PullButtonActor();
        stage.addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        this.pullButtonActor = pullButton;
    }


    private void preparePlayers(DragAndDrop.Target target) {
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup());
        }
        createHumanStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(0).getCards()), target);
        createComputersStaringDeck();
        prepareHandGroups();
    }

    private void createComputersStaringDeck(){
        for(int i = 1; i < handGroups.size(); i++){
            List<Card> playerCards = backend.getPlayers().get(i).getCards();
            PlayerHandGroup currentHandGroup = handGroups.get(i);
            createPlayerStartingDeck(cardActorFactory.createCardActors(playerCards), currentHandGroup);
        }
    }

    private void createPlayerStartingDeck(List<CardActor> cards, PlayerHandGroup handGroup) {
        for (CardActor card : cards) {
            card.setUpSideDown(false);
            handGroup.addActor(card);
        }
    }

    private void createHumanStartingDeck(List<CardActor> cards, DragAndDrop.Target target) {
        PlayerHandGroup group = getHumanHand();
        createPlayerStartingDeck(cards, group);
        for (CardActor card : cards) {
            card.setUpSideDown(false);
            prepareDragAndDrop(card, target);
        }
    }

    private void prepareHandGroups() {
        addHandGroupsToStage();
        setRotationOfHandGroups();
        setPositionOfHandGroups();
    }

    private void addHandGroupsToStage() {
        for (PlayerHandGroup handGroup : handGroups) {
            stage.addActor(handGroup);
        }
    }

    private void setRotationOfHandGroups() {
        handGroups.get(1).setRotation(90);
        handGroups.get(2).setRotation(180);
        handGroups.get(3).setRotation(-90);
    }

    private void setPositionOfHandGroups() {
        handGroups.get(0).setPosition
                (GUIparams.WIDTH / 2f - (GUIparams.CARD_WIDTH / 2f),
                        0);

        handGroups.get(1).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 10,
                GUIparams.HEIGHT / 2.0f - (GUIparams.CARD_HEIGHT / 2f) + 45);

        handGroups.get(2).setPosition(GUIparams.WIDTH - GUIparams.WIDTH / 3.05f,
                GUIparams.HEIGHT + GUIparams.CARD_HEIGHT - 25);

        handGroups.get(3).setPosition(GUIparams.CARD_WIDTH / 5f - 30,
                GUIparams.HEIGHT - 100);
    }

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) { // stack - target
        //Target
        final DragAndDrop.Target target = new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) { // SOURCE - CARD
                CardActor chosenCard = (CardActor) source.getActor();
                CardActor stackCard = (CardActor) stackCardsGroup.getChildren().peek();
                executeDragAction(chosenCard,stackCard);
                return true;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                source.getActor().setColor(Color.WHITE);
                super.reset(source, payload);
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                CardActor chosenCard = (CardActor) source.getActor();
                CardActor stackCard = (CardActor) stackCardsGroup.getChildren().peek();
                executeDropAction(chosenCard,stackCard);
            }

        };
        return target;
    }
    private void executeDragAction(CardActor chosenCard, CardActor stackCard){
        if (isCardActorCorrect(chosenCard, stackCard)) {
            chosenCard.setColor(Color.LIME);
        } else {
            chosenCard.setColor(Color.SCARLET);
        }
    }

    private void executeDropAction(CardActor chosenCard, CardActor stackCard){
        if (isCardActorCorrect(chosenCard, stackCard)) {
            stackCardsGroup.addActor(chosenCard);
            turnOffHumanInput();
            executeComputersTurn();
            turnOnHumanInput();
        } else {
            chosenCard.backToPreviousLocation();
        }
    }

    private boolean isCardActorCorrect(CardActor chosenCard, CardActor stackCard) {
        return backend.isCorrectCard(stackCard.getCard(), chosenCard.getCard());
    }

    private void turnOffHumanInput() {
        backend.endHumanTurn();
        Gdx.input.setInputProcessor(null);
    }

    private void executeComputersTurn() {
        for (int i = 1; i < handGroups.size(); i++) {
            final int playerIndex = i;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    CardActor cardToPlay = preparePlayableCard(playerIndex);
                    if (cardToPlay != null) {
                        addCardActorToStack(cardToPlay);
                    } else {
                        computerPullCard(playerIndex);
                    }
                }
            }, i * 1f); // Opóźnienie względem indeksu
        }
    }

    private void addCardActorToStack(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    private void turnOnHumanInput() {
        Gdx.input.setInputProcessor(stage);
        backend.setInputBlock(false);
    }

    private CardActor preparePlayableCard(int index) {
        SnapshotArray<Actor> playerCards = handGroups.get(index).getChildren();
        for (int i = 0; i < playerCards.size; i++) {
            CardActor card = (CardActor) playerCards.get(i);
            CardActor stackCard = (CardActor) stackCardsGroup.getChildren().peek();
            if (isCardActorCorrect(card, stackCard)) {
                return card;
            }
        }
        return null;
    }

    private void computerPullCard(int playerIndex) {
        CardActor cardActor = cardActorFactory.createCardActor(backend.giveCard());
        cardActor.setUpSideDown(false);
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

        DragAndDrop.Source dropSource = new DragAndDrop.Source(card) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                payload.setObject(card);
                card.saveLocation();
                stage.addActor(card);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    card.backToPreviousLocation();
                }
                super.dragStop(event, x, y, pointer, payload, target);
            }
        };
        return dropSource;
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
    }

    private void humanPullCard() {
        CardActor cardActor = cardActorFactory.createCardActor(backend.giveCard());
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


package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wwil.makao.backend.Card;
import com.wwil.makao.backend.MakaoBackend;

import java.util.*;

public class GameplayScreen implements Screen {
    private Makao makao;
    private OrthographicCamera camera;
    private Stage stage;
    private List<PlayerHandGroup> handGroups = new ArrayList<>();
    private MakaoBackend backend = new MakaoBackend();
    private CardActorFactory cardActorFactory = new CardActorFactory();

    // TODO: 30.10.2023 Karty można położyć jedynie na zgodną karte
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
        stage = new Stage(new ScreenViewport(camera), makao.getBatch());
        Gdx.input.setInputProcessor(stage);
    }

    private void prepareGameComponents() {
        //BoardDeckGroup boardDeckGroup = new BoardDeckGroup();
        final StackCardsGroup stackCardsGroup = new StackCardsGroup();
        prepareStack(stackCardsGroup, backend.getCard());
        DragAndDrop.Target target = prepareTarget(stackCardsGroup);
        preparePlayers(target);
    }

    // TODO: 16.12.2023 Karta się klei do Stacka nawet jeśli jest nieprawidłowa

    private void prepareStack(StackCardsGroup stackCardsGroup, Card card) {
        CardActor stackCard = cardActorFactory.createCardActor(card);//todo refactor
        stackCard.setUpSideDown(false);
        stackCardsGroup.addActor(stackCard);
        stage.addActor(stackCardsGroup);
        stackCard.setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private void preparePlayers(DragAndDrop.Target target) {
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup());
        }

        createHumanStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(0).getCards()), target);
        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(1).getCards()),handGroups.get(1));
        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(2).getCards()),handGroups.get(2));
        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(3).getCards()), handGroups.get(3));


        prepareHandGroups();
    }

    private void createPlayerStartingDeck(List<CardActor> cards, PlayerHandGroup handGroup) {
        for (CardActor card : cards) {
            handGroup.addActor(card);
        }
    }

    private PlayerHandGroup createHumanStartingDeck(List<CardActor> cards, DragAndDrop.Target target) {
        PlayerHandGroup group = getHumanHand();
        createPlayerStartingDeck(cards, group);
        for (CardActor card : cards) {
            card.setUpSideDown(false);
            prepareDragAndDrop(card, target);
        }
        return group;
    }

    private void prepareHandGroups() {
        for (PlayerHandGroup handGroup : handGroups) {
            stage.addActor(handGroup);
        }


        handGroups.get(2).setRotation(90);
        handGroups.get(3).setRotation(90);

        //Set handGroup position
        handGroups.get(0).setPosition(GUIparams.WIDTH / 2f - (GUIparams.CARD_WIDTH / 2f), 0);
        handGroups.get(1).setPosition(GUIparams.WIDTH / 2.0f - (GUIparams.CARD_WIDTH / 2f), GUIparams.HEIGHT - 30);
        handGroups.get(2).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 15, GUIparams.HEIGHT / 2.0f - (GUIparams.CARD_HEIGHT / 2f) + 45);
        handGroups.get(3).setPosition(GUIparams.CARD_HEIGHT, GUIparams.HEIGHT / 2f - GUIparams.CARD_WIDTH / 2f);

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

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) { // stack - target
        //Target
        final DragAndDrop.Target target = new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) { // SOURCE - CARD
                source.getActor().setColor(Color.RED);
                return true;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                source.getActor().setColor(Color.WHITE);
                super.reset(source, payload);
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                boolean cardCorrect = false;
                //Przy true działa normalnie, ale pozycja Z przy powrocie do talii jest źle przepisana
                CardActor card = (CardActor) source.getActor();

                if (cardCorrect) {
                    stackCardsGroup.addActor(source.getActor());
                } else {
                    card.backToPreviousLocation();
                }
            }
        };
        return target;
    }

    private PlayerHandGroup getHumanHand() {
        return handGroups.get(0);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        // makao.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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


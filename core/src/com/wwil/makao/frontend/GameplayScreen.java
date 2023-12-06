package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wwil.makao.backend.Card;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.PlayerHand;

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

        DragAndDrop.Target target = prepareDragAndDrop(stackCardsGroup);

        preparePlayers(target);
    }

    private DragAndDrop.Target prepareDragAndDrop(StackCardsGroup stackCardsGroup){
        final DragAndDrop.Target target = new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                //  stackCardsGroup.addActor(source.getActor());
                CardActor card = (CardActor) source.getActor();
                //sourceGroup.addActor(card);
                Action move = Actions.moveTo(card.getX(), card.getY(), 0);
                card.addAction(move);
                card.loadPosZ();
            }
        };
        return target;
    }

    private void prepareStack(StackCardsGroup stackCardsGroup, Card card) {
        CardActor stackCard = cardActorFactory.createCardActor(card);//todo refactor
        stackCard.setUpSideDown(false);
        stackCardsGroup.addActor(stackCard);
        stage.addActor(stackCardsGroup);
        stackCard.setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private void preparePlayers(DragAndDrop.Target target){
        PlayerHandGroup playerHandGroupSouth = new PlayerHandGroup();

        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(0).getCards()),
                playerHandGroupSouth, true, target);
        handGroups.add(playerHandGroupSouth);

        PlayerHandGroup playerHandGroupNorth = new PlayerHandGroup();
        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(1).getCards()),
                playerHandGroupNorth, true, null);
        handGroups.add(playerHandGroupNorth);

        PlayerHandGroup playerHandGroupEast = new PlayerHandGroup();
        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(2).getCards()),
                playerHandGroupEast, true, null);
        handGroups.add(playerHandGroupEast);

        PlayerHandGroup playerHandGroupWest = new PlayerHandGroup();
        createPlayerStartingDeck(cardActorFactory.createCardActors(backend.getPlayers().get(3).getCards()),
                playerHandGroupWest, true, null);
        handGroups.add(playerHandGroupWest);

        prepareHandGroups();
    }

    private void createPlayerStartingDeck(List<CardActor> cards, PlayerHandGroup playerHandGroup, boolean isHumanPlayer, DragAndDrop.Target target) {
        List<CardActor> playerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (isHumanPlayer) {
                cards.get(0).setUpSideDown(false);
                playerCards.add(cards.get(0));
            }
            playerHandGroup.addActor(cards.remove(0));
        }

        if (isHumanPlayer) {
            for (CardActor card : playerCards) {
                prepareDragAndDrop(card, target, playerHandGroup);
            }
        }
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


    private void prepareDragAndDrop(final CardActor card, DragAndDrop.Target target, final Group sourceGroup) {
        final Vector2 cardPos = new Vector2(card.getX(), card.getY());

        final DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(GUIparams.CARD_WIDTH / 2f, -GUIparams.CARD_HEIGHT / 2f);
        DragAndDrop.Source dropSource = new DragAndDrop.Source(card) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                payload.setObject(card);
                card.savePosZ();
                stage.addActor(card);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    moveCardToPreviousLocation();
                }
                super.dragStop(event, x, y, pointer, payload, target);
            }

            private void moveCardToPreviousLocation() {
                sourceGroup.addActor(card);
                Action move = Actions.moveTo(cardPos.x, cardPos.y, 0);
                card.addAction(move);
                card.loadPosZ();
            }
        };

        dragAndDrop.addSource(dropSource);
        dragAndDrop.addTarget(target);
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

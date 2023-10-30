package com.wwil.makao;

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

import java.util.*;

public class GameplayScreen implements Screen {
    private Makao makao;
    private final OrthographicCamera camera;
    private Stage stage;
    private List<PlayerHandGroup> handGroups = new ArrayList<>();

    public GameplayScreen(Makao makao) {
        this.makao = makao;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);
        stage = new Stage(new ScreenViewport(camera), makao.getBatch());

        //Display players cards
        Gdx.input.setInputProcessor(stage);


        //Display board deck
        BoardDeckGroup boardDeckGroup = new BoardDeckGroup();
        prepareBoardDeck(boardDeckGroup,prepareCardsForBoardDeck());

        //Display stack deck
        final StackCardsGroup stackCardsGroup = new StackCardsGroup();
        prepareStack(stackCardsGroup,takeCardsFromBoardDeck(boardDeckGroup,1).get(0));

        //Drag
        final DragAndDrop.Target target = new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                stackCardsGroup.addActor(source.getActor());
            }
        };

        //Display Players hand group
        PlayerHandGroup playerHandGroupSouth = new PlayerHandGroup();
        createPlayerStartingDeck(takeCardsFromBoardDeck(boardDeckGroup,5),playerHandGroupSouth,true,target);
        handGroups.add(playerHandGroupSouth);

        PlayerHandGroup playerHandGroupNorth = new PlayerHandGroup();
        createPlayerStartingDeck(takeCardsFromBoardDeck(boardDeckGroup,5),playerHandGroupNorth,false,null);
        handGroups.add(playerHandGroupNorth);

        PlayerHandGroup playerHandGroupEast = new PlayerHandGroup();
        createPlayerStartingDeck(takeCardsFromBoardDeck(boardDeckGroup,5),playerHandGroupEast,false,null);
        handGroups.add(playerHandGroupEast);

        PlayerHandGroup playerHandGroupWest = new PlayerHandGroup();
        createPlayerStartingDeck(takeCardsFromBoardDeck(boardDeckGroup,5),playerHandGroupWest,false,null);
        handGroups.add(playerHandGroupWest);

        prepareHandGroups();
    }

    private List<CardActor> prepareCardsForBoardDeck(){
        CardActorFactory cardActorFactory = new CardActorFactory();
        List<CardActor> cards = cardActorFactory.createCardActors();
        Collections.shuffle(cards);
        return cards;
    }

    private void prepareBoardDeck(BoardDeckGroup boardDeckGroup,List<CardActor> cards){
        stage.addActor(boardDeckGroup);

        boardDeckGroup.setPosition(GUIparams.WIDTH / 2f - 350, GUIparams.HEIGHT / 2f);
        for(CardActor card : cards){
            boardDeckGroup.addActor(card);
        }
    }

    private List<CardActor> takeCardsFromBoardDeck(BoardDeckGroup boardDeck, int amount){
        List<CardActor> cards = new ArrayList<>();
        for(int i = 0; i < amount; i++){
           CardActor cardActor = (CardActor) boardDeck.getChild(i);
           cards.add(cardActor);
           cardActor.remove();
        }
        return cards;
    }

    private void prepareStack(StackCardsGroup stackCardsGroup, CardActor card){
        CardActor stackCard = new CardActor(card.getFrontSide(),card.getRank(),card.getSuit());
        stackCard.setUpSideDown(false);
        stackCardsGroup.addActor(stackCard);
        stage.addActor(stackCardsGroup);
        stackCard.setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private void createPlayerStartingDeck(List<CardActor> cards, PlayerHandGroup playerHandGroup, boolean isHumanPlayer, DragAndDrop.Target target){
        List<CardActor> playerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if(isHumanPlayer) {
                cards.get(0).setUpSideDown(false);
                playerCards.add(cards.get(0));
            }
            playerHandGroup.addActor(cards.remove(0));
        }

        if(isHumanPlayer) {
            for (CardActor card : playerCards) {
                prepareDragAndDrop(card, target, playerHandGroup);
            }
        }
    }

    private void prepareHandGroups(){
        for(PlayerHandGroup handGroup : handGroups){
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
        final int cardZ = card.getZIndex();

        final DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(GUIparams.CARD_WIDTH / 2f, -GUIparams.CARD_HEIGHT / 2f);
        DragAndDrop.Source dropSource = new DragAndDrop.Source(card) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                payload.setObject(card);
                stage.addActor(card);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    sourceGroup.addActor(card);
                    Action move = Actions.moveTo(cardPos.x, cardPos.y, 0);
                    card.addAction(move);
                    card.setZIndex(cardZ);
                }
                super.dragStop(event, x, y, pointer, payload, target);
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

package com.wwil.makao;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.smartcardio.Card;
import java.util.*;

public class GameplayScreen implements Screen {
    private Makao makao;
    private final OrthographicCamera camera;
    private Stage stage;
    private String backCardPath = "Cards/backCard.png";
    private List<PlayerHandGroup> handGroups = new ArrayList<>();

    public GameplayScreen(Makao makao) {
        this.makao = makao;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);
        stage = new Stage(new ScreenViewport(camera), makao.getBatch());

        //Display players cards
        Gdx.input.setInputProcessor(stage);

        CardActorFactory cardActorFactory = new CardActorFactory();
        List<CardActor> cards = cardActorFactory.createCardActors();
        Collections.shuffle(cards);

        //Display stack deck
        final StackCardsGroup stackCardsGroup = new StackCardsGroup();
        prepareStack(stackCardsGroup,cards);

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
        //Display Player hand group

        PlayerHandGroup playerHandGroupSouth = new PlayerHandGroup();
        createPlayerStartingDeck(cards,playerHandGroupSouth,true,target);
        handGroups.add(playerHandGroupSouth);

        PlayerHandGroup playerHandGroupNorth = new PlayerHandGroup();
        createPlayerStartingDeck(cards,playerHandGroupNorth,false,null);
        handGroups.add(playerHandGroupNorth);

        PlayerHandGroup playerHandGroupEast = new PlayerHandGroup();
        createPlayerStartingDeck(cards,playerHandGroupEast,false,null);
        handGroups.add(playerHandGroupEast);

        PlayerHandGroup playerHandGroupWest = new PlayerHandGroup();
        createPlayerStartingDeck(cards,playerHandGroupWest,false,null);
        handGroups.add(playerHandGroupWest);

        prepareHandGroups();

        //Display board deck
        BoardDeckGroup boardDeckGroup = new BoardDeckGroup();
        prepareBoardDeck(boardDeckGroup,cards);
    }

    private void prepareStack(StackCardsGroup stackCardsGroup, List<CardActor> cards){
        CardActor stackCard = new CardActor(cards.get(0).getFrontSide());
        stackCard.setUpSideDown(false);
        cards.remove(0);
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
        handGroups.get(1).setPosition(GUIparams.WIDTH / 2.0f - (GUIparams.CARD_WIDTH / 2f), GUIparams.HEIGHT - 15);
        handGroups.get(2).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 15, GUIparams.HEIGHT / 2.0f - (GUIparams.CARD_HEIGHT / 2f) + 45);
        handGroups.get(3).setPosition(GUIparams.CARD_HEIGHT, GUIparams.HEIGHT / 2f - GUIparams.CARD_WIDTH / 2f);

    }

    private void prepareBoardDeck(BoardDeckGroup boardDeckGroup,List<CardActor> cards){
        stage.addActor(boardDeckGroup);

        boardDeckGroup.setPosition(GUIparams.WIDTH / 2f - 350, GUIparams.HEIGHT / 2f);

        for (CardActor card : cards) {
            boardDeckGroup.addActor(card);
        }
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
                    System.out.println(cardZ);
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

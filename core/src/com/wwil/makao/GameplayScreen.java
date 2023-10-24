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
    private String blankCardPath = "Cards/BlankCard.png";
    private List<CardActor> playerHand = new ArrayList<>();

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

        //Display board deck
        BoardDeckGroup boardDeckGroup = new BoardDeckGroup();
        CardActor blankCard6 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor blankCard7 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor blankCard8 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        stage.addActor(boardDeckGroup);

        boardDeckGroup.setPosition(GUIparams.WIDTH / 2f - 350, GUIparams.HEIGHT / 2f);
        boardDeckGroup.addActor(blankCard6);
        boardDeckGroup.addActor(blankCard7);
        boardDeckGroup.addActor(blankCard8);


        //Display stack deck
        final StackCardsGroup stackCardsGroup = new StackCardsGroup();
        final CardActor stackCard = new CardActor(cards.get(0).getFrontSide());
        stackCard.setUpSideDown(false);
        cards.remove(0);
        stackCardsGroup.addActor(stackCard);
        stage.addActor(stackCardsGroup);
        stackCard.setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);

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


        PlayerHandGroup playerHandGroupSouth = new PlayerHandGroup();
        List<CardActor> playerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cards.get(0).setUpSideDown(false);
            playerCards.add(cards.get(0));
            playerHandGroupSouth.addActor(cards.remove(0));
        }

        for (CardActor card : playerCards) {
            prepareDragAndDrop(card,target,playerHandGroupSouth);
        }


        final PlayerHandGroup playerHandGroupNorth = new PlayerHandGroup();
        for (int i = 0; i < 5; i++) {
            playerHandGroupNorth.addActor(cards.remove(0));
        }

        final PlayerHandGroup playerHandGroupEast = new PlayerHandGroup();
        for (int i = 0; i < 5; i++) {
            playerHandGroupEast.addActor(cards.remove(0));
        }

        final PlayerHandGroup playerHandGroupWest = new PlayerHandGroup();
        for (int i = 0; i < 5; i++) {
            playerHandGroupWest.addActor(cards.remove(0));
        }

        stage.addActor(playerHandGroupSouth);
        stage.addActor(playerHandGroupNorth);
        stage.addActor(playerHandGroupEast);
        stage.addActor(playerHandGroupWest);


        playerHandGroupEast.setRotation(90);
        playerHandGroupWest.setRotation(90);

        //Set handGroup position
        playerHandGroupSouth.setPosition(GUIparams.WIDTH / 2f - (GUIparams.CARD_WIDTH / 2f), 0);
        playerHandGroupNorth.setPosition(GUIparams.WIDTH / 2.0f - (GUIparams.CARD_WIDTH / 2f), GUIparams.HEIGHT - 15);
        playerHandGroupEast.setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 15, GUIparams.HEIGHT / 2.0f - (GUIparams.CARD_HEIGHT / 2f) + 45);
        playerHandGroupWest.setPosition(GUIparams.CARD_HEIGHT, GUIparams.HEIGHT / 2f - GUIparams.CARD_WIDTH / 2f);


//
//        prepareDragAndDrop(blankCard2,target,playerHandGroupSouth);
//        prepareDragAndDrop(blankCard3,target,playerHandGroupSouth);
//        prepareDragAndDrop(blankCard4,target,playerHandGroupSouth);
//        prepareDragAndDrop(blankCard5,target,playerHandGroupSouth);

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

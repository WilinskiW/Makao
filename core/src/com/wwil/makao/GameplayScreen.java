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

public class GameplayScreen implements Screen {
    private Makao makao;
    private final OrthographicCamera camera;
    private Stage stage;
    private String backCardPath = "Cards/backCard.png";
    private String blankCardPath = "Cards/BlankCard.png";
    public GameplayScreen(Makao makao) {
        this.makao = makao;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);
        stage = new Stage(new ScreenViewport(camera), makao.getBatch());

        //Display players cards
        Gdx.input.setInputProcessor(stage);
        final CardActor blankCard = new CardActor(new Texture(Gdx.files.internal(blankCardPath)));
        final CardActor blankCard2 = new CardActor(new Texture(Gdx.files.internal(blankCardPath)));
        final CardActor blankCard3 = new CardActor(new Texture(Gdx.files.internal(blankCardPath)));
        CardActor blankCard4 = new CardActor(new Texture(Gdx.files.internal(blankCardPath)));
        CardActor blankCard5 = new CardActor(new Texture(Gdx.files.internal(blankCardPath)));

        final PlayerHandGroup playerHandGroupSouth = new PlayerHandGroup();
        PlayerHandGroup playerHandGroupNorth = new PlayerHandGroup();
        PlayerHandGroup playerHandGroupEast = new PlayerHandGroup();
        PlayerHandGroup playerHandGroupWest = new PlayerHandGroup();

        final CardActor back = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back2 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back3 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back4 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back5 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));

        CardActor back6 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back7 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back8 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back9 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back10 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));

        CardActor back11 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back12 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back13 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back14 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor back15 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));



        stage.addActor(playerHandGroupSouth);
        stage.addActor(playerHandGroupNorth);
        stage.addActor(playerHandGroupEast);
        stage.addActor(playerHandGroupWest);

        playerHandGroupEast.setRotation(90);
        playerHandGroupWest.setRotation(90);

        playerHandGroupSouth.setPosition(GUIparams.WIDTH/2f-(GUIparams.CARD_WIDTH/2f),0);
        playerHandGroupSouth.addActor(blankCard2);
        playerHandGroupSouth.addActor(blankCard3);
        playerHandGroupSouth.addActor(blankCard4);
        playerHandGroupSouth.addActor(blankCard5);
        playerHandGroupSouth.addActor(blankCard);

        playerHandGroupNorth.setPosition(GUIparams.WIDTH/2.0f-(GUIparams.CARD_WIDTH/2f), GUIparams.HEIGHT-15);
        playerHandGroupNorth.addActor(back);
        playerHandGroupNorth.addActor(back2);
        playerHandGroupNorth.addActor(back3);
        playerHandGroupNorth.addActor(back4);
        playerHandGroupNorth.addActor(back5);

        playerHandGroupEast.setPosition(GUIparams.WIDTH+GUIparams.CARD_HEIGHT-15, GUIparams.HEIGHT/2.0f-(GUIparams.CARD_HEIGHT/2f)+45);
        playerHandGroupEast.addActor(back6);
        playerHandGroupEast.addActor(back7);
        playerHandGroupEast.addActor(back8);
        playerHandGroupEast.addActor(back9);
        playerHandGroupEast.addActor(back10);

        playerHandGroupWest.setPosition(GUIparams.CARD_HEIGHT,GUIparams.HEIGHT/2f-GUIparams.CARD_WIDTH/2f);
        playerHandGroupWest.addActor(back11);
        playerHandGroupWest.addActor(back12);
        playerHandGroupWest.addActor(back13);
        playerHandGroupWest.addActor(back14);
        playerHandGroupWest.addActor(back15);

        //Display board deck
        BoardDeckGroup boardDeckGroup = new BoardDeckGroup();
        CardActor blankCard6 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor blankCard7 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        CardActor blankCard8 = new CardActor(new Texture(Gdx.files.internal(backCardPath)));
        stage.addActor(boardDeckGroup);

        boardDeckGroup.setPosition(GUIparams.WIDTH/2f-350,GUIparams.HEIGHT/2f);
        boardDeckGroup.addActor(blankCard6);
        boardDeckGroup.addActor(blankCard7);
        boardDeckGroup.addActor(blankCard8);

        System.out.println(playerHandGroupWest.getX());
        System.out.println(playerHandGroupWest.getY());

        //Display stack deck
        final StackCardsGroup stackCardsGroup = new StackCardsGroup();
        final CardActor stackCard = new CardActor(new Texture(Gdx.files.internal(blankCardPath)));
        stackCardsGroup.addActor(stackCard);
        stage.addActor(stackCardsGroup);
        stackCard.setPosition(GUIparams.WIDTH/2f,GUIparams.HEIGHT/2f);

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

        prepareDragAndDrop(blankCard,target,playerHandGroupSouth);
        prepareDragAndDrop(blankCard2,target,playerHandGroupSouth);
        prepareDragAndDrop(blankCard3,target,playerHandGroupSouth);
        prepareDragAndDrop(blankCard4,target,playerHandGroupSouth);
        prepareDragAndDrop(blankCard5,target,playerHandGroupSouth);

    }

    private void prepareDragAndDrop(final CardActor card, DragAndDrop.Target target, final Group sourceGroup){
        final Vector2 cardPos = new Vector2(card.getX(),card.getY());
        final int cardZ = card.getZIndex();


        final DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(GUIparams.CARD_WIDTH/2f , -GUIparams.CARD_HEIGHT/2f);
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
                if(target == null){
                    sourceGroup.addActor(card);
                    Action move = Actions.moveTo(cardPos.x,cardPos.y, 0);
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
        stage.getViewport().update(width,height,true);
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

package com.wwil.makao.frontend.cardChooserWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.backend.Rank;
import com.wwil.makao.backend.Suit;
import com.wwil.makao.frontend.CardActor;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
//todo Refactor
public class CardChooserWindow extends Actor {
    private final TextureRegion window;
    private final TextureAtlas cardAtlas;
    private String currentRankName;
    private String currentSuitName;
    private final CardActor displayCard;
    private final GameController gameController;
    private final List<ArrowButtonActor> arrowButtons;
    private final PutButtonActor putButton;
    private final SortedMap<Integer, String> rankMap;
    private final SortedMap<Integer, String> suitMap;
    private int currentRankIndex = 1;
    private int currentSuitIndex = 1;

    public CardChooserWindow(GameController gameController) {
        this.window = new TextureRegion
                (new Texture(Gdx.files.internal("assets/windows/CardChooserWindow.png")));
        this.cardAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
        this.gameController = gameController;
        this.arrowButtons = createArrows();
        this.putButton = createPutButton();
        this.displayCard = createCardActor();
        displayCard.setPosition(855, 392); //todo
        this.rankMap = createRankMap();
        this.suitMap = createSuitMap();
        setBounds(GUIparams.CHOOSER_WINDOW_X_POS, GUIparams.CHOOSER_WINDOW_Y_POS,
                GUIparams.CHOOSER_WINDOW_WIDTH, GUIparams.CHOOSER_WINDOW_HEIGHT);
    }

    private List<ArrowButtonActor> createArrows() {
        List<ArrowButtonActor> buttons = new ArrayList<>();
        for (CardChooserButtonParams type : CardChooserButtonParams.values()) {
            if (type != CardChooserButtonParams.PUT) {
                buttons.add(new ArrowButtonActor(this, type));
            }
        }
        return buttons;
    }

    private PutButtonActor createPutButton() {
        return new PutButtonActor(this);
    }

    private CardActor createCardActor() {
        CardActor stackCard = gameController.getStackCardsGroup().peekCardActor();
        setCurrentRankName(stackCard.getCard().getRank().getName());
        setCurrentSuitName(stackCard.getCard().getSuit().getName());
        return new CardActor(stackCard.getFrontSide());
    }

    private TreeMap<Integer, String> createRankMap() {
        TreeMap<Integer, String> rankMap = new TreeMap<>();
        int i = 1;
        for (Rank rank : Rank.values()) {
            rankMap.put(i++, rank.getName());
        }
        return rankMap;
    }

    private TreeMap<Integer, String> createSuitMap() {
        TreeMap<Integer, String> suitMap = new TreeMap<>();
        int i = 1;
        for (Suit suit : Suit.values()) {
            suitMap.put(i++, suit.getName());
        }
        return suitMap;
    }

    public void show(boolean isToShow) {
        setVisible(isToShow);
        getDisplayCard().setVisible(isToShow);
        for (ArrowButtonActor arrowButtonActor : getArrowButtons()) {
            arrowButtonActor.setVisible(isToShow);
        }
        getPutButton().setVisible(isToShow);
    }

    public void changeRank(int indexChanger) {
        //Dla J
        SortedMap<Integer,String> map;
        if(gameController.getStackCardsGroup().peekCardActor().getCard().getRank().equals(Rank.J)){
            map = getMapForJ();
            System.out.println(map);
        }
        else {
            map = rankMap;
        }

        currentRankIndex += indexChanger;
        if(currentRankIndex > map.size()){
            currentRankIndex = map.firstKey();
        }
        else if(currentRankIndex < map.firstKey()){
            currentRankIndex = map.lastKey();
        }
        String rank = map.get(currentRankIndex);
        setCurrentRankName(rank);
        displayCard.changeFrontSide(cardAtlas.findRegion(currentSuitName +rank));
    }

    private SortedMap<Integer,String> getMapForJ(){
        SortedMap<Integer,String> mapForJ = new TreeMap<>();
        for(int i = 1; i <= 6; i++){
            mapForJ.put(i,rankMap.get(4+i));
        }
        return mapForJ;
    }

    public void changeSuit(int indexChanger) {
        //Dla AS
        currentSuitIndex += indexChanger;
        if(currentSuitIndex > suitMap.size()){
            currentSuitIndex = suitMap.firstKey();
        }
        else if(currentSuitIndex < suitMap.firstKey()){
            currentSuitIndex = suitMap.lastKey();
        }
        String suit = suitMap.get(currentSuitIndex);
        setCurrentSuitName(suit);
        displayCard.changeFrontSide(cardAtlas.findRegion(suit+ currentRankName));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(window, getX(), getY());
        batch.setColor(Color.WHITE);
    }

    public CardActor getDisplayCard() {
        return displayCard;
    }

    public List<ArrowButtonActor> getArrowButtons() {
        return arrowButtons;
    }

    public PutButtonActor getPutButton() {
        return putButton;
    }

    public void setCurrentRankName(String currentRankName) {
        this.currentRankName = currentRankName;
    }

    public void setCurrentSuitName(String currentSuitName) {
        this.currentSuitName = currentSuitName;
    }
}

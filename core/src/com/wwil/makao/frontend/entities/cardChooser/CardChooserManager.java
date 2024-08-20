package com.wwil.makao.frontend.entities.cardChooser;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.card.Suit;
import com.wwil.makao.frontend.entities.CardActor;

import java.util.SortedMap;
import java.util.TreeMap;

public class CardChooserManager {
    private final CardChooserGroup cardChooser;
    private final TextureAtlas cardAtlas;
    private String currentRankName;
    private String currentSuitName;
    private final SortedMap<Integer, String> rankMap;
    private final SortedMap<Integer, String> mapForJ;
    private final SortedMap<Integer, String> suitMap;
    private int currentRankIndex = 1;
    private int currentSuitIndex = 1;

    public CardChooserManager(CardChooserGroup cardChooserGroup) {
        this.cardChooser = cardChooserGroup;
        this.cardAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
        this.rankMap = createRankMap();
        this.mapForJ = getMapForJ();
        this.suitMap = createSuitMap();
    }

    private TreeMap<Integer, String> createRankMap() {
        TreeMap<Integer, String> rankMap = new TreeMap<>();
        int i = 1;
        for (Rank rank : Rank.values()) {
            if(rank.equals(Rank.JOKER)){
                continue;
            }
            rankMap.put(i++, rank.getName());
        }
        return rankMap;
    }

    private SortedMap<Integer,String> getMapForJ(){
        SortedMap<Integer,String> mapForJ = new TreeMap<>();
        for(int i = 1; i <= 6; i++){
            mapForJ.put(i,rankMap.get(4+i));
        }
        return mapForJ;
    }

    private TreeMap<Integer, String> createSuitMap() {
        TreeMap<Integer, String> suitMap = new TreeMap<>();
        int i = 1;
        for (Suit suit : Suit.getNormalSuits()) {
            suitMap.put(i++, suit.getName());
        }
        return suitMap;
    }

    void changeRank(int indexChanger) {
        SortedMap<Integer,String> map;
        if(cardChooser.getGameController().getUiManager().peekStackCardActor().getCard().getRank().equals(Rank.J)){
            map = mapForJ;
        }
        else  {
            map = rankMap;
        }
        changeRankIndex(indexChanger,map);
        String rank = map.get(currentRankIndex);
        currentRankName = rank;
        cardChooser.getDisplayCard().changeFrontSide(cardAtlas.findRegion(currentSuitName +rank));
    }

    private void changeRankIndex(int indexChanger, SortedMap<Integer,String> map){
        currentRankIndex += indexChanger;
        if(currentRankIndex > map.size()){
            currentRankIndex = map.firstKey();
        }
        else if(currentRankIndex < map.firstKey()){
            currentRankIndex = map.lastKey();
        }
    }

    public void changeSuit(int indexChanger) {
        changeSuitIndex(indexChanger,suitMap);
        String suit = suitMap.get(currentSuitIndex);
        currentSuitName = suit;
        cardChooser.getDisplayCard().changeFrontSide(cardAtlas.findRegion(suit+currentRankName));
    }

    private void changeSuitIndex(int indexChanger, SortedMap<Integer,String> map){
        currentSuitIndex += indexChanger;
        if(currentSuitIndex > map.size()){
            currentSuitIndex = map.firstKey();
        }
        else if(currentSuitIndex < map.firstKey()){
            currentSuitIndex = map.lastKey();
        }
    }

    CardActor giveCardActor(){
        return new CardActor(cardChooser.getDisplayCard().getFrontSide(),
                new Card(Rank.getRank(currentRankName),Suit.getSuit(currentSuitName)));
    }

    public void setDisplayCard(CardActor stackCard, CardActor cardPlayed) {
        Rank cardPlayedRank = cardPlayed.getCard().getRank();
        currentSuitName = cardPlayed.getCard().getSuit().getName();
        if(cardPlayedRank.equals(Rank.AS)) {
            currentRankName = cardPlayedRank.getName();
            hideArrows("RANK");
        }
        else if(cardPlayedRank.equals(Rank.J)){
            currentRankName = "5";
            hideArrows("SUIT");
        }
        else if(stackCard.getCard().getRank().equals(Rank.JOKER)){
            currentRankName = cardPlayed.getCard().getRank().getName();
            currentSuitName = cardPlayed.getCard().getSuit().getName();
        }
        else {
            currentRankName = "AS";
            currentSuitName = stackCard.getCard().getSuit().getName();
        }

        cardChooser.getDisplayCard().setFrontSide
                (new TextureRegion(cardAtlas.findRegion(currentSuitName+currentRankName)));
    }


    void resetIndexes(){
        currentRankIndex = 1;
        currentSuitIndex = 1;
    }

    private void hideArrows(String typeString){
        for(ArrowButtonActor arrow : cardChooser.getArrowButtons()){
            if(arrow.getType().name().contains(typeString)){
                arrow.setVisible(false);
            }
        }
    }

}

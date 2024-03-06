package com.wwil.makao.frontend.cardChooserWindow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.wwil.makao.backend.Rank;
import com.wwil.makao.backend.Suit;

import java.util.SortedMap;
import java.util.TreeMap;

public class CardChooserManager {
    private final CardChooserWindow window;
    private final TextureAtlas cardAtlas;
    private String currentRankName;
    private String currentSuitName;
    private final SortedMap<Integer, String> rankMap;
    private final SortedMap<Integer, String> mapForJ;
    private final SortedMap<Integer, String> suitMap;
    private int currentRankIndex = 1;
    private int currentSuitIndex = 1;

    public CardChooserManager(CardChooserWindow window) {
        this.window = window;
        this.cardAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
        this.rankMap = createRankMap();
        this.mapForJ = getMapForJ();
        this.suitMap = createSuitMap();
    }

    private TreeMap<Integer, String> createRankMap() {
        TreeMap<Integer, String> rankMap = new TreeMap<>();
        int i = 1;
        for (Rank rank : Rank.values()) {
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
        for (Suit suit : Suit.values()) {
            suitMap.put(i++, suit.getName());
        }
        return suitMap;
    }

    public void changeRank(int indexChanger) {
        //Dla J i Jokera
        SortedMap<Integer,String> map;
        if(window.getGameController().peekStackCardActor().getCard().getRank().equals(Rank.J)){
            map = mapForJ;
        }
        else {
            map = rankMap;
        }
        changeRankIndex(indexChanger,map);
        String rank = map.get(currentRankIndex);
        setCurrentRankName(rank);
        window.getDisplayCard().changeFrontSide(cardAtlas.findRegion(currentSuitName +rank));
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
        //Dla AS
        changeSuitIndex(indexChanger,suitMap);
        String suit = suitMap.get(currentSuitIndex);
        setCurrentSuitName(suit);
        window.getDisplayCard().changeFrontSide(cardAtlas.findRegion(suit+currentRankName));
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

    public void setCurrentRankName(String currentRankName) {
        this.currentRankName = currentRankName;
    }

    public void setCurrentSuitName(String currentSuitName) {
        this.currentSuitName = currentSuitName;
    }
}

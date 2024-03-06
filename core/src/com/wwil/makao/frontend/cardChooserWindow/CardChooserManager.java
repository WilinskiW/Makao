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
    private int currentIndex = 1;

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
        changeIndex(indexChanger,map);
        String rank = map.get(currentIndex);
        setCurrentRankName(rank);
        window.getDisplayCard().changeFrontSide(cardAtlas.findRegion(currentSuitName +rank));
    }

    private void changeIndex(int indexChanger, SortedMap<Integer,String> map){
        currentIndex += indexChanger;
        if(currentIndex > map.size()){
            currentIndex = map.firstKey();
        }
        else if(currentIndex < map.firstKey()){
            currentIndex = map.lastKey();
        }
    }

    public void changeSuit(int indexChanger) {
        //Dla AS
        changeIndex(indexChanger,suitMap);
        String suit = suitMap.get(currentIndex);
        setCurrentSuitName(suit);
        window.getDisplayCard().changeFrontSide(cardAtlas.findRegion(suit+currentRankName));
    }

    public void setCurrentRankName(String currentRankName) {
        this.currentRankName = currentRankName;
    }

    public void setCurrentSuitName(String currentSuitName) {
        this.currentSuitName = currentSuitName;
    }
}

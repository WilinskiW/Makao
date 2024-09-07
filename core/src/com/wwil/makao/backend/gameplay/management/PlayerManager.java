package com.wwil.makao.backend.gameplay.management;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.utils.PlayerComebackHandler;
import com.wwil.makao.backend.model.player.Computer;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final PlayerComebackHandler playerComebackHandler;
    private final List<Player> players = new ArrayList<>();
    private Human humanPlayer;
    private int currentPlayerIndex = 0;


    public PlayerManager(int amountOfPlayers, int startingCards, DeckManager deckManager) {
        this.playerComebackHandler = new PlayerComebackHandler(this);
        createHumanPlayer(startingCards,deckManager);
        createComputerPlayers(amountOfPlayers, startingCards, deckManager);
    }

    private void createHumanPlayer(int startingCards, DeckManager deckManager){
        humanPlayer = new Human(deckManager.giveCards(startingCards));
        players.add(humanPlayer);
    }

    private void createComputerPlayers(int amountOfPlayers, int startingCards, DeckManager deckManager) {
        for (int i = 0; i < amountOfPlayers - 1; i++) {
            Player player = new Computer(deckManager.giveCards(startingCards));
            players.add(player);
        }
    }

    boolean shouldProceedToNextPlayer(Player player) {
        return player == getCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    void goToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex > players.size() - 1) {
            currentPlayerIndex = 0;
        }
    }

    void goToPreviousPlayer() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size() - 1;
        }
    }

    public void goToHumanPlayer(){
        currentPlayerIndex = 0;
    }

    Player getPreviousPlayer() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    Player getNextPlayer() {
        int nextPlayerIndex = currentPlayerIndex + 1;
        if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return players.get(nextPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Human getHumanPlayer(){
        return humanPlayer;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public PlayerComebackHandler getPlayerComebackHandler() {
        return playerComebackHandler;
    }
}

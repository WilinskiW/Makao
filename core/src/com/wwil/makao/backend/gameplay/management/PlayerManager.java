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
    private int currentPlayerId = 0;


    public PlayerManager(int amountOfPlayers, int startingCards, DeckManager deckManager) {
        this.playerComebackHandler = new PlayerComebackHandler(this);
        createPlayers(amountOfPlayers, startingCards, deckManager);
    }

    private void createPlayers(int amountOfPlayers, int startingCards, DeckManager deckManager) {
        createHumanPlayer(startingCards, deckManager);
        createComputerPlayers(amountOfPlayers, startingCards, deckManager);
    }

    private void createHumanPlayer(int startingCards, DeckManager deckManager) {
        humanPlayer = new Human(0, deckManager.giveCards(startingCards));
        players.add(humanPlayer);
    }

    private void createComputerPlayers(int amountOfPlayers, int startingCards, DeckManager deckManager) {
        int id = 1;
        for (int i = 0; i < amountOfPlayers - 1; i++) {
            Player player = new Computer(id, deckManager.giveCards(startingCards));
            players.add(player);
            id++;
        }
    }

    public void proceedToNextPlayerIfNeeded(Player currentPlayer) {
        if (playerComebackHandler.isPreviousMakaoPlayerIndexExist()) {
            playerComebackHandler.returnToMakaoPlayer();
        } else if (shouldProceedToNextPlayer(currentPlayer)) {
            goToNextPlayer();
        }
    }

    boolean shouldProceedToNextPlayer(Player player) {
        return player == getCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerId);
    }

    void goToNextPlayer() {
        currentPlayerId++;
        if (currentPlayerId > players.size() - 1) {
            currentPlayerId = 0;
        }
    }

    void goToPreviousPlayer() {
        currentPlayerId--;
        if (currentPlayerId < 0) {
            currentPlayerId = players.size() - 1;
        }
    }

    public void goToHumanPlayer() {
        currentPlayerId = 0;
    }

    Player getPreviousPlayer() {
        int playerBeforeIndex = currentPlayerId - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    Player getNextPlayer() {
        int nextPlayerIndex = currentPlayerId + 1;
        if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return players.get(nextPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public Human getHumanPlayer() {
        return humanPlayer;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public PlayerComebackHandler getPlayerComebackHandler() {
        return playerComebackHandler;
    }
}

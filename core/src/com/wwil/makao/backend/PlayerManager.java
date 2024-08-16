package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public PlayerManager(int amountOfPlayers, int startingCards, DeckManager deckManager) {
        createPlayers(amountOfPlayers, startingCards, deckManager);
    }

    private void createPlayers(int amountOfPlayers, int startingCards, DeckManager deckManager  ) {
        for (int i = 0; i < amountOfPlayers; i++) {
            Player player = new Player(deckManager.giveCards(startingCards));
            players.add(player);
        }
    }

    protected boolean shouldProceedToNextPlayer(Player player){
        return player == getCurrentPlayer();
    }

    protected Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    protected void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex > players.size() - 1) {
            currentPlayerIndex = 0;
        }
    }

    protected void playerBefore() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size() - 1;
        }
    }

    protected Player getPlayerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    protected Player getNextPlayer() {
        int nextPlayerIndex = currentPlayerIndex + 1;
        if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return players.get(nextPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }
    public Player getHumanPlayer() {
        return players.get(0);
    }

}

package com.wwil.makao.backend.model.player;

import com.wwil.makao.backend.core.DeckManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final List<Player> players = new ArrayList<>();
    private Human humanPlayer;
    private int currentPlayerIndex = 0;

    public PlayerManager(int amountOfPlayers, int startingCards, DeckManager deckManager) {
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

    public boolean shouldProceedToNextPlayer(Player player) {
        return player == getCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void goToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex > players.size() - 1) {
            currentPlayerIndex = 0;
        }
    }

    public void goToPreviousPlayer() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size() - 1;
        }
    }

    public Player getPreviousPlayer() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    public Player getNextPlayer() {
        int nextPlayerIndex = currentPlayerIndex + 1;
        if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return players.get(nextPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Human getHumanPlayer(){
        return humanPlayer;
    }
}

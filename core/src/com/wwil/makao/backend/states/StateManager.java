package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public class StateManager {
    private final RoundManager roundManager;
    private final ComputerPlayFactory computerPlayFactory;

    public StateManager(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.computerPlayFactory = new ComputerPlayFactory(roundManager, this);
    }

    public List<Play> generatePlays(Player currentPlayer) {
        return computerPlayFactory.generatePlays(currentPlayer, roundManager);
    }

    public void applyDefenceState(Player player, Card attackingCard) {
        changePlayerState(player, new DefenseState(attackingCard));
    }

    public void applyDefaultState(Player player){
        changePlayerState(player, new DefaultState());
    }

    void applyPullingState(Player player){
        changePlayerState(player, new PullingState(roundManager.giveAmountOfPulls()));
    }

    void applyBlockedState(Player player){
        changePlayerState(player, new BlockedState(roundManager.giveAmountOfWaits()));
    }

    private void changePlayerState(Player player, PlayerState newState) {
        player.changeState(newState);
    }

}

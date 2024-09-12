package com.wwil.makao.backend.gameplay.utils;

import com.wwil.makao.backend.gameplay.management.PlayerManager;
/*
  Używana przy zgłoszeniu makao. Jeżeli komputer zgłosi makao to wtedy kolejka wraca do gracza człowieka.
  Po ruchu człowieka kolejka wraca do komputera, który zgłosił makao.
  */
public class PlayerComebackHandler {
    private Integer previousMakaoPlayerIndex = null;
    private final PlayerManager playerManager;

    public PlayerComebackHandler(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void saveCurrentIndexBeforeMakao() {
        previousMakaoPlayerIndex = playerManager.getCurrentPlayerId();
    }

    public boolean isPreviousMakaoPlayerIndexExist() {
        return previousMakaoPlayerIndex != null;
    }

    public void returnToMakaoPlayer() {
        if (previousMakaoPlayerIndex != null) {
            playerManager.setCurrentPlayerId(previousMakaoPlayerIndex);
            previousMakaoPlayerIndex = null;
        }
    }
}

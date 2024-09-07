package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.Gdx;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.utils.sound.SoundManager;

public abstract class TurnManager {
    protected final UIManager uiManager;
    protected final InputManager inputManager;
    protected final SoundManager soundManager;

    public TurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        this.uiManager = uiManager;
        this.inputManager = inputManager;
        this.soundManager = soundManager;
    }

    public abstract void show(RoundReport roundReport);

    abstract void endTurn();

    protected void putCard(CardActor playedCard, PlayerHandGroup player, boolean alignCards) {
        uiManager.addCardActorToStackGroup(playedCard);
        endIfPlayerWon(player);
        if (alignCards) {
            player.moveCloserToStartingPosition();
        }

        if(player.getPlayer().hasOneCard()){
            uiManager.getMakaoButton().setActive(true);
        }

        if (!playedCard.getCard().isShadow()) {
            soundManager.play("put.wav");
        }
    }


    protected void pull(PlayReport playReport, PlayerHandGroup player) {
        CardActor drawnCardActor = uiManager.getCardActorFactory().createCardActor(playReport.getDrawn());
        player.addActor(drawnCardActor);
        soundManager.play("pull.wav");
    }

    private void endIfPlayerWon(PlayerHandGroup handGroup) {
        //Do czasu wprowadzenia menu
        if (handGroup.getPlayer().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
            System.out.println(handGroup.getCardsAlignment() + " won");
            Gdx.app.exit();
        }
    }

    protected void informMakao(){
        uiManager.getMakaoButton().setActive(false);
    }

    protected PlayerHandGroup humanHand() {
        return uiManager.getHumanHandGroup();
    }
}

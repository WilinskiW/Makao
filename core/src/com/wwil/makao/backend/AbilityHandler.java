package com.wwil.makao.backend;

public class AbilityHandler {
    private final PlayerManager playerManager;
    private final PlayMaker playMaker;

    public AbilityHandler(PlayerManager playerManager, PlayMaker playMaker) {
        this.playerManager = playerManager;
        this.playMaker = playMaker;
    }

    protected void useCardAbility(PlayReport playReport) {
        Card card = playReport.getPlay().getCardPlayed();

        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                changeSuit(playReport);
                break;
            case PLUS_2:
                attack(2);
                break;
            case PLUS_3:
                attack(3);
                break;
            case WAIT:
                //useWaitAbility(wildCard);
                break;
            case DEMAND:
                demand(playReport);
                break;
            case KING:
                chooseAbilityForKing(card);
                break;
            case WILD_CARD:
                createCard(playReport);
                break;
        }
    }

    private void changeSuit(PlayReport playReport){
        if(playReport.getPlayer() == playerManager.getHumanPlayer()){
            if (!playReport.getPlay().isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }

    private void attack(int amountOfCards) {
        playMaker.increaseAmountOfPulls(amountOfCards);
        playerManager.getNextPlayer().setAttack(true);
    }

    private void demand(PlayReport playReport){
        if(playReport.getPlayer() == playerManager.getHumanPlayer()){
            if (!playReport.isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(5);
                break;
            case SPADE:
                attackPrevious();
                break;
        }
    }

    private void attackPrevious(){
        playMaker.increaseAmountOfPulls(5);
        playerManager.playerBefore();
        playerManager.getCurrentPlayer().setAttack(true);
    }

    private void createCard(PlayReport playReport){
        if(playReport.getPlayer() == playerManager.getHumanPlayer()){
            if (!playReport.isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }
}

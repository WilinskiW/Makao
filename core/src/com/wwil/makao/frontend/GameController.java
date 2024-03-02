package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.Play;
import com.wwil.makao.backend.PlayReport;
import com.wwil.makao.backend.RoundReport;
import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.frontend.gameComponents.CardActor;
import com.wwil.makao.frontend.gameComponents.PlayerHandGroup;
import com.wwil.makao.frontend.gameComponents.PullButtonActor;
import com.wwil.makao.frontend.gameComponents.StackCardsGroup;
import com.wwil.makao.frontend.parameters.CardsAlignmentParams;

import java.util.ArrayList;
import java.util.List;

//Komunikacja miedzy backendem a frontendem
public class GameController {
    private final GameplayScreen gameplayScreen;
    private final MakaoBackend backend = new MakaoBackend();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final StackCardsGroup stackCardsGroup = new StackCardsGroup(backend.getStack());
    private PullButtonActor pullButtonActor;
    private final DragAndDropManager dragAndDropManager = new DragAndDropManager(this);
    private final Stage stage;

    //Frontend może jedynie odczytywać. Jedynie może wykonywać w executeDropAction
    //Zrobic zeby dzialalo
    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.stage = gameplayScreen.getStage();
    }
//Skupic się na jednej metodzie. (Rozbudowac inne)

    public void executeDropAction(CardActor chosenCardActor) {
        RoundReport report = backend.executeAction(new Play(chosenCardActor.getCard(),false)); //przycisk też tu sprowadzimy i damy wtedy true
        PlayerHandGroup human = handGroups.get(0);
        if (!report.isCorrect()) {
            positionCardInGroup(human, chosenCardActor);
        }
        else {
            putCard(chosenCardActor,human);
            computerTurns(report);
        }
    }

    private void putCard(CardActor cardToPlay, PlayerHandGroup player){
        addCardActorToStackGroup(cardToPlay); //polozyc aktora
        player.moveCloserToStartingPosition(); //autowyrównanie
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }



    public void createHandGroups() {
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup(getBackend().getPlayers().get(i)));
        }
        setPlayersCardActorsAlignmentParams();
        for(PlayerHandGroup handGroup : handGroups){
            for(Card card : handGroup.getPlayerHand().getCards()){
                CardActor cardActor = cardActorFactory.createCardActor(card);
                handGroup.addActor(cardActor);
            }
        }
    }

    private void setPlayersCardActorsAlignmentParams() {
        getHandGroups().get(0).setCardsAlignment(CardsAlignmentParams.SOUTH);
        getHandGroups().get(1).setCardsAlignment(CardsAlignmentParams.EAST);
        getHandGroups().get(2).setCardsAlignment(CardsAlignmentParams.NORTH);
        getHandGroups().get(3).setCardsAlignment(CardsAlignmentParams.WEST);
    }

    public CardActor createStartingCardActorForStackGroup(){
        return cardActorFactory.createCardActor(backend.getStack().peekCard());
    }

//todo Zmiana koloru przy Drag

//    public void executeDragAction(CardActor chosenCardActor) {
//        if (isCardActorCorrect(chosenCardActor)) {
//            chosenCardActor.setColor(Color.LIME);
//        } else {
//            chosenCardActor.setColor(Color.SCARLET);
//        }
//    }



    private void moveCardBackToHumanGroup(PlayerHandGroup humanGroup, CardActor card) {
        humanGroup.addActor(card);
        card.setX(card.getLastPositionBeforeRemove().x);
        card.setY(card.getLastPositionBeforeRemove().y);
        card.setZIndex((int) card.getLastPositionBeforeRemove().z);
    }

    private void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
        if (!human.getChildren().isEmpty()) {
            chosenCard.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(human, chosenCard);
        }
    }

    private void computerTurns(RoundReport roundReport) {
        turnOffHumanInput();
        executeComputersTurn(roundReport);
        turnOnHumanInput();
    }

    private void turnOffHumanInput() { //todo na razie nie używane - przyda się na czas animowania
        pullButtonActor.changeTransparency(0.5f);
        Gdx.input.setInputProcessor(null);
    }

    //Logika będzie wykonywana w backendzie. Backend przygotuje juz szybciej dane
    //Frontend bedzie dodawał do sceny i wykonywał delay
    private void executeComputersTurn(final RoundReport roundReport) {
        for (int i = 1; i < handGroups.size(); i++) {
            final PlayerHandGroup currentHandGroup = handGroups.get(i);
            final PlayReport currentPlay = roundReport.getPlays().get(i);
            final float delta = 1.5f;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if(!currentPlay.isWaiting()){
                        if(currentPlay.getPlayed() != null) {
                            CardActor cardToPlay = currentHandGroup.findCardActor(currentPlay.getPlayed());
                            putCard(cardToPlay,currentHandGroup);
                        }
                        else{
                            CardActor drawnCard = cardActorFactory.createCardActor(currentPlay.getDrawn());
                            currentHandGroup.addActor(drawnCard);
                        }
                    }
                }
            }, i * delta); // Opóźnienie względem indeksu
        }
    }
//todo Pullbutton

//    private void playerPullCardActor(int playerIndex){
//        CardActor cardActor = cardActorFactory.createCardActor(backend.playerPullCard(playerIndex));
//        gameplayScreen.getStage().addActor(cardActor);
//        handGroups.get(playerIndex).addActor(cardActor);
//        if(playerIndex == 0){
//            cardActor.setUpSideDown(false);
//            dragAndDropManager.prepareDragAndDrop(cardActor, dragAndDropManager.getTarget());
//        }
//    }

    private void turnOnHumanInput() {
        pullButtonActor.changeTransparency(1);
        Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }


    public void executeDragStop(CardActor card) {
        PlayerHandGroup humanGroup = handGroups.get(0);
        if (!humanGroup.getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(humanGroup, card);
        }
    }

    public PlayerHandGroup getHumanHand() {
        return handGroups.get(0);
    }

    public void handlePullButtonInput() {
        int graphicsY = gameplayScreen.getViewport().getScreenHeight() - Gdx.input.getY();
        if (pullButtonActor.checkIfButtonIsClick(graphicsY) ) { //todo Zrobić input blocka
            performPullButtonClick();
        }
    }

    private void performPullButtonClick() {
        pullButtonActor.setClick(true);
        performButtonAnimation();
    }


    private void performButtonAnimation() {
        Timer.Task undoClick = new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                pullButtonActor.setClick(false);
            }
        };
        Timer.schedule(undoClick, 0.5f);
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public void setPullButtonActor(PullButtonActor pullButtonActor) {
        this.pullButtonActor = pullButtonActor;
    }

    public List<PlayerHandGroup> getHandGroups() {
        return handGroups;
    }

    public MakaoBackend getBackend() {
        return backend;
    }

    public DragAndDropManager getDragAndDropManager() {
        return dragAndDropManager;
    }

    public GameplayScreen getGameplayScreen() {
        return gameplayScreen;
    }
}

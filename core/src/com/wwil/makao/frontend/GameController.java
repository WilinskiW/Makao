package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.*;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.groups.CardChooserGroup;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.groups.PlayerHandGroup;
import com.wwil.makao.frontend.entities.groups.StackCardsGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Komunikacja miedzy back endem a front endem

public class GameController {
    private final GameplayScreen gameplayScreen;
    private final MakaoBackend backend = new MakaoBackend();
    private final CardActorFactory cardActorFactory = new CardActorFactory();
    private final List<PlayerHandGroup> handGroups = new ArrayList<>();
    private final StackCardsGroup stackCardsGroup = new StackCardsGroup(backend.getStack());
    private CardChooserGroup cardChooser;
    private GameButton pullButton;
    private GameButton endTurnButton;
    private final DragAndDropManager dragAndDropManager = new DragAndDropManager(this);
    private final Stage stage;
    private boolean inputBlockActive = false;

    //Frontend może jedynie odczytywać.
    public GameController(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.stage = gameplayScreen.getStage();
    }

    //Jak działał report?
    //1. Gracz wybierał akcje (drag, put, pull).
    //2. Akcja ta była wysyła do backendu
    //3. Backend tworzył Play Report'y gracza i komputerów
    //4. Wszystko to było pakowane do RoundReporta
    //5. Frontend wszystko wyświetla.

    //Jak ma teraz działać?
    //1. Gracz kładzie tyle kart ile może -> wymagane ich sprawdzania przez backend
    //2. Gdy chce pociągnąć wtedy tura jest automatycznie kończona.
    //3. Gdy naciśnie przycisk End turn to wtedy są tworzone playa dla komputerów

    //Plan działania:
    //DLA GRACZA:
    //1. Zrobić tak, żeby gracz mógł położyć więcej niż jedną kartę (ZROBIONE)
    //2. Gdy gracz dobiera kartę to wtedy automatycznie jest kończona tura. (ZROBIONE)
    //3. Gdy gracz położy kartę to wtedy nie może dobrać karty (ZROBIONE)
    //4. Gdy gracz nie położył żadnej karty to przycisk końca tury jest nieaktywny. (ZROBIONE)
    //5. Gracz jedynie może położyć karty tej samej rangi (ZROBIONE)
    //6. Położone karty używają swoich umiejętności:
        //- Dla jednej karty. (ZROBIONE)
        //- Dla więcej niż jedna. [NA RAZIE BEZ STACKOWANIA]
    //7. Każda karta, która aktywuje okno wyboru działa:
        //- Dla jednej karty:
            //-AS (ZROBIONE)
            //-J
            //-JOKER
        //- Dla więcej niż jedna.

    public void executeTurn(CardActor cardPlayed, boolean isDropped, boolean humanBlock, boolean isDemanding) {
        RoundReport report;
        PlayerHandGroup human = handGroups.get(0);

        if (humanBlock) {
            report = createWaitReport();
        } else if (pullButton.isClick()) {
            report = createPullReport(isDropped, human);
        }
        else if(endTurnButton.isClick()){
            report = backend.executeAction(new HumanPlay(null,false,
                    true,false,false,false, true));
        } else {
            if (isDemanding) {
                report = createDemandReport(cardPlayed);
            } else if (isDropped) {
                report = createDropReport(cardPlayed, cardChooser.isVisible());
            } else {
                report = createDragReport(cardPlayed);
            }
        }

        if (report.isBlockPullButton()) {
            pullButton.setActive(false);
        }

        if (endTurnButton.isClick() || pullButton.isClick()) {
            cardChooser.setVisibility(false);
            pullCard(report.getPlayReports().get(0));
            turnOffHumanInput();
            executeComputersTurn(report);
        }
    }

    private RoundReport createWaitReport() {
        return backend.executeAction
                (new HumanPlay(null, false, false, false, true, false, true));
    }

    private RoundReport createPullReport(boolean isDropped, PlayerHandGroup human) {
        RoundReport report = backend.executeAction
                (new HumanPlay(null, true, isDropped,
                        false, false, false, false));
        pullCard(report.getPlayReports().get(0).getDrawn(), human);
        return report;
    }

    private void pullCard(Card card, PlayerHandGroup player) {
        CardActor drawnCard = cardActorFactory.createCardActor(card);
        if (player == getHumanHand()) {
            drawnCard.setUpSideDown(false);
            dragAndDropManager.prepareDragAndDrop(drawnCard);
        }
        player.addActor(drawnCard);
    }

    private RoundReport createDemandReport(CardActor cardPlayed) {
        getHumanHand().moveCloserToStartingPosition();
        cardChooser.setVisibility(false);
        endTurnButton.setActive(true);
        return backend.executeAction
                (new HumanPlay(Collections.singletonList(cardPlayed.getCard()), false, false,
                        true, false, true, false));
    }

    private RoundReport createDragReport(CardActor cardPlayed) {
        RoundReport report = backend.executeAction
                (new HumanPlay(Collections.singletonList(cardPlayed.getCard()), false, false,
                        false, false, false, false));
        changeCardColor(report.getPlayReports().get(0).isCardCorrect(), cardPlayed);
        return report;
    }

    public void changeCardColor(boolean isCardCorrect, CardActor chosenCardActor) {
        if (isCardCorrect) {
            chosenCardActor.setColor(Color.LIME);
        } else {
            chosenCardActor.setColor(Color.SCARLET);
        }
    }

    private RoundReport createDropReport(CardActor cardPlayed, boolean isCardChooserActive) {
        RoundReport report = backend.executeAction
                (new HumanPlay(Collections.singletonList(cardPlayed.getCard()), false, true,
                        isCardChooserActive, false, false, false));
        if (report.isAttemptCorrect() && report.getPlayReports().get(0).isCardCorrect()) {
            putCard(cardPlayed, getHumanHand(),true);
            endTurnButton.setActive(true);
            cardChooser.setVisibility(false);
        }else if(report.isChooserActive()){
            showCardChooser(cardPlayed);
        }
        else {
            positionCardInGroup(getHumanHand(), cardPlayed);
        }

        return report;
    }

    private void putCard(CardActor cardToPlay, PlayerHandGroup player, boolean alignCards) {
        addCardActorToStackGroup(cardToPlay);
        endIfPlayerWon(player);
        if(alignCards) {
            player.moveCloserToStartingPosition();
        }
    }

    private void showCardChooser(CardActor cardPlayed) {
        CardActor stackCard = stackCardsGroup.peekCardActor();
        putCard(cardPlayed, getHumanHand(),false);
        cardChooser.setVisibility(true);
        cardChooser.getManager().setDisplayCard(stackCard, cardPlayed);
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        stage.addActor(cardActor);
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    private void endIfPlayerWon(PlayerHandGroup playerHandGroup) {
        //Do czasu wprowadzenia menu
        if (playerHandGroup.getPlayerHand().checkIfPlayerHaveNoCards()) {
            System.out.println(playerHandGroup + " won");
            Gdx.app.exit();
        }
    }

    private void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
        if (!human.getChildren().isEmpty()) {
            chosenCard.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(human, chosenCard);
        }
    }

    private void moveCardBackToHumanGroup(PlayerHandGroup humanGroup, CardActor card) {
        humanGroup.addActor(card);
        card.setX(card.getLastPositionBeforeRemove().x);
        card.setY(card.getLastPositionBeforeRemove().y);
        card.setZIndex((int) card.getLastPositionBeforeRemove().z);
    }

    private void pullCard(PlayReport playReport) {
        AbilityReport abilityReport = playReport.getAbilityReport();
        if (abilityReport != null && abilityReport.getToPull() != null) {
            for (Card card : abilityReport.getToPull()) {
                pullCard(card, handGroups.get(abilityReport.getPerformerIndex()));
            }
        }
    }

    public void executeDragStop(CardActor card) {
        if (!getHumanHand().getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(getHumanHand(), card);
        }
    }

    public void turnOffHumanInput() {
        pullButton.setActive(false);
        endTurnButton.setActive(false);
        Gdx.input.setInputProcessor(null);
        setInputBlockActive(true);
    }

    private void executeComputersTurn(final RoundReport roundReport) {
        float delta = 1.5f;
        final int numberOfComputers = handGroups.size() - 1;
        final AtomicInteger completedComputers = new AtomicInteger(0);

        for (int i = 1; i < handGroups.size(); i++) {
            final PlayerHandGroup currentHandGroup = handGroups.get(i);
            final PlayReport currentPlayReport = roundReport.getPlayReports().get(i);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (!currentPlayReport.isBlocked()) {
                        processComputerTurn(currentPlayReport, currentHandGroup);
                    }
                    // Sprawdź, czy to był ostatni ruch komputera
                    if (completedComputers.incrementAndGet() == numberOfComputers) {
                        if (currentPlayReport.getAbilityReport() != null && currentPlayReport.getAbilityReport().isBlockNext()) {
                            executeTurn(null, false, true, false);
                        } else {
                            turnOnHumanInput();
                        }
                    }
                }
            }, i * delta); // Opóźnienie względem indeksu
        }
    }

    private void processComputerTurn(PlayReport currentPlayReport, PlayerHandGroup currentHandGroup) {
        pullCard(currentPlayReport);
        List<Card> cardsToPlay = currentPlayReport.getPlay().getCardsPlayed();
        if (cardsToPlay != null) {
            putCard(currentHandGroup.getCardActor(cardsToPlay.get(0)), currentHandGroup,false);
            putChosenCardIfNecessary(currentPlayReport.getAbilityReport(), currentHandGroup, currentPlayReport.getPlay());
        } else {
            CardActor drawnCard = cardActorFactory.createCardActor(currentPlayReport.getDrawn());
            currentHandGroup.addActor(drawnCard);
        }
    }

    private void putChosenCardIfNecessary(AbilityReport abilityReport, PlayerHandGroup currentHandGroup, Play play) {
        if (abilityReport != null && (abilityReport.getChoosenCard() != null && !abilityReport.isDemanded()
                || play.getCardPlayed().getRank().equals(Rank.JOKER))) {
            putCard(cardActorFactory.createCardActor(abilityReport.getChoosenCard()), currentHandGroup,false);
        }
    }

    private void turnOnHumanInput() {
        setInputBlockActive(false);
        pullButton.setActive(true);
        Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }

    public CardActor peekStackCardActor() {
        return stackCardsGroup.peekCardActor();
    }

    public PlayerHandGroup getHumanHand() {
        return handGroups.get(0);
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public void setPullButton(GameButton pullButton) {
        this.pullButton = pullButton;
    }

    public void setEndTurnButton(GameButton endTurnButton) {
        this.endTurnButton = endTurnButton;
    }

    public void setInputBlockActive(boolean inputBlockActive) {
        this.inputBlockActive = inputBlockActive;
    }

    public void setCardChooser(CardChooserGroup cardChooser) {
        this.cardChooser = cardChooser;
    }

    public boolean isInputBlockActive() {
        return inputBlockActive;
    }

    public CardActorFactory getCardActorFactory() {
        return cardActorFactory;
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
/*
* todo To zrobić:
*  Bez podświetlania (na razie)
* Obiekt Play:
* Okno może się uruchamiać ale może nie przepuścić
* zagranie próba
* zagranie normalne
* zagranie normalne + wybór
* dobranie karty (nie pozwoli Ci jeśli już zagrałeś) (Pierwsza karta ratuje)
* kończę turę -> zapytanie na backendzie
*
*
*  zagralem karte (próba lub prawdziwe zagranie)
*    -> wybór
*  kończe turę
*  dobieram karte
*
* zadam figur (jopek)
* zmiana koloru (as)
* przeciwnik dobiera x (2,3, krol pik/kier)
* przeciwnik czeka x tur (4)
* jocker ( zmienia sie w dowolna wybrana karte)
*
* //Abstrakcyjna klasa Event
* //event dobierania -> aktywny, jakie karty
* //event czekania
* //event żądania -> aktywny, jaka karta (ranga)
*
*
* */
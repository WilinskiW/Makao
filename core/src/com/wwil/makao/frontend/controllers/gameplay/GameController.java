package com.wwil.makao.frontend.controllers.gameplay;
import com.wwil.makao.backend.gameplay.Action;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundReport;
import com.wwil.makao.frontend.utils.sound.SoundManager;
import com.wwil.makao.frontend.controllers.facedes.BackendFacade;
import com.wwil.makao.frontend.controllers.managers.ComputerTurnManager;
import com.wwil.makao.frontend.controllers.managers.HumanTurnManager;
import com.wwil.makao.frontend.controllers.managers.InputManager;
import com.wwil.makao.frontend.controllers.managers.UIManager;

//Komunikacja miedzy back endem a front endem

public class GameController {
    private final BackendFacade backend;
    private final UIManager uiManager;
    private final InputManager inputManager;
    private final SoundManager soundManager;
    private final HumanTurnManager humanTurnManager;
    private final ComputerTurnManager computerTurnManager;

    public GameController(GameplayScreen gameplayScreen) {
        this.backend = new BackendFacade();
        this.uiManager = new UIManager(this, gameplayScreen);
        this.inputManager = new InputManager(this);
        this.soundManager = new SoundManager();
        this.humanTurnManager = new HumanTurnManager(uiManager,inputManager, soundManager);
        this.computerTurnManager = new ComputerTurnManager(uiManager,inputManager, soundManager);
    }

    public void executePlay(Play play) {
        RoundReport report = backend.processHumanPlay(play);
        humanTurnManager.show(report);
        if (play.getAction() == Action.END) {
            computerTurnManager.show(report);
        }
    }

    public BackendFacade getBackend() {
        return backend;
    }

    public UIManager getUiManager() {
        return uiManager;
    }
    public InputManager getInputManager() {
        return inputManager;
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
}
package com.wwil.makao.frontend.utils.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    public void playPut() {
        playButtonClick("put.wav");
    }

    public void playPull() {
        playButtonClick("pull.wav");
    }

    private void playButtonClick(String soundName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/soundEffects/" + soundName));
        sound.play();
    }

    public void playButtonClick() {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/soundEffects/" + "buttonClick.mp3"));
        sound.play(0.05f);
    }
}

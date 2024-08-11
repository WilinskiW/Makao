package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    public void play(String soundName,float volume){
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/soundEffects/"+soundName));
        sound.play(volume);
    }

    public void play(String soundName){
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/soundEffects/"+soundName));
        sound.play();
    }
}

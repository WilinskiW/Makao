package com.wwil.makao;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.Makao;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Makao");
		config.setWindowedMode(GUIparams.WIDTH,GUIparams.HEIGHT);
		config.setMaximized(true);

		new Lwjgl3Application(new Makao(), config);
	}
}

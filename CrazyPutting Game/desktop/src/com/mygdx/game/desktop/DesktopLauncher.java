package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.physics.bullet.collision.MyCallback;
import com.mygdx.game.Demo3D;
import com.mygdx.game.Isometric;
import com.mygdx.game.Mesh1;
import com.mygdx.game.Isometric;
import com.mygdx.game.Mesh3D;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.States.test;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GolfMain.WIDTH;
		config.height = GolfMain.HEIGHT;
		config.title = GolfMain.frameTitle;
		new LwjglApplication(new GolfMain(), config);
	}
}

package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.States.GameStateManager;
import com.mygdx.game.States.MenuState;

public class GolfMain extends ApplicationAdapter{

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public static Music music;
    public static Sound audioeffect;

    public static String frameTitle = "Fire Golf";

    private SpriteBatch batch;
    private GameStateManager gsm;

    public static ParticleEffect pe;

    public void create(){
        batch = new SpriteBatch(); 
        gsm =new GameStateManager();
        music = Gdx.audio.newMusic(Gdx.files.internal("golf_music_full.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        audioeffect = Gdx.audio.newSound(Gdx.files.internal("golfgameeffect.mp3"));
        Gdx.gl.glClearColor(1,0,0,1);
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("fire.particle"), Gdx.files.internal(""));
        pe.getEmitters().first().setPosition(WIDTH/2 + 10, HEIGHT/2+180);
        pe.start();
        gsm.push(new MenuState(gsm));
   }

    public void render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }    

    public void dispose(){
        super.dispose();
        music.dispose();
    }

}
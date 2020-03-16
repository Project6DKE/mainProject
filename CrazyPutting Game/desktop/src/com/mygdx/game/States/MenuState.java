package com.mygdx.game.States;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.desktop.GolfMain;

public class MenuState extends State{

    private Texture background;
    private Texture playbtn;
    private Texture coursegeneratorbtn;
    private Texture mutemusic;
    private Texture exitbtn;

    private final int WIDTH = 1920;    
    private final int HEIGHT = 1110;    

    public MenuState(GameStateManager gsm){
        super(gsm); 
        background = new Texture("menu_bgd.jpeg");
        playbtn = new Texture("playbtn.png");
        coursegeneratorbtn = new Texture("coursegeneratorbtn.png");
        mutemusic = new Texture("mutemusic.png");
        exitbtn = new Texture("exitbtn.png");
    }


    protected void handleInput(){
        if(Gdx.input.isTouched()){
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            long id = GolfMain.audioeffect.play(0.03f);
            GolfMain.audioeffect.setLooping(id, false);
            //System.out.println(x + "   " + y);
            if(x>((WIDTH/2)-(291/2)) && x<((WIDTH/2)+291/2) && y>((HEIGHT/2)-(120*2)) && y<((HEIGHT/2)-120)){
                //in start button
                System.out.println("IN PLAY");
                gsm.set(new PlayMenuState(gsm));
            }
            if(x>((WIDTH/2)-(1262/2)) && x<((WIDTH/2)+1262/2) && y>((HEIGHT/2) - 120) && y<((HEIGHT/2+20))){
                //in course generator button
                System.out.println("in coursegenerator");
            }
            if(x>((WIDTH/2)-(806/2)) && x<((WIDTH/2)+806/2) && y>((HEIGHT/2)) && y<((HEIGHT/2+120+20))){
                //mute button
                if(GolfMain.music.isPlaying()){
                    GolfMain.music.pause();
                }
                else{
                    GolfMain.music.play();
                }
            }
            if(x>((WIDTH/2)-(285/2)) && x<((WIDTH/2)+285/2) && y>((HEIGHT/2)+120) && y<((HEIGHT/2+(120*2)+20))){
                //exit button
                Gdx.app.exit();
            }
            
        }
    }

    public void update(float dt){
        handleInput();
    }
    public void render(SpriteBatch sb){
        sb.begin();
        sb.draw(background, 0, 0, WIDTH, HEIGHT);
        sb.draw(playbtn,WIDTH/2 - (291/2),HEIGHT/2,291,168);
        sb.draw(coursegeneratorbtn, WIDTH/2-(1262/2), (HEIGHT/2) - 120, 1262, 134);
        sb.draw(mutemusic, WIDTH/2-(806/2),HEIGHT/2 - (120*2), 806, 146);
        sb.draw(exitbtn, WIDTH/2 - (285/2) , HEIGHT/2 - (120*3), 285, 131);
        GolfMain.pe.update(Gdx.graphics.getDeltaTime());
        GolfMain.pe.draw(sb);
        sb.end();
    }

    public void dispose(){
        background.dispose();
        playbtn.dispose();
        coursegeneratorbtn.dispose();
        mutemusic.dispose();
        exitbtn.dispose();
    }

}
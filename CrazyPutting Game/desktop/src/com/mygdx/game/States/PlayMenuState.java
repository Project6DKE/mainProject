package com.mygdx.game.States;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.desktop.GolfMain;

public class PlayMenuState extends State{

    private Texture background;
    private Texture mode1btn;
    private Texture mode2btn;
    private Texture returnbtn;

    private final int WIDTH = 1920;    
    private final int HEIGHT = 1110;    

    public PlayMenuState(GameStateManager gsm){
        super(gsm); 
        background = new Texture("menu_bgd.jpeg");
        mode1btn = new Texture("mode1.png");
        mode2btn = new Texture("mode2.png");
        returnbtn = new Texture("returnbtn.png");
    }


    protected void handleInput(){
        if(Gdx.input.isTouched()){
            long id = GolfMain.audioeffect.play(0.03f);
            GolfMain.audioeffect.setLooping(id, false);
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            //System.out.println(x + "   " + y);
            if(x>((WIDTH/2)-(512/2)) && x<((WIDTH/2)+512/2) && y>((HEIGHT/2)-(128*2)) && y<((HEIGHT/2)-128)){
                //in mode1 button
                System.out.println("IN MODE1");
            }
            if(x>((WIDTH/2)-(512/2)) && x<((WIDTH/2)+512/2) && y>((HEIGHT/2) - 128) && y<((HEIGHT/2+20))){
                //in mode2 button
                System.out.println("IN MODE2");
            
            }
            if(x>((WIDTH/2)-(455/2)) && x<((WIDTH/2)+455/2) && y>((HEIGHT/2)+20) && y<((HEIGHT/2+134+20))){
                //in return button
                gsm.set(new MenuState(gsm));
            
            }
        }
    }

    public void update(float dt){
        handleInput();
    }
    public void render(SpriteBatch sb){
        sb.begin();
        sb.draw(background, 0, 0, WIDTH, HEIGHT);
        sb.draw(mode1btn, WIDTH/2 - (512/2),HEIGHT/2,512,128);
        sb.draw(mode2btn, WIDTH/2-(512/2), (HEIGHT/2) - 128, 512, 128);
        sb.draw(returnbtn, WIDTH/2-(455/2), HEIGHT/2-(128*2), 455, 134);
        GolfMain.pe.update(Gdx.graphics.getDeltaTime());
        GolfMain.pe.draw(sb);
        sb.end();
    }

    public void dispose(){
        background.dispose();
        mode1btn.dispose();
        mode2btn.dispose();
        returnbtn.dispose();
    }

}
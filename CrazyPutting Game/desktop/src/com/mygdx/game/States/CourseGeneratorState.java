package com.mygdx.game.States;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.game.desktop.GolfMain;


public class CourseGeneratorState extends State{

    private Texture background;
    private Skin skin;
    private Stage stage;

    private final int WIDTH = 1920;    
    private final int HEIGHT = 1110;    

    public CourseGeneratorState(GameStateManager gsm){
        super(gsm); 
        background = new Texture("menu_bgd.jpeg");
        
    }

    public void create(){
        skin = new Skin(Gdx.files.internal("glassy-ui.json"));
        stage = new Stage();
        final TextButton button = new TextButton("click me", skin);
        button.setWidth(50);
        button.setHeight(200);
        stage.addActor(button);
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
        GolfMain.pe.update(Gdx.graphics.getDeltaTime());
        GolfMain.pe.draw(sb);
        sb.end();
 
    }

    public void dispose(){
        background.dispose();
    }


}
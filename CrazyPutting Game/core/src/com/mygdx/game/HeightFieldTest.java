package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
 
public class HeightFieldTest{

    Pixmap map = new Pixmap(Gdx.files.internal("grass.jpg"));
    HeightField test = new HeightField(true, map, true, 4);



}
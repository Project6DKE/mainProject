package com.mygdx.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class HeightmapConverter {

   public int mapWidth, mapHeight;
   private float[] heightMap;
   public float[] vertices;
   public short[] indices;
   private int strength;
   private String heightmapFile;
   private float textureWidth;
   
   public HeightmapConverter(int mapWidth, int mapHeight, int strength, String heightmapFile)
   {
      this.heightMap = new float[(mapWidth+1) * (mapHeight+1)];
      this.mapWidth = mapWidth;
      this.mapHeight = mapHeight;
      this.vertices = new float[heightMap.length*5];
        this.indices = new short[mapWidth * mapHeight * 6];
        this.strength = strength;
        this.heightmapFile = heightmapFile;
       
       loadHeightmap();
        createIndices();
        createVertices();
   }
   
   public void loadHeightmap()
   {
      try{
         FileHandle handle = Gdx.files.internal(heightmapFile);
         Pixmap heightmapImage = new Pixmap(handle);
         textureWidth = (float)heightmapImage.getWidth();
         Color color = new Color();
         int indexToIterate = 0;
         for(int y = 0;  y < mapHeight + 1; y++)
         {
            for(int x = 0; x < mapWidth + 1; x++)
            {
               Color.rgba8888ToColor(color, heightmapImage.getPixel(x, y));
               heightMap[indexToIterate++] = color.r;
            }
         }
         handle = null;
         heightmapImage.dispose();
         heightmapImage = null;
         color = null;
         indexToIterate = 0;
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   public void createVertices()
   {
        int heightPitch = mapHeight + 1;
        int widthPitch = mapWidth + 1;

        int idx = 0;
        int hIdx = 0;

        for(int z = 0; z < heightPitch; z++)
        {
           for(int x = 0; x < widthPitch; x++)
            {
              vertices[idx+0] = x;
              vertices[idx+1] = heightMap[hIdx++] * strength;
              vertices[idx+2] = z;
              vertices[idx+3] = x/textureWidth;
              vertices[idx+4] = z/textureWidth;
              idx += 5;
            }
        }
   }

   public void createIndices()
   {
        int idx = 0;
        short pitch = (short)(mapWidth + 1);
        short i1 = 0;
        short i2 = 1;
        short i3 = (short)(1 + pitch);
        short i4 = pitch;

        short row = 0;

        for(int z = 0; z < mapHeight; z++)
        {
           for(int x = 0; x < mapWidth; x++)
            {
               indices[idx++] = (short)(i1);
                indices[idx++] = (short)(i2);
                indices[idx++] = (short)(i3);

                indices[idx++] = (short)(i3);
                indices[idx++] = (short)(i4);
                indices[idx++] = (short)(i1);

                i1++;
                i2++;
                i3++;
                i4++;
            }

           row += pitch;
           i1 = row;
           i2 = (short)(row + 1);
           i3 = (short)(i2 + pitch);
           i4 = (short)(row + pitch);
        }
   }
   
   public String getHeightmapFile()
   {
      return heightmapFile;
   }
   
}



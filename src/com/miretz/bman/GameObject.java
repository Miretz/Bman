package com.miretz.bman;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

public class GameObject implements Configuration {

    public int x;
    public int y;

    public void render(String textureName) {
        BombermanGame.textures.get(textureName).bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2i(x, y);
        glTexCoord2f(1, 0);
        glVertex2i(x + BOX_SIZE, y);
        glTexCoord2f(1, 1);
        glVertex2i(x + BOX_SIZE, y + BOX_SIZE);
        glTexCoord2f(0, 1);
        glVertex2i(x, y + BOX_SIZE);
        glEnd();
    }

    public void renderPlayer(String textureName, float xOffset) {
        BombermanGame.textures.get(textureName).bind();
        glBegin(GL_QUADS);
        glTexCoord2f(xOffset + 0, 0);
        glVertex2i(x, y);
        glTexCoord2f(xOffset + 0.25f, 0);
        glVertex2i(x + BOX_SIZE, y);
        glTexCoord2f(xOffset + 0.25f, 1);
        glVertex2i(x + BOX_SIZE, y + BOX_SIZE);
        glTexCoord2f(xOffset + 0, 1);
        glVertex2i(x, y + BOX_SIZE);
        glEnd();
    }

    public void renderExplosion() {
        //center
        renderCustom(x, y);
        renderCustom(x - BOX_SIZE, y);
        renderCustom(x + BOX_SIZE, y);
        renderCustom(x, y - BOX_SIZE);
        renderCustom(x, y + BOX_SIZE);
    }

    public void renderCustom(int xr, int yr) {
        BombermanGame.textures.get("explosion").bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2i(xr, yr);
        glTexCoord2f(1, 0);
        glVertex2i(xr + BOX_SIZE, yr);
        glTexCoord2f(1, 1);
        glVertex2i(xr + BOX_SIZE, yr + BOX_SIZE);
        glTexCoord2f(0, 1);
        glVertex2i(xr, yr + BOX_SIZE);
        glEnd();

    }
}
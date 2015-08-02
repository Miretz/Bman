package com.miretz.bman;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class BombermanGame implements Configuration {

    public static final Map<String, Texture> textures = new HashMap<>();
    public static final List<Box> level = new ArrayList<>();
    public static final List<Bomb> bombs = new ArrayList<>();
    public static final List<Player> players = new ArrayList<>();

    private BombermanGame() {

        initializeDisplay();

        // create the players
        Player player1 = new Player("Player 1", 0, SCREEN_Y - BOX_SIZE, "player1");
        Player player2 = new Player("Player 2", SCREEN_X - BOX_SIZE, SCREEN_Y - BOX_SIZE, "player2");
        players.add(player1);
        players.add(player2);

        generateLevel();

        //load font
        Font awtFont = new Font(FONT_TYPE, Font.BOLD, 24);
        TrueTypeFont font = new TrueTypeFont(awtFont, false);

        // load textures
        for (String textureName : textureNames) {
            textures.put(textureName, loadTexture(textureName));
        }

        Display.setTitle("Bman");

        while (!Display.isCloseRequested()) {

            // render
            glClear(GL_COLOR_BUFFER_BIT);

            // esc exit game
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Display.destroy();
                System.exit(0);
            }

            if (players.stream().anyMatch(player -> player.getLives() == 0)) {
                Optional<Player> livingPlayer = players.stream().filter(player -> player.getLives() > 0).findFirst();
                font.drawString(200, 100, livingPlayer.get().getName() + " has won the match!", Color.yellow);
                if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                    Display.destroy();
                    System.exit(0);
                }
            } else {

                //remove detonated bombs
                bombs.removeIf(Bomb::isDetonated);

                // iterate bombs in reverse
                // this way the explosion will be over unexploded bombs
                ListIterator<Bomb> li = bombs.listIterator(bombs.size());
                while (li.hasPrevious()) {
                    li.previous().draw();
                }

                level.stream().forEach(Box::draw);
                players.stream().forEach(Player::draw);

                //down, up, right, left, bomb
                player1.playerKeyPress(Keyboard.KEY_S, Keyboard.KEY_W, Keyboard.KEY_D, Keyboard.KEY_A, Keyboard.KEY_LSHIFT);
                player2.playerKeyPress(Keyboard.KEY_DOWN, Keyboard.KEY_UP, Keyboard.KEY_RIGHT, Keyboard.KEY_LEFT, Keyboard.KEY_RSHIFT);

            }

            //draw score in the game
            font.drawString(10, 0, "" + player1.getLives(), Color.yellow);
            font.drawString(SCREEN_X - 30, 0, "" + player2.getLives(), Color.yellow);
            glColor3f(1.0f, 1.0f, 1.0f);

            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    private void initializeDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(SCREEN_X, SCREEN_Y));
            Display.setTitle("Loading...");
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // init opengl
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, SCREEN_X, SCREEN_Y, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //background color
        glClearColor(0.12f, 0.12f, 0.12f, 1);

    }

    private void generateLevel() {

        //13 x 17

        // generate stones
        for (int by = BOX_SIZE; by < SCREEN_Y; by += 2 * BOX_SIZE) {
            for (int bx = BOX_SIZE; bx < SCREEN_X; bx += 2 * BOX_SIZE) {
                level.add(new Box(bx, by, false));
            }
        }

        //generate boxes
        Random rnd = new Random();
        for (int by = 0; by < SCREEN_Y; by += BOX_SIZE) {

            if (by == SCREEN_Y - BOX_SIZE) {

                for (int bx = 3 * BOX_SIZE; bx < SCREEN_X - (3 * BOX_SIZE); bx += BOX_SIZE) {
                    if ((rnd.nextInt(100)) < PROBABLE_BOX)
                        level.add(new Box(bx, by, true));
                }
            } else {
                if (by % (2 * BOX_SIZE) == 0) {
                    for (int bx = 0; bx < SCREEN_X; bx += BOX_SIZE) {
                        if ((rnd.nextInt(100)) < PROBABLE_BOX)
                            level.add(new Box(bx, by, true));
                    }
                } else {
                    for (int bx = 0; bx < SCREEN_X; bx += 2 * BOX_SIZE) {
                        if ((rnd.nextInt(100)) < PROBABLE_BOX)
                            level.add(new Box(bx, by, true));
                    }
                }

            }
        }
    }

    private Texture loadTexture(String key) {
        try {
            return TextureLoader.getTexture("PNG", new FileInputStream(
                    new File("res/" + key + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new BombermanGame();
    }

}

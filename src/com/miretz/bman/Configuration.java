package com.miretz.bman;

import java.util.Arrays;
import java.util.List;

public interface Configuration {

    int SCREEN_X = 680;
    int SCREEN_Y = 520;
    int BOX_SIZE = 40;
    int PROBABLE_BOX = 35;
    int SPEED = 4;
    int MOVEMENT_SMOOTHING = 20;
    String FONT_TYPE = "Arial";
    int MAX_BOMBS = 5;

    List<String> textureNames = Arrays.asList("wood", "stone", "player1", "player2", "bomb", "explosion", "kill");
}

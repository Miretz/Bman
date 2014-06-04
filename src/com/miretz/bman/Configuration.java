package com.miretz.bman;

import java.util.Arrays;
import java.util.List;

interface Configuration {

    public static final int SCREEN_X = 680;
    public static final int SCREEN_Y = 520;
    public static final int BOX_SIZE = 40;
    public static final int PROBABLE_BOX = 35;
    public static final int SPEED = 4;
    public static final int MOVEMENT_SMOOTHING = 20;
    public static final String FONT_TYPE = "Arial";
    public static final int MAX_BOMBS = 1;

    public static final List<String> textureNames = Arrays.asList("wood", "stone", "player", "bomb", "explosion");
}

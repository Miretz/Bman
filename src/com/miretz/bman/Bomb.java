package com.miretz.bman;

import java.util.Iterator;

class Bomb extends GameObject implements Configuration {

    //bomb timer 3 seconds
    private int timer = 3 * 60;
    private boolean isDetonated = false;
    private final Player placedByPlayer;

    public Bomb(int x, int y, Player placedByPlayer) {
        this.x = x;
        this.y = y;
        this.placedByPlayer = placedByPlayer;
    }

    public boolean isDetonated() {
        return isDetonated;
    }

    public Player getPlacedByPlayer() {
        return placedByPlayer;
    }

    public void draw() {
        timer--;
        if (timer == 0) {
            //if a player died reset all players and detonate all bombs
            if (BombermanGame.players.stream().anyMatch(player -> !player.living)) {
                BombermanGame.players.forEach(Player::resetPosition);
                for (Bomb b : BombermanGame.bombs) {
                    b.isDetonated = true;
                }
            }
            isDetonated = true;
        } else if (timer == 20) {
            this.destroyBoxes();
        } else if (timer < 20) {
            //kill player instantly
            BombermanGame.players.forEach(this::destroyPlayer);
            super.renderExplosion();
        } else {
            super.render("bomb");
        }

    }

    private void destroyBoxes() {
        Iterator<Box> i = BombermanGame.level.iterator();

        //destroy boxes
        while (i.hasNext()) {
            Box box = i.next();
            if (x == box.x) {
                if (y == box.y + BOX_SIZE || y == box.y - BOX_SIZE) {
                    if (box.isBreakable()) {
                        i.remove();
                    }
                }
            }
            if (y == box.y) {
                if (x == box.x + BOX_SIZE || x == box.x - BOX_SIZE) {
                    if (box.isBreakable()) {
                        i.remove();
                    }
                }
            }
        }
    }

    private void destroyPlayer(Player pl) {
        //destroy players
        if(!pl.living) return;

        if (x > pl.x - BOX_SIZE * 2 + 5 && x < pl.x + BOX_SIZE * 2 - 5 && pl.y == y) {
            pl.die();
            return;
        }
        if (y > pl.y - BOX_SIZE * 2 + 5 && y < pl.y + BOX_SIZE * 2 - 5 && pl.x == x) {
            pl.die();
        }
    }


}
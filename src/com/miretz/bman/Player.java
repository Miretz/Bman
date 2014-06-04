package com.miretz.bman;

import org.lwjgl.input.Keyboard;

class Player extends GameObject implements Configuration {
	
	private int lives;
	private final String name;
    private final int start_x;
    private final int start_y;

	Player(String name, int x, int y) {
		this.x = x;
		this.y = y;
        this.start_x = x;
        this.start_y = y;
		this.lives = 5;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void die(){
		lives--;
        this.x = start_x;
        this.y = start_y;
	}

	public int getLives() {
		return lives;
	}
	
	void update(Direction direction) {
		switch (direction) {
		case UP:
			if ((y - SPEED) < 0) break;
			int ax = searchLeftRight();
			if (ax==-1) break;
			if ((BombermanGame.level.stream().anyMatch((box -> box.x==ax && y == box.y+BOX_SIZE)))) break;
			if ((BombermanGame.bombs.stream().anyMatch((bomb -> bomb.x==ax && y == bomb.y+BOX_SIZE)))) break;
            if ((BombermanGame.players.stream().anyMatch((player -> player.x==ax && y == player.y+BOX_SIZE)))) break;
			this.x = ax;
			this.y = y - SPEED;
			break;
		case DOWN:
			if ((y + SPEED) > SCREEN_Y - BOX_SIZE) break;
			int bx = searchLeftRight();
			if (bx==-1) break;
			if ((BombermanGame.level.stream().anyMatch((box -> box.x==bx && y+BOX_SIZE == box.y)))) break;
			if ((BombermanGame.bombs.stream().anyMatch((box -> box.x==bx && y+BOX_SIZE == box.y)))) break;
            if ((BombermanGame.players.stream().anyMatch((player -> player.x==bx && y+BOX_SIZE == player.y)))) break;
			this.x = bx;
			this.y = y + SPEED;
			break;
		case LEFT:
			if ((x - SPEED) < 0) break;
			int ay = searchUpDown();
			if (ay==-1) break;
			if ((BombermanGame.level.stream().anyMatch((box -> box.y==ay && x == box.x+BOX_SIZE)))) break;
			if ((BombermanGame.bombs.stream().anyMatch((box -> box.y==ay && x == box.x+BOX_SIZE)))) break;
            if ((BombermanGame.players.stream().anyMatch((players -> players.y==ay && x == players.x+BOX_SIZE)))) break;
			this.y = ay;
			this.x = x - SPEED;
			break;
		case RIGHT:
			if ((x + SPEED) > SCREEN_X - BOX_SIZE) break;
			int by = searchUpDown();
			if (by==-1) break;
			if ((BombermanGame.level.stream().anyMatch((box -> box.y==by && x+BOX_SIZE == box.x)))) break;
			if ((BombermanGame.bombs.stream().anyMatch((box -> box.y==by && x+BOX_SIZE == box.x)))) break;
            if ((BombermanGame.players.stream().anyMatch((players -> players.y==by && x+BOX_SIZE == players.x)))) break;
			this.y = by;
			this.x = x + SPEED;
			break;
		}
	}
	
	//movement smooth
    int searchLeftRight(){
		if(x % BOX_SIZE != 0){
			for(int a = 0; a<MOVEMENT_SMOOTHING; a++){
				if((x+a)%BOX_SIZE==0){
					return x+a;
				}
				if((x-a)%BOX_SIZE==0){
					return x-a;
				}
			}
			return -1;
		}
		return x;
	}
	
	//movement smooth
    int searchUpDown(){
		if(y % BOX_SIZE != 0){
			for(int a = 0; a<MOVEMENT_SMOOTHING; a++){
				if((y+a)%BOX_SIZE==0){
					return y+a;

				}
				if((y-a)%BOX_SIZE==0){
					return y-a;
				}
			}
			return -1;
		} 
		return y;
	}

	
	void placeBomb(){
        if (BombermanGame.bombs.stream().filter(bomb -> bomb.getPlacedByPlayer() == this).count()<MAX_BOMBS) {
            int bombX = x;
            int bombY = y;
            while (bombX % BOX_SIZE != 0) {
                bombX++;
            }
            while (bombY % BOX_SIZE != 0) {
                bombY++;
            }
            Bomb bomb = new Bomb(bombX, bombY, this);
            BombermanGame.bombs.add(bomb);
        }
	}
	

	
	public void playerKeyPress(int down, int up, int right, int left, int bomb){
		if (Keyboard.isKeyDown(down)) {
			update(Direction.DOWN);
		}
		if (Keyboard.isKeyDown(up)) {
			update(Direction.UP);
		}
		if (Keyboard.isKeyDown(right)) {
			update(Direction.RIGHT);
		}
		if (Keyboard.isKeyDown(left)) {
			update(Direction.LEFT);
		}
		if (Keyboard.isKeyDown(bomb)) {
			placeBomb();
		}
	}
									
	public void draw() {
		super.render("player");
	}
}
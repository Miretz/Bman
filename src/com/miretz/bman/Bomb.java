package com.miretz.bman;

import java.util.Iterator;

class Bomb extends GameObject implements Configuration {
	
	private int timer = 3 * 60;
	private boolean isDetonated = false;
	
	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isDetonated() {
		return isDetonated;
	}

	public void draw() {
		timer--;
		if(timer==0){
			isDetonated = true;
            BombermanGame.players.forEach(this::destroyPlayer);
		} else if(timer==20){
				this.destroyBoxes();
		} else if(timer<20){
			super.renderExplosion();
		} else {
			super.render("bomb");
		}
		
	}
		
	private void destroyBoxes(){
		Iterator<Box> i = BombermanGame.level.iterator();
		
		//destroy boxes
		while(i.hasNext()){
			Box box = i.next();
			if(x == box.x){
				if(y==box.y+BOX_SIZE || y == box.y-BOX_SIZE){
					if(box.isBreakable()){
						i.remove();
					}
				}
			}
			if(y == box.y){
				if(x==box.x+BOX_SIZE || x == box.x-BOX_SIZE){
					if(box.isBreakable()){
						i.remove();
					}
				}
			}
		}
	}

    private void destroyPlayer(Player pl){
		//destroy players
        boolean died = false;
		if(x > pl.x - BOX_SIZE*2 + 5 && x < pl.x + BOX_SIZE*2 - 5 && pl.y == y){
			died = true;
		}
		if(y > pl.y - BOX_SIZE*2  + 5 && y < pl.y + BOX_SIZE*2 - 5 && pl.x == x){
			died = true;
		}
        if(died){
            pl.die();
            for(Bomb b : BombermanGame.bombs){
                b.isDetonated = true;
            }
        }
	}
	

}
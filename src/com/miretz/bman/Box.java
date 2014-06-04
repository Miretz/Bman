package com.miretz.bman;

class Box extends GameObject {
	
	private boolean isBreakable = true;

	Box(int x, int y, boolean isBreakable) {
		this.x = x;
		this.y = y;
		this.isBreakable = isBreakable;
	}
	
	public boolean isBreakable() {
		return isBreakable;
	}

	public void draw() {
		if (!this.isBreakable) {
			super.render("stone");
		} else {
			super.render("wood");
		}
	}

}

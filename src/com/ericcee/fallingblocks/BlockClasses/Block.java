package com.ericcee.fallingblocks.BlockClasses;
import java.awt.Color;

/**
 * Tetromino
 * Program Code by Eric Cee
 * Please dont redistribute
 * @author Eric Cee
 */

public class Block {
	private int x,y;
	private Color color;
	private boolean isMiddle=false;
	
	public Block(int x,int y,boolean middle,Color color){
		this.x=x;
		this.y=y;
		this.isMiddle=middle;
		this.color=color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public Boolean getMiddle() {
		return isMiddle;
	}
	
	
}

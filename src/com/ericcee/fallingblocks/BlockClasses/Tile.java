package com.ericcee.fallingblocks.BlockClasses;
import java.util.ArrayList;


/**
 * Tetromino
 * Program Code by Eric Cee
 * Please dont redistribute
 * @author Eric Cee
 */

public class Tile {
	private Block [] blocks=null;
	private Block middle = null;
	
	private int x = 0;
	private int y = 0;
	private boolean rotateable = true;
	private boolean inverted=false;
	
	/**
	 * Tetromino Constructor
	 * @param x
	 * @param y
	 * @param blocks
	 */
	public Tile(int x, int y, Block[] blocks){
		this.x=x;
		this.y=y;
		this.blocks = blocks;
		
		for(Block xx : blocks) if(xx.getMiddle()) middle = xx;
		
		setBlocksToPos(x, y);
	}
	
	/**
	 * Tetromino Contructor for cloning
	 * @param t
	 */
	public Tile(Tile t){
		blocks =  new Block[t.getBlocks().length];
		
		for(int i =0; i < t.getBlocks().length;i++) blocks[i]= new Block(t.getBlocks()[i].getX(),
				t.getBlocks()[i].getY(),
				t.getBlocks()[i].getMiddle(), t.getBlocks()[i].getColor());
		
		this.x=t.getX();
		this.y=t.getY();
		
		for(Block xx : blocks) if(xx.getMiddle()) middle = xx;
		setRotateable(t.isRotateable());
		setBlocksToPos(this.x, this.y);
	}
	
	public void setRotateable(boolean rotateable) {
		this.rotateable = rotateable;
	}
	
	/**
	 * Set the block to the position from the middle perspective
	 * @param X
	 * @param Y
	 */
	public void setBlocksToPos(int X,int Y){
		for(Block z : blocks){
			
			if(!z.getMiddle()){
				z.setX( (z.getX() - middle.getX()) + X);
				z.setY( (z.getY() - middle.getY()) + Y);
			}
		}
		
		middle.setX(X);
		middle.setY(Y);
		this.x=X;
		this.y=Y;
	}
	
	/**
	 * Rotates the teromino clockwise for the middle perspective
	 * @param blockss
	 * @param MaxWidth
	 * @param MaxHeight
	 * @return
	 */
	
	public boolean rotateClockwise(ArrayList<Block> blockss,int MaxWidth,int MaxHeight){
		
		if(!rotateable) return false;
		
		for(Block z : blocks){
			if(!z.getMiddle()){
				int X = middle.getX() - (z.getY() - middle.getY());
				int Y = middle.getY() + (z.getX() - middle.getX());
				for(Block b : blockss) if(b.getX() == X && b.getY() == Y || b.getY()> MaxHeight) return false;
				if(X < 0 || X >= MaxWidth || Y > MaxHeight) return false;
			}
			else for(Block b : blockss) if(b.getX() == z.getX() && b.getY() == z.getY()) return false;
		}
		
		for(Block z : blocks){
			if(!z.getMiddle()){
				int X = middle.getX() - (z.getY() - middle.getY());
				int Y = middle.getY() + (z.getX() - middle.getX());
				z.setX(X);
				z.setY(Y);
			}
		}
		setBlocksToPos(x,y);
		
		return true;
	}
	
	
	public void moveDown(){
		y++;
		setBlocksToPos(x,y);
	}
	
	public void moveLeft(){
		x--;
		setBlocksToPos(x, y);
	}
	
	public void moveRight(){
		x++;
		setBlocksToPos(x, y);
	}
	
	/**
	 * Checks leftside collision with wall|objects
	 * @param blocks
	 * @return
	 */
	public boolean checkCollisionLeft(ArrayList<Block> blocks) {
		for(Block xx : this.blocks){
			for(Block yy : blocks){
				if(xx.getX()-1 == yy.getX() && yy.getY() == xx.getY()) return true;
			}
			if( xx.getX() == 0) return true;
		}
		return false;
	}
	
	/**
	 * Checks rightside collision with wall|objects
	 * @param blocks
	 * @param m
	 * @return
	 */
	public boolean checkCollisionRight(ArrayList<Block> blocks,int m) {
		for(Block xx : this.blocks){
			for(Block yy : blocks){
				if(xx.getX()+1 == yy.getX() && yy.getY() == xx.getY()) return true;
			}
			if(xx.getX() == m-1) return true;
		}
		return false;
	}
	
	/**
	 * checks collision with bottom wall|objects
	 * @param blocks
	 * @param m
	 * @return
	 */
	public boolean checkCollisionBottom(ArrayList<Block> blocks,int m) {
		for(Block xx : this.blocks){
			for(Block yy : blocks){
				if((xx.getY()+1==yy.getY() && xx.getX() == yy.getX()) || (xx.getY()==yy.getY()&&xx.getX() == yy.getX()) ) return true;
			}
			if(xx.getY() == m-1)return true;
		}
		return false;
	}
	
	public Block[] getBlocks(){
		return blocks;
	}
	
	public boolean isRotateable(){
		return rotateable;
	}
	
	public Tile getCollisionPrediction(ArrayList<Block> blocks,int m){
		Tile ret = new Tile(this);
		
		for(int i =0; i< m; i++){
			if(ret.checkCollisionBottom(blocks, m)) break;
			ret.moveDown();
		}
		
		return ret;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public boolean getRotateable(){return rotateable;}
}

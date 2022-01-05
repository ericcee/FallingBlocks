/**
 * 
 */
package com.ericcee.fallingblocks.BlockClasses;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author e.cee
 *
 */
public class TileGen{
	
	private ArrayList<Block> blocks;
	private Random rng;
	private Color thC;
	private Color[] clrs = {Color.RED,Color.BLUE,Color.GREEN,Color.cyan,Color.yellow,Color.magenta};
	
	public TileGen(){
		rng = new Random();
		blocks=new ArrayList<>();
		thC = clrs[Math.abs(rng.nextInt())%6];
	}
	
	private boolean getNextBlock(int mx){
		boolean correct = false;
		boolean newBlock = true;
		int x = Math.abs(rng.nextInt())%mx;
		int y = Math.abs(rng.nextInt())%mx;
		for(Block thBlock : blocks){
			if(x==thBlock.getX()&&(y+1==thBlock.getY()||y-1==thBlock.getY())) correct=true;
			if(y==thBlock.getY()&&(x+1==thBlock.getX()||x-1==thBlock.getX())) correct=true;
			if(thBlock.getY()==y&&thBlock.getX()==x) newBlock=false;
		}
		
		if(correct&&newBlock){
			blocks.add(new Block(x, y, false, thC));
		}
		
		return correct&&newBlock;
	}
	
	public Tile genMino(int x, int y, int blocks){
		this.blocks.add(new Block(0, 0, true, thC));
		
		for(int i=0;i<blocks-1;i++){
			if(!getNextBlock(blocks))i--;
		}
		
		for(Block xxx : this.blocks) System.out.println("Block pos: "+ xxx.getX()+" "+xxx.getY());
		return new Tile(x, y, Arrays.copyOf(this.blocks.toArray(), this.blocks.toArray().length, Block[].class));
	}
	
}

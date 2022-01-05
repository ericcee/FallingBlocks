package com.ericcee.fallingblocks.tiles;

import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class ITile extends Tile {
	private static Block [] blks = {new Block(-2, 0, false, Color.red),new Block(-1, 0, false, Color.red),
			new Block(0, 0, true, Color.red),new Block(1, 0, false, Color.red)};
	
	public ITile(int x,int y){
		super(x,y,blks);
	}
}

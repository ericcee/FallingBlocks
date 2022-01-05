package com.ericcee.fallingblocks.tiles;

import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class RLTile extends Tile {
	private static Block [] blks = {new Block(0, 0, false, Color.green),new Block(0, 1, false, Color.green),
			new Block(1, 1, true, Color.green),new Block(2, 1, false, Color.green)};
	
	public RLTile(int x,int y){
		super(x,y,blks);
	}
}

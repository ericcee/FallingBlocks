package com.ericcee.fallingblocks.tiles;

import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class TTile extends Tile {
	private static Block [] blks = {new Block(1, 0, false, Color.orange),new Block(1, 1, true, Color.orange),
			new Block(0, 1, false, Color.orange),new Block(2, 1, false, Color.orange)};
	public TTile(int x,int y){
		super(x,y,blks);
	}
}

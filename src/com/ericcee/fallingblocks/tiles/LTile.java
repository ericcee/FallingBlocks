package com.ericcee.fallingblocks.tiles;
import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class LTile extends Tile {
	private static Block [] blks = {new Block(0, 1, false, Color.yellow),new Block(0, 0, false, Color.yellow),
			new Block(1, 0, true, Color.yellow),new Block(2, 0, false, Color.yellow)};
	
	public LTile(int x, int y){
		super(x,y,blks);
	}
}

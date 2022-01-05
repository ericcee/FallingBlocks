package com.ericcee.fallingblocks.tiles;

import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class ZTile extends Tile {
	private static Block [] blks = {new Block(0, 0, false, Color.cyan),new Block(1, 0, true, Color.cyan),
			new Block(1, 1, false, Color.cyan),new Block(2, 1, false, Color.cyan)};
	public ZTile(int x,int y){
		super(x,y,blks);
	}
}

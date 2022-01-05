package com.ericcee.fallingblocks.tiles;

import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class RZTIle extends Tile {
	private static Block [] blks = {new Block(0, 1, false, Color.pink),new Block(1, 1, false, Color.pink),
			new Block(1, 0, true, Color.pink),new Block(2, 0, false, Color.pink)};
	
	public RZTIle(int x,int y){
		super(x,y,blks);
	}
}

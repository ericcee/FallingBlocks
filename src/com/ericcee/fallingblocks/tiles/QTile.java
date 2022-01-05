package com.ericcee.fallingblocks.tiles;

import java.awt.Color;

import com.ericcee.fallingblocks.BlockClasses.Block;
import com.ericcee.fallingblocks.BlockClasses.Tile;

public class QTile extends Tile {
	private static Block [] blks = {new Block(0, 1, false, Color.blue),new Block(0, 0, false, Color.blue),
			new Block(1, 0, true, Color.blue),new Block(1, 1, false, Color.blue)};
	
	public QTile(int x,int y){
		super(x,y,blks);
		setRotateable(false);
	}
}

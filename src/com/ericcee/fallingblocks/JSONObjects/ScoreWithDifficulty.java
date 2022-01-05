package com.ericcee.fallingblocks.JSONObjects;

import java.util.Date;

public class ScoreWithDifficulty extends ScoreItem {
	private int difficulty = 0;

	public ScoreWithDifficulty(String name, int score, Date date,int difficulty) {
		super(name, score, date);
		this.difficulty=difficulty;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
}

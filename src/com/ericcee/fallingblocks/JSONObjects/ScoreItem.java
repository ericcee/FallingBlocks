package com.ericcee.fallingblocks.JSONObjects;

import java.util.Date;

public class ScoreItem {
	public String name;
	public int Score;
	public Date date;
	
	public ScoreItem(String name,int score,Date date){
		this.name=name;
		this.Score=score;
		this.date=date;
	}
}

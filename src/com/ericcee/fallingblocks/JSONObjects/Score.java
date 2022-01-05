package com.ericcee.fallingblocks.JSONObjects;

import java.util.ArrayList;

public class Score {
	public ArrayList<ScoreItem> scores;
	private String name="";
	public Score(ArrayList<ScoreItem>  scores,String name){
		this.scores=scores;
		this.name=name;
	}
	public String getName(){
		return name;
	}
}

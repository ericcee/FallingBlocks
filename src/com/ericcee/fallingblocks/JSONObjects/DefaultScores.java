package com.ericcee.fallingblocks.JSONObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DefaultScores extends Score {
	private static ScoreItem [] scoreList = {
			new ScoreItem("Peter", 10000, new Date()),
			new ScoreItem("Wolfram", 7000, new Date()),
			new ScoreItem("Jasmine", 2500, new Date()),
			new ScoreItem("Sarah", 2000, new Date()),
			new ScoreItem("Josef", 5000, new Date()),
			new ScoreItem("Jessica", 1700, new Date()),
	};
	public DefaultScores(String name){
		super( new ArrayList<ScoreItem>(Arrays.asList(scoreList)),name);
	}
}

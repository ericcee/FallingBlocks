package com.ericcee.fallingblocks.JSONObjects;

public class Command {
	private String command;
	private String returntext;
	private int code;
	private Score score;
	private ScoreWithDifficulty scoreItem;

	public String getCommand() {
		return command;
	}

	public String getReturntext() {
		return returntext;
	}

	public int getCode() {
		return code;
	}
	
	public Score getScore(){
		return score;
	}
	public ScoreWithDifficulty getScoreItem(){
		return scoreItem;
	}

	public Command(String command, String returntext, int code, Score score,ScoreWithDifficulty scoreItem) {
		super();
		this.command = command;
		this.returntext = returntext;
		this.code = code;
		this.score=score;
		this.scoreItem= scoreItem;
	}
}

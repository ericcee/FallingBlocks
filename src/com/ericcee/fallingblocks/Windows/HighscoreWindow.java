package com.ericcee.fallingblocks.Windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.ericcee.fallingblocks.JSONObjects.DefaultScores;
import com.ericcee.fallingblocks.JSONObjects.Score;
import com.ericcee.fallingblocks.JSONObjects.ScoreItem;
import com.ericcee.fallingblocks.JSONObjects.ScoreWithDifficulty;
import com.ericcee.fallingblocks.client.Client;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.Comparator;
import java.util.Date;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * HighscoreWindow
 * Program Code by Eric Cee
 * Please dont redistribute
 * @author Eric Cee
 */
public class HighscoreWindow extends Frame {
	
	private String highscoreLocation ="./Score.json";
	private JTable highscoreTable;
	private JButton aboutButton;
	private Score scores;
	private Client thClient = null;
	private boolean offline = true;
	private int difficulty = 1;
	
	public HighscoreWindow(int score, int diff,boolean offline) throws JsonSyntaxException, IOException, InterruptedException {
		super("Highscore");
		DisplayMode screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		
		this.setSize(512, 500);
		this.setLocation(screenSize.getWidth()/2-this.getWidth()/2, screenSize.getHeight()/2- this.getHeight()/2);
		this.setLayout(null);
		this.setResizable(false);
		difficulty=diff;
		
		String[] headerText = {"Nr.","Name","Score","Date"};
		
		highscoreTable = new JTable(new DefaultTableModel(headerText,25));
		
		highscoreTable.setBounds(0,30,this.getSize().width,this.getSize().height-90);
		highscoreTable.setEnabled(false);
		highscoreTable.setPreferredScrollableViewportSize(highscoreTable.getPreferredSize());
		highscoreTable.setValueAt("Nr.", 0, 0);
		highscoreTable.setValueAt("Name", 0, 1);
		highscoreTable.setValueAt("Score", 0, 2);
		highscoreTable.setValueAt("Date", 0, 3);
		
		aboutButton = new JButton("About");
		aboutButton.setSize(this.getWidth(),55);
		aboutButton.setLocation(0,highscoreTable.getHeight()+30);
		aboutButton.setPreferredSize(aboutButton.getSize());
		aboutButton.setMaximumSize(aboutButton.getSize());
		this.offline=offline;
		
		if(!this.offline) thClient  = new Client("http://ericcee.com", 8080);
		else reloadScores();
		
		aboutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showAboutDialog();
			}
		});
		
		this.add(aboutButton);
		this.add(highscoreTable);
		
		if(!doScoresExist()){
			try {
				saveDefaultScores();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(!this.offline){
			if(score!=0) thClient.requestSaveEntry( new ScoreWithDifficulty(showEnterNameDialog(), score, new Date(), difficulty));
		}
		else{
			if(checkScoresInList(score)&&score!=0){
				ScoreItem thi = new ScoreItem(showEnterNameDialog(), score, new Date());
				addScore(thi);
				try {
					saveScores(scores);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					
					reloadScores();
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	private void saveDefaultScores() throws IOException{
		saveScores(new DefaultScores("Default"));
	}
	
	private void saveScores(Score scores) throws IOException{
		Gson gson = new Gson();
		String scoreJson = gson.toJson(scores);
		FileWriter f = new FileWriter(new File(highscoreLocation));
		BufferedWriter bf = new BufferedWriter(f);
		bf.write(scoreJson);
		bf.flush();
		bf.close();
	}
	
	private boolean doScoresExist(){
		File f = new File(highscoreLocation);
		return f.exists();
	}
	
	/**
	 * Sets table item 
	 * @param row
	 * @param score
	 */
	private void setTableItem(int row, ScoreItem score){		
		highscoreTable.setValueAt(row+1, row+1, 0);
		highscoreTable.setValueAt(unescapeHtml4(score.name), row+1, 1);
		highscoreTable.setValueAt(score.Score, row+1, 2);
		highscoreTable.setValueAt(score.date.toGMTString(), row+1, 3);

	}
	
	private void reloadScores() throws IOException, InterruptedException{
		Gson gson = new Gson();
		BufferedReader bf = new BufferedReader(new FileReader(new File(highscoreLocation)));
		String currentl ="";
		String rawJson ="";
		int r=0;
		
		while((currentl=bf.readLine() )!=null)rawJson+=currentl;
		bf.close();
		scores = gson.fromJson(rawJson, Score.class);
		
		Score sc = null;
		
		if(!offline){
			sc=thClient.requestScores(difficulty);
		}
		else{
			sc=scores;
		}
		
		sc.scores.sort( new Comparator<ScoreItem>() {

			@Override
			public int compare(ScoreItem o1, ScoreItem o2) {
				return o2.Score-o1.Score;
			}
		});
		
		for(ScoreItem x : sc.scores) {
			setTableItem(r, x);
			r++;
		}
	}
	
	/**
	 * Check if score gets a seat
	 * @param score
	 * @return Returns true/false if the score has a seat
	 */
	private boolean checkScoresInList(int score){
		for(ScoreItem s : scores.scores) {if(score<s.Score) return true;}
		if(scores.scores.toArray().length>=24) return false;
		return true;
	}
	
	/**
	 * Adds score to the list
	 * @param sco
	 */
	private void addScore(ScoreItem sco){
		if(scores.scores.toArray().length==24){
			ScoreItem ch = scores.scores.get(0);
			for(ScoreItem s : scores.scores) if(ch.Score > s.Score)ch=s;
			scores.scores.remove(ch);
		}
		
		scores.scores.add(sco);
	}
	
	private String showEnterNameDialog(){
		String z = (String) JOptionPane.showInputDialog(this, "Please enter your name:",
				"Enter Highscore", JOptionPane.CLOSED_OPTION,
                null,
                null,
                "Max Muster");
		return z;
	}
	private void showAboutDialog(){
		JOptionPane.showConfirmDialog(this, "Program written by Eric Cee\n"
				+ "Program Code © Eric Cee\n"
				+ "please do not copy or redistribute","About", JOptionPane.CLOSED_OPTION);
	}
}

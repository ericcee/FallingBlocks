package com.ericcee.fallingblocks.Windows;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.*;

import com.ericcee.fallingblocks.BlockClasses.Tile;
import com.ericcee.fallingblocks.BlockClasses.TileField;
import com.ericcee.fallingblocks.client.Client;
import com.google.gson.JsonSyntaxException;

/**
 * BlockWindow
 * Program Code by Eric Cee
 * Please dont redistribute
 * @author Eric cee
 *
 */

public class BlockWindow extends Frame {
	private TileField thFeld=null;
	private JLabel scoreLabel;
	private JLabel levelLabel;
	private JLabel linesLabel;
	private JLabel currentLabel;
	private JLabel nextLabel;
	private JLabel holdLabel;
	private JButton highscoreButton;
	private Timer updateTimer;
	private Tile oldCurrent = null;
	private Tile oldNext = null;
	private Tile oldHold = null;
	private int mode=1;
	
	public BlockWindow(){
		super("Falling Blocks");
		this.setSize(256*3,512);
		DisplayMode screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		this.setLocation(screenSize.getWidth()/2-this.getWidth()/2, screenSize.getHeight()/2- this.getHeight()/2);
		this.setPreferredSize(new Dimension(256*3, 512));
		this.setResizable(false);
		this.setLayout(null);
		
		thFeld = new TileField(256, 0, 10, 22, 250, this.getHeight());
		
		scoreLabel = new JLabel("Score: 0");
		levelLabel = new JLabel("Level: 1");
		linesLabel = new JLabel("Lines: 0");
		currentLabel = new JLabel("Current Tile:");
		nextLabel = new JLabel("Next Tile:");
		holdLabel = new JLabel("Holding Tile");
		highscoreButton= new JButton("Highscore");
		
		this.add(scoreLabel);
		this.add(linesLabel);
		this.add(levelLabel);
		this.add(currentLabel);
		this.add(nextLabel);
		this.add(holdLabel);
		this.add(highscoreButton);

		
		scoreLabel.setBounds(50, 20+35, 100, 10);
		levelLabel.setBounds(50, 40+35, 100, 10);
		linesLabel.setBounds(50, 60+35, 100, 10);
		
		currentLabel.setLocation(2*256+50, 20+35);
		nextLabel.setLocation(2*256+50, 100+35);
		holdLabel.setLocation(2*256+50, 180+35);
		
		nextLabel.setSize(nextLabel.getPreferredSize());
		holdLabel.setSize(holdLabel.getPreferredSize());
		currentLabel.setSize(currentLabel.getPreferredSize());
		
		highscoreButton.setBounds(this.getWidth()-256, this.getHeight()-50, 256, 50);
		highscoreButton.setPreferredSize(highscoreButton.size());
		highscoreButton.setFocusable(false);
		
		scoreLabel.setForeground(Color.white);
		levelLabel.setForeground(Color.white);
		linesLabel.setForeground(Color.white);
		currentLabel.setForeground(Color.white);
		nextLabel.setForeground(Color.white);
		holdLabel.setForeground(Color.white);
		
		
		this.setBackground(Color.gray);
				
		this.requestFocus();
		
		updateTimer = new Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				printScore();
			}
		});
		
		
		KeyListener key = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(thFeld.IsWaitingForStart()){
					thFeld.Start();
					return;
				}
				
				switch(e.getKeyCode()){
				case 39:
					thFeld.moveTetrominoRight();
					break;
				case 37:
					thFeld.moveTetrominoLeft();
					break;
				case 38:
					thFeld.rotateTetromino();
					break;
				case 40:
					thFeld.moveTetrominoDown();
					break;
				case 17:
					thFeld.hardDropTetromino();
					break;
				case 16:
					thFeld.holdItem();
					break;
				case 80:
					if(!thFeld.IsPause())thFeld.Pause();
					else thFeld.Start();
					break;
				case 32:
					thFeld.hardDropTetromino();
					break;
				default:
					System.out.println(e.getKeyCode());
				}
			}
		};
		
		this.addKeyListener(key);
		highscoreButton.addKeyListener(key);
		highscoreButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				thFeld.Pause();
				try {
					(new HighscoreWindow(0, mode, !(new Client("http://ericcee.com", 8080).checkIfServerIsAlive()))).show();
				} catch (JsonSyntaxException | IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {
				updateTimer.start();
				thFeld.Load();
			}
		});
		this.pack();
	}
	
	public void Run(int mode){
		this.setVisible(true);
		thFeld.setGraphics(this.getGraphics());
		thFeld.setMode(mode);
		this.mode=mode;
	}
	
	private void drawCurrTetrominoPreview(){
		Tile curr = new Tile(thFeld.getCurrentTetromino());
		
		if(oldCurrent!=null) thFeld.drawTetromino(2*256+100, 90, oldCurrent, Color.gray,2);
		
		thFeld.drawTetromino(2*256+100, 90, curr,2);
		oldCurrent=curr;
	}
	
	private void drawNextTetrominoPreview(){
		Tile curr = new Tile(thFeld.getNextTetromino());
		
		if(oldNext!=null) thFeld.drawTetromino(2*256+100, 180, oldNext, Color.gray,2);
		
		thFeld.drawTetromino(2*256+100, 180, curr,2);
		oldNext=curr;
	}
	
	private void drawHoldTetrominoPreview(){
		if(thFeld.getHoldTetromino()==null) return;
		Tile curr = new Tile(thFeld.getHoldTetromino());
		
		if(oldHold!=null)thFeld.drawTetromino(2*256+100, 270, oldHold, Color.gray,2);
		
		thFeld.drawTetromino(2*256+100, 270, curr,2);
		oldHold=curr;
	}
	
	private void printScore(){
		drawCurrTetrominoPreview();
		drawNextTetrominoPreview();
		drawHoldTetrominoPreview();
		
		scoreLabel.setText("Score: "+thFeld.getScore());
		levelLabel.setText("Level: "+(thFeld.getLevel()+1));
		linesLabel.setText("Lines: "+thFeld.getLines());
	}
	
}

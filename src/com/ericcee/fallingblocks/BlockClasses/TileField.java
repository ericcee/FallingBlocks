package com.ericcee.fallingblocks.BlockClasses;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.ericcee.fallingblocks.Windows.HighscoreWindow;
import com.ericcee.fallingblocks.client.Client;
import com.ericcee.fallingblocks.random.Randomizer;
import com.ericcee.fallingblocks.tiles.ITile;
import com.ericcee.fallingblocks.tiles.LTile;
import com.ericcee.fallingblocks.tiles.QTile;
import com.ericcee.fallingblocks.tiles.RLTile;
import com.ericcee.fallingblocks.tiles.RZTIle;
import com.ericcee.fallingblocks.tiles.TTile;
import com.ericcee.fallingblocks.tiles.ZTile;
import com.google.gson.JsonSyntaxException;

/**
 * Tetris Field
 * Program Code by Eric Cee
 * Please dont redistribute
 * @author Eric Cee
 */

public class TileField {
	private int x,y;
	private int wPerBlock= 0;
	private int hPerBlock= 0;
	private int BlocksPerW= 0;
	private int BlocksPerH = 0;
	private int Width=0;
	private int Height=0;
	private int score=0;
	private int level =0;
	private int nextLevelReach =10;
	private int lines = 0;
	private Random rnd = new Random();
	private Randomizer rng ;
	private Timer gameTimer;
	private boolean pause=true;
	private boolean allowedToHold=true;
	private boolean waitForStart=true;
	private boolean blinker=false;
	private Graphics2D graphics;
	private boolean inverted = false;
	private ArrayList<Block> fixedBlocks = new ArrayList<>();
	private Tile current = null;
	private Tile next = null;
	private Tile hold = null;
	private int Mode =0;
	
	
	private Tile [] tetrs = {new LTile(5, 0), new RLTile(5, 0),
			new ITile(5, 0), new ZTile(5, 0),
			new RZTIle(5, 0),new QTile(5, 0), new TTile(5,0)};
	
	/**
	 * TetrisField Constructor
	 * @param x
	 * @param y
	 * @param BlocksPerW
	 * @param BlocksPerH
	 * @param width
	 * @param height
	 */
	public TileField(int x,int y,int BlocksPerW,int BlocksPerH,int width,int height){
		this.x=x;
		this.y=y;
		this.BlocksPerH=BlocksPerH;
		this.BlocksPerW=BlocksPerW;
		this.wPerBlock=Math.round(width/BlocksPerW);
		this.hPerBlock=Math.round(height/BlocksPerH);
		this.Width=width;
		this.Height=height;
		rng = new Randomizer(0, 6);
		setNextTetromino();
		setNextTetromino();
		
		gameTimer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gameTick();
				} catch (JsonSyntaxException | IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void Load(){
		gameTimer.start();
	}
	
	public void Pause(){
		if(waitForStart)return;
		clearScreen();
		gameTimer.stop();
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("ARIAL BLACK", Font.PLAIN, 25)); 
		graphics.drawString("Paused.", x+50,256);
		pause=true;
	}
	
	public void Start(){
		if(!gameTimer.isRunning()) gameTimer.start(); 
		
		pause=false;
		waitForStart=false;
	}
	
	/**
	 * Sets next tetromino and moves the other to current
	 */
	private void setNextTetromino(){
		TileGen xy = new TileGen();
		current = next;
		
		if(Mode==3){
			next=xy.genMino(5, 0, rnd.nextInt(2)+4);
		}
		else next = new Tile(tetrs[rng.getNextNum()]);
		
		allowedToHold=true;
	}
	
	/**
	 * Game Timer Tick function: Here happens the game cycles
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 * @throws InterruptedException 
	 */
	private void gameTick() throws JsonSyntaxException, IOException, InterruptedException{
		redraw();
		if(waitForStart) return;
		
		if(!moveTetrominoDown()){ // Tetromino settle down
			if(current.getY()==0) { // lost game
				int sc = score;
				reset();
				HighscoreWindow w = new HighscoreWindow(sc,Mode, !(new Client("http://ericcee.com", 8080).checkIfServerIsAlive()));
				w.setVisible(true);
				return;
			}
			fixTetromino(current);
			addToScore(removeFullLines());
			setNextTetromino();
		}
		
	}
	
	/**
	 * getting the Frame graphics to draw the field
	 * @param g
	 */
	public void setGraphics(Graphics g){
		graphics=(Graphics2D)g;
	}
	
	private void clearScreen(){
		graphics.setColor(Color.black);
		graphics.fillRect(x, y, Width+1, Height-1);
	}
	
	private void redraw(){
		clearScreen();
		
		if(waitForStart){
			if(blinker){
				graphics.setColor(Color.WHITE);
				graphics.setFont(new Font("ARIAL BLACK", Font.PLAIN, 25)); 
				graphics.drawString("Any key to start.", x+30, 256);
			}
			blinker=!blinker;
		}
		else{
			drawTetromino(current);
			drawPredictionTetromino(current.getCollisionPrediction(fixedBlocks, BlocksPerH));
			drawBlocks();
		}
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	/**
	 * draws block at a diffrent x,y location
	 * param scale is the size divisor for the block
	 * param shadow draws a holow block
	 * @param x
	 * @param y
	 * @param b
	 * @param shadow
	 * @param c
	 * @param scale
	 */
	private void drawBlock(int x,int y, Block b,boolean shadow,Color c,int scale){
		int pointX = x + b.getX()* Math.round(this.wPerBlock/scale);
		int pointY = y + b.getY()* Math.round(this.hPerBlock/scale);
		drawBlock(pointX, pointY,shadow,c,scale);
	}
	
	/**
	 * draws block at the field
	 * @param b
	 * @param shadow
	 */
	private void drawBlock(Block b,boolean shadow){
		int pointX = this.x + b.getX()*this.wPerBlock;
		int pointY = this.y + (inverted?BlocksPerH-b.getY():b.getY())*this.hPerBlock;
		if(pointY+hPerBlock>Height) return;
		
		drawBlock(pointX, pointY,shadow,b.getColor(),1);
	}
	
	/**
	 * Draws block without block object
	 * param scale is the size divisor for the block
	 * param shadow draws a hollow block
	 * @param x
	 * @param y
	 * @param shadow
	 * @param color
	 * @param scale
	 */
	private void drawBlock(int x, int y,boolean shadow, Color color,int scale){
		graphics.setColor(color);
		
		if(!shadow)graphics.fillRect(x, y, wPerBlock/scale, hPerBlock/scale);
		
		graphics.setColor(Color.gray);
		graphics.drawRect(x, y, wPerBlock/scale, hPerBlock/scale);
	}
	
	/**
	 * Draws tetromino in the game field
	 * @param t
	 */
	private void drawTetromino(Tile t){
		for(Block x : t.getBlocks()){
			drawBlock(x,false);
		}
	}
	
	/**
	 * Draws tetromino at a different location with scalability
	 * @param x
	 * @param y
	 * @param t
	 * @param scale
	 */
	public void drawTetromino(int x, int y, Tile t,int scale){
		Tile diffL = new Tile(t);
		diffL.setBlocksToPos(0, 0);
		for(Block b : diffL.getBlocks()){
			drawBlock(x,y,b,false,b.getColor(),scale);
		}
	}
	
	/**
	 * Draws a tetromino with different color at a different position
	 * @param x
	 * @param y
	 * @param t
	 * @param c
	 * @param scale
	 */
	public void drawTetromino(int x, int y, Tile t,Color c,int scale){
		Tile diffL = new Tile(t);
		diffL.setBlocksToPos(0, 0);
		for(Block b : diffL.getBlocks()){
			drawBlock(x,y,b,false,c,scale);
		}
	}
	
	/**
	 * Draws the fixed blocks in the gamefield
	 */
	private void drawBlocks(){
		for(Block z : fixedBlocks)
			drawBlock(z,false);
	}
	
	/**
	 * draws the position of the tetromino whegameboyre it should land in hollow blocks
	 * @param t
	 */
	private void drawPredictionTetromino(Tile t){
		for(Block x : t.getBlocks()){
			drawBlock(x,true);
		}
	}
	
	public boolean moveTetrominoLeft(){
		if(pause) return false;
		if(!current.checkCollisionLeft(fixedBlocks)){
			current.moveLeft();
			redraw();
			return true;
		}
		return false;
	}
	
	public boolean moveTetrominoRight(){
		if(pause) return false;
		if(!current.checkCollisionRight(fixedBlocks, BlocksPerW)){
			current.moveRight();
			redraw();
			return true;
		}
		return false;
	}
	
	public boolean moveTetrominoDown(){
		if(pause) return false;
		if(!current.checkCollisionBottom(fixedBlocks,BlocksPerH)){
			current.moveDown();
			redraw();
			return true;
		}
		return false;
	}
	
	/**
	 * Hard drops a tetromino
	 */
	public void hardDropTetromino(){
		if(pause||current==null) return;
		current.setBlocksToPos(current.getX(), current.getCollisionPrediction(fixedBlocks, BlocksPerH).getY());
		try {
			gameTick();
		} catch (JsonSyntaxException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public boolean rotateTetromino() {
		if(pause||current==null) return false;
		int ox=current.getX();
		int oy=current.getY();
		boolean alternate = false;
		int xp =1;
		int z  =0;
		
		while(!current.rotateClockwise(fixedBlocks, BlocksPerW, BlocksPerH) ){
			oy=current.getY();
			current.setBlocksToPos(ox,oy);
			if(xp==3)break;
			oy=current.getY();
			current.setBlocksToPos(ox+(alternate?xp:-xp), oy);
			alternate=!alternate;
			if(z==2){xp++;z=0;}
			else z++;
			oy=current.getY();
		}
		
		redraw();
		return true;
	}
	/**
	 * removes a line of blocks in the field
	 * @param l
	 */
	private void removeLine(int l){
		ArrayList<Block> blocksRem = new ArrayList<>();
		for(Block b : fixedBlocks){
			if(b.getY()==l){
				blocksRem.add(b);
			}
			if(b.getY()<l) b.setY(b.getY()+1);
		}
		
		for(Block t : blocksRem) fixedBlocks.remove(t);
	}
	
	/**
	 * Score formula: adds lines to score
	 * @param lines
	 */
	private void addToScore(int lines){
		switch(lines){
		case 0:
			return;
		case 1:
			score+=40*(level+1);
			break;
		case 2:
			score+=100*(level+1);
			break;
		case 3:
			score+=300*(level+1);
			break;
		case 4:
			score+=1200*(level+1);
			break;
		}
		
		this.lines+=lines;
		
		if(this.lines >= nextLevelReach){
			switch(Mode){
			case 1:
				break;
			case 2:
				inverted=!inverted;
				break;
			case 3:
				inverted=!inverted;
				break;
			}
			
			nextLevelReach+=10;
			level++;
			gameTimer.setDelay(gameTimer.getDelay()-(gameTimer.getDelay()/5));
			addLineToBottom(rnd.nextInt(BlocksPerW));
		}
	}
	
	/**
	 * fixes the tetrominos blocks in the field
	 * @param curr
	 */
	
	private void fixTetromino(Tile curr){
		for(Block a : curr.getBlocks()) fixedBlocks.add(a);
	}
	
	/**
	 * checks lines and removes full ones
	 * returns number of lines what got removed
	 * @return returns number of lines removed
	 */
	public int removeFullLines(){
		int fullLines =0;
		int [] lines = new int [BlocksPerH];
		ArrayList<Integer> linesToRemove = new ArrayList<>();
		for(Block z : fixedBlocks){
			lines[z.getY()]++;
			if(lines[z.getY()]==BlocksPerW) {linesToRemove.add(z.getY()); fullLines++;};
		}
		
		linesToRemove.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer arg0, Integer arg1) {
				return arg0 - arg1;
			}
		});
		
		for(int xx : linesToRemove) { removeLine(xx); }
		
		if(fullLines!=0)
			try {
				lineRemoveAnimation(linesToRemove.toArray());
			} catch (InterruptedException e) { 
				e.printStackTrace();
		}
		
		return fullLines;
	}
	
	public boolean holdItem(){
		if(allowedToHold){
			if(hold==null) {
				hold=new Tile(current);
				hold.setBlocksToPos(4, 0);
				setNextTetromino();
			}
			else{
				Tile t = new Tile(current);
				current=hold;
				hold = t;
				hold.setBlocksToPos(4, 0);
			}
		}
		else return false;
		allowedToHold=false;
		return true;
	}
	
	/**
	 * the animation for the line removal
	 * @param l
	 * @throws InterruptedException (sleep function)
	 */
	
	private void lineRemoveAnimation(Object [] l) throws InterruptedException{
		for(int i=1; i<10; i++){
			graphics.setColor(i%2==0?Color.white:Color.black);
			
			for(Object ll : l){
				graphics.fillRect(x, y+hPerBlock*(inverted?BlocksPerH-(Integer)ll:(Integer)ll), Width, hPerBlock);
				graphics.drawRect(x, y+hPerBlock*(inverted?BlocksPerH-(Integer)ll:(Integer)ll), Width, hPerBlock);
				Toolkit.getDefaultToolkit().sync();
			}
			Thread.sleep(100);
		}
	}
	
	private void reset(){
		score=0;
		level=0;
		lines=0;
		nextLevelReach=10;
		inverted=false;
		waitForStart=true;
		gameTimer.setDelay(500);
		hold=null;
		fixedBlocks= new ArrayList<Block>();
	}
	
	private void addLineToBottom(int gapPos){
		ArrayList<Block> newBlockLine = new ArrayList<Block>();
		moveFixedBlocksUpward();
		for(int i=0; i<BlocksPerW; i++) {
			if(i==gapPos) continue;
			newBlockLine.add(new Block(i, BlocksPerH-1, false, Color.lightGray));
		}
		fixedBlocks.addAll(newBlockLine);
	}
	
	private void moveFixedBlocksUpward(){
		for(Block x : fixedBlocks) x.setY(x.getY()-1);
	}
	
	public int getScore(){return score;}
	public int getLines(){return lines;}
	public int getLevel(){return level;}
	public boolean IsPause(){return pause;}
	public Tile getCurrentTetromino(){return current;}
	public Tile getNextTetromino(){return next;}
	public Tile getHoldTetromino(){return hold;}
	public boolean IsWaitingForStart(){return waitForStart;}
	public void setMode(int m){Mode=m;}
	private int getMode(){return Mode;}
}

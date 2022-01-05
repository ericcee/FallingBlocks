package com.ericcee.fallingblocks.Windows;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;


public class ModeSelection extends Frame {
	private JRadioButton Mode1;
	private JRadioButton Mode2;
	private JRadioButton Mode3;
	private JButton okBtn;
	
	public ModeSelection(){
		super("Select mode");
		DisplayMode screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		
		this.setBounds(0,0,256,256);
		this.setLayout(new GridLayout(4, 1));
		this.setMaximumSize(this.getSize());
		
		this.setResizable(false);
		this.setLocation(screenSize.getWidth()/2-this.getWidth()/2, screenSize.getHeight()/2- this.getHeight()/2);
		this.setBackground(Color.GRAY);
		Mode1= new JRadioButton("Standard",true);
		Mode2= new JRadioButton("Advanced");
		Mode3= new JRadioButton("Crazy");
		okBtn = new JButton("Ok");
		
		ButtonGroup g = new ButtonGroup();
		g.add(Mode1);
		g.add(Mode2);
		g.add(Mode3);

		
		this.add(Mode1);
		this.add(Mode2);
		this.add(Mode3);
		this.add(okBtn);
		
		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockWindow w  = new BlockWindow();
				if(Mode1.isSelected()) {
					w.Run(1);
					dispose();
					
				}
				if(Mode2.isSelected()) {
					w.Run(2);
					dispose();
					
				}
				if(Mode3.isSelected()) {
					w.Run(3);
					dispose();
				}
			}
		});
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(-123);
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}

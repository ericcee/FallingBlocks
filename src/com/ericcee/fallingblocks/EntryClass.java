package com.ericcee.fallingblocks;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ericcee.fallingblocks.Windows.BlockWindow;
import com.ericcee.fallingblocks.Windows.ModeSelection;
import com.ericcee.fallingblocks.random.Randomizer;

public class EntryClass {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		ModeSelection MainW = new ModeSelection();
		MainW.setVisible(true);
	}
}

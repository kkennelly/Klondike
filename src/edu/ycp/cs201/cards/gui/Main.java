package edu.ycp.cs201.cards.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.ycp.cs201.cards.KlondikeController;
import edu.ycp.cs201.cards.KlondikeModel;
import edu.ycp.cs201.cards.StringifyGameState;

public class Main {
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				KlondikeView view = new KlondikeView();
				
				KlondikeController controller = new KlondikeController();
				
				KlondikeModel model = new KlondikeModel();

				// Start with a random initial game state
				controller.initModel(model);
				
				// Alternatively, you can comment out the line above and
				// uncomment one of the following lines to load one of
				// the game states used in KlondikeControllerTest.  This
				// is useful for testing features that rely on more advanced
				// game states.
				//StringifyGameState.stringToModel(model, "00000023G-NE,(MS')=TR#Q0IPB82A%D!00000053!00000000\"!00000000/!00000000!00000000!000000001J!00000001<4&?!00000002L+6!0000000397OU:!00000004H.>KF!00000000!00000006$5@C*;3");
				//StringifyGameState.stringToModel(model, "00000009G-NE,(MS')!00000053DA28BQT!" +
				//		"00000000\"#!00000000/0!00000000I!00000000<=!00000000U:!00000000!00000002L+6O4&?!" +
				//		"000000009!00000004H.>KFR7P!00000000!00000006$5@C*;3%1J");
				
				model.getWastePile().setExposeIndex(0);
				
				view.setModel(model);
				view.setController(controller);
				
				JFrame frame = new JFrame("Klondike!");
				frame.setContentPane(view);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

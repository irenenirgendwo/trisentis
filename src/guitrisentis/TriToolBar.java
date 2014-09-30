package guitrisentis;

import gui.AnalyseToolBar;
import gui.GameBoard;
import gui.GameToolBar;
import gui.HelpFrame;
import guigraph.GraphBoard;
import guigraph.GraphToolBar;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import trisentis.Trisentis;
/**
 * This ToolBar is a part of the Trisentis examination framework. On this ToolBar you can 
 * specify the Trisentis game with number of rows and number of columns and the option
 * whether diagonal neighbored fields are counted as neighbored or not.
 * It is possible to start the game, to show a help frame and something more.
 * 
 * @author Irene Thesing
 */
public class TriToolBar extends GameToolBar{
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = -4537804866917703920L;

	
		/**Where the actual row number is placed*/
		private SpinnerNumberModel rowSpinner;
		/**Where the actual column number is stored*/
		private SpinnerNumberModel colSpinner;
		
		/**
		 * Whether the grid game is a Trisentis game (a field in the middle has eight neighbours
		 * or only the four fields with common edges are neigbored.
		 * Checkbox to change this property.
		 */
		private JCheckBox diagChecker;
		
		/** 
		 * Creates a new TrisentisToolBar, 
		 * where we can change the properties and start a game 
		 * as well as some other actions.
		 * 
		 * @param gameboard the board where the game takes place.
		 */
		public TriToolBar(TrisentisBoard gameboard){
			super(gameboard);	
		}

		@Override
		protected void initOptionPart() {
			this.add(new JLabel("Einstellungen:  "));
			this.add(new JLabel(" Anzahl Zeilen "));
			rowSpinner = new SpinnerNumberModel(4,1,100,1);
			colSpinner = new SpinnerNumberModel(4,1,100,1);
			this.add(new JSpinner(rowSpinner));
			this.add(new JLabel(" Anzahl Spalten "));
			this.add(new JSpinner(colSpinner));
			this.add(new JLabel("  "));

			diagChecker = new JCheckBox("Diagonalnachbarschaften",true);
			this.add(diagChecker);
			this.addSeparator();
			
		}

		@Override
		protected void additionalInputAction() {
			if (diagChecker.isSelected())
				Trisentis.setTrisentis(true);
			else Trisentis.setTrisentis(false);
			Trisentis game = new Trisentis(rowSpinner.getNumber().intValue(),colSpinner.getNumber().intValue());
			this.gameboard = new TrisentisBoard(game,TrisentisBoard.INPUT_MODE);
			owner.putCentral(gameboard);
		}

		@Override
		protected AnalyseToolBar additionalStartAction() {
				if (diagChecker.isSelected())
					Trisentis.setTrisentis(true);
				else Trisentis.setTrisentis(false);
				Trisentis game = new Trisentis(rowSpinner.getNumber().intValue(),colSpinner.getNumber().intValue());
				this.gameboard = new TrisentisBoard(game,TrisentisBoard.GAME_MODE);
				owner.putCentral(gameboard);
				return new TriAnalyseToolBar(game,owner);
		}

		@Override
		protected void changeToOtherGame() {
			GraphBoard board = new GraphBoard(GameBoard.SHOW_MODE);
			GraphToolBar tools = new GraphToolBar(board);
			tools.setOwner(owner);
			owner.putCentral(board);
			owner.setToolBar(tools);
			owner.setTitle("Das \u03c3-Spiel auf allgemeinen Graphen spielen - Viel Spaß!");
			owner.repaint();
			owner.setVisible(true);
		}


		@Override
		protected int getSubclassConstant() {
			return HelpFrame.TRISENTIS;
		}
		
				

}

package gui;

import guigraph.GraphBoard;
import guigraph.GraphToolBar;
import guitrisentis.TriToolBar;
import guitrisentis.TrisentisBoard;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import trisentis.Trisentis;

/**
 * This is the frame started at the beginning. It is possible to choose whether 
 * a Trisentis game board or the game board to create an arbitrary graph shall be shown.
 * The main components are the game board in the middle, a GameToolBar to modify and start the
 * game and (if the game is started) a ToolBar to analyze the game in the south.
 * The class provides several methods to change this components in the flow.
 * 
 * @author Irene Thesing
 *
 */
public class MainFrame extends JFrame{
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = 3888753742434742654L;

	/**The game board which is shown in the middle of the frame.*/
	protected GameBoard gameboard;
	
	/**The ToolBar which is shown at the head of the frame, to modify and start the game.*/
	protected GameToolBar toolbar;
	
	/**
	 *The current component in the center of the grame. 
	 */
	protected JComponent centerComp;
	
	/**
	 * The present component in the south of the frame, normally a ToolBar for analysing the current game
	 * or nothing.
	 */
	protected JComponent southComp;


	/**
	 * Creates a new main frame with the given data. 
	 * 
	 * @param title the title of the frame.
	 * @param board the game board which is placed in the middle of the frame.
	 * @param toolbar the ToolBar which is placed at the head of the frame to modify and start the game.
	 */
	public MainFrame(String title,GameBoard board, GameToolBar toolbar){
		super(title);
		this.gameboard = board;
		centerComp = gameboard;
		this.toolbar = toolbar;
		toolbar.setOwner(this);
		setLayout(new BorderLayout());
		add(centerComp,BorderLayout.CENTER);
		add(this.toolbar,BorderLayout.NORTH);
		
		this.setSize(1400,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	
	/**
	 * Starts a new MainFrame. At the beginning, the user is asked whether to start with 
	 * Trisentis or the sigma-game on an arbitrary graph. 
	 * The corresponding ToolBars and GameBoards are created.
	 * 
	 * @param args not needed.
	 */
	public static void main(String[] args){
		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){}
		
		
		int n = JOptionPane.showOptionDialog(null, "Welchen Spieltyp möchten Sie analysieren? ",
				"Spiel-Auswahl",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, 
                new String[]{"\u03c3-Spiel auf Gittern (Trisentis)", "\u03c3-Spiel auf beliebigen Graphen", "Abbruch"}, "Trisentis");
		if (n == JOptionPane.YES_OPTION){
			Trisentis game = new Trisentis(4,4);
			TrisentisBoard board = new TrisentisBoard(game,GameBoard.SHOW_MODE);
			TriToolBar tools = new TriToolBar(board);
			new MainFrame("Trisentis spielen - Viel Spaß!", board,tools);
		}	
		else if (n == JOptionPane.NO_OPTION) {
			GraphBoard board = new GraphBoard(GameBoard.SHOW_MODE);
			GraphToolBar tools = new GraphToolBar(board);
			MainFrame frame = new MainFrame("Das \u03c3-Spiel spielen - Viel Spaß!", board,tools);
			tools.setOwner(frame);
		}
	}

	/**
	 * Adds the given panel in the center of the frame.
	 * 
	 * @param centerPanel new panel to put in the center, for analysis for example.
	 */
	public void putCentral(JComponent centerPanel) {
		this.remove(centerComp);
		centerComp = centerPanel;
		this.add(centerComp,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	
	/**
	 * Replaces the component in the south of the frame by the given component.
	 * 
	 * @param southPanel the new component.
	 */
	public void putSouth(JComponent southPanel){
		this.setVisible(false);
		if (southComp != null)
			this.remove(southComp);
		southComp = southPanel;
		this.add(southComp,BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	/**
	 * Sets a new ToolBar in the head of the frame.
	 * This method is only needed, if the game is changed from Trisentis to the graph game
	 * or the other way round.
	 * 
	 * @param tools the new ToolBar.
	 */
	public void setToolBar(GameToolBar tools) {	
		this.setVisible(false);
		this.remove(toolbar);
		this.toolbar = tools;
		this.add(toolbar,BorderLayout.NORTH);
		this.setVisible(true);
	}

	/**
	 * Removes the component in the south.
	 */
	public void removeAnalyse() {
		if (southComp != null){
			this.setVisible(false);
			this.remove(southComp);
			this.setVisible(true);
		}
	}
	

}

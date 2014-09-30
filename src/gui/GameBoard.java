package gui;

import game.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * The class represents a game board of a sigma-Game. 
 * How it is presented exactly must be defined in the subclass.
 * But this class provides general methods for different game modi.
 * 
 * @author Irene Thesing
 */
public abstract class GameBoard extends JPanel{
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = 1313480107369405318L;
	/**Color representing value 0 in the game.*/
	public static final Color VALUE_0 = Color.blue;
	/**Color representing value 1 in the game.*/
	public static final Color VALUE_1 = Color.yellow;
	
	
	/** In the game mode, the player is allowed to do moves on every node.
	 */
	public final static int GAME_MODE = 0;
	
	/** In the game mode, the player is allowed to do moves on every node. Aditionally there is a record panel, which shows a record of the moves.
	 */
	public final static int GAME_RECORD_MODE = 1;
	
	/**In the input mode, the player shall be allowed to give an input configuration by clicking on the nodes.*/
	public final static int INPUT_MODE = 2;
	
	/**In the show mode, nobody shall be able to change the view of the board. It ist used for showing solutions or kernel elements.*/
	public final static int SHOW_MODE = 3;
	
	
	/**The game which is the base for the game board.*/
	protected Game game;

	
	/**Describes which mode the game board has. That is if it shall be provide playing, giving an input or no operations.*/
	protected int mode;
	
	/**On this Panel the game board is displayed. Depending on the mode, some other panels are added.*/
	protected JComponent boardPanel;
	

	
	/**This is the panel, where the record is shown on. It is only needed in GAME_RECORD_MODE.*/
	protected JComponent recordPanel;
	
	/**This Button is only needed in INPUT_MODE, it starts the game.*/
	protected JButton inputReady;

    /** The second panel, where the record is shown on.*/
    protected JPanel panel2;
	
	/**
	 * Creates a new game board with the given game.
	 * After calling this method it is necessary to call the methods initBoardPanel() and initModous(startMode)
	 * with a fitting mode to complete the creation of the game board.
	 */
	public GameBoard(){
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
	}
	
	
	/**
	 * Initializes the game board or more exactly the additional components depending on the actual mode.
	 * For example in GAME_RECORD_MODE an additional record panel is added, therefore the method initRecordPanel must be overwritten.
	 * 
	 * @param startMode the new mode which shall be prepared.
	 */
	public void initMode(int startMode){
		this.setVisible(false);
		this.removeAll();
		switch (startMode){
		case GAME_RECORD_MODE:
			startGame(true);
			gameRecordView();
			break;
		case GAME_MODE:
			startGame(false);
			gameView();
			break;
		case INPUT_MODE:
			startInput();
			inputView();
		case SHOW_MODE: 
			this.setLayout(new BorderLayout());
			this.add(boardPanel,BorderLayout.CENTER);
			break;
		default: initSpecialMode(startMode);	
		}
		this.mode = startMode;
		this.setVisible(true);
	}
	
	/**
	 * Changes the view of the game board, so that it is divided into one part,
	 * where the player makes the moves and another, where the record is shown.
	 */
	protected void gameRecordView(){
		this.setVisible(false);
		this.removeAll();
		boardPanel.setEnabled(true);
		this.setLayout(new GridLayout(1,2,1,1));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(boardPanel);
		panel1.add(new JLabel("Spielfeld"),BorderLayout.NORTH);
		this.add(panel1);
		
		initRecordPanel();
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(recordPanel);
		panel2.add(new JLabel("Protokollanzeige"),BorderLayout.NORTH);
		this.add(panel2);
		this.setVisible(true);
	}
	
	/**
	 * Changes the view of the game board such that only the game board is shown.
	 */
	protected void gameView(){
		this.setVisible(false);
		this.removeAll();
		this.setLayout(new BorderLayout());
		boardPanel.setEnabled(true);
		this.add(boardPanel,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	/**
	 * Changes the view of the game board such that only the game board is shown.
	 * No Buttons can be pressed.
	 */
	protected void showView(){
		this.setVisible(false);
		this.removeAll();
		this.setLayout(new BorderLayout());
		this.add(boardPanel,BorderLayout.CENTER);
		boardPanel.setEnabled(false);
		this.setVisible(true);
	}
	
	/**
	 * Changes the view of the game board such that there is an additionally button
	 * under the game board to start the game with the input which is done.
	 */
	protected void inputView(){
		this.setVisible(false);
		this.removeAll();
		boardPanel.setEnabled(true);
		inputReady = new JButton("Spiel mit Eingabe starten");
		inputReady.addActionListener(new ActionListener(){
			//@Override
			public void actionPerformed(ActionEvent act) {
				int n = JOptionPane.showConfirmDialog(boardPanel,
					    "Soll ein Protokoll des Spiels geführt werden?",
					    "Protokollanzeige",
					    JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
					initMode(GAME_RECORD_MODE);
				else initMode(GAME_MODE);
			}		
		});
		this.setLayout(new BorderLayout());
		this.add(boardPanel,BorderLayout.CENTER);
		this.add(inputReady,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}


	/**
	 * Initializes the view of the game board.
	 */
	protected abstract void initBoardPanel();
	
	/**
	 * Initializes the view of the record board. It is needed if GAME_RECORD_MODE is active.
	 */
	protected abstract void initRecordPanel();
	
	/**
	 * If the subclass has some more possible mode values, they shall be dealed with in this method.
	 * Otherwise leave the method empty or throw some exception.
	 * 
	 * @param mode Some new mode values.
	 */
	protected abstract void initSpecialMode(int mode);
	
	/**
	 * Starts the game with or without record.
	 * Calls the method #startConGame to deal with the concrete game start in the subclasses.
	 * 
	 * @param recordActive whether the game shall be recorded.
	 */
	public void startGame(boolean recordActive){
		game = startConGame(recordActive);
		if (recordActive){
			this.mode = GAME_RECORD_MODE;
			gameRecordView();
		} else {
			this.mode = GAME_MODE;
			gameView();
		}
		
	}
	
	/**
	 * Starts the game with or without record - here all the things are dealt
	 * which depends on the concrete game, so dealt with in the subclasses.
	 * 
	 * @param recordActive whether the game shall be recorded.
	 */
	protected abstract Game startConGame(boolean recordActive);
	
	/**
	 * Starts the input mode.
	 * Calls the method #startConInput to deal with the concrete game start in the subclasses.
	 */
	public void startInput(){
			this.mode = INPUT_MODE;
			inputView();
			startConInput();
	}
	
	/**
	 * Here all the things are dealt
	 * which depends on the concrete game to start the input mode, so dealt with in the subclasses.
	 */
	protected abstract void startConInput();
	
	/**
	 * One node is clicked. So the corresponding move on the sigma-game is done.
	 * 
	 * @param nodeNumber number of clicked node.
	 */

	/**
	 * Updates a situation with the game configuration which is given by the actual state of the trisentis game.
	 * Updates also the record Panel if the mode is GAME_RECORD_MODE. The method is useful for GAME_MODE, GAME_RECORD_MODE
	 * or INPUT_MODE.
	 */
	public abstract void updateSituation();
	
	
	/**
	 * Updates a situation with the given configuration given as a list of boolean values, numbered in an array.
	 * This method is useful to show some solutions in SHOW_MODE.
	 * 
	 * @param showSit situation which shall be shown on the board.
	 */
	public abstract void updateSituation(boolean[] showSit);
	
	/**
	 * Returns the mode in which the game board is at the moment.
	 * 
	 * @return the actual mode of the game board, such as GAME_MODE for playing,INPUT_MODE or SHOW_MODE.
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Returns the game which is represented on the game board. Please only call this method if the game was started.
	 * 
	 * @return the game played on the game board.
	 */
	public Game getGame() {
		return game;
	}





}

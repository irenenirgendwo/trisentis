package gui;

import help.NewJToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;

/**
 * This class describes the ToolBar for setting options or drawing graphs for creating the game board.
 * Then it provides several options, like starting the game or the input mode. 
 * There is also can be chosen whether the game shall be recorded.
 * 
 * @author Irene Thesing
 *
 */
public abstract class GameToolBar extends NewJToolBar{
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = -6970689575341657486L;
	
	
	/**
	 *The corresponding game board. 
	 */
	protected GameBoard gameboard;
	
	/**The checkbox where the record function can be chosen.*/
	final protected JCheckBox recChecker;
	
	/**
	 * The main frame, where the game board and this toolbar are shown.
	 * It is necessary to address the center panel of it to put new components there.
	 */
	protected MainFrame owner;
	
	/**
	 * The toolbar to analyse the game, shown below the game when started.
	 */
	protected AnalyseToolBar analyse;
	
	/**
	 * Creates a new ToolBar for the game.
	 * It is necessary to call #setOwner() after creating a new toolbar.
	 * 
	 * @param board the game board, which can be addressed by the options on the toolbar.
	 */
	public GameToolBar(GameBoard board){
		super(JToolBar.HORIZONTAL);
		this.setFloatable(false);
		gameboard = board;
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
		setFloatable(false);
		initOptionPart();
		
		//Record possiblity 
	//	this.addSeparator();
		recChecker = new JCheckBox();
		recChecker.setText("Protokollanzeige");
		this.add(recChecker);
		this.addSeparator();
		
		JButton start = new JButton("Spiel starten");
		start.addActionListener(new ActionListener(){
			//@Override
			public void actionPerformed(ActionEvent act) {
				analyse = additionalStartAction();
				gameboard.startGame(recChecker.isSelected());
				analyse.showAnalyse(gameboard);
				owner.putSouth(analyse);
			}
		});
		this.add(start);
		
		JButton input = new JButton("Startkonfiguration eingeben");
		input.addActionListener(new ActionListener(){
			//@Override
			public void actionPerformed(ActionEvent act) {
				additionalInputAction();
				gameboard.startInput();
				owner.removeAnalyse();
			}
		});
		this.add(input);
		
		JButton help = new JButton("Hilfe");
		help.addActionListener(new ActionListener(){
			//@Override
			public void actionPerformed(ActionEvent act) {
				owner.removeAnalyse();
				new HelpFrame(getSubclassConstant());
			}
		});
		this.add(help);
		
		this.addSeparator();
		JButton change = new JButton("Spielwechsel");
		change.addActionListener(new ActionListener(){
			//@Override
			public void actionPerformed(ActionEvent act) {
				changeToOtherGame();
				owner.removeAnalyse();
			}
		});
		this.add(change);
		
		this.setVisible(true);
	}

	/**
	 * Returns the constant for the subclass to see different help frames.
	 * 
	 * @return subclass constant from the help frame.
	 */
	protected abstract int getSubclassConstant();

	/**
	 * Changes the whole board to the other game.
	 */
	protected abstract void changeToOtherGame();

	/**
	 * Initializes the property part of the toolbar, which depends on the concret game, so it 
	 * must be implemented in the subclass.
	 */
	protected abstract void initOptionPart();
	
	/**
	 * Sometimes it is necessary to do some changes on the game board
	 * if the game is started. Therefore this method is called while clicking on the start button first.
	 * 
	 * @return the ToolBar to do analyses of the game which shall be shown.
	 */
	protected abstract AnalyseToolBar additionalStartAction();
	/**
	 * Sometimes it is necessary to do some changes on the game board
	 * if the input mode is started. Therefore this method is called while clicking on the input button first.
	 */
	protected abstract void additionalInputAction();

	/**
	 * This method must be called after creating a new GameToolBar to make the connection
	 * to the gameboard, such that there can be made changes.
	 * 
	 * @param mainFrame the main frame where everything takes place.
	 */
	public void setOwner(MainFrame mainFrame) {
		owner = mainFrame;
	}

}

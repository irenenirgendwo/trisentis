package guitrisentis;

import game.BooleanMatrix;
import gui.GameBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import trisentis.Trisentis;

/**
 * This class represents the graphical representation of a game board 
 * for the Trisentis
 * game or the game on a grid. 
 * Here the clicks on fields are realized by clicks on panels.
 * A MouseListener on each field waits for clicks.
 * 
 * @author Irene Thesing
 *
 */
public class TrisentisBoard extends GameBoard {
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = 3436985704707142322L;
	
	/**
	 * In this mode the given configuration on the game board is shown as well as it appliance 
	 * to a Trisentis game board with an additional border.
	 */
	public static final int SHOW_APPLIANCE_MODE = 128;

	
	/**The Game which is shown.*/
	private Trisentis cont;
	
	
	/**
	 * Here all game fields are listed. It is useful to address them.
	 */
	private List<JPanel> cells = new ArrayList<JPanel>();

	
	/**List of cells, where the record is shown.*/
	private HashMap<JPanel,JPanel> recCells = new HashMap<JPanel,JPanel>();
	
	/**
	 * This Trisentis board is needed to show the appliance on a Trisentis game with added border.
	 * Only needed in SHOW_APPLIANCE_MODE.
	 */
	private TrisentisBoard borderPanel;

	
	/**
	 * Creates a new Trisentis game board.
	 * 
	 * @param trisentis the Trisentis game which is displayed.
	 * @param startMode the mode which the gameBoard shall have, GAME_MODE for playing or GAME_RECORD_MODE for playing with record
	 * for example.
	 */
	public TrisentisBoard(Trisentis trisentis, int startMode) {
		super();	
		this.cont = trisentis;
		game =cont;
		initBoardPanel();
		initMode(startMode);
	}
	
	/**
	 * Creates a new Trisentis game board which shows the given matrix.
	 * 
	 * @param toShow the matrix which shall be shown on the Trisentis game board.
	 */
	public TrisentisBoard(BooleanMatrix toShow) {
		super();	
		this.cont = new Trisentis(toShow.getNumRows(),toShow.getNumCols());
		game =cont;
		cont.setState(toShow.getMatrixAsBooleanArray());
		initBoardPanel();
		initMode(SHOW_MODE);
	}


	@Override
	protected void initBoardPanel() {
		cells.clear();
		//this.removeAll();
		int rows = cont.getRows();
		int cols = cont.getCols();
		
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(rows,cols));
		
		for (int i=0; i<rows*cols; i++) {
			final JPanel but = new JPanel();
			but.setBorder(BorderFactory.createLineBorder(Color.gray,1));
			if (cont.getValueAt(i))
				but.setBackground(VALUE_1);
			else but.setBackground(VALUE_0);
			cells.add(but);
			boardPanel.add(but);
		}
		boardPanel.setVisible(true);
	}
	
	@Override
	protected void initRecordPanel() {
		int rows = cont.getRows();
		int cols = cont.getCols();
		
		recordPanel = new JPanel();
		recordPanel.setLayout(new GridLayout(rows,cols));
		
		for (JPanel but: cells){
			JPanel rec = new JPanel();
			rec.setBackground(VALUE_0);
			rec.setBorder(BorderFactory.createLineBorder(Color.gray,1));
			recCells.put(but,rec);
			recordPanel.add(rec);
		}
		recordPanel.setVisible(true);
	}
	
	

	/* (non-Javadoc)
	 * @see gui.GameBoard#updateSituation()
	 */
	@Override
	public void updateSituation() {
		int i=0;
		for (JPanel but: cells){
			if (cont.getValueAt(i))
				but.setBackground(VALUE_1);
			else but.setBackground(VALUE_0);
			if (mode ==GAME_RECORD_MODE){
				JPanel rec = recCells.get(but);
				boolean value = cont.getRecord()[i];
				if (value)
					rec.setBackground(VALUE_1);
				else rec.setBackground(VALUE_0);
			}
			
			i++;
		}
		if (cont.isWon())
			JOptionPane.showMessageDialog(this,"Sie haben gewonnen.");
	}



	/* (non-Javadoc)
	 * @see gui.GameBoard#initSpecialMode(int)
	 */
	@Override
	protected void initSpecialMode(int mode) {
		if (mode == SHOW_APPLIANCE_MODE){
			applianceView(cont.getState());
		}
		else 
			throw new IllegalArgumentException("Mode is not allowed.");
	}

	/**
	 * Changes the view of the game board, so that it is divided into one part,
	 * one, where the game board with the configuration is shown and one where
	 * the appliance on a game board with an additional border is shown.
	 */
	@SuppressWarnings("static-access")
	private void applianceView(boolean[] configuration){
		this.setVisible(false);
		this.removeAll();
		this.updateSituation(configuration);
		boardPanel.setEnabled(false);
		this.setLayout(new GridLayout(1,2,1,1));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(boardPanel);
		panel1.add(new JLabel("Konfiguration"),BorderLayout.NORTH);
		this.add(panel1);
		
		//Initialize the second game board
		Trisentis borderTri = new Trisentis(cont.getRows()+2,cont.getCols()+2);
		borderPanel = new TrisentisBoard(borderTri,this.SHOW_MODE);
		borderPanel.updateSituation(cont.applySolWithBorder2(configuration));
		
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(borderPanel);
		panel2.add(new JLabel("Anwendung auf ein Trisentis-Spiel mit zugefügtem Rand"),BorderLayout.NORTH);
		this.add(panel2);
		this.setVisible(true);
	}
	

	@Override
	public void updateSituation(boolean[] showSit) {
		int i=0;
		for (JPanel but: cells){
			if (showSit[i])
				but.setBackground(VALUE_1);
			else but.setBackground(VALUE_0);
			i++;
		}
		if (this.mode == SHOW_APPLIANCE_MODE){
			borderPanel.updateSituation(cont.applySolWithBorder2(showSit));
		}
	}


	@Override
	protected Trisentis startConGame(boolean recordActive) {
		cont.setRecord(recordActive);		
		if (recordActive)
			this.gameRecordView();
		else this.gameView();
		//Buttons verändern
		int i=0;
		for (final JPanel but: cells){
			for (MouseListener listener: but.getMouseListeners())
				but.removeMouseListener(listener);
			but.addMouseListener(new FieldMouseListener(but,i));
			i++;
		}
		/*for (final JButton but: cells){
			for (ActionListener listener: but.getActionListeners())
				but.removeActionListener(listener);
			but.setActionCommand(Integer.toString(i));
			but.addActionListener(new ActionListener() {
				//@Override
				public void actionPerformed(ActionEvent act) {
					cont.play(Integer.parseInt(act.getActionCommand()));
					updateSituation();
				}
			});
			i++;
		}*/
		return cont;
	}
	
	
	@Override
	protected void startConInput() {
		//Buttons verändern
		int i=0;
		for (final JPanel but: cells){
			for (MouseListener listener: but.getMouseListeners())
				but.removeMouseListener(listener);
			but.addMouseListener(new FieldMouseListener(but,i));
			i++;
		}
		
	/*	for (final JButton but: cells){
			for (ActionListener listener: but.getActionListeners())
				but.removeActionListener(listener);
			but.setActionCommand(Integer.toString(i));
			but.addActionListener(new ActionListener() {
				//@Override
				public void actionPerformed(ActionEvent act) {
					cont.changeState(Integer.parseInt(act.getActionCommand()));
					if (but.getBackground() == VALUE_0)
						but.setBackground(VALUE_1);
						else but.setBackground(VALUE_0);
				}
			});
			i++;
		}*/
		
		
	}
	
	
	/**
	 * Starts a new TrisentisBoard for testing it
	 * in a game mode with record display.
	 * 
	 * @param args not needed.
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame("Trisentis");
		frame.setSize(800,600);
		frame.setLayout(new BorderLayout());
		Trisentis tri = new Trisentis(6,6);
		frame.add(new TrisentisBoard(tri,GameBoard.GAME_RECORD_MODE));
		frame.setVisible(true);
	}
	
	
	/**
	 * This MouseListener waits for click on the corresponding
	 * game field. Dependent from the actual mode
	 * it performes a move on the game board or an input
	 * making which changes the color of the field.
	 * It is connected to a number which characterizes the 
	 * number of the node in the Trisentis game.
	 * 
	 * @author Irene Thesing
	 *
	 */
	private class FieldMouseListener extends MouseAdapter{
		
		/**The field which shall be clicked,
		 * where the MouseListener takes place on.
		 */
		JPanel field;
		
		/**Number of the corresponding node in the Trisentis game.*/
		int number;
	
		
		/**
		 * Creates a new FieldMouseListener on the given field
		 * with the given number, which shall be the number
		 * of the corresponding node in the game.
		 * 
		 * @param ownfield The field which shall be clicked,
		 * where the MouseListener takes place on.
		 * @param fieldNumber Number of the corresponding node in the Trisentis game.
		 */
		public FieldMouseListener(JPanel ownfield, int fieldNumber){
			this.field = ownfield;
			this.number = fieldNumber;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (mode == GAME_MODE || mode == GAME_RECORD_MODE){
				cont.play(number);
				updateSituation();
			} else if (mode == INPUT_MODE){
				cont.changeState(number);
				if (field.getBackground() == VALUE_0)
					field.setBackground(VALUE_1);
				else field.setBackground(VALUE_0);
			}
			
		}
		
	}

}

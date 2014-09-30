package guigraph;

import gui.MSPOption;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * This class represents an option panel for the sigma-game on graphs.
 * It implements only the abstract methods of #MSPOption
 * and adds no additional functionalities.
 * 
 * 
 * @author Irene Thesing
 */
public class GraphMSPOption extends MSPOption {

	/**
	 * Generated id.
	 */
	private static final long serialVersionUID = 746120380807170794L;

	/**
	 * Creates a new option panel for the game on graphs. It calls the constructor of the superclass #MSPOption. 
	 * 
	 * @param typ the type of the option panel, i.e. whether it represents kernel or solution elements or both.
	 * @param owner the AnalyseToolBar which has started this panel. 
	 */
	public GraphMSPOption(int typ,GraphAnalyseToolBar owner) {
		super(typ,owner);
	}

	@Override
	protected void display(boolean[] newValue) {
		GraphAnalyseToolBar analyseTool = (GraphAnalyseToolBar)owner;
		GamingGraph gaming = analyseTool.getGameGraph();
		GraphBoard gameboard = new GraphBoard(GraphBoard.SHOW_MODE,gaming);
		gameboard.updateSituation(newValue);
		
		JFrame display = new JFrame("Resultat der Operation");
		display.setLayout(new BorderLayout());
		display.setSize(600,600);
		display.add(gameboard,BorderLayout.CENTER);
		display.setVisible(true);
	}

	@Override
	protected void initAdditionalAllActions() {	}

	
}

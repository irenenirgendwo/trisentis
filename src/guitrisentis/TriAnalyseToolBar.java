package guitrisentis;

import gui.AnalyseToolBar;
import gui.GameBoard;
import gui.MSPOption;
import gui.MainFrame;

import java.util.List;

import javax.swing.JComponent;

import trisentis.Trisentis;

/**
 * The ToolBar for beginning to analyze the Trisentis game.
 * It only implements the abstract methods of #AnalyseToolBar
 * to show Trisentis configurations.
 * 
 * @author Irene Thesing
 *
 */
public class TriAnalyseToolBar extends AnalyseToolBar{
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = -6281977546877003480L;
	
	/**Trisentis game where we work on.*/
	private Trisentis trisentis;

	/**
	 * Creates a new Trisentis analyse toolbar with the given game tri
	 * and in the MainFrame own. It has Buttons to analyse the Trisentis game.
	 * 
	 * @param tri Trisentis game
	 * @param own the frame where the ToolBar is placed.
	 */
	public TriAnalyseToolBar(Trisentis tri, MainFrame own) {
		super(tri, own);
		this.trisentis = tri;
	}
	
	/**
	 * Creates a new Trisentis analyse toolbar in the MainFrame own. 
	 * It has Buttons to analyse the Trisentis game.
	 * 
	 * @param own the frame where the ToolBar is placed.
	 */
	public TriAnalyseToolBar(MainFrame own) {
		super(own);
	}

	@Override
	public MSPOption initOptionPanel(int type) {
		return new TriMSPOption(type,this);
	}

	@Override
	public JComponent[] initSolutionPanels(List<boolean[]> solutions) {
		int length = solutions.size();
		JComponent[] compList = new JComponent[length];
		for (int number =0; number<length;number++){
			TrisentisBoard board = new TrisentisBoard(trisentis,GameBoard.SHOW_MODE);
			board.updateSituation(solutions.get(number));
			compList[number] = board;
		}
		return compList;
	}

	
	
}

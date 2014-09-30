package guigraph;

import gui.AnalyseToolBar;
import gui.MSPOption;
import gui.MainFrame;

import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JComponent;

import com.mxgraph.swing.mxGraphComponent;


/**
 * This class is the version of the #AnalyseToolBar for graphs.
 * It initializes the view of some configurations 
 * with the help of a #GameGraph, where the connection
 * between graph on the GUI and game is stored.
 * 
 * @author Irene Thesing
 *
 */
public class GraphAnalyseToolBar extends AnalyseToolBar {
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = 4001850032961575473L;
	
	/**
	 * The connection to the game which is analyzed.
	 */
	private GamingGraph gameGraph;


	/**
	 * Returns the #GamingGraph which stores the connection between
	 * GUI and #Game.
	 * 
	 * @return the connection between game and GUI.
	 */
	public GamingGraph getGameGraph() {
		return gameGraph;
	}

	/**
	 * Creates a new ToolBar on the given MainFrame.
	 * 
	 * @param own where the ToolBar is shown.
	 */
	public GraphAnalyseToolBar(MainFrame own) {
		super(own);
	}
	
	/**
	 * Creates a new ToolBar on the given MainFrame
	 * with a game which is given through the #GamingGraph.
	 * 
	 * @param gaming the connection to a game.
	 * @param own where the ToolBar is shown.
	 */
	public GraphAnalyseToolBar(GamingGraph gaming, MainFrame own) {
		super(gaming.getGame(),own);
		this.gameGraph =gaming;
	}
	
	@Override
	public MSPOption initOptionPanel(int type) {
		return new GraphMSPOption(type,this);
	}

	@Override
	public JComponent[] initSolutionPanels(List<boolean[]> solutions) {
		GraphBoard graphboard = (GraphBoard)gameboard;
		gameGraph = graphboard.getGameGraph();
		int length = solutions.size();
		JComponent[] entryPanels = new JComponent[length];
		for (int number = 0;number<length;number++){
				//neuen Graphen für die Anzeige erzeugen
				GamingGraph newGameGraph = gameGraph.copy();
				//entsprechend einfärben
				newGameGraph.refreshGame(solutions.get(number));
				//Verdoppelung, weil sonst die Anzeige nicht funktioniert, keine Ahnung warum das nötig ist.
				newGameGraph = gameGraph.copy();
				newGameGraph.refreshGame(solutions.get(number));
				mxGraphComponent comp = new mxGraphComponent(newGameGraph.getMxGraph());
				int width = comp.getPreferredSize().width;
				int height = comp.getSize().height;
				for (int i=0;i<2000;i+=120)
					if (width > 300+i || height > 50+i/2){
						comp.zoomOut();
				}
				for (MouseListener active: comp.getGraphControl().getMouseListeners()){
					comp.getGraphControl().removeMouseListener(active);
				}
				//comp.setZoomPolicy(comp.ZOOM_POLICY_WIDTH);
				entryPanels[number] = comp;
		}
		return entryPanels;
	}

}

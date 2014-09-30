package guigraph;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMouseAdapter;
import com.mxgraph.view.mxGraph;


/**
 * This MouseListener reacts only on clicks on vertices of the basic graph.
 * A click on a vertex causes that all neighbored vertices change the color.
 * So the move corresponding to this click is executed.
 * Therefore the connection over the #GamingGraph to the #Game is needed.
 * The #GamingGraph is used to update the game and the graphics.
 * 
 * @author Irene Thesing
 */
public class GameMouseListener extends mxMouseAdapter{
	
	/**The component where the graph is placed.*/
	private mxGraphComponent comp;
	
	/**The graph which makes the connection between gaming logik and gui. */
	private GamingGraph gameGraph;
	
	/**
	 * The graph in the gui, which is used to create the game and the corresponding gaming graph. 
	 */
	private mxGraph graph;
	
	
	/** If the moves shall be logged. If it is true, the record is shown on a seperate graph.*/
	private boolean recordActive;
	
	/**Game board, where the mouse is active.*/
	private GraphBoard owner;
	
	/**
	 * Creates a new GameMouseListener, which makes a move by clicking on a node.
	 * In this method the game is constructed out of the graph and the connection
	 * between both is stored in the #GamingGraph #gameGraph.
	 * 
	 * @param owner the game board where the game takes place and the mouse listener shall change the fields.
	 * @param component the graph component where the game is shown.
	 * @param recordOn whether a record panel shall be shown.
	 */
	public GameMouseListener(GraphBoard owner, mxGraphComponent component, boolean recordOn){
		this.owner = owner;
		recordActive = recordOn;
		comp = component;
		graph = comp.getGraph();
		gameGraph= new GamingGraph(graph);
		
		//Situation updaten und alles richtig einfärben
		gameGraph.getGame().setRecord(recordActive);
		if (recordActive){
			gameGraph.getGame().setRecord(true);
			GamingGraph recordGraph = gameGraph.copy();
			recordGraph.refreshGame(gameGraph.getGame().getRecord()); // auf 0 setzen
		}
		gameGraph.refreshGame();
	}
	
	/**
	 * Creates a new GameMouseListener, which makes a move by clicking on a node.
	 * In this method the game is constructed out of the graph and the connection
	 * between both is stored in the #GamingGraph #gameGraph.
	 * 
	 * @param owner the game board where the game takes place and the mouse listener shall change the fields.
	 * @param graphComponent the graph component where the game is shown.
	 * @param recordOn whether a record panel shall be shown.
	 * @param state a starting state for the game.
	 */
	public GameMouseListener(GraphBoard owner, mxGraphComponent graphComponent,
			boolean recordOn, boolean[] state) {
		this(owner, graphComponent,recordOn);
		this.gameGraph.getGame().setState(state);
		gameGraph.refreshGame();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//Hier sollen die Farben wechseln
		Object cell = comp.getCellAt(e.getX(), e.getY());
		//wenn an der Stelle ein Knoten ist
	    if (gameGraph.getMxGraph().getModel().isVertex(cell)){
	    	//Spielzug im Graphen realisieren
	    	gameGraph.play(cell);
	    	owner.updateSituation(); //refreshGame();
	    	if (gameGraph.getGame().isWon()){
	    		JOptionPane.showMessageDialog(comp,"Sie haben gewonnen.");
	    	}
	    }
	}
	

	/**
	 * Returns the game graph where the mouse listener works on.
	 * 
	 * @return game graph.
	 */
	public GamingGraph getGameGraph() {
		return gameGraph;
	}
	
	
}

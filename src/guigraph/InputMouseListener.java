package guigraph;

import java.awt.event.MouseEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMouseAdapter;
import com.mxgraph.view.mxGraph;


/**
 * This MouseListener reacts only on clicks on vertices of the basic graph.
 * A click on a vertex causes that it changes the color. So an input value for starting the game can be 
 * clicked together.
 * This MouseListener creates a game out of the given graph and stores it in the #GamingGraph #gameGraph.
 * It is used to put the input also in the game logic.
 * 
 * @author Irene Thesing
 */
public class InputMouseListener extends mxMouseAdapter{
	
	/**The component where the graph is placed.*/
	private mxGraphComponent comp;
	
	/**The graph which makes the connection between gaming logik and gui. */
	private GamingGraph gameGraph;
	
	/**
	 * The graph in the GUI, 
	 * which is used to create the game and the corresponding gaming graph.
	 */
	private mxGraph graph;
	
	/**
	 * Creates a new InputMouseListener, which creates also a game out of the graph
	 * on the given component.
	 * A click changes the color of the vertex, which was chosen.
	 * 
	 * @param component the component where the graph for the game is placed on.
	 */
	public InputMouseListener(mxGraphComponent component){
		comp = component;
		graph = comp.getGraph();
		gameGraph= new GamingGraph(graph);
		gameGraph.refreshGame();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//Hier sollen die Farben wechseln
		Object cell = comp.getCellAt(e.getX(), e.getY());
		//wenn an der Stelle ein Knoten ist
	    if (gameGraph.getMxGraph().getModel().isVertex(cell)){
	    	//Spielzug im Graphen realisieren
	    	gameGraph.changeState(cell);
	    }
	}

	/**
	 * Returns the actual state of the game.
	 * 
	 * @return the actual state of the game.
	 */
	public boolean[] getState() {
		return this.gameGraph.getGame().getState();
	}
	
	
}
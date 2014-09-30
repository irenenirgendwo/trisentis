package guigraph;

import game.Game;
import game.GameGraph;

import java.util.HashMap;
import java.util.LinkedList;


import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

/**
 * Encapsulates the connection between the graph on which the game works and the game logic.
 * Therefore a map between nodes of the graph and integer values which represent them in the game is saved.
 * Furthermore, there are methods to do a move on the game board, #play(Object cell)
 * or to change the state for making any input, #changeState(Object cell).
 * This methods are called from the different MouseListeners on the #GraphBoard.
 * Here they execute the corresponding methods in #Game.
 * 
 * @author Irene Thesing
 *
 */
public class GamingGraph {
	
	/**
	 * The graph which is displayed and where the vertices and edges are stored as objects.
	 * It is used to create the game.
	 */
	private mxGraph presGraph;
	
	/**The list from cells to integers, which are vertices in the Game. 
	 * This is a connection between GUI and playing logic.*/
	private HashMap<Object,Integer> vertList ;

	/**Game which is presented.*/
	private Game game;
	
	
	
	
	
	/**
	 * Creates a new game out of a given graph of the GUI.
	 * It calculates the adjacency lists and creates the #GameGraph for the game.
	 * It stores the mapping between cells on the GUI and vertices in the #Game.
	 * 
	 * @param graph basic graph where the game should be played on.
	 */
	public GamingGraph(mxGraph graph){
		presGraph = graph;
		
		//GameGraph erzeugen
		Object[] vertices  = graph.getChildVertices(graph.getDefaultParent());
		int size = vertices.length;
		//System.out.println(size);
	
		//Verbindungsliste von Knoten zu Integern erzeugen
		vertList = new HashMap<Object,Integer>();
		int i=0;
		for (Object vertex: vertices){
			vertList.put(vertex, i);
			i++;
		}
		
		//Adjazenzlisten erzeugen
		HashMap<Integer,LinkedList<Integer>> adjazenz = new HashMap<Integer,LinkedList<Integer>>();
		
		for (Object vertex: vertices){
			Object[] cell = {vertex}; 
			//Alle Kanten zu dem Knoten
			Object[] edges = graph.getAllEdges(cell);
			//Alle über diese Kanten benachbarten Knoten sollen auch im Graphen benachbart sein.
			Object[] opposites = graph.getOpposites(edges, vertex);
			LinkedList<Integer> neighbours = new LinkedList<Integer>();
			for (Object op: opposites){
				neighbours.add(vertList.get(op));
			}
			adjazenz.put(vertList.get(vertex), neighbours);
		}
		game = new Game(new GameGraph(size,adjazenz));
	}

	/**
	 * Returns the graph for presentation of the game.
	 * 
	 * @return the graph for presentation
	 */
	public mxGraph getMxGraph() {
		return presGraph;
	}
	
	/**
	 * Sets the graph for presentation of the game
	 * 
	 * @param newGraph the graph for presentation
	 */
	public void setMxGraph(mxGraph newGraph) {
		presGraph = newGraph;
	}

	/**
	 * Returns the mapping between nodes of the graph and the integers presenting this nodes in game logic.
	 * 
	 * @return the mapping between nodes of the graph and the integers presenting this nodes in game logic.
	 */
	public HashMap<Object, Integer> getVertList() {
		return vertList;
	}


	/**
	 * Returns the game (game logic).
	 * 
	 * @return the game (game logic).
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Shows the given boolean values in the graph as different colours
	 * 
	 * @param show the values to the nodes which shall be shown
	 */
	public void refreshGame(boolean[] show){
		for (Object vertex: vertList.keySet()){
			Object[] vert = {vertex};
			presGraph.getModel().setVisible(vertex,false);
			//wenn an dem Knoten wahr ist, gelb, sonst blauen Hintegrund
			if (show[vertList.get(vertex)]){
				presGraph.setCellStyle(mxConstants.STYLE_FILLCOLOR+"=yellow",vert);
			}
			else
				presGraph.setCellStyle(mxConstants.STYLE_FILLCOLOR+"=blue",vert);
			presGraph.getModel().setVisible(vertex,true);
		}
		presGraph.repaint();
		
	}

	/**
	 * Refreshes the game picture with the actual state of the game.
	 */
	public void refreshGame() {
		this.refreshGame(game.getState());
	}
	
	/**
	 * Returns the record graph corresponding to this graph.
	 */
	public mxGraph returnRecordGraph(){
		if (game.isRecordActive()){
			this.refreshGame(game.getRecord());
			//System.out.println("Here is record Active");
		}
		mxGraph newgraph = GamingGraph.copyGraph(presGraph); 
		this.refreshGame();
		return newgraph;
	}
	
	
	/**
	 * Copies a gaming graph, so that it can be used for other colors of the vertices.
	 * 
	 * @return new gaming graph with same components. The color etc. shall be independent from old object.
	 */
	public GamingGraph copy(){
		mxGraph newgraph = GamingGraph.copyGraph(presGraph); 
		return new GamingGraph(newgraph);
	}
	
	
	/**
	 * Returns a copy of the given graph.
	 * 
	 * mxGraph does not support cloning, so this method can be used instead.
	 * Copies all cells with id, style and value to the returned graph.
	 * 
	 * @param graph the graph that is going to be copied
	 * @return a copy of the given graph
	 */
	public static mxGraph copyGraph(mxGraph graph){
		Object parent = graph.getDefaultParent();
		mxGraphModel model = (mxGraphModel) graph.getModel();
		Object[] nodeCells = mxGraphModel.getChildVertices(model, parent);
		Object[] edgeCells = mxGraphModel.getChildEdges(model, parent);
		
		//initializing
		mxGraph copy = new mxGraph(graph.getStylesheet());
		parent = copy.getDefaultParent();
		
		//copying nodes
		for(int i = 0; i < nodeCells.length; i++){
			if(nodeCells[i] instanceof mxCell){
				mxCell node = (mxCell) nodeCells[i];
				copy.insertVertex(parent, node.getId(), node.getValue(), node.getGeometry().getX(), node.getGeometry().getY(), node.getGeometry().getWidth(), node.getGeometry().getHeight(), node.getStyle());
			}
		}
		
		model = (mxGraphModel) copy.getModel();
		//copying edges
		for(int i = 0; i< edgeCells.length; i++){
			if(edgeCells[i] instanceof mxCell){
				mxCell edge = (mxCell) edgeCells[i];
				Object source = model.getCell(edge.getSource().getId());
				Object target = model.getCell(edge.getTarget().getId());
				copy.insertEdge(parent, edge.getId(), edge.getValue(), source, target, edge.getStyle());
			}
		}
		
		return copy;
	}

	/**
	 * Gives a game move made on a cell to the game and plays it there.
	 * Then it refreshes the display of the game.
	 * 
	 * @param cell the cell where the move shall be made.
	 */
	public void play(Object cell) {
		game.play(vertList.get(cell));
		refreshGame();
	}

	/**
	 * Changes the state of the given cell and saves it in the game.
	 * 
	 * @param cell the cell which shall change its state.
	 */
	public void changeState(Object cell) {
    	int i = vertList.get(cell);
    	game.changeState(i);
    	refreshGame();
	}
	

}
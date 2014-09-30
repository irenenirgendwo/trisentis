package guigraph;

import game.Game;
import gui.GameBoard;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;


/**
 * This class provides the game board for arbitrary graphs. 
 * There are also drawing modes, 
 * where new nodes and edges can be added to the game board.
 * It works with different MouseListeners.
 * 
 * @author Irene Thesing
 *
 */
public class GraphBoard extends GameBoard {


	/**
	 * Id.
	 */
	private static final long serialVersionUID = -4023448508889910413L;
	
	/**If this mode is active, 
	 * it is possible to draw new vertices by clicking on the canvas.*/
	public static final int DRAW_VERTEX_MODE = 42;
	/**If this mode is active, it is possible to draw new edges by pressing on the first
	 * vertex and releasing on the second.*/
	public static final int DRAW_EDGE_MODE = 43;
	
	/**With this mode it is possible to delete nodes from the canvas.*/
	public static final int DELETE_MODE = 44;
	
	/**This mode helps to move vertices to other places, the edges are updated.*/
	public static final int MOVE_VERTEX_MODE = 45;
	
	
	/**The component where the graph is shown and can be modified.*/
	private mxGraphComponent graphComp;
	
	/**The component where the graph is shown and the moves where done are logged.*/
	private mxGraphComponent recordComp;
	
	/**This is the connection between game logic and GUI.*/
	private GamingGraph gameGraph;
	
	/**Stylesheet for nodes (they shall look nice).*/
	private mxStylesheet stylesheet;
	
	/**The MouseListener which is active and performs 
	 * the corresponding actions.*/
	private MouseListener activeMouse;
	
	
	
	/**
	 * Creates a new game board for the sigma game on graphs, 
	 * beginning with two vertices.
	 * 
	 * @param startMode the mode where the game board shall be at the beginning.
	 */
	public GraphBoard(int startMode) {
		super();
		graphComp = new mxGraphComponent(new mxGraph());
		initStartGraph();
		initBoardPanel();
		initMode(startMode);
	}
	
	/**
	 * Creates a new game board for the sigma game on graphs, beginning with the given graph.
	 * 
	 * @param startMode the mode where the game board shall be at the beginning.
	 * @param gaming the graph where we play the game on, 
	 * connecting the game and the graph on the gui.
	 */
	public GraphBoard(int startMode, GamingGraph gaming) {
		super();
		gameGraph = gaming;
		graphComp = new mxGraphComponent(gameGraph.getMxGraph());
		initBoardPanel();
		initMode(startMode);
	}
	
	
	/**
	 * Initalizes a starting graph with two nodes and sets the graph style with round nodes as wished.
	 */
	private void initStartGraph(){
	    mxGraph graph = graphComp.getGraph();
		Object parent = graph.getDefaultParent();
		
		//Style setzen: Kreise und Kanten ohne Pfeile
		stylesheet = graph.getStylesheet();
		Map<String,Object> vertexStyle = stylesheet.getDefaultVertexStyle();
		Map<String, Object> edgeStyle = stylesheet.getDefaultEdgeStyle();
		vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		vertexStyle.put(mxConstants.STYLE_OPACITY, 50);
		vertexStyle.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
		vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "blue");
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
		stylesheet.setDefaultVertexStyle(vertexStyle);
		stylesheet.setDefaultEdgeStyle(edgeStyle);

		graph.getModel().beginUpdate();
		try
		{
			Object v1 = graph.insertVertex(parent, null, "", 20, 20, 30,
					30);
			Object v2 = graph.insertVertex(parent, null, "", 240, 150,
					30, 30);
			graph.insertEdge(parent, null, "", v1, v2,"endArrow=none");
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		drawCircle(4);
	}
	
	@Override
	protected void initBoardPanel() {
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(1,1));
		boardPanel.add(graphComp);
		boardPanel.setVisible(true);
		changeMouseListener(null);
	}

	@Override
	protected void initRecordPanel() {
		recordPanel= new JPanel();
		recordPanel.setLayout(new GridLayout(1,1));
		
		mxGraph recordGraph = GamingGraph.copyGraph(graphComp.getGraph()); 
		recordComp = new mxGraphComponent(recordGraph);
		recordPanel.add(recordComp);
		recordPanel.setVisible(true);
	}

	@Override
	protected void initSpecialMode(int mode) {
		this.setLayout(new GridLayout(1,1));
		this.add(boardPanel);
		switch(mode){
		case DRAW_VERTEX_MODE:
			changeMouseListener(new VertexMouseListener(graphComp));
			break;
		case DRAW_EDGE_MODE:
			changeMouseListener(new EdgeMouseListener(graphComp));
			break;
		case MOVE_VERTEX_MODE:
			changeMouseListener(new MoveMouseListener(graphComp));
			break;	
		case DELETE_MODE:
			changeMouseListener(new DeleteMouseListener(graphComp));
			break;
		default:
			throw new IllegalArgumentException("Mode is not allowed.");
		}
		this.setVisible(true);
	}
	
	
	/**
	 * Changes the active MouseListener on the graph control.
	 * 
	 * @param newMouse the new mouse listener which shall be active
	 */
	private void changeMouseListener(MouseListener newMouse){
		//removes all active Mouse listeners from graph component.
		for (MouseListener active: graphComp.getGraphControl().getMouseListeners()){
			this.graphComp.getGraphControl().removeMouseListener(active);
		}
		//sets the new one
		activeMouse = newMouse;
		this.graphComp.getGraphControl().addMouseListener(activeMouse);
	//	System.out.println("MouseListener " + newMouse.getClass());
	}


	@Override
	public void updateSituation() {
		this.setVisible(false);

		GameMouseListener gameMouse = (GameMouseListener) activeMouse;
		gameGraph = gameMouse.getGameGraph();
		gameGraph.refreshGame();
		
		if (mode == GAME_RECORD_MODE){
			mxGraph recGraph = gameGraph.returnRecordGraph();
			recordComp  = new mxGraphComponent(recGraph);
			recordComp.repaint();
			MouseListener[] mouses= recordComp.getMouseListeners();
			for (MouseListener mouse: mouses){
				recordComp.removeMouseListener(mouse);
			}
		
			recordPanel.removeAll();
			recordPanel.add(recordComp);
			recordPanel.setVisible(true);

			panel2.remove(recordPanel);
			panel2.removeAll();
			panel2.add(new JLabel("Protokollanzeige"),BorderLayout.NORTH);
		//	panel2.add(recordComp);
			mxGraphComponent newComp = new mxGraphComponent(recGraph);
			newComp.setEnabled(false);
			panel2.add(newComp);//new mxGraphComponent(recGraph));
			panel2.repaint();
			panel2.setEnabled(false);
		}
		this.setVisible(true);
	}

	@Override
	public void updateSituation(boolean[] showSit) {
		gameGraph.refreshGame(showSit);
	}

	@Override
	protected Game startConGame(boolean recordActive) {
		if (mode == INPUT_MODE){
			InputMouseListener inputMouse = (InputMouseListener)activeMouse;
			boolean[] state = inputMouse.getState();
			GameMouseListener mouse = new GameMouseListener(this,graphComp,recordActive,state);
			changeMouseListener(mouse);
		} else {
			GameMouseListener mouse = new GameMouseListener(this,graphComp,recordActive);
			changeMouseListener(mouse);
		}
		GameMouseListener gameMouse = (GameMouseListener) activeMouse;
		gameGraph = gameMouse.getGameGraph();
		return this.gameGraph.getGame();
	}

	@Override
	protected void startConInput() {
		changeMouseListener(new InputMouseListener(graphComp));	
	}

	/**
	 * Returns the graph component where we work on.
	 * 
	 * @return the graph component which forms the board.
	 */
	public mxGraphComponent getGraphComponent() {
		return graphComp;
	}
	
	/**
	 * Returns the gameGraph belonging to the game logic.
	 * 
	 * @return the gameGraph which is shown.
	 */
	public GamingGraph getGameGraph() {
		return gameGraph;
	}


	
	
	/**
	 * Draws a path as a new graph with the given number of nodes.
	 * 
	 * @param length number of nodes which shall be in the path.
	 */
	public void drawPath(int length){
		mxGraph graph = graphComp.getGraph();
		Object[] vertices  = graph.getChildVertices(graph.getDefaultParent());
		Object[] edges = graph.getAllEdges(vertices);
		graph.removeCells(edges);
		graph.removeCells(vertices);
		Object parent = graph.getDefaultParent();
		  graph.getModel().beginUpdate();
		  try
			{
			  Object[] cells = new Object[length];
			  for (int i=0;i<length;i++){
				  cells[i] = graph.insertVertex(parent, null, "", 20+i*50, 20, 30, 30);
			  }
			  for (int i=1;i<length;i++){
				  graph.insertEdge(parent, null, "", cells[i-1], cells[i],"endArrow=none");
			  }
			}
			finally
			{
				graph.getModel().endUpdate();
			}
			changeMouseListener(new MoveMouseListener(graphComp));
	}
	
	/**
	 * Draws a circle as a new graph with the given number of nodes.
	 * 
	 * @param length number of nodes which shall be in the circle.
	 */
	public void drawCircle(int length){
		mxGraph graph = graphComp.getGraph();
		Object[] vertices  = graph.getChildVertices(graph.getDefaultParent());
		Object[] edges = graph.getAllEdges(vertices);
		graph.removeCells(edges);
		graph.removeCells(vertices);
		  changeMouseListener(new MoveMouseListener(graphComp));
		  Object parent = graph.getDefaultParent();
		  graph.getModel().beginUpdate();
		  try
			{
			  Object[] cells = new Object[length];
			  double bogenschritt = 2*Math.PI/length;
			  for (int i=0;i<length;i++){
				  double sinus = Math.sin(bogenschritt*i);
				  double cosinus = Math.cos(bogenschritt*i);
				  cells[i] = graph.insertVertex(parent, null, "", 180+(int)(150*sinus),  180+(int)(150*cosinus), 30, 30);
			  }
			  for (int i=1;i<length;i++){
				  graph.insertEdge(parent, null, "", cells[i-1], cells[i],"endArrow=none");
			  }
			  graph.insertEdge(parent, null, "", cells[0], cells[length-1],"endArrow=none");
			}
			finally
			{
				graph.getModel().endUpdate();
			}
			this.repaint();
	}

	/**
	 * Draws a new triangle Trisentis game field where each middle node has six neighbors.
	 * 
	 * @param height the height of the triangle.
	 */
	public void drawTriangle(int height){
		mxGraph graph = graphComp.getGraph();
		Object[] vertices  = graph.getChildVertices(graph.getDefaultParent());
		Object[] edges = graph.getAllEdges(vertices);
		graph.removeCells(edges);
		graph.removeCells(vertices);
		  changeMouseListener(new MoveMouseListener(graphComp));
		  Object parent = graph.getDefaultParent();
		  graph.getModel().beginUpdate();
		  try
			{
			  int start = height*30;
			  Object[] newrow = new Object[0];
			  for (int row= 0;row<height;row++){
				  Object[] oldrow = newrow;
				  newrow = new Object[row+1];
				  for (int i=0;i<=row;i++){
					  newrow[i] = graph.insertVertex(parent, null, "", start-row*30+i*60, 20+row*50, 30, 30);
				  }
				  for (int i=0;i<row;i++){
					  graph.insertEdge(parent, null, "", newrow[i], newrow[i+1],"endArrow=none");
					  graph.insertEdge(parent, null, "", newrow[i], oldrow[i],"endArrow=none");
					  graph.insertEdge(parent, null, "", newrow[i+1], oldrow[i],"endArrow=none");
				  }
			  }
			 
			}
			finally
			{
				graph.getModel().endUpdate();
			}
	}
	
	
	/**
	 * Draws a new complete binary test.tree as a graph.
	 * 
	 * @param height the height of the test.tree. Shall be one or bigger.
	 */
	public void drawBinaryTree(int height){
		mxGraph graph = graphComp.getGraph();
		Object[] vertices  = graph.getChildVertices(graph.getDefaultParent());
		Object[] edges = graph.getAllEdges(vertices);
		graph.removeCells(edges);
		graph.removeCells(vertices);
		  changeMouseListener(new MoveMouseListener(graphComp));
		  Object parent = graph.getDefaultParent();
		  graph.getModel().beginUpdate();
		  try
			{
			  int start = (int)Math.pow(2,height-1)*20;
			  int startvalue = start*2;
			  int pointdif = start*2;
			  int leftmove = 0;
			  Object[] newrow = new Object[0];
			  for (int row= 0;row<height;row++){
				  Object[] oldrow = newrow;
				  int rowlength = (int)Math.pow(2,row);
				  newrow = new Object[rowlength];
				  leftmove = leftmove + (int)(start/(Math.pow(2,row)));
				  for (int i=0;i<rowlength;i++){
					  newrow[i] = graph.insertVertex(parent, null, "", startvalue-leftmove+i*pointdif, 20+row*50, 30, 30);
				  }
				  pointdif = pointdif/2;
				  for (int i=1;i<rowlength;i+=2){
					  graph.insertEdge(parent, null, "", newrow[i], oldrow[i/2],"endArrow=none");
					  graph.insertEdge(parent, null, "", newrow[i-1], oldrow[i/2],"endArrow=none");
				  }
			  }
			 
			}
			finally
			{
				graph.getModel().endUpdate();
			}
	}
	
	
	/**
	 * Draws a new complete 3-test.tree as a graph.
	 * 
	 * @param height the height of the test.tree. Shall be one or bigger.
	 */
	public void drawTertiaryTree(int height){
		mxGraph graph = graphComp.getGraph();
		Object[] vertices  = graph.getChildVertices(graph.getDefaultParent());
		Object[] edges = graph.getAllEdges(vertices);
		graph.removeCells(edges);
		graph.removeCells(vertices);
		  changeMouseListener(new MoveMouseListener(graphComp));
		  Object parent = graph.getDefaultParent();
		  graph.getModel().beginUpdate();
		  try
			{
			  int start = (int)Math.pow(3,height-1)*15;
			  int startvalue = 3*start/2;
			  int pointdif = 3*start;
			  int leftmove = 0;
			  Object[] newrow = new Object[0];
			  for (int row= 0;row<height;row++){
				  Object[] oldrow = newrow;
				  int rowlength = (int)Math.pow(3,row);
				  newrow = new Object[rowlength];

				  for (int i=0;i<rowlength;i++){
					  newrow[i] = graph.insertVertex(parent, null, "", startvalue-leftmove+i*pointdif, 20+row*50, 30, 30);
				  }
				  pointdif = pointdif/3;
				  leftmove = leftmove + (int)(start/(Math.pow(3,row)));
				  for (int i=1;i<rowlength;i+=3){
					  graph.insertEdge(parent, null, "", newrow[i+1], oldrow[i/3],"endArrow=none");
					  graph.insertEdge(parent, null, "", newrow[i], oldrow[i/3],"endArrow=none");
					  graph.insertEdge(parent, null, "", newrow[i-1], oldrow[i/3],"endArrow=none");
					  
				  }
			  }
			 
			}
			finally
			{
				graph.getModel().endUpdate();
			}
	}
	
	/**
	 * Starts a new game on two vertices,
	 * for testing the class.
	 * 
	 * @param args not needed.
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame("Gisentis");
		frame.setSize(1200,600);
		frame.setLayout(new BorderLayout());
		frame.add(new GraphBoard(GraphBoard.GAME_RECORD_MODE));
		frame.setVisible(true);
	}
	
}

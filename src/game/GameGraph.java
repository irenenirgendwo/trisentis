package game;

import java.util.HashMap;
import java.util.LinkedList;



/**
 * The class represents an (undirected) graph.
 * The nodes are numbered and called with integers beginning with zero.
 * The edges are represented by a BooleanMatrix and an adjazenzlist stored 
 * by a HashMap where every Node has a list with its neighbours.
 * For creating a new #Game the GameGraph is needed.
 * 
 * @author Irene Thesing
 */
public class GameGraph {
	
	/**
	 * Saves the number of nodes. 
	 * The corresponding integer also characterizes one node, so the nodes are numbered from 0 to countKnots-1.
	 */
	private int countKnots;
	
	/** 
	 * A matrix with boolean values, which saves the connections between the knots as a matrix for calculations.
	 */
	private BooleanMatrix adjMatrix;
	
	/**
	 * Here the connections between the knots are saved as adjazenzlists.
	 */
	private HashMap<Integer,LinkedList<Integer>> adjList;
	
	/**
	 * Creates a new graph with the given number of nodes and no edges.
	 * 
	 * @param knots number of nodes for the graph.
	 */
	public GameGraph(int knots){
		this.countKnots =knots;
	//	this.connections = new boolean[knots][knots];
		this.adjList = new HashMap<Integer,LinkedList<Integer>>();
		
	}
	
	/**
	 * Creates a new GameGraph with the given count of knots and the
	 * connections given by the matrix displayed in connect.
	 * The matrix must have the dimension knots x knots.
	 * 
	 * @param knots knots of the graph.
	 * @param connect adjazenzmatrix of the edges of the graph.
	 */
	public GameGraph(int knots, boolean[][] connect){
		if (connect.length != knots)
			throw new IllegalArgumentException("Anzahl Knoten und Größe Adjazenzmatrix müssen passen.");
		this.countKnots =knots;
	//	this.connections=connect;
		this.setBmConnections(new BooleanMatrix(connect));
		

	}
	
	/**
	 * Creates a new graph with the given number of nodes, all given connections
	 * in the adjazenz lists are taken in both directions.
	 * Initializes all states with false.
	 * 
	 * @param knots number of nodes.
	 * @param adjlists2 adjazenz lists for the connections in the graph.
	 */
	public GameGraph(int knots, HashMap<Integer, LinkedList<Integer>> adjlists2) {
		this.countKnots =knots;
		this.adjList = adjlists2;
		this.adjMatrix = new BooleanMatrix(knots);
		for (int i= 0;i<knots;i++){
			for (int j=0;j<knots;j++){
				this.adjMatrix.set(i, j, adjList.get(i).contains(j));
				if (adjList.get(i).contains(j) != adjList.get(j).contains(i))
					this.adjMatrix.set(i, j, true);
					//throw new IllegalArgumentException("Adjazenzlisten nicht symmetrisch");
			}
		}
	//	this.setBmConnections(new BooleanMatrix(connections));
	}

	/**
	 * Returns the direct neighbours of a node in the graph.
	 * That are all nodes which have an edge to the given node.
	 * 
	 * @param i number of the node.
	 * @return list of numbers of neighbours of the given node i.
	 */
	public LinkedList<Integer> getNeighbours(int i){
		LinkedList<Integer> ret = new LinkedList<Integer>();
		for (int j:adjList.get(i))
			ret.add(j);
		return ret;
	}

	/**
	 * Returns the number of Nodes in the graph.
	 * 
	 * @return the number of knots in the graph.
	 */
	public int getCountKnots() {
		return countKnots;
	}

	/*public boolean[][] getConnections() {
		return connections;
	}*/

	/*public HashMap<Integer, LinkedList<Integer>> getAdjlists() {
		return adjlists;
	}*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s= "Anzahl Knoten: " + countKnots;
		for (int i=0;i<countKnots;i++){
			s+="\n"+ i +":"+ this.getNeighbours(i).toString();
		}
		return s;
	}
//
	/**
	 * Sets up edges between the nodes in the graph corresponding to the given matrix
	 * and calculates the corresponding adjazenzlist.
	 * 
	 * @param bmConnections the adjazenzmatrix.
	 */
	public void setBmConnections(BooleanMatrix bmConnections) {
		this.adjMatrix = bmConnections;
		this.adjList = new HashMap<Integer,LinkedList<Integer>>();
		if (this.adjMatrix.getNumCols() != countKnots || this.adjMatrix.getNumRows() != countKnots )
			throw new IllegalArgumentException();
		for (int i=0;i<this.adjMatrix.getNumRows();i++){
			//verbindungsmatrix
			LinkedList<Integer> aims = new LinkedList<Integer>();
			for (int j=0;j<=i;j++){
				//Matrix muss symetrisch sein
				if (this.adjMatrix.get(i,j) !=this.adjMatrix.get(j,i))
					throw new IllegalArgumentException("Matriy nicht symmetrisch!");
				//adjazenzliste aufbauen
				if (this.adjMatrix.get(i,j))
					aims.add(j);
			}
			this.adjList.put(i, aims);
		}
	}
//
	/**
	 * Returns the adjazenzmatrix of the graph. 
	 * In this form they it can be used for many calculations.
	 * 
	 * @return the edges of the graph, represented in a matrix.
	 */
	public BooleanMatrix getBmConnections() {
		return adjMatrix;
	}
//
//	

}

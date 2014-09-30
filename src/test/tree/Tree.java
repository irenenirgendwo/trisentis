package test.tree;

import game.BooleanMatrix;
import game.GameGraph;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * This class represents a test.tree graph, i.e. a graph without circles.
 * It is represented by a root node and a map, which
 * connects the nodes of the test.tree with numbers of vertices in the graph.
 * 
 * @author Irene Thesing
 */
public class Tree extends GameGraph {

	/**The root node of the test.tree.*/
	private Node root;
	
	/** Maps the node number to corresponding node in the test.tree.*/
	HashMap<Integer,Node> treeStructure = new HashMap<Integer,Node>();
	
	/**Maps the node to the corresponding node number.*/
	HashMap<Node,Integer> nodeStructure = new HashMap<Node,Integer>();
	
	

	/**
	 * Creates a test.tree out of the root node, which has already 
	 * some child nodes. It creates the mapping between
	 * nodes and integer numbers of the nodes.
	 * 
	 * @param rootNode the root node.
	 */
	public Tree(Node rootNode) {
		super(rootNode.size());
		this.root = rootNode;
		int i=0;
		BooleanMatrix adjmatrix = new BooleanMatrix(this.getCountKnots());
		for (Node node: rootNode.getAllNodes(new LinkedList<Node>())){
			treeStructure.put(i, node);
			nodeStructure.put(node,i);
			node.setNumber(i);
			i++;
		}
		//Boolean Matrix erzeugen
		for (i=0;i<this.getCountKnots();i++){
			for (Node connector: treeStructure.get(i).getNeighbours()){
				adjmatrix.set(i, nodeStructure.get(connector), true);
			}
		}
		this.setBmConnections(adjmatrix);
	}
	
	/* (non-Javadoc)
	 * @see game.GameGraph#toString()
	 */
	public String toString(){
		return "Baum:\n" + root.toString();
	}

	
	/**
	 * Creates a String out of a solution which looks like a test.tree.
	 * Uses #Node.stringSolValues().
	 * 
	 * @param sol the given solution.
	 * @return the solution as a String.
	 */
	public String treeSolution(boolean[] sol){
		if (sol.length != this.getCountKnots())
			throw new IllegalArgumentException("Die Läsung passt nicht.");
		for (int i=0;i<sol.length;i++){
			treeStructure.get(i).setSolValue(sol[i]);
		}
		return root.stringSolValues();
	}
	
}

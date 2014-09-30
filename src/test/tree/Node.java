package test.tree;

import java.util.LinkedList;


/**
 * Represents a node in a test.tree with arbitrary many child nodes.
 * 
 * @author Irene Thesing
 */
public class Node{
	
	
	/**
	 * The parent node, which has this node as child node. 
	 * If it is null this node is a root of a test.tree.
	 */
	private Node parent = null;
	
	/**
	 * The list of child nodes. An empty list means that 
	 * this node is a leaf in the test.tree.
	 */
	private LinkedList<Node> children = new LinkedList<Node>();

	
	/**
	 * The number shows the given number of the node,
	 * if it is used as a node in a TreeGraph for a sigma game.
	 * It is useful to present the solution.	 
	 */
	private int number = -1;
	
	/**The solution state for representing a configuration.*/
	private boolean solvalue;
	

	/**
	 * Returns the number of the node, 
	 * if none was set, it returns -1.
	 * 
	 * @return number of the node.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number of the node.
	 * The user must garantee that two nodes in one test.tree have
	 * different numbers.
	 * 
	 * @param number number of the node.
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Creates a new node with the given child nodes.
	 * The parent nodes are null for the moment,
	 * the numbers are not set. 
	 * The parent values of the child nodes are also set in this method.
	 * 
	 * @param childs child nodes which are already created.
	 */
	public Node(LinkedList<Node> childs){
		this.parent = null;
		this.children = childs;
		for (Node child:children){
			child.setParent(this);
		}
	}
	
	/**
	 * Creates a new node with the given child nodes.
	 * The parent nodes are null for the moment,
	 * the numbers are not set. 
	 * The parent values of the child nodes are also set in this method.
	 * 
	 * @param child child nodes which are already created.
	 */
	public Node(Node... child){
		children = new LinkedList<Node>();
		for (Node ch: child){
			children.add(ch);
			ch.setParent(this);
		}
		this.parent = null;
	}
	
	/**
	 * Creates a new leaf node.
	 * 
	 * @param par parent node.
	 */
	public Node(Node par){
		this.parent = null;
		children = new LinkedList<Node>();
		//leaf = true;
	}

	/**
	 * Returns the parent node.
	 * 
	 * @return the parent node.
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Sets the parent node. Used if this node is created as child node.
	 * 
	 * @param parent sets this as parent node of this node.
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * Returns the list of children.
	 * 
	 * @return the list of the children.
	 */
	public LinkedList<Node> getChildren() {
		return children;
	}
	
	/**
	 * Creates a list with all nodes under this one in all generations 
	 * until the leafs inclusive this one. 
	 * 
	 * @param iteratorList the list with all nodes.
	 */
	public LinkedList<Node> getAllNodes(LinkedList<Node> iteratorList){
		iteratorList.add(this);
		for (Node child: children){
			child.getAllNodes(iteratorList);
		}
		return iteratorList;
	}

	
	/**
	 * Returns the count of nodes in this subtree.
	 * 
	 * @return count of nodes in this subtree.
	 */
	public int size() {
		int size = 1;
		for (Node child: children)
			size += child.size();
		return size;
	}

	/**
	 * Returns all neighbours of this nodes: That are all child nodes
	 * and the parent node.
	 * 
	 * @return the neighbours in this node in the test.tree graph. 
	 */
	public LinkedList<Node> getNeighbours() {
		LinkedList<Node> result = new LinkedList<Node>();
		if (parent != null)
			result.add(parent);
		result.addAll(children);
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return toString("");
	}
	
	/**
	 * Helps to create a string for the #toString() method.
	 * 
	 * @param leerzeichen how many empty spaces are there.
	 * @return the ready String.
	 */
	private String toString(String leerzeichen){
		String s= leerzeichen + "("+getNumber()+")\n";
		leerzeichen +="  ";
		for (Node child: children)
			s+=child.toString(leerzeichen);
		return s;
	}

	/**
	 * Copies this node. 
	 * 
	 * @return the copied node.
	 */
	public Node copy() {
		LinkedList<Node> newChildren = new LinkedList<Node>();
		for (Node child: children)
			newChildren.add(child.copy());
		return new Node(newChildren);
	}

	/**
	 * Sets the solution state of this node.
	 * 
	 * @param solvalue the value of the displayed configuration.
	 */
	public void setSolValue(boolean solvalue) {
		this.solvalue = solvalue;
	}



	/**
	 * Returns the configuration as a nice string which looks like a test.tree.
	 * 
	 * @return the configuration String.
	 */
	public String stringSolValues() {
		return stringSolValues("");
	}
	
	/**
	 * Returns the configuration as a nice string which looks like a test.tree.
	 * 
	 * @param leerzeichen empty spaces.
	 * @return the configuration as a nice string which looks like a test.tree.
	 */
	private String stringSolValues(String leerzeichen){
		String s= leerzeichen;
		if (solvalue)
			s += "(1)\n";
		else s+="(0)\n";
		leerzeichen +="  ";
		for (Node child: children)
			s+=child.stringSolValues(leerzeichen);
		return s;
	}

	
}

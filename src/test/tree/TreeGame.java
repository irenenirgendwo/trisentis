package test.tree;

import game.Game;
import help.StringRepresentation;

/**
 * Represents a sigma-game on a graph which is a test.tree.
 * Provides a main method to test the game on several trees.
 * 
 * @author Irene Thesing
 *
 */
public class TreeGame extends Game {

	/**
	 * Creates a new game on a test.tree, i.e. a graph without circles.
	 * 
	 * @param tree the test.tree.
	 */
	public TreeGame(Tree tree) {
		super(tree);
	}
	
	/**
	 * The method tests the sigma-game on different trees.
	 * With the #Node.copy() function it is possible to
	 * create trees inductively and test the sigma
	 * game for example on binary trees with different heights.
	 * 
	 * @param args not needed.
	 */
	public static void main(String[] args){
		Node left = new Node(new Node(new Node(),new Node()), new Node());
		Node right = new Node(new Node(new Node(),new Node()), new Node());
		Node root = new Node(new Node(left,new Node()),right);
		Tree graph = new Tree(root);
		TreeGame game1 = new TreeGame(graph);
		System.out.println(StringRepresentation.listToStr(game1.getSolutions()));
		
		Node second = new Node(new Node(),new Node());
		TreeGame game2 = new TreeGame(new Tree(second));
		System.out.println(StringRepresentation.listToStr(game2.getSolutions()));
		
		Node bin = new Node(new Node(), new Node());
	    Node bin3 = new Node(bin,bin.copy());
	    Node bin4 = new Node(bin3,bin3.copy());
	    TreeGame game4 = new TreeGame(new Tree(bin4));
		System.out.println("BIinärbaum4:" + game4.getGraph().toString() + 
				"\n Lösungen: " + StringRepresentation.listToStr(game4.getSolutions()));
			Node[] nodear = new Node[100];
		nodear[1]= new Node();
		for (int i=2;i<=10;i++){
			nodear[i] = new Node(nodear[i-1],nodear[i-1].copy());
			Tree tree = new Tree(nodear[i]);
			TreeGame game= new TreeGame(tree);
			System.out.println("BIinärbaum:" + i + !(game.getSolutions()).isEmpty());
			if (i==6){
				System.out.println(tree.toString() + "solution:"+tree.treeSolution(game.getSolutions().get(0)));
			}
		}
		
	}

}

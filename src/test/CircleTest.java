package test;

import game.Game;
import game.GameGraph;
import help.StringRepresentation;

/**
 * This class simulates the sigma-game on a circle.
 * It tests the game for some values.
 * 
 * The solution is not difficult, 
 * there exists always a solution if the number of nodes 
 * is divided by four.
 * 
 * @author Irene Thesing
 */
public class CircleTest{

	
	/**
	 * Creates a new circle game with the given number of nodes.
	 * Two neighbored nodes have an edge between them and every node 
	 * has exactly two neighbors.
	 * 
	 * @param numKnots count of nodes for the circle.
	 * @return the corresponding game on the circle.
	 */
	public static Game createCircle(int numKnots){
		boolean[][] adjmatrix = new boolean[numKnots][numKnots];
		//Verbindungen in beide Richtungen
		for (int i=0;i<numKnots;i++){
			if (i>0)
				adjmatrix[i][i-1] =true;
			else adjmatrix[i][numKnots-1]= true;
			if (i<numKnots-1)
				adjmatrix[i][i+1]=true;
			else adjmatrix[i][0]=true;
		}
		GameGraph graph = new GameGraph(numKnots,adjmatrix);
		return new Game(graph);
	}
	
	
	

	/**
	 * Test method for the sigma-game on the circle. It tries some game sizes.
	 * 
	 * @param args is not needed.
	 */
	public static void main(String[] args) {
		for (int i=3;i<20;i++){
			Game circ = createCircle(i);
			System.out.println("Lösungen zum Kreis "+i+": " + StringRepresentation.listToStr(circ.getSolutions()));
			System.out.println("Kerndimension zum Kreis "+i+": " + (circ.getKernel().size()-1));
		}
	}

}

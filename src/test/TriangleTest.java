package test;

import game.Game;
import game.GameGraph;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * This class calculates kernel elements and solutions for triangular graphs,
 * which have three sides with the same number of vertices and 
 * each vertex in the middle has six neighbors.
 * 
 * @author Irene Thesing
 */
public class TriangleTest {
	
	
	/**
	 * Creates a triangular graph, i.e. a graph on a triangular, in the middle
	 * every vertex has six neighbors.
	 * 
	 * @param size some integer, bigger or equal than 1 which describes the length of one side (in count of nodes).
	 * @return the triangular graph which corresponds to this side length.
	 */
	public static GameGraph createTriangleGameGraph(int size){
		int countNodes = 0;
		HashMap<Integer, LinkedList<Integer>> adjlist = new HashMap<Integer, LinkedList<Integer>>();
		for (int row = 0; row<size;row++){
			//Anzahl Knoten erhöhen
			//Verbindungen einrichten
			for (int i=0;i<row+1;i++){
			//	System.out.println("row:"+row +" i:"+i);
				LinkedList<Integer> neighboursi = new LinkedList<Integer>();
				if (i!=0){
					neighboursi.add(countNodes+ i-1);
					neighboursi.add(countNodes - row +i-1);
				//	System.out.println("links Nachbarn von "+(countNodes+i)+"sind"+(countNodes+i-1)+","+(countNodes - row +i-1));
				}
				if (i!=row){
					neighboursi.add(countNodes+ i+1);
					neighboursi.add(countNodes - row +i);
				//	System.out.println("rechts Nachbarn von "+(countNodes+i)+"sind"+(countNodes+ i+1)+","+(countNodes - row +i));
				}
				if (row<size-2){
					neighboursi.add(countNodes + row+i+1);
					neighboursi.add(countNodes + row+i+2);
					//System.out.println("unten Nachbarn von "+(countNodes+i)+"sind"+(countNodes + row+i+1)+","+(countNodes + row+i+2));
				}
				adjlist.put(countNodes+i,neighboursi);
			}
			//Anzahl Knoten erhöhen
			countNodes += row+1; 
		}
	//	System.out.println("countNodes " + countNodes);
		return new GameGraph(countNodes,adjlist);
		
	}

	/**
	 * Tests the triangular graph with different values for the side length.
	 * 
	 * @param args is not needed.
	 */
	public static void main(String[] args) {
		for (int k=1; k< 31; k+=1){
			Game game = new Game(createTriangleGameGraph(k));
			System.out.println(k + " ergibt Lsg "+ (!game.getSolutions().isEmpty()));
			System.out.println(k+ " ergibt Kerndimension "+ (game.getKernel().size()-1));
		}
	}

}

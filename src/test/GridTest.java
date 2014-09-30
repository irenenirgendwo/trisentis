package test;

import trisentis.Trisentis;

/**
 * This class is for testing the sigma game on a grid,
 * where diagonal neighbored elements does not count as neighbored in the graph.
 * It uses a possibility in the Trisentis class to change the neighborhoods.
 * 
 * @author Irene Thesing
 */
public class GridTest {

	/**
	 * Tests the sigma game on several grids for solutions.
	 * 
	 * @param args not needed.
	 */
	public static void main(String[] args) {
		for (int k=2; k< 71; k+=1){
			for (int l = 2;l<20;l++){
				Trisentis gitter = new Trisentis(k,l);
				if (gitter.getSolutions().isEmpty())
					System.out.println(k + ","+l+ " ergibt keine Lsg ");//k + ","+l+ " ergibt Lsg "+ (!gitter.getSolutions().isEmpty()));
			}
		}
	}

}

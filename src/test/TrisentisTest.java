package test;

import trisentis.TriExaminer;
import trisentis.Trisentis;

/**
 * Tests the Trisentis game for some values.
 * 
 * @author Irene Thesing
 */
public class TrisentisTest {


	/**
	 * Tests some values for the Trisentis game.
	 * 
	 * @param args is not needed.
	 */
	public static void main(String[] args) {
		//test values
		for (int i=2;i<30;i++){
			for (int j=2;j<30;j++){
				Trisentis tris = new Trisentis(i,j);
				if (tris.getSolutions().isEmpty())
					System.out.println(i + ","+j+ " ergibt keine Lösung. ");
				else System.out.println(i + ","+j+ " besitzt eine Lösung. ");
				System.out.println("Das Trisentis-Spiel "+ i + "x"+j +
						" hat Kerndimension "+(tris.getKernel().size()-1));
			}
		}
		
		Trisentis game = new Trisentis(9,4);
		
		// zusammen setzen testen
		TriExaminer exam = new TriExaminer(game);
		exam.zusammensetzenOU();
	}

}

/**
 * 
 */
package guitrisentis;

import game.BooleanMatrix;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


/**
 * Shows to matrixes above each other or on the left and the right.
 * It is used to show to configurations which 
 * fit together to form a new solution for a bigger Trisentis game.
 *  
 * @author Irene Thesing
 *
 */
public class TwoMatrixPanel extends JPanel {
	

		/**Id.*/
	    private static final long serialVersionUID = -7791694850526673320L;
		/**First matrix to be shown.*/
		private BooleanMatrix first;
		/**Second matrix to be shown.*/
		private BooleanMatrix second;
		/**True if and only if the matrices shall be shown
		 * above each other, otherwise they are placed
		 * left and right.*/
		private boolean isOUComb;
		
		/**Where the two matrizes should be placed next to each other.*/
		private JSplitPane splitPane;

		
		
		/**
		 * Generates a new Panel which shows both 
		 * matrices next to each other.
		 * 
		 * @param fir first matrix.
		 * @param sec second matrix.
		 * @param isOU true if and only if the second matrix should be placed under the first, otherwise it is placed on the right side.
		 * @param description text for the label which describes the view.
		 */
		public TwoMatrixPanel(BooleanMatrix fir, BooleanMatrix sec, boolean isOU,String description){
			this.first = fir;
			this.second = sec;
			this.isOUComb = isOU;
			if (isOUComb){
				splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new TrisentisBoard(first),new TrisentisBoard(second));
			} else 
				splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new TrisentisBoard(first),new TrisentisBoard(second));
			this.setLayout(new BorderLayout());
			splitPane.setVisible(true);
			this.add(new JLabel(description),BorderLayout.NORTH);
			this.add(splitPane,BorderLayout.CENTER);
			this.setVisible(true);
			splitPane.setResizeWeight(0.5); 
		}

}

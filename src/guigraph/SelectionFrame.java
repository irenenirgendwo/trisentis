package guigraph;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

/**
 * A Frame where the user can choose some programmed graphs
 * to analyse the sigma game on them or to change them to own graphs.
 * 
 * @author Irene Thesing
 *
 */
public class SelectionFrame extends JFrame {

	/**
	 * Generated Id.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Generates a new SelectionFrame.
	 */
	public SelectionFrame(GraphBoard owner){
		super("Voreingestellte Graphen auswählen");
		final GraphBoard board = owner;

		JToolBar streckenTool = new JToolBar();
		streckenTool.add(new JLabel("Eine Strecke mit "));
		final SpinnerNumberModel numVertex = new SpinnerNumberModel(6,1,20,1);
		streckenTool.add(new JSpinner(numVertex));
		streckenTool.add(new JLabel("Knoten "));
		JButton path = new JButton("zeichnen!");
		streckenTool.add(path);
		path.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				board.drawPath(numVertex.getNumber().intValue());
				doAtAllActions();
			}
		});
		
		JToolBar circleTool = new JToolBar();
		circleTool.add(new JLabel("Einen Kreis mit  "));
		final SpinnerNumberModel numCircle = new SpinnerNumberModel(8,1,20,1);
		circleTool.add(new JSpinner(numCircle));
		circleTool.add(new JLabel("  Knoten "));
		JButton circle = new JButton("zeichnen!");
		circleTool.add(circle);
		circle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				board.drawCircle(numCircle.getNumber().intValue());
				doAtAllActions();
			}
		});
		
		JToolBar triangleTool = new JToolBar();
		triangleTool.add(new JLabel("Ein dreieckiges Spielfeld mit Seitenlänge  "));
		final SpinnerNumberModel numTriangle = new SpinnerNumberModel(4,1,10,1);
		triangleTool.add(new JSpinner(numTriangle));
//		triangleTool.add(new JLabel("  Knoten "));
		JButton triangle = new JButton("  zeichnen!");
		triangleTool.add(triangle);
		triangle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				board.drawTriangle(numTriangle.getNumber().intValue());
				doAtAllActions();
			}
		});
		
		JToolBar binaryTool = new JToolBar();
		binaryTool.add(new JLabel("Einen vollständigen Binärbaum der Höhe  "));
		final SpinnerNumberModel numBinary = new SpinnerNumberModel(4,1,8,1);
		binaryTool.add(new JSpinner(numBinary));
//		triangleTool.add(new JLabel("  Knoten "));
		JButton binaryTree = new JButton("  zeichnen!");
		binaryTool.add(binaryTree);
		binaryTree.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				board.drawBinaryTree(numBinary.getNumber().intValue());
				doAtAllActions();
			}
		});
		
		JToolBar tertTool = new JToolBar();
		tertTool.add(new JLabel("Einen vollständigen 3-Baum der Höhe  "));
		final SpinnerNumberModel numTert = new SpinnerNumberModel(2,1,5,1);
		tertTool.add(new JSpinner(numTert));
//		triangleTool.add(new JLabel("  Knoten "));
		JButton tertTree = new JButton("  zeichnen!");
		tertTool.add(tertTree);
		tertTree.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				board.drawTertiaryTree(numTert.getNumber().intValue());
				doAtAllActions();
			}
		});
		
		this.setLayout(new GridLayout(5,1));
		this.add(streckenTool);
		this.add(circleTool);
		this.add(triangleTool);
		this.add(binaryTool);
		this.add(tertTool);
		
		this.setSize(500,300);
		this.setVisible(true);
		this.toFront();
	}
	
	
	private void doAtAllActions(){
		this.setVisible(false); // verschwinden lassen
		this.dispose(); // abräumen
	}
	
	public static void main(String[] args){
		new SelectionFrame(new GraphBoard(GraphBoard.SHOW_MODE));
	}
	
	
	
}

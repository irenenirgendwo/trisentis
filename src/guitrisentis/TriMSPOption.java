package guitrisentis;

import game.BooleanMatrix;
import gui.MSPOption;
import help.Pair;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import trisentis.TriExaminer;
import trisentis.Trisentis;

/**
 * As subclass of MSPOption this class implements the display of a result of an operation.
 * It represents more ways to analyse a Trisentis game.
 * 
 * With a Trisentis game it is interesting to look whether it is possible to move two solutions
 * into each other to get a new solution element of a bigger Trisentis game.
 * That is why we display the appliance on a Trisentis game with an additional border.
 * 
 * We also can watch out for symmetric kernel elements, axially symmetrical for this time.
 * On the other hand we can try to puzzle solutions together as explained above.
 * 
 * @author Irene Thesing
 *
 */
public class TriMSPOption extends MSPOption {


	/**
	 * Id.
	 */
	private static final long serialVersionUID = -249267759176563171L;

	
	
	/**
	 * Creates a new TriMSPOption which provides more functionalities to analyse the Trisentis game.
	 * 
	 * @param type if we are to analyse the kernel or solutions or both.
	 * @param owner the ToolBar which has created this and where the presented elements are stored.
	 */
	public TriMSPOption(int type,TriAnalyseToolBar owner) {
		super(type,owner);
		sum.setText("Addieren und Anwenden");
	}


	@Override
	protected void display(boolean[] newValue) {
		Trisentis game = (Trisentis)owner.getGame();
		game.setState(newValue);
		TrisentisBoard board = new TrisentisBoard(game,TrisentisBoard.SHOW_APPLIANCE_MODE);
		board.updateSituation(newValue);
		Frame showFrame = new JFrame("Resultat der Operation und deren Anwendung");
		showFrame.setLayout(new BorderLayout());
		showFrame.setSize(600,600);
		showFrame.add(board);
		showFrame.setVisible(true);
	}


	@Override
	protected void initAdditionalAllActions() {
		if (this.type == TYPE_KERNEL || this.type == TYPE_SOL_KER){
			//Button zum alle Kernelemente anzeigen
			JButton symCore = new JButton("Symmetrische Kernelemente anzeigen");
			symCore.addActionListener(new ActionListener(){
				//@Override
				public void actionPerformed(ActionEvent act) {
						Trisentis game = (Trisentis) owner.getGame();
						List<boolean[]> cores = game.getSymTriKernel();
						String[] undertexts = new String[cores.size()];
						for (int i=0;i<undertexts.length;i++){
							undertexts[i] = "Kernelement "+(i+1);
						}
						owner.initMoreSolPanel(cores, undertexts, 
								TYPE_KERNEL, "Symmetrische Kernelemente des Trisentis-Spiels");
					
				}
			});
			this.add(symCore);
		}
		
		if (this.type == TYPE_KERNEL || this.type == TYPE_SOL_KER){
			//Button zum alle Kernelemente anzeigen
			JButton symCore1 = new JButton("Symmetrischen Kern mit mittlerer 1");
			symCore1.addActionListener(new ActionListener(){
				//@Override
				public void actionPerformed(ActionEvent act) {
						Trisentis game = (Trisentis) owner.getGame();
						List<boolean[]> cores = game.getSymTriKernelWithOne();
						String[] undertexts = new String[cores.size()];
						for (int i=0;i<undertexts.length;i++){
							undertexts[i] = "Kernelement "+(i+1);
						}
						owner.initMoreSolPanel(cores, undertexts, 
								TYPE_KERNEL, "Symmetrische Kernelemente mit 1 in der Mitte");
					
				}
			});
			this.add(symCore1);
		}
	
		if (type == TYPE_SOL_KER || type == TYPE_SOLUTION){
			JButton togLR = new JButton("Versuche Lösung zusammen zusetzen (LR)");
			togLR.addActionListener(new ActionListener(){
				//@Override
				public void actionPerformed(ActionEvent act) {
					List<Pair<BooleanMatrix,BooleanMatrix>> resultList;
					Trisentis tris = (Trisentis)owner.getGame();
					if (!tris.getSolutions().isEmpty()){
						resultList = TriExaminer.solTogetherLR(tris,tris);
					} else {
						resultList = TriExaminer.coreTogehterLR(tris,tris);
					}
					if (resultList.isEmpty())
						JOptionPane.showMessageDialog(null,"Zusammen setzen von zwei Elementen nebeneinander ist nicht möglich.");
					else {
						JFrame showResults = new JFrame("Möglichkeiten zum Zusammen setzen zu einer Lösung für das "
								+ (2*tris.getRows()+1) + "x" + tris.getCols() +"Trsientis Spiel");
						showResults.setSize(400,800);
						showResults.setLayout(new GridLayout(5,2));
						for (int i=0;i<10 && i<resultList.size();i+=2){
							JPanel p = new JPanel();
							p.setLayout(new BorderLayout());
							p.add(new TwoMatrixPanel(resultList.get(i).getFirst(),resultList.get(i).getSecond(),false,
									"Zwei Lösungen, die zusammen gesetzt werden können"),BorderLayout.CENTER);
							p.add(new JLabel("Möglichkeit "+i),BorderLayout.SOUTH);
							p.setVisible(true);
							showResults.add(p);
						}
						showResults.setVisible(true);
					}
				}
			});
			this.add(togLR);
		}
		
	}

}

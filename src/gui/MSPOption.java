package gui;

import game.BooleanMatrix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;


/**
 * This Panel is shown on the right border if on the main frame some solutions or kernel elements
 * are displayed. It provides some buttons for further analysis of the kernel or solution elements.
 * It is possible to add some, to show all kernel elements and to check whether the chosen elements are linear
 * independent.
 * The subclasses can add further methods.
 * 
 * @author Irene Thesing
 */
public abstract class MSPOption extends JToolBar{

	/**
	 * Generated id.
	 */
	private static final long serialVersionUID = 1117389110296556701L;
	
	/**This type is chosen if solutions are presented.*/
	public static final int TYPE_SOLUTION = 0;
	/**This type is chosen if kernel elements are presented.*/
	public static final int TYPE_KERNEL = 1;
	/**This type is chosen if one solution and a base of kernel elements are presented.*/
	public static final int TYPE_SOL_KER = 2;
	
	/**The actual type of presentation.*/
	protected int type;
	
	/**
	 * This class is started by an AnalyseToolBar, there the connections between the shown 
	 * pictures and the corresponding game elements are stored.
	 */
	protected AnalyseToolBar owner;
	
	/**
	 * Button for adding all checked values and forming a sum.
	 */
	protected JButton sum; 

	
	/**
	 * Creates a new ToolBar for further analyses.
	 * The buttons which are shown depend on the type.
	 *  
	 * @param typ whether kernel elements or solutions are presented.
	 * @param analyse the AnalyseToolBar which has started this and where we can fetch the 
	 * game elements we need.
	 */
	public MSPOption(int typ, AnalyseToolBar analyse){
		super(JToolBar.VERTICAL);
		type = typ;
		owner = analyse;
		this.add(new JLabel("Weitere Analysemöglichkeiten"));
		
		
		if (type == TYPE_KERNEL || type == TYPE_SOL_KER){
			JButton allCore = new JButton("Alle Kernelemente zeigen");
			allCore.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent act) {
					List<boolean[]> showList = owner.getGame().getAllKernel();
					String[] undertexts = new String[showList.size()];
					for (int i=0;i<undertexts.length;i++){
						undertexts[i] = "Kernelement "+(i+1);
					}
					owner.initMoreSolPanel(showList,undertexts,MSPOption.TYPE_KERNEL, 
							"Alle Kernelemente des Spiels");
				}
			});
			this.add(allCore);
		} else if  (type == TYPE_SOLUTION || type == TYPE_SOL_KER){
			JButton allSol = new JButton("Alle Lösungen zeigen");
			allSol.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent act) {
					List<boolean[]> showList = owner.getGame().getAllSolutions();
					String[] undertexts = new String[showList.size()];
					for (int i=0;i<undertexts.length;i++){
						undertexts[i] = "Kernelement "+(i+1);
					}
					owner.initMoreSolPanel(showList,undertexts,MSPOption.TYPE_SOLUTION, 
							"Alle Lösungen des Spiels");
				}
			});
			this.add(allSol);
		}
		initAdditionalAllActions();
		
		this.addSeparator();
		this.add(new JLabel("Ausgewählte Elemente:"));
		sum = new JButton("Addieren");
		sum.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				List<boolean[]> checkedEntries = getCheckedElems();
				if (checkedEntries.isEmpty())
					JOptionPane.showMessageDialog(null,"Keine Einträge ausgewählt.","Titel", JOptionPane.INFORMATION_MESSAGE);
				else {
					boolean[] newValue = BooleanMatrix.sumVectors(checkedEntries);
					display(newValue);
				}
				
			}
		});
	
		
		
		this.add(sum);
		if (type == TYPE_KERNEL || type == TYPE_SOL_KER){
			JButton ind = new JButton("Linear unabhängig?");
			ind.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent act) {
					if (BooleanMatrix.isLinearIndependent(getCheckedElems()))
						JOptionPane.showMessageDialog(null,"Die ausgewählten Elemente sind linear unabhängig.","Linear unabhängig?", JOptionPane.INFORMATION_MESSAGE);
					else {
						JOptionPane.showMessageDialog(null,"Die ausgewählten Elemente sind voneinander linear abhängig.","Linear unabhängig?", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
			});
			this.add(ind);
		}
		
	}
	
	protected abstract void initAdditionalAllActions();

	/**
	 * Displays a result of a summation of different solutions or kernel elements in a new window.
	 * 
	 * @param newValue the result which shall be presented.
	 */
	protected abstract void display(boolean[] newValue);

	
	/**
	 * Returns a list of the selected entries.
	 * 
	 * @return list of selected matrices.
	 */
	public List<boolean[]> getCheckedElems(){
		List<Integer> numbersChecked = owner.getMoreSolPanel().getNumbersOfChecked();
		List<boolean[]> checked = new LinkedList<boolean[]>();
		for (int check: numbersChecked){
			checked.add(owner.getShowList().get(check));
		}
		return checked;
	}

}

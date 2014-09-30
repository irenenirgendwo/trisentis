package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Shows a list of panels, where solutions or kernel elements or other configurations 
 * of a trisentis or a gisentis game. It is possible to choose every entry by a checkbox.
 * The entry panels must be saved also somewhere else, so that the belonging games 
 * can be adressed. This class only provides a nice view.
 * 
 * @author Irene Thesing
 */
public class MoreSolPanel extends JPanel {
	
	/**
	 * The java id.
	 */
	private static final long serialVersionUID = 1L;

	/**The panels which shall be shown on the MoreSolPanel.*/
	private SolOptionPanel[] entryPanels;
	

	/**The text which shall be presentes under any entry. It is completed by a running number.*/
	private String[] underlines;// undertext = "Lösung Nummer ";



	/**
	 * Creates a new panel where the given panels are shown with the given texts as underlines.
	 * The entries shown with a checkbox, where they can be choosen.
	 * 
	 * @param title The String which shall be the headline of the panel.
	 * @param solutions Array of panels which shall be shown as entries.
	 * @param undertexts Text what shall be under each entry. Of course, undertexts[i] belongs to solutions[i].
	 * @param applyButtons For each entry a button which shall be under it, for example to apply a solution or a kernel element.
	 */
	public MoreSolPanel(String title, JComponent[] solutions, String[] undertexts,JButton[] applyButtons){
		JPanel innerPanel = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(new JLabel(title),BorderLayout.NORTH);
		
		int n = solutions.length;
		if (n != undertexts.length || applyButtons.length != n)
			throw new IllegalArgumentException("Both Arrays have to have the same length.");
		//this.entrySolutions = new J[n];
		this.entryPanels = new SolOptionPanel[n];
		this.underlines = undertexts;
		innerPanel.setLayout(new GridLayout(n/4+1,4));
		//	this.setLayout(new GridLayout(n/4+1,4));
		for (int i=0; i<n;i++){
			entryPanels[i] = new SolOptionPanel(solutions[i],underlines[i],applyButtons[i]);
			entryPanels[i].setVisible(true);
			//next.integrateInto(p);
			innerPanel.add(entryPanels[i]);
		}
		innerPanel.setVisible(true);
		JScrollPane scroller = new JScrollPane(innerPanel);
		this.add(scroller,BorderLayout.CENTER);
		this.setVisible(true);
	}
	

	/**
	 * Creates a new panel where the given panels are shown with the given texts as underlines.
	 * The entries shown with a checkbox, where they can be choosen.
	 *  
	 * @param title The String which shall be the headline of the panel.
	 * @param solutions Array of panels which shall be shown as entries.
	 * @param undertexts Text what shall be under each entry. Of course, undertexts[i] belongs to solutions[i].
	 */
	public MoreSolPanel(String title, JComponent[] solutions, String[] undertexts){
		JPanel innerPanel = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(new JLabel(title),BorderLayout.NORTH);
		
		int n = solutions.length;
		if (n != undertexts.length)
			throw new IllegalArgumentException("Both Arrays have to have the same length.");
		//this.entrySolutions = new J[n];
		this.entryPanels = new SolOptionPanel[n];
		this.underlines = undertexts;
		innerPanel.setLayout(new GridLayout(n/4+1,4));
		//	this.setLayout(new GridLayout(n/4+1,4));
		//innerPanel.setVisible(true);
		for (int i=0; i<n;i++){
			entryPanels[i] = new SolOptionPanel(solutions[i],underlines[i]);
			entryPanels[i].setVisible(true);
			solutions[i].setVisible(true);
			//next.integrateInto(p);
			innerPanel.add(entryPanels[i]);
			entryPanels[i].setVisible(true);
			//System.out.println("panel gesetzt");
		}
		innerPanel.setVisible(true);
		this.add(innerPanel,BorderLayout.CENTER);
		this.setVisible(true);
	}
	

	

	/**
	 * Returns the entry panels, which are shown. 
	 * @return The entry panels, which are shown.
	 */
	public SolOptionPanel[] getEntryPanels() {
		return entryPanels;
	}

	 /**
	 * Returns the underline text for each panel.
	 * @return The underline text for each panel.
	 */
	public String[] getUnderlines() {
		return underlines;
	}

	/**
	 * Sets the underline text for each panel with the given values. Of course the length of the given array
	 * must fit to the number of entries. 
	 * 
	 * @param undertext The underline text for each panel.
	 */
	public void setUnderlines(String[] undertext) {
		this.underlines = undertext;
	}

	/**
	 * Places a new Label at the head of this frame, as a title of the pictures.
	 * 
	 * @param string Content of the new Label.
	 */
	public void setTitleLabel(String string) {
		this.add(new JLabel(string),BorderLayout.NORTH);
	}


	/**
	 * Returns a list of all numbers of checked entries, i.e. the numbers for adressing all entry panels, 
	 * where the checkbox was chosen.
	 * 
	 * @return A list of all numbers of checked entries.
	 */
	public List<Integer> getNumbersOfChecked(){
		List<Integer> result = new LinkedList<Integer>();
		for (int i=0;i<entryPanels.length;i++){
			if (entryPanels[i].isChecked())
				result.add(i);
		}
		return result;
	}
	
	

	/**
	 * This is only used as a test method for this class. 
	 * 
	 * @param args This parameters are not needed.
	 */
	public static void main(String[] args){
		JPanel[] entries = new JPanel[7];
		String[] texts = new String[7];
		for (int i=0;i<7;i++){
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout());
			panel.add(new JLabel("test " + i));

			entries[i] = panel;
			texts[i]="txt";
			panel.setVisible(true);
		}
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout());
		panel1.add(new JLabel("test"));
		panel1.setVisible(true);
		
		JFrame mainWindow = new JFrame();
		mainWindow.setSize(640,640);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("Test MoreSolPanel");
		mainWindow.add(new MoreSolPanel("title",entries,texts));
	//	mainWindow.add(new SolOptionPanel(panel1,"name"));
		mainWindow.setVisible(true);
	}


	/**
	 * Adds an Option Panel to the east of this panel, where some options can be chosen for further analyse.
	 * 
	 * @param initOptionPanel the panel which shall be shown.
	 */
	public void setOptionPanel(JComponent initOptionPanel) {
		this.setVisible(false);
		this.add(initOptionPanel,BorderLayout.EAST);
		this.setVisible(true);
	}

		
}

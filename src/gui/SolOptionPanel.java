package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * For a better look this class containers a view of a matrix or a view of a graph or any other component.
 * It is used for displaying a list of components, entries, solutions or kernel elements in a frame or
 * on a panel. Every entry has a a own name and a checkbox to select it.
 * 
 * @author Irene Thesing
 */
public class SolOptionPanel extends JPanel {
	
	/**The java id.*/
	private static final long serialVersionUID = 1L;
	
	/**The main entry is the view of a Trisentis or a game on a graph.*/
	private JComponent entry;
	/**A Panel to describe the entry with a name and a checkbox to select it.*/
	private JPanel options;

	/**The checkbox for selection of the entry.*/
	private JCheckBox check;
	
	
	/**
	 * Constructs a new SolOptionPanel, which containers the entry with a name of this view and a check box to select
	 * this special view and in this case, an additional button for applying it.
	 * 
	 * @param content The shown entry.
	 * @param name Name of this entry.
	 * @param applyButton an apply Button if it shall be shown 
	 * 
	 */
	public SolOptionPanel(JComponent content, String name, JButton applyButton){
		this(content,name);
		options.removeAll();
		options.setLayout(new GridLayout(2,2));
		options.add(new JLabel(name));
		options.add(applyButton);
		options.add(check);
		options.setVisible(true);
		/*super();
		this.entry=content;
		this.setLayout(new BorderLayout());
		options = new JPanel();
		options.setLayout(new GridLayout(2,2));
		options.add(new JLabel(name));
		options.add(applyButton);
		check = new JCheckBox("Auswählen");
		options.add(check);
		options.setVisible(true);
		this.add(entry,BorderLayout.CENTER);
		this.add(options,BorderLayout.SOUTH);
		this.setVisible(true);*/

	}
	/**
	 * Constructs a new SolOptionPanel, which containers the entry with a name of this view and a check box to select
	 * this special view.
	 * 
	 * @param content The shown entry.
	 * @param name Name of this entry.
	 * 
	 */
	public SolOptionPanel(JComponent content, String name){
		super();
		this.entry=content;
		this.setLayout(new BorderLayout());
		options = new JPanel();
		options.setLayout(new GridLayout(2,1));
		options.add(new JLabel(name));
		check = new JCheckBox("Auswählen");
		options.add(check);
		options.setVisible(true);
		this.add(entry,BorderLayout.CENTER);
		this.add(options,BorderLayout.SOUTH);
		this.setVisible(true);
		//System.out.println("visible solOptPanel");
	}

	
	/**
	 * Returns whether the checkbox is selected.
	 * @return True, if the checkbox is selected, false otherwise.
	 */
	public boolean isChecked(){
		return this.check.isSelected();
	}


}


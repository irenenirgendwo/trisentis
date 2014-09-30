package gui;

import game.Game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;


/**
 * This ToolBar is placed in the south of the main frame, it is only shown when the game was started.
 * It provides four buttons to analyse the game, it is possible to show some solutions,
 * a base of the kernel elements or a solution together with the base of the kernel elements.
 * At last, there can be calculated some facts about the kernel dimension and the solvability of the game.
 * 
 * @author Irene Thesing
 *
 */
public abstract class AnalyseToolBar extends JToolBar {
	
	/**
	 * Generated id.
	 */
	private static final long serialVersionUID = -1773959285461797530L;
	
	/**Owner of this Toolbar*/
	protected final MainFrame owner;
	
	/**
	 * The game which is analysed.
	 */
	protected Game game;
	
	/**
	 * The GameBoard is kept to show it again if it shall be modified.
	 */
	protected GameBoard gameboard;
	
	/**Button for a view of some solutions, it checks also whether there is a solution.*/
	protected JButton solve;
	/**Button for view a basis of the kernel*/
	protected JButton kernel;
	/**Button for a view of one solution and basic kernel elements*/
	protected JButton solKer;
	/**Button to show dimension of the graph (number of nodes), dimension of the kernel and so on*/
	protected JButton facts;
	/**What happens when the solve Button is pressed.*/
	protected ActionListener solveAction;
	/**What happens when the kernel Button is pressed.*/
	protected ActionListener kernelAction;
	/**What happens when the solveCore Button is pressed.*/
	protected ActionListener solveKernelAction;
	/**What happens when the facts Button is pressed.*/
	protected ActionListener factAction;
	
	/**
	 * The configurations which are at the moment shown on 
	 * the center of the #MainFrame.
	 */
	protected List<boolean[]> showList;
	
	/**Where the different configurations are shown on.*/
	protected MoreSolPanel moreSolPanel;

	/**
	 * Generates a new AnalyseToolBar, invisible for the user until the game starts.
	 * The user shall first build up the game and start it before doing analyses.
	 * 
	 * @param own the owner of the toolbar (where it is placed)
	 */
	public AnalyseToolBar(MainFrame own) {
		super(JToolBar.HORIZONTAL);
		this.setFloatable(false);
		owner = own;
		this.setLayout(new GridLayout(1,4));
		solve = new JButton("Zeige Lösungen");
		this.add(solve);
		kernel = new JButton("Zeige Basis des Kerns");
		this.add(kernel);
		solKer = new JButton("Eine Lösung und Kern-Elemente");
		this.add(solKer);
		facts = new JButton("Zeige einige Fakten.");
		this.add(facts);
		this.setVisible(false);
		initActions();
	}
	

	/**
	 * Generates a new active AnalyseToolBar with the given game.
	 * 
	 * @param tri the game created with the input graph	 
	 * @param own the owner of the toolbar (where it is placed)
	 */
	public AnalyseToolBar(Game tri,MainFrame own) {
		this(own);
		game = tri;
		initActions();
		this.setVisible(true);
	}

	/**
	 * Shows the analyse ToolBar to a given gaming graph, i.e. a game on a given graph
	 * 
	 * @param gameboard the game board which is active since the game was startet.
	 * We get the game via the gameboard.
	 */
	public void showAnalyse(GameBoard gameboard){
		this.setVisible(false);
		this.gameboard = gameboard;
		this.game = gameboard.getGame();
		initActions();
		this.setVisible(true);
	}
	
	/** hides the analyse toolbar, when game is interrupted*/
	public void hideAnalyse(){
		this.setVisible(false);
	}
	
	
	/**
	 * Inits the action performed by click on the buttons.
	 * The button solve checks whether there is a solution. If there are solutions, it shows some in a new window.
	 * The button kernel shows the basic kernel elements.
	 */
	protected void initActions(){
		solve.removeActionListener(solveAction);
		solveAction = new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				showList = game.getSolutions();
				if (showList.isEmpty())
					JOptionPane.showMessageDialog(null,"Es gibt keine Lösung.","Keine Lösung!", JOptionPane.INFORMATION_MESSAGE);
				else {
					String[] undertexts = new String[showList.size()];
					for (int i=0;i<undertexts.length;i++){
						undertexts[i] = "Lösung Nummer "+i;
					}
					initMoreSolPanel(showList, undertexts, MSPOption.TYPE_SOLUTION,
							"Einige Lösungen des Spiels");
				}
				
			}
		};
		solve.addActionListener(solveAction);
		
		kernel.removeActionListener(kernelAction);
		kernelAction = new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				showList = game.getKernel();
				if (showList.isEmpty())
					JOptionPane.showMessageDialog(null,"Dieser Fall sollte nicht auftreten","Titel", JOptionPane.INFORMATION_MESSAGE);
				else {
					String[] undertexts = new String[showList.size()];
					for (int i=0;i<undertexts.length;i++){
						undertexts[i] = "Kernelement Nummer "+ i;
					}
					initMoreSolPanel(showList, undertexts, MSPOption.TYPE_KERNEL,
							"Basis-Kernelemente des Spiels");
				}
				
			}
		};
		kernel.addActionListener(kernelAction);
		
		solKer.removeActionListener(solveKernelAction);
		solveKernelAction = new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				List<boolean[]> solutions = game.getSolutions();
				if (solutions.isEmpty())
					JOptionPane.showMessageDialog(null,"Es gibt keine Lösung.","Keine Lösung!", JOptionPane.INFORMATION_MESSAGE);
				else {
					showList = new LinkedList<boolean[]>();
					showList.add(solutions.get(0));
					showList.addAll(game.getKernel());
					String[] undertexts = new String[showList.size()];
					undertexts[0] = "Eine Lösung";
					for (int i=1;i<undertexts.length;i++){
						undertexts[i] = "Kernelement Nummer "+ (i-1);
					}
					initMoreSolPanel(showList, undertexts, MSPOption.TYPE_SOL_KER,
							"Eine Lösung und die Basis-Kernelemente des Spiels");
				}
				
			}
		};
		solKer.addActionListener(solveKernelAction);
		
		facts.removeActionListener(factAction);
		factAction = new ActionListener(){
			public void actionPerformed(ActionEvent act) {
				JFrame factFrame = new JFrame("Eigenschaften des untersuchten \u03c3-Spiels");
				JPanel factPanel = new JPanel();
				factPanel.setLayout(new GridLayout(3,1));
				JLabel labelSol = new JLabel("");
				if (game.getSolutions().isEmpty())
						labelSol.setText("Das \u03c3-Spiel hat keine Lösung.");
				else if (game.getSolutions().size()==1)
					labelSol.setText("Das \u03c3-Spiel hat genau eine Lösung.");
				else 
					labelSol.setText("Das \u03c3-Spiel hat mehrere Lösungen.");
				factPanel.add(labelSol);
				int dimCore = game.getKernel().size()-1;
				factPanel.add(new JLabel("Das Spiel besitzt "+game.getBig() + " Knoten."));
				factPanel.add(new JLabel("Die Dimension des Kerns ist "+dimCore+"."));
				//owner.putCentral(factPanel);
				factPanel.setVisible(true);
				factFrame.setSize(500,200);
				factFrame.setLayout(new GridLayout(1,1));
				factFrame.add(factPanel);
				factFrame.setVisible(true);
			}
		};
		facts.addActionListener(factAction);
	}


	/**
	 * The subclasses must instantiate the option panel which shall be shown at the
	 * east border. There the displayed solutions or kernel elements should be analysed.
	 * This can be different with different games.
	 * For example in Trisentis game it is interesting to apply it on a game with additional border fields.
	 * 
	 * @param type describes whether solutions, kernel elements or both are displayed.
	 * @return the corresponding option panel.
	 */
	public abstract MSPOption initOptionPanel(int type);


	/**
	 * The subclasses must instantiate the components which are displayed as solutions
	 * or kernel elements in the way there are shown, so graphs or Trisentis game boards.
	 * 
	 * @param solutions the configurations which shall be shown.
	 * @return the list of the components which are filled with pictures of the configurations.
	 */
	public abstract JComponent[] initSolutionPanels(List<boolean[]> solutions);


	/**
	 * Returns the panel where the soultions or kernel elements are shown on.
	 * 
	 * @return the panel where the solutions are shown on.
	 */
	public MoreSolPanel getMoreSolPanel() {
		return moreSolPanel;
	}
	
	/**
	 * Returns the panel where the solutions or kernel elements are shown on.
	 * 
	 * @return the panel where the solutions are shown on.
	 */
	public List<boolean[]> getShowList() {
		return showList;
	}

	/**
	 * Returns the game what we analyse.
	 * 
	 * @return the game what is analysed.
	 */
	public Game getGame() {
		return game;
	}


	/**
	 * Initializes a new MoreSolPanel with the given values which shall be shown there 
	 * and places it one the MainFrame in the middle.
	 * 
	 * @param showList2 the elements which shall be shown in the MoreSolPanel.
	 * @param undertexts the descriptions of the elements.
	 * @param type the type of the #MSPOption.
	 * @param title the title of the MoreSolPanel
	 */
	public void initMoreSolPanel(List<boolean[]> showList2, String[] undertexts, 
			int type, String title) {
		showList = showList2;
		JComponent[] solutionPanels= initSolutionPanels(showList);
		moreSolPanel = new MoreSolPanel(title,solutionPanels,undertexts);
		moreSolPanel.setOptionPanel(initOptionPanel(type));
		moreSolPanel.setTitleLabel(title);
		owner.putCentral(moreSolPanel);		
	}


}
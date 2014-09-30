package game;

import help.StringRepresentation;

import java.util.LinkedList;
import java.util.List;


/**
 * Describes a sigma-game or lights-out-game on an arbitrary graph.
 * A graph with n nodes and some edges between them is the basic of the game.
 * A click on one node changes the colour of all neighboured nodes, i.e. of all nodes connected through an edge.
 * The aim is to put all lights on.
 * The class provides some basic functions to examine the kernel and the solutions
 * of such games.
 * 
 * @author Irene Thesing
 */
public class Game {

	/**Graph which describes the sigma-game with nodes 0,...,n-1.*/
	protected GameGraph graph;
	
	/** Actual state of the game (which lights are on?), There state[i] defines whether the light on node i is on or not.*/
	protected boolean[] state;
	
	/**Number of nodes in the game.*/
	private int n;
	
	/**
	 * Remembers which nodes was already chosen from #beginRecord().
	 * The order is not relevant.
	 */
	protected boolean[] record;
	
	/**Whether the moves shall be recorded.*/
	protected boolean recordActive;
	
	/**
	 * Returns whether the record function is on.
	 * 
	 * @return whether the record function is on.
	 */
	public boolean isRecordActive() {
		return recordActive;
	}


	/**
	 * Sets the record function on or out.
	 * 
	 * @param recordActive whether the record function shall be activated.
	 */
	public void setRecordActive(boolean recordActive) {
		this.recordActive = recordActive;
	}


	/**
	 * Constructs a new game, only with nodes, without edges.
	 * The GameGraph where the edges are defined can be added later for example with #init().
	 * 
	 * @param numbStates number of nodes.
	 **/
	public Game(int numbStates){
		this.n=numbStates;
		this.state=new boolean[numbStates];
		for (int i=0;i<numbStates;i++){
			state[i]=false;
		}
	}
	
	
	/**
	 * Constructs a new game on the given graph with the given configuration as start values.
	 * 
	 * @param graph graph of the game.
	 * @param beginState states for the begin of the game, normally all false.
	 */
	public Game(GameGraph graph, boolean[] beginState){
		this.n= graph.getCountKnots();
		if (beginState.length != n)
			throw new IllegalArgumentException();
		this.graph=graph;
		this.state=beginState;
	}
	
	/**
	 * Constructs a new game on the given graph with an empty start configuration (all fields 0).
	 * 
	 * @param graph graph of the game.
	 */
	public Game(GameGraph graph){
		this.graph=graph;
		this.n= graph.getCountKnots();
		this.state=new boolean[n];
		for (int i=0;i<n;i++){
			state[i]=false;
		}
		//System.out.println(state.toString());
	}
	
	/**
	 * Initializes this sigma-game. All values are set, so the number of nodes, 
	 * the graph with the edges as g and a start configuration. Of course the 
	 * start configuration must have the same length as the count of nodes.
	 * 
	 * @param g the graph where should be played on.
	 * @param numNodes the number of nodes. 
	 * @param startConfig the start configuration for the game.
	 */
	protected void init(GameGraph g, int numNodes, boolean[] startConfig){
		this.graph = g;
		this.n= numNodes;
		if (n != startConfig.length)
			throw new IllegalArgumentException("Dimension of the Configuration does not fit.");
		this.state = startConfig;
	}
//TODO: Parameter numNodes überflüssig
	
	
	/**
	 * Checks if a given boolean vector is a solution of the game 
	 * in the actual state. Uses that an even number of ones 
	 * around the state switches the light again out.
	 * 
	 * @param solution a vector which shall be checked.
	 * @return whether the given vector is a solution of this game.
	 */
	public boolean checkSolution(boolean[] solution){
	//	boolean[] vector = this.graph.bmConnections.applyTo(solution);
		if (solution.length != n)
			return false;
		//wenn ringsherum eine gerade anzahl einsen ist, passt alles
		int countones = 0;
		for (int i=0; i<n;i++){
			countones = 0;
			for (int j: graph.getNeighbours(i)){
				if (solution[j])
					countones ++;
			}
			//anfangszustaende beachten
			if (!((countones%2==0 && state[i]) ||(countones%2==1 && !(state[i]))))
				return false;
		}
		return true;		
	}
	
	
	/**
	 * Checks if a given boolean vector is a kernel element of the game 
	 * in the actual state. Uses that an even number of ones 
	 * around the state switches the light again out.
	 * 
	 * @param solution a vector which shall be checked.
	 * @return whether the given vector is a kernel element of this game.
	 */
	public boolean checkKernel(boolean[] solution){
		if (solution.length != n)
			return false;
		//wenn ringsherum eine gerade anzahl einsen ist, passt alles
		int countones = 0;
		for (int i=0; i<n;i++){
			countones = 0;
			for (int j: graph.getNeighbours(i)){
				if (solution[j])
					countones ++;
			}
			if (!((countones%2==0 && !state[i]) ||(countones%2==1 && state[i])))
				return false;
		}
		return true;		
	}
	
	
	/**
	 * Calculates a linear independent system of kernel elements. 
	 * The trivial kernel element is given, too.
	 * 
	 * @return list with linear independent kernel elements and 
	 * the first element as zero, since it is the trivial kernel element.
	 */
	public List<boolean[]> getKernel(){
	//	boolean[][] matrix = this.graph.getConnections();
	/*	boolean[] result = new boolean[this.n];
		for (int i=0;i<n;i++){
			result[i] = false;
		}*/
		return this.graph.getBmConnections().computeKernelBase(); 
		//math.LGS.allSolutions(matrix, result);
		/*
		LinkedList<boolean[]> kernel = new LinkedList<boolean[]>();
		//Zahlen kleiner 2^n kodieren eine 0-1-Folge der Laenge n
		for (int nsol=0; nsol<Math.pow(2, n);nsol++){
			boolean[] solution = new boolean[n];
			int value = nsol;
			for (int i=n-1;i >= 0;i--){
				//wenn 1 an stelle i, true schreiben, sonst false
				if (value-Math.pow(2, i)>=0){
					value -= Math.pow(2, i);
					solution[i]=true;
				} else 
					solution[i]=false;
			}
			//Loesung ausprobieren
			if (checkCore(solution))
				kernel.add(solution);
		}
		return kernel;*/
	}
	
	/**
	 * Calculates all kernel elements, not only a base.
	 * For details see #BooleanMatrix.computeAllCore() .
	 * 
	 * @return a list of all kernel elements.
	 */
	public LinkedList<boolean[]> getAllKernel(){
		return this.graph.getBmConnections().computeAllKernel();
	}
	

	/**
	 * Calculates all possible solutions by calculating one solution and adding all kernel elements.
	 * 
	 * @return a list of all solutions.
	 */
	public LinkedList<boolean[]> getAllSolutions(){
		LinkedList<boolean[]> allSols = new LinkedList<boolean[]>();
		boolean[] result = new boolean[this.n];
		for (int i=0;i<n;i++){
			result[i] = true;
		}
		boolean[] sol = getOneSolution(result);
		for (boolean[] core: this.graph.getBmConnections().computeAllKernel()){
			allSols.add(BooleanMatrix.sumTwoVectors(core,sol));
		}
		return allSols;
	}
	
	
	/**
	 * Calculates some solutions of the game, in fact as many as the kernel dimension is.
	 * 
	 * @return list with solution elements.
	 */
	public List<boolean[]> getSolutions(){
		//boolean[][] matrix = this.graph.getConnections();
		boolean[] result = new boolean[this.n];
		for (int i=0;i<n;i++){
			result[i] = true;
		}
		return this.graph.getBmConnections().computeSolutions(result); 
		//math.LGS.allSolutions(matrix, result);
		/*LinkedList<boolean[]> kernel = new LinkedList<boolean[]>();
		//Zahlen kleiner 2^n kodieren eine 0-1-Folge der Laenge n
		for (int nsol=0; nsol<Math.pow(2, n);nsol++){
			boolean[] solution = new boolean[n];
			int value = nsol;
			for (int i=n-1;i >= 0;i--){
				//wenn 1 an stelle i, true schreiben, sonst false
				if (value-Math.pow(2, i)>=0){
					value -= Math.pow(2, i);
					solution[i]=true;
				} else 
					solution[i]=false;
			}
			//Loesung ausprobieren
			if (checkSolution(solution))
				kernel.add(solution);
			System.out.println(StringRepresentation.solToStr(solution));
		}
		return kernel;*/
	}
	
	
	

	/**
	 * Calculates solutions of the game beginning with the given configuration.
	 * 
	 * @param config start configuration.
	 * @return list with some solutions elements.
	 */
	public List<boolean[]> getSolutions(boolean[] config){
		//boolean[][] matrix = this.graph.getConnections();
		if (config.length != this.n)
			throw new IllegalArgumentException("The configuration does not fit to the game.");
		return this.graph.getBmConnections().computeSolutions(config); 
	}
	
	/**
	 * Calculates only one solution of the game beginning with the start configuration
	 * 
	 * @param config start configuration.
	 * @return a solution of Adj(#graph) X = config.
	 */
	public boolean[] getOneSolution(boolean[] config){
		//boolean[][] matrix = this.graph.getConnections();
		if (config.length != this.n)
			throw new IllegalArgumentException("The configuration does not fit to the game.");
		return this.graph.getBmConnections().computeOneSolution(config); 
	}
	
	/**
	 * Returns the actual state (or configuration) of the game.
	 * 
	 * @return the actual state of the game.
	 */
	public boolean[] getState() {
		return state;
	}

	/**
	 * Sets the actual state (or configuration) of the game.
	 * 
	 * @param state the new configuration for this game.
	 */
	public void setState(boolean[] state) {
		this.state = state;
	}

	/**
	 * Returns the graph of the game.
	 * 
	 * @return the graph of the game.
	 */
	public GameGraph getGraph() {
		return graph;
	}

	/**
	 * Returns the size of the game, i.e. the number of nodes.
	 * 
	 * @return the size of the game.
	 */
	public int getBig() {
		return n;
	}

	/**
	 * Returns the actual state of node i.
	 * 
	 * @return the actual state of node i.
	 */
	public boolean getValueAt(int i) {
		return state[i];
	}

	/**
	 * Clicks on a node and makes the correspdonding move. That is changing all boolean values of the
	 * fields around this node-
	 * 
	 * @param m node which is clicked.
	 */
	public void play(int m) {
		LinkedList<Integer> neighbours = graph.getNeighbours(m);
		for (int neigh: neighbours){
			state[neigh]= !state[neigh];
		}
		if (recordActive)
			record[m]= !(record[m]);
	}
		
	/**
	* The string representation of the game is done by the graph and the actual state.
	*
	*@return string representation of the game.
	*/
	public String toString(){
		return "Graph: "+graph +"\n"+ StringRepresentation.solToStr(state);
	}


	/**
	 * The game is won, if all nodes are marked, i.e. have the value true.
	 * This method return whether the game is won in the actual state or not,
	 * 
	 * @return whether the game is won in the actual state.
	 */
	public boolean isWon() {
		for (int i=0;i<n;i++){
			if (!state[i])
				return false;
		}
		return true;
	}
	
	
	/**
	 * Begins to record the moves (value true) and ends it (with value false).
	 * 
	 * @param activate whether record shall be started or ended.
	 */
	public void setRecord(boolean activate){
		recordActive = activate;
		if (activate){
			record = new boolean[n];
			for (int i=0;i<n;i++)
				record[i]=false;
		}
	}

	
	/**
	 * Returns a history of the moves (without order)
	 * from beginRecord() on. So the played moves can be visualized.
	 * 
	 * @return the record of the moves.
	 */
	public boolean[] getRecord(){
		return record;
	}
	

	/**
	 * Changes the actual state in the node with the given number to true if it was false
	 * and to false if it was true.
	 * 
	 * @param number the number of the node whose state shall be changed.
	 */
	public void changeState(int number) {
		state[number] = !state[number];
	}
}

package trisentis;

import game.BooleanMatrix;
import game.Game;
import game.GameGraph;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;



/**
 * A Trisentis game is played on a boolean matrix which is specified by the number of columns and the number of rows.
 * A click on one field turns all the neighbored fields, but not the field itself.
 * To put some solutions together it is of interest to examine the border of the applied solutions,
 * wherefore this class provides some methods to see it.
 * 
 * @author Irene Thesing
 *
 */
public class Trisentis extends Game {
		
	/**
	 * Number of columns of the game board.
	 */
	private int cols;
	
	/**Number of rows of the game board.*/
	private int rows;


	/**
	 * Describes whether the game is a Trisentis game or a sigma-game on a grid.
	 * It differences only in the defined neighborhoods:
	 * A field in the Trisentis game board has also diagonal neighbors, on a grid, these fields are not counted as neighbours.
	 */
	private static boolean isTrisentis = true;
	
	/**
	 * Returns whether the game is a Trisentis game or a sigma-game on a rectangular grid, i.e. whether diagonal neighboured fields
	 * are counted as neighbored.
	 * 
	 * @return whether the game is a Trisentis game or a sigma-game on a rectangular grid.
	 */
	public static boolean isTrisentis() {
		return isTrisentis;
	}


	/**
	 * Sets whether the game is a Trisentis game or a sigma-game on a rectangular grid.
	 * 
	 * @param isTrisentis whether a field in the middle has eight neighbors (Trisentis) or four (grid game).
	 */
	public static void setTrisentis(boolean isTrisentis) {
		Trisentis.isTrisentis = isTrisentis;
	}


	/**
	 * Creates a new Trisentis game with the given row and column number.
	 * 
	 * @param r number of rows. It must be greater than zero.
	 * @param c number of columns. It must be greater than zero.
	 */
	public Trisentis(int r, int c) {
		super(r*c);
		this.rows=r; this.cols=c;
		HashMap<Integer,LinkedList<Integer>> adjlists = new HashMap<Integer,LinkedList<Integer>>();
		int count = rows * cols;
		for (int i=0; i<rows;i++)
			for (int j=0; j<cols; j++){
				LinkedList<Integer> list = new LinkedList<Integer>();
				if (i-1 >= 0){
					list.add(cols*(i-1)+j);
					if (isTrisentis){
						if (j-1>=0)
							list.add(cols*(i-1)+j-1);
						if (j+1<cols)
							list.add(cols*(i-1)+j+1);
					}
				}
				if (i+1 < rows){
					list.add(cols*(i+1)+j);
					if (isTrisentis){
						if (j-1>=0)
							list.add(cols*(i+1)+j-1);
						if (j+1<cols)
							list.add(cols*(i+1)+j+1);
					}
				}
				if (j-1>= 0)
					list.add(cols*i+j-1);
				if (j+1<cols)
					list.add(cols*i+j+1);
				adjlists.put(i*cols+j, list);
			}
	
		this.graph= new GameGraph(count,adjlists);
	//	System.out.println(this);
		//System.out.println(this.getGraph().toString());
	}
	

	/**
	 * Creates a new Trisentis game with the given state.
	 * 
	 * @param configuration the state for the new Trisentis game.
	 */
	public Trisentis(BooleanMatrix configuration){
		this(configuration.getNumRows(),configuration.getNumCols());
		this.state = configuration.getMatrixAsBooleanArray();
	}
	
	
	/**
	 * Calculates some solutions of the Trisentis game as a list of matrices, this
	 * representation is characteristic for Trisentis games and not possible for most other sigma-games.
	 * 
	 * @return list of matrices, which are solutions of the Trisentis game. 
	 */
	public LinkedList<BooleanMatrix> getTriSolutions(){
		LinkedList<BooleanMatrix> ret= new LinkedList<BooleanMatrix>();
		//Sys2tem.out.println(this.getSolutions().toString());
		for (boolean[] sol: this.getSolutions()){
			//System.out.println(StringRepresentation.solToStr(sol));
			ret.add(new BooleanMatrix(sol,rows, cols));
		}
		return ret;
	}
	
	
	

	/**
	 * Calculates the base of the kernel of the Trisentis game as a list of matrices, this
	 * representation is characteristic for Trisentis games and not possible for most other sigma-games.
	 * 
	 * @return list of matrices, which form a base of the kernel of the Trisentis game. 
	 */
	public List<BooleanMatrix> getTriKernel() {
		LinkedList<BooleanMatrix> ret= new LinkedList<BooleanMatrix>();
		//Sys2tem.out.println(this.getSolutions().toString());
		for (boolean[] sol: this.getKernel()){
			//System.out.println(StringRepresentation.solToStr(sol));
			ret.add(new BooleanMatrix(sol,rows, cols));
		}
		return ret;
	}
	
	
	/**
	 * Calculates all symmetric kernel elements of the Trisentis game, this symmetric condition is meant in the first component.
	 * It is used that the first column determines the kernel element totally.
	 * 
	 * @return a list of the symmetric kernel elements.
	 */
	public List<boolean[]> getSymTriKernel(){
		List<boolean[]> allCore = this.getAllKernel();
		List<boolean[]> symElem = new LinkedList<boolean[]>();
		for (boolean[] matrix: allCore){
			if (this.isSymmetric(matrix)){
				symElem.add(matrix);
			}
		}
		return symElem;
	}
	
	/**
	 * Calculates all symmetric kernel elements of the Trisentis game, this symmetric condition is meant in the first component.
	 * It is used that the first column determines the kernel element totally. Additionally, the middle element in the first column
	 * must be one. It makes sense only for odd values of #rows.
	 * 
	 * @return a list of the symmetric kernel elements.
	 */
	public List<boolean[]> getSymTriKernelWithOne() {
		List<boolean[]> allCore = this.getAllKernel();
		List<boolean[]> symElem = new LinkedList<boolean[]>();
		for (boolean[] matrix: allCore){
			if (this.isSymmetric(matrix) && (matrix[((rows-1)/2)*cols])){
				symElem.add(matrix);
			}
		}
		return symElem;
	}
	
	
	/**
	 * Checks whether a boolean matrix kernel element is symmetric. It is used that the first column determines the kernel element totally.
	 * 
	 * @param mat matrix which shall be checked.
	 * @return true if it is symmetric in first component.
	 */
	private boolean isSymmetric(boolean[] mat){
		//System.out.println("Überprüfe Symmetrie");
		if (this.rows*cols != mat.length)
			throw new IllegalArgumentException("Dimensionen stimmen nicht.");
		for (int i=0; i< rows/2+1; i++){
			if (mat[i*cols] != mat[(rows-1-i)*cols])
				return false;
		}
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see game.Game#toString()
	 */
	public String toString(){
		return rows + "x"+cols + " Trisentis game" + (new BooleanMatrix(this.getState(), rows, cols)).toString();
	}

	/**
	 * Returns the number of columns of the game board.
	 * 
	 * @return number of columns of the Trisentis game.
	 */
	public int getCols() {
		return cols;
	}


	/**
	 * Returns the number of rows of the game board.
	 * 
	 * @return number of rows of the Trisentis game.
	 */
	public int getRows() {
		return rows;
	}
	
	
	
	
	
	/**
	 * Applies a solution to the zero state of the Trisentis game and returns a matrix
	 * which also includes an additional border and the corresponding values on it.
	 * It is helpful to examine whether we can put two solutions together.
	 * 
	 * @param solution solution which shall be applied on the zero matrix (the basic game).
	 * @return a matrix which contains also a border (the only interesting thing if it is really a solution or kernel element).
	 */
	public BooleanMatrix applySolWithBorder(BooleanMatrix solution){
		int newRows = rows+2;
		int newCols = cols+2;
		//ermöglicht einfachere Zugriffe
		BooleanMatrix resultMatrix = new BooleanMatrix(newRows,newCols);
	//	BooleanMatrix solutionMatrix = new BooleanMatrix(solution,rows,cols);
		//Zähle jeweils einsen bzw boolean-werte ringsrum
		for (int rn = 0; rn<newRows;rn++){
			for (int cn = 0;cn<newCols;cn++){
				boolean value = false;
				// zeile selber	
				if (rn > 0 && rn < rows+1){
					/*eigener Wert spielt keine Rolle
					 * if (cn> 0 && cn < cols +1)
						value = value ^ solutionMatrix.get(rn-1,cn-1);*/
					if (cn>1)
						value = value ^ solution.get(rn-1,cn-2);
					if (cn<cols)
						value = value ^ solution.get(rn-1,cn);
				}
				//darüber liegende Zeile
				if (rn > 1){
					if (cn> 0 && cn < cols +1)
						value = value ^ solution.get(rn-2,cn-1);
					if (cn>1)
						value = value ^ solution.get(rn-2,cn-2);
					if (cn<cols)
						value = value ^ solution.get(rn-2,cn);
						
				}
				//darunter liegende Zeile
				if (rn < rows){
					if (cn> 0 && cn < cols +1)
						value = value ^ solution.get(rn,cn-1);
					if (cn>1)
						value = value ^ solution.get(rn,cn-2);
					if (cn<cols)
						value = value ^ solution.get(rn,cn);
				}
				resultMatrix.set(rn,cn,value);
			}
		}
		return resultMatrix;
	}
	
	
	/**
	 * Applies a list of solutions to the zero state of the Trisentis game and returns a list of matrices
	 * which also include an additional border and the corresponding values on it.
	 * It is helpful to examine whether we can put two solutions together.
	 * 
	 * @param solutions solutions which shall be applied on the zero matrix (the basic game).
	 * @return a list of matrices which contains also a border (the only interesting thing).
	 */
	public LinkedList<BooleanMatrix> applyAllSolsWithBorder(List<BooleanMatrix> solutions){
		LinkedList<BooleanMatrix> result = new LinkedList<BooleanMatrix>();
		for (BooleanMatrix sol: solutions)
			result.add(this.applySolWithBorder(sol));
		return result;
	}


	/**
	 * Look at #applySolWithBorder, here only the arguments are taken as boolean arrays.
	 * 
	 * @param configuration
	 * @return applies the configuration on the matrix with border.
	 */
	public boolean[] applySolWithBorder2(boolean[] configuration) {
		BooleanMatrix matrix = this.applySolWithBorder(new BooleanMatrix(configuration,rows,cols));
		return matrix.getMatrixAsBooleanArray();
	}


}

package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 * The class represents a matrix with boolean entries.
 * It transforms it also in row-echolon form to make it easier to use the Gauss-algorithm several times.
 * This class provides some methods to calculate solutions of equations with the represented matrix involved
 * and some static methods to produce special matrices.
 * 
 * @author Irene Thesing
 *
 */
/**
 * @author grabowski
 *
 */
public class BooleanMatrix {

	/** 
	 * Entries of the matrix and the transformed matrix.
	 * Above there are the entries of the matrix, next there follow the entries of the identity matrix.
	 * After transformation first comes the matrix in re-normalform, then the transformation matrix.
	 */
	private boolean[] entries;

	/** Number of rows in matrix. */
	private int numRows;

	/** Mumber of columns in matrix. */
	private int numCols;

	/** Whether the matrix has been transformed to row-echelon form. */
	private boolean inREForm = false;

	/**
	 * Creates a new boolean matrix with given size and initializes all entries with zero.
	 * @param size size of new matrix.
	 */
	public BooleanMatrix(int size) {
		this(size,size);
	}
	

	/**
	 * Creates a new boolean matrix with given sizes and initializes all entries with zero.
	 * 
	 * @param numRows number of rows in new matrix.
	 * @param numCols number of columns in new matrix.
	 */
	public BooleanMatrix(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.entries = new boolean[2*numRows*numCols];
		for (int i=0; i<numRows; i++) {
			for (int j=0; j<numCols; j++) {
				set(i,j, zero());
				set(i+numRows, j, (i==j));
			}

		}
	}
	
	/**
	 * Creates a new boolean matrix out of the given entries in mat
	 * with the given number of rows and number of columns.
	 * 
	 * @param mat a boolean array with the entries of the matrix, column-wise.
	 * @param numRows number of rows in new matrix.
	 * @param numCols number of columns in new matrix.
	 */
	public BooleanMatrix(boolean[] mat, int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		if (numRows*numCols != mat.length)
			throw new IllegalArgumentException("Matrix size does not fit to other parameters.");
		this.entries = mat;
	}
	
	/**
	 * Creates a new boolean matrix out of a matrix with boolean values.
	 * 
	 * @param mat which shall be put in the Boolean Matrix
	 */
	public BooleanMatrix(boolean[][] mat) {
		this.numRows = mat.length;
		if (mat.length == 0){
			this.entries = new boolean[0];
			this.numCols = 0;
			return;
		}
		this.numCols = mat[0].length;
		this.entries = new boolean[2*numRows*numCols];
		for (int i=0; i<numRows; i++) {
			for (int j=0; j<numCols; j++) {
				set(i,j, mat[i][j]);
				set(i+numRows, j, (i==j));
			}
		}
	}
	
	/**
	 * Reinitializes the matrix with the given entries.
	 * 
	 * @param newEntries new entries.
	 */
	public void setEntries(boolean[] newEntries) {
		if (newEntries.length!=numRows*numCols)
			throw new IllegalArgumentException("wrong size of array!");
		this.inREForm = false;
		for (int i=0; i<numRows; i++)
			for (int j=0; j<numCols; j++) {
				set(i,j,newEntries[i*numCols+j]);
				set(i+numRows, j, (i==j));
			}	
	}

	/**
	 * Returns the number of rows in this matrix.
	 * @return the number of rows in this matrix.
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * Returns the number of columns in this matrix.
	 * @return the number of columns in this matrix.
	 */
	public int getNumCols() {
		return numCols;
	}

	/**
	 * Returns the entry at row i and column j.
	 * @param i row index.
	 * @param j column index.
	 * @return entry at row i and column j.
	 */
	public boolean get(int i, int j) {
		return entries[numCols*i+j];
	}


	/**
	 * Sets the entry at row i and column j to given value.
	 * @param i row index of entry to set.
	 * @param j column index of entry to set.
	 * @param value new value of entry.
	 */
	public void set(int i, int j, boolean value) {
		entries[numCols*i+j] = value;
	}

	/**
	 * Adds row i2 to row i1.
	 * @param i1 row to add other row to.
	 * @param i2 row to add.
	 */
	protected void addRows(int i1, int i2) {
		for (int j=0; j<numCols; j++) {
			set(i1,j, sum(get(i1,j), get(i2,j)));
			set(numRows+i1,j, sum(get(numRows+i1,j), get(numRows+i2,j)));
		}
	}

	/**
	 * Returns the sum of two booleans. It is simply the operation XOR.
	 * @param a first booleans.
	 * @param b second booleans.
	 * @return sum of the given booleans.
	 */
	private static boolean sum(boolean a, boolean b) {
		return a!=b;
	}

	/**
	 * Returns the product of two booleans. The product corresponds to AND.
	 * @param a first booleans.
	 * @param b second booleans.
	 * @return sum of the given booleans.
	 */
	private static boolean prod(boolean a, boolean b) {
		return a && b;
	}


	/**
	 * Helper method to get the zero value, what is false.
	 * @return false.
	 */
	private boolean zero() {
		return false;
	}
	
	/**
	 * Helper method to get the one value, what is true.
	 * @return true.
	 */
	private boolean one() {
		return true;
	}

	/**
	 * Swaps row i1 and row i2.
	 * @param i1 index of first row to swap.
	 * @param i2 index of second row to swap.
	 */
	protected void swapRows(int i1, int i2) {
		boolean tmp;
		for (int j=0; j<numCols; j++) {
			tmp = get(i1,j);
			set(i1,j,get(i2,j));
			set(i2,j,tmp);
			tmp = get(numRows+i1,j);
			set(numRows+i1,j,get(numRows+i2,j));
			set(numRows+i2,j,tmp);
		}
	}

	/**
	 * Applies the original matrix to given column vector.
	 * @param vector column vector to apply this matrix to.
	 * @return image of the given column vector under this matrix.
	 */
	public boolean[] applyTo(boolean[] vector) {
		return applyTo(applyTo(vector, false),true);
	}
	
	

	/**
	 * Helper method - applies a certain portion of the matrix to given column vector.
	 * @param v column vector to apply this matrix to.
	 * @param trans if true, then transformation matrix is applied - if false, then the matrix itself is applied.
	 * @return image of the given column vector under this matrix or the transformation matrix, respectively.
	 */
	private boolean[] applyTo(boolean[] v, boolean trans) {
		if (v.length!=numRows)
			throw new IllegalArgumentException("Cannot apply matrix to vector! Incompatible sizes!");

		int off;
		if (trans)
			off = numRows;
		else
			off = 0;
		boolean[] ret = new boolean[numRows];
		for (int i=0; i<numRows; i++) {
			ret[i] = zero();
			for (int j=0; j<numCols; j++)
				ret[i] = sum(ret[i], prod(get(i+off,j), v[j]));
		}
		return ret;
	}

	/**
	 * Returns a list of independent solutions.
	 * 
	 * @param rightSide vector b of the equality Ax=b.
	 * @return list of linear independent solutions.
	 */
	public List<boolean[]> computeSolutions(boolean[] rightSide) {
		if (rightSide.length != this.numRows)
			throw new IllegalArgumentException("Längen zur Multiplikation Matrix mit Vector müssen passen!");
		if (!inREForm)
			transform2REForm();

		boolean[] rhsTrans = applyTo(rightSide, true);

		for (int i=0; i<numRows; i++) {
			boolean allZero = true;
			for (int j=0; j<numCols; j++) {
				if (get(i,j)) {
					allZero = false;
					break;
				}
			}
			if (allZero && rhsTrans[i])
				return Collections.<boolean[]>emptyList(); // no solution
		}
		
		//check which variables are independent

		List<Integer> dependent = new ArrayList<Integer>();
		List<Integer> independent = new ArrayList<Integer>();

		Map<Integer,Integer> depEq = new HashMap<Integer, Integer>();
		for (int i=numRows-1; i>=0; i--) {
			int j=0;
			while (j<numCols && !get(i,j))
				j++;
			if (j==numCols)
				continue; // ignore zero row
			else {
				dependent.add(j);
				depEq.put(j,i);
			}
		}

		for (int j=numCols-1; j>=0; j--)
			independent.add(j);

		for (Integer j: dependent)
			independent.remove(j);

		List<boolean[]> ret = new ArrayList<boolean[]>();
		boolean[] solZero = new boolean[numCols];
		for (Integer q: independent)
			solZero[q] = zero();

		ret.add(solZero);

		for (Integer p: independent) {
			boolean[] sol = new boolean[numCols];
			for (Integer q: independent)
				sol[q] = (p.equals(q))?one():zero(); // initialize independent variables with unit vector
				ret.add(sol);
		}

		for (boolean[] sol: ret) {
			for (Integer k: dependent) {
				sol[k] = rhsTrans[depEq.get(k)];
				for (int j=k+1; j<numCols; j++)
					sol[k] = sum(sol[k], prod(get(depEq.get(k),j),sol[j]));
			}
		}

		return ret;
	}

	
	/**
	 * Returns only one solution, so does not need to make a long list of solutions.
	 * The independent fields are set zero.
	 * 
	 * @param rightSide vector b of the equality Ax=b.
	 * @return one solution.
	 */
	public boolean[] computeOneSolution(boolean[] rightSide) {
		if (rightSide.length != this.numRows)
			throw new IllegalArgumentException("Längen zur Multiplikation Matrix mit Vector müssen passen!");
		if (!inREForm)
			transform2REForm();

		boolean[] rhsTrans = applyTo(rightSide, true);

		for (int i=0; i<numRows; i++) {
			boolean allZero = true;
			for (int j=0; j<numCols; j++) {
				if (get(i,j)) {
					allZero = false;
					break;
				}
			}
			if (allZero && rhsTrans[i])
				return null; // no solution
		}
		
		//check which variables are independent

		List<Integer> dependent = new ArrayList<Integer>();
		List<Integer> independent = new ArrayList<Integer>();

		Map<Integer,Integer> depEq = new HashMap<Integer, Integer>();
		for (int i=numRows-1; i>=0; i--) {
			int j=0;
			while (j<numCols && !get(i,j))
				j++;
			if (j==numCols)
				continue; // ignore zero row
			else {
				dependent.add(j);
				depEq.put(j,i);
			}
		}

		for (int j=numCols-1; j>=0; j--)
			independent.add(j);

		for (Integer j: dependent)
			independent.remove(j);

		boolean[] sol = new boolean[numCols];
		for (Integer q: independent)
			sol[q] = zero();

		
			for (Integer k: dependent) {
				sol[k] = rhsTrans[depEq.get(k)];
				for (int j=k+1; j<numCols; j++)
					sol[k] = sum(sol[k], prod(get(depEq.get(k),j),sol[j]));
			}
	
		return sol;
	}
	
	/**
	 * Transforms the matrix to the row-echelon form using the Gauss algorithm.
	 */
	public void transform2REForm() {

		for (int i=0; i<numRows && i<numCols; i++) {
			int j0=i;
			int i0=i;

			// look for non-zero entry right below of (i,i)
			while (!get(i0,j0) && j0<numCols) {
				while (i0<numRows && !get(i0,j0))
					i0++;
				if (i0==numRows) {
					j0++;
					i0=i;
				}
				else
					break;
			}

			if (j0==numCols) {
				inREForm = true;
				return;
			}

			swapRows(i,i0);

			for (int i1=i+1; i1<numRows; i1++)
				if (get(i1,j0))
					addRows(i1,i);
		}

		inREForm = true;
	}

	/**
	 * Returns the unity matrix of the given size.
	 * @param size size of unity matrix to return.
	 * @return unity matrix of the given size.
	 */
	public static BooleanMatrix getUnityMatrix(int size) {
		BooleanMatrix ret = new BooleanMatrix(size);
		for (int i=0; i<size; i++)
			ret.set(i,i,true);
		return ret;
	}

	/**
	 * Returns the adjacency matrix of the square with the given size.
	 * @param size size of square.
	 * @return the adjacency matrix of the square with the given size.
	 */
	public static BooleanMatrix getAMOfSquare(int size) {
		return getAMOfRect(size,size);
	}

	/**
	 * Returns the adjacency matrix of the Trisentis game with the given sizes.
	 * 
	 * @param numRows number of rows in the rectangle.
	 * @param numCols number of columns in the rectangle.
	 * @return the adjacency matrix of the square with the given size.
	 */
	public static BooleanMatrix getAMOfRect(int numRows, int numCols) {
		BooleanMatrix ret = new BooleanMatrix(numRows*numCols);
		for (int i=0; i<numRows; i++)
			for (int j=0; j<numCols; j++)
				for (int di=-1; di<=1; di++)
					for (int dj=-1; dj<=1; dj++) {
						int i0 = i+di;
						int j0 = j+dj;
						if (i0>=0 && i0<numRows && j0>=0 && j0<numCols && (di!=0 || dj!=0))
							ret.set(i*numCols+j, i0*numCols+j0, true);
					}
		return ret;		
	}
	
	/**
	 * calculates in every entry the sum of the corresponding entries 
	 * in the given matrices.
	 * Please only call this method if you have checked that 
	 * all matrices have the same dimension.
	 * 
	 * @param matrices list of matrices.
	 * @return sum of the matrices, entry-wise in F_2.
	 */
	public static BooleanMatrix sumMatrices(List<BooleanMatrix> matrices){
		if (matrices.isEmpty())
			return null;//throw new IllegalArgumentException("No matrices selected.");
		int rows = matrices.get(0).numRows;
		int cols = matrices.get(0).numCols;
		BooleanMatrix result = new BooleanMatrix(rows,cols);
		for (int i=0;i<rows;i++)
			for (int j=0;j<cols;j++){
				boolean sum = false;
				for (BooleanMatrix matrix: matrices){
					if (matrix.numRows != rows || matrix.numCols != cols)
						throw new IllegalArgumentException("Matrix dimensions does not fit.");
					sum = sum(sum,matrix.get(i, j));
				}
				result.set(i, j,sum);
			}
			
		return result;
	}
	
	/**
	 * Returns the boolean matrix as boolean array, all values are ordered row-wise. 
	 * 
	 * @return the Boolean Matrix as boolean array.
	 */
	public boolean[] getMatrixAsBooleanArray(){
		boolean[] result = new boolean[numRows*numCols];
		for (int i= 0;i<numRows;i++){
			for (int j= 0;j<numCols;j++){
				result[i*numCols+j]= get(i,j);
			}
		}
		return result;
	}
	
	/**
	 * Converts a boolean matrix into a boolean array.
	 * 
	 * @param matrix matrix which shall be converted into a boolean array.
	 * @return boolean array the resulting array, contains the matrix row by row.
	 */
	public static boolean[] changeBack(boolean[][] matrix){
		int rows = matrix.length;
		int cols = matrix[0].length;
		boolean[] result = new boolean[rows*cols];
		for (int i= 0;i<rows;i++){
			for (int j= 0;j<cols;j++){
				result[i*cols+j]= matrix[i][j];
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s="Matrix:\n";
		for (int i=0; i<numRows;i++){
			s+="[";
			for (int j=0;j<numCols;j++){
				if (this.get(i, j))
					s+="1,";
				else s+= "0,";
			}
			s+="]\n";
		}
		return s;
	}
	
	
	/**
	 * Returns all entries even the intern only for calculating as a string.
	 * 
	 * @return all entries even the intern only for calculating as a string.
	 */
	public String toStringAll(){
		String s="Matrix:\n";
		for (int i=0; i<2*numRows;i++){
			if (i==numRows)
				s+= "-------------\n";
			s+="row "+i+": [";
			for (int j=0;j<numCols;j++){
				if (this.get(i, j))
					s+="1,";
				else s+= "0,";
			}
			s+="]\n";
		}
		return s;
	}
	
	
	/**
	 * Checks a list of vectors for linear independence. 
	 * They are linear dependent if there is a non-trivial 
	 * linear combination giving zero. 
	 * This check writes all vectors as a matrix and makes a Gauss-elimination.
	 * 
	 * @param list list of vectors which shall be checks for linear independence. 
	 * All vectors must have the same length.
	 * @return whether the given vectors are linear independent.
	 */
	public static boolean isLinearIndependent(List<boolean[]> list){
		//keine Vectoren sind linear unabhängig
		//System.out.println("Prüfe lineare Unabhängigkeit folgender Vektoren:\n"+ StringRepresentation.listToStr(list));
		if (list.isEmpty())
			return true;
		//erstelle Matrix mit Spalten = angegebene Vektoren
		int size = list.size();
		int rowcount = list.get(0).length;
		BooleanMatrix newmatrix = new BooleanMatrix(rowcount,size);
		for (int col=0;col<size;col++){
			for (int row=0;row<rowcount;row++){
				newmatrix.set(row, col, list.get(col)[row]);
			}
		}
		
		//in rechte obere Dreiecksgestalt bringen
		newmatrix.transform2REForm();
		//Überprüfe ob letzte Zeile der transformierten Matrix alle 0 bzw. falsch sind
		//System.out.println(newmatrix.toStringAll());
		
		if (rowcount < size){
			return false;
		} else {
			//System.out.println("rowcount = "+rowcount+ "size="+size + " rowcount+size-1,size-1:"+newmatrix.get(rowcount+size-1,size));
		/*	for (int i=rowcount;i<2*rowcount;i++){
				if (newmatrix.get(i,size-1))
					return true;
			}
			return false;*/
			if (newmatrix.get(size-1, size-1))
				return true;
			else return false;
		}
	}


	/**
	 * Calculates the sum of two vectors in F_2.
	 * 
	 * @param applyTo first vector.
	 * @param applyTo2 second vector.
	 * @return the sum of both vectors.
	 */
	public static boolean[] sumTwoVectors(boolean[] applyTo, boolean[] applyTo2) {
		if (applyTo.length != applyTo2.length)
			throw new IllegalArgumentException("The length must be the same.");
		int n= applyTo.length;
		boolean[] result = new boolean[n];
		for (int i=0;i<n;i++)
			result[i] = (applyTo[i] != applyTo2[i]); 
		return result;
	}
	
	/**
	 * Calculates the sum of arbitrary many vectors in F2, they must have the same length and must be one ore more.
	 * 
	 * @param vectors the list of vectors where the sum shall be calculated.
	 * @return the sum of the given vectors.
	 */
	public static boolean[] sumVectors(List<boolean[]> vectors) {
		if (vectors.isEmpty())
			throw new IllegalArgumentException("Nothing to calculate.");
		boolean[] result = vectors.get(0);
		for (int i=1; i< vectors.size();i++)
			result = sumTwoVectors(result,vectors.get(i)); 
		return result;
	}
	
	
	

	
	/**
	 * Calculates a base of the kernel space.
	 * 
	 * @return a list of base elements of the kernel space.
	 */
	public List<boolean[]> computeKernelBase(){
		boolean[] result = new boolean[this.numRows];
		for (int i=0;i<numRows;i++){
			result[i] = false;
		}
		return this.computeSolutions(result);
	}
	
	/**
	 * Computes all elements of the kernel, not only a linear independent base.
	 * It works by calculating linear combinations of the basic kernel elements.
	 * 
	 * @return a list of all kernel elements.
	 */
	public LinkedList<boolean[]> computeAllKernel(){
		List<boolean[]> indCore = this.computeKernelBase();
		indCore.remove(0);
		return allLinearCombinations(indCore);
	}
	


	/**
	 * Calculates all possible linear combinations of the list of basic elements.
	 * 
	 * @param basics basic elements which shall be combined.
	 * @return a list of all possible linear combinations with the basic elements.
	 */
	protected static LinkedList<boolean[]> allLinearCombinations(List<boolean[]> basics){
		if (basics.isEmpty())
			return new LinkedList<boolean[]>();
	//	int rows = basics.get(0).getNumRows(); 
	//	int cols = basics.get(0).getNumCols();
		int vlength = basics.get(0).length;
		LinkedList<boolean[]> result = new LinkedList<boolean[]>();
		boolean[][] allCombos = initalizeAllBooleanArrays(basics.size());
		for (boolean[] combo: allCombos){
			boolean[] sumVector = new boolean[vlength];
			for (int i=0;i<combo.length;i++){
				//falls i in dem Kombinationsvektor drin ist, Matrix i zu der bisherigen Summe addieren
				if (combo[i])
					for (int r=0;r<vlength; r++){
						sumVector[r] = sumVector[r] ^ basics.get(i)[r];
						/*for (int c=0;c<cols;c++)
							sumVector.set(r, c, sumVector.get(r,c) ^ basics.get(i).get(r, c));*/
					}
			}
			result.add(sumVector);
		}
		//System.out.println("Result:"+result.size());
		return result;
	}
	
	/**
	 * Constructs an array with all boolean arrays of a certain length
	 * which can be made with boolean values.
	 * 
	 * @param size length of the boolean arrays.
	 * @return an array with all possible boolean arrays of length size.
	 */
	public static boolean[][] initalizeAllBooleanArrays(int size){
		boolean[][] result = new boolean[(int)(Math.pow(2,size))][size];
		write(result,0,result.length,0,size);
		//System.out.println(StringRepresentation.bolToStr(result));
		return result;
	}
	

	/**
	 * Helper recursive method for {@link #initalizeAllBooleanArrays(int)}.
	 *
	 * @param array the array to write in.
	 * @param leftBorder left border of initalize section.
	 * @param rightBorder right border of initalize section.
	 * @param cipher the actual iteration number.
	 * @param innerArraySize size of the array which is done in this method.
	 */
	private static void write(boolean[][] array, int leftBorder, int rightBorder, int cipher, int innerArraySize){
		if (cipher == innerArraySize)
			return;
		int half = (int) (leftBorder+(rightBorder-leftBorder)/2);
		for (int i= leftBorder; i<half;i++){
			array[i][cipher]= true;
		}
		write(array,leftBorder,half,cipher+1,innerArraySize);
		for (int i= half; i<rightBorder;i++){
			array[i][cipher]= false;
		}
		write(array,half,rightBorder,cipher+1,innerArraySize);
	}


}

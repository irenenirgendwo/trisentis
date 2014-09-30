package trisentis;

import game.BooleanMatrix;
import help.Pair;
import help.StringRepresentation;

import java.util.LinkedList;
import java.util.List;


/**
 * The class provides some more methods to examine the Trisentis game,
 * especially to put two solutions together to a new solution
 * of a bigger Trisentis game.
 * 
 * @author Irene Thesing
 */
public class TriExaminer {
	
	
	/**
	 * The game which shall be examined.
	 */
	private Trisentis game;
	
	/**
	 * Creates a new TriExaminer with the given game.
	 * 
	 * @param game the game to examine.
	 */
	public TriExaminer(Trisentis game){
		this.game = game;
	}
	
	/**
	 * Tries all possibilities to put some kernel elements
	 * from two Trisentis games over each other so that
	 * in there is a new kernel element.
	 * 
	 * @return all possible breaking points, where we can divide the given game
	 * into two games (the above matrix has as many rows what this number is).
	 */
	public List<Integer> zusammensetzenOU(){
		int rows = game.getRows();
		int cols = game.getCols();
		List<Integer> result = new LinkedList<Integer>();
		for (int i=1;i<rows-1;i++){
			Trisentis gameO = new Trisentis(i,cols);
			Trisentis gameU = new Trisentis (rows-i-1,cols);
			System.out.println(gameO);
			System.out.println(gameU);
			List<Pair<BooleanMatrix, BooleanMatrix>> sols = solTogetherOU(gameO, gameU);
			if (!sols.isEmpty()){
				System.out.println("Zusammensetzung Stufe" + i + " sol: " + sols.get(0).toString());
				result.add(i);
				System.out.println(i);
			}
		}
	//	System.out.println(result);
		return result;
	}
	

	/**
	 * Calculates pair in the kernel of a matrix in left Trisentis game which fit together
	 * to one in the right Trisentis game so that the border of the result of both matrizes
	 * form a zero line between it. Both matrizes must have the same row number rows.
	 * The solution is of dimension rows x gameL.cols + 1 + gameR.cols.
	 * 
	 * @param gameL left Trisentis game
	 * @param gameR right Trisentis game
	 * @return all pairs of linear combinations which fit together
	 */
	public static List<Pair<BooleanMatrix,BooleanMatrix>> coreTogehterLR(Trisentis gameL, Trisentis gameR){
		if (gameL.getRows() != gameR.getRows())
			throw new IllegalArgumentException("Dimensionsfehler: Matrizen brauchen die gleiche Zeilenanzahl.");
		List<BooleanMatrix> coreL =  gameL.getTriKernel();
		coreL.remove(0); //coreR.remove(0);
		List<BooleanMatrix> coreR = gameR.getTriKernel();
		List<BooleanMatrix> linearCombosL = allLinearCombinations(coreL);
		List<BooleanMatrix> linearCombosR = allLinearCombinations(coreR);
		List<Pair<BooleanMatrix,BooleanMatrix>> result = new LinkedList<Pair<BooleanMatrix,BooleanMatrix>>();
		for (BooleanMatrix first:linearCombosL){
			BooleanMatrix firstMatrix = gameL.applySolWithBorder(first);
			for (BooleanMatrix second: linearCombosR){
				BooleanMatrix secondMatrix = gameR.applySolWithBorder(second);
				boolean fits = true;
				for (int i=1;i<secondMatrix.getNumRows()-1;i++){
					if ((firstMatrix.get(i, gameL.getCols()+1)^secondMatrix.get(i,0))){
						fits = false;
						break;
					}
				}
				if (fits)
					result.add(new Pair<BooleanMatrix,BooleanMatrix>(first, second));
			}
		}
		System.out.println(result.toString());
		return result;
	}
	
	
	
	/**
	 * Searches all solutions of an mxn Trisentis game which can be but together to a new solution
	 * of the mx(2n+1) Trisentis game. It is done by taking one solution of the mxn Trisentis game
	 * and producing the other solutions by adding all kernel elements. Then the overlappings on the border are compared,
	 * if they fit, the pair is added.
	 * 
	 * @return all pairs of neighboured solutions which present a mx(2n+1) solution
	 */
	public static List<Pair<BooleanMatrix,BooleanMatrix>> solTogetherLR(Trisentis game1, Trisentis game2){
		if (game1.getRows() != game2.getRows())
			throw new IllegalArgumentException("Matrix Dimensionen müssen in der Zeilenzahl übereinstimmen.");
		List<BooleanMatrix> core1 =  game1.getTriKernel();
		List<BooleanMatrix> core2 =  game2.getTriKernel();
		List<BooleanMatrix> linearCombos1 = allLinearCombinations(core1);
		List<BooleanMatrix> linearCombos2 = allLinearCombinations(core2);
		List<Pair<BooleanMatrix,BooleanMatrix>> result = new LinkedList<Pair<BooleanMatrix,BooleanMatrix>>();
		List<BooleanMatrix> sol1 = game1.getTriSolutions();
		List<BooleanMatrix> sol2 = game2.getTriSolutions();
		if (sol1.isEmpty() || sol2.isEmpty())
			return result; 
		//eine Lösung nehmen
		BooleanMatrix solution1 = sol1.get(0);
		BooleanMatrix solution2 = sol2.get(0);
		//kern-elemente addieren und zwar beliebig, dann gibts auch alle lösungen
		for (BooleanMatrix first:linearCombos1){
			List<BooleanMatrix> list = new LinkedList<BooleanMatrix>();
			list.add(first);
			list.add(solution1);
			BooleanMatrix firstSol = BooleanMatrix.sumMatrices(list);
			BooleanMatrix firstMatrix = game1.applySolWithBorder(firstSol);
			for (BooleanMatrix second: linearCombos2){
				list.clear();
				list.add(second);
				list.add(solution2);
				BooleanMatrix secondSol = BooleanMatrix.sumMatrices(list);
				BooleanMatrix secondMatrix = game2.applySolWithBorder(secondSol);
				boolean fits = true;
				for (int i=1;i<secondMatrix.getNumRows()-1;i++){
					if (!(firstMatrix.get(i, game1.getCols()+1)^secondMatrix.get(i,0))){
						fits = false;
						break;
					}
				}
				if (fits)
					result.add(new Pair<BooleanMatrix,BooleanMatrix>(firstSol,secondSol));
			}
		}
		System.out.println(result.toString());
		return result;
	}
	
	
	/**
	 * Calculates all pairs of the kernel of two Trisentis games,
	 * where the first game is above the second and one row 
	 * between both games. 
	 * So the effects of the kernel elements overlap at this row.
	 * We search the pairs where the effects absorb each other,
	 * so the new configuration is again a kernel element.
	 * The number of columns must be the same in both games.
	 * 
	 * @param gameO above Trisentis game.
	 * @param gameU below Trisentis game.
	 * @return all pairs of linear combinations which fit together.
	 */
	public static List<Pair<BooleanMatrix,BooleanMatrix>> coreTogetherOU(Trisentis gameO, Trisentis gameU){
		if (gameO.getCols() != gameU.getCols())
			throw new IllegalArgumentException("Dimensionsfehler: Matrizen brauchen die gleiche Zeilenanzahl.");
		List<BooleanMatrix> coreL =  gameO.getTriKernel();
		List<BooleanMatrix> coreR = gameU.getTriKernel();
		List<BooleanMatrix> linearCombosL = allLinearCombinations(coreL);
		List<BooleanMatrix> linearCombosR = allLinearCombinations(coreR);
		List<Pair<BooleanMatrix,BooleanMatrix>> result = new LinkedList<Pair<BooleanMatrix,BooleanMatrix>>();
		for (BooleanMatrix first:linearCombosL){
			BooleanMatrix firstMatrix = gameO.applySolWithBorder(first);
			for (BooleanMatrix second: linearCombosR){
				BooleanMatrix secondMatrix = gameU.applySolWithBorder(second);
				boolean fits = true;
				for (int i=1;i<secondMatrix.getNumRows()-1;i++){
					if ((firstMatrix.get(gameO.getRows()+1,i)^secondMatrix.get(0,i))){
						fits = false;
						break;
					}
				}
				if (fits)
					result.add(new Pair<BooleanMatrix,BooleanMatrix>(first,second));
			}
		}
	//	System.out.println(result.toString());
		return result;
	}
	
	/**
	 * Searches all solutions of an mxn Trisentis game which can be but together to a new solution
	 * of the mx(2n+1) Trisentis game. It is done by taking one solution of the mxn Trisentis game
	 * and producing the other solutions by adding all kernel elements. Then the overlappings on the border are compared,
	 * if they fit, the pair is added.
	 * 
	 * @return all pairs of neighboured solutions which present a mx(2n+1) solution
	 */
	public static List<Pair<BooleanMatrix,BooleanMatrix>> solTogetherOU(Trisentis game1, Trisentis game2){
		if (game1.getCols() != game2.getCols())
			throw new IllegalArgumentException("Matrix Dimensionen müssen in der Spaltenzahl übereinstimmen.");
		List<BooleanMatrix> core1 =  game1.getTriKernel();
		List<BooleanMatrix> core2 =  game2.getTriKernel();
		List<BooleanMatrix> linearCombos1 = allLinearCombinations(core1);
		List<BooleanMatrix> linearCombos2 = allLinearCombinations(core2);
		List<Pair<BooleanMatrix,BooleanMatrix>> result = new LinkedList<Pair<BooleanMatrix,BooleanMatrix>>();
		List<BooleanMatrix> sol1 = game1.getTriSolutions();
		List<BooleanMatrix> sol2 = game2.getTriSolutions();
		if (sol1.isEmpty() || sol2.isEmpty())
			return result; 
		//eine Lösung nehmen
		BooleanMatrix solution1 = sol1.get(0);
		BooleanMatrix solution2 = sol2.get(0);
		//kern-elemente addieren und zwar beliebig, dann gibts auch alle lösungen
		for (BooleanMatrix first:linearCombos1){
			List<BooleanMatrix> list = new LinkedList<BooleanMatrix>();
			list.add(first);
			list.add(solution1);
			BooleanMatrix firstSol = BooleanMatrix.sumMatrices(list);
			BooleanMatrix firstMatrix = game1.applySolWithBorder(firstSol);
			for (BooleanMatrix second: linearCombos2){
				list.clear();
				list.add(second);
				list.add(solution2);
				BooleanMatrix secondSol = BooleanMatrix.sumMatrices(list);
				BooleanMatrix secondMatrix = game2.applySolWithBorder(secondSol);
				boolean fits = true;
				for (int i=1;i<secondMatrix.getNumRows()-1;i++){
					if (!(firstMatrix.get(game1.getRows()+1,i)^secondMatrix.get(0,i))){
						fits = false;
						break;
					}
				}
				if (fits)
					result.add(new Pair<BooleanMatrix,BooleanMatrix>(firstSol,secondSol));
			}
		}
	//	System.out.println(result.toString());
		return result;
	}
	
	

	
	/**
	 * Calculates all possible linear combinations of the 
	 * list of basic elements.
	 * 
	 * @param basics basic elements which shall be combined.
	 * @return a list of all possible linear combinations with the basic elements.
	 */
	protected static List<BooleanMatrix> allLinearCombinations(List<BooleanMatrix> basics){
		if (basics.isEmpty())
			return new LinkedList<BooleanMatrix>();
		int rows = basics.get(0).getNumRows(); 
		int cols = basics.get(0).getNumCols();
		List<BooleanMatrix> result = new LinkedList<BooleanMatrix>();
		boolean[][] allCombos = initalizeAllBooleanArrays(basics.size());
		for (boolean[] combo: allCombos){
			BooleanMatrix sumVector = new BooleanMatrix(rows,cols);
			for (int i=0;i<combo.length;i++){
				//falls i in dem Kombinationsvektor drin ist, Matrix i zu der bisherigen Summe addieren
				if (combo[i])
					for (int r=0;r<rows;r++){
						for (int c=0;c<cols;c++)
							sumVector.set(r, c, sumVector.get(r,c) ^ basics.get(i).get(r, c));
					}
			}
			result.add(sumVector);
		}
	//	System.out.println("Result:"+result.size());
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
		System.out.println(StringRepresentation.bolToStr(result));
		return result;
	}
	
	/**
	 * Helper recursive method for {@link #initalizeAllBooleanArrays(int)}.
	 *
	 * @param array
	 * @param leftBorder the left border of the arrays are initalized.
	 * @param rightBorder the right border of the arrays are initalized.
	 * @param cipher 
	 * @param innerArraySize
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
	
	

	/**
	 * This method calculates the n precedessors of a column
	 * in n iterations.
	 * 
	 * @param n the number of iterations which shall be made.
	 */
	public static void calculatePrecedessors(int n){
		BooleanMatrix sigma = new BooleanMatrix(n);
		for (int i=0;i<n-1;i++){
			sigma.set(i,i+1,true);
			sigma.set(i+1,i,true);
		}
		BooleanMatrix sigmaplus = BooleanMatrix.getUnityMatrix(n);
		for (int i=0;i<n-1;i++){
			sigmaplus.set(i,i+1,true);
			sigmaplus.set(i+1,i,true);
		}
		System.out.println("Untersuche Vorgänger des Kernelementes von sigma+ " + n );
		if (n%3==2){
			// Kernelement erzeugen
			boolean[] coreElem = new boolean[n];
			for (int i=0;i<n;i++){
				if ((i+1)%3==0)
					coreElem[i]=false;
				else coreElem[i]=true;
			}
			System.out.println("Kernelement von sigma+ ist: "+ StringRepresentation.solToStr(coreElem));
			LinkedList<boolean[]> worklist = new LinkedList<boolean[]>();
			worklist.add(coreElem);
			for (int i=1; i< 50; i++){
				LinkedList<boolean[]> newworklist = new LinkedList<boolean[]>();;
				for (boolean[] actElem: worklist){
					List<boolean[]> sols = sigmaplus.computeSolutions(actElem);
					if (sols.isEmpty()){
						System.out.println("Ende in Iteration "+i +" nicht umkehbar: "+ StringRepresentation.solToStr(actElem));
						break;
					} else {
						newworklist.add(sols.get(0));
					}
				}
				worklist = newworklist;
				
			}
		}
	}

}

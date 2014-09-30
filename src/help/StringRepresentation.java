package help;

import java.util.List;


/**
 * This class helps to find mistakes in the code, since it helps to express arrays of boolean values 
 * or Lists of such arrays or matrices as a String, which can be read on the terminal.
 * 
 * @author Irene Thesing
 *
 */
public class StringRepresentation {
	
	/**
	 * Transforms a boolean vector to a string.
	 * 
	 * @param sol boolean vector.
	 * @return String vector values in brackets.
	 */
	public static String solToStr(boolean[] sol){
		String s = "[";
		for (int i=0;i<sol.length;i++){
			if (sol[i])
				s+="1,";
			else s+="0,";
		}
		return s+"]";
	}
	
	/**
	 * Transforms a boolean matrix to a string, every row is a line.
	 * 
	 * @param bol boolean matrix which shall be presented.
	 * @return corresponding string.
	 */
	public static String bolToStr(boolean[][] bol){
		String s = "(";
		for (int i=0;i<bol.length;i++)
			s+=StringRepresentation.solToStr(bol[i])+"\n";
		return s+")";
	}
	
	/**
	 * Transforms integer arrays to strings.
	 * 
	 * @param sol integer array which shall be presented.
	 * @return corresponding string.
	 */
	public static String intsToStr(int[] sol){
		String s = "[";
		for (int i=0;i<sol.length;i++){
			s+=sol[i]+",";
		}
		return s+"]";
	}

	/**
	 * Transforms a list of arrays with boolean values to a string, every array is represented in brackets.
	 * 
	 * @param allSolutions list which shall be presented as string.
	 * @return the corresponding string.
	 */
	public static String listToStr(List<boolean[]> allSolutions) {
		String s="(";
		for (boolean[] x: allSolutions){
			s +=solToStr(x) + ",";
		}
		return s +")";
	}

}

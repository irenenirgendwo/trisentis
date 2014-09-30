package help;

/**
 * This class provides the possibility to take two objects into one object with is defined as the pair of both.
 * The pair is ordered, so first and second element can be seperated and adressed.
 *
 * @param <T1> type of the first parameter.
 * @param <T2> type of the second parameter.
 */
public class Pair<T1, T2> {
	
	/**First value.*/
	private T1 first;
	/**Second value.*/
	private T2 second;
	
	/**
	 * Creates a new pair containing the given values.
	 * 
	 * @param fir first value.
	 * @param sec second value.
	 */
	public Pair(T1 fir,T2 sec){
		first = fir;
		second = sec;
	}

	
	/**
	 * Returns the first parameter.
	 * @return the first parameter.
	 */
	public T1 getFirst() {
		return first;
	}

	/**
	 * Sets the first parameter.
	 * @param first the new first parameter.
	 */
	public void setFirst(T1 first) {
		this.first = first;
	}

	/**
	 * Returns the second parameter.
	 * @return second the second parameter.
	 */
	public T2 getSecond() {
		return second;
	}

	/**
	 * Sets the second parameter.
	 * @param second the new second parameter.
	 */
	public void setSecond(T2 second) {
		this.second = second;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "("+first.toString()+","+second.toString()+")";
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair<T1,T2> other = (Pair<T1,T2>) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

}

package ac.il.technion.twc.impl.models.partC;

import java.io.Serializable;

public class Pair<T> implements Serializable {
	private static final String PAIR_STRING_SEPERATOR = ", ";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1239202504731787857L;
	public final T first;
	public final T second;

	public Pair(T first, T second) {
		super();
		this.first = first;
		this.second = second;
	}

	public int hashCode() {
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	@SuppressWarnings("rawtypes")
	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair otherPair = (Pair) other;
			return ((this.first == otherPair.first || (this.first != null
					&& otherPair.first != null && this.first
						.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
					&& otherPair.second != null && this.second
						.equals(otherPair.second))));
		}

		return false;
	}

	public String toString() {
		String firstString = first.toString();
		String secondString = second.toString();
		if(firstString.compareTo(secondString) < 0)
			return first + PAIR_STRING_SEPERATOR + second;
		else
			return second + PAIR_STRING_SEPERATOR + first;
	}
}

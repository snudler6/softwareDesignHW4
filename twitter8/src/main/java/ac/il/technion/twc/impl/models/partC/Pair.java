package ac.il.technion.twc.impl.models.partC;

import java.io.Serializable;

public class Pair<L,R> implements Serializable {
	private static final String PAIR_STRING_SEPERATOR = ", ";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1239202504731787857L;
	public final L left;
	public final R right;

	public Pair(L _left, R _right) {
		super();
		this.left = _left;
		this.right = _right;
	}

	public int hashCode() {
		int hashFirst = left != null ? left.hashCode() : 0;
		int hashSecond = right != null ? right.hashCode() : 0;

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	@SuppressWarnings("rawtypes")
	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair otherPair = (Pair) other;
			return ((this.left == otherPair.left || (this.left != null
					&& otherPair.left != null && this.left
						.equals(otherPair.left))) && (this.right == otherPair.right || (this.right != null
					&& otherPair.right != null && this.right
						.equals(otherPair.right))));
		}

		return false;
	}

	public String toString() {
		String firstString = left.toString();
		String secondString = right.toString();
		if(firstString.compareTo(secondString) < 0)
			return left + PAIR_STRING_SEPERATOR + right;
		else
			return right + PAIR_STRING_SEPERATOR + left;
	}
}

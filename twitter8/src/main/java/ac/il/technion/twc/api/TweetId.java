package ac.il.technion.twc.api;

import java.io.Serializable;

/**
 * The TweetId class provides an extension of the abstract class
 * AbstractTweetId.
 */
public class TweetId implements Serializable
{
	private static final long serialVersionUID = 2357756020923836748L;

	private String id;

	/**
	 * A constructor for TweetId to set it's id.
	 * 
	 * @param id
	 *            the string id of the tweet
	 */
	public TweetId(String id)
	{
		if (id == null)
			throw new IllegalArgumentException();

		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return id;
	};

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TweetId other = (TweetId) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}

package ac.il.technion.twc.impl.services;

import ac.il.technion.twc.api.interfaces.IQueryHandler;

public interface ITweetsNumberByUser  extends IQueryHandler {

	/**
	 * Gets the number of tweets or retweets made by a specific user
	 * 
	 * @param userId the id of the user
	 * @return The number of tweets made by the user
	 */
	public abstract String getTweetsNumberByUser(String usersId);

}
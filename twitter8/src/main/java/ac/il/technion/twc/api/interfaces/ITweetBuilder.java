package ac.il.technion.twc.api.interfaces;

import java.text.ParseException;

import ac.il.technion.twc.api.models.Tweet;

/*
 * A class that responsible for building a tweet.
 * 
 * @example
 * 
 * importData(lines, new ITweetBuilder()
 * {
 * 	@Override public Tweet buildTweet(String string) throws ParseException {
 * 		return TweetFactory.buildTweet(string);
 * 	}
 * });
 */
public interface ITweetBuilder
{
	
	/**
	 * Build a tweet from a given string.
	 * 
	 * @param tweetString 	the string to build the tweet from
	 * @return				the built tweet
	 * @throws ParseException if there was a parsing problem
	 */
	Tweet buildTweet(String tweetString) throws ParseException;
}

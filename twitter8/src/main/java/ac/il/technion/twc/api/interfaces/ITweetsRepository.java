package ac.il.technion.twc.api.interfaces;

import java.io.Serializable;
import java.util.List;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.models.AbstractTweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;

/**
 * The ITweetsRepository interface describes an object that encapsulates the
 * functionality of the adding tweets and manipulating their updated
 * information. It is responsible for keeping their date consistent.
 */
public interface ITweetsRepository extends Serializable {
    /**
     * Get RootTweet by tweet id.
     * 
     * @param tweetId
     *            The tweet id of the root tweet we want to get from the
     *            repository.
     */
    public RootTweet getRootTweetById(TweetId tweetId);

    /**
     * Get the RootTweet of the given tweet.
     * 
     * @param tweet
     *            The tweet to retrieve its root tweet
     */
    public RootTweet getRootTweet(AbstractTweet tweet);
    
    /**
     * Add a tweet to repository.
     * 
     * @param tweet
     *            the tweet to add.
     * @return the root tweet that its last retweet time was changed (i.e. its
     *         life time was changed);
     */
    public List<Tweet> add(List<Tweet> tweets);
}

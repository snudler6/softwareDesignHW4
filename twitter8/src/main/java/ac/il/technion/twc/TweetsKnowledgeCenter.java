package ac.il.technion.twc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.TweetsIndex;
import ac.il.technion.twc.api.TweetsManager;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.impl.models.TweetsHashtagsAppearencesQueryHandler;
import ac.il.technion.twc.impl.models.TweetsHashtagsQueryHandler;
import ac.il.technion.twc.impl.models.TweetsLifetimeQueryHandler;
import ac.il.technion.twc.impl.models.TweetsTemporalHistogram;
import ac.il.technion.twc.impl.services.ITweetsHashtagsQueryHandler;
import ac.il.technion.twc.impl.services.ITweetsLifetimeQueryHandler;

import com.google.inject.Inject;

/**
 * TweetsKnowledgeCenter
 */
public class TweetsKnowledgeCenter extends TweetsManager
{
	final String TWEETS_LIFETIME_QUERY_HANDLER = "TWEETS_LIFETIME_QUERY_HANDLER";
	final String TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER = "TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER";
	final String TWEETS_HASHTAGS_QUERY_HANDLER = "TWEETS_HASHTAGS_QUERY_HANDLER";
	final String TWEETS_HASHTAG_APPEARENCES_HANDLER = "TWEETS_HASHTAG_APPEARENCES_HANDLER"; 

	@Inject
	public TweetsKnowledgeCenter(ITweetsRepository tweetsRepository, TweetsIndex tweetsIndex)
	{
		super(tweetsRepository, tweetsIndex);
		this.subscribe(TWEETS_LIFETIME_QUERY_HANDLER, new TweetsLifetimeQueryHandler());
		this.subscribe(TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER, new TweetsTemporalHistogram());
		this.subscribe(TWEETS_HASHTAGS_QUERY_HANDLER, new TweetsHashtagsQueryHandler());
		this.subscribe(TWEETS_HASHTAG_APPEARENCES_HANDLER, new TweetsHashtagsAppearencesQueryHandler());
	}

	public ITweetsLifetimeQueryHandler getTweetsLifetimeQueryHandler()
	{
		return (ITweetsLifetimeQueryHandler) this.getQueryHandler(TWEETS_LIFETIME_QUERY_HANDLER);
	}

	public TweetsTemporalHistogram getTweetsTemporalHistogrmQueryHandler()
	{
		return (TweetsTemporalHistogram) this.getQueryHandler(TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER);
	}

	public TweetsHashtagsQueryHandler getTweetsHashtagsQueryHandler()
	{
		return (TweetsHashtagsQueryHandler) this.getQueryHandler(TWEETS_HASHTAGS_QUERY_HANDLER);
	}

	public ITweetsHashtagsQueryHandler getExtendedTweetsHashtagsQuesryHandler()
	{
		return (ITweetsHashtagsQueryHandler) this.getQueryHandler(TWEETS_HASHTAG_APPEARENCES_HANDLER);
	}

	/**
	 * Get life time of root tweet.
	 * 
	 * @param tweetId
	 *            The tweedId of the RootTweet we want to get its life time.
	 * @return the life time (string) of the corresponding tweet.
	 */
	public String getLifetimeOfTweet(String tweetId)
	{
		TweetId id = new TweetId(tweetId);
		Long lifeTime = getTweetsLifetimeQueryHandler().getLifetimeOfTweetInMilliseconds(id);
		return lifeTime.toString();
	}

	/**
	 * Gets the current statistics of the daily histogram.
	 * 
	 * @return An array of strings, each string describes the info of one
	 *         dayOfWeek.
	 */
	public String[] getDailyHistogram()
	{
		return getTweetsTemporalHistogrmQueryHandler().getStrings(new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE));
	}

	/**
	 * Gets the current statistics of the temporal histogram in interval
	 * [t1,t2].
	 * 
	 * @param t1
	 *            The first time.
	 * 
	 * @param t2
	 *            The second time
	 * 
	 * @return An array of strings, each string describes the info of one
	 *         dayOfWeek.
	 */
	public String[] getTemporalHistogram(String t1Str, String t2Str) throws ParseException
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse(t1Str);
		Date t2 = dateFormat.parse(t2Str);

		return getTweetsTemporalHistogrmQueryHandler().getStrings(t1, t2);
	}

	/**
	 * Returns the popularity of the hashtag
	 * 
	 * @param hashtag
	 *            the hashtag to get the popularity of
	 * @return 0 if the hashtag does not exist. otherwise, the number of
	 *         retweets to roottweets that contains the given hashtag
	 */
	public String getHashtagPopularity(String hashtag)
	{
		return getTweetsHashtagsQueryHandler().getPopularity(hashtag).toString();
	}
	
	public String getHashtagAppearences(String hashtag)
	{
		return getExtendedTweetsHashtagsQuesryHandler().getPopularity(hashtag).toString();
	}
}

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
import ac.il.technion.twc.impl.models.TweetsHashtagsQueryHandler;
import ac.il.technion.twc.impl.models.TweetsLifetimeQueryHandler;
import ac.il.technion.twc.impl.models.TweetsTemporalHistogram;
import ac.il.technion.twc.impl.models.partA.TweetsAncestorQueryHandler;
import ac.il.technion.twc.impl.models.partA.TweetsHashtagsAppearencesQueryHandler;
import ac.il.technion.twc.impl.models.partA.UsersFirstTweetQueryHandler;
import ac.il.technion.twc.impl.models.partB.TweetsNumberByUserQueryHandler;
import ac.il.technion.twc.impl.models.partB.TweetsRetweetsAmountQueryHandler;
import ac.il.technion.twc.impl.models.partC.TweetsHashtagsCouplingQueryHandler;
import ac.il.technion.twc.impl.services.ITweetsLifetimeQueryHandler;
import ac.il.technion.twc.impl.services.partA.ITweetsAncestorQueryHandler;
import ac.il.technion.twc.impl.services.partA.ITweetsHashtagsAppearenceQueryHandler;
import ac.il.technion.twc.impl.services.partA.IUsersFirstTweetQueryHandler;
import ac.il.technion.twc.impl.services.partB.ITweetsNumberByUserQueryHandler;
import ac.il.technion.twc.impl.services.partB.ITweetsRetweetsAmountQueryHandler;
import ac.il.technion.twc.impl.services.partC.ITweetsHashtagsCouplingQueryHandler;

import com.google.inject.Inject;

/**
 * TweetsKnowledgeCenter
 */
public class TweetsKnowledgeCenter extends TweetsManager
{
	final String TWEETS_LIFETIME_QUERY_HANDLER = "TWEETS_LIFETIME_QUERY_HANDLER";
	final String TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER = "TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER";
	final String TWEETS_HASHTAGS_QUERY_HANDLER = "TWEETS_HASHTAGS_QUERY_HANDLER";
	
	//Part A data handlers
	final String TWEETS_HASHTAG_APPEARENCES_HANDLER = "TWEETS_HASHTAG_APPEARENCES_HANDLER"; 
	final String TWEETS_ANCESTOR_QUERY_HANDLER = "TWEETS_ANCESTOR_QUERY_HANDLER";
	final String TWEETS_USER_FIRST_TWEET_QUERY_HANDLER = "TWEETS_USER_FIRST_TWEET_QUERY_HANDLER";
	
	//Part B data handlers
	final String TWEETS_NUMBER_BY_USER_QUERY_HANDLER = "TWEETS_NUMBER_BY_USER_QUERY_HANDLER";
	final String TWEETS_RETWEETS_AMOUNT = "TWEETS_RETWEETS_AMOUNT";
	
	//Part C data handlers
	final String TWEETS_HASHTAGS_COUPLING_QUERY_HANDLER = "TWEETS_HASHTAGS_COUPLING_QUERY_HANDLER";
	
	@Inject
	public TweetsKnowledgeCenter(ITweetsRepository tweetsRepository, TweetsIndex tweetsIndex)
	{
		super(tweetsRepository, tweetsIndex);
		this.subscribe(TWEETS_LIFETIME_QUERY_HANDLER, new TweetsLifetimeQueryHandler());
		this.subscribe(TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER, new TweetsTemporalHistogram());
		this.subscribe(TWEETS_HASHTAGS_QUERY_HANDLER, new TweetsHashtagsQueryHandler());
		this.subscribe(TWEETS_HASHTAG_APPEARENCES_HANDLER, new TweetsHashtagsAppearencesQueryHandler());
		this.subscribe(TWEETS_ANCESTOR_QUERY_HANDLER, new TweetsAncestorQueryHandler());
		this.subscribe(TWEETS_USER_FIRST_TWEET_QUERY_HANDLER, new UsersFirstTweetQueryHandler());
		this.subscribe(TWEETS_NUMBER_BY_USER_QUERY_HANDLER, new TweetsNumberByUserQueryHandler());
		this.subscribe(TWEETS_RETWEETS_AMOUNT, new TweetsRetweetsAmountQueryHandler());
		this.subscribe(TWEETS_HASHTAGS_COUPLING_QUERY_HANDLER, new TweetsHashtagsCouplingQueryHandler());
	}
	
	public ITweetsHashtagsCouplingQueryHandler getTweetsHashtagsCouplingQueryHandler(){
		return (ITweetsHashtagsCouplingQueryHandler) this.getQueryHandler(TWEETS_HASHTAGS_COUPLING_QUERY_HANDLER);
	}
	
	public ITweetsRetweetsAmountQueryHandler getTweetsRetweetsAmountQueryHandler(){
		return (ITweetsRetweetsAmountQueryHandler) this.getQueryHandler(TWEETS_RETWEETS_AMOUNT);
	}

	public ITweetsNumberByUserQueryHandler getTweetsNumberByUserQueryHandler(){		
		return (ITweetsNumberByUserQueryHandler) this.getQueryHandler(TWEETS_NUMBER_BY_USER_QUERY_HANDLER);
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

	public ITweetsHashtagsAppearenceQueryHandler getTweetsHashtagsAppearenceQueryHandler()
	{
		return (ITweetsHashtagsAppearenceQueryHandler) this.getQueryHandler(TWEETS_HASHTAG_APPEARENCES_HANDLER);
	}

	public ITweetsAncestorQueryHandler getTweetsAncestorQueryHandler(){
		return (ITweetsAncestorQueryHandler) this.getQueryHandler(TWEETS_ANCESTOR_QUERY_HANDLER);
	}

	public IUsersFirstTweetQueryHandler getUserFirstTweetQueryHandler(){
		return (IUsersFirstTweetQueryHandler) this.getQueryHandler(TWEETS_USER_FIRST_TWEET_QUERY_HANDLER);
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
	 *            The left time.
	 * 
	 * @param t2
	 *            The right time
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

	public String getHashtagPopularity(String hashtag)
	{
		return getTweetsHashtagsQueryHandler().getPopularity(hashtag).toString();
	}
	
	public String getHashtagAppearences(String hashtag)
	{
		return getTweetsHashtagsAppearenceQueryHandler().getHashtagAppearences(hashtag).toString();
	}
	
	public String getAncestorTweetsId(String id)
	{
		return getTweetsAncestorQueryHandler().getAncestor(new TweetId(id)).toString();
	}

	public String getUserFirstTweet(String userId)
	{
		return getUserFirstTweetQueryHandler().getUsersFirstTweetId(userId).toString();
	}

	public String getTweetsNumberByUser(String user){
		return getTweetsNumberByUserQueryHandler().getTweetsNumberByUser(user).toString();
	}

	public String getTweetsRetweetsAmount(String id){
		return getTweetsRetweetsAmountQueryHandler().getRetweetsAmount(new TweetId(id)).toString();
	}

	public String[] getTweetsHashtagsCoupling(int k){
		return getTweetsHashtagsCouplingQueryHandler().getMostCoupled(k).toArray(new String[0]);
	}
}

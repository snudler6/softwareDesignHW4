package ac.il.technion.twc.api.interfaces;

import java.io.IOException;
import java.text.ParseException;

/**
 * The ITweetsManager interface describes an object that encapsulates the
 * functionality of managing the tweets repository and the the index, as well as
 * importing and exporting them.
 * 
 * @example
 * public class TweetsKnowledgeCenter extends TweetsManager {
 * 
 * 	final String TWEETS_LIFETIME_QUERY_HANDLER = "TWEETS_LIFETIME_QUERY_HANDLER";
 * 	final String TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER = "TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER";
 * 
 * 	@Inject
 * 	public TweetsKnowledgeCenter(ITweetsRepository tweetsRepository,ITweetsIndex tweetsIndex) {
 * 		super(tweetsRepository, tweetsIndex);
 * 		this.subscribe(TWEETS_LIFETIME_QUERY_HANDLER, new TweetsLifetimeQueryHandler());
 * 		this.subscribe(TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER, new TweetsTemporalHistogram());
 * 	}
 *
 * 	public ITweetsLifetimeQueryHandler getTweetsLifetimeQueryHandler() {
 * 		return (ITweetsLifetimeQueryHandler) this.getQueryHandler(TWEETS_LIFETIME_QUERY_HANDLER);
 * 	}
 * 
 * 	public TweetsTemporalHistogram getTweetsTemporalHistogrmQueryHandler() {
 * 		return (TweetsTemporalHistogram) this.getQueryHandler(TWEETS_TEMPORAL_HISTOGRAM_QUERY_HANDLER);
 * 	}
 * 
 * 
 * 	public String getLifetimeOfTweet(String tweetId) {
 * 		return getTweetsLifetimeQueryHandler().getLifetimeOfTweetInMilliseconds(id);
 * 	}
 *
 * 	public String[] getTemporalHistogram(String t1, String t2) throws ParseException {
 * 		return getTweetsTemporalHistogrmQueryHandler().getTemporalHistogram(t1, t2);
 * 	}
 * }
 * 
 * 
 * public class oldFuntionalityTester {
 * 	private IDataManager repositoryDataManager;
 * 	private IDataManager indexDataManager;
 * 	private TweetsKnowledgeCenter tweetsKnowledgeCenter;
 * 
 * 	private void importData(String[] lines, ITweetBuilder builder) throws Exception {
 * 		tweetsKnowledgeCenter.importRepository(repositoryDataManager);
 * 		tweetsKnowledgeCenter.addTweetsData(lines, builder);
 * 		tweetsKnowledgeCenter.exportIndex(indexDataManager);
 * 	}
 * 
 * 	public void setupIndex() throws Exception {
 * 		tweetsKnowledgeCenter.importIndex(indexDataManager);
 * 	}
 * 
 * 	public void cleanPersistentData() {
 * 		this.repositoryDataManager.cleanData();
 * 		this.indexDataManager.cleanData();
 * 	}
 * }
 */
public interface ITweetsManager {

    /**
     * Add tweets to tweets manager.
     * 
     * @param lines
     *            the tweets data. every line contains one tweet.
     * @param builder
     *            the builder used to build the tweets
     * @throws ParseException 
     */
 	public void addTweetsData(String[] lines, ITweetBuilder builder) throws ParseException;

    /**
     * Import the tweets repository from disk.
     * 
     * @param repositoryDataManager
     *            the data manager that used to describe the repository data
     *            properties (See IDataManager).
     */
    public void importRepository(IDataManager repositoryDataManager)
	    throws IOException;

    /**
     * Import the tweets index from disk.
     * 
     * @param indexDataManager
     *            the data manager that used to describe the index data
     *            properties (See IDataManager).
     */
    public void importIndex(IDataManager indexDataManager) throws IOException;

    /**
     * Export the tweets repository to disk.
     * 
     * @param repositoryDataManager
     *            the data manager that used to describe the repository data
     *            properties (See IDataManager).
     */
    public void exportRepository(IDataManager repositoryDataManager)
	    throws IOException;

    /**
     * Export the tweets index to disk.
     * 
     * @param indexDataManager
     *            the data manager that used to describe the index data
     *            properties (See IDataManager).
     */
    public void exportIndex(IDataManager indexDataManager) throws IOException;

	/**
	 * This method subscribes an handler to be called when the TweetsManager publishes
	 *  an event, e.g. add tweet,...
	 *  see example above.
	 * 
	 * @param identifier an identifier to the IQueryHandler
	 * @param queryHandler the IQueryHandler
	 */
	public void subscribe(String identifier, IQueryHandler queryHandler);
	
	/**
	 * Gets the IQueryHandler subscribed in the method 
	 * subscribe(String identifier, IQueryHandler queryHandler).
	 * see example above.
	 * 
	 * @param identifier the identifier of the IQueryHandler
	 * @return the IQueryHandler
	 */
	public IQueryHandler getQueryHandler(String identifier);
}

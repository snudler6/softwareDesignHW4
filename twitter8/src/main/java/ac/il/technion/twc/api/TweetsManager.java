package ac.il.technion.twc.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ac.il.technion.twc.api.interfaces.IDataManager;
import ac.il.technion.twc.api.interfaces.IQueryHandler;
import ac.il.technion.twc.api.interfaces.ITweetBuilder;
import ac.il.technion.twc.api.interfaces.ITweetsManager;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;

import com.google.inject.Inject;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectInputNoShared;
import de.ruedigermoeller.serialization.FSTObjectOutput;

/**
 * The TweetsManager class provides an implementation of the interface
 * ITweetsManager.
 */
public class TweetsManager implements ITweetsManager
{
	private ITweetsRepository tweetsRepository;
	private TweetsIndex tweetsIndex;

	@Inject
	public TweetsManager(ITweetsRepository tweetsRepository, TweetsIndex tweetsIndex)
	{
		this.tweetsRepository = tweetsRepository;
		this.tweetsIndex = tweetsIndex;
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#addTweetsData(java.lang.String[], ac.il.technion.twc.api.interfaces.ITweetBuilder)
	 */
	@Override
	public void addTweetsData(String[] lines, ITweetBuilder builder) throws ParseException
	{
		List<Tweet> tweets = new ArrayList<Tweet>();
		for (String line : lines)
		{
			Tweet tweet = builder.buildTweet(line);
			tweets.add(tweet);
		}
		
		List<Tweet> changedRootTweets = tweetsRepository.add(tweets);
		
		for (IQueryHandler queryHandler : tweetsIndex.getQueryHandlers())
		{
			queryHandler.onTweetsAdded(tweets, tweetsRepository);
		}

		for (IQueryHandler queryHandler : tweetsIndex.getQueryHandlers())
		{
			queryHandler.onRootTweetsDataChanged(changedRootTweets);
		}
	}

	private Object importObject(IDataManager dataManager, Object objectToImport) throws IOException
	{
		if (!dataManager.exists())
			return objectToImport;
		InputStream fileIn = dataManager.getInputStreamInstance();
		FSTObjectInput in = new FSTObjectInputNoShared(fileIn);
		try
		{
			return in.readObject();
		}
		catch (ClassNotFoundException e)
		{
			throw new IOException("File has been changed");
		}
		finally
		{
			in.close();
			fileIn.close();
		}
	}

	private void exportObject(IDataManager dataManager, Object objectToExport) throws IOException
	{
		OutputStream fileOut = dataManager.getOutputStreamInstance();
		FSTObjectOutput out = new FSTObjectOutput(fileOut);
		out.writeObject(objectToExport);
		out.close();
		fileOut.close();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#importRepository(ac.il.technion.twc.api.interfaces.IDataManager)
	 */
	public void importRepository(IDataManager repositoryDataManager) throws IOException
	{
		tweetsRepository = (TweetsRepository) importObject(repositoryDataManager, tweetsRepository);
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#exportRepository(ac.il.technion.twc.api.interfaces.IDataManager)
	 */
	public void exportRepository(IDataManager repositoryDataManager) throws IOException
	{
		exportObject(repositoryDataManager, tweetsRepository);
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#exportIndex(ac.il.technion.twc.api.interfaces.IDataManager)
	 */
	public void exportIndex(IDataManager indexDataManager) throws IOException
	{
		exportObject(indexDataManager, tweetsIndex);
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#importIndex(ac.il.technion.twc.api.interfaces.IDataManager)
	 */
	public void importIndex(IDataManager indexDataManager) throws IOException
	{
		tweetsIndex = (TweetsIndex) importObject(indexDataManager, tweetsIndex);
	}
	
	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#subscribe(java.lang.String, ac.il.technion.twc.api.interfaces.IQueryHandler)
	 */
	public void subscribe(String identifier, IQueryHandler queryHandler)
	{
		tweetsIndex.putQueryHandler(identifier, queryHandler);
	}
	
	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsManager#getQueryHandler(java.lang.String)
	 */
	public IQueryHandler getQueryHandler(String identifier)
	{
		return tweetsIndex.getQueryHandler(identifier);
	}
}

package ac.il.technion.twc.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class AcceptanceTesting
{
	FunctionalityTester target;

	@Before
	public void setup() throws Exception
	{
		IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexDataFile");
		IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryDataFile");
		this.target = new FunctionalityTester(repositoryDataManager, indexDataManager);
		this.target.cleanPersistentData();
	}

	@After
	public void clear() throws Exception
	{
		this.target.cleanPersistentData();		
	}
	
	@Test
	public void AcceptanceTest_1() throws Exception
	{
		String[] lines = new String[] { "05/04/2014 1:00:00, 1", "05/04/2014 1:00:01, 2, 1", "05/04/2014 1:00:01, 3, 1", "05/04/2014 4:00:00, 4" };

		target.importData(lines);

		target.setupIndex();

		String json5 = "{\"created_at\":\"Sun May 19 10:08:08 +0000 2013\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"5\",\"retweeted_status\":null,\"user\":{\"id_str\":\"100\"}}";
		String json6 = "{\"created_at\":\"Sun May 19 10:08:09 +0000 2013\",\"id_str\":\"6\",\"retweeted_status\":{\"id_str\":\"5\"},\"user\":{\"id_str\":\"101\"}}";
		String json7 = "{\"created_at\":\"Sun May 20 10:08:08 +0000 2013\",\"text\":\"Java is #bad #good\",\"id_str\":\"7\",\"retweeted_status\":null,\"user\":{\"id_str\":\"100\"}}";

		lines = new String[] { json5, json6, json7 };

		target.importDataJson(lines);

		target.setupIndex();

		//Part A tests
		assertEquals("5", target.getOriginalTweetsId("5"));
		assertEquals("5", target.getOriginalTweetsId("6"));
		assertEquals("7", target.getOriginalTweetsId("7"));
		
		assertEquals("0", target.countHashtagAppearances("nonExistingHashtag"));
		assertEquals("1", target.countHashtagAppearances("good"));
		assertEquals("2", target.countHashtagAppearances("bad"));
		
		assertEquals("5", target.getFirstTweet("100"));
		assertEquals("6", target.getFirstTweet("101"));
		//Part B tests
		
		
	}

	@Test
	public void AcceptanceTest_2() throws Exception
	{
		String[] lines = new String[] { "05/04/2014 01:00:01, 1, 5", "05/04/2014 01:00:02, 2, 1" };

		target.importData(lines);

		target.setupIndex();

		assertEquals("0", target.countHashtagAppearances("bad"));
		
		String json5 = "{\"created_at\":\"Sat Apr 5 01:00:00 +0000 2014\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"5\",\"retweeted_status\":null}";
		String json6 = "{\"created_at\":\"Sat Apr 5 01:00:03 +0000 2014\",\"id_str\":\"6\",\"retweeted_status\":{\"id_str\":\"2\"}}";

		lines = new String[] { json5, json6 };

		target.importDataJson(lines);

		target.setupIndex();

		assertEquals("5", target.getOriginalTweetsId("5"));
		assertEquals("5", target.getOriginalTweetsId("6"));
		assertEquals("1", target.countHashtagAppearances("bad"));
		
		lines = new String[] { "05/04/2014 01:00:10, 7, 5" };
		
		target.importData(lines);

		target.setupIndex();

		assertEquals("5", target.getOriginalTweetsId("7"));
		assertEquals("1", target.countHashtagAppearances("bad"));
		
//		old irrelevent tests, kept here for debugging reasons		
//		assertEquals("10000", target.getLifetimeOfTweets("5"));
	}
}
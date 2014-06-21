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
	FunctionalityTester $;

	@Before
	public void setup() throws Exception
	{
		IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexDataFile");
		IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryDataFile");
		this.$ = new FunctionalityTester(repositoryDataManager, indexDataManager);
		this.$.cleanPersistentData();
	}

	@After
	public void clear() throws Exception
	{
		this.$.cleanPersistentData();		
	}
	
	@Test
	public void AcceptanceTest_1() throws Exception
	{
		String[] lines = new String[] { "05/04/2014 1:00:00, 1", "05/04/2014 1:00:01, 2, 1", "05/04/2014 1:00:01, 3, 1", "05/04/2014 4:00:00, 4" };

		$.importData(lines);

		$.setupIndex();

		String json5 = "{\"created_at\":\"Sun May 19 10:08:08 +0000 2013\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"5\",\"retweeted_status\":null,\"user\":{\"id_str\":\"100\"}}";
		String json6 = "{\"created_at\":\"Sun May 19 10:08:09 +0000 2013\",\"id_str\":\"6\",\"retweeted_status\":{\"id_str\":\"5\"},\"user\":{\"id_str\":\"101\"}}";
		String json7 = "{\"created_at\":\"Sun May 20 10:08:08 +0000 2013\",\"text\":\"Java is #bad #good\",\"id_str\":\"7\",\"retweeted_status\":null,\"user\":{\"id_str\":\"100\"}}";

		lines = new String[] { json5, json6, json7 };

		$.importDataJson(lines);

		$.setupIndex();

		//Part A tests
		assertEquals("5", $.getOriginalTweetsId("5"));
		assertEquals("5", $.getOriginalTweetsId("6"));
		assertEquals("7", $.getOriginalTweetsId("7"));
		
		assertEquals("0", $.countHashtagAppearances("nonExistingHashtag"));
		assertEquals("1", $.countHashtagAppearances("good"));
		assertEquals("2", $.countHashtagAppearances("bad"));
		
		assertEquals("5", $.getFirstTweet("100"));
		assertEquals("6", $.getFirstTweet("101"));
		
		//Part B tests
		
		
	}

	@Test
	public void AcceptanceTest_2() throws Exception
	{
		String[] lines = new String[] { "05/04/2014 01:00:01, 1, 5", "05/04/2014 01:00:02, 2, 1" };

		$.importData(lines);

		$.setupIndex();

		assertEquals("0", $.countHashtagAppearances("bad"));
		
		String json5 = "{\"created_at\":\"Sat Apr 5 01:00:00 +0000 2014\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"5\",\"retweeted_status\":null}";
		String json6 = "{\"created_at\":\"Sat Apr 5 01:00:03 +0000 2014\",\"id_str\":\"6\",\"retweeted_status\":{\"id_str\":\"2\"}}";

		lines = new String[] { json5, json6 };

		$.importDataJson(lines);

		$.setupIndex();

		assertEquals("5", $.getOriginalTweetsId("5"));
		assertEquals("5", $.getOriginalTweetsId("6"));
		assertEquals("1", $.countHashtagAppearances("bad"));
		
		lines = new String[] { "05/04/2014 01:00:10, 7, 5" };
		
		$.importData(lines);

		$.setupIndex();

		//part A
		assertEquals("5", $.getOriginalTweetsId("7"));

		assertEquals("1", $.countHashtagAppearances("bad"));
		
	}
}
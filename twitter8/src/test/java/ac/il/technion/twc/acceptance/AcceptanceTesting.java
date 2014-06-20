package ac.il.technion.twc.acceptance;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class AcceptanceTesting
{
	FuntionalityTester target;

	@Before
	public void setup() throws Exception
	{
		IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexDataFile");
		IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryDataFile");
		this.target = new FuntionalityTester(repositoryDataManager, indexDataManager);
		this.target.cleanPersistentData();
	}

	@Test
	public void AcceptanceTest_1() throws Exception
	{
		String[] lines = new String[] { "05/04/2014 1:00:00, 1", "05/04/2014 1:00:01, 2, 1", "05/04/2014 1:00:01, 3, 1", "05/04/2014 4:00:00, 4" };

		target.importData(lines);

		target.setupIndex();

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "4,2" }, target.getDailyHistogram());

		String json5 = "{\"created_at\":\"Sun May 19 10:08:08 +0000 2013\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"5\",\"retweeted_status\":null}";
		String json6 = "{\"created_at\":\"Sun May 19 10:08:09 +0000 2013\",\"id_str\":\"6\",\"retweeted_status\":{\"id_str\":\"5\"}}";
		String json7 = "{\"created_at\":\"Sun May 19 10:08:08 +0000 2013\",\"id_str\":\"7\",\"retweeted_status\":null}";

		lines = new String[] { json5, json6, json7 };

		target.importDataJson(lines);

		target.setupIndex();

		assertEquals("1000", target.getLifetimeOfTweets("5"));
		assertEquals("1", target.getHashtagPopularity("bad"));
		assertEquals("1000", target.getLifetimeOfTweets("1"));

		assertArrayEquals(new String[] { "3,1", "0,0", "0,0", "0,0", "0,0", "0,0", "4,2" }, target.getDailyHistogram());
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "2,2" }, target.getTemporalHistogram("05/04/2014 1:00:01", "05/04/2014 3:01:06"));
	}

	@Test
	public void AcceptanceTest_2() throws Exception
	{
		String[] lines = new String[] { "05/04/2014 01:00:01, 1, 5", "05/04/2014 01:00:02, 2, 1" };

		target.importData(lines);

		target.setupIndex();

		String json5 = "{\"created_at\":\"Sat Apr 5 01:00:00 +0000 2014\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"5\",\"retweeted_status\":null}";
		String json6 = "{\"created_at\":\"Sat Apr 5 01:00:03 +0000 2014\",\"id_str\":\"6\",\"retweeted_status\":{\"id_str\":\"2\"}}";

		lines = new String[] { json5, json6 };

		target.importDataJson(lines);

		target.setupIndex();

		assertEquals("3", target.getHashtagPopularity("bad"));
		assertEquals("3000", target.getLifetimeOfTweets("5"));
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "4,3" }, target.getDailyHistogram());
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "2,2" }, target.getTemporalHistogram("05/04/2014 1:00:01", "05/04/2014 01:00:02"));

		lines = new String[] { "05/04/2014 01:00:10, 7, 5" };

		target.importData(lines);

		target.setupIndex();

		assertEquals("10000", target.getLifetimeOfTweets("5"));
	}
}
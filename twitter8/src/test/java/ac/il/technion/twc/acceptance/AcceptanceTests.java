package ac.il.technion.twc.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;
import ac.il.technion.twc.timeTests.TestUtils;

public class AcceptanceTests {

	FunctionalityTester $;

	@Before
	public void setup() throws Exception
	{
		IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexDataFile");
		IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryDataFile");
		this.$ = new FunctionalityTester(repositoryDataManager, indexDataManager);
		this.$.cleanPersistentData();
		
		testInitialization();
	}

	private void testInitialization() throws Exception {
		String[] lines = new String[] { "05/04/2014 1:00:00, 1", "05/04/2014 1:00:01, 2, 1", "05/04/2014 1:00:01, 3, 1", "05/04/2014 4:00:00, 4" };

		$.importData(lines);

		$.setupIndex();

		String json5 = TestUtils.createJsonTweet(5,"100","Sun May 19 10:08:08 +0000 2013",new String[]{"bad","a","b","c"},false,-1) ;
		String json6 = TestUtils.createJsonTweet(6,"101","Sun May 19 10:08:08 +0000 2013",new String[]{"a","c"},true,5) ; 
		String json7 = TestUtils.createJsonTweet(7,"100","Sun May 20 10:08:08 +0000 2013",new String[]{"a","c","bad","good"},false,-1) ;
		String json8 = TestUtils.createJsonTweet(9,"111","Sun May 19 10:08:08 +0000 2013",new String[]{"a","c"},false,-1) ; 
		String json9 = TestUtils.createJsonTweet(10,"120","Sun May 19 10:08:08 +0000 2013",new String[]{"a","bad"},false,-1) ; 
		
		lines = new String[] { json5, json6, json7,json8,json9 };

		$.importDataJson(lines);

		$.setupIndex();		
	}

	@After
	public void clear() throws Exception
	{
		this.$.cleanPersistentData();		
	}
	
	@Test
	public final void partA_getOriginalTweetIDTest() throws Exception {
		assertEquals("5", $.getOriginalTweetsId("5"));
		assertEquals("5", $.getOriginalTweetsId("6"));
		assertEquals("7", $.getOriginalTweetsId("7"));
	}
	
	@Test
	public final void partA_countHashtagAppearencesTest() throws Exception {	
		assertEquals("0", $.countHashtagAppearances("nonExistingHashtag"));
		assertEquals("1", $.countHashtagAppearances("good"));
		assertEquals("3", $.countHashtagAppearances("bad"));
	}
	
	@Test
	public final void partA_getFirstTweetTest() throws Exception {	
		assertEquals("5", $.getFirstTweet("100"));
		assertEquals("6", $.getFirstTweet("101"));
	}
	
	@Test
	public final void partB_RetweetsAmountTest() throws Exception {
		assertEquals("2",$.numberOfRetweets("1"));
		assertEquals("1",$.numberOfRetweets("5"));
		assertEquals("0",$.numberOfRetweets("4")); //no retweets
		assertEquals("0",$.numberOfRetweets("non-existing")); //non exsisting
	}

	@Test
	public final void partB_TweetsNumberByUserTest() throws Exception {
		assertEquals("2", $.numberTweetsByUser("100"));
		assertEquals("1", $.numberTweetsByUser("101"));
		assertEquals("0", $.numberTweetsByUser("non-existing"));//non existing
	}
	
	@Test
	public final void partC_HashtagCouplingTest() throws Exception {
		String[] mostCoupled = $.hashtagCoupling(3);
		assertEquals("a, c",mostCoupled[0]);
		assertEquals("a, bad",mostCoupled[1]);
		assertEquals("bad, c",mostCoupled[2]);
	}
}
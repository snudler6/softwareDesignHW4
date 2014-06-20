package ac.il.technion.twc.timeTests.partA;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.timeTests.TestUtils;

public abstract class PartA_StressTest {

	FunctionalityTester target = new FunctionalityTester();
	
	@Before
	public void setUp() throws Exception {
		String[] jsonlines = TestUtils.generateTweets(TestUtils.MEDIUM_SAMPLE_LINES);
				
		target.cleanPersistentData();
		target.importDataJson(jsonlines);
		target.setupIndex();
	}

	@After
	public void tearDown()
	{
		target.cleanPersistentData();
	}
	
	@Test(timeout = TestUtils.LARGE_SAMPLE_LINES / 2) // 0.5ms to tweet. 10,000 tweets . 5 seconds limit
	public abstract void testMediumSizeTweets() throws Exception;	

}

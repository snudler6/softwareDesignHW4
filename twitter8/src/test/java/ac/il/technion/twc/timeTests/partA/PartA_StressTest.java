package ac.il.technion.twc.timeTests.partA;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.timeTests.TestUtils;

public abstract class PartA_StressTest {

	FunctionalityTester target = new FunctionalityTester();
	
	@Before
	public void setUp() throws Exception {
		String[] lines = TestUtils.generateTweets(TestUtils.MEDIUM_SAMPLE_LINES,new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH));
				
		target.cleanPersistentData();
		target.importData(lines);
		target.setupIndex();
	}

	@After
	public void tearDown()
	{
		target.cleanPersistentData();
	}
	
	@Test(timeout = TestUtils.LARGE_SAMPLE_LINES / 2) // 0.5ms to tweet. 1,000,000 tweets . 500 seconds limit
	public abstract void testMediumSizeTweets() throws Exception;	

}
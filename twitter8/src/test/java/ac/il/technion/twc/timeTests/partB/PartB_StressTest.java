package ac.il.technion.twc.timeTests.partB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.timeTests.TestUtils;

public abstract class PartB_StressTest {

	FunctionalityTester target = new FunctionalityTester();

	static final int times = 1000; // we check that the average time is good enough

	@Before
	public void setUp() throws Exception {
		String[] jsonlines = TestUtils
				.generateTweets(TestUtils.MEDIUM_SAMPLE_LINES);

		target.cleanPersistentData();
		target.importDataJson(jsonlines);
		target.setupIndex();
	}

	@After
	public void tearDown() {
		target.cleanPersistentData();
	}

	@Test(timeout = 10 * times) // 10ms for each run
	public abstract void testMediumSizeTweets() throws Exception;

}

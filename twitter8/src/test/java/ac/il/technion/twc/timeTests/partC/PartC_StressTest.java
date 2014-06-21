package ac.il.technion.twc.timeTests.partC;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.timeTests.TestUtils;

public abstract class PartC_StressTest {

	FunctionalityTester target = new FunctionalityTester();

	static final int times = 1000; // we check that the average time is good enough
	static final int K1 = 1;
	static final int K2 = 4;

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

	@Test(timeout = 10 * times * K1) // 10ms for each run
	public abstract void testMediumSizeTweetsDepth1() throws Exception;
	
	@Test(timeout = 10 * times * K2) // 10ms for each run
	public abstract void testMediumSizeTweetsDepth4() throws Exception;

}

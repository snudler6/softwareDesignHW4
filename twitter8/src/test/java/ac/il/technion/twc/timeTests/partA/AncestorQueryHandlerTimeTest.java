package ac.il.technion.twc.timeTests.partA;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;
import ac.il.technion.twc.timeTests.TestUtils;

public class AncestorQueryHandlerTimeTest {

	FunctionalityTester target = new FunctionalityTester();
	IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryMillionTweetsSameDate");
	IDataManager indexDataManager = new FileDataManager("./src/test/resources/MillionTweetsHalfOfThemRetweets");
	
	@Before
	public void setUp() throws Exception {
		String[] lines = TestUtils.generateTweets(TestUtils.LARGE_SAMPLE_LINES,new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH));
		target.importData(lines);
		target.setupIndex();
	}

	
	@Test(timeout = TestUtils.LARGE_SAMPLE_LINES / 2) // 0.5ms to tweet. 1,000,000 tweets . 500 seconds limit
	public final void testMillionTweets() throws Exception {
		target.getOriginalTweetsId("4"); //some random tweet. this is a stree test
	}

}

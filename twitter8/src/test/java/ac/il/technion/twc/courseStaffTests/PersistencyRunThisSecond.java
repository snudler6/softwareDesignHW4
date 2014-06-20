package ac.il.technion.twc.courseStaffTests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import ac.il.technion.twc.oldFuntionalityTester;

public class PersistencyRunThisSecond {
	public static void main(String[] args) throws Exception {
		oldFuntionalityTester $ = new oldFuntionalityTester();
		$.setupIndex();
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "98,16", "0,0", "1,0", "1,1" }, $.getDailyHistogram());
		assertEquals("86400000", $.getLifetimeOfTweets("iddqd"));
		System.out.println("Test passed, data is persistent");
	}
}

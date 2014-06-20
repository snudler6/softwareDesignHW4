package ac.il.technion.twc.unitTests.partA;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.impl.models.partA.UsersFirstTweetQueryHandler;

import com.googlecode.totallylazy.Lists;

public class UsersFirstTweetQueryHandlerTest {
	
	private static final TweetId FIRST_ID = new TweetId("first");
	private static final TweetId SECOND_ID = new TweetId("second");
	private static final TweetId THIRD_ID = new TweetId("third");
	private static final String userName = "Moshe";
	UsersFirstTweetQueryHandler qh = new UsersFirstTweetQueryHandler();

	@Before
	public void setUp() throws Exception {
		
		RootTweet first = Mockito.mock(RootTweet.class);
		Mockito.when(first.getId()).thenReturn(FIRST_ID);
		Mockito.when(first.getUserId()).thenReturn(userName);
		Mockito.when(first.getTime()).thenReturn(new Date(0L));
		
		Retweet second = Mockito.mock(Retweet.class);
		Mockito.when(second.getId()).thenReturn(SECOND_ID);
		Mockito.when(second.getUserId()).thenReturn(userName);
		Mockito.when(second.getTime()).thenReturn(new Date(1L));
		
		Retweet third = Mockito.mock(Retweet.class);
		Mockito.when(third.getId()).thenReturn(THIRD_ID);
		Mockito.when(third.getUserId()).thenReturn(userName);
		Mockito.when(third.getTime()).thenReturn(new Date(2L));

		
		ITweetsRepository repository = Mockito.mock(ITweetsRepository.class);
		
		
		qh.onTweetsAdded(Lists.list(second, first, third), repository);
	}

	@Test
	public void test() {
		assertEquals(FIRST_ID, qh.getUsersFirstTweetId(userName));
	}

}

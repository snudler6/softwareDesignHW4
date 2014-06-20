package ac.il.technion.twc.unitTests.partB;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.models.partB.TweetsNumberByUserQueryHandler;

import com.googlecode.totallylazy.Lists;

public class TweetsNumberByUserQueryHandlerTest
{
	private static final String USER_NAME = "Moshe";
	private static final String USER_NAME2 = "Aharon";
	
	TweetsNumberByUserQueryHandler qh = new TweetsNumberByUserQueryHandler();

	@Before
	public void setUp() throws Exception {
		
		Tweet firstMoshe = Mockito.mock(Tweet.class);
		Mockito.when(firstMoshe.getUserId()).thenReturn(USER_NAME);
		Tweet secondMoshe = Mockito.mock(Tweet.class);
		Mockito.when(secondMoshe.getUserId()).thenReturn(USER_NAME);
		Tweet thirdMoshe = Mockito.mock(Tweet.class);
		Mockito.when(thirdMoshe.getUserId()).thenReturn(USER_NAME);
		
		Tweet noUserTweet = Mockito.mock(Tweet.class);
		Mockito.when(noUserTweet.getUserId()).thenReturn("");
		
		Tweet anotherUserTweet = Mockito.mock(Tweet.class);
		Mockito.when(anotherUserTweet.getUserId()).thenReturn(USER_NAME2);
		
		
		ITweetsRepository repository = Mockito.mock(ITweetsRepository.class);
		
		
		qh.onTweetsAdded(Lists.list(secondMoshe, firstMoshe, thirdMoshe, noUserTweet, anotherUserTweet), repository);
	}

	@Test
	public void test() {
		assertEquals("3", qh.getTweetsNumberByUser(USER_NAME));
		assertEquals("1", qh.getTweetsNumberByUser(USER_NAME2));
	}
}

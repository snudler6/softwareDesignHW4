package ac.il.technion.twc.unitTests.partC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.models.partC.Pair;
import ac.il.technion.twc.impl.models.partC.TweetsHashtagsCouplingQueryHandler;

import com.googlecode.totallylazy.Lists;

public class TweetsHashtagsCouplingQueryHandlerTest {
	
	private static final String hashtag1 = "hashtag1";
	private static final String hashtag2 = "hashtag2";
	private static final String hashtag3 = "hashtag3";
	private static final String hashtag4 = "hashtag4";
	private static final String hashtag5 = "hashtag5";
	
	TweetsHashtagsCouplingQueryHandler qh = new TweetsHashtagsCouplingQueryHandler();

	@Before
	public void setUp() throws Exception {
		Tweet tweet1 = Mockito.mock(Tweet.class);
		Mockito.when(tweet1.getHashtags()).thenReturn(
				Lists.list(hashtag1, hashtag2, hashtag3, hashtag4, hashtag5));

		Tweet tweet2 = Mockito.mock(Tweet.class);
		Mockito.when(tweet2.getHashtags()).thenReturn(
				Lists.list(hashtag1, hashtag2));

		Tweet tweet3 = Mockito.mock(Tweet.class);
		Mockito.when(tweet3.getHashtags()).thenReturn(
				Lists.list(hashtag5));

		Tweet tweet4 = Mockito.mock(Tweet.class);
		Mockito.when(tweet4.getHashtags()).thenReturn(
				Lists.list(hashtag1, hashtag2));
		
		Tweet tweet5 = Mockito.mock(Tweet.class);
		Mockito.when(tweet5.getHashtags()).thenReturn(
				Lists.list(hashtag1, hashtag3));
		
		ITweetsRepository repository = Mockito.mock(ITweetsRepository.class);
		
		qh.onTweetsAdded(Lists.list(tweet1, tweet2, tweet3, tweet4, tweet5), repository);
		
	}

	@Test
	public void test() {
		assertTrue(qh.getMostCoupled(0).isEmpty());
		
		List<Pair<String>> mostCoupled = qh.getMostCoupled(1);
		assertEquals(1, mostCoupled.size());
		assertEquals(hashtag1 + ", " + hashtag2, mostCoupled.get(0).toString());

		List<Pair<String>> secondMostCoupled = qh.getMostCoupled(1);
		assertEquals(2, secondMostCoupled.size());
		assertEquals(hashtag1 + ", " + hashtag2, mostCoupled.get(0).toString());
		assertEquals(hashtag1 + ", " + hashtag3, mostCoupled.get(1).toString());

		
	}

}

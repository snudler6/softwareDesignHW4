package ac.il.technion.twc.impl.models.partC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partC.ITweetsHashtagsCouplingQueryHandler;

public class TweetsHashtagsCouplingQueryHandler implements
		ITweetsHashtagsCouplingQueryHandler {

	private static final long serialVersionUID = 4958073188101045085L;

	public static class ValueComparator implements Serializable,
			Comparator<Pair<String>> {

		private static final long serialVersionUID = -3909198429646950164L;
		private final Map<Pair<String>, Integer> base;
		

		public ValueComparator(Map<Pair<String>, Integer> hashtagsPairs) {
			base = hashtagsPairs;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(Pair<String> a, Pair<String> b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}

	private Map<Pair<String>, Integer> hashtagPairsValues;
	private NavigableMap<Pair<String>, Integer> hashtagPairs;

	public TweetsHashtagsCouplingQueryHandler() {
		this.hashtagPairsValues = new HashMap<Pair<String>, Integer>();
		this.hashtagPairs = new TreeMap<Pair<String>, Integer>(new ValueComparator(hashtagPairsValues));
	}

	@Override
	public void onTweetsAdded(List<Tweet> tweets,
			ITweetsRepository tweetsRepository) {
		for (Tweet tweet : tweets) {
			List<String> hashtags = tweet.getHashtags();
			for (int i = 0; i < hashtags.size(); ++i)
				for (int j = i + 1; j < hashtags.size(); ++j)
					incPair(hashtags.get(i), hashtags.get(j));
		}
	}
	
	private void putPair(Pair<String> key, Integer val) {
		// should put first in values, for the compression will work
		// in hashtagPairs map. 
		hashtagPairsValues.put(key, val);
		hashtagPairs.put(key, val);
	}

	private void incPair(String hashtag, String hashtag2) {
		Pair<String> pair = new Pair<String>(hashtag, hashtag2);
		Integer coupling = hashtagPairsValues.get(pair);
		if (coupling == null) {
			putPair(pair, new Integer(1));
		}
		else
			putPair(pair, coupling + 1);

	}

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets) {
		// this has no effect on this data query
	}

	@Override
	public List<Pair<String>> getMostCoupled(int k) {
		List<Pair<String>> res = new ArrayList<Pair<String>>(k);
		Iterator<Pair<String>> iter = hashtagPairs.navigableKeySet()
				.descendingIterator();
		for (int i = 0; i < k; ++i) {
			if (!iter.hasNext())
				break;
			res.add(iter.next());
		}
		return res;

	}

}

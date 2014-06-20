package ac.il.technion.twc.api.dependencies;

import ac.il.technion.twc.api.TweetsManager;
import ac.il.technion.twc.api.TweetsRepository;
import ac.il.technion.twc.api.interfaces.ITweetsManager;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.impl.models.TweetsHashtagsQueryHandler;
import ac.il.technion.twc.impl.models.TweetsTemporalHistogram;
import ac.il.technion.twc.impl.services.ITweetsHashtagsQueryHandler;
import ac.il.technion.twc.impl.services.ITweetsTemporalHistogramQueryHandler;

import com.google.inject.AbstractModule;

public class TweetModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(ITweetsManager.class).to(TweetsManager.class);
		bind(ITweetsRepository.class).to(TweetsRepository.class);
		bind(ITweetsManager.class).to(TweetsManager.class);
		
		bind(ITweetsHashtagsQueryHandler.class).to(TweetsHashtagsQueryHandler.class);
		bind(ITweetsTemporalHistogramQueryHandler.class).to(TweetsTemporalHistogram.class);
	}
}

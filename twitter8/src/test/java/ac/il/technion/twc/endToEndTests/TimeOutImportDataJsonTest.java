package ac.il.technion.twc.endToEndTests;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ac.il.technion.twc.FuntionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class TimeOutImportDataJsonTest
{
	FuntionalityTester target;
	IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryMillionJsonTweetsHalfOfThemRetweets");
	IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexMillionJsonTweetsHalfOfThemRetweets");
	String[] lines;
	File file;

	public String getJsonTweet(Date date, String id, String tweetedId, String text) throws Exception
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);

		final String JSON_CREATED_AT = "created_at";
		final String JSON_ID = "id_str";
		final String JSON_TEXT = "text";
		final String JSON_TWEETED_TWEET = "retweeted_status";

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JSON_CREATED_AT, dateFormat.format(date));
		jsonObject.put(JSON_ID, id);
		jsonObject.put(JSON_TEXT, text);
		if (tweetedId != null)
		{
			JSONObject jsonObjectTweeted = new JSONObject();
			jsonObjectTweeted.put(JSON_ID, tweetedId);
			jsonObject.put(JSON_TWEETED_TWEET, jsonObjectTweeted);
		}

		return jsonObject.toString();
	}
	
	private Date fromUTC(int year, int month, int date, int hrs, int min, int sec)
	{
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.set(year, month, date, hrs, min, sec);
		return calendar.getTime();
	}
	
	@Before
	public void setup() throws Exception
	{
		this.target = new FuntionalityTester(repositoryDataManager, indexDataManager);

		/* To create the big file */
		Path filePath = new File("./src/test/resources/MillionJsonTweetsHalfOfThemRetweets").toPath();
		File file = filePath.toFile();
		if (!file.exists())
		{
			FileWriter writer = new FileWriter(filePath.toString());
			for (Integer i = 0; i < 500000; i++)
			{
				writer.write(getJsonTweet(fromUTC(2000, 2, 20, 2, 2, 2), i.toString(), null, "aaaaaaaaaaa")+"\n");
				writer.write(getJsonTweet(fromUTC(1000, 2, 20, 2, 2, 2), "r"+i.toString(), i.toString(), "rrrrrrrrrrrrr")+"\n");
			}
			writer.close();
		}

		/* To create the persistent data */
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		lines = stringList.toArray(new String[] {});
	}

	@Test(timeout = 1000 * 60 * 30)
	@Ignore
	public void setupIndex_TimeOutTest_MillionTweetsInTwoSeconds() throws Exception
	{
		this.target.importDataJson(lines);
	}

	@After
	public void tear()
	{
		repositoryDataManager.cleanData();
		indexDataManager.cleanData();
		
	}
}

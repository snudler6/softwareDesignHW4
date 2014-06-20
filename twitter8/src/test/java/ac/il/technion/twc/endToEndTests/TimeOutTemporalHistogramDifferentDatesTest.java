package ac.il.technion.twc.endToEndTests;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.oldFuntionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class TimeOutTemporalHistogramDifferentDatesTest
{
	oldFuntionalityTester target;
	IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryMillionTweetsDiffDates");
	IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexMillionTweetsDiffDates");
	Random rand = new Random();
	int year1;
	int year2;

	@Before
	public void setup() throws Exception
	{
		this.target = new oldFuntionalityTester(repositoryDataManager, indexDataManager);

		/* To create the big file */
		Path filePath = new File("./src/test/resources/MillionTweetsDiffDates").toPath();
		File file = filePath.toFile();
		if (!file.exists())
		{
			FileWriter writer = new FileWriter(filePath.toString());
			for (int i = 0; i < 1000000; i++)
			{
				int year = rand.nextInt(3000) + 1000;
				writer.write("04/04/" + year + " 12:00:00, " + i + "\n");
			}
			writer.close();
			
			/* To create the persistent data */
			Charset charset = Charset.defaultCharset();
			List<String> stringList = Files.readAllLines(filePath, charset);
			String[] lines = stringList.toArray(new String[] {});
			this.target.importData(lines);
		}	
		this.target.setupIndex();

		year1 = rand.nextInt(3000) + 1000;
		year2 = rand.nextInt(3000) + 1000;
		if (year2 < year1)
		{
			int tmp = year2;
			year2 = year1;
			year1 = tmp;
		}
	}

	@Test(timeout = 5000) // the maximum is 10 sec (i.e. 10000)
	public void setupIndex_TimeOutTest_MillionTweetsInTwoSeconds() throws Exception
	{
		this.target.getTemporalHistogram("01/01/" + year1 + " 12:00:00", "01/01/" + year2 + " 12:00:00");
	}

	@After
	public void tear()
	{
		repositoryDataManager.cleanData();
		indexDataManager.cleanData();
	}
}

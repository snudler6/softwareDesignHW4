package ac.il.technion.twc.endToEndTests;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.oldFuntionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class TimeOutSetupIndexTest
{
	oldFuntionalityTester target;
	IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryMillionTweetsHalfOfThemRetweets");
	IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexMillionTweetsHalfOfThemRetweets");

	@Before
	public void setup() throws Exception
	{
		this.target = new oldFuntionalityTester(repositoryDataManager, indexDataManager);

		/* To create the big file */
		Path filePath = new File("./src/test/resources/MillionTweetsHalfOfThemRetweets").toPath();
		File file = filePath.toFile();
		if (!file.exists())
		{
			FileWriter writer = new FileWriter(filePath.toString());
			for (int i = 0; i < 500000; i++)
			{
				writer.write("04/04/2014 12:00:00, " + i + "\n");
				writer.write("04/04/2014 12:00:00, r" + i + ", " + i + "\n");
			}
			writer.close();
			
			/* To create the persistent data */
			Charset charset = Charset.defaultCharset();
			List<String> stringList = Files.readAllLines(filePath, charset);
			String[] lines = stringList.toArray(new String[] {});
			this.target.importData(lines);
		}		
	}

	@Test(timeout = 5000) // the maximum is 10 sec (i.e. 10000)
	public void setupIndex_TimeOutTest_MillionTweetsInTwoSeconds() throws Exception
	{
		target.setupIndex();
	}

	@After
	public void tear()
	{
		repositoryDataManager.cleanData();
		indexDataManager.cleanData();
	}
}

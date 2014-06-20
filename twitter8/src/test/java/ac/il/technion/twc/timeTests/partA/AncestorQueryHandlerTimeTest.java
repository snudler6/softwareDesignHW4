package ac.il.technion.twc.timeTests.partA;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FunctionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class AncestorQueryHandlerTimeTest {

	FunctionalityTester target;
	IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryMillionTweetsSameDate");
	IDataManager indexDataManager = new FileDataManager("./src/test/resources/MillionTweetsHalfOfThemRetweets");
	
	@Before
	public void setUp() throws Exception {
		this.target = new FunctionalityTester(repositoryDataManager, indexDataManager);

		/* To create the big file */
		Path filePath = new File("./src/test/resources/MillionTweetsHalfOfThemRetweets").toPath();
		File file = filePath.toFile();
		if (!file.exists())
		{
			FileWriter writer = new FileWriter(filePath.toString());
			for (int i = 0; i < 1000000; i++)
			{
				writer.write("04/04/2014 12:00:00, " + i + "\n");
			}
			writer.close();
			
			/* To create the persistent data */
			Charset charset = Charset.defaultCharset();
			List<String> stringList = Files.readAllLines(filePath, charset);
			String[] lines = stringList.toArray(new String[] {});
			this.target.importData(lines);
		}				
		this.target.setupIndex();
	}

	
	@Test
	public final void test() {
		fail("Not yet implemented");
	}

}

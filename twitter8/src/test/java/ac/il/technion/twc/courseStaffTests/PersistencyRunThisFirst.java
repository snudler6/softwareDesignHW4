package ac.il.technion.twc.courseStaffTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ac.il.technion.twc.oldFuntionalityTester;

public class PersistencyRunThisFirst {
	
	public static void main(String[] args) throws Exception {
		oldFuntionalityTester $ = new oldFuntionalityTester();
		$.cleanPersistentData();
		try (Scanner scanner = new Scanner(PersistencyRunThisFirst.class.getResourceAsStream("small_sample.txt"))) {
			List<String> lines = new ArrayList<>();
			while (scanner.hasNextLine())
				lines.add(scanner.nextLine());
			$.importData(lines.toArray(new String[0]));
		}
		$.importData(new String[] { "04/04/2014 12:00:00, iddqd", "05/04/2014 12:00:00, idkfa, iddqd" });
		System.out.println("Done, you can now run PersistencyRunThisSecond main");
	}
	
}

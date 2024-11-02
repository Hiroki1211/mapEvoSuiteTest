package setup;

import java.io.File;

public class SetUp {

	public static void main(String[] args) {
		String path1 = "src/test/resources/EvoSuite/";
		String path2 = "src/test/resources/IntegrationTestTrace/";
		
		File file1 = new File(path1);
		File file2 = new File(path2);
		
		file1.mkdir();
		file2.mkdir();
	}
	
}

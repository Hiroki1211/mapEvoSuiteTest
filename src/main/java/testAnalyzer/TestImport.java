package testAnalyzer;

public class TestImport {

	private String statement;
	private String importClass;
	
	public TestImport(String s, String iC) {
		statement = s;
		importClass = iC;
	}
	
	public String getStatement() {
		return statement;
	}
	
	public String getImportClass() {
		return importClass;
	}
}

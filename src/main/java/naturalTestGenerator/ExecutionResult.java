package naturalTestGenerator;

import java.util.ArrayList;

public class ExecutionResult {

	private String className;
	private ArrayList<String> results = new ArrayList<String>();
	
	public ExecutionResult(String className) {
		this.className = className;
	}
	
	public void addResults(String input) {
		results.add(input);
	}
	
	public String getClassName() {
		return className;
	}
	
	public ArrayList<String> getResults(){
		return results;
	}
}

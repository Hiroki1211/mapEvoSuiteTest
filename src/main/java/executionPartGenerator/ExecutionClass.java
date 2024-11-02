package executionPartGenerator;

import java.util.ArrayList;

import testAnalyzer.TestImport;

public class ExecutionClass {

	private String className;
	private ArrayList<ExecutionPart> executionPartLists = new ArrayList<ExecutionPart>();
	private ArrayList<TestImport> importLists = new ArrayList<TestImport>();
	private ArrayList<String> contents = new ArrayList<String>();
	
	public ExecutionClass(String className) {
		this.className = className;
	}
	
	public void setExecutionPart(ArrayList<ExecutionPart> executionPartLists) {
		this.executionPartLists = executionPartLists;
	}
	
	public void setImportLists(ArrayList<TestImport> importLists) {
		this.importLists = importLists;
	}
	
	public void setContents(ArrayList<String> contents) {
		this.contents = contents;
	}
	
	public String getClassName() {
		return className;
	}
	
	public ArrayList<ExecutionPart> getExecutionPartLists(){
		return executionPartLists;
	}
	
	public ArrayList<TestImport> getImportLists(){
		return importLists;
	}
	
	public ArrayList<String> getContents(){
		return contents;
	}
}

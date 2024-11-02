package testAnalyzer;

import java.util.ArrayList;

import testMatcher.SameExecutePath;

public class TestClass {

	private String className;
	private String packageName;
	private ArrayList<TestImport> importLists = new ArrayList<TestImport>();
	private ArrayList<Test> testLists = new ArrayList<Test>();
	
	private ArrayList<SameExecutePath> sameExecutePathLists = new ArrayList<SameExecutePath>();
	
	public TestClass(String cN, String pN, ArrayList<TestImport> iL) {
		className = cN;
		packageName = pN;
		importLists = new ArrayList<TestImport>(iL);
	}
	
	public ArrayList<SameExecutePath> getSameExecutePathLists(){
		return sameExecutePathLists;
	}
	
	public void setSameExecutePathLists(ArrayList<SameExecutePath> input) {
		sameExecutePathLists = input;
	}
	
	public void display() {
		System.out.println("className:" + className);
		System.out.println("packageName:" + packageName);
		System.out.print("importList:");
		for(int i = 0; i < importLists.size(); i++) {
			System.out.print(importLists.get(i).getImportClass() + ", ");
		}
		System.out.println();
	}
	
	public void addTestLists(Test input) {
		testLists.add(input);
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public ArrayList<TestImport> getImportLists(){
		return importLists;
	}
	
	public ArrayList<Test> getTestLists(){
		return testLists;
	}
}

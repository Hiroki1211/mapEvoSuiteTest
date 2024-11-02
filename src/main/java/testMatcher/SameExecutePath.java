package testMatcher;

import java.util.ArrayList;

import testAnalyzer.Test;

public class SameExecutePath {

	private ArrayList<Test> testLists = new ArrayList<Test>();

	public void addTestLists(Test input) {
		testLists.add(input);
	}

	public ArrayList<Test> getTestLists(){
		return testLists;
	}

}

package testMatcher;

import java.util.ArrayList;

import paramaterExtracter.Instance;

public class SameExecuteExtractPath {

	private ArrayList<Instance> instanceLists = new ArrayList<Instance>();
	
	public void addInstanceLists(Instance input) {
		instanceLists.add(input);
	}
	
	public ArrayList<Instance> getInstanecLists(){
		return instanceLists;
	}
	
}

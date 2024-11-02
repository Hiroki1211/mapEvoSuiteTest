package exportFileGenerator;

import java.util.ArrayList;

public class NaturalTest {

	private ArrayList<String> contentLists = new ArrayList<String>();
	
	public void addContentLists(String input) {
		contentLists.add(input);
	}
	
	public ArrayList<String> getContentLists(){
		return contentLists;
	}
}

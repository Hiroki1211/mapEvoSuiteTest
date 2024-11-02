package analyzer;

public class Class {

	private String accessModifier = "";
	private String name = "";
	
	public Class(String aM, String n) {
		accessModifier = aM;
		name = n;
	}
	
	public String getAccessModifier() {
		return accessModifier;
	}
	
	public String getName() {
		return name;
	}
}

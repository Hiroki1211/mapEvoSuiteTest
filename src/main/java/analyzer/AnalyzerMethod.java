package analyzer;

import java.util.ArrayList;

public class AnalyzerMethod {

	private String accessModifier = "";
	private String methodModifier = "";
	private String returnValueType = "";
	private String name = "";
	private Class ownerClass;
	
	private ArrayList<String> argumentTypeLists = new ArrayList<String>();
	
	public void display() {
		System.out.println("ownerClass:" + ownerClass.getName());
		System.out.println("accessModifier:" + accessModifier);
		System.out.println("methodModifier:" + methodModifier);
		System.out.println("returnValueType:" + returnValueType);
		System.out.println("name:" + name);
		System.out.println("argument:" + argumentTypeLists);
		System.out.println();
	}
	
	public AnalyzerMethod(Class oC) {
		ownerClass = oC;
	}
	
	public void setAccessModifier(String input) {
		accessModifier = input;
	}
	
	public void setMethodModifier(String input) {
		methodModifier = input;
	}
	
	public void setReturnValueType(String input) {
		returnValueType = input;
	}
	
	public void setName(String input) {
		name = input;
	}
	
	public void addArgumentTypeLists(String input) {
		argumentTypeLists.add(input);
	}
	
	public String getAccessModifier() {
		return accessModifier;
	}
	
	public String getMethodModifier() {
		return methodModifier;
	}
	
	public String getReturnValueType() {
		return returnValueType;
	}
	
	public String getName() {
		return name;
	}
	
	public Class getOwnerClass() {
		return ownerClass;
	}
	
	public ArrayList<String> getTypeArgumentLists(){
		return argumentTypeLists;
	}
}

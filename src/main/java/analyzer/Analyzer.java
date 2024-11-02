package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Analyzer {
	
	private String accessModifierLists[] = {
			"public",
			"private",
			"protected"
	};
	
	private String methodOrFieldModifierLists[] = {
			"final",
			"static"
	};
	
	private String methodModifierLists[] = {
			"abstract",
			"synchronized",
			"native",
			"strictfp"
	};
	
	private String fieldModifierLists[] = {
			"transient",
			"volatile"
	};
	
	private ArrayList<AnalyzerMethod> methodLists = new ArrayList<AnalyzerMethod>();
	private ArrayList<AnalyzerVariable> variableLists = new ArrayList<AnalyzerVariable>();

	public static void main(String[] argv) {
		Analyzer analyzer = new Analyzer();
		ArrayList<String> fileNameLists = analyzer.getFileNameLists();
		analyzer.run(fileNameLists);
		analyzer.displayMethodLists();
		analyzer.displayVariableLists();
	}
	
	public ArrayList<AnalyzerMethod> getMethodLists(){
		return methodLists;
	}
	
	public ArrayList<AnalyzerVariable> getVariableLists(){
		return variableLists;
	}
	
	public void displayMethodLists() {
		System.out.println("---Method---");
		for(int i = 0; i < methodLists.size(); i++) {
			methodLists.get(i).display();
		}
	}
	
	public void displayVariableLists() {
		System.out.println("---Variable---");
		for(int i = 0; i < variableLists.size(); i++) {
			variableLists.get(i).display();
		}
	}
	
	public ArrayList<String> getFileNameLists(){
		
		ArrayList<String> fileNameLists = new ArrayList<String>();
		fileNameLists.add("src/main/resources/ex03/Executer.java");
		fileNameLists.add("src/main/resources/ex03/Example.java");
		
		return fileNameLists;
	}
	
	public void run(ArrayList<String> inputFileNameLists) {
		
		for(int fileNum = 0; fileNum < inputFileNameLists.size(); fileNum ++) {
			File inputFile = new File(inputFileNameLists.get(fileNum));
			try {
				FileReader fr = new FileReader(inputFile);
				BufferedReader br = new BufferedReader(fr);
				String readLine;
				Class ownerClass = null;
				AnalyzerMethod method = null;
				
				while((readLine = br.readLine()) != null) {
					String[] splitBracket = readLine.split("[(){]");
//					System.out.println("******");
//					for(int i = 0; i < splitBracket.length; i++) {
//						System.out.println(splitBracket[i]);
//					}
					
					ArrayList<String> splitReadLine = this.splitContent(splitBracket[0]);
					String accessModifier = "";
					String methodOrFieldModifier = "";
					String methodModifier = "";
					String fieldModifier = "";
					String type = "";
					String name = "";
					
					// method or variable の生成
					if(splitReadLine.size() > 1) {
						
						if((accessModifier = this.isAccessModifier(splitReadLine.get(0))) != null ) {
							if(splitReadLine.get(1).equals("class")) {
								ownerClass = new Class(accessModifier, splitReadLine.get(2));
							}
							
							if((methodOrFieldModifier = this.isMethodOrFieldModiferLists(splitReadLine.get(1))) != null) {
								type = splitReadLine.get(2);
								name = splitReadLine.get(3);
								
								method = this.createMethodOrVariable(ownerClass, accessModifier, methodOrFieldModifier, type, name, splitBracket[1]);
								
							}else if((methodModifier = this.isMethodModiferLists(splitReadLine.get(1))) != null) {
								type = splitReadLine.get(2);
								name = splitReadLine.get(3);
								
								method = this.createMethod(ownerClass, accessModifier, methodModifier, type, name, splitBracket[1]);
								
							}else if((fieldModifier = this.isFieldModiferLists(splitReadLine.get(1))) != null) {
								type = splitReadLine.get(2);
								name = splitReadLine.get(3);
								
								this.createVariable(ownerClass, accessModifier, fieldModifier, type, name);
								
							}else {
								if(splitReadLine.size() == 2) {									
									this.createMethod(ownerClass, accessModifier, methodOrFieldModifier, splitReadLine.get(1), "<init>", splitBracket[1]);
								}else {
									type = splitReadLine.get(1);
									name = splitReadLine.get(2);
									
									if(!type.contains("(") && !type.equals("class")) {
										if(readLine.contains("=")) {
											this.createVariable(ownerClass, accessModifier, methodOrFieldModifier, type, name);
										}else if(splitBracket.length > 1) {
											method = this.createMethodOrVariable(ownerClass, accessModifier, null, type, name, splitBracket[1]);
										}else {
											method = this.createMethodOrVariable(ownerClass, accessModifier, null, type, name, "");
										}
										
									}
								}
								
							}
						}
						
						// getter の特定
						if(method != null) {
							if(method.getName().contains("get")) {
								String getterMethodName = method.getName();
								getterMethodName = getterMethodName.replace("get", "");
								getterMethodName = getterMethodName.split("\\(", 1)[0];
								getterMethodName = getterMethodName.substring(0, 1).toLowerCase() + getterMethodName.substring(1);
								
								for(int i = 0; i < variableLists.size(); i++) {
									if(variableLists.get(i).getName().equals(getterMethodName)) {
										variableLists.get(i).setGetterMethod(method);
										break;
									}
								}
							}else if(method.getName().contains("set")) {
								String setterMethodName = method.getName();
								setterMethodName = setterMethodName.replace("set", "");
								setterMethodName = setterMethodName.split("\\(", 1)[0];
								setterMethodName = setterMethodName.substring(0, 1).toLowerCase() + setterMethodName.substring(1);
								
								for(int i = 0; i < variableLists.size(); i++) {
									if(variableLists.get(i).getName().equals(setterMethodName)) {
										variableLists.get(i).setSetterMethod(method);
										break;
									}
								}
							}
						}
					}
				}
				
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<String> splitContent(String input) {
		ArrayList<String> result = new ArrayList<String>();
		input = input.replace("{", "");
		String[] split = input.split(" +");
		
		for(int i = 0; i < split.length; i++) {
			String splitContent = split[i];
			String[] splitSplitContent = splitContent.split("[=\t]");
			if(splitSplitContent.length == 1) {
				result.add(splitSplitContent[0]);
			}else {
				for(int j = 0; j < splitSplitContent.length; j++) {
					result.add(splitSplitContent[j]);
				}
			}
		}
		
		if(result.size() > 0) {
			while(result.size() != 0 && result.get(0).equals("")) {
				result.remove(0);
			}
		}
		
		return result;
	}
	
	private AnalyzerMethod createMethod(Class ownerClass, String accessModifier, String methodOrFieldModifier, String type, String name, String argument) {
		String argumentSplit[] = argument.split("[,]");
		
		AnalyzerMethod method = new AnalyzerMethod(ownerClass);
		method.setAccessModifier(accessModifier);
		method.setMethodModifier(methodOrFieldModifier);
		method.setReturnValueType(type);
		method.setName(name);
		methodLists.add(method);
		
		if(!argumentSplit[0].equals("")) {
			for(int i = 0; i < argumentSplit.length; i++) {
				String splitSpace[] = argumentSplit[i].split(" +");
				if(splitSpace[0].equals("")) {
					splitSpace[0] = splitSpace[1];
					splitSpace[1] = splitSpace[2];
				}	
				
				method.addArgumentTypeLists(splitSpace[0]);
			}
		}
		
		return method;
	}
	
	private void createVariable(Class ownerClass, String accessModifier, String methodOrFieldModifier, String type, String name) {
		if(name.contains("[]")) {
			type += "[]";
			name = name.replace("[]", "");
		}
		
		name = name.replace(";", "");
		
		if(!type.equals("class")) {
			AnalyzerVariable variable = new AnalyzerVariable(ownerClass);
			variable.setAccessModifier(accessModifier);
			variable.setFieldModifier(methodOrFieldModifier);
			variable.setType(type);
			variable.setName(name);
			variableLists.add(variable);
		}
	}
	
	private AnalyzerMethod createMethodOrVariable(Class ownerClass, String accessModifier, String methodOrFieldModifier, String type, String name, String argument) {
		if(!name.contains(";")) {
			String split[] = name.split("\\(");
			String argumentSplit[] = argument.split("[,]");
			
//			for(int i = 0; i < argumentSplit.length; i++) {
//				System.out.println(argumentSplit[i]);
//			}

			AnalyzerMethod method = new AnalyzerMethod(ownerClass);
			method.setAccessModifier(accessModifier);
			method.setMethodModifier(methodOrFieldModifier);
			method.setReturnValueType(type);
			method.setName(split[0]);
			methodLists.add(method);
			
			if(!argumentSplit[0].equals("")) {
				for(int i = 0; i < argumentSplit.length; i++) {
					String splitSpace[] = argumentSplit[i].split(" +");
					if(splitSpace[0].equals("")) {
						splitSpace[0] = splitSpace[1];
						splitSpace[1] = splitSpace[2];
					}	
					
					method.addArgumentTypeLists(splitSpace[0]);
				}
			}
			
			return method;
		}else {
			if(name.contains("[]")) {
				type += "[]";
				name = name.replace("[]", "");
			}
			
			name = name.replace(";", "");
			
			if(!type.equals("class")) {
				AnalyzerVariable variable = new AnalyzerVariable(ownerClass);
				variable.setAccessModifier(accessModifier);
				variable.setFieldModifier(methodOrFieldModifier);
				variable.setType(type);
				variable.setName(name);
				variableLists.add(variable);
			}
			
			return null;
		}
	}
	
	private String isAccessModifier(String input) {
		for(int i = 0; i < accessModifierLists.length; i++) {
			if(input.equals(accessModifierLists[i])) {
				return accessModifierLists[i];
			}
		}
		
		return null;
	}
	
	public String isMethodOrFieldModiferLists(String input) {
		for(int i = 0; i < methodOrFieldModifierLists.length; i++) {
			if(input.equals(methodOrFieldModifierLists[i])) {
				return methodOrFieldModifierLists[i];
			}
		}
		
		return null;
	}
	
	public String isMethodModiferLists(String input) {
		for(int i = 0; i < methodModifierLists.length; i++) {
			if(input.equals(methodModifierLists[i])) {
				return methodModifierLists[i];
			}
		}
		
		return null;
	}
	
	public String isFieldModiferLists(String input) {
		for(int i = 0; i < fieldModifierLists.length; i++) {
			if(input.equals(fieldModifierLists[i])) {
				return fieldModifierLists[i];
			}
		}
		
		return null;
	}
}

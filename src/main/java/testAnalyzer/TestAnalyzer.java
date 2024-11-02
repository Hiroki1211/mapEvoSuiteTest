package testAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import analyzer.Analyzer;
import analyzer.AnalyzerMethod;

public class TestAnalyzer {

	public static void main(String[] args){
		TestAnalyzer testAnalyzer = new TestAnalyzer();
		ArrayList<String> inputFileNameLists = testAnalyzer.getInputFileNameLists();
		ArrayList<String> inputTestFileNameLists = testAnalyzer.getInputTestFileNameLists();
		Analyzer analyzer = new Analyzer();
		analyzer.run(inputFileNameLists);
		testAnalyzer.run(inputTestFileNameLists, analyzer.getMethodLists());
	}
	
	private ArrayList<String> getInputFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("src/main/resources/ex03/Example.java");
		result.add("src/main/resources/ex03/Executer.java");
		
		return result;
	}
	
	private ArrayList<String> getInputTestFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("src/main/resources/ex03/Example_ESTest.java");
		result.add("src/main/resources/ex03/Executer_ESTest.java");
		
		return result;
	}
	
	public ArrayList<TestClass> run(ArrayList<String> inputTestFileNameLists, ArrayList<AnalyzerMethod> analyzerMethodLists) {
		ArrayList<TestClass> testClassLists = this.createtestClassLists(inputTestFileNameLists);
		this.analyzetestClass(testClassLists);
		// assert attach method
		this.attachAssertionAndMethod(testClassLists);
		// detect Analyzer Method
		this.detectAnalzerMethod(testClassLists, analyzerMethodLists);
		
		return testClassLists;
	}
	
	private void detectAnalzerMethod(ArrayList<TestClass> testClassLists, ArrayList<AnalyzerMethod> analyzerMethodLists) {
		for(int classNum = 0; classNum < testClassLists.size(); classNum++) {
			TestClass testClass = testClassLists.get(classNum);
			
			for(int testNum = 0; testNum < testClass.getTestLists().size(); testNum++){
				Test testTest = testClass.getTestLists().get(testNum);
				
				for(int methodNum = 0; methodNum < testTest.getMethodLists().size(); methodNum++) {
					TestMethod testMethod = testTest.getMethodLists().get(methodNum);
					
					ArrayList<String> argumentTypeLists = new ArrayList<String>();
					for(int argNum = 0; argNum < testMethod.getArgumentLists().size(); argNum++) {
						argumentTypeLists.add(this.gudgeVariableType(testMethod.getArgumentLists().get(argNum), testTest.getMethodLists()));
					}
					
					ArrayList<AnalyzerMethod> sameMethodNameLists = new ArrayList<AnalyzerMethod>();
					for(int anaNum = 0; anaNum < analyzerMethodLists.size(); anaNum++) {
						if(analyzerMethodLists.get(anaNum).getName().equals(testMethod.getMethodName())) {
							sameMethodNameLists.add(analyzerMethodLists.get(anaNum));
						}
					}
					
					if(sameMethodNameLists.size() == 1) {
						if(sameMethodNameLists.get(0).getName().equals("<init>")) {
							if(sameMethodNameLists.get(0).getOwnerClass().getName().equals(testMethod.getConstructorClass())) {
								testMethod.setAnalyzerMethod(sameMethodNameLists.get(0));
							}
						}else {
							testMethod.setAnalyzerMethod(sameMethodNameLists.get(0));
						}
						
					}else {
						for(int sameNum = 0; sameNum < sameMethodNameLists.size(); sameNum++) {
							AnalyzerMethod tmpAnalyzerMethod = sameMethodNameLists.get(sameNum);
							
							if(tmpAnalyzerMethod.getTypeArgumentLists().size() == argumentTypeLists.size()) {
								boolean sameMethodFlag = true;
								for(int argNum = 0; argNum < argumentTypeLists.size(); argNum++) {
									if(!tmpAnalyzerMethod.getTypeArgumentLists().get(argNum).equals(argumentTypeLists.get(argNum))) {
										sameMethodFlag = false;
										break;
									}
								}
								
								if(sameMethodFlag ) {
									testMethod.setAnalyzerMethod(tmpAnalyzerMethod);
									break;
								}
							}
						}
					}
					
//					System.out.println(testMethod.getStatement());
//					if(testMethod.getAnalyzerMethod() != null) {
//						testMethod.getAnalyzerMethod().display();
//					}
//					System.out.println("********");
				}
			}
		}
	}
	
	private String gudgeVariableType(String input, ArrayList<TestMethod> testMethodLists) {
		String result = "";
		
		if(input.equals("true") || input.equals("false")) {
			result = "boolean";
		}else if(isNumber(input)) {
			result = "int";
		}else if(input.contains("\"")) {
			result = "String";
		}else if(input.contains("'")) {
			result = "char";
		}else {
			for(int methodNum = 0; methodNum < testMethodLists.size(); methodNum++) {
				String variable = testMethodLists.get(methodNum).getReturnVariable();
				if(input.equals(variable)) {
					result = testMethodLists.get(methodNum).getReturnType();
					break;
				}
			}
			
			if(result.equals("")) {
				// long, float, double
				if(input.contains("l") || input.contains("L")) {
					result = "long";
				}else if(input.contains("f") || input.contains("F")) {
					result = "float";
				}else {
					result = "double";
				}
			}
		}
		
		return result;
	}
	
	private boolean isNumber(String val) {
		String regex = "\\A[-]?[0-9]+\\z";
		Pattern p = Pattern.compile(regex);
		Matcher m1 = p.matcher(val);
		return m1.find();
	}
	
	private void attachAssertionAndMethod(ArrayList<TestClass> testClassLists) {
		for(int classNum = 0; classNum < testClassLists.size(); classNum++) {
			TestClass testClass = testClassLists.get(classNum);
			
			for(int testNum = 0; testNum < testClass.getTestLists().size(); testNum++) {
				Test testTest = testClass.getTestLists().get(testNum);
				
				for(int assertionNum = 0; assertionNum < testTest.getAssertionLists().size(); assertionNum++) {
					TestAssertion testAssertion = testTest.getAssertionLists().get(assertionNum);
					
					if(!testAssertion.getVariable().equals("")) {
						String variable = testAssertion.getVariable();
						
						for(int methodNum = 0; methodNum < testTest.getMethodLists().size(); methodNum++) {
							if(testTest.getMethodLists().get(methodNum).getReturnVariable().equals(variable)) {
								testAssertion.setAssertionTargetMethod(testTest.getMethodLists().get(methodNum));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void analyzetestClass(ArrayList<TestClass> testClassLists) {
		for(int classNum = 0; classNum < testClassLists.size(); classNum++) {
			TestClass testClass = testClassLists.get(classNum);
			
			for(int testNum = 0; testNum < testClass.getTestLists().size(); testNum++) {
				Test testTest = testClass.getTestLists().get(testNum);
				ArrayList<String> body = testTest.getBody();
				
				for(int bodyNum = 0; bodyNum < body.size(); bodyNum++) {
					String[] splitBracket = body.get(bodyNum).split("[()]");
					if(splitBracket.length > 0) {
						String[] splitSpace = splitBracket[0].split(" +");
						
						if(splitSpace[1].equals("assertEquals")) {
							String assertion = body.get(bodyNum);
							assertion = assertion.replace(" ", "");
							assertion = assertion.replace("assertEquals(", "");
							assertion = assertion.replace(");", "");
							
							String[] splitAssertionArgument = assertion.split(",");
							splitAssertionArgument[0] = splitAssertionArgument[0].replace("(", "");
							splitAssertionArgument[0] = splitAssertionArgument[0].replace(")", "");
							TestAssertion testAssertion;
							String target = splitAssertionArgument[1];
							int i = 1;
							while(target.contains("\"") && (i + 1) < splitAssertionArgument.length) {
								i += 1;
								target = splitAssertionArgument[i];
							}
							
							if(target.contains(".")) {
								testAssertion = new TestAssertion(splitAssertionArgument[0], "", target, body.get(bodyNum));
							}else {
								testAssertion = new TestAssertion(splitAssertionArgument[0], target, "", body.get(bodyNum));
							}
							
							testTest.addAssertionLists(testAssertion);
						
						}else if(!splitSpace[1].equals("}") && !splitSpace[1].equals("try") && !splitSpace[1].equals("//")){
							TestMethod testMethod = new TestMethod(body.get(bodyNum));
							testTest.addMethodLists(testMethod);
							
						}else if(splitSpace[1].equals("try")) {
							TestMethod testMethod = new TestMethod(body.get(bodyNum), "try");
							testTest.addMethodLists(testMethod);
							
						}else if(splitSpace.length > 2 && splitSpace[2].equals("catch")){
							TestMethod testMethod = new TestMethod(body.get(bodyNum), "catch");
							testTest.addMethodLists(testMethod);
							
						}
					}
				}
			}
		}
	}
	
	private ArrayList<TestClass> createtestClassLists(ArrayList<String> inputFileNameLists){
		ArrayList<TestClass> testClassLists = new ArrayList<TestClass>();
		
		for(int fileNum = 0; fileNum < inputFileNameLists.size(); fileNum ++) {
			File inputFile = new File(inputFileNameLists.get(fileNum));
			
			try {
				FileReader fr = new FileReader(inputFile);
				BufferedReader br = new BufferedReader(fr);
				
				String readLine;
				
				String packageName = "";
				ArrayList<TestImport> importLists = new ArrayList<TestImport>();
				TestClass testClass = null;
				Test testTest = null;
				
				while((readLine = br.readLine()) != null) {
					if(readLine.length() != 0) {
						String[] splitBracket = readLine.split("[()]");
						String[] splitSpace = splitBracket[0].split(" +");
						
						if(splitSpace.length > 0) {
							if(splitSpace[0].equals("package")) {
								packageName = splitSpace[1].replace(";", "");
								
							}else if(splitSpace[0].equals("import")) {
								TestImport testImport;
								if(splitSpace[1].equals("static")) {
									testImport = new TestImport(readLine, splitSpace[2].replace(";", ""));
								}else {
									testImport = new TestImport(readLine, splitSpace[1].replace(";", ""));
								}
								
								importLists.add(testImport);
								
							}else if(splitSpace[0].equals("public")) {
								testClass = new TestClass(splitSpace[2], packageName, importLists);
								testClassLists.add(testClass);
								
							}else {
								if(splitSpace.length > 1) {
									if(splitSpace[1].equals("@Test")) {
										testTest = new Test();
										testClass.addTestLists(testTest);
										
									}else if(splitSpace[1].equals("public")) {
										testTest.setMethodName(splitSpace[3]);
										
									}else {
										if(testTest != null ) {
											testTest.addBody(readLine);
										}
									}
									
									if(testTest != null) {
										testTest.addContents(readLine);
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
		
		return testClassLists;
	}
}

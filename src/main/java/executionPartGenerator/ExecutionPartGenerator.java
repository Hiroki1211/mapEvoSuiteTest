package executionPartGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import analyzer.Analyzer;
import executer.Executer;
import testMatcher.ExecutePath;
import paramaterExtracter.ExtractClass;
import paramaterExtracter.ExtractMethod;
import paramaterExtracter.Instance;
import paramaterExtracter.ParamaterExtracter;
import pathExtracter.PathExtracter;
import pathExtracter.TraceMethodBlock;
import testAnalyzer.Test;
import testAnalyzer.TestAssertion;
import testAnalyzer.TestClass;
import testAnalyzer.TestImport;
import testAnalyzer.TestMethod;
import testMatcher.MatchingResult;
import testMatcher.PartiallyMatchingResult;
import testMatcher.Result;
import testMatcher.TestMatcher;
import tracer.Lexer;
import tracer.Trace;

public class ExecutionPartGenerator {

	private String executionTestPath = "src/main/java/executionTest/";
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ExecutionPartGenerator executionPartGenerator = new ExecutionPartGenerator();
		Executer executer = new Executer();
		
		ArrayList<String> inputFileNameLists = executer.getInputFileNameLists();
		ArrayList<String> inputEvoSuiteTestFileNameLists = executer.getInputEvoSuiteTestFileNameLists();
		ArrayList<String> inputEvoSuiteTestTraceFileNameLists = executer.getInputEvoSuiteTestTraceFileNameLists();
		String integrationTestTrace = "src/test/resources/IntegrationTestTrace/trace.json";
		
		// 1. product code analyze
		Analyzer analyzer = new Analyzer();
		analyzer.run(inputFileNameLists);
		
		// 2. evosuite test
		PathExtracter pathExtracter = new PathExtracter();
		ArrayList<TestClass> evoSuiteTestClassLists = pathExtracter.run(analyzer, inputEvoSuiteTestFileNameLists, inputEvoSuiteTestTraceFileNameLists);
		
		// 3. extract natural paramater
		File file = new File(integrationTestTrace);
		Lexer lexer = new Lexer(file);
		ArrayList<Trace> traceLists = lexer.getTraceLists();
		ParamaterExtracter paramaterExtracter = new ParamaterExtracter();
		ArrayList<ExtractClass> extractClassLists = paramaterExtracter.run(analyzer.getMethodLists(), analyzer.getVariableLists(), traceLists);
	
//		for(int i = 0; i < extractClassLists.size(); i++) {
//			System.out.println(extractClassLists.get(i).getOwnerClass());
//		}
		
		// 4. matching test
		TestMatcher testMatcher = new TestMatcher();
		ArrayList<Result> resultLists = testMatcher.run(evoSuiteTestClassLists, extractClassLists);
//		for(int i = 0; i < resultLists.size(); i++) {
//			resultLists.get(i).display();
//		}
		
		// 5. execute execution part
		executionPartGenerator.run(resultLists);
	}
	
	public void run(ArrayList<Result> resultLists) {
		
		ArrayList<ExecutionClass> executionClassLists = this.createExecutionClass(resultLists);
		this.createExecutionTest(executionClassLists);
		this.exportExecutionTest(executionClassLists);
		ArrayList<String> executeExecutionTestContents = this.createExecuteExecutionTest(executionClassLists);
		this.exportExecuteExecutionTest(executeExecutionTestContents);
	}
	
	public ArrayList<ExecutionClass> getExecutionClass(ArrayList<Result> resultLists){
		ArrayList<ExecutionClass> executionClassLists = this.createExecutionClass(resultLists);
		return executionClassLists;
	}
	
	private void exportExecuteExecutionTest(ArrayList<String> contents) {
		File file = new File(executionTestPath + "Executer.java");
		file.setExecutable(true);
		file.setReadable(true);
		file.setWritable(true);
		
		try {
			FileWriter fw = new FileWriter(file);
			
			for(int i = 0; i < contents.size(); i++) {
				fw.write(contents.get(i) + "\n");
			}
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> createExecuteExecutionTest(ArrayList<ExecutionClass> executionClassLists) {
		ArrayList<String> contents = new ArrayList<String>();
		contents.add("package executionTest;");
		contents.add("");
		
		for(int classNum = 0; classNum < executionClassLists.size(); classNum++) {
			ExecutionClass executionClass = executionClassLists.get(classNum);

			if(classNum == 0) {
				
				contents.add("import java.util.ArrayList;");
				contents.add("import java.io.File;");
				contents.add("import java.io.FileWriter;");
				contents.add("import java.io.IOException;");
				contents.add("");
				contents.add("public class Executer {");
				contents.add("");
				contents.add("  public static void main(String[] args) {");
				contents.add("");
				contents.add("      ArrayList<String> result;");
				contents.add("      File file = new File(\"" + executionTestPath + "result.txt\");");
				contents.add("      file.setExecutable(true);");
				contents.add("      file.setReadable(true);");
				contents.add("      file.setWritable(true);");
				contents.add("");
				contents.add("      try {");
				contents.add("          FileWriter fw = new FileWriter(file);");
			}
			
			contents.add("");
			String instanceName = "execute" + executionClass.getClassName();
			contents.add("          fw.write(\"" + executionClass.getClassName() + "_EXTest\" + \"" + "\\" + "n\");");
			contents.add("          " + executionClass.getClassName() + "_EXTest " + instanceName + " = new " + executionClass.getClassName() + "_EXTest();");
			for(int methodNum = 0; methodNum < executionClass.getExecutionPartLists().size(); methodNum++) {
				contents.add("          result = " + instanceName + ".test" + methodNum + "();");
				contents.add("          fw.write(result.toString() + \"\\n\");");
			}
			
		}
		
		contents.add("          fw.close();");
		contents.add("      } catch (IOException e) {");
		contents.add("          e.printStackTrace();");
		contents.add("      }");
		contents.add("  }");
		contents.add("}");
		
//		for(int i = 0; i < contents.size(); i++) {
//			System.out.println(contents.get(i));
//		}
		
		return contents;
	}
	
	private void exportExecutionTest(ArrayList<ExecutionClass> executionClassLists) {
		File dir = new File(executionTestPath);
		dir.mkdir();
		
		for(int classNum = 0; classNum < executionClassLists.size(); classNum++) {
			ExecutionClass executionClass = executionClassLists.get(classNum);
			String className = executionClass.getClassName() + "_EXTest";
			String fileName = executionTestPath + className + ".java";
			
			File file = new File(fileName);
			file.setExecutable(true);
			file.setReadable(true);
			file.setWritable(true);
			
			try {
				FileWriter fw = new FileWriter(file);
				ArrayList<String> contents = executionClass.getContents();
				for(int contentsNum = 0; contentsNum < contents.size(); contentsNum++) {
					fw.write(contents.get(contentsNum) + "\n");
				}
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void createExecutionTest(ArrayList<ExecutionClass> executionClassLists) {
		for(int classNum = 0; classNum < executionClassLists.size(); classNum++) {
			ExecutionClass executionClass = executionClassLists.get(classNum);
			ArrayList<String> contents = new ArrayList<String>();
			
			contents.add("package executionTest;");
			contents.add("");
			
			ArrayList<TestImport> importLists = executionClass.getImportLists();
			for(int importNum = 0; importNum < importLists.size(); importNum++) {
				if(!importLists.get(importNum).getImportClass().contains("org")) {
					contents.add(importLists.get(importNum).getStatement());
				}
			}
			contents.add("import java.util.ArrayList;");
			
			contents.add("");
			contents.add("public class " + executionClass.getClassName() + "_EXTest {");
			
			ArrayList<ExecutionPart> executionPartLists = executionClass.getExecutionPartLists();
			for(int partNum = 0; partNum < executionPartLists.size(); partNum++) {
				ExecutionPart executionPart = executionPartLists.get(partNum);
				
				contents.add("");
				contents.add("  public ArrayList<String> test" + partNum + "(){");
				contents.add("      ArrayList<String> results = new ArrayList<String>();");
				
				ArrayList<TestMethod> executionMethodLists = executionPart.getMethodLists();
				for(int methodNum = 0; methodNum < executionMethodLists.size(); methodNum++) {
					TestMethod executionMethod = executionMethodLists.get(methodNum);
					contents.add("      " + executionMethod.getStatement() + ";");
				}
				
				ArrayList<String> assertionTargetLists = executionPart.getAssertionTarget();
				for(int assertNum = 0; assertNum < assertionTargetLists.size(); assertNum++) {
					contents.add("      results.add(String.valueOf(" + assertionTargetLists.get(assertNum) + "));");
				}
				
				contents.add("      return results;");
				contents.add("  }");
			}
			
			contents.add("}");
			
			executionClass.setContents(contents);
			
//			for(int i = 0; i < contents.size(); i++) {
//				System.out.println(contents.get(i));
//			}
		}
	}
	
	private ArrayList<ExecutionClass> createExecutionClass(ArrayList<Result> resultLists) {
		ArrayList<ExecutionClass> executionClassLists = new ArrayList<ExecutionClass>();
		for(int resultNum = 0; resultNum < resultLists.size(); resultNum++) {
			Result result = resultLists.get(resultNum);
			ExecutionClass executionClass = new ExecutionClass(result.getEvoSuiteTestClass().getClassName().replace("_ESTest", ""));
			executionClassLists.add(executionClass);
			executionClass.setImportLists(result.getEvoSuiteTestClass().getImportLists());
			ArrayList<ExecutionPart> executionPartLists = new ArrayList<ExecutionPart>();
			executionClass.setExecutionPart(executionPartLists);
			
			// perfect Matching
			ArrayList<MatchingResult> matchingResultLists = result.getMatchingResultLists();
			for(int matchNum = 0; matchNum < matchingResultLists.size(); matchNum++) {
				MatchingResult matchingResult = matchingResultLists.get(matchNum);
				
				ArrayList<Test> testLists = matchingResult.getSameExecuteEvoSuitePath().getTestLists();
				Instance instance = matchingResult.getSameExecuteExtractPath().getInstanecLists().get(0);
				for(int testNum = 0; testNum < testLists.size(); testNum++) {
					Test test = testLists.get(testNum);
					ArrayList<TestMethod> methodLists = test.getMethodLists();
					
					ExecutionPart executionPart = new ExecutionPart(test);
					executionPartLists.add(executionPart);
					
					for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
						TestMethod method = methodLists.get(methodNum);
						ExtractMethod extractMethod = instance.getExtractMethodLists().get(methodNum);
						
						String statement = this.createStatement(method, extractMethod);
						String methodName = extractMethod.getMethodName();
						TestMethod executionMethod = new TestMethod(statement, methodName);
						executionPart.addMethodLists(executionMethod);
					}
					
					ArrayList<TestAssertion> assertionLists = test.getAssertionLists();
					for(int assertNum = 0; assertNum < assertionLists.size(); assertNum++) {
						TestAssertion testAssertion = assertionLists.get(assertNum);
						
						if(!testAssertion.getVariable().equals("")) {
							executionPart.addAssertionTarget(testAssertion.getVariable());
						}else {
							executionPart.addAssertionTarget(this.createAssertionTargetMethod(testAssertion));
						}
					}
				}
			}
		
			// partially matching
			ArrayList<PartiallyMatchingResult> partiallyMatchingResultLists = result.getPartiallyMatchingResultLists();
			for(int partNum = 0; partNum < partiallyMatchingResultLists.size(); partNum++) {
				PartiallyMatchingResult partiallyMatchingResult = partiallyMatchingResultLists.get(partNum);
				
				ArrayList<Test> testLists = partiallyMatchingResult.getSameExecuteEvoSuitePath().getTestLists();
				Instance instance = partiallyMatchingResult.getPartiallyMatchingExtractPathLists().get(0).getInstanecLists().get(0);
				
				for(int testNum = 0; testNum < testLists.size(); testNum++) {
					Test test = testLists.get(testNum);
					ArrayList<TestMethod> methodLists = test.getMethodLists();
					ArrayList<ExtractMethod> extractMethodLists = instance.getExtractMethodLists();
					
					for(int extractNum = 0; extractNum < extractMethodLists.size(); extractNum++) {
						ExtractMethod extractMethod = extractMethodLists.get(extractNum);
						ExecutePath executePath = this.createExecutePath(extractMethod.getTraceMethodBlock());
						extractMethod.setExecutePath(executePath);
					}
					
					for(int evoMethodNum = 0; evoMethodNum < methodLists.size(); evoMethodNum++) {
						TestMethod testMethod = methodLists.get(evoMethodNum);
						ExecutePath executePath = this.createExecutePath(testMethod.getTraceMethodBlock());
						testMethod.setExecutePath(executePath);
					}
					
					int evoSuiteTargetMethodNum = 0;
					ArrayList<MatchMethod> matchMethodLists = new ArrayList<MatchMethod>();
					for(int extractNum = 0; extractNum < extractMethodLists.size(); extractNum++) {
						ExtractMethod extractMethod = extractMethodLists.get(extractNum);
						TestMethod testMethod = methodLists.get(evoSuiteTargetMethodNum);
						
						ExecutePath extractPath = extractMethod.getExecutePath();
						ExecutePath evoSuitePath = testMethod.getExecutePath();
						
						if(this.isSameExecutePath(extractPath, evoSuitePath)) {
							MatchMethod matchMethod = new MatchMethod(testMethod, extractMethod);
							matchMethodLists.add(matchMethod);
							evoSuiteTargetMethodNum += 1;
							
							if(evoSuiteTargetMethodNum == methodLists.size()) {
								break;
							}
						}
						
					}
					
					ExecutionPart executionPart = new ExecutionPart(test);
					executionPartLists.add(executionPart);
					
					for(int matchNum = 0; matchNum < matchMethodLists.size(); matchNum++) {
						MatchMethod matchMethod = matchMethodLists.get(matchNum);
						TestMethod testMethod = matchMethod.getTestMethod();
						ExtractMethod extractMethod = matchMethod.getExtractMethod();
						
						String statement = this.createStatement(testMethod, extractMethod);
						String methodName = extractMethod.getMethodName();
						TestMethod executionMethod = new TestMethod(statement, methodName);
						executionPart.addMethodLists(executionMethod);
					}
					
					ArrayList<TestAssertion> assertionLists = test.getAssertionLists();
					for(int assertNum = 0; assertNum < assertionLists.size(); assertNum++) {
						TestAssertion testAssertion = assertionLists.get(assertNum);
						
						if(!testAssertion.getVariable().equals("")) {
							executionPart.addAssertionTarget(testAssertion.getVariable());
						}else {
							executionPart.addAssertionTarget(this.createAssertionTargetMethod(testAssertion));
						}
					}
				}
				
			}
		}
		
		return executionClassLists;
	}
	
	private ExecutePath createExecutePath(TraceMethodBlock traceMethodBlock) {
		ArrayList<Trace> traceLists = traceMethodBlock.getTraceLists();
		
		ArrayList<Integer> lineLists = new ArrayList<Integer>();
		
		for(int traceNum = 0; traceNum < traceLists.size(); traceNum++) {
			Trace trace = traceLists.get(traceNum);
			
			if(lineLists.size() == 0) {
				lineLists.add(trace.getLine());
			}else {
				if(!lineLists.contains(trace.getLine())) {
					lineLists.add(trace.getLine());
				}
			}
		}
		
		ExecutePath executePath = new ExecutePath(lineLists, traceLists.get(0).getMname(), traceLists.get(0).getFilename());
		return executePath;
	}
	
	private boolean isSameExecutePath(ExecutePath path1, ExecutePath path2) {
		if(!path1.getMname().equals(path2.getMname())) {
			return false;
		}
		
		if(!path1.getFileName().equals(path2.getFileName())) {
			return false;
		}
		
		ArrayList<Integer> lineLists1 = path1.getLineLists();
		ArrayList<Integer> lineLists2 = path2.getLineLists();
		
		if(lineLists1.size() != lineLists2.size()) {
			return false;
		}
		
		for(int lineNum = 0; lineNum < lineLists1.size(); lineNum++) {
			if(lineLists1.get(lineNum) != lineLists2.get(lineNum)) {
				return false;
			}
		}
		
		return true;
	}
	
	private String createAssertionTargetMethod(TestAssertion testAssertion) {
		String targetAssertion = "";
		targetAssertion += testAssertion.getGetterMethodInstance() + "." + testAssertion.getGetterMethodName() + "(";
		
		if(testAssertion.getGetterMethodArgument().size() == 0) {
			targetAssertion += ")";
		}else {
			for(int argNum = 0; argNum < testAssertion.getGetterMethodArgument().size(); argNum++) {
				targetAssertion += testAssertion.getGetterMethodArgument().get(argNum);
				
				if(argNum == testAssertion.getGetterMethodArgument().size() - 1) {
					targetAssertion += ")";
				}else {
					targetAssertion += ", ";
				}
			}
			
		}
		
		return targetAssertion;
	}
	
	private String createStatement(TestMethod method, ExtractMethod extractMethod) {
		String statement = "";
		
		if(!method.getReturnType().equals("")) {
			statement += method.getReturnType() + " " + method.getReturnVariable() + " = ";
		}
		
		if(!method.getInstance().equals("")) {
			statement += method.getInstance() + "." + method.getMethodName() + "(";
		}else {
			statement += "new " + method.getConstructorClass() + "(";
		}
		
		if(extractMethod.getArgmentLists().size() == 0) {
			statement += ");";
		}else {
			for(int argNum = 0; argNum < extractMethod.getArgmentLists().size(); argNum++) {
				statement += extractMethod.getArgmentLists().get(argNum).getValue();
				
				if(argNum == extractMethod.getArgmentLists().size() - 1) {
					statement += ");";
				}else {
					statement += ", ";
				}
			}
		}
		
		return statement;
	}
	
}

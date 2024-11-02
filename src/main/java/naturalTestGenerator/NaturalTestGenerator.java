package naturalTestGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import analyzer.Analyzer;
import analyzer.AnalyzerMethod;

import executionPartGenerator.ExecutionClass;
import executionPartGenerator.ExecutionPart;
import paramaterExtracter.ExtractClass;
import paramaterExtracter.ExtractMethod;
import paramaterExtracter.Instance;
import testAnalyzer.Test;
import testAnalyzer.TestAssertion;
import testAnalyzer.TestImport;
import testAnalyzer.TestMethod;
import testMatcher.MatchingResult;
import testMatcher.PartiallyMatchingResult;
import testMatcher.Result;
import testMatcher.SameExecuteExtractPath;
import testMatcher.SameExecutePath;
import tracer.ValueOption;

public class NaturalTestGenerator {

	private String executionTestPath = "src/main/java/executionTest/";
	private String exportPath = "src/test/java/mapEvoSuiteTest/";
	
	public void run(ArrayList<ExecutionClass> executionClassLists, ArrayList<Result> resultLists, Analyzer analyzer, ArrayList<ExtractClass> extractClassLists) {
		ArrayList<ExecutionResult> executionResultLists = this.getResult();
		this.setAssertionValue(executionResultLists, executionClassLists);
		this.createNaturalTest(executionClassLists, resultLists, analyzer);
		this.exportNaturalTest(executionClassLists);
		this.createEx01(extractClassLists);
		this.createEx02(resultLists);
		this.createEx03(resultLists);
	}
	
	private void createEx03(ArrayList<Result> resultLists) {
		String path = exportPath + "ex03.txt";
		
		File file = new File(path);
		file.setReadable(true);
		file.setWritable(true);
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			
			for(int resultNum = 0; resultNum < resultLists.size(); resultNum++) {
				Result result = resultLists.get(resultNum);
				
				String className = result.getExtractClass().getOwnerClass();
				
				ArrayList<MatchingResult> matchingResultLists = result.getMatchingResultLists();
				for(int matchNum = 0; matchNum < matchingResultLists.size(); matchNum++) {
					MatchingResult matchingResult = matchingResultLists.get(matchNum);
					ArrayList<Test> evoSuiteTestLists = matchingResult.getSameExecuteEvoSuitePath().getTestLists();
					
					for(int testNum = 0; testNum < evoSuiteTestLists.size(); testNum++) {
						Test test = evoSuiteTestLists.get(testNum);
						ArrayList<TestMethod> methodLists = test.getMethodLists();
						
						for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
							TestMethod method = methodLists.get(methodNum);
							String statement = "EvoSuite" + "\t" + test.getMethodName() + "\t" + className + "\t" + method.getMethodName();
							ArrayList<String> argumentLists = method.getArgumentLists();
							
							for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
								statement += "\t" + argumentLists.get(argNum);
							}
							
							fw.write(statement + "\n");
						}
					}
					
					ArrayList<Instance> instanceLists = matchingResult.getSameExecuteExtractPath().getInstanecLists();
					for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
						Instance instance = instanceLists.get(instanceNum);
						
						fw.write("Extract" + "\t" + instance.getId() + "\t" + className + "\n");
					}
				}
				
				ArrayList<PartiallyMatchingResult> partiallyMatchingLists = result.getPartiallyMatchingResultLists();
				for(int partNum = 0; partNum < partiallyMatchingLists.size(); partNum++) {
					PartiallyMatchingResult partiallyMatchingResult = partiallyMatchingLists.get(partNum);
					ArrayList<Test> testLists = partiallyMatchingResult.getSameExecuteEvoSuitePath().getTestLists();
					
					for(int testNum = 0; testNum < testLists.size(); testNum++) {
						Test test = testLists.get(testNum);
						ArrayList<TestMethod> methodLists = test.getMethodLists();
						
						for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
							TestMethod method = methodLists.get(methodNum);
							String statement = "EvoSuite" + "\t" + test.getMethodName() + "\t" + className + "\t" + method.getMethodName();
							ArrayList<String> argumentLists = method.getArgumentLists();
							
							for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
								statement += "\t" + argumentLists.get(argNum);
							}
							
							fw.write(statement + "\n");
						}
					}
					
					ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists = partiallyMatchingResult.getPartiallyMatchingExtractPathLists();
					
					for(int executeNum = 0; executeNum < sameExecuteExtractPathLists.size(); executeNum++) {
						ArrayList<Instance> instanceLists = sameExecuteExtractPathLists.get(executeNum).getInstanecLists();
						for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
							Instance instance = instanceLists.get(instanceNum);
							
							fw.write("Extract" + "\t" + instance.getId() + "\t" + className + "\n");
						}
					}
				}
				
			}
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createEx02(ArrayList<Result> resultLists) {
		String path = exportPath + "ex02.txt";
		
		File file = new File(path);
		file.setReadable(true);
		file.setWritable(true);
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			
			for(int resultNum = 0; resultNum < resultLists.size(); resultNum++) {
				Result result = resultLists.get(resultNum);
				
				int evoSuiteNum = result.getEvoSuiteTestClass().getTestLists().size();
				int instanceNum = result.getExtractClass().getInstanceLists().size();
				
//				int matchingEvoSuiteNum = 0;
//				int matchingExtractNum = 0;
//				ArrayList<MatchingResult> matchingResultLists = result.getMatchingResultLists();
//				for(int matchNum = 0; matchNum < matchingResultLists.size(); matchNum++) {
//					MatchingResult matchingResult = matchingResultLists.get(matchNum);
//					matchingEvoSuiteNum += matchingResult.getSameExecuteEvoSuitePath().getTestLists().size();
//					matchingExtractNum += matchingResult.getSameExecuteExtractPath().getInstanecLists().size();
//				}
//				
//				int partiallyMatchingEvoSuiteNum = 0;
//				int partiallyMatchingExtractNum = 0;
//				ArrayList<PartiallyMatchingResult> partiallyMatchingResultLists = result.getPartiallyMatchingResultLists();
//				for(int partMatchNum = 0; partMatchNum < partiallyMatchingResultLists.size(); partMatchNum++) {
//					PartiallyMatchingResult partiallyMatchingResult = partiallyMatchingResultLists.get(partMatchNum);
//					partiallyMatchingEvoSuiteNum += partiallyMatchingResult.getSameExecuteEvoSuitePath().getTestLists().size();
//					ArrayList<SameExecuteExtractPath> sameExecuteExtractLists = partiallyMatchingResult.getPartiallyMatchingExtractPathLists();
//					for(int sameExtractNum = 0; sameExtractNum < sameExecuteExtractLists.size(); sameExtractNum++) {
//						partiallyMatchingExtractNum += sameExecuteExtractLists.get(sameExtractNum).getInstanecLists().size();
//					}
//				}
				
				int notMatchingEvoSuiteNum = 0;
				ArrayList<SameExecutePath> notMatchingEvoSuiteLists = result.getNotMatchingEvoSuiteLists();
				for(int sameEvoNum = 0; sameEvoNum < notMatchingEvoSuiteLists.size(); sameEvoNum++) {
					notMatchingEvoSuiteNum += notMatchingEvoSuiteLists.get(sameEvoNum).getTestLists().size();
				}
				
				int notMatchingInstanceNum = 0;
				ArrayList<SameExecuteExtractPath> notMatchingSameExecuteExtractPaths = result.getNotMatchingExtractLists();
				for(int sameInstNum = 0; sameInstNum < notMatchingSameExecuteExtractPaths.size(); sameInstNum++) {
					notMatchingInstanceNum += notMatchingSameExecuteExtractPaths.get(sameInstNum).getInstanecLists().size();
				}
				
				String statement = result.getEvoSuiteTestClass().getClassName().replace("_ESTest", "") + "\t\t" + 
						evoSuiteNum + "\t" + (evoSuiteNum - notMatchingEvoSuiteNum) + "\t" + notMatchingEvoSuiteNum + "\t" +
						instanceNum + "\t" + (instanceNum - notMatchingInstanceNum) + "\t" + notMatchingInstanceNum;
				fw.write(statement + "\n");
			}
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createEx01(ArrayList<ExtractClass> extractClassLists) {
		String path = exportPath + "ex01.txt";
		
		File file = new File(path);
		file.setReadable(true);
		file.setWritable(true);
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			
			for(int extractNum = 0; extractNum < extractClassLists.size(); extractNum++) {
				ArrayList<Instance> instanceLists = extractClassLists.get(extractNum).getInstanceLists();
				for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
					Instance instance = instanceLists.get(instanceNum);
					ArrayList<ExtractMethod> methodLists = instance.getExtractMethodLists();
					
					for(int methodNum = 0; methodNum < methodLists.size(); methodNum++) {
						ExtractMethod extractMethod = methodLists.get(methodNum);
						
						ArrayList<ValueOption> argumentLists = extractMethod.getArgmentLists();
						if(argumentLists.size() != 0) {
							String statement = instance.getId() + "\t" + instance.getOwnerClass() + "\t" + extractMethod.getMethodName();
							
							for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
								statement += "\t" + argumentLists.get(argNum).getValue();
							}
							
							fw.write(statement + "\n");
						}
					}
				}
			}

			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void exportNaturalTest(ArrayList<ExecutionClass> executionClassLists) {
		File dir = new File(exportPath);
		dir.mkdir();
		
		for(int classNum = 0; classNum < executionClassLists.size(); classNum++) {
			ExecutionClass executionClass = executionClassLists.get(classNum);
			String className = executionClass.getClassName() + "_MapTest";
			String fileName = exportPath + className + ".java";
			
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
	
	private void createNaturalTest(ArrayList<ExecutionClass> executionClassLists, ArrayList<Result> resultLists, Analyzer analyzer) {
		for(int classNum = 0; classNum < executionClassLists.size(); classNum++) {
			ExecutionClass executionClass = executionClassLists.get(classNum);
			
			Result result = null;
			for(int resultNum = 0; resultNum < resultLists.size(); resultNum++) {
				Result targetResult = resultLists.get(resultNum);
				if(executionClass.getClassName().equals(targetResult.getExtractClass().getOwnerClass())) {
					result = targetResult;
				}
			}
			
			ArrayList<String> contents = new ArrayList<String>();
			
			contents.add("package mapEvoSuiteTest;");
			contents.add("");
			
			ArrayList<TestImport> importLists = executionClass.getImportLists();
			for(int importNum = 0; importNum < importLists.size(); importNum++) {
				contents.add(importLists.get(importNum).getStatement());
			}
			contents.add("import java.util.ArrayList;");
			
			contents.add("");
			contents.add("public class " + executionClass.getClassName() + "_MapTest {");
			
			ArrayList<ExecutionPart> executionPartLists = executionClass.getExecutionPartLists();
			for(int partNum = 0; partNum < executionPartLists.size(); partNum++) {
				ExecutionPart executionPart = executionPartLists.get(partNum);
				
				contents.add("");
				contents.add("  @Test");
				contents.add("  public void mapTest" + partNum + "(){");
				
				ArrayList<TestMethod> executionMethodLists = executionPart.getMethodLists();
				for(int methodNum = 0; methodNum < executionMethodLists.size(); methodNum++) {
					TestMethod executionMethod = executionMethodLists.get(methodNum);
					contents.add("      " + executionMethod.getStatement());
				}
				
				ArrayList<String> assertionTargetLists = executionPart.getAssertionTarget();
				ArrayList<String> assertionValueLists = executionPart.getAssertionValueLists();
				Test test = executionPart.getTest();
				ArrayList<TestAssertion> assertionLists = test.getAssertionLists();
				for(int assertNum = 0; assertNum < assertionTargetLists.size(); assertNum++) {
					TestAssertion testAssertion = assertionLists.get(assertNum);
					if(!testAssertion.getVariable().equals("")) {
						TestMethod targetMethod = testAssertion.getAssertionTargetMethod();
						if(targetMethod.getReturnType().equals("String")) {
							contents.add("      assertEquals(\"" + assertionValueLists.get(assertNum) + "\", " + assertionTargetLists.get(assertNum) + ");");
						}else {
							contents.add("      assertEquals(" + assertionValueLists.get(assertNum) + ", " + assertionTargetLists.get(assertNum) + ");");
						}
					}else {
						String getterMethodName = testAssertion.getGetterMethodName();
						ArrayList<AnalyzerMethod> analyzerMethodLists = analyzer.getMethodLists();
						AnalyzerMethod analyzerMethod = null;
						for(int analyzeNum = 0; analyzeNum < analyzerMethodLists.size(); analyzeNum++) {
							AnalyzerMethod targetMethod = analyzerMethodLists.get(analyzeNum);
							if(targetMethod.getOwnerClass().getName().equals(executionClass.getClassName()) && targetMethod.getName().equals(getterMethodName)) {
								analyzerMethod = targetMethod;
								break;
							}
						}
						
						if(analyzerMethod.getReturnValueType().equals("String")) {
							contents.add("      assertEquals(\"" + assertionValueLists.get(assertNum) + "\", " + assertionTargetLists.get(assertNum) + ");");
						}else {
							contents.add("      assertEquals(" + assertionValueLists.get(assertNum) + ", " + assertionTargetLists.get(assertNum) + ");");
						}
					}
				}
				
				contents.add("  }");
			}
			
			contents.add("");
			contents.add("  // not match evosuite test");
			ArrayList<SameExecutePath> notMatchEvoSuitePathLists = result.getNotMatchingEvoSuiteLists();
			for(int pathNum = 0; pathNum < notMatchEvoSuitePathLists.size(); pathNum++) {
				ArrayList<Test> testLists = notMatchEvoSuitePathLists.get(pathNum).getTestLists();
				
				for(int testNum = 0; testNum < testLists.size(); testNum++) {
					Test test = testLists.get(testNum);
					ArrayList<String> statementLists = test.getContents();
					for(int stateNum = 0; stateNum < statementLists.size(); stateNum++) {
						contents.add(statementLists.get(stateNum));
					}
					contents.add("");
				}
			}
			
			contents.add("}");
			
			executionClass.setContents(contents);
			
//			for(int i = 0; i < contents.size(); i++) {
//				System.out.println(contents.get(i));
//			}
		}
	}
	
	private void setAssertionValue(ArrayList<ExecutionResult> executionResultLists, ArrayList<ExecutionClass> executionClassLists) {
		for(int resultNum = 0; resultNum < executionResultLists.size(); resultNum++) {
			ExecutionResult executionResult = executionResultLists.get(resultNum);
			
			ExecutionClass executionClass = null;
			for(int classNum = 0; classNum < executionClassLists.size(); classNum++) {
				ExecutionClass targetClass = executionClassLists.get(classNum);
				
				if(targetClass.getClassName().equals(executionResult.getClassName().replace("_EXTest", ""))) {
					executionClass = targetClass;
					break;
				}
			}
			
			for(int partNum = 0; partNum < executionResult.getResults().size(); partNum++) {
				String assertValueListToString = executionResult.getResults().get(partNum);
				assertValueListToString = assertValueListToString.substring(1, assertValueListToString.length() - 1);
				String split[] = assertValueListToString.split(", ");
				
				ExecutionPart executionPart = executionClass.getExecutionPartLists().get(partNum);
				for(int splitNum = 0; splitNum < split.length; splitNum++) {
					executionPart.addAssertionValueLists(split[splitNum]);
				}
			}
		}
	}
	
	private ArrayList<ExecutionResult> getResult() {
		File file = new File(executionTestPath + "result.txt");
		ArrayList<ExecutionResult> executionResultLists = new ArrayList<ExecutionResult>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String readLine = "";
			ExecutionResult executionResult = null;
			
			while((readLine = br.readLine()) != null) {
				if(readLine.contains("_EXTest")) {
					executionResult = new ExecutionResult(readLine);
					executionResultLists.add(executionResult);
				}else {
					executionResult.addResults(readLine);
				}
			}
			
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return executionResultLists;
	}
}
